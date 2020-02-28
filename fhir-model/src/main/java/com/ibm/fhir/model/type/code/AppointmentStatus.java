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
     */
    public static final AppointmentStatus PROPOSED = AppointmentStatus.builder().value(ValueSet.PROPOSED).build();

    /**
     * Pending
     */
    public static final AppointmentStatus PENDING = AppointmentStatus.builder().value(ValueSet.PENDING).build();

    /**
     * Booked
     */
    public static final AppointmentStatus BOOKED = AppointmentStatus.builder().value(ValueSet.BOOKED).build();

    /**
     * Arrived
     */
    public static final AppointmentStatus ARRIVED = AppointmentStatus.builder().value(ValueSet.ARRIVED).build();

    /**
     * Fulfilled
     */
    public static final AppointmentStatus FULFILLED = AppointmentStatus.builder().value(ValueSet.FULFILLED).build();

    /**
     * Cancelled
     */
    public static final AppointmentStatus CANCELLED = AppointmentStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * No Show
     */
    public static final AppointmentStatus NOSHOW = AppointmentStatus.builder().value(ValueSet.NOSHOW).build();

    /**
     * Entered in error
     */
    public static final AppointmentStatus ENTERED_IN_ERROR = AppointmentStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Checked In
     */
    public static final AppointmentStatus CHECKED_IN = AppointmentStatus.builder().value(ValueSet.CHECKED_IN).build();

    /**
     * Waitlisted
     */
    public static final AppointmentStatus WAITLIST = AppointmentStatus.builder().value(ValueSet.WAITLIST).build();

    private volatile int hashCode;

    private AppointmentStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

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

    public static AppointmentStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
         */
        PROPOSED("proposed"),

        /**
         * Pending
         */
        PENDING("pending"),

        /**
         * Booked
         */
        BOOKED("booked"),

        /**
         * Arrived
         */
        ARRIVED("arrived"),

        /**
         * Fulfilled
         */
        FULFILLED("fulfilled"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * No Show
         */
        NOSHOW("noshow"),

        /**
         * Entered in error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Checked In
         */
        CHECKED_IN("checked-in"),

        /**
         * Waitlisted
         */
        WAITLIST("waitlist");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

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
