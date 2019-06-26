/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class EligibilityResponsePurpose extends Code {
    /**
     * Coverage auth-requirements
     */
    public static final EligibilityResponsePurpose AUTH_REQUIREMENTS = EligibilityResponsePurpose.of(ValueSet.AUTH_REQUIREMENTS);

    /**
     * Coverage benefits
     */
    public static final EligibilityResponsePurpose BENEFITS = EligibilityResponsePurpose.of(ValueSet.BENEFITS);

    /**
     * Coverage Discovery
     */
    public static final EligibilityResponsePurpose DISCOVERY = EligibilityResponsePurpose.of(ValueSet.DISCOVERY);

    /**
     * Coverage Validation
     */
    public static final EligibilityResponsePurpose VALIDATION = EligibilityResponsePurpose.of(ValueSet.VALIDATION);

    private EligibilityResponsePurpose(Builder builder) {
        super(builder);
    }

    public static EligibilityResponsePurpose of(java.lang.String value) {
        return EligibilityResponsePurpose.builder().value(value).build();
    }

    public static EligibilityResponsePurpose of(ValueSet value) {
        return EligibilityResponsePurpose.builder().value(value).build();
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
        public EligibilityResponsePurpose build() {
            return new EligibilityResponsePurpose(this);
        }
    }

    public enum ValueSet {
        /**
         * Coverage auth-requirements
         */
        AUTH_REQUIREMENTS("auth-requirements"),

        /**
         * Coverage benefits
         */
        BENEFITS("benefits"),

        /**
         * Coverage Discovery
         */
        DISCOVERY("discovery"),

        /**
         * Coverage Validation
         */
        VALIDATION("validation");

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
