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

@System("http://terminology.hl7.org/CodeSystem/medicationdispense-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationDispenseStatus extends Code {
    /**
     * Preparation
     * 
     * <p>The core event has not started yet, but some staging activities have begun (e.g. initial compounding or packaging 
     * of medication). Preparation stages may be tracked for billing purposes.
     */
    public static final MedicationDispenseStatus PREPARATION = MedicationDispenseStatus.builder().value(Value.PREPARATION).build();

    /**
     * In Progress
     * 
     * <p>The dispensed product is ready for pickup.
     */
    public static final MedicationDispenseStatus IN_PROGRESS = MedicationDispenseStatus.builder().value(Value.IN_PROGRESS).build();

    /**
     * Cancelled
     * 
     * <p>The dispensed product was not and will never be picked up by the patient.
     */
    public static final MedicationDispenseStatus CANCELLED = MedicationDispenseStatus.builder().value(Value.CANCELLED).build();

    /**
     * On Hold
     * 
     * <p>The dispense process is paused while waiting for an external event to reactivate the dispense. For example, new 
     * stock has arrived or the prescriber has called.
     */
    public static final MedicationDispenseStatus ON_HOLD = MedicationDispenseStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Completed
     * 
     * <p>The dispensed product has been picked up.
     */
    public static final MedicationDispenseStatus COMPLETED = MedicationDispenseStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>The dispense was entered in error and therefore nullified.
     */
    public static final MedicationDispenseStatus ENTERED_IN_ERROR = MedicationDispenseStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Stopped
     * 
     * <p>Actions implied by the dispense have been permanently halted, before all of them occurred.
     */
    public static final MedicationDispenseStatus STOPPED = MedicationDispenseStatus.builder().value(Value.STOPPED).build();

    /**
     * Declined
     * 
     * <p>The dispense was declined and not performed.
     */
    public static final MedicationDispenseStatus DECLINED = MedicationDispenseStatus.builder().value(Value.DECLINED).build();

    /**
     * Unknown
     * 
     * <p>The authoring system does not know which of the status values applies for this medication dispense. Note: this 
     * concept is not to be used for other - one of the listed statuses is presumed to apply, it's just now known which one.
     */
    public static final MedicationDispenseStatus UNKNOWN = MedicationDispenseStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private MedicationDispenseStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MedicationDispenseStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MedicationDispenseStatus objects from a passed enum value.
     */
    public static MedicationDispenseStatus of(Value value) {
        switch (value) {
        case PREPARATION:
            return PREPARATION;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case CANCELLED:
            return CANCELLED;
        case ON_HOLD:
            return ON_HOLD;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case STOPPED:
            return STOPPED;
        case DECLINED:
            return DECLINED;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MedicationDispenseStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MedicationDispenseStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MedicationDispenseStatus objects from a passed string value.
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
     * Inherited factory method for creating MedicationDispenseStatus objects from a passed string value.
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
        MedicationDispenseStatus other = (MedicationDispenseStatus) obj;
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
         *     An enum constant for MedicationDispenseStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MedicationDispenseStatus build() {
            MedicationDispenseStatus medicationDispenseStatus = new MedicationDispenseStatus(this);
            if (validating) {
                validate(medicationDispenseStatus);
            }
            return medicationDispenseStatus;
        }

        protected void validate(MedicationDispenseStatus medicationDispenseStatus) {
            super.validate(medicationDispenseStatus);
        }

        protected Builder from(MedicationDispenseStatus medicationDispenseStatus) {
            super.from(medicationDispenseStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Preparation
         * 
         * <p>The core event has not started yet, but some staging activities have begun (e.g. initial compounding or packaging 
         * of medication). Preparation stages may be tracked for billing purposes.
         */
        PREPARATION("preparation"),

        /**
         * In Progress
         * 
         * <p>The dispensed product is ready for pickup.
         */
        IN_PROGRESS("in-progress"),

        /**
         * Cancelled
         * 
         * <p>The dispensed product was not and will never be picked up by the patient.
         */
        CANCELLED("cancelled"),

        /**
         * On Hold
         * 
         * <p>The dispense process is paused while waiting for an external event to reactivate the dispense. For example, new 
         * stock has arrived or the prescriber has called.
         */
        ON_HOLD("on-hold"),

        /**
         * Completed
         * 
         * <p>The dispensed product has been picked up.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>The dispense was entered in error and therefore nullified.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Stopped
         * 
         * <p>Actions implied by the dispense have been permanently halted, before all of them occurred.
         */
        STOPPED("stopped"),

        /**
         * Declined
         * 
         * <p>The dispense was declined and not performed.
         */
        DECLINED("declined"),

        /**
         * Unknown
         * 
         * <p>The authoring system does not know which of the status values applies for this medication dispense. Note: this 
         * concept is not to be used for other - one of the listed statuses is presumed to apply, it's just now known which one.
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
         * Factory method for creating MedicationDispenseStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MedicationDispenseStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "preparation":
                return PREPARATION;
            case "in-progress":
                return IN_PROGRESS;
            case "cancelled":
                return CANCELLED;
            case "on-hold":
                return ON_HOLD;
            case "completed":
                return COMPLETED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "stopped":
                return STOPPED;
            case "declined":
                return DECLINED;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
