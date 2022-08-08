/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.search.parameters;

import static org.linuxforhealth.fhir.core.ResourceType.LOCATION;
import static org.linuxforhealth.fhir.core.ResourceType.MEDICATION;
import static org.linuxforhealth.fhir.core.ResourceType.ORGANIZATION;
import static org.linuxforhealth.fhir.core.ResourceType.PRACTITIONER;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.search.compartment.CompartmentHelper;
import org.linuxforhealth.fhir.search.util.SearchHelper;

public class IncludesUtilTest {
    private static boolean DEBUG = false;

    @Test
    void testPatientCompartmentIncludes() throws Exception {
        CompartmentHelper compartmentHelper = new CompartmentHelper();
        SearchHelper searchHelper = new SearchHelper();
        for (String resourceType : compartmentHelper.getCompartmentResourceTypes("Patient")) {
            Map<ResourceType,List<String>> oldSearchCodesByType = oldGetSearchCodesByType(resourceType);
            Map<ResourceType,List<String>> newSearchCodesByType = IncludesUtil.getSearchCodesByType(resourceType, searchHelper);

            for (ResourceType targetType : List.of(LOCATION, MEDICATION, PRACTITIONER, ORGANIZATION)) {
                List<String> oldList = oldSearchCodesByType.get(targetType);
                List<String> newList = newSearchCodesByType.get(targetType);

                if (DEBUG && (oldList == null || oldList.size() != newList.size())) {
                    System.out.println(resourceType + " to " + targetType.value());
                    System.out.println("old: " + oldList);
                    System.out.println("new: " + newList);
                    System.out.println();
                }

                // the new list is expected to contain some search parameters that the old one was missing
                // because the old list was gathered manually and did not include parameters that target "Any"
                if (oldList != null) {
                    assertTrue(newList.containsAll(oldList));
                }
            }
        }
    }


