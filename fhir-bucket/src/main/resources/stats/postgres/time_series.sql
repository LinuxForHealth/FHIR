SELECT day.hour, hour.second
  FROM (VALUES (0),(900),(1800),(2700)) AS hour(second),
       (VALUES (0),(1),(2),(3),(4),(5),(6),
               (7),(8),(9),(10),(11),(12),
               (13),(14),(15),(16),(17),(18),
               (19),(20),(21),(22),(23)) AS day(hour)
ORDER BY day.hour, hour.second
;
