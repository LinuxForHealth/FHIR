/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActivityDefinitionKind extends Code {
    /**
     * Appointment
     */
    public static final ActivityDefinitionKind APPOINTMENT = ActivityDefinitionKind.of(ValueSet.APPOINTMENT);

    /**
     * AppointmentResponse
     */
    public static final ActivityDefinitionKind APPOINTMENT_RESPONSE = ActivityDefinitionKind.of(ValueSet.APPOINTMENT_RESPONSE);

    /**
     * CarePlan
     */
    public static final ActivityDefinitionKind CARE_PLAN = ActivityDefinitionKind.of(ValueSet.CARE_PLAN);

    /**
     * Claim
     */
    public static final ActivityDefinitionKind CLAIM = ActivityDefinitionKind.of(ValueSet.CLAIM);

    /**
     * CommunicationRequest
     */
    public static final ActivityDefinitionKind COMMUNICATION_REQUEST = ActivityDefinitionKind.of(ValueSet.COMMUNICATION_REQUEST);

    /**
     * Contract
     */
    public static final ActivityDefinitionKind CONTRACT = ActivityDefinitionKind.of(ValueSet.CONTRACT);

    /**
     * DeviceRequest
     */
    public static final ActivityDefinitionKind DEVICE_REQUEST = ActivityDefinitionKind.of(ValueSet.DEVICE_REQUEST);

    /**
     * EnrollmentRequest
     */
    public static final ActivityDefinitionKind ENROLLMENT_REQUEST = ActivityDefinitionKind.of(ValueSet.ENROLLMENT_REQUEST);

    /**
     * ImmunizationRecommendation
     */
    public static final ActivityDefinitionKind IMMUNIZATION_RECOMMENDATION = ActivityDefinitionKind.of(ValueSet.IMMUNIZATION_RECOMMENDATION);

    /**
     * MedicationRequest
     */
    public static final ActivityDefinitionKind MEDICATION_REQUEST = ActivityDefinitionKind.of(ValueSet.MEDICATION_REQUEST);

    /**
     * NutritionOrder
     */
    public static final ActivityDefinitionKind NUTRITION_ORDER = ActivityDefinitionKind.of(ValueSet.NUTRITION_ORDER);

    /**
     * ServiceRequest
     */
    public static final ActivityDefinitionKind SERVICE_REQUEST = ActivityDefinitionKind.of(ValueSet.SERVICE_REQUEST);

    /**
     * SupplyRequest
     */
    public static final ActivityDefinitionKind SUPPLY_REQUEST = ActivityDefinitionKind.of(ValueSet.SUPPLY_REQUEST);

    /**
     * Task
     */
    public static final ActivityDefinitionKind TASK = ActivityDefinitionKind.of(ValueSet.TASK);

    /**
     * VisionPrescription
     */
    public static final ActivityDefinitionKind VISION_PRESCRIPTION = ActivityDefinitionKind.of(ValueSet.VISION_PRESCRIPTION);

    private volatile int hashCode;

    private ActivityDefinitionKind(Builder builder) {
        super(builder);
    }

    public static ActivityDefinitionKind of(java.lang.String value) {
        return ActivityDefinitionKind.builder().value(value).build();
    }

    public static ActivityDefinitionKind of(ValueSet value) {
        return ActivityDefinitionKind.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return ActivityDefinitionKind.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return ActivityDefinitionKind.builder().value(value).build();
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
        ActivityDefinitionKind other = (ActivityDefinitionKind) obj;
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
        public ActivityDefinitionKind build() {
            return new ActivityDefinitionKind(this);
        }
    }

    public enum ValueSet {
        /**
         * Appointment
         */
        APPOINTMENT("Appointment"),

        /**
         * AppointmentResponse
         */
        APPOINTMENT_RESPONSE("AppointmentResponse"),

        /**
         * CarePlan
         */
        CARE_PLAN("CarePlan"),

        /**
         * Claim
         */
        CLAIM("Claim"),

        /**
         * CommunicationRequest
         */
        COMMUNICATION_REQUEST("CommunicationRequest"),

        /**
         * Contract
         */
        CONTRACT("Contract"),

        /**
         * DeviceRequest
         */
        DEVICE_REQUEST("DeviceRequest"),

        /**
         * EnrollmentRequest
         */
        ENROLLMENT_REQUEST("EnrollmentRequest"),

        /**
         * ImmunizationRecommendation
         */
        IMMUNIZATION_RECOMMENDATION("ImmunizationRecommendation"),

        /**
         * MedicationRequest
         */
        MEDICATION_REQUEST("MedicationRequest"),

        /**
         * NutritionOrder
         */
        NUTRITION_ORDER("NutritionOrder"),

        /**
         * ServiceRequest
         */
        SERVICE_REQUEST("ServiceRequest"),

        /**
         * SupplyRequest
         */
        SUPPLY_REQUEST("SupplyRequest"),

        /**
         * Task
         */
        TASK("Task"),

        /**
         * VisionPrescription
         */
        VISION_PRESCRIPTION("VisionPrescription");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

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
