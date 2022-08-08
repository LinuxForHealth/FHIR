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

@System("http://hl7.org/fhir/metric-color")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class DeviceMetricColor extends Code {
    /**
     * Color Black
     * 
     * <p>Color for representation - black.
     */
    public static final DeviceMetricColor BLACK = DeviceMetricColor.builder().value(Value.BLACK).build();

    /**
     * Color Red
     * 
     * <p>Color for representation - red.
     */
    public static final DeviceMetricColor RED = DeviceMetricColor.builder().value(Value.RED).build();

    /**
     * Color Green
     * 
     * <p>Color for representation - green.
     */
    public static final DeviceMetricColor GREEN = DeviceMetricColor.builder().value(Value.GREEN).build();

    /**
     * Color Yellow
     * 
     * <p>Color for representation - yellow.
     */
    public static final DeviceMetricColor YELLOW = DeviceMetricColor.builder().value(Value.YELLOW).build();

    /**
     * Color Blue
     * 
     * <p>Color for representation - blue.
     */
    public static final DeviceMetricColor BLUE = DeviceMetricColor.builder().value(Value.BLUE).build();

    /**
     * Color Magenta
     * 
     * <p>Color for representation - magenta.
     */
    public static final DeviceMetricColor MAGENTA = DeviceMetricColor.builder().value(Value.MAGENTA).build();

    /**
     * Color Cyan
     * 
     * <p>Color for representation - cyan.
     */
    public static final DeviceMetricColor CYAN = DeviceMetricColor.builder().value(Value.CYAN).build();

    /**
     * Color White
     * 
     * <p>Color for representation - white.
     */
    public static final DeviceMetricColor WHITE = DeviceMetricColor.builder().value(Value.WHITE).build();

    private volatile int hashCode;

    private DeviceMetricColor(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DeviceMetricColor as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DeviceMetricColor objects from a passed enum value.
     */
    public static DeviceMetricColor of(Value value) {
        switch (value) {
        case BLACK:
            return BLACK;
        case RED:
            return RED;
        case GREEN:
            return GREEN;
        case YELLOW:
            return YELLOW;
        case BLUE:
            return BLUE;
        case MAGENTA:
            return MAGENTA;
        case CYAN:
            return CYAN;
        case WHITE:
            return WHITE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DeviceMetricColor objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DeviceMetricColor of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DeviceMetricColor objects from a passed string value.
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
     * Inherited factory method for creating DeviceMetricColor objects from a passed string value.
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
        DeviceMetricColor other = (DeviceMetricColor) obj;
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
         *     An enum constant for DeviceMetricColor
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DeviceMetricColor build() {
            DeviceMetricColor deviceMetricColor = new DeviceMetricColor(this);
            if (validating) {
                validate(deviceMetricColor);
            }
            return deviceMetricColor;
        }

        protected void validate(DeviceMetricColor deviceMetricColor) {
            super.validate(deviceMetricColor);
        }

        protected Builder from(DeviceMetricColor deviceMetricColor) {
            super.from(deviceMetricColor);
            return this;
        }
    }

    public enum Value {
        /**
         * Color Black
         * 
         * <p>Color for representation - black.
         */
        BLACK("black"),

        /**
         * Color Red
         * 
         * <p>Color for representation - red.
         */
        RED("red"),

        /**
         * Color Green
         * 
         * <p>Color for representation - green.
         */
        GREEN("green"),

        /**
         * Color Yellow
         * 
         * <p>Color for representation - yellow.
         */
        YELLOW("yellow"),

        /**
         * Color Blue
         * 
         * <p>Color for representation - blue.
         */
        BLUE("blue"),

        /**
         * Color Magenta
         * 
         * <p>Color for representation - magenta.
         */
        MAGENTA("magenta"),

        /**
         * Color Cyan
         * 
         * <p>Color for representation - cyan.
         */
        CYAN("cyan"),

        /**
         * Color White
         * 
         * <p>Color for representation - white.
         */
        WHITE("white");

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
         * Factory method for creating DeviceMetricColor.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DeviceMetricColor.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "black":
                return BLACK;
            case "red":
                return RED;
            case "green":
                return GREEN;
            case "yellow":
                return YELLOW;
            case "blue":
                return BLUE;
            case "magenta":
                return MAGENTA;
            case "cyan":
                return CYAN;
            case "white":
                return WHITE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
