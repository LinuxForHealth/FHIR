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

@System("http://hl7.org/fhir/administrative-gender")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AdministrativeGender extends Code {
    /**
     * Male
     * 
     * <p>Male.
     */
    public static final AdministrativeGender MALE = AdministrativeGender.builder().value(Value.MALE).build();

    /**
     * Female
     * 
     * <p>Female.
     */
    public static final AdministrativeGender FEMALE = AdministrativeGender.builder().value(Value.FEMALE).build();

    /**
     * Other
     * 
     * <p>Other.
     */
    public static final AdministrativeGender OTHER = AdministrativeGender.builder().value(Value.OTHER).build();

    /**
     * Unknown
     * 
     * <p>Unknown.
     */
    public static final AdministrativeGender UNKNOWN = AdministrativeGender.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private AdministrativeGender(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AdministrativeGender as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AdministrativeGender objects from a passed enum value.
     */
    public static AdministrativeGender of(Value value) {
        switch (value) {
        case MALE:
            return MALE;
        case FEMALE:
            return FEMALE;
        case OTHER:
            return OTHER;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AdministrativeGender objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AdministrativeGender of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AdministrativeGender objects from a passed string value.
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
     * Inherited factory method for creating AdministrativeGender objects from a passed string value.
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
        AdministrativeGender other = (AdministrativeGender) obj;
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
         *     An enum constant for AdministrativeGender
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AdministrativeGender build() {
            AdministrativeGender administrativeGender = new AdministrativeGender(this);
            if (validating) {
                validate(administrativeGender);
            }
            return administrativeGender;
        }

        protected void validate(AdministrativeGender administrativeGender) {
            super.validate(administrativeGender);
        }

        protected Builder from(AdministrativeGender administrativeGender) {
            super.from(administrativeGender);
            return this;
        }
    }

    public enum Value {
        /**
         * Male
         * 
         * <p>Male.
         */
        MALE("male"),

        /**
         * Female
         * 
         * <p>Female.
         */
        FEMALE("female"),

        /**
         * Other
         * 
         * <p>Other.
         */
        OTHER("other"),

        /**
         * Unknown
         * 
         * <p>Unknown.
         */
        UNKNOWN("unknown");

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
         * Factory method for creating AdministrativeGender.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AdministrativeGender.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "male":
                return MALE;
            case "female":
                return FEMALE;
            case "other":
                return OTHER;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
