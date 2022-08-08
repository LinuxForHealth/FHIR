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

@System("http://hl7.org/fhir/request-priority")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ServiceRequestPriority extends Code {
    /**
     * Routine
     * 
     * <p>The request has normal priority.
     */
    public static final ServiceRequestPriority ROUTINE = ServiceRequestPriority.builder().value(Value.ROUTINE).build();

    /**
     * Urgent
     * 
     * <p>The request should be actioned promptly - higher priority than routine.
     */
    public static final ServiceRequestPriority URGENT = ServiceRequestPriority.builder().value(Value.URGENT).build();

    /**
     * ASAP
     * 
     * <p>The request should be actioned as soon as possible - higher priority than urgent.
     */
    public static final ServiceRequestPriority ASAP = ServiceRequestPriority.builder().value(Value.ASAP).build();

    /**
     * STAT
     * 
     * <p>The request should be actioned immediately - highest possible priority. E.g. an emergency.
     */
    public static final ServiceRequestPriority STAT = ServiceRequestPriority.builder().value(Value.STAT).build();

    private volatile int hashCode;

    private ServiceRequestPriority(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ServiceRequestPriority as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ServiceRequestPriority objects from a passed enum value.
     */
    public static ServiceRequestPriority of(Value value) {
        switch (value) {
        case ROUTINE:
            return ROUTINE;
        case URGENT:
            return URGENT;
        case ASAP:
            return ASAP;
        case STAT:
            return STAT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ServiceRequestPriority objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ServiceRequestPriority of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ServiceRequestPriority objects from a passed string value.
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
     * Inherited factory method for creating ServiceRequestPriority objects from a passed string value.
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
        ServiceRequestPriority other = (ServiceRequestPriority) obj;
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
         *     An enum constant for ServiceRequestPriority
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ServiceRequestPriority build() {
            ServiceRequestPriority serviceRequestPriority = new ServiceRequestPriority(this);
            if (validating) {
                validate(serviceRequestPriority);
            }
            return serviceRequestPriority;
        }

        protected void validate(ServiceRequestPriority serviceRequestPriority) {
            super.validate(serviceRequestPriority);
        }

        protected Builder from(ServiceRequestPriority serviceRequestPriority) {
            super.from(serviceRequestPriority);
            return this;
        }
    }

    public enum Value {
        /**
         * Routine
         * 
         * <p>The request has normal priority.
         */
        ROUTINE("routine"),

        /**
         * Urgent
         * 
         * <p>The request should be actioned promptly - higher priority than routine.
         */
        URGENT("urgent"),

        /**
         * ASAP
         * 
         * <p>The request should be actioned as soon as possible - higher priority than urgent.
         */
        ASAP("asap"),

        /**
         * STAT
         * 
         * <p>The request should be actioned immediately - highest possible priority. E.g. an emergency.
         */
        STAT("stat");

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
         * Factory method for creating ServiceRequestPriority.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ServiceRequestPriority.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "routine":
                return ROUTINE;
            case "urgent":
                return URGENT;
            case "asap":
                return ASAP;
            case "stat":
                return STAT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
