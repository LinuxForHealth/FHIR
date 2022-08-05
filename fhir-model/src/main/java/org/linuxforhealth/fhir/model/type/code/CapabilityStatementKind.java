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

@System("http://hl7.org/fhir/capability-statement-kind")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class CapabilityStatementKind extends Code {
    /**
     * Instance
     * 
     * <p>The CapabilityStatement instance represents the present capabilities of a specific system instance. This is the 
     * kind returned by /metadata for a FHIR server end-point.
     */
    public static final CapabilityStatementKind INSTANCE = CapabilityStatementKind.builder().value(Value.INSTANCE).build();

    /**
     * Capability
     * 
     * <p>The CapabilityStatement instance represents the capabilities of a system or piece of software, independent of a 
     * particular installation.
     */
    public static final CapabilityStatementKind CAPABILITY = CapabilityStatementKind.builder().value(Value.CAPABILITY).build();

    /**
     * Requirements
     * 
     * <p>The CapabilityStatement instance represents a set of requirements for other systems to meet; e.g. as part of an 
     * implementation guide or 'request for proposal'.
     */
    public static final CapabilityStatementKind REQUIREMENTS = CapabilityStatementKind.builder().value(Value.REQUIREMENTS).build();

    private volatile int hashCode;

    private CapabilityStatementKind(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CapabilityStatementKind as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CapabilityStatementKind objects from a passed enum value.
     */
    public static CapabilityStatementKind of(Value value) {
        switch (value) {
        case INSTANCE:
            return INSTANCE;
        case CAPABILITY:
            return CAPABILITY;
        case REQUIREMENTS:
            return REQUIREMENTS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CapabilityStatementKind objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CapabilityStatementKind of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CapabilityStatementKind objects from a passed string value.
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
     * Inherited factory method for creating CapabilityStatementKind objects from a passed string value.
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
        CapabilityStatementKind other = (CapabilityStatementKind) obj;
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
         *     An enum constant for CapabilityStatementKind
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CapabilityStatementKind build() {
            CapabilityStatementKind capabilityStatementKind = new CapabilityStatementKind(this);
            if (validating) {
                validate(capabilityStatementKind);
            }
            return capabilityStatementKind;
        }

        protected void validate(CapabilityStatementKind capabilityStatementKind) {
            super.validate(capabilityStatementKind);
        }

        protected Builder from(CapabilityStatementKind capabilityStatementKind) {
            super.from(capabilityStatementKind);
            return this;
        }
    }

    public enum Value {
        /**
         * Instance
         * 
         * <p>The CapabilityStatement instance represents the present capabilities of a specific system instance. This is the 
         * kind returned by /metadata for a FHIR server end-point.
         */
        INSTANCE("instance"),

        /**
         * Capability
         * 
         * <p>The CapabilityStatement instance represents the capabilities of a system or piece of software, independent of a 
         * particular installation.
         */
        CAPABILITY("capability"),

        /**
         * Requirements
         * 
         * <p>The CapabilityStatement instance represents a set of requirements for other systems to meet; e.g. as part of an 
         * implementation guide or 'request for proposal'.
         */
        REQUIREMENTS("requirements");

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
         * Factory method for creating CapabilityStatementKind.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CapabilityStatementKind.Value or null if a null value was passed
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
            case "capability":
                return CAPABILITY;
            case "requirements":
                return REQUIREMENTS;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
