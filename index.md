---
# Feel free to add content and custom Front Matter to this file.
# To modify the layout, see https://jekyllrb.com/docs/themes/#overriding-theme-defaults

layout: default
title: IBM Server for HL7 FHIR
---
## IBM Server for HL7 FHIR

### Overview
The IBM Server for HL7 FHIR is a modular Java implementation of version 4 of the [HL7 FHIR specification](https://www.hl7.org/fhir/r4/http.html) with a focus on performance and configurability.  The server can be packaged as a set of jar files, a web application archive (war), an application installer, or a Docker image.

For a detailed description of FHIR conformance, please see [Conformance](https://github.com/ibm/fhir/blob/master/docs/Conformance.md).

### Build Status
The current build status is [![Build Status](https://travis-ci.com/IBM/FHIR.svg?branch=master)](https://travis-ci.com/IBM/FHIR)

### Running the IBM Server for HL7 FHIR
The IBM Server for HL7 FHIR is currently under development. To run the server you must clone or download the project and build it. See [Setting Up for Development](https://github.com/IBM/FHIR/wiki/Setting-up-for-development) for more info.

Information on installing and running the IBM Server for HL7 FHIR will be available in the User Guide at [FHIR Server User Guide](https://github.com/ibm/fhir/blob/master/docs/FHIRServerUsersGuide.md), but presently this document needs love/attention.

### Building on top of the IBM Server for HL7 FHIR
IBM Server for HL7 FHIR artifacts will become available on JCenter and Maven Central with a group ID of `com.ibm.fhir` in the near future.

For example, if you are using Maven and would like to use our object model (including our high-performance parser, generator, and validator), you could declare the dependency like this:

```
<dependencies>
    <dependency>
      <groupId>com.ibm.fhir</groupId>
      <artifactId>fhir-model</artifactId>
      <version>${fhir.version}</version>
    </dependency>
    ...
</dependencies>
```

### Contributing to the IBM Server for HL7 FHIR
See [CONTRIBUTING.md](CONTRIBUTING.md).

### License
The IBM Server for HL7 FHIR is licensed under the Apache 2.0 license. Full license text is
available at [LICENSE](LICENSE).
