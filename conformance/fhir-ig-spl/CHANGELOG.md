# FHIR SPL Mapping FHIR Implementation Guide - 0.1.0 - Build CI

1 - Removed ig-r4.json from the .index.json as it does not conform / process correctly. 
WARNING: Unable to load resource: hl7/fhir/us/spl/package/ig-r4.json due to the following exception: copyrightyear [ImplementationGuide.definition.parameter[0].code]

2 - Changed # references to Organization/1234 

3 - Updated #usagent to Organization/1234

``` json 
    "organization": {
            "reference": "Organization/1234"
        },
        "participatingOrganization": {
            "reference": "Organization/1234"
        }
```