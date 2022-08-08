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

@System("http://hl7.org/fhir/device-nametype")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class DeviceNameType extends Code {
    /**
     * UDI Label name
     * 
     * <p>UDI Label name.
     */
    public static final DeviceNameType UDI_LABEL_NAME = DeviceNameType.builder().value(Value.UDI_LABEL_NAME).build();

    /**
     * User Friendly name
     * 
     * <p>User Friendly name.
     */
    public static final DeviceNameType USER_FRIENDLY_NAME = DeviceNameType.builder().value(Value.USER_FRIENDLY_NAME).build();

    /**
     * Patient Reported name
     * 
     * <p>Patient Reported name.
     */
    public static final DeviceNameType PATIENT_REPORTED_NAME = DeviceNameType.builder().value(Value.PATIENT_REPORTED_NAME).build();

    /**
     * Manufacturer name
     * 
     * <p>Manufacturer name.
     */
    public static final DeviceNameType MANUFACTURER_NAME = DeviceNameType.builder().value(Value.MANUFACTURER_NAME).build();

    /**
     * Model name
     * 
     * <p>Model name.
     */
    public static final DeviceNameType MODEL_NAME = DeviceNameType.builder().value(Value.MODEL_NAME).build();

    /**
     * other
     * 
     * <p>other.
     */
    public static final DeviceNameType OTHER = DeviceNameType.builder().value(Value.OTHER).build();

    private volatile int hashCode;

    private DeviceNameType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DeviceNameType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DeviceNameType objects from a passed enum value.
     */
    public static DeviceNameType of(Value value) {
        switch (value) {
        case UDI_LABEL_NAME:
            return UDI_LABEL_NAME;
        case USER_FRIENDLY_NAME:
            return USER_FRIENDLY_NAME;
        case PATIENT_REPORTED_NAME:
            return PATIENT_REPORTED_NAME;
        case MANUFACTURER_NAME:
            return MANUFACTURER_NAME;
        case MODEL_NAME:
            return MODEL_NAME;
        case OTHER:
            return OTHER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceNameType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceNameType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DeviceNameType objects from a passed string value.
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
     * Inherited factory method for creating DeviceNameType objects from a passed string value.
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
        DeviceNameType other = (DeviceNameType) obj;
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
         *     An enum constant for DeviceNameType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DeviceNameType build() {
            DeviceNameType deviceNameType = new DeviceNameType(this);
            if (validating) {
                validate(deviceNameType);
            }
            return deviceNameType;
        }

        protected void validate(DeviceNameType deviceNameType) {
            super.validate(deviceNameType);
        }

        protected Builder from(DeviceNameType deviceNameType) {
            super.from(deviceNameType);
            return this;
        }
    }

    public enum Value {
        /**
         * UDI Label name
         * 
         * <p>UDI Label name.
         */
        UDI_LABEL_NAME("udi-label-name"),

        /**
         * User Friendly name
         * 
         * <p>User Friendly name.
         */
        USER_FRIENDLY_NAME("user-friendly-name"),

        /**
         * Patient Reported name
         * 
         * <p>Patient Reported name.
         */
        PATIENT_REPORTED_NAME("patient-reported-name"),

        /**
         * Manufacturer name
         * 
         * <p>Manufacturer name.
         */
        MANUFACTURER_NAME("manufacturer-name"),

        /**
         * Model name
         * 
         * <p>Model name.
         */
        MODEL_NAME("model-name"),

        /**
         * other
         * 
         * <p>other.
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
         * Factory method for creating DeviceNameType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DeviceNameType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "udi-label-name":
                return UDI_LABEL_NAME;
            case "user-friendly-name":
                return USER_FRIENDLY_NAME;
            case "patient-reported-name":
                return PATIENT_REPORTED_NAME;
            case "manufacturer-name":
                return MANUFACTURER_NAME;
            case "model-name":
                return MODEL_NAME;
            case "other":
                return OTHER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
