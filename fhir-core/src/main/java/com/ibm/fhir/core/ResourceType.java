/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.core;

import static com.ibm.fhir.core.FHIRVersionParam.VERSION_40;
import static com.ibm.fhir.core.FHIRVersionParam.VERSION_43;

/**
 * Enum constants for all resource types across all versions of HL7 FHIR
 */
public enum ResourceType {
    /**
     * Resource
     *
     * <p>--- Abstract Type! ---This is the base resource type for everything.
     */
    RESOURCE("Resource", VERSION_40),

    /**
     * Binary
     *
     * <p>A resource that represents the data of a single raw artifact as digital content accessible in its native format. A
     * Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
     */
    BINARY("Binary", VERSION_40),

    /**
     * Bundle
     *
     * <p>A container for a collection of resources.
     */
    BUNDLE("Bundle", VERSION_40),

    /**
     * DomainResource
     *
     * <p>--- Abstract Type! ---A resource that includes narrative, extensions, and contained resources.
     */
    DOMAIN_RESOURCE("DomainResource", VERSION_40),

    /**
     * Account
     *
     * <p>A financial tool for tracking value accrued for a particular purpose. In the healthcare field, used to track
     * charges for a patient, cost centers, etc.
     */
    ACCOUNT("Account", VERSION_40),

    /**
     * ActivityDefinition
     *
     * <p>This resource allows for the definition of some activity to be performed, independent of a particular patient,
     * practitioner, or other performance context.
     */
    ACTIVITY_DEFINITION("ActivityDefinition", VERSION_40),

    /**
     * AdministrableProductDefinition
     *
     * <p>A medicinal product in the final form which is suitable for administering to a patient (after any mixing of
     * multiple components, dissolution etc. has been performed).
     */
    ADMINISTRABLE_PRODUCT_DEFINITION("AdministrableProductDefinition", VERSION_43),

    /**
     * AdverseEvent
     *
     * <p>Actual or potential/avoided event causing unintended physical injury resulting from or contributed to by medical
     * care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or
     * hospitalization, or that results in death.
     */
    ADVERSE_EVENT("AdverseEvent", VERSION_40),

    /**
     * AllergyIntolerance
     *
     * <p>Risk of harmful or undesirable, physiological response which is unique to an individual and associated with
     * exposure to a substance.
     */
    ALLERGY_INTOLERANCE("AllergyIntolerance", VERSION_40),

    /**
     * Appointment
     *
     * <p>A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a
     * specific date/time. This may result in one or more Encounter(s).
     */
    APPOINTMENT("Appointment", VERSION_40),

    /**
     * AppointmentResponse
     *
     * <p>A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
     */
    APPOINTMENT_RESPONSE("AppointmentResponse", VERSION_40),

    /**
     * AuditEvent
     *
     * <p>A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion
     * attempts and monitoring for inappropriate usage.
     */
    AUDIT_EVENT("AuditEvent", VERSION_40),

    /**
     * Basic
     *
     * <p>Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing
     * resource, and custom resources not appropriate for inclusion in the FHIR specification.
     */
    BASIC("Basic", VERSION_40),

    /**
     * BiologicallyDerivedProduct
     *
     * <p>A material substance originating from a biological entity intended to be transplanted or infused
     * <p>into another (possibly the same) biological entity.
     */
    BIOLOGICALLY_DERIVED_PRODUCT("BiologicallyDerivedProduct", VERSION_40),

    /**
     * BodyStructure
     *
     * <p>Record details about an anatomical structure. This resource may be used when a coded concept does not provide the
     * necessary detail needed for the use case.
     */
    BODY_STRUCTURE("BodyStructure", VERSION_40),

    /**
     * CapabilityStatement
     *
     * <p>A Capability Statement documents a set of capabilities (behaviors) of a FHIR Server for a particular version of
     * FHIR that may be used as a statement of actual server functionality or a statement of required or desired server
     * implementation.
     */
    CAPABILITY_STATEMENT("CapabilityStatement", VERSION_40),

    /**
     * CarePlan
     *
     * <p>Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or
     * community for a period of time, possibly limited to care for a specific condition or set of conditions.
     */
    CARE_PLAN("CarePlan", VERSION_40),

    /**
     * CareTeam
     *
     * <p>The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of
     * care for a patient.
     */
    CARE_TEAM("CareTeam", VERSION_40),

    /**
     * CatalogEntry
     *
     * <p>Catalog entries are wrappers that contextualize items included in a catalog.
     */
    CATALOG_ENTRY("CatalogEntry", VERSION_40),

    /**
     * ChargeItem
     *
     * <p>The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore
     * referring not only to the product, but containing in addition details of the provision, like date, time, amounts and
     * participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal
     * cost allocation.
     */
    CHARGE_ITEM("ChargeItem", VERSION_40),

    /**
     * ChargeItemDefinition
     *
     * <p>The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate
     * costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a
     * rough structure and requires profiling for each type of billing code system.
     */
    CHARGE_ITEM_DEFINITION("ChargeItemDefinition", VERSION_40),

    /**
     * Citation
     *
     * <p>The Citation Resource enables reference to any knowledge artifact for purposes of identification and attribution.
     * The Citation Resource supports existing reference structures and developing publication practices such as versioning,
     * expressing complex contributorship roles, and referencing computable resources.
     */
    CITATION("Citation", VERSION_43),

    /**
     * Claim
     *
     * <p>A provider issued list of professional services and products which have been provided, or are to be provided, to a
     * patient which is sent to an insurer for reimbursement.
     */
    CLAIM("Claim", VERSION_40),

    /**
     * ClaimResponse
     *
     * <p>This resource provides the adjudication details from the processing of a Claim resource.
     */
    CLAIM_RESPONSE("ClaimResponse", VERSION_40),

    /**
     * ClinicalImpression
     *
     * <p>A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning
     * the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with
     * a clinical consultation / encounter, but this varies greatly depending on the clinical workflow. This resource is
     * called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools
     * such as Apgar score.
     */
    CLINICAL_IMPRESSION("ClinicalImpression", VERSION_40),

    /**
     * ClinicalUseDefinition
     *
     * <p>A single issue - either an indication, contraindication, interaction or an undesirable effect for a medicinal
     * product, medication, device or procedure.
     */
    CLINICAL_USE_DEFINITION("ClinicalUseDefinition", VERSION_43),

    /**
     * CodeSystem
     *
     * <p>The CodeSystem resource is used to declare the existence of and describe a code system or code system supplement
     * and its key properties, and optionally define a part or all of its content.
     */
    CODE_SYSTEM("CodeSystem", VERSION_40),

