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

@System("http://hl7.org/fhir/operation-parameter-use")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ParameterUse extends Code {
    /**
     * In
     * 
     * <p>This is an input parameter.
     */
    public static final ParameterUse IN = ParameterUse.builder().value(Value.IN).build();

    /**
     * Out
     * 
     * <p>This is an output parameter.
     */
    public static final ParameterUse OUT = ParameterUse.builder().value(Value.OUT).build();

    private volatile int hashCode;

    private ParameterUse(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ParameterUse as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ParameterUse objects from a passed enum value.
     */
    public static ParameterUse of(Value value) {
        switch (value) {
        case IN:
            return IN;
        case OUT:
            return OUT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ParameterUse objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ParameterUse of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ParameterUse objects from a passed string value.
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
     * Inherited factory method for creating ParameterUse objects from a passed string value.
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
        ParameterUse other = (ParameterUse) obj;
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
         *     An enum constant for ParameterUse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ParameterUse build() {
            ParameterUse parameterUse = new ParameterUse(this);
            if (validating) {
                validate(parameterUse);
            }
            return parameterUse;
        }

        protected void validate(ParameterUse parameterUse) {
            super.validate(parameterUse);
        }

        protected Builder from(ParameterUse parameterUse) {
            super.from(parameterUse);
            return this;
        }
    }

    public enum Value {
        /**
         * In
         * 
         * <p>This is an input parameter.
         */
        IN("in"),

        /**
         * Out
         * 
         * <p>This is an output parameter.
         */
        OUT("out");

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
         * Factory method for creating ParameterUse.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ParameterUse.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "in":
                return IN;
            case "out":
                return OUT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
