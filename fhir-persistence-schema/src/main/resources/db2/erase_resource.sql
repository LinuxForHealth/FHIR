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
    ( IN p_resource_type                VARCHAR(  36 OCTETS),
      IN p_logical_id                   VARCHAR( 255 OCTETS),
      OUT o_deleted                     BIGINT)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN
  -- Declare the variables
  DECLARE v_schema_name         VARCHAR(128 OCTETS) DEFAULT '{{SCHEMA_NAME}}';
  DECLARE v_logical_resource_id BIGINT;
  DECLARE v_resource_type_id    BIGINT DEFAULT -1;
  DECLARE v_resource_id         BIGINT DEFAULT -1;
  DECLARE v_version             INT DEFAULT 0;
  DECLARE v_total               BIGINT DEFAULT 0;
  DECLARE v_cur                 BIGINT DEFAULT 1;
  DECLARE v_rows                BIGINT DEFAULT 0;
  DECLARE v_msg                 VARCHAR(128 OCTETS) DEFAULT 'DEFAULT ERROR';

  DECLARE r_stmt, dr_stmt, d_stmt, dlr_stmt, dglr_stmt, drcl_stmt STATEMENT;

  -- Set a condition when the resource is not found.
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_logical_resource_id = -1;

  -- Prep 1: Get the v_resource_type_id
  SELECT resource_type_id INTO v_resource_type_id 
  FROM {{SCHEMA_NAME}}.resource_types
  WHERE resource_type = p_resource_type;
  
  -- Prep 2: Get the logical from the system-wide logical resource level
  SELECT logical_resource_id INTO v_logical_resource_id 
  FROM {{SCHEMA_NAME}}.logical_resources
  WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id;

  IF v_logical_resource_id = -1
  THEN
    -- indicates if the resource does not exist.
    -- SET v_msg = ('DOES_NOT_EXIST logical_resource_id[' || CAST(v_logical_resource_id AS VARCHAR(128)) || ']');
    -- SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = v_msg;
    SET v_total = -1;
  ELSE
    -- Step 1: Get the Details for the Resource/Logical_Resource
    -- the resource_id and version_id need to be fetched.
    -- these should never be null since we have a lock, and the resource exists.
    PREPARE r_stmt FROM
       'SET (?,?) = (SELECT R1.RESOURCE_ID, R1.VERSION_ID'
    || ' FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES LR'
    || '   INNER JOIN {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES R1'
    || '   ON R1.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID'
    || '   AND R1.VERSION_ID = LR.VERSION_ID'
    || ' WHERE LR.LOGICAL_ID = ? WITH RS)';
    EXECUTE r_stmt INTO v_resource_id, v_version USING p_logical_id;

    -- Step 2: Delete All Versions from Resources Table 
    -- Create the prepared statement to delete Resource Versions in chunks
    -- Implementation note: fetch must be the last part of the sub-select
    PREPARE dr_stmt FROM 
       'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES WHERE RESOURCE_ID IN ('
    || ' SELECT R1.RESOURCE_ID FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES R1' 
    || ' WHERE R1.LOGICAL_RESOURCE_ID = ? AND R1.VERSION_ID <= ?'
    || '    FETCH FIRST 1000 ROWS ONLY)';

    -- Start the delete_loop (up to the v_version)
    -- Implementation Note: We may have to delete this last for integrity reasons, thus not using <=
    delete_loop:
    WHILE v_cur <= v_version DO
      EXECUTE dr_stmt USING v_logical_resource_id, v_version;
      GET DIAGNOSTICS v_rows = ROW_COUNT;

      -- Nothing to commit, and no reason to continue
      IF v_rows = 0
      THEN
        LEAVE delete_loop;
      END IF;

      SET v_total = (v_total + v_rows);
      SET v_cur = (v_cur + 1000);
    END WHILE;

    SET v_msg = 'Total: ' || CAST(v_total AS VARCHAR(128));

    -- Step 3: Delete from All Parameters Tables
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_str_values          WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_number_values       WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_date_values         WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_latlng_values       WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_resource_token_refs WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_quantity_values     WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'str_values          WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'date_values         WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;
    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'resource_token_refs WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING v_logical_resource_id;

    -- Step 4: Delete from Logical Resources table 
    PREPARE dlr_stmt FROM 
       'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES'
    || '  WHERE LOGICAL_RESOURCE_ID = ?';
    EXECUTE dlr_stmt USING v_logical_resource_id;
    
    -- Step 5: Delete from Global Logical Resources
    PREPARE dglr_stmt FROM 
       'DELETE FROM {{SCHEMA_NAME}}.LOGICAL_RESOURCES'
    || '  WHERE LOGICAL_RESOURCE_ID = ?'
    || '    AND RESOURCE_TYPE_ID = ?';
    EXECUTE dglr_stmt USING v_logical_resource_id, v_resource_type_id;
    
    -- Step 6: Delete from resource_change_log
    PREPARE drcl_stmt FROM 
       'DELETE FROM {{SCHEMA_NAME}}.resource_change_log'
    || '  WHERE RESOURCE_ID = ?'
    || '    AND RESOURCE_TYPE_ID = ?'
    || '    AND LOGICAL_RESOURCE_ID = ?';
    EXECUTE drcl_stmt USING v_resource_id, v_resource_type_id, v_logical_resource_id;
  END IF;

  -- Return the total number of deleted versions
  SET o_deleted = v_total;
END