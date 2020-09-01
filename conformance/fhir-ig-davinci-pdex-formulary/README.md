This profile DaVinci Payer Data Exchange (PDex) US Drug Formulary STU 1

```
2020-01-21  1.0.0   4.0.1   Payer Data Exchange (PDex) Drug Formulary, Release 1 - US Realm STU 1
```

Retrieved on 26 AUG 2020

Link to the Source - http://hl7.org/fhir/us/davinci-drug-formulary/downloads.html

Check the Profiles are on the system: 

```
~$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i davi
List,["http://hl7.org/fhir/us/Davinci-drug-formulary/StructureDefinition/usdf-CoveragePlan|1.0.0"]
MedicationKnowledge,["http://hl7.org/fhir/us/Davinci-drug-formulary/StructureDefinition/usdf-FormularyDrug|1.0.0"]
```