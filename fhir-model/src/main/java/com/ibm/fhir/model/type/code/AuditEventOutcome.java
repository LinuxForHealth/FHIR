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

@System("http://hl7.org/fhir/audit-event-outcome")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AuditEventOutcome extends Code {
    /**
     * Success
     * 
     * <p>The operation completed successfully (whether with warnings or not).
     */
    public static final AuditEventOutcome OUTCOME_0 = AuditEventOutcome.builder().value(Value.OUTCOME_0).build();

    /**
     * Minor failure
     * 
     * <p>The action was not successful due to some kind of minor failure (often equivalent to an HTTP 400 response).
     */
    public static final AuditEventOutcome OUTCOME_4 = AuditEventOutcome.builder().value(Value.OUTCOME_4).build();

    /**
     * Serious failure
     * 
     * <p>The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).
     */
    public static final AuditEventOutcome OUTCOME_8 = AuditEventOutcome.builder().value(Value.OUTCOME_8).build();

    /**
     * Major failure
     * 
     * <p>An error of such magnitude occurred that the system is no longer available for use (i.e. the system died).
     */
    public static final AuditEventOutcome OUTCOME_12 = AuditEventOutcome.builder().value(Value.OUTCOME_12).build();

    private volatile int hashCode;

    private AuditEventOutcome(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AuditEventOutcome as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AuditEventOutcome objects from a passed enum value.
     */
    public static AuditEventOutcome of(Value value) {
        switch (value) {
        case OUTCOME_0:
            return OUTCOME_0;
        case OUTCOME_4:
            return OUTCOME_4;
        case OUTCOME_8:
            return OUTCOME_8;
        case OUTCOME_12:
            return OUTCOME_12;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AuditEventOutcome objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AuditEventOutcome of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AuditEventOutcome objects from a passed string value.
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
     * Inherited factory method for creating AuditEventOutcome objects from a passed string value.
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
        AuditEventOutcome other = (AuditEventOutcome) obj;
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
         *     An enum constant for AuditEventOutcome
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AuditEventOutcome build() {
            AuditEventOutcome auditEventOutcome = new AuditEventOutcome(this);
            if (validating) {
                validate(auditEventOutcome);
            }
            return auditEventOutcome;
        }

        protected void validate(AuditEventOutcome auditEventOutcome) {
            super.validate(auditEventOutcome);
        }

        protected Builder from(AuditEventOutcome auditEventOutcome) {
            super.from(auditEventOutcome);
            return this;
        }
    }

    public enum Value {
        /**
         * Success
         * 
         * <p>The operation completed successfully (whether with warnings or not).
         */
        OUTCOME_0("0"),

        /**
         * Minor failure
         * 
         * <p>The action was not successful due to some kind of minor failure (often equivalent to an HTTP 400 response).
         */
        OUTCOME_4("4"),

        /**
         * Serious failure
         * 
         * <p>The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).
         */
        OUTCOME_8("8"),

        /**
         * Major failure
         * 
         * <p>An error of such magnitude occurred that the system is no longer available for use (i.e. the system died).
         */
        OUTCOME_12("12");

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
         * Factory method for creating AuditEventOutcome.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AuditEventOutcome.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "0":
                return OUTCOME_0;
            case "4":
                return OUTCOME_4;
            case "8":
                return OUTCOME_8;
            case "12":
                return OUTCOME_12;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
