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
public class DeviceMetricOperationalStatus extends Code {
    /**
     * On
     */
    public static final DeviceMetricOperationalStatus ON = DeviceMetricOperationalStatus.builder().value(ValueSet.ON).build();

    /**
     * Off
     */
    public static final DeviceMetricOperationalStatus OFF = DeviceMetricOperationalStatus.builder().value(ValueSet.OFF).build();

    /**
     * Standby
     */
    public static final DeviceMetricOperationalStatus STANDBY = DeviceMetricOperationalStatus.builder().value(ValueSet.STANDBY).build();

    /**
     * Entered In Error
     */
    public static final DeviceMetricOperationalStatus ENTERED_IN_ERROR = DeviceMetricOperationalStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private DeviceMetricOperationalStatus(Builder builder) {
        super(builder);
    }

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
            throw new IllegalArgumentException(value.name());
        }
    }

    public static DeviceMetricOperationalStatus of(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.valueOf(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.valueOf(value));
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
         */
        ON("on"),

        /**
         * Off
         */
        OFF("off"),

        /**
         * Standby
         */
        STANDBY("standby"),

        /**
         * Entered In Error
         */
        ENTERED_IN_ERROR("entered-in-error");

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
