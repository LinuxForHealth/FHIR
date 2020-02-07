/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubscriptionStatus extends Code {
    /**
     * Requested
     */
    public static final SubscriptionStatus REQUESTED = SubscriptionStatus.builder().value(ValueSet.REQUESTED).build();

    /**
     * Active
     */
    public static final SubscriptionStatus ACTIVE = SubscriptionStatus.builder().value(ValueSet.ACTIVE).build();

    /**
     * Error
     */
    public static final SubscriptionStatus ERROR = SubscriptionStatus.builder().value(ValueSet.ERROR).build();

    /**
     * Off
     */
    public static final SubscriptionStatus OFF = SubscriptionStatus.builder().value(ValueSet.OFF).build();

    private volatile int hashCode;

    private SubscriptionStatus(Builder builder) {
        super(builder);
    }

    public static SubscriptionStatus of(ValueSet value) {
        switch (value) {
        case REQUESTED:
            return REQUESTED;
        case ACTIVE:
            return ACTIVE;
        case ERROR:
            return ERROR;
        case OFF:
            return OFF;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static SubscriptionStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
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
