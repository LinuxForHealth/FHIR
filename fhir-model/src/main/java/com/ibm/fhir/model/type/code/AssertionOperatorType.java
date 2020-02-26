/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/assert-operator-codes")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AssertionOperatorType extends Code {
    /**
     * equals
     */
    public static final AssertionOperatorType EQUALS = AssertionOperatorType.builder().value(ValueSet.EQUALS).build();

    /**
     * notEquals
     */
    public static final AssertionOperatorType NOT_EQUALS = AssertionOperatorType.builder().value(ValueSet.NOT_EQUALS).build();

    /**
     * in
     */
    public static final AssertionOperatorType IN = AssertionOperatorType.builder().value(ValueSet.IN).build();

    /**
     * notIn
     */
    public static final AssertionOperatorType NOT_IN = AssertionOperatorType.builder().value(ValueSet.NOT_IN).build();

    /**
     * greaterThan
     */
    public static final AssertionOperatorType GREATER_THAN = AssertionOperatorType.builder().value(ValueSet.GREATER_THAN).build();

    /**
     * lessThan
     */
    public static final AssertionOperatorType LESS_THAN = AssertionOperatorType.builder().value(ValueSet.LESS_THAN).build();

    /**
     * empty
     */
    public static final AssertionOperatorType EMPTY = AssertionOperatorType.builder().value(ValueSet.EMPTY).build();

    /**
     * notEmpty
     */
    public static final AssertionOperatorType NOT_EMPTY = AssertionOperatorType.builder().value(ValueSet.NOT_EMPTY).build();

    /**
     * contains
     */
    public static final AssertionOperatorType CONTAINS = AssertionOperatorType.builder().value(ValueSet.CONTAINS).build();

    /**
     * notContains
     */
    public static final AssertionOperatorType NOT_CONTAINS = AssertionOperatorType.builder().value(ValueSet.NOT_CONTAINS).build();

    /**
     * evaluate
     */
    public static final AssertionOperatorType EVAL = AssertionOperatorType.builder().value(ValueSet.EVAL).build();

    private volatile int hashCode;

    private AssertionOperatorType(Builder builder) {
        super(builder);
    }

    public static AssertionOperatorType of(ValueSet value) {
        switch (value) {
        case EQUALS:
            return EQUALS;
        case NOT_EQUALS:
            return NOT_EQUALS;
        case IN:
            return IN;
        case NOT_IN:
            return NOT_IN;
        case GREATER_THAN:
            return GREATER_THAN;
        case LESS_THAN:
            return LESS_THAN;
        case EMPTY:
            return EMPTY;
        case NOT_EMPTY:
            return NOT_EMPTY;
        case CONTAINS:
            return CONTAINS;
        case NOT_CONTAINS:
            return NOT_CONTAINS;
        case EVAL:
            return EVAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static AssertionOperatorType of(java.lang.String value) {
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
