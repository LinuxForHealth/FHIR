30 NOV 2020 - Initial Load
 - remove invalid <br/> tags from between <li> tags in CapabilityStatement-c4bb.json
 - move invalid <sup> tag into subsequent <p> tag in CapabilityStatement-c4bb.json 
 - removed invalid ImplementationGuide parameters `copyrightyear`, `releaselabel`, and more
 
02 JUN 2021 - service-date fixup
 - changed the FHIRPath expression from 
   "ExplanationOfBenefit.billablePeriod | ExplanationOfBenefit.item.serviced as Date |  ExplanationOfBenefit.item.serviced as Period" to 
   "ExplanationOfBenefit.billablePeriod | ExplanationOfBenefit.item.serviced"
 - see https://jira.hl7.org/browse/FHIR-30443 for more detail