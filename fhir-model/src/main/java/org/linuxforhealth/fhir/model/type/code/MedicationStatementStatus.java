/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/CodeSystem/medication-statement-status")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class MedicationStatementStatus extends Code {
    /**
     * Active
     * 
     * <p>The medication is still being taken.
     */
    public static final MedicationStatementStatus ACTIVE = MedicationStatementStatus.builder().value(Value.ACTIVE).build();

    /**
     * Completed
     * 
     * <p>The medication is no longer being taken.
     */
    public static final MedicationStatementStatus COMPLETED = MedicationStatementStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>Some of the actions that are implied by the medication statement may have occurred. For example, the patient may 
     * have taken some of the medication. Clinical decision support systems should take this status into account.
     */
    public static final MedicationStatementStatus ENTERED_IN_ERROR = MedicationStatementStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Intended
     * 
     * <p>The medication may be taken at some time in the future.
     */
    public static final MedicationStatementStatus INTENDED = MedicationStatementStatus.builder().value(Value.INTENDED).build();

    /**
     * Stopped
     * 
     * <p>Actions implied by the statement have been permanently halted, before all of them occurred. This should not be used 
     * if the statement was entered in error.
     */
    public static final MedicationStatementStatus STOPPED = MedicationStatementStatus.builder().value(Value.STOPPED).build();

    /**
     * On Hold
     * 
     * <p>Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be 
     * called 'suspended'.
     */
    public static final MedicationStatementStatus ON_HOLD = MedicationStatementStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Unknown
     * 
     * <p>The state of the medication use is not currently known.
     */
    public static final MedicationStatementStatus UNKNOWN = MedicationStatementStatus.builder().value(Value.UNKNOWN).build();

    /**
     * Not Taken
     * 
     * <p>The medication was not consumed by the patient
     */
    public static final MedicationStatementStatus NOT_TAKEN = MedicationStatementStatus.builder().value(Value.NOT_TAKEN).build();

    private volatile int hashCode;

    private MedicationStatementStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MedicationStatementStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MedicationStatementStatus objects from a passed enum value.
     */
    public static MedicationStatementStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case INTENDED:
            return INTENDED;
        case STOPPED:
            return STOPPED;
        case ON_HOLD:
            return ON_HOLD;
        case UNKNOWN:
            return UNKNOWN;
        case NOT_TAKEN:
            return NOT_TAKEN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MedicationStatementStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MedicationStatementStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MedicationStatementStatus objects from a passed string value.
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
     * Inherited factory method for creating MedicationStatementStatus objects from a passed string value.
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
        MedicationStatementStatus other = (MedicationStatementStatus) obj;
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
         *     An enum constant for MedicationStatementStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MedicationStatementStatus build() {
            MedicationStatementStatus medicationStatementStatus = new MedicationStatementStatus(this);
            if (validating) {
                validate(medicationStatementStatus);
            }
            return medicationStatementStatus;
        }

        protected void validate(MedicationStatementStatus medicationStatementStatus) {
            super.validate(medicationStatementStatus);
        }

        protected Builder from(MedicationStatementStatus medicationStatementStatus) {
            super.from(medicationStatementStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>The medication is still being taken.
         */
        ACTIVE("active"),

        /**
         * Completed
         * 
         * <p>The medication is no longer being taken.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>Some of the actions that are implied by the medication statement may have occurred. For example, the patient may 
         * have taken some of the medication. Clinical decision support systems should take this status into account.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Intended
         * 
         * <p>The medication may be taken at some time in the future.
         */
        INTENDED("intended"),

        /**
         * Stopped
         * 
         * <p>Actions implied by the statement have been permanently halted, before all of them occurred. This should not be used 
         * if the statement was entered in error.
         */
        STOPPED("stopped"),

        /**
         * On Hold
         * 
         * <p>Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be 
         * called 'suspended'.
         */
        ON_HOLD("on-hold"),

        /**
         * Unknown
         * 
         * <p>The state of the medication use is not currently known.
         */
        UNKNOWN("unknown"),

        /**
         * Not Taken
         * 
         * <p>The medication was not consumed by the patient
         */
        NOT_TAKEN("not-taken");

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
         * Factory method for creating MedicationStatementStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MedicationStatementStatus.Value or null if a null value was passed
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
            case "completed":
                return COMPLETED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "intended":
                return INTENDED;
            case "stopped":
                return STOPPED;
            case "on-hold":
                return ON_HOLD;
            case "unknown":
                return UNKNOWN;
            case "not-taken":
                return NOT_TAKEN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
