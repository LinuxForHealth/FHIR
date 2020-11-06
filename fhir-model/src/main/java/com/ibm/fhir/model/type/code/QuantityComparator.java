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

@System("http://hl7.org/fhir/quantity-comparator")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuantityComparator extends Code {
    /**
     * Less than
     * 
     * <p>The actual value is less than the given value.
     */
    public static final QuantityComparator LESS_THAN = QuantityComparator.builder().value(ValueSet.LESS_THAN).build();

    /**
     * Less or Equal to
     * 
     * <p>The actual value is less than or equal to the given value.
     */
    public static final QuantityComparator LESS_OR_EQUALS = QuantityComparator.builder().value(ValueSet.LESS_OR_EQUALS).build();

    /**
     * Greater or Equal to
     * 
     * <p>The actual value is greater than or equal to the given value.
     */
    public static final QuantityComparator GREATER_OR_EQUALS = QuantityComparator.builder().value(ValueSet.GREATER_OR_EQUALS).build();

    /**
     * Greater than
     * 
     * <p>The actual value is greater than the given value.
     */
    public static final QuantityComparator GREATER_THAN = QuantityComparator.builder().value(ValueSet.GREATER_THAN).build();

    private volatile int hashCode;

    private QuantityComparator(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating QuantityComparator objects from a passed enum value.
     */
    public static QuantityComparator of(ValueSet value) {
        switch (value) {
        case LESS_THAN:
            return LESS_THAN;
        case LESS_OR_EQUALS:
            return LESS_OR_EQUALS;
        case GREATER_OR_EQUALS:
            return GREATER_OR_EQUALS;
        case GREATER_THAN:
            return GREATER_THAN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating QuantityComparator objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static QuantityComparator of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating QuantityComparator objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating QuantityComparator objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
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
        QuantityComparator other = (QuantityComparator) obj;
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
        public QuantityComparator build() {
            return new QuantityComparator(this);
        }
    }

    public enum ValueSet {
        /**
         * Less than
         * 
         * <p>The actual value is less than the given value.
         */
        LESS_THAN("<"),

        /**
         * Less or Equal to
         * 
         * <p>The actual value is less than or equal to the given value.
         */
        LESS_OR_EQUALS("<="),

        /**
         * Greater or Equal to
         * 
         * <p>The actual value is greater than or equal to the given value.
         */
        GREATER_OR_EQUALS(">="),

        /**
         * Greater than
         * 
         * <p>The actual value is greater than the given value.
         */
        GREATER_THAN(">");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating QuantityComparator.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
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
