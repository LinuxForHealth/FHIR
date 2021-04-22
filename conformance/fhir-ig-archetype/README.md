# Create an Archetype

```
mvn archetype:generate       -DgroupId=com.ibm.fhir        -DartifactId=fhir-ig-archetype        -DarchetypeGroupId=org.apache.maven.archetypes        -DarchetypeArtifactId=maven-archetype-archetype
```

Create an IG from the ig

```
mvn archetype:generate \
    -DgroupId=com.ibm.fhir \
    -DartifactId=fhir-ig-test \
    -DarchetypeGroupId=com.ibm.fhir \
    -DarchetypeArtifactId=fhir-ig-archetype \
    -Dpackage=com.ibm.fhir.ig.us.core \
    -Dfhir-package=package \
    -Dfhir=hl7.us.core
```