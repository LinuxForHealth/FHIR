The contents in this module are from HL7 FHIR® Da Vinci Health Record Exchange (HRex) 0.2.0 - STU R1 - 2nd ballot.

Downloaded from http://hl7.org/fhir/us/davinci-hrex/2020Sep/

Retrieved on 08 SEPT 2020

Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i hrex
Coverage,["http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage|0.2.0","http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-coverage|0.1.0"]
Organization,["http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Network|0.3.0","http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-organization|0.1.0","http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Organization|0.3.0","http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-organization|0.2.0","http://hl7.org/fhir/us/core/StructureDefinition/us-core-organization|3.1.1"]
Parameters,["http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-out|0.2.0","http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in|0.2.0"]
Practitioner,["http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitioner|0.2.0","http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitioner|3.1.1","http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Practitioner|0.3.0"]
PractitionerRole,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitionerrole|3.1.1","http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitionerrole|0.2.0","http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-PractitionerRole|0.3.0","http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-practitionerrole|0.1.0"]
Provenance,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance|3.1.1","http://hl7.org/fhir/StructureDefinition/ehrsrle-provenance|4.0.1","http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-provenance|0.2.0","http://hl7.org/fhir/StructureDefinition/provenance-relevant-history|4.0.1"]
Task,["http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-task-data-request|0.2.0"]
```