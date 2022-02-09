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

@System("http://hl7.org/fhir/CodeSystem/medicationrequest-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationRequestStatus extends Code {
    /**
     * Active
     * 
     * <p>The prescription is 'actionable', but not all actions that are implied by it have occurred yet.
     */
    public static final MedicationRequestStatus ACTIVE = MedicationRequestStatus.builder().value(Value.ACTIVE).build();

    /**
     * On Hold
     * 
     * <p>Actions implied by the prescription are to be temporarily halted, but are expected to continue later. May also be 
     * called 'suspended'.
     */
    public static final MedicationRequestStatus ON_HOLD = MedicationRequestStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Cancelled
     * 
     * <p>The prescription has been withdrawn before any administrations have occurred
     */
    public static final MedicationRequestStatus CANCELLED = MedicationRequestStatus.builder().value(Value.CANCELLED).build();

    /**
     * Completed
     * 
     * <p>All actions that are implied by the prescription have occurred.
     */
    public static final MedicationRequestStatus COMPLETED = MedicationRequestStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>Some of the actions that are implied by the medication request may have occurred. For example, the medication may 
     * have been dispensed and the patient may have taken some of the medication. Clinical decision support systems should 
     * take this status into account
     */
    public static final MedicationRequestStatus ENTERED_IN_ERROR = MedicationRequestStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Stopped
     * 
     * <p>Actions implied by the prescription are to be permanently halted, before all of the administrations occurred. This 
     * should not be used if the original order was entered in error
     */
    public static final MedicationRequestStatus STOPPED = MedicationRequestStatus.builder().value(Value.STOPPED).build();

    /**
     * Draft
     * 
     * <p>The prescription is not yet 'actionable', e.g. it is a work in progress, requires sign-off, verification or needs 
     * to be run through decision support process.
     */
    public static final MedicationRequestStatus DRAFT = MedicationRequestStatus.builder().value(Value.DRAFT).build();

    /**
     * Unknown
     * 
     * <p>The authoring/source system does not know which of the status values currently applies for this observation. Note: 
     * This concept is not to be used for 'other' - one of the listed statuses is presumed to apply, but the authoring/source 
     * system does not know which.
     */
    public static final MedicationRequestStatus UNKNOWN = MedicationRequestStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private MedicationRequestStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MedicationRequestStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MedicationRequestStatus objects from a passed enum value.
     */
    public static MedicationRequestStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case ON_HOLD:
            return ON_HOLD;
        case CANCELLED:
            return CANCELLED;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case STOPPED:
            return STOPPED;
        case DRAFT:
            return DRAFT;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MedicationRequestStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MedicationRequestStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MedicationRequestStatus objects from a passed string value.
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
     * Inherited factory method for creating MedicationRequestStatus objects from a passed string value.
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
        MedicationRequestStatus other = (MedicationRequestStatus) obj;
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
         *     An enum constant for MedicationRequestStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MedicationRequestStatus build() {
            MedicationRequestStatus medicationRequestStatus = new MedicationRequestStatus(this);
            if (validating) {
                validate(medicationRequestStatus);
            }
            return medicationRequestStatus;
        }

        protected void validate(MedicationRequestStatus medicationRequestStatus) {
            super.validate(medicationRequestStatus);
        }

        protected Builder from(MedicationRequestStatus medicationRequestStatus) {
            super.from(medicationRequestStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>The prescription is 'actionable', but not all actions that are implied by it have occurred yet.
         */
        ACTIVE("active"),

        /**
         * On Hold
         * 
         * <p>Actions implied by the prescription are to be temporarily halted, but are expected to continue later. May also be 
         * called 'suspended'.
         */
        ON_HOLD("on-hold"),

        /**
         * Cancelled
         * 
         * <p>The prescription has been withdrawn before any administrations have occurred
         */
        CANCELLED("cancelled"),

        /**
         * Completed
         * 
         * <p>All actions that are implied by the prescription have occurred.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>Some of the actions that are implied by the medication request may have occurred. For example, the medication may 
         * have been dispensed and the patient may have taken some of the medication. Clinical decision support systems should 
         * take this status into account
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Stopped
         * 
         * <p>Actions implied by the prescription are to be permanently halted, before all of the administrations occurred. This 
         * should not be used if the original order was entered in error
         */
        STOPPED("stopped"),

        /**
         * Draft
         * 
         * <p>The prescription is not yet 'actionable', e.g. it is a work in progress, requires sign-off, verification or needs 
         * to be run through decision support process.
         */
        DRAFT("draft"),

        /**
         * Unknown
         * 
         * <p>The authoring/source system does not know which of the status values currently applies for this observation. Note: 
         * This concept is not to be used for 'other' - one of the listed statuses is presumed to apply, but the authoring/source 
         * system does not know which.
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
         * Factory method for creating MedicationRequestStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MedicationRequestStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "active":
                return ACTIVE;
            case "on-hold":
                return ON_HOLD;
            case "cancelled":
                return CANCELLED;
            case "completed":
                return COMPLETED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "stopped":
                return STOPPED;
            case "draft":
                return DRAFT;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
