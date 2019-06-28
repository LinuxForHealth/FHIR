/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class DeviceMetricColor extends Code {
    /**
     * Color Black
     */
    public static final DeviceMetricColor BLACK = DeviceMetricColor.of(ValueSet.BLACK);

    /**
     * Color Red
     */
    public static final DeviceMetricColor RED = DeviceMetricColor.of(ValueSet.RED);

    /**
     * Color Green
     */
    public static final DeviceMetricColor GREEN = DeviceMetricColor.of(ValueSet.GREEN);

    /**
     * Color Yellow
     */
    public static final DeviceMetricColor YELLOW = DeviceMetricColor.of(ValueSet.YELLOW);

    /**
     * Color Blue
     */
    public static final DeviceMetricColor BLUE = DeviceMetricColor.of(ValueSet.BLUE);

    /**
     * Color Magenta
     */
    public static final DeviceMetricColor MAGENTA = DeviceMetricColor.of(ValueSet.MAGENTA);

    /**
     * Color Cyan
     */
    public static final DeviceMetricColor CYAN = DeviceMetricColor.of(ValueSet.CYAN);

    /**
     * Color White
     */
    public static final DeviceMetricColor WHITE = DeviceMetricColor.of(ValueSet.WHITE);

    private DeviceMetricColor(Builder builder) {
        super(builder);
    }

    public static DeviceMetricColor of(java.lang.String value) {
        return DeviceMetricColor.builder().value(value).build();
    }

    public static DeviceMetricColor of(ValueSet value) {
        return DeviceMetricColor.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return DeviceMetricColor.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return DeviceMetricColor.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
        public DeviceMetricColor build() {
            return new DeviceMetricColor(this);
        }
    }

    public enum ValueSet {
        /**
         * Color Black
         */
        BLACK("black"),

        /**
         * Color Red
         */
        RED("red"),

        /**
         * Color Green
         */
        GREEN("green"),

        /**
         * Color Yellow
         */
        YELLOW("yellow"),

        /**
         * Color Blue
         */
        BLUE("blue"),

        /**
         * Color Magenta
         */
        MAGENTA("magenta"),

        /**
         * Color Cyan
         */
        CYAN("cyan"),

        /**
         * Color White
         */
        WHITE("white");

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
