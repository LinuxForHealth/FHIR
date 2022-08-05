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

@System("http://hl7.org/fhir/product-category")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class BiologicallyDerivedProductCategory extends Code {
    /**
     * Organ
     * 
     * <p>A collection of tissues joined in a structural unit to serve a common function.
     */
    public static final BiologicallyDerivedProductCategory ORGAN = BiologicallyDerivedProductCategory.builder().value(Value.ORGAN).build();

    /**
     * Tissue
     * 
     * <p>An ensemble of similar cells and their extracellular matrix from the same origin that together carry out a specific 
     * function.
     */
    public static final BiologicallyDerivedProductCategory TISSUE = BiologicallyDerivedProductCategory.builder().value(Value.TISSUE).build();

    /**
     * Fluid
     * 
     * <p>Body fluid.
     */
    public static final BiologicallyDerivedProductCategory FLUID = BiologicallyDerivedProductCategory.builder().value(Value.FLUID).build();

    /**
     * Cells
     * 
     * <p>Collection of cells.
     */
    public static final BiologicallyDerivedProductCategory CELLS = BiologicallyDerivedProductCategory.builder().value(Value.CELLS).build();

    /**
     * BiologicalAgent
     * 
     * <p>Biological agent of unspecified type.
     */
    public static final BiologicallyDerivedProductCategory BIOLOGICAL_AGENT = BiologicallyDerivedProductCategory.builder().value(Value.BIOLOGICAL_AGENT).build();

    private volatile int hashCode;

    private BiologicallyDerivedProductCategory(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this BiologicallyDerivedProductCategory as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating BiologicallyDerivedProductCategory objects from a passed enum value.
     */
    public static BiologicallyDerivedProductCategory of(Value value) {
        switch (value) {
        case ORGAN:
            return ORGAN;
        case TISSUE:
            return TISSUE;
        case FLUID:
            return FLUID;
        case CELLS:
            return CELLS;
        case BIOLOGICAL_AGENT:
            return BIOLOGICAL_AGENT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating BiologicallyDerivedProductCategory objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static BiologicallyDerivedProductCategory of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating BiologicallyDerivedProductCategory objects from a passed string value.
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
     * Inherited factory method for creating BiologicallyDerivedProductCategory objects from a passed string value.
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
        BiologicallyDerivedProductCategory other = (BiologicallyDerivedProductCategory) obj;
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
         *     An enum constant for BiologicallyDerivedProductCategory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public BiologicallyDerivedProductCategory build() {
            BiologicallyDerivedProductCategory biologicallyDerivedProductCategory = new BiologicallyDerivedProductCategory(this);
            if (validating) {
                validate(biologicallyDerivedProductCategory);
            }
            return biologicallyDerivedProductCategory;
        }

        protected void validate(BiologicallyDerivedProductCategory biologicallyDerivedProductCategory) {
            super.validate(biologicallyDerivedProductCategory);
        }

        protected Builder from(BiologicallyDerivedProductCategory biologicallyDerivedProductCategory) {
            super.from(biologicallyDerivedProductCategory);
            return this;
        }
    }

    public enum Value {
        /**
         * Organ
         * 
         * <p>A collection of tissues joined in a structural unit to serve a common function.
         */
        ORGAN("organ"),

        /**
         * Tissue
         * 
         * <p>An ensemble of similar cells and their extracellular matrix from the same origin that together carry out a specific 
         * function.
         */
        TISSUE("tissue"),

        /**
         * Fluid
         * 
         * <p>Body fluid.
         */
        FLUID("fluid"),

        /**
         * Cells
         * 
         * <p>Collection of cells.
         */
        CELLS("cells"),

        /**
         * BiologicalAgent
         * 
         * <p>Biological agent of unspecified type.
         */
        BIOLOGICAL_AGENT("biologicalAgent");

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
         * Factory method for creating BiologicallyDerivedProductCategory.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding BiologicallyDerivedProductCategory.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "organ":
                return ORGAN;
            case "tissue":
                return TISSUE;
            case "fluid":
                return FLUID;
            case "cells":
                return CELLS;
            case "biologicalAgent":
                return BIOLOGICAL_AGENT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
