/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://hl7.org/fhir/location-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class LocationStatus extends Code {
    /**
     * Active
     * 
     * <p>The location is operational.
     */
    public static final LocationStatus ACTIVE = LocationStatus.builder().value(Value.ACTIVE).build();

    /**
     * Suspended
     * 
     * <p>The location is temporarily closed.
     */
    public static final LocationStatus SUSPENDED = LocationStatus.builder().value(Value.SUSPENDED).build();

    /**
     * Inactive
     * 
     * <p>The location is no longer used.
     */
    public static final LocationStatus INACTIVE = LocationStatus.builder().value(Value.INACTIVE).build();

    private volatile int hashCode;

    private LocationStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this LocationStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating LocationStatus objects from a passed enum value.
     */
    public static LocationStatus of(Value value) {
        switch (value) {
        case ACTIVE:
            return ACTIVE;
        case SUSPENDED:
            return SUSPENDED;
        case INACTIVE:
            return INACTIVE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating LocationStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static LocationStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating LocationStatus objects from a passed string value.
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
     * Inherited factory method for creating LocationStatus objects from a passed string value.
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
        LocationStatus other = (LocationStatus) obj;
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
         *     An enum constant for LocationStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public LocationStatus build() {
            LocationStatus locationStatus = new LocationStatus(this);
            if (validating) {
                validate(locationStatus);
            }
            return locationStatus;
        }

        protected void validate(LocationStatus locationStatus) {
            super.validate(locationStatus);
        }

        protected Builder from(LocationStatus locationStatus) {
            super.from(locationStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Active
         * 
         * <p>The location is operational.
         */
        ACTIVE("active"),

        /**
         * Suspended
         * 
         * <p>The location is temporarily closed.
         */
        SUSPENDED("suspended"),

        /**
         * Inactive
         * 
         * <p>The location is no longer used.
         */
        INACTIVE("inactive");

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
         * Factory method for creating LocationStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding LocationStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "active":
                return ACTIVE;
            case "suspended":
                return SUSPENDED;
            case "inactive":
                return INACTIVE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
