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

@System("http://hl7.org/fhir/resource-types")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class CarePlanActivityKind extends Code {
    public static final CarePlanActivityKind APPOINTMENT = CarePlanActivityKind.builder().value(Value.APPOINTMENT).build();

    public static final CarePlanActivityKind COMMUNICATION_REQUEST = CarePlanActivityKind.builder().value(Value.COMMUNICATION_REQUEST).build();

    public static final CarePlanActivityKind DEVICE_REQUEST = CarePlanActivityKind.builder().value(Value.DEVICE_REQUEST).build();

    public static final CarePlanActivityKind MEDICATION_REQUEST = CarePlanActivityKind.builder().value(Value.MEDICATION_REQUEST).build();

    public static final CarePlanActivityKind NUTRITION_ORDER = CarePlanActivityKind.builder().value(Value.NUTRITION_ORDER).build();

    public static final CarePlanActivityKind TASK = CarePlanActivityKind.builder().value(Value.TASK).build();

    public static final CarePlanActivityKind SERVICE_REQUEST = CarePlanActivityKind.builder().value(Value.SERVICE_REQUEST).build();

    public static final CarePlanActivityKind VISION_PRESCRIPTION = CarePlanActivityKind.builder().value(Value.VISION_PRESCRIPTION).build();

    private volatile int hashCode;

    private CarePlanActivityKind(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CarePlanActivityKind as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CarePlanActivityKind objects from a passed enum value.
     */
    public static CarePlanActivityKind of(Value value) {
        switch (value) {
        case APPOINTMENT:
            return APPOINTMENT;
        case COMMUNICATION_REQUEST:
            return COMMUNICATION_REQUEST;
        case DEVICE_REQUEST:
            return DEVICE_REQUEST;
        case MEDICATION_REQUEST:
            return MEDICATION_REQUEST;
        case NUTRITION_ORDER:
            return NUTRITION_ORDER;
        case TASK:
            return TASK;
        case SERVICE_REQUEST:
            return SERVICE_REQUEST;
        case VISION_PRESCRIPTION:
            return VISION_PRESCRIPTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CarePlanActivityKind objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CarePlanActivityKind of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CarePlanActivityKind objects from a passed string value.
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
     * Inherited factory method for creating CarePlanActivityKind objects from a passed string value.
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
        CarePlanActivityKind other = (CarePlanActivityKind) obj;
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
         *     An enum constant for CarePlanActivityKind
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CarePlanActivityKind build() {
            CarePlanActivityKind carePlanActivityKind = new CarePlanActivityKind(this);
            if (validating) {
                validate(carePlanActivityKind);
            }
            return carePlanActivityKind;
        }

        protected void validate(CarePlanActivityKind carePlanActivityKind) {
            super.validate(carePlanActivityKind);
        }

        protected Builder from(CarePlanActivityKind carePlanActivityKind) {
            super.from(carePlanActivityKind);
            return this;
        }
    }

    public enum Value {
        APPOINTMENT("Appointment"),

        COMMUNICATION_REQUEST("CommunicationRequest"),

        DEVICE_REQUEST("DeviceRequest"),

        MEDICATION_REQUEST("MedicationRequest"),

        NUTRITION_ORDER("NutritionOrder"),

        TASK("Task"),

        SERVICE_REQUEST("ServiceRequest"),

        VISION_PRESCRIPTION("VisionPrescription");

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
         * Factory method for creating CarePlanActivityKind.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CarePlanActivityKind.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "Appointment":
                return APPOINTMENT;
            case "CommunicationRequest":
                return COMMUNICATION_REQUEST;
            case "DeviceRequest":
                return DEVICE_REQUEST;
            case "MedicationRequest":
                return MEDICATION_REQUEST;
            case "NutritionOrder":
                return NUTRITION_ORDER;
            case "Task":
                return TASK;
            case "ServiceRequest":
                return SERVICE_REQUEST;
            case "VisionPrescription":
                return VISION_PRESCRIPTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
