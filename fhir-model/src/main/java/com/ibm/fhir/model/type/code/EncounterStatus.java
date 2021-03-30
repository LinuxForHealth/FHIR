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

@System("http://hl7.org/fhir/encounter-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EncounterStatus extends Code {
    /**
     * Planned
     * 
     * <p>The Encounter has not yet started.
     */
    public static final EncounterStatus PLANNED = EncounterStatus.builder().value(ValueSet.PLANNED).build();

    /**
     * Arrived
     * 
     * <p>The Patient is present for the encounter, however is not currently meeting with a practitioner.
     */
    public static final EncounterStatus ARRIVED = EncounterStatus.builder().value(ValueSet.ARRIVED).build();

    /**
     * Triaged
     * 
     * <p>The patient has been assessed for the priority of their treatment based on the severity of their condition.
     */
    public static final EncounterStatus TRIAGED = EncounterStatus.builder().value(ValueSet.TRIAGED).build();

    /**
     * In Progress
     * 
     * <p>The Encounter has begun and the patient is present / the practitioner and the patient are meeting.
     */
    public static final EncounterStatus IN_PROGRESS = EncounterStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * On Leave
     * 
     * <p>The Encounter has begun, but the patient is temporarily on leave.
     */
    public static final EncounterStatus ONLEAVE = EncounterStatus.builder().value(ValueSet.ONLEAVE).build();

    /**
     * Finished
     * 
     * <p>The Encounter has ended.
     */
    public static final EncounterStatus FINISHED = EncounterStatus.builder().value(ValueSet.FINISHED).build();

    /**
     * Cancelled
     * 
     * <p>The Encounter has ended before it has begun.
     */
    public static final EncounterStatus CANCELLED = EncounterStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * Entered in Error
     * 
     * <p>This instance should not have been part of this patient's medical record.
     */
    public static final EncounterStatus ENTERED_IN_ERROR = EncounterStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The encounter status is unknown. Note that "unknown" is a value of last resort and every attempt should be made to 
     * provide a meaningful value other than "unknown".
     */
    public static final EncounterStatus UNKNOWN = EncounterStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private EncounterStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating EncounterStatus objects from a passed enum value.
     */
    public static EncounterStatus of(ValueSet value) {
        switch (value) {
        case PLANNED:
            return PLANNED;
        case ARRIVED:
            return ARRIVED;
        case TRIAGED:
            return TRIAGED;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case ONLEAVE:
            return ONLEAVE;
        case FINISHED:
            return FINISHED;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EncounterStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static EncounterStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating EncounterStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating EncounterStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        EncounterStatus other = (EncounterStatus) obj;
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
        Builder builder = new Builder();
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
        return builder;
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public EncounterStatus build() {
            return new EncounterStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Planned
         * 
         * <p>The Encounter has not yet started.
         */
        PLANNED("planned"),

        /**
         * Arrived
         * 
         * <p>The Patient is present for the encounter, however is not currently meeting with a practitioner.
         */
        ARRIVED("arrived"),

        /**
         * Triaged
         * 
         * <p>The patient has been assessed for the priority of their treatment based on the severity of their condition.
         */
        TRIAGED("triaged"),

        /**
         * In Progress
         * 
         * <p>The Encounter has begun and the patient is present / the practitioner and the patient are meeting.
         */
        IN_PROGRESS("in-progress"),

        /**
         * On Leave
         * 
         * <p>The Encounter has begun, but the patient is temporarily on leave.
         */
        ONLEAVE("onleave"),

        /**
         * Finished
         * 
         * <p>The Encounter has ended.
         */
        FINISHED("finished"),

        /**
         * Cancelled
         * 
         * <p>The Encounter has ended before it has begun.
         */
        CANCELLED("cancelled"),

        /**
         * Entered in Error
         * 
         * <p>This instance should not have been part of this patient's medical record.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The encounter status is unknown. Note that "unknown" is a value of last resort and every attempt should be made to 
         * provide a meaningful value other than "unknown".
         */
        UNKNOWN("unknown");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
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
         * Factory method for creating EncounterStatus.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
