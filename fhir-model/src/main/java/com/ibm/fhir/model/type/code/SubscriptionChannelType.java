/*
 * (C) Copyright IBM Corp. 2019, 2022
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

@System("http://hl7.org/fhir/subscription-channel-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubscriptionChannelType extends Code {
    /**
     * Rest Hook
     * 
     * <p>The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the 
     * service base, and an update (PUT) is made.
     */
    public static final SubscriptionChannelType REST_HOOK = SubscriptionChannelType.builder().value(Value.REST_HOOK).build();

    /**
     * Websocket
     * 
     * <p>The channel is executed by sending a packet across a web socket connection maintained by the client. The URL 
     * identifies the websocket, and the client binds to this URL.
     */
    public static final SubscriptionChannelType WEBSOCKET = SubscriptionChannelType.builder().value(Value.WEBSOCKET).build();

    /**
     * Email
     * 
     * <p>The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).
     */
    public static final SubscriptionChannelType EMAIL = SubscriptionChannelType.builder().value(Value.EMAIL).build();

    /**
     * SMS
     * 
     * <p>The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).
     */
    public static final SubscriptionChannelType SMS = SubscriptionChannelType.builder().value(Value.SMS).build();

    /**
     * Message
     * 
     * <p>The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application 
     * identified in the URI.
     */
    public static final SubscriptionChannelType MESSAGE = SubscriptionChannelType.builder().value(Value.MESSAGE).build();

    private volatile int hashCode;

    private SubscriptionChannelType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SubscriptionChannelType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SubscriptionChannelType objects from a passed enum value.
     */
    public static SubscriptionChannelType of(Value value) {
        switch (value) {
        case REST_HOOK:
            return REST_HOOK;
        case WEBSOCKET:
            return WEBSOCKET;
        case EMAIL:
            return EMAIL;
        case SMS:
            return SMS;
        case MESSAGE:
            return MESSAGE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SubscriptionChannelType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SubscriptionChannelType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SubscriptionChannelType objects from a passed string value.
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
     * Inherited factory method for creating SubscriptionChannelType objects from a passed string value.
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
        SubscriptionChannelType other = (SubscriptionChannelType) obj;
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
         *     An enum constant for SubscriptionChannelType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SubscriptionChannelType build() {
            SubscriptionChannelType subscriptionChannelType = new SubscriptionChannelType(this);
            if (validating) {
                validate(subscriptionChannelType);
            }
            return subscriptionChannelType;
        }

        protected void validate(SubscriptionChannelType subscriptionChannelType) {
            super.validate(subscriptionChannelType);
        }

        protected Builder from(SubscriptionChannelType subscriptionChannelType) {
            super.from(subscriptionChannelType);
            return this;
        }
    }

    public enum Value {
        /**
         * Rest Hook
         * 
         * <p>The channel is executed by making a post to the URI. If a payload is included, the URL is interpreted as the 
         * service base, and an update (PUT) is made.
         */
        REST_HOOK("rest-hook"),

        /**
         * Websocket
         * 
         * <p>The channel is executed by sending a packet across a web socket connection maintained by the client. The URL 
         * identifies the websocket, and the client binds to this URL.
         */
        WEBSOCKET("websocket"),

        /**
         * Email
         * 
         * <p>The channel is executed by sending an email to the email addressed in the URI (which must be a mailto:).
         */
        EMAIL("email"),

        /**
         * SMS
         * 
         * <p>The channel is executed by sending an SMS message to the phone number identified in the URL (tel:).
         */
        SMS("sms"),

        /**
         * Message
         * 
         * <p>The channel is executed by sending a message (e.g. a Bundle with a MessageHeader resource etc.) to the application 
         * identified in the URI.
         */
        MESSAGE("message");

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
         * Factory method for creating SubscriptionChannelType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SubscriptionChannelType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "rest-hook":
                return REST_HOOK;
            case "websocket":
                return WEBSOCKET;
            case "email":
                return EMAIL;
            case "sms":
                return SMS;
            case "message":
                return MESSAGE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
