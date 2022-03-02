The contents in this module are from HL7 FHIR® Da Vinci Payer Data exchange Implementation Guide version 2.0.0-ballot - CI Build.

COMMIT https://github.com/HL7/davinci-epdx/commit/4cad24b1f278f9887c7076049248e43d04002fb1

Downloaded from http://build.fhir.org/ig/HL7/davinci-epdx/

Retrieved on 03 MAR 2022

Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i davinci
```
