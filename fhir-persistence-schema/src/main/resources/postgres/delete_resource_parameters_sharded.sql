-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2021
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Procedure to delete all search parameters values for a given resource.
-- This variant is for use with the distributed schema variant typically
-- deployed to a distributed database service like Citus.
-- 
-- p_shard_key: the key used for distribution (sharding)
-- p_resource_type: the resource type name
-- p_logical_resource_id: the database id of the resource for which the parameters are to be deleted
-- ----------------------------------------------------------------------------
    (  IN p_shard_key          SMALLINT,
       IN p_resource_type       VARCHAR( 36),
       IN p_logical_resource_id  BIGINT,
      OUT o_logical_resource_id  BIGINT)
       RETURNS BIGINT
    LANGUAGE plpgsql
     AS $$

  DECLARE
  v_schema_name         VARCHAR(128);

BEGIN
  v_schema_name := '{{SCHEMA_NAME}}';

    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_str_values          WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_number_values       WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_date_values         WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_latlng_values       WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_resource_token_refs WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_quantity_values     WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_profiles            WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_tags                WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_security            WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.' || p_resource_type || '_ref_values          WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.str_values                 WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.date_values                WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.resource_token_refs        WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.logical_resource_profiles  WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.logical_resource_tags      WHERE shard_key = $1 AND logical_resource_id = $2'
    USING p_shard_key, p_logical_resource_id;
    EXECUTE 'DELETE FROM {{SCHEMA_NAME}}.logical_resource_security  WHERE shard_key = $1 AND logical_resource_id = $2'
	USING p_shard_key, p_logical_resource_id;
	
	-- because we're a function, pass back a result
	o_logical_resource_id := p_logical_resource_id;
END $$;
