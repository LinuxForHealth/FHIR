/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class BiologicallyDerivedProductStorageScale extends Code {
    /**
     * Fahrenheit
     */
    public static final BiologicallyDerivedProductStorageScale FARENHEIT = BiologicallyDerivedProductStorageScale.of(ValueSet.FARENHEIT);

    /**
     * Celsius
     */
    public static final BiologicallyDerivedProductStorageScale CELSIUS = BiologicallyDerivedProductStorageScale.of(ValueSet.CELSIUS);

    /**
     * Kelvin
     */
    public static final BiologicallyDerivedProductStorageScale KELVIN = BiologicallyDerivedProductStorageScale.of(ValueSet.KELVIN);

    private BiologicallyDerivedProductStorageScale(Builder builder) {
        super(builder);
    }

    public static BiologicallyDerivedProductStorageScale of(java.lang.String value) {
        return BiologicallyDerivedProductStorageScale.builder().value(value).build();
    }

    public static BiologicallyDerivedProductStorageScale of(ValueSet value) {
        return BiologicallyDerivedProductStorageScale.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public BiologicallyDerivedProductStorageScale build() {
            return new BiologicallyDerivedProductStorageScale(this);
        }
    }

    public enum ValueSet {
        /**
         * Fahrenheit
         */
        FARENHEIT("farenheit"),

        /**
         * Celsius
         */
        CELSIUS("celsius"),

        /**
         * Kelvin
         */
        KELVIN("kelvin");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
