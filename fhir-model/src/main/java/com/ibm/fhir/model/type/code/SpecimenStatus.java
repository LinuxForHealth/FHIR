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

@System("http://hl7.org/fhir/specimen-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SpecimenStatus extends Code {
    /**
     * Available
     * 
     * <p>The physical specimen is present and in good condition.
     */
    public static final SpecimenStatus AVAILABLE = SpecimenStatus.builder().value(Value.AVAILABLE).build();

    /**
     * Unavailable
     * 
     * <p>There is no physical specimen because it is either lost, destroyed or consumed.
     */
    public static final SpecimenStatus UNAVAILABLE = SpecimenStatus.builder().value(Value.UNAVAILABLE).build();

    /**
     * Unsatisfactory
     * 
     * <p>The specimen cannot be used because of a quality issue such as a broken container, contamination, or too old.
     */
    public static final SpecimenStatus UNSATISFACTORY = SpecimenStatus.builder().value(Value.UNSATISFACTORY).build();

    /**
     * Entered in Error
     * 
     * <p>The specimen was entered in error and therefore nullified.
     */
    public static final SpecimenStatus ENTERED_IN_ERROR = SpecimenStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private SpecimenStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SpecimenStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SpecimenStatus objects from a passed enum value.
     */
    public static SpecimenStatus of(Value value) {
        switch (value) {
        case AVAILABLE:
            return AVAILABLE;
        case UNAVAILABLE:
            return UNAVAILABLE;
        case UNSATISFACTORY:
            return UNSATISFACTORY;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SpecimenStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SpecimenStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SpecimenStatus objects from a passed string value.
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
     * Inherited factory method for creating SpecimenStatus objects from a passed string value.
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
        SpecimenStatus other = (SpecimenStatus) obj;
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
         *     An enum constant for SpecimenStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SpecimenStatus build() {
            SpecimenStatus specimenStatus = new SpecimenStatus(this);
            if (validating) {
                validate(specimenStatus);
            }
            return specimenStatus;
        }

        protected void validate(SpecimenStatus specimenStatus) {
            super.validate(specimenStatus);
        }

        protected Builder from(SpecimenStatus specimenStatus) {
            super.from(specimenStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Available
         * 
         * <p>The physical specimen is present and in good condition.
         */
        AVAILABLE("available"),

        /**
         * Unavailable
         * 
         * <p>There is no physical specimen because it is either lost, destroyed or consumed.
         */
        UNAVAILABLE("unavailable"),

        /**
         * Unsatisfactory
         * 
         * <p>The specimen cannot be used because of a quality issue such as a broken container, contamination, or too old.
         */
        UNSATISFACTORY("unsatisfactory"),

        /**
         * Entered in Error
         * 
         * <p>The specimen was entered in error and therefore nullified.
         */
        ENTERED_IN_ERROR("entered-in-error");

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
         * Factory method for creating SpecimenStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SpecimenStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "available":
                return AVAILABLE;
            case "unavailable":
                return UNAVAILABLE;
            case "unsatisfactory":
                return UNSATISFACTORY;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
