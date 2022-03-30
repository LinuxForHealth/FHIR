# HL7 FHIR® Da Vinci Health Record Exchange (HRex) 1.0.0 - STU R1 - continuous build

- Downloaded - https://hl7.org/fhir/us/davinci-hrex/STU1/package.tgz
- Retrieved - March 29, 2022

# *Notes*

1. The IG states a dependency on [UDAP Security IG](http://build.fhir.org/ig/HL7/davinci-ehrx/index.html#dependencies). This implementation treats the UDAP dependency as a SOFT dependency.

# Check for HREX Profile
Check the Profiles are on the system: 

```
$ curl -ks -u fhiruser:change-password https://localhost:9443/fhir-server/api/v4/metadata 2>&1 | jq -r '.rest[].resource[] | "\(.type),\(.supportedProfile)"' | grep -i hrex
```