-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a new code system in a thread-safe way
-- ----------------------------------------------------------------------------
    ( IN p_code_system_name  VARCHAR(255),
      OUT p_code_system_id        INT)
    LANGUAGE plpgsql
      AS $$

  BEGIN  
    SELECT code_system_id INTO p_code_system_id
    FROM {{SCHEMA_NAME}}.code_systems
    WHERE code_system_name = p_code_system_name;

    IF p_code_system_id IS NULL
    THEN
      SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_ref_sequence') INTO p_code_system_id;
      INSERT INTO {{SCHEMA_NAME}}.code_systems (code_system_id, code_system_name)
         VALUES (p_code_system_id, p_code_system_name) ON CONFLICT DO NOTHING;

      SELECT code_system_id INTO p_code_system_id
        FROM {{SCHEMA_NAME}}.code_systems
       WHERE code_system_name = p_code_system_name;
     END IF;  
END $$;

