DaVinci Payer Data Exchange (PDex) US Drug Formulary STU 1, version 1.0.1

Retrieved from http://build.fhir.org/ig/HL7/davinci-pdex-formulary/ on 30 NOV 2020

Check that the Profiles are on the system: 

```
~$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i davinci-drug-formulary
List,["http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-CoveragePlan|1.0.0"]
MedicationKnowledge,["http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-FormularyDrug|1.0.0"]
```