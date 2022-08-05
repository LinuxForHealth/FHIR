/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.visitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.resource.*;
import org.linuxforhealth.fhir.model.type.*;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.String;

/**
 * DefaultVisitor provides a default implementation of the Visitor interface which uses the
 * value of the passed {@code visitChildren} boolean to control whether or not to
 * visit the children of the Resource or Element being visited.
 * 
 * Subclasses can override the default behavior in a number of places, including:
 * <ul>
 * <li>preVisit methods to control whether a given Resource or Element gets visited
 * <li>visitStart methods to provide setup behavior prior to the visit
 * <li>supertype visit methods to perform some common action on all visited Resources and Elements
 * <li>subtype visit methods to perform unique behavior that varies by the type being visited
 * <li>visitEnd methods to provide initial cleanup behavior after a Resource or Element has been visited
 * <li>postVisit methods to provide final cleanup behavior after a Resource or Element has been visited
 * </ul>
 */
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class DefaultVisitor implements Visitor {
    protected boolean visitChildren;

    /**
     * Subclasses can override this method to provide a default action for all visit methods.
     * @return
     *     whether to visit the children of this resource; returns the value of the {@code visitChildren} boolean by default
     */
    public boolean visit(java.lang.String elementName, int elementIndex, Visitable visitable) {
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

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Account account) {
        return visit(elementName, elementIndex, (DomainResource) account);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ActivityDefinition activityDefinition) {
        return visit(elementName, elementIndex, (DomainResource) activityDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Address address) {
        return visit(elementName, elementIndex, (Element) address);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AdministrableProductDefinition administrableProductDefinition) {
        return visit(elementName, elementIndex, (DomainResource) administrableProductDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AdverseEvent adverseEvent) {
        return visit(elementName, elementIndex, (DomainResource) adverseEvent);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Age age) {
        return visit(elementName, elementIndex, (Quantity) age);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AllergyIntolerance allergyIntolerance) {
        return visit(elementName, elementIndex, (DomainResource) allergyIntolerance);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Annotation annotation) {
        return visit(elementName, elementIndex, (Element) annotation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Appointment appointment) {
        return visit(elementName, elementIndex, (DomainResource) appointment);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AppointmentResponse appointmentResponse) {
        return visit(elementName, elementIndex, (DomainResource) appointmentResponse);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Attachment attachment) {
        return visit(elementName, elementIndex, (Element) attachment);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, AuditEvent auditEvent) {
        return visit(elementName, elementIndex, (DomainResource) auditEvent);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BackboneElement backboneElement) {
        return visit(elementName, elementIndex, (Element) backboneElement);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Base64Binary base64Binary) {
        return visit(elementName, elementIndex, (Element) base64Binary);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Basic basic) {
        return visit(elementName, elementIndex, (DomainResource) basic);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Resource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Binary binary) {
        return visit(elementName, elementIndex, (Resource) binary);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BiologicallyDerivedProduct biologicallyDerivedProduct) {
        return visit(elementName, elementIndex, (DomainResource) biologicallyDerivedProduct);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, BodyStructure bodyStructure) {
        return visit(elementName, elementIndex, (DomainResource) bodyStructure);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Boolean _boolean) {
        return visit(elementName, elementIndex, (Element) _boolean);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Resource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Bundle bundle) {
        return visit(elementName, elementIndex, (Resource) bundle);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Canonical canonical) {
        return visit(elementName, elementIndex, (Uri) canonical);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CapabilityStatement capabilityStatement) {
        return visit(elementName, elementIndex, (DomainResource) capabilityStatement);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CarePlan carePlan) {
        return visit(elementName, elementIndex, (DomainResource) carePlan);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CareTeam careTeam) {
        return visit(elementName, elementIndex, (DomainResource) careTeam);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CatalogEntry catalogEntry) {
        return visit(elementName, elementIndex, (DomainResource) catalogEntry);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItem chargeItem) {
        return visit(elementName, elementIndex, (DomainResource) chargeItem);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ChargeItemDefinition chargeItemDefinition) {
        return visit(elementName, elementIndex, (DomainResource) chargeItemDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Citation citation) {
        return visit(elementName, elementIndex, (DomainResource) citation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Claim claim) {
        return visit(elementName, elementIndex, (DomainResource) claim);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClaimResponse claimResponse) {
        return visit(elementName, elementIndex, (DomainResource) claimResponse);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClinicalImpression clinicalImpression) {
        return visit(elementName, elementIndex, (DomainResource) clinicalImpression);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ClinicalUseDefinition clinicalUseDefinition) {
        return visit(elementName, elementIndex, (DomainResource) clinicalUseDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, String)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Code code) {
        return visit(elementName, elementIndex, (String) code);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeSystem codeSystem) {
        return visit(elementName, elementIndex, (DomainResource) codeSystem);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeableConcept codeableConcept) {
        return visit(elementName, elementIndex, (Element) codeableConcept);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CodeableReference codeableReference) {
        return visit(elementName, elementIndex, (Element) codeableReference);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coding coding) {
        return visit(elementName, elementIndex, (Element) coding);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Communication communication) {
        return visit(elementName, elementIndex, (DomainResource) communication);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CommunicationRequest communicationRequest) {
        return visit(elementName, elementIndex, (DomainResource) communicationRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CompartmentDefinition compartmentDefinition) {
        return visit(elementName, elementIndex, (DomainResource) compartmentDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Composition composition) {
        return visit(elementName, elementIndex, (DomainResource) composition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ConceptMap conceptMap) {
        return visit(elementName, elementIndex, (DomainResource) conceptMap);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Condition condition) {
        return visit(elementName, elementIndex, (DomainResource) condition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Consent consent) {
        return visit(elementName, elementIndex, (DomainResource) consent);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactDetail contactDetail) {
        return visit(elementName, elementIndex, (Element) contactDetail);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ContactPoint contactPoint) {
        return visit(elementName, elementIndex, (Element) contactPoint);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contract contract) {
        return visit(elementName, elementIndex, (DomainResource) contract);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Contributor contributor) {
        return visit(elementName, elementIndex, (Element) contributor);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Count count) {
        return visit(elementName, elementIndex, (Quantity) count);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Coverage coverage) {
        return visit(elementName, elementIndex, (DomainResource) coverage);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityRequest coverageEligibilityRequest) {
        return visit(elementName, elementIndex, (DomainResource) coverageEligibilityRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, CoverageEligibilityResponse coverageEligibilityResponse) {
        return visit(elementName, elementIndex, (DomainResource) coverageEligibilityResponse);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DataRequirement dataRequirement) {
        return visit(elementName, elementIndex, (Element) dataRequirement);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Date date) {
        return visit(elementName, elementIndex, (Element) date);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DateTime dateTime) {
        return visit(elementName, elementIndex, (Element) dateTime);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Decimal decimal) {
        return visit(elementName, elementIndex, (Element) decimal);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DetectedIssue detectedIssue) {
        return visit(elementName, elementIndex, (DomainResource) detectedIssue);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Device device) {
        return visit(elementName, elementIndex, (DomainResource) device);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceDefinition deviceDefinition) {
        return visit(elementName, elementIndex, (DomainResource) deviceDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceMetric deviceMetric) {
        return visit(elementName, elementIndex, (DomainResource) deviceMetric);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceRequest deviceRequest) {
        return visit(elementName, elementIndex, (DomainResource) deviceRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DeviceUseStatement deviceUseStatement) {
        return visit(elementName, elementIndex, (DomainResource) deviceUseStatement);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DiagnosticReport diagnosticReport) {
        return visit(elementName, elementIndex, (DomainResource) diagnosticReport);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Distance distance) {
        return visit(elementName, elementIndex, (Quantity) distance);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentManifest documentManifest) {
        return visit(elementName, elementIndex, (DomainResource) documentManifest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DocumentReference documentReference) {
        return visit(elementName, elementIndex, (DomainResource) documentReference);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Resource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, DomainResource domainResource) {
        return visit(elementName, elementIndex, (Resource) domainResource);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Dosage dosage) {
        return visit(elementName, elementIndex, (BackboneElement) dosage);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Duration duration) {
        return visit(elementName, elementIndex, (Quantity) duration);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Visitable)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Element element) {
        return visit(elementName, elementIndex, (Visitable) element);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ElementDefinition elementDefinition) {
        return visit(elementName, elementIndex, (BackboneElement) elementDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Encounter encounter) {
        return visit(elementName, elementIndex, (DomainResource) encounter);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Endpoint endpoint) {
        return visit(elementName, elementIndex, (DomainResource) endpoint);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EnrollmentRequest enrollmentRequest) {
        return visit(elementName, elementIndex, (DomainResource) enrollmentRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EnrollmentResponse enrollmentResponse) {
        return visit(elementName, elementIndex, (DomainResource) enrollmentResponse);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EpisodeOfCare episodeOfCare) {
        return visit(elementName, elementIndex, (DomainResource) episodeOfCare);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EventDefinition eventDefinition) {
        return visit(elementName, elementIndex, (DomainResource) eventDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Evidence evidence) {
        return visit(elementName, elementIndex, (DomainResource) evidence);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EvidenceReport evidenceReport) {
        return visit(elementName, elementIndex, (DomainResource) evidenceReport);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, EvidenceVariable evidenceVariable) {
        return visit(elementName, elementIndex, (DomainResource) evidenceVariable);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExampleScenario exampleScenario) {
        return visit(elementName, elementIndex, (DomainResource) exampleScenario);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ExplanationOfBenefit explanationOfBenefit) {
        return visit(elementName, elementIndex, (DomainResource) explanationOfBenefit);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Expression expression) {
        return visit(elementName, elementIndex, (Element) expression);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Extension extension) {
        return visit(elementName, elementIndex, (Element) extension);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, FamilyMemberHistory familyMemberHistory) {
        return visit(elementName, elementIndex, (DomainResource) familyMemberHistory);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Flag flag) {
        return visit(elementName, elementIndex, (DomainResource) flag);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Goal goal) {
        return visit(elementName, elementIndex, (DomainResource) goal);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GraphDefinition graphDefinition) {
        return visit(elementName, elementIndex, (DomainResource) graphDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Group group) {
        return visit(elementName, elementIndex, (DomainResource) group);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, GuidanceResponse guidanceResponse) {
        return visit(elementName, elementIndex, (DomainResource) guidanceResponse);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HealthcareService healthcareService) {
        return visit(elementName, elementIndex, (DomainResource) healthcareService);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, HumanName humanName) {
        return visit(elementName, elementIndex, (Element) humanName);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, String)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Id id) {
        return visit(elementName, elementIndex, (String) id);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Identifier identifier) {
        return visit(elementName, elementIndex, (Element) identifier);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImagingStudy imagingStudy) {
        return visit(elementName, elementIndex, (DomainResource) imagingStudy);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Immunization immunization) {
        return visit(elementName, elementIndex, (DomainResource) immunization);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationEvaluation immunizationEvaluation) {
        return visit(elementName, elementIndex, (DomainResource) immunizationEvaluation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImmunizationRecommendation immunizationRecommendation) {
        return visit(elementName, elementIndex, (DomainResource) immunizationRecommendation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ImplementationGuide implementationGuide) {
        return visit(elementName, elementIndex, (DomainResource) implementationGuide);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Ingredient ingredient) {
        return visit(elementName, elementIndex, (DomainResource) ingredient);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Instant instant) {
        return visit(elementName, elementIndex, (Element) instant);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, InsurancePlan insurancePlan) {
        return visit(elementName, elementIndex, (DomainResource) insurancePlan);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Integer integer) {
        return visit(elementName, elementIndex, (Element) integer);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Invoice invoice) {
        return visit(elementName, elementIndex, (DomainResource) invoice);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Library library) {
        return visit(elementName, elementIndex, (DomainResource) library);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Linkage linkage) {
        return visit(elementName, elementIndex, (DomainResource) linkage);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, List list) {
        return visit(elementName, elementIndex, (DomainResource) list);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location location) {
        return visit(elementName, elementIndex, (DomainResource) location);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ManufacturedItemDefinition manufacturedItemDefinition) {
        return visit(elementName, elementIndex, (DomainResource) manufacturedItemDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, String)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Markdown markdown) {
        return visit(elementName, elementIndex, (String) markdown);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MarketingStatus marketingStatus) {
        return visit(elementName, elementIndex, (BackboneElement) marketingStatus);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Measure measure) {
        return visit(elementName, elementIndex, (DomainResource) measure);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MeasureReport measureReport) {
        return visit(elementName, elementIndex, (DomainResource) measureReport);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Media media) {
        return visit(elementName, elementIndex, (DomainResource) media);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Medication medication) {
        return visit(elementName, elementIndex, (DomainResource) medication);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationAdministration medicationAdministration) {
        return visit(elementName, elementIndex, (DomainResource) medicationAdministration);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationDispense medicationDispense) {
        return visit(elementName, elementIndex, (DomainResource) medicationDispense);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationKnowledge medicationKnowledge) {
        return visit(elementName, elementIndex, (DomainResource) medicationKnowledge);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationRequest medicationRequest) {
        return visit(elementName, elementIndex, (DomainResource) medicationRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicationStatement medicationStatement) {
        return visit(elementName, elementIndex, (DomainResource) medicationStatement);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MedicinalProductDefinition medicinalProductDefinition) {
        return visit(elementName, elementIndex, (DomainResource) medicinalProductDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageDefinition messageDefinition) {
        return visit(elementName, elementIndex, (DomainResource) messageDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MessageHeader messageHeader) {
        return visit(elementName, elementIndex, (DomainResource) messageHeader);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Meta meta) {
        return visit(elementName, elementIndex, (Element) meta);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MolecularSequence molecularSequence) {
        return visit(elementName, elementIndex, (DomainResource) molecularSequence);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Money money) {
        return visit(elementName, elementIndex, (Element) money);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, MoneyQuantity moneyQuantity) {
        return visit(elementName, elementIndex, (Quantity) moneyQuantity);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NamingSystem namingSystem) {
        return visit(elementName, elementIndex, (DomainResource) namingSystem);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Narrative narrative) {
        return visit(elementName, elementIndex, (Element) narrative);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionOrder nutritionOrder) {
        return visit(elementName, elementIndex, (DomainResource) nutritionOrder);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, NutritionProduct nutritionProduct) {
        return visit(elementName, elementIndex, (DomainResource) nutritionProduct);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Observation observation) {
        return visit(elementName, elementIndex, (DomainResource) observation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ObservationDefinition observationDefinition) {
        return visit(elementName, elementIndex, (DomainResource) observationDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Oid oid) {
        return visit(elementName, elementIndex, (Uri) oid);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationDefinition operationDefinition) {
        return visit(elementName, elementIndex, (DomainResource) operationDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OperationOutcome operationOutcome) {
        return visit(elementName, elementIndex, (DomainResource) operationOutcome);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Organization organization) {
        return visit(elementName, elementIndex, (DomainResource) organization);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, OrganizationAffiliation organizationAffiliation) {
        return visit(elementName, elementIndex, (DomainResource) organizationAffiliation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PackagedProductDefinition packagedProductDefinition) {
        return visit(elementName, elementIndex, (DomainResource) packagedProductDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ParameterDefinition parameterDefinition) {
        return visit(elementName, elementIndex, (Element) parameterDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Resource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Parameters parameters) {
        return visit(elementName, elementIndex, (Resource) parameters);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Patient patient) {
        return visit(elementName, elementIndex, (DomainResource) patient);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentNotice paymentNotice) {
        return visit(elementName, elementIndex, (DomainResource) paymentNotice);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PaymentReconciliation paymentReconciliation) {
        return visit(elementName, elementIndex, (DomainResource) paymentReconciliation);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Period period) {
        return visit(elementName, elementIndex, (Element) period);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Person person) {
        return visit(elementName, elementIndex, (DomainResource) person);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PlanDefinition planDefinition) {
        return visit(elementName, elementIndex, (DomainResource) planDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Population population) {
        return visit(elementName, elementIndex, (BackboneElement) population);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Integer)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PositiveInt positiveInt) {
        return visit(elementName, elementIndex, (Integer) positiveInt);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Practitioner practitioner) {
        return visit(elementName, elementIndex, (DomainResource) practitioner);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, PractitionerRole practitionerRole) {
        return visit(elementName, elementIndex, (DomainResource) practitionerRole);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Procedure procedure) {
        return visit(elementName, elementIndex, (DomainResource) procedure);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ProdCharacteristic prodCharacteristic) {
        return visit(elementName, elementIndex, (BackboneElement) prodCharacteristic);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ProductShelfLife productShelfLife) {
        return visit(elementName, elementIndex, (BackboneElement) productShelfLife);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Provenance provenance) {
        return visit(elementName, elementIndex, (DomainResource) provenance);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Quantity quantity) {
        return visit(elementName, elementIndex, (Element) quantity);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Questionnaire questionnaire) {
        return visit(elementName, elementIndex, (DomainResource) questionnaire);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, QuestionnaireResponse questionnaireResponse) {
        return visit(elementName, elementIndex, (DomainResource) questionnaireResponse);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Range range) {
        return visit(elementName, elementIndex, (Element) range);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Ratio ratio) {
        return visit(elementName, elementIndex, (Element) ratio);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RatioRange ratioRange) {
        return visit(elementName, elementIndex, (Element) ratioRange);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Reference reference) {
        return visit(elementName, elementIndex, (Element) reference);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RegulatedAuthorization regulatedAuthorization) {
        return visit(elementName, elementIndex, (DomainResource) regulatedAuthorization);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedArtifact relatedArtifact) {
        return visit(elementName, elementIndex, (Element) relatedArtifact);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RelatedPerson relatedPerson) {
        return visit(elementName, elementIndex, (DomainResource) relatedPerson);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RequestGroup requestGroup) {
        return visit(elementName, elementIndex, (DomainResource) requestGroup);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchDefinition researchDefinition) {
        return visit(elementName, elementIndex, (DomainResource) researchDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchElementDefinition researchElementDefinition) {
        return visit(elementName, elementIndex, (DomainResource) researchElementDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchStudy researchStudy) {
        return visit(elementName, elementIndex, (DomainResource) researchStudy);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ResearchSubject researchSubject) {
        return visit(elementName, elementIndex, (DomainResource) researchSubject);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Visitable)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Resource resource) {
        return visit(elementName, elementIndex, (Visitable) resource);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, RiskAssessment riskAssessment) {
        return visit(elementName, elementIndex, (DomainResource) riskAssessment);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SampledData sampledData) {
        return visit(elementName, elementIndex, (Element) sampledData);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Schedule schedule) {
        return visit(elementName, elementIndex, (DomainResource) schedule);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SearchParameter searchParameter) {
        return visit(elementName, elementIndex, (DomainResource) searchParameter);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ServiceRequest serviceRequest) {
        return visit(elementName, elementIndex, (DomainResource) serviceRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Signature signature) {
        return visit(elementName, elementIndex, (Element) signature);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Quantity)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SimpleQuantity simpleQuantity) {
        return visit(elementName, elementIndex, (Quantity) simpleQuantity);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Slot slot) {
        return visit(elementName, elementIndex, (DomainResource) slot);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Specimen specimen) {
        return visit(elementName, elementIndex, (DomainResource) specimen);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SpecimenDefinition specimenDefinition) {
        return visit(elementName, elementIndex, (DomainResource) specimenDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, String string) {
        return visit(elementName, elementIndex, (Element) string);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureDefinition structureDefinition) {
        return visit(elementName, elementIndex, (DomainResource) structureDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, StructureMap structureMap) {
        return visit(elementName, elementIndex, (DomainResource) structureMap);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Subscription subscription) {
        return visit(elementName, elementIndex, (DomainResource) subscription);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubscriptionStatus subscriptionStatus) {
        return visit(elementName, elementIndex, (DomainResource) subscriptionStatus);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubscriptionTopic subscriptionTopic) {
        return visit(elementName, elementIndex, (DomainResource) subscriptionTopic);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Substance substance) {
        return visit(elementName, elementIndex, (DomainResource) substance);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SubstanceDefinition substanceDefinition) {
        return visit(elementName, elementIndex, (DomainResource) substanceDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyDelivery supplyDelivery) {
        return visit(elementName, elementIndex, (DomainResource) supplyDelivery);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, SupplyRequest supplyRequest) {
        return visit(elementName, elementIndex, (DomainResource) supplyRequest);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Task task) {
        return visit(elementName, elementIndex, (DomainResource) task);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TerminologyCapabilities terminologyCapabilities) {
        return visit(elementName, elementIndex, (DomainResource) terminologyCapabilities);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestReport testReport) {
        return visit(elementName, elementIndex, (DomainResource) testReport);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TestScript testScript) {
        return visit(elementName, elementIndex, (DomainResource) testScript);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Time time) {
        return visit(elementName, elementIndex, (Element) time);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, BackboneElement)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Timing timing) {
        return visit(elementName, elementIndex, (BackboneElement) timing);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, TriggerDefinition triggerDefinition) {
        return visit(elementName, elementIndex, (Element) triggerDefinition);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Integer)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, UnsignedInt unsignedInt) {
        return visit(elementName, elementIndex, (Integer) unsignedInt);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Uri uri) {
        return visit(elementName, elementIndex, (Element) uri);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Url url) {
        return visit(elementName, elementIndex, (Uri) url);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, UsageContext usageContext) {
        return visit(elementName, elementIndex, (Element) usageContext);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Uri)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Uuid uuid) {
        return visit(elementName, elementIndex, (Uri) uuid);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, ValueSet valueSet) {
        return visit(elementName, elementIndex, (DomainResource) valueSet);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VerificationResult verificationResult) {
        return visit(elementName, elementIndex, (DomainResource) verificationResult);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, DomainResource)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, VisionPrescription visionPrescription) {
        return visit(elementName, elementIndex, (DomainResource) visionPrescription);
    }

    /**
     * Delegates to {@link #visit(elementName, elementIndex, Element)}
     * @return
     *     {@inheritDoc}
     */
    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Xhtml xhtml) {
        return visit(elementName, elementIndex, (Element) xhtml);
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

    @Override
    public boolean visit(java.lang.String elementName, int elementIndex, Location.Position position) {
        return visit(elementName, elementIndex, (BackboneElement) position);
    }
}
