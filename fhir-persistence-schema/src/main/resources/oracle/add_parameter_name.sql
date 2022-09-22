-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2022
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- LOADED ON: {{DATE}}

-- ----------------------------------------------------------------------------
-- The stored procedure adds search parameters name (code) to logical id mappings
-- ----------------------------------------------------------------------------
    ( p_parameter_name     IN VARCHAR2,
      p_parameter_name_id OUT      INT) AS
  v_not_found              INT   := 0;
  v_duplicate              INT   := 0;
BEGIN

  -- See if we already have it
  BEGIN
    SELECT parameter_name_id INTO p_parameter_name_id
      FROM {{SCHEMA_NAME}}.parameter_names
     WHERE parameter_name = p_parameter_name;
  EXCEPTION
    WHEN no_data_found THEN
      p_parameter_name_id := {{SCHEMA_NAME}}.fhir_ref_sequence.nextval;
      BEGIN
        INSERT INTO {{SCHEMA_NAME}}.parameter_names (parameter_name_id, parameter_name)
             VALUES (p_parameter_name_id, p_parameter_name);
      EXCEPTION
        -- remember that we have a concurrent system...so there is a possibility
        -- that another thread snuck in before us and created the parameter. This
        -- is easy to handle, just turn around and read it again
        WHEN dup_val_on_index THEN
          SELECT parameter_name_id INTO p_parameter_name_id
            FROM {{SCHEMA_NAME}}.parameter_names
           WHERE parameter_name = p_parameter_name;
      END;
  END;
END;