# IBM FHIR Server - Demo

This module contains a minimal docker-compose environment to facilitate demonstrations and evaluations from a single-node system (e.g. from your laptop).

### Prerequisites

A functional docker-compose and enough storage (~4GB) and RAM (~4GB) to run the environment.

### Using docker-compose

First, navigate to the `demo` directory.
Then execute the following commands.

To start the services:
```
docker-compose up -d
```

To monitor the services:
```
docker-compose logs -f
```

To stop the services:
```
docker-compose down
```

### Using the IBM FHIR Server

Once the server is running, navigate to http://localhost:9080/openapi/ui to explore the server endpoints.

### Limitations

To speed up the schema deployment and keep the schema size down, this demo environment is limited to the following resource types:
* Patient
* Group
* Practitioner
* PractitionerRole
* Device
* Organization
* Location
* Encounter
* AllergyIntolerance
* Observation
* Condition
* CarePlan
* Provenance
* Medication
* MedicationAdministration
* StructureDefinition
* ElementDefinition
* CodeSystem
* ValueSet

To adjust the list of supported resource types, change the `resourceTypes` property in start command of the fhir-server in docker-compose.yml.
