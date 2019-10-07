/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class FHIRResourceType extends Code {
    /**
     * Account
     */
    public static final FHIRResourceType ACCOUNT = FHIRResourceType.of(ValueSet.ACCOUNT);

    /**
     * ActivityDefinition
     */
    public static final FHIRResourceType ACTIVITY_DEFINITION = FHIRResourceType.of(ValueSet.ACTIVITY_DEFINITION);

    /**
     * AdverseEvent
     */
    public static final FHIRResourceType ADVERSE_EVENT = FHIRResourceType.of(ValueSet.ADVERSE_EVENT);

    /**
     * AllergyIntolerance
     */
    public static final FHIRResourceType ALLERGY_INTOLERANCE = FHIRResourceType.of(ValueSet.ALLERGY_INTOLERANCE);

    /**
     * Appointment
     */
    public static final FHIRResourceType APPOINTMENT = FHIRResourceType.of(ValueSet.APPOINTMENT);

    /**
     * AppointmentResponse
     */
    public static final FHIRResourceType APPOINTMENT_RESPONSE = FHIRResourceType.of(ValueSet.APPOINTMENT_RESPONSE);

    /**
     * AuditEvent
     */
    public static final FHIRResourceType AUDIT_EVENT = FHIRResourceType.of(ValueSet.AUDIT_EVENT);

    /**
     * Basic
     */
    public static final FHIRResourceType BASIC = FHIRResourceType.of(ValueSet.BASIC);

    /**
     * Binary
     */
    public static final FHIRResourceType BINARY = FHIRResourceType.of(ValueSet.BINARY);

    /**
     * BiologicallyDerivedProduct
     */
    public static final FHIRResourceType BIOLOGICALLY_DERIVED_PRODUCT = FHIRResourceType.of(ValueSet.BIOLOGICALLY_DERIVED_PRODUCT);

    /**
     * BodyStructure
     */
    public static final FHIRResourceType BODY_STRUCTURE = FHIRResourceType.of(ValueSet.BODY_STRUCTURE);

    /**
     * Bundle
     */
    public static final FHIRResourceType BUNDLE = FHIRResourceType.of(ValueSet.BUNDLE);

    /**
     * CapabilityStatement
     */
    public static final FHIRResourceType CAPABILITY_STATEMENT = FHIRResourceType.of(ValueSet.CAPABILITY_STATEMENT);

    /**
     * CarePlan
     */
    public static final FHIRResourceType CARE_PLAN = FHIRResourceType.of(ValueSet.CARE_PLAN);

    /**
     * CareTeam
     */
    public static final FHIRResourceType CARE_TEAM = FHIRResourceType.of(ValueSet.CARE_TEAM);

    /**
     * CatalogEntry
     */
    public static final FHIRResourceType CATALOG_ENTRY = FHIRResourceType.of(ValueSet.CATALOG_ENTRY);

    /**
     * ChargeItem
     */
    public static final FHIRResourceType CHARGE_ITEM = FHIRResourceType.of(ValueSet.CHARGE_ITEM);

    /**
     * ChargeItemDefinition
     */
    public static final FHIRResourceType CHARGE_ITEM_DEFINITION = FHIRResourceType.of(ValueSet.CHARGE_ITEM_DEFINITION);

    /**
     * Claim
     */
    public static final FHIRResourceType CLAIM = FHIRResourceType.of(ValueSet.CLAIM);

    /**
     * ClaimResponse
     */
    public static final FHIRResourceType CLAIM_RESPONSE = FHIRResourceType.of(ValueSet.CLAIM_RESPONSE);

    /**
     * ClinicalImpression
     */
    public static final FHIRResourceType CLINICAL_IMPRESSION = FHIRResourceType.of(ValueSet.CLINICAL_IMPRESSION);

    /**
     * CodeSystem
     */
    public static final FHIRResourceType CODE_SYSTEM = FHIRResourceType.of(ValueSet.CODE_SYSTEM);

    /**
     * Communication
     */
    public static final FHIRResourceType COMMUNICATION = FHIRResourceType.of(ValueSet.COMMUNICATION);

    /**
     * CommunicationRequest
     */
    public static final FHIRResourceType COMMUNICATION_REQUEST = FHIRResourceType.of(ValueSet.COMMUNICATION_REQUEST);

    /**
     * CompartmentDefinition
     */
    public static final FHIRResourceType COMPARTMENT_DEFINITION = FHIRResourceType.of(ValueSet.COMPARTMENT_DEFINITION);

    /**
     * Composition
     */
    public static final FHIRResourceType COMPOSITION = FHIRResourceType.of(ValueSet.COMPOSITION);

    /**
     * ConceptMap
     */
    public static final FHIRResourceType CONCEPT_MAP = FHIRResourceType.of(ValueSet.CONCEPT_MAP);

    /**
     * Condition
     */
    public static final FHIRResourceType CONDITION = FHIRResourceType.of(ValueSet.CONDITION);

    /**
     * Consent
     */
    public static final FHIRResourceType CONSENT = FHIRResourceType.of(ValueSet.CONSENT);

    /**
     * Contract
     */
    public static final FHIRResourceType CONTRACT = FHIRResourceType.of(ValueSet.CONTRACT);

    /**
     * Coverage
     */
    public static final FHIRResourceType COVERAGE = FHIRResourceType.of(ValueSet.COVERAGE);

    /**
     * CoverageEligibilityRequest
     */
    public static final FHIRResourceType COVERAGE_ELIGIBILITY_REQUEST = FHIRResourceType.of(ValueSet.COVERAGE_ELIGIBILITY_REQUEST);

    /**
     * CoverageEligibilityResponse
     */
    public static final FHIRResourceType COVERAGE_ELIGIBILITY_RESPONSE = FHIRResourceType.of(ValueSet.COVERAGE_ELIGIBILITY_RESPONSE);

    /**
     * DetectedIssue
     */
    public static final FHIRResourceType DETECTED_ISSUE = FHIRResourceType.of(ValueSet.DETECTED_ISSUE);

    /**
     * Device
     */
    public static final FHIRResourceType DEVICE = FHIRResourceType.of(ValueSet.DEVICE);

    /**
     * DeviceDefinition
     */
    public static final FHIRResourceType DEVICE_DEFINITION = FHIRResourceType.of(ValueSet.DEVICE_DEFINITION);

    /**
     * DeviceMetric
     */
    public static final FHIRResourceType DEVICE_METRIC = FHIRResourceType.of(ValueSet.DEVICE_METRIC);

    /**
     * DeviceRequest
     */
    public static final FHIRResourceType DEVICE_REQUEST = FHIRResourceType.of(ValueSet.DEVICE_REQUEST);

    /**
     * DeviceUseStatement
     */
    public static final FHIRResourceType DEVICE_USE_STATEMENT = FHIRResourceType.of(ValueSet.DEVICE_USE_STATEMENT);

    /**
     * DiagnosticReport
     */
    public static final FHIRResourceType DIAGNOSTIC_REPORT = FHIRResourceType.of(ValueSet.DIAGNOSTIC_REPORT);

    /**
     * DocumentManifest
     */
    public static final FHIRResourceType DOCUMENT_MANIFEST = FHIRResourceType.of(ValueSet.DOCUMENT_MANIFEST);

    /**
     * DocumentReference
     */
    public static final FHIRResourceType DOCUMENT_REFERENCE = FHIRResourceType.of(ValueSet.DOCUMENT_REFERENCE);

    /**
     * DomainResource
     */
    public static final FHIRResourceType DOMAIN_RESOURCE = FHIRResourceType.of(ValueSet.DOMAIN_RESOURCE);

    /**
     * EffectEvidenceSynthesis
     */
    public static final FHIRResourceType EFFECT_EVIDENCE_SYNTHESIS = FHIRResourceType.of(ValueSet.EFFECT_EVIDENCE_SYNTHESIS);

    /**
     * Encounter
     */
    public static final FHIRResourceType ENCOUNTER = FHIRResourceType.of(ValueSet.ENCOUNTER);

    /**
     * Endpoint
     */
    public static final FHIRResourceType ENDPOINT = FHIRResourceType.of(ValueSet.ENDPOINT);

    /**
     * EnrollmentRequest
     */
    public static final FHIRResourceType ENROLLMENT_REQUEST = FHIRResourceType.of(ValueSet.ENROLLMENT_REQUEST);

    /**
     * EnrollmentResponse
     */
    public static final FHIRResourceType ENROLLMENT_RESPONSE = FHIRResourceType.of(ValueSet.ENROLLMENT_RESPONSE);

    /**
     * EpisodeOfCare
     */
    public static final FHIRResourceType EPISODE_OF_CARE = FHIRResourceType.of(ValueSet.EPISODE_OF_CARE);

    /**
     * EventDefinition
     */
    public static final FHIRResourceType EVENT_DEFINITION = FHIRResourceType.of(ValueSet.EVENT_DEFINITION);

    /**
     * Evidence
     */
    public static final FHIRResourceType EVIDENCE = FHIRResourceType.of(ValueSet.EVIDENCE);

    /**
     * EvidenceVariable
     */
    public static final FHIRResourceType EVIDENCE_VARIABLE = FHIRResourceType.of(ValueSet.EVIDENCE_VARIABLE);

    /**
     * ExampleScenario
     */
    public static final FHIRResourceType EXAMPLE_SCENARIO = FHIRResourceType.of(ValueSet.EXAMPLE_SCENARIO);

    /**
     * ExplanationOfBenefit
     */
    public static final FHIRResourceType EXPLANATION_OF_BENEFIT = FHIRResourceType.of(ValueSet.EXPLANATION_OF_BENEFIT);

    /**
     * FamilyMemberHistory
     */
    public static final FHIRResourceType FAMILY_MEMBER_HISTORY = FHIRResourceType.of(ValueSet.FAMILY_MEMBER_HISTORY);

    /**
     * Flag
     */
    public static final FHIRResourceType FLAG = FHIRResourceType.of(ValueSet.FLAG);

    /**
     * Goal
     */
    public static final FHIRResourceType GOAL = FHIRResourceType.of(ValueSet.GOAL);

    /**
     * GraphDefinition
     */
    public static final FHIRResourceType GRAPH_DEFINITION = FHIRResourceType.of(ValueSet.GRAPH_DEFINITION);

    /**
     * Group
     */
    public static final FHIRResourceType GROUP = FHIRResourceType.of(ValueSet.GROUP);

    /**
     * GuidanceResponse
     */
    public static final FHIRResourceType GUIDANCE_RESPONSE = FHIRResourceType.of(ValueSet.GUIDANCE_RESPONSE);

    /**
     * HealthcareService
     */
    public static final FHIRResourceType HEALTHCARE_SERVICE = FHIRResourceType.of(ValueSet.HEALTHCARE_SERVICE);

    /**
     * ImagingStudy
     */
    public static final FHIRResourceType IMAGING_STUDY = FHIRResourceType.of(ValueSet.IMAGING_STUDY);

    /**
     * Immunization
     */
    public static final FHIRResourceType IMMUNIZATION = FHIRResourceType.of(ValueSet.IMMUNIZATION);

    /**
     * ImmunizationEvaluation
     */
    public static final FHIRResourceType IMMUNIZATION_EVALUATION = FHIRResourceType.of(ValueSet.IMMUNIZATION_EVALUATION);

    /**
     * ImmunizationRecommendation
     */
    public static final FHIRResourceType IMMUNIZATION_RECOMMENDATION = FHIRResourceType.of(ValueSet.IMMUNIZATION_RECOMMENDATION);

    /**
     * ImplementationGuide
     */
    public static final FHIRResourceType IMPLEMENTATION_GUIDE = FHIRResourceType.of(ValueSet.IMPLEMENTATION_GUIDE);

    /**
     * InsurancePlan
     */
    public static final FHIRResourceType INSURANCE_PLAN = FHIRResourceType.of(ValueSet.INSURANCE_PLAN);

    /**
     * Invoice
     */
    public static final FHIRResourceType INVOICE = FHIRResourceType.of(ValueSet.INVOICE);

    /**
     * Library
     */
    public static final FHIRResourceType LIBRARY = FHIRResourceType.of(ValueSet.LIBRARY);

    /**
     * Linkage
     */
    public static final FHIRResourceType LINKAGE = FHIRResourceType.of(ValueSet.LINKAGE);

    /**
     * List
     */
    public static final FHIRResourceType LIST = FHIRResourceType.of(ValueSet.LIST);

    /**
     * Location
     */
    public static final FHIRResourceType LOCATION = FHIRResourceType.of(ValueSet.LOCATION);

    /**
     * Measure
     */
    public static final FHIRResourceType MEASURE = FHIRResourceType.of(ValueSet.MEASURE);

    /**
     * MeasureReport
     */
    public static final FHIRResourceType MEASURE_REPORT = FHIRResourceType.of(ValueSet.MEASURE_REPORT);

    /**
     * Media
     */
    public static final FHIRResourceType MEDIA = FHIRResourceType.of(ValueSet.MEDIA);

    /**
     * Medication
     */
    public static final FHIRResourceType MEDICATION = FHIRResourceType.of(ValueSet.MEDICATION);

    /**
     * MedicationAdministration
     */
    public static final FHIRResourceType MEDICATION_ADMINISTRATION = FHIRResourceType.of(ValueSet.MEDICATION_ADMINISTRATION);

    /**
     * MedicationDispense
     */
    public static final FHIRResourceType MEDICATION_DISPENSE = FHIRResourceType.of(ValueSet.MEDICATION_DISPENSE);

    /**
     * MedicationKnowledge
     */
    public static final FHIRResourceType MEDICATION_KNOWLEDGE = FHIRResourceType.of(ValueSet.MEDICATION_KNOWLEDGE);

    /**
     * MedicationRequest
     */
    public static final FHIRResourceType MEDICATION_REQUEST = FHIRResourceType.of(ValueSet.MEDICATION_REQUEST);

    /**
     * MedicationStatement
     */
    public static final FHIRResourceType MEDICATION_STATEMENT = FHIRResourceType.of(ValueSet.MEDICATION_STATEMENT);

    /**
     * MedicinalProduct
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT);

    /**
     * MedicinalProductAuthorization
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_AUTHORIZATION = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_AUTHORIZATION);

    /**
     * MedicinalProductContraindication
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_CONTRAINDICATION = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_CONTRAINDICATION);

    /**
     * MedicinalProductIndication
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_INDICATION = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_INDICATION);

    /**
     * MedicinalProductIngredient
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_INGREDIENT = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_INGREDIENT);

    /**
     * MedicinalProductInteraction
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_INTERACTION = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_INTERACTION);

    /**
     * MedicinalProductManufactured
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_MANUFACTURED = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_MANUFACTURED);

    /**
     * MedicinalProductPackaged
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_PACKAGED = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_PACKAGED);

    /**
     * MedicinalProductPharmaceutical
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_PHARMACEUTICAL = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_PHARMACEUTICAL);

    /**
     * MedicinalProductUndesirableEffect
     */
    public static final FHIRResourceType MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT = FHIRResourceType.of(ValueSet.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT);

    /**
     * MessageDefinition
     */
    public static final FHIRResourceType MESSAGE_DEFINITION = FHIRResourceType.of(ValueSet.MESSAGE_DEFINITION);

    /**
     * MessageHeader
     */
    public static final FHIRResourceType MESSAGE_HEADER = FHIRResourceType.of(ValueSet.MESSAGE_HEADER);

    /**
     * MolecularSequence
     */
    public static final FHIRResourceType MOLECULAR_SEQUENCE = FHIRResourceType.of(ValueSet.MOLECULAR_SEQUENCE);

    /**
     * NamingSystem
     */
    public static final FHIRResourceType NAMING_SYSTEM = FHIRResourceType.of(ValueSet.NAMING_SYSTEM);

    /**
     * NutritionOrder
     */
    public static final FHIRResourceType NUTRITION_ORDER = FHIRResourceType.of(ValueSet.NUTRITION_ORDER);

    /**
     * Observation
     */
    public static final FHIRResourceType OBSERVATION = FHIRResourceType.of(ValueSet.OBSERVATION);

    /**
     * ObservationDefinition
     */
    public static final FHIRResourceType OBSERVATION_DEFINITION = FHIRResourceType.of(ValueSet.OBSERVATION_DEFINITION);

    /**
     * OperationDefinition
     */
    public static final FHIRResourceType OPERATION_DEFINITION = FHIRResourceType.of(ValueSet.OPERATION_DEFINITION);

    /**
     * OperationOutcome
     */
    public static final FHIRResourceType OPERATION_OUTCOME = FHIRResourceType.of(ValueSet.OPERATION_OUTCOME);

    /**
     * Organization
     */
    public static final FHIRResourceType ORGANIZATION = FHIRResourceType.of(ValueSet.ORGANIZATION);

    /**
     * OrganizationAffiliation
     */
    public static final FHIRResourceType ORGANIZATION_AFFILIATION = FHIRResourceType.of(ValueSet.ORGANIZATION_AFFILIATION);

    /**
     * Parameters
     */
    public static final FHIRResourceType PARAMETERS = FHIRResourceType.of(ValueSet.PARAMETERS);

    /**
     * Patient
     */
    public static final FHIRResourceType PATIENT = FHIRResourceType.of(ValueSet.PATIENT);

    /**
     * PaymentNotice
     */
    public static final FHIRResourceType PAYMENT_NOTICE = FHIRResourceType.of(ValueSet.PAYMENT_NOTICE);

    /**
     * PaymentReconciliation
     */
    public static final FHIRResourceType PAYMENT_RECONCILIATION = FHIRResourceType.of(ValueSet.PAYMENT_RECONCILIATION);

    /**
     * Person
     */
    public static final FHIRResourceType PERSON = FHIRResourceType.of(ValueSet.PERSON);

    /**
     * PlanDefinition
     */
    public static final FHIRResourceType PLAN_DEFINITION = FHIRResourceType.of(ValueSet.PLAN_DEFINITION);

    /**
     * Practitioner
     */
    public static final FHIRResourceType PRACTITIONER = FHIRResourceType.of(ValueSet.PRACTITIONER);

    /**
     * PractitionerRole
     */
    public static final FHIRResourceType PRACTITIONER_ROLE = FHIRResourceType.of(ValueSet.PRACTITIONER_ROLE);

    /**
     * Procedure
     */
    public static final FHIRResourceType PROCEDURE = FHIRResourceType.of(ValueSet.PROCEDURE);

    /**
     * Provenance
     */
    public static final FHIRResourceType PROVENANCE = FHIRResourceType.of(ValueSet.PROVENANCE);

    /**
     * Questionnaire
     */
    public static final FHIRResourceType QUESTIONNAIRE = FHIRResourceType.of(ValueSet.QUESTIONNAIRE);

    /**
     * QuestionnaireResponse
     */
    public static final FHIRResourceType QUESTIONNAIRE_RESPONSE = FHIRResourceType.of(ValueSet.QUESTIONNAIRE_RESPONSE);

    /**
     * RelatedPerson
     */
    public static final FHIRResourceType RELATED_PERSON = FHIRResourceType.of(ValueSet.RELATED_PERSON);

    /**
     * RequestGroup
     */
    public static final FHIRResourceType REQUEST_GROUP = FHIRResourceType.of(ValueSet.REQUEST_GROUP);

    /**
     * ResearchDefinition
     */
    public static final FHIRResourceType RESEARCH_DEFINITION = FHIRResourceType.of(ValueSet.RESEARCH_DEFINITION);

    /**
     * ResearchElementDefinition
     */
    public static final FHIRResourceType RESEARCH_ELEMENT_DEFINITION = FHIRResourceType.of(ValueSet.RESEARCH_ELEMENT_DEFINITION);

    /**
     * ResearchStudy
     */
    public static final FHIRResourceType RESEARCH_STUDY = FHIRResourceType.of(ValueSet.RESEARCH_STUDY);

    /**
     * ResearchSubject
     */
    public static final FHIRResourceType RESEARCH_SUBJECT = FHIRResourceType.of(ValueSet.RESEARCH_SUBJECT);

    /**
     * Resource
     */
    public static final FHIRResourceType RESOURCE = FHIRResourceType.of(ValueSet.RESOURCE);

    /**
     * RiskAssessment
     */
    public static final FHIRResourceType RISK_ASSESSMENT = FHIRResourceType.of(ValueSet.RISK_ASSESSMENT);

    /**
     * RiskEvidenceSynthesis
     */
    public static final FHIRResourceType RISK_EVIDENCE_SYNTHESIS = FHIRResourceType.of(ValueSet.RISK_EVIDENCE_SYNTHESIS);

    /**
     * Schedule
     */
    public static final FHIRResourceType SCHEDULE = FHIRResourceType.of(ValueSet.SCHEDULE);

    /**
     * SearchParameter
     */
    public static final FHIRResourceType SEARCH_PARAMETER = FHIRResourceType.of(ValueSet.SEARCH_PARAMETER);

    /**
     * ServiceRequest
     */
    public static final FHIRResourceType SERVICE_REQUEST = FHIRResourceType.of(ValueSet.SERVICE_REQUEST);

    /**
     * Slot
     */
    public static final FHIRResourceType SLOT = FHIRResourceType.of(ValueSet.SLOT);

    /**
     * Specimen
     */
    public static final FHIRResourceType SPECIMEN = FHIRResourceType.of(ValueSet.SPECIMEN);

    /**
     * SpecimenDefinition
     */
    public static final FHIRResourceType SPECIMEN_DEFINITION = FHIRResourceType.of(ValueSet.SPECIMEN_DEFINITION);

    /**
     * StructureDefinition
     */
    public static final FHIRResourceType STRUCTURE_DEFINITION = FHIRResourceType.of(ValueSet.STRUCTURE_DEFINITION);

    /**
     * StructureMap
     */
    public static final FHIRResourceType STRUCTURE_MAP = FHIRResourceType.of(ValueSet.STRUCTURE_MAP);

    /**
     * Subscription
     */
    public static final FHIRResourceType SUBSCRIPTION = FHIRResourceType.of(ValueSet.SUBSCRIPTION);

    /**
     * Substance
     */
    public static final FHIRResourceType SUBSTANCE = FHIRResourceType.of(ValueSet.SUBSTANCE);

    /**
     * SubstanceNucleicAcid
     */
    public static final FHIRResourceType SUBSTANCE_NUCLEIC_ACID = FHIRResourceType.of(ValueSet.SUBSTANCE_NUCLEIC_ACID);

    /**
     * SubstancePolymer
     */
    public static final FHIRResourceType SUBSTANCE_POLYMER = FHIRResourceType.of(ValueSet.SUBSTANCE_POLYMER);

    /**
     * SubstanceProtein
     */
    public static final FHIRResourceType SUBSTANCE_PROTEIN = FHIRResourceType.of(ValueSet.SUBSTANCE_PROTEIN);

    /**
     * SubstanceReferenceInformation
     */
    public static final FHIRResourceType SUBSTANCE_REFERENCE_INFORMATION = FHIRResourceType.of(ValueSet.SUBSTANCE_REFERENCE_INFORMATION);

    /**
     * SubstanceSourceMaterial
     */
    public static final FHIRResourceType SUBSTANCE_SOURCE_MATERIAL = FHIRResourceType.of(ValueSet.SUBSTANCE_SOURCE_MATERIAL);

    /**
     * SubstanceSpecification
     */
    public static final FHIRResourceType SUBSTANCE_SPECIFICATION = FHIRResourceType.of(ValueSet.SUBSTANCE_SPECIFICATION);

    /**
     * SupplyDelivery
     */
    public static final FHIRResourceType SUPPLY_DELIVERY = FHIRResourceType.of(ValueSet.SUPPLY_DELIVERY);

    /**
     * SupplyRequest
     */
    public static final FHIRResourceType SUPPLY_REQUEST = FHIRResourceType.of(ValueSet.SUPPLY_REQUEST);

    /**
     * Task
     */
    public static final FHIRResourceType TASK = FHIRResourceType.of(ValueSet.TASK);

    /**
     * TerminologyCapabilities
     */
    public static final FHIRResourceType TERMINOLOGY_CAPABILITIES = FHIRResourceType.of(ValueSet.TERMINOLOGY_CAPABILITIES);

    /**
     * TestReport
     */
    public static final FHIRResourceType TEST_REPORT = FHIRResourceType.of(ValueSet.TEST_REPORT);

    /**
     * TestScript
     */
    public static final FHIRResourceType TEST_SCRIPT = FHIRResourceType.of(ValueSet.TEST_SCRIPT);

    /**
     * ValueSet
     */
    public static final FHIRResourceType VALUE_SET = FHIRResourceType.of(ValueSet.VALUE_SET);

    /**
     * VerificationResult
     */
    public static final FHIRResourceType VERIFICATION_RESULT = FHIRResourceType.of(ValueSet.VERIFICATION_RESULT);

    /**
     * VisionPrescription
     */
    public static final FHIRResourceType VISION_PRESCRIPTION = FHIRResourceType.of(ValueSet.VISION_PRESCRIPTION);

    private volatile int hashCode;

    private FHIRResourceType(Builder builder) {
        super(builder);
    }

    public static FHIRResourceType of(java.lang.String value) {
        return FHIRResourceType.builder().value(value).build();
    }

    public static FHIRResourceType of(ValueSet value) {
        return FHIRResourceType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return FHIRResourceType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return FHIRResourceType.builder().value(value).build();
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
        FHIRResourceType other = (FHIRResourceType) obj;
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
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
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
        public FHIRResourceType build() {
            return new FHIRResourceType(this);
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
