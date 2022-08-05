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

@System("http://hl7.org/fhir/constraint-severity")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ConstraintSeverity extends Code {
    /**
     * Error
     * 
     * <p>If the constraint is violated, the resource is not conformant.
     */
    public static final ConstraintSeverity ERROR = ConstraintSeverity.builder().value(Value.ERROR).build();

    /**
     * Warning
     * 
     * <p>If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.
     */
    public static final ConstraintSeverity WARNING = ConstraintSeverity.builder().value(Value.WARNING).build();

    private volatile int hashCode;

    private ConstraintSeverity(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ConstraintSeverity as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ConstraintSeverity objects from a passed enum value.
     */
    public static ConstraintSeverity of(Value value) {
        switch (value) {
        case ERROR:
            return ERROR;
        case WARNING:
            return WARNING;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ConstraintSeverity objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ConstraintSeverity of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ConstraintSeverity objects from a passed string value.
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
     * Inherited factory method for creating ConstraintSeverity objects from a passed string value.
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
        ConstraintSeverity other = (ConstraintSeverity) obj;
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
         *     An enum constant for ConstraintSeverity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ConstraintSeverity build() {
            ConstraintSeverity constraintSeverity = new ConstraintSeverity(this);
            if (validating) {
                validate(constraintSeverity);
            }
            return constraintSeverity;
        }

        protected void validate(ConstraintSeverity constraintSeverity) {
            super.validate(constraintSeverity);
        }

        protected Builder from(ConstraintSeverity constraintSeverity) {
            super.from(constraintSeverity);
            return this;
        }
    }

    public enum Value {
        /**
         * Error
         * 
         * <p>If the constraint is violated, the resource is not conformant.
         */
        ERROR("error"),

        /**
         * Warning
         * 
         * <p>If the constraint is violated, the resource is conformant, but it is not necessarily following best practice.
         */
        WARNING("warning");

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
         * Factory method for creating ConstraintSeverity.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ConstraintSeverity.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "error":
                return ERROR;
            case "warning":
                return WARNING;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