    /**
     * Communication
     *
     * <p>An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public
     * health agency that was notified about a reportable condition.
     */
    COMMUNICATION("Communication", VERSION_40),

    /**
     * CommunicationRequest
     *
     * <p>A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the
     * CDS system proposes that the public health agency be notified about a reportable condition.
     */
    COMMUNICATION_REQUEST("CommunicationRequest", VERSION_40),

    /**
     * CompartmentDefinition
     *
     * <p>A compartment definition that defines how resources are accessed on a server.
     */
    COMPARTMENT_DEFINITION("CompartmentDefinition", VERSION_40),

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
    COMPOSITION("Composition", VERSION_40),

    /**
     * ConceptMap
     *
     * <p>A statement of relationships from one set of concepts to one or more other concepts - either concepts in code
     * systems, or data element/data element concepts, or classes in class models.
     */
    CONCEPT_MAP("ConceptMap", VERSION_40),

    /**
     * Condition
     *
     * <p>A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a
     * level of concern.
     */
    CONDITION("Condition", VERSION_40),

    /**
     * Consent
     *
     * <p>A record of a healthcare consumer’s choices, which permits or denies identified recipient(s) or recipient role(s)
     * to perform one or more actions within a given policy context, for specific purposes and periods of time.
     */
    CONSENT("Consent", VERSION_40),

    /**
     * Contract
     *
     * <p>Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
     */
    CONTRACT("Contract", VERSION_40),

    /**
     * Coverage
     *
     * <p>Financial instrument which may be used to reimburse or pay for health care products and services. Includes both
     * insurance and self-payment.
     */
    COVERAGE("Coverage", VERSION_40),

    /**
     * CoverageEligibilityRequest
     *
     * <p>The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to
     * respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is
     * valid and in-force and optionally to provide the insurance details of the policy.
     */
    COVERAGE_ELIGIBILITY_REQUEST("CoverageEligibilityRequest", VERSION_40),

    /**
     * CoverageEligibilityResponse
     *
     * <p>This resource provides eligibility and plan details from the processing of an CoverageEligibilityRequest resource.
     */
    COVERAGE_ELIGIBILITY_RESPONSE("CoverageEligibilityResponse", VERSION_40),

    /**
     * DetectedIssue
     *
     * <p>Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for
     * a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
     */
    DETECTED_ISSUE("DetectedIssue", VERSION_40),

    /**
     * Device
     *
     * <p>A type of a manufactured item that is used in the provision of healthcare without being substantially changed
     * through that activity. The device may be a medical or non-medical device.
     */
    DEVICE("Device", VERSION_40),

    /**
     * DeviceDefinition
     *
     * <p>The characteristics, operational status and capabilities of a medical-related component of a medical device.
     */
    DEVICE_DEFINITION("DeviceDefinition", VERSION_40),

    /**
     * DeviceMetric
     *
     * <p>Describes a measurement, calculation or setting capability of a medical device.
     */
    DEVICE_METRIC("DeviceMetric", VERSION_40),

    /**
     * DeviceRequest
     *
     * <p>Represents a request for a patient to employ a medical device. The device may be an implantable device, or an
     * external assistive device, such as a walker.
     */
    DEVICE_REQUEST("DeviceRequest", VERSION_40),

    /**
     * DeviceUseStatement
     *
     * <p>A record of a device being used by a patient where the record is the result of a report from the patient or another
     * clinician.
     */
    DEVICE_USE_STATEMENT("DeviceUseStatement", VERSION_40),

    /**
     * DiagnosticReport
     *
     * <p>The findings and interpretation of diagnostic tests performed on patients, groups of patients, devices, and
     * locations, and/or specimens derived from these. The report includes clinical context such as requesting and provider
     * information, and some mix of atomic results, images, textual and coded interpretations, and formatted representation
     * of diagnostic reports.
     */
    DIAGNOSTIC_REPORT("DiagnosticReport", VERSION_40),

    /**
     * DocumentManifest
     *
     * <p>A collection of documents compiled for a purpose together with metadata that applies to the collection.
     */
    DOCUMENT_MANIFEST("DocumentManifest", VERSION_40),

    /**
     * DocumentReference
     *
     * <p>A reference to a document of any kind for any purpose. Provides metadata about the document so that the document
     * can be discovered and managed. The scope of a document is any seralized object with a mime-type, so includes formal
     * patient centric documents (CDA), cliical notes, scanned paper, and non-patient specific documents like policy text.
     */
    DOCUMENT_REFERENCE("DocumentReference", VERSION_40),

    /**
     * EffectEvidenceSynthesis
     *
     * <p>The EffectEvidenceSynthesis resource describes the difference in an outcome between exposures states in a
     * population where the effect estimate is derived from a combination of research studies.
     */
    EFFECT_EVIDENCE_SYNTHESIS("EffectEvidenceSynthesis", VERSION_40, VERSION_43),

    /**
     * Encounter
     *
     * <p>An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or
     * assessing the health status of a patient.
     */
    ENCOUNTER("Encounter", VERSION_40),

    /**
     * Endpoint
     *
     * <p>The technical details of an endpoint that can be used for electronic services, such as for web services providing
     * XDS.b or a REST endpoint for another FHIR server. This may include any security context information.
     */
    ENDPOINT("Endpoint", VERSION_40),

    /**
     * EnrollmentRequest
     *
     * <p>This resource provides the insurance enrollment details to the insurer regarding a specified coverage.
     */
    ENROLLMENT_REQUEST("EnrollmentRequest", VERSION_40),

    /**
     * EnrollmentResponse
     *
     * <p>This resource provides enrollment and plan details from the processing of an EnrollmentRequest resource.
     */
    ENROLLMENT_RESPONSE("EnrollmentResponse", VERSION_40),

    /**
     * EpisodeOfCare
     *
     * <p>An association between a patient and an organization / healthcare provider(s) during which time encounters may
     * occur. The managing organization assumes a level of responsibility for the patient during this time.
     */
    EPISODE_OF_CARE("EpisodeOfCare", VERSION_40),

    /**
     * EventDefinition
     *
     * <p>The EventDefinition resource provides a reusable description of when a particular event can occur.
     */
    EVENT_DEFINITION("EventDefinition", VERSION_40),

    /**
     * Evidence
     *
     * <p>The Evidence Resource provides a machine-interpretable expression of an evidence concept including the evidence
     * variables (eg population, exposures/interventions, comparators, outcomes, measured variables, confounding variables),
     * the statistics, and the certainty of this evidence.
     */
    EVIDENCE("Evidence", VERSION_40),