    /**
     * The old implementation that was manually computed.
     */
    private Map<ResourceType, List<String>> oldGetSearchCodesByType(String compartmentMemberType) {
        Map<ResourceType, List<String>> codesByType = new HashMap<>();
        if (compartmentMemberType.equals(ResourceType.ADVERSE_EVENT.value())) {
            addLocationCodes(codesByType, "location");
            addPractitionerCodes(codesByType, "recorder", "subject");
            addMedicationCodes(codesByType, "substance");
        } else if (compartmentMemberType.equals(ResourceType.ALLERGY_INTOLERANCE.value())) {
            addPractitionerCodes(codesByType, "recorder", "asserter");
        } else if (compartmentMemberType.equals(ResourceType.APPOINTMENT.value())) {
            addLocationCodes(codesByType, "location");
            addPractitionerCodes(codesByType, "practitioner");
        } else if (compartmentMemberType.equals(ResourceType.APPOINTMENT_RESPONSE.value())) {
            addLocationCodes(codesByType, "actor", "location");
            addPractitionerCodes(codesByType, "actor", "practitioner");
        } else if (compartmentMemberType.equals(ResourceType.ACCOUNT.value())) {
            addOrganizationCodes(codesByType, "owner", "subject");
            addLocationCodes(codesByType, "subject");
            addPractitionerCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.AUDIT_EVENT.value())) {
            addOrganizationCodes(codesByType, "agent", "source");
            addPractitionerCodes(codesByType, "agent", "source");
        } else if (compartmentMemberType.equals(ResourceType.BASIC.value())) {
            addOrganizationCodes(codesByType, "author");
            addPractitionerCodes(codesByType, "author");
        } else if (compartmentMemberType.equals(ResourceType.CARE_PLAN.value())) {
            addOrganizationCodes(codesByType, "performer");
            addPractitionerCodes(codesByType, "performer");
        } else if (compartmentMemberType.equals(ResourceType.CARE_TEAM.value())) {
            addOrganizationCodes(codesByType, "participant");
            addPractitionerCodes(codesByType, "participant");
        } else if (compartmentMemberType.equals("ChargeHistory")) {
            addOrganizationCodes(codesByType, "service");
            addPractitionerCodes(codesByType, "enterer", "performer-actor");
        } else if (compartmentMemberType.equals(ResourceType.CLAIM.value())) {
            addOrganizationCodes(codesByType, "care-team", "insurer", "payee", "provider");
            addPractitionerCodes(codesByType, "care-team", "enterer", "payee", "provider");
            addLocationCodes(codesByType, "facility");
        } else if (compartmentMemberType.equals(ResourceType.CLAIM_RESPONSE.value())) {
            addOrganizationCodes(codesByType, "insurer", "requestor");
            addPractitionerCodes(codesByType, "requestor");
        } else if (compartmentMemberType.equals(ResourceType.CLINICAL_IMPRESSION.value())) {
            addPractitionerCodes(codesByType, "assessor");
        } else if (compartmentMemberType.equals(ResourceType.COMMUNICATION.value())) {
            addPractitionerCodes(codesByType, "recipient", "sender");
        } else if (compartmentMemberType.equals(ResourceType.COMPOSITION.value())) {
            addPractitionerCodes(codesByType, "attester", "author");
        } else if (compartmentMemberType.equals(ResourceType.CONDITION.value())) {
            addPractitionerCodes(codesByType, "asserter");
        } else if (compartmentMemberType.equals(ResourceType.COMMUNICATION_REQUEST.value())) {
            addOrganizationCodes(codesByType, "recipient", "requester", "sender");
            addPractitionerCodes(codesByType, "recipient", "requester", "sender");
        } else if (compartmentMemberType.equals(ResourceType.CONSENT.value())) {
            addOrganizationCodes(codesByType, "actor", "consentor", "organization");
            addPractitionerCodes(codesByType, "actor", "consentor");
        } else if (compartmentMemberType.equals(ResourceType.COVERAGE.value())) {
            addOrganizationCodes(codesByType, "payor", "policy-holder");
        } else if (compartmentMemberType.equals(ResourceType.COVERAGE_ELIGIBILITY_REQUEST.value())) {
            addPractitionerCodes(codesByType, "enterer", "provider");
            addLocationCodes(codesByType, "facility");
            addOrganizationCodes(codesByType, "provider");
        } else if (compartmentMemberType.equals(ResourceType.COVERAGE_ELIGIBILITY_RESPONSE.value())) {
            addOrganizationCodes(codesByType, "insurer", "requestor");
            addPractitionerCodes(codesByType, "requestor");
        } else if (compartmentMemberType.equals(ResourceType.DETECTED_ISSUE.value())) {
            addPractitionerCodes(codesByType, "author");
        } else if (compartmentMemberType.equals(ResourceType.DEVICE_REQUEST.value())) {
            addOrganizationCodes(codesByType, "performer", "requester");
            addPractitionerCodes(codesByType, "performer", "requester");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.DIAGNOSTIC_REPORT.value())) {
            addOrganizationCodes(codesByType, "performer", "results-interpreter");
            addPractitionerCodes(codesByType, "performer", "results-interpreter");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.DOCUMENT_MANIFEST.value())) {
            addOrganizationCodes(codesByType, "author", "recipient");
            addPractitionerCodes(codesByType, "author", "recipient", "subject");
        } else if (compartmentMemberType.equals(ResourceType.DOCUMENT_REFERENCE.value())) {
            addOrganizationCodes(codesByType, "authenticator", "author", "custodian");
            addPractitionerCodes(codesByType, "authenticator", "author", "subject");
        } else if (compartmentMemberType.equals(ResourceType.ENCOUNTER.value())) {
            addLocationCodes(codesByType, "location");
            addOrganizationCodes(codesByType, "service-provider");
            addPractitionerCodes(codesByType, "participant");
        } else if (compartmentMemberType.equals(ResourceType.EPISODE_OF_CARE.value())) {
            addPractitionerCodes(codesByType, "care-manager");
            addOrganizationCodes(codesByType, "organization");
        } else if (compartmentMemberType.equals(ResourceType.EXPLANATION_OF_BENEFIT.value())) {
            // the old implementation erroneously included "insurer" here
            addOrganizationCodes(codesByType, "care-team", "payee", "provider");
            addPractitionerCodes(codesByType, "care-team", "enterer", "payee", "provider");
            addLocationCodes(codesByType, "facility");
        } else if (compartmentMemberType.equals(ResourceType.FLAG.value())) {
            addOrganizationCodes(codesByType, "author", "subject");
            addPractitionerCodes(codesByType, "author", "subject");
            addLocationCodes(codesByType, "subject");
            addMedicationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.GOAL.value())) {
            addOrganizationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.GROUP.value())) {
            addMedicationCodes(codesByType, "member");
            addPractitionerCodes(codesByType, "member", "managing-entity");
            addOrganizationCodes(codesByType, "managing-entity");
        } else if (compartmentMemberType.equals(ResourceType.IMAGING_STUDY.value())) {
            addPractitionerCodes(codesByType, "interpreter", "performer", "referrer");
        } else if (compartmentMemberType.equals(ResourceType.IMMUNIZATION.value())) {
            addLocationCodes(codesByType, "location");
            addOrganizationCodes(codesByType, "manufacturer", "performer");
            addPractitionerCodes(codesByType, "performer");
        } else if (compartmentMemberType.equals(ResourceType.INVOICE.value())) {
            addOrganizationCodes(codesByType, "issuer", "participant", "recipient");
            addPractitionerCodes(codesByType, "participant");
        } else if (compartmentMemberType.equals(ResourceType.LIST.value())) {
            addPractitionerCodes(codesByType, "source");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.MEASURE_REPORT.value())) {
            addLocationCodes(codesByType, "reporter", "subject");
            addOrganizationCodes(codesByType, "reporter");
            addPractitionerCodes(codesByType, "reporter", "subject");
        } else if (compartmentMemberType.equals(ResourceType.MEDIA.value())) {
            addOrganizationCodes(codesByType, "operator");
            addPractitionerCodes(codesByType, "operator", "subject");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.MEDICATION_ADMINISTRATION.value())) {
            addMedicationCodes(codesByType, "medication");
            addPractitionerCodes(codesByType, "performer");
        } else if (compartmentMemberType.equals(ResourceType.MEDICATION_DISPENSE.value())) {
            addLocationCodes(codesByType, "destination");
            addMedicationCodes(codesByType, "medication");
            addPractitionerCodes(codesByType, "performer", "receiver", "responsibleparty");
        } else if (compartmentMemberType.equals(ResourceType.MEDICATION_REQUEST.value())) {
            addOrganizationCodes(codesByType, "intended-dispenser", "intended-performer", "requester");
            addPractitionerCodes(codesByType, "intended-performer", "requester");
            addMedicationCodes(codesByType, "medication");
        } else if (compartmentMemberType.equals(ResourceType.MEDICATION_STATEMENT.value())) {
            addMedicationCodes(codesByType, "medication");
            addOrganizationCodes(codesByType, "source");
            addPractitionerCodes(codesByType, "source");
        } else if (compartmentMemberType.equals("NutritionHistory")) {
            addPractitionerCodes(codesByType, "provider");
        } else if (compartmentMemberType.equals(ResourceType.OBSERVATION.value())) {
            addOrganizationCodes(codesByType, "performer");
            addPractitionerCodes(codesByType, "performer");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.QUESTIONNAIRE_RESPONSE.value())) {
            addOrganizationCodes(codesByType, "author");
            addPractitionerCodes(codesByType, "author", "source");
        } else if (compartmentMemberType.equals(ResourceType.PATIENT.value())) {
            addOrganizationCodes(codesByType, "general-practitioner", "organization");
            addPractitionerCodes(codesByType, "general-practitioner");
        } else if (compartmentMemberType.equals(ResourceType.PERSON.value())) {
            addOrganizationCodes(codesByType, "organization");
            addPractitionerCodes(codesByType, "practitioner");
        } else if (compartmentMemberType.equals(ResourceType.PROCEDURE.value())) {
            addLocationCodes(codesByType, "location");
            addOrganizationCodes(codesByType, "performer");
            addPractitionerCodes(codesByType, "performer");
        } else if (compartmentMemberType.equals(ResourceType.PROVENANCE.value())) {
            addLocationCodes(codesByType, "location");
            addOrganizationCodes(codesByType, "agent");
            addPractitionerCodes(codesByType, "agent");
        } else if (compartmentMemberType.equals(ResourceType.REQUEST_GROUP.value())) {
            addPractitionerCodes(codesByType, "author", "participant");
        } else if (compartmentMemberType.equals(ResourceType.RISK_ASSESSMENT.value())) {
            addPractitionerCodes(codesByType, "performer");
        } else if (compartmentMemberType.equals(ResourceType.SCHEDULE.value())) {
            addLocationCodes(codesByType, "actor");
            addPractitionerCodes(codesByType, "actor");
        } else if (compartmentMemberType.equals(ResourceType.SERVICE_REQUEST.value())) {
            addOrganizationCodes(codesByType, "performer", "requester");
            addPractitionerCodes(codesByType, "performer", "requester");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.SPECIMEN.value())) {
            addPractitionerCodes(codesByType, "collector");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.SUPPLY_DELIVERY.value())) {
            addPractitionerCodes(codesByType, "receiver", "supplier");
            addOrganizationCodes(codesByType, "supplier");
        } else if (compartmentMemberType.equals(ResourceType.SUPPLY_REQUEST.value())) {
            addOrganizationCodes(codesByType, "requester", "subject", "supplier");
            addPractitionerCodes(codesByType, "requester");
            addLocationCodes(codesByType, "subject");
        } else if (compartmentMemberType.equals(ResourceType.VISION_PRESCRIPTION.value())) {
            addPractitionerCodes(codesByType, "prescriber");
        }
        return codesByType;
    }
    private void addLocationCodes(Map<ResourceType, List<String>> codesByType, String... codes) {
        codesByType.put(ResourceType.LOCATION, Arrays.asList(codes));
    }
    private void addMedicationCodes(Map<ResourceType, List<String>> codesByType, String... codes) {
        codesByType.put(ResourceType.MEDICATION, Arrays.asList(codes));
    }
    private void addPractitionerCodes(Map<ResourceType, List<String>> codesByType, String... codes) {
        codesByType.put(ResourceType.PRACTITIONER, Arrays.asList(codes));
    }
    private void addOrganizationCodes(Map<ResourceType, List<String>> codesByType, String... codes) {
        codesByType.put(ResourceType.ORGANIZATION, Arrays.asList(codes));
    }
}
