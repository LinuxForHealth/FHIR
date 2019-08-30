/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class SearchParamType extends Code {
    /**
     * Number
     */
    public static final SearchParamType NUMBER = SearchParamType.of(ValueSet.NUMBER);

    /**
     * Date/DateTime
     */
    public static final SearchParamType DATE = SearchParamType.of(ValueSet.DATE);

    /**
     * String
     */
    public static final SearchParamType STRING = SearchParamType.of(ValueSet.STRING);

    /**
     * Token
     */
    public static final SearchParamType TOKEN = SearchParamType.of(ValueSet.TOKEN);

    /**
     * Reference
     */
    public static final SearchParamType REFERENCE = SearchParamType.of(ValueSet.REFERENCE);

    /**
     * Composite
     */
    public static final SearchParamType COMPOSITE = SearchParamType.of(ValueSet.COMPOSITE);

    /**
     * Quantity
     */
    public static final SearchParamType QUANTITY = SearchParamType.of(ValueSet.QUANTITY);

    /**
     * URI
     */
    public static final SearchParamType URI = SearchParamType.of(ValueSet.URI);

    /**
     * Special
     */
    public static final SearchParamType SPECIAL = SearchParamType.of(ValueSet.SPECIAL);

    private volatile int hashCode;

    private SearchParamType(Builder builder) {
        super(builder);
    }

    public static SearchParamType of(java.lang.String value) {
        return SearchParamType.builder().value(value).build();
    }

    public static SearchParamType of(ValueSet value) {
        return SearchParamType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return SearchParamType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return SearchParamType.builder().value(value).build();
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
        SearchParamType other = (SearchParamType) obj;
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
        public SearchParamType build() {
            return new SearchParamType(this);
        }
    }

    public enum ValueSet {
        /**
         * Number
         */
        NUMBER("number"),

        /**
         * Date/DateTime
         */
        DATE("date"),

        /**
         * String
         */
        STRING("string"),

        /**
         * Token
         */
        TOKEN("token"),

        /**
         * Reference
         */
        REFERENCE("reference"),

        /**
         * Composite
         */
        COMPOSITE("composite"),

        /**
         * Quantity
         */
        QUANTITY("quantity"),

        /**
         * URI
         */
        URI("uri"),

        /**
         * Special
         */
        SPECIAL("special");

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
