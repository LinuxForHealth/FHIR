Source - https://build.fhir.org/ig/HL7/davinci-pdex-plan-net
Corresponding to https://github.com/HL7/davinci-pdex-plan-net/commit/2c561128f864e87f50a00a478aa5dbccba6865fc
- Modified CapabilityStatement-plan-net.json to remove <br/> tags between list items in the narrative text (which is invalid XHTML)
- Modified ig-r4.json to remove parameters that aren't valid in FHIR R4