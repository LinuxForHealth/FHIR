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
public class FHIRAllTypes extends Code {
    /**
     * Address
     */
    public static final FHIRAllTypes ADDRESS = FHIRAllTypes.of(ValueSet.ADDRESS);

    /**
     * Age
     */
    public static final FHIRAllTypes AGE = FHIRAllTypes.of(ValueSet.AGE);

    /**
     * Annotation
     */
    public static final FHIRAllTypes ANNOTATION = FHIRAllTypes.of(ValueSet.ANNOTATION);

    /**
     * Attachment
     */
    public static final FHIRAllTypes ATTACHMENT = FHIRAllTypes.of(ValueSet.ATTACHMENT);

    /**
     * BackboneElement
     */
    public static final FHIRAllTypes BACKBONE_ELEMENT = FHIRAllTypes.of(ValueSet.BACKBONE_ELEMENT);

    /**
     * CodeableConcept
     */
    public static final FHIRAllTypes CODEABLE_CONCEPT = FHIRAllTypes.of(ValueSet.CODEABLE_CONCEPT);

    /**
     * Coding
     */
    public static final FHIRAllTypes CODING = FHIRAllTypes.of(ValueSet.CODING);

    /**
     * ContactDetail
     */
    public static final FHIRAllTypes CONTACT_DETAIL = FHIRAllTypes.of(ValueSet.CONTACT_DETAIL);

    /**
     * ContactPoint
     */
    public static final FHIRAllTypes CONTACT_POINT = FHIRAllTypes.of(ValueSet.CONTACT_POINT);

    /**
     * Contributor
     */
    public static final FHIRAllTypes CONTRIBUTOR = FHIRAllTypes.of(ValueSet.CONTRIBUTOR);

    /**
     * Count
     */
    public static final FHIRAllTypes COUNT = FHIRAllTypes.of(ValueSet.COUNT);

    /**
     * DataRequirement
     */
    public static final FHIRAllTypes DATA_REQUIREMENT = FHIRAllTypes.of(ValueSet.DATA_REQUIREMENT);

    /**
     * Distance
     */
    public static final FHIRAllTypes DISTANCE = FHIRAllTypes.of(ValueSet.DISTANCE);

    /**
     * Dosage
     */
    public static final FHIRAllTypes DOSAGE = FHIRAllTypes.of(ValueSet.DOSAGE);

    /**
     * Duration
     */
    public static final FHIRAllTypes DURATION = FHIRAllTypes.of(ValueSet.DURATION);

    /**
     * Element
     */
    public static final FHIRAllTypes ELEMENT = FHIRAllTypes.of(ValueSet.ELEMENT);

    /**
     * ElementDefinition
     */
    public static final FHIRAllTypes ELEMENT_DEFINITION = FHIRAllTypes.of(ValueSet.ELEMENT_DEFINITION);

    /**
     * Expression
     */
    public static final FHIRAllTypes EXPRESSION = FHIRAllTypes.of(ValueSet.EXPRESSION);

    /**
     * Extension
     */
    public static final FHIRAllTypes EXTENSION = FHIRAllTypes.of(ValueSet.EXTENSION);

    /**
     * HumanName
     */
    public static final FHIRAllTypes HUMAN_NAME = FHIRAllTypes.of(ValueSet.HUMAN_NAME);

    /**
     * Identifier
     */
    public static final FHIRAllTypes IDENTIFIER = FHIRAllTypes.of(ValueSet.IDENTIFIER);

    /**
     * MarketingStatus
     */
    public static final FHIRAllTypes MARKETING_STATUS = FHIRAllTypes.of(ValueSet.MARKETING_STATUS);

    /**
     * Meta
     */
    public static final FHIRAllTypes META = FHIRAllTypes.of(ValueSet.META);

    /**
     * Money
     */
    public static final FHIRAllTypes MONEY = FHIRAllTypes.of(ValueSet.MONEY);

    /**
     * MoneyQuantity
     */
    public static final FHIRAllTypes MONEY_QUANTITY = FHIRAllTypes.of(ValueSet.MONEY_QUANTITY);

    /**
     * Narrative
     */
    public static final FHIRAllTypes NARRATIVE = FHIRAllTypes.of(ValueSet.NARRATIVE);

    /**
     * ParameterDefinition
     */
    public static final FHIRAllTypes PARAMETER_DEFINITION = FHIRAllTypes.of(ValueSet.PARAMETER_DEFINITION);

    /**
     * Period
     */
    public static final FHIRAllTypes PERIOD = FHIRAllTypes.of(ValueSet.PERIOD);

    /**
     * Population
     */
    public static final FHIRAllTypes POPULATION = FHIRAllTypes.of(ValueSet.POPULATION);

    /**
     * ProdCharacteristic
     */
    public static final FHIRAllTypes PROD_CHARACTERISTIC = FHIRAllTypes.of(ValueSet.PROD_CHARACTERISTIC);

    /**
     * ProductShelfLife
     */
    public static final FHIRAllTypes PRODUCT_SHELF_LIFE = FHIRAllTypes.of(ValueSet.PRODUCT_SHELF_LIFE);

    /**
     * Quantity
     */
    public static final FHIRAllTypes QUANTITY = FHIRAllTypes.of(ValueSet.QUANTITY);

