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

@System("http://hl7.org/fhir/compartment-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class CompartmentCode extends Code {
    /**
     * Patient
     * 
     * <p>The compartment definition is for the patient compartment.
     */
    public static final CompartmentCode PATIENT = CompartmentCode.builder().value(Value.PATIENT).build();

    /**
     * Encounter
     * 
     * <p>The compartment definition is for the encounter compartment.
     */
    public static final CompartmentCode ENCOUNTER = CompartmentCode.builder().value(Value.ENCOUNTER).build();

    /**
     * RelatedPerson
     * 
     * <p>The compartment definition is for the related-person compartment.
     */
    public static final CompartmentCode RELATED_PERSON = CompartmentCode.builder().value(Value.RELATED_PERSON).build();

    /**
     * Practitioner
     * 
     * <p>The compartment definition is for the practitioner compartment.
     */
    public static final CompartmentCode PRACTITIONER = CompartmentCode.builder().value(Value.PRACTITIONER).build();

    /**
     * Device
     * 
     * <p>The compartment definition is for the device compartment.
     */
    public static final CompartmentCode DEVICE = CompartmentCode.builder().value(Value.DEVICE).build();

    private volatile int hashCode;

    private CompartmentCode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CompartmentCode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CompartmentCode objects from a passed enum value.
     */
    public static CompartmentCode of(Value value) {
        switch (value) {
        case PATIENT:
            return PATIENT;
        case ENCOUNTER:
            return ENCOUNTER;
        case RELATED_PERSON:
            return RELATED_PERSON;
        case PRACTITIONER:
            return PRACTITIONER;
        case DEVICE:
            return DEVICE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CompartmentCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CompartmentCode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CompartmentCode objects from a passed string value.
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
     * Inherited factory method for creating CompartmentCode objects from a passed string value.
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
        CompartmentCode other = (CompartmentCode) obj;
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
         *     An enum constant for CompartmentCode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CompartmentCode build() {
            CompartmentCode compartmentCode = new CompartmentCode(this);
            if (validating) {
                validate(compartmentCode);
            }
            return compartmentCode;
        }

        protected void validate(CompartmentCode compartmentCode) {
            super.validate(compartmentCode);
        }

        protected Builder from(CompartmentCode compartmentCode) {
            super.from(compartmentCode);
            return this;
        }
    }

    public enum Value {
        /**
         * Patient
         * 
         * <p>The compartment definition is for the patient compartment.
         */
        PATIENT("Patient"),

        /**
         * Encounter
         * 
         * <p>The compartment definition is for the encounter compartment.
         */
        ENCOUNTER("Encounter"),

        /**
         * RelatedPerson
         * 
         * <p>The compartment definition is for the related-person compartment.
         */
        RELATED_PERSON("RelatedPerson"),

        /**
         * Practitioner
         * 
         * <p>The compartment definition is for the practitioner compartment.
         */
        PRACTITIONER("Practitioner"),

        /**
         * Device
         * 
         * <p>The compartment definition is for the device compartment.
         */
        DEVICE("Device");

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
         * Factory method for creating CompartmentCode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CompartmentCode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "Patient":
                return PATIENT;
            case "Encounter":
                return ENCOUNTER;
            case "RelatedPerson":
                return RELATED_PERSON;
            case "Practitioner":
                return PRACTITIONER;
            case "Device":
                return DEVICE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
