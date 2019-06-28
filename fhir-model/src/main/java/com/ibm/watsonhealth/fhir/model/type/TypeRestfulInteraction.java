/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class TypeRestfulInteraction extends Code {
    public static final TypeRestfulInteraction READ = TypeRestfulInteraction.of(ValueSet.READ);

    public static final TypeRestfulInteraction VREAD = TypeRestfulInteraction.of(ValueSet.VREAD);

    public static final TypeRestfulInteraction UPDATE = TypeRestfulInteraction.of(ValueSet.UPDATE);

    public static final TypeRestfulInteraction PATCH = TypeRestfulInteraction.of(ValueSet.PATCH);

    public static final TypeRestfulInteraction DELETE = TypeRestfulInteraction.of(ValueSet.DELETE);

    public static final TypeRestfulInteraction HISTORY_INSTANCE = TypeRestfulInteraction.of(ValueSet.HISTORY_INSTANCE);

    public static final TypeRestfulInteraction HISTORY_TYPE = TypeRestfulInteraction.of(ValueSet.HISTORY_TYPE);

    public static final TypeRestfulInteraction CREATE = TypeRestfulInteraction.of(ValueSet.CREATE);

    public static final TypeRestfulInteraction SEARCH_TYPE = TypeRestfulInteraction.of(ValueSet.SEARCH_TYPE);

    private TypeRestfulInteraction(Builder builder) {
        super(builder);
    }

    public static TypeRestfulInteraction of(java.lang.String value) {
        return TypeRestfulInteraction.builder().value(value).build();
    }

    public static TypeRestfulInteraction of(ValueSet value) {
        return TypeRestfulInteraction.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return TypeRestfulInteraction.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return TypeRestfulInteraction.builder().value(value).build();
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
        public TypeRestfulInteraction build() {
            return new TypeRestfulInteraction(this);
        }
    }

    public enum ValueSet {
        READ("read"),

        VREAD("vread"),

        UPDATE("update"),

        PATCH("patch"),

        DELETE("delete"),

        HISTORY_INSTANCE("history-instance"),

        HISTORY_TYPE("history-type"),

        CREATE("create"),

        SEARCH_TYPE("search-type");

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