    /**
     * Range
     */
    public static final FHIRAllTypes RANGE = FHIRAllTypes.of(ValueSet.RANGE);

    /**
     * Ratio
     */
    public static final FHIRAllTypes RATIO = FHIRAllTypes.of(ValueSet.RATIO);

    /**
     * Reference
     */
    public static final FHIRAllTypes REFERENCE = FHIRAllTypes.of(ValueSet.REFERENCE);

    /**
     * RelatedArtifact
     */
    public static final FHIRAllTypes RELATED_ARTIFACT = FHIRAllTypes.of(ValueSet.RELATED_ARTIFACT);

    /**
     * SampledData
     */
    public static final FHIRAllTypes SAMPLED_DATA = FHIRAllTypes.of(ValueSet.SAMPLED_DATA);

    /**
     * Signature
     */
    public static final FHIRAllTypes SIGNATURE = FHIRAllTypes.of(ValueSet.SIGNATURE);

    /**
     * SimpleQuantity
     */
    public static final FHIRAllTypes SIMPLE_QUANTITY = FHIRAllTypes.of(ValueSet.SIMPLE_QUANTITY);

    /**
     * SubstanceAmount
     */
    public static final FHIRAllTypes SUBSTANCE_AMOUNT = FHIRAllTypes.of(ValueSet.SUBSTANCE_AMOUNT);

    /**
     * Timing
     */
    public static final FHIRAllTypes TIMING = FHIRAllTypes.of(ValueSet.TIMING);

    /**
     * TriggerDefinition
     */
    public static final FHIRAllTypes TRIGGER_DEFINITION = FHIRAllTypes.of(ValueSet.TRIGGER_DEFINITION);

    /**
     * UsageContext
     */
    public static final FHIRAllTypes USAGE_CONTEXT = FHIRAllTypes.of(ValueSet.USAGE_CONTEXT);

    /**
     * base64Binary
     */
    public static final FHIRAllTypes BASE64BINARY = FHIRAllTypes.of(ValueSet.BASE64BINARY);

    /**
     * boolean
     */
    public static final FHIRAllTypes BOOLEAN = FHIRAllTypes.of(ValueSet.BOOLEAN);

    /**
     * canonical
     */
    public static final FHIRAllTypes CANONICAL = FHIRAllTypes.of(ValueSet.CANONICAL);

    /**
     * code
     */
    public static final FHIRAllTypes CODE = FHIRAllTypes.of(ValueSet.CODE);

    /**
     * date
     */
    public static final FHIRAllTypes DATE = FHIRAllTypes.of(ValueSet.DATE);

    /**
     * dateTime
     */
    public static final FHIRAllTypes DATE_TIME = FHIRAllTypes.of(ValueSet.DATE_TIME);

    /**
     * decimal
     */
    public static final FHIRAllTypes DECIMAL = FHIRAllTypes.of(ValueSet.DECIMAL);

    /**
     * id
     */
    public static final FHIRAllTypes ID = FHIRAllTypes.of(ValueSet.ID);

    /**
     * instant
     */
    public static final FHIRAllTypes INSTANT = FHIRAllTypes.of(ValueSet.INSTANT);

    /**
     * integer
     */
    public static final FHIRAllTypes INTEGER = FHIRAllTypes.of(ValueSet.INTEGER);

    /**
     * markdown
     */
    public static final FHIRAllTypes MARKDOWN = FHIRAllTypes.of(ValueSet.MARKDOWN);

    /**
     * oid
     */
    public static final FHIRAllTypes OID = FHIRAllTypes.of(ValueSet.OID);

    /**
     * positiveInt
     */
    public static final FHIRAllTypes POSITIVE_INT = FHIRAllTypes.of(ValueSet.POSITIVE_INT);

    /**
     * string
     */
    public static final FHIRAllTypes STRING = FHIRAllTypes.of(ValueSet.STRING);

    /**
     * time
     */
    public static final FHIRAllTypes TIME = FHIRAllTypes.of(ValueSet.TIME);

    /**
     * unsignedInt
     */
    public static final FHIRAllTypes UNSIGNED_INT = FHIRAllTypes.of(ValueSet.UNSIGNED_INT);

    /**
     * uri
     */
    public static final FHIRAllTypes URI = FHIRAllTypes.of(ValueSet.URI);

    /**
     * url
     */
    public static final FHIRAllTypes URL = FHIRAllTypes.of(ValueSet.URL);

    /**
     * uuid
     */
    public static final FHIRAllTypes UUID = FHIRAllTypes.of(ValueSet.UUID);

    /**
     * XHTML
     */
    public static final FHIRAllTypes XHTML = FHIRAllTypes.of(ValueSet.XHTML);

    /**
     * Account
     */
    public static final FHIRAllTypes ACCOUNT = FHIRAllTypes.of(ValueSet.ACCOUNT);

    /**
     * ActivityDefinition
     */
    public static final FHIRAllTypes ACTIVITY_DEFINITION = FHIRAllTypes.of(ValueSet.ACTIVITY_DEFINITION);

    /**
     * AdverseEvent
     */
    public static final FHIRAllTypes ADVERSE_EVENT = FHIRAllTypes.of(ValueSet.ADVERSE_EVENT);

