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

@System("http://hl7.org/fhir/verificationresult-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Status extends Code {
    /**
     * Attested
     * 
     * <p>***TODO***
     */
    public static final Status ATTESTED = Status.builder().value(Value.ATTESTED).build();

    /**
     * Validated
     * 
     * <p>***TODO***
     */
    public static final Status VALIDATED = Status.builder().value(Value.VALIDATED).build();

    /**
     * In process
     * 
     * <p>***TODO***
     */
    public static final Status IN_PROCESS = Status.builder().value(Value.IN_PROCESS).build();

    /**
     * Requires revalidation
     * 
     * <p>***TODO***
     */
    public static final Status REQ_REVALID = Status.builder().value(Value.REQ_REVALID).build();

    /**
     * Validation failed
     * 
     * <p>***TODO***
     */
    public static final Status VAL_FAIL = Status.builder().value(Value.VAL_FAIL).build();

    /**
     * Re-Validation failed
     * 
     * <p>***TODO***
     */
    public static final Status REVAL_FAIL = Status.builder().value(Value.REVAL_FAIL).build();

    private volatile int hashCode;

    private Status(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this Status as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating Status objects from a passed enum value.
     */
    public static Status of(Value value) {
        switch (value) {
        case ATTESTED:
            return ATTESTED;
        case VALIDATED:
            return VALIDATED;
        case IN_PROCESS:
            return IN_PROCESS;
        case REQ_REVALID:
            return REQ_REVALID;
        case VAL_FAIL:
            return VAL_FAIL;
        case REVAL_FAIL:
            return REVAL_FAIL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating Status objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Status of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating Status objects from a passed string value.
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
     * Inherited factory method for creating Status objects from a passed string value.
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
        Status other = (Status) obj;
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
         *     An enum constant for Status
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public Status build() {
            Status status = new Status(this);
            if (validating) {
                validate(status);
            }
            return status;
        }

        protected void validate(Status status) {
            super.validate(status);
        }

        protected Builder from(Status status) {
            super.from(status);
            return this;
        }
    }

    public enum Value {
        /**
         * Attested
         * 
         * <p>***TODO***
         */
        ATTESTED("attested"),

        /**
         * Validated
         * 
         * <p>***TODO***
         */
        VALIDATED("validated"),

        /**
         * In process
         * 
         * <p>***TODO***
         */
        IN_PROCESS("in-process"),

        /**
         * Requires revalidation
         * 
         * <p>***TODO***
         */
        REQ_REVALID("req-revalid"),

        /**
         * Validation failed
         * 
         * <p>***TODO***
         */
        VAL_FAIL("val-fail"),

        /**
         * Re-Validation failed
         * 
         * <p>***TODO***
         */
        REVAL_FAIL("reval-fail");

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
         * Factory method for creating Status.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding Status.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "attested":
                return ATTESTED;
            case "validated":
                return VALIDATED;
            case "in-process":
                return IN_PROCESS;
            case "req-revalid":
                return REQ_REVALID;
            case "val-fail":
                return VAL_FAIL;
            case "reval-fail":
                return REVAL_FAIL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
