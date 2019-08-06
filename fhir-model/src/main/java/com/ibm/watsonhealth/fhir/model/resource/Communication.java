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
import com.ibm.watsonhealth.fhir.model.type.Attachment;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.CommunicationPriority;
import com.ibm.watsonhealth.fhir.model.type.CommunicationStatus;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health 
 * agency that was notified about a reportable condition.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Communication extends DomainResource {
    private final List<Identifier> identifier;
    private final List<Canonical> instantiatesCanonical;
    private final List<Uri> instantiatesUri;
    private final List<Reference> basedOn;
    private final List<Reference> partOf;
    private final List<Reference> inResponseTo;
    private final CommunicationStatus status;
    private final CodeableConcept statusReason;
    private final List<CodeableConcept> category;
    private final CommunicationPriority priority;
    private final List<CodeableConcept> medium;
    private final Reference subject;
    private final CodeableConcept topic;
    private final List<Reference> about;
    private final Reference encounter;
    private final DateTime sent;
    private final DateTime received;
    private final List<Reference> recipient;
    private final Reference sender;
    private final List<CodeableConcept> reasonCode;
    private final List<Reference> reasonReference;
    private final List<Payload> payload;
    private final List<Annotation> note;

    private volatile int hashCode;

    private Communication(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.identifier, "identifier"));
        instantiatesCanonical = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesCanonical, "instantiatesCanonical"));
        instantiatesUri = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.instantiatesUri, "instantiatesUri"));
        basedOn = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.basedOn, "basedOn"));
        partOf = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.partOf, "partOf"));
        inResponseTo = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.inResponseTo, "inResponseTo"));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        statusReason = builder.statusReason;
        category = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.category, "category"));
        priority = builder.priority;
        medium = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.medium, "medium"));
        subject = builder.subject;
        topic = builder.topic;
        about = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.about, "about"));
        encounter = builder.encounter;
        sent = builder.sent;
        received = builder.received;
        recipient = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.recipient, "recipient"));
        sender = builder.sender;
        reasonCode = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonCode, "reasonCode"));
        reasonReference = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.reasonReference, "reasonReference"));
        payload = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.payload, "payload"));
        note = Collections.unmodifiableList(ValidationSupport.requireNonNull(builder.note, "note"));
    }

    /**
     * <p>
     * Business identifiers assigned to this communication by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
     * part by this Communication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical}.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * <p>
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this Communication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri}.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * <p>
     * An order, proposal or plan fulfilled in whole or in part by this Communication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * <p>
     * Part of this action.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * <p>
     * Prior communication that this communication is in response to.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getInResponseTo() {
        return inResponseTo;
    }

    /**
     * <p>
     * The status of the transmission.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CommunicationStatus}.
     */
    public CommunicationStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Captures the reason for the current state of the Communication.
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
     * The type of message conveyed such as alert, notification, reminder, instruction, etc.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * <p>
     * Characterizes how quickly the planned or in progress communication must be addressed. Includes concepts such as stat, 
     * urgent, routine.
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
     * A channel that was used for this communication (e.g. email, fax).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getMedium() {
        return medium;
    }

    /**
     * <p>
     * The patient or group that was the focus of this communication.
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
     * Description of the purpose/content, similar to a subject line in an email.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getTopic() {
        return topic;
    }

    /**
     * <p>
     * Other resources that pertain to this communication and to which this communication should be associated.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getAbout() {
        return about;
    }

    /**
     * <p>
     * The Encounter during which this Communication was created or to which the creation of this record is tightly 
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
     * The time when this communication was sent.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getSent() {
        return sent;
    }

    /**
     * <p>
     * The time when this communication arrived at the destination.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link DateTime}.
     */
    public DateTime getReceived() {
        return received;
    }

    /**
     * <p>
     * The entity (e.g. person, organization, clinical information system, care team or device) which was the target of the 
     * communication. If receipts need to be tracked by an individual, a separate resource instance will need to be created 
     * for each recipient. Multiple recipient communications are intended where either receipts are not tracked (e.g. a mass 
     * mail-out) or a receipt is captured in aggregate (all emails confirmed received by a particular time).
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getRecipient() {
        return recipient;
    }

    /**
     * <p>
     * The entity (e.g. person, organization, clinical information system, or device) which was the source of the 
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
     * The reason or justification for the communication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept}.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * <p>
     * Indicates another resource whose existence justifies this communication.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * <p>
     * Text, attachment(s), or resource(s) that was communicated to the recipient.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Payload}.
     */
    public List<Payload> getPayload() {
        return payload;
    }

    /**
     * <p>
     * Additional notes or commentary about the communication by the sender, receiver or other interested parties.
     * </p>
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation}.
     */
    public List<Annotation> getNote() {
        return note;
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
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
                accept(partOf, "partOf", visitor, Reference.class);
                accept(inResponseTo, "inResponseTo", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(statusReason, "statusReason", visitor);
                accept(category, "category", visitor, CodeableConcept.class);
                accept(priority, "priority", visitor);
                accept(medium, "medium", visitor, CodeableConcept.class);
                accept(subject, "subject", visitor);
                accept(topic, "topic", visitor);
                accept(about, "about", visitor, Reference.class);
                accept(encounter, "encounter", visitor);
                accept(sent, "sent", visitor);
                accept(received, "received", visitor);
                accept(recipient, "recipient", visitor, Reference.class);
                accept(sender, "sender", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(payload, "payload", visitor, Payload.class);
                accept(note, "note", visitor, Annotation.class);
            }
            visitor.visitEnd(elementName, elementIndex, this);
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
        Communication other = (Communication) obj;
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
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(inResponseTo, other.inResponseTo) && 
            Objects.equals(status, other.status) && 
            Objects.equals(statusReason, other.statusReason) && 
            Objects.equals(category, other.category) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(medium, other.medium) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(topic, other.topic) && 
            Objects.equals(about, other.about) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(sent, other.sent) && 
            Objects.equals(received, other.received) && 
            Objects.equals(recipient, other.recipient) && 
            Objects.equals(sender, other.sender) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(payload, other.payload) && 
            Objects.equals(note, other.note);
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
                partOf, 
                inResponseTo, 
                status, 
                statusReason, 
                category, 
                priority, 
                medium, 
                subject, 
                topic, 
                about, 
                encounter, 
                sent, 
                received, 
                recipient, 
                sender, 
                reasonCode, 
                reasonReference, 
                payload, 
                note);
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

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<Canonical> instantiatesCanonical = new ArrayList<>();
        private List<Uri> instantiatesUri = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private List<Reference> inResponseTo = new ArrayList<>();
        private CommunicationStatus status;
        private CodeableConcept statusReason;
        private List<CodeableConcept> category = new ArrayList<>();
        private CommunicationPriority priority;
        private List<CodeableConcept> medium = new ArrayList<>();
        private Reference subject;
        private CodeableConcept topic;
        private List<Reference> about = new ArrayList<>();
        private Reference encounter;
        private DateTime sent;
        private DateTime received;
        private List<Reference> recipient = new ArrayList<>();
        private Reference sender;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private List<Payload> payload = new ArrayList<>();
        private List<Annotation> note = new ArrayList<>();

        private Builder() {
            super();
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * Business identifiers assigned to this communication by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param identifier
         *     Unique identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Business identifiers assigned to this communication by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param identifier
         *     Unique identifier
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * <p>
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this Communication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
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
         * part by this Communication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * <p>
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this Communication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
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
         * whole or in part by this Communication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * <p>
         * An order, proposal or plan fulfilled in whole or in part by this Communication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param basedOn
         *     Request fulfilled by this communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * <p>
         * An order, proposal or plan fulfilled in whole or in part by this Communication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param basedOn
         *     Request fulfilled by this communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * <p>
         * Part of this action.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Part of this action.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * <p>
         * Prior communication that this communication is in response to.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param inResponseTo
         *     Reply to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder inResponseTo(Reference... inResponseTo) {
            for (Reference value : inResponseTo) {
                this.inResponseTo.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Prior communication that this communication is in response to.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param inResponseTo
         *     Reply to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder inResponseTo(Collection<Reference> inResponseTo) {
            this.inResponseTo = new ArrayList<>(inResponseTo);
            return this;
        }

        /**
         * <p>
         * The status of the transmission.
         * </p>
         * 
         * @param status
         *     preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CommunicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * <p>
         * Captures the reason for the current state of the Communication.
         * </p>
         * 
         * @param statusReason
         *     Reason for current status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder statusReason(CodeableConcept statusReason) {
            this.statusReason = statusReason;
            return this;
        }

        /**
         * <p>
         * The type of message conveyed such as alert, notification, reminder, instruction, etc.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param category
         *     Message category
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(CodeableConcept... category) {
            for (CodeableConcept value : category) {
                this.category.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The type of message conveyed such as alert, notification, reminder, instruction, etc.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param category
         *     Message category
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * <p>
         * Characterizes how quickly the planned or in progress communication must be addressed. Includes concepts such as stat, 
         * urgent, routine.
         * </p>
         * 
         * @param priority
         *     Message urgency
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(CommunicationPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * <p>
         * A channel that was used for this communication (e.g. email, fax).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param medium
         *     A channel of communication
         * 
         * @return
         *     A reference to this Builder instance
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
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param medium
         *     A channel of communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder medium(Collection<CodeableConcept> medium) {
            this.medium = new ArrayList<>(medium);
            return this;
        }

        /**
         * <p>
         * The patient or group that was the focus of this communication.
         * </p>
         * 
         * @param subject
         *     Focus of message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * <p>
         * Description of the purpose/content, similar to a subject line in an email.
         * </p>
         * 
         * @param topic
         *     Description of the purpose/content
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder topic(CodeableConcept topic) {
            this.topic = topic;
            return this;
        }

        /**
         * <p>
         * Other resources that pertain to this communication and to which this communication should be associated.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param about
         *     Resources that pertain to this communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder about(Reference... about) {
            for (Reference value : about) {
                this.about.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Other resources that pertain to this communication and to which this communication should be associated.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param about
         *     Resources that pertain to this communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder about(Collection<Reference> about) {
            this.about = new ArrayList<>(about);
            return this;
        }

        /**
         * <p>
         * The Encounter during which this Communication was created or to which the creation of this record is tightly 
         * associated.
         * </p>
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * <p>
         * The time when this communication was sent.
         * </p>
         * 
         * @param sent
         *     When sent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sent(DateTime sent) {
            this.sent = sent;
            return this;
        }

        /**
         * <p>
         * The time when this communication arrived at the destination.
         * </p>
         * 
         * @param received
         *     When received
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder received(DateTime received) {
            this.received = received;
            return this;
        }

        /**
         * <p>
         * The entity (e.g. person, organization, clinical information system, care team or device) which was the target of the 
         * communication. If receipts need to be tracked by an individual, a separate resource instance will need to be created 
         * for each recipient. Multiple recipient communications are intended where either receipts are not tracked (e.g. a mass 
         * mail-out) or a receipt is captured in aggregate (all emails confirmed received by a particular time).
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param recipient
         *     Message recipient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recipient(Reference... recipient) {
            for (Reference value : recipient) {
                this.recipient.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The entity (e.g. person, organization, clinical information system, care team or device) which was the target of the 
         * communication. If receipts need to be tracked by an individual, a separate resource instance will need to be created 
         * for each recipient. Multiple recipient communications are intended where either receipts are not tracked (e.g. a mass 
         * mail-out) or a receipt is captured in aggregate (all emails confirmed received by a particular time).
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param recipient
         *     Message recipient
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recipient(Collection<Reference> recipient) {
            this.recipient = new ArrayList<>(recipient);
            return this;
        }

        /**
         * <p>
         * The entity (e.g. person, organization, clinical information system, or device) which was the source of the 
         * communication.
         * </p>
         * 
         * @param sender
         *     Message sender
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sender(Reference sender) {
            this.sender = sender;
            return this;
        }

        /**
         * <p>
         * The reason or justification for the communication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonCode
         *     Indication for message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The reason or justification for the communication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonCode
         *     Indication for message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * <p>
         * Indicates another resource whose existence justifies this communication.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param reasonReference
         *     Why was communication done?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Indicates another resource whose existence justifies this communication.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param reasonReference
         *     Why was communication done?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * <p>
         * Text, attachment(s), or resource(s) that was communicated to the recipient.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param payload
         *     Message payload
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payload(Payload... payload) {
            for (Payload value : payload) {
                this.payload.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Text, attachment(s), or resource(s) that was communicated to the recipient.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param payload
         *     Message payload
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder payload(Collection<Payload> payload) {
            this.payload = new ArrayList<>(payload);
            return this;
        }

        /**
         * <p>
         * Additional notes or commentary about the communication by the sender, receiver or other interested parties.
         * </p>
         * <p>
         * Adds new element(s) to existing list
         * </p>
         * 
         * @param note
         *     Comments made about the communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Additional notes or commentary about the communication by the sender, receiver or other interested parties.
         * </p>
         * <p>
         * Replaces existing list with a new one containing elements from the Collection
         * </p>
         * 
         * @param note
         *     Comments made about the communication
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        @Override
        public Communication build() {
            return new Communication(this);
        }

        protected Builder from(Communication communication) {
            super.from(communication);
            identifier.addAll(communication.identifier);
            instantiatesCanonical.addAll(communication.instantiatesCanonical);
            instantiatesUri.addAll(communication.instantiatesUri);
            basedOn.addAll(communication.basedOn);
            partOf.addAll(communication.partOf);
            inResponseTo.addAll(communication.inResponseTo);
            status = communication.status;
            statusReason = communication.statusReason;
            category.addAll(communication.category);
            priority = communication.priority;
            medium.addAll(communication.medium);
            subject = communication.subject;
            topic = communication.topic;
            about.addAll(communication.about);
            encounter = communication.encounter;
            sent = communication.sent;
            received = communication.received;
            recipient.addAll(communication.recipient);
            sender = communication.sender;
            reasonCode.addAll(communication.reasonCode);
            reasonReference.addAll(communication.reasonReference);
            payload.addAll(communication.payload);
            note.addAll(communication.note);
            return this;
        }
    }

    /**
     * <p>
     * Text, attachment(s), or resource(s) that was communicated to the recipient.
     * </p>
     */
    public static class Payload extends BackboneElement {
        private final Element content;

        private volatile int hashCode;

        private Payload(Builder builder) {
            super(builder);
            content = ValidationSupport.requireChoiceElement(builder.content, "content", String.class, Attachment.class, Reference.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * <p>
         * A communicated content (or for multi-part communications, one portion of the communication).
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getContent() {
            return content;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (content != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(content, "content", visitor);
                }
                visitor.visitEnd(elementName, elementIndex, this);
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
            Payload other = (Payload) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(content, other.content);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    content);
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
            private Element content;

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
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Adds new element(s) to existing list
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
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
             * <p>
             * Replaces existing list with a new one containing elements from the Collection
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * A communicated content (or for multi-part communications, one portion of the communication).
             * </p>
             * 
             * @param content
             *     Message part content
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder content(Element content) {
                this.content = content;
                return this;
            }

            @Override
            public Payload build() {
                return new Payload(this);
            }

            protected Builder from(Payload payload) {
                super.from(payload);
                content = payload.content;
                return this;
            }
        }
    }
}
