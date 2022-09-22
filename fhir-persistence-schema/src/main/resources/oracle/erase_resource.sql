-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2022
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
    ( p_resource_type              IN  VARCHAR2,
      p_logical_id                 IN  VARCHAR2,
      p_erased_resource_group_id   IN  NUMBER,
      o_deleted                   OUT  NUMBER) AS
  -- Declare the variables
  v_logical_resource_id NUMBER := NULL;
  v_resource_type_id    NUMBER := -1;
  v_total               NUMBER := 0;

  stmt VARCHAR2(1024);

BEGIN

  -- Prep 1: Get the v_resource_type_id
  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types
  WHERE resource_type = p_resource_type;
  
  -- Prep 2: Get the logical from the system-wide logical resource level
  BEGIN
    SELECT logical_resource_id INTO v_logical_resource_id 
      FROM {{SCHEMA_NAME}}.logical_resources
     WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
       FOR UPDATE;
  EXCEPTION
    WHEN no_data_found THEN v_total := -1;
  END;

  IF v_logical_resource_id IS NOT NULL
  THEN
    -- Step 1: Delete from resource_change_log
    stmt := 'DELETE FROM {{SCHEMA_NAME}}.RESOURCE_CHANGE_LOG'
    || '  WHERE RESOURCE_ID IN ('
    || '    SELECT RESOURCE_ID'
    || '    FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES'
    || '    WHERE LOGICAL_RESOURCE_ID = :1)';
    EXECUTE IMMEDIATE stmt USING v_logical_resource_id;

    -- Step 1.1: Record the versions we need to delete if we are doing payload offload
    stmt := 'INSERT INTO {{SCHEMA_NAME}}.erased_resources(erased_resource_group_id, resource_type_id, logical_id, version_id) ' 
        || '      SELECT :1, :2, :3, version_id '
        || '        FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES '
        || '       WHERE LOGICAL_RESOURCE_ID = :4 ';
    EXECUTE IMMEDIATE stmt USING p_erased_resource_group_id, v_resource_type_id, p_logical_id, v_logical_resource_id;

    -- Step 2: Delete All Versions from Resources Table 
    -- Create the prepared statement to delete Resource Versions in chunks
    -- Implementation note: fetch must be the last part of the sub-select
    EXECUTE IMMEDIATE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_RESOURCES WHERE LOGICAL_RESOURCE_ID = :1'
                USING v_logical_resource_id;
    v_total := SQL%rowcount;

    -- Step 3: Delete from all parameters tables
    {{SCHEMA_NAME}}.delete_resource_parameters(p_resource_type, v_logical_resource_id);

    -- Step 4: Delete from Logical Resources table 
    EXECUTE IMMEDIATE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = :1'
                USING v_logical_resource_id;

    -- Step 5: Delete from Global Logical Resources
    EXECUTE IMMEDIATE 'DELETE FROM {{SCHEMA_NAME}}.LOGICAL_RESOURCES WHERE LOGICAL_RESOURCE_ID = :1 AND RESOURCE_TYPE_ID = :2'
                USING v_logical_resource_id, v_resource_type_id;
  END IF;

  -- Return the total number of deleted versions
  o_deleted := v_total;
END;