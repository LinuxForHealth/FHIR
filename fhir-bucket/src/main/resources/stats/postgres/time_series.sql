-------------------------------------------------------------------------------
-- (C) Copyright IBM Corp. 2020
--
-- SPDX-License-Identifier: Apache-2.0
-------------------------------------------------------------------------------

-- demonstrates use of a cartesian product to generate a time series which can
-- be useful to drive performance analysis queries.

SELECT day.hour, hour.second
  FROM (VALUES (0),(900),(1800),(2700)) AS hour(second),
       (VALUES (0),(1),(2),(3),(4),(5),(6),
               (7),(8),(9),(10),(11),(12),
               (13),(14),(15),(16),(17),(18),
               (19),(20),(21),(22),(23)) AS day(hour)
ORDER BY day.hour, hour.second
;