    /**
     * EvidenceReport
     *
     * <p>The EvidenceReport Resource is a specialized container for a collection of resources and codable concepts, adapted
     * to support compositions of Evidence, EvidenceVariable, and Citation resources and related concepts.
     */
    EVIDENCE_REPORT("EvidenceReport", VERSION_43),

    /**
     * EvidenceVariable
     *
     * <p>The EvidenceVariable resource describes an element that knowledge (Evidence) is about.
     */
    EVIDENCE_VARIABLE("EvidenceVariable", VERSION_40),

    /**
     * ExampleScenario
     *
     * <p>Example of workflow instance.
     */
    EXAMPLE_SCENARIO("ExampleScenario", VERSION_40),

    /**
     * ExplanationOfBenefit
     *
     * <p>This resource provides: the claim details; adjudication details from the processing of a Claim; and optionally
     * account balance information, for informing the subscriber of the benefits provided.
     */
    EXPLANATION_OF_BENEFIT("ExplanationOfBenefit", VERSION_40),

    /**
     * FamilyMemberHistory
     *
     * <p>Significant health conditions for a person related to the patient relevant in the context of care for the patient.
     */
    FAMILY_MEMBER_HISTORY("FamilyMemberHistory", VERSION_40),

    /**
     * Flag
     *
     * <p>Prospective warnings of potential issues when providing care to the patient.
     */
    FLAG("Flag", VERSION_40),

    /**
     * Goal
     *
     * <p>Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring
     * an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
     */
    GOAL("Goal", VERSION_40),

    /**
     * GraphDefinition
     *
     * <p>A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by
     * following references. The Graph Definition resource defines a set and makes rules about the set.
     */
    GRAPH_DEFINITION("GraphDefinition", VERSION_40),

    /**
     * Group
     *
     * <p>Represents a defined collection of entities that may be discussed or acted upon collectively but which are not
     * expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an
     * Organization.
     */
    GROUP("Group", VERSION_40),

    /**
     * GuidanceResponse
     *
     * <p>A guidance response is the formal response to a guidance request, including any output parameters returned by the
     * evaluation, as well as the description of any proposed actions to be taken.
     */
    GUIDANCE_RESPONSE("GuidanceResponse", VERSION_40),

    /**
     * HealthcareService
     *
     * <p>The details of a healthcare service available at a location.
     */
    HEALTHCARE_SERVICE("HealthcareService", VERSION_40),

    /**
     * ImagingStudy
     *
     * <p>Representation of the content produced in a DICOM imaging study. A study comprises a set of series, each of which
     * includes a set of Service-Object Pair Instances (SOP Instances - images or other data) acquired or produced in a
     * common context. A series is of only one modality (e.g. X-ray, CT, MR, ultrasound), but a study may have multiple
     * series of different modalities.
     */
    IMAGING_STUDY("ImagingStudy", VERSION_40),

    /**
     * Immunization
     *
     * <p>Describes the event of a patient being administered a vaccine or a record of an immunization as reported by a
     * patient, a clinician or another party.
     */
    IMMUNIZATION("Immunization", VERSION_40),

    /**
     * ImmunizationEvaluation
     *
     * <p>Describes a comparison of an immunization event against published recommendations to determine if the
     * administration is "valid" in relation to those recommendations.
     */
    IMMUNIZATION_EVALUATION("ImmunizationEvaluation", VERSION_40),

    /**
     * ImmunizationRecommendation
     *
     * <p>A patient's point-in-time set of recommendations (i.e. forecasting) according to a published schedule with optional
     * supporting justification.
     */
    IMMUNIZATION_RECOMMENDATION("ImmunizationRecommendation", VERSION_40),

    /**
     * ImplementationGuide
     *
     * <p>A set of rules of how a particular interoperability or standards problem is solved - typically through the use of
     * FHIR resources. This resource is used to gather all the parts of an implementation guide into a logical whole and to
     * publish a computable definition of all the parts.
     */
    IMPLEMENTATION_GUIDE("ImplementationGuide", VERSION_40),

    /**
     * Ingredient
     *
     * <p>An ingredient of a manufactured item or pharmaceutical product.
     */
    INGREDIENT("Ingredient", VERSION_43),

    /**
     * InsurancePlan
     *
     * <p>Details of a Health Insurance product/plan provided by an organization.
     */
    INSURANCE_PLAN("InsurancePlan", VERSION_40),

    /**
     * Invoice
     *
     * <p>Invoice containing collected ChargeItems from an Account with calculated individual and total price for Billing
     * purpose.
     */
    INVOICE("Invoice", VERSION_40),

    /**
     * Library
     *
     * <p>The Library resource is a general-purpose container for knowledge asset definitions. It can be used to describe and
     * expose existing knowledge assets such as logic libraries and information model descriptions, as well as to describe a
     * collection of knowledge assets.
     */
    LIBRARY("Library", VERSION_40),

    /**
     * Linkage
     *
     * <p>Identifies two or more records (resource instances) that refer to the same real-world "occurrence".
     */
    LINKAGE("Linkage", VERSION_40),

    /**
     * List
     *
     * <p>A list is a curated collection of resources.
     */
    LIST("List", VERSION_40),

    /**
     * Location
     *
     * <p>Details and position information for a physical place where services are provided and resources and participants
     * may be stored, found, contained, or accommodated.
     */
    LOCATION("Location", VERSION_40),

    /**
     * ManufacturedItemDefinition
     *
     * <p>The definition and characteristics of a medicinal manufactured item, such as a tablet or capsule, as contained in a
     * packaged medicinal product.
     */
    MANUFACTURED_ITEM_DEFINITION("ManufacturedItemDefinition", VERSION_43),

    /**
     * Measure
     *
     * <p>The Measure resource provides the definition of a quality measure.
     */
    MEASURE("Measure", VERSION_40),

    /**
     * MeasureReport
     *
     * <p>The MeasureReport resource contains the results of the calculation of a measure; and optionally a reference to the
     * resources involved in that calculation.
     */
    MEASURE_REPORT("MeasureReport", VERSION_40),

    /**
     * Media
     *
     * <p>A photo, video, or audio recording acquired or used in healthcare. The actual content may be inline or provided by
     * direct reference.
     */
    MEDIA("Media", VERSION_40),

    /**
     * Medication
     *
     * <p>This resource is primarily used for the identification and definition of a medication for the purposes of
     * prescribing, dispensing, and administering a medication as well as for making statements about medication use.
     */
    MEDICATION("Medication", VERSION_40),

