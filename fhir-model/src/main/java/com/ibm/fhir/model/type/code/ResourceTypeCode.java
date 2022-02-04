/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/resource-types")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ResourceTypeCode extends Code {
    /**
     * Resource
     * 
     * <p>--- Abstract Type! ---This is the base resource type for everything.
     */
    public static final ResourceTypeCode RESOURCE = ResourceTypeCode.builder().value(ResourceType.RESOURCE).build();

    /**
     * Binary
     * 
     * <p>A resource that represents the data of a single raw artifact as digital content accessible in its native format. A 
     * Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
     */
    public static final ResourceTypeCode BINARY = ResourceTypeCode.builder().value(ResourceType.BINARY).build();

    /**
     * Bundle
     * 
     * <p>A container for a collection of resources.
     */
    public static final ResourceTypeCode BUNDLE = ResourceTypeCode.builder().value(ResourceType.BUNDLE).build();

    /**
     * DomainResource
     * 
     * <p>--- Abstract Type! ---A resource that includes narrative, extensions, and contained resources.
     */
    public static final ResourceTypeCode DOMAIN_RESOURCE = ResourceTypeCode.builder().value(ResourceType.DOMAIN_RESOURCE).build();

    /**
     * Account
     * 
     * <p>A financial tool for tracking value accrued for a particular purpose. In the healthcare field, used to track 
     * charges for a patient, cost centers, etc.
     */
    public static final ResourceTypeCode ACCOUNT = ResourceTypeCode.builder().value(ResourceType.ACCOUNT).build();

    /**
     * ActivityDefinition
     * 
     * <p>This resource allows for the definition of some activity to be performed, independent of a particular patient, 
     * practitioner, or other performance context.
     */
    public static final ResourceTypeCode ACTIVITY_DEFINITION = ResourceTypeCode.builder().value(ResourceType.ACTIVITY_DEFINITION).build();

    /**
     * AdministrableProductDefinition
     * 
     * <p>A medicinal product in the final form which is suitable for administering to a patient (after any mixing of 
     * multiple components, dissolution etc. has been performed).
     */
    public static final ResourceTypeCode ADMINISTRABLE_PRODUCT_DEFINITION = ResourceTypeCode.builder().value(ResourceType.ADMINISTRABLE_PRODUCT_DEFINITION).build();

    /**
     * AdverseEvent
     * 
     * <p>Actual or potential/avoided event causing unintended physical injury resulting from or contributed to by medical 
     * care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or 
     * hospitalization, or that results in death.
     */
    public static final ResourceTypeCode ADVERSE_EVENT = ResourceTypeCode.builder().value(ResourceType.ADVERSE_EVENT).build();

    /**
     * AllergyIntolerance
     * 
     * <p>Risk of harmful or undesirable, physiological response which is unique to an individual and associated with 
     * exposure to a substance.
     */
    public static final ResourceTypeCode ALLERGY_INTOLERANCE = ResourceTypeCode.builder().value(ResourceType.ALLERGY_INTOLERANCE).build();

    /**
     * Appointment
     * 
     * <p>A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a 
     * specific date/time. This may result in one or more Encounter(s).
     */
    public static final ResourceTypeCode APPOINTMENT = ResourceTypeCode.builder().value(ResourceType.APPOINTMENT).build();

    /**
     * AppointmentResponse
     * 
     * <p>A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
     */
    public static final ResourceTypeCode APPOINTMENT_RESPONSE = ResourceTypeCode.builder().value(ResourceType.APPOINTMENT_RESPONSE).build();

    /**
     * AuditEvent
     * 
     * <p>A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion 
     * attempts and monitoring for inappropriate usage.
     */
    public static final ResourceTypeCode AUDIT_EVENT = ResourceTypeCode.builder().value(ResourceType.AUDIT_EVENT).build();

    /**
     * Basic
     * 
     * <p>Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing 
     * resource, and custom resources not appropriate for inclusion in the FHIR specification.
     */
    public static final ResourceTypeCode BASIC = ResourceTypeCode.builder().value(ResourceType.BASIC).build();

    /**
     * BiologicallyDerivedProduct
     * 
     * <p>A material substance originating from a biological entity intended to be transplanted or infused
     * <p>into another (possibly the same) biological entity.
     */
    public static final ResourceTypeCode BIOLOGICALLY_DERIVED_PRODUCT = ResourceTypeCode.builder().value(ResourceType.BIOLOGICALLY_DERIVED_PRODUCT).build();

    /**
     * BodyStructure
     * 
     * <p>Record details about an anatomical structure. This resource may be used when a coded concept does not provide the 
     * necessary detail needed for the use case.
     */
    public static final ResourceTypeCode BODY_STRUCTURE = ResourceTypeCode.builder().value(ResourceType.BODY_STRUCTURE).build();

    /**
     * CapabilityStatement
     * 
     * <p>A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server for a particular version of 
     * FHIR that may be used as a statement of actual server functionality or a statement of required or desired server 
     * implementation.
     */
    public static final ResourceTypeCode CAPABILITY_STATEMENT = ResourceTypeCode.builder().value(ResourceType.CAPABILITY_STATEMENT).build();

    /**
     * CarePlan
     * 
     * <p>Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or 
     * community for a period of time, possibly limited to care for a specific condition or set of conditions.
     */
    public static final ResourceTypeCode CARE_PLAN = ResourceTypeCode.builder().value(ResourceType.CARE_PLAN).build();

    /**
     * CareTeam
     * 
     * <p>The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of 
     * care for a patient.
     */
    public static final ResourceTypeCode CARE_TEAM = ResourceTypeCode.builder().value(ResourceType.CARE_TEAM).build();

    /**
     * CatalogEntry
     * 
     * <p>Catalog entries are wrappers that contextualize items included in a catalog.
     */
    public static final ResourceTypeCode CATALOG_ENTRY = ResourceTypeCode.builder().value(ResourceType.CATALOG_ENTRY).build();

    /**
     * ChargeItem
     * 
     * <p>The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore 
     * referring not only to the product, but containing in addition details of the provision, like date, time, amounts and 
     * participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal 
     * cost allocation.
     */
    public static final ResourceTypeCode CHARGE_ITEM = ResourceTypeCode.builder().value(ResourceType.CHARGE_ITEM).build();

    /**
     * ChargeItemDefinition
     * 
     * <p>The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate 
     * costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a 
     * rough structure and requires profiling for each type of billing code system.
     */
    public static final ResourceTypeCode CHARGE_ITEM_DEFINITION = ResourceTypeCode.builder().value(ResourceType.CHARGE_ITEM_DEFINITION).build();

    /**
     * Citation
     * 
     * <p>The Citation Resource enables reference to any knowledge artifact for purposes of identification and attribution. 
     * The Citation Resource supports existing reference structures and developing publication practices such as versioning, 
     * expressing complex contributorship roles, and referencing computable resources.
     */
    public static final ResourceTypeCode CITATION = ResourceTypeCode.builder().value(ResourceType.CITATION).build();

    /**
     * Claim
     * 
     * <p>A provider issued list of professional services and products which have been provided, or are to be provided, to a 
     * patient which is sent to an insurer for reimbursement.
     */
    public static final ResourceTypeCode CLAIM = ResourceTypeCode.builder().value(ResourceType.CLAIM).build();

    /**
     * ClaimResponse
     * 
     * <p>This resource provides the adjudication details from the processing of a Claim resource.
     */
    public static final ResourceTypeCode CLAIM_RESPONSE = ResourceTypeCode.builder().value(ResourceType.CLAIM_RESPONSE).build();

    /**
     * ClinicalImpression
     * 
     * <p>A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning 
     * the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with 
     * a clinical consultation / encounter, but this varies greatly depending on the clinical workflow. This resource is 
     * called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools 
     * such as Apgar score.
     */
    public static final ResourceTypeCode CLINICAL_IMPRESSION = ResourceTypeCode.builder().value(ResourceType.CLINICAL_IMPRESSION).build();

    /**
     * ClinicalUseDefinition
     * 
     * <p>A single issue - either an indication, contraindication, interaction or an undesirable effect for a medicinal 
     * product, medication, device or procedure.
     */
    public static final ResourceTypeCode CLINICAL_USE_DEFINITION = ResourceTypeCode.builder().value(ResourceType.CLINICAL_USE_DEFINITION).build();

    /**
     * CodeSystem
     * 
     * <p>The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement 
     * and its key properties, and optionally define a part or all of its content.
     */
    public static final ResourceTypeCode CODE_SYSTEM = ResourceTypeCode.builder().value(ResourceType.CODE_SYSTEM).build();

    /**
     * Communication
     * 
     * <p>An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public 
     * health agency that was notified about a reportable condition.
     */
    public static final ResourceTypeCode COMMUNICATION = ResourceTypeCode.builder().value(ResourceType.COMMUNICATION).build();

    /**
     * CommunicationRequest
     * 
     * <p>A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the 
     * CDS system proposes that the public health agency be notified about a reportable condition.
     */
    public static final ResourceTypeCode COMMUNICATION_REQUEST = ResourceTypeCode.builder().value(ResourceType.COMMUNICATION_REQUEST).build();

    /**
     * CompartmentDefinition
     * 
     * <p>A compartment definition that defines how resources are accessed on a server.
     */
    public static final ResourceTypeCode COMPARTMENT_DEFINITION = ResourceTypeCode.builder().value(ResourceType.COMPARTMENT_DEFINITION).build();

    /**
     * Composition
     * 
     * <p>A set of healthcare-related information that is assembled together into a single logical package that provides a 
     * single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who 
     * is making the statement. A Composition defines the structure and narrative content necessary for a document. However, 
     * a Composition alone does not constitute a document. Rather, the Composition must be the first entry in a Bundle where 
     * Bundle.type=document, and any other resources referenced from Composition must be included as subsequent entries in 
     * the Bundle (for example Patient, Practitioner, Encounter, etc.).
     */
    public static final ResourceTypeCode COMPOSITION = ResourceTypeCode.builder().value(ResourceType.COMPOSITION).build();

    /**
     * ConceptMap
     * 
     * <p>A statement of relationships from one set of concepts to one or more other concepts - either concepts in code 
     * systems, or data element/data element concepts, or classes in class models.
     */
    public static final ResourceTypeCode CONCEPT_MAP = ResourceTypeCode.builder().value(ResourceType.CONCEPT_MAP).build();

    /**
     * Condition
     * 
     * <p>A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a 
     * level of concern.
     */
    public static final ResourceTypeCode CONDITION = ResourceTypeCode.builder().value(ResourceType.CONDITION).build();

    /**
     * Consent
     * 
     * <p>A record of a healthcare consumer’s choices, which permits or denies identified recipient(s) or recipient role(s) 
     * to perform one or more actions within a given policy context, for specific purposes and periods of time.
     */
    public static final ResourceTypeCode CONSENT = ResourceTypeCode.builder().value(ResourceType.CONSENT).build();

    /**
     * Contract
     * 
     * <p>Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
     */
    public static final ResourceTypeCode CONTRACT = ResourceTypeCode.builder().value(ResourceType.CONTRACT).build();

    /**
     * Coverage
     * 
     * <p>Financial instrument which may be used to reimburse or pay for health care products and services. Includes both 
     * insurance and self-payment.
     */
    public static final ResourceTypeCode COVERAGE = ResourceTypeCode.builder().value(ResourceType.COVERAGE).build();

    /**
     * CoverageEligibilityRequest
     * 
     * <p>The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to 
     * respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is 
     * valid and in-force and optionally to provide the insurance details of the policy.
     */
    public static final ResourceTypeCode COVERAGE_ELIGIBILITY_REQUEST = ResourceTypeCode.builder().value(ResourceType.COVERAGE_ELIGIBILITY_REQUEST).build();

    /**
     * CoverageEligibilityResponse
     * 
     * <p>This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
     */
    public static final ResourceTypeCode COVERAGE_ELIGIBILITY_RESPONSE = ResourceTypeCode.builder().value(ResourceType.COVERAGE_ELIGIBILITY_RESPONSE).build();

    /**
     * DetectedIssue
     * 
     * <p>Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for 
     * a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
     */
    public static final ResourceTypeCode DETECTED_ISSUE = ResourceTypeCode.builder().value(ResourceType.DETECTED_ISSUE).build();

    /**
     * Device
     * 
     * <p>A type of a manufactured item that is used in the provision of healthcare without being substantially changed 
     * through that activity. The device may be a medical or non-medical device.
     */
    public static final ResourceTypeCode DEVICE = ResourceTypeCode.builder().value(ResourceType.DEVICE).build();

    /**
     * DeviceDefinition
     * 
     * <p>The characteristics, operational status and capabilities of a medical-related component of a medical device.
     */
    public static final ResourceTypeCode DEVICE_DEFINITION = ResourceTypeCode.builder().value(ResourceType.DEVICE_DEFINITION).build();

    /**
     * DeviceMetric
     * 
     * <p>Describes a measurement, calculation or setting capability of a medical device.
     */
    public static final ResourceTypeCode DEVICE_METRIC = ResourceTypeCode.builder().value(ResourceType.DEVICE_METRIC).build();

    /**
     * DeviceRequest
     * 
     * <p>Represents a request for a patient to employ a medical device. The device may be an implantable device, or an 
     * external assistive device, such as a walker.
     */
    public static final ResourceTypeCode DEVICE_REQUEST = ResourceTypeCode.builder().value(ResourceType.DEVICE_REQUEST).build();

    /**
     * DeviceUseStatement
     * 
     * <p>A record of a device being used by a patient where the record is the result of a report from the patient or another 
     * clinician.
     */
    public static final ResourceTypeCode DEVICE_USE_STATEMENT = ResourceTypeCode.builder().value(ResourceType.DEVICE_USE_STATEMENT).build();

    /**
     * DiagnosticReport
     * 
     * <p>The findings and interpretation of diagnostic tests performed on patients, groups of patients, devices, and 
     * locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider 
     * information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation 
     * of diagnostic reports.
     */
    public static final ResourceTypeCode DIAGNOSTIC_REPORT = ResourceTypeCode.builder().value(ResourceType.DIAGNOSTIC_REPORT).build();

    /**
     * DocumentManifest
     * 
     * <p>A collection of documents compiled for a purpose together with metadata that applies to the collection.
     */
    public static final ResourceTypeCode DOCUMENT_MANIFEST = ResourceTypeCode.builder().value(ResourceType.DOCUMENT_MANIFEST).build();

    /**
     * DocumentReference
     * 
     * <p>A reference to a document of any kind for any purpose. Provides metadata about the document so that the document 
     * can be discovered and managed. The scope of a document is any seralized object with a mime-type, so includes formal 
     * patient centric documents (CDA), cliical notes, scanned paper, and non-patient specific documents like policy text.
     */
    public static final ResourceTypeCode DOCUMENT_REFERENCE = ResourceTypeCode.builder().value(ResourceType.DOCUMENT_REFERENCE).build();

    /**
     * Encounter
     * 
     * <p>An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or 
     * assessing the health status of a patient.
     */
    public static final ResourceTypeCode ENCOUNTER = ResourceTypeCode.builder().value(ResourceType.ENCOUNTER).build();

    /**
     * Endpoint
     * 
     * <p>The technical details of an endpoint that can be used for electronic services, such as for web services providing 
     * XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
     */
    public static final ResourceTypeCode ENDPOINT = ResourceTypeCode.builder().value(ResourceType.ENDPOINT).build();

    /**
     * EnrollmentRequest
     * 
     * <p>This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
     */
    public static final ResourceTypeCode ENROLLMENT_REQUEST = ResourceTypeCode.builder().value(ResourceType.ENROLLMENT_REQUEST).build();

    /**
     * EnrollmentResponse
     * 
     * <p>This resource provides enrollment and plan details from the processing of an EnrollmentRequest resource.
     */
    public static final ResourceTypeCode ENROLLMENT_RESPONSE = ResourceTypeCode.builder().value(ResourceType.ENROLLMENT_RESPONSE).build();

    /**
     * EpisodeOfCare
     * 
     * <p>An association between a patient and an organization / healthcare provider(s) during which time encounters may 
     * occur. The managing organization assumes a level of responsibility for the patient during this time.
     */
    public static final ResourceTypeCode EPISODE_OF_CARE = ResourceTypeCode.builder().value(ResourceType.EPISODE_OF_CARE).build();

    /**
     * EventDefinition
     * 
     * <p>The EventDefinition resource provides a reusable description of when a particular event can occur.
     */
    public static final ResourceTypeCode EVENT_DEFINITION = ResourceTypeCode.builder().value(ResourceType.EVENT_DEFINITION).build();

    /**
     * Evidence
     * 
     * <p>The Evidence Resource provides a machine-interpretable expression of an evidence concept including the evidence 
     * variables (eg population, exposures/interventions, comparators, outcomes, measured variables, confounding variables), 
     * the statistics, and the certainty of this evidence.
     */
    public static final ResourceTypeCode EVIDENCE = ResourceTypeCode.builder().value(ResourceType.EVIDENCE).build();

    /**
     * EvidenceReport
     * 
     * <p>The EvidenceReport Resource is a specialized container for a collection of resources and codable concepts, adapted 
     * to support compositions of Evidence, EvidenceVariable, and Citation resources and related concepts.
     */
    public static final ResourceTypeCode EVIDENCE_REPORT = ResourceTypeCode.builder().value(ResourceType.EVIDENCE_REPORT).build();

    /**
     * EvidenceVariable
     * 
     * <p>The EvidenceVariable resource describes an element that knowledge (Evidence) is about.
     */
    public static final ResourceTypeCode EVIDENCE_VARIABLE = ResourceTypeCode.builder().value(ResourceType.EVIDENCE_VARIABLE).build();

    /**
     * ExampleScenario
     * 
     * <p>Example of workflow instance.
     */
    public static final ResourceTypeCode EXAMPLE_SCENARIO = ResourceTypeCode.builder().value(ResourceType.EXAMPLE_SCENARIO).build();

    /**
     * ExplanationOfBenefit
     * 
     * <p>This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally 
     * account balance information, for informing the subscriber of the benefits provided.
     */
    public static final ResourceTypeCode EXPLANATION_OF_BENEFIT = ResourceTypeCode.builder().value(ResourceType.EXPLANATION_OF_BENEFIT).build();

    /**
     * FamilyMemberHistory
     * 
     * <p>Significant health conditions for a person related to the patient relevant in the context of care for the patient.
     */
    public static final ResourceTypeCode FAMILY_MEMBER_HISTORY = ResourceTypeCode.builder().value(ResourceType.FAMILY_MEMBER_HISTORY).build();

    /**
     * Flag
     * 
     * <p>Prospective warnings of potential issues when providing care to the patient.
     */
    public static final ResourceTypeCode FLAG = ResourceTypeCode.builder().value(ResourceType.FLAG).build();

    /**
     * Goal
     * 
     * <p>Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring 
     * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
     */
    public static final ResourceTypeCode GOAL = ResourceTypeCode.builder().value(ResourceType.GOAL).build();

    /**
     * GraphDefinition
     * 
     * <p>A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by 
     * following references. The Graph Definition resource defines a set and makes rules about the set.
     */
    public static final ResourceTypeCode GRAPH_DEFINITION = ResourceTypeCode.builder().value(ResourceType.GRAPH_DEFINITION).build();

    /**
     * Group
     * 
     * <p>Represents a defined collection of entities that may be discussed or acted upon collectively but which are not 
     * expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an 
     * Organization.
     */
    public static final ResourceTypeCode GROUP = ResourceTypeCode.builder().value(ResourceType.GROUP).build();

    /**
     * GuidanceResponse
     * 
     * <p>A guidance response is the formal response to a guidance request, including any output parameters returned by the 
     * evaluation, as well as the description of any proposed actions to be taken.
     */
    public static final ResourceTypeCode GUIDANCE_RESPONSE = ResourceTypeCode.builder().value(ResourceType.GUIDANCE_RESPONSE).build();

    /**
     * HealthcareService
     * 
     * <p>The details of a healthcare service available at a location.
     */
    public static final ResourceTypeCode HEALTHCARE_SERVICE = ResourceTypeCode.builder().value(ResourceType.HEALTHCARE_SERVICE).build();

    /**
     * ImagingStudy
     * 
     * <p>Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which 
     * includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a 
     * common context. A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple 
     * series of different modalities.
     */
    public static final ResourceTypeCode IMAGING_STUDY = ResourceTypeCode.builder().value(ResourceType.IMAGING_STUDY).build();

    /**
     * Immunization
     * 
     * <p>Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a 
     * patient, a clinician or another party.
     */
    public static final ResourceTypeCode IMMUNIZATION = ResourceTypeCode.builder().value(ResourceType.IMMUNIZATION).build();

    /**
     * ImmunizationEvaluation
     * 
     * <p>Describes a comparison of an immunization event against published recommendations to determine if the 
     * administration is "valid" in relation to those recommendations.
     */
    public static final ResourceTypeCode IMMUNIZATION_EVALUATION = ResourceTypeCode.builder().value(ResourceType.IMMUNIZATION_EVALUATION).build();

    /**
     * ImmunizationRecommendation
     * 
     * <p>A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional 
     * supporting justification.
     */
    public static final ResourceTypeCode IMMUNIZATION_RECOMMENDATION = ResourceTypeCode.builder().value(ResourceType.IMMUNIZATION_RECOMMENDATION).build();

    /**
     * ImplementationGuide
     * 
     * <p>A set of rules of how a particular interoperability or standards problem is solved - typically through the use of 
     * FHIR resources. This resource is used to gather all the parts of an implementation guide into a logical whole and to 
     * publish a computable definition of all the parts.
     */
    public static final ResourceTypeCode IMPLEMENTATION_GUIDE = ResourceTypeCode.builder().value(ResourceType.IMPLEMENTATION_GUIDE).build();

    /**
     * Ingredient
     * 
     * <p>An ingredient of a manufactured item or pharmaceutical product.
     */
    public static final ResourceTypeCode INGREDIENT = ResourceTypeCode.builder().value(ResourceType.INGREDIENT).build();

    /**
     * InsurancePlan
     * 
     * <p>Details of a Health Insurance product/plan provided by an organization.
     */
    public static final ResourceTypeCode INSURANCE_PLAN = ResourceTypeCode.builder().value(ResourceType.INSURANCE_PLAN).build();

    /**
     * Invoice
     * 
     * <p>Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing 
     * purpose.
     */
    public static final ResourceTypeCode INVOICE = ResourceTypeCode.builder().value(ResourceType.INVOICE).build();

    /**
     * Library
     * 
     * <p>The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and 
     * expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a 
     * collection of knowledge assets.
     */
    public static final ResourceTypeCode LIBRARY = ResourceTypeCode.builder().value(ResourceType.LIBRARY).build();

    /**
     * Linkage
     * 
     * <p>Identifies two or more records (resource instances) that refer to the same real-world "occurrence".
     */
    public static final ResourceTypeCode LINKAGE = ResourceTypeCode.builder().value(ResourceType.LINKAGE).build();

    /**
     * List
     * 
     * <p>A list is a curated collection of resources.
     */
    public static final ResourceTypeCode LIST = ResourceTypeCode.builder().value(ResourceType.LIST).build();

    /**
     * Location
     * 
     * <p>Details and position information for a physical place where services are provided and resources and participants 
     * may be stored, found, contained, or accommodated.
     */
    public static final ResourceTypeCode LOCATION = ResourceTypeCode.builder().value(ResourceType.LOCATION).build();

    /**
     * ManufacturedItemDefinition
     * 
     * <p>The definition and characteristics of a medicinal manufactured item, such as a tablet or capsule, as contained in a 
     * packaged medicinal product.
     */
    public static final ResourceTypeCode MANUFACTURED_ITEM_DEFINITION = ResourceTypeCode.builder().value(ResourceType.MANUFACTURED_ITEM_DEFINITION).build();

    /**
     * Measure
     * 
     * <p>The Measure resource provides the definition of a quality measure.
     */
    public static final ResourceTypeCode MEASURE = ResourceTypeCode.builder().value(ResourceType.MEASURE).build();

    /**
     * MeasureReport
     * 
     * <p>The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the 
     * resources involved in that calculation.
     */
    public static final ResourceTypeCode MEASURE_REPORT = ResourceTypeCode.builder().value(ResourceType.MEASURE_REPORT).build();

    /**
     * Media
     * 
     * <p>A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by 
     * direct reference.
     */
    public static final ResourceTypeCode MEDIA = ResourceTypeCode.builder().value(ResourceType.MEDIA).build();

    /**
     * Medication
     * 
     * <p>This resource is primarily used for the identification and definition of a medication for the purposes of 
     * prescribing, dispensing, and administering a medication as well as for making statements about medication use.
     */
    public static final ResourceTypeCode MEDICATION = ResourceTypeCode.builder().value(ResourceType.MEDICATION).build();

    /**
     * MedicationAdministration
     * 
     * <p>Describes the event of a patient consuming or otherwise being administered a medication. This may be as simple as 
     * swallowing a tablet or it may be a long running infusion. Related resources tie this event to the authorizing 
     * prescription, and the specific encounter between patient and health care practitioner.
     */
    public static final ResourceTypeCode MEDICATION_ADMINISTRATION = ResourceTypeCode.builder().value(ResourceType.MEDICATION_ADMINISTRATION).build();

    /**
     * MedicationDispense
     * 
     * <p>Indicates that a medication product is to be or has been dispensed for a named person/patient. This includes a 
     * description of the medication product (supply) provided and the instructions for administering the medication. The 
     * medication dispense is the result of a pharmacy system responding to a medication order.
     */
    public static final ResourceTypeCode MEDICATION_DISPENSE = ResourceTypeCode.builder().value(ResourceType.MEDICATION_DISPENSE).build();

    /**
     * MedicationKnowledge
     * 
     * <p>Information about a medication that is used to support knowledge.
     */
    public static final ResourceTypeCode MEDICATION_KNOWLEDGE = ResourceTypeCode.builder().value(ResourceType.MEDICATION_KNOWLEDGE).build();

    /**
     * MedicationRequest
     * 
     * <p>An order or request for both supply of the medication and the instructions for administration of the medication to 
     * a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to 
     * generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with 
     * workflow patterns.
     */
    public static final ResourceTypeCode MEDICATION_REQUEST = ResourceTypeCode.builder().value(ResourceType.MEDICATION_REQUEST).build();

    /**
     * MedicationStatement
     * 
     * <p>A record of a medication that is being consumed by a patient. A MedicationStatement may indicate that the patient 
     * may be taking the medication now or has taken the medication in the past or will be taking the medication in the 
     * future. The source of this information can be the patient, significant other (such as a family member or spouse), or a 
     * clinician. A common scenario where this information is captured is during the history taking process during a patient 
     * visit or stay. The medication information may come from sources such as the patient's memory, from a prescription 
     * bottle, or from a list of medications the patient, clinician or other party maintains. 
     * 
     * <p>The primary difference between a medication statement and a medication administration is that the medication 
     * administration has complete administration information and is based on actual administration information from the 
     * person who administered the medication. A medication statement is often, if not always, less specific. There is no 
     * required date/time when the medication was administered, in fact we only know that a source has reported the patient 
     * is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete 
     * or missing or less precise. As stated earlier, the medication statement information may come from the patient's 
     * memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains. 
     * Medication administration is more formal and is not missing detailed information.
     */
    public static final ResourceTypeCode MEDICATION_STATEMENT = ResourceTypeCode.builder().value(ResourceType.MEDICATION_STATEMENT).build();

    /**
     * MedicinalProductDefinition
     * 
     * <p>Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use, 
     * drug catalogs).
     */
    public static final ResourceTypeCode MEDICINAL_PRODUCT_DEFINITION = ResourceTypeCode.builder().value(ResourceType.MEDICINAL_PRODUCT_DEFINITION).build();

    /**
     * MessageDefinition
     * 
     * <p>Defines the characteristics of a message that can be shared between systems, including the type of event that 
     * initiates the message, the content to be transmitted and what response(s), if any, are permitted.
     */
    public static final ResourceTypeCode MESSAGE_DEFINITION = ResourceTypeCode.builder().value(ResourceType.MESSAGE_DEFINITION).build();

    /**
     * MessageHeader
     * 
     * <p>The header for a message exchange that is either requesting or responding to an action. The reference(s) that are 
     * the subject of the action as well as other information related to the action are typically transmitted in a bundle in 
     * which the MessageHeader resource instance is the first resource in the bundle.
     */
    public static final ResourceTypeCode MESSAGE_HEADER = ResourceTypeCode.builder().value(ResourceType.MESSAGE_HEADER).build();

    /**
     * MolecularSequence
     * 
     * <p>Raw data describing a biological sequence.
     */
    public static final ResourceTypeCode MOLECULAR_SEQUENCE = ResourceTypeCode.builder().value(ResourceType.MOLECULAR_SEQUENCE).build();

    /**
     * NamingSystem
     * 
     * <p>A curated namespace that issues unique symbols within that namespace for the identification of concepts, people, 
     * devices, etc. Represents a "System" used within the Identifier and Coding data types.
     */
    public static final ResourceTypeCode NAMING_SYSTEM = ResourceTypeCode.builder().value(ResourceType.NAMING_SYSTEM).build();

    /**
     * NutritionOrder
     * 
     * <p>A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
     */
    public static final ResourceTypeCode NUTRITION_ORDER = ResourceTypeCode.builder().value(ResourceType.NUTRITION_ORDER).build();

    /**
     * NutritionProduct
     * 
     * <p>A food or fluid product that is consumed by patients.
     */
    public static final ResourceTypeCode NUTRITION_PRODUCT = ResourceTypeCode.builder().value(ResourceType.NUTRITION_PRODUCT).build();

    /**
     * Observation
     * 
     * <p>Measurements and simple assertions made about a patient, device or other subject.
     */
    public static final ResourceTypeCode OBSERVATION = ResourceTypeCode.builder().value(ResourceType.OBSERVATION).build();

    /**
     * ObservationDefinition
     * 
     * <p>Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable 
     * health care service.
     */
    public static final ResourceTypeCode OBSERVATION_DEFINITION = ResourceTypeCode.builder().value(ResourceType.OBSERVATION_DEFINITION).build();

    /**
     * OperationDefinition
     * 
     * <p>A formal computable definition of an operation (on the RESTful interface) or a named query (using the search 
     * interaction).
     */
    public static final ResourceTypeCode OPERATION_DEFINITION = ResourceTypeCode.builder().value(ResourceType.OPERATION_DEFINITION).build();

    /**
     * OperationOutcome
     * 
     * <p>A collection of error, warning, or information messages that result from a system action.
     */
    public static final ResourceTypeCode OPERATION_OUTCOME = ResourceTypeCode.builder().value(ResourceType.OPERATION_OUTCOME).build();

    /**
     * Organization
     * 
     * <p>A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some 
     * form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare 
     * practice groups, payer/insurer, etc.
     */
    public static final ResourceTypeCode ORGANIZATION = ResourceTypeCode.builder().value(ResourceType.ORGANIZATION).build();

    /**
     * OrganizationAffiliation
     * 
     * <p>Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of 
     * relationship/sub-division relationship.
     */
    public static final ResourceTypeCode ORGANIZATION_AFFILIATION = ResourceTypeCode.builder().value(ResourceType.ORGANIZATION_AFFILIATION).build();

    /**
     * PackagedProductDefinition
     * 
     * <p>A medically related item or items, in a container or package.
     */
    public static final ResourceTypeCode PACKAGED_PRODUCT_DEFINITION = ResourceTypeCode.builder().value(ResourceType.PACKAGED_PRODUCT_DEFINITION).build();

    /**
     * Patient
     * 
     * <p>Demographics and other administrative information about an individual or animal receiving care or other health-
     * related services.
     */
    public static final ResourceTypeCode PATIENT = ResourceTypeCode.builder().value(ResourceType.PATIENT).build();

    /**
     * PaymentNotice
     * 
     * <p>This resource provides the status of the payment for goods and services rendered, and the request and response 
     * resource references.
     */
    public static final ResourceTypeCode PAYMENT_NOTICE = ResourceTypeCode.builder().value(ResourceType.PAYMENT_NOTICE).build();

    /**
     * PaymentReconciliation
     * 
     * <p>This resource provides the details including amount of a payment and allocates the payment items being paid.
     */
    public static final ResourceTypeCode PAYMENT_RECONCILIATION = ResourceTypeCode.builder().value(ResourceType.PAYMENT_RECONCILIATION).build();

    /**
     * Person
     * 
     * <p>Demographics and administrative information about a person independent of a specific health-related context.
     */
    public static final ResourceTypeCode PERSON = ResourceTypeCode.builder().value(ResourceType.PERSON).build();

    /**
     * PlanDefinition
     * 
     * <p>This resource allows for the definition of various types of plans as a sharable, consumable, and executable 
     * artifact. The resource is general enough to support the description of a broad range of clinical and non-clinical 
     * artifacts such as clinical decision support rules, order sets, protocols, and drug quality specifications.
     */
    public static final ResourceTypeCode PLAN_DEFINITION = ResourceTypeCode.builder().value(ResourceType.PLAN_DEFINITION).build();

    /**
     * Practitioner
     * 
     * <p>A person who is directly or indirectly involved in the provisioning of healthcare.
     */
    public static final ResourceTypeCode PRACTITIONER = ResourceTypeCode.builder().value(ResourceType.PRACTITIONER).build();

    /**
     * PractitionerRole
     * 
     * <p>A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a 
     * period of time.
     */
    public static final ResourceTypeCode PRACTITIONER_ROLE = ResourceTypeCode.builder().value(ResourceType.PRACTITIONER_ROLE).build();

    /**
     * Procedure
     * 
     * <p>An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or 
     * less invasive like long term services, counseling, or hypnotherapy.
     */
    public static final ResourceTypeCode PROCEDURE = ResourceTypeCode.builder().value(ResourceType.PROCEDURE).build();

    /**
     * Provenance
     * 
     * <p>Provenance of a resource is a record that describes entities and processes involved in producing and delivering or 
     * otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling 
     * trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become 
     * important records with their own provenance. Provenance statement indicates clinical significance in terms of 
     * confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document 
     * Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust 
     * policies.
     */
    public static final ResourceTypeCode PROVENANCE = ResourceTypeCode.builder().value(ResourceType.PROVENANCE).build();

    /**
     * Questionnaire
     * 
     * <p>A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide 
     * detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
     */
    public static final ResourceTypeCode QUESTIONNAIRE = ResourceTypeCode.builder().value(ResourceType.QUESTIONNAIRE).build();

    /**
     * QuestionnaireResponse
     * 
     * <p>A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, 
     * corresponding to the structure of the grouping of the questionnaire being responded to.
     */
    public static final ResourceTypeCode QUESTIONNAIRE_RESPONSE = ResourceTypeCode.builder().value(ResourceType.QUESTIONNAIRE_RESPONSE).build();

    /**
     * RegulatedAuthorization
     * 
     * <p>Regulatory approval, clearance or licencing related to a regulated product, treatment, facility or activity that is 
     * cited in a guidance, regulation, rule or legislative act. An example is Market Authorization relating to a Medicinal 
     * Product.
     */
    public static final ResourceTypeCode REGULATED_AUTHORIZATION = ResourceTypeCode.builder().value(ResourceType.REGULATED_AUTHORIZATION).build();

    /**
     * RelatedPerson
     * 
     * <p>Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor 
     * has a formal responsibility in the care process.
     */
    public static final ResourceTypeCode RELATED_PERSON = ResourceTypeCode.builder().value(ResourceType.RELATED_PERSON).build();

    /**
     * RequestGroup
     * 
     * <p>A group of related requests that can be used to capture intended activities that have inter-dependencies such as 
     * "give this medication after that one".
     */
    public static final ResourceTypeCode REQUEST_GROUP = ResourceTypeCode.builder().value(ResourceType.REQUEST_GROUP).build();

    /**
     * ResearchDefinition
     * 
     * <p>The ResearchDefinition resource describes the conditional state (population and any exposures being compared within 
     * the population) and outcome (if specified) that the knowledge (evidence, assertion, recommendation) is about.
     */
    public static final ResourceTypeCode RESEARCH_DEFINITION = ResourceTypeCode.builder().value(ResourceType.RESEARCH_DEFINITION).build();

    /**
     * ResearchElementDefinition
     * 
     * <p>The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion, 
     * recommendation) is about.
     */
    public static final ResourceTypeCode RESEARCH_ELEMENT_DEFINITION = ResourceTypeCode.builder().value(ResourceType.RESEARCH_ELEMENT_DEFINITION).build();

    /**
     * ResearchStudy
     * 
     * <p>A process where a researcher or organization plans and then executes a series of steps intended to increase the 
     * field of healthcare-related knowledge. This includes studies of safety, efficacy, comparative effectiveness and other 
     * information about medications, devices, therapies and other interventional and investigative techniques. A 
     * ResearchStudy involves the gathering of information about human or animal subjects.
     */
    public static final ResourceTypeCode RESEARCH_STUDY = ResourceTypeCode.builder().value(ResourceType.RESEARCH_STUDY).build();

    /**
     * ResearchSubject
     * 
     * <p>A physical entity which is the primary unit of operational and/or administrative interest in a study.
     */
    public static final ResourceTypeCode RESEARCH_SUBJECT = ResourceTypeCode.builder().value(ResourceType.RESEARCH_SUBJECT).build();

    /**
     * RiskAssessment
     * 
     * <p>An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
     */
    public static final ResourceTypeCode RISK_ASSESSMENT = ResourceTypeCode.builder().value(ResourceType.RISK_ASSESSMENT).build();

    /**
     * Schedule
     * 
     * <p>A container for slots of time that may be available for booking appointments.
     */
    public static final ResourceTypeCode SCHEDULE = ResourceTypeCode.builder().value(ResourceType.SCHEDULE).build();

    /**
     * SearchParameter
     * 
     * <p>A search parameter that defines a named search item that can be used to search/filter on a resource.
     */
    public static final ResourceTypeCode SEARCH_PARAMETER = ResourceTypeCode.builder().value(ResourceType.SEARCH_PARAMETER).build();

    /**
     * ServiceRequest
     * 
     * <p>A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
     */
    public static final ResourceTypeCode SERVICE_REQUEST = ResourceTypeCode.builder().value(ResourceType.SERVICE_REQUEST).build();

    /**
     * Slot
     * 
     * <p>A slot of time on a schedule that may be available for booking appointments.
     */
    public static final ResourceTypeCode SLOT = ResourceTypeCode.builder().value(ResourceType.SLOT).build();

    /**
     * Specimen
     * 
     * <p>A sample to be used for analysis.
     */
    public static final ResourceTypeCode SPECIMEN = ResourceTypeCode.builder().value(ResourceType.SPECIMEN).build();

    /**
     * SpecimenDefinition
     * 
     * <p>A kind of specimen with associated set of requirements.
     */
    public static final ResourceTypeCode SPECIMEN_DEFINITION = ResourceTypeCode.builder().value(ResourceType.SPECIMEN_DEFINITION).build();

    /**
     * StructureDefinition
     * 
     * <p>A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in 
     * FHIR, and also for describing extensions and constraints on resources and data types.
     */
    public static final ResourceTypeCode STRUCTURE_DEFINITION = ResourceTypeCode.builder().value(ResourceType.STRUCTURE_DEFINITION).build();

    /**
     * StructureMap
     * 
     * <p>A Map of relationships between 2 structures that can be used to transform data.
     */
    public static final ResourceTypeCode STRUCTURE_MAP = ResourceTypeCode.builder().value(ResourceType.STRUCTURE_MAP).build();

    /**
     * Subscription
     * 
     * <p>The subscription resource is used to define a push-based subscription from a server to another system. Once a 
     * subscription is registered with the server, the server checks every resource that is created or updated, and if the 
     * resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an 
     * appropriate action.
     */
    public static final ResourceTypeCode SUBSCRIPTION = ResourceTypeCode.builder().value(ResourceType.SUBSCRIPTION).build();

    /**
     * SubscriptionStatus
     * 
     * <p>The SubscriptionStatus resource describes the state of a Subscription during notifications.
     */
    public static final ResourceTypeCode SUBSCRIPTION_STATUS = ResourceTypeCode.builder().value(ResourceType.SUBSCRIPTION_STATUS).build();

    /**
     * SubscriptionTopic
     * 
     * <p>Describes a stream of resource state changes identified by trigger criteria and annotated with labels useful to 
     * filter projections from this topic.
     */
    public static final ResourceTypeCode SUBSCRIPTION_TOPIC = ResourceTypeCode.builder().value(ResourceType.SUBSCRIPTION_TOPIC).build();

    /**
     * Substance
     * 
     * <p>A homogeneous material with a definite composition.
     */
    public static final ResourceTypeCode SUBSTANCE = ResourceTypeCode.builder().value(ResourceType.SUBSTANCE).build();

    /**
     * SubstanceDefinition
     * 
     * <p>The detailed description of a substance, typically at a level beyond what is used for prescribing.
     */
    public static final ResourceTypeCode SUBSTANCE_DEFINITION = ResourceTypeCode.builder().value(ResourceType.SUBSTANCE_DEFINITION).build();

    /**
     * SupplyDelivery
     * 
     * <p>Record of delivery of what is supplied.
     */
    public static final ResourceTypeCode SUPPLY_DELIVERY = ResourceTypeCode.builder().value(ResourceType.SUPPLY_DELIVERY).build();

    /**
     * SupplyRequest
     * 
     * <p>A record of a request for a medication, substance or device used in the healthcare setting.
     */
    public static final ResourceTypeCode SUPPLY_REQUEST = ResourceTypeCode.builder().value(ResourceType.SUPPLY_REQUEST).build();

    /**
     * Task
     * 
     * <p>A task to be performed.
     */
    public static final ResourceTypeCode TASK = ResourceTypeCode.builder().value(ResourceType.TASK).build();

    /**
     * TerminologyCapabilities
     * 
     * <p>A TerminologyCapabilities resource documents a set of capabilities (behaviors) of a FHIR Terminology Server that 
     * may be used as a statement of actual server functionality or a statement of required or desired server implementation.
     */
    public static final ResourceTypeCode TERMINOLOGY_CAPABILITIES = ResourceTypeCode.builder().value(ResourceType.TERMINOLOGY_CAPABILITIES).build();

    /**
     * TestReport
     * 
     * <p>A summary of information based on the results of executing a TestScript.
     */
    public static final ResourceTypeCode TEST_REPORT = ResourceTypeCode.builder().value(ResourceType.TEST_REPORT).build();

    /**
     * TestScript
     * 
     * <p>A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR 
     * specification.
     */
    public static final ResourceTypeCode TEST_SCRIPT = ResourceTypeCode.builder().value(ResourceType.TEST_SCRIPT).build();

    /**
     * ValueSet
     * 
     * <p>A ValueSet resource instance specifies a set of codes drawn from one or more code systems, intended for use in a 
     * particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements]
     * (terminologies.html).
     */
    public static final ResourceTypeCode VALUE_SET = ResourceTypeCode.builder().value(ResourceType.VALUE_SET).build();

    /**
     * VerificationResult
     * 
     * <p>Describes validation requirements, source(s), status and dates for one or more elements.
     */
    public static final ResourceTypeCode VERIFICATION_RESULT = ResourceTypeCode.builder().value(ResourceType.VERIFICATION_RESULT).build();

    /**
     * VisionPrescription
     * 
     * <p>An authorization for the provision of glasses and/or contact lenses to a patient.
     */
    public static final ResourceTypeCode VISION_PRESCRIPTION = ResourceTypeCode.builder().value(ResourceType.VISION_PRESCRIPTION).build();

    /**
     * Parameters
     * 
     * <p>This resource is a non-persisted resource used to pass information into and back from an [operation](operations.
     * html). It has no other use, and there is no RESTful endpoint associated with it.
     */
    public static final ResourceTypeCode PARAMETERS = ResourceTypeCode.builder().value(ResourceType.PARAMETERS).build();

    private volatile int hashCode;

    private ResourceTypeCode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ResourceTypeCode as an enum constant.
     */
    public ResourceType getValueAsEnum() {
        return (value != null) ? ResourceType.from(value) : null;
    }

    /**
     * Factory method for creating ResourceTypeCode objects from a passed enum value.
     */
    public static ResourceTypeCode of(ResourceType value) {
        switch (value) {
        case RESOURCE:
            return RESOURCE;
        case BINARY:
            return BINARY;
        case BUNDLE:
            return BUNDLE;
        case DOMAIN_RESOURCE:
            return DOMAIN_RESOURCE;
        case ACCOUNT:
            return ACCOUNT;
        case ACTIVITY_DEFINITION:
            return ACTIVITY_DEFINITION;
        case ADMINISTRABLE_PRODUCT_DEFINITION:
            return ADMINISTRABLE_PRODUCT_DEFINITION;
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
        case BIOLOGICALLY_DERIVED_PRODUCT:
            return BIOLOGICALLY_DERIVED_PRODUCT;
        case BODY_STRUCTURE:
            return BODY_STRUCTURE;
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
        case CITATION:
            return CITATION;
        case CLAIM:
            return CLAIM;
        case CLAIM_RESPONSE:
            return CLAIM_RESPONSE;
        case CLINICAL_IMPRESSION:
            return CLINICAL_IMPRESSION;
        case CLINICAL_USE_DEFINITION:
            return CLINICAL_USE_DEFINITION;
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
        case EVIDENCE_REPORT:
            return EVIDENCE_REPORT;
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
        case INGREDIENT:
            return INGREDIENT;
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
        case MANUFACTURED_ITEM_DEFINITION:
            return MANUFACTURED_ITEM_DEFINITION;
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
        case MEDICINAL_PRODUCT_DEFINITION:
            return MEDICINAL_PRODUCT_DEFINITION;
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
        case NUTRITION_PRODUCT:
            return NUTRITION_PRODUCT;
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
        case PACKAGED_PRODUCT_DEFINITION:
            return PACKAGED_PRODUCT_DEFINITION;
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
        case REGULATED_AUTHORIZATION:
            return REGULATED_AUTHORIZATION;
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
        case RISK_ASSESSMENT:
            return RISK_ASSESSMENT;
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
        case SUBSCRIPTION_STATUS:
            return SUBSCRIPTION_STATUS;
        case SUBSCRIPTION_TOPIC:
            return SUBSCRIPTION_TOPIC;
        case SUBSTANCE:
            return SUBSTANCE;
        case SUBSTANCE_DEFINITION:
            return SUBSTANCE_DEFINITION;
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
        case PARAMETERS:
            return PARAMETERS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ResourceTypeCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ResourceTypeCode of(java.lang.String value) {
        return of(ResourceType.from(value));
    }

    /**
     * Inherited factory method for creating ResourceTypeCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ResourceType.from(value));
    }

    /**
     * Inherited factory method for creating ResourceTypeCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(ResourceType.from(value));
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
        ResourceTypeCode other = (ResourceTypeCode) obj;
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(ResourceType.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for ResourceTypeCode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(ResourceType value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ResourceTypeCode build() {
            ResourceTypeCode resourceTypeCode = new ResourceTypeCode(this);
            if (validating) {
                validate(resourceTypeCode);
            }
            return resourceTypeCode;
        }

        protected void validate(ResourceTypeCode resourceTypeCode) {
            super.validate(resourceTypeCode);
        }

        protected Builder from(ResourceTypeCode resourceTypeCode) {
            super.from(resourceTypeCode);
            return this;
        }
    }

}
