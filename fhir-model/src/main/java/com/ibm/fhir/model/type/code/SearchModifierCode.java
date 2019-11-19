/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SearchModifierCode extends Code {
    /**
     * Missing
     */
    public static final SearchModifierCode MISSING = SearchModifierCode.builder().value(ValueSet.MISSING).build();

    /**
     * Exact
     */
    public static final SearchModifierCode EXACT = SearchModifierCode.builder().value(ValueSet.EXACT).build();

    /**
     * Contains
     */
    public static final SearchModifierCode CONTAINS = SearchModifierCode.builder().value(ValueSet.CONTAINS).build();

    /**
     * Not
     */
    public static final SearchModifierCode NOT = SearchModifierCode.builder().value(ValueSet.NOT).build();

    /**
     * Text
     */
    public static final SearchModifierCode TEXT = SearchModifierCode.builder().value(ValueSet.TEXT).build();

    /**
     * In
     */
    public static final SearchModifierCode IN = SearchModifierCode.builder().value(ValueSet.IN).build();

    /**
     * Not In
     */
    public static final SearchModifierCode NOT_IN = SearchModifierCode.builder().value(ValueSet.NOT_IN).build();

    /**
     * Below
     */
    public static final SearchModifierCode BELOW = SearchModifierCode.builder().value(ValueSet.BELOW).build();

    /**
     * Above
     */
    public static final SearchModifierCode ABOVE = SearchModifierCode.builder().value(ValueSet.ABOVE).build();

    /**
     * Type
     */
    public static final SearchModifierCode TYPE = SearchModifierCode.builder().value(ValueSet.TYPE).build();

    /**
     * Identifier
     */
    public static final SearchModifierCode IDENTIFIER = SearchModifierCode.builder().value(ValueSet.IDENTIFIER).build();

    /**
     * Of Type
     */
    public static final SearchModifierCode OF_TYPE = SearchModifierCode.builder().value(ValueSet.OF_TYPE).build();

    private volatile int hashCode;

    private SearchModifierCode(Builder builder) {
        super(builder);
    }

    public static SearchModifierCode of(ValueSet value) {
        switch (value) {
        case MISSING:
            return MISSING;
        case EXACT:
            return EXACT;
        case CONTAINS:
            return CONTAINS;
        case NOT:
            return NOT;
        case TEXT:
            return TEXT;
        case IN:
            return IN;
        case NOT_IN:
            return NOT_IN;
        case BELOW:
            return BELOW;
        case ABOVE:
            return ABOVE;
        case TYPE:
            return TYPE;
        case IDENTIFIER:
            return IDENTIFIER;
        case OF_TYPE:
            return OF_TYPE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static SearchModifierCode of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
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
