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

@System("http://hl7.org/fhir/request-resource-types")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ActivityDefinitionKind extends Code {
    /**
     * Appointment
     */
    public static final ActivityDefinitionKind APPOINTMENT = ActivityDefinitionKind.builder().value(ValueSet.APPOINTMENT).build();

    /**
     * AppointmentResponse
     */
    public static final ActivityDefinitionKind APPOINTMENT_RESPONSE = ActivityDefinitionKind.builder().value(ValueSet.APPOINTMENT_RESPONSE).build();

    /**
     * CarePlan
     */
    public static final ActivityDefinitionKind CARE_PLAN = ActivityDefinitionKind.builder().value(ValueSet.CARE_PLAN).build();

    /**
     * Claim
     */
    public static final ActivityDefinitionKind CLAIM = ActivityDefinitionKind.builder().value(ValueSet.CLAIM).build();

    /**
     * CommunicationRequest
     */
    public static final ActivityDefinitionKind COMMUNICATION_REQUEST = ActivityDefinitionKind.builder().value(ValueSet.COMMUNICATION_REQUEST).build();

    /**
     * Contract
     */
    public static final ActivityDefinitionKind CONTRACT = ActivityDefinitionKind.builder().value(ValueSet.CONTRACT).build();

    /**
     * DeviceRequest
     */
    public static final ActivityDefinitionKind DEVICE_REQUEST = ActivityDefinitionKind.builder().value(ValueSet.DEVICE_REQUEST).build();

    /**
     * EnrollmentRequest
     */
    public static final ActivityDefinitionKind ENROLLMENT_REQUEST = ActivityDefinitionKind.builder().value(ValueSet.ENROLLMENT_REQUEST).build();

    /**
     * ImmunizationRecommendation
     */
    public static final ActivityDefinitionKind IMMUNIZATION_RECOMMENDATION = ActivityDefinitionKind.builder().value(ValueSet.IMMUNIZATION_RECOMMENDATION).build();

    /**
     * MedicationRequest
     */
    public static final ActivityDefinitionKind MEDICATION_REQUEST = ActivityDefinitionKind.builder().value(ValueSet.MEDICATION_REQUEST).build();

    /**
     * NutritionOrder
     */
    public static final ActivityDefinitionKind NUTRITION_ORDER = ActivityDefinitionKind.builder().value(ValueSet.NUTRITION_ORDER).build();

    /**
     * ServiceRequest
     */
    public static final ActivityDefinitionKind SERVICE_REQUEST = ActivityDefinitionKind.builder().value(ValueSet.SERVICE_REQUEST).build();

    /**
     * SupplyRequest
     */
    public static final ActivityDefinitionKind SUPPLY_REQUEST = ActivityDefinitionKind.builder().value(ValueSet.SUPPLY_REQUEST).build();

    /**
     * Task
     */
    public static final ActivityDefinitionKind TASK = ActivityDefinitionKind.builder().value(ValueSet.TASK).build();

    /**
     * VisionPrescription
     */
    public static final ActivityDefinitionKind VISION_PRESCRIPTION = ActivityDefinitionKind.builder().value(ValueSet.VISION_PRESCRIPTION).build();

    private volatile int hashCode;

    private ActivityDefinitionKind(Builder builder) {
        super(builder);
    }

    public static ActivityDefinitionKind of(ValueSet value) {
        switch (value) {
        case APPOINTMENT:
            return APPOINTMENT;
        case APPOINTMENT_RESPONSE:
            return APPOINTMENT_RESPONSE;
        case CARE_PLAN:
            return CARE_PLAN;
        case CLAIM:
            return CLAIM;
        case COMMUNICATION_REQUEST:
            return COMMUNICATION_REQUEST;
        case CONTRACT:
            return CONTRACT;
        case DEVICE_REQUEST:
            return DEVICE_REQUEST;
        case ENROLLMENT_REQUEST:
            return ENROLLMENT_REQUEST;
        case IMMUNIZATION_RECOMMENDATION:
            return IMMUNIZATION_RECOMMENDATION;
        case MEDICATION_REQUEST:
            return MEDICATION_REQUEST;
        case NUTRITION_ORDER:
            return NUTRITION_ORDER;
        case SERVICE_REQUEST:
            return SERVICE_REQUEST;
        case SUPPLY_REQUEST:
            return SUPPLY_REQUEST;
        case TASK:
            return TASK;
        case VISION_PRESCRIPTION:
            return VISION_PRESCRIPTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static ActivityDefinitionKind of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
