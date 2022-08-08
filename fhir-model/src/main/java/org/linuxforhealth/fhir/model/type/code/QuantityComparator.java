/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/quantity-comparator")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class QuantityComparator extends Code {
    /**
     * Less than
     * 
     * <p>The actual value is less than the given value.
     */
    public static final QuantityComparator LESS_THAN = QuantityComparator.builder().value(Value.LESS_THAN).build();

    /**
     * Less or Equal to
     * 
     * <p>The actual value is less than or equal to the given value.
     */
    public static final QuantityComparator LESS_OR_EQUALS = QuantityComparator.builder().value(Value.LESS_OR_EQUALS).build();

    /**
     * Greater or Equal to
     * 
     * <p>The actual value is greater than or equal to the given value.
     */
    public static final QuantityComparator GREATER_OR_EQUALS = QuantityComparator.builder().value(Value.GREATER_OR_EQUALS).build();

    /**
     * Greater than
     * 
     * <p>The actual value is greater than the given value.
     */
    public static final QuantityComparator GREATER_THAN = QuantityComparator.builder().value(Value.GREATER_THAN).build();

    private volatile int hashCode;

    private QuantityComparator(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this QuantityComparator as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating QuantityComparator objects from a passed enum value.
     */
    public static QuantityComparator of(Value value) {
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for QuantityComparator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public QuantityComparator build() {
            QuantityComparator quantityComparator = new QuantityComparator(this);
            if (validating) {
                validate(quantityComparator);
            }
            return quantityComparator;
        }

        protected void validate(QuantityComparator quantityComparator) {
            super.validate(quantityComparator);
        }

        protected Builder from(QuantityComparator quantityComparator) {
            super.from(quantityComparator);
            return this;
        }
    }

    public enum Value {
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

        Value(java.lang.String value) {
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
         * Factory method for creating QuantityComparator.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding QuantityComparator.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "<":
                return LESS_THAN;
            case "<=":
                return LESS_OR_EQUALS;
            case ">=":
                return GREATER_OR_EQUALS;
            case ">":
                return GREATER_THAN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
