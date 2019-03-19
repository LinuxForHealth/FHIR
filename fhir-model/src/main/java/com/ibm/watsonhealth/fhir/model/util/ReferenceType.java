/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.util;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.watsonhealth.fhir.model.Reference;

public enum ReferenceType {
    
    INTERNAL_CONTAINED,
    EXTERNAL_ABSOLUTE_FHIR_URL,
    EXTERNAL_ABSOLUTE_OTHER_URL,
    EXTERNAL_ABSOLUTE_OID,
    EXTERNAL_ABSOLUTE_UUID,
    EXTERNAL_ABSOLUTE_OTHER,
    EXTERNAL_RELATIVE_FHIR_URL,
    EXTERNAL_INVALID,
    NO_REFERENCE_VALUE;
    
    /**
     * https://www.hl7.org/fhir/dstu2/references.html#regex
     */
    private static final Pattern REST_PATTERN = Pattern.compile("((http|https)://([A-Za-z0-9\\\\\\/\\.\\:\\%\\$])*)?(Account|AllergyIntolerance|Appointment|AppointmentResponse|AuditEvent|Basic|Binary|BodySite|Bundle|CarePlan|Claim|ClaimResponse|ClinicalImpression|Communication|CommunicationRequest|Composition|ConceptMap|Condition|Conformance|Contract|Coverage|DataElement|DetectedIssue|Device|DeviceComponent|DeviceMetric|DeviceUseRequest|DeviceUseStatement|DiagnosticOrder|DiagnosticReport|DocumentManifest|DocumentReference|EligibilityRequest|EligibilityResponse|Encounter|EnrollmentRequest|EnrollmentResponse|EpisodeOfCare|ExplanationOfBenefit|FamilyMemberHistory|Flag|Goal|Group|HealthcareService|ImagingObjectSelection|ImagingStudy|Immunization|ImmunizationRecommendation|ImplementationGuide|List|Location|Media|Medication|MedicationAdministration|MedicationDispense|MedicationOrder|MedicationStatement|MessageHeader|NamingSystem|NutritionOrder|Observation|OperationDefinition|OperationOutcome|Order|OrderResponse|Organization|Patient|PaymentNotice|PaymentReconciliation|Person|Practitioner|Procedure|ProcedureRequest|ProcessRequest|ProcessResponse|Provenance|Questionnaire|QuestionnaireResponse|ReferralRequest|RelatedPerson|RiskAssessment|Schedule|SearchParameter|Slot|Specimen|StructureDefinition|Subscription|Substance|SupplyDelivery|SupplyRequest|TestScript|ValueSet|VisionPrescription)\\/[A-Za-z0-9\\-\\.]{1,64}(\\/_history\\/[A-Za-z0-9\\-\\.]{1,64})?");
    
    
    public static ReferenceType of(Reference ref) {
        if (ref == null || ref.getReference() == null || ref.getReference().getValue() == null) {
            return ReferenceType.NO_REFERENCE_VALUE;
        }
        String referenceUriString = ref.getReference().getValue();
        if (referenceUriString.startsWith("#")) {
            return INTERNAL_CONTAINED;
        }
        
        URI referenceUri;
        try {
            referenceUri = new URI(referenceUriString);
        } catch (URISyntaxException e) {
            return EXTERNAL_INVALID;
        }
        
        Matcher restUrlMatcher = REST_PATTERN.matcher(referenceUriString);
        boolean isFhirUrl = restUrlMatcher.matches();
        
        if (!referenceUri.isAbsolute()) {
            if (isFhirUrl) {
                return EXTERNAL_RELATIVE_FHIR_URL;
            } else {
                return EXTERNAL_INVALID;
            }
        }
        
        if (isFhirUrl) {
            return EXTERNAL_ABSOLUTE_FHIR_URL;
        } else if (referenceUriString.startsWith("http://") || referenceUriString.startsWith("https://")){
            return EXTERNAL_ABSOLUTE_OTHER_URL;
        } else if (referenceUriString.startsWith("urn:oid:")){
            return EXTERNAL_ABSOLUTE_OID;
        } else if (referenceUriString.startsWith("urn:uuid:")) {
            return EXTERNAL_ABSOLUTE_UUID;
        } else {
            return EXTERNAL_ABSOLUTE_OTHER;
        }
    }
}
