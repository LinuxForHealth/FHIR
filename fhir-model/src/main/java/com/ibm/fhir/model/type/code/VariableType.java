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

@System("http://hl7.org/fhir/variable-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class VariableType extends Code {
    /**
     * Dichotomous
     * 
     * <p>The variable is dichotomous, such as present or absent.
     */
    public static final VariableType DICHOTOMOUS = VariableType.builder().value(Value.DICHOTOMOUS).build();

    /**
     * Continuous
     * 
     * <p>The variable is a continuous result such as a quantity.
     */
    public static final VariableType CONTINUOUS = VariableType.builder().value(Value.CONTINUOUS).build();

    /**
     * Descriptive
     * 
     * <p>The variable is described narratively rather than quantitatively.
     */
    public static final VariableType DESCRIPTIVE = VariableType.builder().value(Value.DESCRIPTIVE).build();

    private volatile int hashCode;

    private VariableType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this VariableType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating VariableType objects from a passed enum value.
     */
    public static VariableType of(Value value) {
        switch (value) {
        case DICHOTOMOUS:
            return DICHOTOMOUS;
        case CONTINUOUS:
            return CONTINUOUS;
        case DESCRIPTIVE:
            return DESCRIPTIVE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating VariableType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static VariableType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating VariableType objects from a passed string value.
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
     * Inherited factory method for creating VariableType objects from a passed string value.
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
        VariableType other = (VariableType) obj;
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
         *     An enum constant for VariableType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public VariableType build() {
            VariableType variableType = new VariableType(this);
            if (validating) {
                validate(variableType);
            }
            return variableType;
        }

        protected void validate(VariableType variableType) {
            super.validate(variableType);
        }

        protected Builder from(VariableType variableType) {
            super.from(variableType);
            return this;
        }
    }

    public enum Value {
        /**
         * Dichotomous
         * 
         * <p>The variable is dichotomous, such as present or absent.
         */
        DICHOTOMOUS("dichotomous"),

        /**
         * Continuous
         * 
         * <p>The variable is a continuous result such as a quantity.
         */
        CONTINUOUS("continuous"),

        /**
         * Descriptive
         * 
         * <p>The variable is described narratively rather than quantitatively.
         */
        DESCRIPTIVE("descriptive");

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
         * Factory method for creating VariableType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding VariableType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "dichotomous":
                return DICHOTOMOUS;
            case "continuous":
                return CONTINUOUS;
            case "descriptive":
                return DESCRIPTIVE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
