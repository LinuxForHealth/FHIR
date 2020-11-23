-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

UPDATE resource_bundles
   SET loader_instance_id = NULL,
       allocation_id = NULL,
       version = version + 1
 WHERE resource_bundle_id IN (
SELECT rb.resource_bundle_id 
  FROM resource_bundles rb, 
       resource_bundle_loads bl
 WHERE bl.allocation_id = rb.allocation_id
   AND bl.loader_instance_id = rb.loader_instance_id
   AND bl.resource_bundle_id = rb.resource_bundle_id
   AND bl.load_completed IS NOT NULL
   AND EXTRACT(EPOCH FROM CURRENT_TIMESTAMP - bl.load_completed) >= 3600
);
