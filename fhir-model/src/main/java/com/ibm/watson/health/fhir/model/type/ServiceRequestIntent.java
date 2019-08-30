/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

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
     * Original Order
     */
    public static final ServiceRequestIntent ORIGINAL_ORDER = ServiceRequestIntent.of(ValueSet.ORIGINAL_ORDER);

    /**
     * Reflex Order
     */
    public static final ServiceRequestIntent REFLEX_ORDER = ServiceRequestIntent.of(ValueSet.REFLEX_ORDER);

    /**
     * Filler Order
     */
    public static final ServiceRequestIntent FILLER_ORDER = ServiceRequestIntent.of(ValueSet.FILLER_ORDER);

    /**
     * Instance Order
     */
    public static final ServiceRequestIntent INSTANCE_ORDER = ServiceRequestIntent.of(ValueSet.INSTANCE_ORDER);

    /**
     * Option
     */
    public static final ServiceRequestIntent OPTION = ServiceRequestIntent.of(ValueSet.OPTION);

    private volatile int hashCode;

    private ServiceRequestIntent(Builder builder) {
        super(builder);
    }

    public static ServiceRequestIntent of(java.lang.String value) {
        return ServiceRequestIntent.builder().value(value).build();
    }

    public static ServiceRequestIntent of(ValueSet value) {
        return ServiceRequestIntent.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ServiceRequestIntent.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ServiceRequestIntent.builder().value(value).build();
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
        ServiceRequestIntent other = (ServiceRequestIntent) obj;
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
         * Original Order
         */
        ORIGINAL_ORDER("original-order"),

        /**
         * Reflex Order
         */
        REFLEX_ORDER("reflex-order"),

        /**
         * Filler Order
         */
        FILLER_ORDER("filler-order"),

        /**
         * Instance Order
         */
        INSTANCE_ORDER("instance-order"),

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
