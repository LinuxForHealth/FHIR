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

@System("http://hl7.org/fhir/guidance-response-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class GuidanceResponseStatus extends Code {
    /**
     * Success
     * 
     * <p>The request was processed successfully.
     */
    public static final GuidanceResponseStatus SUCCESS = GuidanceResponseStatus.builder().value(Value.SUCCESS).build();

    /**
     * Data Requested
     * 
     * <p>The request was processed successfully, but more data may result in a more complete evaluation.
     */
    public static final GuidanceResponseStatus DATA_REQUESTED = GuidanceResponseStatus.builder().value(Value.DATA_REQUESTED).build();

    /**
     * Data Required
     * 
     * <p>The request was processed, but more data is required to complete the evaluation.
     */
    public static final GuidanceResponseStatus DATA_REQUIRED = GuidanceResponseStatus.builder().value(Value.DATA_REQUIRED).build();

    /**
     * In Progress
     * 
     * <p>The request is currently being processed.
     */
    public static final GuidanceResponseStatus IN_PROGRESS = GuidanceResponseStatus.builder().value(Value.IN_PROGRESS).build();

    /**
     * Failure
     * 
     * <p>The request was not processed successfully.
     */
    public static final GuidanceResponseStatus FAILURE = GuidanceResponseStatus.builder().value(Value.FAILURE).build();

    /**
     * Entered In Error
     * 
     * <p>The response was entered in error.
     */
    public static final GuidanceResponseStatus ENTERED_IN_ERROR = GuidanceResponseStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private GuidanceResponseStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this GuidanceResponseStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating GuidanceResponseStatus objects from a passed enum value.
     */
    public static GuidanceResponseStatus of(Value value) {
        switch (value) {
        case SUCCESS:
            return SUCCESS;
        case DATA_REQUESTED:
            return DATA_REQUESTED;
        case DATA_REQUIRED:
            return DATA_REQUIRED;
        case IN_PROGRESS:
            return IN_PROGRESS;
        case FAILURE:
            return FAILURE;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating GuidanceResponseStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static GuidanceResponseStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating GuidanceResponseStatus objects from a passed string value.
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
     * Inherited factory method for creating GuidanceResponseStatus objects from a passed string value.
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
        GuidanceResponseStatus other = (GuidanceResponseStatus) obj;
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
         *     An enum constant for GuidanceResponseStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public GuidanceResponseStatus build() {
            GuidanceResponseStatus guidanceResponseStatus = new GuidanceResponseStatus(this);
            if (validating) {
                validate(guidanceResponseStatus);
            }
            return guidanceResponseStatus;
        }

        protected void validate(GuidanceResponseStatus guidanceResponseStatus) {
            super.validate(guidanceResponseStatus);
        }

        protected Builder from(GuidanceResponseStatus guidanceResponseStatus) {
            super.from(guidanceResponseStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Success
         * 
         * <p>The request was processed successfully.
         */
        SUCCESS("success"),

        /**
         * Data Requested
         * 
         * <p>The request was processed successfully, but more data may result in a more complete evaluation.
         */
        DATA_REQUESTED("data-requested"),

        /**
         * Data Required
         * 
         * <p>The request was processed, but more data is required to complete the evaluation.
         */
        DATA_REQUIRED("data-required"),

        /**
         * In Progress
         * 
         * <p>The request is currently being processed.
         */
        IN_PROGRESS("in-progress"),

        /**
         * Failure
         * 
         * <p>The request was not processed successfully.
         */
        FAILURE("failure"),

        /**
         * Entered In Error
         * 
         * <p>The response was entered in error.
         */
        ENTERED_IN_ERROR("entered-in-error");

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
         * Factory method for creating GuidanceResponseStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding GuidanceResponseStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "success":
                return SUCCESS;
            case "data-requested":
                return DATA_REQUESTED;
            case "data-required":
                return DATA_REQUIRED;
            case "in-progress":
                return IN_PROGRESS;
            case "failure":
                return FAILURE;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
