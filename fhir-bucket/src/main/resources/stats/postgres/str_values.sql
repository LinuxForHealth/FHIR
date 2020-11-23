-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
select * from fhirdata.observation_str_values 
where logical_resource_id = 170200592
fetch first 20 rows only;

select max(logical_resource_id) from fhirdata.observation_str_values;

select count(*) from observation_str_values where str_value = 'Patient/1746b099b1c-4b2f2cd7-12eb-457e-9e90-4dfb935959b9'
