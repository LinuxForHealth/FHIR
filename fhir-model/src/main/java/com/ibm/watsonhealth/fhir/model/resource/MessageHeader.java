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

import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Canonical;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.ResponseType;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The header for a message exchange that is either requesting or responding to an action. The reference(s) that are the 
 * subject of the action as well as other information related to the action are typically transmitted in a bundle in 
 * which the MessageHeader resource instance is the first resource in the bundle.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class MessageHeader extends DomainResource {
    private final Element event;
    private final List<Destination> destination;
    private final Reference sender;
    private final Reference enterer;
    private final Reference author;
    private final Source source;
    private final Reference responsible;
    private final CodeableConcept reason;
    private final Response response;
    private final List<Reference> focus;
    private final Canonical definition;

    private volatile int hashCode;

    private MessageHeader(Builder builder) {
        super(builder);
        event = ValidationSupport.requireChoiceElement(builder.event, "event", Coding.class, Uri.class);
        destination = Collections.unmodifiableList(builder.destination);
        sender = builder.sender;
        enterer = builder.enterer;
        author = builder.author;
        source = ValidationSupport.requireNonNull(builder.source, "source");
        responsible = builder.responsible;
        reason = builder.reason;
        response = builder.response;
        focus = Collections.unmodifiableList(builder.focus);
        definition = builder.definition;
    }

    /**
     * <p>
     * Code that identifies the event this message represents and connects it with its definition. Events defined as part of 
     * the FHIR specification have the system value "http://terminology.hl7.org/CodeSystem/message-events". Alternatively uri 
     * to the EventDefinition.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getEvent() {
        return event;
    }

    /**
     * <p>
     * The destination application which the message is intended for.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Destination}.
     */
    public List<Destination> getDestination() {
        return destination;
    }

    /**
     * <p>
     * Identifies the sending system to allow the use of a trust relationship.
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
     * The person or device that performed the data entry leading to this message. When there is more than one candidate, 
     * pick the most proximal to the message. Can provide other enterers in extensions.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * <p>
     * The logical author of the message - the person or device that decided the described event should happen. When there is 
     * more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * <p>
     * The source application from which this message originated.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Source}.
     */
    public Source getSource() {
        return source;
    }

    /**
     * <p>
     * The person or organization that accepts overall responsibility for the contents of the message. The implication is 
     * that the message event happened under the policies of the responsible party.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getResponsible() {
        return responsible;
    }

    /**
     * <p>
     * Coded indication of the cause for the event - indicates a reason for the occurrence of the event that is a focus of 
     * this message.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getReason() {
        return reason;
    }

    /**
     * <p>
     * Information about the message that this message is a response to. Only present if this message is a response.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Response}.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * <p>
     * The actual data of the message - a reference to the root/focus class of the event.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Reference}.
     */
    public List<Reference> getFocus() {
        return focus;
    }

    /**
     * <p>
     * Permanent link to the MessageDefinition for this message.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Canonical}.
     */
    public Canonical getDefinition() {
        return definition;
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
        return new Builder(event, source).from(this);
    }

    public Builder toBuilder(Element event, Source source) {
        return new Builder(event, source).from(this);
    }

    public static Builder builder(Element event, Source source) {
        return new Builder(event, source);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final Element event;
        private final Source source;

        // optional
        private List<Destination> destination = new ArrayList<>();
        private Reference sender;
        private Reference enterer;
        private Reference author;
        private Reference responsible;
        private CodeableConcept reason;
        private Response response;
        private List<Reference> focus = new ArrayList<>();
        private Canonical definition;

        private Builder(Element event, Source source) {
            super();
            this.event = event;
            this.source = source;
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
         * The destination application which the message is intended for.
         * </p>
         * 
         * @param destination
         *     Message destination application(s)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder destination(Destination... destination) {
            for (Destination value : destination) {
                this.destination.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The destination application which the message is intended for.
         * </p>
         * 
         * @param destination
         *     Message destination application(s)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder destination(Collection<Destination> destination) {
            this.destination.addAll(destination);
            return this;
        }

        /**
         * <p>
         * Identifies the sending system to allow the use of a trust relationship.
         * </p>
         * 
         * @param sender
         *     Real world sender of the message
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
         * The person or device that performed the data entry leading to this message. When there is more than one candidate, 
         * pick the most proximal to the message. Can provide other enterers in extensions.
         * </p>
         * 
         * @param enterer
         *     The source of the data entry
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * <p>
         * The logical author of the message - the person or device that decided the described event should happen. When there is 
         * more than one candidate, pick the most proximal to the MessageHeader. Can provide other authors in extensions.
         * </p>
         * 
         * @param author
         *     The source of the decision
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * <p>
         * The person or organization that accepts overall responsibility for the contents of the message. The implication is 
         * that the message event happened under the policies of the responsible party.
         * </p>
         * 
         * @param responsible
         *     Final responsibility for event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder responsible(Reference responsible) {
            this.responsible = responsible;
            return this;
        }

        /**
         * <p>
         * Coded indication of the cause for the event - indicates a reason for the occurrence of the event that is a focus of 
         * this message.
         * </p>
         * 
         * @param reason
         *     Cause of event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder reason(CodeableConcept reason) {
            this.reason = reason;
            return this;
        }

        /**
         * <p>
         * Information about the message that this message is a response to. Only present if this message is a response.
         * </p>
         * 
         * @param response
         *     If this is a reply to prior message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder response(Response response) {
            this.response = response;
            return this;
        }

        /**
         * <p>
         * The actual data of the message - a reference to the root/focus class of the event.
         * </p>
         * 
         * @param focus
         *     The actual content of the message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Reference... focus) {
            for (Reference value : focus) {
                this.focus.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The actual data of the message - a reference to the root/focus class of the event.
         * </p>
         * 
         * @param focus
         *     The actual content of the message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder focus(Collection<Reference> focus) {
            this.focus.addAll(focus);
            return this;
        }

        /**
         * <p>
         * Permanent link to the MessageDefinition for this message.
         * </p>
         * 
         * @param definition
         *     Link to the definition for this message
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder definition(Canonical definition) {
            this.definition = definition;
            return this;
        }

        @Override
        public MessageHeader build() {
            return new MessageHeader(this);
        }

        private Builder from(MessageHeader messageHeader) {
            id = messageHeader.id;
            meta = messageHeader.meta;
            implicitRules = messageHeader.implicitRules;
            language = messageHeader.language;
            text = messageHeader.text;
            contained.addAll(messageHeader.contained);
            extension.addAll(messageHeader.extension);
            modifierExtension.addAll(messageHeader.modifierExtension);
            destination.addAll(messageHeader.destination);
            sender = messageHeader.sender;
            enterer = messageHeader.enterer;
            author = messageHeader.author;
            responsible = messageHeader.responsible;
            reason = messageHeader.reason;
            response = messageHeader.response;
            focus.addAll(messageHeader.focus);
            definition = messageHeader.definition;
            return this;
        }
    }

    /**
     * <p>
     * The destination application which the message is intended for.
     * </p>
     */
    public static class Destination extends BackboneElement {
        private final String name;
        private final Reference target;
        private final Url endpoint;
        private final Reference receiver;

        private volatile int hashCode;

        private Destination(Builder builder) {
            super(builder);
            name = builder.name;
            target = builder.target;
            endpoint = ValidationSupport.requireNonNull(builder.endpoint, "endpoint");
            receiver = builder.receiver;
        }

        /**
         * <p>
         * Human-readable name for the target system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getTarget() {
            return target;
        }

        /**
         * <p>
         * Indicates where the message should be routed to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Url}.
         */
        public Url getEndpoint() {
            return endpoint;
        }

        /**
         * <p>
         * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific 
         * application isn't sufficient.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getReceiver() {
            return receiver;
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
                    accept(name, "name", visitor);
                    accept(target, "target", visitor);
                    accept(endpoint, "endpoint", visitor);
                    accept(receiver, "receiver", visitor);
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
            return new Builder(endpoint).from(this);
        }

        public Builder toBuilder(Url endpoint) {
            return new Builder(endpoint).from(this);
        }

        public static Builder builder(Url endpoint) {
            return new Builder(endpoint);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Url endpoint;

            // optional
            private String name;
            private Reference target;
            private Reference receiver;

            private Builder(Url endpoint) {
                super();
                this.endpoint = endpoint;
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
             * Human-readable name for the target system.
             * </p>
             * 
             * @param name
             *     Name of system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * Identifies the target end system in situations where the initial message transmission is to an intermediary system.
             * </p>
             * 
             * @param target
             *     Particular delivery destination within the destination
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder target(Reference target) {
                this.target = target;
                return this;
            }

            /**
             * <p>
             * Allows data conveyed by a message to be addressed to a particular person or department when routing to a specific 
             * application isn't sufficient.
             * </p>
             * 
             * @param receiver
             *     Intended "real-world" recipient for the data
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder receiver(Reference receiver) {
                this.receiver = receiver;
                return this;
            }

            @Override
            public Destination build() {
                return new Destination(this);
            }

            private Builder from(Destination destination) {
                id = destination.id;
                extension.addAll(destination.extension);
                modifierExtension.addAll(destination.modifierExtension);
                name = destination.name;
                target = destination.target;
                receiver = destination.receiver;
                return this;
            }
        }
    }

    /**
     * <p>
     * The source application from which this message originated.
     * </p>
     */
    public static class Source extends BackboneElement {
        private final String name;
        private final String software;
        private final String version;
        private final ContactPoint contact;
        private final Url endpoint;

        private volatile int hashCode;

        private Source(Builder builder) {
            super(builder);
            name = builder.name;
            software = builder.software;
            version = builder.version;
            contact = builder.contact;
            endpoint = ValidationSupport.requireNonNull(builder.endpoint, "endpoint");
        }

        /**
         * <p>
         * Human-readable name for the source system.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getName() {
            return name;
        }

        /**
         * <p>
         * May include configuration or other information useful in debugging.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getSoftware() {
            return software;
        }

        /**
         * <p>
         * Can convey versions of multiple systems in situations where a message passes through multiple hands.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link String}.
         */
        public String getVersion() {
            return version;
        }

        /**
         * <p>
         * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ContactPoint}.
         */
        public ContactPoint getContact() {
            return contact;
        }

        /**
         * <p>
         * Identifies the routing target to send acknowledgements to.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Url}.
         */
        public Url getEndpoint() {
            return endpoint;
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
                    accept(name, "name", visitor);
                    accept(software, "software", visitor);
                    accept(version, "version", visitor);
                    accept(contact, "contact", visitor);
                    accept(endpoint, "endpoint", visitor);
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
            return new Builder(endpoint).from(this);
        }

        public Builder toBuilder(Url endpoint) {
            return new Builder(endpoint).from(this);
        }

        public static Builder builder(Url endpoint) {
            return new Builder(endpoint);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Url endpoint;

            // optional
            private String name;
            private String software;
            private String version;
            private ContactPoint contact;

            private Builder(Url endpoint) {
                super();
                this.endpoint = endpoint;
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
             * Human-readable name for the source system.
             * </p>
             * 
             * @param name
             *     Name of system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * <p>
             * May include configuration or other information useful in debugging.
             * </p>
             * 
             * @param software
             *     Name of software running the system
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder software(String software) {
                this.software = software;
                return this;
            }

            /**
             * <p>
             * Can convey versions of multiple systems in situations where a message passes through multiple hands.
             * </p>
             * 
             * @param version
             *     Version of software running
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder version(String version) {
                this.version = version;
                return this;
            }

            /**
             * <p>
             * An e-mail, phone, website or other contact point to use to resolve issues with message communications.
             * </p>
             * 
             * @param contact
             *     Human contact for problems
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder contact(ContactPoint contact) {
                this.contact = contact;
                return this;
            }

            @Override
            public Source build() {
                return new Source(this);
            }

            private Builder from(Source source) {
                id = source.id;
                extension.addAll(source.extension);
                modifierExtension.addAll(source.modifierExtension);
                name = source.name;
                software = source.software;
                version = source.version;
                contact = source.contact;
                return this;
            }
        }
    }

    /**
     * <p>
     * Information about the message that this message is a response to. Only present if this message is a response.
     * </p>
     */
    public static class Response extends BackboneElement {
        private final Id identifier;
        private final ResponseType code;
        private final Reference details;

        private volatile int hashCode;

        private Response(Builder builder) {
            super(builder);
            identifier = ValidationSupport.requireNonNull(builder.identifier, "identifier");
            code = ValidationSupport.requireNonNull(builder.code, "code");
            details = builder.details;
        }

        /**
         * <p>
         * The MessageHeader.id of the message to which this message is a response.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Id}.
         */
        public Id getIdentifier() {
            return identifier;
        }

        /**
         * <p>
         * Code that identifies the type of response to the message - whether it was successful or not, and whether it should be 
         * resent or not.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link ResponseType}.
         */
        public ResponseType getCode() {
            return code;
        }

        /**
         * <p>
         * Full details of any issues found in the message.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getDetails() {
            return details;
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
                    accept(identifier, "identifier", visitor);
                    accept(code, "code", visitor);
                    accept(details, "details", visitor);
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
            return new Builder(identifier, code).from(this);
        }

        public Builder toBuilder(Id identifier, ResponseType code) {
            return new Builder(identifier, code).from(this);
        }

        public static Builder builder(Id identifier, ResponseType code) {
            return new Builder(identifier, code);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Id identifier;
            private final ResponseType code;

            // optional
            private Reference details;

            private Builder(Id identifier, ResponseType code) {
                super();
                this.identifier = identifier;
                this.code = code;
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
             * Full details of any issues found in the message.
             * </p>
             * 
             * @param details
             *     Specific list of hints/warnings/errors
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder details(Reference details) {
                this.details = details;
                return this;
            }

            @Override
            public Response build() {
                return new Response(this);
            }

            private Builder from(Response response) {
                id = response.id;
                extension.addAll(response.extension);
                modifierExtension.addAll(response.modifierExtension);
                details = response.details;
                return this;
            }
        }
    }
}
