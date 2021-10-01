The contents in this module are from HL7 FHIR® Da Vinci Health Record Exchange (HRex) 0.2.0 - STU R1 - 2nd ballot.

Downloaded from http://build.fhir.org/ig/HL7/davinci-ehrx/index.html

Retrieved on 01 OCT 2021 https://github.com/HL7/davinci-ehrx/commit/8bc618cafd677393fffc409633d863e753a3cf54

Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i hrex
```