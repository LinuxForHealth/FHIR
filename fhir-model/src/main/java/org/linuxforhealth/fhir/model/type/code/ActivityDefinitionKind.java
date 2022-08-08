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

@System("http://hl7.org/fhir/request-resource-types")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ActivityDefinitionKind extends Code {
    /**
     * Appointment
     * 
     * <p>A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a 
     * specific date/time. This may result in one or more Encounter(s).
     */
    public static final ActivityDefinitionKind APPOINTMENT = ActivityDefinitionKind.builder().value(Value.APPOINTMENT).build();

    /**
     * AppointmentResponse
     * 
     * <p>A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
     */
    public static final ActivityDefinitionKind APPOINTMENT_RESPONSE = ActivityDefinitionKind.builder().value(Value.APPOINTMENT_RESPONSE).build();

    /**
     * CarePlan
     * 
     * <p>Healthcare plan for patient or group.
     */
    public static final ActivityDefinitionKind CARE_PLAN = ActivityDefinitionKind.builder().value(Value.CARE_PLAN).build();

    /**
     * Claim
     * 
     * <p>Claim, Pre-determination or Pre-authorization.
     */
    public static final ActivityDefinitionKind CLAIM = ActivityDefinitionKind.builder().value(Value.CLAIM).build();

    /**
     * CommunicationRequest
     * 
     * <p>A request for information to be sent to a receiver.
     */
    public static final ActivityDefinitionKind COMMUNICATION_REQUEST = ActivityDefinitionKind.builder().value(Value.COMMUNICATION_REQUEST).build();

    /**
     * Contract
     * 
     * <p>Legal Agreement.
     */
    public static final ActivityDefinitionKind CONTRACT = ActivityDefinitionKind.builder().value(Value.CONTRACT).build();

    /**
     * DeviceRequest
     * 
     * <p>Medical device request.
     */
    public static final ActivityDefinitionKind DEVICE_REQUEST = ActivityDefinitionKind.builder().value(Value.DEVICE_REQUEST).build();

    /**
     * EnrollmentRequest
     * 
     * <p>Enrollment request.
     */
    public static final ActivityDefinitionKind ENROLLMENT_REQUEST = ActivityDefinitionKind.builder().value(Value.ENROLLMENT_REQUEST).build();

    /**
     * ImmunizationRecommendation
     * 
     * <p>Guidance or advice relating to an immunization.
     */
    public static final ActivityDefinitionKind IMMUNIZATION_RECOMMENDATION = ActivityDefinitionKind.builder().value(Value.IMMUNIZATION_RECOMMENDATION).build();

    /**
     * MedicationRequest
     * 
     * <p>Ordering of medication for patient or group.
     */
    public static final ActivityDefinitionKind MEDICATION_REQUEST = ActivityDefinitionKind.builder().value(Value.MEDICATION_REQUEST).build();

    /**
     * NutritionOrder
     * 
     * <p>Diet, formula or nutritional supplement request.
     */
    public static final ActivityDefinitionKind NUTRITION_ORDER = ActivityDefinitionKind.builder().value(Value.NUTRITION_ORDER).build();

    /**
     * ServiceRequest
     * 
     * <p>A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
     */
    public static final ActivityDefinitionKind SERVICE_REQUEST = ActivityDefinitionKind.builder().value(Value.SERVICE_REQUEST).build();

    /**
     * SupplyRequest
     * 
     * <p>Request for a medication, substance or device.
     */
    public static final ActivityDefinitionKind SUPPLY_REQUEST = ActivityDefinitionKind.builder().value(Value.SUPPLY_REQUEST).build();

    /**
     * Task
     * 
     * <p>A task to be performed.
     */
    public static final ActivityDefinitionKind TASK = ActivityDefinitionKind.builder().value(Value.TASK).build();

    /**
     * VisionPrescription
     * 
     * <p>Prescription for vision correction products for a patient.
     */
    public static final ActivityDefinitionKind VISION_PRESCRIPTION = ActivityDefinitionKind.builder().value(Value.VISION_PRESCRIPTION).build();

    private volatile int hashCode;

    private ActivityDefinitionKind(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ActivityDefinitionKind as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ActivityDefinitionKind objects from a passed enum value.
     */
    public static ActivityDefinitionKind of(Value value) {
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

    /**
     * Factory method for creating ActivityDefinitionKind objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ActivityDefinitionKind of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ActivityDefinitionKind objects from a passed string value.
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
     * Inherited factory method for creating ActivityDefinitionKind objects from a passed string value.
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
         *     An enum constant for ActivityDefinitionKind
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ActivityDefinitionKind build() {
            ActivityDefinitionKind activityDefinitionKind = new ActivityDefinitionKind(this);
            if (validating) {
                validate(activityDefinitionKind);
            }
            return activityDefinitionKind;
        }

        protected void validate(ActivityDefinitionKind activityDefinitionKind) {
            super.validate(activityDefinitionKind);
        }

        protected Builder from(ActivityDefinitionKind activityDefinitionKind) {
            super.from(activityDefinitionKind);
            return this;
        }
    }

    public enum Value {
        /**
         * Appointment
         * 
         * <p>A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a 
         * specific date/time. This may result in one or more Encounter(s).
         */
        APPOINTMENT("Appointment"),

        /**
         * AppointmentResponse
         * 
         * <p>A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
         */
        APPOINTMENT_RESPONSE("AppointmentResponse"),

        /**
         * CarePlan
         * 
         * <p>Healthcare plan for patient or group.
         */
        CARE_PLAN("CarePlan"),

        /**
         * Claim
         * 
         * <p>Claim, Pre-determination or Pre-authorization.
         */
        CLAIM("Claim"),

        /**
         * CommunicationRequest
         * 
         * <p>A request for information to be sent to a receiver.
         */
        COMMUNICATION_REQUEST("CommunicationRequest"),

        /**
         * Contract
         * 
         * <p>Legal Agreement.
         */
        CONTRACT("Contract"),

        /**
         * DeviceRequest
         * 
         * <p>Medical device request.
         */
        DEVICE_REQUEST("DeviceRequest"),

        /**
         * EnrollmentRequest
         * 
         * <p>Enrollment request.
         */
        ENROLLMENT_REQUEST("EnrollmentRequest"),

        /**
         * ImmunizationRecommendation
         * 
         * <p>Guidance or advice relating to an immunization.
         */
        IMMUNIZATION_RECOMMENDATION("ImmunizationRecommendation"),

        /**
         * MedicationRequest
         * 
         * <p>Ordering of medication for patient or group.
         */
        MEDICATION_REQUEST("MedicationRequest"),

        /**
         * NutritionOrder
         * 
         * <p>Diet, formula or nutritional supplement request.
         */
        NUTRITION_ORDER("NutritionOrder"),

        /**
         * ServiceRequest
         * 
         * <p>A record of a request for service such as diagnostic investigations, treatments, or operations to be performed.
         */
        SERVICE_REQUEST("ServiceRequest"),

        /**
         * SupplyRequest
         * 
         * <p>Request for a medication, substance or device.
         */
        SUPPLY_REQUEST("SupplyRequest"),

        /**
         * Task
         * 
         * <p>A task to be performed.
         */
        TASK("Task"),

        /**
         * VisionPrescription
         * 
         * <p>Prescription for vision correction products for a patient.
         */
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
         * Factory method for creating ActivityDefinitionKind.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ActivityDefinitionKind.Value or null if a null value was passed
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
            case "AppointmentResponse":
                return APPOINTMENT_RESPONSE;
            case "CarePlan":
                return CARE_PLAN;
            case "Claim":
                return CLAIM;
            case "CommunicationRequest":
                return COMMUNICATION_REQUEST;
            case "Contract":
                return CONTRACT;
            case "DeviceRequest":
                return DEVICE_REQUEST;
            case "EnrollmentRequest":
                return ENROLLMENT_REQUEST;
            case "ImmunizationRecommendation":
                return IMMUNIZATION_RECOMMENDATION;
            case "MedicationRequest":
                return MEDICATION_REQUEST;
            case "NutritionOrder":
                return NUTRITION_ORDER;
            case "ServiceRequest":
                return SERVICE_REQUEST;
            case "SupplyRequest":
                return SUPPLY_REQUEST;
            case "Task":
                return TASK;
            case "VisionPrescription":
                return VISION_PRESCRIPTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
