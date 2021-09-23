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

@System("http://hl7.org/fhir/participationstatus")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ParticipantStatus extends Code {
    /**
     * Accepted
     * 
     * <p>The participant has accepted the appointment.
     */
    public static final ParticipantStatus ACCEPTED = ParticipantStatus.builder().value(Value.ACCEPTED).build();

    /**
     * Declined
     * 
     * <p>The participant has declined the appointment and will not participate in the appointment.
     */
    public static final ParticipantStatus DECLINED = ParticipantStatus.builder().value(Value.DECLINED).build();

    /**
     * Tentative
     * 
     * <p>The participant has tentatively accepted the appointment. This could be automatically created by a system and 
     * requires further processing before it can be accepted. There is no commitment that attendance will occur.
     */
    public static final ParticipantStatus TENTATIVE = ParticipantStatus.builder().value(Value.TENTATIVE).build();

    /**
     * Needs Action
     * 
     * <p>The participant needs to indicate if they accept the appointment by changing this status to one of the other 
     * statuses.
     */
    public static final ParticipantStatus NEEDS_ACTION = ParticipantStatus.builder().value(Value.NEEDS_ACTION).build();

    private volatile int hashCode;

    private ParticipantStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ParticipantStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ParticipantStatus objects from a passed enum value.
     */
    public static ParticipantStatus of(Value value) {
        switch (value) {
        case ACCEPTED:
            return ACCEPTED;
        case DECLINED:
            return DECLINED;
        case TENTATIVE:
            return TENTATIVE;
        case NEEDS_ACTION:
            return NEEDS_ACTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ParticipantStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ParticipantStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ParticipantStatus objects from a passed string value.
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
     * Inherited factory method for creating ParticipantStatus objects from a passed string value.
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
        ParticipantStatus other = (ParticipantStatus) obj;
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
         *     An enum constant for ParticipantStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ParticipantStatus build() {
            ParticipantStatus participantStatus = new ParticipantStatus(this);
            if (validating) {
                validate(participantStatus);
            }
            return participantStatus;
        }

        protected void validate(ParticipantStatus participantStatus) {
            super.validate(participantStatus);
        }

        protected Builder from(ParticipantStatus participantStatus) {
            super.from(participantStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Accepted
         * 
         * <p>The participant has accepted the appointment.
         */
        ACCEPTED("accepted"),

        /**
         * Declined
         * 
         * <p>The participant has declined the appointment and will not participate in the appointment.
         */
        DECLINED("declined"),

        /**
         * Tentative
         * 
         * <p>The participant has tentatively accepted the appointment. This could be automatically created by a system and 
         * requires further processing before it can be accepted. There is no commitment that attendance will occur.
         */
        TENTATIVE("tentative"),

        /**
         * Needs Action
         * 
         * <p>The participant needs to indicate if they accept the appointment by changing this status to one of the other 
         * statuses.
         */
        NEEDS_ACTION("needs-action");

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
         * Factory method for creating ParticipantStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ParticipantStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "accepted":
                return ACCEPTED;
            case "declined":
                return DECLINED;
            case "tentative":
                return TENTATIVE;
            case "needs-action":
                return NEEDS_ACTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
