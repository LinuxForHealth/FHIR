/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.visitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import javax.annotation.Generated;

import com.ibm.fhir.model.resource.*;
import com.ibm.fhir.model.type.*;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.String;

/**
 * Visitor interface for visiting FHIR model objects that implement Visitable.
 * 
 * Each model object can accept a visitor and contains logic for invoking the corresponding visit method for itself and all its members.
 * 
 * At each level, the visitor can control traversal by returning true or false as indicated in the following snippet:
 * <pre>
 * if (visitor.preVisit(this)) {
 *     visitor.visitStart(elementName, elementIndex, this);
 *     if (visitor.visit(elementName, elementIndex, this)) {
 *         // visit children
 *     }
 *     visitor.visitEnd(elementName, elementIndex, this);
 *     visitor.postVisit(this);
 * }
 * </pre>
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public interface Visitor {
    /**
     * @return true if this Element should be visited; otherwise false
     */
    boolean preVisit(Element element);

    /**
     * @return true if this Resource should be visited; otherwise false
     */
    boolean preVisit(Resource resource);

    void postVisit(Element element);
    void postVisit(Resource resource);

    void visitStart(java.lang.String elementName, int elementIndex, Element element);
    void visitStart(java.lang.String elementName, int elementIndex, Resource resource);
    void visitStart(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type);

    void visitEnd(java.lang.String elementName, int elementIndex, Element element);
    void visitEnd(java.lang.String elementName, int elementIndex, Resource resource);
    void visitEnd(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type);

    /**
     * @return
     *     true if the children of this visitable should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Visitable visitable);
    /**
     * @return
     *     true if the children of this account should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Account account);

    /**
     * @return
     *     true if the children of this activityDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ActivityDefinition activityDefinition);

    /**
     * @return
     *     true if the children of this address should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Address address);

    /**
     * @return
     *     true if the children of this administrableProductDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, AdministrableProductDefinition administrableProductDefinition);

    /**
     * @return
     *     true if the children of this adverseEvent should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, AdverseEvent adverseEvent);

    /**
     * @return
     *     true if the children of this age should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Age age);

    /**
     * @return
     *     true if the children of this allergyIntolerance should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, AllergyIntolerance allergyIntolerance);

    /**
     * @return
     *     true if the children of this annotation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Annotation annotation);

    /**
     * @return
     *     true if the children of this appointment should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Appointment appointment);

    /**
     * @return
     *     true if the children of this appointmentResponse should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, AppointmentResponse appointmentResponse);

    /**
     * @return
     *     true if the children of this attachment should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Attachment attachment);

    /**
     * @return
     *     true if the children of this auditEvent should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, AuditEvent auditEvent);

    /**
     * @return
     *     true if the children of this backboneElement should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, BackboneElement backboneElement);

    /**
     * @return
     *     true if the children of this base64Binary should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Base64Binary base64Binary);

    /**
     * @return
     *     true if the children of this basic should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Basic basic);

    /**
     * @return
     *     true if the children of this binary should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Binary binary);

    /**
     * @return
     *     true if the children of this biologicallyDerivedProduct should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct biologicallyDerivedProduct);

    /**
     * @return
     *     true if the children of this bodyStructure should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, BodyStructure bodyStructure);

    /**
     * @return
     *     true if the children of this _boolean should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Boolean _boolean);

    /**
     * @return
     *     true if the children of this bundle should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Bundle bundle);

    /**
     * @return
     *     true if the children of this canonical should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Canonical canonical);

    /**
     * @return
     *     true if the children of this capabilityStatement should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement capabilityStatement);

    /**
     * @return
     *     true if the children of this carePlan should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CarePlan carePlan);

    /**
     * @return
     *     true if the children of this careTeam should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CareTeam careTeam);

    /**
     * @return
     *     true if the children of this catalogEntry should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CatalogEntry catalogEntry);

    /**
     * @return
     *     true if the children of this chargeItem should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ChargeItem chargeItem);

    /**
     * @return
     *     true if the children of this chargeItemDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition chargeItemDefinition);

    /**
     * @return
     *     true if the children of this citation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Citation citation);

    /**
     * @return
     *     true if the children of this claim should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Claim claim);

    /**
     * @return
     *     true if the children of this claimResponse should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse claimResponse);

    /**
     * @return
     *     true if the children of this clinicalImpression should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ClinicalImpression clinicalImpression);

    /**
     * @return
     *     true if the children of this clinicalUseDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ClinicalUseDefinition clinicalUseDefinition);

    /**
     * @return
     *     true if the children of this clinicalUseIssue should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ClinicalUseIssue clinicalUseIssue);

    /**
     * @return
     *     true if the children of this code should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Code code);

    /**
     * @return
     *     true if the children of this codeSystem should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CodeSystem codeSystem);

    /**
     * @return
     *     true if the children of this codeableConcept should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CodeableConcept codeableConcept);

    /**
     * @return
     *     true if the children of this codeableReference should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CodeableReference codeableReference);

    /**
     * @return
     *     true if the children of this coding should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Coding coding);

    /**
     * @return
     *     true if the children of this communication should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Communication communication);

    /**
     * @return
     *     true if the children of this communicationRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CommunicationRequest communicationRequest);

    /**
     * @return
     *     true if the children of this compartmentDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CompartmentDefinition compartmentDefinition);

    /**
     * @return
     *     true if the children of this composition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Composition composition);

    /**
     * @return
     *     true if the children of this conceptMap should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ConceptMap conceptMap);

    /**
     * @return
     *     true if the children of this condition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Condition condition);

    /**
     * @return
     *     true if the children of this consent should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Consent consent);

    /**
     * @return
     *     true if the children of this contactDetail should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ContactDetail contactDetail);

    /**
     * @return
     *     true if the children of this contactPoint should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ContactPoint contactPoint);

    /**
     * @return
     *     true if the children of this contract should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Contract contract);

    /**
     * @return
     *     true if the children of this contributor should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Contributor contributor);

    /**
     * @return
     *     true if the children of this count should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Count count);

    /**
     * @return
     *     true if the children of this coverage should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Coverage coverage);

    /**
     * @return
     *     true if the children of this coverageEligibilityRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest coverageEligibilityRequest);

    /**
     * @return
     *     true if the children of this coverageEligibilityResponse should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse coverageEligibilityResponse);

    /**
     * @return
     *     true if the children of this dataRequirement should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DataRequirement dataRequirement);

    /**
     * @return
     *     true if the children of this dataType should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DataType dataType);

    /**
     * @return
     *     true if the children of this date should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Date date);

    /**
     * @return
     *     true if the children of this dateTime should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DateTime dateTime);

    /**
     * @return
     *     true if the children of this decimal should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Decimal decimal);

    /**
     * @return
     *     true if the children of this detectedIssue should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DetectedIssue detectedIssue);

    /**
     * @return
     *     true if the children of this device should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Device device);

    /**
     * @return
     *     true if the children of this deviceDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition deviceDefinition);

    /**
     * @return
     *     true if the children of this deviceMetric should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DeviceMetric deviceMetric);

    /**
     * @return
     *     true if the children of this deviceRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DeviceRequest deviceRequest);

    /**
     * @return
     *     true if the children of this deviceUseStatement should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DeviceUseStatement deviceUseStatement);

    /**
     * @return
     *     true if the children of this diagnosticReport should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DiagnosticReport diagnosticReport);

    /**
     * @return
     *     true if the children of this distance should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Distance distance);

    /**
     * @return
     *     true if the children of this documentManifest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DocumentManifest documentManifest);

    /**
     * @return
     *     true if the children of this documentReference should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DocumentReference documentReference);

    /**
     * @return
     *     true if the children of this domainResource should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, DomainResource domainResource);

    /**
     * @return
     *     true if the children of this dosage should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Dosage dosage);

    /**
     * @return
     *     true if the children of this duration should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Duration duration);

    /**
     * @return
     *     true if the children of this element should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Element element);

    /**
     * @return
     *     true if the children of this elementDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition elementDefinition);

    /**
     * @return
     *     true if the children of this encounter should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Encounter encounter);

    /**
     * @return
     *     true if the children of this endpoint should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Endpoint endpoint);

    /**
     * @return
     *     true if the children of this enrollmentRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, EnrollmentRequest enrollmentRequest);

    /**
     * @return
     *     true if the children of this enrollmentResponse should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, EnrollmentResponse enrollmentResponse);

    /**
     * @return
     *     true if the children of this episodeOfCare should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, EpisodeOfCare episodeOfCare);

    /**
     * @return
     *     true if the children of this eventDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, EventDefinition eventDefinition);

    /**
     * @return
     *     true if the children of this evidence should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Evidence evidence);

    /**
     * @return
     *     true if the children of this evidenceReport should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, EvidenceReport evidenceReport);

    /**
     * @return
     *     true if the children of this evidenceVariable should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, EvidenceVariable evidenceVariable);

    /**
     * @return
     *     true if the children of this exampleScenario should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario exampleScenario);

    /**
     * @return
     *     true if the children of this explanationOfBenefit should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit explanationOfBenefit);

    /**
     * @return
     *     true if the children of this expression should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Expression expression);

    /**
     * @return
     *     true if the children of this extension should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Extension extension);

    /**
     * @return
     *     true if the children of this familyMemberHistory should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, FamilyMemberHistory familyMemberHistory);

    /**
     * @return
     *     true if the children of this flag should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Flag flag);

    /**
     * @return
     *     true if the children of this goal should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Goal goal);

    /**
     * @return
     *     true if the children of this graphDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition graphDefinition);

    /**
     * @return
     *     true if the children of this group should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Group group);

    /**
     * @return
     *     true if the children of this guidanceResponse should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, GuidanceResponse guidanceResponse);

    /**
     * @return
     *     true if the children of this healthcareService should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, HealthcareService healthcareService);

    /**
     * @return
     *     true if the children of this humanName should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName);

    /**
     * @return
     *     true if the children of this id should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Id id);

    /**
     * @return
     *     true if the children of this identifier should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier);

    /**
     * @return
     *     true if the children of this imagingStudy should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy imagingStudy);

    /**
     * @return
     *     true if the children of this immunization should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Immunization immunization);

    /**
     * @return
     *     true if the children of this immunizationEvaluation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ImmunizationEvaluation immunizationEvaluation);

    /**
     * @return
     *     true if the children of this immunizationRecommendation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ImmunizationRecommendation immunizationRecommendation);

    /**
     * @return
     *     true if the children of this implementationGuide should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide implementationGuide);

    /**
     * @return
     *     true if the children of this ingredient should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Ingredient ingredient);

    /**
     * @return
     *     true if the children of this instant should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Instant instant);

    /**
     * @return
     *     true if the children of this insurancePlan should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan insurancePlan);

    /**
     * @return
     *     true if the children of this integer should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Integer integer);

    /**
     * @return
     *     true if the children of this invoice should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Invoice invoice);

    /**
     * @return
     *     true if the children of this library should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Library library);

    /**
     * @return
     *     true if the children of this linkage should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Linkage linkage);

    /**
     * @return
     *     true if the children of this list should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, List list);

    /**
     * @return
     *     true if the children of this location should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Location location);

    /**
     * @return
     *     true if the children of this manufacturedItemDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ManufacturedItemDefinition manufacturedItemDefinition);

    /**
     * @return
     *     true if the children of this markdown should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Markdown markdown);

    /**
     * @return
     *     true if the children of this marketingStatus should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MarketingStatus marketingStatus);

    /**
     * @return
     *     true if the children of this measure should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Measure measure);

    /**
     * @return
     *     true if the children of this measureReport should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MeasureReport measureReport);

    /**
     * @return
     *     true if the children of this media should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Media media);

    /**
     * @return
     *     true if the children of this medication should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Medication medication);

    /**
     * @return
     *     true if the children of this medicationAdministration should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MedicationAdministration medicationAdministration);

    /**
     * @return
     *     true if the children of this medicationDispense should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MedicationDispense medicationDispense);

    /**
     * @return
     *     true if the children of this medicationKnowledge should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge medicationKnowledge);

    /**
     * @return
     *     true if the children of this medicationRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest medicationRequest);

    /**
     * @return
     *     true if the children of this medicationStatement should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MedicationStatement medicationStatement);

    /**
     * @return
     *     true if the children of this medicinalProductDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductDefinition medicinalProductDefinition);

    /**
     * @return
     *     true if the children of this messageDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MessageDefinition messageDefinition);

    /**
     * @return
     *     true if the children of this messageHeader should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MessageHeader messageHeader);

    /**
     * @return
     *     true if the children of this meta should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Meta meta);

    /**
     * @return
     *     true if the children of this molecularSequence should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence molecularSequence);

    /**
     * @return
     *     true if the children of this money should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Money money);

    /**
     * @return
     *     true if the children of this moneyQuantity should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, MoneyQuantity moneyQuantity);

    /**
     * @return
     *     true if the children of this namingSystem should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, NamingSystem namingSystem);

    /**
     * @return
     *     true if the children of this narrative should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Narrative narrative);

    /**
     * @return
     *     true if the children of this nutritionOrder should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder nutritionOrder);

    /**
     * @return
     *     true if the children of this nutritionProduct should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, NutritionProduct nutritionProduct);

    /**
     * @return
     *     true if the children of this observation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Observation observation);

    /**
     * @return
     *     true if the children of this observationDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ObservationDefinition observationDefinition);

    /**
     * @return
     *     true if the children of this oid should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Oid oid);

    /**
     * @return
     *     true if the children of this operationDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition operationDefinition);

    /**
     * @return
     *     true if the children of this operationOutcome should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, OperationOutcome operationOutcome);

    /**
     * @return
     *     true if the children of this organization should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Organization organization);

    /**
     * @return
     *     true if the children of this organizationAffiliation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, OrganizationAffiliation organizationAffiliation);

    /**
     * @return
     *     true if the children of this packagedProductDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, PackagedProductDefinition packagedProductDefinition);

    /**
     * @return
     *     true if the children of this parameterDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ParameterDefinition parameterDefinition);

    /**
     * @return
     *     true if the children of this parameters should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Parameters parameters);

    /**
     * @return
     *     true if the children of this patient should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Patient patient);

    /**
     * @return
     *     true if the children of this paymentNotice should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, PaymentNotice paymentNotice);

    /**
     * @return
     *     true if the children of this paymentReconciliation should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, PaymentReconciliation paymentReconciliation);

    /**
     * @return
     *     true if the children of this period should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Period period);

    /**
     * @return
     *     true if the children of this person should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Person person);

    /**
     * @return
     *     true if the children of this planDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition planDefinition);

    /**
     * @return
     *     true if the children of this population should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Population population);

    /**
     * @return
     *     true if the children of this positiveInt should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, PositiveInt positiveInt);

    /**
     * @return
     *     true if the children of this practitioner should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Practitioner practitioner);

    /**
     * @return
     *     true if the children of this practitionerRole should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, PractitionerRole practitionerRole);

    /**
     * @return
     *     true if the children of this procedure should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Procedure procedure);

    /**
     * @return
     *     true if the children of this prodCharacteristic should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ProdCharacteristic prodCharacteristic);

    /**
     * @return
     *     true if the children of this productShelfLife should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ProductShelfLife productShelfLife);

    /**
     * @return
     *     true if the children of this provenance should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Provenance provenance);

    /**
     * @return
     *     true if the children of this quantity should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Quantity quantity);

    /**
     * @return
     *     true if the children of this questionnaire should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Questionnaire questionnaire);

    /**
     * @return
     *     true if the children of this questionnaireResponse should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, QuestionnaireResponse questionnaireResponse);

    /**
     * @return
     *     true if the children of this range should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Range range);

    /**
     * @return
     *     true if the children of this ratio should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Ratio ratio);

    /**
     * @return
     *     true if the children of this ratioRange should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, RatioRange ratioRange);

    /**
     * @return
     *     true if the children of this reference should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Reference reference);

    /**
     * @return
     *     true if the children of this regulatedAuthorization should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, RegulatedAuthorization regulatedAuthorization);

    /**
     * @return
     *     true if the children of this relatedArtifact should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, RelatedArtifact relatedArtifact);

    /**
     * @return
     *     true if the children of this relatedPerson should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, RelatedPerson relatedPerson);

    /**
     * @return
     *     true if the children of this requestGroup should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, RequestGroup requestGroup);

    /**
     * @return
     *     true if the children of this researchDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ResearchDefinition researchDefinition);

    /**
     * @return
     *     true if the children of this researchElementDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ResearchElementDefinition researchElementDefinition);

    /**
     * @return
     *     true if the children of this researchStudy should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ResearchStudy researchStudy);

    /**
     * @return
     *     true if the children of this researchSubject should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ResearchSubject researchSubject);

    /**
     * @return
     *     true if the children of this resource should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Resource resource);

    /**
     * @return
     *     true if the children of this riskAssessment should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, RiskAssessment riskAssessment);

    /**
     * @return
     *     true if the children of this sampledData should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SampledData sampledData);

    /**
     * @return
     *     true if the children of this schedule should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Schedule schedule);

    /**
     * @return
     *     true if the children of this searchParameter should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SearchParameter searchParameter);

    /**
     * @return
     *     true if the children of this serviceRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ServiceRequest serviceRequest);

    /**
     * @return
     *     true if the children of this signature should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Signature signature);

    /**
     * @return
     *     true if the children of this simpleQuantity should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SimpleQuantity simpleQuantity);

    /**
     * @return
     *     true if the children of this slot should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Slot slot);

    /**
     * @return
     *     true if the children of this specimen should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Specimen specimen);

    /**
     * @return
     *     true if the children of this specimenDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition specimenDefinition);

    /**
     * @return
     *     true if the children of this string should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, String string);

    /**
     * @return
     *     true if the children of this structureDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition structureDefinition);

    /**
     * @return
     *     true if the children of this structureMap should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, StructureMap structureMap);

    /**
     * @return
     *     true if the children of this subscription should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Subscription subscription);

    /**
     * @return
     *     true if the children of this subscriptionStatus should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SubscriptionStatus subscriptionStatus);

    /**
     * @return
     *     true if the children of this subscriptionTopic should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SubscriptionTopic subscriptionTopic);

    /**
     * @return
     *     true if the children of this substance should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Substance substance);

    /**
     * @return
     *     true if the children of this substanceDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SubstanceDefinition substanceDefinition);

    /**
     * @return
     *     true if the children of this supplyDelivery should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SupplyDelivery supplyDelivery);

    /**
     * @return
     *     true if the children of this supplyRequest should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, SupplyRequest supplyRequest);

    /**
     * @return
     *     true if the children of this task should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Task task);

    /**
     * @return
     *     true if the children of this terminologyCapabilities should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities terminologyCapabilities);

    /**
     * @return
     *     true if the children of this testReport should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, TestReport testReport);

    /**
     * @return
     *     true if the children of this testScript should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, TestScript testScript);

    /**
     * @return
     *     true if the children of this time should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Time time);

    /**
     * @return
     *     true if the children of this timing should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Timing timing);

    /**
     * @return
     *     true if the children of this triggerDefinition should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, TriggerDefinition triggerDefinition);

    /**
     * @return
     *     true if the children of this unsignedInt should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, UnsignedInt unsignedInt);

    /**
     * @return
     *     true if the children of this uri should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Uri uri);

    /**
     * @return
     *     true if the children of this url should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Url url);

    /**
     * @return
     *     true if the children of this usageContext should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, UsageContext usageContext);

    /**
     * @return
     *     true if the children of this uuid should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Uuid uuid);

    /**
     * @return
     *     true if the children of this valueSet should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, ValueSet valueSet);

    /**
     * @return
     *     true if the children of this verificationResult should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, VerificationResult verificationResult);

    /**
     * @return
     *     true if the children of this visionPrescription should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, VisionPrescription visionPrescription);

    /**
     * @return
     *     true if the children of this xhtml should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Xhtml xhtml);

    void visit(java.lang.String elementName, byte[] value);
    void visit(java.lang.String elementName, BigDecimal value);
    void visit(java.lang.String elementName, java.lang.Boolean value);
    void visit(java.lang.String elementName, java.lang.Integer value);
    void visit(java.lang.String elementName, LocalDate value);
    void visit(java.lang.String elementName, LocalTime value);
    void visit(java.lang.String elementName, java.lang.String value);
    void visit(java.lang.String elementName, Year value);
    void visit(java.lang.String elementName, YearMonth value);
    void visit(java.lang.String elementName, ZonedDateTime value);
    /**
     * @return
     *     true if the children of this Location.Position should be visited; otherwise false
     */
    boolean visit(java.lang.String elementName, int elementIndex, Location.Position position);
}
