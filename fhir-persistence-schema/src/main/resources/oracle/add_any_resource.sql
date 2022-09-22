-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2022
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
--   SQLSTATE 20001: on version conflict (concurrency)
--   SQLSTATE 20002: missing expected row (data integrity)
--   SQLSTATE 20004: called delete on a currently deleted resource
-- ----------------------------------------------------------------------------
    ( p_resource_type            IN   VARCHAR2,
      p_logical_id               IN   VARCHAR2, 
      p_payload                  IN       BLOB,
      p_last_updated             IN  TIMESTAMP,
      p_is_deleted               IN       CHAR,
      p_version                  IN        INT,
      p_parameter_hash_b64       IN   VARCHAR2,
      p_if_none_match            IN        INT,
      p_resource_payload_key     IN   VARCHAR2,
      o_logical_resource_id     OUT     NUMBER,
      o_current_parameter_hash  OUT   VARCHAR2,
      o_interaction_status      OUT        INT,
      o_if_none_match_version   OUT        INT
    ) AS

  v_schema_name         VARCHAR2(128) := '{{SCHEMA_NAME}}';
  v_logical_resource_id  NUMBER     := NULL;
  t_logical_resource_id  NUMBER     := NULL;
  v_current_resource_id  NUMBER     := NULL;
  v_resource_id          NUMBER     := NULL;
  v_resource_type_id        INT     := NULL;
  v_new_resource            INT     := 0;
  v_currently_deleted      CHAR(1)  := NULL;
  v_not_found               INT     := 0;
  v_duplicate               INT     := 0;
  v_current_version         INT     := 0;
  v_require_params          INT     := 1;
  v_change_type            CHAR(1)  := NULL;
  stmt                 VARCHAR2(2000);

