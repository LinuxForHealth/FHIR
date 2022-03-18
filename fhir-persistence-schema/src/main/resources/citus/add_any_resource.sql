-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020, 2022
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource version and its associated parameters. These
-- parameters only ever point to the latest version of a resource, never to
-- previous versions, which are kept to support history queries.
-- implNote - Conventions:
--           p_... prefix used to represent input parameters
--           v_... prefix used to represent declared variables
--           t_... prefix used to represent temp variables
--           o_... prefix used to represent output parameters
-- Parameters:
--   p_logical_id: the logical id given to the resource by the FHIR server
--   p_payload:    the BLOB (of JSON) which is the resource content
--   p_last_updated the last_updated time given by the FHIR server
--   p_is_deleted: the soft delete flag
--   p_version_id: the intended new version id of the resource (matching the JSON payload)
--   p_parameter_hash_b64 the Base64 encoded hash of parameter values
--   p_if_none_match the encoded If-None-Match value
--   o_logical_resource_id: output field returning the newly assigned logical_resource_id value
--   o_current_parameter_hash: Base64 current parameter hash if existing resource
--   o_interaction_status: output indicating whether a change was made or IfNoneMatch hit
--   o_if_none_match_version: output revealing the version found when o_interaction_status is 1 (IfNoneMatch)
-- Exceptions:
--   SQLSTATE 99001: on version conflict (concurrency)
--   SQLSTATE 99002: missing expected row (data integrity)
--   SQLSTATE 99004: delete a currently deleted resource (data integrity)
-- ----------------------------------------------------------------------------
    ( IN p_resource_type                 VARCHAR( 36),
      IN p_logical_id                    VARCHAR(255), 
      IN p_payload                         BYTEA,
      IN p_last_updated                TIMESTAMP,
      IN p_is_deleted                       CHAR(  1),
      IN p_source_key                    VARCHAR( 64),
      IN p_version                           INT,
      IN p_parameter_hash_b64            VARCHAR( 44),
      IN p_if_none_match                     INT,
      IN p_resource_payload_key          VARCHAR( 36),
      OUT o_logical_resource_id           BIGINT,
      OUT o_current_parameter_hash       VARCHAR( 44),
      OUT o_interaction_status               INT,
      OUT o_if_none_match_version            INT)
    LANGUAGE plpgsql
     AS $$

  DECLARE 
  v_schema_name         VARCHAR(128);
  v_logical_resource_id  BIGINT := NULL;
  t_logical_resource_id  BIGINT := NULL;
  v_current_resource_id  BIGINT := NULL;
  v_resource_id          BIGINT := NULL;
  v_resource_type_id        INT := NULL;
  v_currently_deleted      CHAR(1) := NULL;
  v_new_resource            INT := 0;
  v_duplicate               INT := 0;
  v_current_version         INT := 0;
  v_change_type            CHAR(1) := NULL;
  
  -- Because we don't really update any existing key, so use NO KEY UPDATE to achieve better concurrence performance. 
  lock_cur CURSOR (t_resource_type_id INT, t_logical_id VARCHAR(255)) FOR SELECT logical_resource_id FROM {{SCHEMA_NAME}}.logical_resource_shards WHERE resource_type_id = t_resource_type_id AND logical_id = t_logical_id FOR NO KEY UPDATE;

  BEGIN
  -- default value unless we hit If-None-Match
  o_interaction_status := 0;

  -- LOADED ON: {{DATE}}
  v_schema_name := '{{SCHEMA_NAME}}';
  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types WHERE resource_type = p_resource_type;

  -- Grab the new resource_id so that we can use it right away (and skip an update to xx_logical_resources later)
  SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_sequence') INTO v_resource_id;

  -- Get a lock using the sharded logical_id in logical_resource_shards
  OPEN lock_cur(t_resource_type_id := v_resource_type_id, t_logical_id := p_logical_id);
  FETCH lock_cur INTO v_logical_resource_id;
  CLOSE lock_cur;
  
  -- Create the resource if we don't have it already
  IF v_logical_resource_id IS NULL
  THEN
    SELECT nextval('{{SCHEMA_NAME}}.fhir_sequence') INTO v_logical_resource_id;
    -- logical_resource_shards provides a sharded lookup mechanism for obtaining
    -- the logical_resource_id when you know resource_type_id and logical_id
    INSERT INTO {{SCHEMA_NAME}}.logical_resource_shards (resource_type_id, logical_id, logical_resource_id)
         VALUES (v_resource_type_id, p_logical_id, v_logical_resource_id) ON CONFLICT DO NOTHING;

    -- The above insert could fail silently in a concurrent insert scenario, so we now just
    -- need to try and obtain the lock (even though we may already have it if the above insert
    -- succeeded. Whatever the case, we know there's a row there.
    OPEN lock_cur (t_resource_type_id := v_resource_type_id, t_logical_id := p_logical_id);
    FETCH lock_cur INTO t_logical_resource_id;
    CLOSE lock_cur;

    -- check to see if it was us who actually created the record
    IF v_logical_resource_id = t_logical_resource_id
    THEN
      v_new_resource := 1;
    ELSE
      -- resource was created by another thread, so use that id instead
      v_logical_resource_id := t_logical_resource_id;
    END IF;
  END IF;

  -- add_logical_resource has 13 IN parameters followed by 3 OUT parameters
  EXECUTE 'SELECT * FROM {{SCHEMA_NAME}}.add_logical_resource($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13)'
     INTO o_current_parameter_hash,
          o_interaction_status,
          o_if_none_match_version
    USING v_logical_resource_id, v_new_resource, 
          p_resource_type, v_resource_type_id, p_logical_id, 
          p_payload, p_last_updated, p_is_deleted,
          p_source_key, p_version, p_parameter_hash_b64,
          p_if_none_match, p_resource_payload_key;

END $$;