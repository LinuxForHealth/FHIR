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

@System("http://hl7.org/fhir/slotstatus")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SlotStatus extends Code {
    /**
     * Busy
     * 
     * <p>Indicates that the time interval is busy because one or more events have been scheduled for that interval.
     */
    public static final SlotStatus BUSY = SlotStatus.builder().value(Value.BUSY).build();

    /**
     * Free
     * 
     * <p>Indicates that the time interval is free for scheduling.
     */
    public static final SlotStatus FREE = SlotStatus.builder().value(Value.FREE).build();

    /**
     * Busy (Unavailable)
     * 
     * <p>Indicates that the time interval is busy and that the interval cannot be scheduled.
     */
    public static final SlotStatus BUSY_UNAVAILABLE = SlotStatus.builder().value(Value.BUSY_UNAVAILABLE).build();

    /**
     * Busy (Tentative)
     * 
     * <p>Indicates that the time interval is busy because one or more events have been tentatively scheduled for that 
     * interval.
     */
    public static final SlotStatus BUSY_TENTATIVE = SlotStatus.builder().value(Value.BUSY_TENTATIVE).build();

    /**
     * Entered in error
     * 
     * <p>This instance should not have been part of this patient's medical record.
     */
    public static final SlotStatus ENTERED_IN_ERROR = SlotStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private SlotStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SlotStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SlotStatus objects from a passed enum value.
     */
    public static SlotStatus of(Value value) {
        switch (value) {
        case BUSY:
            return BUSY;
        case FREE:
            return FREE;
        case BUSY_UNAVAILABLE:
            return BUSY_UNAVAILABLE;
        case BUSY_TENTATIVE:
            return BUSY_TENTATIVE;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SlotStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SlotStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SlotStatus objects from a passed string value.
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
     * Inherited factory method for creating SlotStatus objects from a passed string value.
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
        SlotStatus other = (SlotStatus) obj;
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
         *     An enum constant for SlotStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SlotStatus build() {
            SlotStatus slotStatus = new SlotStatus(this);
            if (validating) {
                validate(slotStatus);
            }
            return slotStatus;
        }

        protected void validate(SlotStatus slotStatus) {
            super.validate(slotStatus);
        }

        protected Builder from(SlotStatus slotStatus) {
            super.from(slotStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Busy
         * 
         * <p>Indicates that the time interval is busy because one or more events have been scheduled for that interval.
         */
        BUSY("busy"),

        /**
         * Free
         * 
         * <p>Indicates that the time interval is free for scheduling.
         */
        FREE("free"),

        /**
         * Busy (Unavailable)
         * 
         * <p>Indicates that the time interval is busy and that the interval cannot be scheduled.
         */
        BUSY_UNAVAILABLE("busy-unavailable"),

        /**
         * Busy (Tentative)
         * 
         * <p>Indicates that the time interval is busy because one or more events have been tentatively scheduled for that 
         * interval.
         */
        BUSY_TENTATIVE("busy-tentative"),

        /**
         * Entered in error
         * 
         * <p>This instance should not have been part of this patient's medical record.
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
         * Factory method for creating SlotStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SlotStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "busy":
                return BUSY;
            case "free":
                return FREE;
            case "busy-unavailable":
                return BUSY_UNAVAILABLE;
            case "busy-tentative":
                return BUSY_TENTATIVE;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
