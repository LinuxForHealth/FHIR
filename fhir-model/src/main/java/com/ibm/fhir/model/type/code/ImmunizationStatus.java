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

@System("http://hl7.org/fhir/event-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ImmunizationStatus extends Code {
    public static final ImmunizationStatus COMPLETED = ImmunizationStatus.builder().value(Value.COMPLETED).build();

    public static final ImmunizationStatus ENTERED_IN_ERROR = ImmunizationStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    public static final ImmunizationStatus NOT_DONE = ImmunizationStatus.builder().value(Value.NOT_DONE).build();

    private volatile int hashCode;

    private ImmunizationStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ImmunizationStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ImmunizationStatus objects from a passed enum value.
     */
    public static ImmunizationStatus of(Value value) {
        switch (value) {
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case NOT_DONE:
            return NOT_DONE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ImmunizationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ImmunizationStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ImmunizationStatus objects from a passed string value.
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
     * Inherited factory method for creating ImmunizationStatus objects from a passed string value.
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
        ImmunizationStatus other = (ImmunizationStatus) obj;
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
         *     An enum constant for ImmunizationStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ImmunizationStatus build() {
            ImmunizationStatus immunizationStatus = new ImmunizationStatus(this);
            if (validating) {
                validate(immunizationStatus);
            }
            return immunizationStatus;
        }

        protected void validate(ImmunizationStatus immunizationStatus) {
            super.validate(immunizationStatus);
        }

        protected Builder from(ImmunizationStatus immunizationStatus) {
            super.from(immunizationStatus);
            return this;
        }
    }

    public enum Value {
        COMPLETED("completed"),

        ENTERED_IN_ERROR("entered-in-error"),

        NOT_DONE("not-done");

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
         * Factory method for creating ImmunizationStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ImmunizationStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "completed":
                return COMPLETED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "not-done":
                return NOT_DONE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
