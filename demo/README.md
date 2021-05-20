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

### Adding implementation guides
The IBM FHIR Server can be extended with conformance resources from FHIR implementation guides as described at https://ibm.github.io/FHIR/guides/FHIRValidationGuide.

If the IBM FHIR Server team has already packaged the desired implementation guide, you can use the following command to use maven to download the latest version of this IG jar to the mounted userlib directory:
```
mvn dependency:copy -DoutputDirectory=fhir/userlib -Dartifact=com.ibm.fhir:fhir-ig-us-core:LATEST
```

### Limitations
To speed up the schema deployment and keep the schema size down, this demo environment is limited to the following resource types:
* AllergyIntolerance
* CarePlan
* CareTeam
* CodeSystem
* Condition
* Coverage
* Device
* DiagnosticReport
* DocumentReference
* Encounter
* ExplanationOfBenefit
* Goal
* Group
* Immunization
* List
* Location
* Medication
* MedicationAdministration
* MedicationDispense
* MedicationRequest
* Observation
* Organization
* Patient
* Practitioner
* PractitionerRole
* Procedure
* Provenance
* StructureDefinition
* ValueSet

To adjust the list of supported resource types, change the `resourceTypes` property in start command of the fhir-server in docker-compose.yml. To support all resource types, you can omit the resourceTypes property entirely.

In addition, the set of supported resource endpoints must be adjusted in the server config at `fhir/config/default/fhir-server-config.json`. Add entries for each resource type you wish to support, or set `fhirServer/resources/open` to `true` to support all resource types. See the [User's Guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#412-fhir-rest-api) for more information.
