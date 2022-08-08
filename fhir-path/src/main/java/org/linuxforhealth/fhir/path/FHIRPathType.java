/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.linuxforhealth.fhir.model.resource.*;
import org.linuxforhealth.fhir.model.type.Address;
import org.linuxforhealth.fhir.model.type.Age;
import org.linuxforhealth.fhir.model.type.Annotation;
import org.linuxforhealth.fhir.model.type.Attachment;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Base64Binary;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.CodeableReference;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.ContactDetail;
import org.linuxforhealth.fhir.model.type.ContactPoint;
import org.linuxforhealth.fhir.model.type.Contributor;
import org.linuxforhealth.fhir.model.type.Count;
import org.linuxforhealth.fhir.model.type.DataRequirement;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Decimal;
import org.linuxforhealth.fhir.model.type.Distance;
import org.linuxforhealth.fhir.model.type.Dosage;
import org.linuxforhealth.fhir.model.type.Duration;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.ElementDefinition;
import org.linuxforhealth.fhir.model.type.Expression;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.MarketingStatus;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Money;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Oid;
import org.linuxforhealth.fhir.model.type.ParameterDefinition;
import org.linuxforhealth.fhir.model.type.Period;
import org.linuxforhealth.fhir.model.type.Population;
import org.linuxforhealth.fhir.model.type.PositiveInt;
import org.linuxforhealth.fhir.model.type.ProdCharacteristic;
import org.linuxforhealth.fhir.model.type.ProductShelfLife;
import org.linuxforhealth.fhir.model.type.Quantity;
import org.linuxforhealth.fhir.model.type.Range;
import org.linuxforhealth.fhir.model.type.Ratio;
import org.linuxforhealth.fhir.model.type.RatioRange;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.RelatedArtifact;
import org.linuxforhealth.fhir.model.type.SampledData;
import org.linuxforhealth.fhir.model.type.Signature;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Time;
import org.linuxforhealth.fhir.model.type.Timing;
import org.linuxforhealth.fhir.model.type.TriggerDefinition;
import org.linuxforhealth.fhir.model.type.UnsignedInt;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.Url;
import org.linuxforhealth.fhir.model.type.UsageContext;
import org.linuxforhealth.fhir.model.type.Uuid;
import org.linuxforhealth.fhir.model.type.Xhtml;
import org.linuxforhealth.fhir.model.util.ModelSupport;

/**
 * An enumeration that contains all of the FHIR base types, FHIR complex data types, FHIR resource types,
 * FHIRPath system types and FHIRPath metamodel types needed for evaluating FHIRPath expressions.
 */
public enum FHIRPathType {
    // FHIR base types
    FHIR_ANY("FHIR", "Any"),
    FHIR_RESOURCE("FHIR", "Resource", FHIR_ANY, Resource.class),
    FHIR_DOMAIN_RESOURCE("FHIR", "DomainResource", FHIR_RESOURCE, DomainResource.class),
    FHIR_ELEMENT("FHIR", "Element", FHIR_ANY, Element.class),
    FHIR_BACKBONE_ELEMENT("FHIR", "BackboneElement", FHIR_ELEMENT, BackboneElement.class),
    FHIR_TYPE("FHIR", "Type"),

