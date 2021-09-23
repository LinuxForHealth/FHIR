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

@System("http://hl7.org/fhir/measure-report-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MeasureReportType extends Code {
    /**
     * Individual
     * 
     * <p>An individual report that provides information on the performance for a given measure with respect to a single 
     * subject.
     */
    public static final MeasureReportType INDIVIDUAL = MeasureReportType.builder().value(Value.INDIVIDUAL).build();

    /**
     * Subject List
     * 
     * <p>A subject list report that includes a listing of subjects that satisfied each population criteria in the measure.
     */
    public static final MeasureReportType SUBJECT_LIST = MeasureReportType.builder().value(Value.SUBJECT_LIST).build();

    /**
     * Summary
     * 
     * <p>A summary report that returns the number of members in each population criteria for the measure.
     */
    public static final MeasureReportType SUMMARY = MeasureReportType.builder().value(Value.SUMMARY).build();

    /**
     * Data Collection
     * 
     * <p>A data collection report that contains data-of-interest for the measure.
     */
    public static final MeasureReportType DATA_COLLECTION = MeasureReportType.builder().value(Value.DATA_COLLECTION).build();

    private volatile int hashCode;

    private MeasureReportType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MeasureReportType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MeasureReportType objects from a passed enum value.
     */
    public static MeasureReportType of(Value value) {
        switch (value) {
        case INDIVIDUAL:
            return INDIVIDUAL;
        case SUBJECT_LIST:
            return SUBJECT_LIST;
        case SUMMARY:
            return SUMMARY;
        case DATA_COLLECTION:
            return DATA_COLLECTION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MeasureReportType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MeasureReportType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MeasureReportType objects from a passed string value.
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
     * Inherited factory method for creating MeasureReportType objects from a passed string value.
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
        MeasureReportType other = (MeasureReportType) obj;
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
         *     An enum constant for MeasureReportType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MeasureReportType build() {
            MeasureReportType measureReportType = new MeasureReportType(this);
            if (validating) {
                validate(measureReportType);
            }
            return measureReportType;
        }

        protected void validate(MeasureReportType measureReportType) {
            super.validate(measureReportType);
        }

        protected Builder from(MeasureReportType measureReportType) {
            super.from(measureReportType);
            return this;
        }
    }

    public enum Value {
        /**
         * Individual
         * 
         * <p>An individual report that provides information on the performance for a given measure with respect to a single 
         * subject.
         */
        INDIVIDUAL("individual"),

        /**
         * Subject List
         * 
         * <p>A subject list report that includes a listing of subjects that satisfied each population criteria in the measure.
         */
        SUBJECT_LIST("subject-list"),

        /**
         * Summary
         * 
         * <p>A summary report that returns the number of members in each population criteria for the measure.
         */
        SUMMARY("summary"),

        /**
         * Data Collection
         * 
         * <p>A data collection report that contains data-of-interest for the measure.
         */
        DATA_COLLECTION("data-collection");

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
         * Factory method for creating MeasureReportType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MeasureReportType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "individual":
                return INDIVIDUAL;
            case "subject-list":
                return SUBJECT_LIST;
            case "summary":
                return SUMMARY;
            case "data-collection":
                return DATA_COLLECTION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
