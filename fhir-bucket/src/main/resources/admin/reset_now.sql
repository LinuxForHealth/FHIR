-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- Reset every bundle we currently know about so that each will be picked up
-- and processed again (as though the file had changed).
UPDATE fhirbucket.resource_bundles
   SET loader_instance_id = NULL,
       allocation_id = NULL,
       version = version + 1
 WHERE resource_bundle_id IN (
SELECT rb.resource_bundle_id 
  FROM fhirbucket.resource_bundles rb, 
       fhirbucket.resource_bundle_loads bl
 WHERE bl.allocation_id = rb.allocation_id
   AND bl.loader_instance_id = rb.loader_instance_id
   AND bl.resource_bundle_id = rb.resource_bundle_id
   AND bl.load_completed IS NOT NULL
);
