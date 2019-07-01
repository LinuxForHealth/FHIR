/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class SearchComparator extends Code {
    /**
     * Equals
     */
    public static final SearchComparator EQ = SearchComparator.of(ValueSet.EQ);

    /**
     * Not Equals
     */
    public static final SearchComparator NE = SearchComparator.of(ValueSet.NE);

    /**
     * Greater Than
     */
    public static final SearchComparator GT = SearchComparator.of(ValueSet.GT);

    /**
     * Less Than
     */
    public static final SearchComparator LT = SearchComparator.of(ValueSet.LT);

    /**
     * Greater or Equals
     */
    public static final SearchComparator GE = SearchComparator.of(ValueSet.GE);

    /**
     * Less of Equal
     */
    public static final SearchComparator LE = SearchComparator.of(ValueSet.LE);

    /**
     * Starts After
     */
    public static final SearchComparator SA = SearchComparator.of(ValueSet.SA);

    /**
     * Ends Before
     */
    public static final SearchComparator EB = SearchComparator.of(ValueSet.EB);

    /**
     * Approximately
     */
    public static final SearchComparator AP = SearchComparator.of(ValueSet.AP);

    private volatile int hashCode;

    private SearchComparator(Builder builder) {
        super(builder);
    }

    public static SearchComparator of(java.lang.String value) {
        return SearchComparator.builder().value(value).build();
    }

    public static SearchComparator of(ValueSet value) {
        return SearchComparator.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return SearchComparator.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return SearchComparator.builder().value(value).build();
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
        SearchComparator other = (SearchComparator) obj;
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
        public SearchComparator build() {
            return new SearchComparator(this);
        }
    }

    public enum ValueSet {
        /**
         * Equals
         */
        EQ("eq"),

        /**
         * Not Equals
         */
        NE("ne"),

        /**
         * Greater Than
         */
        GT("gt"),

        /**
         * Less Than
         */
        LT("lt"),

        /**
         * Greater or Equals
         */
        GE("ge"),

        /**
         * Less of Equal
         */
        LE("le"),

        /**
         * Starts After
         */
        SA("sa"),

        /**
         * Ends Before
         */
        EB("eb"),

        /**
         * Approximately
         */
        AP("ap");

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
