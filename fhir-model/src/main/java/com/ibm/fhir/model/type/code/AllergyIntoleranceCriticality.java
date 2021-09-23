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

@System("http://hl7.org/fhir/allergy-intolerance-criticality")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AllergyIntoleranceCriticality extends Code {
    /**
     * Low Risk
     * 
     * <p>Worst case result of a future exposure is not assessed to be life-threatening or having high potential for organ 
     * system failure.
     */
    public static final AllergyIntoleranceCriticality LOW = AllergyIntoleranceCriticality.builder().value(Value.LOW).build();

    /**
     * High Risk
     * 
     * <p>Worst case result of a future exposure is assessed to be life-threatening or having high potential for organ system 
     * failure.
     */
    public static final AllergyIntoleranceCriticality HIGH = AllergyIntoleranceCriticality.builder().value(Value.HIGH).build();

    /**
     * Unable to Assess Risk
     * 
     * <p>Unable to assess the worst case result of a future exposure.
     */
    public static final AllergyIntoleranceCriticality UNABLE_TO_ASSESS = AllergyIntoleranceCriticality.builder().value(Value.UNABLE_TO_ASSESS).build();

    private volatile int hashCode;

    private AllergyIntoleranceCriticality(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AllergyIntoleranceCriticality as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AllergyIntoleranceCriticality objects from a passed enum value.
     */
    public static AllergyIntoleranceCriticality of(Value value) {
        switch (value) {
        case LOW:
            return LOW;
        case HIGH:
            return HIGH;
        case UNABLE_TO_ASSESS:
            return UNABLE_TO_ASSESS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AllergyIntoleranceCriticality objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AllergyIntoleranceCriticality of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AllergyIntoleranceCriticality objects from a passed string value.
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
     * Inherited factory method for creating AllergyIntoleranceCriticality objects from a passed string value.
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
        AllergyIntoleranceCriticality other = (AllergyIntoleranceCriticality) obj;
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
         *     An enum constant for AllergyIntoleranceCriticality
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AllergyIntoleranceCriticality build() {
            AllergyIntoleranceCriticality allergyIntoleranceCriticality = new AllergyIntoleranceCriticality(this);
            if (validating) {
                validate(allergyIntoleranceCriticality);
            }
            return allergyIntoleranceCriticality;
        }

        protected void validate(AllergyIntoleranceCriticality allergyIntoleranceCriticality) {
            super.validate(allergyIntoleranceCriticality);
        }

        protected Builder from(AllergyIntoleranceCriticality allergyIntoleranceCriticality) {
            super.from(allergyIntoleranceCriticality);
            return this;
        }
    }

    public enum Value {
        /**
         * Low Risk
         * 
         * <p>Worst case result of a future exposure is not assessed to be life-threatening or having high potential for organ 
         * system failure.
         */
        LOW("low"),

        /**
         * High Risk
         * 
         * <p>Worst case result of a future exposure is assessed to be life-threatening or having high potential for organ system 
         * failure.
         */
        HIGH("high"),

        /**
         * Unable to Assess Risk
         * 
         * <p>Unable to assess the worst case result of a future exposure.
         */
        UNABLE_TO_ASSESS("unable-to-assess");

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
         * Factory method for creating AllergyIntoleranceCriticality.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AllergyIntoleranceCriticality.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "low":
                return LOW;
            case "high":
                return HIGH;
            case "unable-to-assess":
                return UNABLE_TO_ASSESS;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
