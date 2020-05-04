-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- The stored procedure adds search parameters name (code) to logical id mappings
-- ----------------------------------------------------------------------------
    ( IN p_parameter_name    VARCHAR(255 OCTETS),
     OUT p_parameter_name_id     INT)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN

  DECLARE v_not_found              INT   DEFAULT 0;
  DECLARE v_duplicate              INT   DEFAULT 0;
  DECLARE c_duplicate CONDITION FOR SQLSTATE '23505';
  DECLARE CONTINUE HANDLER FOR NOT FOUND          SET v_not_found = 1;
  DECLARE CONTINUE HANDLER FOR c_duplicate        SET v_duplicate = 1;

  -- See if we already have it
  SELECT parameter_name_id INTO p_parameter_name_id
    FROM {{SCHEMA_NAME}}.parameter_names
   WHERE parameter_name = p_parameter_name;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_ref_sequence INTO p_parameter_name_id;
    INSERT INTO {{SCHEMA_NAME}}.parameter_names (mt_id, parameter_name_id, parameter_name)
         VALUES ({{ADMIN_SCHEMA_NAME}}.sv_tenant_id, p_parameter_name_id, p_parameter_name);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the parameter. This
    -- is easy to handle, just turn around and read it again
    IF v_duplicate = 1
    THEN
      SELECT parameter_name_id INTO p_parameter_name_id
        FROM {{SCHEMA_NAME}}.parameter_names
       WHERE parameter_name = p_parameter_name;
    END IF;
  END IF;

END

