/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class FamilyHistoryStatus extends Code {
    /**
     * Partial
     */
    public static final FamilyHistoryStatus PARTIAL = FamilyHistoryStatus.of(ValueSet.PARTIAL);

    /**
     * Completed
     */
    public static final FamilyHistoryStatus COMPLETED = FamilyHistoryStatus.of(ValueSet.COMPLETED);

    /**
     * Entered in Error
     */
    public static final FamilyHistoryStatus ENTERED_IN_ERROR = FamilyHistoryStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Health Unknown
     */
    public static final FamilyHistoryStatus HEALTH_UNKNOWN = FamilyHistoryStatus.of(ValueSet.HEALTH_UNKNOWN);

    private FamilyHistoryStatus(Builder builder) {
        super(builder);
    }

    public static FamilyHistoryStatus of(java.lang.String value) {
        return FamilyHistoryStatus.builder().value(value).build();
    }

    public static FamilyHistoryStatus of(ValueSet value) {
        return FamilyHistoryStatus.builder().value(value).build();
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
        public FamilyHistoryStatus build() {
            return new FamilyHistoryStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Partial
         */
        PARTIAL("partial"),

        /**
         * Completed
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Health Unknown
         */
        HEALTH_UNKNOWN("health-unknown");

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
