-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2016, 2021
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- LOADED ON: {{DATE}}

-- ----------------------------------------------------------------------------
-- Procedure to add a resource version and its associated parameters. These
-- parameters only ever point to the latest version of a resource, never to
-- previous versions, which are kept to support history queries.
-- implNote - Conventions:
--           p_... prefix used to represent input parameters
--           v_... prefix used to represent declared variables
--           c_... prefix used to represent conditions
--           o_... prefix used to represent output parameters
-- Parameters:
--   p_logical_id:  the logical id given to the resource by the FHIR server
--   p_payload:     the BLOB (of JSON) which is the resource content
--   p_last_updated the last_updated time given by the FHIR server
--   p_is_deleted:  the soft delete flag
--   p_version:     the intended version id for this resource
--   p_parameter_hash_b64: Base64 encoded hash of parameter values
--   p_if_none_match: conditional create-on-update
--   o_logical_resource_id: output field returning the newly assigned logical_resource_id value
--   o_resource_id: output field returning the newly assigned resource_id value
--   o_current_parameter_hash: Base64 current parameter hash if existing resource
--   o_interaction_status: output indicating whether a change was made or IfNoneMatch hit
--   o_if_none_match_version: output revealing the version found when o_interaction_status is 1 (IfNoneMatch)
-- Exceptions:
--   SQLSTATE 99001: on version conflict (concurrency)
--   SQLSTATE 99002: missing expected row (data integrity)
-- ----------------------------------------------------------------------------
    ( IN p_resource_type                VARCHAR( 36 OCTETS),
      IN p_logical_id                   VARCHAR(255 OCTETS), 
      IN p_payload                         BLOB(1048576),
      IN p_last_updated               TIMESTAMP,
      IN p_is_deleted                      CHAR(  1),
      IN p_version                          INT,
      IN p_parameter_hash_b64           VARCHAR(44 OCTETS),
      IN p_if_none_match                    INT,
      OUT o_logical_resource_id          BIGINT,
      OUT o_resource_row_id              BIGINT,
      OUT o_current_parameter_hash      VARCHAR(44 OCTETS),
      OUT o_interaction_status              INT,
      OUT o_if_none_match_version           INT
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
  DECLARE v_currently_deleted      CHAR(1)  DEFAULT NULL;
  DECLARE v_not_found               INT     DEFAULT 0;
  DECLARE v_duplicate               INT     DEFAULT 0;
  DECLARE v_current_version         INT     DEFAULT 0;
  DECLARE v_require_params          INT     DEFAULT 1;
  DECLARE v_change_type            CHAR(1)  DEFAULT NULL;
  DECLARE c_duplicate CONDITION FOR SQLSTATE '23505';
  DECLARE stmt STATEMENT;
  DECLARE CONTINUE HANDLER FOR NOT FOUND          SET v_not_found = 1;
  DECLARE CONTINUE HANDLER FOR c_duplicate        SET v_duplicate = 1;

  -- interaction status defaults to 0 unless we hit If-None-Match
  SET o_interaction_status = 0;
  
  -- use a variable for the schema in our prepared statements to make them easier 
  -- to write
  SET v_schema_name = '{{SCHEMA_NAME}}';

    -- Grab the new resource_id so that we can use it right away (and skip an update to xx_logical_resources later)
  VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_sequence INTO v_resource_id;

  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types WHERE resource_type = p_resource_type;
  
  -- FOR UPDATE WITH RS does not appear to work using a prepared statement and
  -- cursor, so we have to run this directly against the logical_resources table.
  SELECT logical_resource_id, parameter_hash, is_deleted INTO v_logical_resource_id, o_current_parameter_hash, v_currently_deleted
    FROM {{SCHEMA_NAME}}.logical_resources
   WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
     FOR UPDATE WITH RS
   ;
  
  -- Create the resource if we don't have it already
  IF v_logical_resource_id IS NULL
  THEN
    VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_sequence INTO v_logical_resource_id;
    PREPARE stmt FROM
       'INSERT INTO ' || v_schema_name || '.logical_resources (mt_id, logical_resource_id, resource_type_id, logical_id, reindex_tstamp, is_deleted, last_updated, parameter_hash) '
    || '     VALUES (?, ?, ?, ?, ?, ?, ?, ?)';
    EXECUTE stmt USING {{ADMIN_SCHEMA_NAME}}.sv_tenant_id, v_logical_resource_id, v_resource_type_id, p_logical_id, '1970-01-01-00.00.00.0', p_is_deleted, p_last_updated, p_parameter_hash_b64;

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the logical resource. This
    -- is easy to handle, just turn around and read it
    IF v_duplicate = 1
    THEN
      -- row exists, so we just need to obtain a lock on it. Because logical resource records are
      -- never deleted, we don't need to worry about it disappearing again before we grab the row lock
      SELECT logical_resource_id, parameter_hash, is_deleted INTO v_logical_resource_id, o_current_parameter_hash, v_currently_deleted
        FROM {{SCHEMA_NAME}}.logical_resources
       WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
         FOR UPDATE WITH RS
       ;
       
      -- Since the resource did not previously exist, set o_current_parameter_hash back to NULL
      SET o_current_parameter_hash = NULL;

    ELSE
      -- we created the logical resource and therefore we already own the lock. So now we can
      -- safely create the corresponding record in the resource-type-specific logical_resources table
      PREPARE stmt FROM
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_logical_resources (mt_id, logical_resource_id, logical_id, is_deleted, last_updated, version_id, current_resource_id) '
      || '     VALUES (?, ?, ?, ?, ?, ?, ?)';
      EXECUTE stmt USING {{ADMIN_SCHEMA_NAME}}.sv_tenant_id, v_logical_resource_id, p_logical_id, p_is_deleted, p_last_updated, p_version, v_resource_id;
      SET v_new_resource = 1;
    END IF;
  END IF;

  -- Remember everying is locked at the logical resource level, so we are thread-safe here
  IF v_new_resource = 0 THEN
    -- as this is an existing resource, we need to know the current resource id.
    -- This is only available at the resource-specific logical_resources level
    PREPARE stmt FROM
         'SET (?,?) = ('
      || 'SELECT current_resource_id, version_id FROM ' || v_schema_name || '.' || p_resource_type || '_logical_resources '
      || ' WHERE logical_resource_id = ? )';
    EXECUTE stmt INTO v_current_resource_id, v_current_version USING v_logical_resource_id;
    
    IF v_current_resource_id IS NULL OR v_current_version IS NULL
    THEN
        -- our concurrency protection means that this shouldn't happen
        SIGNAL SQLSTATE '99002' SET MESSAGE_TEXT = 'Schema data corruption - missing logical resource';
    END IF;

    -- If-None-Match does not apply if the current resource is deleted
    IF v_currently_deleted = 'N' AND p_if_none_match = 0
    THEN
        -- If-None-Match: * hit
        SET o_interaction_status = 1;
        SET o_if_none_match_version = v_current_version;
        RETURN;
    END IF;
    
    -- Concurrency check:
    --   the version parameter we've been given (which is also embedded in the JSON payload) must be 
    --   one greater than the current version, otherwise we've hit a concurrent update race condition
    IF p_version != v_current_version + 1
    THEN
        SIGNAL SQLSTATE '99001' SET MESSAGE_TEXT = 'Concurrent update - mismatch of version in JSON';
    END IF;
    
    -- check the current vs new parameter hash to see if we can bypass the delete/insert
    IF o_current_parameter_hash IS NULL OR o_current_parameter_hash != p_parameter_hash_b64
    THEN
	    -- existing resource, so need to delete all its parameters. 
	    -- TODO patch parameter sets instead of all delete/all insert.
	    PREPARE stmt FROM 'CALL ' || v_schema_name || '.delete_resource_parameters(?,?)';
	    EXECUTE stmt USING p_resource_type, v_logical_resource_id;
	END IF; -- end if parameter hash is different    
  END IF; -- end if existing resource

  PREPARE stmt FROM
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_resources (mt_id, resource_id, logical_resource_id, version_id, data, last_updated, is_deleted) '
      || ' VALUES ( ?, ?, ?, ?, ?, ?, ?)';
  EXECUTE stmt USING {{ADMIN_SCHEMA_NAME}}.sv_tenant_id, v_resource_id, v_logical_resource_id, p_version, p_payload, p_last_updated, p_is_deleted;

  IF v_new_resource = 0 THEN
    -- As this is an existing logical resource, we need to update the xx_logical_resource values to match
    -- the values of the current resource. For new resources, these are added by the insert so we don't
    -- need to update them here.
    PREPARE stmt FROM 'UPDATE ' || v_schema_name || '.' || p_resource_type || '_logical_resources SET current_resource_id = ?, is_deleted = ?, last_updated = ?, version_id = ? WHERE logical_resource_id = ?';
    EXECUTE stmt USING v_resource_id, p_is_deleted, p_last_updated, p_version, v_logical_resource_id;

    -- For V0014 we also store is_deleted and last_updated at the logical_resource level
    PREPARE stmt FROM 'UPDATE ' || v_schema_name || '.logical_resources SET is_deleted = ?, last_updated = ?, parameter_hash = ? WHERE logical_resource_id = ?';
    EXECUTE stmt USING p_is_deleted, p_last_updated, p_parameter_hash_b64, v_logical_resource_id;
  END IF;
  
  -- DB2 doesn't support user defined array types in dynamic SQL UNNEST/CAST statements,
  -- so we can no longer insert the parameters here - instead we have to use individual
  -- JDBC statements.
  
  -- Finally, write a record to RESOURCE_CHANGE_LOG which records each event
  -- related to resources changes (issue-1955)
  IF p_is_deleted = 'Y'
  THEN
    SET v_change_type = 'D';
  ELSE 
    IF v_new_resource = 0
    THEN
      SET v_change_type = 'U';
    ELSE
      SET v_change_type = 'C';
    END IF;
  END IF;

  INSERT INTO {{SCHEMA_NAME}}.resource_change_log(mt_id, resource_id, change_tstamp, resource_type_id, logical_resource_id, version_id, change_type)
       VALUES ({{ADMIN_SCHEMA_NAME}}.sv_tenant_id, v_resource_id, p_last_updated, v_resource_type_id, v_logical_resource_id, p_version, v_change_type);

  -- Hand back the id of the logical resource we created earlier
  SET o_logical_resource_id = v_logical_resource_id;

  -- Resource Row Id which is used to set large blobs
  SET o_resource_row_id = v_resource_id;
END
