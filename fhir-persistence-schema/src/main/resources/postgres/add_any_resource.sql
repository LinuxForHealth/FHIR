-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020, 2021
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
--   o_resource_id: output field returning the newly assigned resource_id value
-- Exceptions:
--   SQLSTATE 99001: on version conflict (concurrency)
--   SQLSTATE 99002: missing expected row (data integrity)
-- ----------------------------------------------------------------------------
    ( IN p_resource_type                 VARCHAR( 36),
      IN p_logical_id                    VARCHAR(255), 
      IN p_payload                          BYTEA,
      IN p_last_updated                TIMESTAMP,
      IN p_is_deleted                       CHAR(  1),
      IN p_source_key                    VARCHAR( 64),
      IN p_version                           INT,
      OUT o_logical_resource_id            BIGINT)
    LANGUAGE plpgsql
     AS $$

  DECLARE 
  v_schema_name         VARCHAR(128);
  v_logical_resource_id  BIGINT := NULL;
  t_logical_resource_id  BIGINT := NULL;
  v_current_resource_id  BIGINT := NULL;
  v_resource_id          BIGINT := NULL;
  v_resource_type_id        INT := NULL;
  v_new_resource            INT := 0;
  v_duplicate               INT := 0;
  v_current_version         INT := 0;
  v_change_type            CHAR(1) := NULL;

  -- Because we don't really update any existing key, so use NO KEY UPDATE to achieve better concurrence performance.
  lock_cur CURSOR (t_resource_type_id INT, t_logical_id VARCHAR(255)) FOR SELECT logical_resource_id FROM {{SCHEMA_NAME}}.logical_resources WHERE resource_type_id = t_resource_type_id AND logical_id = t_logical_id FOR NO KEY UPDATE;

