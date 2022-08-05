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

@System("http://hl7.org/fhir/vision-base-codes")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class VisionBase extends Code {
    /**
     * Up
     * 
     * <p>top.
     */
    public static final VisionBase UP = VisionBase.builder().value(Value.UP).build();

    /**
     * Down
     * 
     * <p>bottom.
     */
    public static final VisionBase DOWN = VisionBase.builder().value(Value.DOWN).build();

    /**
     * In
     * 
     * <p>inner edge.
     */
    public static final VisionBase IN = VisionBase.builder().value(Value.IN).build();

    /**
     * Out
     * 
     * <p>outer edge.
     */
    public static final VisionBase OUT = VisionBase.builder().value(Value.OUT).build();

    private volatile int hashCode;

    private VisionBase(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this VisionBase as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating VisionBase objects from a passed enum value.
     */
    public static VisionBase of(Value value) {
        switch (value) {
        case UP:
            return UP;
        case DOWN:
            return DOWN;
        case IN:
            return IN;
        case OUT:
            return OUT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating VisionBase objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static VisionBase of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating VisionBase objects from a passed string value.
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
     * Inherited factory method for creating VisionBase objects from a passed string value.
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
        VisionBase other = (VisionBase) obj;
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
         *     An enum constant for VisionBase
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public VisionBase build() {
            VisionBase visionBase = new VisionBase(this);
            if (validating) {
                validate(visionBase);
            }
            return visionBase;
        }

        protected void validate(VisionBase visionBase) {
            super.validate(visionBase);
        }

        protected Builder from(VisionBase visionBase) {
            super.from(visionBase);
            return this;
        }
    }

    public enum Value {
        /**
         * Up
         * 
         * <p>top.
         */
        UP("up"),

        /**
         * Down
         * 
         * <p>bottom.
         */
        DOWN("down"),

        /**
         * In
         * 
         * <p>inner edge.
         */
        IN("in"),

        /**
         * Out
         * 
         * <p>outer edge.
         */
        OUT("out");

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
         * Factory method for creating VisionBase.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding VisionBase.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "up":
                return UP;
            case "down":
                return DOWN;
            case "in":
                return IN;
            case "out":
                return OUT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
