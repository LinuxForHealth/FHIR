/*
 * (C) Copyright IBM Corp. 2019, 2021
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

@System("http://unitsofmeasure.org")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class UnitsOfTime extends Code {
    /**
     * second
     */
    public static final UnitsOfTime S = UnitsOfTime.builder().value(Value.S).build();

    /**
     * minute
     */
    public static final UnitsOfTime MIN = UnitsOfTime.builder().value(Value.MIN).build();

    /**
     * hour
     */
    public static final UnitsOfTime H = UnitsOfTime.builder().value(Value.H).build();

    /**
     * day
     */
    public static final UnitsOfTime D = UnitsOfTime.builder().value(Value.D).build();

    /**
     * week
     */
    public static final UnitsOfTime WK = UnitsOfTime.builder().value(Value.WK).build();

    /**
     * month
     */
    public static final UnitsOfTime MO = UnitsOfTime.builder().value(Value.MO).build();

    /**
     * year
     */
    public static final UnitsOfTime A = UnitsOfTime.builder().value(Value.A).build();

    private volatile int hashCode;

    private UnitsOfTime(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this UnitsOfTime as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating UnitsOfTime objects from a passed enum value.
     */
    public static UnitsOfTime of(Value value) {
        switch (value) {
        case S:
            return S;
        case MIN:
            return MIN;
        case H:
            return H;
        case D:
            return D;
        case WK:
            return WK;
        case MO:
            return MO;
        case A:
            return A;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating UnitsOfTime objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static UnitsOfTime of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating UnitsOfTime objects from a passed string value.
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
     * Inherited factory method for creating UnitsOfTime objects from a passed string value.
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
        UnitsOfTime other = (UnitsOfTime) obj;
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
         *     An enum constant for UnitsOfTime
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public UnitsOfTime build() {
            UnitsOfTime unitsOfTime = new UnitsOfTime(this);
            if (validating) {
                validate(unitsOfTime);
            }
            return unitsOfTime;
        }

        protected void validate(UnitsOfTime unitsOfTime) {
            super.validate(unitsOfTime);
        }

        protected Builder from(UnitsOfTime unitsOfTime) {
            super.from(unitsOfTime);
            return this;
        }
    }

    public enum Value {
        /**
         * second
         */
        S("s"),

        /**
         * minute
         */
        MIN("min"),

        /**
         * hour
         */
        H("h"),

        /**
         * day
         */
        D("d"),

        /**
         * week
         */
        WK("wk"),

        /**
         * month
         */
        MO("mo"),

        /**
         * year
         */
        A("a");

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
         * Factory method for creating UnitsOfTime.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding UnitsOfTime.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "s":
                return S;
            case "min":
                return MIN;
            case "h":
                return H;
            case "d":
                return D;
            case "wk":
                return WK;
            case "mo":
                return MO;
            case "a":
                return A;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
