/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.CommunicationPriority;
import com.ibm.fhir.model.type.code.CommunicationStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health 
 * agency that was notified about a reportable condition.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Communication extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final List<Canonical> instantiatesCanonical;
    @Summary
    private final List<Uri> instantiatesUri;
    @Summary
    private final List<Reference> basedOn;
    @Summary
    private final List<Reference> partOf;
    @ReferenceTarget({ "Communication" })
    private final List<Reference> inResponseTo;
    @Summary
    @Binding(
        bindingName = "CommunicationStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of the communication.",
        valueSet = "http://hl7.org/fhir/ValueSet/event-status|4.0.1"
    )
    @Required
    private final CommunicationStatus status;
    @Summary
    @Binding(
        bindingName = "CommunicationNotDoneReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for the reason why a communication did not happen.",
        valueSet = "http://hl7.org/fhir/ValueSet/communication-not-done-reason"
    )
    private final CodeableConcept statusReason;
    @Binding(
        bindingName = "CommunicationCategory",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for general categories of communications such as alerts, instructions, etc.",
        valueSet = "http://hl7.org/fhir/ValueSet/communication-category"
    )
    private final List<CodeableConcept> category;
    @Summary
    @Binding(
        bindingName = "CommunicationPriority",
        strength = BindingStrength.Value.REQUIRED,
        description = "Codes indicating the relative importance of a communication.",
        valueSet = "http://hl7.org/fhir/ValueSet/request-priority|4.0.1"
    )
    private final CommunicationPriority priority;
    @Binding(
        bindingName = "CommunicationMedium",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for communication mediums such as phone, fax, email, in person, etc.",
        valueSet = "http://terminology.hl7.org/ValueSet/v3-ParticipationMode"
    )
    private final List<CodeableConcept> medium;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    private final Reference subject;
    @Binding(
        bindingName = "CommunicationTopic",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes describing the purpose or content of the communication.",
        valueSet = "http://hl7.org/fhir/ValueSet/communication-topic"
    )
    private final CodeableConcept topic;
    private final List<Reference> about;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    private final DateTime sent;
    private final DateTime received;
    @ReferenceTarget({ "Device", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Group", "CareTeam", "HealthcareService" })
    private final List<Reference> recipient;
    @ReferenceTarget({ "Device", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "HealthcareService" })
    private final Reference sender;
    @Summary
    @Binding(
        bindingName = "CommunicationReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes for describing reasons for the occurrence of a communication.",
        valueSet = "http://hl7.org/fhir/ValueSet/clinical-findings"
    )
    private final List<CodeableConcept> reasonCode;
    @Summary
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport", "DocumentReference" })
    private final List<Reference> reasonReference;
    private final List<Payload> payload;
    private final List<Annotation> note;

    private Communication(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        instantiatesCanonical = Collections.unmodifiableList(builder.instantiatesCanonical);
        instantiatesUri = Collections.unmodifiableList(builder.instantiatesUri);
        basedOn = Collections.unmodifiableList(builder.basedOn);
        partOf = Collections.unmodifiableList(builder.partOf);
        inResponseTo = Collections.unmodifiableList(builder.inResponseTo);
        status = builder.status;
        statusReason = builder.statusReason;
        category = Collections.unmodifiableList(builder.category);
        priority = builder.priority;
        medium = Collections.unmodifiableList(builder.medium);
        subject = builder.subject;
        topic = builder.topic;
        about = Collections.unmodifiableList(builder.about);
        encounter = builder.encounter;
        sent = builder.sent;
        received = builder.received;
        recipient = Collections.unmodifiableList(builder.recipient);
        sender = builder.sender;
        reasonCode = Collections.unmodifiableList(builder.reasonCode);
        reasonReference = Collections.unmodifiableList(builder.reasonReference);
        payload = Collections.unmodifiableList(builder.payload);
        note = Collections.unmodifiableList(builder.note);
    }

    /**
     * Business identifiers assigned to this communication by the performer or other systems which remain constant as the 
     * resource is updated and propagates from server to server.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
     * part by this Communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Canonical} that may be empty.
     */
    public List<Canonical> getInstantiatesCanonical() {
        return instantiatesCanonical;
    }

    /**
     * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
     * whole or in part by this Communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Uri} that may be empty.
     */
    public List<Uri> getInstantiatesUri() {
        return instantiatesUri;
    }

    /**
     * An order, proposal or plan fulfilled in whole or in part by this Communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * Part of this action.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * Prior communication that this communication is in response to.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getInResponseTo() {
        return inResponseTo;
    }

    /**
     * The status of the transmission.
     * 
     * @return
     *     An immutable object of type {@link CommunicationStatus} that is non-null.
     */
    public CommunicationStatus getStatus() {
        return status;
    }

    /**
     * Captures the reason for the current state of the Communication.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatusReason() {
        return statusReason;
    }

    /**
     * The type of message conveyed such as alert, notification, reminder, instruction, etc.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getCategory() {
        return category;
    }

    /**
     * Characterizes how quickly the planned or in progress communication must be addressed. Includes concepts such as stat, 
     * urgent, routine.
     * 
     * @return
     *     An immutable object of type {@link CommunicationPriority} that may be null.
     */
    public CommunicationPriority getPriority() {
        return priority;
    }

    /**
     * A channel that was used for this communication (e.g. email, fax).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getMedium() {
        return medium;
    }

    /**
     * The patient or group that was the focus of this communication.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Description of the purpose/content, similar to a subject line in an email.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getTopic() {
        return topic;
    }

    /**
     * Other resources that pertain to this communication and to which this communication should be associated.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getAbout() {
        return about;
    }

    /**
     * The Encounter during which this Communication was created or to which the creation of this record is tightly 
     * associated.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The time when this communication was sent.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getSent() {
        return sent;
    }

    /**
     * The time when this communication arrived at the destination.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getReceived() {
        return received;
    }

    /**
     * The entity (e.g. person, organization, clinical information system, care team or device) which was the target of the 
     * communication. If receipts need to be tracked by an individual, a separate resource instance will need to be created 
     * for each recipient. Multiple recipient communications are intended where either receipts are not tracked (e.g. a mass 
     * mail-out) or a receipt is captured in aggregate (all emails confirmed received by a particular time).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getRecipient() {
        return recipient;
    }

    /**
     * The entity (e.g. person, organization, clinical information system, or device) which was the source of the 
     * communication.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSender() {
        return sender;
    }

    /**
     * The reason or justification for the communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Indicates another resource whose existence justifies this communication.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * Text, attachment(s), or resource(s) that was communicated to the recipient.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Payload} that may be empty.
     */
    public List<Payload> getPayload() {
        return payload;
    }

    /**
     * Additional notes or commentary about the communication by the sender, receiver or other interested parties.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !instantiatesCanonical.isEmpty() || 
            !instantiatesUri.isEmpty() || 
            !basedOn.isEmpty() || 
            !partOf.isEmpty() || 
            !inResponseTo.isEmpty() || 
            (status != null) || 
            (statusReason != null) || 
            !category.isEmpty() || 
            (priority != null) || 
            !medium.isEmpty() || 
            (subject != null) || 
            (topic != null) || 
            !about.isEmpty() || 
            (encounter != null) || 
            (sent != null) || 
            (received != null) || 
            !recipient.isEmpty() || 
            (sender != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            !payload.isEmpty() || 
            !note.isEmpty();
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
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
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
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
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
         * The base language in which the resource is written.
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
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
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
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * Business identifiers assigned to this communication by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Business identifiers assigned to this communication by the performer or other systems which remain constant as the 
         * resource is updated and propagates from server to server.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique identifier
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this Communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The URL pointing to a FHIR-defined protocol, guideline, orderset or other definition that is adhered to in whole or in 
         * part by this Communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesCanonical
         *     Instantiates FHIR protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesCanonical(Collection<Canonical> instantiatesCanonical) {
            this.instantiatesCanonical = new ArrayList<>(instantiatesCanonical);
            return this;
        }

        /**
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this Communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The URL pointing to an externally maintained protocol, guideline, orderset or other definition that is adhered to in 
         * whole or in part by this Communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param instantiatesUri
         *     Instantiates external protocol or definition
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder instantiatesUri(Collection<Uri> instantiatesUri) {
            this.instantiatesUri = new ArrayList<>(instantiatesUri);
            return this;
        }

        /**
         * An order, proposal or plan fulfilled in whole or in part by this Communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * An order, proposal or plan fulfilled in whole or in part by this Communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param basedOn
         *     Request fulfilled by this communication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * Part of this action.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Part of this action.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * Prior communication that this communication is in response to.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Communication}</li>
         * </ul>
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
         * Prior communication that this communication is in response to.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Communication}</li>
         * </ul>
         * 
         * @param inResponseTo
         *     Reply to
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder inResponseTo(Collection<Reference> inResponseTo) {
            this.inResponseTo = new ArrayList<>(inResponseTo);
            return this;
        }

        /**
         * The status of the transmission.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     preparation | in-progress | not-done | on-hold | stopped | completed | entered-in-error | unknown
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CommunicationStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Captures the reason for the current state of the Communication.
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
         * The type of message conveyed such as alert, notification, reminder, instruction, etc.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The type of message conveyed such as alert, notification, reminder, instruction, etc.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param category
         *     Message category
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder category(Collection<CodeableConcept> category) {
            this.category = new ArrayList<>(category);
            return this;
        }

        /**
         * Characterizes how quickly the planned or in progress communication must be addressed. Includes concepts such as stat, 
         * urgent, routine.
         * 
         * @param priority
         *     routine | urgent | asap | stat
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(CommunicationPriority priority) {
            this.priority = priority;
            return this;
        }

        /**
         * A channel that was used for this communication (e.g. email, fax).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * A channel that was used for this communication (e.g. email, fax).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param medium
         *     A channel of communication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder medium(Collection<CodeableConcept> medium) {
            this.medium = new ArrayList<>(medium);
            return this;
        }

        /**
         * The patient or group that was the focus of this communication.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
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
         * Description of the purpose/content, similar to a subject line in an email.
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
         * Other resources that pertain to this communication and to which this communication should be associated.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Other resources that pertain to this communication and to which this communication should be associated.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param about
         *     Resources that pertain to this communication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder about(Collection<Reference> about) {
            this.about = new ArrayList<>(about);
            return this;
        }

        /**
         * The Encounter during which this Communication was created or to which the creation of this record is tightly 
         * associated.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
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
         * The time when this communication was sent.
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
         * The time when this communication arrived at the destination.
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
         * The entity (e.g. person, organization, clinical information system, care team or device) which was the target of the 
         * communication. If receipts need to be tracked by an individual, a separate resource instance will need to be created 
         * for each recipient. Multiple recipient communications are intended where either receipts are not tracked (e.g. a mass 
         * mail-out) or a receipt is captured in aggregate (all emails confirmed received by a particular time).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Group}</li>
         * <li>{@link CareTeam}</li>
         * <li>{@link HealthcareService}</li>
         * </ul>
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
         * The entity (e.g. person, organization, clinical information system, care team or device) which was the target of the 
         * communication. If receipts need to be tracked by an individual, a separate resource instance will need to be created 
         * for each recipient. Multiple recipient communications are intended where either receipts are not tracked (e.g. a mass 
         * mail-out) or a receipt is captured in aggregate (all emails confirmed received by a particular time).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Group}</li>
         * <li>{@link CareTeam}</li>
         * <li>{@link HealthcareService}</li>
         * </ul>
         * 
         * @param recipient
         *     Message recipient
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder recipient(Collection<Reference> recipient) {
            this.recipient = new ArrayList<>(recipient);
            return this;
        }

        /**
         * The entity (e.g. person, organization, clinical information system, or device) which was the source of the 
         * communication.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Organization}</li>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link HealthcareService}</li>
         * </ul>
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
         * The reason or justification for the communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * The reason or justification for the communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param reasonCode
         *     Indication for message
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Indicates another resource whose existence justifies this communication.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
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
         * Indicates another resource whose existence justifies this communication.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was communication done?
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * Text, attachment(s), or resource(s) that was communicated to the recipient.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Text, attachment(s), or resource(s) that was communicated to the recipient.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param payload
         *     Message payload
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder payload(Collection<Payload> payload) {
            this.payload = new ArrayList<>(payload);
            return this;
        }

        /**
         * Additional notes or commentary about the communication by the sender, receiver or other interested parties.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
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
         * Additional notes or commentary about the communication by the sender, receiver or other interested parties.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param note
         *     Comments made about the communication
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Build the {@link Communication}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Communication}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Communication per the base specification
         */
        @Override
        public Communication build() {
            Communication communication = new Communication(this);
            if (validating) {
                validate(communication);
            }
            return communication;
        }

        protected void validate(Communication communication) {
            super.validate(communication);
            ValidationSupport.checkList(communication.identifier, "identifier", Identifier.class);
            ValidationSupport.checkList(communication.instantiatesCanonical, "instantiatesCanonical", Canonical.class);
            ValidationSupport.checkList(communication.instantiatesUri, "instantiatesUri", Uri.class);
            ValidationSupport.checkList(communication.basedOn, "basedOn", Reference.class);
            ValidationSupport.checkList(communication.partOf, "partOf", Reference.class);
            ValidationSupport.checkList(communication.inResponseTo, "inResponseTo", Reference.class);
            ValidationSupport.requireNonNull(communication.status, "status");
            ValidationSupport.checkList(communication.category, "category", CodeableConcept.class);
            ValidationSupport.checkList(communication.medium, "medium", CodeableConcept.class);
            ValidationSupport.checkList(communication.about, "about", Reference.class);
            ValidationSupport.checkList(communication.recipient, "recipient", Reference.class);
            ValidationSupport.checkList(communication.reasonCode, "reasonCode", CodeableConcept.class);
            ValidationSupport.checkList(communication.reasonReference, "reasonReference", Reference.class);
            ValidationSupport.checkList(communication.payload, "payload", Payload.class);
            ValidationSupport.checkList(communication.note, "note", Annotation.class);
            ValidationSupport.checkReferenceType(communication.inResponseTo, "inResponseTo", "Communication");
            ValidationSupport.checkReferenceType(communication.subject, "subject", "Patient", "Group");
            ValidationSupport.checkReferenceType(communication.encounter, "encounter", "Encounter");
            ValidationSupport.checkReferenceType(communication.recipient, "recipient", "Device", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "Group", "CareTeam", "HealthcareService");
            ValidationSupport.checkReferenceType(communication.sender, "sender", "Device", "Organization", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson", "HealthcareService");
            ValidationSupport.checkReferenceType(communication.reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport", "DocumentReference");
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
     * Text, attachment(s), or resource(s) that was communicated to the recipient.
     */
    public static class Payload extends BackboneElement {
        @Choice({ String.class, Attachment.class, Reference.class })
        @Required
        private final Element content;

        private Payload(Builder builder) {
            super(builder);
            content = builder.content;
        }

        /**
         * A communicated content (or for multi-part communications, one portion of the communication).
         * 
         * @return
         *     An immutable object of type {@link String}, {@link Attachment} or {@link Reference} that is non-null.
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
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Convenience method for setting {@code content} with choice type String.
             * 
             * <p>This element is required.
             * 
             * @param content
             *     Message part content
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #content(Element)
             */
            public Builder content(java.lang.String content) {
                this.content = (content == null) ? null : String.of(content);
                return this;
            }

            /**
             * A communicated content (or for multi-part communications, one portion of the communication).
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link String}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link Reference}</li>
             * </ul>
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

            /**
             * Build the {@link Payload}
             * 
             * <p>Required elements:
             * <ul>
             * <li>content</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Payload}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Payload per the base specification
             */
            @Override
            public Payload build() {
                Payload payload = new Payload(this);
                if (validating) {
                    validate(payload);
                }
                return payload;
            }

            protected void validate(Payload payload) {
                super.validate(payload);
                ValidationSupport.requireChoiceElement(payload.content, "content", String.class, Attachment.class, Reference.class);
                ValidationSupport.requireValueOrChildren(payload);
            }

            protected Builder from(Payload payload) {
                super.from(payload);
                content = payload.content;
                return this;
            }
        }
    }
}
