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

@System("http://terminology.hl7.org/CodeSystem/medicationdispense-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationDispenseStatus extends Code {
    /**
     * Preparation
     * 
     * <p>The core event has not started yet, but some staging activities have begun (e.g. initial compounding or packaging 
     * of medication). Preparation stages may be tracked for billing purposes.
     */
    public static final MedicationDispenseStatus PREPARATION = MedicationDispenseStatus.builder().value(ValueSet.PREPARATION).build();

    /**
     * In Progress
     * 
     * <p>The dispensed product is ready for pickup.
     */
    public static final MedicationDispenseStatus IN_PROGRESS = MedicationDispenseStatus.builder().value(ValueSet.IN_PROGRESS).build();

    /**
     * Cancelled
     * 
     * <p>The dispensed product was not and will never be picked up by the patient.
     */
    public static final MedicationDispenseStatus CANCELLED = MedicationDispenseStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * On Hold
     * 
     * <p>The dispense process is paused while waiting for an external event to reactivate the dispense. For example, new 
     * stock has arrived or the prescriber has called.
     */
    public static final MedicationDispenseStatus ON_HOLD = MedicationDispenseStatus.builder().value(ValueSet.ON_HOLD).build();

    /**
     * Completed
     * 
     * <p>The dispensed product has been picked up.
     */
    public static final MedicationDispenseStatus COMPLETED = MedicationDispenseStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>The dispense was entered in error and therefore nullified.
     */
    public static final MedicationDispenseStatus ENTERED_IN_ERROR = MedicationDispenseStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Stopped
     * 
     * <p>Actions implied by the dispense have been permanently halted, before all of them occurred.
     */
    public static final MedicationDispenseStatus STOPPED = MedicationDispenseStatus.builder().value(ValueSet.STOPPED).build();

    /**
     * Declined
     * 
     * <p>The dispense was declined and not performed.
     */
    public static final MedicationDispenseStatus DECLINED = MedicationDispenseStatus.builder().value(ValueSet.DECLINED).build();

    /**
     * Unknown
     * 
     * <p>The authoring system does not know which of the status values applies for this medication dispense. Note: this 
     * concept is not to be used for other - one of the listed statuses is presumed to apply, it's just now known which one.
     */
    public static final MedicationDispenseStatus UNKNOWN = MedicationDispenseStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private MedicationDispenseStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating MedicationDispenseStatus objects from a passed enum value.
     */
    public static MedicationDispenseStatus of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public MedicationDispenseStatus build() {
            return new MedicationDispenseStatus(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating MedicationDispenseStatus.ValueSet values from a passed string value.
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
