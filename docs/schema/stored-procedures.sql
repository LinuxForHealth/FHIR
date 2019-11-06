-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2019
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
-- IBM FHIR Server - Schema Output
-- Generated at 2019-11-06T18:40:44.870Z
-------------------------------------------------------------------------------

CREATE OR REPLACE PROCEDURE FHIRADMIN.SET_TENANT
-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2019
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
( IN p_tenant_name VARCHAR(36),
  IN p_tenant_key  VARCHAR(44)
)
  LANGUAGE SQL
BEGIN
  DECLARE v_not_found INT DEFAULT 0;
  DECLARE v_tenant_status VARCHAR(16);
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_not_found = 1;

  IF p_tenant_name IS NULL
  THEN
      -- a null tenant name is asking us to clear the old tenant id value
      -- this is usually done at the end of an interaction, but can also
      -- be called by the datasource provider before it returns a connection,
      -- preventing accidental exposure of tenant-specific data
      SET fhir_admin.sv_tenant_id = NULL;
  ELSE
	  -- set the sv_tenant_id session variable for the 
	  -- given tenant_name, but only if the correct key
	  -- is provided
	  SELECT t.mt_id, t.tenant_status INTO fhir_admin.sv_tenant_id, v_tenant_status
	    FROM fhir_admin.tenants t
	   WHERE t.tenant_name = p_tenant_name
	     AND EXISTS (SELECT 1 FROM fhir_admin.tenant_keys tk WHERE tk.mt_id = t.mt_id
	     AND tk.tenant_hash = sysibm.hash(tk.tenant_salt || p_tenant_key, 2));
	
	  IF v_not_found = 1
	  THEN
	    -- applications should not be passing invalid keys, so treat this as
	    -- an exception
	    SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: INVALID TENANT ID';
	  ELSEIF v_tenant_status = 'FROZEN'
	  THEN
	    -- the tenant_key is valid, but the status of tenant has been set to frozen
	    -- which means the tenant data should not be accessible
	    SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: TENANT IS FROZEN';
	  END IF;
  END IF;

END
@

CREATE OR REPLACE PROCEDURE FHIRAPP.ADD_CODE_SYSTEM
-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016
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
    FROM FHIRAPP.code_systems
   WHERE code_system_name = p_code_system_name;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR FHIRAPP.fhir_ref_sequence INTO p_code_system_id;
    INSERT INTO FHIRAPP.code_systems (mt_id, code_system_id, code_system_name)
         VALUES (FHIRADMIN.sv_tenant_id, p_code_system_id, p_code_system_name);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the code system. This
    -- is easy to handle, just turn around and read it again
    IF v_duplicate = 1
    THEN
      SELECT code_system_id INTO p_code_system_id
        FROM FHIRAPP.code_systems
       WHERE code_system_name = p_code_system_name;
    END IF;
  END IF;

END

@

CREATE OR REPLACE PROCEDURE FHIRAPP.ADD_PARAMETER_NAME
-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016
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


  -- See if we already have it
  SELECT parameter_name_id INTO p_parameter_name_id
    FROM FHIRAPP.parameter_names
   WHERE parameter_name = p_parameter_name;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR FHIRAPP.fhir_ref_sequence INTO p_parameter_name_id;
    INSERT INTO FHIRAPP.parameter_names (mt_id, parameter_name_id, parameter_name)
         VALUES (FHIRADMIN.sv_tenant_id, p_parameter_name_id, p_parameter_name);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the parameter. This
    -- is easy to handle, just turn around and read it again
    IF v_duplicate = 1
    THEN
      SELECT parameter_name_id INTO p_parameter_name_id
        FROM FHIRAPP.parameter_names
       WHERE parameter_name = p_parameter_name;
    END IF;
  END IF;

END

@

CREATE OR REPLACE PROCEDURE FHIRAPP.ADD_RESOURCE_TYPE
-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016
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
    FROM FHIRAPP.resource_types
   WHERE resource_type = p_resource_type;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR FHIRAPP.fhir_ref_sequence INTO p_resource_type_id;
    INSERT INTO FHIRAPP.resource_types (mt_id, resource_type_id, resource_type)
         VALUES (FHIRADMIN.sv_tenant_id, p_resource_type_id, p_resource_type);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the resource type. This
    -- is easy to handle, just turn around and read it again
    IF v_duplicate = 1
    THEN
      SELECT resource_type_id INTO p_resource_type_id
        FROM FHIRAPP.resource_types
       WHERE resource_type = p_resource_type;
    END IF;
  END IF;

END
@

