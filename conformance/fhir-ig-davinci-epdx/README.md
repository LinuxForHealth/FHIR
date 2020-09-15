The contents in this module are from HL7 FHIR® Da Vinci Payer Data exchange Implementation Guide 0.1.18 - CI Build.

COMMIT https://github.com/HL7/davinci-epdx/commit/a8420e6db30b9831618c36308dfff3f48949a72e

Downloaded from http://build.fhir.org/ig/HL7/davinci-epdx/

Retrieved on 08 SEPT 2020

Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i davinci
```
