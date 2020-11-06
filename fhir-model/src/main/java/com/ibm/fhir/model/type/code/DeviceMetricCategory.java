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

@System("http://hl7.org/fhir/metric-category")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricCategory extends Code {
    /**
     * Measurement
     * 
     * <p>DeviceObservations generated for this DeviceMetric are measured.
     */
    public static final DeviceMetricCategory MEASUREMENT = DeviceMetricCategory.builder().value(ValueSet.MEASUREMENT).build();

    /**
     * Setting
     * 
     * <p>DeviceObservations generated for this DeviceMetric is a setting that will influence the behavior of the Device.
     */
    public static final DeviceMetricCategory SETTING = DeviceMetricCategory.builder().value(ValueSet.SETTING).build();

    /**
     * Calculation
     * 
     * <p>DeviceObservations generated for this DeviceMetric are calculated.
     */
    public static final DeviceMetricCategory CALCULATION = DeviceMetricCategory.builder().value(ValueSet.CALCULATION).build();

    /**
     * Unspecified
     * 
     * <p>The category of this DeviceMetric is unspecified.
     */
    public static final DeviceMetricCategory UNSPECIFIED = DeviceMetricCategory.builder().value(ValueSet.UNSPECIFIED).build();

    private volatile int hashCode;

    private DeviceMetricCategory(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricCategory objects from a passed enum value.
     */
    public static DeviceMetricCategory of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public DeviceMetricCategory build() {
            return new DeviceMetricCategory(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating DeviceMetricCategory.ValueSet values from a passed string value.
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
