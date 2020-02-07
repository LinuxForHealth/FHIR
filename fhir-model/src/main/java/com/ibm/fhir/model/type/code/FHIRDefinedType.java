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
public class FHIRDefinedType extends Code {
    /**
     * Address
     */
    public static final FHIRDefinedType ADDRESS = FHIRDefinedType.builder().value(ValueSet.ADDRESS).build();

    /**
     * Age
     */
    public static final FHIRDefinedType AGE = FHIRDefinedType.builder().value(ValueSet.AGE).build();

    /**
     * Annotation
     */
    public static final FHIRDefinedType ANNOTATION = FHIRDefinedType.builder().value(ValueSet.ANNOTATION).build();

    /**
     * Attachment
     */
    public static final FHIRDefinedType ATTACHMENT = FHIRDefinedType.builder().value(ValueSet.ATTACHMENT).build();

    /**
     * BackboneElement
     */
    public static final FHIRDefinedType BACKBONE_ELEMENT = FHIRDefinedType.builder().value(ValueSet.BACKBONE_ELEMENT).build();

    /**
     * CodeableConcept
     */
    public static final FHIRDefinedType CODEABLE_CONCEPT = FHIRDefinedType.builder().value(ValueSet.CODEABLE_CONCEPT).build();

    /**
     * Coding
     */
    public static final FHIRDefinedType CODING = FHIRDefinedType.builder().value(ValueSet.CODING).build();

    /**
     * ContactDetail
     */
    public static final FHIRDefinedType CONTACT_DETAIL = FHIRDefinedType.builder().value(ValueSet.CONTACT_DETAIL).build();

    /**
     * ContactPoint
     */
    public static final FHIRDefinedType CONTACT_POINT = FHIRDefinedType.builder().value(ValueSet.CONTACT_POINT).build();

    /**
     * Contributor
     */
    public static final FHIRDefinedType CONTRIBUTOR = FHIRDefinedType.builder().value(ValueSet.CONTRIBUTOR).build();

    /**
     * Count
     */
    public static final FHIRDefinedType COUNT = FHIRDefinedType.builder().value(ValueSet.COUNT).build();

    /**
     * DataRequirement
     */
    public static final FHIRDefinedType DATA_REQUIREMENT = FHIRDefinedType.builder().value(ValueSet.DATA_REQUIREMENT).build();

    /**
     * Distance
     */
    public static final FHIRDefinedType DISTANCE = FHIRDefinedType.builder().value(ValueSet.DISTANCE).build();

    /**
     * Dosage
     */
    public static final FHIRDefinedType DOSAGE = FHIRDefinedType.builder().value(ValueSet.DOSAGE).build();

    /**
     * Duration
     */
    public static final FHIRDefinedType DURATION = FHIRDefinedType.builder().value(ValueSet.DURATION).build();

    /**
     * Element
     */
    public static final FHIRDefinedType ELEMENT = FHIRDefinedType.builder().value(ValueSet.ELEMENT).build();

    /**
     * ElementDefinition
     */
    public static final FHIRDefinedType ELEMENT_DEFINITION = FHIRDefinedType.builder().value(ValueSet.ELEMENT_DEFINITION).build();

    /**
     * Expression
     */
    public static final FHIRDefinedType EXPRESSION = FHIRDefinedType.builder().value(ValueSet.EXPRESSION).build();

    /**
     * Extension
     */
    public static final FHIRDefinedType EXTENSION = FHIRDefinedType.builder().value(ValueSet.EXTENSION).build();

    /**
     * HumanName
     */
    public static final FHIRDefinedType HUMAN_NAME = FHIRDefinedType.builder().value(ValueSet.HUMAN_NAME).build();

    /**
     * Identifier
     */
    public static final FHIRDefinedType IDENTIFIER = FHIRDefinedType.builder().value(ValueSet.IDENTIFIER).build();

    /**
     * MarketingStatus
     */
    public static final FHIRDefinedType MARKETING_STATUS = FHIRDefinedType.builder().value(ValueSet.MARKETING_STATUS).build();

    /**
     * Meta
     */
    public static final FHIRDefinedType META = FHIRDefinedType.builder().value(ValueSet.META).build();

    /**
     * Money
     */
    public static final FHIRDefinedType MONEY = FHIRDefinedType.builder().value(ValueSet.MONEY).build();

    /**
     * MoneyQuantity
     */
    public static final FHIRDefinedType MONEY_QUANTITY = FHIRDefinedType.builder().value(ValueSet.MONEY_QUANTITY).build();

    /**
     * Narrative
     */
    public static final FHIRDefinedType NARRATIVE = FHIRDefinedType.builder().value(ValueSet.NARRATIVE).build();

    /**
     * ParameterDefinition
     */
    public static final FHIRDefinedType PARAMETER_DEFINITION = FHIRDefinedType.builder().value(ValueSet.PARAMETER_DEFINITION).build();

    /**
     * Period
     */
    public static final FHIRDefinedType PERIOD = FHIRDefinedType.builder().value(ValueSet.PERIOD).build();

    /**
     * Population
     */
    public static final FHIRDefinedType POPULATION = FHIRDefinedType.builder().value(ValueSet.POPULATION).build();

