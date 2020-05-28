-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource type to the resource_types table
-- ----------------------------------------------------------------------------
    ( IN p_resource_type     VARCHAR(255),
      OUT p_resource_type_id      INT)
     LANGUAGE plpgsql
       AS $$

BEGIN
    SELECT resource_type_id INTO p_resource_type_id
    FROM {{SCHEMA_NAME}}.resource_types
    WHERE resource_type = p_resource_type;

    IF p_resource_type_id IS NULL
    THEN
      SELECT NEXTVAL('{{SCHEMA_NAME}}.fhir_ref_sequence') INTO p_resource_type_id;
      INSERT INTO {{SCHEMA_NAME}}.resource_types (resource_type_id, resource_type)
         VALUES (p_resource_type_id, p_resource_type) ON CONFLICT DO NOTHING;

      SELECT resource_type_id INTO p_resource_type_id
      FROM {{SCHEMA_NAME}}.resource_types
      WHERE resource_type = p_resource_type;
    END IF;
END $$;