    /**
     * AllergyIntolerance
     */
    public static final FHIRAllTypes ALLERGY_INTOLERANCE = FHIRAllTypes.of(ValueSet.ALLERGY_INTOLERANCE);

    /**
     * Appointment
     */
    public static final FHIRAllTypes APPOINTMENT = FHIRAllTypes.of(ValueSet.APPOINTMENT);

    /**
     * AppointmentResponse
     */
    public static final FHIRAllTypes APPOINTMENT_RESPONSE = FHIRAllTypes.of(ValueSet.APPOINTMENT_RESPONSE);

    /**
     * AuditEvent
     */
    public static final FHIRAllTypes AUDIT_EVENT = FHIRAllTypes.of(ValueSet.AUDIT_EVENT);

    /**
     * Basic
     */
    public static final FHIRAllTypes BASIC = FHIRAllTypes.of(ValueSet.BASIC);

    /**
     * Binary
     */
    public static final FHIRAllTypes BINARY = FHIRAllTypes.of(ValueSet.BINARY);

    /**
     * BiologicallyDerivedProduct
     */
    public static final FHIRAllTypes BIOLOGICALLY_DERIVED_PRODUCT = FHIRAllTypes.of(ValueSet.BIOLOGICALLY_DERIVED_PRODUCT);

    /**
     * BodyStructure
     */
    public static final FHIRAllTypes BODY_STRUCTURE = FHIRAllTypes.of(ValueSet.BODY_STRUCTURE);

    /**
     * Bundle
     */
    public static final FHIRAllTypes BUNDLE = FHIRAllTypes.of(ValueSet.BUNDLE);

    /**
     * CapabilityStatement
     */
    public static final FHIRAllTypes CAPABILITY_STATEMENT = FHIRAllTypes.of(ValueSet.CAPABILITY_STATEMENT);

    /**
     * CarePlan
     */
    public static final FHIRAllTypes CARE_PLAN = FHIRAllTypes.of(ValueSet.CARE_PLAN);

    /**
     * CareTeam
     */
    public static final FHIRAllTypes CARE_TEAM = FHIRAllTypes.of(ValueSet.CARE_TEAM);

    /**
     * CatalogEntry
     */
    public static final FHIRAllTypes CATALOG_ENTRY = FHIRAllTypes.of(ValueSet.CATALOG_ENTRY);

    /**
     * ChargeItem
     */
    public static final FHIRAllTypes CHARGE_ITEM = FHIRAllTypes.of(ValueSet.CHARGE_ITEM);

    /**
     * ChargeItemDefinition
     */
    public static final FHIRAllTypes CHARGE_ITEM_DEFINITION = FHIRAllTypes.of(ValueSet.CHARGE_ITEM_DEFINITION);

    /**
     * Claim
     */
    public static final FHIRAllTypes CLAIM = FHIRAllTypes.of(ValueSet.CLAIM);

    /**
     * ClaimResponse
     */
    public static final FHIRAllTypes CLAIM_RESPONSE = FHIRAllTypes.of(ValueSet.CLAIM_RESPONSE);

    /**
     * ClinicalImpression
     */
    public static final FHIRAllTypes CLINICAL_IMPRESSION = FHIRAllTypes.of(ValueSet.CLINICAL_IMPRESSION);

    /**
     * CodeSystem
     */
    public static final FHIRAllTypes CODE_SYSTEM = FHIRAllTypes.of(ValueSet.CODE_SYSTEM);

    /**
     * Communication
     */
    public static final FHIRAllTypes COMMUNICATION = FHIRAllTypes.of(ValueSet.COMMUNICATION);

    /**
     * CommunicationRequest
     */
    public static final FHIRAllTypes COMMUNICATION_REQUEST = FHIRAllTypes.of(ValueSet.COMMUNICATION_REQUEST);

    /**
     * CompartmentDefinition
     */
    public static final FHIRAllTypes COMPARTMENT_DEFINITION = FHIRAllTypes.of(ValueSet.COMPARTMENT_DEFINITION);

    /**
     * Composition
     */
    public static final FHIRAllTypes COMPOSITION = FHIRAllTypes.of(ValueSet.COMPOSITION);

    /**
     * ConceptMap
     */
    public static final FHIRAllTypes CONCEPT_MAP = FHIRAllTypes.of(ValueSet.CONCEPT_MAP);

    /**
     * Condition
     */
    public static final FHIRAllTypes CONDITION = FHIRAllTypes.of(ValueSet.CONDITION);

    /**
     * Consent
     */
    public static final FHIRAllTypes CONSENT = FHIRAllTypes.of(ValueSet.CONSENT);

    /**
     * Contract
     */
    public static final FHIRAllTypes CONTRACT = FHIRAllTypes.of(ValueSet.CONTRACT);

    /**
     * Coverage
     */
    public static final FHIRAllTypes COVERAGE = FHIRAllTypes.of(ValueSet.COVERAGE);

    /**
     * CoverageEligibilityRequest
     */
    public static final FHIRAllTypes COVERAGE_ELIGIBILITY_REQUEST = FHIRAllTypes.of(ValueSet.COVERAGE_ELIGIBILITY_REQUEST);

