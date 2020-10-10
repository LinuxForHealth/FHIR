select resource_type || param.nm AS param_table, pg_relation_size('fhirdata.' || resource_type || param.nm)
  from fhirdata.resource_types,
    (VALUES 
	   ('_str_values'),('_token_values'),('_date_values'),
	   ('_logical_resources'),('_resources'),('_number_values'),
	   ('_quantity_values'),('_composites')
	) AS param(nm)
order by 2 DESC, resource_type, param.nm
;

-- better in GB
select resource_type, round(total_size / 1073741824) AS total_gb, 
   round(tbl_size / 1073741824) AS tbl_gb, round(index_size / 1073741824) AS index_gb
  from (
select resource_type,
        sum(pg_indexes_size('fhirdata.' || resource_type || param.nm)) index_size,
        sum(pg_relation_size('fhirdata.' || resource_type || param.nm)) tbl_size,
        sum(pg_total_relation_size('fhirdata.' || resource_type || param.nm)) total_size
  from fhirdata.resource_types,
    (VALUES 
	   ('_str_values'),('_token_values'),('_date_values'),
	   ('_logical_resources'),('_resources'),('_number_values'),
	   ('_quantity_values'),('_composites'),('_latlng_values')
	) AS param(nm)
GROUP BY resource_type
  ) sub
order by 2 DESC, 1
;



