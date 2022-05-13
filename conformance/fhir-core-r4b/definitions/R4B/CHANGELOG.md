Files downloaded from https://build.fhir.org/branches/R4B/downloads.html on 2022-05-11
* https://build.fhir.org/branches/R4B/definitions.json.zip
* https://build.fhir.org/branches/R4B/expansions.json

## Changes

### valuesets.json

Added required status field to the "CatalogType" CodeSystem and ValueSet

### expansions.json

Removed the following very long expansions from the collection due to designations with missing values:
* http://hl7.org/fhir/ValueSet/questionnaire-questions
* http://hl7.org/fhir/ValueSet/report-codes
* http://hl7.org/fhir/ValueSet/consent-content-code
* http://hl7.org/fhir/ValueSet/observation-codes
* http://hl7.org/fhir/ValueSet/doc-typecodes

Modified one instance of illegal control char \u0002: "Polish zE\u0002oty" -> "Polish złoty"

## Steps to update

1. update `fhir-core-r4b/definitions` with the latest (and note what downloads we used from where in this file)
2. review the changes listed above and apply as necessary
2. execute IndexGenerator.java from src/test/java
3. execute ResourceProcessor.java from src/test/java
4. execute SearchParameterAugmenter from *fhir-search*/src/test/java
   * I wanted to move that tool here but it depends on fhir-path and that introduces a circular dependency