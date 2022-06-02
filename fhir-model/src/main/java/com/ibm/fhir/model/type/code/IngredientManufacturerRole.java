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

@System("http://hl7.org/fhir/ingredient-manufacturer-role")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class IngredientManufacturerRole extends Code {
    /**
     * Manufacturer is specifically allowed for this ingredient
     */
    public static final IngredientManufacturerRole ALLOWED = IngredientManufacturerRole.builder().value(Value.ALLOWED).build();

    /**
     * Manufacturer is known to make this ingredient in general
     */
    public static final IngredientManufacturerRole POSSIBLE = IngredientManufacturerRole.builder().value(Value.POSSIBLE).build();

    /**
     * Manufacturer actually makes this particular ingredient
     */
    public static final IngredientManufacturerRole ACTUAL = IngredientManufacturerRole.builder().value(Value.ACTUAL).build();

    private volatile int hashCode;

    private IngredientManufacturerRole(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this IngredientManufacturerRole as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating IngredientManufacturerRole objects from a passed enum value.
     */
    public static IngredientManufacturerRole of(Value value) {
        switch (value) {
        case ALLOWED:
            return ALLOWED;
        case POSSIBLE:
            return POSSIBLE;
        case ACTUAL:
            return ACTUAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating IngredientManufacturerRole objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static IngredientManufacturerRole of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating IngredientManufacturerRole objects from a passed string value.
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
     * Inherited factory method for creating IngredientManufacturerRole objects from a passed string value.
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
        IngredientManufacturerRole other = (IngredientManufacturerRole) obj;
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
         *     An enum constant for IngredientManufacturerRole
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public IngredientManufacturerRole build() {
            IngredientManufacturerRole ingredientManufacturerRole = new IngredientManufacturerRole(this);
            if (validating) {
                validate(ingredientManufacturerRole);
            }
            return ingredientManufacturerRole;
        }

        protected void validate(IngredientManufacturerRole ingredientManufacturerRole) {
            super.validate(ingredientManufacturerRole);
        }

        protected Builder from(IngredientManufacturerRole ingredientManufacturerRole) {
            super.from(ingredientManufacturerRole);
            return this;
        }
    }

    public enum Value {
        /**
         * Manufacturer is specifically allowed for this ingredient
         */
        ALLOWED("allowed"),

        /**
         * Manufacturer is known to make this ingredient in general
         */
        POSSIBLE("possible"),

        /**
         * Manufacturer actually makes this particular ingredient
         */
        ACTUAL("actual");

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
         * Factory method for creating IngredientManufacturerRole.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding IngredientManufacturerRole.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "allowed":
                return ALLOWED;
            case "possible":
                return POSSIBLE;
            case "actual":
                return ACTUAL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
