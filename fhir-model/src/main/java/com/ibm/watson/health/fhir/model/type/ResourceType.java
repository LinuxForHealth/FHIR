/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class ResourceType extends Code {
    /**
     * Account
     */
    public static final ResourceType ACCOUNT = ResourceType.of(ValueSet.ACCOUNT);

    /**
     * ActivityDefinition
     */
    public static final ResourceType ACTIVITY_DEFINITION = ResourceType.of(ValueSet.ACTIVITY_DEFINITION);

    /**
     * AdverseEvent
     */
    public static final ResourceType ADVERSE_EVENT = ResourceType.of(ValueSet.ADVERSE_EVENT);

    /**
     * AllergyIntolerance
     */
    public static final ResourceType ALLERGY_INTOLERANCE = ResourceType.of(ValueSet.ALLERGY_INTOLERANCE);

    /**
     * Appointment
     */
    public static final ResourceType APPOINTMENT = ResourceType.of(ValueSet.APPOINTMENT);

    /**
     * AppointmentResponse
     */
    public static final ResourceType APPOINTMENT_RESPONSE = ResourceType.of(ValueSet.APPOINTMENT_RESPONSE);

    /**
     * AuditEvent
     */
    public static final ResourceType AUDIT_EVENT = ResourceType.of(ValueSet.AUDIT_EVENT);

    /**
     * Basic
     */
    public static final ResourceType BASIC = ResourceType.of(ValueSet.BASIC);

    /**
     * Binary
     */
    public static final ResourceType BINARY = ResourceType.of(ValueSet.BINARY);

    /**
     * BiologicallyDerivedProduct
     */
    public static final ResourceType BIOLOGICALLY_DERIVED_PRODUCT = ResourceType.of(ValueSet.BIOLOGICALLY_DERIVED_PRODUCT);

    /**
     * BodyStructure
     */
    public static final ResourceType BODY_STRUCTURE = ResourceType.of(ValueSet.BODY_STRUCTURE);

    /**
     * Bundle
     */
    public static final ResourceType BUNDLE = ResourceType.of(ValueSet.BUNDLE);

    /**
     * CapabilityStatement
     */
    public static final ResourceType CAPABILITY_STATEMENT = ResourceType.of(ValueSet.CAPABILITY_STATEMENT);

    /**
     * CarePlan
     */
    public static final ResourceType CARE_PLAN = ResourceType.of(ValueSet.CARE_PLAN);

    /**
     * CareTeam
     */
    public static final ResourceType CARE_TEAM = ResourceType.of(ValueSet.CARE_TEAM);

    /**
     * CatalogEntry
     */
    public static final ResourceType CATALOG_ENTRY = ResourceType.of(ValueSet.CATALOG_ENTRY);

    /**
     * ChargeItem
     */
    public static final ResourceType CHARGE_ITEM = ResourceType.of(ValueSet.CHARGE_ITEM);

    /**
     * ChargeItemDefinition
     */
    public static final ResourceType CHARGE_ITEM_DEFINITION = ResourceType.of(ValueSet.CHARGE_ITEM_DEFINITION);

    /**
     * Claim
     */
    public static final ResourceType CLAIM = ResourceType.of(ValueSet.CLAIM);

    /**
     * ClaimResponse
     */
    public static final ResourceType CLAIM_RESPONSE = ResourceType.of(ValueSet.CLAIM_RESPONSE);

    /**
     * ClinicalImpression
     */
    public static final ResourceType CLINICAL_IMPRESSION = ResourceType.of(ValueSet.CLINICAL_IMPRESSION);

    /**
     * CodeSystem
     */
    public static final ResourceType CODE_SYSTEM = ResourceType.of(ValueSet.CODE_SYSTEM);

    /**
     * Communication
     */
    public static final ResourceType COMMUNICATION = ResourceType.of(ValueSet.COMMUNICATION);

    /**
     * CommunicationRequest
     */
    public static final ResourceType COMMUNICATION_REQUEST = ResourceType.of(ValueSet.COMMUNICATION_REQUEST);

    /**
     * CompartmentDefinition
     */
    public static final ResourceType COMPARTMENT_DEFINITION = ResourceType.of(ValueSet.COMPARTMENT_DEFINITION);

    /**
     * Composition
     */
    public static final ResourceType COMPOSITION = ResourceType.of(ValueSet.COMPOSITION);

    /**
     * ConceptMap
     */
    public static final ResourceType CONCEPT_MAP = ResourceType.of(ValueSet.CONCEPT_MAP);

    /**
     * Condition
     */
    public static final ResourceType CONDITION = ResourceType.of(ValueSet.CONDITION);

    /**
     * Consent
     */
    public static final ResourceType CONSENT = ResourceType.of(ValueSet.CONSENT);

    /**
     * Contract
     */
    public static final ResourceType CONTRACT = ResourceType.of(ValueSet.CONTRACT);

    /**
     * Coverage
     */
    public static final ResourceType COVERAGE = ResourceType.of(ValueSet.COVERAGE);

    /**
     * CoverageEligibilityRequest
     */
    public static final ResourceType COVERAGE_ELIGIBILITY_REQUEST = ResourceType.of(ValueSet.COVERAGE_ELIGIBILITY_REQUEST);

    /**
     * CoverageEligibilityResponse
     */
    public static final ResourceType COVERAGE_ELIGIBILITY_RESPONSE = ResourceType.of(ValueSet.COVERAGE_ELIGIBILITY_RESPONSE);

    /**
     * DetectedIssue
     */
    public static final ResourceType DETECTED_ISSUE = ResourceType.of(ValueSet.DETECTED_ISSUE);

    /**
     * Device
     */
    public static final ResourceType DEVICE = ResourceType.of(ValueSet.DEVICE);

    /**
     * DeviceDefinition
     */
    public static final ResourceType DEVICE_DEFINITION = ResourceType.of(ValueSet.DEVICE_DEFINITION);

    /**
     * DeviceMetric
     */
    public static final ResourceType DEVICE_METRIC = ResourceType.of(ValueSet.DEVICE_METRIC);

    /**
     * DeviceRequest
     */
    public static final ResourceType DEVICE_REQUEST = ResourceType.of(ValueSet.DEVICE_REQUEST);

    /**
     * DeviceUseStatement
     */
    public static final ResourceType DEVICE_USE_STATEMENT = ResourceType.of(ValueSet.DEVICE_USE_STATEMENT);

    /**
     * DiagnosticReport
     */
    public static final ResourceType DIAGNOSTIC_REPORT = ResourceType.of(ValueSet.DIAGNOSTIC_REPORT);

    /**
     * DocumentManifest
     */
    public static final ResourceType DOCUMENT_MANIFEST = ResourceType.of(ValueSet.DOCUMENT_MANIFEST);

    /**
     * DocumentReference
     */
    public static final ResourceType DOCUMENT_REFERENCE = ResourceType.of(ValueSet.DOCUMENT_REFERENCE);

    /**
     * DomainResource
     */
    public static final ResourceType DOMAIN_RESOURCE = ResourceType.of(ValueSet.DOMAIN_RESOURCE);

    /**
     * EffectEvidenceSynthesis
     */
    public static final ResourceType EFFECT_EVIDENCE_SYNTHESIS = ResourceType.of(ValueSet.EFFECT_EVIDENCE_SYNTHESIS);

    /**
     * Encounter
     */
    public static final ResourceType ENCOUNTER = ResourceType.of(ValueSet.ENCOUNTER);

    /**
     * Endpoint
     */
    public static final ResourceType ENDPOINT = ResourceType.of(ValueSet.ENDPOINT);

    /**
     * EnrollmentRequest
     */
    public static final ResourceType ENROLLMENT_REQUEST = ResourceType.of(ValueSet.ENROLLMENT_REQUEST);

    /**
     * EnrollmentResponse
     */
    public static final ResourceType ENROLLMENT_RESPONSE = ResourceType.of(ValueSet.ENROLLMENT_RESPONSE);

    /**
     * EpisodeOfCare
     */
    public static final ResourceType EPISODE_OF_CARE = ResourceType.of(ValueSet.EPISODE_OF_CARE);

    /**
     * EventDefinition
     */
    public static final ResourceType EVENT_DEFINITION = ResourceType.of(ValueSet.EVENT_DEFINITION);

    /**
     * Evidence
     */
    public static final ResourceType EVIDENCE = ResourceType.of(ValueSet.EVIDENCE);

    /**
     * EvidenceVariable
     */
    public static final ResourceType EVIDENCE_VARIABLE = ResourceType.of(ValueSet.EVIDENCE_VARIABLE);

    /**
     * ExampleScenario
     */
    public static final ResourceType EXAMPLE_SCENARIO = ResourceType.of(ValueSet.EXAMPLE_SCENARIO);

    /**
     * ExplanationOfBenefit
     */
    public static final ResourceType EXPLANATION_OF_BENEFIT = ResourceType.of(ValueSet.EXPLANATION_OF_BENEFIT);

    /**
     * FamilyMemberHistory
     */
    public static final ResourceType FAMILY_MEMBER_HISTORY = ResourceType.of(ValueSet.FAMILY_MEMBER_HISTORY);

    /**
     * Flag
     */
    public static final ResourceType FLAG = ResourceType.of(ValueSet.FLAG);

    /**
     * Goal
     */
    public static final ResourceType GOAL = ResourceType.of(ValueSet.GOAL);

    /**
     * GraphDefinition
     */
    public static final ResourceType GRAPH_DEFINITION = ResourceType.of(ValueSet.GRAPH_DEFINITION);

    /**
     * Group
     */
    public static final ResourceType GROUP = ResourceType.of(ValueSet.GROUP);

    /**
     * GuidanceResponse
     */
    public static final ResourceType GUIDANCE_RESPONSE = ResourceType.of(ValueSet.GUIDANCE_RESPONSE);

    /**
     * HealthcareService
     */
    public static final ResourceType HEALTHCARE_SERVICE = ResourceType.of(ValueSet.HEALTHCARE_SERVICE);

    /**
     * ImagingStudy
     */
    public static final ResourceType IMAGING_STUDY = ResourceType.of(ValueSet.IMAGING_STUDY);

    /**
     * Immunization
     */
    public static final ResourceType IMMUNIZATION = ResourceType.of(ValueSet.IMMUNIZATION);

    /**
     * ImmunizationEvaluation
     */
    public static final ResourceType IMMUNIZATION_EVALUATION = ResourceType.of(ValueSet.IMMUNIZATION_EVALUATION);

    /**
     * ImmunizationRecommendation
     */
    public static final ResourceType IMMUNIZATION_RECOMMENDATION = ResourceType.of(ValueSet.IMMUNIZATION_RECOMMENDATION);

    /**
     * ImplementationGuide
     */
    public static final ResourceType IMPLEMENTATION_GUIDE = ResourceType.of(ValueSet.IMPLEMENTATION_GUIDE);

    /**
     * InsurancePlan
     */
    public static final ResourceType INSURANCE_PLAN = ResourceType.of(ValueSet.INSURANCE_PLAN);

    /**
     * Invoice
     */
    public static final ResourceType INVOICE = ResourceType.of(ValueSet.INVOICE);

    /**
     * Library
     */
    public static final ResourceType LIBRARY = ResourceType.of(ValueSet.LIBRARY);

    /**
     * Linkage
     */
    public static final ResourceType LINKAGE = ResourceType.of(ValueSet.LINKAGE);

    /**
     * List
     */
    public static final ResourceType LIST = ResourceType.of(ValueSet.LIST);

    /**
     * Location
     */
    public static final ResourceType LOCATION = ResourceType.of(ValueSet.LOCATION);

    /**
     * Measure
     */
    public static final ResourceType MEASURE = ResourceType.of(ValueSet.MEASURE);

    /**
     * MeasureReport
     */
    public static final ResourceType MEASURE_REPORT = ResourceType.of(ValueSet.MEASURE_REPORT);

    /**
     * Media
     */
    public static final ResourceType MEDIA = ResourceType.of(ValueSet.MEDIA);

    /**
     * Medication
     */
    public static final ResourceType MEDICATION = ResourceType.of(ValueSet.MEDICATION);

    /**
     * MedicationAdministration
     */
    public static final ResourceType MEDICATION_ADMINISTRATION = ResourceType.of(ValueSet.MEDICATION_ADMINISTRATION);

    /**
     * MedicationDispense
     */
    public static final ResourceType MEDICATION_DISPENSE = ResourceType.of(ValueSet.MEDICATION_DISPENSE);

    /**
     * MedicationKnowledge
     */
    public static final ResourceType MEDICATION_KNOWLEDGE = ResourceType.of(ValueSet.MEDICATION_KNOWLEDGE);

    /**
     * MedicationRequest
     */
    public static final ResourceType MEDICATION_REQUEST = ResourceType.of(ValueSet.MEDICATION_REQUEST);

    /**
     * MedicationStatement
     */
    public static final ResourceType MEDICATION_STATEMENT = ResourceType.of(ValueSet.MEDICATION_STATEMENT);

    /**
     * MedicinalProduct
     */
    public static final ResourceType MEDICINAL_PRODUCT = ResourceType.of(ValueSet.MEDICINAL_PRODUCT);

    /**
     * MedicinalProductAuthorization
     */
    public static final ResourceType MEDICINAL_PRODUCT_AUTHORIZATION = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_AUTHORIZATION);

    /**
     * MedicinalProductContraindication
     */
    public static final ResourceType MEDICINAL_PRODUCT_CONTRAINDICATION = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_CONTRAINDICATION);

    /**
     * MedicinalProductIndication
     */
    public static final ResourceType MEDICINAL_PRODUCT_INDICATION = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_INDICATION);

    /**
     * MedicinalProductIngredient
     */
    public static final ResourceType MEDICINAL_PRODUCT_INGREDIENT = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_INGREDIENT);

    /**
     * MedicinalProductInteraction
     */
    public static final ResourceType MEDICINAL_PRODUCT_INTERACTION = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_INTERACTION);

    /**
     * MedicinalProductManufactured
     */
    public static final ResourceType MEDICINAL_PRODUCT_MANUFACTURED = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_MANUFACTURED);

    /**
     * MedicinalProductPackaged
     */
    public static final ResourceType MEDICINAL_PRODUCT_PACKAGED = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_PACKAGED);

    /**
     * MedicinalProductPharmaceutical
     */
    public static final ResourceType MEDICINAL_PRODUCT_PHARMACEUTICAL = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_PHARMACEUTICAL);

    /**
     * MedicinalProductUndesirableEffect
     */
    public static final ResourceType MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT = ResourceType.of(ValueSet.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT);

    /**
     * MessageDefinition
     */
    public static final ResourceType MESSAGE_DEFINITION = ResourceType.of(ValueSet.MESSAGE_DEFINITION);

    /**
     * MessageHeader
     */
    public static final ResourceType MESSAGE_HEADER = ResourceType.of(ValueSet.MESSAGE_HEADER);

    /**
     * MolecularSequence
     */
    public static final ResourceType MOLECULAR_SEQUENCE = ResourceType.of(ValueSet.MOLECULAR_SEQUENCE);

    /**
     * NamingSystem
     */
    public static final ResourceType NAMING_SYSTEM = ResourceType.of(ValueSet.NAMING_SYSTEM);

    /**
     * NutritionOrder
     */
    public static final ResourceType NUTRITION_ORDER = ResourceType.of(ValueSet.NUTRITION_ORDER);

    /**
     * Observation
     */
    public static final ResourceType OBSERVATION = ResourceType.of(ValueSet.OBSERVATION);

    /**
     * ObservationDefinition
     */
    public static final ResourceType OBSERVATION_DEFINITION = ResourceType.of(ValueSet.OBSERVATION_DEFINITION);

    /**
     * OperationDefinition
     */
    public static final ResourceType OPERATION_DEFINITION = ResourceType.of(ValueSet.OPERATION_DEFINITION);

    /**
     * OperationOutcome
     */
    public static final ResourceType OPERATION_OUTCOME = ResourceType.of(ValueSet.OPERATION_OUTCOME);

    /**
     * Organization
     */
    public static final ResourceType ORGANIZATION = ResourceType.of(ValueSet.ORGANIZATION);

    /**
     * OrganizationAffiliation
     */
    public static final ResourceType ORGANIZATION_AFFILIATION = ResourceType.of(ValueSet.ORGANIZATION_AFFILIATION);

    /**
     * Parameters
     */
    public static final ResourceType PARAMETERS = ResourceType.of(ValueSet.PARAMETERS);

    /**
     * Patient
     */
    public static final ResourceType PATIENT = ResourceType.of(ValueSet.PATIENT);

    /**
     * PaymentNotice
     */
    public static final ResourceType PAYMENT_NOTICE = ResourceType.of(ValueSet.PAYMENT_NOTICE);

    /**
     * PaymentReconciliation
     */
    public static final ResourceType PAYMENT_RECONCILIATION = ResourceType.of(ValueSet.PAYMENT_RECONCILIATION);

    /**
     * Person
     */
    public static final ResourceType PERSON = ResourceType.of(ValueSet.PERSON);

    /**
     * PlanDefinition
     */
    public static final ResourceType PLAN_DEFINITION = ResourceType.of(ValueSet.PLAN_DEFINITION);

    /**
     * Practitioner
     */
    public static final ResourceType PRACTITIONER = ResourceType.of(ValueSet.PRACTITIONER);

    /**
     * PractitionerRole
     */
    public static final ResourceType PRACTITIONER_ROLE = ResourceType.of(ValueSet.PRACTITIONER_ROLE);

    /**
     * Procedure
     */
    public static final ResourceType PROCEDURE = ResourceType.of(ValueSet.PROCEDURE);

    /**
     * Provenance
     */
    public static final ResourceType PROVENANCE = ResourceType.of(ValueSet.PROVENANCE);

    /**
     * Questionnaire
     */
    public static final ResourceType QUESTIONNAIRE = ResourceType.of(ValueSet.QUESTIONNAIRE);

    /**
     * QuestionnaireResponse
     */
    public static final ResourceType QUESTIONNAIRE_RESPONSE = ResourceType.of(ValueSet.QUESTIONNAIRE_RESPONSE);

    /**
     * RelatedPerson
     */
    public static final ResourceType RELATED_PERSON = ResourceType.of(ValueSet.RELATED_PERSON);

    /**
     * RequestGroup
     */
    public static final ResourceType REQUEST_GROUP = ResourceType.of(ValueSet.REQUEST_GROUP);

    /**
     * ResearchDefinition
     */
    public static final ResourceType RESEARCH_DEFINITION = ResourceType.of(ValueSet.RESEARCH_DEFINITION);

    /**
     * ResearchElementDefinition
     */
    public static final ResourceType RESEARCH_ELEMENT_DEFINITION = ResourceType.of(ValueSet.RESEARCH_ELEMENT_DEFINITION);

    /**
     * ResearchStudy
     */
    public static final ResourceType RESEARCH_STUDY = ResourceType.of(ValueSet.RESEARCH_STUDY);

    /**
     * ResearchSubject
     */
    public static final ResourceType RESEARCH_SUBJECT = ResourceType.of(ValueSet.RESEARCH_SUBJECT);

    /**
     * Resource
     */
    public static final ResourceType RESOURCE = ResourceType.of(ValueSet.RESOURCE);

    /**
     * RiskAssessment
     */
    public static final ResourceType RISK_ASSESSMENT = ResourceType.of(ValueSet.RISK_ASSESSMENT);

    /**
     * RiskEvidenceSynthesis
     */
    public static final ResourceType RISK_EVIDENCE_SYNTHESIS = ResourceType.of(ValueSet.RISK_EVIDENCE_SYNTHESIS);

    /**
     * Schedule
     */
    public static final ResourceType SCHEDULE = ResourceType.of(ValueSet.SCHEDULE);

    /**
     * SearchParameter
     */
    public static final ResourceType SEARCH_PARAMETER = ResourceType.of(ValueSet.SEARCH_PARAMETER);

    /**
     * ServiceRequest
     */
    public static final ResourceType SERVICE_REQUEST = ResourceType.of(ValueSet.SERVICE_REQUEST);

    /**
     * Slot
     */
    public static final ResourceType SLOT = ResourceType.of(ValueSet.SLOT);

    /**
     * Specimen
     */
    public static final ResourceType SPECIMEN = ResourceType.of(ValueSet.SPECIMEN);

    /**
     * SpecimenDefinition
     */
    public static final ResourceType SPECIMEN_DEFINITION = ResourceType.of(ValueSet.SPECIMEN_DEFINITION);

    /**
     * StructureDefinition
     */
    public static final ResourceType STRUCTURE_DEFINITION = ResourceType.of(ValueSet.STRUCTURE_DEFINITION);

    /**
     * StructureMap
     */
    public static final ResourceType STRUCTURE_MAP = ResourceType.of(ValueSet.STRUCTURE_MAP);

    /**
     * Subscription
     */
    public static final ResourceType SUBSCRIPTION = ResourceType.of(ValueSet.SUBSCRIPTION);

    /**
     * Substance
     */
    public static final ResourceType SUBSTANCE = ResourceType.of(ValueSet.SUBSTANCE);

    /**
     * SubstanceNucleicAcid
     */
    public static final ResourceType SUBSTANCE_NUCLEIC_ACID = ResourceType.of(ValueSet.SUBSTANCE_NUCLEIC_ACID);

    /**
     * SubstancePolymer
     */
    public static final ResourceType SUBSTANCE_POLYMER = ResourceType.of(ValueSet.SUBSTANCE_POLYMER);

    /**
     * SubstanceProtein
     */
    public static final ResourceType SUBSTANCE_PROTEIN = ResourceType.of(ValueSet.SUBSTANCE_PROTEIN);

    /**
     * SubstanceReferenceInformation
     */
    public static final ResourceType SUBSTANCE_REFERENCE_INFORMATION = ResourceType.of(ValueSet.SUBSTANCE_REFERENCE_INFORMATION);

    /**
     * SubstanceSourceMaterial
     */
    public static final ResourceType SUBSTANCE_SOURCE_MATERIAL = ResourceType.of(ValueSet.SUBSTANCE_SOURCE_MATERIAL);

    /**
     * SubstanceSpecification
     */
    public static final ResourceType SUBSTANCE_SPECIFICATION = ResourceType.of(ValueSet.SUBSTANCE_SPECIFICATION);

    /**
     * SupplyDelivery
     */
    public static final ResourceType SUPPLY_DELIVERY = ResourceType.of(ValueSet.SUPPLY_DELIVERY);

    /**
     * SupplyRequest
     */
    public static final ResourceType SUPPLY_REQUEST = ResourceType.of(ValueSet.SUPPLY_REQUEST);

    /**
     * Task
     */
    public static final ResourceType TASK = ResourceType.of(ValueSet.TASK);

    /**
     * TerminologyCapabilities
     */
    public static final ResourceType TERMINOLOGY_CAPABILITIES = ResourceType.of(ValueSet.TERMINOLOGY_CAPABILITIES);

    /**
     * TestReport
     */
    public static final ResourceType TEST_REPORT = ResourceType.of(ValueSet.TEST_REPORT);

    /**
     * TestScript
     */
    public static final ResourceType TEST_SCRIPT = ResourceType.of(ValueSet.TEST_SCRIPT);

    /**
     * ValueSet
     */
    public static final ResourceType VALUE_SET = ResourceType.of(ValueSet.VALUE_SET);

    /**
     * VerificationResult
     */
    public static final ResourceType VERIFICATION_RESULT = ResourceType.of(ValueSet.VERIFICATION_RESULT);

    /**
     * VisionPrescription
     */
    public static final ResourceType VISION_PRESCRIPTION = ResourceType.of(ValueSet.VISION_PRESCRIPTION);

    private volatile int hashCode;

    private ResourceType(Builder builder) {
        super(builder);
    }

    public static ResourceType of(java.lang.String value) {
        return ResourceType.builder().value(value).build();
    }

    public static ResourceType of(ValueSet value) {
        return ResourceType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ResourceType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ResourceType.builder().value(value).build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ResourceType other = (ResourceType) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public ResourceType build() {
            return new ResourceType(this);
        }
    }

    public enum ValueSet {
        /**
         * Account
         */
        ACCOUNT("Account"),

        /**
         * ActivityDefinition
         */
        ACTIVITY_DEFINITION("ActivityDefinition"),

        /**
         * AdverseEvent
         */
        ADVERSE_EVENT("AdverseEvent"),

        /**
         * AllergyIntolerance
         */
        ALLERGY_INTOLERANCE("AllergyIntolerance"),

        /**
         * Appointment
         */
        APPOINTMENT("Appointment"),

        /**
         * AppointmentResponse
         */
        APPOINTMENT_RESPONSE("AppointmentResponse"),

        /**
         * AuditEvent
         */
        AUDIT_EVENT("AuditEvent"),

        /**
         * Basic
         */
        BASIC("Basic"),

        /**
         * Binary
         */
        BINARY("Binary"),

        /**
         * BiologicallyDerivedProduct
         */
        BIOLOGICALLY_DERIVED_PRODUCT("BiologicallyDerivedProduct"),

        /**
         * BodyStructure
         */
        BODY_STRUCTURE("BodyStructure"),

        /**
         * Bundle
         */
        BUNDLE("Bundle"),

        /**
         * CapabilityStatement
         */
        CAPABILITY_STATEMENT("CapabilityStatement"),

        /**
         * CarePlan
         */
        CARE_PLAN("CarePlan"),

        /**
         * CareTeam
         */
        CARE_TEAM("CareTeam"),

        /**
         * CatalogEntry
         */
        CATALOG_ENTRY("CatalogEntry"),

        /**
         * ChargeItem
         */
        CHARGE_ITEM("ChargeItem"),

        /**
         * ChargeItemDefinition
         */
        CHARGE_ITEM_DEFINITION("ChargeItemDefinition"),

        /**
         * Claim
         */
        CLAIM("Claim"),

        /**
         * ClaimResponse
         */
        CLAIM_RESPONSE("ClaimResponse"),

        /**
         * ClinicalImpression
         */
        CLINICAL_IMPRESSION("ClinicalImpression"),

        /**
         * CodeSystem
         */
        CODE_SYSTEM("CodeSystem"),

        /**
         * Communication
         */
        COMMUNICATION("Communication"),

        /**
         * CommunicationRequest
         */
        COMMUNICATION_REQUEST("CommunicationRequest"),

        /**
         * CompartmentDefinition
         */
        COMPARTMENT_DEFINITION("CompartmentDefinition"),

        /**
         * Composition
         */
        COMPOSITION("Composition"),

        /**
         * ConceptMap
         */
        CONCEPT_MAP("ConceptMap"),

        /**
         * Condition
         */
        CONDITION("Condition"),

        /**
         * Consent
         */
        CONSENT("Consent"),

        /**
         * Contract
         */
        CONTRACT("Contract"),

        /**
         * Coverage
         */
        COVERAGE("Coverage"),

        /**
         * CoverageEligibilityRequest
         */
        COVERAGE_ELIGIBILITY_REQUEST("CoverageEligibilityRequest"),

        /**
         * CoverageEligibilityResponse
         */
        COVERAGE_ELIGIBILITY_RESPONSE("CoverageEligibilityResponse"),

        /**
         * DetectedIssue
         */
        DETECTED_ISSUE("DetectedIssue"),

        /**
         * Device
         */
        DEVICE("Device"),

        /**
         * DeviceDefinition
         */
        DEVICE_DEFINITION("DeviceDefinition"),

        /**
         * DeviceMetric
         */
        DEVICE_METRIC("DeviceMetric"),

        /**
         * DeviceRequest
         */
        DEVICE_REQUEST("DeviceRequest"),

        /**
         * DeviceUseStatement
         */
        DEVICE_USE_STATEMENT("DeviceUseStatement"),

        /**
         * DiagnosticReport
         */
        DIAGNOSTIC_REPORT("DiagnosticReport"),

        /**
         * DocumentManifest
         */
        DOCUMENT_MANIFEST("DocumentManifest"),

        /**
         * DocumentReference
         */
        DOCUMENT_REFERENCE("DocumentReference"),

        /**
         * DomainResource
         */
        DOMAIN_RESOURCE("DomainResource"),

        /**
         * EffectEvidenceSynthesis
         */
        EFFECT_EVIDENCE_SYNTHESIS("EffectEvidenceSynthesis"),

        /**
         * Encounter
         */
        ENCOUNTER("Encounter"),

        /**
         * Endpoint
         */
        ENDPOINT("Endpoint"),

        /**
         * EnrollmentRequest
         */
        ENROLLMENT_REQUEST("EnrollmentRequest"),

        /**
         * EnrollmentResponse
         */
        ENROLLMENT_RESPONSE("EnrollmentResponse"),

        /**
         * EpisodeOfCare
         */
        EPISODE_OF_CARE("EpisodeOfCare"),

        /**
         * EventDefinition
         */
        EVENT_DEFINITION("EventDefinition"),

        /**
         * Evidence
         */
        EVIDENCE("Evidence"),

        /**
         * EvidenceVariable
         */
        EVIDENCE_VARIABLE("EvidenceVariable"),

        /**
         * ExampleScenario
         */
        EXAMPLE_SCENARIO("ExampleScenario"),

        /**
         * ExplanationOfBenefit
         */
        EXPLANATION_OF_BENEFIT("ExplanationOfBenefit"),

        /**
         * FamilyMemberHistory
         */
        FAMILY_MEMBER_HISTORY("FamilyMemberHistory"),

        /**
         * Flag
         */
        FLAG("Flag"),

        /**
         * Goal
         */
        GOAL("Goal"),

        /**
         * GraphDefinition
         */
        GRAPH_DEFINITION("GraphDefinition"),

        /**
         * Group
         */
        GROUP("Group"),

        /**
         * GuidanceResponse
         */
        GUIDANCE_RESPONSE("GuidanceResponse"),

        /**
         * HealthcareService
         */
        HEALTHCARE_SERVICE("HealthcareService"),

        /**
         * ImagingStudy
         */
        IMAGING_STUDY("ImagingStudy"),

        /**
         * Immunization
         */
        IMMUNIZATION("Immunization"),

        /**
         * ImmunizationEvaluation
         */
        IMMUNIZATION_EVALUATION("ImmunizationEvaluation"),

        /**
         * ImmunizationRecommendation
         */
        IMMUNIZATION_RECOMMENDATION("ImmunizationRecommendation"),

        /**
         * ImplementationGuide
         */
        IMPLEMENTATION_GUIDE("ImplementationGuide"),

        /**
         * InsurancePlan
         */
        INSURANCE_PLAN("InsurancePlan"),

        /**
         * Invoice
         */
        INVOICE("Invoice"),

        /**
         * Library
         */
        LIBRARY("Library"),

        /**
         * Linkage
         */
        LINKAGE("Linkage"),

        /**
         * List
         */
        LIST("List"),

        /**
         * Location
         */
        LOCATION("Location"),

        /**
         * Measure
         */
        MEASURE("Measure"),

        /**
         * MeasureReport
         */
        MEASURE_REPORT("MeasureReport"),

        /**
         * Media
         */
        MEDIA("Media"),

        /**
         * Medication
         */
        MEDICATION("Medication"),

        /**
         * MedicationAdministration
         */
        MEDICATION_ADMINISTRATION("MedicationAdministration"),

        /**
         * MedicationDispense
         */
        MEDICATION_DISPENSE("MedicationDispense"),

        /**
         * MedicationKnowledge
         */
        MEDICATION_KNOWLEDGE("MedicationKnowledge"),

        /**
         * MedicationRequest
         */
        MEDICATION_REQUEST("MedicationRequest"),

        /**
         * MedicationStatement
         */
        MEDICATION_STATEMENT("MedicationStatement"),

        /**
         * MedicinalProduct
         */
        MEDICINAL_PRODUCT("MedicinalProduct"),

        /**
         * MedicinalProductAuthorization
         */
        MEDICINAL_PRODUCT_AUTHORIZATION("MedicinalProductAuthorization"),

        /**
         * MedicinalProductContraindication
         */
        MEDICINAL_PRODUCT_CONTRAINDICATION("MedicinalProductContraindication"),

        /**
         * MedicinalProductIndication
         */
        MEDICINAL_PRODUCT_INDICATION("MedicinalProductIndication"),

        /**
         * MedicinalProductIngredient
         */
        MEDICINAL_PRODUCT_INGREDIENT("MedicinalProductIngredient"),

        /**
         * MedicinalProductInteraction
         */
        MEDICINAL_PRODUCT_INTERACTION("MedicinalProductInteraction"),

        /**
         * MedicinalProductManufactured
         */
        MEDICINAL_PRODUCT_MANUFACTURED("MedicinalProductManufactured"),

        /**
         * MedicinalProductPackaged
         */
        MEDICINAL_PRODUCT_PACKAGED("MedicinalProductPackaged"),

        /**
         * MedicinalProductPharmaceutical
         */
        MEDICINAL_PRODUCT_PHARMACEUTICAL("MedicinalProductPharmaceutical"),

        /**
         * MedicinalProductUndesirableEffect
         */
        MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT("MedicinalProductUndesirableEffect"),

        /**
         * MessageDefinition
         */
        MESSAGE_DEFINITION("MessageDefinition"),

        /**
         * MessageHeader
         */
        MESSAGE_HEADER("MessageHeader"),

        /**
         * MolecularSequence
         */
        MOLECULAR_SEQUENCE("MolecularSequence"),

        /**
         * NamingSystem
         */
        NAMING_SYSTEM("NamingSystem"),

        /**
         * NutritionOrder
         */
        NUTRITION_ORDER("NutritionOrder"),

        /**
         * Observation
         */
        OBSERVATION("Observation"),

        /**
         * ObservationDefinition
         */
        OBSERVATION_DEFINITION("ObservationDefinition"),

        /**
         * OperationDefinition
         */
        OPERATION_DEFINITION("OperationDefinition"),

        /**
         * OperationOutcome
         */
        OPERATION_OUTCOME("OperationOutcome"),

        /**
         * Organization
         */
        ORGANIZATION("Organization"),

        /**
         * OrganizationAffiliation
         */
        ORGANIZATION_AFFILIATION("OrganizationAffiliation"),

        /**
         * Parameters
         */
        PARAMETERS("Parameters"),

        /**
         * Patient
         */
        PATIENT("Patient"),

        /**
         * PaymentNotice
         */
        PAYMENT_NOTICE("PaymentNotice"),

        /**
         * PaymentReconciliation
         */
        PAYMENT_RECONCILIATION("PaymentReconciliation"),

        /**
         * Person
         */
        PERSON("Person"),

        /**
         * PlanDefinition
         */
        PLAN_DEFINITION("PlanDefinition"),

        /**
         * Practitioner
         */
        PRACTITIONER("Practitioner"),

        /**
         * PractitionerRole
         */
        PRACTITIONER_ROLE("PractitionerRole"),

        /**
         * Procedure
         */
        PROCEDURE("Procedure"),

        /**
         * Provenance
         */
        PROVENANCE("Provenance"),

        /**
         * Questionnaire
         */
        QUESTIONNAIRE("Questionnaire"),

        /**
         * QuestionnaireResponse
         */
        QUESTIONNAIRE_RESPONSE("QuestionnaireResponse"),

        /**
         * RelatedPerson
         */
        RELATED_PERSON("RelatedPerson"),

        /**
         * RequestGroup
         */
        REQUEST_GROUP("RequestGroup"),

        /**
         * ResearchDefinition
         */
        RESEARCH_DEFINITION("ResearchDefinition"),

        /**
         * ResearchElementDefinition
         */
        RESEARCH_ELEMENT_DEFINITION("ResearchElementDefinition"),

        /**
         * ResearchStudy
         */
        RESEARCH_STUDY("ResearchStudy"),

        /**
         * ResearchSubject
         */
        RESEARCH_SUBJECT("ResearchSubject"),

        /**
         * Resource
         */
        RESOURCE("Resource"),

        /**
         * RiskAssessment
         */
        RISK_ASSESSMENT("RiskAssessment"),

        /**
         * RiskEvidenceSynthesis
         */
        RISK_EVIDENCE_SYNTHESIS("RiskEvidenceSynthesis"),

        /**
         * Schedule
         */
        SCHEDULE("Schedule"),

        /**
         * SearchParameter
         */
        SEARCH_PARAMETER("SearchParameter"),

        /**
         * ServiceRequest
         */
        SERVICE_REQUEST("ServiceRequest"),

        /**
         * Slot
         */
        SLOT("Slot"),

        /**
         * Specimen
         */
        SPECIMEN("Specimen"),

        /**
         * SpecimenDefinition
         */
        SPECIMEN_DEFINITION("SpecimenDefinition"),

        /**
         * StructureDefinition
         */
        STRUCTURE_DEFINITION("StructureDefinition"),

        /**
         * StructureMap
         */
        STRUCTURE_MAP("StructureMap"),

        /**
         * Subscription
         */
        SUBSCRIPTION("Subscription"),

        /**
         * Substance
         */
        SUBSTANCE("Substance"),

        /**
         * SubstanceNucleicAcid
         */
        SUBSTANCE_NUCLEIC_ACID("SubstanceNucleicAcid"),

        /**
         * SubstancePolymer
         */
        SUBSTANCE_POLYMER("SubstancePolymer"),

        /**
         * SubstanceProtein
         */
        SUBSTANCE_PROTEIN("SubstanceProtein"),

        /**
         * SubstanceReferenceInformation
         */
        SUBSTANCE_REFERENCE_INFORMATION("SubstanceReferenceInformation"),

        /**
         * SubstanceSourceMaterial
         */
        SUBSTANCE_SOURCE_MATERIAL("SubstanceSourceMaterial"),

        /**
         * SubstanceSpecification
         */
        SUBSTANCE_SPECIFICATION("SubstanceSpecification"),

        /**
         * SupplyDelivery
         */
        SUPPLY_DELIVERY("SupplyDelivery"),

        /**
         * SupplyRequest
         */
        SUPPLY_REQUEST("SupplyRequest"),

        /**
         * Task
         */
        TASK("Task"),

        /**
         * TerminologyCapabilities
         */
        TERMINOLOGY_CAPABILITIES("TerminologyCapabilities"),

        /**
         * TestReport
         */
        TEST_REPORT("TestReport"),

        /**
         * TestScript
         */
        TEST_SCRIPT("TestScript"),

        /**
         * ValueSet
         */
        VALUE_SET("ValueSet"),

        /**
         * VerificationResult
         */
        VERIFICATION_RESULT("VerificationResult"),

        /**
         * VisionPrescription
         */
        VISION_PRESCRIPTION("VisionPrescription");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
