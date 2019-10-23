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
-- p_json_version: the version FHIR injected into the JSON
-- p_str_values        an array of string parameter values
-- p_number_values     an array of number parameter values
-- p_date_values       an array of date parameter values
-- p_latlng_values     an array of lat/long parameter values
-- p_token_values      an array of token values
-- p_quantity_values   an array of quantity values
-- o_resource_id: output field returning the newly assigned resource_id value
-- ----------------------------------------------------------------------------
    ( IN p_logical_id                    VARCHAR(255 OCTETS), 
      IN p_payload                          BLOB(2147483647),
      IN p_last_updated                TIMESTAMP,
      IN p_is_deleted                       CHAR(1),
      IN p_json_version                      INT,
      IN p_str_values           {{SCHEMA_NAME}}.t_str_values_arr,
      IN p_number_values     {{SCHEMA_NAME}}.t_number_values_arr,
      IN p_date_values         {{SCHEMA_NAME}}.t_date_values_arr,
      IN p_latlng_values     {{SCHEMA_NAME}}.t_latlng_values_arr,
      IN p_token_values       {{SCHEMA_NAME}}.t_token_values_arr,
      IN p_quantity_values {{SCHEMA_NAME}}.t_quantity_values_arr,
     OUT o_resource_id                    BIGINT
    )
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN

  DECLARE v_logical_resource_id BIGINT     DEFAULT NULL;
  DECLARE v_current_resource_id BIGINT     DEFAULT NULL;
  DECLARE v_resource_id         BIGINT     DEFAULT NULL;
  DECLARE v_resource_type_id       INT     DEFAULT NULL;
  DECLARE v_new_resource           INT     DEFAULT 0;
  DECLARE v_not_found              INT     DEFAULT 0;
  DECLARE v_duplicate              INT     DEFAULT 0;
  DECLARE v_version                INT     DEFAULT 0;
  DECLARE v_insert_version         INT     DEFAULT 0;
  DECLARE v_resource_type      VARCHAR(32) DEFAULT '{{RESOURCE_TYPE}}';
  DECLARE c_duplicate CONDITION FOR SQLSTATE '23505';
  DECLARE CONTINUE HANDLER FOR NOT FOUND          SET v_not_found = 1;
  DECLARE CONTINUE HANDLER FOR c_duplicate        SET v_duplicate = 1;

  -- Set up our tenant security
  SELECT t.tenant_id INTO {{SCHEMA_NAME}}.session_tenant
    FROM {{SCHEMA_NAME}}.tenants t
   WHERE t.tenant_name = p_tenant_data_id;
  
  -- Stop right here if we don't have a valid tenant
  IF ({{SCHEMA_NAME}}.session_tenant IS NULL) THEN
  	SIGNAL SQLSTATE '99401' SET MESSAGE_TEXT = 'NOT AUTHORIZED: INVALID TENANT ID';
  END IF;

  SELECT resource_type_id INTO v_resource_type_id 
    FROM {{SCHEMA_NAME}}.resource_types WHERE resource_type = v_resource_type;

  -- Get a lock at the logical resource level
  SELECT logical_resource_id, current_resource_id INTO v_logical_resource_id, v_current_resource_id
    FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_logical_resources
   WHERE logical_id = p_logical_id
     FOR UPDATE WITH RS;

  -- Create the resource if we don't have it already (set by the continue handler)
  IF v_not_found = 1
  THEN
    VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_sequence INTO v_logical_resource_id;
    INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_logical_resources (logical_resource_id, logical_id)
         VALUES (v_logical_resource_id, p_logical_id);

    -- remember that we have a concurrent system...so there is a possibility
    -- that another thread snuck in before us and created the logical resource. This
    -- is easy to handle, just turn around and read it
    IF v_duplicate = 1
    THEN
      SELECT logical_resource_id, current_resource_id INTO v_logical_resource_id, v_current_resource_id
        FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_logical_resources
       WHERE logical_id = p_logical_id
         FOR UPDATE WITH RS;
    ELSE
      -- we created the logical resource, so it's completely new
      SET v_new_resource = 1;
    END IF;
  END IF;

  -- Remember everying is locked at the logical resource level, so we are thread-safe here
  IF v_new_resource = 0 THEN
    -- resource exists, so if we are storing a specific version, do a quick check to make
    -- sure that this version doesn't currently exist. This is only done when processing
    -- replication messages
    IF p_version IS NOT NULL
    THEN
      SET v_not_found = 0;
      SELECT resource_id INTO v_resource_id
        FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_resources dr
       WHERE dr.logical_resource_id = v_logical_resource_id
         AND dr.version_id = p_version;

      IF v_not_found = 0
      THEN
        -- this version of this resource already exists, so we bail out right away
        SET o_resource_id = v_resource_id;
        RETURN;
      END IF;
    END IF;

    -- Grab important information for the current version
    SELECT version_id INTO v_version
      FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_resources 
     WHERE resource_id = v_current_resource_id;

    -- If we have been passed a version number, this means that this is a replicated
    -- resource, and so we only need to delete if the given version is later than the
    -- the current version
    IF p_version IS NULL OR p_version > v_version
    THEN
      -- existing resource, so need to delete all its parameters
      DELETE FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_str_values             WHERE resource_id = v_current_resource_id;
      DELETE FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_number_values          WHERE resource_id = v_current_resource_id;
      DELETE FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_date_values            WHERE resource_id = v_current_resource_id;
      DELETE FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_latlng_values          WHERE resource_id = v_current_resource_id;
      DELETE FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_token_values           WHERE resource_id = v_current_resource_id;
      DELETE FROM {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_quantity_values        WHERE resource_id = v_current_resource_id;
    END IF;

  END IF;

  -- Persist the data using the given version number if required
  IF p_version IS NOT NULL
  THEN
    SET v_insert_version = p_version;
  ELSE
    SET v_insert_version = v_version + 1;

    -- Check the version number we're going to use matches the version
    -- number injected by the FHIR server into the JSON payload
    IF v_insert_version != p_json_version
    THEN
        SIGNAL SQLSTATE '99001' SET MESSAGE_TEXT = 'Concurrent update - mismatch of version in JSON';
    END IF;
  END IF;

  -- Create the new resource version.
  -- Alpha version uses last_updated time from the app-server, so we keep that here
  VALUES NEXT VALUE FOR {{SCHEMA_NAME}}.fhir_sequence INTO v_resource_id;
  INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_resources (resource_id, logical_resource_id, version_id, data, last_updated, is_deleted) 
       VALUES (v_resource_id, v_logical_resource_id, v_insert_version, p_payload, p_last_updated, p_is_deleted);

  IF p_version IS NULL OR p_version > v_version
  THEN
    -- only update the logical resource if the resource we are adding supercedes the
    -- the current resource
    UPDATE {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_logical_resources SET current_resource_id = v_resource_id WHERE logical_resource_id = v_logical_resource_id;

    -- Insert the parameter arrays into each table
    IF p_str_values IS NOT NULL
    THEN
      INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_str_values(parameter_name_id, str_value, str_value_lcase, resource_id)
           SELECT t.parameter_name_id, t.str_value, t.str_value_lcase, v_resource_id
             FROM UNNEST(p_str_values) AS t;
    END IF;

    IF p_number_values IS NOT NULL
    THEN
      INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_number_values (parameter_name_id, number_value, resource_id)
           SELECT t.parameter_name_id, t.number_value, v_resource_id
             FROM UNNEST(p_number_values) AS t;
    END IF;

    IF p_date_values IS NOT NULL
    THEN
      INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_date_values (parameter_name_id, date_value, date_start, date_end, resource_id)
           SELECT t.parameter_name_id, t.date_value, t.date_start, t.date_end, v_resource_id
             FROM UNNEST(p_date_values) AS t;
    END IF;

    IF p_latlng_values IS NOT NULL
    THEN
      INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_latlng_values (parameter_name_id, latitude_value, longitude_value, resource_id)
           SELECT t.parameter_name_id, t.latitude_value, t.longitude_value, v_resource_id
             FROM UNNEST(p_latlng_values) AS t;
    END IF;

    IF p_token_values IS NOT NULL
    THEN
      INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_token_values (parameter_name_id, code_system_id, token_value, resource_id)
           SELECT t.parameter_name_id, t.code_system_id, t.token_value, v_resource_id
             FROM UNNEST(p_token_values) AS t;
    END IF;

    IF p_quantity_values IS NOT NULL
    THEN
      INSERT INTO {{SCHEMA_NAME}}.{{LC_RESOURCE_TYPE}}_quantity_values (parameter_name_id, code, code_system_id, quantity_value, quantity_value_low, quantity_value_high, resource_id)
           SELECT t.parameter_name_id, t.code, t.code_system_id, t.quantity_value, t.quantity_value_low, t.quantity_value_high, v_resource_id
             FROM UNNEST(p_quantity_values) AS t;
    END IF;
  END IF;

  -- Hand back the id of the resource we created earlier
  SET o_resource_id = v_resource_id;

END
