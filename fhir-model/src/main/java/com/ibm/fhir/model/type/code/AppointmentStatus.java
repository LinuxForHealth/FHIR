/*
 * (C) Copyright IBM Corp. 2019, 2020
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

@System("http://hl7.org/fhir/appointmentstatus")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AppointmentStatus extends Code {
    /**
     * Proposed
     * 
     * <p>None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time might 
     * not be set yet.
     */
    public static final AppointmentStatus PROPOSED = AppointmentStatus.builder().value(ValueSet.PROPOSED).build();

    /**
     * Pending
     * 
     * <p>Some or all of the participant(s) have not finalized their acceptance of the appointment request.
     */
    public static final AppointmentStatus PENDING = AppointmentStatus.builder().value(ValueSet.PENDING).build();

    /**
     * Booked
     * 
     * <p>All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.
     */
    public static final AppointmentStatus BOOKED = AppointmentStatus.builder().value(ValueSet.BOOKED).build();

    /**
     * Arrived
     * 
     * <p>The patient/patients has/have arrived and is/are waiting to be seen.
     */
    public static final AppointmentStatus ARRIVED = AppointmentStatus.builder().value(ValueSet.ARRIVED).build();

    /**
     * Fulfilled
     * 
     * <p>The planning stages of the appointment are now complete, the encounter resource will exist and will track further 
     * status changes. Note that an encounter may exist before the appointment status is fulfilled for many reasons.
     */
    public static final AppointmentStatus FULFILLED = AppointmentStatus.builder().value(ValueSet.FULFILLED).build();

    /**
     * Cancelled
     * 
     * <p>The appointment has been cancelled.
     */
    public static final AppointmentStatus CANCELLED = AppointmentStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * No Show
     * 
     * <p>Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).
     */
    public static final AppointmentStatus NOSHOW = AppointmentStatus.builder().value(ValueSet.NOSHOW).build();

    /**
     * Entered in error
     * 
     * <p>This instance should not have been part of this patient's medical record.
     */
    public static final AppointmentStatus ENTERED_IN_ERROR = AppointmentStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Checked In
     * 
     * <p>When checked in, all pre-encounter administrative work is complete, and the encounter may begin. (where multiple 
     * patients are involved, they are all present).
     */
    public static final AppointmentStatus CHECKED_IN = AppointmentStatus.builder().value(ValueSet.CHECKED_IN).build();

    /**
     * Waitlisted
     * 
     * <p>The appointment has been placed on a waitlist, to be scheduled/confirmed in the future when a slot/service is 
     * available.
     * <p>A specific time might or might not be pre-allocated.
     */
    public static final AppointmentStatus WAITLIST = AppointmentStatus.builder().value(ValueSet.WAITLIST).build();

    private volatile int hashCode;

    private AppointmentStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating AppointmentStatus objects from a passed enum value.
     */
    public static AppointmentStatus of(ValueSet value) {
        switch (value) {
        case PROPOSED:
            return PROPOSED;
        case PENDING:
            return PENDING;
        case BOOKED:
            return BOOKED;
        case ARRIVED:
            return ARRIVED;
        case FULFILLED:
            return FULFILLED;
        case CANCELLED:
            return CANCELLED;
        case NOSHOW:
            return NOSHOW;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case CHECKED_IN:
            return CHECKED_IN;
        case WAITLIST:
            return WAITLIST;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AppointmentStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AppointmentStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating AppointmentStatus objects from a passed string value.
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
     * Inherited factory method for creating AppointmentStatus objects from a passed string value.
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
        AppointmentStatus other = (AppointmentStatus) obj;
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
        public AppointmentStatus build() {
            return new AppointmentStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Proposed
         * 
         * <p>None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time might 
         * not be set yet.
         */
        PROPOSED("proposed"),

        /**
         * Pending
         * 
         * <p>Some or all of the participant(s) have not finalized their acceptance of the appointment request.
         */
        PENDING("pending"),

        /**
         * Booked
         * 
         * <p>All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.
         */
        BOOKED("booked"),

        /**
         * Arrived
         * 
         * <p>The patient/patients has/have arrived and is/are waiting to be seen.
         */
        ARRIVED("arrived"),

        /**
         * Fulfilled
         * 
         * <p>The planning stages of the appointment are now complete, the encounter resource will exist and will track further 
         * status changes. Note that an encounter may exist before the appointment status is fulfilled for many reasons.
         */
        FULFILLED("fulfilled"),

        /**
         * Cancelled
         * 
         * <p>The appointment has been cancelled.
         */
        CANCELLED("cancelled"),

        /**
         * No Show
         * 
         * <p>Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).
         */
        NOSHOW("noshow"),

        /**
         * Entered in error
         * 
         * <p>This instance should not have been part of this patient's medical record.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Checked In
         * 
         * <p>When checked in, all pre-encounter administrative work is complete, and the encounter may begin. (where multiple 
         * patients are involved, they are all present).
         */
        CHECKED_IN("checked-in"),

        /**
         * Waitlisted
         * 
         * <p>The appointment has been placed on a waitlist, to be scheduled/confirmed in the future when a slot/service is 
         * available.
         * <p>A specific time might or might not be pre-allocated.
         */
        WAITLIST("waitlist");

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
         * Factory method for creating AppointmentStatus.ValueSet values from a passed string value.
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
