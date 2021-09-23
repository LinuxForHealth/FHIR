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

@System("http://hl7.org/fhir/product-storage-scale")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class BiologicallyDerivedProductStorageScale extends Code {
    /**
     * Fahrenheit
     * 
     * <p>Fahrenheit temperature scale.
     */
    public static final BiologicallyDerivedProductStorageScale FARENHEIT = BiologicallyDerivedProductStorageScale.builder().value(Value.FARENHEIT).build();

    /**
     * Celsius
     * 
     * <p>Celsius or centigrade temperature scale.
     */
    public static final BiologicallyDerivedProductStorageScale CELSIUS = BiologicallyDerivedProductStorageScale.builder().value(Value.CELSIUS).build();

    /**
     * Kelvin
     * 
     * <p>Kelvin absolute thermodynamic temperature scale.
     */
    public static final BiologicallyDerivedProductStorageScale KELVIN = BiologicallyDerivedProductStorageScale.builder().value(Value.KELVIN).build();

    private volatile int hashCode;

    private BiologicallyDerivedProductStorageScale(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this BiologicallyDerivedProductStorageScale as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating BiologicallyDerivedProductStorageScale objects from a passed enum value.
     */
    public static BiologicallyDerivedProductStorageScale of(Value value) {
        switch (value) {
        case FARENHEIT:
            return FARENHEIT;
        case CELSIUS:
            return CELSIUS;
        case KELVIN:
            return KELVIN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating BiologicallyDerivedProductStorageScale objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static BiologicallyDerivedProductStorageScale of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating BiologicallyDerivedProductStorageScale objects from a passed string value.
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
     * Inherited factory method for creating BiologicallyDerivedProductStorageScale objects from a passed string value.
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
        BiologicallyDerivedProductStorageScale other = (BiologicallyDerivedProductStorageScale) obj;
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
         *     An enum constant for BiologicallyDerivedProductStorageScale
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public BiologicallyDerivedProductStorageScale build() {
            BiologicallyDerivedProductStorageScale biologicallyDerivedProductStorageScale = new BiologicallyDerivedProductStorageScale(this);
            if (validating) {
                validate(biologicallyDerivedProductStorageScale);
            }
            return biologicallyDerivedProductStorageScale;
        }

        protected void validate(BiologicallyDerivedProductStorageScale biologicallyDerivedProductStorageScale) {
            super.validate(biologicallyDerivedProductStorageScale);
        }

        protected Builder from(BiologicallyDerivedProductStorageScale biologicallyDerivedProductStorageScale) {
            super.from(biologicallyDerivedProductStorageScale);
            return this;
        }
    }

    public enum Value {
        /**
         * Fahrenheit
         * 
         * <p>Fahrenheit temperature scale.
         */
        FARENHEIT("farenheit"),

        /**
         * Celsius
         * 
         * <p>Celsius or centigrade temperature scale.
         */
        CELSIUS("celsius"),

        /**
         * Kelvin
         * 
         * <p>Kelvin absolute thermodynamic temperature scale.
         */
        KELVIN("kelvin");

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
         * Factory method for creating BiologicallyDerivedProductStorageScale.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding BiologicallyDerivedProductStorageScale.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "farenheit":
                return FARENHEIT;
            case "celsius":
                return CELSIUS;
            case "kelvin":
                return KELVIN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
