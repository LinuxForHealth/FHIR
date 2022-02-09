/*
 * (C) Copyright IBM Corp. 2019, 2022
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

@System("http://hl7.org/fhir/allergy-intolerance-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AllergyIntoleranceType extends Code {
    /**
     * Allergy
     * 
     * <p>A propensity for hypersensitive reaction(s) to a substance. These reactions are most typically type I 
     * hypersensitivity, plus other "allergy-like" reactions, including pseudoallergy.
     */
    public static final AllergyIntoleranceType ALLERGY = AllergyIntoleranceType.builder().value(Value.ALLERGY).build();

    /**
     * Intolerance
     * 
     * <p>A propensity for adverse reactions to a substance that is not judged to be allergic or "allergy-like". These 
     * reactions are typically (but not necessarily) non-immune. They are to some degree idiosyncratic and/or patient-
     * specific (i.e. are not a reaction that is expected to occur with most or all patients given similar circumstances).
     */
    public static final AllergyIntoleranceType INTOLERANCE = AllergyIntoleranceType.builder().value(Value.INTOLERANCE).build();

    private volatile int hashCode;

    private AllergyIntoleranceType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AllergyIntoleranceType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AllergyIntoleranceType objects from a passed enum value.
     */
    public static AllergyIntoleranceType of(Value value) {
        switch (value) {
        case ALLERGY:
            return ALLERGY;
        case INTOLERANCE:
            return INTOLERANCE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AllergyIntoleranceType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AllergyIntoleranceType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AllergyIntoleranceType objects from a passed string value.
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
     * Inherited factory method for creating AllergyIntoleranceType objects from a passed string value.
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
        AllergyIntoleranceType other = (AllergyIntoleranceType) obj;
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
         *     An enum constant for AllergyIntoleranceType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AllergyIntoleranceType build() {
            AllergyIntoleranceType allergyIntoleranceType = new AllergyIntoleranceType(this);
            if (validating) {
                validate(allergyIntoleranceType);
            }
            return allergyIntoleranceType;
        }

        protected void validate(AllergyIntoleranceType allergyIntoleranceType) {
            super.validate(allergyIntoleranceType);
        }

        protected Builder from(AllergyIntoleranceType allergyIntoleranceType) {
            super.from(allergyIntoleranceType);
            return this;
        }
    }

    public enum Value {
        /**
         * Allergy
         * 
         * <p>A propensity for hypersensitive reaction(s) to a substance. These reactions are most typically type I 
         * hypersensitivity, plus other "allergy-like" reactions, including pseudoallergy.
         */
        ALLERGY("allergy"),

        /**
         * Intolerance
         * 
         * <p>A propensity for adverse reactions to a substance that is not judged to be allergic or "allergy-like". These 
         * reactions are typically (but not necessarily) non-immune. They are to some degree idiosyncratic and/or patient-
         * specific (i.e. are not a reaction that is expected to occur with most or all patients given similar circumstances).
         */
        INTOLERANCE("intolerance");

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
         * Factory method for creating AllergyIntoleranceType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AllergyIntoleranceType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "allergy":
                return ALLERGY;
            case "intolerance":
                return INTOLERANCE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
