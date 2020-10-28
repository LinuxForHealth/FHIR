-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

select param_table, pg_size_pretty(total_size - table_size) as index_size
from (
select resource_type || param.nm AS param_table, 
pg_relation_size('fhirdata.' || resource_type || param.nm) AS table_size,
pg_total_relation_size('fhirdata.' || resource_type || param.nm) AS total_size
  from fhirdata.resource_types,
    (VALUES
           ('_str_values'),('_token_values'),('_date_values'),
           ('_logical_resources'),('_resources'),('_number_values'),
           ('_quantity_values'),('_composites')
        ) AS param(nm)
	) sizes
order by total_size - table_size DESC, 1
;
