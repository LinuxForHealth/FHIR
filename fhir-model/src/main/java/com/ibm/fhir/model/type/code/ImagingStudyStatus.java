/*
 * (C) Copyright IBM Corp. 2019, 2020
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
    public static final ImagingStudyStatus REGISTERED = ImagingStudyStatus.builder().value(ValueSet.REGISTERED).build();

    /**
     * Available
     * 
     * <p>At least one instance has been associated with this imaging study.
     */
    public static final ImagingStudyStatus AVAILABLE = ImagingStudyStatus.builder().value(ValueSet.AVAILABLE).build();

    /**
     * Cancelled
     * 
     * <p>The imaging study is unavailable because the imaging study was not started or not completed (also sometimes called 
     * "aborted").
     */
    public static final ImagingStudyStatus CANCELLED = ImagingStudyStatus.builder().value(ValueSet.CANCELLED).build();

    /**
     * Entered in Error
     * 
     * <p>The imaging study has been withdrawn following a previous final release. This electronic record should never have 
     * existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the 
     * status should be "cancelled" rather than "entered-in-error".).
     */
    public static final ImagingStudyStatus ENTERED_IN_ERROR = ImagingStudyStatus.builder().value(ValueSet.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The system does not know which of the status values currently applies for this request. Note: This concept is not 
     * to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
     */
    public static final ImagingStudyStatus UNKNOWN = ImagingStudyStatus.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private ImagingStudyStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating ImagingStudyStatus objects from a passed enum value.
     */
    public static ImagingStudyStatus of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        Builder builder = new Builder();
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
        return builder;
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ImagingStudyStatus build() {
            return new ImagingStudyStatus(this);
        }
    }

    public enum ValueSet {
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

        ValueSet(java.lang.String value) {
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
         * Factory method for creating ImagingStudyStatus.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