BEGIN
  -- LOADED ON: {{DATE}}
  v_schema_name := '{{SCHEMA_NAME}}';
  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types WHERE resource_type = p_resource_type;

  -- Grab the new resource_id so that we can use it right away (and skip an update to xx_logical_resources later)
  SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_sequence') INTO v_resource_id;

  -- Get a lock at the system-wide logical resource level
  OPEN lock_cur(t_resource_type_id := v_resource_type_id, t_logical_id := p_logical_id);
  FETCH lock_cur INTO v_logical_resource_id;
  CLOSE lock_cur;
  
  -- Create the resource if we don't have it already
  IF v_logical_resource_id IS NULL
  THEN
    SELECT nextval('{{SCHEMA_NAME}}.fhir_sequence') INTO v_logical_resource_id;
    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the logical resource. This
    -- is easy to handle, just turn around and read it
    INSERT INTO {{SCHEMA_NAME}}.logical_resources (logical_resource_id, resource_type_id, logical_id, reindex_tstamp, is_deleted, last_updated)
         VALUES (v_logical_resource_id, v_resource_type_id, p_logical_id, '1970-01-01', p_is_deleted, p_last_updated) ON CONFLICT DO NOTHING;
       
      -- row exists, so we just need to obtain a lock on it. Because logical resource records are
      -- never deleted, we don't need to worry about it disappearing again before we grab the row lock
      OPEN lock_cur (t_resource_type_id := v_resource_type_id, t_logical_id := p_logical_id);
      FETCH lock_cur INTO t_logical_resource_id;
      CLOSE lock_cur;

    IF v_logical_resource_id = t_logical_resource_id
    THEN
      -- we created the logical resource and therefore we already own the lock. So now we can
      -- safely create the corresponding record in the resource-type-specific logical_resources table
      EXECUTE 'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_logical_resources (logical_resource_id, logical_id, is_deleted, last_updated, version_id, current_resource_id) '
      || '     VALUES ($1, $2, $3, $4, $5, $6)' USING v_logical_resource_id, p_logical_id, p_is_deleted, p_last_updated, p_version, v_resource_id;
      v_new_resource := 1;
    ELSE
      v_logical_resource_id := t_logical_resource_id;
    END IF;
  END IF;

  -- Remember everying is locked at the logical resource level, so we are thread-safe here
  IF v_new_resource = 0 THEN
    -- as this is an existing resource, we need to know the current resource id.
    -- This is only available at the resource-specific logical_resources level
    EXECUTE
         'SELECT current_resource_id, version_id FROM ' || v_schema_name || '.' || p_resource_type || '_logical_resources '
      || ' WHERE logical_resource_id = $1 '
    INTO v_current_resource_id, v_current_version USING v_logical_resource_id;
    
    IF v_current_resource_id IS NULL OR v_current_version IS NULL
    THEN
        -- our concurrency protection means that this shouldn't happen
        RAISE 'Schema data corruption - missing logical resource' USING ERRCODE = '99002';
    END IF;

    -- Concurrency check:
    --   the version parameter we've been given (which is also embedded in the JSON payload) must be 
    --   one greater than the current version, otherwise we've hit a concurrent update race condition
    IF p_version != v_current_version + 1
    THEN
      RAISE 'Concurrent update - mismatch of version in JSON' USING ERRCODE = '99001';
    END IF;
    
    -- existing resource, so need to delete all its parameters. 
    -- TODO patch parameter sets instead of all delete/all insert.
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_str_values          WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_number_values       WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_date_values         WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_latlng_values       WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_resource_token_refs WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_quantity_values     WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_profiles            WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_tags                WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || 'str_values                 WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || 'date_values                WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || 'resource_token_refs        WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || 'logical_resource_profiles  WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
    EXECUTE 'DELETE FROM ' || v_schema_name || '.' || 'logical_resource_tags      WHERE logical_resource_id = $1'
      USING v_logical_resource_id;
  END IF;


  EXECUTE
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted) '
      || ' VALUES ($1, $2, $3, $4, $5, $6)'
    USING v_resource_id, v_logical_resource_id, p_version, p_payload, p_last_updated, p_is_deleted;

  
  IF v_new_resource = 0 THEN
    -- As this is an existing logical resource, we need to update the xx_logical_resource values to match
    -- the values of the current resource. For new resources, these are added by the insert so we don't
    -- need to update them here.
    EXECUTE 'UPDATE ' || v_schema_name || '.' || p_resource_type || '_logical_resources SET current_resource_id = $1, is_deleted = $2, last_updated = $3, version_id = $4 WHERE logical_resource_id = $5'
      USING v_resource_id, p_is_deleted, p_last_updated, p_version, v_logical_resource_id;

    -- For V0014 we now also store is_deleted and last_updated values at the whole-system logical_resources level
    EXECUTE 'UPDATE ' || v_schema_name || '.logical_resources SET is_deleted = $1, last_updated = $2 WHERE logical_resource_id = $3'
      USING p_is_deleted, p_last_updated, v_logical_resource_id;
  END IF;

  -- Finally, write a record to RESOURCE_CHANGE_LOG which records each event
  -- related to resources changes (issue-1955)
  IF p_is_deleted = 'Y'
  THEN
    v_change_type := 'D';
  ELSE 
    IF v_new_resource = 0
    THEN
      v_change_type := 'U';
    ELSE
      v_change_type := 'C';
    END IF;
  END IF;

  INSERT INTO {{SCHEMA_NAME}}.resource_change_log(resource_id, change_tstamp, resource_type_id, logical_resource_id, version_id, change_type)
       VALUES (v_resource_id, p_last_updated, v_resource_type_id, v_logical_resource_id, p_version, v_change_type);
  
  -- Hand back the id of the logical resource we created earlier. In the new R4 schema
  -- only the logical_resource_id is the target of any FK, so there's no need to return
  -- the resource_id (which is now private to the _resources tables).
  o_logical_resource_id := v_logical_resource_id;
END $$;