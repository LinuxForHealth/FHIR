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
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ResponseType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The header for a message exchange that is either requesting or responding to an action. The reference(s) that are the 
 * subject of the action as well as other information related to the action are typically transmitted in a bundle in 
 * which the MessageHeader resource instance is the first resource in the bundle.
 * 
 * <p>Maturity level: FMM4 (Trial Use)
 */
@Maturity(
    level = 4,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MessageHeader extends DomainResource {
    @Summary
    @Choice({ Coding.class, Uri.class })
    @Binding(
        bindingName = "MessageEvent",
        strength = BindingStrength.Value.EXAMPLE,
        description = "One of the message events defined as part of this version of FHIR.",
        valueSet = "http://hl7.org/fhir/ValueSet/message-events"
    )
    @Required
    private final Element event;
    @Summary
    private final List<Destination> destination;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference sender;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference enterer;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference author;
    @Summary
    @Required
    private final Source source;
    @Summary
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference responsible;
    @Summary
    @Binding(
        bindingName = "EventReason",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Reason for event occurrence.",
        valueSet = "http://hl7.org/fhir/ValueSet/message-reason-encounter"
    )
    private final CodeableConcept reason;
    @Summary
    private final Response response;
    @Summary
    private final List<Reference> focus;
    @Summary
    private final Canonical definition;

    private MessageHeader(Builder builder) {
        super(builder);
        event = builder.event;
        destination = Collections.unmodifiableList(builder.destination);
        sender = builder.sender;
        enterer = builder.enterer;
        author = builder.author;
        source = builder.source;
        responsible = builder.responsible;
        reason = builder.reason;
        response = builder.response;
        focus = Collections.unmodifiableList(builder.focus);
        definition = builder.definition;
    }

    /**
     * Code that identifies the event this message represents and connects it with its definition. Events defined as part of 
     * the FHIR specification have the system value "http://terminology.hl7.org/CodeSystem/message-events". Alternatively uri 
     * to the EventDefinition.
     * 
     * @return
     *     An immutable object of type {@link Coding} or {@link Uri} that is non-null.
     */
    public Element getEvent() {
        return event;
    }

    /**
     * The destination application which the message is intended for.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Destination} that may be empty.
     */
    public List<Destination> getDestination() {
        return destination;
    }

    /**
     * Identifies the sending system to allow the use of a trust relationship.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSender() {
        return sender;
    }

    /**
     * The person or device that performed the data entry leading to this message. When there is more than one candidate, 
     * pick the most proximal to the message. Can provide other enterers in extensions.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * The logical author of the message - the person or device that decided the described event should happen. When there is 
     * more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * The source application from which this message originated.
     * 
     * @return
     *     An immutable object of type {@link Source} that is non-null.
     */
    public Source getSource() {
        return source;
    }

    /**
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is 
     * that the message event happened under the policies of the responsible party.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getResponsible() {
        return responsible;
    }

    /**
     * Coded indication of the cause for the event - indicates a reason for the occurrence of the event that is a focus of 
     * this message.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getReason() {
        return reason;
    }

    /**
     * Information about the message that this message is a response to. Only present if this message is a response.
     * 
     * @return
     *     An immutable object of type {@link Response} that may be null.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * The actual data of the message - a reference to the root/focus class of the event.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getFocus() {
        return focus;
    }

    /**
     * Permanent link to the MessageDefinition for this message.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getDefinition() {
        return definition;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (event != null) || 
            !destination.isEmpty() || 
            (sender != null) || 
            (enterer != null) || 
            (author != null) || 
            (source != null) || 
            (responsible != null) || 
            (reason != null) || 
            (response != null) || 
            !focus.isEmpty() || 
            (definition != null);
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
                accept(event, "event", visitor);
                accept(destination, "destination", visitor, Destination.class);
                accept(sender, "sender", visitor);
                accept(enterer, "enterer", visitor);
                accept(author, "author", visitor);
                accept(source, "source", visitor);
                accept(responsible, "responsible", visitor);
                accept(reason, "reason", visitor);
                accept(response, "response", visitor);
                accept(focus, "focus", visitor, Reference.class);
                accept(definition, "definition", visitor);
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
        MessageHeader other = (MessageHeader) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(event, other.event) && 
            Objects.equals(destination, other.destination) && 
            Objects.equals(sender, other.sender) && 
            Objects.equals(enterer, other.enterer) && 
            Objects.equals(author, other.author) && 
            Objects.equals(source, other.source) && 
            Objects.equals(responsible, other.responsible) && 
            Objects.equals(reason, other.reason) && 
            Objects.equals(response, other.response) && 
            Objects.equals(focus, other.focus) && 
            Objects.equals(definition, other.definition);
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
                event, 
                destination, 
                sender, 
                enterer, 
                author, 
                source, 
                responsible, 
                reason, 
                response, 
                focus, 
                definition);
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
        private Element event;
        private List<Destination> destination = new ArrayList<>();
        private Reference sender;
        private Reference enterer;
        private Reference author;
        private Source source;
        private Reference responsible;
        private CodeableConcept reason;
        private Response response;
        private List<Reference> focus = new ArrayList<>();
        private Canonical definition;

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
         * Code that identifies the event this message represents and connects it with its definition. Events defined as part of 
         * the FHIR specification have the system value "http://terminology.hl7.org/CodeSystem/message-events". Alternatively uri 
         * to the EventDefinition.
         * 
         * <p>This element is required.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Coding}</li>
         * <li>{@link Uri}</li>
         * </ul>
         * 
         * @param event
         *     Code for the event this message represents or link to event definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder event(Element event) {
            this.event = event;
            return this;
        }

        /**
         * The destination application which the message is intended for.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param destination
         *     Message destination application(s)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder destination(Destination... destination) {
            for (Destination value : destination) {
                this.destination.add(value);
            }
            return this;
        }

        /**
         * The destination application which the message is intended for.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param destination
         *     Message destination application(s)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder destination(Collection<Destination> destination) {
            this.destination = new ArrayList<>(destination);
            return this;
        }

        /**
         * Identifies the sending system to allow the use of a trust relationship.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param sender
         *     Real world sender of the message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sender(Reference sender) {
            this.sender = sender;
            return this;
        }

        /**
         * The person or device that performed the data entry leading to this message. When there is more than one candidate, 
         * pick the most proximal to the message. Can provide other enterers in extensions.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param enterer
         *     The source of the data entry
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * The logical author of the message - the person or device that decided the described event should happen. When there is 
         * more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param author
         *     The source of the decision
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * The source application from which this message originated.
         * 
         * <p>This element is required.
         * 
         * @param source
         *     Message source application
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Source source) {
            this.source = source;
            return this;
        }

        /**
         * The person or organization that accepts overall responsibility for the contents of the message. The implication is 
         * that the message event happened under the policies of the responsible party.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param responsible
         *     Final responsibility for event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder responsible(Reference responsible) {
            this.responsible = responsible;
            return this;
        }

        /**
         * Coded indication of the cause for the event - indicates a reason for the occurrence of the event that is a focus of 
         * this message.
         * 
         * @param reason
         *     Cause of event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reason(CodeableConcept reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Information about the message that this message is a response to. Only present if this message is a response.
         * 
         * @param response
         *     If this is a reply to prior message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder response(Response response) {
            this.response = response;
            return this;
        }

        /**
         * The actual data of the message - a reference to the root/focus class of the event.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param focus
         *     The actual content of the message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder focus(Reference... focus) {
            for (Reference value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * The actual data of the message - a reference to the root/focus class of the event.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param focus
         *     The actual content of the message
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder focus(Collection<Reference> focus) {
            this.focus = new ArrayList<>(focus);
            return this;
        }

        /**
         * Permanent link to the MessageDefinition for this message.
         * 
         * @param definition
         *     Link to the definition for this message
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder definition(Canonical definition) {
            this.definition = definition;
            return this;
        }

        /**
         * Build the {@link MessageHeader}
         * 
         * <p>Required elements:
         * <ul>
         * <li>event</li>
         * <li>source</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link MessageHeader}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid MessageHeader per the base specification
         */
        @Override
        public MessageHeader build() {
            MessageHeader messageHeader = new MessageHeader(this);
            if (validating) {
                validate(messageHeader);
            }
            return messageHeader;
        }

        protected void validate(MessageHeader messageHeader) {
            super.validate(messageHeader);
            ValidationSupport.requireChoiceElement(messageHeader.event, "event", Coding.class, Uri.class);
            ValidationSupport.checkList(messageHeader.destination, "destination", Destination.class);
            ValidationSupport.requireNonNull(messageHeader.source, "source");
            ValidationSupport.checkList(messageHeader.focus, "focus", Reference.class);
            ValidationSupport.checkReferenceType(messageHeader.sender, "sender", "Practitioner", "PractitionerRole", "Organization");
            ValidationSupport.checkReferenceType(messageHeader.enterer, "enterer", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(messageHeader.author, "author", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(messageHeader.responsible, "responsible", "Practitioner", "PractitionerRole", "Organization");
        }

        protected Builder from(MessageHeader messageHeader) {
            super.from(messageHeader);
            event = messageHeader.event;
            destination.addAll(messageHeader.destination);
            sender = messageHeader.sender;
            enterer = messageHeader.enterer;
            author = messageHeader.author;
            source = messageHeader.source;
            responsible = messageHeader.responsible;
            reason = messageHeader.reason;
            response = messageHeader.response;
            focus.addAll(messageHeader.focus);
            definition = messageHeader.definition;
            return this;
        }
    }

    /**
     * The destination application which the message is intended for.
     */
    public static class Destination extends BackboneElement {
        @Summary
        private final String name;
        @Summary
        @ReferenceTarget({ "Device" })
        private final Reference target;
        @Summary
        @Required
        private final Url endpoint;
        @Summary
        @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
        private final Reference receiver;

        private Destination(Builder builder) {
            super(builder);
            name = builder.name;
            target = builder.target;
            endpoint = builder.endpoint;
            receiver = builder.receiver;
        }

        /**
         * Human-readable name for the target system.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getTarget() {
            return target;
        }

        /**
         * Indicates where the message should be routed to.
         * 
         * @return
         *     An immutable object of type {@link Url} that is non-null.
         */
        public Url getEndpoint() {
            return endpoint;
        }

        /**
         * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific 
         * application isn't sufficient.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getReceiver() {
            return receiver;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (target != null) || 
                (endpoint != null) || 
                (receiver != null);
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
                    accept(name, "name", visitor);
                    accept(target, "target", visitor);
                    accept(endpoint, "endpoint", visitor);
                    accept(receiver, "receiver", visitor);
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
            Destination other = (Destination) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(target, other.target) && 
                Objects.equals(endpoint, other.endpoint) && 
                Objects.equals(receiver, other.receiver);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    target, 
                    endpoint, 
                    receiver);
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
            private String name;
            private Reference target;
            private Url endpoint;
            private Reference receiver;

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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Name of system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * Human-readable name for the target system.
             * 
             * @param name
             *     Name of system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Device}</li>
             * </ul>
             * 
             * @param target
             *     Particular delivery destination within the destination
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder target(Reference target) {
                this.target = target;
                return this;
            }

            /**
             * Indicates where the message should be routed to.
             * 
             * <p>This element is required.
             * 
             * @param endpoint
             *     Actual destination address or id
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder endpoint(Url endpoint) {
                this.endpoint = endpoint;
                return this;
            }

            /**
             * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific 
             * application isn't sufficient.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param receiver
             *     Intended "real-world" recipient for the data
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder receiver(Reference receiver) {
                this.receiver = receiver;
                return this;
            }

            /**
             * Build the {@link Destination}
             * 
             * <p>Required elements:
             * <ul>
             * <li>endpoint</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Destination}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Destination per the base specification
             */
            @Override
            public Destination build() {
                Destination destination = new Destination(this);
                if (validating) {
                    validate(destination);
                }
                return destination;
            }

            protected void validate(Destination destination) {
                super.validate(destination);
                ValidationSupport.requireNonNull(destination.endpoint, "endpoint");
                ValidationSupport.checkReferenceType(destination.target, "target", "Device");
                ValidationSupport.checkReferenceType(destination.receiver, "receiver", "Practitioner", "PractitionerRole", "Organization");
                ValidationSupport.requireValueOrChildren(destination);
            }

            protected Builder from(Destination destination) {
                super.from(destination);
                name = destination.name;
                target = destination.target;
                endpoint = destination.endpoint;
                receiver = destination.receiver;
                return this;
            }
        }
    }

    /**
     * The source application from which this message originated.
     */
    public static class Source extends BackboneElement {
        @Summary
        private final String name;
        @Summary
        private final String software;
        @Summary
        private final String version;
        @Summary
        private final ContactPoint contact;
        @Summary
        @Required
        private final Url endpoint;

        private Source(Builder builder) {
            super(builder);
            name = builder.name;
            software = builder.software;
            version = builder.version;
            contact = builder.contact;
            endpoint = builder.endpoint;
        }

        /**
         * Human-readable name for the source system.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * May include configuration or other information useful in debugging.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getSoftware() {
            return software;
        }

        /**
         * Can convey versions of multiple systems in situations where a message passes through multiple hands.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getVersion() {
            return version;
        }

        /**
         * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
         * 
         * @return
         *     An immutable object of type {@link ContactPoint} that may be null.
         */
        public ContactPoint getContact() {
            return contact;
        }

        /**
         * Identifies the routing target to send acknowledgements to.
         * 
         * @return
         *     An immutable object of type {@link Url} that is non-null.
         */
        public Url getEndpoint() {
            return endpoint;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (software != null) || 
                (version != null) || 
                (contact != null) || 
                (endpoint != null);
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
                    accept(name, "name", visitor);
                    accept(software, "software", visitor);
                    accept(version, "version", visitor);
                    accept(contact, "contact", visitor);
                    accept(endpoint, "endpoint", visitor);
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
            Source other = (Source) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(software, other.software) && 
                Objects.equals(version, other.version) && 
                Objects.equals(contact, other.contact) && 
                Objects.equals(endpoint, other.endpoint);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    software, 
                    version, 
                    contact, 
                    endpoint);
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
            private String name;
            private String software;
            private String version;
            private ContactPoint contact;
            private Url endpoint;

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
             * Convenience method for setting {@code name}.
             * 
             * @param name
             *     Name of system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * Human-readable name for the source system.
             * 
             * @param name
             *     Name of system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code software}.
             * 
             * @param software
             *     Name of software running the system
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #software(com.ibm.fhir.model.type.String)
             */
            public Builder software(java.lang.String software) {
                this.software = (software == null) ? null : String.of(software);
                return this;
            }

            /**
             * May include configuration or other information useful in debugging.
             * 
             * @param software
             *     Name of software running the system
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder software(String software) {
                this.software = software;
                return this;
            }

            /**
             * Convenience method for setting {@code version}.
             * 
             * @param version
             *     Version of software running
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #version(com.ibm.fhir.model.type.String)
             */
            public Builder version(java.lang.String version) {
                this.version = (version == null) ? null : String.of(version);
                return this;
            }

            /**
             * Can convey versions of multiple systems in situations where a message passes through multiple hands.
             * 
             * @param version
             *     Version of software running
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            /**
             * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
             * 
             * @param contact
             *     Human contact for problems
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder contact(ContactPoint contact) {
                this.contact = contact;
                return this;
            }

            /**
             * Identifies the routing target to send acknowledgements to.
             * 
             * <p>This element is required.
             * 
             * @param endpoint
             *     Actual message source address or id
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder endpoint(Url endpoint) {
                this.endpoint = endpoint;
                return this;
            }

            /**
             * Build the {@link Source}
             * 
             * <p>Required elements:
             * <ul>
             * <li>endpoint</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Source}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Source per the base specification
             */
            @Override
            public Source build() {
                Source source = new Source(this);
                if (validating) {
                    validate(source);
                }
                return source;
            }

            protected void validate(Source source) {
                super.validate(source);
                ValidationSupport.requireNonNull(source.endpoint, "endpoint");
                ValidationSupport.requireValueOrChildren(source);
            }

            protected Builder from(Source source) {
                super.from(source);
                name = source.name;
                software = source.software;
                version = source.version;
                contact = source.contact;
                endpoint = source.endpoint;
                return this;
            }
        }
    }

    /**
     * Information about the message that this message is a response to. Only present if this message is a response.
     */
    public static class Response extends BackboneElement {
        @Summary
        @Required
        private final Id identifier;
        @Summary
        @Binding(
            bindingName = "ResponseType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The kind of response to a message.",
            valueSet = "http://hl7.org/fhir/ValueSet/response-code|4.0.1"
        )
        @Required
        private final ResponseType code;
        @Summary
        @ReferenceTarget({ "OperationOutcome" })
        private final Reference details;

        private Response(Builder builder) {
            super(builder);
            identifier = builder.identifier;
            code = builder.code;
            details = builder.details;
        }

        /**
         * The MessageHeader.id of the message to which this message is a response.
         * 
         * @return
         *     An immutable object of type {@link Id} that is non-null.
         */
        public Id getIdentifier() {
            return identifier;
        }

        /**
         * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be 
         * resent or not.
         * 
         * @return
         *     An immutable object of type {@link ResponseType} that is non-null.
         */
        public ResponseType getCode() {
            return code;
        }

        /**
         * Full details of any issues found in the message.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getDetails() {
            return details;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (identifier != null) || 
                (code != null) || 
                (details != null);
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
                    accept(identifier, "identifier", visitor);
                    accept(code, "code", visitor);
                    accept(details, "details", visitor);
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
            Response other = (Response) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(code, other.code) && 
                Objects.equals(details, other.details);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    identifier, 
                    code, 
                    details);
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
            private Id identifier;
            private ResponseType code;
            private Reference details;

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
             * The MessageHeader.id of the message to which this message is a response.
             * 
             * <p>This element is required.
             * 
             * @param identifier
             *     Id of original message
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Id identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be 
             * resent or not.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     ok | transient-error | fatal-error
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(ResponseType code) {
                this.code = code;
                return this;
            }

            /**
             * Full details of any issues found in the message.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link OperationOutcome}</li>
             * </ul>
             * 
             * @param details
             *     Specific list of hints/warnings/errors
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder details(Reference details) {
                this.details = details;
                return this;
            }

            /**
             * Build the {@link Response}
             * 
             * <p>Required elements:
             * <ul>
             * <li>identifier</li>
             * <li>code</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Response}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Response per the base specification
             */
            @Override
            public Response build() {
                Response response = new Response(this);
                if (validating) {
                    validate(response);
                }
                return response;
            }

            protected void validate(Response response) {
                super.validate(response);
                ValidationSupport.requireNonNull(response.identifier, "identifier");
                ValidationSupport.requireNonNull(response.code, "code");
                ValidationSupport.checkReferenceType(response.details, "details", "OperationOutcome");
                ValidationSupport.requireValueOrChildren(response);
            }

            protected Builder from(Response response) {
                super.from(response);
                identifier = response.identifier;
                code = response.code;
                details = response.details;
                return this;
            }
        }
    }
}
