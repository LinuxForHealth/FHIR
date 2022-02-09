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

@System("http://hl7.org/fhir/history-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class FamilyHistoryStatus extends Code {
    /**
     * Partial
     * 
     * <p>Some health information is known and captured, but not complete - see notes for details.
     */
    public static final FamilyHistoryStatus PARTIAL = FamilyHistoryStatus.builder().value(Value.PARTIAL).build();

    /**
     * Completed
     * 
     * <p>All available related health information is captured as of the date (and possibly time) when the family member 
     * history was taken.
     */
    public static final FamilyHistoryStatus COMPLETED = FamilyHistoryStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>This instance should not have been part of this patient's medical record.
     */
    public static final FamilyHistoryStatus ENTERED_IN_ERROR = FamilyHistoryStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Health Unknown
     * 
     * <p>Health information for this family member is unavailable/unknown.
     */
    public static final FamilyHistoryStatus HEALTH_UNKNOWN = FamilyHistoryStatus.builder().value(Value.HEALTH_UNKNOWN).build();

    private volatile int hashCode;

    private FamilyHistoryStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this FamilyHistoryStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating FamilyHistoryStatus objects from a passed enum value.
     */
    public static FamilyHistoryStatus of(Value value) {
        switch (value) {
        case PARTIAL:
            return PARTIAL;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case HEALTH_UNKNOWN:
            return HEALTH_UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating FamilyHistoryStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static FamilyHistoryStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating FamilyHistoryStatus objects from a passed string value.
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
     * Inherited factory method for creating FamilyHistoryStatus objects from a passed string value.
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
        FamilyHistoryStatus other = (FamilyHistoryStatus) obj;
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
         *     An enum constant for FamilyHistoryStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public FamilyHistoryStatus build() {
            FamilyHistoryStatus familyHistoryStatus = new FamilyHistoryStatus(this);
            if (validating) {
                validate(familyHistoryStatus);
            }
            return familyHistoryStatus;
        }

        protected void validate(FamilyHistoryStatus familyHistoryStatus) {
            super.validate(familyHistoryStatus);
        }

        protected Builder from(FamilyHistoryStatus familyHistoryStatus) {
            super.from(familyHistoryStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Partial
         * 
         * <p>Some health information is known and captured, but not complete - see notes for details.
         */
        PARTIAL("partial"),

        /**
         * Completed
         * 
         * <p>All available related health information is captured as of the date (and possibly time) when the family member 
         * history was taken.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>This instance should not have been part of this patient's medical record.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Health Unknown
         * 
         * <p>Health information for this family member is unavailable/unknown.
         */
        HEALTH_UNKNOWN("health-unknown");

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
         * Factory method for creating FamilyHistoryStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding FamilyHistoryStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "partial":
                return PARTIAL;
            case "completed":
                return COMPLETED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "health-unknown":
                return HEALTH_UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
