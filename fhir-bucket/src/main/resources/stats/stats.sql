-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
SET CURRENT SCHEMA FHIRBUCKET;

SELECT loader_instance_id, resource_count, resource_count / run_seconds AS resources_per_second
  FROM (
       SELECT lr.loader_instance_id, count(*) AS resource_count,
              timestampdiff(2, max(rb.load_completed) - min(rb.load_started)) run_seconds
         FROM resource_bundles rb,
              logical_resources lr
        WHERE lr.resource_bundle_id = rb.resource_bundle_id
          AND lr.loader_instance_id IS NOT NULL
     GROUP BY lr.loader_instance_id
)
;

