/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class MedicationDispenseStatus extends Code {
    /**
     * Preparation
     */
    public static final MedicationDispenseStatus PREPARATION = MedicationDispenseStatus.of(ValueSet.PREPARATION);

    /**
     * In Progress
     */
    public static final MedicationDispenseStatus IN_PROGRESS = MedicationDispenseStatus.of(ValueSet.IN_PROGRESS);

    /**
     * Cancelled
     */
    public static final MedicationDispenseStatus CANCELLED = MedicationDispenseStatus.of(ValueSet.CANCELLED);

    /**
     * On Hold
     */
    public static final MedicationDispenseStatus ON_HOLD = MedicationDispenseStatus.of(ValueSet.ON_HOLD);

    /**
     * Completed
     */
    public static final MedicationDispenseStatus COMPLETED = MedicationDispenseStatus.of(ValueSet.COMPLETED);

    /**
     * Entered in Error
     */
    public static final MedicationDispenseStatus ENTERED_IN_ERROR = MedicationDispenseStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Stopped
     */
    public static final MedicationDispenseStatus STOPPED = MedicationDispenseStatus.of(ValueSet.STOPPED);

    /**
     * Declined
     */
    public static final MedicationDispenseStatus DECLINED = MedicationDispenseStatus.of(ValueSet.DECLINED);

    /**
     * Unknown
     */
    public static final MedicationDispenseStatus UNKNOWN = MedicationDispenseStatus.of(ValueSet.UNKNOWN);

    private volatile int hashCode;

    private MedicationDispenseStatus(Builder builder) {
        super(builder);
    }

    public static MedicationDispenseStatus of(java.lang.String value) {
        return MedicationDispenseStatus.builder().value(value).build();
    }

    public static MedicationDispenseStatus of(ValueSet value) {
        return MedicationDispenseStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return MedicationDispenseStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return MedicationDispenseStatus.builder().value(value).build();
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
        public MedicationDispenseStatus build() {
            return new MedicationDispenseStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Preparation
         */
        PREPARATION("preparation"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold"),

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
         * Declined
         */
        DECLINED("declined"),

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
