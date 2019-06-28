/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class EndpointStatus extends Code {
    /**
     * Active
     */
    public static final EndpointStatus ACTIVE = EndpointStatus.of(ValueSet.ACTIVE);

    /**
     * Suspended
     */
    public static final EndpointStatus SUSPENDED = EndpointStatus.of(ValueSet.SUSPENDED);

    /**
     * Error
     */
    public static final EndpointStatus ERROR = EndpointStatus.of(ValueSet.ERROR);

    /**
     * Off
     */
    public static final EndpointStatus OFF = EndpointStatus.of(ValueSet.OFF);

    /**
     * Entered in error
     */
    public static final EndpointStatus ENTERED_IN_ERROR = EndpointStatus.of(ValueSet.ENTERED_IN_ERROR);

    /**
     * Test
     */
    public static final EndpointStatus TEST = EndpointStatus.of(ValueSet.TEST);

    private EndpointStatus(Builder builder) {
        super(builder);
    }

    public static EndpointStatus of(java.lang.String value) {
        return EndpointStatus.builder().value(value).build();
    }

    public static EndpointStatus of(ValueSet value) {
        return EndpointStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return EndpointStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return EndpointStatus.builder().value(value).build();
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
        public EndpointStatus build() {
            return new EndpointStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Suspended
         */
        SUSPENDED("suspended"),

        /**
         * Error
         */
        ERROR("error"),

        /**
         * Off
         */
        OFF("off"),

        /**
         * Entered in error
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Test
         */
        TEST("test");

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
