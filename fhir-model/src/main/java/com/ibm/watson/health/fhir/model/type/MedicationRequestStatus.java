/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class MedicationRequestStatus extends Code {
    /**
     * Active
     */
    public static final MedicationRequestStatus ACTIVE = MedicationRequestStatus.of(ValueSet.ACTIVE);

    /**
     * On Hold
     */
    public static final MedicationRequestStatus ON_HOLD = MedicationRequestStatus.of(ValueSet.ON_HOLD);

    /**
     * Cancelled
     */
    public static final MedicationRequestStatus CANCELLED = MedicationRequestStatus.of(ValueSet.CANCELLED);

    /**
     * Completed
     */
    public static final MedicationRequestStatus COMPLETED = MedicationRequestStatus.of(ValueSet.COMPLETED);

    /**
     * Entered in Error
     */
    public static final MedicationRequestStatus ENTERED_IN_ERROR = MedicationRequestStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Stopped
     */
    public static final MedicationRequestStatus STOPPED = MedicationRequestStatus.of(ValueSet.STOPPED);

    /**
     * Draft
     */
    public static final MedicationRequestStatus DRAFT = MedicationRequestStatus.of(ValueSet.DRAFT);

    /**
     * Unknown
     */
    public static final MedicationRequestStatus UNKNOWN = MedicationRequestStatus.of(ValueSet.UNKNOWN);

    private volatile int hashCode;

    private MedicationRequestStatus(Builder builder) {
        super(builder);
    }

    public static MedicationRequestStatus of(java.lang.String value) {
        return MedicationRequestStatus.builder().value(value).build();
    }

    public static MedicationRequestStatus of(ValueSet value) {
        return MedicationRequestStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return MedicationRequestStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return MedicationRequestStatus.builder().value(value).build();
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
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
