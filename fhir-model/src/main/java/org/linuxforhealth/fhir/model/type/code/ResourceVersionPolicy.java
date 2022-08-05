/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type.code;

import org.linuxforhealth.fhir.model.annotation.System;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/versioning-policy")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ResourceVersionPolicy extends Code {
    /**
     * No VersionId Support
     * 
     * <p>VersionId meta-property is not supported (server) or used (client).
     */
    public static final ResourceVersionPolicy NO_VERSION = ResourceVersionPolicy.builder().value(Value.NO_VERSION).build();

    /**
     * Versioned
     * 
     * <p>VersionId meta-property is supported (server) or used (client).
     */
    public static final ResourceVersionPolicy VERSIONED = ResourceVersionPolicy.builder().value(Value.VERSIONED).build();

    /**
     * VersionId tracked fully
     * 
     * <p>VersionId must be correct for updates (server) or will be specified (If-match header) for updates (client).
     */
    public static final ResourceVersionPolicy VERSIONED_UPDATE = ResourceVersionPolicy.builder().value(Value.VERSIONED_UPDATE).build();

    private volatile int hashCode;

    private ResourceVersionPolicy(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ResourceVersionPolicy as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ResourceVersionPolicy objects from a passed enum value.
     */
    public static ResourceVersionPolicy of(Value value) {
        switch (value) {
        case NO_VERSION:
            return NO_VERSION;
        case VERSIONED:
            return VERSIONED;
        case VERSIONED_UPDATE:
            return VERSIONED_UPDATE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ResourceVersionPolicy objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ResourceVersionPolicy of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ResourceVersionPolicy objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ResourceVersionPolicy objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(Value.from(value));
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
        ResourceVersionPolicy other = (ResourceVersionPolicy) obj;
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
        return new Builder().from(this);
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
            return (value != null) ? (Builder) super.value(Value.from(value).value()) : this;
        }

        /**
         * Primitive value for code
         * 
         * @param value
         *     An enum constant for ResourceVersionPolicy
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ResourceVersionPolicy build() {
            ResourceVersionPolicy resourceVersionPolicy = new ResourceVersionPolicy(this);
            if (validating) {
                validate(resourceVersionPolicy);
            }
            return resourceVersionPolicy;
        }

        protected void validate(ResourceVersionPolicy resourceVersionPolicy) {
            super.validate(resourceVersionPolicy);
        }

        protected Builder from(ResourceVersionPolicy resourceVersionPolicy) {
            super.from(resourceVersionPolicy);
            return this;
        }
    }

    public enum Value {
        /**
         * No VersionId Support
         * 
         * <p>VersionId meta-property is not supported (server) or used (client).
         */
        NO_VERSION("no-version"),

        /**
         * Versioned
         * 
         * <p>VersionId meta-property is supported (server) or used (client).
         */
        VERSIONED("versioned"),

        /**
         * VersionId tracked fully
         * 
         * <p>VersionId must be correct for updates (server) or will be specified (If-match header) for updates (client).
         */
        VERSIONED_UPDATE("versioned-update");

        private final java.lang.String value;

        Value(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating ResourceVersionPolicy.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ResourceVersionPolicy.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "no-version":
                return NO_VERSION;
            case "versioned":
                return VERSIONED;
            case "versioned-update":
                return VERSIONED_UPDATE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
