select resource_type || param.nm AS param_table, pg_relation_size('fhirdata.' || resource_type || param.nm)
  from fhirdata.resource_types,
    (VALUES 
	   ('_str_values'),('_token_values'),('_date_values'),
	   ('_logical_resources'),('_resources'),('_number_values'),
	   ('_quantity_values'),('_composites')
	) AS param(nm)
order by 2 DESC, resource_type, param.nm
;
