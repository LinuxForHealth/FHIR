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

@System("http://hl7.org/fhir/metric-calibration-state")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricCalibrationState extends Code {
    /**
     * Not Calibrated
     * 
     * <p>The metric has not been calibrated.
     */
    public static final DeviceMetricCalibrationState NOT_CALIBRATED = DeviceMetricCalibrationState.builder().value(ValueSet.NOT_CALIBRATED).build();

    /**
     * Calibration Required
     * 
     * <p>The metric needs to be calibrated.
     */
    public static final DeviceMetricCalibrationState CALIBRATION_REQUIRED = DeviceMetricCalibrationState.builder().value(ValueSet.CALIBRATION_REQUIRED).build();

    /**
     * Calibrated
     * 
     * <p>The metric has been calibrated.
     */
    public static final DeviceMetricCalibrationState CALIBRATED = DeviceMetricCalibrationState.builder().value(ValueSet.CALIBRATED).build();

    /**
     * Unspecified
     * 
     * <p>The state of calibration of this metric is unspecified.
     */
    public static final DeviceMetricCalibrationState UNSPECIFIED = DeviceMetricCalibrationState.builder().value(ValueSet.UNSPECIFIED).build();

    private volatile int hashCode;

    private DeviceMetricCalibrationState(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricCalibrationState objects from a passed enum value.
     */
    public static DeviceMetricCalibrationState of(ValueSet value) {
        switch (value) {
        case NOT_CALIBRATED:
            return NOT_CALIBRATED;
        case CALIBRATION_REQUIRED:
            return CALIBRATION_REQUIRED;
        case CALIBRATED:
            return CALIBRATED;
        case UNSPECIFIED:
            return UNSPECIFIED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceMetricCalibrationState objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceMetricCalibrationState of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating DeviceMetricCalibrationState objects from a passed string value.
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
     * Inherited factory method for creating DeviceMetricCalibrationState objects from a passed string value.
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
        DeviceMetricCalibrationState other = (DeviceMetricCalibrationState) obj;
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
        public DeviceMetricCalibrationState build() {
            return new DeviceMetricCalibrationState(this);
        }
    }

    public enum ValueSet {
        /**
         * Not Calibrated
         * 
         * <p>The metric has not been calibrated.
         */
        NOT_CALIBRATED("not-calibrated"),

        /**
         * Calibration Required
         * 
         * <p>The metric needs to be calibrated.
         */
        CALIBRATION_REQUIRED("calibration-required"),

        /**
         * Calibrated
         * 
         * <p>The metric has been calibrated.
         */
        CALIBRATED("calibrated"),

        /**
         * Unspecified
         * 
         * <p>The state of calibration of this metric is unspecified.
         */
        UNSPECIFIED("unspecified");

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
         * Factory method for creating DeviceMetricCalibrationState.ValueSet values from a passed string value.
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
