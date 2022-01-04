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
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.type.code.SubscriptionNotificationType;
import com.ibm.fhir.model.type.code.SubscriptionStatusCode;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The SubscriptionStatus resource describes the state of a Subscription during notifications.
 * 
 * <p>Maturity level: FMM0 (draft)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.Value.DRAFT
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubscriptionStatus extends DomainResource {
    @Summary
    @Binding(
        bindingName = "SubscriptionStatus",
        strength = BindingStrength.Value.REQUIRED,
        description = "The status of a subscription at the time this notification was generated.",
        valueSet = "http://hl7.org/fhir/ValueSet/subscription-status|4.3.0-CIBUILD"
    )
    private final SubscriptionStatusCode status;
    @Summary
    @Binding(
        bindingName = "SubscriptionNotificationType",
        strength = BindingStrength.Value.REQUIRED,
        description = "The type of notification represented by the status message.",
        valueSet = "http://hl7.org/fhir/ValueSet/subscription-notification-type|4.3.0-CIBUILD"
    )
    @Required
    private final SubscriptionNotificationType type;
    @Summary
    private final String eventsSinceSubscriptionStart;
    @Summary
    private final Integer eventsInNotification;
    private final List<NotificationEvent> notificationEvent;
    @Summary
    @ReferenceTarget({ "Subscription" })
    @Required
    private final Reference subscription;
    @Summary
    private final Canonical topic;
    @Summary
    @Binding(
        bindingName = "SubscriptionError",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Codes to represent subscription error details.",
        valueSet = "http://hl7.org/fhir/ValueSet/subscription-error"
    )
    private final List<CodeableConcept> error;

    private SubscriptionStatus(Builder builder) {
        super(builder);
        status = builder.status;
        type = builder.type;
        eventsSinceSubscriptionStart = builder.eventsSinceSubscriptionStart;
        eventsInNotification = builder.eventsInNotification;
        notificationEvent = Collections.unmodifiableList(builder.notificationEvent);
        subscription = builder.subscription;
        topic = builder.topic;
        error = Collections.unmodifiableList(builder.error);
    }

    /**
     * The status of the subscription, which marks the server state for managing the subscription.
     * 
     * @return
     *     An immutable object of type {@link SubscriptionStatusCode} that may be null.
     */
    public SubscriptionStatusCode getStatus() {
        return status;
    }

    /**
     * The type of event being conveyed with this notificaiton.
     * 
     * @return
     *     An immutable object of type {@link SubscriptionNotificationType} that is non-null.
     */
    public SubscriptionNotificationType getType() {
        return type;
    }

    /**
     * The total number of actual events which have been generated since the Subscription was created (inclusive of this 
     * notification) - regardless of how many have been successfully communicated. This number is NOT incremented for 
     * handshake and heartbeat notifications.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getEventsSinceSubscriptionStart() {
        return eventsSinceSubscriptionStart;
    }

    /**
     * The total number of actual events represented within this notification. For handshake and heartbeat notifications, 
     * this will be zero or not present. For event-notifications, this number may be one or more, depending on server 
     * batching.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getEventsInNotification() {
        return eventsInNotification;
    }

    /**
     * Detailed information about events relevant to this subscription notification.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link NotificationEvent} that may be empty.
     */
    public List<NotificationEvent> getNotificationEvent() {
        return notificationEvent;
    }

    /**
     * The reference to the Subscription which generated this notification.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubscription() {
        return subscription;
    }

    /**
     * The reference to the SubscriptionTopic for the Subscription which generated this notification.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getTopic() {
        return topic;
    }

    /**
     * A record of errors that occurred when the server processed a notification.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getError() {
        return error;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (status != null) || 
            (type != null) || 
            (eventsSinceSubscriptionStart != null) || 
            (eventsInNotification != null) || 
            !notificationEvent.isEmpty() || 
            (subscription != null) || 
            (topic != null) || 
            !error.isEmpty();
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
                accept(type, "type", visitor);
                accept(eventsSinceSubscriptionStart, "eventsSinceSubscriptionStart", visitor);
                accept(eventsInNotification, "eventsInNotification", visitor);
                accept(notificationEvent, "notificationEvent", visitor, NotificationEvent.class);
                accept(subscription, "subscription", visitor);
                accept(topic, "topic", visitor);
                accept(error, "error", visitor, CodeableConcept.class);
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
        SubscriptionStatus other = (SubscriptionStatus) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(status, other.status) && 
            Objects.equals(type, other.type) && 
            Objects.equals(eventsSinceSubscriptionStart, other.eventsSinceSubscriptionStart) && 
            Objects.equals(eventsInNotification, other.eventsInNotification) && 
            Objects.equals(notificationEvent, other.notificationEvent) && 
            Objects.equals(subscription, other.subscription) && 
            Objects.equals(topic, other.topic) && 
            Objects.equals(error, other.error);
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
                type, 
                eventsSinceSubscriptionStart, 
                eventsInNotification, 
                notificationEvent, 
                subscription, 
                topic, 
                error);
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
        private SubscriptionNotificationType type;
        private String eventsSinceSubscriptionStart;
        private Integer eventsInNotification;
        private List<NotificationEvent> notificationEvent = new ArrayList<>();
        private Reference subscription;
        private Canonical topic;
        private List<CodeableConcept> error = new ArrayList<>();

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
         * @param status
         *     requested | active | error | off | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(SubscriptionStatusCode status) {
            this.status = status;
            return this;
        }

        /**
         * The type of event being conveyed with this notificaiton.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     handshake | heartbeat | event-notification | query-status | query-event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(SubscriptionNotificationType type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code eventsSinceSubscriptionStart}.
         * 
         * @param eventsSinceSubscriptionStart
         *     Events since the Subscription was created
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #eventsSinceSubscriptionStart(com.ibm.fhir.model.type.String)
         */
        public Builder eventsSinceSubscriptionStart(java.lang.String eventsSinceSubscriptionStart) {
            this.eventsSinceSubscriptionStart = (eventsSinceSubscriptionStart == null) ? null : String.of(eventsSinceSubscriptionStart);
            return this;
        }

        /**
         * The total number of actual events which have been generated since the Subscription was created (inclusive of this 
         * notification) - regardless of how many have been successfully communicated. This number is NOT incremented for 
         * handshake and heartbeat notifications.
         * 
         * @param eventsSinceSubscriptionStart
         *     Events since the Subscription was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eventsSinceSubscriptionStart(String eventsSinceSubscriptionStart) {
            this.eventsSinceSubscriptionStart = eventsSinceSubscriptionStart;
            return this;
        }

        /**
         * Convenience method for setting {@code eventsInNotification}.
         * 
         * @param eventsInNotification
         *     The number of actual notifications represented by this bundle
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #eventsInNotification(com.ibm.fhir.model.type.Integer)
         */
        public Builder eventsInNotification(java.lang.Integer eventsInNotification) {
            this.eventsInNotification = (eventsInNotification == null) ? null : Integer.of(eventsInNotification);
            return this;
        }

        /**
         * The total number of actual events represented within this notification. For handshake and heartbeat notifications, 
         * this will be zero or not present. For event-notifications, this number may be one or more, depending on server 
         * batching.
         * 
         * @param eventsInNotification
         *     The number of actual notifications represented by this bundle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder eventsInNotification(Integer eventsInNotification) {
            this.eventsInNotification = eventsInNotification;
            return this;
        }

        /**
         * Detailed information about events relevant to this subscription notification.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param notificationEvent
         *     Detailed information about any events relevant to this notification
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder notificationEvent(NotificationEvent... notificationEvent) {
            for (NotificationEvent value : notificationEvent) {
                this.notificationEvent.add(value);
            }
            return this;
        }

        /**
         * Detailed information about events relevant to this subscription notification.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param notificationEvent
         *     Detailed information about any events relevant to this notification
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder notificationEvent(Collection<NotificationEvent> notificationEvent) {
            this.notificationEvent = new ArrayList<>(notificationEvent);
            return this;
        }

        /**
         * The reference to the Subscription which generated this notification.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Subscription}</li>
         * </ul>
         * 
         * @param subscription
         *     Reference to the Subscription responsible for this notification
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subscription(Reference subscription) {
            this.subscription = subscription;
            return this;
        }

        /**
         * The reference to the SubscriptionTopic for the Subscription which generated this notification.
         * 
         * @param topic
         *     Reference to the SubscriptionTopic this notification relates to
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder topic(Canonical topic) {
            this.topic = topic;
            return this;
        }

        /**
         * A record of errors that occurred when the server processed a notification.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param error
         *     List of errors on the subscription
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder error(CodeableConcept... error) {
            for (CodeableConcept value : error) {
                this.error.add(value);
            }
            return this;
        }

        /**
         * A record of errors that occurred when the server processed a notification.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param error
         *     List of errors on the subscription
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder error(Collection<CodeableConcept> error) {
            this.error = new ArrayList<>(error);
            return this;
        }

        /**
         * Build the {@link SubscriptionStatus}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>subscription</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link SubscriptionStatus}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubscriptionStatus per the base specification
         */
        @Override
        public SubscriptionStatus build() {
            SubscriptionStatus subscriptionStatus = new SubscriptionStatus(this);
            if (validating) {
                validate(subscriptionStatus);
            }
            return subscriptionStatus;
        }

        protected void validate(SubscriptionStatus subscriptionStatus) {
            super.validate(subscriptionStatus);
            ValidationSupport.requireNonNull(subscriptionStatus.type, "type");
            ValidationSupport.checkList(subscriptionStatus.notificationEvent, "notificationEvent", NotificationEvent.class);
            ValidationSupport.requireNonNull(subscriptionStatus.subscription, "subscription");
            ValidationSupport.checkList(subscriptionStatus.error, "error", CodeableConcept.class);
            ValidationSupport.checkReferenceType(subscriptionStatus.subscription, "subscription", "Subscription");
        }

        protected Builder from(SubscriptionStatus subscriptionStatus) {
            super.from(subscriptionStatus);
            status = subscriptionStatus.status;
            type = subscriptionStatus.type;
            eventsSinceSubscriptionStart = subscriptionStatus.eventsSinceSubscriptionStart;
            eventsInNotification = subscriptionStatus.eventsInNotification;
            notificationEvent.addAll(subscriptionStatus.notificationEvent);
            subscription = subscriptionStatus.subscription;
            topic = subscriptionStatus.topic;
            error.addAll(subscriptionStatus.error);
            return this;
        }
    }

    /**
     * Detailed information about events relevant to this subscription notification.
     */
    public static class NotificationEvent extends BackboneElement {
        @Required
        private final String eventNumber;
        private final Instant timestamp;
        private final Reference focus;
        private final List<Reference> additionalContext;

        private NotificationEvent(Builder builder) {
            super(builder);
            eventNumber = builder.eventNumber;
            timestamp = builder.timestamp;
            focus = builder.focus;
            additionalContext = Collections.unmodifiableList(builder.additionalContext);
        }

        /**
         * The sequential number of this event in this subscription context. Note that this value is a 64-bit integer value, 
         * encoded as a string.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getEventNumber() {
            return eventNumber;
        }

        /**
         * The actual time this event occured on the server.
         * 
         * @return
         *     An immutable object of type {@link Instant} that may be null.
         */
        public Instant getTimestamp() {
            return timestamp;
        }

        /**
         * The focus of this event. While this will usually be a reference to the focus resource of the event, it MAY contain a 
         * reference to a non-FHIR object.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getFocus() {
            return focus;
        }

        /**
         * Additional context information for this event. Generally, this will contain references to additional resources 
         * included with the event (e.g., the Patient relevant to an Encounter), however it MAY refer to non-FHIR objects.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getAdditionalContext() {
            return additionalContext;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (eventNumber != null) || 
                (timestamp != null) || 
                (focus != null) || 
                !additionalContext.isEmpty();
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
                    accept(eventNumber, "eventNumber", visitor);
                    accept(timestamp, "timestamp", visitor);
                    accept(focus, "focus", visitor);
                    accept(additionalContext, "additionalContext", visitor, Reference.class);
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
            NotificationEvent other = (NotificationEvent) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(eventNumber, other.eventNumber) && 
                Objects.equals(timestamp, other.timestamp) && 
                Objects.equals(focus, other.focus) && 
                Objects.equals(additionalContext, other.additionalContext);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    eventNumber, 
                    timestamp, 
                    focus, 
                    additionalContext);
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
            private String eventNumber;
            private Instant timestamp;
            private Reference focus;
            private List<Reference> additionalContext = new ArrayList<>();

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
             * Convenience method for setting {@code eventNumber}.
             * 
             * <p>This element is required.
             * 
             * @param eventNumber
             *     Event number
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #eventNumber(com.ibm.fhir.model.type.String)
             */
            public Builder eventNumber(java.lang.String eventNumber) {
                this.eventNumber = (eventNumber == null) ? null : String.of(eventNumber);
                return this;
            }

            /**
             * The sequential number of this event in this subscription context. Note that this value is a 64-bit integer value, 
             * encoded as a string.
             * 
             * <p>This element is required.
             * 
             * @param eventNumber
             *     Event number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder eventNumber(String eventNumber) {
                this.eventNumber = eventNumber;
                return this;
            }

            /**
             * Convenience method for setting {@code timestamp}.
             * 
             * @param timestamp
             *     The instant this event occurred
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #timestamp(com.ibm.fhir.model.type.Instant)
             */
            public Builder timestamp(java.time.ZonedDateTime timestamp) {
                this.timestamp = (timestamp == null) ? null : Instant.of(timestamp);
                return this;
            }

            /**
             * The actual time this event occured on the server.
             * 
             * @param timestamp
             *     The instant this event occurred
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timestamp(Instant timestamp) {
                this.timestamp = timestamp;
                return this;
            }

            /**
             * The focus of this event. While this will usually be a reference to the focus resource of the event, it MAY contain a 
             * reference to a non-FHIR object.
             * 
             * @param focus
             *     The focus of this event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focus(Reference focus) {
                this.focus = focus;
                return this;
            }

            /**
             * Additional context information for this event. Generally, this will contain references to additional resources 
             * included with the event (e.g., the Patient relevant to an Encounter), however it MAY refer to non-FHIR objects.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param additionalContext
             *     Additional context for this event
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder additionalContext(Reference... additionalContext) {
                for (Reference value : additionalContext) {
                    this.additionalContext.add(value);
                }
                return this;
            }

            /**
             * Additional context information for this event. Generally, this will contain references to additional resources 
             * included with the event (e.g., the Patient relevant to an Encounter), however it MAY refer to non-FHIR objects.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param additionalContext
             *     Additional context for this event
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder additionalContext(Collection<Reference> additionalContext) {
                this.additionalContext = new ArrayList<>(additionalContext);
                return this;
            }

            /**
             * Build the {@link NotificationEvent}
             * 
             * <p>Required elements:
             * <ul>
             * <li>eventNumber</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link NotificationEvent}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid NotificationEvent per the base specification
             */
            @Override
            public NotificationEvent build() {
                NotificationEvent notificationEvent = new NotificationEvent(this);
                if (validating) {
                    validate(notificationEvent);
                }
                return notificationEvent;
            }

            protected void validate(NotificationEvent notificationEvent) {
                super.validate(notificationEvent);
                ValidationSupport.requireNonNull(notificationEvent.eventNumber, "eventNumber");
                ValidationSupport.checkList(notificationEvent.additionalContext, "additionalContext", Reference.class);
                ValidationSupport.requireValueOrChildren(notificationEvent);
            }

            protected Builder from(NotificationEvent notificationEvent) {
                super.from(notificationEvent);
                eventNumber = notificationEvent.eventNumber;
                timestamp = notificationEvent.timestamp;
                focus = notificationEvent.focus;
                additionalContext.addAll(notificationEvent.additionalContext);
                return this;
            }
        }
    }
}
