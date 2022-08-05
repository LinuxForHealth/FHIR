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

@System("http://hl7.org/fhir/graph-compartment-rule")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class GraphCompartmentRule extends Code {
    /**
     * Identical
     * 
     * <p>The compartment must be identical (the same literal reference).
     */
    public static final GraphCompartmentRule IDENTICAL = GraphCompartmentRule.builder().value(Value.IDENTICAL).build();

    /**
     * Matching
     * 
     * <p>The compartment must be the same - the record must be about the same patient, but the reference may be different.
     */
    public static final GraphCompartmentRule MATCHING = GraphCompartmentRule.builder().value(Value.MATCHING).build();

    /**
     * Different
     * 
     * <p>The compartment must be different.
     */
    public static final GraphCompartmentRule DIFFERENT = GraphCompartmentRule.builder().value(Value.DIFFERENT).build();

    /**
     * Custom
     * 
     * <p>The compartment rule is defined in the accompanying FHIRPath expression.
     */
    public static final GraphCompartmentRule CUSTOM = GraphCompartmentRule.builder().value(Value.CUSTOM).build();

    private volatile int hashCode;

    private GraphCompartmentRule(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this GraphCompartmentRule as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating GraphCompartmentRule objects from a passed enum value.
     */
    public static GraphCompartmentRule of(Value value) {
        switch (value) {
        case IDENTICAL:
            return IDENTICAL;
        case MATCHING:
            return MATCHING;
        case DIFFERENT:
            return DIFFERENT;
        case CUSTOM:
            return CUSTOM;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating GraphCompartmentRule objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static GraphCompartmentRule of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating GraphCompartmentRule objects from a passed string value.
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
     * Inherited factory method for creating GraphCompartmentRule objects from a passed string value.
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
        GraphCompartmentRule other = (GraphCompartmentRule) obj;
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
         *     An enum constant for GraphCompartmentRule
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public GraphCompartmentRule build() {
            GraphCompartmentRule graphCompartmentRule = new GraphCompartmentRule(this);
            if (validating) {
                validate(graphCompartmentRule);
            }
            return graphCompartmentRule;
        }

        protected void validate(GraphCompartmentRule graphCompartmentRule) {
            super.validate(graphCompartmentRule);
        }

        protected Builder from(GraphCompartmentRule graphCompartmentRule) {
            super.from(graphCompartmentRule);
            return this;
        }
    }

    public enum Value {
        /**
         * Identical
         * 
         * <p>The compartment must be identical (the same literal reference).
         */
        IDENTICAL("identical"),

        /**
         * Matching
         * 
         * <p>The compartment must be the same - the record must be about the same patient, but the reference may be different.
         */
        MATCHING("matching"),

        /**
         * Different
         * 
         * <p>The compartment must be different.
         */
        DIFFERENT("different"),

        /**
         * Custom
         * 
         * <p>The compartment rule is defined in the accompanying FHIRPath expression.
         */
        CUSTOM("custom");

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
         * Factory method for creating GraphCompartmentRule.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding GraphCompartmentRule.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "identical":
                return IDENTICAL;
            case "matching":
                return MATCHING;
            case "different":
                return DIFFERENT;
            case "custom":
                return CUSTOM;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