    // FHIR complex data types
    FHIR_ADDRESS("FHIR", "Address", FHIR_ELEMENT, Address.class),
    FHIR_AGE("FHIR", "Age", FHIR_ELEMENT, Age.class),
    FHIR_ANNOTATION("FHIR", "Annotation", FHIR_ELEMENT, Annotation.class),
    FHIR_ATTACHMENT("FHIR", "Attachment", FHIR_ELEMENT, Attachment.class),
    FHIR_CODEABLE_CONCEPT("FHIR", "CodeableConcept", FHIR_ELEMENT, CodeableConcept.class),
    FHIR_CODING("FHIR", "Coding", FHIR_ELEMENT, Coding.class),
    FHIR_CONTACT_DETAIL("FHIR", "ContactDetail", FHIR_ELEMENT, ContactDetail.class),
    FHIR_CONTACT_POINT("FHIR", "ContactPoint", FHIR_ELEMENT, ContactPoint.class),
    FHIR_CONTRIBUTOR("FHIR", "Contributor", FHIR_ELEMENT, Contributor.class),
    FHIR_COUNT("FHIR", "Count", FHIR_ELEMENT, Count.class),
    FHIR_DATA_REQUIREMENT("FHIR", "DataRequirement", FHIR_ELEMENT, DataRequirement.class),
    FHIR_DISTANCE("FHIR", "Distance", FHIR_ELEMENT, Distance.class),
    FHIR_DOSAGE("FHIR", "Dosage", FHIR_BACKBONE_ELEMENT, Dosage.class),
    FHIR_DURATION("FHIR", "Duration", FHIR_ELEMENT, Duration.class),
    FHIR_ELEMENT_DEFINITION("FHIR", "ElementDefinition", FHIR_BACKBONE_ELEMENT, ElementDefinition.class),
    FHIR_EXPRESSION("FHIR", "Expression", FHIR_ELEMENT, Expression.class),
    FHIR_EXTENSION("FHIR", "Extension", FHIR_ELEMENT, Extension.class),
    FHIR_HUMAN_NAME("FHIR", "HumanName", FHIR_ELEMENT, HumanName.class),
    FHIR_IDENTIFIER("FHIR", "Identifier", FHIR_ELEMENT, Identifier.class),
    FHIR_MARKETING_STATUS("FHIR", "MarketingStatus", FHIR_BACKBONE_ELEMENT, MarketingStatus.class),
    FHIR_META("FHIR", "Meta", FHIR_ELEMENT, Meta.class),
    FHIR_MONEY("FHIR", "Money", FHIR_ELEMENT, Money.class),
    FHIR_NARRATIVE("FHIR", "Narrative", FHIR_ELEMENT, Narrative.class),
    FHIR_PARAMETER_DEFINITION("FHIR", "ParameterDefinition", FHIR_ELEMENT, ParameterDefinition.class),
    FHIR_PERIOD("FHIR", "Period", FHIR_ELEMENT, Period.class),
    FHIR_POPULATION("FHIR", "Population", FHIR_BACKBONE_ELEMENT, Population.class),
    FHIR_PROD_CHARACTERISTIC("FHIR", "ProdCharacteristic", FHIR_BACKBONE_ELEMENT, ProdCharacteristic.class),
    FHIR_PRODUCT_SHELF_LIFE("FHIR", "ProductShelfLife", FHIR_BACKBONE_ELEMENT, ProductShelfLife.class),
    FHIR_QUANTITY("FHIR", "Quantity", FHIR_ELEMENT, Quantity.class),
    FHIR_RANGE("FHIR", "Range", FHIR_ELEMENT, Range.class),
    FHIR_RATIO("FHIR", "Ratio", FHIR_ELEMENT, Ratio.class),
    FHIR_REFERENCE("FHIR", "Reference", FHIR_ELEMENT, Reference.class),
    FHIR_RELATED_ARTIFACT("FHIR", "RelatedArtifact", FHIR_ELEMENT, RelatedArtifact.class),
    FHIR_SAMPLED_DATA("FHIR", "SampledData", FHIR_ELEMENT, SampledData.class),
    FHIR_SIGNATURE("FHIR", "Signature", FHIR_ELEMENT, Signature.class),
    FHIR_CODEABLE_REFERENCE("FHIR", "CodeableReference", FHIR_BACKBONE_ELEMENT, CodeableReference.class),
    FHIR_RATIO_RANGE("FHIR", "RatioRange", FHIR_BACKBONE_ELEMENT, RatioRange.class),
    FHIR_TIMING("FHIR", "Timing", FHIR_BACKBONE_ELEMENT, Timing.class),
    FHIR_TRIGGER_DEFINITION("FHIR", "TriggerDefinition", FHIR_ELEMENT, TriggerDefinition.class),
    FHIR_USAGE_CONTEXT("FHIR", "UsageContext", FHIR_ELEMENT, UsageContext.class),

    // FHIR primitive data types
    FHIR_BASE64BINARY("FHIR", "base64Binary", FHIR_ELEMENT, Base64Binary.class),
    FHIR_BOOLEAN("FHIR", "boolean", FHIR_ELEMENT, Boolean.class),
    FHIR_CANONICAL("FHIR", "canonical", FHIR_ELEMENT, Canonical.class),
    FHIR_CODE("FHIR", "code", FHIR_ELEMENT, Code.class),
    FHIR_DATE("FHIR", "date", FHIR_ELEMENT, Date.class),
    FHIR_DATE_TIME("FHIR", "dateTime", FHIR_ELEMENT, DateTime.class),
    FHIR_DECIMAL("FHIR", "decimal", FHIR_ELEMENT, Decimal.class),
    FHIR_ID("FHIR", "id", FHIR_ELEMENT, Id.class),
    FHIR_INSTANT("FHIR", "instant", FHIR_ELEMENT, Instant.class),
    FHIR_INTEGER("FHIR", "integer", FHIR_ELEMENT, Integer.class),
    FHIR_MARKDOWN("FHIR", "markdown", FHIR_ELEMENT, Markdown.class),
    FHIR_OID("FHIR", "oid", FHIR_ELEMENT, Oid.class),
    FHIR_POSITIVE_INT("FHIR", "positiveInt", FHIR_ELEMENT, PositiveInt.class),
    FHIR_STRING("FHIR", "string", FHIR_ELEMENT, String.class),
    FHIR_TIME("FHIR", "time", FHIR_ELEMENT, Time.class),
    FHIR_UNSIGNED_INT("FHIR", "unsignedInt", FHIR_ELEMENT, UnsignedInt.class),
    FHIR_URI("FHIR", "uri", FHIR_ELEMENT, Uri.class),
    FHIR_URL("FHIR", "url", FHIR_ELEMENT, Url.class),
    FHIR_UUID("FHIR", "uuid", FHIR_ELEMENT, Uuid.class),
    FHIR_XHTML("FHIR", "xhtml", FHIR_ELEMENT, Xhtml.class),

