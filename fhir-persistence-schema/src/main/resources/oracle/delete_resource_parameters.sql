-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2022
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to delete all the search parameters associated with a resource
-- 
-- p_logical_resource_id: the id representing the resource for which to delete the parameters
-- ----------------------------------------------------------------------------
    ( p_resource_type       IN VARCHAR2,
      p_logical_resource_id IN NUMBER) AS
  v_schema_name     VARCHAR2(128) := '{{SCHEMA_NAME}}';
  d_stmt            VARCHAR2(256);
BEGIN

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_str_values          WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_number_values       WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_date_values         WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_latlng_values       WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_resource_token_refs WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_quantity_values     WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_profiles            WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_tags                WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_security            WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_ref_values          WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || 'str_values                WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || 'date_values               WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || 'resource_token_refs       WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || 'logical_resource_profiles WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || 'logical_resource_tags     WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;

    d_stmt := 'DELETE FROM {{SCHEMA_NAME}}.' || 'logical_resource_security WHERE logical_resource_id = :1';
    EXECUTE IMMEDIATE d_stmt USING p_logical_resource_id;
END;