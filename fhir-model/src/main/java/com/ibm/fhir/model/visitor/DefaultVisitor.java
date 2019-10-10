/*
 * (C) Copyright IBM Corp. 2019
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
 * DefaultVisitor provides a default implementation of the Visitor interface which uses the
 * value of the passed {@code visitChildren} boolean to control whether or not to
 * visit the children of the Resource or Element being visited.
 * 
 * Subclasses can override the default behavior in a number of places, including:
 * <ul>
 * <li>preVisit methods to control whether a given Resource or Element gets visited
 * <li>visitStart methods to provide setup behavior prior to the visit
 * <li>defaultAction methods to perform some common action on all visited Resources and Elements
 * <li>specific visit methods to perform unique behavior that varies by the type being visited
 * <li>visitEnd methods to provide initial cleanup behavior after a Resource or Element has been visited
 * <li>postVisit methods to provide final cleanup behavior after a Resource or Element has been visited
 * </ul>
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DefaultVisitor implements Visitor {
    protected boolean visitChildren;
    /**
     * Subclasses can override this method to provide a default action for all Resource visit methods.
     * @return
     *     whether to visit the children of this resource; returns the value of the {@code visitChildren} boolean by default
     */
    protected boolean defaultAction(java.lang.String elementName, int elementIndex, Resource resource) {
        return visitChildren;
    }
    /**
     * Subclasses can override this method to provide a default action for all Element visit methods.
     * @return
     *     whether to visit the children of this resource; returns the value of the {@code visitChildren} boolean by default
     */
    protected boolean defaultAction(java.lang.String elementName, int elementIndex, Element element) {
        return visitChildren;
    }

    /**
     * @param visitChildren
     *     Whether to visit children of a Resource or Element by default. Note that subclasses may override the visit methods 
     *     and/or the defaultAction methods and decide whether to use the passed boolean or not.
     */
    public DefaultVisitor(boolean visitChildren) {
        this.visitChildren = visitChildren;
    }

    @Override
    public boolean preVisit(Element element) {
        return true;
    }

    @Override
    public boolean preVisit(Resource resource) {
        return true;
    }

    @Override
    public void postVisit(Element element) {
    }

    @Override
    public void postVisit(Resource resource) {
    }

    @Override
    public void visitStart(java.lang.String elementName, int elementIndex, Element element) {
    }

    @Override
    public void visitStart(java.lang.String elementName, int elementIndex, Resource resource) {
    }

    @Override
    public void visitStart(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, int elementIndex, Element element) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, int elementIndex, Resource resource) {
    }

    @Override
    public void visitEnd(java.lang.String elementName, java.util.List<? extends Visitable> visitables, Class<?> type) {
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Account account) {
        return defaultAction(elementName, elementIndex, account);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ActivityDefinition activityDefinition) {
        return defaultAction(elementName, elementIndex, activityDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Address address) {
        return defaultAction(elementName, elementIndex, address);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AdverseEvent adverseEvent) {
        return defaultAction(elementName, elementIndex, adverseEvent);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Age age) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Quantity) age);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AllergyIntolerance allergyIntolerance) {
        return defaultAction(elementName, elementIndex, allergyIntolerance);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Annotation annotation) {
        return defaultAction(elementName, elementIndex, annotation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Appointment appointment) {
        return defaultAction(elementName, elementIndex, appointment);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AppointmentResponse appointmentResponse) {
        return defaultAction(elementName, elementIndex, appointmentResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Attachment attachment) {
        return defaultAction(elementName, elementIndex, attachment);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent auditEvent) {
        return defaultAction(elementName, elementIndex, auditEvent);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BackboneElement backboneElement) {
        return defaultAction(elementName, elementIndex, backboneElement);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Base64Binary base64Binary) {
        return defaultAction(elementName, elementIndex, base64Binary);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Basic basic) {
        return defaultAction(elementName, elementIndex, basic);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Binary binary) {
        return defaultAction(elementName, elementIndex, binary);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct biologicallyDerivedProduct) {
        return defaultAction(elementName, elementIndex, biologicallyDerivedProduct);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BodyStructure bodyStructure) {
        return defaultAction(elementName, elementIndex, bodyStructure);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Boolean _boolean) {
        return defaultAction(elementName, elementIndex, _boolean);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle bundle) {
        return defaultAction(elementName, elementIndex, bundle);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Canonical canonical) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Uri) canonical);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement capabilityStatement) {
        return defaultAction(elementName, elementIndex, capabilityStatement);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CarePlan carePlan) {
        return defaultAction(elementName, elementIndex, carePlan);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CareTeam careTeam) {
        return defaultAction(elementName, elementIndex, careTeam);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CatalogEntry catalogEntry) {
        return defaultAction(elementName, elementIndex, catalogEntry);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItem chargeItem) {
        return defaultAction(elementName, elementIndex, chargeItem);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition chargeItemDefinition) {
        return defaultAction(elementName, elementIndex, chargeItemDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim claim) {
        return defaultAction(elementName, elementIndex, claim);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse claimResponse) {
        return defaultAction(elementName, elementIndex, claimResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClinicalImpression clinicalImpression) {
        return defaultAction(elementName, elementIndex, clinicalImpression);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.String)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Code code) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.String) code);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem codeSystem) {
        return defaultAction(elementName, elementIndex, codeSystem);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeableConcept codeableConcept) {
        return defaultAction(elementName, elementIndex, codeableConcept);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coding coding) {
        return defaultAction(elementName, elementIndex, coding);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Communication communication) {
        return defaultAction(elementName, elementIndex, communication);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CommunicationRequest communicationRequest) {
        return defaultAction(elementName, elementIndex, communicationRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CompartmentDefinition compartmentDefinition) {
        return defaultAction(elementName, elementIndex, compartmentDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition composition) {
        return defaultAction(elementName, elementIndex, composition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap conceptMap) {
        return defaultAction(elementName, elementIndex, conceptMap);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Condition condition) {
        return defaultAction(elementName, elementIndex, condition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent consent) {
        return defaultAction(elementName, elementIndex, consent);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactDetail contactDetail) {
        return defaultAction(elementName, elementIndex, contactDetail);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactPoint contactPoint) {
        return defaultAction(elementName, elementIndex, contactPoint);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract contract) {
        return defaultAction(elementName, elementIndex, contract);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contributor contributor) {
        return defaultAction(elementName, elementIndex, contributor);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Count count) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Quantity) count);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coverage coverage) {
        return defaultAction(elementName, elementIndex, coverage);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest coverageEligibilityRequest) {
        return defaultAction(elementName, elementIndex, coverageEligibilityRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse coverageEligibilityResponse) {
        return defaultAction(elementName, elementIndex, coverageEligibilityResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DataRequirement dataRequirement) {
        return defaultAction(elementName, elementIndex, dataRequirement);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Date date) {
        return defaultAction(elementName, elementIndex, date);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DateTime dateTime) {
        return defaultAction(elementName, elementIndex, dateTime);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Decimal decimal) {
        return defaultAction(elementName, elementIndex, decimal);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DetectedIssue detectedIssue) {
        return defaultAction(elementName, elementIndex, detectedIssue);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device device) {
        return defaultAction(elementName, elementIndex, device);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition deviceDefinition) {
        return defaultAction(elementName, elementIndex, deviceDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceMetric deviceMetric) {
        return defaultAction(elementName, elementIndex, deviceMetric);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceRequest deviceRequest) {
        return defaultAction(elementName, elementIndex, deviceRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceUseStatement deviceUseStatement) {
        return defaultAction(elementName, elementIndex, deviceUseStatement);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DiagnosticReport diagnosticReport) {
        return defaultAction(elementName, elementIndex, diagnosticReport);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Distance distance) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Quantity) distance);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentManifest documentManifest) {
        return defaultAction(elementName, elementIndex, documentManifest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentReference documentReference) {
        return defaultAction(elementName, elementIndex, documentReference);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DomainResource domainResource) {
        return defaultAction(elementName, elementIndex, domainResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Dosage dosage) {
        return defaultAction(elementName, elementIndex, dosage);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Duration duration) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Quantity) duration);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EffectEvidenceSynthesis effectEvidenceSynthesis) {
        return defaultAction(elementName, elementIndex, effectEvidenceSynthesis);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Element element) {
        return defaultAction(elementName, elementIndex, element);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition elementDefinition) {
        return defaultAction(elementName, elementIndex, elementDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter encounter) {
        return defaultAction(elementName, elementIndex, encounter);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Endpoint endpoint) {
        return defaultAction(elementName, elementIndex, endpoint);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EnrollmentRequest enrollmentRequest) {
        return defaultAction(elementName, elementIndex, enrollmentRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EnrollmentResponse enrollmentResponse) {
        return defaultAction(elementName, elementIndex, enrollmentResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EpisodeOfCare episodeOfCare) {
        return defaultAction(elementName, elementIndex, episodeOfCare);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EventDefinition eventDefinition) {
        return defaultAction(elementName, elementIndex, eventDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Evidence evidence) {
        return defaultAction(elementName, elementIndex, evidence);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EvidenceVariable evidenceVariable) {
        return defaultAction(elementName, elementIndex, evidenceVariable);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario exampleScenario) {
        return defaultAction(elementName, elementIndex, exampleScenario);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit explanationOfBenefit) {
        return defaultAction(elementName, elementIndex, explanationOfBenefit);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Expression expression) {
        return defaultAction(elementName, elementIndex, expression);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Extension extension) {
        return defaultAction(elementName, elementIndex, extension);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, FamilyMemberHistory familyMemberHistory) {
        return defaultAction(elementName, elementIndex, familyMemberHistory);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Flag flag) {
        return defaultAction(elementName, elementIndex, flag);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Goal goal) {
        return defaultAction(elementName, elementIndex, goal);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition graphDefinition) {
        return defaultAction(elementName, elementIndex, graphDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Group group) {
        return defaultAction(elementName, elementIndex, group);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GuidanceResponse guidanceResponse) {
        return defaultAction(elementName, elementIndex, guidanceResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HealthcareService healthcareService) {
        return defaultAction(elementName, elementIndex, healthcareService);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName) {
        return defaultAction(elementName, elementIndex, humanName);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.String)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Id id) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.String) id);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier) {
        return defaultAction(elementName, elementIndex, identifier);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy imagingStudy) {
        return defaultAction(elementName, elementIndex, imagingStudy);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization immunization) {
        return defaultAction(elementName, elementIndex, immunization);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationEvaluation immunizationEvaluation) {
        return defaultAction(elementName, elementIndex, immunizationEvaluation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationRecommendation immunizationRecommendation) {
        return defaultAction(elementName, elementIndex, immunizationRecommendation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide implementationGuide) {
        return defaultAction(elementName, elementIndex, implementationGuide);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Instant instant) {
        return defaultAction(elementName, elementIndex, instant);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan insurancePlan) {
        return defaultAction(elementName, elementIndex, insurancePlan);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Integer integer) {
        return defaultAction(elementName, elementIndex, integer);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Invoice invoice) {
        return defaultAction(elementName, elementIndex, invoice);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Library library) {
        return defaultAction(elementName, elementIndex, library);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Linkage linkage) {
        return defaultAction(elementName, elementIndex, linkage);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, List list) {
        return defaultAction(elementName, elementIndex, list);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location location) {
        return defaultAction(elementName, elementIndex, location);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.String)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Markdown markdown) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.String) markdown);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MarketingStatus marketingStatus) {
        return defaultAction(elementName, elementIndex, marketingStatus);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure measure) {
        return defaultAction(elementName, elementIndex, measure);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport measureReport) {
        return defaultAction(elementName, elementIndex, measureReport);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Media media) {
        return defaultAction(elementName, elementIndex, media);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Medication medication) {
        return defaultAction(elementName, elementIndex, medication);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationAdministration medicationAdministration) {
        return defaultAction(elementName, elementIndex, medicationAdministration);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationDispense medicationDispense) {
        return defaultAction(elementName, elementIndex, medicationDispense);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge medicationKnowledge) {
        return defaultAction(elementName, elementIndex, medicationKnowledge);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest medicationRequest) {
        return defaultAction(elementName, elementIndex, medicationRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationStatement medicationStatement) {
        return defaultAction(elementName, elementIndex, medicationStatement);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProduct medicinalProduct) {
        return defaultAction(elementName, elementIndex, medicinalProduct);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductAuthorization medicinalProductAuthorization) {
        return defaultAction(elementName, elementIndex, medicinalProductAuthorization);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductContraindication medicinalProductContraindication) {
        return defaultAction(elementName, elementIndex, medicinalProductContraindication);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIndication medicinalProductIndication) {
        return defaultAction(elementName, elementIndex, medicinalProductIndication);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductIngredient medicinalProductIngredient) {
        return defaultAction(elementName, elementIndex, medicinalProductIngredient);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductInteraction medicinalProductInteraction) {
        return defaultAction(elementName, elementIndex, medicinalProductInteraction);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductManufactured medicinalProductManufactured) {
        return defaultAction(elementName, elementIndex, medicinalProductManufactured);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPackaged medicinalProductPackaged) {
        return defaultAction(elementName, elementIndex, medicinalProductPackaged);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductPharmaceutical medicinalProductPharmaceutical) {
        return defaultAction(elementName, elementIndex, medicinalProductPharmaceutical);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductUndesirableEffect medicinalProductUndesirableEffect) {
        return defaultAction(elementName, elementIndex, medicinalProductUndesirableEffect);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageDefinition messageDefinition) {
        return defaultAction(elementName, elementIndex, messageDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageHeader messageHeader) {
        return defaultAction(elementName, elementIndex, messageHeader);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Meta meta) {
        return defaultAction(elementName, elementIndex, meta);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MetadataResource metadataResource) {
        return defaultAction(elementName, elementIndex, metadataResource);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence molecularSequence) {
        return defaultAction(elementName, elementIndex, molecularSequence);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Money money) {
        return defaultAction(elementName, elementIndex, money);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MoneyQuantity moneyQuantity) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Quantity) moneyQuantity);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NamingSystem namingSystem) {
        return defaultAction(elementName, elementIndex, namingSystem);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Narrative narrative) {
        return defaultAction(elementName, elementIndex, narrative);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder nutritionOrder) {
        return defaultAction(elementName, elementIndex, nutritionOrder);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Observation observation) {
        return defaultAction(elementName, elementIndex, observation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ObservationDefinition observationDefinition) {
        return defaultAction(elementName, elementIndex, observationDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Oid oid) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Uri) oid);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition operationDefinition) {
        return defaultAction(elementName, elementIndex, operationDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationOutcome operationOutcome) {
        return defaultAction(elementName, elementIndex, operationOutcome);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Organization organization) {
        return defaultAction(elementName, elementIndex, organization);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OrganizationAffiliation organizationAffiliation) {
        return defaultAction(elementName, elementIndex, organizationAffiliation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ParameterDefinition parameterDefinition) {
        return defaultAction(elementName, elementIndex, parameterDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Parameters parameters) {
        return defaultAction(elementName, elementIndex, parameters);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Patient patient) {
        return defaultAction(elementName, elementIndex, patient);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentNotice paymentNotice) {
        return defaultAction(elementName, elementIndex, paymentNotice);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentReconciliation paymentReconciliation) {
        return defaultAction(elementName, elementIndex, paymentReconciliation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Period period) {
        return defaultAction(elementName, elementIndex, period);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Person person) {
        return defaultAction(elementName, elementIndex, person);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition planDefinition) {
        return defaultAction(elementName, elementIndex, planDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Population population) {
        return defaultAction(elementName, elementIndex, population);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Integer)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PositiveInt positiveInt) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Integer) positiveInt);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Practitioner practitioner) {
        return defaultAction(elementName, elementIndex, practitioner);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PractitionerRole practitionerRole) {
        return defaultAction(elementName, elementIndex, practitionerRole);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Procedure procedure) {
        return defaultAction(elementName, elementIndex, procedure);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ProdCharacteristic prodCharacteristic) {
        return defaultAction(elementName, elementIndex, prodCharacteristic);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ProductShelfLife productShelfLife) {
        return defaultAction(elementName, elementIndex, productShelfLife);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Provenance provenance) {
        return defaultAction(elementName, elementIndex, provenance);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Quantity quantity) {
        return defaultAction(elementName, elementIndex, quantity);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire questionnaire) {
        return defaultAction(elementName, elementIndex, questionnaire);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, QuestionnaireResponse questionnaireResponse) {
        return defaultAction(elementName, elementIndex, questionnaireResponse);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Range range) {
        return defaultAction(elementName, elementIndex, range);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Ratio ratio) {
        return defaultAction(elementName, elementIndex, ratio);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Reference reference) {
        return defaultAction(elementName, elementIndex, reference);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedArtifact relatedArtifact) {
        return defaultAction(elementName, elementIndex, relatedArtifact);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedPerson relatedPerson) {
        return defaultAction(elementName, elementIndex, relatedPerson);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RequestGroup requestGroup) {
        return defaultAction(elementName, elementIndex, requestGroup);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchDefinition researchDefinition) {
        return defaultAction(elementName, elementIndex, researchDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchElementDefinition researchElementDefinition) {
        return defaultAction(elementName, elementIndex, researchElementDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchStudy researchStudy) {
        return defaultAction(elementName, elementIndex, researchStudy);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchSubject researchSubject) {
        return defaultAction(elementName, elementIndex, researchSubject);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Resource resource) {
        return defaultAction(elementName, elementIndex, resource);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskAssessment riskAssessment) {
        return defaultAction(elementName, elementIndex, riskAssessment);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskEvidenceSynthesis riskEvidenceSynthesis) {
        return defaultAction(elementName, elementIndex, riskEvidenceSynthesis);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SampledData sampledData) {
        return defaultAction(elementName, elementIndex, sampledData);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Schedule schedule) {
        return defaultAction(elementName, elementIndex, schedule);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SearchParameter searchParameter) {
        return defaultAction(elementName, elementIndex, searchParameter);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ServiceRequest serviceRequest) {
        return defaultAction(elementName, elementIndex, serviceRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Signature signature) {
        return defaultAction(elementName, elementIndex, signature);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SimpleQuantity simpleQuantity) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Quantity) simpleQuantity);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Slot slot) {
        return defaultAction(elementName, elementIndex, slot);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Specimen specimen) {
        return defaultAction(elementName, elementIndex, specimen);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition specimenDefinition) {
        return defaultAction(elementName, elementIndex, specimenDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, String string) {
        return defaultAction(elementName, elementIndex, string);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition structureDefinition) {
        return defaultAction(elementName, elementIndex, structureDefinition);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap structureMap) {
        return defaultAction(elementName, elementIndex, structureMap);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Subscription subscription) {
        return defaultAction(elementName, elementIndex, subscription);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Substance substance) {
        return defaultAction(elementName, elementIndex, substance);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceAmount substanceAmount) {
        return defaultAction(elementName, elementIndex, substanceAmount);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceNucleicAcid substanceNucleicAcid) {
        return defaultAction(elementName, elementIndex, substanceNucleicAcid);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstancePolymer substancePolymer) {
        return defaultAction(elementName, elementIndex, substancePolymer);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceProtein substanceProtein) {
        return defaultAction(elementName, elementIndex, substanceProtein);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceReferenceInformation substanceReferenceInformation) {
        return defaultAction(elementName, elementIndex, substanceReferenceInformation);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSourceMaterial substanceSourceMaterial) {
        return defaultAction(elementName, elementIndex, substanceSourceMaterial);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceSpecification substanceSpecification) {
        return defaultAction(elementName, elementIndex, substanceSpecification);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyDelivery supplyDelivery) {
        return defaultAction(elementName, elementIndex, supplyDelivery);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyRequest supplyRequest) {
        return defaultAction(elementName, elementIndex, supplyRequest);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Task task) {
        return defaultAction(elementName, elementIndex, task);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities terminologyCapabilities) {
        return defaultAction(elementName, elementIndex, terminologyCapabilities);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport testReport) {
        return defaultAction(elementName, elementIndex, testReport);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript testScript) {
        return defaultAction(elementName, elementIndex, testScript);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Time time) {
        return defaultAction(elementName, elementIndex, time);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Timing timing) {
        return defaultAction(elementName, elementIndex, timing);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TriggerDefinition triggerDefinition) {
        return defaultAction(elementName, elementIndex, triggerDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Integer)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, UnsignedInt unsignedInt) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Integer) unsignedInt);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Uri uri) {
        return defaultAction(elementName, elementIndex, uri);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Url url) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Uri) url);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, UsageContext usageContext) {
        return defaultAction(elementName, elementIndex, usageContext);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, com.ibm.fhir.model.type.Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Uuid uuid) {
        return visit(elementName, elementIndex, (com.ibm.fhir.model.type.Uri) uuid);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet valueSet) {
        return defaultAction(elementName, elementIndex, valueSet);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VerificationResult verificationResult) {
        return defaultAction(elementName, elementIndex, verificationResult);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VisionPrescription visionPrescription) {
        return defaultAction(elementName, elementIndex, visionPrescription);
    }

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Xhtml xhtml) {
        return defaultAction(elementName, elementIndex, xhtml);
    }

    @Override
    public void visit(java.lang.String elementName, byte[] value) {
    }

    @Override
    public void visit(java.lang.String elementName, BigDecimal value) {
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Boolean value) {
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.Integer value) {
    }

    @Override
    public void visit(java.lang.String elementName, LocalDate value) {
    }

    @Override
    public void visit(java.lang.String elementName, LocalTime value) {
    }

    @Override
    public void visit(java.lang.String elementName, java.lang.String value) {
    }

    @Override
    public void visit(java.lang.String elementName, Year value) {
    }

    @Override
    public void visit(java.lang.String elementName, YearMonth value) {
    }

    @Override
    public void visit(java.lang.String elementName, ZonedDateTime value) {
    }
}