CREATE OR REPLACE PROCEDURE FHIRAPP.ADD_ANY_RESOURCE
-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2019
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to add a resource version and its associated parameters. These
-- parameters only ever point to the latest version of a resource, never to
-- previous versions, which are kept to support history queries.
-- Parameters must be loaded into the parameters_gtt global temporary table
-- prior to this procedure being called
-- p_logical_id: the logical id given to the resource by the FHIR server
-- p_payload:    the BLOB (of JSON) which is the resource content
-- p_last_updated the last_updated time given by the FHIR server
-- p_is_deleted: the soft delete flag
-- p_version_id: the version id if this is a replicated message
-- o_resource_id: output field returning the newly assigned resource_id value
-- ----------------------------------------------------------------------------
    ( IN p_resource_type                 VARCHAR( 36 OCTETS),
      IN p_logical_id                    VARCHAR(255 OCTETS), 
      IN p_payload                          BLOB(2147483647),
      IN p_last_updated                TIMESTAMP,
      IN p_is_deleted                       CHAR(  1),
      IN p_source_key                    VARCHAR( 64),
      IN p_version                           INT,
      OUT o_logical_resource_id            BIGINT
    )
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN

  DECLARE v_schema_name         VARCHAR(128 OCTETS);
  DECLARE v_logical_resource_id  BIGINT     DEFAULT NULL;
  DECLARE v_current_resource_id  BIGINT     DEFAULT NULL;
  DECLARE v_resource_id          BIGINT     DEFAULT NULL;
  DECLARE v_resource_type_id        INT     DEFAULT NULL;
  DECLARE v_new_resource            INT     DEFAULT 0;
    --  DECLARE v_not_found               INT     DEFAULT 0;
  DECLARE v_duplicate               INT     DEFAULT 0;
  DECLARE v_version                 INT     DEFAULT 0;
  DECLARE v_insert_version          INT     DEFAULT 0;
  DECLARE c_duplicate CONDITION FOR SQLSTATE '23505';
  DECLARE stmt,lock_stmt STATEMENT;
  DECLARE lock_cur CURSOR FOR lock_stmt;