    /**
     * MedicationAdministration
     *
     * <p>Describes the event of a patient consuming or otherwise being administered a medication. This may be as simple as
     * swallowing a tablet or it may be a long running infusion. Related resources tie this event to the authorizing
     * prescription, and the specific encounter between patient and health care practitioner.
     */
    MEDICATION_ADMINISTRATION("MedicationAdministration", VERSION_40),

    /**
     * MedicationDispense
     *
     * <p>Indicates that a medication product is to be or has been dispensed for a named person/patient. This includes a
     * description of the medication product (supply) provided and the instructions for administering the medication. The
     * medication dispense is the result of a pharmacy system responding to a medication order.
     */
    MEDICATION_DISPENSE("MedicationDispense", VERSION_40),

    /**
     * MedicationKnowledge
     *
     * <p>Information about a medication that is used to support knowledge.
     */
    MEDICATION_KNOWLEDGE("MedicationKnowledge", VERSION_40),

    /**
     * MedicationRequest
     *
     * <p>An order or request for both supply of the medication and the instructions for administration of the medication to
     * a patient. The resource is called "MedicationRequest" rather than "MedicationPrescription" or "MedicationOrder" to
     * generalize the use across inpatient and outpatient settings, including care plans, etc., and to harmonize with
     * workflow patterns.
     */
    MEDICATION_REQUEST("MedicationRequest", VERSION_40),

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
    MEDICATION_STATEMENT("MedicationStatement", VERSION_40),

    /**
     * MedicinalProduct
     *
     * <p>Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use).
     */
    MEDICINAL_PRODUCT("MedicinalProduct", VERSION_40, VERSION_43),

    /**
     * MedicinalProductAuthorization
     *
     * <p>The regulatory authorization of a medicinal product.
     */
    MEDICINAL_PRODUCT_AUTHORIZATION("MedicinalProductAuthorization", VERSION_40, VERSION_43),

    /**
     * MedicinalProductContraindication
     *
     * <p>The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory
     * purposes.
     */
    MEDICINAL_PRODUCT_CONTRAINDICATION("MedicinalProductContraindication", VERSION_40, VERSION_43),

    /**
     * MedicinalProductDefinition
     *
     * <p>Detailed definition of a medicinal product, typically for uses other than direct patient care (e.g. regulatory use,
     * drug catalogs).
     */
    MEDICINAL_PRODUCT_DEFINITION("MedicinalProductDefinition", VERSION_43),

    /**
     * MedicinalProductIndication
     *
     * <p>Indication for the Medicinal Product.
     */
    MEDICINAL_PRODUCT_INDICATION("MedicinalProductIndication", VERSION_40, VERSION_43),

    /**
     * MedicinalProductIngredient
     *
     * <p>An ingredient of a manufactured item or pharmaceutical product.
     */
    MEDICINAL_PRODUCT_INGREDIENT("MedicinalProductIngredient", VERSION_40, VERSION_43),

    /**
     * MedicinalProductInteraction
     *
     * <p>The interactions of the medicinal product with other medicinal products, or other forms of interactions.
     */
    MEDICINAL_PRODUCT_INTERACTION("MedicinalProductInteraction", VERSION_40, VERSION_43),

    /**
     * MedicinalProductManufactured
     *
     * <p>The manufactured item as contained in the packaged medicinal product.
     */
    MEDICINAL_PRODUCT_MANUFACTURED("MedicinalProductManufactured", VERSION_40, VERSION_43),

    /**
     * MedicinalProductPackaged
     *
     * <p>A medicinal product in a container or package.
     */
    MEDICINAL_PRODUCT_PACKAGED("MedicinalProductPackaged", VERSION_40, VERSION_43),

    /**
     * MedicinalProductPharmaceutical
     *
     * <p>A pharmaceutical product described in terms of its composition and dose form.
     */
    MEDICINAL_PRODUCT_PHARMACEUTICAL("MedicinalProductPharmaceutical", VERSION_40, VERSION_43),

    /**
     * MedicinalProductUndesirableEffect
     *
     * <p>Describe the undesirable effects of the medicinal product.
     */
    MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT("MedicinalProductUndesirableEffect", VERSION_40, VERSION_43),

    /**
     * MessageDefinition
     *
     * <p>Defines the characteristics of a message that can be shared between systems, including the type of event that
     * initiates the message, the content to be transmitted and what response(s), if any, are permitted.
     */
    MESSAGE_DEFINITION("MessageDefinition", VERSION_40),

    /**
     * MessageHeader
     *
     * <p>The header for a message exchange that is either requesting or responding to an action. The reference(s) that are
     * the subject of the action as well as other information related to the action are typically transmitted in a bundle in
     * which the MessageHeader resource instance is the first resource in the bundle.
     */
    MESSAGE_HEADER("MessageHeader", VERSION_40),

    /**
     * MolecularSequence
     *
     * <p>Raw data describing a biological sequence.
     */
    MOLECULAR_SEQUENCE("MolecularSequence", VERSION_40),

    /**
     * NamingSystem
     *
     * <p>A curated namespace that issues unique symbols within that namespace for the identification of concepts, people,
     * devices, etc. Represents a "System" used within the Identifier and Coding data types.
     */
    NAMING_SYSTEM("NamingSystem", VERSION_40),

    /**
     * NutritionOrder
     *
     * <p>A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
     */
    NUTRITION_ORDER("NutritionOrder", VERSION_40),

    /**
     * NutritionProduct
     *
     * <p>A food or fluid product that is consumed by patients.
     */
    NUTRITION_PRODUCT("NutritionProduct", VERSION_43),

    /**
     * Observation
     *
     * <p>Measurements and simple assertions made about a patient, device or other subject.
     */
    OBSERVATION("Observation", VERSION_40),

    /**
     * ObservationDefinition
     *
     * <p>Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable
     * health care service.
     */
    OBSERVATION_DEFINITION("ObservationDefinition", VERSION_40),

    /**
     * OperationDefinition
     *
     * <p>A formal computable definition of an operation (on the RESTful interface) or a named query (using the search
     * interaction).
     */
    OPERATION_DEFINITION("OperationDefinition", VERSION_40),

    /**
     * OperationOutcome
     *
     * <p>A collection of error, warning, or information messages that result from a system action.
     */
    OPERATION_OUTCOME("OperationOutcome", VERSION_40),

    /**
     * Organization
     *
     * <p>A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some
     * form of collective action. Includes companies, institutions, corporations, departments, community groups, healthcare
     * practice groups, payer/insurer, etc.
     */
    ORGANIZATION("Organization", VERSION_40),

