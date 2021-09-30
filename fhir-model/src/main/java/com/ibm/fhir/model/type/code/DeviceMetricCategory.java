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

@System("http://hl7.org/fhir/metric-category")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricCategory extends Code {
    /**
     * Measurement
     * 
     * <p>DeviceObservations generated for this DeviceMetric are measured.
     */
    public static final DeviceMetricCategory MEASUREMENT = DeviceMetricCategory.builder().value(Value.MEASUREMENT).build();

    /**
     * Setting
     * 
     * <p>DeviceObservations generated for this DeviceMetric is a setting that will influence the behavior of the Device.
     */
    public static final DeviceMetricCategory SETTING = DeviceMetricCategory.builder().value(Value.SETTING).build();

    /**
     * Calculation
     * 
     * <p>DeviceObservations generated for this DeviceMetric are calculated.
     */
    public static final DeviceMetricCategory CALCULATION = DeviceMetricCategory.builder().value(Value.CALCULATION).build();

    /**
     * Unspecified
     * 
     * <p>The category of this DeviceMetric is unspecified.
     */
    public static final DeviceMetricCategory UNSPECIFIED = DeviceMetricCategory.builder().value(Value.UNSPECIFIED).build();

    private volatile int hashCode;

    private DeviceMetricCategory(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DeviceMetricCategory as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricCategory objects from a passed enum value.
     */
    public static DeviceMetricCategory of(Value value) {
        switch (value) {
        case MEASUREMENT:
            return MEASUREMENT;
        case SETTING:
            return SETTING;
        case CALCULATION:
            return CALCULATION;
        case UNSPECIFIED:
            return UNSPECIFIED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceMetricCategory objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceMetricCategory of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DeviceMetricCategory objects from a passed string value.
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
     * Inherited factory method for creating DeviceMetricCategory objects from a passed string value.
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
        DeviceMetricCategory other = (DeviceMetricCategory) obj;
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
         *     An enum constant for DeviceMetricCategory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DeviceMetricCategory build() {
            DeviceMetricCategory deviceMetricCategory = new DeviceMetricCategory(this);
            if (validating) {
                validate(deviceMetricCategory);
            }
            return deviceMetricCategory;
        }

        protected void validate(DeviceMetricCategory deviceMetricCategory) {
            super.validate(deviceMetricCategory);
        }

        protected Builder from(DeviceMetricCategory deviceMetricCategory) {
            super.from(deviceMetricCategory);
            return this;
        }
    }

    public enum Value {
        /**
         * Measurement
         * 
         * <p>DeviceObservations generated for this DeviceMetric are measured.
         */
        MEASUREMENT("measurement"),

        /**
         * Setting
         * 
         * <p>DeviceObservations generated for this DeviceMetric is a setting that will influence the behavior of the Device.
         */
        SETTING("setting"),

        /**
         * Calculation
         * 
         * <p>DeviceObservations generated for this DeviceMetric are calculated.
         */
        CALCULATION("calculation"),

        /**
         * Unspecified
         * 
         * <p>The category of this DeviceMetric is unspecified.
         */
        UNSPECIFIED("unspecified");

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
         * Factory method for creating DeviceMetricCategory.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DeviceMetricCategory.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "measurement":
                return MEASUREMENT;
            case "setting":
                return SETTING;
            case "calculation":
                return CALCULATION;
            case "unspecified":
                return UNSPECIFIED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
