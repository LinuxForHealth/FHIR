/*
 * (C) Copyright IBM Corp. 2019, 2022
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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.SubscriptionChannelType;
import com.ibm.fhir.model.type.code.SubscriptionStatusCode;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The subscription resource is used to define a push-based subscription from a server to another system. Once a 
 * subscription is registered with the server, the server checks every resource that is created or updated, and if the 
 * resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an 
 * appropriate action.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.Value.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Subscription extends DomainResource {
    @Summary
    @Binding(
        bindingName = "SubscriptionStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of a subscription.",
        valueSet = "http://hl7.org/fhir/ValueSet/subscription-status|4.3.0"
    )
    @Required
    private final SubscriptionStatusCode status;
    @Summary
    private final List<ContactPoint> contact;
    @Summary
    private final Instant end;
    @Summary
    @Required
    private final String reason;
    @Summary
    @Required
    private final String criteria;
    @Summary
    private final String error;
    @Summary
    @Required
    private final Channel channel;

    private Subscription(Builder builder) {
        super(builder);
        status = builder.status;
        contact = Collections.unmodifiableList(builder.contact);
        end = builder.end;
        reason = builder.reason;
        criteria = builder.criteria;
        error = builder.error;
        channel = builder.channel;
    }

    /**
     * The status of the subscription, which marks the server state for managing the subscription.
     * 
     * @return
     *     An immutable object of type {@link SubscriptionStatusCode} that is non-null.
     */
    public SubscriptionStatusCode getStatus() {
        return status;
    }

    /**
     * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
     * troubleshooting.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link ContactPoint} that may be empty.
     */
    public List<ContactPoint> getContact() {
        return contact;
    }

    /**
     * The time for the server to turn the subscription off.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * A description of why this subscription is defined.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getReason() {
        return reason;
    }

    /**
     * The rules that the server should use to determine when to generate notifications for this subscription.
     * 
     * @return
     *     An immutable object of type {@link String} that is non-null.
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * A record of the last error that occurred when the server processed a notification.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getError() {
        return error;
    }

    /**
     * Details where to send notifications when resources are received that meet the criteria.
     * 
     * @return
     *     An immutable object of type {@link Channel} that is non-null.
     */
    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (status != null) || 
            !contact.isEmpty() || 
            (end != null) || 
            (reason != null) || 
            (criteria != null) || 
            (error != null) || 
            (channel != null);
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
                accept(status, "status", visitor);
                accept(contact, "contact", visitor, ContactPoint.class);
                accept(end, "end", visitor);
                accept(reason, "reason", visitor);
                accept(criteria, "criteria", visitor);
                accept(error, "error", visitor);
                accept(channel, "channel", visitor);
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
        Subscription other = (Subscription) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(status, other.status) && 
            Objects.equals(contact, other.contact) && 
            Objects.equals(end, other.end) && 
            Objects.equals(reason, other.reason) && 
            Objects.equals(criteria, other.criteria) && 
            Objects.equals(error, other.error) && 
            Objects.equals(channel, other.channel);
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
                status, 
                contact, 
                end, 
                reason, 
                criteria, 
                error, 
                channel);
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
        private SubscriptionStatusCode status;
        private List<ContactPoint> contact = new ArrayList<>();
        private Instant end;
        private String reason;
        private String criteria;
        private String error;
        private Channel channel;

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
         * The status of the subscription, which marks the server state for managing the subscription.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     requested | active | error | off
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(SubscriptionStatusCode status) {
            this.status = status;
            return this;
        }

        /**
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder contact(ContactPoint... contact) {
            for (ContactPoint value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder contact(Collection<ContactPoint> contact) {
            this.contact = new ArrayList<>(contact);
            return this;
        }

        /**
         * Convenience method for setting {@code end}.
         * 
         * @param end
         *     When to automatically delete the subscription
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #end(com.ibm.fhir.model.type.Instant)
         */
        public Builder end(java.time.ZonedDateTime end) {
            this.end = (end == null) ? null : Instant.of(end);
            return this;
        }

        /**
         * The time for the server to turn the subscription off.
         * 
         * @param end
         *     When to automatically delete the subscription
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder end(Instant end) {
            this.end = end;
            return this;
        }

        /**
         * Convenience method for setting {@code reason}.
         * 
         * <p>This element is required.
         * 
         * @param reason
         *     Description of why this subscription was created
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #reason(com.ibm.fhir.model.type.String)
         */
        public Builder reason(java.lang.String reason) {
            this.reason = (reason == null) ? null : String.of(reason);
            return this;
        }

        /**
         * A description of why this subscription is defined.
         * 
         * <p>This element is required.
         * 
         * @param reason
         *     Description of why this subscription was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * Convenience method for setting {@code criteria}.
         * 
         * <p>This element is required.
         * 
         * @param criteria
         *     Rule for server push
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #criteria(com.ibm.fhir.model.type.String)
         */
        public Builder criteria(java.lang.String criteria) {
            this.criteria = (criteria == null) ? null : String.of(criteria);
            return this;
        }

        /**
         * The rules that the server should use to determine when to generate notifications for this subscription.
         * 
         * <p>This element is required.
         * 
         * @param criteria
         *     Rule for server push
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder criteria(String criteria) {
            this.criteria = criteria;
            return this;
        }

        /**
         * Convenience method for setting {@code error}.
         * 
         * @param error
         *     Latest error note
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #error(com.ibm.fhir.model.type.String)
         */
        public Builder error(java.lang.String error) {
            this.error = (error == null) ? null : String.of(error);
            return this;
        }

        /**
         * A record of the last error that occurred when the server processed a notification.
         * 
         * @param error
         *     Latest error note
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder error(String error) {
            this.error = error;
            return this;
        }

        /**
         * Details where to send notifications when resources are received that meet the criteria.
         * 
         * <p>This element is required.
         * 
         * @param channel
         *     The channel on which to report matches to the criteria
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder channel(Channel channel) {
            this.channel = channel;
            return this;
        }

        /**
         * Build the {@link Subscription}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>reason</li>
         * <li>criteria</li>
         * <li>channel</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Subscription}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Subscription per the base specification
         */
        @Override
        public Subscription build() {
            Subscription subscription = new Subscription(this);
            if (validating) {
                validate(subscription);
            }
            return subscription;
        }

        protected void validate(Subscription subscription) {
            super.validate(subscription);
            ValidationSupport.requireNonNull(subscription.status, "status");
            ValidationSupport.checkList(subscription.contact, "contact", ContactPoint.class);
            ValidationSupport.requireNonNull(subscription.reason, "reason");
            ValidationSupport.requireNonNull(subscription.criteria, "criteria");
            ValidationSupport.requireNonNull(subscription.channel, "channel");
        }

        protected Builder from(Subscription subscription) {
            super.from(subscription);
            status = subscription.status;
            contact.addAll(subscription.contact);
            end = subscription.end;
            reason = subscription.reason;
            criteria = subscription.criteria;
            error = subscription.error;
            channel = subscription.channel;
            return this;
        }
    }

    /**
     * Details where to send notifications when resources are received that meet the criteria.
     */
    public static class Channel extends BackboneElement {
        @Summary
        @Binding(
            bindingName = "SubscriptionChannelType",
            strength = BindingStrength.Value.REQUIRED,
            description = "The type of method used to execute a subscription.",
            valueSet = "http://hl7.org/fhir/ValueSet/subscription-channel-type|4.3.0"
        )
        @Required
        private final SubscriptionChannelType type;
        @Summary
        private final Url endpoint;
        @Summary
        @Binding(
            bindingName = "MimeType",
            strength = BindingStrength.Value.REQUIRED,
            description = "BCP 13 (RFCs 2045, 2046, 2047, 4288, 4289 and 2049)",
            valueSet = "http://hl7.org/fhir/ValueSet/mimetypes|4.3.0"
        )
        private final Code payload;
        @Summary
        private final List<String> header;

        private Channel(Builder builder) {
            super(builder);
            type = builder.type;
            endpoint = builder.endpoint;
            payload = builder.payload;
            header = Collections.unmodifiableList(builder.header);
        }

        /**
         * The type of channel to send notifications on.
         * 
         * @return
         *     An immutable object of type {@link SubscriptionChannelType} that is non-null.
         */
        public SubscriptionChannelType getType() {
            return type;
        }

        /**
         * The url that describes the actual end-point to send messages to.
         * 
         * @return
         *     An immutable object of type {@link Url} that may be null.
         */
        public Url getEndpoint() {
            return endpoint;
        }

        /**
         * The mime type to send the payload in - either application/fhir+xml, or application/fhir+json. If the payload is not 
         * present, then there is no payload in the notification, just a notification. The mime type "text/plain" may also be 
         * used for Email and SMS subscriptions.
         * 
         * @return
         *     An immutable object of type {@link Code} that may be null.
         */
        public Code getPayload() {
            return payload;
        }

        /**
         * Additional headers / information to send as part of the notification.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
         */
        public List<String> getHeader() {
            return header;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (type != null) || 
                (endpoint != null) || 
                (payload != null) || 
                !header.isEmpty();
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
                    accept(type, "type", visitor);
                    accept(endpoint, "endpoint", visitor);
                    accept(payload, "payload", visitor);
                    accept(header, "header", visitor, String.class);
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
            Channel other = (Channel) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(type, other.type) && 
                Objects.equals(endpoint, other.endpoint) && 
                Objects.equals(payload, other.payload) && 
                Objects.equals(header, other.header);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    type, 
                    endpoint, 
                    payload, 
                    header);
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
            private SubscriptionChannelType type;
            private Url endpoint;
            private Code payload;
            private List<String> header = new ArrayList<>();

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
             * The type of channel to send notifications on.
             * 
             * <p>This element is required.
             * 
             * @param type
             *     rest-hook | websocket | email | sms | message
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(SubscriptionChannelType type) {
                this.type = type;
                return this;
            }

            /**
             * The url that describes the actual end-point to send messages to.
             * 
             * @param endpoint
             *     Where the channel points to
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder endpoint(Url endpoint) {
                this.endpoint = endpoint;
                return this;
            }

            /**
             * The mime type to send the payload in - either application/fhir+xml, or application/fhir+json. If the payload is not 
             * present, then there is no payload in the notification, just a notification. The mime type "text/plain" may also be 
             * used for Email and SMS subscriptions.
             * 
             * @param payload
             *     MIME type to send, or omit for no payload
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder payload(Code payload) {
                this.payload = payload;
                return this;
            }

            /**
             * Convenience method for setting {@code header}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param header
             *     Usage depends on the channel type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #header(com.ibm.fhir.model.type.String)
             */
            public Builder header(java.lang.String... header) {
                for (java.lang.String value : header) {
                    this.header.add((value == null) ? null : String.of(value));
                }
                return this;
            }

            /**
             * Additional headers / information to send as part of the notification.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param header
             *     Usage depends on the channel type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder header(String... header) {
                for (String value : header) {
                    this.header.add(value);
                }
                return this;
            }

            /**
             * Additional headers / information to send as part of the notification.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param header
             *     Usage depends on the channel type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder header(Collection<String> header) {
                this.header = new ArrayList<>(header);
                return this;
            }

            /**
             * Build the {@link Channel}
             * 
             * <p>Required elements:
             * <ul>
             * <li>type</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Channel}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Channel per the base specification
             */
            @Override
            public Channel build() {
                Channel channel = new Channel(this);
                if (validating) {
                    validate(channel);
                }
                return channel;
            }

            protected void validate(Channel channel) {
                super.validate(channel);
                ValidationSupport.requireNonNull(channel.type, "type");
                ValidationSupport.checkList(channel.header, "header", String.class);
                ValidationSupport.requireValueOrChildren(channel);
            }

            protected Builder from(Channel channel) {
                super.from(channel);
                type = channel.type;
                endpoint = channel.endpoint;
                payload = channel.payload;
                header.addAll(channel.header);
                return this;
            }
        }
    }
}
