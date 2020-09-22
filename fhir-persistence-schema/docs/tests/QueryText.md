# Scratch Area for Query Development

## Upsert for Ghost Logical Resource
```
INSERT INTO logical_resources (logical_resource_id, resource_type_id, logical_id) 
     SELECT NEXT VALUE FOR APP.fhir_sequence,
            src.resource_type_id, 
            src.logical_id   
       FROM (SELECT rt.resource_type_id, CAST(? AS VARCHAR(255)) AS logical_id           
               FROM resource_types rt          
              WHERE rt.resource_type = ? ) AS src 
         LEFT OUTER JOIN logical_resources lr              
                      ON (lr.resource_type_id = src.resource_type_id             
                     AND lr.logical_id = src.logical_id)  
      WHERE lr.logical_id IS NULL;
```


## Upsert for external_systems
```
INSERT INTO external_systems (external_system_name)      
     SELECT v.name 
       FROM (VALUES (CAST(? AS VARCHAR(255))), (CAST(? AS VARCHAR(255))) ) AS v(name)  
 LEFT OUTER JOIN external_systems s
              ON (s.external_system_name = v.name)
      WHERE s.external_system_name IS NULL
```

