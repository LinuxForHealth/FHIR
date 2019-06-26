/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class SystemRestfulInteraction extends Code {
    public static final SystemRestfulInteraction TRANSACTION = SystemRestfulInteraction.of(ValueSet.TRANSACTION);

    public static final SystemRestfulInteraction BATCH = SystemRestfulInteraction.of(ValueSet.BATCH);

    public static final SystemRestfulInteraction SEARCH_SYSTEM = SystemRestfulInteraction.of(ValueSet.SEARCH_SYSTEM);

    public static final SystemRestfulInteraction HISTORY_SYSTEM = SystemRestfulInteraction.of(ValueSet.HISTORY_SYSTEM);

    private SystemRestfulInteraction(Builder builder) {
        super(builder);
    }

    public static SystemRestfulInteraction of(java.lang.String value) {
        return SystemRestfulInteraction.builder().value(value).build();
    }

    public static SystemRestfulInteraction of(ValueSet value) {
        return SystemRestfulInteraction.builder().value(value).build();
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
        public SystemRestfulInteraction build() {
            return new SystemRestfulInteraction(this);
        }
    }

    public enum ValueSet {
        TRANSACTION("transaction"),

        BATCH("batch"),

        SEARCH_SYSTEM("search-system"),

        HISTORY_SYSTEM("history-system");

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