    // FHIR resource types
    FHIR_ACCOUNT("FHIR", "Account", FHIR_DOMAIN_RESOURCE, Account.class),
    FHIR_ACTIVITY_DEFINITION("FHIR", "ActivityDefinition", FHIR_DOMAIN_RESOURCE, ActivityDefinition.class),
    FHIR_ADVERSE_EVENT("FHIR", "AdverseEvent", FHIR_DOMAIN_RESOURCE, AdverseEvent.class),
    FHIR_ALLERGY_INTOLERANCE("FHIR", "AllergyIntolerance", FHIR_DOMAIN_RESOURCE, AllergyIntolerance.class),
    FHIR_APPOINTMENT("FHIR", "Appointment", FHIR_DOMAIN_RESOURCE, Appointment.class),
    FHIR_APPOINTMENT_RESPONSE("FHIR", "AppointmentResponse", FHIR_DOMAIN_RESOURCE, AppointmentResponse.class),
    FHIR_AUDIT_EVENT("FHIR", "AuditEvent", FHIR_DOMAIN_RESOURCE, AuditEvent.class),
    FHIR_BASIC("FHIR", "Basic", FHIR_DOMAIN_RESOURCE, Basic.class),
    FHIR_BINARY("FHIR", "Binary", FHIR_RESOURCE, Binary.class),
    FHIR_BIOLOGICALLY_DERIVED_PRODUCT("FHIR", "BiologicallyDerivedProduct", FHIR_DOMAIN_RESOURCE, BiologicallyDerivedProduct.class),
    FHIR_BODY_STRUCTURE("FHIR", "BodyStructure", FHIR_DOMAIN_RESOURCE, BodyStructure.class),
    FHIR_BUNDLE("FHIR", "Bundle", FHIR_RESOURCE, Bundle.class),
    FHIR_CAPABILITY_STATEMENT("FHIR", "CapabilityStatement", FHIR_DOMAIN_RESOURCE, CapabilityStatement.class),
    FHIR_CARE_PLAN("FHIR", "CarePlan", FHIR_DOMAIN_RESOURCE, CarePlan.class),
    FHIR_CARE_TEAM("FHIR", "CareTeam", FHIR_DOMAIN_RESOURCE, CareTeam.class),
    FHIR_CATALOG_ENTRY("FHIR", "CatalogEntry", FHIR_DOMAIN_RESOURCE, CatalogEntry.class),
    FHIR_CHARGE_ITEM("FHIR", "ChargeItem", FHIR_DOMAIN_RESOURCE, ChargeItem.class),
    FHIR_CHARGE_ITEM_DEFINITION("FHIR", "ChargeItemDefinition", FHIR_DOMAIN_RESOURCE, ChargeItemDefinition.class),
    FHIR_CLAIM("FHIR", "Claim", FHIR_DOMAIN_RESOURCE, Claim.class),
    FHIR_CLAIM_RESPONSE("FHIR", "ClaimResponse", FHIR_DOMAIN_RESOURCE, ClaimResponse.class),
    FHIR_CLINICAL_IMPRESSION("FHIR", "ClinicalImpression", FHIR_DOMAIN_RESOURCE, ClinicalImpression.class),
    FHIR_CODE_SYSTEM("FHIR", "CodeSystem", FHIR_DOMAIN_RESOURCE, CodeSystem.class),
    FHIR_COMMUNICATION("FHIR", "Communication", FHIR_DOMAIN_RESOURCE, Communication.class),
    FHIR_COMMUNICATION_REQUEST("FHIR", "CommunicationRequest", FHIR_DOMAIN_RESOURCE, CommunicationRequest.class),
    FHIR_COMPARTMENT_DEFINITION("FHIR", "CompartmentDefinition", FHIR_DOMAIN_RESOURCE, CompartmentDefinition.class),
    FHIR_COMPOSITION("FHIR", "Composition", FHIR_DOMAIN_RESOURCE, Composition.class),
    FHIR_CONCEPT_MAP("FHIR", "ConceptMap", FHIR_DOMAIN_RESOURCE, ConceptMap.class),
    FHIR_CONDITION("FHIR", "Condition", FHIR_DOMAIN_RESOURCE, Condition.class),
    FHIR_CONSENT("FHIR", "Consent", FHIR_DOMAIN_RESOURCE, Consent.class),
    FHIR_CONTRACT("FHIR", "Contract", FHIR_DOMAIN_RESOURCE, Contract.class),
    FHIR_COVERAGE("FHIR", "Coverage", FHIR_DOMAIN_RESOURCE, Coverage.class),
    FHIR_COVERAGE_ELIGIBILITY_REQUEST("FHIR", "CoverageEligibilityRequest", FHIR_DOMAIN_RESOURCE, CoverageEligibilityRequest.class),
    FHIR_COVERAGE_ELIGIBILITY_RESPONSE("FHIR", "CoverageEligibilityResponse", FHIR_DOMAIN_RESOURCE, CoverageEligibilityResponse.class),
    FHIR_DETECTED_ISSUE("FHIR", "DetectedIssue", FHIR_DOMAIN_RESOURCE, DetectedIssue.class),
    FHIR_DEVICE("FHIR", "Device", FHIR_DOMAIN_RESOURCE, Device.class),
    FHIR_DEVICE_DEFINITION("FHIR", "DeviceDefinition", FHIR_DOMAIN_RESOURCE, DeviceDefinition.class),
    FHIR_DEVICE_METRIC("FHIR", "DeviceMetric", FHIR_DOMAIN_RESOURCE, DeviceMetric.class),
    FHIR_DEVICE_REQUEST("FHIR", "DeviceRequest", FHIR_DOMAIN_RESOURCE, DeviceRequest.class),
    FHIR_DEVICE_USE_STATEMENT("FHIR", "DeviceUseStatement", FHIR_DOMAIN_RESOURCE, DeviceUseStatement.class),
    FHIR_DIAGNOSTIC_REPORT("FHIR", "DiagnosticReport", FHIR_DOMAIN_RESOURCE, DiagnosticReport.class),
    FHIR_DOCUMENT_MANIFEST("FHIR", "DocumentManifest", FHIR_DOMAIN_RESOURCE, DocumentManifest.class),
    FHIR_DOCUMENT_REFERENCE("FHIR", "DocumentReference", FHIR_DOMAIN_RESOURCE, DocumentReference.class),
    FHIR_ENCOUNTER("FHIR", "Encounter", FHIR_DOMAIN_RESOURCE, Encounter.class),
    FHIR_ENDPOINT("FHIR", "Endpoint", FHIR_DOMAIN_RESOURCE, Endpoint.class),
    FHIR_ENROLLMENT_REQUEST("FHIR", "EnrollmentRequest", FHIR_DOMAIN_RESOURCE, EnrollmentRequest.class),
    FHIR_ENROLLMENT_RESPONSE("FHIR", "EnrollmentResponse", FHIR_DOMAIN_RESOURCE, EnrollmentResponse.class),
    FHIR_EPISODE_OF_CARE("FHIR", "EpisodeOfCare", FHIR_DOMAIN_RESOURCE, EpisodeOfCare.class),
    FHIR_EVENT_DEFINITION("FHIR", "EventDefinition", FHIR_DOMAIN_RESOURCE, EventDefinition.class),
    FHIR_EVIDENCE("FHIR", "Evidence", FHIR_DOMAIN_RESOURCE, Evidence.class),
    FHIR_EVIDENCE_VARIABLE("FHIR", "EvidenceVariable", FHIR_DOMAIN_RESOURCE, EvidenceVariable.class),
    FHIR_EXAMPLE_SCENARIO("FHIR", "ExampleScenario", FHIR_DOMAIN_RESOURCE, ExampleScenario.class),
    FHIR_EXPLANATION_OF_BENEFIT("FHIR", "ExplanationOfBenefit", FHIR_DOMAIN_RESOURCE, ExplanationOfBenefit.class),
    FHIR_FAMILY_MEMBER_HISTORY("FHIR", "FamilyMemberHistory", FHIR_DOMAIN_RESOURCE, FamilyMemberHistory.class),
    FHIR_FLAG("FHIR", "Flag", FHIR_DOMAIN_RESOURCE, Flag.class),
    FHIR_GOAL("FHIR", "Goal", FHIR_DOMAIN_RESOURCE, Goal.class),
    FHIR_GRAPH_DEFINITION("FHIR", "GraphDefinition", FHIR_DOMAIN_RESOURCE, GraphDefinition.class),
    FHIR_GROUP("FHIR", "Group", FHIR_DOMAIN_RESOURCE, Group.class),
    FHIR_GUIDANCE_RESPONSE("FHIR", "GuidanceResponse", FHIR_DOMAIN_RESOURCE, GuidanceResponse.class),
    FHIR_HEALTHCARE_SERVICE("FHIR", "HealthcareService", FHIR_DOMAIN_RESOURCE, HealthcareService.class),
    FHIR_IMAGING_STUDY("FHIR", "ImagingStudy", FHIR_DOMAIN_RESOURCE, ImagingStudy.class),
    FHIR_IMMUNIZATION("FHIR", "Immunization", FHIR_DOMAIN_RESOURCE, Immunization.class),
    FHIR_IMMUNIZATION_EVALUATION("FHIR", "ImmunizationEvaluation", FHIR_DOMAIN_RESOURCE, ImmunizationEvaluation.class),
    FHIR_IMMUNIZATION_RECOMMENDATION("FHIR", "ImmunizationRecommendation", FHIR_DOMAIN_RESOURCE, ImmunizationRecommendation.class),
    FHIR_IMPLEMENTATION_GUIDE("FHIR", "ImplementationGuide", FHIR_DOMAIN_RESOURCE, ImplementationGuide.class),
    FHIR_INSURANCE_PLAN("FHIR", "InsurancePlan", FHIR_DOMAIN_RESOURCE, InsurancePlan.class),
    FHIR_INVOICE("FHIR", "Invoice", FHIR_DOMAIN_RESOURCE, Invoice.class),
    FHIR_LIBRARY("FHIR", "Library", FHIR_DOMAIN_RESOURCE, Library.class),
    FHIR_LINKAGE("FHIR", "Linkage", FHIR_DOMAIN_RESOURCE, Linkage.class),
    FHIR_LIST("FHIR", "List", FHIR_DOMAIN_RESOURCE, List.class),
    FHIR_LOCATION("FHIR", "Location", FHIR_DOMAIN_RESOURCE, Location.class),
    FHIR_MEASURE("FHIR", "Measure", FHIR_DOMAIN_RESOURCE, Measure.class),
    FHIR_MEASURE_REPORT("FHIR", "MeasureReport", FHIR_DOMAIN_RESOURCE, MeasureReport.class),
    FHIR_MEDIA("FHIR", "Media", FHIR_DOMAIN_RESOURCE, Media.class),
    FHIR_MEDICATION("FHIR", "Medication", FHIR_DOMAIN_RESOURCE, Medication.class),
    FHIR_MEDICATION_ADMINISTRATION("FHIR", "MedicationAdministration", FHIR_DOMAIN_RESOURCE, MedicationAdministration.class),
    FHIR_MEDICATION_DISPENSE("FHIR", "MedicationDispense", FHIR_DOMAIN_RESOURCE, MedicationDispense.class),
    FHIR_MEDICATION_KNOWLEDGE("FHIR", "MedicationKnowledge", FHIR_DOMAIN_RESOURCE, MedicationKnowledge.class),
    FHIR_MEDICATION_REQUEST("FHIR", "MedicationRequest", FHIR_DOMAIN_RESOURCE, MedicationRequest.class),
    FHIR_MEDICATION_STATEMENT("FHIR", "MedicationStatement", FHIR_DOMAIN_RESOURCE, MedicationStatement.class),
    FHIR_MESSAGE_DEFINITION("FHIR", "MessageDefinition", FHIR_DOMAIN_RESOURCE, MessageDefinition.class),
    FHIR_MESSAGE_HEADER("FHIR", "MessageHeader", FHIR_DOMAIN_RESOURCE, MessageHeader.class),
    FHIR_MOLECULAR_SEQUENCE("FHIR", "MolecularSequence", FHIR_DOMAIN_RESOURCE, MolecularSequence.class),
    FHIR_NAMING_SYSTEM("FHIR", "NamingSystem", FHIR_DOMAIN_RESOURCE, NamingSystem.class),
    FHIR_NUTRITION_ORDER("FHIR", "NutritionOrder", FHIR_DOMAIN_RESOURCE, NutritionOrder.class),
    FHIR_OBSERVATION("FHIR", "Observation", FHIR_DOMAIN_RESOURCE, Observation.class),
    FHIR_OBSERVATION_DEFINITION("FHIR", "ObservationDefinition", FHIR_DOMAIN_RESOURCE, ObservationDefinition.class),
    FHIR_OPERATION_DEFINITION("FHIR", "OperationDefinition", FHIR_DOMAIN_RESOURCE, OperationDefinition.class),
    FHIR_OPERATION_OUTCOME("FHIR", "OperationOutcome", FHIR_DOMAIN_RESOURCE, OperationOutcome.class),
    FHIR_ORGANIZATION("FHIR", "Organization", FHIR_DOMAIN_RESOURCE, Organization.class),
    FHIR_ORGANIZATION_AFFILIATION("FHIR", "OrganizationAffiliation", FHIR_DOMAIN_RESOURCE, OrganizationAffiliation.class),
    FHIR_PARAMETERS("FHIR", "Parameters", FHIR_RESOURCE, Parameters.class),
    FHIR_PATIENT("FHIR", "Patient", FHIR_DOMAIN_RESOURCE, Patient.class),
    FHIR_PAYMENT_NOTICE("FHIR", "PaymentNotice", FHIR_DOMAIN_RESOURCE, PaymentNotice.class),
    FHIR_PAYMENT_RECONCILIATION("FHIR", "PaymentReconciliation", FHIR_DOMAIN_RESOURCE, PaymentReconciliation.class),
    FHIR_PERSON("FHIR", "Person", FHIR_DOMAIN_RESOURCE, Person.class),
    FHIR_PLAN_DEFINITION("FHIR", "PlanDefinition", FHIR_DOMAIN_RESOURCE, PlanDefinition.class),
    FHIR_PRACTITIONER("FHIR", "Practitioner", FHIR_DOMAIN_RESOURCE, Practitioner.class),
    FHIR_PRACTITIONER_ROLE("FHIR", "PractitionerRole", FHIR_DOMAIN_RESOURCE, PractitionerRole.class),
    FHIR_PROCEDURE("FHIR", "Procedure", FHIR_DOMAIN_RESOURCE, Procedure.class),
    FHIR_PROVENANCE("FHIR", "Provenance", FHIR_DOMAIN_RESOURCE, Provenance.class),
    FHIR_QUESTIONNAIRE("FHIR", "Questionnaire", FHIR_DOMAIN_RESOURCE, Questionnaire.class),
    FHIR_QUESTIONNAIRE_RESPONSE("FHIR", "QuestionnaireResponse", FHIR_DOMAIN_RESOURCE, QuestionnaireResponse.class),
    FHIR_RELATED_PERSON("FHIR", "RelatedPerson", FHIR_DOMAIN_RESOURCE, RelatedPerson.class),
    FHIR_REQUEST_GROUP("FHIR", "RequestGroup", FHIR_DOMAIN_RESOURCE, RequestGroup.class),
    FHIR_RESEARCH_DEFINITION("FHIR", "ResearchDefinition", FHIR_DOMAIN_RESOURCE, ResearchDefinition.class),
    FHIR_RESEARCH_ELEMENT_DEFINITION("FHIR", "ResearchElementDefinition", FHIR_DOMAIN_RESOURCE, ResearchElementDefinition.class),
    FHIR_RESEARCH_STUDY("FHIR", "ResearchStudy", FHIR_DOMAIN_RESOURCE, ResearchStudy.class),
    FHIR_RESEARCH_SUBJECT("FHIR", "ResearchSubject", FHIR_DOMAIN_RESOURCE, ResearchSubject.class),
    FHIR_RISK_ASSESSMENT("FHIR", "RiskAssessment", FHIR_DOMAIN_RESOURCE, RiskAssessment.class),
    FHIR_SCHEDULE("FHIR", "Schedule", FHIR_DOMAIN_RESOURCE, Schedule.class),
    FHIR_SEARCH_PARAMETER("FHIR", "SearchParameter", FHIR_DOMAIN_RESOURCE, SearchParameter.class),
    FHIR_SERVICE_REQUEST("FHIR", "ServiceRequest", FHIR_DOMAIN_RESOURCE, ServiceRequest.class),
    FHIR_SLOT("FHIR", "Slot", FHIR_DOMAIN_RESOURCE, Slot.class),
    FHIR_SPECIMEN("FHIR", "Specimen", FHIR_DOMAIN_RESOURCE, Specimen.class),
    FHIR_SPECIMEN_DEFINITION("FHIR", "SpecimenDefinition", FHIR_DOMAIN_RESOURCE, SpecimenDefinition.class),
    FHIR_STRUCTURE_DEFINITION("FHIR", "StructureDefinition", FHIR_DOMAIN_RESOURCE, StructureDefinition.class),
    FHIR_STRUCTURE_MAP("FHIR", "StructureMap", FHIR_DOMAIN_RESOURCE, StructureMap.class),
    FHIR_SUBSCRIPTION("FHIR", "Subscription", FHIR_DOMAIN_RESOURCE, Subscription.class),
    FHIR_SUBSTANCE("FHIR", "Substance", FHIR_DOMAIN_RESOURCE, Substance.class),
    FHIR_SUPPLY_DELIVERY("FHIR", "SupplyDelivery", FHIR_DOMAIN_RESOURCE, SupplyDelivery.class),
    FHIR_SUPPLY_REQUEST("FHIR", "SupplyRequest", FHIR_DOMAIN_RESOURCE, SupplyRequest.class),
    FHIR_TASK("FHIR", "Task", FHIR_DOMAIN_RESOURCE, Task.class),
    FHIR_TERMINOLOGY_CAPABILITIES("FHIR", "TerminologyCapabilities", FHIR_DOMAIN_RESOURCE, TerminologyCapabilities.class),
    FHIR_TEST_REPORT("FHIR", "TestReport", FHIR_DOMAIN_RESOURCE, TestReport.class),
    FHIR_TEST_SCRIPT("FHIR", "TestScript", FHIR_DOMAIN_RESOURCE, TestScript.class),
    FHIR_VALUE_SET("FHIR", "ValueSet", FHIR_DOMAIN_RESOURCE, ValueSet.class),
    FHIR_VERIFICATION_RESULT("FHIR", "VerificationResult", FHIR_DOMAIN_RESOURCE, VerificationResult.class),
    FHIR_VISION_PRESCRIPTION("FHIR", "VisionPrescription", FHIR_DOMAIN_RESOURCE, VisionPrescription.class),

