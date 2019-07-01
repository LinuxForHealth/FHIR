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
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.SubscriptionChannelType;
import com.ibm.watsonhealth.fhir.model.type.SubscriptionStatus;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.type.Url;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * The subscription resource is used to define a push-based subscription from a server to another system. Once a 
 * subscription is registered with the server, the server checks every resource that is created or updated, and if the 
 * resource matches the given criteria, it sends a message on the defined "channel" so that another system can take an 
 * appropriate action.
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Subscription extends DomainResource {
    private final SubscriptionStatus status;
    private final List<ContactPoint> contact;
    private final Instant end;
    private final String reason;
    private final String criteria;
    private final String error;
    private final Channel channel;

    private volatile int hashCode;

    private Subscription(Builder builder) {
        super(builder);
        status = ValidationSupport.requireNonNull(builder.status, "status");
        contact = Collections.unmodifiableList(builder.contact);
        end = builder.end;
        reason = ValidationSupport.requireNonNull(builder.reason, "reason");
        criteria = ValidationSupport.requireNonNull(builder.criteria, "criteria");
        error = builder.error;
        channel = ValidationSupport.requireNonNull(builder.channel, "channel");
    }

    /**
     * <p>
     * The status of the subscription, which marks the server state for managing the subscription.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link SubscriptionStatus}.
     */
    public SubscriptionStatus getStatus() {
        return status;
    }

    /**
     * <p>
     * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
     * troubleshooting.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link ContactPoint}.
     */
    public List<ContactPoint> getContact() {
        return contact;
    }

    /**
     * <p>
     * The time for the server to turn the subscription off.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Instant}.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * <p>
     * A description of why this subscription is defined.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getReason() {
        return reason;
    }

    /**
     * <p>
     * The rules that the server should use to determine when to generate notifications for this subscription.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * <p>
     * A record of the last error that occurred when the server processed a notification.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getError() {
        return error;
    }

    /**
     * <p>
     * Details where to send notifications when resources are received that meet the criteria.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Channel}.
     */
    public Channel getChannel() {
        return channel;
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
                accept(status, "status", visitor);
                accept(contact, "contact", visitor, ContactPoint.class);
                accept(end, "end", visitor);
                accept(reason, "reason", visitor);
                accept(criteria, "criteria", visitor);
                accept(error, "error", visitor);
                accept(channel, "channel", visitor);
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
        return new Builder(status, reason, criteria, channel).from(this);
    }

    public Builder toBuilder(SubscriptionStatus status, String reason, String criteria, Channel channel) {
        return new Builder(status, reason, criteria, channel).from(this);
    }

    public static Builder builder(SubscriptionStatus status, String reason, String criteria, Channel channel) {
        return new Builder(status, reason, criteria, channel);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final SubscriptionStatus status;
        private final String reason;
        private final String criteria;
        private final Channel channel;

        // optional
        private List<ContactPoint> contact = new ArrayList<>();
        private Instant end;
        private String error;

        private Builder(SubscriptionStatus status, String reason, String criteria, Channel channel) {
            super();
            this.status = status;
            this.reason = reason;
            this.criteria = criteria;
            this.channel = channel;
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
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * </p>
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(ContactPoint... contact) {
            for (ContactPoint value : contact) {
                this.contact.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Contact details for a human to contact about the subscription. The primary use of this for system administrator 
         * troubleshooting.
         * </p>
         * 
         * @param contact
         *     Contact details for source (e.g. troubleshooting)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder contact(Collection<ContactPoint> contact) {
            this.contact.addAll(contact);
            return this;
        }

        /**
         * <p>
         * The time for the server to turn the subscription off.
         * </p>
         * 
         * @param end
         *     When to automatically delete the subscription
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder end(Instant end) {
            this.end = end;
            return this;
        }

        /**
         * <p>
         * A record of the last error that occurred when the server processed a notification.
         * </p>
         * 
         * @param error
         *     Latest error note
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder error(String error) {
            this.error = error;
            return this;
        }

        @Override
        public Subscription build() {
            return new Subscription(this);
        }

        private Builder from(Subscription subscription) {
            id = subscription.id;
            meta = subscription.meta;
            implicitRules = subscription.implicitRules;
            language = subscription.language;
            text = subscription.text;
            contained.addAll(subscription.contained);
            extension.addAll(subscription.extension);
            modifierExtension.addAll(subscription.modifierExtension);
            contact.addAll(subscription.contact);
            end = subscription.end;
            error = subscription.error;
            return this;
        }
    }

    /**
     * <p>
     * Details where to send notifications when resources are received that meet the criteria.
     * </p>
     */
    public static class Channel extends BackboneElement {
        private final SubscriptionChannelType type;
        private final Url endpoint;
        private final Code payload;
        private final List<String> header;

        private volatile int hashCode;

        private Channel(Builder builder) {
            super(builder);
            type = ValidationSupport.requireNonNull(builder.type, "type");
            endpoint = builder.endpoint;
            payload = builder.payload;
            header = Collections.unmodifiableList(builder.header);
        }

        /**
         * <p>
         * The type of channel to send notifications on.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link SubscriptionChannelType}.
         */
        public SubscriptionChannelType getType() {
            return type;
        }

        /**
         * <p>
         * The url that describes the actual end-point to send messages to.
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
         * The mime type to send the payload in - either application/fhir+xml, or application/fhir+json. If the payload is not 
         * present, then there is no payload in the notification, just a notification. The mime type "text/plain" may also be 
         * used for Email and SMS subscriptions.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Code}.
         */
        public Code getPayload() {
            return payload;
        }

        /**
         * <p>
         * Additional headers / information to send as part of the notification.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link String}.
         */
        public List<String> getHeader() {
            return header;
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
                    accept(type, "type", visitor);
                    accept(endpoint, "endpoint", visitor);
                    accept(payload, "payload", visitor);
                    accept(header, "header", visitor, String.class);
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
            return new Builder(type).from(this);
        }

        public Builder toBuilder(SubscriptionChannelType type) {
            return new Builder(type).from(this);
        }

        public static Builder builder(SubscriptionChannelType type) {
            return new Builder(type);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final SubscriptionChannelType type;

            // optional
            private Url endpoint;
            private Code payload;
            private List<String> header = new ArrayList<>();

            private Builder(SubscriptionChannelType type) {
                super();
                this.type = type;
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
             * The url that describes the actual end-point to send messages to.
             * </p>
             * 
             * @param endpoint
             *     Where the channel points to
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder endpoint(Url endpoint) {
                this.endpoint = endpoint;
                return this;
            }

            /**
             * <p>
             * The mime type to send the payload in - either application/fhir+xml, or application/fhir+json. If the payload is not 
             * present, then there is no payload in the notification, just a notification. The mime type "text/plain" may also be 
             * used for Email and SMS subscriptions.
             * </p>
             * 
             * @param payload
             *     MIME type to send, or omit for no payload
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder payload(Code payload) {
                this.payload = payload;
                return this;
            }

            /**
             * <p>
             * Additional headers / information to send as part of the notification.
             * </p>
             * 
             * @param header
             *     Usage depends on the channel type
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder header(String... header) {
                for (String value : header) {
                    this.header.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Additional headers / information to send as part of the notification.
             * </p>
             * 
             * @param header
             *     Usage depends on the channel type
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder header(Collection<String> header) {
                this.header.addAll(header);
                return this;
            }

            @Override
            public Channel build() {
                return new Channel(this);
            }

            private Builder from(Channel channel) {
                id = channel.id;
                extension.addAll(channel.extension);
                modifierExtension.addAll(channel.modifierExtension);
                endpoint = channel.endpoint;
                payload = channel.payload;
                header.addAll(channel.header);
                return this;
            }
        }
    }
}
