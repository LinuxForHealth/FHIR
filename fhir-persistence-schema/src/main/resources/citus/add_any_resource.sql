-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020, 2022
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource version and its associated parameters. These
-- parameters only ever point to the latest version of a resource, never to
-- previous versions, which are kept to support history queries.
-- From V0027, we now use a logical_resource_ident table for locking. Records
-- can be created in this table either by this procedure, or as part of
-- reference parameter processing.
--
-- This variant is for use with the Citus distributed variant of the schema.
-- This function is distributed by logical_resource_id (the first parameter)
-- because all SQL/DML it executes uses logical_resource_id. The
-- logical_resource_ident record must already exist and be locked for update
-- before this function is called.
--
-- Because this function is distributed all object names must be fully
-- qualified.
--
-- implNote - Conventions:
--           p_... prefix used to represent input parameters
--           v_... prefix used to represent declared variables
--           t_... prefix used to represent temp variables
--           o_... prefix used to represent output parameters
-- Parameters:
--   p_logical_resource_id: the logical_resource_ident primary key value for the resource
--   p_resource_type_id: the resource_type_id from resource_types
--   p_resource_type: the resource type name
--   p_logical_id: the logical id given to the resource by the FHIR server
--   p_payload:    the BLOB (of JSON) which is the resource content
--   p_last_updated the last_updated time given by the FHIR server
--   p_is_deleted: the soft delete flag
--   p_version_id: the intended new version id of the resource (matching the JSON payload)
--   p_parameter_hash_b64 the Base64 encoded hash of parameter values
--   p_if_none_match the encoded If-None-Match value
--   o_current_parameter_hash: Base64 current parameter hash if existing resource
--   o_interaction_status: output indicating whether a change was made or IfNoneMatch hit
--   o_if_none_match_version: output revealing the version found when o_interaction_status is 1 (IfNoneMatch)
-- Exceptions:
--   SQLSTATE 99001: on version conflict (concurrency)
--   SQLSTATE 99002: missing expected row (data integrity)
--   SQLSTATE 99004: delete a currently deleted resource (data integrity)
-- ----------------------------------------------------------------------------
    ( IN p_logical_resource_id            BIGINT,
      IN p_resource_type_id                  INT,
      IN p_resource_type                 VARCHAR( 36),
      IN p_logical_id                    VARCHAR(255), 
      IN p_payload                         BYTEA,
      IN p_last_updated                TIMESTAMP,
      IN p_is_deleted                       CHAR(  1),
      IN p_source_key                    VARCHAR( 64),
      IN p_version                           INT,
      IN p_parameter_hash_b64            VARCHAR( 44),
      IN p_if_none_match                     INT,
      IN p_resource_payload_key          VARCHAR( 36),
      OUT o_current_parameter_hash       VARCHAR( 44),
      OUT o_interaction_status               INT,
      OUT o_if_none_match_version            INT)
    LANGUAGE plpgsql
     AS $$

  DECLARE 
  v_schema_name         VARCHAR(128);
  t_logical_resource_id  BIGINT := NULL;
  v_current_resource_id  BIGINT := NULL;
  v_resource_id          BIGINT := NULL;
  v_currently_deleted      CHAR(1) := NULL;
  v_new_resource            INT := 0;
  v_duplicate               INT := 0;
  v_current_version         INT := 0;
  v_ghost_resource          INT := 0;
  v_change_type            CHAR(1) := NULL;

