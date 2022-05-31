Files downloaded from https://hl7.org/fhir/R4B/downloads.html on 2022-05-31
* https://hl7.org/fhir/R4B/definitions.json.zip
* https://hl7.org/fhir/R4B/expansions.json

## Changes

### valuesets.json

Added required status field to the "CatalogType" CodeSystem and ValueSet

### expansions.json

Removed all incomplete/"too-costly" expansions from the collection.
  - This is done via a new ValueSetExpansionCleaner utility found under fhir-tools.

Select manual fixes to http://hl7.org/fhir/ValueSet/currencies
* "Polish zE\u0002oty" -> "Polish złoty"
  - this is the one that originally drove these changes as `\u0002` is a unicode control char that our validator rejects
* "Paraguayan guaranC-" -> "Paraguayan guaraní"
* "Philippine piso[13]" -> "Philippine peso"
* "Mongolian tC6grC6g" -> "Mongolian tugrik"
* "Renminbi (Chinese) yuan[8]" -> "Renminbi (Chinese) yuan"
* "Unidad de Valor Real (UVR) (funds code)[9]" -> "Unidad de Valor Real (UVR) (funds code)"


## Steps to update

1. update `fhir-core-r4b/definitions` with the latest (and note what downloads we used from where in this file)
   * add `expansions.json` to fhir-tools/definitions, execute ValueSetExpansionCleaner from *fhir-tools*/src/test/java to remove incomplete expansions, and then copy it here
2. review the changes listed above and apply as necessary
2. execute IndexGenerator.java from src/test/java
3. execute ResourceProcessor.java from src/test/java
4. execute SearchParameterAugmenter from *fhir-search*/src/test/java
   * I wanted to move that tool here but it depends on fhir-path and that introduces a circular dependency