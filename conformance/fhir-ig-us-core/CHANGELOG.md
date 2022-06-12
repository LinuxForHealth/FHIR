# Changes 
## US Core 3.1.1 (Version 2) - STU311
Source - http://hl7.org/fhir/us/core/STU3.1.1/
- Removed 3.1.0 Artifacts
- Updated to 3.1.1 Artifacts STU3.1.1
- Examples are under src/test/resources/JSON/311
- Replace all narrative text with minimal placeholder for space efficiency
- Per https://github.com/IBM/FHIR/issues/1460 Relaxed required to extensible
    - https://jira.hl7.org/browse/FHIR-27911 Change binding to UCUM from required to extensible + max binding UCUM
    - Per https://chat.fhir.org/#narrow/stream/179252-IG-creation/topic/US.20Core.20QA.20Issue.20.233-.20nasty.20profiling.20error
    - Changed Required to extensible for 
        - "id": "Observation.component:FlowRate.value[x]",
        - "id": "Observation.component:Concentration.code",
- Modify MedicationRequest-uscore-mo2.json and MedicationRequest-uscore-mo2.xml to remove invalid XHTML
   - Any <p> (or other block-level element) will implicitly close any open <p>.
   - https://www.w3.org/TR/html401/struct/text.html#h-9.3.1
   - The P element represents a paragraph. It cannot contain block-level elements (including P itself).
   - Refer to https://jira.hl7.org/browse/FHIR-28409 Invalid XHTML in Examples MedicationRequest-uscore-mo2 XML/JSON
- Modify DiagnosticReport-cardiology-report JSON and XML samples contain invalid hashes https://jira.hl7.org/browse/FHIR-28408
- Add implicit-system extensions to applicable token SearchParameter definitions (performance optimization for https://github.com/IBM/FHIR/issues/1929)
- Updated Examples to point to 3.1.1 profile
- Update the StructureDefinitions and other artifacts for 3.1.1 internal references with target profiles and valueset bindings.

## US Core 4.0.0 - STU4
Source - https://www.hl7.org/fhir/us/core/stu4/
- Examples are under src/test/resources/JSON/400
- Replace all narrative text with minimal placeholder for space efficiency
- Revised Endpoint in the Practitioner endpoint so it points to relative path, not absolute path
- Move provenance-1 constraint from Provenance.agent.onBehalfOf to Provenance.agent and fix the expression (https://jira.hl7.org/browse/FHIR-36328)
- Fix Condition constraint us-core-1 expression (https://jira.hl7.org/browse/FHIR-36336) 
- Replace the packaged USPS valueset with an expanded version obtained from https://tx.fhir.org/r4/ValueSet/$expand?url=http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state on 2022-03-28
- Added version id to each targetProfile canonical reference (e.g. `http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient` -> `http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient|4.0.0`)
- Added version id to each valueSet binding target (e.g. `http://hl7.org/fhir/us/core/ValueSet/us-core-vital-signs` -> `http://hl7.org/fhir/us/core/ValueSet/us-core-vital-signs|4.0.0`)


## US Core 5.0.0 - STU5
Source - https://hl7.org/fhir/us/core/stu5/
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4
- Replace &reg; with ® in the following examples:
  - Procedure-defib-implant.json
  - Encounter-example-1.json


# Steps to update
1. download the npm package for whatever version of Us Core you want (and note what downloads we used from where in this file)
2. update `src/main/resources` with the latest conformance artifacts and `src/test/resources` with the latest examples
3. if its a new version
  - ensure references from the previous package are version-specific (e.g. to avoid a 3.1.1 profile from picking up a 5.0.0 valueset during validation)
  - add a new provider (`src/main/java` and `src/main/resources/META-INF`)
  - create tests for this new version (copy tests from the existing versions into a new package for this version)
3. update the http://hl7.org/fhir/us/core/ValueSet-us-core-usps-state.html valueset from https://tx.fhir.org (if needed)
  - https://tx.fhir.org/r4/ValueSet/$expand?url=http://hl7.org/fhir/us/core/ValueSet/us-core-usps-state
4. temporarily add your UMLS API Key to VSACRegistryResourceProvider so that we can expand the value sets using the VSAC terminology server
5. execute ResourceProcessor from src/test/java with the proper arguments
6. execute SearchParameterAugmenter from src/test/java with the proper arguments