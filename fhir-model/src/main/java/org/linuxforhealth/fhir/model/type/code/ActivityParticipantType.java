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

@System("http://hl7.org/fhir/action-participant-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ActivityParticipantType extends Code {
    /**
     * Patient
     * 
     * <p>The participant is the patient under evaluation.
     */
    public static final ActivityParticipantType PATIENT = ActivityParticipantType.builder().value(Value.PATIENT).build();

    /**
     * Practitioner
     * 
     * <p>The participant is a practitioner involved in the patient's care.
     */
    public static final ActivityParticipantType PRACTITIONER = ActivityParticipantType.builder().value(Value.PRACTITIONER).build();

    /**
     * Related Person
     * 
     * <p>The participant is a person related to the patient.
     */
    public static final ActivityParticipantType RELATED_PERSON = ActivityParticipantType.builder().value(Value.RELATED_PERSON).build();

    /**
     * Device
     * 
     * <p>The participant is a system or device used in the care of the patient.
     */
    public static final ActivityParticipantType DEVICE = ActivityParticipantType.builder().value(Value.DEVICE).build();

    private volatile int hashCode;

    private ActivityParticipantType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActivityParticipantType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActivityParticipantType objects from a passed enum value.
     */
    public static ActivityParticipantType of(Value value) {
        switch (value) {
        case PATIENT:
            return PATIENT;
        case PRACTITIONER:
            return PRACTITIONER;
        case RELATED_PERSON:
            return RELATED_PERSON;
        case DEVICE:
            return DEVICE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ActivityParticipantType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActivityParticipantType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActivityParticipantType objects from a passed string value.
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
     * Inherited factory method for creating ActivityParticipantType objects from a passed string value.
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
        ActivityParticipantType other = (ActivityParticipantType) obj;
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
         *     An enum constant for ActivityParticipantType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActivityParticipantType build() {
            ActivityParticipantType activityParticipantType = new ActivityParticipantType(this);
            if (validating) {
                validate(activityParticipantType);
            }
            return activityParticipantType;
        }

        protected void validate(ActivityParticipantType activityParticipantType) {
            super.validate(activityParticipantType);
        }

        protected Builder from(ActivityParticipantType activityParticipantType) {
            super.from(activityParticipantType);
            return this;
        }
    }

    public enum Value {
        /**
         * Patient
         * 
         * <p>The participant is the patient under evaluation.
         */
        PATIENT("patient"),

        /**
         * Practitioner
         * 
         * <p>The participant is a practitioner involved in the patient's care.
         */
        PRACTITIONER("practitioner"),

        /**
         * Related Person
         * 
         * <p>The participant is a person related to the patient.
         */
        RELATED_PERSON("related-person"),

        /**
         * Device
         * 
         * <p>The participant is a system or device used in the care of the patient.
         */
        DEVICE("device");

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
         * Factory method for creating ActivityParticipantType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActivityParticipantType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "patient":
                return PATIENT;
            case "practitioner":
                return PRACTITIONER;
            case "related-person":
                return RELATED_PERSON;
            case "device":
                return DEVICE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
