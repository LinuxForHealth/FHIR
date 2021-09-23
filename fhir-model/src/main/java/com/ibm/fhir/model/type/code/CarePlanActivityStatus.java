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

@System("http://hl7.org/fhir/care-plan-activity-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CarePlanActivityStatus extends Code {
    /**
     * Not Started
     * 
     * <p>Care plan activity is planned but no action has yet been taken.
     */
    public static final CarePlanActivityStatus NOT_STARTED = CarePlanActivityStatus.builder().value(Value.NOT_STARTED).build();

    /**
     * Scheduled
     * 
     * <p>Appointment or other booking has occurred but activity has not yet begun.
     */
    public static final CarePlanActivityStatus SCHEDULED = CarePlanActivityStatus.builder().value(Value.SCHEDULED).build();

    /**
     * In Progress
     * 
     * <p>Care plan activity has been started but is not yet complete.
     */
    public static final CarePlanActivityStatus IN_PROGRESS = CarePlanActivityStatus.builder().value(Value.IN_PROGRESS).build();

    /**
     * On Hold
     * 
     * <p>Care plan activity was started but has temporarily ceased with an expectation of resumption at a future time.
     */
    public static final CarePlanActivityStatus ON_HOLD = CarePlanActivityStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Completed
     * 
     * <p>Care plan activity has been completed (more or less) as planned.
     */
    public static final CarePlanActivityStatus COMPLETED = CarePlanActivityStatus.builder().value(Value.COMPLETED).build();

    /**
     * Cancelled
     * 
     * <p>The planned care plan activity has been withdrawn.
     */
    public static final CarePlanActivityStatus CANCELLED = CarePlanActivityStatus.builder().value(Value.CANCELLED).build();

    /**
     * Stopped
     * 
     * <p>The planned care plan activity has been ended prior to completion after the activity was started.
     */
    public static final CarePlanActivityStatus STOPPED = CarePlanActivityStatus.builder().value(Value.STOPPED).build();

    /**
     * Unknown
     * 
     * <p>The current state of the care plan activity is not known. Note: This concept is not to be used for "other" - one of 
     * the listed statuses is presumed to apply, but the authoring/source system does not know which one.
     */
    public static final CarePlanActivityStatus UNKNOWN = CarePlanActivityStatus.builder().value(Value.UNKNOWN).build();

    /**
     * Entered in Error
     * 
     * <p>Care plan activity was entered in error and voided.
     */
    public static final CarePlanActivityStatus ENTERED_IN_ERROR = CarePlanActivityStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private CarePlanActivityStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CarePlanActivityStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CarePlanActivityStatus objects from a passed enum value.
     */
    public static CarePlanActivityStatus of(Value value) {
        switch (value) {
        case NOT_STARTED:
            return NOT_STARTED;
        case SCHEDULED:
            return SCHEDULED;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case ON_HOLD:
            return ON_HOLD;
        case COMPLETED:
            return COMPLETED;
        case CANCELLED:
            return CANCELLED;
        case STOPPED:
            return STOPPED;
        case UNKNOWN:
            return UNKNOWN;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CarePlanActivityStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CarePlanActivityStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CarePlanActivityStatus objects from a passed string value.
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
     * Inherited factory method for creating CarePlanActivityStatus objects from a passed string value.
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
        CarePlanActivityStatus other = (CarePlanActivityStatus) obj;
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
         *     An enum constant for CarePlanActivityStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CarePlanActivityStatus build() {
            CarePlanActivityStatus carePlanActivityStatus = new CarePlanActivityStatus(this);
            if (validating) {
                validate(carePlanActivityStatus);
            }
            return carePlanActivityStatus;
        }

        protected void validate(CarePlanActivityStatus carePlanActivityStatus) {
            super.validate(carePlanActivityStatus);
        }

        protected Builder from(CarePlanActivityStatus carePlanActivityStatus) {
            super.from(carePlanActivityStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Not Started
         * 
         * <p>Care plan activity is planned but no action has yet been taken.
         */
        NOT_STARTED("not-started"),

        /**
         * Scheduled
         * 
         * <p>Appointment or other booking has occurred but activity has not yet begun.
         */
        SCHEDULED("scheduled"),

        /**
         * In Progress
         * 
         * <p>Care plan activity has been started but is not yet complete.
         */
        IN_PROGRESS("in-progress"),

        /**
         * On Hold
         * 
         * <p>Care plan activity was started but has temporarily ceased with an expectation of resumption at a future time.
         */
        ON_HOLD("on-hold"),

        /**
         * Completed
         * 
         * <p>Care plan activity has been completed (more or less) as planned.
         */
        COMPLETED("completed"),

        /**
         * Cancelled
         * 
         * <p>The planned care plan activity has been withdrawn.
         */
        CANCELLED("cancelled"),

        /**
         * Stopped
         * 
         * <p>The planned care plan activity has been ended prior to completion after the activity was started.
         */
        STOPPED("stopped"),

        /**
         * Unknown
         * 
         * <p>The current state of the care plan activity is not known. Note: This concept is not to be used for "other" - one of 
         * the listed statuses is presumed to apply, but the authoring/source system does not know which one.
         */
        UNKNOWN("unknown"),

        /**
         * Entered in Error
         * 
         * <p>Care plan activity was entered in error and voided.
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
         * Factory method for creating CarePlanActivityStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CarePlanActivityStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "not-started":
                return NOT_STARTED;
            case "scheduled":
                return SCHEDULED;
            case "in-progress":
                return IN_PROGRESS;
            case "on-hold":
                return ON_HOLD;
            case "completed":
                return COMPLETED;
            case "cancelled":
                return CANCELLED;
            case "stopped":
                return STOPPED;
            case "unknown":
                return UNKNOWN;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