    FHIR_ADMINISTRABLE_PRODUCT_DEFINITION("FHIR", "AdministrableProductDefinition", FHIR_DOMAIN_RESOURCE, AdministrableProductDefinition.class),
    FHIR_CITATION("FHIR", "Citation", FHIR_DOMAIN_RESOURCE, Citation.class),
    FHIR_CLINICAL_USE_DEFINITION("FHIR", "ClinicalUseDefinition", FHIR_DOMAIN_RESOURCE, ClinicalUseDefinition.class),
    FHIR_EVIDENCE_REPORT("FHIR", "EvidenceReport", FHIR_DOMAIN_RESOURCE, EvidenceReport.class),
    FHIR_INGREDIENT("FHIR", "Ingredient", FHIR_DOMAIN_RESOURCE, Ingredient.class),
    FHIR_MANUFACTURED_ITEM_DEFINITION("FHIR", "ManufacturedItemDefinition", FHIR_DOMAIN_RESOURCE, ManufacturedItemDefinition.class),
    FHIR_MEDICINAL_PRODUCT_DEFINITION("FHIR", "MedicinalProductDefinition", FHIR_DOMAIN_RESOURCE, MedicinalProductDefinition.class),
    FHIR_NUTRITION_PRODUCT("FHIR", "NutritionProduct", FHIR_DOMAIN_RESOURCE, NutritionProduct.class),
    FHIR_PACKAGED_PRODUCT_DEFINITION("FHIR", "PackagedProductDefinition", FHIR_DOMAIN_RESOURCE, PackagedProductDefinition.class),
    FHIR_REGULATED_AUTHORIZATION("FHIR", "RegulatedAuthorization", FHIR_DOMAIN_RESOURCE, RegulatedAuthorization.class),
    FHIR_SUBSCRIPTION_STATUS("FHIR", "SubscriptionStatus", FHIR_DOMAIN_RESOURCE, SubscriptionStatus.class),
    FHIR_SUBSCRIPTION_TOPIC("FHIR", "SubscriptionTopic", FHIR_DOMAIN_RESOURCE, SubscriptionTopic.class),
    FHIR_SUBSTANCE_DEFINITION("FHIR", "SubstanceDefinition", FHIR_DOMAIN_RESOURCE, SubstanceDefinition.class),

