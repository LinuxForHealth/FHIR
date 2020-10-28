-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

     SELECT lr.logical_id
       FROM fhirbucket.logical_resources lr, 
           (SELECT sub.resource_type_id, round(random() * (sub.max_id - sub.min_id)) + sub.min_id AS pick_id 
              FROM (SELECT lr.resource_type_id, MIN(logical_resource_id) min_id, MAX(logical_resource_id) max_id
                      FROM fhirbucket.logical_resources lr,
                           fhirbucket.resource_types rt
                     WHERE lr.resource_type_id = rt.resource_type_id 
                       AND rt.resource_type = 'Patient'
                  GROUP BY lr.resource_type_id) AS sub) AS pick  
      WHERE lr.logical_resource_id >= pick.pick_id
        AND lr.resource_type_id = pick.resource_type_id 
FETCH FIRST 1 ROWS ONLY;
