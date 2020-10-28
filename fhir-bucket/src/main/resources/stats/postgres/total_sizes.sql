-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

select resource_type, total_size, total_size - tbl_size AS index_size
  from (
select resource_type,
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
