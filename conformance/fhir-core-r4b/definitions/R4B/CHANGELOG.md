All files downloaded from https://build.fhir.org/branches/R4B/definitions.json.zip on 2021-12-08

## Steps to update

1. update `fhir-core-r4b/definitions` with the latest (and note what downloads we used from where in this file)
2. execute IndexGenerator.java from src/test/java
3. execute ResourceProcessor.java from src/test/java
4. execute SearchParameterAugmenter from *fhir-search*/src/test/java
   * I wanted to move that tool here but it depends on fhir-path and that introduces a circular dependency

## Changes

### valuesets.json

Added required status field to the following:
- StandardsStatus CodeSystem and ValueSet
- ConceptSubsumptionOutcome CodeSystem and ValueSet
- ResourceSecurityCategory CodeSystem and ValueSet
- ResourceValidationMode ValueSet
- Observation-statistics ValueSet
https://chat.fhir.org/#narrow/stream/179166-implementers/topic/R4B.20missing.20valueset.3F/near/264231411

Removed invalid ConceptMap at [Bundle.entry[489].resource.contained[0].group[0].element[0].target[0]]
This ConceptMap was embedded within http://hl7.org/fhir/ValueSet/nhin-purposeofuse
java.lang.IllegalArgumentException: Unrecognized element: 'relationship'