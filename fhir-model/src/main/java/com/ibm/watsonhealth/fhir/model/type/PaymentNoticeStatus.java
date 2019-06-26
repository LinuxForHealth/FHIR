/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class PaymentNoticeStatus extends Code {
    /**
     * Active
     */
    public static final PaymentNoticeStatus ACTIVE = PaymentNoticeStatus.of(ValueSet.ACTIVE);

    /**
     * Cancelled
     */
    public static final PaymentNoticeStatus CANCELLED = PaymentNoticeStatus.of(ValueSet.CANCELLED);

    /**
     * Draft
     */
    public static final PaymentNoticeStatus DRAFT = PaymentNoticeStatus.of(ValueSet.DRAFT);

    /**
     * Entered in Error
     */
    public static final PaymentNoticeStatus ENTERED_IN_ERROR = PaymentNoticeStatus.of(ValueSet.ENTERED_IN_ERROR);

    private PaymentNoticeStatus(Builder builder) {
        super(builder);
    }

    public static PaymentNoticeStatus of(java.lang.String value) {
        return PaymentNoticeStatus.builder().value(value).build();
    }

    public static PaymentNoticeStatus of(ValueSet value) {
        return PaymentNoticeStatus.builder().value(value).build();
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
        public PaymentNoticeStatus build() {
            return new PaymentNoticeStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Cancelled
         */
        CANCELLED("cancelled"),

        /**
         * Draft
         */
        DRAFT("draft"),

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