    /**
     * OrganizationAffiliation
     *
     * <p>Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of
     * relationship/sub-division relationship.
     */
    ORGANIZATION_AFFILIATION("OrganizationAffiliation", VERSION_40),

    /**
     * PackagedProductDefinition
     *
     * <p>A medically related item or items, in a container or package.
     */
    PACKAGED_PRODUCT_DEFINITION("PackagedProductDefinition", VERSION_43),

    /**
     * Parameters
     *
     * <p>This resource is a non-persisted resource used to pass information into and back from an [operation](operations.
     * html). It has no other use, and there is no RESTful endpoint associated with it.
     */
    PARAMETERS("Parameters", VERSION_40),

    /**
     * Patient
     *
     * <p>Demographics and other administrative information about an individual or animal receiving care or other health-
     * related services.
     */
    PATIENT("Patient", VERSION_40),

    /**
     * PaymentNotice
     *
     * <p>This resource provides the status of the payment for goods and services rendered, and the request and response
     * resource references.
     */
    PAYMENT_NOTICE("PaymentNotice", VERSION_40),

    /**
     * PaymentReconciliation
     *
     * <p>This resource provides the details including amount of a payment and allocates the payment items being paid.
     */
    PAYMENT_RECONCILIATION("PaymentReconciliation", VERSION_40),

    /**
     * Person
     *
     * <p>Demographics and administrative information about a person independent of a specific health-related context.
     */
    PERSON("Person", VERSION_40),

    /**
     * PlanDefinition
     *
     * <p>This resource allows for the definition of various types of plans as a sharable, consumable, and executable
     * artifact. The resource is general enough to support the description of a broad range of clinical and non-clinical
     * artifacts such as clinical decision support rules, order sets, protocols, and drug quality specifications.
     */
    PLAN_DEFINITION("PlanDefinition", VERSION_40),

    /**
     * Practitioner
     *
     * <p>A person who is directly or indirectly involved in the provisioning of healthcare.
     */
    PRACTITIONER("Practitioner", VERSION_40),

    /**
     * PractitionerRole
     *
     * <p>A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a
     * period of time.
     */
    PRACTITIONER_ROLE("PractitionerRole", VERSION_40),

    /**
     * Procedure
     *
     * <p>An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or
     * less invasive like long term services, counseling, or hypnotherapy.
     */
    PROCEDURE("Procedure", VERSION_40),

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
    PROVENANCE("Provenance", VERSION_40),

    /**
     * Questionnaire
     *
     * <p>A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide
     * detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
     */
    QUESTIONNAIRE("Questionnaire", VERSION_40),

    /**
     * QuestionnaireResponse
     *
     * <p>A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets,
     * corresponding to the structure of the grouping of the questionnaire being responded to.
     */
    QUESTIONNAIRE_RESPONSE("QuestionnaireResponse", VERSION_40),

    /**
     * RegulatedAuthorization
     *
     * <p>Regulatory approval, clearance or licencing related to a regulated product, treatment, facility or activity that is
     * cited in a guidance, regulation, rule or legislative act. An example is Market Authorization relating to a Medicinal
     * Product.
     */
    REGULATED_AUTHORIZATION("RegulatedAuthorization", VERSION_43),

    /**
     * RelatedPerson
     *
     * <p>Information about a person that is involved in the care for a patient, but who is not the target of healthcare, nor
     * has a formal responsibility in the care process.
     */
    RELATED_PERSON("RelatedPerson", VERSION_40),

    /**
     * RequestGroup
     *
     * <p>A group of related requests that can be used to capture intended activities that have inter-dependencies such as
     * "give this medication after that one".
     */
    REQUEST_GROUP("RequestGroup", VERSION_40),

    /**
     * ResearchDefinition
     *
     * <p>The ResearchDefinition resource describes the conditional state (population and any exposures being compared within
     * the population) and outcome (if specified) that the knowledge (evidence, assertion, recommendation) is about.
     */
    RESEARCH_DEFINITION("ResearchDefinition", VERSION_40),

    /**
     * ResearchElementDefinition
     *
     * <p>The ResearchElementDefinition resource describes a "PICO" element that knowledge (evidence, assertion,
     * recommendation) is about.
     */
    RESEARCH_ELEMENT_DEFINITION("ResearchElementDefinition", VERSION_40),

    /**
     * ResearchStudy
     *
     * <p>A process where a researcher or organization plans and then executes a series of steps intended to increase the
     * field of healthcare-related knowledge. This includes studies of safety, efficacy, comparative effectiveness and other
     * information about medications, devices, therapies and other interventional and investigative techniques. A
     * ResearchStudy involves the gathering of information about human or animal subjects.
     */
    RESEARCH_STUDY("ResearchStudy", VERSION_40),

    /**
     * ResearchSubject
     *
     * <p>A physical entity which is the primary unit of operational and/or administrative interest in a study.
     */
    RESEARCH_SUBJECT("ResearchSubject", VERSION_40),

    /**
     * RiskAssessment
     *
     * <p>An assessment of the likely outcome(s) for a patient or other subject as well as the likelihood of each outcome.
     */
    RISK_ASSESSMENT("RiskAssessment", VERSION_40),

    /**
     * RiskEvidenceSynthesis
     *
     * <p>The RiskEvidenceSynthesis resource describes the likelihood of an outcome in a population plus exposure state where
     * the risk estimate is derived from a combination of research studies.
     */
    RISK_EVIDENCE_SYNTHESIS("RiskEvidenceSynthesis", VERSION_40, VERSION_43),

    /**
     * Schedule
     *
     * <p>A container for slots of time that may be available for booking appointments.
     */
    SCHEDULE("Schedule", VERSION_40),

    /**
     * SearchParameter
     *
     * <p>A search parameter that defines a named search item that can be used to search/filter on a resource.
     */
    SEARCH_PARAMETER("SearchParameter", VERSION_40),

    /**
     * ServiceRequest
     *
     * <p>A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
     */
    SERVICE_REQUEST("ServiceRequest", VERSION_40),

    /**
     * Slot
     *
     * <p>A slot of time on a schedule that may be available for booking appointments.
     */
    SLOT("Slot", VERSION_40),

    /**
     * Specimen
     *
     * <p>A sample to be used for analysis.
     */
    SPECIMEN("Specimen", VERSION_40),

    /**
     * SpecimenDefinition
     *
     * <p>A kind of specimen with associated set of requirements.
     */
    SPECIMEN_DEFINITION("SpecimenDefinition", VERSION_40),

