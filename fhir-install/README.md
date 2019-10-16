# IBM FHIR Server Install

Running IBM FHIR Server as a Docker container.

## Prerequisites

- [Docker]
- Updated fhir-install/target/fhir-server-distribution.zip

## Build

Using Docker Terminal, access the fhir-install directory and run:

```sh
docker build -t fhir-server . --squash
```
## Run
 
Once the image is built, start it with:

```sh
docker run -it -p 9080:9080 -p 9443:9443 --name fhir-server --rm fhir-server
```

## Test

Once the fhir-server is ready, you can test it accessing: http://localhost:9080/fhir-server/api/v4/metadata.

If you are using Docker for Mac, instead of using localhost you should use your Docker VM IP: http://192.168.99.100:9080/fhir-server/api/v4/metadata.

The test should result in something like:

```xml
<Conformance xmlns="http://hl7.org/fhir" xmlns:xhtml="http://www.w3.org/1999/xhtml">
  <version value="0.0.1-SNAPSHOT"/>
  <name value="IBM FHIR Server"/>
  <publisher value="IBM Corporation"/>
  <date value="Thu May 19 16:51:53 UTC 2016"/>
  <description value="IBM FHIR Server version 0.0.1-SNAPSHOT build id development"/>
  <copyright value="© Copyright IBM Corporation 2019"/>
  <kind value="instance"/>
  <software id="development">
    <name value="IBM FHIR Server"/>
    <version value="0.0.1-SNAPSHOT"/>
  </software>
  <fhirVersion value="1.0.2"/>
  <format value="application/json"/>
  <format value="application/fhir+json"/>
  <format value="application/xml"/>
  <format value="application/fhir+xml"/>
</Conformance>
```

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.

[Docker]: <http://docker.com>