--  DECLARE CONTINUE HANDLER FOR NOT FOUND          SET v_not_found = 1;
  DECLARE CONTINUE HANDLER FOR c_duplicate        SET v_duplicate = 1;

  -- use a variable for the schema in our prepared statements to make them easier 
  -- to write
  SET v_schema_name = 'FHIRAPP';

  SELECT resource_type_id INTO v_resource_type_id 
    FROM FHIRAPP.resource_types WHERE resource_type = p_resource_type;

  -- Get a lock at the system-wide logical resource level
  PREPARE lock_stmt FROM
     ' SELECT logical_resource_id '
  || '   FROM ' || v_schema_name || '.logical_resources '
  || '  WHERE resource_type_id = ? AND logical_id = ? '
  || ' FOR UPDATE WITH RS';

  -- we need to use a cursor in this context because we need the FOR UPDATE WITH RS support
  -- and this does not work with the SET (?,?) = (select ...) construct
  OPEN lock_cur USING v_resource_type_id, p_logical_id;
  FETCH lock_cur INTO v_logical_resource_id;
  CLOSE lock_cur;
  
  -- Create the resource if we don't have it already
  IF v_logical_resource_id IS NULL
  THEN
    VALUES NEXT VALUE FOR FHIRAPP.fhir_sequence INTO v_logical_resource_id;
    PREPARE stmt FROM
       'INSERT INTO ' || v_schema_name || '.logical_resources (mt_id, logical_resource_id, resource_type_id, logical_id) '
    || '     VALUES (?, ?, ?, ?)';
    EXECUTE stmt USING FHIRADMIN.sv_tenant_id, v_logical_resource_id, v_resource_type_id, p_logical_id;

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the logical resource. This
    -- is easy to handle, just turn around and read it
    IF v_duplicate = 1
    THEN
      -- row exists, so we just need to obtain a lock on it. Because logical resource records are
      -- never deleted, we don't need to worry about it disappearing again before we grab the row lock
      OPEN lock_cur USING v_resource_type_id, p_logical_id;
      FETCH lock_cur INTO v_logical_resource_id;
      CLOSE lock_cur;
    ELSE
      -- we created the logical resource and therefore we already own the lock. So now we can
      -- safely create the corresponding record in the resource-type-specific logical_resources table
      PREPARE stmt FROM
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_logical_resources (mt_id, logical_resource_id, logical_id) '
      || '     VALUES (?, ?, ?)';
      EXECUTE stmt USING FHIRADMIN.sv_tenant_id, v_logical_resource_id, p_logical_id;
      SET v_new_resource = 1;
    END IF;
  END IF;

  -- Remember everying is locked at the logical resource level, so we are thread-safe here
  IF v_new_resource = 0 THEN
    -- as this is an existing resource, we need to know the current resource id.
    -- This is only available at the resource-specific logical_resources level
    PREPARE stmt FROM
         'SET (?) = ('
      || 'SELECT current_resource_id FROM ' || v_schema_name || '.' || p_resource_type || '_logical_resources '
      || ' WHERE logical_resource_id = ? )';
    EXECUTE stmt INTO v_current_resource_id USING v_logical_resource_id;
    
    IF v_current_resource_id IS NULL
    THEN
        -- our concurrency protection means that this shouldn't happen
        SIGNAL SQLSTATE '99002' SET MESSAGE_TEXT = 'Schema data corruption - missing logical resource';
    END IF;
  
    -- resource exists, so if we are storing a specific version, do a quick check to make
    -- sure that this version doesn't currently exist. This is only done when processing
    -- replication messages or other custom operations which explicitly provide a version
    IF p_version IS NOT NULL
    THEN
      PREPARE stmt FROM
         'SET (?) = ('
      || 'SELECT resource_id FROM ' || v_schema_name || '.' || p_resource_type || '_resources dr '
      || ' WHERE dr.logical_resource_id = ? '
      || '   AND dr.version_id = ?)';
      EXECUTE stmt INTO v_resource_id USING v_logical_resource_id, p_version;

      IF v_resource_id IS NOT NULL
      THEN
        -- this version of this resource already exists, so we bail out right away (we
        -- don't allow any updating of an existing resource version)
        SET o_logical_resource_id = v_logical_resource_id;
        RETURN;
      END IF;
    END IF;

    -- Grab the version_id for the current version
    PREPARE stmt FROM 
       'SET (?) = ('
    || 'SELECT version_id FROM ' || v_schema_name || '.' || p_resource_type || '_resources '
    || ' WHERE resource_id = ?)';
    EXECUTE stmt INTO v_version USING v_current_resource_id;

    -- If we have been passed a version number, this means that this is a replicated
    -- resource, and so we only need to delete parameters if the given version is later 
    -- than the current version. This allows versions (from replication or custom ops)
    -- to arrive out of order, and we're just filling in the gaps
    IF p_version IS NULL OR p_version > v_version
    THEN
      -- existing resource, so need to delete all its parameters. 
      -- TODO patch parameter sets instead of all delete/all insert.
      PREPARE stmt FROM 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_str_values      WHERE logical_resource_id = ?';
      EXECUTE stmt USING v_logical_resource_id;
      PREPARE stmt FROM 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_number_values   WHERE logical_resource_id = ?';
      EXECUTE stmt USING v_logical_resource_id;
      PREPARE stmt FROM 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_date_values     WHERE logical_resource_id = ?';
      EXECUTE stmt USING v_logical_resource_id;
      PREPARE stmt FROM 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_latlng_values   WHERE logical_resource_id = ?';
      EXECUTE stmt USING v_logical_resource_id;
      PREPARE stmt FROM 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_token_values    WHERE logical_resource_id = ?';
      EXECUTE stmt USING v_logical_resource_id;
      PREPARE stmt FROM 'DELETE FROM ' || v_schema_name || '.' || p_resource_type || '_quantity_values WHERE logical_resource_id = ?';
      EXECUTE stmt USING v_logical_resource_id;
    END IF;

  END IF;

  -- Persist the data using the given version number if required
  IF p_version IS NOT NULL
  THEN
    SET v_insert_version = p_version;
  ELSE
    SET v_insert_version = v_version + 1;
  END IF;

  -- Create the new resource version.
  -- Alpha version uses last_updated time from the app-server, so we keep that here
  VALUES NEXT VALUE FOR FHIRAPP.fhir_sequence INTO v_resource_id;

  PREPARE stmt FROM
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_resources (mt_id, resource_id, logical_resource_id, version_id, data, last_updated, is_deleted) '
      || ' VALUES ( ?, ?, ?, ?, ?, ?, ?)';
  EXECUTE stmt USING FHIRADMIN.sv_tenant_id, v_resource_id, v_logical_resource_id, v_insert_version, p_payload, p_last_updated, p_is_deleted;

  IF p_version IS NULL OR p_version > v_version
  THEN
    -- only update the logical resource if the resource we are adding supercedes the
    -- the current resource. mt_id isn't needed here...implied via permission
    PREPARE stmt FROM 'UPDATE ' || v_schema_name || '.' || p_resource_type || '_logical_resources SET current_resource_id = ? WHERE logical_resource_id = ?';
    EXECUTE stmt USING v_resource_id, v_logical_resource_id;

    -- DB2 doesn't support user defined array types in dynamic SQL UNNEST/CAST statements,
    -- so we can no longer insert the parameters here - instead we have to use individual
    -- JDBC statements.
  END IF;

  -- Hand back the id of the logical resource we created earlier. In the new R4 schema
  -- only the logical_resource_id is the target of any FK, so there's no need to return
  -- the resource_id (which is now private to the _resources tables).
  SET o_logical_resource_id = v_logical_resource_id;
END
@

