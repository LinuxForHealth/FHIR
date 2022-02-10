# HL7 FHIR® Da Vinci Health Record Exchange (HRex) 0.2.0 - STU R1 - 2nd ballot

- Downloaded - http://build.fhir.org/ig/HL7/davinci-ehrx/index.html
- Retrieved - 9 FEB 2022
- Commit - https://github.com/HL7/davinci-ehrx/commit/ffb76646a1044736b71d86d17252cc799ff5153d

Please note STU1 is NOT yet completed the balloting process at HL7. The IG may change.

# *Notes*

1. The IG states a dependency on [UDAP Security IG](http://build.fhir.org/ig/HL7/davinci-ehrx/index.html#dependencies). This implementation treats the UDAP dependency as a SOFT dependency.

# Check for HREX Profile
Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i hrex
```