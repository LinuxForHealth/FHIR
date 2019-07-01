/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class FHIRDefinedType extends Code {
    /**
     * Address
     */
    public static final FHIRDefinedType ADDRESS = FHIRDefinedType.of(ValueSet.ADDRESS);

    /**
     * Age
     */
    public static final FHIRDefinedType AGE = FHIRDefinedType.of(ValueSet.AGE);

    /**
     * Annotation
     */
    public static final FHIRDefinedType ANNOTATION = FHIRDefinedType.of(ValueSet.ANNOTATION);

    /**
     * Attachment
     */
    public static final FHIRDefinedType ATTACHMENT = FHIRDefinedType.of(ValueSet.ATTACHMENT);

    /**
     * BackboneElement
     */
    public static final FHIRDefinedType BACKBONE_ELEMENT = FHIRDefinedType.of(ValueSet.BACKBONE_ELEMENT);

    /**
     * CodeableConcept
     */
    public static final FHIRDefinedType CODEABLE_CONCEPT = FHIRDefinedType.of(ValueSet.CODEABLE_CONCEPT);

    /**
     * Coding
     */
    public static final FHIRDefinedType CODING = FHIRDefinedType.of(ValueSet.CODING);

    /**
     * ContactDetail
     */
    public static final FHIRDefinedType CONTACT_DETAIL = FHIRDefinedType.of(ValueSet.CONTACT_DETAIL);

    /**
     * ContactPoint
     */
    public static final FHIRDefinedType CONTACT_POINT = FHIRDefinedType.of(ValueSet.CONTACT_POINT);

    /**
     * Contributor
     */
    public static final FHIRDefinedType CONTRIBUTOR = FHIRDefinedType.of(ValueSet.CONTRIBUTOR);

    /**
     * Count
     */
    public static final FHIRDefinedType COUNT = FHIRDefinedType.of(ValueSet.COUNT);

    /**
     * DataRequirement
     */
    public static final FHIRDefinedType DATA_REQUIREMENT = FHIRDefinedType.of(ValueSet.DATA_REQUIREMENT);

    /**
     * Distance
     */
    public static final FHIRDefinedType DISTANCE = FHIRDefinedType.of(ValueSet.DISTANCE);

    /**
     * Dosage
     */
    public static final FHIRDefinedType DOSAGE = FHIRDefinedType.of(ValueSet.DOSAGE);

    /**
     * Duration
     */
    public static final FHIRDefinedType DURATION = FHIRDefinedType.of(ValueSet.DURATION);

    /**
     * Element
     */
    public static final FHIRDefinedType ELEMENT = FHIRDefinedType.of(ValueSet.ELEMENT);

    /**
     * ElementDefinition
     */
    public static final FHIRDefinedType ELEMENT_DEFINITION = FHIRDefinedType.of(ValueSet.ELEMENT_DEFINITION);

    /**
     * Expression
     */
    public static final FHIRDefinedType EXPRESSION = FHIRDefinedType.of(ValueSet.EXPRESSION);

    /**
     * Extension
     */
    public static final FHIRDefinedType EXTENSION = FHIRDefinedType.of(ValueSet.EXTENSION);

    /**
     * HumanName
     */
    public static final FHIRDefinedType HUMAN_NAME = FHIRDefinedType.of(ValueSet.HUMAN_NAME);

    /**
     * Identifier
     */
    public static final FHIRDefinedType IDENTIFIER = FHIRDefinedType.of(ValueSet.IDENTIFIER);

    /**
     * MarketingStatus
     */
    public static final FHIRDefinedType MARKETING_STATUS = FHIRDefinedType.of(ValueSet.MARKETING_STATUS);

    /**
     * Meta
     */
    public static final FHIRDefinedType META = FHIRDefinedType.of(ValueSet.META);

    /**
     * Money
     */
    public static final FHIRDefinedType MONEY = FHIRDefinedType.of(ValueSet.MONEY);

    /**
     * MoneyQuantity
     */
    public static final FHIRDefinedType MONEY_QUANTITY = FHIRDefinedType.of(ValueSet.MONEY_QUANTITY);

    /**
     * Narrative
     */
    public static final FHIRDefinedType NARRATIVE = FHIRDefinedType.of(ValueSet.NARRATIVE);

    /**
     * ParameterDefinition
     */
    public static final FHIRDefinedType PARAMETER_DEFINITION = FHIRDefinedType.of(ValueSet.PARAMETER_DEFINITION);

    /**
     * Period
     */
    public static final FHIRDefinedType PERIOD = FHIRDefinedType.of(ValueSet.PERIOD);

    /**
     * Population
     */
    public static final FHIRDefinedType POPULATION = FHIRDefinedType.of(ValueSet.POPULATION);

    /**
     * ProdCharacteristic
     */
    public static final FHIRDefinedType PROD_CHARACTERISTIC = FHIRDefinedType.of(ValueSet.PROD_CHARACTERISTIC);

    /**
     * ProductShelfLife
     */
    public static final FHIRDefinedType PRODUCT_SHELF_LIFE = FHIRDefinedType.of(ValueSet.PRODUCT_SHELF_LIFE);

    /**
     * Quantity
     */
    public static final FHIRDefinedType QUANTITY = FHIRDefinedType.of(ValueSet.QUANTITY);

    /**
     * Range
     */
    public static final FHIRDefinedType RANGE = FHIRDefinedType.of(ValueSet.RANGE);

    /**
     * Ratio
     */
    public static final FHIRDefinedType RATIO = FHIRDefinedType.of(ValueSet.RATIO);

    /**
     * Reference
     */
    public static final FHIRDefinedType REFERENCE = FHIRDefinedType.of(ValueSet.REFERENCE);

    /**
     * RelatedArtifact
     */
    public static final FHIRDefinedType RELATED_ARTIFACT = FHIRDefinedType.of(ValueSet.RELATED_ARTIFACT);

    /**
     * SampledData
     */
    public static final FHIRDefinedType SAMPLED_DATA = FHIRDefinedType.of(ValueSet.SAMPLED_DATA);

    /**
     * Signature
     */
    public static final FHIRDefinedType SIGNATURE = FHIRDefinedType.of(ValueSet.SIGNATURE);

    /**
     * SimpleQuantity
     */
    public static final FHIRDefinedType SIMPLE_QUANTITY = FHIRDefinedType.of(ValueSet.SIMPLE_QUANTITY);

    /**
     * SubstanceAmount
     */
    public static final FHIRDefinedType SUBSTANCE_AMOUNT = FHIRDefinedType.of(ValueSet.SUBSTANCE_AMOUNT);

    /**
     * Timing
     */
    public static final FHIRDefinedType TIMING = FHIRDefinedType.of(ValueSet.TIMING);

    /**
     * TriggerDefinition
     */
    public static final FHIRDefinedType TRIGGER_DEFINITION = FHIRDefinedType.of(ValueSet.TRIGGER_DEFINITION);

    /**
     * UsageContext
     */
    public static final FHIRDefinedType USAGE_CONTEXT = FHIRDefinedType.of(ValueSet.USAGE_CONTEXT);

    /**
     * base64Binary
     */
    public static final FHIRDefinedType BASE64BINARY = FHIRDefinedType.of(ValueSet.BASE64BINARY);

    /**
     * boolean
     */
    public static final FHIRDefinedType BOOLEAN = FHIRDefinedType.of(ValueSet.BOOLEAN);

    /**
     * canonical
     */
    public static final FHIRDefinedType CANONICAL = FHIRDefinedType.of(ValueSet.CANONICAL);

    /**
     * code
     */
    public static final FHIRDefinedType CODE = FHIRDefinedType.of(ValueSet.CODE);

    /**
     * date
     */
    public static final FHIRDefinedType DATE = FHIRDefinedType.of(ValueSet.DATE);

    /**
     * dateTime
     */
    public static final FHIRDefinedType DATE_TIME = FHIRDefinedType.of(ValueSet.DATE_TIME);

    /**
     * decimal
     */
    public static final FHIRDefinedType DECIMAL = FHIRDefinedType.of(ValueSet.DECIMAL);

    /**
     * id
     */
    public static final FHIRDefinedType ID = FHIRDefinedType.of(ValueSet.ID);

    /**
     * instant
     */
    public static final FHIRDefinedType INSTANT = FHIRDefinedType.of(ValueSet.INSTANT);

    /**
     * integer
     */
    public static final FHIRDefinedType INTEGER = FHIRDefinedType.of(ValueSet.INTEGER);

    /**
     * markdown
     */
    public static final FHIRDefinedType MARKDOWN = FHIRDefinedType.of(ValueSet.MARKDOWN);

    /**
     * oid
     */
    public static final FHIRDefinedType OID = FHIRDefinedType.of(ValueSet.OID);

    /**
     * positiveInt
     */
    public static final FHIRDefinedType POSITIVE_INT = FHIRDefinedType.of(ValueSet.POSITIVE_INT);

    /**
     * string
     */
    public static final FHIRDefinedType STRING = FHIRDefinedType.of(ValueSet.STRING);

    /**
     * time
     */
    public static final FHIRDefinedType TIME = FHIRDefinedType.of(ValueSet.TIME);

    /**
     * unsignedInt
     */
    public static final FHIRDefinedType UNSIGNED_INT = FHIRDefinedType.of(ValueSet.UNSIGNED_INT);

    /**
     * uri
     */
    public static final FHIRDefinedType URI = FHIRDefinedType.of(ValueSet.URI);

    /**
     * url
     */
    public static final FHIRDefinedType URL = FHIRDefinedType.of(ValueSet.URL);

    /**
     * uuid
     */
    public static final FHIRDefinedType UUID = FHIRDefinedType.of(ValueSet.UUID);

    /**
     * XHTML
     */
    public static final FHIRDefinedType XHTML = FHIRDefinedType.of(ValueSet.XHTML);

    /**
     * Account
     */
    public static final FHIRDefinedType ACCOUNT = FHIRDefinedType.of(ValueSet.ACCOUNT);

    /**
     * ActivityDefinition
     */
    public static final FHIRDefinedType ACTIVITY_DEFINITION = FHIRDefinedType.of(ValueSet.ACTIVITY_DEFINITION);

    /**
     * AdverseEvent
     */
    public static final FHIRDefinedType ADVERSE_EVENT = FHIRDefinedType.of(ValueSet.ADVERSE_EVENT);

    /**
     * AllergyIntolerance
     */
    public static final FHIRDefinedType ALLERGY_INTOLERANCE = FHIRDefinedType.of(ValueSet.ALLERGY_INTOLERANCE);

    /**
     * Appointment
     */
    public static final FHIRDefinedType APPOINTMENT = FHIRDefinedType.of(ValueSet.APPOINTMENT);

    /**
     * AppointmentResponse
     */
    public static final FHIRDefinedType APPOINTMENT_RESPONSE = FHIRDefinedType.of(ValueSet.APPOINTMENT_RESPONSE);

    /**
     * AuditEvent
     */
    public static final FHIRDefinedType AUDIT_EVENT = FHIRDefinedType.of(ValueSet.AUDIT_EVENT);

    /**
     * Basic
     */
    public static final FHIRDefinedType BASIC = FHIRDefinedType.of(ValueSet.BASIC);

    /**
     * Binary
     */
    public static final FHIRDefinedType BINARY = FHIRDefinedType.of(ValueSet.BINARY);

    /**
     * BiologicallyDerivedProduct
     */
    public static final FHIRDefinedType BIOLOGICALLY_DERIVED_PRODUCT = FHIRDefinedType.of(ValueSet.BIOLOGICALLY_DERIVED_PRODUCT);

    /**
     * BodyStructure
     */
    public static final FHIRDefinedType BODY_STRUCTURE = FHIRDefinedType.of(ValueSet.BODY_STRUCTURE);

    /**
     * Bundle
     */
    public static final FHIRDefinedType BUNDLE = FHIRDefinedType.of(ValueSet.BUNDLE);

    /**
     * CapabilityStatement
     */
    public static final FHIRDefinedType CAPABILITY_STATEMENT = FHIRDefinedType.of(ValueSet.CAPABILITY_STATEMENT);

    /**
     * CarePlan
     */
    public static final FHIRDefinedType CARE_PLAN = FHIRDefinedType.of(ValueSet.CARE_PLAN);

    /**
     * CareTeam
     */
    public static final FHIRDefinedType CARE_TEAM = FHIRDefinedType.of(ValueSet.CARE_TEAM);

    /**
     * CatalogEntry
     */
    public static final FHIRDefinedType CATALOG_ENTRY = FHIRDefinedType.of(ValueSet.CATALOG_ENTRY);

    /**
     * ChargeItem
     */
    public static final FHIRDefinedType CHARGE_ITEM = FHIRDefinedType.of(ValueSet.CHARGE_ITEM);

    /**
     * ChargeItemDefinition
     */
    public static final FHIRDefinedType CHARGE_ITEM_DEFINITION = FHIRDefinedType.of(ValueSet.CHARGE_ITEM_DEFINITION);

    /**
     * Claim
     */
    public static final FHIRDefinedType CLAIM = FHIRDefinedType.of(ValueSet.CLAIM);

    /**
     * ClaimResponse
     */
    public static final FHIRDefinedType CLAIM_RESPONSE = FHIRDefinedType.of(ValueSet.CLAIM_RESPONSE);

    /**
     * ClinicalImpression
     */
    public static final FHIRDefinedType CLINICAL_IMPRESSION = FHIRDefinedType.of(ValueSet.CLINICAL_IMPRESSION);

    /**
     * CodeSystem
     */
    public static final FHIRDefinedType CODE_SYSTEM = FHIRDefinedType.of(ValueSet.CODE_SYSTEM);

    /**
     * Communication
     */
    public static final FHIRDefinedType COMMUNICATION = FHIRDefinedType.of(ValueSet.COMMUNICATION);

    /**
     * CommunicationRequest
     */
    public static final FHIRDefinedType COMMUNICATION_REQUEST = FHIRDefinedType.of(ValueSet.COMMUNICATION_REQUEST);

    /**
     * CompartmentDefinition
     */
    public static final FHIRDefinedType COMPARTMENT_DEFINITION = FHIRDefinedType.of(ValueSet.COMPARTMENT_DEFINITION);

    /**
     * Composition
     */
    public static final FHIRDefinedType COMPOSITION = FHIRDefinedType.of(ValueSet.COMPOSITION);

    /**
     * ConceptMap
     */
    public static final FHIRDefinedType CONCEPT_MAP = FHIRDefinedType.of(ValueSet.CONCEPT_MAP);

    /**
     * Condition
     */
    public static final FHIRDefinedType CONDITION = FHIRDefinedType.of(ValueSet.CONDITION);

    /**
     * Consent
     */
    public static final FHIRDefinedType CONSENT = FHIRDefinedType.of(ValueSet.CONSENT);

    /**
     * Contract
     */
    public static final FHIRDefinedType CONTRACT = FHIRDefinedType.of(ValueSet.CONTRACT);

    /**
     * Coverage
     */
    public static final FHIRDefinedType COVERAGE = FHIRDefinedType.of(ValueSet.COVERAGE);

    /**
     * CoverageEligibilityRequest
     */
    public static final FHIRDefinedType COVERAGE_ELIGIBILITY_REQUEST = FHIRDefinedType.of(ValueSet.COVERAGE_ELIGIBILITY_REQUEST);

    /**
     * CoverageEligibilityResponse
     */
    public static final FHIRDefinedType COVERAGE_ELIGIBILITY_RESPONSE = FHIRDefinedType.of(ValueSet.COVERAGE_ELIGIBILITY_RESPONSE);

    /**
     * DetectedIssue
     */
    public static final FHIRDefinedType DETECTED_ISSUE = FHIRDefinedType.of(ValueSet.DETECTED_ISSUE);

    /**
     * Device
     */
    public static final FHIRDefinedType DEVICE = FHIRDefinedType.of(ValueSet.DEVICE);

    /**
     * DeviceDefinition
     */
    public static final FHIRDefinedType DEVICE_DEFINITION = FHIRDefinedType.of(ValueSet.DEVICE_DEFINITION);

    /**
     * DeviceMetric
     */
    public static final FHIRDefinedType DEVICE_METRIC = FHIRDefinedType.of(ValueSet.DEVICE_METRIC);

    /**
     * DeviceRequest
     */
    public static final FHIRDefinedType DEVICE_REQUEST = FHIRDefinedType.of(ValueSet.DEVICE_REQUEST);

    /**
     * DeviceUseStatement
     */
    public static final FHIRDefinedType DEVICE_USE_STATEMENT = FHIRDefinedType.of(ValueSet.DEVICE_USE_STATEMENT);

    /**
     * DiagnosticReport
     */
    public static final FHIRDefinedType DIAGNOSTIC_REPORT = FHIRDefinedType.of(ValueSet.DIAGNOSTIC_REPORT);

    /**
     * DocumentManifest
     */
    public static final FHIRDefinedType DOCUMENT_MANIFEST = FHIRDefinedType.of(ValueSet.DOCUMENT_MANIFEST);

    /**
     * DocumentReference
     */
    public static final FHIRDefinedType DOCUMENT_REFERENCE = FHIRDefinedType.of(ValueSet.DOCUMENT_REFERENCE);

    /**
     * DomainResource
     */
    public static final FHIRDefinedType DOMAIN_RESOURCE = FHIRDefinedType.of(ValueSet.DOMAIN_RESOURCE);

    /**
     * EffectEvidenceSynthesis
     */
    public static final FHIRDefinedType EFFECT_EVIDENCE_SYNTHESIS = FHIRDefinedType.of(ValueSet.EFFECT_EVIDENCE_SYNTHESIS);

    /**
     * Encounter
     */
    public static final FHIRDefinedType ENCOUNTER = FHIRDefinedType.of(ValueSet.ENCOUNTER);

    /**
     * Endpoint
     */
    public static final FHIRDefinedType ENDPOINT = FHIRDefinedType.of(ValueSet.ENDPOINT);

    /**
     * EnrollmentRequest
     */
    public static final FHIRDefinedType ENROLLMENT_REQUEST = FHIRDefinedType.of(ValueSet.ENROLLMENT_REQUEST);

    /**
     * EnrollmentResponse
     */
    public static final FHIRDefinedType ENROLLMENT_RESPONSE = FHIRDefinedType.of(ValueSet.ENROLLMENT_RESPONSE);

    /**
     * EpisodeOfCare
     */
    public static final FHIRDefinedType EPISODE_OF_CARE = FHIRDefinedType.of(ValueSet.EPISODE_OF_CARE);

    /**
     * EventDefinition
     */
    public static final FHIRDefinedType EVENT_DEFINITION = FHIRDefinedType.of(ValueSet.EVENT_DEFINITION);

    /**
     * Evidence
     */
    public static final FHIRDefinedType EVIDENCE = FHIRDefinedType.of(ValueSet.EVIDENCE);

    /**
     * EvidenceVariable
     */
    public static final FHIRDefinedType EVIDENCE_VARIABLE = FHIRDefinedType.of(ValueSet.EVIDENCE_VARIABLE);

    /**
     * ExampleScenario
     */
    public static final FHIRDefinedType EXAMPLE_SCENARIO = FHIRDefinedType.of(ValueSet.EXAMPLE_SCENARIO);

    /**
     * ExplanationOfBenefit
     */
    public static final FHIRDefinedType EXPLANATION_OF_BENEFIT = FHIRDefinedType.of(ValueSet.EXPLANATION_OF_BENEFIT);

    /**
     * FamilyMemberHistory
     */
    public static final FHIRDefinedType FAMILY_MEMBER_HISTORY = FHIRDefinedType.of(ValueSet.FAMILY_MEMBER_HISTORY);

    /**
     * Flag
     */
    public static final FHIRDefinedType FLAG = FHIRDefinedType.of(ValueSet.FLAG);

    /**
     * Goal
     */
    public static final FHIRDefinedType GOAL = FHIRDefinedType.of(ValueSet.GOAL);

    /**
     * GraphDefinition
     */
    public static final FHIRDefinedType GRAPH_DEFINITION = FHIRDefinedType.of(ValueSet.GRAPH_DEFINITION);

    /**
     * Group
     */
    public static final FHIRDefinedType GROUP = FHIRDefinedType.of(ValueSet.GROUP);

    /**
     * GuidanceResponse
     */
    public static final FHIRDefinedType GUIDANCE_RESPONSE = FHIRDefinedType.of(ValueSet.GUIDANCE_RESPONSE);

    /**
     * HealthcareService
     */
    public static final FHIRDefinedType HEALTHCARE_SERVICE = FHIRDefinedType.of(ValueSet.HEALTHCARE_SERVICE);

    /**
     * ImagingStudy
     */
    public static final FHIRDefinedType IMAGING_STUDY = FHIRDefinedType.of(ValueSet.IMAGING_STUDY);

    /**
     * Immunization
     */
    public static final FHIRDefinedType IMMUNIZATION = FHIRDefinedType.of(ValueSet.IMMUNIZATION);

    /**
     * ImmunizationEvaluation
     */
    public static final FHIRDefinedType IMMUNIZATION_EVALUATION = FHIRDefinedType.of(ValueSet.IMMUNIZATION_EVALUATION);

    /**
     * ImmunizationRecommendation
     */
    public static final FHIRDefinedType IMMUNIZATION_RECOMMENDATION = FHIRDefinedType.of(ValueSet.IMMUNIZATION_RECOMMENDATION);

    /**
     * ImplementationGuide
     */
    public static final FHIRDefinedType IMPLEMENTATION_GUIDE = FHIRDefinedType.of(ValueSet.IMPLEMENTATION_GUIDE);

    /**
     * InsurancePlan
     */
    public static final FHIRDefinedType INSURANCE_PLAN = FHIRDefinedType.of(ValueSet.INSURANCE_PLAN);

    /**
     * Invoice
     */
    public static final FHIRDefinedType INVOICE = FHIRDefinedType.of(ValueSet.INVOICE);

    /**
     * Library
     */
    public static final FHIRDefinedType LIBRARY = FHIRDefinedType.of(ValueSet.LIBRARY);

    /**
     * Linkage
     */
    public static final FHIRDefinedType LINKAGE = FHIRDefinedType.of(ValueSet.LINKAGE);

    /**
     * List
     */
    public static final FHIRDefinedType LIST = FHIRDefinedType.of(ValueSet.LIST);

    /**
     * Location
     */
    public static final FHIRDefinedType LOCATION = FHIRDefinedType.of(ValueSet.LOCATION);

    /**
     * Measure
     */
    public static final FHIRDefinedType MEASURE = FHIRDefinedType.of(ValueSet.MEASURE);

    /**
     * MeasureReport
     */
    public static final FHIRDefinedType MEASURE_REPORT = FHIRDefinedType.of(ValueSet.MEASURE_REPORT);

    /**
     * Media
     */
    public static final FHIRDefinedType MEDIA = FHIRDefinedType.of(ValueSet.MEDIA);

    /**
     * Medication
     */
    public static final FHIRDefinedType MEDICATION = FHIRDefinedType.of(ValueSet.MEDICATION);

    /**
     * MedicationAdministration
     */
    public static final FHIRDefinedType MEDICATION_ADMINISTRATION = FHIRDefinedType.of(ValueSet.MEDICATION_ADMINISTRATION);

    /**
     * MedicationDispense
     */
    public static final FHIRDefinedType MEDICATION_DISPENSE = FHIRDefinedType.of(ValueSet.MEDICATION_DISPENSE);

    /**
     * MedicationKnowledge
     */
    public static final FHIRDefinedType MEDICATION_KNOWLEDGE = FHIRDefinedType.of(ValueSet.MEDICATION_KNOWLEDGE);

    /**
     * MedicationRequest
     */
    public static final FHIRDefinedType MEDICATION_REQUEST = FHIRDefinedType.of(ValueSet.MEDICATION_REQUEST);

    /**
     * MedicationStatement
     */
    public static final FHIRDefinedType MEDICATION_STATEMENT = FHIRDefinedType.of(ValueSet.MEDICATION_STATEMENT);

    /**
     * MedicinalProduct
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT);

    /**
     * MedicinalProductAuthorization
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_AUTHORIZATION = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_AUTHORIZATION);

    /**
     * MedicinalProductContraindication
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_CONTRAINDICATION = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_CONTRAINDICATION);

    /**
     * MedicinalProductIndication
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_INDICATION = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_INDICATION);

    /**
     * MedicinalProductIngredient
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_INGREDIENT = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_INGREDIENT);

    /**
     * MedicinalProductInteraction
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_INTERACTION = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_INTERACTION);

    /**
     * MedicinalProductManufactured
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_MANUFACTURED = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_MANUFACTURED);

    /**
     * MedicinalProductPackaged
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_PACKAGED = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_PACKAGED);

    /**
     * MedicinalProductPharmaceutical
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_PHARMACEUTICAL = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_PHARMACEUTICAL);

    /**
     * MedicinalProductUndesirableEffect
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT = FHIRDefinedType.of(ValueSet.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT);

    /**
     * MessageDefinition
     */
    public static final FHIRDefinedType MESSAGE_DEFINITION = FHIRDefinedType.of(ValueSet.MESSAGE_DEFINITION);

    /**
     * MessageHeader
     */
    public static final FHIRDefinedType MESSAGE_HEADER = FHIRDefinedType.of(ValueSet.MESSAGE_HEADER);

    /**
     * MolecularSequence
     */
    public static final FHIRDefinedType MOLECULAR_SEQUENCE = FHIRDefinedType.of(ValueSet.MOLECULAR_SEQUENCE);

    /**
     * NamingSystem
     */
    public static final FHIRDefinedType NAMING_SYSTEM = FHIRDefinedType.of(ValueSet.NAMING_SYSTEM);

    /**
     * NutritionOrder
     */
    public static final FHIRDefinedType NUTRITION_ORDER = FHIRDefinedType.of(ValueSet.NUTRITION_ORDER);

    /**
     * Observation
     */
    public static final FHIRDefinedType OBSERVATION = FHIRDefinedType.of(ValueSet.OBSERVATION);

    /**
     * ObservationDefinition
     */
    public static final FHIRDefinedType OBSERVATION_DEFINITION = FHIRDefinedType.of(ValueSet.OBSERVATION_DEFINITION);

    /**
     * OperationDefinition
     */
    public static final FHIRDefinedType OPERATION_DEFINITION = FHIRDefinedType.of(ValueSet.OPERATION_DEFINITION);

    /**
     * OperationOutcome
     */
    public static final FHIRDefinedType OPERATION_OUTCOME = FHIRDefinedType.of(ValueSet.OPERATION_OUTCOME);

    /**
     * Organization
     */
    public static final FHIRDefinedType ORGANIZATION = FHIRDefinedType.of(ValueSet.ORGANIZATION);

    /**
     * OrganizationAffiliation
     */
    public static final FHIRDefinedType ORGANIZATION_AFFILIATION = FHIRDefinedType.of(ValueSet.ORGANIZATION_AFFILIATION);

    /**
     * Parameters
     */
    public static final FHIRDefinedType PARAMETERS = FHIRDefinedType.of(ValueSet.PARAMETERS);

    /**
     * Patient
     */
    public static final FHIRDefinedType PATIENT = FHIRDefinedType.of(ValueSet.PATIENT);

    /**
     * PaymentNotice
     */
    public static final FHIRDefinedType PAYMENT_NOTICE = FHIRDefinedType.of(ValueSet.PAYMENT_NOTICE);

    /**
     * PaymentReconciliation
     */
    public static final FHIRDefinedType PAYMENT_RECONCILIATION = FHIRDefinedType.of(ValueSet.PAYMENT_RECONCILIATION);

    /**
     * Person
     */
    public static final FHIRDefinedType PERSON = FHIRDefinedType.of(ValueSet.PERSON);

    /**
     * PlanDefinition
     */
    public static final FHIRDefinedType PLAN_DEFINITION = FHIRDefinedType.of(ValueSet.PLAN_DEFINITION);

    /**
     * Practitioner
     */
    public static final FHIRDefinedType PRACTITIONER = FHIRDefinedType.of(ValueSet.PRACTITIONER);

    /**
     * PractitionerRole
     */
    public static final FHIRDefinedType PRACTITIONER_ROLE = FHIRDefinedType.of(ValueSet.PRACTITIONER_ROLE);

    /**
     * Procedure
     */
    public static final FHIRDefinedType PROCEDURE = FHIRDefinedType.of(ValueSet.PROCEDURE);

    /**
     * Provenance
     */
    public static final FHIRDefinedType PROVENANCE = FHIRDefinedType.of(ValueSet.PROVENANCE);

    /**
     * Questionnaire
     */
    public static final FHIRDefinedType QUESTIONNAIRE = FHIRDefinedType.of(ValueSet.QUESTIONNAIRE);

    /**
     * QuestionnaireResponse
     */
    public static final FHIRDefinedType QUESTIONNAIRE_RESPONSE = FHIRDefinedType.of(ValueSet.QUESTIONNAIRE_RESPONSE);

    /**
     * RelatedPerson
     */
    public static final FHIRDefinedType RELATED_PERSON = FHIRDefinedType.of(ValueSet.RELATED_PERSON);

    /**
     * RequestGroup
     */
    public static final FHIRDefinedType REQUEST_GROUP = FHIRDefinedType.of(ValueSet.REQUEST_GROUP);

    /**
     * ResearchDefinition
     */
    public static final FHIRDefinedType RESEARCH_DEFINITION = FHIRDefinedType.of(ValueSet.RESEARCH_DEFINITION);

    /**
     * ResearchElementDefinition
     */
    public static final FHIRDefinedType RESEARCH_ELEMENT_DEFINITION = FHIRDefinedType.of(ValueSet.RESEARCH_ELEMENT_DEFINITION);

    /**
     * ResearchStudy
     */
    public static final FHIRDefinedType RESEARCH_STUDY = FHIRDefinedType.of(ValueSet.RESEARCH_STUDY);

    /**
     * ResearchSubject
     */
    public static final FHIRDefinedType RESEARCH_SUBJECT = FHIRDefinedType.of(ValueSet.RESEARCH_SUBJECT);

    /**
     * Resource
     */
    public static final FHIRDefinedType RESOURCE = FHIRDefinedType.of(ValueSet.RESOURCE);

    /**
     * RiskAssessment
     */
    public static final FHIRDefinedType RISK_ASSESSMENT = FHIRDefinedType.of(ValueSet.RISK_ASSESSMENT);

    /**
     * RiskEvidenceSynthesis
     */
    public static final FHIRDefinedType RISK_EVIDENCE_SYNTHESIS = FHIRDefinedType.of(ValueSet.RISK_EVIDENCE_SYNTHESIS);

    /**
     * Schedule
     */
    public static final FHIRDefinedType SCHEDULE = FHIRDefinedType.of(ValueSet.SCHEDULE);

    /**
     * SearchParameter
     */
    public static final FHIRDefinedType SEARCH_PARAMETER = FHIRDefinedType.of(ValueSet.SEARCH_PARAMETER);

    /**
     * ServiceRequest
     */
    public static final FHIRDefinedType SERVICE_REQUEST = FHIRDefinedType.of(ValueSet.SERVICE_REQUEST);

    /**
     * Slot
     */
    public static final FHIRDefinedType SLOT = FHIRDefinedType.of(ValueSet.SLOT);

    /**
     * Specimen
     */
    public static final FHIRDefinedType SPECIMEN = FHIRDefinedType.of(ValueSet.SPECIMEN);

    /**
     * SpecimenDefinition
     */
    public static final FHIRDefinedType SPECIMEN_DEFINITION = FHIRDefinedType.of(ValueSet.SPECIMEN_DEFINITION);

    /**
     * StructureDefinition
     */
    public static final FHIRDefinedType STRUCTURE_DEFINITION = FHIRDefinedType.of(ValueSet.STRUCTURE_DEFINITION);

    /**
     * StructureMap
     */
    public static final FHIRDefinedType STRUCTURE_MAP = FHIRDefinedType.of(ValueSet.STRUCTURE_MAP);

    /**
     * Subscription
     */
    public static final FHIRDefinedType SUBSCRIPTION = FHIRDefinedType.of(ValueSet.SUBSCRIPTION);

    /**
     * Substance
     */
    public static final FHIRDefinedType SUBSTANCE = FHIRDefinedType.of(ValueSet.SUBSTANCE);

    /**
     * SubstanceNucleicAcid
     */
    public static final FHIRDefinedType SUBSTANCE_NUCLEIC_ACID = FHIRDefinedType.of(ValueSet.SUBSTANCE_NUCLEIC_ACID);

    /**
     * SubstancePolymer
     */
    public static final FHIRDefinedType SUBSTANCE_POLYMER = FHIRDefinedType.of(ValueSet.SUBSTANCE_POLYMER);

    /**
     * SubstanceProtein
     */
    public static final FHIRDefinedType SUBSTANCE_PROTEIN = FHIRDefinedType.of(ValueSet.SUBSTANCE_PROTEIN);

    /**
     * SubstanceReferenceInformation
     */
    public static final FHIRDefinedType SUBSTANCE_REFERENCE_INFORMATION = FHIRDefinedType.of(ValueSet.SUBSTANCE_REFERENCE_INFORMATION);

    /**
     * SubstanceSourceMaterial
     */
    public static final FHIRDefinedType SUBSTANCE_SOURCE_MATERIAL = FHIRDefinedType.of(ValueSet.SUBSTANCE_SOURCE_MATERIAL);

    /**
     * SubstanceSpecification
     */
    public static final FHIRDefinedType SUBSTANCE_SPECIFICATION = FHIRDefinedType.of(ValueSet.SUBSTANCE_SPECIFICATION);

    /**
     * SupplyDelivery
     */
    public static final FHIRDefinedType SUPPLY_DELIVERY = FHIRDefinedType.of(ValueSet.SUPPLY_DELIVERY);

    /**
     * SupplyRequest
     */
    public static final FHIRDefinedType SUPPLY_REQUEST = FHIRDefinedType.of(ValueSet.SUPPLY_REQUEST);

    /**
     * Task
     */
    public static final FHIRDefinedType TASK = FHIRDefinedType.of(ValueSet.TASK);

    /**
     * TerminologyCapabilities
     */
    public static final FHIRDefinedType TERMINOLOGY_CAPABILITIES = FHIRDefinedType.of(ValueSet.TERMINOLOGY_CAPABILITIES);

    /**
     * TestReport
     */
    public static final FHIRDefinedType TEST_REPORT = FHIRDefinedType.of(ValueSet.TEST_REPORT);

    /**
     * TestScript
     */
    public static final FHIRDefinedType TEST_SCRIPT = FHIRDefinedType.of(ValueSet.TEST_SCRIPT);

    /**
     * ValueSet
     */
    public static final FHIRDefinedType VALUE_SET = FHIRDefinedType.of(ValueSet.VALUE_SET);

    /**
     * VerificationResult
     */
    public static final FHIRDefinedType VERIFICATION_RESULT = FHIRDefinedType.of(ValueSet.VERIFICATION_RESULT);

    /**
     * VisionPrescription
     */
    public static final FHIRDefinedType VISION_PRESCRIPTION = FHIRDefinedType.of(ValueSet.VISION_PRESCRIPTION);

    private volatile int hashCode;

    private FHIRDefinedType(Builder builder) {
        super(builder);
    }

    public static FHIRDefinedType of(java.lang.String value) {
        return FHIRDefinedType.builder().value(value).build();
    }

    public static FHIRDefinedType of(ValueSet value) {
        return FHIRDefinedType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return FHIRDefinedType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return FHIRDefinedType.builder().value(value).build();
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
        FHIRDefinedType other = (FHIRDefinedType) obj;
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
        public FHIRDefinedType build() {
            return new FHIRDefinedType(this);
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
