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

@System("http://hl7.org/fhir/endpoint-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EndpointStatus extends Code {
    /**
     * Active
     * 
     * <p>This endpoint is expected to be active and can be used.
     */
    public static final EndpointStatus ACTIVE = EndpointStatus.builder().value(Value.ACTIVE).build();

    /**
     * Suspended
     * 
     * <p>This endpoint is temporarily unavailable.
     */
    public static final EndpointStatus SUSPENDED = EndpointStatus.builder().value(Value.SUSPENDED).build();

    /**
     * Error
     * 
     * <p>This endpoint has exceeded connectivity thresholds and is considered in an error state and should no longer be 
     * attempted to connect to until corrective action is taken.
     */
    public static final EndpointStatus ERROR = EndpointStatus.builder().value(Value.ERROR).build();

    /**
     * Off
     * 
     * <p>This endpoint is no longer to be used.
     */
    public static final EndpointStatus OFF = EndpointStatus.builder().value(Value.OFF).build();

    /**
     * Entered in error
     * 
     * <p>This instance should not have been part of this patient's medical record.
     */
    public static final EndpointStatus ENTERED_IN_ERROR = EndpointStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Test
     * 
     * <p>This endpoint is not intended for production usage.
     */
    public static final EndpointStatus TEST = EndpointStatus.builder().value(Value.TEST).build();

    private volatile int hashCode;

    private EndpointStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this EndpointStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating EndpointStatus objects from a passed enum value.
     */
    public static EndpointStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case SUSPENDED:
            return SUSPENDED;
        case ERROR:
            return ERROR;
        case OFF:
            return OFF;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case TEST:
            return TEST;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EndpointStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static EndpointStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating EndpointStatus objects from a passed string value.
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
     * Inherited factory method for creating EndpointStatus objects from a passed string value.
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
        EndpointStatus other = (EndpointStatus) obj;
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
         *     An enum constant for EndpointStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public EndpointStatus build() {
            EndpointStatus endpointStatus = new EndpointStatus(this);
            if (validating) {
                validate(endpointStatus);
            }
            return endpointStatus;
        }

        protected void validate(EndpointStatus endpointStatus) {
            super.validate(endpointStatus);
        }

        protected Builder from(EndpointStatus endpointStatus) {
            super.from(endpointStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>This endpoint is expected to be active and can be used.
         */
        ACTIVE("active"),

        /**
         * Suspended
         * 
         * <p>This endpoint is temporarily unavailable.
         */
        SUSPENDED("suspended"),

        /**
         * Error
         * 
         * <p>This endpoint has exceeded connectivity thresholds and is considered in an error state and should no longer be 
         * attempted to connect to until corrective action is taken.
         */
        ERROR("error"),

        /**
         * Off
         * 
         * <p>This endpoint is no longer to be used.
         */
        OFF("off"),

        /**
         * Entered in error
         * 
         * <p>This instance should not have been part of this patient's medical record.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Test
         * 
         * <p>This endpoint is not intended for production usage.
         */
        TEST("test");

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
         * Factory method for creating EndpointStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding EndpointStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "active":
                return ACTIVE;
            case "suspended":
                return SUSPENDED;
            case "error":
                return ERROR;
            case "off":
                return OFF;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "test":
                return TEST;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
