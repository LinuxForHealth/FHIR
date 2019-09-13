/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class DeviceNameType extends Code {
    /**
     * UDI Label name
     */
    public static final DeviceNameType UDI_LABEL_NAME = DeviceNameType.of(ValueSet.UDI_LABEL_NAME);

    /**
     * User Friendly name
     */
    public static final DeviceNameType USER_FRIENDLY_NAME = DeviceNameType.of(ValueSet.USER_FRIENDLY_NAME);

    /**
     * Patient Reported name
     */
    public static final DeviceNameType PATIENT_REPORTED_NAME = DeviceNameType.of(ValueSet.PATIENT_REPORTED_NAME);

    /**
     * Manufacturer name
     */
    public static final DeviceNameType MANUFACTURER_NAME = DeviceNameType.of(ValueSet.MANUFACTURER_NAME);

    /**
     * Model name
     */
    public static final DeviceNameType MODEL_NAME = DeviceNameType.of(ValueSet.MODEL_NAME);

    /**
     * other
     */
    public static final DeviceNameType OTHER = DeviceNameType.of(ValueSet.OTHER);

    private volatile int hashCode;

    private DeviceNameType(Builder builder) {
        super(builder);
    }

    public static DeviceNameType of(java.lang.String value) {
        return DeviceNameType.builder().value(value).build();
    }

    public static DeviceNameType of(ValueSet value) {
        return DeviceNameType.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return DeviceNameType.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return DeviceNameType.builder().value(value).build();
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
        DeviceNameType other = (DeviceNameType) obj;
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
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public DeviceNameType build() {
            return new DeviceNameType(this);
        }
    }

    public enum ValueSet {
        /**
         * UDI Label name
         */
        UDI_LABEL_NAME("udi-label-name"),

        /**
         * User Friendly name
         */
        USER_FRIENDLY_NAME("user-friendly-name"),

        /**
         * Patient Reported name
         */
        PATIENT_REPORTED_NAME("patient-reported-name"),

        /**
         * Manufacturer name
         */
        MANUFACTURER_NAME("manufacturer-name"),

        /**
         * Model name
         */
        MODEL_NAME("model-name"),

        /**
         * other
         */
        OTHER("other");

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
