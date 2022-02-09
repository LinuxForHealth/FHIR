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

@System("http://hl7.org/fhir/imagingstudy-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ImagingStudyStatus extends Code {
    /**
     * Registered
     * 
     * <p>The existence of the imaging study is registered, but there is nothing yet available.
     */
    public static final ImagingStudyStatus REGISTERED = ImagingStudyStatus.builder().value(Value.REGISTERED).build();

    /**
     * Available
     * 
     * <p>At least one instance has been associated with this imaging study.
     */
    public static final ImagingStudyStatus AVAILABLE = ImagingStudyStatus.builder().value(Value.AVAILABLE).build();

    /**
     * Cancelled
     * 
     * <p>The imaging study is unavailable because the imaging study was not started or not completed (also sometimes called 
     * "aborted").
     */
    public static final ImagingStudyStatus CANCELLED = ImagingStudyStatus.builder().value(Value.CANCELLED).build();

    /**
     * Entered in Error
     * 
     * <p>The imaging study has been withdrawn following a previous final release. This electronic record should never have 
     * existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the 
     * status should be "cancelled" rather than "entered-in-error".).
     */
    public static final ImagingStudyStatus ENTERED_IN_ERROR = ImagingStudyStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The system does not know which of the status values currently applies for this request. Note: This concept is not 
     * to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
     */
    public static final ImagingStudyStatus UNKNOWN = ImagingStudyStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private ImagingStudyStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ImagingStudyStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ImagingStudyStatus objects from a passed enum value.
     */
    public static ImagingStudyStatus of(Value value) {
        switch (value) {
        case REGISTERED:
            return REGISTERED;
        case AVAILABLE:
            return AVAILABLE;
        case CANCELLED:
            return CANCELLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ImagingStudyStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ImagingStudyStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ImagingStudyStatus objects from a passed string value.
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
     * Inherited factory method for creating ImagingStudyStatus objects from a passed string value.
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
        ImagingStudyStatus other = (ImagingStudyStatus) obj;
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
         *     An enum constant for ImagingStudyStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ImagingStudyStatus build() {
            ImagingStudyStatus imagingStudyStatus = new ImagingStudyStatus(this);
            if (validating) {
                validate(imagingStudyStatus);
            }
            return imagingStudyStatus;
        }

        protected void validate(ImagingStudyStatus imagingStudyStatus) {
            super.validate(imagingStudyStatus);
        }

        protected Builder from(ImagingStudyStatus imagingStudyStatus) {
            super.from(imagingStudyStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Registered
         * 
         * <p>The existence of the imaging study is registered, but there is nothing yet available.
         */
        REGISTERED("registered"),

        /**
         * Available
         * 
         * <p>At least one instance has been associated with this imaging study.
         */
        AVAILABLE("available"),

        /**
         * Cancelled
         * 
         * <p>The imaging study is unavailable because the imaging study was not started or not completed (also sometimes called 
         * "aborted").
         */
        CANCELLED("cancelled"),

        /**
         * Entered in Error
         * 
         * <p>The imaging study has been withdrawn following a previous final release. This electronic record should never have 
         * existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the 
         * status should be "cancelled" rather than "entered-in-error".).
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The system does not know which of the status values currently applies for this request. Note: This concept is not 
         * to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN("unknown");

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
         * Factory method for creating ImagingStudyStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ImagingStudyStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "registered":
                return REGISTERED;
            case "available":
                return AVAILABLE;
            case "cancelled":
                return CANCELLED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
