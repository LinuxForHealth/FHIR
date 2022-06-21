-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2022
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to either create or select for update a logical_resource_ident
-- record. For Citus, this part of the "add_any_resource" logic is split
-- off into its own function here which allows us to distribute the function
-- on logical_id, which is used in all the SQL/DML executed by this
-- function. This allows Citus to push execution of the entire function
-- down to the worker node.
--
-- implNote - Conventions:
--           p_... prefix used to represent input parameters
--           v_... prefix used to represent declared variables
--           t_... prefix used to represent temp variables
--           o_... prefix used to represent output parameters
-- Parameters:
--   p_resource_type_id: the resource type id from resource_types
--   p_logical_id: the logical id given to the resource by the FHIR server
--   o_logical_resource_id: output field returning the newly assigned logical_resource_id value
--
-- ----------------------------------------------------------------------------
    (  IN p_resource_type_id                 INT,
       IN p_logical_id                   VARCHAR(64), 
      OUT o_logical_resource_id           BIGINT)
    LANGUAGE plpgsql
     AS $$

  DECLARE 
  v_schema_name         VARCHAR(128);
  v_logical_resource_id  BIGINT := NULL;
  t_logical_resource_id  BIGINT := NULL;
  
  -- Because we don't really update any existing key, so use NO KEY UPDATE to achieve better concurrence performance. 
  lock_cur CURSOR (t_resource_type_id INT, t_logical_id VARCHAR(1024)) FOR SELECT logical_resource_id FROM {{SCHEMA_NAME}}.logical_resource_ident WHERE resource_type_id = t_resource_type_id AND logical_id = t_logical_id FOR NO KEY UPDATE;

BEGIN

  -- LOADED ON: {{DATE}}
  v_schema_name := '{{SCHEMA_NAME}}';

  -- Get a lock on the logical resource identity record
  OPEN lock_cur(t_resource_type_id := p_resource_type_id, t_logical_id := p_logical_id);
  FETCH lock_cur INTO v_logical_resource_id;
  CLOSE lock_cur;
  
  -- Create the resource ident record if we don't have it already
  IF v_logical_resource_id IS NULL
  THEN
    -- allocate the new logical_resource_id value
    SELECT nextval('{{SCHEMA_NAME}}.fhir_sequence') INTO v_logical_resource_id;

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the ident record. To
    -- handle this in PostgreSQL, we INSERT...ON CONFLICT DO NOTHING, then turn
    -- around and read again to check that the logical_resource_id in the table
    -- matches the value we tried to insert.
    INSERT INTO {{SCHEMA_NAME}}.logical_resource_ident (resource_type_id, logical_id, logical_resource_id)
         VALUES (p_resource_type_id, p_logical_id, v_logical_resource_id) ON CONFLICT DO NOTHING;

    -- Do a read so that we can verify that *we* did the insert
    OPEN lock_cur(t_resource_type_id := p_resource_type_id, t_logical_id := p_logical_id);
    FETCH lock_cur INTO t_logical_resource_id;
    CLOSE lock_cur;

    IF v_logical_resource_id != t_logical_resource_id
    THEN
      -- logical_resource_ident record was created by another thread...so use that id instead
      v_logical_resource_id := t_logical_resource_id;
    END IF;
  END IF;

  -- Hand back the id of the logical resource we created earlier. In the new R4 schema
  -- only the logical_resource_id is the target of any FK, so there's no need to return
  -- the resource_id (which is now private to the _resources tables).
  o_logical_resource_id := v_logical_resource_id;
END $$;
