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

@System("http://hl7.org/fhir/permitted-data-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ObservationDataType extends Code {
    /**
     * Quantity
     * 
     * <p>A measured amount.
     */
    public static final ObservationDataType QUANTITY = ObservationDataType.builder().value(Value.QUANTITY).build();

    /**
     * CodeableConcept
     * 
     * <p>A coded concept from a reference terminology and/or text.
     */
    public static final ObservationDataType CODEABLE_CONCEPT = ObservationDataType.builder().value(Value.CODEABLE_CONCEPT).build();

    /**
     * string
     * 
     * <p>A sequence of Unicode characters.
     */
    public static final ObservationDataType STRING = ObservationDataType.builder().value(Value.STRING).build();

    /**
     * boolean
     * 
     * <p>true or false.
     */
    public static final ObservationDataType BOOLEAN = ObservationDataType.builder().value(Value.BOOLEAN).build();

    /**
     * integer
     * 
     * <p>A signed integer.
     */
    public static final ObservationDataType INTEGER = ObservationDataType.builder().value(Value.INTEGER).build();

    /**
     * Range
     * 
     * <p>A set of values bounded by low and high.
     */
    public static final ObservationDataType RANGE = ObservationDataType.builder().value(Value.RANGE).build();

    /**
     * Ratio
     * 
     * <p>A ratio of two Quantity values - a numerator and a denominator.
     */
    public static final ObservationDataType RATIO = ObservationDataType.builder().value(Value.RATIO).build();

    /**
     * SampledData
     * 
     * <p>A series of measurements taken by a device.
     */
    public static final ObservationDataType SAMPLED_DATA = ObservationDataType.builder().value(Value.SAMPLED_DATA).build();

    /**
     * time
     * 
     * <p>A time during the day, in the format hh:mm:ss.
     */
    public static final ObservationDataType TIME = ObservationDataType.builder().value(Value.TIME).build();

    /**
     * dateTime
     * 
     * <p>A date, date-time or partial date (e.g. just year or year + month) as used in human communication.
     */
    public static final ObservationDataType DATE_TIME = ObservationDataType.builder().value(Value.DATE_TIME).build();

    /**
     * Period
     * 
     * <p>A time range defined by start and end date/time.
     */
    public static final ObservationDataType PERIOD = ObservationDataType.builder().value(Value.PERIOD).build();

    private volatile int hashCode;

    private ObservationDataType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ObservationDataType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ObservationDataType objects from a passed enum value.
     */
    public static ObservationDataType of(Value value) {
        switch (value) {
        case QUANTITY:
            return QUANTITY;
        case CODEABLE_CONCEPT:
            return CODEABLE_CONCEPT;
        case STRING:
            return STRING;
        case BOOLEAN:
            return BOOLEAN;
        case INTEGER:
            return INTEGER;
        case RANGE:
            return RANGE;
        case RATIO:
            return RATIO;
        case SAMPLED_DATA:
            return SAMPLED_DATA;
        case TIME:
            return TIME;
        case DATE_TIME:
            return DATE_TIME;
        case PERIOD:
            return PERIOD;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ObservationDataType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ObservationDataType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ObservationDataType objects from a passed string value.
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
     * Inherited factory method for creating ObservationDataType objects from a passed string value.
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
        ObservationDataType other = (ObservationDataType) obj;
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
         *     An enum constant for ObservationDataType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ObservationDataType build() {
            ObservationDataType observationDataType = new ObservationDataType(this);
            if (validating) {
                validate(observationDataType);
            }
            return observationDataType;
        }

        protected void validate(ObservationDataType observationDataType) {
            super.validate(observationDataType);
        }

        protected Builder from(ObservationDataType observationDataType) {
            super.from(observationDataType);
            return this;
        }
    }

    public enum Value {
        /**
         * Quantity
         * 
         * <p>A measured amount.
         */
        QUANTITY("Quantity"),

        /**
         * CodeableConcept
         * 
         * <p>A coded concept from a reference terminology and/or text.
         */
        CODEABLE_CONCEPT("CodeableConcept"),

        /**
         * string
         * 
         * <p>A sequence of Unicode characters.
         */
        STRING("string"),

        /**
         * boolean
         * 
         * <p>true or false.
         */
        BOOLEAN("boolean"),

        /**
         * integer
         * 
         * <p>A signed integer.
         */
        INTEGER("integer"),

        /**
         * Range
         * 
         * <p>A set of values bounded by low and high.
         */
        RANGE("Range"),

        /**
         * Ratio
         * 
         * <p>A ratio of two Quantity values - a numerator and a denominator.
         */
        RATIO("Ratio"),

        /**
         * SampledData
         * 
         * <p>A series of measurements taken by a device.
         */
        SAMPLED_DATA("SampledData"),

        /**
         * time
         * 
         * <p>A time during the day, in the format hh:mm:ss.
         */
        TIME("time"),

        /**
         * dateTime
         * 
         * <p>A date, date-time or partial date (e.g. just year or year + month) as used in human communication.
         */
        DATE_TIME("dateTime"),

        /**
         * Period
         * 
         * <p>A time range defined by start and end date/time.
         */
        PERIOD("Period");

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
         * Factory method for creating ObservationDataType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ObservationDataType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "Quantity":
                return QUANTITY;
            case "CodeableConcept":
                return CODEABLE_CONCEPT;
            case "string":
                return STRING;
            case "boolean":
                return BOOLEAN;
            case "integer":
                return INTEGER;
            case "Range":
                return RANGE;
            case "Ratio":
                return RATIO;
            case "SampledData":
                return SAMPLED_DATA;
            case "time":
                return TIME;
            case "dateTime":
                return DATE_TIME;
            case "Period":
                return PERIOD;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
