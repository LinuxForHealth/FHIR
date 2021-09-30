/*
 * (C) Copyright IBM Corp. 2019, 2021
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

@System("http://terminology.hl7.org/CodeSystem/data-absent-reason")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DataAbsentReason extends Code {
    /**
     * Unknown
     * 
     * <p>The value is expected to exist but is not known.
     */
    public static final DataAbsentReason UNKNOWN = DataAbsentReason.builder().value(Value.UNKNOWN).build();

    /**
     * Asked But Unknown
     * 
     * <p>The source was asked but does not know the value.
     */
    public static final DataAbsentReason ASKED_UNKNOWN = DataAbsentReason.builder().value(Value.ASKED_UNKNOWN).build();

    /**
     * Temporarily Unknown
     * 
     * <p>There is reason to expect (from the workflow) that the value may become known.
     */
    public static final DataAbsentReason TEMP_UNKNOWN = DataAbsentReason.builder().value(Value.TEMP_UNKNOWN).build();

    /**
     * Not Asked
     * 
     * <p>The workflow didn't lead to this value being known.
     */
    public static final DataAbsentReason NOT_ASKED = DataAbsentReason.builder().value(Value.NOT_ASKED).build();

    /**
     * Asked But Declined
     * 
     * <p>The source was asked but declined to answer.
     */
    public static final DataAbsentReason ASKED_DECLINED = DataAbsentReason.builder().value(Value.ASKED_DECLINED).build();

    /**
     * Masked
     * 
     * <p>The information is not available due to security, privacy or related reasons.
     */
    public static final DataAbsentReason MASKED = DataAbsentReason.builder().value(Value.MASKED).build();

    /**
     * Not Applicable
     * 
     * <p>There is no proper value for this element (e.g. last menstrual period for a male).
     */
    public static final DataAbsentReason NOT_APPLICABLE = DataAbsentReason.builder().value(Value.NOT_APPLICABLE).build();

    /**
     * Unsupported
     * 
     * <p>The source system wasn't capable of supporting this element.
     */
    public static final DataAbsentReason UNSUPPORTED = DataAbsentReason.builder().value(Value.UNSUPPORTED).build();

    /**
     * As Text
     * 
     * <p>The content of the data is represented in the resource narrative.
     */
    public static final DataAbsentReason AS_TEXT = DataAbsentReason.builder().value(Value.AS_TEXT).build();

    /**
     * Error
     * 
     * <p>Some system or workflow process error means that the information is not available.
     */
    public static final DataAbsentReason ERROR = DataAbsentReason.builder().value(Value.ERROR).build();

    /**
     * Not a Number (NaN)
     * 
     * <p>The numeric value is undefined or unrepresentable due to a floating point processing error.
     */
    public static final DataAbsentReason NOT_A_NUMBER = DataAbsentReason.builder().value(Value.NOT_A_NUMBER).build();

    /**
     * Negative Infinity (NINF)
     * 
     * <p>The numeric value is excessively low and unrepresentable due to a floating point processing error.
     */
    public static final DataAbsentReason NEGATIVE_INFINITY = DataAbsentReason.builder().value(Value.NEGATIVE_INFINITY).build();

    /**
     * Positive Infinity (PINF)
     * 
     * <p>The numeric value is excessively high and unrepresentable due to a floating point processing error.
     */
    public static final DataAbsentReason POSITIVE_INFINITY = DataAbsentReason.builder().value(Value.POSITIVE_INFINITY).build();

    /**
     * Not Performed
     * 
     * <p>The value is not available because the observation procedure (test, etc.) was not performed.
     */
    public static final DataAbsentReason NOT_PERFORMED = DataAbsentReason.builder().value(Value.NOT_PERFORMED).build();

    /**
     * Not Permitted
     * 
     * <p>The value is not permitted in this context (e.g. due to profiles, or the base data types).
     */
    public static final DataAbsentReason NOT_PERMITTED = DataAbsentReason.builder().value(Value.NOT_PERMITTED).build();

    private volatile int hashCode;

    private DataAbsentReason(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DataAbsentReason as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DataAbsentReason objects from a passed enum value.
     */
    public static DataAbsentReason of(Value value) {
        switch (value) {
        case UNKNOWN:
            return UNKNOWN;
        case ASKED_UNKNOWN:
            return ASKED_UNKNOWN;
        case TEMP_UNKNOWN:
            return TEMP_UNKNOWN;
        case NOT_ASKED:
            return NOT_ASKED;
        case ASKED_DECLINED:
            return ASKED_DECLINED;
        case MASKED:
            return MASKED;
        case NOT_APPLICABLE:
            return NOT_APPLICABLE;
        case UNSUPPORTED:
            return UNSUPPORTED;
        case AS_TEXT:
            return AS_TEXT;
        case ERROR:
            return ERROR;
        case NOT_A_NUMBER:
            return NOT_A_NUMBER;
        case NEGATIVE_INFINITY:
            return NEGATIVE_INFINITY;
        case POSITIVE_INFINITY:
            return POSITIVE_INFINITY;
        case NOT_PERFORMED:
            return NOT_PERFORMED;
        case NOT_PERMITTED:
            return NOT_PERMITTED;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DataAbsentReason objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DataAbsentReason of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DataAbsentReason objects from a passed string value.
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
     * Inherited factory method for creating DataAbsentReason objects from a passed string value.
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
        DataAbsentReason other = (DataAbsentReason) obj;
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
         *     An enum constant for DataAbsentReason
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DataAbsentReason build() {
            DataAbsentReason dataAbsentReason = new DataAbsentReason(this);
            if (validating) {
                validate(dataAbsentReason);
            }
            return dataAbsentReason;
        }

        protected void validate(DataAbsentReason dataAbsentReason) {
            super.validate(dataAbsentReason);
        }

        protected Builder from(DataAbsentReason dataAbsentReason) {
            super.from(dataAbsentReason);
            return this;
        }
    }

    public enum Value {
        /**
         * Unknown
         * 
         * <p>The value is expected to exist but is not known.
         */
        UNKNOWN("unknown"),

        /**
         * Asked But Unknown
         * 
         * <p>The source was asked but does not know the value.
         */
        ASKED_UNKNOWN("asked-unknown"),

        /**
         * Temporarily Unknown
         * 
         * <p>There is reason to expect (from the workflow) that the value may become known.
         */
        TEMP_UNKNOWN("temp-unknown"),

        /**
         * Not Asked
         * 
         * <p>The workflow didn't lead to this value being known.
         */
        NOT_ASKED("not-asked"),

        /**
         * Asked But Declined
         * 
         * <p>The source was asked but declined to answer.
         */
        ASKED_DECLINED("asked-declined"),

        /**
         * Masked
         * 
         * <p>The information is not available due to security, privacy or related reasons.
         */
        MASKED("masked"),

        /**
         * Not Applicable
         * 
         * <p>There is no proper value for this element (e.g. last menstrual period for a male).
         */
        NOT_APPLICABLE("not-applicable"),

        /**
         * Unsupported
         * 
         * <p>The source system wasn't capable of supporting this element.
         */
        UNSUPPORTED("unsupported"),

        /**
         * As Text
         * 
         * <p>The content of the data is represented in the resource narrative.
         */
        AS_TEXT("as-text"),

        /**
         * Error
         * 
         * <p>Some system or workflow process error means that the information is not available.
         */
        ERROR("error"),

        /**
         * Not a Number (NaN)
         * 
         * <p>The numeric value is undefined or unrepresentable due to a floating point processing error.
         */
        NOT_A_NUMBER("not-a-number"),

        /**
         * Negative Infinity (NINF)
         * 
         * <p>The numeric value is excessively low and unrepresentable due to a floating point processing error.
         */
        NEGATIVE_INFINITY("negative-infinity"),

        /**
         * Positive Infinity (PINF)
         * 
         * <p>The numeric value is excessively high and unrepresentable due to a floating point processing error.
         */
        POSITIVE_INFINITY("positive-infinity"),

        /**
         * Not Performed
         * 
         * <p>The value is not available because the observation procedure (test, etc.) was not performed.
         */
        NOT_PERFORMED("not-performed"),

        /**
         * Not Permitted
         * 
         * <p>The value is not permitted in this context (e.g. due to profiles, or the base data types).
         */
        NOT_PERMITTED("not-permitted");

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
         * Factory method for creating DataAbsentReason.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DataAbsentReason.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "unknown":
                return UNKNOWN;
            case "asked-unknown":
                return ASKED_UNKNOWN;
            case "temp-unknown":
                return TEMP_UNKNOWN;
            case "not-asked":
                return NOT_ASKED;
            case "asked-declined":
                return ASKED_DECLINED;
            case "masked":
                return MASKED;
            case "not-applicable":
                return NOT_APPLICABLE;
            case "unsupported":
                return UNSUPPORTED;
            case "as-text":
                return AS_TEXT;
            case "error":
                return ERROR;
            case "not-a-number":
                return NOT_A_NUMBER;
            case "negative-infinity":
                return NEGATIVE_INFINITY;
            case "positive-infinity":
                return POSITIVE_INFINITY;
            case "not-performed":
                return NOT_PERFORMED;
            case "not-permitted":
                return NOT_PERMITTED;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
