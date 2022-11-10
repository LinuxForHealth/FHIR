# Changes 

## PDEX US Drug Formulary 1.0.1
Source - https://build.fhir.org/ig/HL7/davinci-pdex-formulary retrieved on Nov 30, 2020.
- Added missing / to the CapabilityStatement.url (https://jira.hl7.org/browse/FHIR-29936)
- Updated the searchParam.definition for List-identifier to properly reference a core spec parameter (https://jira.hl7.org/browse/FHIR-29937)
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4
- Stripped narrative text to reduce the size and formatted the JSON contents (both via the ResourceProcessor tool)
- Added version id to extension definition canonical references in StructureDefinitions (e.g. `http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-Network-extension` -> `http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-Network-extension|1.0.1`)
- Added version id to ValueSet binding targets in StructureDefinitions (e.g. `http://hl7.org/fhir/us/davinci-drug-formulary/ValueSet/DrugTierVS` -> `http://hl7.org/fhir/us/davinci-drug-formulary/ValueSet/DrugTierVS|1.0.1`)
- Added version element under ValueSets.compose.include to specify that these valuesets refer to the 1.0.1 codes.
- Added version id to each extension's profile reference in each StructureDefinition

Note: the examples were subsequently retrieved from http://hl7.org/fhir/us/davinci-drug-formulary/STU1.0.1/package.tgz on May 20, 2022.
- Added version id to the Meta.profile entry for each example

## PDEX US Drug Formulary 1.1.0
Source - http://hl7.org/fhir/us/davinci-drug-formulary/STU1.1 retrieved on May 20, 2022.
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4
- Stripped narrative text to reduce the size and formatted the JSON contents (both via the ResourceProcessor tool)

## PDEX US Drug Formulary 2.0.0
Source - http://hl7.org/fhir/us/davinci-drug-formulary/STU2/ retrieved on September 05, 2022.
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4
- Stripped narrative text to reduce the size and formatted the JSON contents (both via the ResourceProcessor tool)
- Fixed the code for SearchParameter-Basic-subject.json (per https://jira.hl7.org/browse/FHIR-39401)

# Steps to update
1. download the npm package for whatever version of PDEX US Drug Formulary you want (and note what downloads we used from where in this file)
2. update `src/main/resources` with the latest conformance artifacts and `src/test/resources` with the latest examples
3. if its a new version
   - ensure references from the previous package are version-specific (e.g. to avoid a 1.0.1 profile from picking up a 1.1.0 valueset during validation)
      - Add version id to each targetProfile canonical reference
      - Add version id to each valueSet binding target
      - Add version id to each extension's profile reference in each StructureDefinition
      - Add version element to each CodeSystem reference from each ValueSet definition
   - add a new provider (`src/main/java` and `src/main/resources/META-INF`)
   - create tests for this new version (update ConstraintGeneratorTest and FormularyResourceProviderTest)
4. execute ResourceProcessor from src/test/java with the proper arguments
5. execute SearchParameterAugmenter from src/test/java with the proper arguments