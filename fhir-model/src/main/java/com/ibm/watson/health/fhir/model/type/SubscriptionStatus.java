/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class SubscriptionStatus extends Code {
    /**
     * Requested
     */
    public static final SubscriptionStatus REQUESTED = SubscriptionStatus.of(ValueSet.REQUESTED);

    /**
     * Active
     */
    public static final SubscriptionStatus ACTIVE = SubscriptionStatus.of(ValueSet.ACTIVE);

    /**
     * Error
     */
    public static final SubscriptionStatus ERROR = SubscriptionStatus.of(ValueSet.ERROR);

    /**
     * Off
     */
    public static final SubscriptionStatus OFF = SubscriptionStatus.of(ValueSet.OFF);

    private volatile int hashCode;

    private SubscriptionStatus(Builder builder) {
        super(builder);
    }

    public static SubscriptionStatus of(java.lang.String value) {
        return SubscriptionStatus.builder().value(value).build();
    }

    public static SubscriptionStatus of(ValueSet value) {
        return SubscriptionStatus.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return SubscriptionStatus.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return SubscriptionStatus.builder().value(value).build();
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
        SubscriptionStatus other = (SubscriptionStatus) obj;
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
        public SubscriptionStatus build() {
            return new SubscriptionStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Requested
         */
        REQUESTED("requested"),

        /**
         * Active
         */
        ACTIVE("active"),

        /**
         * Error
         */
        ERROR("error"),

        /**
         * Off
         */
        OFF("off");

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