BEGIN

  -- interaction status defaults to 0 unless we hit If-None-Match
  o_interaction_status := 0;
  
    -- Grab the new resource_id so that we can use it right away (and skip an update to xx_logical_resources later)
  v_resource_id := {{SCHEMA_NAME}}.fhir_sequence.nextval;

  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types WHERE resource_type = p_resource_type;

  -- get an update lock on the logical resource identity record
  BEGIN
    SELECT logical_resource_id INTO v_logical_resource_id
      FROM {{SCHEMA_NAME}}.logical_resource_ident
     WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
       FOR UPDATE;
  EXCEPTION
    WHEN no_data_found THEN NULL;
  END;

  -- Create the resource if we don't have it already
  IF v_logical_resource_id IS NULL
  THEN
    v_logical_resource_id := {{SCHEMA_NAME}}.fhir_sequence.nextval;

    BEGIN
      INSERT INTO {{SCHEMA_NAME}}.logical_resource_ident (resource_type_id, logical_id, logical_resource_id)
           VALUES (v_resource_type_id, p_logical_id, v_logical_resource_id);
    EXCEPTION
      WHEN dup_val_on_index THEN v_duplicate := 1;
    END;

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the logical resource ident. This
    -- is easy to handle, just turn around and read it
    IF v_duplicate = 1
    THEN
      -- we expect the record to exist here
      SELECT logical_resource_id INTO v_logical_resource_id
        FROM {{SCHEMA_NAME}}.logical_resource_ident
       WHERE resource_type_id = v_resource_type_id AND logical_id = p_logical_id
         FOR UPDATE;

       -- Because someone else created the logical_resoure_ident record, we need to see if
       -- they also created the corresponding logical_resources record
       SELECT logical_resource_id, parameter_hash, is_deleted 
         INTO t_logical_resource_id, o_current_parameter_hash, v_currently_deleted
         FROM {{SCHEMA_NAME}}.logical_resources 
        WHERE logical_resource_id = v_logical_resource_id;
       
       IF (t_logical_resource_id IS NULL)
       THEN
         -- other thread only created the ident record, so we still need to treat
         -- this as a new resource
         v_new_resource := 1;
       END IF;
     ELSE
       -- we created the logical_resource_ident, so we know this is a new resource
       v_new_resource := 1;
     END IF;
   ELSE
     -- the logical_resource_ident record exists, so now we need to find out
     -- if the corresponding logical_resources record exists
     SELECT logical_resource_id, parameter_hash, is_deleted 
       INTO t_logical_resource_id, o_current_parameter_hash, v_currently_deleted
       FROM {{SCHEMA_NAME}}.logical_resources 
      WHERE logical_resource_id = v_logical_resource_id;
       
     IF (t_logical_resource_id IS NULL)
     THEN
       -- the ident record was created as a reference, but because there's no logical_resources
       -- record, we treat this as a new resource
       v_new_resource := 1;
     END IF;
  END IF;

  IF v_new_resource = 1
  THEN
    -- create the logical_resources record
    INSERT INTO {{SCHEMA_NAME}}.logical_resources (logical_resource_id, resource_type_id, logical_id, reindex_tstamp, is_deleted, last_updated, parameter_hash)
         VALUES (v_logical_resource_id, v_resource_type_id, p_logical_id, to_timestamp('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), p_is_deleted, p_last_updated, p_parameter_hash_b64);

    -- create the xx_logical_resources record
    stmt :=
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_logical_resources (logical_resource_id, logical_id, is_deleted, last_updated, version_id, current_resource_id) '
      || '     VALUES (:1, :2, :3, :4, :5, :6)';
    EXECUTE IMMEDIATE stmt USING v_logical_resource_id, p_logical_id, p_is_deleted, p_last_updated, p_version, v_resource_id;

    -- Since the resource did not previously exist, make sure o_current_parameter_hash is NULL
    o_current_parameter_hash := NULL;

  ELSE
    -- as this is an existing resource, we need to know the current resource id.
    -- This is only available at the resource-specific logical_resources level
    stmt :=
         'SELECT current_resource_id, version_id FROM ' || v_schema_name || '.' || p_resource_type || '_logical_resources '
      || ' WHERE logical_resource_id = ? ';
    EXECUTE IMMEDIATE stmt INTO v_current_resource_id, v_current_version USING v_logical_resource_id;
    
    IF v_current_resource_id IS NULL OR v_current_version IS NULL
    THEN
        -- our concurrency protection means that this shouldn't happen
        raise_application_error(-20002, 'Schema data corruption - missing logical resource');
    END IF;

    -- If-None-Match does not apply if the current resource is deleted
    IF v_currently_deleted = 'N' AND p_if_none_match = 0
    THEN
        -- If-None-Match: * hit
        o_interaction_status := 1;
        o_if_none_match_version := v_current_version;
        RETURN;
    END IF;
    
    -- Concurrency check:
    --   the version parameter we've been given (which is also embedded in the JSON payload) must be 
    --   one greater than the current version, otherwise we've hit a concurrent update race condition
    IF p_version != v_current_version + 1
    THEN
        raise_application_error(-20001, 'Concurrent update - mismatch of version in JSON');
    END IF;

    -- Prevent creating a new deletion marker if the resource is currently deleted
    IF v_currently_deleted = 'Y' AND p_is_deleted = 'Y'
    THEN
        raise_application_error(-20004, 'Unexpected attempt to delete a Resource which is currently deleted');
    END IF;
    
    -- check the current vs new parameter hash to see if we can bypass the delete/insert
    IF o_current_parameter_hash IS NULL OR o_current_parameter_hash != p_parameter_hash_b64
    THEN
        -- existing resource, so need to delete all its parameters. 
        {{SCHEMA_NAME}}.delete_resource_parameters(p_resource_type, v_logical_resource_id);
    END IF; -- end if parameter hash is different    
  END IF; -- end if existing resource

  stmt :=
         'INSERT INTO ' || v_schema_name || '.' || p_resource_type || '_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted, resource_payload_key) '
      || ' VALUES (:1, :2, :3, :4, :5, :6, :7)';
  EXECUTE IMMEDIATE stmt USING v_resource_id, v_logical_resource_id, p_version, p_payload, p_last_updated, p_is_deleted, p_resource_payload_key;

  IF v_new_resource = 0 THEN
    -- As this is an existing logical resource, we need to update the xx_logical_resource values to match
    -- the values of the current resource. For new resources, these are added by the insert so we don't
    -- need to update them here.
    stmt := 'UPDATE ' || v_schema_name || '.' || p_resource_type || '_logical_resources SET current_resource_id = :1, is_deleted = :2, last_updated = :3, version_id = :4 WHERE logical_resource_id = :5';
    EXECUTE IMMEDIATE stmt USING v_resource_id, p_is_deleted, p_last_updated, p_version, v_logical_resource_id;

    -- For V0014 we also store is_deleted and last_updated at the logical_resource level
    UPDATE {{SCHEMA_NAME}}.logical_resources SET is_deleted = p_is_deleted, last_updated = p_last_updated, parameter_hash = p_parameter_hash_b64 WHERE logical_resource_id = v_logical_resource_id;
  END IF;

  -- Finally, write a record to RESOURCE_CHANGE_LOG which records each event
  -- related to resources changes (issue-1955)
  IF p_is_deleted = 'Y'
  THEN
    v_change_type := 'D';
  ELSE 
    IF v_new_resource = 0 AND v_currently_deleted = 'N'
    THEN
      v_change_type := 'U';
    ELSE
      v_change_type := 'C';
    END IF;
  END IF;

  INSERT INTO {{SCHEMA_NAME}}.resource_change_log(resource_id, change_tstamp, resource_type_id, logical_resource_id, version_id, change_type)
       VALUES (v_resource_id, p_last_updated, v_resource_type_id, v_logical_resource_id, p_version, v_change_type);

  -- Hand back the id of the logical resource we created earlier
  o_logical_resource_id := v_logical_resource_id;
END;