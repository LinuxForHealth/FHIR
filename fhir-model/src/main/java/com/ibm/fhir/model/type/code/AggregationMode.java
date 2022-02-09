/*
 * (C) Copyright IBM Corp. 2019, 2022
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

@System("http://hl7.org/fhir/resource-aggregation-mode")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AggregationMode extends Code {
    /**
     * Contained
     * 
     * <p>The reference is a local reference to a contained resource.
     */
    public static final AggregationMode CONTAINED = AggregationMode.builder().value(Value.CONTAINED).build();

    /**
     * Referenced
     * 
     * <p>The reference to a resource that has to be resolved externally to the resource that includes the reference.
     */
    public static final AggregationMode REFERENCED = AggregationMode.builder().value(Value.REFERENCED).build();

    /**
     * Bundled
     * 
     * <p>The resource the reference points to will be found in the same bundle as the resource that includes the reference.
     */
    public static final AggregationMode BUNDLED = AggregationMode.builder().value(Value.BUNDLED).build();

    private volatile int hashCode;

    private AggregationMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AggregationMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AggregationMode objects from a passed enum value.
     */
    public static AggregationMode of(Value value) {
        switch (value) {
        case CONTAINED:
            return CONTAINED;
        case REFERENCED:
            return REFERENCED;
        case BUNDLED:
            return BUNDLED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AggregationMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AggregationMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AggregationMode objects from a passed string value.
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
     * Inherited factory method for creating AggregationMode objects from a passed string value.
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
        AggregationMode other = (AggregationMode) obj;
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
         *     An enum constant for AggregationMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AggregationMode build() {
            AggregationMode aggregationMode = new AggregationMode(this);
            if (validating) {
                validate(aggregationMode);
            }
            return aggregationMode;
        }

        protected void validate(AggregationMode aggregationMode) {
            super.validate(aggregationMode);
        }

        protected Builder from(AggregationMode aggregationMode) {
            super.from(aggregationMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Contained
         * 
         * <p>The reference is a local reference to a contained resource.
         */
        CONTAINED("contained"),

        /**
         * Referenced
         * 
         * <p>The reference to a resource that has to be resolved externally to the resource that includes the reference.
         */
        REFERENCED("referenced"),

        /**
         * Bundled
         * 
         * <p>The resource the reference points to will be found in the same bundle as the resource that includes the reference.
         */
        BUNDLED("bundled");

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
         * Factory method for creating AggregationMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AggregationMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "contained":
                return CONTAINED;
            case "referenced":
                return REFERENCED;
            case "bundled":
                return BUNDLED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
