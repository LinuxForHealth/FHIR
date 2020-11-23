-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

SELECT loader_instance_id, resource_count, 
        resource_count / run_seconds AS resources_per_second
  FROM (
       SELECT bl.loader_instance_id, count(*) AS resource_count,
              EXTRACT(EPOCH FROM max(bl.load_completed) - min(bl.load_started)) run_seconds
         FROM fhirbucket.resource_bundles rb,
              fhirbucket.resource_bundle_loads bl,
              fhirbucket.logical_resources lr
        WHERE lr.resource_bundle_load_id = bl.resource_bundle_load_id
          AND rb.resource_bundle_id = bl.resource_bundle_id
          AND bl.loader_instance_id IS NOT NULL
     GROUP BY bl.loader_instance_id) AS sub
	 ;
