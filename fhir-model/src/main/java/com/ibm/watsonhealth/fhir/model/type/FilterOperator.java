/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class FilterOperator extends Code {
    /**
     * Equals
     */
    public static final FilterOperator EQUALS = FilterOperator.of(ValueSet.EQUALS);

    /**
     * Is A (by subsumption)
     */
    public static final FilterOperator IS_A = FilterOperator.of(ValueSet.IS_A);

    /**
     * Descendent Of (by subsumption)
     */
    public static final FilterOperator DESCENDENT_OF = FilterOperator.of(ValueSet.DESCENDENT_OF);

    /**
     * Not (Is A) (by subsumption)
     */
    public static final FilterOperator IS_NOT_A = FilterOperator.of(ValueSet.IS_NOT_A);

    /**
     * Regular Expression
     */
    public static final FilterOperator REGEX = FilterOperator.of(ValueSet.REGEX);

    /**
     * In Set
     */
    public static final FilterOperator IN = FilterOperator.of(ValueSet.IN);

    /**
     * Not in Set
     */
    public static final FilterOperator NOT_IN = FilterOperator.of(ValueSet.NOT_IN);

    /**
     * Generalizes (by Subsumption)
     */
    public static final FilterOperator GENERALIZES = FilterOperator.of(ValueSet.GENERALIZES);

    /**
     * Exists
     */
    public static final FilterOperator EXISTS = FilterOperator.of(ValueSet.EXISTS);

    private volatile int hashCode;

    private FilterOperator(Builder builder) {
        super(builder);
    }

    public static FilterOperator of(java.lang.String value) {
        return FilterOperator.builder().value(value).build();
    }

    public static FilterOperator of(ValueSet value) {
        return FilterOperator.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return FilterOperator.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return FilterOperator.builder().value(value).build();
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
        FilterOperator other = (FilterOperator) obj;
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
        public FilterOperator build() {
            return new FilterOperator(this);
        }
    }

    public enum ValueSet {
        /**
         * Equals
         */
        EQUALS("="),

        /**
         * Is A (by subsumption)
         */
        IS_A("is-a"),

        /**
         * Descendent Of (by subsumption)
         */
        DESCENDENT_OF("descendent-of"),

        /**
         * Not (Is A) (by subsumption)
         */
        IS_NOT_A("is-not-a"),

        /**
         * Regular Expression
         */
        REGEX("regex"),

        /**
         * In Set
         */
        IN("in"),

        /**
         * Not in Set
         */
        NOT_IN("not-in"),

        /**
         * Generalizes (by Subsumption)
         */
        GENERALIZES("generalizes"),

        /**
         * Exists
         */
        EXISTS("exists");

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