BEGIN
  -- default value unless we hit If-None-Match
  o_interaction_status := 0;

  -- LOADED ON: {{DATE}}
  v_schema_name := '{{SCHEMA_NAME}}';

  -- Grab the new resource_id so that we can use it right away (and skip an update to xx_logical_resources later)
  SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_sequence') INTO v_resource_id;

  -- Read the record from logical_resources to see if this is an existing resource
  SELECT logical_resource_id, parameter_hash, is_deleted 
    INTO t_logical_resource_id, o_current_parameter_hash, v_currently_deleted
    FROM {{SCHEMA_NAME}}.logical_resources 
   WHERE logical_resource_id = p_logical_resource_id;
  IF (t_logical_resource_id IS NULL)
  THEN
     v_new_resource := 1;
    -- we already own the lock on the ident record, so we can safely create
    -- the corresponding records in the logical_resources and resource-type-specific 
    -- xx_logical_resources tables
    INSERT INTO {{SCHEMA_NAME}}.logical_resources (logical_resource_id, resource_type_id, logical_id, reindex_tstamp, is_deleted, last_updated, parameter_hash)
         VALUES (p_logical_resource_id, p_resource_type_id, p_logical_id, '1970-01-01', p_is_deleted, p_last_updated, p_parameter_hash_b64) ON CONFLICT DO NOTHING;

    EXECUTE 'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_logical_resources (logical_resource_id, logical_id, is_deleted, last_updated, version_id, current_resource_id) '
      || '     VALUES ($1, $2, $3, $4, $5, $6)' USING p_logical_resource_id, p_logical_id, p_is_deleted, p_last_updated, p_version, v_resource_id;

    -- Since the resource did not previously exist, make sure o_current_parameter_hash is null
    o_current_parameter_hash := NULL;
  ELSE
    -- as this is an existing resource, we need to know the current resource id.
    -- This is only available at the resource-specific logical_resources level
    EXECUTE
         'SELECT current_resource_id, version_id FROM ' || v_schema_name || '.' || p_resource_type || '_logical_resources '
      || ' WHERE logical_resource_id = $1 '
    INTO v_current_resource_id, v_current_version USING p_logical_resource_id;
    
    IF v_current_resource_id IS NULL OR v_current_version IS NULL
    THEN
        -- our concurrency protection means that this shouldn't happen
        RAISE 'Schema data corruption - missing logical resource' USING ERRCODE = '99002';
    END IF;

    -- If-None-Match does not apply if the resource is currently deleted
    IF v_currently_deleted = 'N' AND p_if_none_match = 0
    THEN
        -- If-None-Match hit. Raising an exception here causes PostgreSQL to mark the
        -- connection with a fatal error, so instead we use an out parameter to
        -- indicate the match
        o_interaction_status := 1;
        o_if_none_match_version := v_current_version;
        RETURN;
    END IF;

    -- Concurrency check:
    --   the version parameter we've been given (which is also embedded in the JSON payload) must be 
    --   one greater than the current version, otherwise we've hit a concurrent update race condition
    IF p_version != v_current_version + 1
    THEN
      RAISE 'Concurrent update - mismatch of version in JSON' USING ERRCODE = '99001';
    END IF;

    -- Prevent creating a new deletion marker if the resource is currently deleted
    IF v_currently_deleted = 'Y' AND p_is_deleted = 'Y'
    THEN
      RAISE 'Unexpected attempt to delete a Resource which is currently deleted' USING ERRCODE = '99004';
    END IF;

    IF o_current_parameter_hash IS NULL OR p_parameter_hash_b64 != o_current_parameter_hash
    THEN
        -- existing resource, so need to delete all its parameters (select because it's a function, not a procedure)
        -- TODO patch parameter sets instead of all delete/all insert.
        EXECUTE 'SELECT {{SCHEMA_NAME}}.delete_resource_parameters($1, $2)'
        USING p_resource_type, p_logical_resource_id;
    END IF; -- end if check parameter hash
  END IF; -- end if existing resource

  -- create the new resource version entry in xx_resources
  EXECUTE
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted, resource_payload_key) '
      || ' VALUES ($1, $2, $3, $4, $5, $6, $7)'
    USING v_resource_id, p_logical_resource_id, p_version, p_payload, p_last_updated, p_is_deleted, p_resource_payload_key;

  IF v_new_resource = 0 THEN
    -- As this is an existing logical resource, we need to update the xx_logical_resource values to match
    -- the values of the current resource. For new resources, these are added by the insert so we don't
    -- need to update them here.
    EXECUTE 'UPDATE ' || v_schema_name || '.' || p_resource_type || '_logical_resources SET current_resource_id = $1, is_deleted = $2, last_updated = $3, version_id = $4 WHERE logical_resource_id = $5'
      USING v_resource_id, p_is_deleted, p_last_updated, p_version, p_logical_resource_id;

    -- For V0014 we now also store is_deleted and last_updated values at the whole-system logical_resources level
    EXECUTE 'UPDATE ' || v_schema_name || '.logical_resources SET is_deleted = $1, last_updated = $2, parameter_hash = $3 WHERE logical_resource_id = $4'
      USING p_is_deleted, p_last_updated, p_parameter_hash_b64, p_logical_resource_id;
  END IF;

  -- Finally, write a record to RESOURCE_CHANGE_LOG which records each event
  -- related to resources changes (issue-1955)
  IF p_is_deleted = 'Y'
  THEN
    v_change_type := 'D';
  ELSE 
    IF v_new_resource = 0 AND v_currently_deleted = 'N'
    THEN
      v_change_type := 'U';
    ELSE
      v_change_type := 'C';
    END IF;
  END IF;

  INSERT INTO {{SCHEMA_NAME}}.resource_change_log(resource_id, change_tstamp, resource_type_id, logical_resource_id, version_id, change_type)
       VALUES (v_resource_id, p_last_updated, p_resource_type_id, p_logical_resource_id, p_version, v_change_type);
  
END $$;
