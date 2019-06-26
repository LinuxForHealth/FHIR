/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class NamingSystemType extends Code {
    /**
     * Code System
     */
    public static final NamingSystemType CODESYSTEM = NamingSystemType.of(ValueSet.CODESYSTEM);

    /**
     * Identifier
     */
    public static final NamingSystemType IDENTIFIER = NamingSystemType.of(ValueSet.IDENTIFIER);

    /**
     * Root
     */
    public static final NamingSystemType ROOT = NamingSystemType.of(ValueSet.ROOT);

    private NamingSystemType(Builder builder) {
        super(builder);
    }

    public static NamingSystemType of(java.lang.String value) {
        return NamingSystemType.builder().value(value).build();
    }

    public static NamingSystemType of(ValueSet value) {
        return NamingSystemType.builder().value(value).build();
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
        public NamingSystemType build() {
            return new NamingSystemType(this);
        }
    }

    public enum ValueSet {
        /**
         * Code System
         */
        CODESYSTEM("codesystem"),

        /**
         * Identifier
         */
        IDENTIFIER("identifier"),

        /**
         * Root
         */
        ROOT("root");

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
