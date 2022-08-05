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

@System("http://hl7.org/fhir/days-of-week")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class DaysOfWeek extends Code {
    /**
     * Monday
     * 
     * <p>Monday.
     */
    public static final DaysOfWeek MON = DaysOfWeek.builder().value(Value.MON).build();

    /**
     * Tuesday
     * 
     * <p>Tuesday.
     */
    public static final DaysOfWeek TUE = DaysOfWeek.builder().value(Value.TUE).build();

    /**
     * Wednesday
     * 
     * <p>Wednesday.
     */
    public static final DaysOfWeek WED = DaysOfWeek.builder().value(Value.WED).build();

    /**
     * Thursday
     * 
     * <p>Thursday.
     */
    public static final DaysOfWeek THU = DaysOfWeek.builder().value(Value.THU).build();

    /**
     * Friday
     * 
     * <p>Friday.
     */
    public static final DaysOfWeek FRI = DaysOfWeek.builder().value(Value.FRI).build();

    /**
     * Saturday
     * 
     * <p>Saturday.
     */
    public static final DaysOfWeek SAT = DaysOfWeek.builder().value(Value.SAT).build();

    /**
     * Sunday
     * 
     * <p>Sunday.
     */
    public static final DaysOfWeek SUN = DaysOfWeek.builder().value(Value.SUN).build();

    private volatile int hashCode;

    private DaysOfWeek(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DaysOfWeek as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DaysOfWeek objects from a passed enum value.
     */
    public static DaysOfWeek of(Value value) {
        switch (value) {
        case MON:
            return MON;
        case TUE:
            return TUE;
        case WED:
            return WED;
        case THU:
            return THU;
        case FRI:
            return FRI;
        case SAT:
            return SAT;
        case SUN:
            return SUN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DaysOfWeek objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DaysOfWeek of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DaysOfWeek objects from a passed string value.
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
     * Inherited factory method for creating DaysOfWeek objects from a passed string value.
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
        DaysOfWeek other = (DaysOfWeek) obj;
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
         *     An enum constant for DaysOfWeek
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DaysOfWeek build() {
            DaysOfWeek daysOfWeek = new DaysOfWeek(this);
            if (validating) {
                validate(daysOfWeek);
            }
            return daysOfWeek;
        }

        protected void validate(DaysOfWeek daysOfWeek) {
            super.validate(daysOfWeek);
        }

        protected Builder from(DaysOfWeek daysOfWeek) {
            super.from(daysOfWeek);
            return this;
        }
    }

    public enum Value {
        /**
         * Monday
         * 
         * <p>Monday.
         */
        MON("mon"),

        /**
         * Tuesday
         * 
         * <p>Tuesday.
         */
        TUE("tue"),

        /**
         * Wednesday
         * 
         * <p>Wednesday.
         */
        WED("wed"),

        /**
         * Thursday
         * 
         * <p>Thursday.
         */
        THU("thu"),

        /**
         * Friday
         * 
         * <p>Friday.
         */
        FRI("fri"),

        /**
         * Saturday
         * 
         * <p>Saturday.
         */
        SAT("sat"),

        /**
         * Sunday
         * 
         * <p>Sunday.
         */
        SUN("sun");

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
         * Factory method for creating DaysOfWeek.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DaysOfWeek.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "mon":
                return MON;
            case "tue":
                return TUE;
            case "wed":
                return WED;
            case "thu":
                return THU;
            case "fri":
                return FRI;
            case "sat":
                return SAT;
            case "sun":
                return SUN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
