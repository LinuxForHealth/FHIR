-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource type to the resource_types table
-- ----------------------------------------------------------------------------
    ( IN p_resource_type     VARCHAR(255 OCTETS),
     OUT p_resource_type_id      INT)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN

  DECLARE v_not_found              INT   DEFAULT 0;
  DECLARE v_duplicate              INT   DEFAULT 0;
  DECLARE c_duplicate CONDITION FOR SQLSTATE '23505';
  DECLARE CONTINUE HANDLER FOR NOT FOUND          SET v_not_found = 1;
  DECLARE CONTINUE HANDLER FOR c_duplicate        SET v_duplicate = 1;

  -- See if we already have it
  SELECT resource_type_id INTO p_resource_type_id
    FROM {{SCHEMA_NAME}}.resource_types
   WHERE resource_type = p_resource_type;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_ref_sequence INTO p_resource_type_id;
    INSERT INTO {{SCHEMA_NAME}}.resource_types (mt_id, resource_type_id, resource_type)
         VALUES ({{ADMIN_SCHEMA_NAME}}.sv_tenant_id, p_resource_type_id, p_resource_type);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the resource type. This
    -- is easy to handle, just turn around and read it again
    IF v_duplicate = 1
    THEN
      SELECT resource_type_id INTO p_resource_type_id
        FROM {{SCHEMA_NAME}}.resource_types
       WHERE resource_type = p_resource_type;
    END IF;
  END IF;

END
