/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class AssertionOperatorType extends Code {
    /**
     * equals
     */
    public static final AssertionOperatorType EQUALS = AssertionOperatorType.of(ValueSet.EQUALS);

    /**
     * notEquals
     */
    public static final AssertionOperatorType NOT_EQUALS = AssertionOperatorType.of(ValueSet.NOT_EQUALS);

    /**
     * in
     */
    public static final AssertionOperatorType IN = AssertionOperatorType.of(ValueSet.IN);

    /**
     * notIn
     */
    public static final AssertionOperatorType NOT_IN = AssertionOperatorType.of(ValueSet.NOT_IN);

    /**
     * greaterThan
     */
    public static final AssertionOperatorType GREATER_THAN = AssertionOperatorType.of(ValueSet.GREATER_THAN);

    /**
     * lessThan
     */
    public static final AssertionOperatorType LESS_THAN = AssertionOperatorType.of(ValueSet.LESS_THAN);

    /**
     * empty
     */
    public static final AssertionOperatorType EMPTY = AssertionOperatorType.of(ValueSet.EMPTY);

    /**
     * notEmpty
     */
    public static final AssertionOperatorType NOT_EMPTY = AssertionOperatorType.of(ValueSet.NOT_EMPTY);

    /**
     * contains
     */
    public static final AssertionOperatorType CONTAINS = AssertionOperatorType.of(ValueSet.CONTAINS);

    /**
     * notContains
     */
    public static final AssertionOperatorType NOT_CONTAINS = AssertionOperatorType.of(ValueSet.NOT_CONTAINS);

    /**
     * evaluate
     */
    public static final AssertionOperatorType EVAL = AssertionOperatorType.of(ValueSet.EVAL);

    private volatile int hashCode;

    private AssertionOperatorType(Builder builder) {
        super(builder);
    }

    public static AssertionOperatorType of(java.lang.String value) {
        return AssertionOperatorType.builder().value(value).build();
    }

    public static AssertionOperatorType of(ValueSet value) {
        return AssertionOperatorType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return AssertionOperatorType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return AssertionOperatorType.builder().value(value).build();
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
        AssertionOperatorType other = (AssertionOperatorType) obj;
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
        public AssertionOperatorType build() {
            return new AssertionOperatorType(this);
        }
    }

    public enum ValueSet {
        /**
         * equals
         */
        EQUALS("equals"),

        /**
         * notEquals
         */
        NOT_EQUALS("notEquals"),

        /**
         * in
         */
        IN("in"),

        /**
         * notIn
         */
        NOT_IN("notIn"),

        /**
         * greaterThan
         */
        GREATER_THAN("greaterThan"),

        /**
         * lessThan
         */
        LESS_THAN("lessThan"),

        /**
         * empty
         */
        EMPTY("empty"),

        /**
         * notEmpty
         */
        NOT_EMPTY("notEmpty"),

        /**
         * contains
         */
        CONTAINS("contains"),

        /**
         * notContains
         */
        NOT_CONTAINS("notContains"),

        /**
         * evaluate
         */
        EVAL("eval");

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
