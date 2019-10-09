/*
 * (C) Copyright IBM Corp. 2019
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

    private volatile int hashCode;

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
        EndpointStatus other = (EndpointStatus) obj;
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
