/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ExtensionContextType extends Code {
    /**
     * FHIRPath
     */
    public static final ExtensionContextType FHIRPATH = ExtensionContextType.of(ValueSet.FHIRPATH);

    /**
     * Element ID
     */
    public static final ExtensionContextType ELEMENT = ExtensionContextType.of(ValueSet.ELEMENT);

    /**
     * Extension URL
     */
    public static final ExtensionContextType EXTENSION = ExtensionContextType.of(ValueSet.EXTENSION);

    private ExtensionContextType(Builder builder) {
        super(builder);
    }

    public static ExtensionContextType of(java.lang.String value) {
        return ExtensionContextType.builder().value(value).build();
    }

    public static ExtensionContextType of(ValueSet value) {
        return ExtensionContextType.builder().value(value).build();
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
        public ExtensionContextType build() {
            return new ExtensionContextType(this);
        }
    }

    public enum ValueSet {
        /**
         * FHIRPath
         */
        FHIRPATH("fhirpath"),

        /**
         * Element ID
         */
        ELEMENT("element"),

        /**
         * Extension URL
         */
        EXTENSION("extension");

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