    /**
     * CoverageEligibilityResponse
     */
    public static final FHIRAllTypes COVERAGE_ELIGIBILITY_RESPONSE = FHIRAllTypes.of(ValueSet.COVERAGE_ELIGIBILITY_RESPONSE);

    /**
     * DetectedIssue
     */
    public static final FHIRAllTypes DETECTED_ISSUE = FHIRAllTypes.of(ValueSet.DETECTED_ISSUE);

    /**
     * Device
     */
    public static final FHIRAllTypes DEVICE = FHIRAllTypes.of(ValueSet.DEVICE);

    /**
     * DeviceDefinition
     */
    public static final FHIRAllTypes DEVICE_DEFINITION = FHIRAllTypes.of(ValueSet.DEVICE_DEFINITION);

    /**
     * DeviceMetric
     */
    public static final FHIRAllTypes DEVICE_METRIC = FHIRAllTypes.of(ValueSet.DEVICE_METRIC);

    /**
     * DeviceRequest
     */
    public static final FHIRAllTypes DEVICE_REQUEST = FHIRAllTypes.of(ValueSet.DEVICE_REQUEST);

    /**
     * DeviceUseStatement
     */
    public static final FHIRAllTypes DEVICE_USE_STATEMENT = FHIRAllTypes.of(ValueSet.DEVICE_USE_STATEMENT);

    /**
     * DiagnosticReport
     */
    public static final FHIRAllTypes DIAGNOSTIC_REPORT = FHIRAllTypes.of(ValueSet.DIAGNOSTIC_REPORT);

    /**
     * DocumentManifest
     */
    public static final FHIRAllTypes DOCUMENT_MANIFEST = FHIRAllTypes.of(ValueSet.DOCUMENT_MANIFEST);

    /**
     * DocumentReference
     */
    public static final FHIRAllTypes DOCUMENT_REFERENCE = FHIRAllTypes.of(ValueSet.DOCUMENT_REFERENCE);

    /**
     * DomainResource
     */
    public static final FHIRAllTypes DOMAIN_RESOURCE = FHIRAllTypes.of(ValueSet.DOMAIN_RESOURCE);

    /**
     * EffectEvidenceSynthesis
     */
    public static final FHIRAllTypes EFFECT_EVIDENCE_SYNTHESIS = FHIRAllTypes.of(ValueSet.EFFECT_EVIDENCE_SYNTHESIS);

    /**
     * Encounter
     */
    public static final FHIRAllTypes ENCOUNTER = FHIRAllTypes.of(ValueSet.ENCOUNTER);

    /**
     * Endpoint
     */
    public static final FHIRAllTypes ENDPOINT = FHIRAllTypes.of(ValueSet.ENDPOINT);

    /**
     * EnrollmentRequest
     */
    public static final FHIRAllTypes ENROLLMENT_REQUEST = FHIRAllTypes.of(ValueSet.ENROLLMENT_REQUEST);

    /**
     * EnrollmentResponse
     */
    public static final FHIRAllTypes ENROLLMENT_RESPONSE = FHIRAllTypes.of(ValueSet.ENROLLMENT_RESPONSE);

    /**
     * EpisodeOfCare
     */
    public static final FHIRAllTypes EPISODE_OF_CARE = FHIRAllTypes.of(ValueSet.EPISODE_OF_CARE);

    /**
     * EventDefinition
     */
    public static final FHIRAllTypes EVENT_DEFINITION = FHIRAllTypes.of(ValueSet.EVENT_DEFINITION);

    /**
     * Evidence
     */
    public static final FHIRAllTypes EVIDENCE = FHIRAllTypes.of(ValueSet.EVIDENCE);

    /**
     * EvidenceVariable
     */
    public static final FHIRAllTypes EVIDENCE_VARIABLE = FHIRAllTypes.of(ValueSet.EVIDENCE_VARIABLE);

    /**
     * ExampleScenario
     */
    public static final FHIRAllTypes EXAMPLE_SCENARIO = FHIRAllTypes.of(ValueSet.EXAMPLE_SCENARIO);

    /**
     * ExplanationOfBenefit
     */
    public static final FHIRAllTypes EXPLANATION_OF_BENEFIT = FHIRAllTypes.of(ValueSet.EXPLANATION_OF_BENEFIT);

    /**
     * FamilyMemberHistory
     */
    public static final FHIRAllTypes FAMILY_MEMBER_HISTORY = FHIRAllTypes.of(ValueSet.FAMILY_MEMBER_HISTORY);

    /**
     * Flag
     */
    public static final FHIRAllTypes FLAG = FHIRAllTypes.of(ValueSet.FLAG);

    /**
     * Goal
     */
    public static final FHIRAllTypes GOAL = FHIRAllTypes.of(ValueSet.GOAL);

    /**
     * GraphDefinition
     */
    public static final FHIRAllTypes GRAPH_DEFINITION = FHIRAllTypes.of(ValueSet.GRAPH_DEFINITION);

    /**
     * Group
     */
    public static final FHIRAllTypes GROUP = FHIRAllTypes.of(ValueSet.GROUP);

    /**
     * GuidanceResponse
     */
    public static final FHIRAllTypes GUIDANCE_RESPONSE = FHIRAllTypes.of(ValueSet.GUIDANCE_RESPONSE);

