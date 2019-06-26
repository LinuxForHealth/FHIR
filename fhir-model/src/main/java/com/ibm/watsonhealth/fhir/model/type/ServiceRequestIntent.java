/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class ServiceRequestIntent extends Code {
    /**
     * Proposal
     */
    public static final ServiceRequestIntent PROPOSAL = ServiceRequestIntent.of(ValueSet.PROPOSAL);

    /**
     * Plan
     */
    public static final ServiceRequestIntent PLAN = ServiceRequestIntent.of(ValueSet.PLAN);

    /**
     * Directive
     */
    public static final ServiceRequestIntent DIRECTIVE = ServiceRequestIntent.of(ValueSet.DIRECTIVE);

    /**
     * Order
     */
    public static final ServiceRequestIntent ORDER = ServiceRequestIntent.of(ValueSet.ORDER);

    /**
     * Option
     */
    public static final ServiceRequestIntent OPTION = ServiceRequestIntent.of(ValueSet.OPTION);

    private ServiceRequestIntent(Builder builder) {
        super(builder);
    }

    public static ServiceRequestIntent of(java.lang.String value) {
        return ServiceRequestIntent.builder().value(value).build();
    }

    public static ServiceRequestIntent of(ValueSet value) {
        return ServiceRequestIntent.builder().value(value).build();
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
        public ServiceRequestIntent build() {
            return new ServiceRequestIntent(this);
        }
    }

    public enum ValueSet {
        /**
         * Proposal
         */
        PROPOSAL("proposal"),

        /**
         * Plan
         */
        PLAN("plan"),

        /**
         * Directive
         */
        DIRECTIVE("directive"),

        /**
         * Order
         */
        ORDER("order"),

        /**
         * Option
         */
        OPTION("option");

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
