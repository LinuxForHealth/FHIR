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

@System("http://hl7.org/fhir/measure-report-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class MeasureReportStatus extends Code {
    /**
     * Complete
     * 
     * <p>The report is complete and ready for use.
     */
    public static final MeasureReportStatus COMPLETE = MeasureReportStatus.builder().value(Value.COMPLETE).build();

    /**
     * Pending
     * 
     * <p>The report is currently being generated.
     */
    public static final MeasureReportStatus PENDING = MeasureReportStatus.builder().value(Value.PENDING).build();

    /**
     * Error
     * 
     * <p>An error occurred attempting to generate the report.
     */
    public static final MeasureReportStatus ERROR = MeasureReportStatus.builder().value(Value.ERROR).build();

    private volatile int hashCode;

    private MeasureReportStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this MeasureReportStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating MeasureReportStatus objects from a passed enum value.
     */
    public static MeasureReportStatus of(Value value) {
        switch (value) {
        case COMPLETE:
            return COMPLETE;
        case PENDING:
            return PENDING;
        case ERROR:
            return ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating MeasureReportStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static MeasureReportStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating MeasureReportStatus objects from a passed string value.
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
     * Inherited factory method for creating MeasureReportStatus objects from a passed string value.
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
        MeasureReportStatus other = (MeasureReportStatus) obj;
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
         *     An enum constant for MeasureReportStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public MeasureReportStatus build() {
            MeasureReportStatus measureReportStatus = new MeasureReportStatus(this);
            if (validating) {
                validate(measureReportStatus);
            }
            return measureReportStatus;
        }

        protected void validate(MeasureReportStatus measureReportStatus) {
            super.validate(measureReportStatus);
        }

        protected Builder from(MeasureReportStatus measureReportStatus) {
            super.from(measureReportStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Complete
         * 
         * <p>The report is complete and ready for use.
         */
        COMPLETE("complete"),

        /**
         * Pending
         * 
         * <p>The report is currently being generated.
         */
        PENDING("pending"),

        /**
         * Error
         * 
         * <p>An error occurred attempting to generate the report.
         */
        ERROR("error");

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
         * Factory method for creating MeasureReportStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding MeasureReportStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "complete":
                return COMPLETE;
            case "pending":
                return PENDING;
            case "error":
                return ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
