-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
SELECT loader_instance_id, substr(object_name, 1, 24) object_name, resource_type, resource_count, resource_count / run_seconds AS resources_per_second,
       timestampdiff(2, bundle_end - bundle_start) bundle_duration
  FROM (
       SELECT lr.loader_instance_id, resource_type_id, rb.object_name, count(*) AS resource_count,
              timestampdiff(2, max(lr.created_tstamp) - min(lr.created_tstamp)) run_seconds,
              min(rb.load_started) bundle_start,
              max(rb.load_completed) bundle_end
         FROM fhirbucket.logical_resources lr,
              fhirbucket.resource_bundles rb
        WHERE lr.loader_instance_id IS NOT NULL
          AND rb.resource_bundle_id = lr.resource_bundle_id
          AND rb.load_completed IS NOT NULL
     GROUP BY lr.loader_instance_id, resource_type_id, rb.object_name
     ) lr,
       resource_types rt
 WHERE rt.resource_type_id = lr.resource_type_id
;

