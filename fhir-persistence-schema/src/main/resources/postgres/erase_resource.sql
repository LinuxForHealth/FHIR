-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2021
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to remove a resource, history and parameters values
-- 
-- p_resource_type: the resource type
-- p_logical_id: the resource logical id
-- o_deleted: the total number of resource versions that are deleted
-- ----------------------------------------------------------------------------
    ( IN p_resource_type                VARCHAR(  36),
      IN p_logical_id                   VARCHAR( 255),
      OUT o_deleted                     BIGINT)
    RETURNS BIGINT
    LANGUAGE plpgsql
     AS $$

  DECLARE
  v_schema_name         VARCHAR(128);
  v_logical_resource_id BIGINT := NULL;
  v_resource_type_id    BIGINT := -1;
  v_resource_id         BIGINT := -1;
  v_version             INT    := 0;
  v_total               BIGINT := 0;

BEGIN
  v_schema_name := '{{SCHEMA_NAME}}';

  -- Prep 1: Get the v_resource_type_id
  SELECT resource_type_id INTO v_resource_type_id 
  FROM {{SCHEMA_NAME}}.resource_types
  WHERE resource_type = p_resource_type;

  -- Prep 2: Get the logical from the system-wide logical resource level
  SELECT logical_resource_id INTO v_logical_resource_id 
  FROM {{SCHEMA_NAME}}.logical_resources
  WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
  FOR UPDATE;
  
  IF NOT FOUND
  THEN
    v_total := -1;
  ELSE
    -- Step 1: Get the Details for the Resource/Logical_Resource
    -- the resource_id and version_id need to be fetched.
    -- these should never be null since we have a lock, and the resource exists.
    EXECUTE 'SELECT CURRENT_RESOURCE_ID, VERSION_ID'
    || ' FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES'
    || ' WHERE LOGICAL_RESOURCE_ID = $1'
    INTO v_resource_id, v_version USING v_logical_resource_id;

    -- Step 2: Delete from resource_change_log
    -- Delete is done before the RESOURCES table entries disappear
    -- This uses the primary_keys of each table to conditional-delete
    EXECUTE 
    'DELETE FROM {{SCHEMA_NAME}}.RESOURCE_CHANGE_LOG'
    || '  WHERE RESOURCE_ID IN ('
    || '    SELECT RESOURCE_ID'
    || '    FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES'
    || '    WHERE LOGICAL_RESOURCE_ID = $1)'
    USING v_logical_resource_id;

    -- Step 3: Delete All Versions from Resources Table 
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES WHERE LOGICAL_RESOURCE_ID = $1'
    USING v_logical_resource_id;
    GET DIAGNOSTICS v_total = ROW_COUNT;

    -- Step 4: Delete from All Parameters Tables
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_str_values          WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_number_values       WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_date_values         WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_latlng_values       WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_resource_token_refs WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_quantity_values     WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.str_values           WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.date_values          WHERE logical_resource_id = $1'
    USING v_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.resource_token_refs  WHERE logical_resource_id = $1'
    USING v_logical_resource_id;

    -- Step 5: Delete from Logical Resources table 
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = $1'
    USING v_logical_resource_id;

    -- Step 6: Delete from Global Logical Resources
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = $1 AND RESOURCE_TYPE_ID = $2'
    USING v_logical_resource_id, v_resource_type_id;
  END IF;

  -- Return the total number of deleted versions
  o_deleted := v_total;
END $$;
