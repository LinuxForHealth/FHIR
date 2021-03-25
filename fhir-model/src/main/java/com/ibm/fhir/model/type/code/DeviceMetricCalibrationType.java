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

@System("http://hl7.org/fhir/metric-calibration-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricCalibrationType extends Code {
    /**
     * Unspecified
     * 
     * <p>Metric calibration method has not been identified.
     */
    public static final DeviceMetricCalibrationType UNSPECIFIED = DeviceMetricCalibrationType.builder().value(ValueSet.UNSPECIFIED).build();

    /**
     * Offset
     * 
     * <p>Offset metric calibration method.
     */
    public static final DeviceMetricCalibrationType OFFSET = DeviceMetricCalibrationType.builder().value(ValueSet.OFFSET).build();

    /**
     * Gain
     * 
     * <p>Gain metric calibration method.
     */
    public static final DeviceMetricCalibrationType GAIN = DeviceMetricCalibrationType.builder().value(ValueSet.GAIN).build();

    /**
     * Two Point
     * 
     * <p>Two-point metric calibration method.
     */
    public static final DeviceMetricCalibrationType TWO_POINT = DeviceMetricCalibrationType.builder().value(ValueSet.TWO_POINT).build();

    private volatile int hashCode;

    private DeviceMetricCalibrationType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricCalibrationType objects from a passed enum value.
     */
    public static DeviceMetricCalibrationType of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public DeviceMetricCalibrationType build() {
            return new DeviceMetricCalibrationType(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating DeviceMetricCalibrationType.ValueSet values from a passed string value.
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
