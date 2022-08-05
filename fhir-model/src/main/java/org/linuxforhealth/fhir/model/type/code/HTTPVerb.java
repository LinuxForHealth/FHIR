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

@System("http://hl7.org/fhir/http-verb")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class HTTPVerb extends Code {
    /**
     * GET
     * 
     * <p>HTTP GET Command.
     */
    public static final HTTPVerb GET = HTTPVerb.builder().value(Value.GET).build();

    /**
     * HEAD
     * 
     * <p>HTTP HEAD Command.
     */
    public static final HTTPVerb HEAD = HTTPVerb.builder().value(Value.HEAD).build();

    /**
     * POST
     * 
     * <p>HTTP POST Command.
     */
    public static final HTTPVerb POST = HTTPVerb.builder().value(Value.POST).build();

    /**
     * PUT
     * 
     * <p>HTTP PUT Command.
     */
    public static final HTTPVerb PUT = HTTPVerb.builder().value(Value.PUT).build();

    /**
     * DELETE
     * 
     * <p>HTTP DELETE Command.
     */
    public static final HTTPVerb DELETE = HTTPVerb.builder().value(Value.DELETE).build();

    /**
     * PATCH
     * 
     * <p>HTTP PATCH Command.
     */
    public static final HTTPVerb PATCH = HTTPVerb.builder().value(Value.PATCH).build();

    private volatile int hashCode;

    private HTTPVerb(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this HTTPVerb as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating HTTPVerb objects from a passed enum value.
     */
    public static HTTPVerb of(Value value) {
        switch (value) {
        case GET:
            return GET;
        case HEAD:
            return HEAD;
        case POST:
            return POST;
        case PUT:
            return PUT;
        case DELETE:
            return DELETE;
        case PATCH:
            return PATCH;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating HTTPVerb objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static HTTPVerb of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating HTTPVerb objects from a passed string value.
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
     * Inherited factory method for creating HTTPVerb objects from a passed string value.
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
        HTTPVerb other = (HTTPVerb) obj;
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
         *     An enum constant for HTTPVerb
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public HTTPVerb build() {
            HTTPVerb hTTPVerb = new HTTPVerb(this);
            if (validating) {
                validate(hTTPVerb);
            }
            return hTTPVerb;
        }

        protected void validate(HTTPVerb hTTPVerb) {
            super.validate(hTTPVerb);
        }

        protected Builder from(HTTPVerb hTTPVerb) {
            super.from(hTTPVerb);
            return this;
        }
    }

    public enum Value {
        /**
         * GET
         * 
         * <p>HTTP GET Command.
         */
        GET("GET"),

        /**
         * HEAD
         * 
         * <p>HTTP HEAD Command.
         */
        HEAD("HEAD"),

        /**
         * POST
         * 
         * <p>HTTP POST Command.
         */
        POST("POST"),

        /**
         * PUT
         * 
         * <p>HTTP PUT Command.
         */
        PUT("PUT"),

        /**
         * DELETE
         * 
         * <p>HTTP DELETE Command.
         */
        DELETE("DELETE"),

        /**
         * PATCH
         * 
         * <p>HTTP PATCH Command.
         */
        PATCH("PATCH");

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
         * Factory method for creating HTTPVerb.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding HTTPVerb.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "GET":
                return GET;
            case "HEAD":
                return HEAD;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
            case "PATCH":
                return PATCH;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
