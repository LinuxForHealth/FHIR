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

@System("http://hl7.org/fhir/request-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CarePlanStatus extends Code {
    /**
     * Draft
     * 
     * <p>The request has been created but is not yet complete or ready for action.
     */
    public static final CarePlanStatus DRAFT = CarePlanStatus.builder().value(Value.DRAFT).build();

    /**
     * Active
     * 
     * <p>The request is in force and ready to be acted upon.
     */
    public static final CarePlanStatus ACTIVE = CarePlanStatus.builder().value(Value.ACTIVE).build();

    /**
     * On Hold
     * 
     * <p>The request (and any implicit authorization to act) has been temporarily withdrawn but is expected to resume in the 
     * future.
     */
    public static final CarePlanStatus ON_HOLD = CarePlanStatus.builder().value(Value.ON_HOLD).build();

    /**
     * Revoked
     * 
     * <p>The request (and any implicit authorization to act) has been terminated prior to the known full completion of the 
     * intended actions. No further activity should occur.
     */
    public static final CarePlanStatus REVOKED = CarePlanStatus.builder().value(Value.REVOKED).build();

    /**
     * Completed
     * 
     * <p>The activity described by the request has been fully performed. No further activity will occur.
     */
    public static final CarePlanStatus COMPLETED = CarePlanStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>This request should never have existed and should be considered 'void'. (It is possible that real-world decisions 
     * were based on it. If real-world activity has occurred, the status should be "revoked" rather than "entered-in-error".).
     */
    public static final CarePlanStatus ENTERED_IN_ERROR = CarePlanStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The authoring/source system does not know which of the status values currently applies for this request. Note: This 
     * concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
     * system does not know which.
     */
    public static final CarePlanStatus UNKNOWN = CarePlanStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private CarePlanStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CarePlanStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CarePlanStatus objects from a passed enum value.
     */
    public static CarePlanStatus of(Value value) {
        switch (value) {
        case DRAFT:
            return DRAFT;
        case ACTIVE:
            return ACTIVE;
        case ON_HOLD:
            return ON_HOLD;
        case REVOKED:
            return REVOKED;
        case COMPLETED:
            return COMPLETED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CarePlanStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CarePlanStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CarePlanStatus objects from a passed string value.
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
     * Inherited factory method for creating CarePlanStatus objects from a passed string value.
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
        CarePlanStatus other = (CarePlanStatus) obj;
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
         *     An enum constant for CarePlanStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CarePlanStatus build() {
            CarePlanStatus carePlanStatus = new CarePlanStatus(this);
            if (validating) {
                validate(carePlanStatus);
            }
            return carePlanStatus;
        }

        protected void validate(CarePlanStatus carePlanStatus) {
            super.validate(carePlanStatus);
        }

        protected Builder from(CarePlanStatus carePlanStatus) {
            super.from(carePlanStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Draft
         * 
         * <p>The request has been created but is not yet complete or ready for action.
         */
        DRAFT("draft"),

        /**
         * Active
         * 
         * <p>The request is in force and ready to be acted upon.
         */
        ACTIVE("active"),

        /**
         * On Hold
         * 
         * <p>The request (and any implicit authorization to act) has been temporarily withdrawn but is expected to resume in the 
         * future.
         */
        ON_HOLD("on-hold"),

        /**
         * Revoked
         * 
         * <p>The request (and any implicit authorization to act) has been terminated prior to the known full completion of the 
         * intended actions. No further activity should occur.
         */
        REVOKED("revoked"),

        /**
         * Completed
         * 
         * <p>The activity described by the request has been fully performed. No further activity will occur.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>This request should never have existed and should be considered 'void'. (It is possible that real-world decisions 
         * were based on it. If real-world activity has occurred, the status should be "revoked" rather than "entered-in-error".).
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The authoring/source system does not know which of the status values currently applies for this request. Note: This 
         * concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
         * system does not know which.
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
         * Factory method for creating CarePlanStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CarePlanStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "draft":
                return DRAFT;
            case "active":
                return ACTIVE;
            case "on-hold":
                return ON_HOLD;
            case "revoked":
                return REVOKED;
            case "completed":
                return COMPLETED;
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
