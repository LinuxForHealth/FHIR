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

@System("http://hl7.org/fhir/resource-types")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CarePlanActivityKind extends Code {
    public static final CarePlanActivityKind APPOINTMENT = CarePlanActivityKind.builder().value(ValueSet.APPOINTMENT).build();

    public static final CarePlanActivityKind COMMUNICATION_REQUEST = CarePlanActivityKind.builder().value(ValueSet.COMMUNICATION_REQUEST).build();

    public static final CarePlanActivityKind DEVICE_REQUEST = CarePlanActivityKind.builder().value(ValueSet.DEVICE_REQUEST).build();

    public static final CarePlanActivityKind MEDICATION_REQUEST = CarePlanActivityKind.builder().value(ValueSet.MEDICATION_REQUEST).build();

    public static final CarePlanActivityKind NUTRITION_ORDER = CarePlanActivityKind.builder().value(ValueSet.NUTRITION_ORDER).build();

    public static final CarePlanActivityKind TASK = CarePlanActivityKind.builder().value(ValueSet.TASK).build();

    public static final CarePlanActivityKind SERVICE_REQUEST = CarePlanActivityKind.builder().value(ValueSet.SERVICE_REQUEST).build();

    public static final CarePlanActivityKind VISION_PRESCRIPTION = CarePlanActivityKind.builder().value(ValueSet.VISION_PRESCRIPTION).build();

    private volatile int hashCode;

    private CarePlanActivityKind(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating CarePlanActivityKind objects from a passed enum value.
     */
    public static CarePlanActivityKind of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public CarePlanActivityKind build() {
            return new CarePlanActivityKind(this);
        }
    }

    public enum ValueSet {
        APPOINTMENT("Appointment"),

        COMMUNICATION_REQUEST("CommunicationRequest"),

        DEVICE_REQUEST("DeviceRequest"),

        MEDICATION_REQUEST("MedicationRequest"),

        NUTRITION_ORDER("NutritionOrder"),

        TASK("Task"),

        SERVICE_REQUEST("ServiceRequest"),

        VISION_PRESCRIPTION("VisionPrescription");

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
         * Factory method for creating CarePlanActivityKind.ValueSet values from a passed string value.
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