    /**
     * HealthcareService
     */
    public static final FHIRAllTypes HEALTHCARE_SERVICE = FHIRAllTypes.of(ValueSet.HEALTHCARE_SERVICE);

    /**
     * ImagingStudy
     */
    public static final FHIRAllTypes IMAGING_STUDY = FHIRAllTypes.of(ValueSet.IMAGING_STUDY);

    /**
     * Immunization
     */
    public static final FHIRAllTypes IMMUNIZATION = FHIRAllTypes.of(ValueSet.IMMUNIZATION);

    /**
     * ImmunizationEvaluation
     */
    public static final FHIRAllTypes IMMUNIZATION_EVALUATION = FHIRAllTypes.of(ValueSet.IMMUNIZATION_EVALUATION);

    /**
     * ImmunizationRecommendation
     */
    public static final FHIRAllTypes IMMUNIZATION_RECOMMENDATION = FHIRAllTypes.of(ValueSet.IMMUNIZATION_RECOMMENDATION);

    /**
     * ImplementationGuide
     */
    public static final FHIRAllTypes IMPLEMENTATION_GUIDE = FHIRAllTypes.of(ValueSet.IMPLEMENTATION_GUIDE);

    /**
     * InsurancePlan
     */
    public static final FHIRAllTypes INSURANCE_PLAN = FHIRAllTypes.of(ValueSet.INSURANCE_PLAN);

    /**
     * Invoice
     */
    public static final FHIRAllTypes INVOICE = FHIRAllTypes.of(ValueSet.INVOICE);

    /**
     * Library
     */
    public static final FHIRAllTypes LIBRARY = FHIRAllTypes.of(ValueSet.LIBRARY);

    /**
     * Linkage
     */
    public static final FHIRAllTypes LINKAGE = FHIRAllTypes.of(ValueSet.LINKAGE);

    /**
     * List
     */
    public static final FHIRAllTypes LIST = FHIRAllTypes.of(ValueSet.LIST);

    /**
     * Location
     */
    public static final FHIRAllTypes LOCATION = FHIRAllTypes.of(ValueSet.LOCATION);

    /**
     * Measure
     */
    public static final FHIRAllTypes MEASURE = FHIRAllTypes.of(ValueSet.MEASURE);

    /**
     * MeasureReport
     */
    public static final FHIRAllTypes MEASURE_REPORT = FHIRAllTypes.of(ValueSet.MEASURE_REPORT);

    /**
     * Media
     */
    public static final FHIRAllTypes MEDIA = FHIRAllTypes.of(ValueSet.MEDIA);

    /**
     * Medication
     */
    public static final FHIRAllTypes MEDICATION = FHIRAllTypes.of(ValueSet.MEDICATION);

    /**
     * MedicationAdministration
     */
    public static final FHIRAllTypes MEDICATION_ADMINISTRATION = FHIRAllTypes.of(ValueSet.MEDICATION_ADMINISTRATION);

    /**
     * MedicationDispense
     */
    public static final FHIRAllTypes MEDICATION_DISPENSE = FHIRAllTypes.of(ValueSet.MEDICATION_DISPENSE);

    /**
     * MedicationKnowledge
     */
    public static final FHIRAllTypes MEDICATION_KNOWLEDGE = FHIRAllTypes.of(ValueSet.MEDICATION_KNOWLEDGE);

    /**
     * MedicationRequest
     */
    public static final FHIRAllTypes MEDICATION_REQUEST = FHIRAllTypes.of(ValueSet.MEDICATION_REQUEST);

    /**
     * MedicationStatement
     */
    public static final FHIRAllTypes MEDICATION_STATEMENT = FHIRAllTypes.of(ValueSet.MEDICATION_STATEMENT);

    /**
     * MedicinalProduct
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT);

    /**
     * MedicinalProductAuthorization
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_AUTHORIZATION = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_AUTHORIZATION);

    /**
     * MedicinalProductContraindication
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_CONTRAINDICATION = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_CONTRAINDICATION);

    /**
     * MedicinalProductIndication
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_INDICATION = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_INDICATION);

    /**
     * MedicinalProductIngredient
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_INGREDIENT = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_INGREDIENT);

    /**
     * MedicinalProductInteraction
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_INTERACTION = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_INTERACTION);

    /**
     * MedicinalProductManufactured
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_MANUFACTURED = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_MANUFACTURED);

    /**
     * MedicinalProductPackaged
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_PACKAGED = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_PACKAGED);

    /**
     * MedicinalProductPharmaceutical
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_PHARMACEUTICAL = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_PHARMACEUTICAL);

    /**
     * MedicinalProductUndesirableEffect
     */
    public static final FHIRAllTypes MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT = FHIRAllTypes.of(ValueSet.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT);

    /**
     * MessageDefinition
     */
    public static final FHIRAllTypes MESSAGE_DEFINITION = FHIRAllTypes.of(ValueSet.MESSAGE_DEFINITION);

    /**
     * MessageHeader
     */
    public static final FHIRAllTypes MESSAGE_HEADER = FHIRAllTypes.of(ValueSet.MESSAGE_HEADER);

    /**
     * MolecularSequence
     */
    public static final FHIRAllTypes MOLECULAR_SEQUENCE = FHIRAllTypes.of(ValueSet.MOLECULAR_SEQUENCE);

