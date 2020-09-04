-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------
  SELECT rt.resource_type,
         count(*)
    FROM fhirbucket.resource_types rt,
         fhirbucket.logical_resources lr
   WHERE lr.resource_type_id = rt.resource_type_id
GROUP BY rt.resource_type
ORDER BY rt.resource_type;
