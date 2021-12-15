## profiles-resources.json

fixed "idendtifier" typo https://jira.hl7.org/browse/FHIR-24935

Replaced \r\r with normal space in definition for "MedicationAdministration.dosage.text"

Removed redundant version from http://hl7.org/fhir/ValueSet/publication-status|4.1.0|4.1.0 in 6 locations

Removed redundant version from http://terminology.hl7.org/ValueSet/v3-Confidentiality|2.0.0|2.0.0 in 2 locations

Removed ClinicalUseIssue (per https://jira.hl7.org/browse/FHIR-31847)

## profiles-types.json

Manually fixed eld-17 to allow for targetProfile on CodeableReference elements (https://jira.hl7.org/browse/FHIR-34462)