    /**
     * "Special" FHIR type returned by the resolve() function when the resource type cannot be determined from a reference.
     */
    FHIR_UNKNOWN_RESOURCE_TYPE("FHIR", "UnknownResourceType", FHIR_RESOURCE),

    FHIR_TERM_SERVICE("FHIR", "TermService", FHIR_TYPE),

    // FHIRPath system types
    SYSTEM_ANY("System", "Any"),
    SYSTEM_BOOLEAN("System", "Boolean", SYSTEM_ANY),
    SYSTEM_STRING("System", "String", SYSTEM_ANY),
    SYSTEM_INTEGER("System", "Integer", SYSTEM_ANY),
    SYSTEM_DECIMAL("System", "Decimal", SYSTEM_ANY),
    SYSTEM_DATE("System", "Date", SYSTEM_ANY),
    SYSTEM_DATE_TIME("System", "DateTime", SYSTEM_ANY),
    SYSTEM_QUANTITY("System", "Quantity", SYSTEM_ANY),
    SYSTEM_TIME("System", "Time", SYSTEM_ANY),

    // FHIRPath metamodel types
    SYSTEM_TYPE_INFO("System", "TypeInfo", SYSTEM_ANY),
    SYSTEM_CLASS_INFO("System", "ClassInfo", SYSTEM_TYPE_INFO),
    SYSTEM_TUPLE_TYPE_INFO("System", "TupleTypeInfo", SYSTEM_TYPE_INFO),
    SYSTEM_LIST_TYPE_INFO("System", "ListTypeInfo", SYSTEM_TYPE_INFO),
    SYSTEM_SIMPLE_TYPE_INFO("System", "SystemTypeInfo", SYSTEM_TYPE_INFO);

