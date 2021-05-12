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
  WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
  FOR UPDATE WITH RS;

  IF v_logical_resource_id = -1
  THEN
    -- indicates if the resource does not exist.
    SET v_total = -1;
  ELSE
    -- Step 1: Get the Details for the Resource/Logical_Resource
    -- the resource_id and version_id need to be fetched.
    -- these should never be null since we have a lock, and the resource exists.
    PREPARE r_stmt FROM
       'SET (?,?) = (SELECT CURRENT_RESOURCE_ID, VERSION_ID'
    || ' FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES'
    || ' WHERE LOGICAL_RESOURCE_ID = ?)';
    EXECUTE r_stmt INTO v_resource_id, v_version USING v_logical_resource_id;

    -- Step 2: Delete All Versions from Resources Table 
    -- Create the prepared statement to delete Resource Versions in chunks
    -- Implementation note: fetch must be the last part of the sub-select
    PREPARE dr_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES WHERE LOGICAL_RESOURCE_ID = ?';
    EXECUTE dr_stmt USING v_logical_resource_id;
    GET DIAGNOSTICS v_total = ROW_COUNT;

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
       'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ?';
    EXECUTE dlr_stmt USING v_logical_resource_id;
    
    -- Step 5: Delete from Global Logical Resources
    PREPARE dglr_stmt FROM 
       'DELETE FROM {{SCHEMA_NAME}}.LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ? AND RESOURCE_TYPE_ID = ?';
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