    /**
     * NamingSystem
     */
    public static final FHIRAllTypes NAMING_SYSTEM = FHIRAllTypes.of(ValueSet.NAMING_SYSTEM);

    /**
     * NutritionOrder
     */
    public static final FHIRAllTypes NUTRITION_ORDER = FHIRAllTypes.of(ValueSet.NUTRITION_ORDER);

    /**
     * Observation
     */
    public static final FHIRAllTypes OBSERVATION = FHIRAllTypes.of(ValueSet.OBSERVATION);

    /**
     * ObservationDefinition
     */
    public static final FHIRAllTypes OBSERVATION_DEFINITION = FHIRAllTypes.of(ValueSet.OBSERVATION_DEFINITION);

    /**
     * OperationDefinition
     */
    public static final FHIRAllTypes OPERATION_DEFINITION = FHIRAllTypes.of(ValueSet.OPERATION_DEFINITION);

    /**
     * OperationOutcome
     */
    public static final FHIRAllTypes OPERATION_OUTCOME = FHIRAllTypes.of(ValueSet.OPERATION_OUTCOME);

    /**
     * Organization
     */
    public static final FHIRAllTypes ORGANIZATION = FHIRAllTypes.of(ValueSet.ORGANIZATION);

    /**
     * OrganizationAffiliation
     */
    public static final FHIRAllTypes ORGANIZATION_AFFILIATION = FHIRAllTypes.of(ValueSet.ORGANIZATION_AFFILIATION);

    /**
     * Parameters
     */
    public static final FHIRAllTypes PARAMETERS = FHIRAllTypes.of(ValueSet.PARAMETERS);

    /**
     * Patient
     */
    public static final FHIRAllTypes PATIENT = FHIRAllTypes.of(ValueSet.PATIENT);

    /**
     * PaymentNotice
     */
    public static final FHIRAllTypes PAYMENT_NOTICE = FHIRAllTypes.of(ValueSet.PAYMENT_NOTICE);

    /**
     * PaymentReconciliation
     */
    public static final FHIRAllTypes PAYMENT_RECONCILIATION = FHIRAllTypes.of(ValueSet.PAYMENT_RECONCILIATION);

    /**
     * Person
     */
    public static final FHIRAllTypes PERSON = FHIRAllTypes.of(ValueSet.PERSON);

    /**
     * PlanDefinition
     */
    public static final FHIRAllTypes PLAN_DEFINITION = FHIRAllTypes.of(ValueSet.PLAN_DEFINITION);

    /**
     * Practitioner
     */
    public static final FHIRAllTypes PRACTITIONER = FHIRAllTypes.of(ValueSet.PRACTITIONER);

    /**
     * PractitionerRole
     */
    public static final FHIRAllTypes PRACTITIONER_ROLE = FHIRAllTypes.of(ValueSet.PRACTITIONER_ROLE);

    /**
     * Procedure
     */
    public static final FHIRAllTypes PROCEDURE = FHIRAllTypes.of(ValueSet.PROCEDURE);

    /**
     * Provenance
     */
    public static final FHIRAllTypes PROVENANCE = FHIRAllTypes.of(ValueSet.PROVENANCE);

    /**
     * Questionnaire
     */
    public static final FHIRAllTypes QUESTIONNAIRE = FHIRAllTypes.of(ValueSet.QUESTIONNAIRE);

    /**
     * QuestionnaireResponse
     */
    public static final FHIRAllTypes QUESTIONNAIRE_RESPONSE = FHIRAllTypes.of(ValueSet.QUESTIONNAIRE_RESPONSE);

    /**
     * RelatedPerson
     */
    public static final FHIRAllTypes RELATED_PERSON = FHIRAllTypes.of(ValueSet.RELATED_PERSON);

    /**
     * RequestGroup
     */
    public static final FHIRAllTypes REQUEST_GROUP = FHIRAllTypes.of(ValueSet.REQUEST_GROUP);

    /**
     * ResearchDefinition
     */
    public static final FHIRAllTypes RESEARCH_DEFINITION = FHIRAllTypes.of(ValueSet.RESEARCH_DEFINITION);

    /**
     * ResearchElementDefinition
     */
    public static final FHIRAllTypes RESEARCH_ELEMENT_DEFINITION = FHIRAllTypes.of(ValueSet.RESEARCH_ELEMENT_DEFINITION);

    /**
     * ResearchStudy
     */
    public static final FHIRAllTypes RESEARCH_STUDY = FHIRAllTypes.of(ValueSet.RESEARCH_STUDY);

    /**
     * ResearchSubject
     */
    public static final FHIRAllTypes RESEARCH_SUBJECT = FHIRAllTypes.of(ValueSet.RESEARCH_SUBJECT);

    /**
     * Resource
     */
    public static final FHIRAllTypes RESOURCE = FHIRAllTypes.of(ValueSet.RESOURCE);

    /**
     * RiskAssessment
     */
    public static final FHIRAllTypes RISK_ASSESSMENT = FHIRAllTypes.of(ValueSet.RISK_ASSESSMENT);

    /**
     * RiskEvidenceSynthesis
     */
    public static final FHIRAllTypes RISK_EVIDENCE_SYNTHESIS = FHIRAllTypes.of(ValueSet.RISK_EVIDENCE_SYNTHESIS);

