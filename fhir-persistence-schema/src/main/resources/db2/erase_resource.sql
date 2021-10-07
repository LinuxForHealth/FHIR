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
  DECLARE v_logical_resource_id BIGINT DEFAULT -1;
  DECLARE v_resource_type_id    BIGINT DEFAULT -1;
  DECLARE v_total               BIGINT DEFAULT 0;
  DECLARE v_not_found           BIGINT DEFAULT 0;
  DECLARE v_msg                 VARCHAR(128 OCTETS) DEFAULT 'DEFAULT ERROR';

  DECLARE r_stmt, dr_stmt, d_stmt, dlr_stmt, dglr_stmt, drcl_stmt STATEMENT;

  -- Set a condition when the resource is not found.
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_not_found = -1;

  -- Prep 1: Get the v_resource_type_id
  SELECT resource_type_id INTO v_resource_type_id 
  FROM {{SCHEMA_NAME}}.resource_types
  WHERE resource_type = p_resource_type;
  
  -- Prep 2: Get the logical from the system-wide logical resource level
  SELECT logical_resource_id INTO v_logical_resource_id 
  FROM {{SCHEMA_NAME}}.logical_resources
  WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
  FOR UPDATE WITH RS;

  IF v_not_found = -1
  THEN
    -- indicates if the resource does not exist.
    SET v_total = -1;
  ELSE
    -- Step 1: Delete from resource_change_log
    PREPARE rcl_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.RESOURCE_CHANGE_LOG'
    || '  WHERE RESOURCE_ID IN ('
    || '    SELECT RESOURCE_ID'
    || '    FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES'
    || '    WHERE LOGICAL_RESOURCE_ID = ?)';
    EXECUTE rcl_stmt USING v_logical_resource_id;

    -- Step 2: Delete All Versions from Resources Table 
    -- Create the prepared statement to delete Resource Versions in chunks
    -- Implementation note: fetch must be the last part of the sub-select
    PREPARE dr_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES WHERE LOGICAL_RESOURCE_ID = ?';
    EXECUTE dr_stmt USING v_logical_resource_id;
    GET DIAGNOSTICS v_total = ROW_COUNT;

    -- Step 3: Delete from all parameters tables
    PREPARE d_stmt FROM 'CALL ' || v_schema_name || '.delete_resource_parameters(?,?)';
    EXECUTE d_stmt USING p_resource_type, v_logical_resource_id;

    -- Step 4: Delete from Logical Resources table 
    PREPARE dlr_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ?';
    EXECUTE dlr_stmt USING v_logical_resource_id;

    -- Step 5: Delete from Global Logical Resources
    PREPARE dglr_stmt FROM 
       'DELETE FROM {{SCHEMA_NAME}}.LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = ? AND RESOURCE_TYPE_ID = ?';
    EXECUTE dglr_stmt USING v_logical_resource_id, v_resource_type_id;
  END IF;

  -- Return the total number of deleted versions
  SET o_deleted = v_total;
END