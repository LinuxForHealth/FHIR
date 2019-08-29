## IBM Watson Health FHIR Server

### Overview
The Watson Health FHIR Server is a modular Java implementation of version 4 of the [HL7 FHIR specification](https://www.hl7.org/fhir/r4/http.html) with a focus on performance and configurability.

For a detailed description of FHIR conformance, please see https://github.com/ibm/fhir/blob/master/docs/Conformance.md.

The server is packaged as a set of jar files, a web application archive (war), an application installer, and a Docker image.

### Running the Watson Health FHIR Server
For information on installing and running the Watson Health FHIR Server, please see the User Guide at https://github.com/ibm/fhir/blob/master/docs/FHIRServerUsersGuide.md.

### Building on top of the Watson Health FHIR Server
Watson Health FHIR Server artifacts are available on JCenter and Maven Central with a group ID of `com.ibm.watson.health`.

For example, if you are using Maven and would like to use our object model (including our high-performance parser, generator, and validator), you could declare the dependency like this:

```
...
<dependencies>
    <dependency>
      <groupId>com.ibm.watson.health</groupId>
      <artifactId>fhir-model</artifactId>
      <version>${fhir.version}</version>
    </dependency>
    ...
```

### Contributing to the Watson Health FHIR Server
See [CONTRIBUTING.md](CONTRIBUTING.md).

### License
The Watson Health FHIR Server is licensed under the Apache 2.0 license. Full license text is
available at [LICENSE](LICENSE).
