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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.type.Annotation;
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.CommunicationPriority;
import com.ibm.watsonhealth.fhir.model.type.CommunicationRequestStatus;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS 
 * system proposes that the public health agency be notified about a reportable condition.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class CommunicationRequest extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Reference> basedOn;
    private final List<Reference> replaces;
    private final Identifier groupIdentifier;
    private final CommunicationRequestStatus status;
    private final CodeableConcept statusReason;
    private final List<CodeableConcept> category;
    private final CommunicationPriority priority;
    private final Boolean doNotPerform;
    private final List<CodeableConcept> medium;
    private final Reference subject;
    private final List<Reference> about;
    private final Reference encounter;
    private final List<Payload> payload;
    private final Element occurrence;
    private final DateTime authoredOn;
    private final Reference requester;
    private final List<Reference> recipient;
    private final Reference sender;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Annotation> note;

    private CommunicationRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        replaces = Collections.unmodifiableList(builder.replaces);
        groupIdentifier = builder.groupIdentifier;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = builder.statusReason;
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        doNotPerform = builder.doNotPerform;
        medium = Collections.unmodifiableList(builder.medium);
        subject = builder.subject;
        about = Collections.unmodifiableList(builder.about);
        encounter = builder.encounter;
        payload = Collections.unmodifiableList(builder.payload);
        occurrence = ValidationSupport.choiceElement(builder.occurrence, "occurrence", DateTime.class, Period.class);
        authoredOn = builder.authoredOn;
        requester = builder.requester;
        recipient = Collections.unmodifiableList(builder.recipient);
        sender = builder.sender;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * <p>
     * Business identifiers assigned to this communication request by the performer or other systems which remain constant as 
     * the resource is updated and propagates from server to server.
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
     * A plan or proposal that is fulfilled in whole or in part by this request.
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
     * Completed or terminated request(s) whose function is taken by this new request.
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
     * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
     * representing the identifier of the requisition, prescription or similar form.
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
     * The status of the proposal or order.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CommunicationRequestStatus}.
     */
    public CommunicationRequestStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Captures the reason for the current state of the CommunicationRequest.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * <p>
     * The type of message to be sent such as alert, notification, reminder, instruction, etc.
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
     * Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CommunicationPriority}.
     */
    public CommunicationPriority getPriority() {
        return priority;
    }

    /**
     * <p>
     * If true indicates that the CommunicationRequest is asking for the specified action to *not* occur.
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
     * A channel that was used for this communication (e.g. email, fax).
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getMedium() {
        return medium;
    }

    /**
     * <p>
     * The patient or group that is the focus of this communication request.
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
     * Other resources that pertain to this communication request and to which this communication request should be 
     * associated.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAbout() {
        return about;
    }

    /**
     * <p>
     * The Encounter during which this CommunicationRequest was created or to which the creation of this record is tightly 
     * associated.
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
     * Text, attachment(s), or resource(s) to be communicated to the recipient.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Payload}.
     */
    public List<Payload> getPayload() {
        return payload;
    }

    /**
     * <p>
     * The time when this communication is to occur.
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
     * For draft requests, indicates the date of initial creation. For requests with other statuses, indicates the date of 
     * activation.
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
     * The device, individual, or organization who initiated the request and has responsibility for its activation.
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
     * The entity (e.g. person, organization, clinical information system, device, group, or care team) which is the intended 
     * target of the communication.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRecipient() {
        return recipient;
    }

    /**
     * <p>
     * The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the 
     * communication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getSender() {
        return sender;
    }

    /**
     * <p>
     * Describes why the request is being made in coded or textual form.
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
     * Indicates another resource whose existence justifies this request.
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
     * Comments made about the request by the requester, sender, recipient, subject or other participants.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
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
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(replaces, "replaces", visitor, Reference.class);
                accept(groupIdentifier, "groupIdentifier", visitor);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(priority, "priority", visitor);
                accept(doNotPerform, "doNotPerform", visitor);
                accept(medium, "medium", visitor, CodeableConcept.class);
                accept(subject, "subject", visitor);
                accept(about, "about", visitor, Reference.class);
                accept(encounter, "encounter", visitor);
                accept(payload, "payload", visitor, Payload.class);
                accept(occurrence, "occurrence", visitor, true);
                accept(authoredOn, "authoredOn", visitor);
                accept(requester, "requester", visitor);
                accept(recipient, "recipient", visitor, Reference.class);
                accept(sender, "sender", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(note, "note", visitor, Annotation.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(status);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.basedOn.addAll(basedOn);
        builder.replaces.addAll(replaces);
        builder.groupIdentifier = groupIdentifier;
        builder.statusReason = statusReason;
        builder.category.addAll(category);
        builder.priority = priority;
        builder.doNotPerform = doNotPerform;
        builder.medium.addAll(medium);
        builder.subject = subject;
        builder.about.addAll(about);
        builder.encounter = encounter;
        builder.payload.addAll(payload);
        builder.occurrence = occurrence;
        builder.authoredOn = authoredOn;
        builder.requester = requester;
        builder.recipient.addAll(recipient);
        builder.sender = sender;
        builder.reasonCode.addAll(reasonCode);
        builder.reasonReference.addAll(reasonReference);
        builder.note.addAll(note);
        return builder;
    }

    public static Builder builder(CommunicationRequestStatus status) {
        return new Builder(status);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final CommunicationRequestStatus status;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> replaces = new ArrayList<>();
        private Identifier groupIdentifier;
        private CodeableConcept statusReason;
        private List<CodeableConcept> category = new ArrayList<>();
        private CommunicationPriority priority;
        private Boolean doNotPerform;
        private List<CodeableConcept> medium = new ArrayList<>();
        private Reference subject;
        private List<Reference> about = new ArrayList<>();
        private Reference encounter;
        private List<Payload> payload = new ArrayList<>();
        private Element occurrence;
        private DateTime authoredOn;
        private Reference requester;
        private List<Reference> recipient = new ArrayList<>();
        private Reference sender;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

        private Builder(CommunicationRequestStatus status) {
            super();
            this.status = status;
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
         * Business identifiers assigned to this communication request by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     Unique identifier
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
         * Business identifiers assigned to this communication request by the performer or other systems which remain constant as 
         * the resource is updated and propagates from server to server.
         * </p>
         * 
         * @param identifier
         *     Unique identifier
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
         * A plan or proposal that is fulfilled in whole or in part by this request.
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan or proposal
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
         * A plan or proposal that is fulfilled in whole or in part by this request.
         * </p>
         * 
         * @param basedOn
         *     Fulfills plan or proposal
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
         * Completed or terminated request(s) whose function is taken by this new request.
         * </p>
         * 
         * @param replaces
         *     Request(s) replaced by this request
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
         * Completed or terminated request(s) whose function is taken by this new request.
         * </p>
         * 
         * @param replaces
         *     Request(s) replaced by this request
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
         * A shared identifier common to all requests that were authorized more or less simultaneously by a single author, 
         * representing the identifier of the requisition, prescription or similar form.
         * </p>
         * 
         * @param groupIdentifier
         *     Composite request this is part of
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
         * Captures the reason for the current state of the CommunicationRequest.
         * </p>
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder statusReason(CodeableConcept statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * <p>
         * The type of message to be sent such as alert, notification, reminder, instruction, etc.
         * </p>
         * 
         * @param category
         *     Message category
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
         * The type of message to be sent such as alert, notification, reminder, instruction, etc.
         * </p>
         * 
         * @param category
         *     Message category
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
         * Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.
         * </p>
         * 
         * @param priority
         *     Message urgency
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder priority(CommunicationPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * If true indicates that the CommunicationRequest is asking for the specified action to *not* occur.
         * </p>
         * 
         * @param doNotPerform
         *     True if request is prohibiting action
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
         * A channel that was used for this communication (e.g. email, fax).
         * </p>
         * 
         * @param medium
         *     A channel of communication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder medium(CodeableConcept... medium) {
            for (CodeableConcept value : medium) {
                this.medium.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A channel that was used for this communication (e.g. email, fax).
         * </p>
         * 
         * @param medium
         *     A channel of communication
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder medium(Collection<CodeableConcept> medium) {
            this.medium.addAll(medium);
            return this;
        }

        /**
         * <p>
         * The patient or group that is the focus of this communication request.
         * </p>
         * 
         * @param subject
         *     Focus of message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * Other resources that pertain to this communication request and to which this communication request should be 
         * associated.
         * </p>
         * 
         * @param about
         *     Resources that pertain to this communication request
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder about(Reference... about) {
            for (Reference value : about) {
                this.about.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Other resources that pertain to this communication request and to which this communication request should be 
         * associated.
         * </p>
         * 
         * @param about
         *     Resources that pertain to this communication request
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder about(Collection<Reference> about) {
            this.about.addAll(about);
            return this;
        }

        /**
         * <p>
         * The Encounter during which this CommunicationRequest was created or to which the creation of this record is tightly 
         * associated.
         * </p>
         * 
         * @param encounter
         *     Encounter created as part of
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
         * Text, attachment(s), or resource(s) to be communicated to the recipient.
         * </p>
         * 
         * @param payload
         *     Message payload
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payload(Payload... payload) {
            for (Payload value : payload) {
                this.payload.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Text, attachment(s), or resource(s) to be communicated to the recipient.
         * </p>
         * 
         * @param payload
         *     Message payload
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder payload(Collection<Payload> payload) {
            this.payload.addAll(payload);
            return this;
        }

        /**
         * <p>
         * The time when this communication is to occur.
         * </p>
         * 
         * @param occurrence
         *     When scheduled
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
         * For draft requests, indicates the date of initial creation. For requests with other statuses, indicates the date of 
         * activation.
         * </p>
         * 
         * @param authoredOn
         *     When request transitioned to being actionable
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
         * The device, individual, or organization who initiated the request and has responsibility for its activation.
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
         * The entity (e.g. person, organization, clinical information system, device, group, or care team) which is the intended 
         * target of the communication.
         * </p>
         * 
         * @param recipient
         *     Message recipient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder recipient(Reference... recipient) {
            for (Reference value : recipient) {
                this.recipient.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The entity (e.g. person, organization, clinical information system, device, group, or care team) which is the intended 
         * target of the communication.
         * </p>
         * 
         * @param recipient
         *     Message recipient
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder recipient(Collection<Reference> recipient) {
            this.recipient.addAll(recipient);
            return this;
        }

        /**
         * <p>
         * The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the 
         * communication.
         * </p>
         * 
         * @param sender
         *     Message sender
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder sender(Reference sender) {
            this.sender = sender;
            return this;
        }

        /**
         * <p>
         * Describes why the request is being made in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why is communication needed?
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
         * Describes why the request is being made in coded or textual form.
         * </p>
         * 
         * @param reasonCode
         *     Why is communication needed?
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
         * Indicates another resource whose existence justifies this request.
         * </p>
         * 
         * @param reasonReference
         *     Why is communication needed?
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
         * Indicates another resource whose existence justifies this request.
         * </p>
         * 
         * @param reasonReference
         *     Why is communication needed?
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
         * Comments made about the request by the requester, sender, recipient, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about communication request
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
         * Comments made about the request by the requester, sender, recipient, subject or other participants.
         * </p>
         * 
         * @param note
         *     Comments made about communication request
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder note(Collection<Annotation> note) {
            this.note.addAll(note);
            return this;
        }

        @Override
        public CommunicationRequest build() {
            return new CommunicationRequest(this);
        }
    }

    /**
     * <p>
     * Text, attachment(s), or resource(s) to be communicated to the recipient.
     * </p>
     */
    public static class Payload extends BackboneElement {
        private final Element content;

        private Payload(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", String.class, Attachment.class, Reference.class);
        }

        /**
         * <p>
         * The communicated content (or for multi-part communications, one portion of the communication).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getContent() {
            return content;
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
                    accept(content, "content", visitor, true);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Element content) {
            return new Builder(content);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Element content;

            private Builder(Element content) {
                super();
                this.content = content;
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

            @Override
            public Payload build() {
                return new Payload(this);
            }

            private static Builder from(Payload payload) {
                Builder builder = new Builder(payload.content);
                return builder;
            }
        }
    }
}
