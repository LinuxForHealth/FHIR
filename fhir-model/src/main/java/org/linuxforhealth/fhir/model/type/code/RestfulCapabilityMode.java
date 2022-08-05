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

@System("http://hl7.org/fhir/restful-capability-mode")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class RestfulCapabilityMode extends Code {
    /**
     * Client
     * 
     * <p>The application acts as a client for this resource.
     */
    public static final RestfulCapabilityMode CLIENT = RestfulCapabilityMode.builder().value(Value.CLIENT).build();

    /**
     * Server
     * 
     * <p>The application acts as a server for this resource.
     */
    public static final RestfulCapabilityMode SERVER = RestfulCapabilityMode.builder().value(Value.SERVER).build();

    private volatile int hashCode;

    private RestfulCapabilityMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this RestfulCapabilityMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating RestfulCapabilityMode objects from a passed enum value.
     */
    public static RestfulCapabilityMode of(Value value) {
        switch (value) {
        case CLIENT:
            return CLIENT;
        case SERVER:
            return SERVER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating RestfulCapabilityMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static RestfulCapabilityMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating RestfulCapabilityMode objects from a passed string value.
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
     * Inherited factory method for creating RestfulCapabilityMode objects from a passed string value.
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
        RestfulCapabilityMode other = (RestfulCapabilityMode) obj;
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
         *     An enum constant for RestfulCapabilityMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public RestfulCapabilityMode build() {
            RestfulCapabilityMode restfulCapabilityMode = new RestfulCapabilityMode(this);
            if (validating) {
                validate(restfulCapabilityMode);
            }
            return restfulCapabilityMode;
        }

        protected void validate(RestfulCapabilityMode restfulCapabilityMode) {
            super.validate(restfulCapabilityMode);
        }

        protected Builder from(RestfulCapabilityMode restfulCapabilityMode) {
            super.from(restfulCapabilityMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Client
         * 
         * <p>The application acts as a client for this resource.
         */
        CLIENT("client"),

        /**
         * Server
         * 
         * <p>The application acts as a server for this resource.
         */
        SERVER("server");

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
         * Factory method for creating RestfulCapabilityMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding RestfulCapabilityMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "client":
                return CLIENT;
            case "server":
                return SERVER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
