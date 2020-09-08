This profile HL7 FHIR® Da Vinci Payer Data exchange Implementation Guide 0.1.18 - CI Build.

COMMIT https://github.com/HL7/davinci-epdx/commit/a8420e6db30b9831618c36308dfff3f48949a72e

Downloaded from http://build.fhir.org/ig/HL7/davinci-epdx/

Retrieved on 27 AUG 2020

Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i us-core
AllergyIntolerance,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance|3.1.1"]
CarePlan,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-careplan|3.1.1"]
CareTeam,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-careteam|3.1.1"]
Condition,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-condition|3.1.1"]
Device,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-implantable-device|3.1.1"]
DiagnosticReport,["http://hl7.org/fhir/StructureDefinition/hlaresult|4.0.1","http://hl7.org/fhir/StructureDefinition/lipidprofile|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-lab|3.1.1","http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-note|3.1.1","http://hl7.org/fhir/StructureDefinition/diagnosticreport-genetics|4.0.1"]
DocumentReference,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-documentreference|3.1.1"]
Encounter,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-encounter|3.1.1"]
Goal,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-goal|3.1.1"]
Immunization,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-immunization|3.1.1"]
Location,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-location|3.1.1"]
Medication,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-medication|3.1.1"]
MedicationRequest,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-medicationrequest|3.1.1"]
Observation,["http://hl7.org/fhir/StructureDefinition/triglyceride|4.0.1","http://hl7.org/fhir/StructureDefinition/oxygensat|4.0.1","http://hl7.org/fhir/StructureDefinition/bp|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/us-core-observation-lab|3.1.1","http://hl7.org/fhir/StructureDefinition/devicemetricobservation|4.0.1","http://hl7.org/fhir/StructureDefinition/observation-genetics|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/pediatric-weight-for-height|3.1.1","http://hl7.org/fhir/StructureDefinition/heartrate|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/us-core-smokingstatus|3.1.1","http://hl7.org/fhir/StructureDefinition/ldlcholesterol|4.0.1","http://hl7.org/fhir/StructureDefinition/resprate|4.0.1","http://hl7.org/fhir/StructureDefinition/bodyheight|4.0.1","http://hl7.org/fhir/StructureDefinition/bodytemp|4.0.1","http://hl7.org/fhir/StructureDefinition/cholesterol|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/head-occipital-frontal-circumference-percentile|3.1.1","http://hl7.org/fhir/StructureDefinition/headcircum|4.0.1","http://hl7.org/fhir/StructureDefinition/vitalspanel|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/pediatric-bmi-for-age|3.1.1","http://hl7.org/fhir/StructureDefinition/bodyweight|4.0.1","http://hl7.org/fhir/StructureDefinition/hdlcholesterol|4.0.1","http://hl7.org/fhir/us/core/StructureDefinition/us-core-pulse-oximetry|3.1.1","http://hl7.org/fhir/StructureDefinition/vitalsigns|4.0.1","http://hl7.org/fhir/StructureDefinition/bmi|4.0.1"]
Organization,["http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-organization|0.1.0","http://hl7.org/fhir/us/core/StructureDefinition/us-core-organization|3.1.1"]
Patient,["http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-patient|0.1.0","http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient|3.1.1"]
Practitioner,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitioner|3.1.1"]
PractitionerRole,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitionerrole|3.1.1","http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-practitionerrole|0.1.0"]
Procedure,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-procedure|3.1.1"]
Provenance,["http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance|3.1.1","http://hl7.org/fhir/StructureDefinition/ehrsrle-provenance|4.0.1","http://hl7.org/fhir/StructureDefinition/provenance-relevant-history|4.0.1"]
```



