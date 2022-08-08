# Da Vinci Health Record Exchange (HRex) - 1.0.0 - STU R1
- https://hl7.org/fhir/us/davinci-hrex/STU1/downloads.html

1. - Modified ig-r4.json to remove the parameters array that contained unsupported values `copyrightyear`, `releaselabel`, and more

2. - Change references targeting us-core to specify version 3.1.1 so that they don't accidentally pick up US Core 4.0.0 conformance artifacts.
    - StructureDefinition-hrex-claimresponse.json
    - StructureDefinition-hrex-consent.json
    - StructureDefinition-hrex-coverage.json
    - StructureDefinition-hrex-task-data-request.json
    - StructureDefinition-hrex-provenance.json
    - StructureDefinition-hrex-practitionerrole.json
    - StructureDefinition-hrex-practitioner.json
    - StructureDefinition-hrex-patient-demographics.json
    - StructureDefinition-hrex-organization.json
    - StructureDefinition-hrex-parameters-member-match-in.json
    - StructureDefinition-extension-itemAuthorizedProvider.json


# Known Issues
WARNING: Slice-specific constraints: [dom-2, dom-4, dom-3, hrex-cov-1, dom-6, dom-5] found on element: Parameters.parameter:CoverageToMatch.resource are not supported
Oct 15, 2021 11:53:57 AM org.linuxforhealth.fhir.profile.ConstraintGenerator generate
WARNING: Constraint was not generated due to the following error: Discriminator not generated (elementDefinition: Parameters.parameter:memberPatient, profile: http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in)
Oct 15, 2021 11:53:57 AM org.linuxforhealth.fhir.profile.ConstraintGenerator generate
WARNING: Constraint was not generated due to the following error: Discriminator not generated (elementDefinition: Parameters.parameter:CoverageToMatch, profile: http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in)
Oct 15, 2021 11:53:57 AM org.linuxforhealth.fhir.profile.ConstraintGenerator generate
WARNING: Constraint was not generated due to the following error: Discriminator not generated (elementDefinition: Parameters.parameter:CoverageToLink, profile: http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in)