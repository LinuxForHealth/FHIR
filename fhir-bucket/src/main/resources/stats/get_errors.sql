-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
select e.line_number, e.http_status_code, e.http_status_text
  from fhirbucket.resource_bundle_errors e,
       fhirbucket.resource_bundle_loads l,
	   fhirbucket.resource_bundles b
 where e.line_number >= 0
   and l.resource_bundle_load_id = e.resource_bundle_load_id
   and b.resource_bundle_id = l.resource_bundle_id
 fetch first 10 rows only;
