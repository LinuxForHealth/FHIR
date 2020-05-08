-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- The stored procedure adds search parameters name (code) to logical id mappings
-- ----------------------------------------------------------------------------
    ( IN p_parameter_name    VARCHAR(255),
      OUT p_parameter_name_id     INT)
    LANGUAGE plpgsql
     AS $$

BEGIN
    SELECT parameter_name_id INTO p_parameter_name_id
    FROM {{SCHEMA_NAME}}.parameter_names
    WHERE parameter_name = p_parameter_name;

    IF p_parameter_name_id IS NULL
    THEN
      SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_ref_sequence') INTO p_parameter_name_id;
      INSERT INTO {{SCHEMA_NAME}}.parameter_names (parameter_name_id, parameter_name)
         VALUES (p_parameter_name_id, p_parameter_name) ON CONFLICT DO NOTHING;

      SELECT parameter_name_id INTO p_parameter_name_id
        FROM {{SCHEMA_NAME}}.parameter_names
       WHERE parameter_name = p_parameter_name;
     END IF;  
END $$;