    /**
     * ProdCharacteristic
     */
    public static final FHIRDefinedType PROD_CHARACTERISTIC = FHIRDefinedType.builder().value(ValueSet.PROD_CHARACTERISTIC).build();

    /**
     * ProductShelfLife
     */
    public static final FHIRDefinedType PRODUCT_SHELF_LIFE = FHIRDefinedType.builder().value(ValueSet.PRODUCT_SHELF_LIFE).build();

    /**
     * Quantity
     */
    public static final FHIRDefinedType QUANTITY = FHIRDefinedType.builder().value(ValueSet.QUANTITY).build();

    /**
     * Range
     */
    public static final FHIRDefinedType RANGE = FHIRDefinedType.builder().value(ValueSet.RANGE).build();

    /**
     * Ratio
     */
    public static final FHIRDefinedType RATIO = FHIRDefinedType.builder().value(ValueSet.RATIO).build();

    /**
     * Reference
     */
    public static final FHIRDefinedType REFERENCE = FHIRDefinedType.builder().value(ValueSet.REFERENCE).build();

    /**
     * RelatedArtifact
     */
    public static final FHIRDefinedType RELATED_ARTIFACT = FHIRDefinedType.builder().value(ValueSet.RELATED_ARTIFACT).build();

    /**
     * SampledData
     */
    public static final FHIRDefinedType SAMPLED_DATA = FHIRDefinedType.builder().value(ValueSet.SAMPLED_DATA).build();

    /**
     * Signature
     */
    public static final FHIRDefinedType SIGNATURE = FHIRDefinedType.builder().value(ValueSet.SIGNATURE).build();

    /**
     * SimpleQuantity
     */
    public static final FHIRDefinedType SIMPLE_QUANTITY = FHIRDefinedType.builder().value(ValueSet.SIMPLE_QUANTITY).build();

