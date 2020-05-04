-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a new code system in a thread-safe way
-- ----------------------------------------------------------------------------
    ( IN p_code_system_name  VARCHAR(255 OCTETS),
     OUT p_code_system_id        INT)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN

  DECLARE v_not_found              INT   DEFAULT 0;
  DECLARE v_duplicate              INT   DEFAULT 0;
  DECLARE c_duplicate CONDITION FOR SQLSTATE '23505';
  DECLARE CONTINUE HANDLER FOR NOT FOUND          SET v_not_found = 1;
  DECLARE CONTINUE HANDLER FOR c_duplicate        SET v_duplicate = 1;

  -- See if we already have it
  SELECT code_system_id INTO p_code_system_id
    FROM {{SCHEMA_NAME}}.code_systems
   WHERE code_system_name = p_code_system_name;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_ref_sequence INTO p_code_system_id;
    INSERT INTO {{SCHEMA_NAME}}.code_systems (mt_id, code_system_id, code_system_name)
         VALUES ({{ADMIN_SCHEMA_NAME}}.sv_tenant_id, p_code_system_id, p_code_system_name);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the code system. This
    -- is easy to handle, just turn around and read it again
    IF v_duplicate = 1
    THEN
      SELECT code_system_id INTO p_code_system_id
        FROM {{SCHEMA_NAME}}.code_systems
       WHERE code_system_name = p_code_system_name;
    END IF;
  END IF;

END

