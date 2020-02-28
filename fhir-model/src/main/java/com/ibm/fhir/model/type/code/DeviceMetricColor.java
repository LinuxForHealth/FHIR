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

@System("http://hl7.org/fhir/metric-color")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceMetricColor extends Code {
    /**
     * Color Black
     */
    public static final DeviceMetricColor BLACK = DeviceMetricColor.builder().value(ValueSet.BLACK).build();

    /**
     * Color Red
     */
    public static final DeviceMetricColor RED = DeviceMetricColor.builder().value(ValueSet.RED).build();

    /**
     * Color Green
     */
    public static final DeviceMetricColor GREEN = DeviceMetricColor.builder().value(ValueSet.GREEN).build();

    /**
     * Color Yellow
     */
    public static final DeviceMetricColor YELLOW = DeviceMetricColor.builder().value(ValueSet.YELLOW).build();

    /**
     * Color Blue
     */
    public static final DeviceMetricColor BLUE = DeviceMetricColor.builder().value(ValueSet.BLUE).build();

    /**
     * Color Magenta
     */
    public static final DeviceMetricColor MAGENTA = DeviceMetricColor.builder().value(ValueSet.MAGENTA).build();

    /**
     * Color Cyan
     */
    public static final DeviceMetricColor CYAN = DeviceMetricColor.builder().value(ValueSet.CYAN).build();

    /**
     * Color White
     */
    public static final DeviceMetricColor WHITE = DeviceMetricColor.builder().value(ValueSet.WHITE).build();

    private volatile int hashCode;

    private DeviceMetricColor(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    public static DeviceMetricColor of(ValueSet value) {
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

    public static DeviceMetricColor of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
