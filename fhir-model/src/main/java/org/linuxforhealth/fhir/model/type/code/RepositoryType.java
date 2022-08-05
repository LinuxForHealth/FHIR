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

@System("http://hl7.org/fhir/repository-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class RepositoryType extends Code {
    /**
     * Click and see
     * 
     * <p>When URL is clicked, the resource can be seen directly (by webpage or by download link format).
     */
    public static final RepositoryType DIRECTLINK = RepositoryType.builder().value(Value.DIRECTLINK).build();

    /**
     * The URL is the RESTful or other kind of API that can access to the result.
     * 
     * <p>When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can 
     * be seen directly (usually in JSON or XML format).
     */
    public static final RepositoryType OPENAPI = RepositoryType.builder().value(Value.OPENAPI).build();

    /**
     * Result cannot be access unless an account is logged in
     * 
     * <p>When logged into the website, the resource can be seen.
     */
    public static final RepositoryType LOGIN = RepositoryType.builder().value(Value.LOGIN).build();

    /**
     * Result need to be fetched with API and need LOGIN( or cookies are required when visiting the link of resource)
     * 
     * <p>When logged in and follow the API in the website related with URL, the resource can be seen.
     */
    public static final RepositoryType OAUTH = RepositoryType.builder().value(Value.OAUTH).build();

    /**
     * Some other complicated or particular way to get resource from URL.
     * 
     * <p>Some other complicated or particular way to get resource from URL.
     */
    public static final RepositoryType OTHER = RepositoryType.builder().value(Value.OTHER).build();

    private volatile int hashCode;

    private RepositoryType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this RepositoryType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating RepositoryType objects from a passed enum value.
     */
    public static RepositoryType of(Value value) {
        switch (value) {
        case DIRECTLINK:
            return DIRECTLINK;
        case OPENAPI:
            return OPENAPI;
        case LOGIN:
            return LOGIN;
        case OAUTH:
            return OAUTH;
        case OTHER:
            return OTHER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating RepositoryType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static RepositoryType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating RepositoryType objects from a passed string value.
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
     * Inherited factory method for creating RepositoryType objects from a passed string value.
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
        RepositoryType other = (RepositoryType) obj;
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
         *     An enum constant for RepositoryType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public RepositoryType build() {
            RepositoryType repositoryType = new RepositoryType(this);
            if (validating) {
                validate(repositoryType);
            }
            return repositoryType;
        }

        protected void validate(RepositoryType repositoryType) {
            super.validate(repositoryType);
        }

        protected Builder from(RepositoryType repositoryType) {
            super.from(repositoryType);
            return this;
        }
    }

    public enum Value {
        /**
         * Click and see
         * 
         * <p>When URL is clicked, the resource can be seen directly (by webpage or by download link format).
         */
        DIRECTLINK("directlink"),

        /**
         * The URL is the RESTful or other kind of API that can access to the result.
         * 
         * <p>When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can 
         * be seen directly (usually in JSON or XML format).
         */
        OPENAPI("openapi"),

        /**
         * Result cannot be access unless an account is logged in
         * 
         * <p>When logged into the website, the resource can be seen.
         */
        LOGIN("login"),

        /**
         * Result need to be fetched with API and need LOGIN( or cookies are required when visiting the link of resource)
         * 
         * <p>When logged in and follow the API in the website related with URL, the resource can be seen.
         */
        OAUTH("oauth"),

        /**
         * Some other complicated or particular way to get resource from URL.
         * 
         * <p>Some other complicated or particular way to get resource from URL.
         */
        OTHER("other");

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
         * Factory method for creating RepositoryType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding RepositoryType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "directlink":
                return DIRECTLINK;
            case "openapi":
                return OPENAPI;
            case "login":
                return LOGIN;
            case "oauth":
                return OAUTH;
            case "other":
                return OTHER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
