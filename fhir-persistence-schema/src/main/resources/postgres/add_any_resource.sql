-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource version and its associated parameters. These
-- parameters only ever point to the latest version of a resource, never to
-- previous versions, which are kept to support history queries.
-- p_logical_id: the logical id given to the resource by the FHIR server
-- p_payload:    the BLOB (of JSON) which is the resource content
-- p_last_updated the last_updated time given by the FHIR server
-- p_is_deleted: the soft delete flag
-- p_version_id: the version id if this is a replicated message
-- o_resource_id: output field returning the newly assigned resource_id value
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
  v_version                 INT := 0;
  v_insert_version          INT := 0;
  -- Because we don't really update any existing key, so use NO KEY UPDATE to achieve better concurrence performance. 
  lock_cur CURSOR (t_resource_type_id INT, t_logical_id VARCHAR(255)) FOR SELECT logical_resource_id FROM {{SCHEMA_NAME}}.logical_resources WHERE resource_type_id = t_resource_type_id AND logical_id = t_logical_id FOR NO KEY UPDATE;

BEGIN
  v_schema_name := '{{SCHEMA_NAME}}';
  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types WHERE resource_type = p_resource_type;

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
    INSERT INTO {{SCHEMA_NAME}}.logical_resources (logical_resource_id, resource_type_id, logical_id)
         VALUES (v_logical_resource_id, v_resource_type_id, p_logical_id) ON CONFLICT DO NOTHING;
       
      -- row exists, so we just need to obtain a lock on it. Because logical resource records are
      -- never deleted, we don't need to worry about it disappearing again before we grab the row lock
      OPEN lock_cur (t_resource_type_id := v_resource_type_id, t_logical_id := p_logical_id);
      FETCH lock_cur INTO t_logical_resource_id;
      CLOSE lock_cur;
           
    IF v_logical_resource_id = t_logical_resource_id
    THEN
      -- we created the logical resource and therefore we already own the lock. So now we can
      -- safely create the corresponding record in the resource-type-specific logical_resources table
      EXECUTE 'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_logical_resources (logical_resource_id, logical_id) '
      || '     VALUES ($1, $2)' USING v_logical_resource_id, p_logical_id;
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
         'SELECT current_resource_id FROM ' || v_schema_name || '.' || p_resource_type || '_logical_resources '
      || ' WHERE logical_resource_id = $1 '
    INTO v_current_resource_id USING v_logical_resource_id;
    
    IF v_current_resource_id IS NULL
    THEN
        -- our concurrency protection means that this shouldn't happen
        RAISE 'Schema data corruption - missing logical resource' USING ERRCODE = '99002';
    END IF;
  
    -- resource exists, so if we are storing a specific version, do a quick check to make
    -- sure that this version doesn't currently exist. This is only done when processing
    -- custom operations which explicitly provide a version
    IF p_version IS NOT NULL
    THEN
      EXECUTE
         'SELECT resource_id FROM ' || v_schema_name || '.' || p_resource_type || '_resources dr '
      || ' WHERE dr.logical_resource_id = $1 '
      || '   AND dr.version_id = $2 '
      INTO v_resource_id USING v_logical_resource_id, p_version;

      IF v_resource_id IS NOT NULL
      THEN
        -- this version of this resource already exists, so we bail out right away (we
        -- don't allow any updating of an existing resource version)
        o_logical_resource_id := v_logical_resource_id;
        RETURN;
      END IF;
    END IF;

    -- Grab the version_id for the current version
    EXECUTE
      'SELECT version_id FROM ' || v_schema_name || '.' || p_resource_type || '_resources '
    || ' WHERE resource_id = $1 '
      INTO v_version USING v_current_resource_id;

    -- If we have been passed a version number, this means that this is a custom ops
    -- resource, and so we only need to delete parameters if the given version is later 
    -- than the current version. This allows versions (from custom ops)
    -- to arrive out of order, and we're just filling in the gaps
    IF p_version IS NULL OR p_version > v_version
    THEN
      -- existing resource, so need to delete all its parameters. 
      -- TODO patch parameter sets instead of all delete/all insert.
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_composites      WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_str_values      WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_number_values   WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_date_values     WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_latlng_values   WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_token_values    WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
      EXECUTE 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_quantity_values WHERE logical_resource_id = $1'
        USING v_logical_resource_id;
    END IF;

  END IF;

  -- Persist the data using the given version number if required
  IF p_version IS NOT NULL
  THEN
    v_insert_version := p_version;
  ELSE
    v_insert_version := v_version + 1;
  END IF;

  -- Create the new resource version.
  -- Alpha version uses last_updated time from the app-server, so we keep that here
  SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_sequence') INTO v_resource_id;

  EXECUTE
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted) '
      || ' VALUES ($1, $2, $3, $4, $5, $6)'
    USING v_resource_id, v_logical_resource_id, v_insert_version, p_payload, p_last_updated, p_is_deleted;

  IF p_version IS NULL OR p_version > v_version
  THEN
    -- only update the logical resource if the resource we are adding supercedes the
    -- the current resource. mt_id isn't needed here...implied via permission
    EXECUTE 'UPDATE ' || v_schema_name || '.' || p_resource_type || '_logical_resources SET current_resource_id = $1 WHERE logical_resource_id = $2'
      USING v_resource_id, v_logical_resource_id;
  END IF;

  -- Hand back the id of the logical resource we created earlier. In the new R4 schema
  -- only the logical_resource_id is the target of any FK, so there's no need to return
  -- the resource_id (which is now private to the _resources tables).
  o_logical_resource_id := v_logical_resource_id;
END $$;
