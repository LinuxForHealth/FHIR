# LinuxForHealth FHIR® Server Install

Running the LinuxForHealth FHIR® Server as a Docker container.

## Prerequisites

- [Docker](http://docker.com)
- Updated fhir-install/target/fhir-server-distribution.zip

## Build

Using Docker Terminal, access the fhir-install directory and run:

```sh
docker build -t fhir-server .
```

## Run

Once the image is built, start it with:

```sh
docker run -it -p 9443:9443 --name fhir-server --rm fhir-server
```

## Test

Once the fhir-server is ready, you can test it by accessing: https://localhost:9443/fhir-server/api/v4/$healthcheck

For example:

```sh
curl -k -i -u 'fhiruser:change-password' 'https://localhost:9443/fhir-server/api/v4/$healthcheck'
```

This request makes a connection to the configured database (embedded Derby by default) and a successful response will return with:

```json
{
    "resourceType": "OperationOutcome",
    "issue": [
        {
            "severity": "information",
            "code": "informational",
            "details": {
                "text": "All OK"
            }
        }
    ]
}
```

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
