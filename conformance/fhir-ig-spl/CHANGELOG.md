# FHIR SPL Mapping FHIR Implementation Guide - 0.1.0 - Build CI
Downloaded from https://build.fhir.org/ig/HL7/fhir-spl/branches/main/package.tgz on 2022-06-13.

1 - Removed parameter element in ig-r4.json and ImplementationGuide-hl7.fhir.us.spl.json as it does not conform / process correctly. 
WARNING: Unable to load resource: hl7/fhir/us/spl/package/ig-r4.json due to the following exception: copyrightyear [ImplementationGuide.definition.parameter[0].code]


## Examples
Performed the following edits to the narrative text in Bundle-AllopurinolTabletLabelBundle.json and Composition-AllopurinolTabletLabelComposition.json
- replace &nbsp; with &#160;
- remove misplaced <caption> element

