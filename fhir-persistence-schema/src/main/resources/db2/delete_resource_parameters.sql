-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2021
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to delete all the search parameters associated with a resource
-- 
-- p_logical_resource_id: the id representing the resource for which to delete the parameters
-- ----------------------------------------------------------------------------
    ( IN p_resource_type      VARCHAR( 36 OCTETS),
      IN p_logical_resource_id BIGINT)
    LANGUAGE SQL
    MODIFIES SQL DATA
BEGIN
  -- Declare the variables
  DECLARE v_schema_name         VARCHAR(128 OCTETS) DEFAULT '{{SCHEMA_NAME}}';
  DECLARE d_stmt STATEMENT;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_str_values          WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_number_values       WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_date_values         WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_latlng_values       WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_resource_token_refs WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_quantity_values     WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_profiles            WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_tags                WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_security            WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'str_values                WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'date_values               WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'resource_token_refs       WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'logical_resource_profiles WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'logical_resource_tags     WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;

    PREPARE d_stmt FROM 'DELETE FROM {{SCHEMA_NAME}}.' || 'logical_resource_security WHERE logical_resource_id = ?';
    EXECUTE d_stmt USING p_logical_resource_id;
END