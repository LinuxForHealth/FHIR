/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class SearchModifierCode extends Code {
    /**
     * Missing
     */
    public static final SearchModifierCode MISSING = SearchModifierCode.of(ValueSet.MISSING);

    /**
     * Exact
     */
    public static final SearchModifierCode EXACT = SearchModifierCode.of(ValueSet.EXACT);

    /**
     * Contains
     */
    public static final SearchModifierCode CONTAINS = SearchModifierCode.of(ValueSet.CONTAINS);

    /**
     * Not
     */
    public static final SearchModifierCode NOT = SearchModifierCode.of(ValueSet.NOT);

    /**
     * Text
     */
    public static final SearchModifierCode TEXT = SearchModifierCode.of(ValueSet.TEXT);

    /**
     * In
     */
    public static final SearchModifierCode IN = SearchModifierCode.of(ValueSet.IN);

    /**
     * Not In
     */
    public static final SearchModifierCode NOT_IN = SearchModifierCode.of(ValueSet.NOT_IN);

    /**
     * Below
     */
    public static final SearchModifierCode BELOW = SearchModifierCode.of(ValueSet.BELOW);

    /**
     * Above
     */
    public static final SearchModifierCode ABOVE = SearchModifierCode.of(ValueSet.ABOVE);

    /**
     * Type
     */
    public static final SearchModifierCode TYPE = SearchModifierCode.of(ValueSet.TYPE);

    /**
     * Identifier
     */
    public static final SearchModifierCode IDENTIFIER = SearchModifierCode.of(ValueSet.IDENTIFIER);

    /**
     * Of Type
     */
    public static final SearchModifierCode OF_TYPE = SearchModifierCode.of(ValueSet.OF_TYPE);

    private volatile int hashCode;

    private SearchModifierCode(Builder builder) {
        super(builder);
    }

    public static SearchModifierCode of(java.lang.String value) {
        return SearchModifierCode.builder().value(value).build();
    }

    public static SearchModifierCode of(ValueSet value) {
        return SearchModifierCode.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return SearchModifierCode.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return SearchModifierCode.builder().value(value).build();
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
        SearchModifierCode other = (SearchModifierCode) obj;
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
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public SearchModifierCode build() {
            return new SearchModifierCode(this);
        }
    }

    public enum ValueSet {
        /**
         * Missing
         */
        MISSING("missing"),

        /**
         * Exact
         */
        EXACT("exact"),

        /**
         * Contains
         */
        CONTAINS("contains"),

        /**
         * Not
         */
        NOT("not"),

        /**
         * Text
         */
        TEXT("text"),

        /**
         * In
         */
        IN("in"),

        /**
         * Not In
         */
        NOT_IN("not-in"),

        /**
         * Below
         */
        BELOW("below"),

        /**
         * Above
         */
        ABOVE("above"),

        /**
         * Type
         */
        TYPE("type"),

        /**
         * Identifier
         */
        IDENTIFIER("identifier"),

        /**
         * Of Type
         */
        OF_TYPE("ofType");

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
