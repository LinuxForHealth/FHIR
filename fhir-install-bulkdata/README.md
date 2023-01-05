# LinuxForHealth FHIR Server Install

Running the LinuxForHealth FHIR Bulkdata Server as a Docker container.

## Prerequisites

- [Docker](http://docker.com)
- Updated fhir-install-bulkdata/target/fhir-bulkdata-server-distribution.zip

## Build

Using Docker Terminal, access the fhir-install-bulkdata directory and run:

```sh
docker build -t fhir-bulkdata-server .
```

## Run

Once the image is built, start it with:

```sh
docker run -it -p 9445:9445 --name fhir-bulkdata-server --rm fhir-bulkdata-server
```

## Test

Once the fhir-server is ready, you can test it by accessing: https://localhost:9444/fhir-bulkdata-server/api/v4/healthcheck

For example:

```sh
curl -k -i -u 'fhiruser:change-password' 'https://localhost:9445/fhir-bulkdata-server/api/v4/healthcheck'
```

This request makes a connection to the configured database (embedded Derby by default) and a successful response will return with:

```
HTTP/2 200 
date: #current_date#
content-length: 0
content-language: en-IN
```

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
