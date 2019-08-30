/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ibm.watson.health.fhir.model.resource.*;
import com.ibm.watson.health.fhir.model.type.Address;
import com.ibm.watson.health.fhir.model.type.Age;
import com.ibm.watson.health.fhir.model.type.Annotation;
import com.ibm.watson.health.fhir.model.type.Attachment;
import com.ibm.watson.health.fhir.model.type.BackboneElement;
import com.ibm.watson.health.fhir.model.type.Base64Binary;
import com.ibm.watson.health.fhir.model.type.Boolean;
import com.ibm.watson.health.fhir.model.type.Canonical;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.CodeableConcept;
import com.ibm.watson.health.fhir.model.type.Coding;
import com.ibm.watson.health.fhir.model.type.ContactDetail;
import com.ibm.watson.health.fhir.model.type.ContactPoint;
import com.ibm.watson.health.fhir.model.type.Contributor;
import com.ibm.watson.health.fhir.model.type.Count;
import com.ibm.watson.health.fhir.model.type.DataRequirement;
import com.ibm.watson.health.fhir.model.type.Date;
import com.ibm.watson.health.fhir.model.type.DateTime;
import com.ibm.watson.health.fhir.model.type.Decimal;
import com.ibm.watson.health.fhir.model.type.Distance;
import com.ibm.watson.health.fhir.model.type.Dosage;
import com.ibm.watson.health.fhir.model.type.Duration;
import com.ibm.watson.health.fhir.model.type.Element;
import com.ibm.watson.health.fhir.model.type.ElementDefinition;
import com.ibm.watson.health.fhir.model.type.Expression;
import com.ibm.watson.health.fhir.model.type.Extension;
import com.ibm.watson.health.fhir.model.type.HumanName;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.Identifier;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.Integer;
import com.ibm.watson.health.fhir.model.type.Markdown;
import com.ibm.watson.health.fhir.model.type.MarketingStatus;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.model.type.Money;
import com.ibm.watson.health.fhir.model.type.Narrative;
import com.ibm.watson.health.fhir.model.type.Oid;
import com.ibm.watson.health.fhir.model.type.ParameterDefinition;
import com.ibm.watson.health.fhir.model.type.Period;
import com.ibm.watson.health.fhir.model.type.Population;
import com.ibm.watson.health.fhir.model.type.PositiveInt;
import com.ibm.watson.health.fhir.model.type.ProdCharacteristic;
import com.ibm.watson.health.fhir.model.type.ProductShelfLife;
import com.ibm.watson.health.fhir.model.type.Quantity;
import com.ibm.watson.health.fhir.model.type.Range;
import com.ibm.watson.health.fhir.model.type.Ratio;
import com.ibm.watson.health.fhir.model.type.Reference;
import com.ibm.watson.health.fhir.model.type.RelatedArtifact;
import com.ibm.watson.health.fhir.model.type.SampledData;
import com.ibm.watson.health.fhir.model.type.Signature;
import com.ibm.watson.health.fhir.model.type.String;
import com.ibm.watson.health.fhir.model.type.SubstanceAmount;
import com.ibm.watson.health.fhir.model.type.Time;
import com.ibm.watson.health.fhir.model.type.Timing;
import com.ibm.watson.health.fhir.model.type.TriggerDefinition;
import com.ibm.watson.health.fhir.model.type.UnsignedInt;
import com.ibm.watson.health.fhir.model.type.Uri;
import com.ibm.watson.health.fhir.model.type.Url;
import com.ibm.watson.health.fhir.model.type.UsageContext;
import com.ibm.watson.health.fhir.model.type.Uuid;
import com.ibm.watson.health.fhir.model.type.Xhtml;
import com.ibm.watson.health.fhir.model.util.ModelSupport;

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
    FHIR_SUBSTANCE_AMOUNT("FHIR", "SubstanceAmount", FHIR_BACKBONE_ELEMENT, SubstanceAmount.class),
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
    FHIR_EFFECT_EVIDENCE_SYNTHESIS("FHIR", "EffectEvidenceSynthesis", FHIR_DOMAIN_RESOURCE, EffectEvidenceSynthesis.class),
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
    FHIR_MEDICINAL_PRODUCT("FHIR", "MedicinalProduct", FHIR_DOMAIN_RESOURCE, MedicinalProduct.class),
    FHIR_MEDICINAL_PRODUCT_AUTHORIZATION("FHIR", "MedicinalProductAuthorization", FHIR_DOMAIN_RESOURCE, MedicinalProductAuthorization.class),
    FHIR_MEDICINAL_PRODUCT_CONTRAINDICATION("FHIR", "MedicinalProductContraindication", FHIR_DOMAIN_RESOURCE, MedicinalProductContraindication.class),
    FHIR_MEDICINAL_PRODUCT_INDICATION("FHIR", "MedicinalProductIndication", FHIR_DOMAIN_RESOURCE, MedicinalProductIndication.class),
    FHIR_MEDICINAL_PRODUCT_INGREDIENT("FHIR", "MedicinalProductIngredient", FHIR_DOMAIN_RESOURCE, MedicinalProductIngredient.class),
    FHIR_MEDICINAL_PRODUCT_INTERACTION("FHIR", "MedicinalProductInteraction", FHIR_DOMAIN_RESOURCE, MedicinalProductInteraction.class),
    FHIR_MEDICINAL_PRODUCT_MANUFACTURED("FHIR", "MedicinalProductManufactured", FHIR_DOMAIN_RESOURCE, MedicinalProductManufactured.class),
    FHIR_MEDICINAL_PRODUCT_PACKAGED("FHIR", "MedicinalProductPackaged", FHIR_DOMAIN_RESOURCE, MedicinalProductPackaged.class),
    FHIR_MEDICINAL_PRODUCT_PHARMACEUTICAL("FHIR", "MedicinalProductPharmaceutical", FHIR_DOMAIN_RESOURCE, MedicinalProductPharmaceutical.class),
    FHIR_MEDICINAL_PRODUCT_UNDESIRABLE_EFFECT("FHIR", "MedicinalProductUndesirableEffect", FHIR_DOMAIN_RESOURCE, MedicinalProductUndesirableEffect.class),
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
    FHIR_RISK_EVIDENCE_SYNTHESIS("FHIR", "RiskEvidenceSynthesis", FHIR_DOMAIN_RESOURCE, RiskEvidenceSynthesis.class),
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
    FHIR_SUBSTANCE_NUCLEIC_ACID("FHIR", "SubstanceNucleicAcid", FHIR_DOMAIN_RESOURCE, SubstanceNucleicAcid.class),
    FHIR_SUBSTANCE_POLYMER("FHIR", "SubstancePolymer", FHIR_DOMAIN_RESOURCE, SubstancePolymer.class),
    FHIR_SUBSTANCE_PROTEIN("FHIR", "SubstanceProtein", FHIR_DOMAIN_RESOURCE, SubstanceProtein.class),
    FHIR_SUBSTANCE_REFERENCE_INFORMATION("FHIR", "SubstanceReferenceInformation", FHIR_DOMAIN_RESOURCE, SubstanceReferenceInformation.class),
    FHIR_SUBSTANCE_SOURCE_MATERIAL("FHIR", "SubstanceSourceMaterial", FHIR_DOMAIN_RESOURCE, SubstanceSourceMaterial.class),
    FHIR_SUBSTANCE_SPECIFICATION("FHIR", "SubstanceSpecification", FHIR_DOMAIN_RESOURCE, SubstanceSpecification.class),
    FHIR_SUPPLY_DELIVERY("FHIR", "SupplyDelivery", FHIR_DOMAIN_RESOURCE, SupplyDelivery.class),
    FHIR_SUPPLY_REQUEST("FHIR", "SupplyRequest", FHIR_DOMAIN_RESOURCE, SupplyRequest.class),
    FHIR_TASK("FHIR", "Task", FHIR_DOMAIN_RESOURCE, Task.class),
    FHIR_TERMINOLOGY_CAPABILITIES("FHIR", "TerminologyCapabilities", FHIR_DOMAIN_RESOURCE, TerminologyCapabilities.class),
    FHIR_TEST_REPORT("FHIR", "TestReport", FHIR_DOMAIN_RESOURCE, TestReport.class),
    FHIR_TEST_SCRIPT("FHIR", "TestScript", FHIR_DOMAIN_RESOURCE, TestScript.class),
    FHIR_VALUE_SET("FHIR", "ValueSet", FHIR_DOMAIN_RESOURCE, ValueSet.class),
    FHIR_VERIFICATION_RESULT("FHIR", "VerificationResult", FHIR_DOMAIN_RESOURCE, VerificationResult.class),
    FHIR_VISION_PRESCRIPTION("FHIR", "VisionPrescription", FHIR_DOMAIN_RESOURCE, VisionPrescription.class),
    
    // FHIRPath system types
    SYSTEM_ANY("System", "Any"),
    SYSTEM_BOOLEAN("System", "Boolean", SYSTEM_ANY, java.lang.Boolean.class),
    SYSTEM_STRING("System", "String", SYSTEM_ANY, java.lang.String.class),
    SYSTEM_INTEGER("System", "Integer", SYSTEM_ANY, java.lang.Integer.class),
    SYSTEM_DECIMAL("System", "Decimal", SYSTEM_ANY, BigDecimal.class),
    SYSTEM_DATE_TIME("System", "DateTime", SYSTEM_ANY, TemporalAccessor.class),
    SYSTEM_TIME("System", "Time", SYSTEM_ANY, LocalTime.class),
    
    // FHIRPath metamodel types
    SYSTEM_TYPE_INFO("System", "TypeInfo", SYSTEM_ANY, TypeInfo.class),
    SYSTEM_CLASS_INFO("System", "ClassInfo", SYSTEM_TYPE_INFO, ClassInfo.class),
    SYSTEM_TUPLE_TYPE_INFO("System", "TupleTypeInfo", SYSTEM_TYPE_INFO, TupleTypeInfo.class),
    SYSTEM_LIST_TYPE_INFO("System", "ListTypeInfo", SYSTEM_TYPE_INFO, ListTypeInfo.class),
    SYSTEM_SIMPLE_TYPE_INFO("System", "SystemTypeInfo", SYSTEM_TYPE_INFO, SimpleTypeInfo.class);
    
    private final java.lang.String namespace;
    private final java.lang.String name;
    private final FHIRPathType baseType;
    private final Class<?> modelClass;
    
    private static final Map<java.lang.String, FHIRPathType> TYPE_NAME_MAP = createTypeNameMap();
    private static final Map<Class<?>, FHIRPathType> TYPE_MAP = createTypeMap();
    private static final Set<FHIRPathType> SYSTEM_TYPES = new HashSet<>(Arrays.asList(SYSTEM_BOOLEAN, SYSTEM_STRING, SYSTEM_INTEGER, SYSTEM_DECIMAL, SYSTEM_DATE_TIME, SYSTEM_TIME));
    
    private static Map<java.lang.String, FHIRPathType> createTypeNameMap() {
        Map<java.lang.String, FHIRPathType> typeNameMap = new HashMap<>();
        for (FHIRPathType type : FHIRPathType.values()) {
            typeNameMap.put(type.namespace + "." + type.name, type);
        }
        return typeNameMap;
    }

    private static Map<Class<?>, FHIRPathType> createTypeMap() {
        Map<Class<?>, FHIRPathType> typeMap = new HashMap<>();
        for (FHIRPathType type : FHIRPathType.values()) {
            if (type.modelClass != null) {
              typeMap.put(type.modelClass, type);
            }
        }
        return typeMap;
    }

    FHIRPathType(java.lang.String namespace, java.lang.String name) {
        this(namespace, name, null, null);
    }
  
    FHIRPathType(java.lang.String namespace, java.lang.String name, FHIRPathType baseType, Class<?> modelClass) {
        this.namespace = namespace;
        this.name = name;
        this.baseType = baseType;
        this.modelClass = modelClass;
    }
    
    public java.lang.String namespace() {
        return namespace;
    }
    
    public java.lang.String getName() {
        return name;
    }
    
    public FHIRPathType baseType() {
        return baseType;
    }
    
    public Class<?> modelClass() {
        return modelClass;
    }
    
    public boolean isAssignableFrom(FHIRPathType type) {
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
        return isAssignableFrom(type.baseType);
    }
    
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
    
    public static FHIRPathType from(java.lang.String namespace, java.lang.String name) {
        return TYPE_NAME_MAP.get(namespace + "." + name);
    }
    
    public static FHIRPathType from(Class<?> modelClass) {
        FHIRPathType type = TYPE_MAP.get(ModelSupport.getConcreteType(modelClass));
        if (type == null) {
            if (BackboneElement.class.isAssignableFrom(modelClass)) {
                type = FHIR_BACKBONE_ELEMENT;
            } else if (Code.class.isAssignableFrom(modelClass)) {
                type = FHIR_CODE;
            }
        }
        return type;
    }
    
    public static Set<FHIRPathType> getSystemTypes() {
        return SYSTEM_TYPES;
    }
    
    public static boolean isSystemType(FHIRPathType type) {
        return SYSTEM_TYPES.contains(type);
    }
}