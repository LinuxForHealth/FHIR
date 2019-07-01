/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Ratio;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ServiceRequestIntent;
import com.ibm.watsonhealth.fhir.model.type.ServiceRequestPriority;
import com.ibm.watsonhealth.fhir.model.type.ServiceRequestStatus;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
 * </p>
 */
@Constraint(
    id = "prr-1",
    level = "Rule",
    location = "(base)",
    description = "orderDetail SHALL only be present if code is present",
    expression = "orderDetail.empty() or code.exists()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class ServiceRequest extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> replaces;
    private final Identifier requisition;
    private final ServiceRequestStatus status;
    private final ServiceRequestIntent intent;
    private final List<CodeableConcept> category;
    private final ServiceRequestPriority priority;
    private final Boolean doNotPerform;
    private final CodeableConcept code;
    private final List<CodeableConcept> orderDetail;
    private final Element quantity;
    private final Reference subject;
    private final Reference encounter;
    private final Element occurrence;
    private final Element asNeeded;
    private final DateTime authoredOn;
    private final Reference requester;
    private final CodeableConcept performerType;
    private final List<Reference> performer;
    private final List<CodeableConcept> locationCode;
    private final List<Reference> locationReference;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Reference> insurance;
    private final List<Reference> supportingInfo;
    private final List<Reference> specimen;
    private final List<CodeableConcept> bodySite;
    private final List<Annotation> note;
    private final String patientInstruction;
    private final List<Reference> relevantHistory;

    private volatile int hashCode;

    private ServiceRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        replaces = Collections.unmodifiableList(builder.replaces);
        requisition = builder.requisition;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        doNotPerform = builder.doNotPerform;
        code = builder.code;
        orderDetail = Collections.unmodifiableList(builder.orderDetail);
        quantity = ValidationSupport.choiceElement(builder.quantity, "quantity", Quantity.class, Ratio.class, Range.class);
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        encounter = builder.encounter;
        occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
        asNeeded = ValidationSupport.choiceElement(builder.asNeeded, "asNeeded", Boolean.class, CodeableConcept.class);
        authoredOn = builder.authoredOn;
        requester = builder.requester;
        performerType = builder.performerType;
        performer = Collections.unmodifiableList(builder.performer);
        locationCode = Collections.unmodifiableList(builder.locationCode);
        locationReference = Collections.unmodifiableList(builder.locationReference);
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        insurance = Collections.unmodifiableList(builder.insurance);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        specimen = Collections.unmodifiableList(builder.specimen);
        bodySite = Collections.unmodifiableList(builder.bodySite);
        note = Collections.unmodifiableList(builder.note);
        patientInstruction = builder.patientInstruction;
        relevantHistory = Collections.unmodifiableList(builder.relevantHistory);
    }

    /**
     * <p>
     * Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
     * part by this ServiceRequest.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this ServiceRequest.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * Plan/proposal/order fulfilled by this request.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * The request takes the place of the referenced completed or terminated request(s).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReplaces() {
        return replaces;
    }

    /**
     * <p>
     * A shared identifier common to all service requests that were authorized more or less simultaneously by a single 
     * author, representing the composite or group identifier.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getRequisition() {
        return requisition;
    }

    /**
     * <p>
     * The status of the order.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ServiceRequestStatus}.
     */
    public ServiceRequestStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Whether the request is a proposal, plan, an original order or a reflex order.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ServiceRequestIntent}.
     */
    public ServiceRequestIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * A code that classifies the service for searching, sorting and display purposes (e.g. "Surgical Procedure").
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Indicates how quickly the ServiceRequest should be addressed with respect to other requests.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link ServiceRequestPriority}.
     */
    public ServiceRequestPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * Set this to true if the record is saying that the service/procedure should NOT be performed.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getDoNotPerform() {
        return doNotPerform;
    }

    /**
     * <p>
     * A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) 
     * that have been requested.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * <p>
     * Additional details and instructions about the how the services are to be delivered. For example, and order for a 
     * urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may 
     * require additional instructions specifying how the bandage should be applied.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getOrderDetail() {
        return orderDetail;
    }

    /**
     * <p>
     * An amount of service being requested which can be a quantity ( for example $1,500 home modification), a ratio ( for 
     * example, 20 half day visits per month), or a range (2.0 to 1.8 Gy per fraction).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * On whom or what the service is to be performed. This is usually a human patient, but can also be requested on animals, 
     * groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * <p>
     * An encounter that provides additional information about the healthcare context in which this request is made.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * <p>
     * The date/time at which the requested service should occur.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getOccurrence() {
        return occurrence;
    }

    /**
     * <p>
     * If a CodeableConcept is present, it indicates the pre-condition for performing the service. For example "pain", "on 
     * flare-up", etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getAsNeeded() {
        return asNeeded;
    }

    /**
     * <p>
     * When the request transitioned to being actionable.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getAuthoredOn() {
        return authoredOn;
    }

    /**
     * <p>
     * The individual who initiated the request and has responsibility for its activation.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getRequester() {
        return requester;
    }

    /**
     * <p>
     * Desired type of performer for doing the requested service.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getPerformerType() {
        return performerType;
    }

    /**
     * <p>
     * The desired performer for doing the requested service. For example, the surgeon, dermatopathologist, endoscopist, etc.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPerformer() {
        return performer;
    }

    /**
     * <p>
     * The preferred location(s) where the procedure should actually happen in coded or free text form. E.g. at home or 
     * nursing day care center.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getLocationCode() {
        return locationCode;
    }

    /**
     * <p>
     * A reference to the the preferred location(s) where the procedure should actually happen. E.g. at home or nursing day 
     * care center.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getLocationReference() {
        return locationReference;
    }

    /**
     * <p>
     * An explanation or justification for why this service is being requested in coded or textual form. This is often for 
     * billing purposes. May relate to the resources referred to in `supportingInfo`.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Indicates another resource that provides a justification for why this service is being requested. May relate to the 
     * resources referred to in `supportingInfo`.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering 
     * the requested service.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getInsurance() {
        return insurance;
    }

    /**
     * <p>
     * Additional clinical information about the patient or specimen that may influence the services or their 
     * interpretations. This information includes diagnosis, clinical findings and other observations. In laboratory ordering 
     * these are typically referred to as "ask at order entry questions (AOEs)". This includes observations explicitly 
     * requested by the producer (filler) to provide context or supporting information needed to complete the order. For 
     * example, reporting the amount of inspired oxygen for blood gas measurements.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * <p>
     * One or more specimens that the laboratory procedure will use.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getSpecimen() {
        return specimen;
    }

    /**
     * <p>
     * Anatomic location where the procedure should be performed. This is the target site.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getBodySite() {
        return bodySite;
    }

    /**
     * <p>
     * Any other notes and comments made about the service request. For example, internal billing notes.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    /**
     * <p>
     * Instructions in terms that are understood by the patient or consumer.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getPatientInstruction() {
        return patientInstruction;
    }

    /**
     * <p>
     * Key events in the history of the request.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRelevantHistory() {
        return relevantHistory;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(instantiatesCanonical, "instantiatesCanonical", visitor, Canonical.class);
                accept(instantiatesUri, "instantiatesUri", visitor, Uri.class);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(replaces, "replaces", visitor, Reference.class);
                accept(requisition, "requisition", visitor);
                accept(status, "status", visitor);
                accept(intent, "intent", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(priority, "priority", visitor);
                accept(doNotPerform, "doNotPerform", visitor);
                accept(code, "code", visitor);
                accept(orderDetail, "orderDetail", visitor, CodeableConcept.class);
                accept(quantity, "quantity", visitor, true);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(occurrence, "occurrence", visitor, true);
                accept(asNeeded, "asNeeded", visitor, true);
                accept(authoredOn, "authoredOn", visitor);
                accept(requester, "requester", visitor);
                accept(performerType, "performerType", visitor);
                accept(performer, "performer", visitor, Reference.class);
                accept(locationCode, "locationCode", visitor, CodeableConcept.class);
                accept(locationReference, "locationReference", visitor, Reference.class);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(insurance, "insurance", visitor, Reference.class);
                accept(supportingInfo, "supportingInfo", visitor, Reference.class);
                accept(specimen, "specimen", visitor, Reference.class);
                accept(bodySite, "bodySite", visitor, CodeableConcept.class);
                accept(note, "note", visitor, Annotation.class);
                accept(patientInstruction, "patientInstruction", visitor);
                accept(relevantHistory, "relevantHistory", visitor, Reference.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
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
        ServiceRequest other = (ServiceRequest) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(instantiatesCanonical, other.instantiatesCanonical) && 
            Objects.equals(instantiatesUri, other.instantiatesUri) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(replaces, other.replaces) && 
            Objects.equals(requisition, other.requisition) && 
            Objects.equals(status, other.status) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(category, other.category) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(doNotPerform, other.doNotPerform) && 
            Objects.equals(code, other.code) && 
            Objects.equals(orderDetail, other.orderDetail) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(occurrence, other.occurrence) && 
            Objects.equals(asNeeded, other.asNeeded) && 
            Objects.equals(authoredOn, other.authoredOn) && 
            Objects.equals(requester, other.requester) && 
            Objects.equals(performerType, other.performerType) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(locationCode, other.locationCode) && 
            Objects.equals(locationReference, other.locationReference) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(specimen, other.specimen) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(note, other.note) && 
            Objects.equals(patientInstruction, other.patientInstruction) && 
            Objects.equals(relevantHistory, other.relevantHistory);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                instantiatesCanonical, 
                instantiatesUri, 
                basedOn, 
                replaces, 
                requisition, 
                status, 
                intent, 
                category, 
                priority, 
                doNotPerform, 
                code, 
                orderDetail, 
                quantity, 
                subject, 
                encounter, 
                occurrence, 
                asNeeded, 
                authoredOn, 
                requester, 
                performerType, 
                performer, 
                locationCode, 
                locationReference, 
                reasonCode, 
                reasonReference, 
                insurance, 
                supportingInfo, 
                specimen, 
                bodySite, 
                note, 
                patientInstruction, 
                relevantHistory);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(status, intent, subject).from(this);
    }

    public Builder toBuilder(ServiceRequestStatus status, ServiceRequestIntent intent, Reference subject) {
        return new Builder(status, intent, subject).from(this);
    }

    public static Builder builder(ServiceRequestStatus status, ServiceRequestIntent intent, Reference subject) {
        return new Builder(status, intent, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final ServiceRequestStatus status;
        private final ServiceRequestIntent intent;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> replaces = new ArrayList<>();
        private Identifier requisition;
        private List<CodeableConcept> category = new ArrayList<>();
        private ServiceRequestPriority priority;
        private Boolean doNotPerform;
        private CodeableConcept code;
        private List<CodeableConcept> orderDetail = new ArrayList<>();
        private Element quantity;
        private Reference encounter;
        private Element occurrence;
        private Element asNeeded;
        private DateTime authoredOn;
        private Reference requester;
        private CodeableConcept performerType;
        private List<Reference> performer = new ArrayList<>();
        private List<CodeableConcept> locationCode = new ArrayList<>();
        private List<Reference> locationReference = new ArrayList<>();
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Reference> insurance = new ArrayList<>();
        private List<Reference> supportingInfo = new ArrayList<>();
        private List<Reference> specimen = new ArrayList<>();
        private List<CodeableConcept> bodySite = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private String patientInstruction;
        private List<Reference> relevantHistory = new ArrayList<>();

        private Builder(ServiceRequestStatus status, ServiceRequestIntent intent, Reference subject) {
            super();
            this.status = status;
            this.intent = intent;
            this.subject = subject;
        }

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * <p>
         * The base language in which the resource is written.
         * </p>
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.
         * </p>
         * 
         * @param identifier
         *     Identifiers assigned to this order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifiers assigned to this order instance by the orderer and/or the receiver and/or order fulfiller.
         * </p>
         * 
         * @param identifier
         *     Identifiers assigned to this order
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this ServiceRequest.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Canonical... instantiatesCanonical) {
            for (Canonical value : instantiatesCanonical) {
                this.instantiatesCanonical.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this ServiceRequest.
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical.addAll(instantiatesCanonical);
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this ServiceRequest.
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Uri... instantiatesUri) {
            for (Uri value : instantiatesUri) {
                this.instantiatesUri.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this ServiceRequest.
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri.addAll(instantiatesUri);
            return this;
        }

        /**
         * <p>
         * Plan/proposal/order fulfilled by this request.
         * </p>
         * 
         * @param basedOn
         *     What request fulfills
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Plan/proposal/order fulfilled by this request.
         * </p>
         * 
         * @param basedOn
         *     What request fulfills
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn.addAll(basedOn);
            return this;
        }

        /**
         * <p>
         * The request takes the place of the referenced completed or terminated request(s).
         * </p>
         * 
         * @param replaces
         *     What request replaces
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder replaces(Reference... replaces) {
            for (Reference value : replaces) {
                this.replaces.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The request takes the place of the referenced completed or terminated request(s).
         * </p>
         * 
         * @param replaces
         *     What request replaces
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder replaces(Collection<Reference> replaces) {
            this.replaces.addAll(replaces);
            return this;
        }

        /**
         * <p>
         * A shared identifier common to all service requests that were authorized more or less simultaneously by a single 
         * author, representing the composite or group identifier.
         * </p>
         * 
         * @param requisition
         *     Composite Request ID
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder requisition(Identifier requisition) {
            this.requisition = requisition;
            return this;
        }

        /**
         * <p>
         * A code that classifies the service for searching, sorting and display purposes (e.g. "Surgical Procedure").
         * </p>
         * 
         * @param category
         *     Classification of service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A code that classifies the service for searching, sorting and display purposes (e.g. "Surgical Procedure").
         * </p>
         * 
         * @param category
         *     Classification of service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category.addAll(category);
            return this;
        }

        /**
         * <p>
         * Indicates how quickly the ServiceRequest should be addressed with respect to other requests.
         * </p>
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(ServiceRequestPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * Set this to true if the record is saying that the service/procedure should NOT be performed.
         * </p>
         * 
         * @param doNotPerform
         *     True if service/procedure should not be performed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder doNotPerform(Boolean doNotPerform) {
            this.doNotPerform = doNotPerform;
            return this;
        }

        /**
         * <p>
         * A code that identifies a particular service (i.e., procedure, diagnostic investigation, or panel of investigations) 
         * that have been requested.
         * </p>
         * 
         * @param code
         *     What is being requested/ordered
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * Additional details and instructions about the how the services are to be delivered. For example, and order for a 
         * urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may 
         * require additional instructions specifying how the bandage should be applied.
         * </p>
         * 
         * @param orderDetail
         *     Additional order information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder orderDetail(CodeableConcept... orderDetail) {
            for (CodeableConcept value : orderDetail) {
                this.orderDetail.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional details and instructions about the how the services are to be delivered. For example, and order for a 
         * urinary catheter may have an order detail for an external or indwelling catheter, or an order for a bandage may 
         * require additional instructions specifying how the bandage should be applied.
         * </p>
         * 
         * @param orderDetail
         *     Additional order information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder orderDetail(Collection<CodeableConcept> orderDetail) {
            this.orderDetail.addAll(orderDetail);
            return this;
        }

        /**
         * <p>
         * An amount of service being requested which can be a quantity ( for example $1,500 home modification), a ratio ( for 
         * example, 20 half day visits per month), or a range (2.0 to 1.8 Gy per fraction).
         * </p>
         * 
         * @param quantity
         *     Service amount
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder quantity(Element quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * An encounter that provides additional information about the healthcare context in which this request is made.
         * </p>
         * 
         * @param encounter
         *     Encounter in which the request was created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The date/time at which the requested service should occur.
         * </p>
         * 
         * @param occurrence
         *     When service should occur
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder occurrence(Element occurrence) {
            this.occurrence = occurrence;
            return this;
        }

        /**
         * <p>
         * If a CodeableConcept is present, it indicates the pre-condition for performing the service. For example "pain", "on 
         * flare-up", etc.
         * </p>
         * 
         * @param asNeeded
         *     Preconditions for service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder asNeeded(Element asNeeded) {
            this.asNeeded = asNeeded;
            return this;
        }

        /**
         * <p>
         * When the request transitioned to being actionable.
         * </p>
         * 
         * @param authoredOn
         *     Date request signed
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder authoredOn(DateTime authoredOn) {
            this.authoredOn = authoredOn;
            return this;
        }

        /**
         * <p>
         * The individual who initiated the request and has responsibility for its activation.
         * </p>
         * 
         * @param requester
         *     Who/what is requesting service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder requester(Reference requester) {
            this.requester = requester;
            return this;
        }

        /**
         * <p>
         * Desired type of performer for doing the requested service.
         * </p>
         * 
         * @param performerType
         *     Performer role
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performerType(CodeableConcept performerType) {
            this.performerType = performerType;
            return this;
        }

        /**
         * <p>
         * The desired performer for doing the requested service. For example, the surgeon, dermatopathologist, endoscopist, etc.
         * </p>
         * 
         * @param performer
         *     Requested performer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Reference... performer) {
            for (Reference value : performer) {
                this.performer.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The desired performer for doing the requested service. For example, the surgeon, dermatopathologist, endoscopist, etc.
         * </p>
         * 
         * @param performer
         *     Requested performer
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Collection<Reference> performer) {
            this.performer.addAll(performer);
            return this;
        }

        /**
         * <p>
         * The preferred location(s) where the procedure should actually happen in coded or free text form. E.g. at home or 
         * nursing day care center.
         * </p>
         * 
         * @param locationCode
         *     Requested location
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder locationCode(CodeableConcept... locationCode) {
            for (CodeableConcept value : locationCode) {
                this.locationCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The preferred location(s) where the procedure should actually happen in coded or free text form. E.g. at home or 
         * nursing day care center.
         * </p>
         * 
         * @param locationCode
         *     Requested location
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder locationCode(Collection<CodeableConcept> locationCode) {
            this.locationCode.addAll(locationCode);
            return this;
        }

        /**
         * <p>
         * A reference to the the preferred location(s) where the procedure should actually happen. E.g. at home or nursing day 
         * care center.
         * </p>
         * 
         * @param locationReference
         *     Requested location
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder locationReference(Reference... locationReference) {
            for (Reference value : locationReference) {
                this.locationReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A reference to the the preferred location(s) where the procedure should actually happen. E.g. at home or nursing day 
         * care center.
         * </p>
         * 
         * @param locationReference
         *     Requested location
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder locationReference(Collection<Reference> locationReference) {
            this.locationReference.addAll(locationReference);
            return this;
        }

        /**
         * <p>
         * An explanation or justification for why this service is being requested in coded or textual form. This is often for 
         * billing purposes. May relate to the resources referred to in `supportingInfo`.
         * </p>
         * 
         * @param reasonCode
         *     Explanation/Justification for procedure or service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An explanation or justification for why this service is being requested in coded or textual form. This is often for 
         * billing purposes. May relate to the resources referred to in `supportingInfo`.
         * </p>
         * 
         * @param reasonCode
         *     Explanation/Justification for procedure or service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode.addAll(reasonCode);
            return this;
        }

        /**
         * <p>
         * Indicates another resource that provides a justification for why this service is being requested. May relate to the 
         * resources referred to in `supportingInfo`.
         * </p>
         * 
         * @param reasonReference
         *     Explanation/Justification for service or service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates another resource that provides a justification for why this service is being requested. May relate to the 
         * resources referred to in `supportingInfo`.
         * </p>
         * 
         * @param reasonReference
         *     Explanation/Justification for service or service
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference.addAll(reasonReference);
            return this;
        }

        /**
         * <p>
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering 
         * the requested service.
         * </p>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Reference... insurance) {
            for (Reference value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be needed for delivering 
         * the requested service.
         * </p>
         * 
         * @param insurance
         *     Associated insurance coverage
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder insurance(Collection<Reference> insurance) {
            this.insurance.addAll(insurance);
            return this;
        }

        /**
         * <p>
         * Additional clinical information about the patient or specimen that may influence the services or their 
         * interpretations. This information includes diagnosis, clinical findings and other observations. In laboratory ordering 
         * these are typically referred to as "ask at order entry questions (AOEs)". This includes observations explicitly 
         * requested by the producer (filler) to provide context or supporting information needed to complete the order. For 
         * example, reporting the amount of inspired oxygen for blood gas measurements.
         * </p>
         * 
         * @param supportingInfo
         *     Additional clinical information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Reference... supportingInfo) {
            for (Reference value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional clinical information about the patient or specimen that may influence the services or their 
         * interpretations. This information includes diagnosis, clinical findings and other observations. In laboratory ordering 
         * these are typically referred to as "ask at order entry questions (AOEs)". This includes observations explicitly 
         * requested by the producer (filler) to provide context or supporting information needed to complete the order. For 
         * example, reporting the amount of inspired oxygen for blood gas measurements.
         * </p>
         * 
         * @param supportingInfo
         *     Additional clinical information
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder supportingInfo(Collection<Reference> supportingInfo) {
            this.supportingInfo.addAll(supportingInfo);
            return this;
        }

        /**
         * <p>
         * One or more specimens that the laboratory procedure will use.
         * </p>
         * 
         * @param specimen
         *     Procedure Samples
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specimen(Reference... specimen) {
            for (Reference value : specimen) {
                this.specimen.add(value);
            }
            return this;
        }

        /**
         * <p>
         * One or more specimens that the laboratory procedure will use.
         * </p>
         * 
         * @param specimen
         *     Procedure Samples
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder specimen(Collection<Reference> specimen) {
            this.specimen.addAll(specimen);
            return this;
        }

        /**
         * <p>
         * Anatomic location where the procedure should be performed. This is the target site.
         * </p>
         * 
         * @param bodySite
         *     Location on Body
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodySite(CodeableConcept... bodySite) {
            for (CodeableConcept value : bodySite) {
                this.bodySite.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Anatomic location where the procedure should be performed. This is the target site.
         * </p>
         * 
         * @param bodySite
         *     Location on Body
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder bodySite(Collection<CodeableConcept> bodySite) {
            this.bodySite.addAll(bodySite);
            return this;
        }

        /**
         * <p>
         * Any other notes and comments made about the service request. For example, internal billing notes.
         * </p>
         * 
         * @param note
         *     Comments
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Any other notes and comments made about the service request. For example, internal billing notes.
         * </p>
         * 
         * @param note
         *     Comments
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        /**
         * <p>
         * Instructions in terms that are understood by the patient or consumer.
         * </p>
         * 
         * @param patientInstruction
         *     Patient or consumer-oriented instructions
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder patientInstruction(String patientInstruction) {
            this.patientInstruction = patientInstruction;
            return this;
        }

        /**
         * <p>
         * Key events in the history of the request.
         * </p>
         * 
         * @param relevantHistory
         *     Request provenance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relevantHistory(Reference... relevantHistory) {
            for (Reference value : relevantHistory) {
                this.relevantHistory.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Key events in the history of the request.
         * </p>
         * 
         * @param relevantHistory
         *     Request provenance
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder relevantHistory(Collection<Reference> relevantHistory) {
            this.relevantHistory.addAll(relevantHistory);
            return this;
        }

        @Override
        public ServiceRequest build() {
            return new ServiceRequest(this);
        }

        private Builder from(ServiceRequest serviceRequest) {
            id = serviceRequest.id;
            meta = serviceRequest.meta;
            implicitRules = serviceRequest.implicitRules;
            language = serviceRequest.language;
            text = serviceRequest.text;
            contained.addAll(serviceRequest.contained);
            extension.addAll(serviceRequest.extension);
            modifierExtension.addAll(serviceRequest.modifierExtension);
            identifier.addAll(serviceRequest.identifier);
            instantiatesCanonical.addAll(serviceRequest.instantiatesCanonical);
            instantiatesUri.addAll(serviceRequest.instantiatesUri);
            basedOn.addAll(serviceRequest.basedOn);
            replaces.addAll(serviceRequest.replaces);
            requisition = serviceRequest.requisition;
            category.addAll(serviceRequest.category);
            priority = serviceRequest.priority;
            doNotPerform = serviceRequest.doNotPerform;
            code = serviceRequest.code;
            orderDetail.addAll(serviceRequest.orderDetail);
            quantity = serviceRequest.quantity;
            encounter = serviceRequest.encounter;
            occurrence = serviceRequest.occurrence;
            asNeeded = serviceRequest.asNeeded;
            authoredOn = serviceRequest.authoredOn;
            requester = serviceRequest.requester;
            performerType = serviceRequest.performerType;
            performer.addAll(serviceRequest.performer);
            locationCode.addAll(serviceRequest.locationCode);
            locationReference.addAll(serviceRequest.locationReference);
            reasonCode.addAll(serviceRequest.reasonCode);
            reasonReference.addAll(serviceRequest.reasonReference);
            insurance.addAll(serviceRequest.insurance);
            supportingInfo.addAll(serviceRequest.supportingInfo);
            specimen.addAll(serviceRequest.specimen);
            bodySite.addAll(serviceRequest.bodySite);
            note.addAll(serviceRequest.note);
            patientInstruction = serviceRequest.patientInstruction;
            relevantHistory.addAll(serviceRequest.relevantHistory);
            return this;
        }
    }
}
