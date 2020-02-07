/*
 * (C) Copyright IBM Corp. 2019, 2020
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
public class ResourceType extends Code {
    /**
     * Account
     */
    public static final ResourceType ACCOUNT = ResourceType.builder().value(ValueSet.ACCOUNT).build();

    /**
     * ActivityDefinition
     */
    public static final ResourceType ACTIVITY_DEFINITION = ResourceType.builder().value(ValueSet.ACTIVITY_DEFINITION).build();

    /**
     * AdverseEvent
     */
    public static final ResourceType ADVERSE_EVENT = ResourceType.builder().value(ValueSet.ADVERSE_EVENT).build();

    /**
     * AllergyIntolerance
     */
    public static final ResourceType ALLERGY_INTOLERANCE = ResourceType.builder().value(ValueSet.ALLERGY_INTOLERANCE).build();

    /**
     * Appointment
     */
    public static final ResourceType APPOINTMENT = ResourceType.builder().value(ValueSet.APPOINTMENT).build();

    /**
     * AppointmentResponse
     */
    public static final ResourceType APPOINTMENT_RESPONSE = ResourceType.builder().value(ValueSet.APPOINTMENT_RESPONSE).build();

    /**
     * AuditEvent
     */
    public static final ResourceType AUDIT_EVENT = ResourceType.builder().value(ValueSet.AUDIT_EVENT).build();

    /**
     * Basic
     */
    public static final ResourceType BASIC = ResourceType.builder().value(ValueSet.BASIC).build();

    /**
     * Binary
     */
    public static final ResourceType BINARY = ResourceType.builder().value(ValueSet.BINARY).build();

    /**
     * BiologicallyDerivedProduct
     */
    public static final ResourceType BIOLOGICALLY_DERIVED_PRODUCT = ResourceType.builder().value(ValueSet.BIOLOGICALLY_DERIVED_PRODUCT).build();

    /**
     * BodyStructure
     */
    public static final ResourceType BODY_STRUCTURE = ResourceType.builder().value(ValueSet.BODY_STRUCTURE).build();

    /**
     * Bundle
     */
    public static final ResourceType BUNDLE = ResourceType.builder().value(ValueSet.BUNDLE).build();

    /**
     * CapabilityStatement
     */
    public static final ResourceType CAPABILITY_STATEMENT = ResourceType.builder().value(ValueSet.CAPABILITY_STATEMENT).build();

    /**
     * CarePlan
     */
    public static final ResourceType CARE_PLAN = ResourceType.builder().value(ValueSet.CARE_PLAN).build();

    /**
     * CareTeam
     */
    public static final ResourceType CARE_TEAM = ResourceType.builder().value(ValueSet.CARE_TEAM).build();

    /**
     * CatalogEntry
     */
    public static final ResourceType CATALOG_ENTRY = ResourceType.builder().value(ValueSet.CATALOG_ENTRY).build();

    /**
     * ChargeItem
     */
    public static final ResourceType CHARGE_ITEM = ResourceType.builder().value(ValueSet.CHARGE_ITEM).build();

    /**
     * ChargeItemDefinition
     */
    public static final ResourceType CHARGE_ITEM_DEFINITION = ResourceType.builder().value(ValueSet.CHARGE_ITEM_DEFINITION).build();

    /**
     * Claim
     */
    public static final ResourceType CLAIM = ResourceType.builder().value(ValueSet.CLAIM).build();

    /**
     * ClaimResponse
     */
    public static final ResourceType CLAIM_RESPONSE = ResourceType.builder().value(ValueSet.CLAIM_RESPONSE).build();

    /**
     * ClinicalImpression
     */
    public static final ResourceType CLINICAL_IMPRESSION = ResourceType.builder().value(ValueSet.CLINICAL_IMPRESSION).build();

    /**
     * CodeSystem
     */
    public static final ResourceType CODE_SYSTEM = ResourceType.builder().value(ValueSet.CODE_SYSTEM).build();

    /**
     * Communication
     */
    public static final ResourceType COMMUNICATION = ResourceType.builder().value(ValueSet.COMMUNICATION).build();

    /**
     * CommunicationRequest
     */
    public static final ResourceType COMMUNICATION_REQUEST = ResourceType.builder().value(ValueSet.COMMUNICATION_REQUEST).build();

    /**
     * CompartmentDefinition
     */
    public static final ResourceType COMPARTMENT_DEFINITION = ResourceType.builder().value(ValueSet.COMPARTMENT_DEFINITION).build();

    /**
     * Composition
     */
    public static final ResourceType COMPOSITION = ResourceType.builder().value(ValueSet.COMPOSITION).build();

    /**
     * ConceptMap
     */
    public static final ResourceType CONCEPT_MAP = ResourceType.builder().value(ValueSet.CONCEPT_MAP).build();

    /**
     * Condition
     */
    public static final ResourceType CONDITION = ResourceType.builder().value(ValueSet.CONDITION).build();

    /**
     * Consent
     */
    public static final ResourceType CONSENT = ResourceType.builder().value(ValueSet.CONSENT).build();

    /**
     * Contract
     */
    public static final ResourceType CONTRACT = ResourceType.builder().value(ValueSet.CONTRACT).build();

    /**
     * Coverage
     */
    public static final ResourceType COVERAGE = ResourceType.builder().value(ValueSet.COVERAGE).build();

    /**
     * CoverageEligibilityRequest
     */
    public static final ResourceType COVERAGE_ELIGIBILITY_REQUEST = ResourceType.builder().value(ValueSet.COVERAGE_ELIGIBILITY_REQUEST).build();

    /**
     * CoverageEligibilityResponse
     */
    public static final ResourceType COVERAGE_ELIGIBILITY_RESPONSE = ResourceType.builder().value(ValueSet.COVERAGE_ELIGIBILITY_RESPONSE).build();

    /**
     * DetectedIssue
     */
    public static final ResourceType DETECTED_ISSUE = ResourceType.builder().value(ValueSet.DETECTED_ISSUE).build();

    /**
     * Device
     */
    public static final ResourceType DEVICE = ResourceType.builder().value(ValueSet.DEVICE).build();

    /**
     * DeviceDefinition
     */
    public static final ResourceType DEVICE_DEFINITION = ResourceType.builder().value(ValueSet.DEVICE_DEFINITION).build();

    /**
     * DeviceMetric
     */
    public static final ResourceType DEVICE_METRIC = ResourceType.builder().value(ValueSet.DEVICE_METRIC).build();

    /**
     * DeviceRequest
     */
    public static final ResourceType DEVICE_REQUEST = ResourceType.builder().value(ValueSet.DEVICE_REQUEST).build();

    /**
     * DeviceUseStatement
     */
    public static final ResourceType DEVICE_USE_STATEMENT = ResourceType.builder().value(ValueSet.DEVICE_USE_STATEMENT).build();

    /**
     * DiagnosticReport
     */
    public static final ResourceType DIAGNOSTIC_REPORT = ResourceType.builder().value(ValueSet.DIAGNOSTIC_REPORT).build();

    /**
     * DocumentManifest
     */
    public static final ResourceType DOCUMENT_MANIFEST = ResourceType.builder().value(ValueSet.DOCUMENT_MANIFEST).build();

    /**
     * DocumentReference
     */
    public static final ResourceType DOCUMENT_REFERENCE = ResourceType.builder().value(ValueSet.DOCUMENT_REFERENCE).build();

    /**
     * DomainResource
     */
    public static final ResourceType DOMAIN_RESOURCE = ResourceType.builder().value(ValueSet.DOMAIN_RESOURCE).build();

    /**
     * EffectEvidenceSynthesis
     */
    public static final ResourceType EFFECT_EVIDENCE_SYNTHESIS = ResourceType.builder().value(ValueSet.EFFECT_EVIDENCE_SYNTHESIS).build();

    /**
     * Encounter
     */
    public static final ResourceType ENCOUNTER = ResourceType.builder().value(ValueSet.ENCOUNTER).build();

    /**
     * Endpoint
     */
    public static final ResourceType ENDPOINT = ResourceType.builder().value(ValueSet.ENDPOINT).build();

    /**
     * EnrollmentRequest
     */
    public static final ResourceType ENROLLMENT_REQUEST = ResourceType.builder().value(ValueSet.ENROLLMENT_REQUEST).build();

    /**
     * EnrollmentResponse
     */
    public static final ResourceType ENROLLMENT_RESPONSE = ResourceType.builder().value(ValueSet.ENROLLMENT_RESPONSE).build();

    /**
     * EpisodeOfCare
     */
    public static final ResourceType EPISODE_OF_CARE = ResourceType.builder().value(ValueSet.EPISODE_OF_CARE).build();

    /**
     * EventDefinition
     */
    public static final ResourceType EVENT_DEFINITION = ResourceType.builder().value(ValueSet.EVENT_DEFINITION).build();

    /**
     * Evidence
     */
    public static final ResourceType EVIDENCE = ResourceType.builder().value(ValueSet.EVIDENCE).build();

    /**
     * EvidenceVariable
     */
    public static final ResourceType EVIDENCE_VARIABLE = ResourceType.builder().value(ValueSet.EVIDENCE_VARIABLE).build();

    /**
     * ExampleScenario
     */
    public static final ResourceType EXAMPLE_SCENARIO = ResourceType.builder().value(ValueSet.EXAMPLE_SCENARIO).build();

    /**
     * ExplanationOfBenefit
     */
    public static final ResourceType EXPLANATION_OF_BENEFIT = ResourceType.builder().value(ValueSet.EXPLANATION_OF_BENEFIT).build();

    /**
     * FamilyMemberHistory
     */
    public static final ResourceType FAMILY_MEMBER_HISTORY = ResourceType.builder().value(ValueSet.FAMILY_MEMBER_HISTORY).build();

    /**
     * Flag
     */
    public static final ResourceType FLAG = ResourceType.builder().value(ValueSet.FLAG).build();

    /**
     * Goal
     */
    public static final ResourceType GOAL = ResourceType.builder().value(ValueSet.GOAL).build();

    /**
     * GraphDefinition
     */
    public static final ResourceType GRAPH_DEFINITION = ResourceType.builder().value(ValueSet.GRAPH_DEFINITION).build();

    /**
     * Group
     */
    public static final ResourceType GROUP = ResourceType.builder().value(ValueSet.GROUP).build();

    /**
     * GuidanceResponse
     */
    public static final ResourceType GUIDANCE_RESPONSE = ResourceType.builder().value(ValueSet.GUIDANCE_RESPONSE).build();

    /**
     * HealthcareService
     */
    public static final ResourceType HEALTHCARE_SERVICE = ResourceType.builder().value(ValueSet.HEALTHCARE_SERVICE).build();

    /**
     * ImagingStudy
     */
    public static final ResourceType IMAGING_STUDY = ResourceType.builder().value(ValueSet.IMAGING_STUDY).build();

    /**
     * Immunization
     */
    public static final ResourceType IMMUNIZATION = ResourceType.builder().value(ValueSet.IMMUNIZATION).build();

    /**
     * ImmunizationEvaluation
     */
    public static final ResourceType IMMUNIZATION_EVALUATION = ResourceType.builder().value(ValueSet.IMMUNIZATION_EVALUATION).build();

    /**
     * ImmunizationRecommendation
     */
    public static final ResourceType IMMUNIZATION_RECOMMENDATION = ResourceType.builder().value(ValueSet.IMMUNIZATION_RECOMMENDATION).build();

    /**
     * ImplementationGuide
     */
    public static final ResourceType IMPLEMENTATION_GUIDE = ResourceType.builder().value(ValueSet.IMPLEMENTATION_GUIDE).build();

    /**
     * InsurancePlan
     */
    public static final ResourceType INSURANCE_PLAN = ResourceType.builder().value(ValueSet.INSURANCE_PLAN).build();

    /**
     * Invoice
     */
    public static final ResourceType INVOICE = ResourceType.builder().value(ValueSet.INVOICE).build();

    /**
     * Library
     */
    public static final ResourceType LIBRARY = ResourceType.builder().value(ValueSet.LIBRARY).build();

    /**
     * Linkage
     */
    public static final ResourceType LINKAGE = ResourceType.builder().value(ValueSet.LINKAGE).build();

    /**
     * List
     */
    public static final ResourceType LIST = ResourceType.builder().value(ValueSet.LIST).build();

    /**
     * Location
     */
    public static final ResourceType LOCATION = ResourceType.builder().value(ValueSet.LOCATION).build();

    /**
     * Measure
     */
    public static final ResourceType MEASURE = ResourceType.builder().value(ValueSet.MEASURE).build();

    /**
     * MeasureReport
     */
    public static final ResourceType MEASURE_REPORT = ResourceType.builder().value(ValueSet.MEASURE_REPORT).build();

    /**
     * Media
     */
    public static final ResourceType MEDIA = ResourceType.builder().value(ValueSet.MEDIA).build();

    /**
     * Medication
     */
    public static final ResourceType MEDICATION = ResourceType.builder().value(ValueSet.MEDICATION).build();

    /**
     * MedicationAdministration
     */
    public static final ResourceType MEDICATION_ADMINISTRATION = ResourceType.builder().value(ValueSet.MEDICATION_ADMINISTRATION).build();

    /**
     * MedicationDispense
     */
    public static final ResourceType MEDICATION_DISPENSE = ResourceType.builder().value(ValueSet.MEDICATION_DISPENSE).build();

    /**
     * MedicationKnowledge
     */
    public static final ResourceType MEDICATION_KNOWLEDGE = ResourceType.builder().value(ValueSet.MEDICATION_KNOWLEDGE).build();

    /**
     * MedicationRequest
     */
    public static final ResourceType MEDICATION_REQUEST = ResourceType.builder().value(ValueSet.MEDICATION_REQUEST).build();

    /**
     * MedicationStatement
     */
    public static final ResourceType MEDICATION_STATEMENT = ResourceType.builder().value(ValueSet.MEDICATION_STATEMENT).build();

    /**
     * MedicinalProduct
     */
    public static final ResourceType MEDICINAL_PRODUCT = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT).build();

    /**
     * MedicinalProductAuthorization
     */
    public static final ResourceType MEDICINAL_PRODUCT_AUTHORIZATION = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_AUTHORIZATION).build();

    /**
     * MedicinalProductContraindication
     */
    public static final ResourceType MEDICINAL_PRODUCT_CONTRAINDICATION = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_CONTRAINDICATION).build();

    /**
     * MedicinalProductIndication
     */
    public static final ResourceType MEDICINAL_PRODUCT_INDICATION = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_INDICATION).build();

    /**
     * MedicinalProductIngredient
     */
    public static final ResourceType MEDICINAL_PRODUCT_INGREDIENT = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_INGREDIENT).build();

    /**
     * MedicinalProductInteraction
     */
    public static final ResourceType MEDICINAL_PRODUCT_INTERACTION = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_INTERACTION).build();

    /**
     * MedicinalProductManufactured
     */
    public static final ResourceType MEDICINAL_PRODUCT_MANUFACTURED = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_MANUFACTURED).build();

    /**
     * MedicinalProductPackaged
     */
    public static final ResourceType MEDICINAL_PRODUCT_PACKAGED = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_PACKAGED).build();

    /**
     * MedicinalProductPharmaceutical
     */
    public static final ResourceType MEDICINAL_PRODUCT_PHARMACEUTICAL = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_PHARMACEUTICAL).build();

    /**
     * MedicinalProductUndesirableEffect
     */
    public static final ResourceType MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT = ResourceType.builder().value(ValueSet.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT).build();

    /**
     * MessageDefinition
     */
    public static final ResourceType MESSAGE_DEFINITION = ResourceType.builder().value(ValueSet.MESSAGE_DEFINITION).build();

    /**
     * MessageHeader
     */
    public static final ResourceType MESSAGE_HEADER = ResourceType.builder().value(ValueSet.MESSAGE_HEADER).build();

    /**
     * MolecularSequence
     */
    public static final ResourceType MOLECULAR_SEQUENCE = ResourceType.builder().value(ValueSet.MOLECULAR_SEQUENCE).build();

    /**
     * NamingSystem
     */
    public static final ResourceType NAMING_SYSTEM = ResourceType.builder().value(ValueSet.NAMING_SYSTEM).build();

    /**
     * NutritionOrder
     */
    public static final ResourceType NUTRITION_ORDER = ResourceType.builder().value(ValueSet.NUTRITION_ORDER).build();

    /**
     * Observation
     */
    public static final ResourceType OBSERVATION = ResourceType.builder().value(ValueSet.OBSERVATION).build();

    /**
     * ObservationDefinition
     */
    public static final ResourceType OBSERVATION_DEFINITION = ResourceType.builder().value(ValueSet.OBSERVATION_DEFINITION).build();

    /**
     * OperationDefinition
     */
    public static final ResourceType OPERATION_DEFINITION = ResourceType.builder().value(ValueSet.OPERATION_DEFINITION).build();

    /**
     * OperationOutcome
     */
    public static final ResourceType OPERATION_OUTCOME = ResourceType.builder().value(ValueSet.OPERATION_OUTCOME).build();

    /**
     * Organization
     */
    public static final ResourceType ORGANIZATION = ResourceType.builder().value(ValueSet.ORGANIZATION).build();

    /**
     * OrganizationAffiliation
     */
    public static final ResourceType ORGANIZATION_AFFILIATION = ResourceType.builder().value(ValueSet.ORGANIZATION_AFFILIATION).build();

    /**
     * Parameters
     */
    public static final ResourceType PARAMETERS = ResourceType.builder().value(ValueSet.PARAMETERS).build();

    /**
     * Patient
     */
    public static final ResourceType PATIENT = ResourceType.builder().value(ValueSet.PATIENT).build();

    /**
     * PaymentNotice
     */
    public static final ResourceType PAYMENT_NOTICE = ResourceType.builder().value(ValueSet.PAYMENT_NOTICE).build();

    /**
     * PaymentReconciliation
     */
    public static final ResourceType PAYMENT_RECONCILIATION = ResourceType.builder().value(ValueSet.PAYMENT_RECONCILIATION).build();

    /**
     * Person
     */
    public static final ResourceType PERSON = ResourceType.builder().value(ValueSet.PERSON).build();

    /**
     * PlanDefinition
     */
    public static final ResourceType PLAN_DEFINITION = ResourceType.builder().value(ValueSet.PLAN_DEFINITION).build();

    /**
     * Practitioner
     */
    public static final ResourceType PRACTITIONER = ResourceType.builder().value(ValueSet.PRACTITIONER).build();

    /**
     * PractitionerRole
     */
    public static final ResourceType PRACTITIONER_ROLE = ResourceType.builder().value(ValueSet.PRACTITIONER_ROLE).build();

    /**
     * Procedure
     */
    public static final ResourceType PROCEDURE = ResourceType.builder().value(ValueSet.PROCEDURE).build();

    /**
     * Provenance
     */
    public static final ResourceType PROVENANCE = ResourceType.builder().value(ValueSet.PROVENANCE).build();

    /**
     * Questionnaire
     */
    public static final ResourceType QUESTIONNAIRE = ResourceType.builder().value(ValueSet.QUESTIONNAIRE).build();

    /**
     * QuestionnaireResponse
     */
    public static final ResourceType QUESTIONNAIRE_RESPONSE = ResourceType.builder().value(ValueSet.QUESTIONNAIRE_RESPONSE).build();

    /**
     * RelatedPerson
     */
    public static final ResourceType RELATED_PERSON = ResourceType.builder().value(ValueSet.RELATED_PERSON).build();

    /**
     * RequestGroup
     */
    public static final ResourceType REQUEST_GROUP = ResourceType.builder().value(ValueSet.REQUEST_GROUP).build();

    /**
     * ResearchDefinition
     */
    public static final ResourceType RESEARCH_DEFINITION = ResourceType.builder().value(ValueSet.RESEARCH_DEFINITION).build();

    /**
     * ResearchElementDefinition
     */
    public static final ResourceType RESEARCH_ELEMENT_DEFINITION = ResourceType.builder().value(ValueSet.RESEARCH_ELEMENT_DEFINITION).build();

    /**
     * ResearchStudy
     */
    public static final ResourceType RESEARCH_STUDY = ResourceType.builder().value(ValueSet.RESEARCH_STUDY).build();

    /**
     * ResearchSubject
     */
    public static final ResourceType RESEARCH_SUBJECT = ResourceType.builder().value(ValueSet.RESEARCH_SUBJECT).build();

    /**
     * Resource
     */
    public static final ResourceType RESOURCE = ResourceType.builder().value(ValueSet.RESOURCE).build();

    /**
     * RiskAssessment
     */
    public static final ResourceType RISK_ASSESSMENT = ResourceType.builder().value(ValueSet.RISK_ASSESSMENT).build();

    /**
     * RiskEvidenceSynthesis
     */
    public static final ResourceType RISK_EVIDENCE_SYNTHESIS = ResourceType.builder().value(ValueSet.RISK_EVIDENCE_SYNTHESIS).build();

    /**
     * Schedule
     */
    public static final ResourceType SCHEDULE = ResourceType.builder().value(ValueSet.SCHEDULE).build();

    /**
     * SearchParameter
     */
    public static final ResourceType SEARCH_PARAMETER = ResourceType.builder().value(ValueSet.SEARCH_PARAMETER).build();

    /**
     * ServiceRequest
     */
    public static final ResourceType SERVICE_REQUEST = ResourceType.builder().value(ValueSet.SERVICE_REQUEST).build();

    /**
     * Slot
     */
    public static final ResourceType SLOT = ResourceType.builder().value(ValueSet.SLOT).build();

    /**
     * Specimen
     */
    public static final ResourceType SPECIMEN = ResourceType.builder().value(ValueSet.SPECIMEN).build();

    /**
     * SpecimenDefinition
     */
    public static final ResourceType SPECIMEN_DEFINITION = ResourceType.builder().value(ValueSet.SPECIMEN_DEFINITION).build();

    /**
     * StructureDefinition
     */
    public static final ResourceType STRUCTURE_DEFINITION = ResourceType.builder().value(ValueSet.STRUCTURE_DEFINITION).build();

    /**
     * StructureMap
     */
    public static final ResourceType STRUCTURE_MAP = ResourceType.builder().value(ValueSet.STRUCTURE_MAP).build();

    /**
     * Subscription
     */
    public static final ResourceType SUBSCRIPTION = ResourceType.builder().value(ValueSet.SUBSCRIPTION).build();

    /**
     * Substance
     */
    public static final ResourceType SUBSTANCE = ResourceType.builder().value(ValueSet.SUBSTANCE).build();

    /**
     * SubstanceNucleicAcid
     */
    public static final ResourceType SUBSTANCE_NUCLEIC_ACID = ResourceType.builder().value(ValueSet.SUBSTANCE_NUCLEIC_ACID).build();

    /**
     * SubstancePolymer
     */
    public static final ResourceType SUBSTANCE_POLYMER = ResourceType.builder().value(ValueSet.SUBSTANCE_POLYMER).build();

    /**
     * SubstanceProtein
     */
    public static final ResourceType SUBSTANCE_PROTEIN = ResourceType.builder().value(ValueSet.SUBSTANCE_PROTEIN).build();

    /**
     * SubstanceReferenceInformation
     */
    public static final ResourceType SUBSTANCE_REFERENCE_INFORMATION = ResourceType.builder().value(ValueSet.SUBSTANCE_REFERENCE_INFORMATION).build();

    /**
     * SubstanceSourceMaterial
     */
    public static final ResourceType SUBSTANCE_SOURCE_MATERIAL = ResourceType.builder().value(ValueSet.SUBSTANCE_SOURCE_MATERIAL).build();

    /**
     * SubstanceSpecification
     */
    public static final ResourceType SUBSTANCE_SPECIFICATION = ResourceType.builder().value(ValueSet.SUBSTANCE_SPECIFICATION).build();

    /**
     * SupplyDelivery
     */
    public static final ResourceType SUPPLY_DELIVERY = ResourceType.builder().value(ValueSet.SUPPLY_DELIVERY).build();

    /**
     * SupplyRequest
     */
    public static final ResourceType SUPPLY_REQUEST = ResourceType.builder().value(ValueSet.SUPPLY_REQUEST).build();

    /**
     * Task
     */
    public static final ResourceType TASK = ResourceType.builder().value(ValueSet.TASK).build();

    /**
     * TerminologyCapabilities
     */
    public static final ResourceType TERMINOLOGY_CAPABILITIES = ResourceType.builder().value(ValueSet.TERMINOLOGY_CAPABILITIES).build();

    /**
     * TestReport
     */
    public static final ResourceType TEST_REPORT = ResourceType.builder().value(ValueSet.TEST_REPORT).build();

    /**
     * TestScript
     */
    public static final ResourceType TEST_SCRIPT = ResourceType.builder().value(ValueSet.TEST_SCRIPT).build();

    /**
     * ValueSet
     */
    public static final ResourceType VALUE_SET = ResourceType.builder().value(ValueSet.VALUE_SET).build();

    /**
     * VerificationResult
     */
    public static final ResourceType VERIFICATION_RESULT = ResourceType.builder().value(ValueSet.VERIFICATION_RESULT).build();

    /**
     * VisionPrescription
     */
    public static final ResourceType VISION_PRESCRIPTION = ResourceType.builder().value(ValueSet.VISION_PRESCRIPTION).build();

    private volatile int hashCode;

    private ResourceType(Builder builder) {
        super(builder);
    }

    public static ResourceType of(ValueSet value) {
        switch (value) {
        case ACCOUNT:
            return ACCOUNT;
        case ACTIVITY_DEFINITION:
            return ACTIVITY_DEFINITION;
        case ADVERSE_EVENT:
            return ADVERSE_EVENT;
        case ALLERGY_INTOLERANCE:
            return ALLERGY_INTOLERANCE;
        case APPOINTMENT:
            return APPOINTMENT;
        case APPOINTMENT_RESPONSE:
            return APPOINTMENT_RESPONSE;
        case AUDIT_EVENT:
            return AUDIT_EVENT;
        case BASIC:
            return BASIC;
        case BINARY:
            return BINARY;
        case BIOLOGICALLY_DERIVED_PRODUCT:
            return BIOLOGICALLY_DERIVED_PRODUCT;
        case BODY_STRUCTURE:
            return BODY_STRUCTURE;
        case BUNDLE:
            return BUNDLE;
        case CAPABILITY_STATEMENT:
            return CAPABILITY_STATEMENT;
        case CARE_PLAN:
            return CARE_PLAN;
        case CARE_TEAM:
            return CARE_TEAM;
        case CATALOG_ENTRY:
            return CATALOG_ENTRY;
        case CHARGE_ITEM:
            return CHARGE_ITEM;
        case CHARGE_ITEM_DEFINITION:
            return CHARGE_ITEM_DEFINITION;
        case CLAIM:
            return CLAIM;
        case CLAIM_RESPONSE:
            return CLAIM_RESPONSE;
        case CLINICAL_IMPRESSION:
            return CLINICAL_IMPRESSION;
        case CODE_SYSTEM:
            return CODE_SYSTEM;
        case COMMUNICATION:
            return COMMUNICATION;
        case COMMUNICATION_REQUEST:
            return COMMUNICATION_REQUEST;
        case COMPARTMENT_DEFINITION:
            return COMPARTMENT_DEFINITION;
        case COMPOSITION:
            return COMPOSITION;
        case CONCEPT_MAP:
            return CONCEPT_MAP;
        case CONDITION:
            return CONDITION;
        case CONSENT:
            return CONSENT;
        case CONTRACT:
            return CONTRACT;
        case COVERAGE:
            return COVERAGE;
        case COVERAGE_ELIGIBILITY_REQUEST:
            return COVERAGE_ELIGIBILITY_REQUEST;
        case COVERAGE_ELIGIBILITY_RESPONSE:
            return COVERAGE_ELIGIBILITY_RESPONSE;
        case DETECTED_ISSUE:
            return DETECTED_ISSUE;
        case DEVICE:
            return DEVICE;
        case DEVICE_DEFINITION:
            return DEVICE_DEFINITION;
        case DEVICE_METRIC:
            return DEVICE_METRIC;
        case DEVICE_REQUEST:
            return DEVICE_REQUEST;
        case DEVICE_USE_STATEMENT:
            return DEVICE_USE_STATEMENT;
        case DIAGNOSTIC_REPORT:
            return DIAGNOSTIC_REPORT;
        case DOCUMENT_MANIFEST:
            return DOCUMENT_MANIFEST;
        case DOCUMENT_REFERENCE:
            return DOCUMENT_REFERENCE;
        case DOMAIN_RESOURCE:
            return DOMAIN_RESOURCE;
        case EFFECT_EVIDENCE_SYNTHESIS:
            return EFFECT_EVIDENCE_SYNTHESIS;
        case ENCOUNTER:
            return ENCOUNTER;
        case ENDPOINT:
            return ENDPOINT;
        case ENROLLMENT_REQUEST:
            return ENROLLMENT_REQUEST;
        case ENROLLMENT_RESPONSE:
            return ENROLLMENT_RESPONSE;
        case EPISODE_OF_CARE:
            return EPISODE_OF_CARE;
        case EVENT_DEFINITION:
            return EVENT_DEFINITION;
        case EVIDENCE:
            return EVIDENCE;
        case EVIDENCE_VARIABLE:
            return EVIDENCE_VARIABLE;
        case EXAMPLE_SCENARIO:
            return EXAMPLE_SCENARIO;
        case EXPLANATION_OF_BENEFIT:
            return EXPLANATION_OF_BENEFIT;
        case FAMILY_MEMBER_HISTORY:
            return FAMILY_MEMBER_HISTORY;
        case FLAG:
            return FLAG;
        case GOAL:
            return GOAL;
        case GRAPH_DEFINITION:
            return GRAPH_DEFINITION;
        case GROUP:
            return GROUP;
        case GUIDANCE_RESPONSE:
            return GUIDANCE_RESPONSE;
        case HEALTHCARE_SERVICE:
            return HEALTHCARE_SERVICE;
        case IMAGING_STUDY:
            return IMAGING_STUDY;
        case IMMUNIZATION:
            return IMMUNIZATION;
        case IMMUNIZATION_EVALUATION:
            return IMMUNIZATION_EVALUATION;
        case IMMUNIZATION_RECOMMENDATION:
            return IMMUNIZATION_RECOMMENDATION;
        case IMPLEMENTATION_GUIDE:
            return IMPLEMENTATION_GUIDE;
        case INSURANCE_PLAN:
            return INSURANCE_PLAN;
        case INVOICE:
            return INVOICE;
        case LIBRARY:
            return LIBRARY;
        case LINKAGE:
            return LINKAGE;
        case LIST:
            return LIST;
        case LOCATION:
            return LOCATION;
        case MEASURE:
            return MEASURE;
        case MEASURE_REPORT:
            return MEASURE_REPORT;
        case MEDIA:
            return MEDIA;
        case MEDICATION:
            return MEDICATION;
        case MEDICATION_ADMINISTRATION:
            return MEDICATION_ADMINISTRATION;
        case MEDICATION_DISPENSE:
            return MEDICATION_DISPENSE;
        case MEDICATION_KNOWLEDGE:
            return MEDICATION_KNOWLEDGE;
        case MEDICATION_REQUEST:
            return MEDICATION_REQUEST;
        case MEDICATION_STATEMENT:
            return MEDICATION_STATEMENT;
        case MEDICINAL_PRODUCT:
            return MEDICINAL_PRODUCT;
        case MEDICINAL_PRODUCT_AUTHORIZATION:
            return MEDICINAL_PRODUCT_AUTHORIZATION;
        case MEDICINAL_PRODUCT_CONTRAINDICATION:
            return MEDICINAL_PRODUCT_CONTRAINDICATION;
        case MEDICINAL_PRODUCT_INDICATION:
            return MEDICINAL_PRODUCT_INDICATION;
        case MEDICINAL_PRODUCT_INGREDIENT:
            return MEDICINAL_PRODUCT_INGREDIENT;
        case MEDICINAL_PRODUCT_INTERACTION:
            return MEDICINAL_PRODUCT_INTERACTION;
        case MEDICINAL_PRODUCT_MANUFACTURED:
            return MEDICINAL_PRODUCT_MANUFACTURED;
        case MEDICINAL_PRODUCT_PACKAGED:
            return MEDICINAL_PRODUCT_PACKAGED;
        case MEDICINAL_PRODUCT_PHARMACEUTICAL:
            return MEDICINAL_PRODUCT_PHARMACEUTICAL;
        case MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT:
            return MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT;
        case MESSAGE_DEFINITION:
            return MESSAGE_DEFINITION;
        case MESSAGE_HEADER:
            return MESSAGE_HEADER;
        case MOLECULAR_SEQUENCE:
            return MOLECULAR_SEQUENCE;
        case NAMING_SYSTEM:
            return NAMING_SYSTEM;
        case NUTRITION_ORDER:
            return NUTRITION_ORDER;
        case OBSERVATION:
            return OBSERVATION;
        case OBSERVATION_DEFINITION:
            return OBSERVATION_DEFINITION;
        case OPERATION_DEFINITION:
            return OPERATION_DEFINITION;
        case OPERATION_OUTCOME:
            return OPERATION_OUTCOME;
        case ORGANIZATION:
            return ORGANIZATION;
        case ORGANIZATION_AFFILIATION:
            return ORGANIZATION_AFFILIATION;
        case PARAMETERS:
            return PARAMETERS;
        case PATIENT:
            return PATIENT;
        case PAYMENT_NOTICE:
            return PAYMENT_NOTICE;
        case PAYMENT_RECONCILIATION:
            return PAYMENT_RECONCILIATION;
        case PERSON:
            return PERSON;
        case PLAN_DEFINITION:
            return PLAN_DEFINITION;
        case PRACTITIONER:
            return PRACTITIONER;
        case PRACTITIONER_ROLE:
            return PRACTITIONER_ROLE;
        case PROCEDURE:
            return PROCEDURE;
        case PROVENANCE:
            return PROVENANCE;
        case QUESTIONNAIRE:
            return QUESTIONNAIRE;
        case QUESTIONNAIRE_RESPONSE:
            return QUESTIONNAIRE_RESPONSE;
        case RELATED_PERSON:
            return RELATED_PERSON;
        case REQUEST_GROUP:
            return REQUEST_GROUP;
        case RESEARCH_DEFINITION:
            return RESEARCH_DEFINITION;
        case RESEARCH_ELEMENT_DEFINITION:
            return RESEARCH_ELEMENT_DEFINITION;
        case RESEARCH_STUDY:
            return RESEARCH_STUDY;
        case RESEARCH_SUBJECT:
            return RESEARCH_SUBJECT;
        case RESOURCE:
            return RESOURCE;
        case RISK_ASSESSMENT:
            return RISK_ASSESSMENT;
        case RISK_EVIDENCE_SYNTHESIS:
            return RISK_EVIDENCE_SYNTHESIS;
        case SCHEDULE:
            return SCHEDULE;
        case SEARCH_PARAMETER:
            return SEARCH_PARAMETER;
        case SERVICE_REQUEST:
            return SERVICE_REQUEST;
        case SLOT:
            return SLOT;
        case SPECIMEN:
            return SPECIMEN;
        case SPECIMEN_DEFINITION:
            return SPECIMEN_DEFINITION;
        case STRUCTURE_DEFINITION:
            return STRUCTURE_DEFINITION;
        case STRUCTURE_MAP:
            return STRUCTURE_MAP;
        case SUBSCRIPTION:
            return SUBSCRIPTION;
        case SUBSTANCE:
            return SUBSTANCE;
        case SUBSTANCE_NUCLEIC_ACID:
            return SUBSTANCE_NUCLEIC_ACID;
        case SUBSTANCE_POLYMER:
            return SUBSTANCE_POLYMER;
        case SUBSTANCE_PROTEIN:
            return SUBSTANCE_PROTEIN;
        case SUBSTANCE_REFERENCE_INFORMATION:
            return SUBSTANCE_REFERENCE_INFORMATION;
        case SUBSTANCE_SOURCE_MATERIAL:
            return SUBSTANCE_SOURCE_MATERIAL;
        case SUBSTANCE_SPECIFICATION:
            return SUBSTANCE_SPECIFICATION;
        case SUPPLY_DELIVERY:
            return SUPPLY_DELIVERY;
        case SUPPLY_REQUEST:
            return SUPPLY_REQUEST;
        case TASK:
            return TASK;
        case TERMINOLOGY_CAPABILITIES:
            return TERMINOLOGY_CAPABILITIES;
        case TEST_REPORT:
            return TEST_REPORT;
        case TEST_SCRIPT:
            return TEST_SCRIPT;
        case VALUE_SET:
            return VALUE_SET;
        case VERIFICATION_RESULT:
            return VERIFICATION_RESULT;
        case VISION_PRESCRIPTION:
            return VISION_PRESCRIPTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ResourceType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
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
