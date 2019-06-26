/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class AccountStatus extends Code {
    /**
     * Active
     */
    public static final AccountStatus ACTIVE = AccountStatus.of(ValueSet.ACTIVE);

    /**
     * Inactive
     */
    public static final AccountStatus INACTIVE = AccountStatus.of(ValueSet.INACTIVE);

    /**
     * Entered in error
     */
    public static final AccountStatus ENTERED_IN_ERROR = AccountStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * On Hold
     */
    public static final AccountStatus ON_HOLD = AccountStatus.of(ValueSet.ON_HOLD);

    /**
     * Unknown
     */
    public static final AccountStatus UNKNOWN = AccountStatus.of(ValueSet.UNKNOWN);

    private AccountStatus(Builder builder) {
        super(builder);
    }

    public static AccountStatus of(java.lang.String value) {
        return AccountStatus.builder().value(value).build();
    }

    public static AccountStatus of(ValueSet value) {
        return AccountStatus.builder().value(value).build();
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
        public AccountStatus build() {
            return new AccountStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Inactive
         */
        INACTIVE("inactive"),

        /**
         * Entered in error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * On Hold
         */
        ON_HOLD("on-hold"),

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
