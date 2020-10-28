-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
select count(*), min(lr.created_tstamp), max(lr.created_tstamp),
        EXTRACT(EPOCH FROM max(lr.created_tstamp) - min(lr.created_tstamp)) run_seconds
from fhirbucket.logical_resources lr,
fhirbucket.resource_bundle_loads bl
where bl.resource_bundle_load_id = lr.resource_bundle_load_id
  and bl.loader_instance_id = ?;


SELECT loader_instance_id, substr(object_name, 1, 24) object_name, resource_type, resource_count, resource_count / run_seconds AS resources_per_second,
       EXTRACT(EPOCH FROM bundle_end - bundle_start) bundle_duration
  FROM (
       SELECT bl.loader_instance_id, lr.resource_type_id, rb.object_name, count(*) AS resource_count,
              EXTRACT(EPOCH FROM max(lr.created_tstamp) - min(lr.created_tstamp)) run_seconds,
              min(bl.load_started) bundle_start,
              max(bl.load_completed) bundle_end
         FROM fhirbucket.logical_resources lr,
              fhirbucket.resource_bundle_loads bl,
              fhirbucket.resource_bundles rb
        WHERE bl.load_completed IS NOT NULL
          AND bl.resource_bundle_id = rb.resource_bundle_id
          AND lr.resource_bundle_load_id = bl.resource_bundle_load_id
     GROUP BY bl.loader_instance_id, lr.resource_type_id, rb.object_name
     ) lr,
       fhirbucket.resource_types rt
 WHERE rt.resource_type_id = lr.resource_type_id
   AND run_seconds > 0
;

