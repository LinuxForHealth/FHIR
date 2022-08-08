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

@System("http://hl7.org/fhir/type-derivation-rule")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class TypeDerivationRule extends Code {
    /**
     * Specialization
     * 
     * <p>This definition defines a new type that adds additional elements to the base type.
     */
    public static final TypeDerivationRule SPECIALIZATION = TypeDerivationRule.builder().value(Value.SPECIALIZATION).build();

    /**
     * Constraint
     * 
     * <p>This definition adds additional rules to an existing concrete type.
     */
    public static final TypeDerivationRule CONSTRAINT = TypeDerivationRule.builder().value(Value.CONSTRAINT).build();

    private volatile int hashCode;

    private TypeDerivationRule(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this TypeDerivationRule as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating TypeDerivationRule objects from a passed enum value.
     */
    public static TypeDerivationRule of(Value value) {
        switch (value) {
        case SPECIALIZATION:
            return SPECIALIZATION;
        case CONSTRAINT:
            return CONSTRAINT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating TypeDerivationRule objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static TypeDerivationRule of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating TypeDerivationRule objects from a passed string value.
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
     * Inherited factory method for creating TypeDerivationRule objects from a passed string value.
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
        TypeDerivationRule other = (TypeDerivationRule) obj;
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
         *     An enum constant for TypeDerivationRule
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public TypeDerivationRule build() {
            TypeDerivationRule typeDerivationRule = new TypeDerivationRule(this);
            if (validating) {
                validate(typeDerivationRule);
            }
            return typeDerivationRule;
        }

        protected void validate(TypeDerivationRule typeDerivationRule) {
            super.validate(typeDerivationRule);
        }

        protected Builder from(TypeDerivationRule typeDerivationRule) {
            super.from(typeDerivationRule);
            return this;
        }
    }

    public enum Value {
        /**
         * Specialization
         * 
         * <p>This definition defines a new type that adds additional elements to the base type.
         */
        SPECIALIZATION("specialization"),

        /**
         * Constraint
         * 
         * <p>This definition adds additional rules to an existing concrete type.
         */
        CONSTRAINT("constraint");

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
         * Factory method for creating TypeDerivationRule.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding TypeDerivationRule.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "specialization":
                return SPECIALIZATION;
            case "constraint":
                return CONSTRAINT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