    /**
     * StructureDefinition
     *
     * <p>A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in
     * FHIR, and also for describing extensions and constraints on resources and data types.
     */
    STRUCTURE_DEFINITION("StructureDefinition", VERSION_40),

    /**
     * StructureMap
     *
     * <p>A Map of relationships between 2 structures that can be used to transform data.
     */
    STRUCTURE_MAP("StructureMap", VERSION_40),

    /**
     * Subscription
     *
     * <p>The subscription resource is used to define a push-based subscription from a server to another system. Once a
     * subscription is registered with the server, the server checks every resource that is created or updated, and if the
     * resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an
     * appropriate action.
     */
    SUBSCRIPTION("Subscription", VERSION_40),

    /**
     * SubscriptionStatus
     *
     * <p>The SubscriptionStatus resource describes the state of a Subscription during notifications.
     */
    SUBSCRIPTION_STATUS("SubscriptionStatus", VERSION_43),

    /**
     * SubscriptionTopic
     *
     * <p>Describes a stream of resource state changes identified by trigger criteria and annotated with labels useful to
     * filter projections from this topic.
     */
    SUBSCRIPTION_TOPIC("SubscriptionTopic", VERSION_43),

    /**
     * Substance
     *
     * <p>A homogeneous material with a definite composition.
     */
    SUBSTANCE("Substance", VERSION_40),

    /**
     * SubstanceDefinition
     *
     * <p>The detailed description of a substance, typically at a level beyond what is used for prescribing.
     */
    SUBSTANCE_DEFINITION("SubstanceDefinition", VERSION_43),

    /**
     * SubstanceNucleicAcid
     *
     * <p>Nucleic acids are defined by three distinct elements: the base, sugar and linkage. Individual substance/moiety IDs
     * will be created for each of these elements. The nucleotide sequence will be always entered in the 5’-3’ direction.
     */
    SUBSTANCE_NUCLEIC_ACID("SubstanceNucleicAcid", VERSION_40, VERSION_43),

    /**
     * SubstancePolymer
     *
     * <p>Todo.
     */
    SUBSTANCE_POLYMER("SubstancePolymer", VERSION_40, VERSION_43),

    /**
     * SubstanceProtein
     *
     * <p>A SubstanceProtein is defined as a single unit of a linear amino acid sequence, or a combination of subunits that
     * are either covalently linked or have a defined invariant stoichiometric relationship. This includes all synthetic,
     * recombinant and purified SubstanceProteins of defined sequence, whether the use is therapeutic or prophylactic. This
     * set of elements will be used to describe albumins, coagulation factors, cytokines, growth factors,
     * peptide/SubstanceProtein hormones, enzymes, toxins, toxoids, recombinant vaccines, and immunomodulators.
     */
    SUBSTANCE_PROTEIN("SubstanceProtein", VERSION_40, VERSION_43),

    /**
     * SubstanceReferenceInformation
     *
     * <p>Todo.
     */
    SUBSTANCE_REFERENCE_INFORMATION("SubstanceReferenceInformation", VERSION_40, VERSION_43),

    /**
     * SubstanceSourceMaterial
     *
     * <p>Source material shall capture information on the taxonomic and anatomical origins as well as the fraction of a
     * material that can result in or can be modified to form a substance. This set of data elements shall be used to define
     * polymer substances isolated from biological matrices. Taxonomic and anatomical origins shall be described using a
     * controlled vocabulary as required. This information is captured for naturally derived polymers ( . starch) and
     * structurally diverse substances. For Organisms belonging to the Kingdom Plantae the Substance level defines the fresh
     * material of a single species or infraspecies, the Herbal Drug and the Herbal preparation. For Herbal preparations, the
     * fraction information will be captured at the Substance information level and additional information for herbal
     * extracts will be captured at the Specified Substance Group 1 information level. See for further explanation the
     * Substance Class: Structurally Diverse and the herbal annex.
     */
    SUBSTANCE_SOURCE_MATERIAL("SubstanceSourceMaterial", VERSION_40, VERSION_43),

    /**
     * SubstanceSpecification
     *
     * <p>The detailed description of a substance, typically at a level beyond what is used for prescribing.
     */
    SUBSTANCE_SPECIFICATION("SubstanceSpecification", VERSION_40, VERSION_43),

    /**
     * SupplyDelivery
     *
     * <p>Record of delivery of what is supplied.
     */
    SUPPLY_DELIVERY("SupplyDelivery", VERSION_40),

    /**
     * SupplyRequest
     *
     * <p>A record of a request for a medication, substance or device used in the healthcare setting.
     */
    SUPPLY_REQUEST("SupplyRequest", VERSION_40),

    /**
     * Task
     *
     * <p>A task to be performed.
     */
    TASK("Task", VERSION_40),

    /**
     * TerminologyCapabilities
     *
     * <p>A TerminologyCapabilities resource documents a set of capabilities (behaviors) of a FHIR Terminology Server that
     * may be used as a statement of actual server functionality or a statement of required or desired server implementation.
     */
    TERMINOLOGY_CAPABILITIES("TerminologyCapabilities", VERSION_40),

    /**
     * TestReport
     *
     * <p>A summary of information based on the results of executing a TestScript.
     */
    TEST_REPORT("TestReport", VERSION_40),

    /**
     * TestScript
     *
     * <p>A structured set of tests against a FHIR server or client implementation to determine compliance against the FHIR
     * specification.
     */
    TEST_SCRIPT("TestScript", VERSION_40),

    /**
     * ValueSet
     *
     * <p>A ValueSet resource instance specifies a set of codes drawn from one or more code systems, intended for use in a
     * particular context. Value sets link between [[[CodeSystem]]] definitions and their use in [coded elements]
     * (terminologies.html).
     */
    VALUE_SET("ValueSet", VERSION_40),

    /**
     * VerificationResult
     *
     * <p>Describes validation requirements, source(s), status and dates for one or more elements.
     */
    VERIFICATION_RESULT("VerificationResult", VERSION_40),

    /**
     * VisionPrescription
     *
     * <p>An authorization for the provision of glasses and/or contact lenses to a patient.
     */
    VISION_PRESCRIPTION("VisionPrescription", VERSION_40);

    private final String value;
    private final FHIRVersionParam introduced;
    private final FHIRVersionParam retired;

    /**
     * Construct a resource type that is active in the latest supported version of HL7 FHIR.
     *
     * @param value
     * @param introduced the higher of
     *         1. the published version of HL7 FHIR that introduced this resource type; and
     *         2. version 4.0 (because this is the first version supported by this project)
     */
    private ResourceType(String value, FHIRVersionParam introduced) {
        this(value, introduced, null);
    }

