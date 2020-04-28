-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource version and its associated parameters. These
-- parameters only ever point to the latest version of a resource, never to
-- previous versions, which are kept to support history queries.
-- Parameters must be loaded into the parameters_gtt global temporary table
-- prior to this procedure being called
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

  -- Stop right here if we don't have a valid tenant
  IF (fhir_admin.sv_tenant_id IS NULL) THEN
  	SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: INVALID TENANT ID OR TENANT KEY';
  END IF;

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

