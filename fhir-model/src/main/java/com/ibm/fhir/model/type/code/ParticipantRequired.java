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

@System("http://hl7.org/fhir/participantrequired")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ParticipantRequired extends Code {
    /**
     * Required
     * 
     * <p>The participant is required to attend the appointment.
     */
    public static final ParticipantRequired REQUIRED = ParticipantRequired.builder().value(Value.REQUIRED).build();

    /**
     * Optional
     * 
     * <p>The participant may optionally attend the appointment.
     */
    public static final ParticipantRequired OPTIONAL = ParticipantRequired.builder().value(Value.OPTIONAL).build();

    /**
     * Information Only
     * 
     * <p>The participant is excluded from the appointment, and might not be informed of the appointment taking place. 
     * (Appointment is about them, not for them - such as 2 doctors discussing results about a patient's test).
     */
    public static final ParticipantRequired INFORMATION_ONLY = ParticipantRequired.builder().value(Value.INFORMATION_ONLY).build();

    private volatile int hashCode;

    private ParticipantRequired(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ParticipantRequired as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ParticipantRequired objects from a passed enum value.
     */
    public static ParticipantRequired of(Value value) {
        switch (value) {
        case REQUIRED:
            return REQUIRED;
        case OPTIONAL:
            return OPTIONAL;
        case INFORMATION_ONLY:
            return INFORMATION_ONLY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ParticipantRequired objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ParticipantRequired of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ParticipantRequired objects from a passed string value.
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
     * Inherited factory method for creating ParticipantRequired objects from a passed string value.
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
        ParticipantRequired other = (ParticipantRequired) obj;
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
         *     An enum constant for ParticipantRequired
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ParticipantRequired build() {
            ParticipantRequired participantRequired = new ParticipantRequired(this);
            if (validating) {
                validate(participantRequired);
            }
            return participantRequired;
        }

        protected void validate(ParticipantRequired participantRequired) {
            super.validate(participantRequired);
        }

        protected Builder from(ParticipantRequired participantRequired) {
            super.from(participantRequired);
            return this;
        }
    }

    public enum Value {
        /**
         * Required
         * 
         * <p>The participant is required to attend the appointment.
         */
        REQUIRED("required"),

        /**
         * Optional
         * 
         * <p>The participant may optionally attend the appointment.
         */
        OPTIONAL("optional"),

        /**
         * Information Only
         * 
         * <p>The participant is excluded from the appointment, and might not be informed of the appointment taking place. 
         * (Appointment is about them, not for them - such as 2 doctors discussing results about a patient's test).
         */
        INFORMATION_ONLY("information-only");

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
         * Factory method for creating ParticipantRequired.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ParticipantRequired.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "required":
                return REQUIRED;
            case "optional":
                return OPTIONAL;
            case "information-only":
                return INFORMATION_ONLY;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
