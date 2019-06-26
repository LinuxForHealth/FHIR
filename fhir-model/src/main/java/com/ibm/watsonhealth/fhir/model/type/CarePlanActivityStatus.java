/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class CarePlanActivityStatus extends Code {
    /**
     * Not Started
     */
    public static final CarePlanActivityStatus NOT_STARTED = CarePlanActivityStatus.of(ValueSet.NOT_STARTED);

    /**
     * Scheduled
     */
    public static final CarePlanActivityStatus SCHEDULED = CarePlanActivityStatus.of(ValueSet.SCHEDULED);

    /**
     * In Progress
     */
    public static final CarePlanActivityStatus IN_PROGRESS = CarePlanActivityStatus.of(ValueSet.IN_PROGRESS);

    /**
     * On Hold
     */
    public static final CarePlanActivityStatus ON_HOLD = CarePlanActivityStatus.of(ValueSet.ON_HOLD);

    /**
     * Completed
     */
    public static final CarePlanActivityStatus COMPLETED = CarePlanActivityStatus.of(ValueSet.COMPLETED);

    /**
     * Cancelled
     */
    public static final CarePlanActivityStatus CANCELLED = CarePlanActivityStatus.of(ValueSet.CANCELLED);

    /**
     * Unknown
     */
    public static final CarePlanActivityStatus UNKNOWN = CarePlanActivityStatus.of(ValueSet.UNKNOWN);

    /**
     * Entered in Error
     */
    public static final CarePlanActivityStatus ENTERED_IN_ERROR = CarePlanActivityStatus.of(ValueSet.ENTERED_IN_ERROR);

    private CarePlanActivityStatus(Builder builder) {
        super(builder);
    }

    public static CarePlanActivityStatus of(java.lang.String value) {
        return CarePlanActivityStatus.builder().value(value).build();
    }

    public static CarePlanActivityStatus of(ValueSet value) {
        return CarePlanActivityStatus.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
        public CarePlanActivityStatus build() {
            return new CarePlanActivityStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Not Started
         */
        NOT_STARTED("not-started"),

        /**
         * Scheduled
         */
        SCHEDULED("scheduled"),

        /**
         * In Progress
         */
        IN_PROGRESS("in-progress"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Unknown
         */
        UNKNOWN("unknown"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error");

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
