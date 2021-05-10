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
  v_cur                 BIGINT := 1;
  v_rows                BIGINT := 0;

BEGIN
  v_schema_name := '{{SCHEMA_NAME}}';

  -- Prep 1: Get the v_resource_type_id
  SELECT resource_type_id INTO v_resource_type_id 
  FROM {{SCHEMA_NAME}}.resource_types
  WHERE resource_type = p_resource_type;

    -- Prep 2: Get the logical from the system-wide logical resource level
  SELECT logical_resource_id INTO v_logical_resource_id 
  FROM {{SCHEMA_NAME}}.logical_resources
  WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id;
  
  IF NOT FOUND
  THEN
    v_total := -1;
  ELSE
    -- Step 1: Get the Details for the Resource/Logical_Resource
    -- the resource_id and version_id need to be fetched.
    -- these should never be null since we have a lock, and the resource exists.
    EXECUTE 'SELECT R1.RESOURCE_ID, R1.VERSION_ID'
    || ' FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES LR'
    || '   INNER JOIN {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES R1'
    || '   ON R1.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID'
    || '   AND R1.VERSION_ID = LR.VERSION_ID'
    || ' WHERE LR.LOGICAL_ID = $1'
    INTO v_resource_id, v_version USING p_logical_id;

    -- Step 2: Delete All Versions from Resources Table 
    -- Start the delete_loop (up to the v_version)
    -- Implementation Note:
    -- 1 - We may have to delete this last for integrity reasons, thus not using <=
    -- 2 - fetch must be the last part of the sub-select
    WHILE v_cur <= v_version LOOP
      EXECUTE
        'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES WHERE RESOURCE_ID IN ('
        || ' SELECT R1.RESOURCE_ID FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES R1' 
        || ' WHERE R1.LOGICAL_RESOURCE_ID = $1 AND  R1.VERSION_ID <= $2'
        || '    FETCH FIRST 1000 ROWS ONLY)' 
        USING v_logical_resource_id, v_version;
        -- might have the wrong assignment :=
      GET DIAGNOSTICS v_rows = ROW_COUNT;

      -- Check if there is a reason to continue
      IF v_rows = 0
      THEN
        EXIT;
      END IF;

      v_total := v_total + v_rows;
      v_cur := v_cur + 1000;
    END LOOP;

    -- Step 3: Delete from All Parameters Tables
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

    -- Step 4: Delete from Logical Resources table 
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = $1'
    USING v_logical_resource_id;

    -- Step 5: Delete from Global Logical Resources
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = $1 AND RESOURCE_TYPE_ID = $2'
    USING v_logical_resource_id, v_resource_type_id;

    -- Step 6: Delete from resource_change_log
    EXECUTE
       'DELETE FROM {{SCHEMA_NAME}}.resource_change_log'
    || '  WHERE RESOURCE_ID = $1'
    || '    AND RESOURCE_TYPE_ID = $2'
    || '    AND LOGICAL_RESOURCE_ID = $3'
    USING v_resource_id, v_resource_type_id, v_logical_resource_id;
  END IF;

  -- Return the total number of deleted versions
  o_deleted := v_total;
END $$;
