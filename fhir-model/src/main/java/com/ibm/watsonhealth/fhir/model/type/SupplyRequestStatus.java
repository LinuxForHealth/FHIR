/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class SupplyRequestStatus extends Code {
    /**
     * Draft
     */
    public static final SupplyRequestStatus DRAFT = SupplyRequestStatus.of(ValueSet.DRAFT);

    /**
     * Active
     */
    public static final SupplyRequestStatus ACTIVE = SupplyRequestStatus.of(ValueSet.ACTIVE);

    /**
     * Suspended
     */
    public static final SupplyRequestStatus SUSPENDED = SupplyRequestStatus.of(ValueSet.SUSPENDED);

    /**
     * Cancelled
     */
    public static final SupplyRequestStatus CANCELLED = SupplyRequestStatus.of(ValueSet.CANCELLED);

    /**
     * Completed
     */
    public static final SupplyRequestStatus COMPLETED = SupplyRequestStatus.of(ValueSet.COMPLETED);

    /**
     * Entered in Error
     */
    public static final SupplyRequestStatus ENTERED_IN_ERROR = SupplyRequestStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Unknown
     */
    public static final SupplyRequestStatus UNKNOWN = SupplyRequestStatus.of(ValueSet.UNKNOWN);

    private SupplyRequestStatus(Builder builder) {
        super(builder);
    }

    public static SupplyRequestStatus of(java.lang.String value) {
        return SupplyRequestStatus.builder().value(value).build();
    }

    public static SupplyRequestStatus of(ValueSet value) {
        return SupplyRequestStatus.builder().value(value).build();
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
        public SupplyRequestStatus build() {
            return new SupplyRequestStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Draft
         */
        DRAFT("draft"),

        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Suspended
         */
        SUSPENDED("suspended"),

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
