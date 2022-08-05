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

@System("http://hl7.org/fhir/vision-eye-codes")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class VisionEyes extends Code {
    /**
     * Right Eye
     * 
     * <p>Right Eye.
     */
    public static final VisionEyes RIGHT = VisionEyes.builder().value(Value.RIGHT).build();

    /**
     * Left Eye
     * 
     * <p>Left Eye.
     */
    public static final VisionEyes LEFT = VisionEyes.builder().value(Value.LEFT).build();

    private volatile int hashCode;

    private VisionEyes(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this VisionEyes as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating VisionEyes objects from a passed enum value.
     */
    public static VisionEyes of(Value value) {
        switch (value) {
        case RIGHT:
            return RIGHT;
        case LEFT:
            return LEFT;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating VisionEyes objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static VisionEyes of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating VisionEyes objects from a passed string value.
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
     * Inherited factory method for creating VisionEyes objects from a passed string value.
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
        VisionEyes other = (VisionEyes) obj;
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
         *     An enum constant for VisionEyes
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public VisionEyes build() {
            VisionEyes visionEyes = new VisionEyes(this);
            if (validating) {
                validate(visionEyes);
            }
            return visionEyes;
        }

        protected void validate(VisionEyes visionEyes) {
            super.validate(visionEyes);
        }

        protected Builder from(VisionEyes visionEyes) {
            super.from(visionEyes);
            return this;
        }
    }

    public enum Value {
        /**
         * Right Eye
         * 
         * <p>Right Eye.
         */
        RIGHT("right"),

        /**
         * Left Eye
         * 
         * <p>Left Eye.
         */
        LEFT("left");

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
         * Factory method for creating VisionEyes.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding VisionEyes.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "right":
                return RIGHT;
            case "left":
                return LEFT;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
