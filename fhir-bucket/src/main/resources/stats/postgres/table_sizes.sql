-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

select resource_type || param.nm AS param_table, pg_relation_size('fhirdata.' || resource_type || param.nm)
  from fhirdata.resource_types,
    (VALUES 
	   ('_str_values'),('_token_values'),('_date_values'),
	   ('_logical_resources'),('_resources'),('_number_values'),
	   ('_quantity_values'),('_composites'),('_latlng_values'),('_token_resource_refs')
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



-- token_values replaced by resource_token_refs
select resource_type, round(total_size / 1073741824) AS total_gb, 
   round(tbl_size / 1073741824) AS tbl_gb, round(index_size / 1073741824) AS index_gb
  from (
select resource_type,
        sum(pg_indexes_size('fhirdata.' || resource_type || param.nm)) index_size,
        sum(pg_relation_size('fhirdata.' || resource_type || param.nm)) tbl_size,
        sum(pg_total_relation_size('fhirdata.' || resource_type || param.nm)) total_size
  from fhirdata.resource_types,
    (VALUES 
	   ('_str_values'),('_date_values'),
	   ('_logical_resources'),('_resources'),('_number_values'),
	   ('_quantity_values'),('_composites'),('_latlng_values'),
           ('_resource_token_refs')
	) AS param(nm)
GROUP BY resource_type
  ) sub
WHERE round(tbl_size / 1073741824) > 0
order by 2 DESC, 1
;


-- vacuum commands for all resource types
select 'VACUUM FULL ' || 'fhirdata.' || resource_type || param.nm
  from fhirdata.resource_types,
    (VALUES
           ('_str_values'),('_date_values'),
           ('_logical_resources'),('_resources'),('_number_values'),
           ('_quantity_values'),('_composites'),('_latlng_values'),
           ('_resource_token_refs')
        ) AS param(nm)
;

-- vacuum commands for one resource type
select 'VACUUM FULL ' || 'fhirdata.' || resource_type || param.nm || ';'
  from fhirdata.resource_types,
    (VALUES
           ('_str_values'),('_date_values'),
           ('_logical_resources'),('_resources'),('_number_values'),
           ('_quantity_values'),('_composites'),('_latlng_values'),
           ('_resource_token_refs')
        ) AS param(nm)
		WHERE resource_type IN ( 
                           'Encounter', 'Procedure', 'DiagnosticReport',
                           'MedicationRequest', 'CarePlan', 'Condition', 'Immunization',
                           'CareTeam', 'Practitioner', 'Organization', 'Patient', 'Goal' )
;

VACUUM FULL fhirdata.ExplanationOfBenefit_str_values;
VACUUM FULL fhirdata.ExplanationOfBenefit_date_values;
VACUUM FULL fhirdata.ExplanationOfBenefit_logical_resources;
VACUUM FULL fhirdata.ExplanationOfBenefit_resources;
VACUUM FULL fhirdata.ExplanationOfBenefit_number_values;
VACUUM FULL fhirdata.ExplanationOfBenefit_quantity_values;
VACUUM FULL fhirdata.ExplanationOfBenefit_composites;
VACUUM FULL fhirdata.ExplanationOfBenefit_latlng_values;
VACUUM FULL fhirdata.ExplanationOfBenefit_resource_token_refs;

VACUUM FULL fhirdata.Claim_str_values;
VACUUM FULL fhirdata.Claim_date_values;
VACUUM FULL fhirdata.Claim_logical_resources;
VACUUM FULL fhirdata.Claim_resources;
VACUUM FULL fhirdata.Claim_number_values;
VACUUM FULL fhirdata.Claim_quantity_values;
VACUUM FULL fhirdata.Claim_composites;
VACUUM FULL fhirdata.Claim_latlng_values;
VACUUM FULL fhirdata.Claim_resource_token_refs;

VACUUM FULL fhirdata.Observation_str_values;
VACUUM FULL fhirdata.Observation_date_values;
VACUUM FULL fhirdata.Observation_logical_resources;
VACUUM FULL fhirdata.Observation_resources;
VACUUM FULL fhirdata.Observation_number_values;
VACUUM FULL fhirdata.Observation_quantity_values;
VACUUM FULL fhirdata.Observation_composites;
VACUUM FULL fhirdata.Observation_latlng_values;
VACUUM FULL fhirdata.Observation_resource_token_refs;
