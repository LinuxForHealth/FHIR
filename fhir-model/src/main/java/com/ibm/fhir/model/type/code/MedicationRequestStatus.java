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

@System("http://hl7.org/fhir/CodeSystem/medicationrequest-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MedicationRequestStatus extends Code {
    /**
     * Active
     */
    public static final MedicationRequestStatus ACTIVE = MedicationRequestStatus.builder().value(ValueSet.ACTIVE).build();

    /**
     * On Hold
     */
    public static final MedicationRequestStatus ON_HOLD = MedicationRequestStatus.builder().value(ValueSet.ON_HOLD).build();

    /**
     * Cancelled
     */
    public static final MedicationRequestStatus CANCELLED = MedicationRequestStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * Completed
     */
    public static final MedicationRequestStatus COMPLETED = MedicationRequestStatus.builder().value(ValueSet.COMPLETED).build();

    /**
     * Entered in Error
     */
    public static final MedicationRequestStatus ENTERED_IN_ERROR = MedicationRequestStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Stopped
     */
    public static final MedicationRequestStatus STOPPED = MedicationRequestStatus.builder().value(ValueSet.STOPPED).build();

    /**
     * Draft
     */
    public static final MedicationRequestStatus DRAFT = MedicationRequestStatus.builder().value(ValueSet.DRAFT).build();

    /**
     * Unknown
     */
    public static final MedicationRequestStatus UNKNOWN = MedicationRequestStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private MedicationRequestStatus(Builder builder) {
        super(builder);
    }

    public static MedicationRequestStatus of(ValueSet value) {
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

    public static MedicationRequestStatus of(java.lang.String value) {
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
        public MedicationRequestStatus build() {
            return new MedicationRequestStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Stopped
         */
        STOPPED("stopped"),

        /**
         * Draft
         */
        DRAFT("draft"),

        /**
         * Unknown
         */
        UNKNOWN("unknown");

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
