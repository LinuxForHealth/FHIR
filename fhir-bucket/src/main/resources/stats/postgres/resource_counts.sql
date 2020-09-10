select rt.resource_type, count(*) 
from fhirdata.logical_resources lr,
      fhirdata.resource_types rt
where lr.resource_type_id = rt.resource_type_id
group by rt.resource_type
order by 2 DESC;
