/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/subscription-status")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SubscriptionStatusCode extends Code {
    /**
     * Requested
     * 
     * <p>The client has requested the subscription, and the server has not yet set it up.
     */
    public static final SubscriptionStatusCode REQUESTED = SubscriptionStatusCode.builder().value(Value.REQUESTED).build();

    /**
     * Active
     * 
     * <p>The subscription is active.
     */
    public static final SubscriptionStatusCode ACTIVE = SubscriptionStatusCode.builder().value(Value.ACTIVE).build();

    /**
     * Error
     * 
     * <p>The server has an error executing the notification.
     */
    public static final SubscriptionStatusCode ERROR = SubscriptionStatusCode.builder().value(Value.ERROR).build();

    /**
     * Off
     * 
     * <p>Too many errors have occurred or the subscription has expired.
     */
    public static final SubscriptionStatusCode OFF = SubscriptionStatusCode.builder().value(Value.OFF).build();

    private volatile int hashCode;

    private SubscriptionStatusCode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SubscriptionStatusCode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SubscriptionStatusCode objects from a passed enum value.
     */
    public static SubscriptionStatusCode of(Value value) {
        switch (value) {
        case REQUESTED:
            return REQUESTED;
        case ACTIVE:
            return ACTIVE;
        case ERROR:
            return ERROR;
        case OFF:
            return OFF;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SubscriptionStatusCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SubscriptionStatusCode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SubscriptionStatusCode objects from a passed string value.
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
     * Inherited factory method for creating SubscriptionStatusCode objects from a passed string value.
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
        SubscriptionStatusCode other = (SubscriptionStatusCode) obj;
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
         *     An enum constant for SubscriptionStatusCode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SubscriptionStatusCode build() {
            SubscriptionStatusCode subscriptionStatusCode = new SubscriptionStatusCode(this);
            if (validating) {
                validate(subscriptionStatusCode);
            }
            return subscriptionStatusCode;
        }

        protected void validate(SubscriptionStatusCode subscriptionStatusCode) {
            super.validate(subscriptionStatusCode);
        }

        protected Builder from(SubscriptionStatusCode subscriptionStatusCode) {
            super.from(subscriptionStatusCode);
            return this;
        }
    }

    public enum Value {
        /**
         * Requested
         * 
         * <p>The client has requested the subscription, and the server has not yet set it up.
         */
        REQUESTED("requested"),

        /**
         * Active
         * 
         * <p>The subscription is active.
         */
        ACTIVE("active"),

        /**
         * Error
         * 
         * <p>The server has an error executing the notification.
         */
        ERROR("error"),

        /**
         * Off
         * 
         * <p>Too many errors have occurred or the subscription has expired.
         */
        OFF("off");

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
         * Factory method for creating SubscriptionStatusCode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SubscriptionStatusCode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "requested":
                return REQUESTED;
            case "active":
                return ACTIVE;
            case "error":
                return ERROR;
            case "off":
                return OFF;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
