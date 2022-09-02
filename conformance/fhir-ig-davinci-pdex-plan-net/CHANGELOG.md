# Changes 

## PDEX PlanNet 1.0.0
Source - https://build.fhir.org/ig/HL7/davinci-pdex-plan-net
Corresponding to https://github.com/HL7/davinci-pdex-plan-net/commit/2c561128f864e87f50a00a478aa5dbccba6865fc
- Modified CapabilityStatement-plan-net.json to remove <br/> tags between list items in the narrative text (which is invalid XHTML)
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4
- Stripped narrative text to reduce the size and formatted the JSON contents (both via the ResourceProcessor tool)
- Added version id to each targetProfile canonical reference (e.g. `http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Organization` -> `http://hl7.org/fhir/us/davinci-pdex-plan-net/StructureDefinition/plannet-Organization|1.0.0`)
- Added version id to each valueSet binding target (e.g. `http://hl7.org/fhir/us/davinci-pdex-plan-net/ValueSet/InsuranceProductTypeVS` -> `http://hl7.org/fhir/us/davinci-pdex-plan-net/ValueSet/InsuranceProductTypeVS|1.0.0`)
- Added version id to each extension's profile reference in each StructureDefinition
- Added version element to each CodeSystem reference from each ValueSet definition

Note: the examples were subsequently retrieved from http://hl7.org/fhir/us/davinci-pdex-plan-net/STU1/package.tgz on May 19, 2022.
- Added version id to the Meta.profile entry for each example

## PDEX PlanNet 1.1.0
Source - http://hl7.org/fhir/us/davinci-pdex-plan-net/STU1.1/package.tgz retrieved on May 19, 2022.
- Modified CapabilityStatement-plan-net.json to remove <br/> tags between list items in the narrative text (which is invalid XHTML)
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4
- Stripped narrative text to reduce the size and formatted the JSON contents (both via the ResourceProcessor tool)
- Added version id to each targetProfile canonical reference
- Added version id to each valueSet binding target
- Added version id to each extension's profile reference in each StructureDefinition
- Added version element to each CodeSystem reference from each ValueSet definition


# Steps to update
1. download the npm package for whatever version of PDEX PlanNet you want (and note what downloads we used from where in this file)
2. update `src/main/resources` with the latest conformance artifacts and `src/test/resources` with the latest examples
3. if its a new version
   - ensure references from the previous package are version-specific (e.g. to avoid a 1.0.0 profile from picking up a 1.1.0 valueset during validation)
   - add a new provider (`src/main/java` and `src/main/resources/META-INF`)
   - create tests for this new version (update ConstraintGeneratorTest and PlanNetResourceProviderTest)
4. execute ResourceProcessor from src/test/java with the proper arguments
5. execute SearchParameterAugmenter from src/test/java with the proper arguments