    private final java.lang.String namespace;
    private final java.lang.String name;
    private final FHIRPathType baseType;
    private final Class<?> modelClass;

    private static final Map<java.lang.String, FHIRPathType> TYPE_NAME_MAP = buildTypeNameMap();
    private static final Map<Class<?>, FHIRPathType> TYPE_MAP = buildTypeMap();
    private static final Set<FHIRPathType> SYSTEM_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(SYSTEM_BOOLEAN, SYSTEM_STRING, SYSTEM_INTEGER, SYSTEM_DECIMAL, SYSTEM_DATE, SYSTEM_DATE_TIME, SYSTEM_QUANTITY, SYSTEM_TIME)));
    private static final Set<FHIRPathType> METAMODEL_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(SYSTEM_TYPE_INFO, SYSTEM_CLASS_INFO, SYSTEM_TUPLE_TYPE_INFO, SYSTEM_LIST_TYPE_INFO, SYSTEM_SIMPLE_TYPE_INFO)));
    private static final Map<Class<?>, FHIRPathType> METAMODEL_TYPE_MAP = buildMetamodelTypeMap();
    private static final Map<Class<?>, FHIRPathType> JAVA_TYPE_MAP = buildJavaTypeMap();

    private static Map<java.lang.String, FHIRPathType> buildTypeNameMap() {
        Map<java.lang.String, FHIRPathType> typeNameMap = new HashMap<>();
        for (FHIRPathType type : FHIRPathType.values()) {
            typeNameMap.put(type.namespace + "." + type.name, type);
        }
        return Collections.unmodifiableMap(typeNameMap);
    }

    private static Map<Class<?>, FHIRPathType> buildMetamodelTypeMap() {
        Map<Class<?>, FHIRPathType> metamodelTypeMap = new HashMap<>();
        metamodelTypeMap.put(TypeInfo.class, SYSTEM_TYPE_INFO);
        metamodelTypeMap.put(ClassInfo.class, SYSTEM_CLASS_INFO);
        metamodelTypeMap.put(TupleTypeInfo.class, SYSTEM_TUPLE_TYPE_INFO);
        metamodelTypeMap.put(ListTypeInfo.class, SYSTEM_LIST_TYPE_INFO);
        metamodelTypeMap.put(SimpleTypeInfo.class, SYSTEM_SIMPLE_TYPE_INFO);
        return Collections.unmodifiableMap(metamodelTypeMap);
    }

    private static Map<Class<?>, FHIRPathType> buildJavaTypeMap() {
        Map<Class<?>, FHIRPathType> javaTypeMap = new HashMap<>();
        javaTypeMap.put(java.lang.Boolean.class, SYSTEM_BOOLEAN);
        javaTypeMap.put(java.lang.Integer.class, SYSTEM_INTEGER);
        javaTypeMap.put(java.math.BigDecimal.class, SYSTEM_DECIMAL);
        javaTypeMap.put(java.lang.String.class, SYSTEM_STRING);
        javaTypeMap.put(java.time.LocalTime.class, SYSTEM_TIME);
        javaTypeMap.put(java.time.ZonedDateTime.class, SYSTEM_DATE_TIME);
        javaTypeMap.put(java.time.LocalDate.class, SYSTEM_DATE_TIME);
        javaTypeMap.put(java.time.YearMonth.class, SYSTEM_DATE_TIME);
        javaTypeMap.put(java.time.Year.class, SYSTEM_DATE_TIME);
        return Collections.unmodifiableMap(javaTypeMap);
    }

    private static Map<Class<?>, FHIRPathType> buildTypeMap() {
        Map<Class<?>, FHIRPathType> typeMap = new HashMap<>();
        for (FHIRPathType type : FHIRPathType.values()) {
            if (type.modelClass != null) {
                typeMap.put(type.modelClass, type);
            }
        }
        return Collections.unmodifiableMap(typeMap);
    }

    FHIRPathType(java.lang.String namespace, java.lang.String name) {
        this(namespace, name, null, null);
    }

    FHIRPathType(java.lang.String namespace, java.lang.String name, FHIRPathType baseType) {
        this(namespace, name, baseType, null);
    }

    FHIRPathType(java.lang.String namespace, java.lang.String name, FHIRPathType baseType, Class<?> modelClass) {
        this.namespace = namespace;
        this.name = name;
        this.baseType = baseType;
        this.modelClass = modelClass;
    }

    /**
     * The namespace of this FHIRPathType
     *
     * @return
     *     the namespace of this FHIRPathType
     */
    public java.lang.String namespace() {
        return namespace;
    }

    /**
     * The name of this FHIRPathType
     *
     * @return
     *     the name of this FHIRPathType
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * The base type of this FHIRPathType
     *
     * @return
     *     the base type of this FHIRPathType if exists, otherwise null
     */
    public FHIRPathType baseType() {
        return baseType;
    }

    /**
     * The model class that corresponds to this FHIRPathType
     *
     * @return
     *     the model class that corresponds to this FHIRPathType if exists, otherwise null
     */
    public Class<?> modelClass() {
        return modelClass;
    }

    /**
     * Determines if this type is the same as or a supertype of the one specified by the method parameter.
     *
     * @param type
     *     the type to check against
     * @return
     *     true if this type is assignable from the one in the method parameter, false otherwise
     */
    public boolean isAssignableFrom(FHIRPathType type) {
        if (type == FHIR_UNKNOWN_RESOURCE_TYPE && FHIR_RESOURCE.isAssignableFrom(this)) {
            // every resource type is assignable from FHIR.UnknownResourceType
            return true;
        }
        if (type == null) {
            // every type is assignable from null
            return true;
        }
        if (this == type) {
            // every type is assignable from itself
            return true;
        }
        if (this == SYSTEM_ANY && "System".equals(type.namespace)) {
            // System.Any is assignable from any System type
            return true;
        }
        if (this == FHIR_ANY && "FHIR".equals(type.namespace)) {
            // FHIR.Any is assignable from any FHIR type
            return true;
        }
        return (type.baseType != null && isAssignableFrom(type.baseType));
    }

    /**
     * Create a FHIRPathType from the {@link java.lang.String} name parameter
     *
     * @param name
     *     the name
     * @return
     *     the FHIRPathType that corresponds to the name parameter if exists, otherwise null
     */
    public static FHIRPathType from(java.lang.String name) {
        if (name.contains(".")) {
            return TYPE_NAME_MAP.get(name);
        }
        FHIRPathType type = from("FHIR", name);
        if (type == null) {
            type = from("System", name);
        }
        return type;
    }

    /**
     * Create a FHIRPathType from a {@link java.lang.String} name and {@link java.lang.String} namespace
     *
     * @param namespace
     *     the namespace
     * @param name
     *     the name
     * @return
     *     the FHIRPathType that corresponds to the name and namespace parameters if exists, otherwise null
     */
    public static FHIRPathType from(java.lang.String namespace, java.lang.String name) {
        return TYPE_NAME_MAP.get(namespace + "." + name);
    }

    /**
     * Create a FHIRPathType from a {@link Class} class
     *
     * @param clazz
     *     the class
     * @return
     *     the FHIRPathType that corresponds to the clazz parameter if exists, otherwise null
     */
    public static FHIRPathType from(Class<?> clazz) {
        if (TypeInfo.class.isAssignableFrom(clazz)) {
            return METAMODEL_TYPE_MAP.get(clazz);
        }
        if (ModelSupport.isModelClass(clazz)) {
            FHIRPathType type = TYPE_MAP.get(ModelSupport.getConcreteType(clazz));
            if (type == null) {
                if (BackboneElement.class.isAssignableFrom(clazz)) {
                    type = FHIR_BACKBONE_ELEMENT;
                } else if (Code.class.isAssignableFrom(clazz)) {
                    type = FHIR_CODE;
                }
            }
            return type;
        }
        if (isJavaType(clazz)) {
            return JAVA_TYPE_MAP.get(clazz);
        }
        return null;
    }

    /**
     * The set of FHIRPath system types
     *
     * @return
     *     the set of FHIRPath system types
     */
    public static Set<FHIRPathType> getSystemTypes() {
        return SYSTEM_TYPES;
    }

    /**
     * Indicates whether the parameter is a FHIRPath system type
     *
     * @param type
     *     the type
     * @return
     *     true if the parameter is a FHIRPath system type, otherwise false
     */
    public static boolean isSystemType(FHIRPathType type) {
        return SYSTEM_TYPES.contains(type);
    }

    /**
     * The set of FHIRPath metamodel thypes
     *
     * @return
     *     the set of FHIRPath metamodel types
     */
    public static Set<FHIRPathType> getMetamodelTypes() {
        return METAMODEL_TYPES;
    }

    /**
     * Indicates whether the parameter is a FHIRPath metamodel type
     *
     * @param type
     *     the type
     * @return
     *     true if the parameter is a FHIRPath metamodel type, otherwise false
     */
    public static boolean isMetamodelType(FHIRPathType type) {
        return METAMODEL_TYPES.contains(type);
    }

    /**
     * Indicates whether the parameter is a Java type
     *
     * @param type
     *     the type
     * @return
     *     true if the parameter is a Java type, otherwise false
     */
    public static boolean isJavaType(Class<?> clazz) {
        return clazz.getName().startsWith("java.");
    }
}
