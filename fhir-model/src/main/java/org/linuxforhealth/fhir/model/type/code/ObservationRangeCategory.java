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

@System("http://hl7.org/fhir/observation-range-category")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ObservationRangeCategory extends Code {
    /**
     * reference range
     * 
     * <p>Reference (Normal) Range for Ordinal and Continuous Observations.
     */
    public static final ObservationRangeCategory REFERENCE = ObservationRangeCategory.builder().value(Value.REFERENCE).build();

    /**
     * critical range
     * 
     * <p>Critical Range for Ordinal and Continuous Observations.
     */
    public static final ObservationRangeCategory CRITICAL = ObservationRangeCategory.builder().value(Value.CRITICAL).build();

    /**
     * absolute range
     * 
     * <p>Absolute Range for Ordinal and Continuous Observations. Results outside this range are not possible.
     */
    public static final ObservationRangeCategory ABSOLUTE = ObservationRangeCategory.builder().value(Value.ABSOLUTE).build();

    private volatile int hashCode;

    private ObservationRangeCategory(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ObservationRangeCategory as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ObservationRangeCategory objects from a passed enum value.
     */
    public static ObservationRangeCategory of(Value value) {
        switch (value) {
        case REFERENCE:
            return REFERENCE;
        case CRITICAL:
            return CRITICAL;
        case ABSOLUTE:
            return ABSOLUTE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ObservationRangeCategory objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ObservationRangeCategory of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ObservationRangeCategory objects from a passed string value.
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
     * Inherited factory method for creating ObservationRangeCategory objects from a passed string value.
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
        ObservationRangeCategory other = (ObservationRangeCategory) obj;
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
         *     An enum constant for ObservationRangeCategory
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ObservationRangeCategory build() {
            ObservationRangeCategory observationRangeCategory = new ObservationRangeCategory(this);
            if (validating) {
                validate(observationRangeCategory);
            }
            return observationRangeCategory;
        }

        protected void validate(ObservationRangeCategory observationRangeCategory) {
            super.validate(observationRangeCategory);
        }

        protected Builder from(ObservationRangeCategory observationRangeCategory) {
            super.from(observationRangeCategory);
            return this;
        }
    }

    public enum Value {
        /**
         * reference range
         * 
         * <p>Reference (Normal) Range for Ordinal and Continuous Observations.
         */
        REFERENCE("reference"),

        /**
         * critical range
         * 
         * <p>Critical Range for Ordinal and Continuous Observations.
         */
        CRITICAL("critical"),

        /**
         * absolute range
         * 
         * <p>Absolute Range for Ordinal and Continuous Observations. Results outside this range are not possible.
         */
        ABSOLUTE("absolute");

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
         * Factory method for creating ObservationRangeCategory.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ObservationRangeCategory.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "reference":
                return REFERENCE;
            case "critical":
                return CRITICAL;
            case "absolute":
                return ABSOLUTE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
