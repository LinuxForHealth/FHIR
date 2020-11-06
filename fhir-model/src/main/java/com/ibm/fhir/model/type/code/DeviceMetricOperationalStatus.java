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

@System("http://hl7.org/fhir/metric-operational-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricOperationalStatus extends Code {
    /**
     * On
     * 
     * <p>The DeviceMetric is operating and will generate DeviceObservations.
     */
    public static final DeviceMetricOperationalStatus ON = DeviceMetricOperationalStatus.builder().value(ValueSet.ON).build();

    /**
     * Off
     * 
     * <p>The DeviceMetric is not operating.
     */
    public static final DeviceMetricOperationalStatus OFF = DeviceMetricOperationalStatus.builder().value(ValueSet.OFF).build();

    /**
     * Standby
     * 
     * <p>The DeviceMetric is operating, but will not generate any DeviceObservations.
     */
    public static final DeviceMetricOperationalStatus STANDBY = DeviceMetricOperationalStatus.builder().value(ValueSet.STANDBY).build();

    /**
     * Entered In Error
     * 
     * <p>The DeviceMetric was entered in error.
     */
    public static final DeviceMetricOperationalStatus ENTERED_IN_ERROR = DeviceMetricOperationalStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private DeviceMetricOperationalStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricOperationalStatus objects from a passed enum value.
     */
    public static DeviceMetricOperationalStatus of(ValueSet value) {
        switch (value) {
        case ON:
            return ON;
        case OFF:
            return OFF;
        case STANDBY:
            return STANDBY;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceMetricOperationalStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceMetricOperationalStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating DeviceMetricOperationalStatus objects from a passed string value.
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
     * Inherited factory method for creating DeviceMetricOperationalStatus objects from a passed string value.
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
        DeviceMetricOperationalStatus other = (DeviceMetricOperationalStatus) obj;
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
        public DeviceMetricOperationalStatus build() {
            return new DeviceMetricOperationalStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * On
         * 
         * <p>The DeviceMetric is operating and will generate DeviceObservations.
         */
        ON("on"),

        /**
         * Off
         * 
         * <p>The DeviceMetric is not operating.
         */
        OFF("off"),

        /**
         * Standby
         * 
         * <p>The DeviceMetric is operating, but will not generate any DeviceObservations.
         */
        STANDBY("standby"),

        /**
         * Entered In Error
         * 
         * <p>The DeviceMetric was entered in error.
         */
        ENTERED_IN_ERROR("entered-in-error");

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
         * Factory method for creating DeviceMetricOperationalStatus.ValueSet values from a passed string value.
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