    /**
     * Schedule
     */
    public static final FHIRAllTypes SCHEDULE = FHIRAllTypes.of(ValueSet.SCHEDULE);

    /**
     * SearchParameter
     */
    public static final FHIRAllTypes SEARCH_PARAMETER = FHIRAllTypes.of(ValueSet.SEARCH_PARAMETER);

    /**
     * ServiceRequest
     */
    public static final FHIRAllTypes SERVICE_REQUEST = FHIRAllTypes.of(ValueSet.SERVICE_REQUEST);

    /**
     * Slot
     */
    public static final FHIRAllTypes SLOT = FHIRAllTypes.of(ValueSet.SLOT);

    /**
     * Specimen
     */
    public static final FHIRAllTypes SPECIMEN = FHIRAllTypes.of(ValueSet.SPECIMEN);

    /**
     * SpecimenDefinition
     */
    public static final FHIRAllTypes SPECIMEN_DEFINITION = FHIRAllTypes.of(ValueSet.SPECIMEN_DEFINITION);

    /**
     * StructureDefinition
     */
    public static final FHIRAllTypes STRUCTURE_DEFINITION = FHIRAllTypes.of(ValueSet.STRUCTURE_DEFINITION);

    /**
     * StructureMap
     */
    public static final FHIRAllTypes STRUCTURE_MAP = FHIRAllTypes.of(ValueSet.STRUCTURE_MAP);

    /**
     * Subscription
     */
    public static final FHIRAllTypes SUBSCRIPTION = FHIRAllTypes.of(ValueSet.SUBSCRIPTION);

    /**
     * Substance
     */
    public static final FHIRAllTypes SUBSTANCE = FHIRAllTypes.of(ValueSet.SUBSTANCE);

    /**
     * SubstanceNucleicAcid
     */
    public static final FHIRAllTypes SUBSTANCE_NUCLEIC_ACID = FHIRAllTypes.of(ValueSet.SUBSTANCE_NUCLEIC_ACID);

    /**
     * SubstancePolymer
     */
    public static final FHIRAllTypes SUBSTANCE_POLYMER = FHIRAllTypes.of(ValueSet.SUBSTANCE_POLYMER);

    /**
     * SubstanceProtein
     */
    public static final FHIRAllTypes SUBSTANCE_PROTEIN = FHIRAllTypes.of(ValueSet.SUBSTANCE_PROTEIN);

    /**
     * SubstanceReferenceInformation
     */
    public static final FHIRAllTypes SUBSTANCE_REFERENCE_INFORMATION = FHIRAllTypes.of(ValueSet.SUBSTANCE_REFERENCE_INFORMATION);

    /**
     * SubstanceSourceMaterial
     */
    public static final FHIRAllTypes SUBSTANCE_SOURCE_MATERIAL = FHIRAllTypes.of(ValueSet.SUBSTANCE_SOURCE_MATERIAL);

    /**
     * SubstanceSpecification
     */
    public static final FHIRAllTypes SUBSTANCE_SPECIFICATION = FHIRAllTypes.of(ValueSet.SUBSTANCE_SPECIFICATION);

    /**
     * SupplyDelivery
     */
    public static final FHIRAllTypes SUPPLY_DELIVERY = FHIRAllTypes.of(ValueSet.SUPPLY_DELIVERY);

    /**
     * SupplyRequest
     */
    public static final FHIRAllTypes SUPPLY_REQUEST = FHIRAllTypes.of(ValueSet.SUPPLY_REQUEST);

    /**
     * Task
     */
    public static final FHIRAllTypes TASK = FHIRAllTypes.of(ValueSet.TASK);

    /**
     * TerminologyCapabilities
     */
    public static final FHIRAllTypes TERMINOLOGY_CAPABILITIES = FHIRAllTypes.of(ValueSet.TERMINOLOGY_CAPABILITIES);

    /**
     * TestReport
     */
    public static final FHIRAllTypes TEST_REPORT = FHIRAllTypes.of(ValueSet.TEST_REPORT);

    /**
     * TestScript
     */
    public static final FHIRAllTypes TEST_SCRIPT = FHIRAllTypes.of(ValueSet.TEST_SCRIPT);

    /**
     * ValueSet
     */
    public static final FHIRAllTypes VALUE_SET = FHIRAllTypes.of(ValueSet.VALUE_SET);

    /**
     * VerificationResult
     */
    public static final FHIRAllTypes VERIFICATION_RESULT = FHIRAllTypes.of(ValueSet.VERIFICATION_RESULT);

    /**
     * VisionPrescription
     */
    public static final FHIRAllTypes VISION_PRESCRIPTION = FHIRAllTypes.of(ValueSet.VISION_PRESCRIPTION);

    /**
     * Type
     */
    public static final FHIRAllTypes TYPE = FHIRAllTypes.of(ValueSet.TYPE);

    /**
     * Any
     */
    public static final FHIRAllTypes ANY = FHIRAllTypes.of(ValueSet.ANY);

    private volatile int hashCode;

    private FHIRAllTypes(Builder builder) {
        super(builder);
    }

    public static FHIRAllTypes of(java.lang.String value) {
        return FHIRAllTypes.builder().value(value).build();
    }

