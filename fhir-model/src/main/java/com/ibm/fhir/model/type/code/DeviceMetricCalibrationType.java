/*
 * (C) Copyright IBM Corp. 2019, 2022
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

@System("http://hl7.org/fhir/metric-calibration-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricCalibrationType extends Code {
    /**
     * Unspecified
     * 
     * <p>Metric calibration method has not been identified.
     */
    public static final DeviceMetricCalibrationType UNSPECIFIED = DeviceMetricCalibrationType.builder().value(Value.UNSPECIFIED).build();

    /**
     * Offset
     * 
     * <p>Offset metric calibration method.
     */
    public static final DeviceMetricCalibrationType OFFSET = DeviceMetricCalibrationType.builder().value(Value.OFFSET).build();

    /**
     * Gain
     * 
     * <p>Gain metric calibration method.
     */
    public static final DeviceMetricCalibrationType GAIN = DeviceMetricCalibrationType.builder().value(Value.GAIN).build();

    /**
     * Two Point
     * 
     * <p>Two-point metric calibration method.
     */
    public static final DeviceMetricCalibrationType TWO_POINT = DeviceMetricCalibrationType.builder().value(Value.TWO_POINT).build();

    private volatile int hashCode;

    private DeviceMetricCalibrationType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DeviceMetricCalibrationType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricCalibrationType objects from a passed enum value.
     */
    public static DeviceMetricCalibrationType of(Value value) {
        switch (value) {
        case UNSPECIFIED:
            return UNSPECIFIED;
        case OFFSET:
            return OFFSET;
        case GAIN:
            return GAIN;
        case TWO_POINT:
            return TWO_POINT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceMetricCalibrationType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceMetricCalibrationType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DeviceMetricCalibrationType objects from a passed string value.
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
     * Inherited factory method for creating DeviceMetricCalibrationType objects from a passed string value.
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
        DeviceMetricCalibrationType other = (DeviceMetricCalibrationType) obj;
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
         *     An enum constant for DeviceMetricCalibrationType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DeviceMetricCalibrationType build() {
            DeviceMetricCalibrationType deviceMetricCalibrationType = new DeviceMetricCalibrationType(this);
            if (validating) {
                validate(deviceMetricCalibrationType);
            }
            return deviceMetricCalibrationType;
        }

        protected void validate(DeviceMetricCalibrationType deviceMetricCalibrationType) {
            super.validate(deviceMetricCalibrationType);
        }

        protected Builder from(DeviceMetricCalibrationType deviceMetricCalibrationType) {
            super.from(deviceMetricCalibrationType);
            return this;
        }
    }

    public enum Value {
        /**
         * Unspecified
         * 
         * <p>Metric calibration method has not been identified.
         */
        UNSPECIFIED("unspecified"),

        /**
         * Offset
         * 
         * <p>Offset metric calibration method.
         */
        OFFSET("offset"),

        /**
         * Gain
         * 
         * <p>Gain metric calibration method.
         */
        GAIN("gain"),

        /**
         * Two Point
         * 
         * <p>Two-point metric calibration method.
         */
        TWO_POINT("two-point");

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
         * Factory method for creating DeviceMetricCalibrationType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DeviceMetricCalibrationType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "unspecified":
                return UNSPECIFIED;
            case "offset":
                return OFFSET;
            case "gain":
                return GAIN;
            case "two-point":
                return TWO_POINT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
