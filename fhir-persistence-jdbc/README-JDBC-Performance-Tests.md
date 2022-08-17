# IBM FHIR Server - fhir-persistence-jdbc performance tests
Performance tests for derby embedded, derby network and postgresql.
fhir-persistence-jdbc/src/test/java/org/linuxforhealth/fhir/persistence/jdbc/test/spec/Main.java

# Run Configurations

## Performance test for derby embedded

```
--derby
--index PERFORMANCE_JSON
--validate
--threads 4
--read-iterations 2
```

## Performance test for derby network

```
--prop-file ./src/test/resources/test.jdbc-perform.properties
--derbynetwork
--index PERFORMANCE_JSON
--validate
--threads 4
--read-iterations 2
--schema-name APP
```

## Performance test for postgresql

```
--prop-file ./src/test/resources/test.jdbc-perform.properties
--postgresql
--index PERFORMANCE_JSON
--validate
--threads 4
--read-iterations 1
--schema-name fhirdata
```

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
