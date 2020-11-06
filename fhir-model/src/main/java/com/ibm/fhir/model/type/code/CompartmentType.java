/*
 * (C) Copyright IBM Corp. 2019, 2020
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

@System("http://hl7.org/fhir/compartment-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CompartmentType extends Code {
    /**
     * Patient
     * 
     * <p>The compartment definition is for the patient compartment.
     */
    public static final CompartmentType PATIENT = CompartmentType.builder().value(ValueSet.PATIENT).build();

    /**
     * Encounter
     * 
     * <p>The compartment definition is for the encounter compartment.
     */
    public static final CompartmentType ENCOUNTER = CompartmentType.builder().value(ValueSet.ENCOUNTER).build();

    /**
     * RelatedPerson
     * 
     * <p>The compartment definition is for the related-person compartment.
     */
    public static final CompartmentType RELATED_PERSON = CompartmentType.builder().value(ValueSet.RELATED_PERSON).build();

    /**
     * Practitioner
     * 
     * <p>The compartment definition is for the practitioner compartment.
     */
    public static final CompartmentType PRACTITIONER = CompartmentType.builder().value(ValueSet.PRACTITIONER).build();

    /**
     * Device
     * 
     * <p>The compartment definition is for the device compartment.
     */
    public static final CompartmentType DEVICE = CompartmentType.builder().value(ValueSet.DEVICE).build();

    private volatile int hashCode;

    private CompartmentType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating CompartmentType objects from a passed enum value.
     */
    public static CompartmentType of(ValueSet value) {
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
     * Factory method for creating CompartmentType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CompartmentType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating CompartmentType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating CompartmentType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        CompartmentType other = (CompartmentType) obj;
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
        public CompartmentType build() {
            return new CompartmentType(this);
        }
    }

    public enum ValueSet {
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

        ValueSet(java.lang.String value) {
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
         * Factory method for creating CompartmentType.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
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