    /**
     * Construct a resource type name with metadata about when it was introduced (and optionally retired).
     *
     * @param value
     * @param introduced the higher of
     *         1. the published version of HL7 FHIR that introduced this resource type; and
     *         2. version 4.0 (because this is the first version supported by this project)
     * @param retired the published version of HL7 FHIR in which this resource type was removed
     *
     * @implNote if HL7 ever *re-introduce* a retired resource type, we'll need to revisit this structure
     */
    private ResourceType(String value, FHIRVersionParam introduced, FHIRVersionParam retired) {
        this.value = value;
        this.introduced = introduced;
        this.retired = retired;
    }

    /**
     * @return
     *     the String value of the resource type name
     */
    public String value() {
        return value;
    }

    /**
     * @return
     *     the higher of
     *         1. the published version of HL7 FHIR that introduced this resource type; and
     *         2. version 4.0 (because this is the first version supported by this project)
     */
    public FHIRVersionParam getIntroduced() {
        return introduced;
    }

    /**
     * @return
     *     the published version of HL7 FHIR in which this resource type was removed
     */
    public FHIRVersionParam getRetired() {
        return retired;
    }

    /**
     * Factory method for retrieving the ResourceType constant for the passed string value.
     *
     * @param value
     *     A string value that matches one of the ResourceType names
     * @return
     *     The corresponding ResourceType or null if a null value was passed
     * @throws IllegalArgumentException
     *     If the passed string is not null and not a valid ResourceType name
     */
    public static ResourceType from(String value) {
        if (value == null) {
            return null;
        }
        switch (value) {
        case "EffectEvidenceSynthesis":
            return EFFECT_EVIDENCE_SYNTHESIS;
        case "MedicinalProduct":
            return MEDICINAL_PRODUCT;
        case "MedicinalProductAuthorization":
            return MEDICINAL_PRODUCT_AUTHORIZATION;
        case "MedicinalProductContraindication":
            return MEDICINAL_PRODUCT_CONTRAINDICATION;
        case "MedicinalProductIndication":
            return MEDICINAL_PRODUCT_INDICATION;
        case "MedicinalProductIngredient":
            return MEDICINAL_PRODUCT_INGREDIENT;
        case "MedicinalProductInteraction":
            return MEDICINAL_PRODUCT_INTERACTION;
        case "MedicinalProductManufactured":
            return MEDICINAL_PRODUCT_MANUFACTURED;
        case "MedicinalProductPackaged":
            return MEDICINAL_PRODUCT_PACKAGED;
        case "MedicinalProductPharmaceutical":
            return MEDICINAL_PRODUCT_PHARMACEUTICAL;
        case "MedicinalProductUndesirableEffect":
            return MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT;
        case "RiskEvidenceSynthesis":
            return RISK_EVIDENCE_SYNTHESIS;
        case "SubstanceNucleicAcid":
            return SUBSTANCE_NUCLEIC_ACID;
        case "SubstancePolymer":
            return SUBSTANCE_POLYMER;
        case "SubstanceProtein":
            return SUBSTANCE_PROTEIN;
        case "SubstanceReferenceInformation":
            return SUBSTANCE_REFERENCE_INFORMATION;
        case "SubstanceSourceMaterial":
            return SUBSTANCE_SOURCE_MATERIAL;
        case "SubstanceSpecification":
            return SUBSTANCE_SPECIFICATION;
        case "Resource":
            return RESOURCE;
        case "Binary":
            return BINARY;
        case "Bundle":
            return BUNDLE;
        case "DomainResource":
            return DOMAIN_RESOURCE;
        case "Account":
            return ACCOUNT;
        case "ActivityDefinition":
            return ACTIVITY_DEFINITION;
        case "AdministrableProductDefinition":
            return ADMINISTRABLE_PRODUCT_DEFINITION;
        case "AdverseEvent":
            return ADVERSE_EVENT;
        case "AllergyIntolerance":
            return ALLERGY_INTOLERANCE;
        case "Appointment":
            return APPOINTMENT;
        case "AppointmentResponse":
            return APPOINTMENT_RESPONSE;
        case "AuditEvent":
            return AUDIT_EVENT;
        case "Basic":
            return BASIC;
        case "BiologicallyDerivedProduct":
            return BIOLOGICALLY_DERIVED_PRODUCT;
        case "BodyStructure":
            return BODY_STRUCTURE;
        case "CapabilityStatement":
            return CAPABILITY_STATEMENT;
        case "CarePlan":
            return CARE_PLAN;
        case "CareTeam":
            return CARE_TEAM;
        case "CatalogEntry":
            return CATALOG_ENTRY;
        case "ChargeItem":
            return CHARGE_ITEM;
        case "ChargeItemDefinition":
            return CHARGE_ITEM_DEFINITION;
        case "Citation":
            return CITATION;
        case "Claim":
            return CLAIM;
        case "ClaimResponse":
            return CLAIM_RESPONSE;
        case "ClinicalImpression":
            return CLINICAL_IMPRESSION;
        case "ClinicalUseDefinition":
            return CLINICAL_USE_DEFINITION;
        case "CodeSystem":
            return CODE_SYSTEM;
        case "Communication":
            return COMMUNICATION;
        case "CommunicationRequest":
            return COMMUNICATION_REQUEST;
        case "CompartmentDefinition":
            return COMPARTMENT_DEFINITION;
        case "Composition":
            return COMPOSITION;
        case "ConceptMap":
            return CONCEPT_MAP;
        case "Condition":
            return CONDITION;
        case "Consent":
            return CONSENT;
        case "Contract":
            return CONTRACT;
        case "Coverage":
            return COVERAGE;
        case "CoverageEligibilityRequest":
            return COVERAGE_ELIGIBILITY_REQUEST;
        case "CoverageEligibilityResponse":
            return COVERAGE_ELIGIBILITY_RESPONSE;
        case "DetectedIssue":
            return DETECTED_ISSUE;
        case "Device":
            return DEVICE;
        case "DeviceDefinition":
            return DEVICE_DEFINITION;
        case "DeviceMetric":
            return DEVICE_METRIC;
        case "DeviceRequest":
            return DEVICE_REQUEST;
        case "DeviceUseStatement":
            return DEVICE_USE_STATEMENT;
        case "DiagnosticReport":
            return DIAGNOSTIC_REPORT;
        case "DocumentManifest":
            return DOCUMENT_MANIFEST;
        case "DocumentReference":
            return DOCUMENT_REFERENCE;
        case "Encounter":
            return ENCOUNTER;
        case "Endpoint":
            return ENDPOINT;
        case "EnrollmentRequest":
            return ENROLLMENT_REQUEST;
        case "EnrollmentResponse":
            return ENROLLMENT_RESPONSE;
        case "EpisodeOfCare":
            return EPISODE_OF_CARE;
        case "EventDefinition":
            return EVENT_DEFINITION;
        case "Evidence":
            return EVIDENCE;
        case "EvidenceReport":
            return EVIDENCE_REPORT;
        case "EvidenceVariable":
            return EVIDENCE_VARIABLE;
        case "ExampleScenario":
            return EXAMPLE_SCENARIO;
        case "ExplanationOfBenefit":
            return EXPLANATION_OF_BENEFIT;
        case "FamilyMemberHistory":
            return FAMILY_MEMBER_HISTORY;
        case "Flag":
            return FLAG;
        case "Goal":
            return GOAL;
        case "GraphDefinition":
            return GRAPH_DEFINITION;
        case "Group":
            return GROUP;
        case "GuidanceResponse":
            return GUIDANCE_RESPONSE;
        case "HealthcareService":
            return HEALTHCARE_SERVICE;
        case "ImagingStudy":
            return IMAGING_STUDY;
        case "Immunization":
            return IMMUNIZATION;
        case "ImmunizationEvaluation":
            return IMMUNIZATION_EVALUATION;
        case "ImmunizationRecommendation":
            return IMMUNIZATION_RECOMMENDATION;
        case "ImplementationGuide":
            return IMPLEMENTATION_GUIDE;
        case "Ingredient":
            return INGREDIENT;
        case "InsurancePlan":
            return INSURANCE_PLAN;
        case "Invoice":
            return INVOICE;
        case "Library":
            return LIBRARY;
        case "Linkage":
            return LINKAGE;
        case "List":
            return LIST;
        case "Location":
            return LOCATION;
        case "ManufacturedItemDefinition":
            return MANUFACTURED_ITEM_DEFINITION;
        case "Measure":
            return MEASURE;
        case "MeasureReport":
            return MEASURE_REPORT;
        case "Media":
            return MEDIA;
        case "Medication":
            return MEDICATION;
        case "MedicationAdministration":
            return MEDICATION_ADMINISTRATION;
        case "MedicationDispense":
            return MEDICATION_DISPENSE;
        case "MedicationKnowledge":
            return MEDICATION_KNOWLEDGE;
        case "MedicationRequest":
            return MEDICATION_REQUEST;
        case "MedicationStatement":
            return MEDICATION_STATEMENT;
        case "MedicinalProductDefinition":
            return MEDICINAL_PRODUCT_DEFINITION;
        case "MessageDefinition":
            return MESSAGE_DEFINITION;
        case "MessageHeader":
            return MESSAGE_HEADER;
        case "MolecularSequence":
            return MOLECULAR_SEQUENCE;
        case "NamingSystem":
            return NAMING_SYSTEM;
        case "NutritionOrder":
            return NUTRITION_ORDER;
        case "NutritionProduct":
            return NUTRITION_PRODUCT;
        case "Observation":
            return OBSERVATION;
        case "ObservationDefinition":
            return OBSERVATION_DEFINITION;
        case "OperationDefinition":
            return OPERATION_DEFINITION;
        case "OperationOutcome":
            return OPERATION_OUTCOME;
        case "Organization":
            return ORGANIZATION;
        case "OrganizationAffiliation":
            return ORGANIZATION_AFFILIATION;
        case "PackagedProductDefinition":
            return PACKAGED_PRODUCT_DEFINITION;
        case "Patient":
            return PATIENT;
        case "PaymentNotice":
            return PAYMENT_NOTICE;
        case "PaymentReconciliation":
            return PAYMENT_RECONCILIATION;
        case "Person":
            return PERSON;
        case "PlanDefinition":
            return PLAN_DEFINITION;
        case "Practitioner":
            return PRACTITIONER;
        case "PractitionerRole":
            return PRACTITIONER_ROLE;
        case "Procedure":
            return PROCEDURE;
        case "Provenance":
            return PROVENANCE;
        case "Questionnaire":
            return QUESTIONNAIRE;
        case "QuestionnaireResponse":
            return QUESTIONNAIRE_RESPONSE;
        case "RegulatedAuthorization":
            return REGULATED_AUTHORIZATION;
        case "RelatedPerson":
            return RELATED_PERSON;
        case "RequestGroup":
            return REQUEST_GROUP;
        case "ResearchDefinition":
            return RESEARCH_DEFINITION;
        case "ResearchElementDefinition":
            return RESEARCH_ELEMENT_DEFINITION;
        case "ResearchStudy":
            return RESEARCH_STUDY;
        case "ResearchSubject":
            return RESEARCH_SUBJECT;
        case "RiskAssessment":
            return RISK_ASSESSMENT;
        case "Schedule":
            return SCHEDULE;
        case "SearchParameter":
            return SEARCH_PARAMETER;
        case "ServiceRequest":
            return SERVICE_REQUEST;
        case "Slot":
            return SLOT;
        case "Specimen":
            return SPECIMEN;
        case "SpecimenDefinition":
            return SPECIMEN_DEFINITION;
        case "StructureDefinition":
            return STRUCTURE_DEFINITION;
        case "StructureMap":
            return STRUCTURE_MAP;
        case "Subscription":
            return SUBSCRIPTION;
        case "SubscriptionStatus":
            return SUBSCRIPTION_STATUS;
        case "SubscriptionTopic":
            return SUBSCRIPTION_TOPIC;
        case "Substance":
            return SUBSTANCE;
        case "SubstanceDefinition":
            return SUBSTANCE_DEFINITION;
        case "SupplyDelivery":
            return SUPPLY_DELIVERY;
        case "SupplyRequest":
            return SUPPLY_REQUEST;
        case "Task":
            return TASK;
        case "TerminologyCapabilities":
            return TERMINOLOGY_CAPABILITIES;
        case "TestReport":
            return TEST_REPORT;
        case "TestScript":
            return TEST_SCRIPT;
        case "ValueSet":
            return VALUE_SET;
        case "VerificationResult":
            return VERIFICATION_RESULT;
        case "VisionPrescription":
            return VISION_PRESCRIPTION;
        case "Parameters":
            return PARAMETERS;
        default:
            throw new IllegalArgumentException(value);
        }
    }
}
