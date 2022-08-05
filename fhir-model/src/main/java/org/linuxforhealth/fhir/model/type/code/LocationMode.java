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

@System("http://hl7.org/fhir/location-mode")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class LocationMode extends Code {
    /**
     * Instance
     * 
     * <p>The Location resource represents a specific instance of a location (e.g. Operating Theatre 1A).
     */
    public static final LocationMode INSTANCE = LocationMode.builder().value(Value.INSTANCE).build();

    /**
     * Kind
     * 
     * <p>The Location represents a class of locations (e.g. Any Operating Theatre) although this class of locations could be 
     * constrained within a specific boundary (such as organization, or parent location, address etc.).
     */
    public static final LocationMode KIND = LocationMode.builder().value(Value.KIND).build();

    private volatile int hashCode;

    private LocationMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this LocationMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating LocationMode objects from a passed enum value.
     */
    public static LocationMode of(Value value) {
        switch (value) {
        case INSTANCE:
            return INSTANCE;
        case KIND:
            return KIND;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating LocationMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static LocationMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating LocationMode objects from a passed string value.
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
     * Inherited factory method for creating LocationMode objects from a passed string value.
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
        LocationMode other = (LocationMode) obj;
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
         *     An enum constant for LocationMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public LocationMode build() {
            LocationMode locationMode = new LocationMode(this);
            if (validating) {
                validate(locationMode);
            }
            return locationMode;
        }

        protected void validate(LocationMode locationMode) {
            super.validate(locationMode);
        }

        protected Builder from(LocationMode locationMode) {
            super.from(locationMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Instance
         * 
         * <p>The Location resource represents a specific instance of a location (e.g. Operating Theatre 1A).
         */
        INSTANCE("instance"),

        /**
         * Kind
         * 
         * <p>The Location represents a class of locations (e.g. Any Operating Theatre) although this class of locations could be 
         * constrained within a specific boundary (such as organization, or parent location, address etc.).
         */
        KIND("kind");

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
         * Factory method for creating LocationMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding LocationMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "instance":
                return INSTANCE;
            case "kind":
                return KIND;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