    /**
     * SubstanceAmount
     */
    public static final FHIRDefinedType SUBSTANCE_AMOUNT = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_AMOUNT).build();

    /**
     * Timing
     */
    public static final FHIRDefinedType TIMING = FHIRDefinedType.builder().value(ValueSet.TIMING).build();

    /**
     * TriggerDefinition
     */
    public static final FHIRDefinedType TRIGGER_DEFINITION = FHIRDefinedType.builder().value(ValueSet.TRIGGER_DEFINITION).build();

    /**
     * UsageContext
     */
    public static final FHIRDefinedType USAGE_CONTEXT = FHIRDefinedType.builder().value(ValueSet.USAGE_CONTEXT).build();

    /**
     * base64Binary
     */
    public static final FHIRDefinedType BASE64BINARY = FHIRDefinedType.builder().value(ValueSet.BASE64BINARY).build();

    /**
     * boolean
     */
    public static final FHIRDefinedType BOOLEAN = FHIRDefinedType.builder().value(ValueSet.BOOLEAN).build();

    /**
     * canonical
     */
    public static final FHIRDefinedType CANONICAL = FHIRDefinedType.builder().value(ValueSet.CANONICAL).build();

    /**
     * code
     */
    public static final FHIRDefinedType CODE = FHIRDefinedType.builder().value(ValueSet.CODE).build();

    /**
     * date
     */
    public static final FHIRDefinedType DATE = FHIRDefinedType.builder().value(ValueSet.DATE).build();

    /**
     * dateTime
     */
    public static final FHIRDefinedType DATE_TIME = FHIRDefinedType.builder().value(ValueSet.DATE_TIME).build();

    /**
     * decimal
     */
    public static final FHIRDefinedType DECIMAL = FHIRDefinedType.builder().value(ValueSet.DECIMAL).build();

    /**
     * id
     */
    public static final FHIRDefinedType ID = FHIRDefinedType.builder().value(ValueSet.ID).build();

    /**
     * instant
     */
    public static final FHIRDefinedType INSTANT = FHIRDefinedType.builder().value(ValueSet.INSTANT).build();

    /**
     * integer
     */
    public static final FHIRDefinedType INTEGER = FHIRDefinedType.builder().value(ValueSet.INTEGER).build();

    /**
     * markdown
     */
    public static final FHIRDefinedType MARKDOWN = FHIRDefinedType.builder().value(ValueSet.MARKDOWN).build();

    /**
     * oid
     */
    public static final FHIRDefinedType OID = FHIRDefinedType.builder().value(ValueSet.OID).build();

    /**
     * positiveInt
     */
    public static final FHIRDefinedType POSITIVE_INT = FHIRDefinedType.builder().value(ValueSet.POSITIVE_INT).build();

    /**
     * string
     */
    public static final FHIRDefinedType STRING = FHIRDefinedType.builder().value(ValueSet.STRING).build();

    /**
     * time
     */
    public static final FHIRDefinedType TIME = FHIRDefinedType.builder().value(ValueSet.TIME).build();

    /**
     * unsignedInt
     */
    public static final FHIRDefinedType UNSIGNED_INT = FHIRDefinedType.builder().value(ValueSet.UNSIGNED_INT).build();

    /**
     * uri
     */
    public static final FHIRDefinedType URI = FHIRDefinedType.builder().value(ValueSet.URI).build();

    /**
     * url
     */
    public static final FHIRDefinedType URL = FHIRDefinedType.builder().value(ValueSet.URL).build();

    /**
     * uuid
     */
    public static final FHIRDefinedType UUID = FHIRDefinedType.builder().value(ValueSet.UUID).build();

    /**
     * XHTML
     */
    public static final FHIRDefinedType XHTML = FHIRDefinedType.builder().value(ValueSet.XHTML).build();

    /**
     * Account
     */
    public static final FHIRDefinedType ACCOUNT = FHIRDefinedType.builder().value(ValueSet.ACCOUNT).build();

    /**
     * ActivityDefinition
     */
    public static final FHIRDefinedType ACTIVITY_DEFINITION = FHIRDefinedType.builder().value(ValueSet.ACTIVITY_DEFINITION).build();

    /**
     * AdverseEvent
     */
    public static final FHIRDefinedType ADVERSE_EVENT = FHIRDefinedType.builder().value(ValueSet.ADVERSE_EVENT).build();

    /**
     * AllergyIntolerance
     */
    public static final FHIRDefinedType ALLERGY_INTOLERANCE = FHIRDefinedType.builder().value(ValueSet.ALLERGY_INTOLERANCE).build();

    /**
     * Appointment
     */
    public static final FHIRDefinedType APPOINTMENT = FHIRDefinedType.builder().value(ValueSet.APPOINTMENT).build();

    /**
     * AppointmentResponse
     */
    public static final FHIRDefinedType APPOINTMENT_RESPONSE = FHIRDefinedType.builder().value(ValueSet.APPOINTMENT_RESPONSE).build();

    /**
     * AuditEvent
     */
    public static final FHIRDefinedType AUDIT_EVENT = FHIRDefinedType.builder().value(ValueSet.AUDIT_EVENT).build();

    /**
     * Basic
     */
    public static final FHIRDefinedType BASIC = FHIRDefinedType.builder().value(ValueSet.BASIC).build();

    /**
     * Binary
     */
    public static final FHIRDefinedType BINARY = FHIRDefinedType.builder().value(ValueSet.BINARY).build();

    /**
     * BiologicallyDerivedProduct
     */
    public static final FHIRDefinedType BIOLOGICALLY_DERIVED_PRODUCT = FHIRDefinedType.builder().value(ValueSet.BIOLOGICALLY_DERIVED_PRODUCT).build();

    /**
     * BodyStructure
     */
    public static final FHIRDefinedType BODY_STRUCTURE = FHIRDefinedType.builder().value(ValueSet.BODY_STRUCTURE).build();

    /**
     * Bundle
     */
    public static final FHIRDefinedType BUNDLE = FHIRDefinedType.builder().value(ValueSet.BUNDLE).build();

    /**
     * CapabilityStatement
     */
    public static final FHIRDefinedType CAPABILITY_STATEMENT = FHIRDefinedType.builder().value(ValueSet.CAPABILITY_STATEMENT).build();

    /**
     * CarePlan
     */
    public static final FHIRDefinedType CARE_PLAN = FHIRDefinedType.builder().value(ValueSet.CARE_PLAN).build();

    /**
     * CareTeam
     */
    public static final FHIRDefinedType CARE_TEAM = FHIRDefinedType.builder().value(ValueSet.CARE_TEAM).build();

    /**
     * CatalogEntry
     */
    public static final FHIRDefinedType CATALOG_ENTRY = FHIRDefinedType.builder().value(ValueSet.CATALOG_ENTRY).build();

    /**
     * ChargeItem
     */
    public static final FHIRDefinedType CHARGE_ITEM = FHIRDefinedType.builder().value(ValueSet.CHARGE_ITEM).build();

    /**
     * ChargeItemDefinition
     */
    public static final FHIRDefinedType CHARGE_ITEM_DEFINITION = FHIRDefinedType.builder().value(ValueSet.CHARGE_ITEM_DEFINITION).build();

    /**
     * Claim
     */
    public static final FHIRDefinedType CLAIM = FHIRDefinedType.builder().value(ValueSet.CLAIM).build();

    /**
     * ClaimResponse
     */
    public static final FHIRDefinedType CLAIM_RESPONSE = FHIRDefinedType.builder().value(ValueSet.CLAIM_RESPONSE).build();

    /**
     * ClinicalImpression
     */
    public static final FHIRDefinedType CLINICAL_IMPRESSION = FHIRDefinedType.builder().value(ValueSet.CLINICAL_IMPRESSION).build();

    /**
     * CodeSystem
     */
    public static final FHIRDefinedType CODE_SYSTEM = FHIRDefinedType.builder().value(ValueSet.CODE_SYSTEM).build();

    /**
     * Communication
     */
    public static final FHIRDefinedType COMMUNICATION = FHIRDefinedType.builder().value(ValueSet.COMMUNICATION).build();

    /**
     * CommunicationRequest
     */
    public static final FHIRDefinedType COMMUNICATION_REQUEST = FHIRDefinedType.builder().value(ValueSet.COMMUNICATION_REQUEST).build();

    /**
     * CompartmentDefinition
     */
    public static final FHIRDefinedType COMPARTMENT_DEFINITION = FHIRDefinedType.builder().value(ValueSet.COMPARTMENT_DEFINITION).build();

    /**
     * Composition
     */
    public static final FHIRDefinedType COMPOSITION = FHIRDefinedType.builder().value(ValueSet.COMPOSITION).build();

    /**
     * ConceptMap
     */
    public static final FHIRDefinedType CONCEPT_MAP = FHIRDefinedType.builder().value(ValueSet.CONCEPT_MAP).build();

    /**
     * Condition
     */
    public static final FHIRDefinedType CONDITION = FHIRDefinedType.builder().value(ValueSet.CONDITION).build();

    /**
     * Consent
     */
    public static final FHIRDefinedType CONSENT = FHIRDefinedType.builder().value(ValueSet.CONSENT).build();

    /**
     * Contract
     */
    public static final FHIRDefinedType CONTRACT = FHIRDefinedType.builder().value(ValueSet.CONTRACT).build();

    /**
     * Coverage
     */
    public static final FHIRDefinedType COVERAGE = FHIRDefinedType.builder().value(ValueSet.COVERAGE).build();

    /**
     * CoverageEligibilityRequest
     */
    public static final FHIRDefinedType COVERAGE_ELIGIBILITY_REQUEST = FHIRDefinedType.builder().value(ValueSet.COVERAGE_ELIGIBILITY_REQUEST).build();

    /**
     * CoverageEligibilityResponse
     */
    public static final FHIRDefinedType COVERAGE_ELIGIBILITY_RESPONSE = FHIRDefinedType.builder().value(ValueSet.COVERAGE_ELIGIBILITY_RESPONSE).build();

    /**
     * DetectedIssue
     */
    public static final FHIRDefinedType DETECTED_ISSUE = FHIRDefinedType.builder().value(ValueSet.DETECTED_ISSUE).build();

    /**
     * Device
     */
    public static final FHIRDefinedType DEVICE = FHIRDefinedType.builder().value(ValueSet.DEVICE).build();

    /**
     * DeviceDefinition
     */
    public static final FHIRDefinedType DEVICE_DEFINITION = FHIRDefinedType.builder().value(ValueSet.DEVICE_DEFINITION).build();

    /**
     * DeviceMetric
     */
    public static final FHIRDefinedType DEVICE_METRIC = FHIRDefinedType.builder().value(ValueSet.DEVICE_METRIC).build();

    /**
     * DeviceRequest
     */
    public static final FHIRDefinedType DEVICE_REQUEST = FHIRDefinedType.builder().value(ValueSet.DEVICE_REQUEST).build();

    /**
     * DeviceUseStatement
     */
    public static final FHIRDefinedType DEVICE_USE_STATEMENT = FHIRDefinedType.builder().value(ValueSet.DEVICE_USE_STATEMENT).build();

    /**
     * DiagnosticReport
     */
    public static final FHIRDefinedType DIAGNOSTIC_REPORT = FHIRDefinedType.builder().value(ValueSet.DIAGNOSTIC_REPORT).build();

    /**
     * DocumentManifest
     */
    public static final FHIRDefinedType DOCUMENT_MANIFEST = FHIRDefinedType.builder().value(ValueSet.DOCUMENT_MANIFEST).build();

    /**
     * DocumentReference
     */
    public static final FHIRDefinedType DOCUMENT_REFERENCE = FHIRDefinedType.builder().value(ValueSet.DOCUMENT_REFERENCE).build();

    /**
     * DomainResource
     */
    public static final FHIRDefinedType DOMAIN_RESOURCE = FHIRDefinedType.builder().value(ValueSet.DOMAIN_RESOURCE).build();

    /**
     * EffectEvidenceSynthesis
     */
    public static final FHIRDefinedType EFFECT_EVIDENCE_SYNTHESIS = FHIRDefinedType.builder().value(ValueSet.EFFECT_EVIDENCE_SYNTHESIS).build();

    /**
     * Encounter
     */
    public static final FHIRDefinedType ENCOUNTER = FHIRDefinedType.builder().value(ValueSet.ENCOUNTER).build();

    /**
     * Endpoint
     */
    public static final FHIRDefinedType ENDPOINT = FHIRDefinedType.builder().value(ValueSet.ENDPOINT).build();

    /**
     * EnrollmentRequest
     */
    public static final FHIRDefinedType ENROLLMENT_REQUEST = FHIRDefinedType.builder().value(ValueSet.ENROLLMENT_REQUEST).build();

    /**
     * EnrollmentResponse
     */
    public static final FHIRDefinedType ENROLLMENT_RESPONSE = FHIRDefinedType.builder().value(ValueSet.ENROLLMENT_RESPONSE).build();

    /**
     * EpisodeOfCare
     */
    public static final FHIRDefinedType EPISODE_OF_CARE = FHIRDefinedType.builder().value(ValueSet.EPISODE_OF_CARE).build();

    /**
     * EventDefinition
     */
    public static final FHIRDefinedType EVENT_DEFINITION = FHIRDefinedType.builder().value(ValueSet.EVENT_DEFINITION).build();

    /**
     * Evidence
     */
    public static final FHIRDefinedType EVIDENCE = FHIRDefinedType.builder().value(ValueSet.EVIDENCE).build();

    /**
     * EvidenceVariable
     */
    public static final FHIRDefinedType EVIDENCE_VARIABLE = FHIRDefinedType.builder().value(ValueSet.EVIDENCE_VARIABLE).build();

    /**
     * ExampleScenario
     */
    public static final FHIRDefinedType EXAMPLE_SCENARIO = FHIRDefinedType.builder().value(ValueSet.EXAMPLE_SCENARIO).build();

    /**
     * ExplanationOfBenefit
     */
    public static final FHIRDefinedType EXPLANATION_OF_BENEFIT = FHIRDefinedType.builder().value(ValueSet.EXPLANATION_OF_BENEFIT).build();

    /**
     * FamilyMemberHistory
     */
    public static final FHIRDefinedType FAMILY_MEMBER_HISTORY = FHIRDefinedType.builder().value(ValueSet.FAMILY_MEMBER_HISTORY).build();

    /**
     * Flag
     */
    public static final FHIRDefinedType FLAG = FHIRDefinedType.builder().value(ValueSet.FLAG).build();

    /**
     * Goal
     */
    public static final FHIRDefinedType GOAL = FHIRDefinedType.builder().value(ValueSet.GOAL).build();

    /**
     * GraphDefinition
     */
    public static final FHIRDefinedType GRAPH_DEFINITION = FHIRDefinedType.builder().value(ValueSet.GRAPH_DEFINITION).build();

    /**
     * Group
     */
    public static final FHIRDefinedType GROUP = FHIRDefinedType.builder().value(ValueSet.GROUP).build();

    /**
     * GuidanceResponse
     */
    public static final FHIRDefinedType GUIDANCE_RESPONSE = FHIRDefinedType.builder().value(ValueSet.GUIDANCE_RESPONSE).build();

    /**
     * HealthcareService
     */
    public static final FHIRDefinedType HEALTHCARE_SERVICE = FHIRDefinedType.builder().value(ValueSet.HEALTHCARE_SERVICE).build();

    /**
     * ImagingStudy
     */
    public static final FHIRDefinedType IMAGING_STUDY = FHIRDefinedType.builder().value(ValueSet.IMAGING_STUDY).build();

    /**
     * Immunization
     */
    public static final FHIRDefinedType IMMUNIZATION = FHIRDefinedType.builder().value(ValueSet.IMMUNIZATION).build();

    /**
     * ImmunizationEvaluation
     */
    public static final FHIRDefinedType IMMUNIZATION_EVALUATION = FHIRDefinedType.builder().value(ValueSet.IMMUNIZATION_EVALUATION).build();

    /**
     * ImmunizationRecommendation
     */
    public static final FHIRDefinedType IMMUNIZATION_RECOMMENDATION = FHIRDefinedType.builder().value(ValueSet.IMMUNIZATION_RECOMMENDATION).build();

    /**
     * ImplementationGuide
     */
    public static final FHIRDefinedType IMPLEMENTATION_GUIDE = FHIRDefinedType.builder().value(ValueSet.IMPLEMENTATION_GUIDE).build();

    /**
     * InsurancePlan
     */
    public static final FHIRDefinedType INSURANCE_PLAN = FHIRDefinedType.builder().value(ValueSet.INSURANCE_PLAN).build();

    /**
     * Invoice
     */
    public static final FHIRDefinedType INVOICE = FHIRDefinedType.builder().value(ValueSet.INVOICE).build();

    /**
     * Library
     */
    public static final FHIRDefinedType LIBRARY = FHIRDefinedType.builder().value(ValueSet.LIBRARY).build();

    /**
     * Linkage
     */
    public static final FHIRDefinedType LINKAGE = FHIRDefinedType.builder().value(ValueSet.LINKAGE).build();

    /**
     * List
     */
    public static final FHIRDefinedType LIST = FHIRDefinedType.builder().value(ValueSet.LIST).build();

    /**
     * Location
     */
    public static final FHIRDefinedType LOCATION = FHIRDefinedType.builder().value(ValueSet.LOCATION).build();

    /**
     * Measure
     */
    public static final FHIRDefinedType MEASURE = FHIRDefinedType.builder().value(ValueSet.MEASURE).build();

    /**
     * MeasureReport
     */
    public static final FHIRDefinedType MEASURE_REPORT = FHIRDefinedType.builder().value(ValueSet.MEASURE_REPORT).build();

    /**
     * Media
     */
    public static final FHIRDefinedType MEDIA = FHIRDefinedType.builder().value(ValueSet.MEDIA).build();

    /**
     * Medication
     */
    public static final FHIRDefinedType MEDICATION = FHIRDefinedType.builder().value(ValueSet.MEDICATION).build();

    /**
     * MedicationAdministration
     */
    public static final FHIRDefinedType MEDICATION_ADMINISTRATION = FHIRDefinedType.builder().value(ValueSet.MEDICATION_ADMINISTRATION).build();

    /**
     * MedicationDispense
     */
    public static final FHIRDefinedType MEDICATION_DISPENSE = FHIRDefinedType.builder().value(ValueSet.MEDICATION_DISPENSE).build();

    /**
     * MedicationKnowledge
     */
    public static final FHIRDefinedType MEDICATION_KNOWLEDGE = FHIRDefinedType.builder().value(ValueSet.MEDICATION_KNOWLEDGE).build();

    /**
     * MedicationRequest
     */
    public static final FHIRDefinedType MEDICATION_REQUEST = FHIRDefinedType.builder().value(ValueSet.MEDICATION_REQUEST).build();

    /**
     * MedicationStatement
     */
    public static final FHIRDefinedType MEDICATION_STATEMENT = FHIRDefinedType.builder().value(ValueSet.MEDICATION_STATEMENT).build();

    /**
     * MedicinalProduct
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT).build();

    /**
     * MedicinalProductAuthorization
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_AUTHORIZATION = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_AUTHORIZATION).build();

    /**
     * MedicinalProductContraindication
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_CONTRAINDICATION = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_CONTRAINDICATION).build();

    /**
     * MedicinalProductIndication
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_INDICATION = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_INDICATION).build();

    /**
     * MedicinalProductIngredient
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_INGREDIENT = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_INGREDIENT).build();

    /**
     * MedicinalProductInteraction
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_INTERACTION = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_INTERACTION).build();

    /**
     * MedicinalProductManufactured
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_MANUFACTURED = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_MANUFACTURED).build();

    /**
     * MedicinalProductPackaged
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_PACKAGED = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_PACKAGED).build();

    /**
     * MedicinalProductPharmaceutical
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_PHARMACEUTICAL = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_PHARMACEUTICAL).build();

    /**
     * MedicinalProductUndesirableEffect
     */
    public static final FHIRDefinedType MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT = FHIRDefinedType.builder().value(ValueSet.MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT).build();

    /**
     * MessageDefinition
     */
    public static final FHIRDefinedType MESSAGE_DEFINITION = FHIRDefinedType.builder().value(ValueSet.MESSAGE_DEFINITION).build();

    /**
     * MessageHeader
     */
    public static final FHIRDefinedType MESSAGE_HEADER = FHIRDefinedType.builder().value(ValueSet.MESSAGE_HEADER).build();

    /**
     * MolecularSequence
     */
    public static final FHIRDefinedType MOLECULAR_SEQUENCE = FHIRDefinedType.builder().value(ValueSet.MOLECULAR_SEQUENCE).build();

    /**
     * NamingSystem
     */
    public static final FHIRDefinedType NAMING_SYSTEM = FHIRDefinedType.builder().value(ValueSet.NAMING_SYSTEM).build();

    /**
     * NutritionOrder
     */
    public static final FHIRDefinedType NUTRITION_ORDER = FHIRDefinedType.builder().value(ValueSet.NUTRITION_ORDER).build();

    /**
     * Observation
     */
    public static final FHIRDefinedType OBSERVATION = FHIRDefinedType.builder().value(ValueSet.OBSERVATION).build();

    /**
     * ObservationDefinition
     */
    public static final FHIRDefinedType OBSERVATION_DEFINITION = FHIRDefinedType.builder().value(ValueSet.OBSERVATION_DEFINITION).build();

    /**
     * OperationDefinition
     */
    public static final FHIRDefinedType OPERATION_DEFINITION = FHIRDefinedType.builder().value(ValueSet.OPERATION_DEFINITION).build();

    /**
     * OperationOutcome
     */
    public static final FHIRDefinedType OPERATION_OUTCOME = FHIRDefinedType.builder().value(ValueSet.OPERATION_OUTCOME).build();

    /**
     * Organization
     */
    public static final FHIRDefinedType ORGANIZATION = FHIRDefinedType.builder().value(ValueSet.ORGANIZATION).build();

    /**
     * OrganizationAffiliation
     */
    public static final FHIRDefinedType ORGANIZATION_AFFILIATION = FHIRDefinedType.builder().value(ValueSet.ORGANIZATION_AFFILIATION).build();

    /**
     * Parameters
     */
    public static final FHIRDefinedType PARAMETERS = FHIRDefinedType.builder().value(ValueSet.PARAMETERS).build();

    /**
     * Patient
     */
    public static final FHIRDefinedType PATIENT = FHIRDefinedType.builder().value(ValueSet.PATIENT).build();

    /**
     * PaymentNotice
     */
    public static final FHIRDefinedType PAYMENT_NOTICE = FHIRDefinedType.builder().value(ValueSet.PAYMENT_NOTICE).build();

    /**
     * PaymentReconciliation
     */
    public static final FHIRDefinedType PAYMENT_RECONCILIATION = FHIRDefinedType.builder().value(ValueSet.PAYMENT_RECONCILIATION).build();

    /**
     * Person
     */
    public static final FHIRDefinedType PERSON = FHIRDefinedType.builder().value(ValueSet.PERSON).build();

    /**
     * PlanDefinition
     */
    public static final FHIRDefinedType PLAN_DEFINITION = FHIRDefinedType.builder().value(ValueSet.PLAN_DEFINITION).build();

    /**
     * Practitioner
     */
    public static final FHIRDefinedType PRACTITIONER = FHIRDefinedType.builder().value(ValueSet.PRACTITIONER).build();

    /**
     * PractitionerRole
     */
    public static final FHIRDefinedType PRACTITIONER_ROLE = FHIRDefinedType.builder().value(ValueSet.PRACTITIONER_ROLE).build();

    /**
     * Procedure
     */
    public static final FHIRDefinedType PROCEDURE = FHIRDefinedType.builder().value(ValueSet.PROCEDURE).build();

    /**
     * Provenance
     */
    public static final FHIRDefinedType PROVENANCE = FHIRDefinedType.builder().value(ValueSet.PROVENANCE).build();

    /**
     * Questionnaire
     */
    public static final FHIRDefinedType QUESTIONNAIRE = FHIRDefinedType.builder().value(ValueSet.QUESTIONNAIRE).build();

    /**
     * QuestionnaireResponse
     */
    public static final FHIRDefinedType QUESTIONNAIRE_RESPONSE = FHIRDefinedType.builder().value(ValueSet.QUESTIONNAIRE_RESPONSE).build();

    /**
     * RelatedPerson
     */
    public static final FHIRDefinedType RELATED_PERSON = FHIRDefinedType.builder().value(ValueSet.RELATED_PERSON).build();

    /**
     * RequestGroup
     */
    public static final FHIRDefinedType REQUEST_GROUP = FHIRDefinedType.builder().value(ValueSet.REQUEST_GROUP).build();

    /**
     * ResearchDefinition
     */
    public static final FHIRDefinedType RESEARCH_DEFINITION = FHIRDefinedType.builder().value(ValueSet.RESEARCH_DEFINITION).build();

    /**
     * ResearchElementDefinition
     */
    public static final FHIRDefinedType RESEARCH_ELEMENT_DEFINITION = FHIRDefinedType.builder().value(ValueSet.RESEARCH_ELEMENT_DEFINITION).build();

    /**
     * ResearchStudy
     */
    public static final FHIRDefinedType RESEARCH_STUDY = FHIRDefinedType.builder().value(ValueSet.RESEARCH_STUDY).build();

    /**
     * ResearchSubject
     */
    public static final FHIRDefinedType RESEARCH_SUBJECT = FHIRDefinedType.builder().value(ValueSet.RESEARCH_SUBJECT).build();

    /**
     * Resource
     */
    public static final FHIRDefinedType RESOURCE = FHIRDefinedType.builder().value(ValueSet.RESOURCE).build();

    /**
     * RiskAssessment
     */
    public static final FHIRDefinedType RISK_ASSESSMENT = FHIRDefinedType.builder().value(ValueSet.RISK_ASSESSMENT).build();

    /**
     * RiskEvidenceSynthesis
     */
    public static final FHIRDefinedType RISK_EVIDENCE_SYNTHESIS = FHIRDefinedType.builder().value(ValueSet.RISK_EVIDENCE_SYNTHESIS).build();

    /**
     * Schedule
     */
    public static final FHIRDefinedType SCHEDULE = FHIRDefinedType.builder().value(ValueSet.SCHEDULE).build();

    /**
     * SearchParameter
     */
    public static final FHIRDefinedType SEARCH_PARAMETER = FHIRDefinedType.builder().value(ValueSet.SEARCH_PARAMETER).build();

    /**
     * ServiceRequest
     */
    public static final FHIRDefinedType SERVICE_REQUEST = FHIRDefinedType.builder().value(ValueSet.SERVICE_REQUEST).build();

    /**
     * Slot
     */
    public static final FHIRDefinedType SLOT = FHIRDefinedType.builder().value(ValueSet.SLOT).build();

    /**
     * Specimen
     */
    public static final FHIRDefinedType SPECIMEN = FHIRDefinedType.builder().value(ValueSet.SPECIMEN).build();

    /**
     * SpecimenDefinition
     */
    public static final FHIRDefinedType SPECIMEN_DEFINITION = FHIRDefinedType.builder().value(ValueSet.SPECIMEN_DEFINITION).build();

    /**
     * StructureDefinition
     */
    public static final FHIRDefinedType STRUCTURE_DEFINITION = FHIRDefinedType.builder().value(ValueSet.STRUCTURE_DEFINITION).build();

    /**
     * StructureMap
     */
    public static final FHIRDefinedType STRUCTURE_MAP = FHIRDefinedType.builder().value(ValueSet.STRUCTURE_MAP).build();

    /**
     * Subscription
     */
    public static final FHIRDefinedType SUBSCRIPTION = FHIRDefinedType.builder().value(ValueSet.SUBSCRIPTION).build();

    /**
     * Substance
     */
    public static final FHIRDefinedType SUBSTANCE = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE).build();

    /**
     * SubstanceNucleicAcid
     */
    public static final FHIRDefinedType SUBSTANCE_NUCLEIC_ACID = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_NUCLEIC_ACID).build();

    /**
     * SubstancePolymer
     */
    public static final FHIRDefinedType SUBSTANCE_POLYMER = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_POLYMER).build();

    /**
     * SubstanceProtein
     */
    public static final FHIRDefinedType SUBSTANCE_PROTEIN = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_PROTEIN).build();

    /**
     * SubstanceReferenceInformation
     */
    public static final FHIRDefinedType SUBSTANCE_REFERENCE_INFORMATION = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_REFERENCE_INFORMATION).build();

    /**
     * SubstanceSourceMaterial
     */
    public static final FHIRDefinedType SUBSTANCE_SOURCE_MATERIAL = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_SOURCE_MATERIAL).build();

    /**
     * SubstanceSpecification
     */
    public static final FHIRDefinedType SUBSTANCE_SPECIFICATION = FHIRDefinedType.builder().value(ValueSet.SUBSTANCE_SPECIFICATION).build();

    /**
     * SupplyDelivery
     */
    public static final FHIRDefinedType SUPPLY_DELIVERY = FHIRDefinedType.builder().value(ValueSet.SUPPLY_DELIVERY).build();

    /**
     * SupplyRequest
     */
    public static final FHIRDefinedType SUPPLY_REQUEST = FHIRDefinedType.builder().value(ValueSet.SUPPLY_REQUEST).build();

    /**
     * Task
     */
    public static final FHIRDefinedType TASK = FHIRDefinedType.builder().value(ValueSet.TASK).build();

    /**
     * TerminologyCapabilities
     */
    public static final FHIRDefinedType TERMINOLOGY_CAPABILITIES = FHIRDefinedType.builder().value(ValueSet.TERMINOLOGY_CAPABILITIES).build();

    /**
     * TestReport
     */
    public static final FHIRDefinedType TEST_REPORT = FHIRDefinedType.builder().value(ValueSet.TEST_REPORT).build();

    /**
     * TestScript
     */
    public static final FHIRDefinedType TEST_SCRIPT = FHIRDefinedType.builder().value(ValueSet.TEST_SCRIPT).build();

    /**
     * ValueSet
     */
    public static final FHIRDefinedType VALUE_SET = FHIRDefinedType.builder().value(ValueSet.VALUE_SET).build();

    /**
     * VerificationResult
     */
    public static final FHIRDefinedType VERIFICATION_RESULT = FHIRDefinedType.builder().value(ValueSet.VERIFICATION_RESULT).build();

    /**
     * VisionPrescription
     */
    public static final FHIRDefinedType VISION_PRESCRIPTION = FHIRDefinedType.builder().value(ValueSet.VISION_PRESCRIPTION).build();

    private volatile int hashCode;

    private FHIRDefinedType(Builder builder) {
        super(builder);
    }

    public static FHIRDefinedType of(ValueSet value) {
        switch (value) {
        case ADDRESS:
            return ADDRESS;
        case AGE:
            return AGE;
        case ANNOTATION:
            return ANNOTATION;
        case ATTACHMENT:
            return ATTACHMENT;
        case BACKBONE_ELEMENT:
            return BACKBONE_ELEMENT;
        case CODEABLE_CONCEPT:
            return CODEABLE_CONCEPT;
        case CODING:
            return CODING;
        case CONTACT_DETAIL:
            return CONTACT_DETAIL;
        case CONTACT_POINT:
            return CONTACT_POINT;
        case CONTRIBUTOR:
            return CONTRIBUTOR;
        case COUNT:
            return COUNT;
        case DATA_REQUIREMENT:
            return DATA_REQUIREMENT;
        case DISTANCE:
            return DISTANCE;
        case DOSAGE:
            return DOSAGE;
        case DURATION:
            return DURATION;
        case ELEMENT:
            return ELEMENT;
        case ELEMENT_DEFINITION:
            return ELEMENT_DEFINITION;
        case EXPRESSION:
            return EXPRESSION;
        case EXTENSION:
            return EXTENSION;
        case HUMAN_NAME:
            return HUMAN_NAME;
        case IDENTIFIER:
            return IDENTIFIER;
        case MARKETING_STATUS:
            return MARKETING_STATUS;
        case META:
            return META;
        case MONEY:
            return MONEY;
        case MONEY_QUANTITY:
            return MONEY_QUANTITY;
        case NARRATIVE:
            return NARRATIVE;
        case PARAMETER_DEFINITION:
            return PARAMETER_DEFINITION;
        case PERIOD:
            return PERIOD;
        case POPULATION:
            return POPULATION;
        case PROD_CHARACTERISTIC:
            return PROD_CHARACTERISTIC;
        case PRODUCT_SHELF_LIFE:
            return PRODUCT_SHELF_LIFE;
        case QUANTITY:
            return QUANTITY;
        case RANGE:
            return RANGE;
        case RATIO:
            return RATIO;
        case REFERENCE:
            return REFERENCE;
        case RELATED_ARTIFACT:
            return RELATED_ARTIFACT;
        case SAMPLED_DATA:
            return SAMPLED_DATA;
        case SIGNATURE:
            return SIGNATURE;
        case SIMPLE_QUANTITY:
            return SIMPLE_QUANTITY;
        case SUBSTANCE_AMOUNT:
            return SUBSTANCE_AMOUNT;
        case TIMING:
            return TIMING;
        case TRIGGER_DEFINITION:
            return TRIGGER_DEFINITION;
        case USAGE_CONTEXT:
            return USAGE_CONTEXT;
        case BASE64BINARY:
            return BASE64BINARY;
        case BOOLEAN:
            return BOOLEAN;
        case CANONICAL:
            return CANONICAL;
        case CODE:
            return CODE;
        case DATE:
            return DATE;
        case DATE_TIME:
            return DATE_TIME;
        case DECIMAL:
            return DECIMAL;
        case ID:
            return ID;
        case INSTANT:
            return INSTANT;
        case INTEGER:
            return INTEGER;
        case MARKDOWN:
            return MARKDOWN;
        case OID:
            return OID;
        case POSITIVE_INT:
            return POSITIVE_INT;
        case STRING:
            return STRING;
        case TIME:
            return TIME;
        case UNSIGNED_INT:
            return UNSIGNED_INT;
        case URI:
            return URI;
        case URL:
            return URL;
        case UUID:
            return UUID;
        case XHTML:
            return XHTML;
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

    public static FHIRDefinedType of(java.lang.String value) {
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
