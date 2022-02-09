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

@System("http://terminology.hl7.org/CodeSystem/medicationknowledge-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationKnowledgeStatus extends Code {
    /**
     * Active
     * 
     * <p>The medication is available for use.
     */
    public static final MedicationKnowledgeStatus ACTIVE = MedicationKnowledgeStatus.builder().value(Value.ACTIVE).build();

    /**
     * Inactive
     * 
     * <p>The medication is not available for use.
     */
    public static final MedicationKnowledgeStatus INACTIVE = MedicationKnowledgeStatus.builder().value(Value.INACTIVE).build();

    /**
     * Entered in Error
     * 
     * <p>The medication was entered in error.
     */
    public static final MedicationKnowledgeStatus ENTERED_IN_ERROR = MedicationKnowledgeStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private MedicationKnowledgeStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MedicationKnowledgeStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MedicationKnowledgeStatus objects from a passed enum value.
     */
    public static MedicationKnowledgeStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case INACTIVE:
            return INACTIVE;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MedicationKnowledgeStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MedicationKnowledgeStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MedicationKnowledgeStatus objects from a passed string value.
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
     * Inherited factory method for creating MedicationKnowledgeStatus objects from a passed string value.
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
        MedicationKnowledgeStatus other = (MedicationKnowledgeStatus) obj;
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
         *     An enum constant for MedicationKnowledgeStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MedicationKnowledgeStatus build() {
            MedicationKnowledgeStatus medicationKnowledgeStatus = new MedicationKnowledgeStatus(this);
            if (validating) {
                validate(medicationKnowledgeStatus);
            }
            return medicationKnowledgeStatus;
        }

        protected void validate(MedicationKnowledgeStatus medicationKnowledgeStatus) {
            super.validate(medicationKnowledgeStatus);
        }

        protected Builder from(MedicationKnowledgeStatus medicationKnowledgeStatus) {
            super.from(medicationKnowledgeStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>The medication is available for use.
         */
        ACTIVE("active"),

        /**
         * Inactive
         * 
         * <p>The medication is not available for use.
         */
        INACTIVE("inactive"),

        /**
         * Entered in Error
         * 
         * <p>The medication was entered in error.
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
         * Factory method for creating MedicationKnowledgeStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MedicationKnowledgeStatus.Value or null if a null value was passed
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
            case "inactive":
                return INACTIVE;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