    public static FHIRAllTypes of(ValueSet value) {
        return FHIRAllTypes.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return FHIRAllTypes.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return FHIRAllTypes.builder().value(value).build();
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
        FHIRAllTypes other = (FHIRAllTypes) obj;
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
        public FHIRAllTypes build() {
            return new FHIRAllTypes(this);
        }
    }

    public enum ValueSet {
        /**
         * Address
         */
        ADDRESS("Address"),

        /**
         * Age
         */
        AGE("Age"),

        /**
         * Annotation
         */
        ANNOTATION("Annotation"),

        /**
         * Attachment
         */
        ATTACHMENT("Attachment"),

        /**
         * BackboneElement
         */
        BACKBONE_ELEMENT("BackboneElement"),

        /**
         * CodeableConcept
         */
        CODEABLE_CONCEPT("CodeableConcept"),

        /**
         * Coding
         */
        CODING("Coding"),

        /**
         * ContactDetail
         */
        CONTACT_DETAIL("ContactDetail"),

        /**
         * ContactPoint
         */
        CONTACT_POINT("ContactPoint"),

        /**
         * Contributor
         */
        CONTRIBUTOR("Contributor"),

        /**
         * Count
         */
        COUNT("Count"),

        /**
         * DataRequirement
         */
        DATA_REQUIREMENT("DataRequirement"),

        /**
         * Distance
         */
        DISTANCE("Distance"),

        /**
         * Dosage
         */
        DOSAGE("Dosage"),

        /**
         * Duration
         */
        DURATION("Duration"),

        /**
         * Element
         */
        ELEMENT("Element"),

        /**
         * ElementDefinition
         */
        ELEMENT_DEFINITION("ElementDefinition"),

        /**
         * Expression
         */
        EXPRESSION("Expression"),

        /**
         * Extension
         */
        EXTENSION("Extension"),

        /**
         * HumanName
         */
        HUMAN_NAME("HumanName"),

        /**
         * Identifier
         */
        IDENTIFIER("Identifier"),

        /**
         * MarketingStatus
         */
        MARKETING_STATUS("MarketingStatus"),

        /**
         * Meta
         */
        META("Meta"),

        /**
         * Money
         */
        MONEY("Money"),

        /**
         * MoneyQuantity
         */
        MONEY_QUANTITY("MoneyQuantity"),

        /**
         * Narrative
         */
        NARRATIVE("Narrative"),

        /**
         * ParameterDefinition
         */
        PARAMETER_DEFINITION("ParameterDefinition"),

        /**
         * Period
         */
        PERIOD("Period"),

        /**
         * Population
         */
        POPULATION("Population"),

        /**
         * ProdCharacteristic
         */
        PROD_CHARACTERISTIC("ProdCharacteristic"),

        /**
         * ProductShelfLife
         */
        PRODUCT_SHELF_LIFE("ProductShelfLife"),

        /**
         * Quantity
         */
        QUANTITY("Quantity"),

        /**
         * Range
         */
        RANGE("Range"),

        /**
         * Ratio
         */
        RATIO("Ratio"),

        /**
         * Reference
         */
        REFERENCE("Reference"),

        /**
         * RelatedArtifact
         */
        RELATED_ARTIFACT("RelatedArtifact"),

        /**
         * SampledData
         */
        SAMPLED_DATA("SampledData"),

        /**
         * Signature
         */
        SIGNATURE("Signature"),

        /**
         * SimpleQuantity
         */
        SIMPLE_QUANTITY("SimpleQuantity"),

        /**
         * SubstanceAmount
         */
        SUBSTANCE_AMOUNT("SubstanceAmount"),

        /**
         * Timing
         */
        TIMING("Timing"),

        /**
         * TriggerDefinition
         */
        TRIGGER_DEFINITION("TriggerDefinition"),

        /**
         * UsageContext
         */
        USAGE_CONTEXT("UsageContext"),

        /**
         * base64Binary
         */
        BASE64BINARY("base64Binary"),

        /**
         * boolean
         */
        BOOLEAN("boolean"),

        /**
         * canonical
         */
        CANONICAL("canonical"),

        /**
         * code
         */
        CODE("code"),

        /**
         * date
         */
        DATE("date"),

        /**
         * dateTime
         */
        DATE_TIME("dateTime"),

        /**
         * decimal
         */
        DECIMAL("decimal"),

        /**
         * id
         */
        ID("id"),

        /**
         * instant
         */
        INSTANT("instant"),

        /**
         * integer
         */
        INTEGER("integer"),

        /**
         * markdown
         */
        MARKDOWN("markdown"),

        /**
         * oid
         */
        OID("oid"),

        /**
         * positiveInt
         */
        POSITIVE_INT("positiveInt"),

        /**
         * string
         */
        STRING("string"),

        /**
         * time
         */
        TIME("time"),

        /**
         * unsignedInt
         */
        UNSIGNED_INT("unsignedInt"),

        /**
         * uri
         */
        URI("uri"),

        /**
         * url
         */
        URL("url"),

        /**
         * uuid
         */
        UUID("uuid"),

        /**
         * XHTML
         */
        XHTML("xhtml"),

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
        VISION_PRESCRIPTION("VisionPrescription"),

        /**
         * Type
         */
        TYPE("Type"),

        /**
         * Any
         */
        ANY("Any");

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
