/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/subscription-notification-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubscriptionNotificationType extends Code {
    /**
     * Handshake
     * 
     * <p>The status was generated as part of the setup or verification of a communications channel.
     */
    public static final SubscriptionNotificationType HANDSHAKE = SubscriptionNotificationType.builder().value(Value.HANDSHAKE).build();

    /**
     * Heartbeat
     * 
     * <p>The status was generated to perform a heartbeat notification to the subscriber.
     */
    public static final SubscriptionNotificationType HEARTBEAT = SubscriptionNotificationType.builder().value(Value.HEARTBEAT).build();

    /**
     * Event Notification
     * 
     * <p>The status was generated for an event to the subscriber.
     */
    public static final SubscriptionNotificationType EVENT_NOTIFICATION = SubscriptionNotificationType.builder().value(Value.EVENT_NOTIFICATION).build();

    /**
     * Query Status
     * 
     * <p>The status was generated in response to a status query/request.
     */
    public static final SubscriptionNotificationType QUERY_STATUS = SubscriptionNotificationType.builder().value(Value.QUERY_STATUS).build();

    /**
     * Query Event
     * 
     * <p>The status was generated in response to an event query/request.
     */
    public static final SubscriptionNotificationType QUERY_EVENT = SubscriptionNotificationType.builder().value(Value.QUERY_EVENT).build();

    private volatile int hashCode;

    private SubscriptionNotificationType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SubscriptionNotificationType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SubscriptionNotificationType objects from a passed enum value.
     */
    public static SubscriptionNotificationType of(Value value) {
        switch (value) {
        case HANDSHAKE:
            return HANDSHAKE;
        case HEARTBEAT:
            return HEARTBEAT;
        case EVENT_NOTIFICATION:
            return EVENT_NOTIFICATION;
        case QUERY_STATUS:
            return QUERY_STATUS;
        case QUERY_EVENT:
            return QUERY_EVENT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SubscriptionNotificationType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SubscriptionNotificationType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SubscriptionNotificationType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SubscriptionNotificationType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
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
        SubscriptionNotificationType other = (SubscriptionNotificationType) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for SubscriptionNotificationType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SubscriptionNotificationType build() {
            SubscriptionNotificationType subscriptionNotificationType = new SubscriptionNotificationType(this);
            if (validating) {
                validate(subscriptionNotificationType);
            }
            return subscriptionNotificationType;
        }

        protected void validate(SubscriptionNotificationType subscriptionNotificationType) {
            super.validate(subscriptionNotificationType);
        }

        protected Builder from(SubscriptionNotificationType subscriptionNotificationType) {
            super.from(subscriptionNotificationType);
            return this;
        }
    }

    public enum Value {
        /**
         * Handshake
         * 
         * <p>The status was generated as part of the setup or verification of a communications channel.
         */
        HANDSHAKE("handshake"),

        /**
         * Heartbeat
         * 
         * <p>The status was generated to perform a heartbeat notification to the subscriber.
         */
        HEARTBEAT("heartbeat"),

        /**
         * Event Notification
         * 
         * <p>The status was generated for an event to the subscriber.
         */
        EVENT_NOTIFICATION("event-notification"),

        /**
         * Query Status
         * 
         * <p>The status was generated in response to a status query/request.
         */
        QUERY_STATUS("query-status"),

        /**
         * Query Event
         * 
         * <p>The status was generated in response to an event query/request.
         */
        QUERY_EVENT("query-event");

        private final java.lang.String value;

        Value(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating SubscriptionNotificationType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SubscriptionNotificationType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "handshake":
                return HANDSHAKE;
            case "heartbeat":
                return HEARTBEAT;
            case "event-notification":
                return EVENT_NOTIFICATION;
            case "query-status":
                return QUERY_STATUS;
            case "query-event":
                return QUERY_EVENT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
