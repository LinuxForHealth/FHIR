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

@System("http://hl7.org/fhir/device-statement-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceUseStatementStatus extends Code {
    /**
     * Active
     * 
     * <p>The device is still being used.
     */
    public static final DeviceUseStatementStatus ACTIVE = DeviceUseStatementStatus.builder().value(Value.ACTIVE).build();

    /**
     * Completed
     * 
     * <p>The device is no longer being used.
     */
    public static final DeviceUseStatementStatus COMPLETED = DeviceUseStatementStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>The statement was recorded incorrectly.
     */
    public static final DeviceUseStatementStatus ENTERED_IN_ERROR = DeviceUseStatementStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Intended
     * 
     * <p>The device may be used at some time in the future.
     */
    public static final DeviceUseStatementStatus INTENDED = DeviceUseStatementStatus.builder().value(Value.INTENDED).build();

    /**
     * Stopped
     * 
     * <p>Actions implied by the statement have been permanently halted, before all of them occurred.
     */
    public static final DeviceUseStatementStatus STOPPED = DeviceUseStatementStatus.builder().value(Value.STOPPED).build();

    /**
     * On Hold
     * 
     * <p>Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be 
     * called "suspended".
     */
    public static final DeviceUseStatementStatus ON_HOLD = DeviceUseStatementStatus.builder().value(Value.ON_HOLD).build();

    private volatile int hashCode;

    private DeviceUseStatementStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DeviceUseStatementStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DeviceUseStatementStatus objects from a passed enum value.
     */
    public static DeviceUseStatementStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case INTENDED:
            return INTENDED;
        case STOPPED:
            return STOPPED;
        case ON_HOLD:
            return ON_HOLD;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceUseStatementStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceUseStatementStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DeviceUseStatementStatus objects from a passed string value.
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
     * Inherited factory method for creating DeviceUseStatementStatus objects from a passed string value.
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
        DeviceUseStatementStatus other = (DeviceUseStatementStatus) obj;
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
         *     An enum constant for DeviceUseStatementStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DeviceUseStatementStatus build() {
            DeviceUseStatementStatus deviceUseStatementStatus = new DeviceUseStatementStatus(this);
            if (validating) {
                validate(deviceUseStatementStatus);
            }
            return deviceUseStatementStatus;
        }

        protected void validate(DeviceUseStatementStatus deviceUseStatementStatus) {
            super.validate(deviceUseStatementStatus);
        }

        protected Builder from(DeviceUseStatementStatus deviceUseStatementStatus) {
            super.from(deviceUseStatementStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>The device is still being used.
         */
        ACTIVE("active"),

        /**
         * Completed
         * 
         * <p>The device is no longer being used.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>The statement was recorded incorrectly.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Intended
         * 
         * <p>The device may be used at some time in the future.
         */
        INTENDED("intended"),

        /**
         * Stopped
         * 
         * <p>Actions implied by the statement have been permanently halted, before all of them occurred.
         */
        STOPPED("stopped"),

        /**
         * On Hold
         * 
         * <p>Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be 
         * called "suspended".
         */
        ON_HOLD("on-hold");

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
         * Factory method for creating DeviceUseStatementStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DeviceUseStatementStatus.Value or null if a null value was passed
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
            case "completed":
                return COMPLETED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "intended":
                return INTENDED;
            case "stopped":
                return STOPPED;
            case "on-hold":
                return ON_HOLD;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
