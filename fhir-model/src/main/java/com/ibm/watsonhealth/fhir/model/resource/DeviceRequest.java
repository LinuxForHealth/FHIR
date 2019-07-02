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

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.DeviceRequestStatus;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.RequestIntent;
import com.ibm.watsonhealth.fhir.model.type.RequestPriority;
import com.ibm.watsonhealth.fhir.model.type.Timing;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external 
 * assistive device, such as a walker.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class DeviceRequest extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> priorRequest;
    private final Identifier groupIdentifier;
    private final DeviceRequestStatus status;
    private final RequestIntent intent;
    private final RequestPriority priority;
    private final Element code;
    private final List<Parameter> parameter;
    private final Reference subject;
    private final Reference encounter;
    private final Element occurrence;
    private final DateTime authoredOn;
    private final Reference requester;
    private final CodeableConcept performerType;
    private final Reference performer;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Reference> insurance;
    private final List<Reference> supportingInfo;
    private final List<Annotation> note;
    private final List<Reference> relevantHistory;

    private volatile int hashCode;

    private DeviceRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        priorRequest = Collections.unmodifiableList(builder.priorRequest);
        groupIdentifier = builder.groupIdentifier;
        status = builder.status;
        intent = ValidationSupport.requireNonNull(builder.intent, "intent");
        priority = builder.priority;
        code = ValidationSupport.requireChoiceElement(builder.code, "code", Reference.class, CodeableConcept.class);
        parameter = Collections.unmodifiableList(builder.parameter);
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        encounter = builder.encounter;
        occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class, Timing.class);
        authoredOn = builder.authoredOn;
        requester = builder.requester;
        performerType = builder.performerType;
        performer = builder.performer;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        insurance = Collections.unmodifiableList(builder.insurance);
        supportingInfo = Collections.unmodifiableList(builder.supportingInfo);
        note = Collections.unmodifiableList(builder.note);
        relevantHistory = Collections.unmodifiableList(builder.relevantHistory);
    }

    /**
     * <p>
     * Identifiers assigned to this order by the orderer or by the receiver.
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
     * part by this DeviceRequest.
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
     * whole or in part by this DeviceRequest.
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
    public List<Reference> getPriorRequest() {
        return priorRequest;
    }

    /**
     * <p>
     * Composite request this is part of.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Identifier}.
     */
    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    /**
     * <p>
     * The status of the request.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DeviceRequestStatus}.
     */
    public DeviceRequestStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Whether the request is a proposal, plan, an original order or a reflex order.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestIntent}.
     */
    public RequestIntent getIntent() {
        return intent;
    }

    /**
     * <p>
     * Indicates how quickly the {{title}} should be addressed with respect to other requests.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link RequestPriority}.
     */
    public RequestPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * The details of the device to be used.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getCode() {
        return code;
    }

    /**
     * <p>
     * Specific parameters for the ordered item. For example, the prism value for lenses.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Parameter}.
     */
    public List<Parameter> getParameter() {
        return parameter;
    }

    /**
     * <p>
     * The patient who will use the device.
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
     * An encounter that provides additional context in which this request is made.
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
     * The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. 
     * "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 
     * Oct 2013 and 1 Nov 2013".
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
     * Desired type of performer for doing the diagnostic testing.
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
     * The desired performer for doing the diagnostic testing.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getPerformer() {
        return performer;
    }

    /**
     * <p>
     * Reason or justification for the use of this device.
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
     * Reason or justification for the use of this device.
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
     * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
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
     * Additional clinical information about the patient that may influence the request fulfilment. For example, this may 
     * include where on the subject's body the device will be used (i.e. the target site).
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
     * Details about this request that were not represented at all or sufficiently in one of the attributes provided in a 
     * class. These may include for example a comment, an instruction, or a note associated with the statement.
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
                accept(priorRequest, "priorRequest", visitor, Reference.class);
                accept(groupIdentifier, "groupIdentifier", visitor);
                accept(status, "status", visitor);
                accept(intent, "intent", visitor);
                accept(priority, "priority", visitor);
                accept(code, "code", visitor);
                accept(parameter, "parameter", visitor, Parameter.class);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(occurrence, "occurrence", visitor);
                accept(authoredOn, "authoredOn", visitor);
                accept(requester, "requester", visitor);
                accept(performerType, "performerType", visitor);
                accept(performer, "performer", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(insurance, "insurance", visitor, Reference.class);
                accept(supportingInfo, "supportingInfo", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
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
        DeviceRequest other = (DeviceRequest) obj;
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
            Objects.equals(priorRequest, other.priorRequest) && 
            Objects.equals(groupIdentifier, other.groupIdentifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(intent, other.intent) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(code, other.code) && 
            Objects.equals(parameter, other.parameter) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(occurrence, other.occurrence) && 
            Objects.equals(authoredOn, other.authoredOn) && 
            Objects.equals(requester, other.requester) && 
            Objects.equals(performerType, other.performerType) && 
            Objects.equals(performer, other.performer) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(note, other.note) && 
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
                priorRequest, 
                groupIdentifier, 
                status, 
                intent, 
                priority, 
                code, 
                parameter, 
                subject, 
                encounter, 
                occurrence, 
                authoredOn, 
                requester, 
                performerType, 
                performer, 
                reasonCode, 
                reasonReference, 
                insurance, 
                supportingInfo, 
                note, 
                relevantHistory);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(intent, code, subject).from(this);
    }

    public Builder toBuilder(RequestIntent intent, Element code, Reference subject) {
        return new Builder(intent, code, subject).from(this);
    }

    public static Builder builder(RequestIntent intent, Element code, Reference subject) {
        return new Builder(intent, code, subject);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final RequestIntent intent;
        private final Element code;
        private final Reference subject;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> priorRequest = new ArrayList<>();
        private Identifier groupIdentifier;
        private DeviceRequestStatus status;
        private RequestPriority priority;
        private List<Parameter> parameter = new ArrayList<>();
        private Reference encounter;
        private Element occurrence;
        private DateTime authoredOn;
        private Reference requester;
        private CodeableConcept performerType;
        private Reference performer;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Reference> insurance = new ArrayList<>();
        private List<Reference> supportingInfo = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();
        private List<Reference> relevantHistory = new ArrayList<>();

        private Builder(RequestIntent intent, Element code, Reference subject) {
            super();
            this.intent = intent;
            this.code = code;
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
         * Identifiers assigned to this order by the orderer or by the receiver.
         * </p>
         * 
         * @param identifier
         *     External Request identifier
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
         * Identifiers assigned to this order by the orderer or by the receiver.
         * </p>
         * 
         * @param identifier
         *     External Request identifier
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
         * part by this DeviceRequest.
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
         * part by this DeviceRequest.
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
         * whole or in part by this DeviceRequest.
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
         * whole or in part by this DeviceRequest.
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
         * @param priorRequest
         *     What request replaces
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priorRequest(Reference... priorRequest) {
            for (Reference value : priorRequest) {
                this.priorRequest.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The request takes the place of the referenced completed or terminated request(s).
         * </p>
         * 
         * @param priorRequest
         *     What request replaces
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priorRequest(Collection<Reference> priorRequest) {
            this.priorRequest.addAll(priorRequest);
            return this;
        }

        /**
         * <p>
         * Composite request this is part of.
         * </p>
         * 
         * @param groupIdentifier
         *     Identifier of composite request
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder groupIdentifier(Identifier groupIdentifier) {
            this.groupIdentifier = groupIdentifier;
            return this;
        }

        /**
         * <p>
         * The status of the request.
         * </p>
         * 
         * @param status
         *     draft | active | suspended | completed | entered-in-error | cancelled
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder status(DeviceRequestStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Indicates how quickly the {{title}} should be addressed with respect to other requests.
         * </p>
         * 
         * @param priority
         *     Indicates how quickly the {{title}} should be addressed with respect to other requests
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(RequestPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * Specific parameters for the ordered item. For example, the prism value for lenses.
         * </p>
         * 
         * @param parameter
         *     Device details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parameter(Parameter... parameter) {
            for (Parameter value : parameter) {
                this.parameter.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Specific parameters for the ordered item. For example, the prism value for lenses.
         * </p>
         * 
         * @param parameter
         *     Device details
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder parameter(Collection<Parameter> parameter) {
            this.parameter.addAll(parameter);
            return this;
        }

        /**
         * <p>
         * An encounter that provides additional context in which this request is made.
         * </p>
         * 
         * @param encounter
         *     Encounter motivating request
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
         * The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. 
         * "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 
         * Oct 2013 and 1 Nov 2013".
         * </p>
         * 
         * @param occurrence
         *     Desired time or schedule for use
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
         * When the request transitioned to being actionable.
         * </p>
         * 
         * @param authoredOn
         *     When recorded
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
         *     Who/what is requesting diagnostics
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
         * Desired type of performer for doing the diagnostic testing.
         * </p>
         * 
         * @param performerType
         *     Filler role
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
         * The desired performer for doing the diagnostic testing.
         * </p>
         * 
         * @param performer
         *     Requested Filler
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder performer(Reference performer) {
            this.performer = performer;
            return this;
        }

        /**
         * <p>
         * Reason or justification for the use of this device.
         * </p>
         * 
         * @param reasonCode
         *     Coded Reason for request
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
         * Reason or justification for the use of this device.
         * </p>
         * 
         * @param reasonCode
         *     Coded Reason for request
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
         * Reason or justification for the use of this device.
         * </p>
         * 
         * @param reasonReference
         *     Linked Reason for request
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
         * Reason or justification for the use of this device.
         * </p>
         * 
         * @param reasonReference
         *     Linked Reason for request
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
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
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
         * Insurance plans, coverage extensions, pre-authorizations and/or pre-determinations that may be required for delivering 
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
         * Additional clinical information about the patient that may influence the request fulfilment. For example, this may 
         * include where on the subject's body the device will be used (i.e. the target site).
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
         * Additional clinical information about the patient that may influence the request fulfilment. For example, this may 
         * include where on the subject's body the device will be used (i.e. the target site).
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
         * Details about this request that were not represented at all or sufficiently in one of the attributes provided in a 
         * class. These may include for example a comment, an instruction, or a note associated with the statement.
         * </p>
         * 
         * @param note
         *     Notes or comments
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
         * Details about this request that were not represented at all or sufficiently in one of the attributes provided in a 
         * class. These may include for example a comment, an instruction, or a note associated with the statement.
         * </p>
         * 
         * @param note
         *     Notes or comments
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
        public DeviceRequest build() {
            return new DeviceRequest(this);
        }

        private Builder from(DeviceRequest deviceRequest) {
            id = deviceRequest.id;
            meta = deviceRequest.meta;
            implicitRules = deviceRequest.implicitRules;
            language = deviceRequest.language;
            text = deviceRequest.text;
            contained.addAll(deviceRequest.contained);
            extension.addAll(deviceRequest.extension);
            modifierExtension.addAll(deviceRequest.modifierExtension);
            identifier.addAll(deviceRequest.identifier);
            instantiatesCanonical.addAll(deviceRequest.instantiatesCanonical);
            instantiatesUri.addAll(deviceRequest.instantiatesUri);
            basedOn.addAll(deviceRequest.basedOn);
            priorRequest.addAll(deviceRequest.priorRequest);
            groupIdentifier = deviceRequest.groupIdentifier;
            status = deviceRequest.status;
            priority = deviceRequest.priority;
            parameter.addAll(deviceRequest.parameter);
            encounter = deviceRequest.encounter;
            occurrence = deviceRequest.occurrence;
            authoredOn = deviceRequest.authoredOn;
            requester = deviceRequest.requester;
            performerType = deviceRequest.performerType;
            performer = deviceRequest.performer;
            reasonCode.addAll(deviceRequest.reasonCode);
            reasonReference.addAll(deviceRequest.reasonReference);
            insurance.addAll(deviceRequest.insurance);
            supportingInfo.addAll(deviceRequest.supportingInfo);
            note.addAll(deviceRequest.note);
            relevantHistory.addAll(deviceRequest.relevantHistory);
            return this;
        }
    }

    /**
     * <p>
     * Specific parameters for the ordered item. For example, the prism value for lenses.
     * </p>
     */
    public static class Parameter extends BackboneElement {
        private final CodeableConcept code;
        private final Element value;

        private volatile int hashCode;

        private Parameter(Builder builder) {
            super(builder);
            code = builder.code;
            value = ValidationSupport.choiceElement(builder.value, "value", CodeableConcept.class, Quantity.class, Range.class, Boolean.class);
        }

        /**
         * <p>
         * A code or string that identifies the device detail being asserted.
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
         * The value of the device detail.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(value, "value", visitor);
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
            Parameter other = (Parameter) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    value);
                hashCode = result;
            }
            return result;
        }

        @Override
        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
            private CodeableConcept code;
            private Element value;

            private Builder() {
                super();
            }

            /**
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
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
             * A code or string that identifies the device detail being asserted.
             * </p>
             * 
             * @param code
             *     Device detail
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
             * The value of the device detail.
             * </p>
             * 
             * @param value
             *     Value of detail
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            @Override
            public Parameter build() {
                return new Parameter(this);
            }

            private Builder from(Parameter parameter) {
                id = parameter.id;
                extension.addAll(parameter.extension);
                modifierExtension.addAll(parameter.modifierExtension);
                code = parameter.code;
                value = parameter.value;
                return this;
            }
        }
    }
}
