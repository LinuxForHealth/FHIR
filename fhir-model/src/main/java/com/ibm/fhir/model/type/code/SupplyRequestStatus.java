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

@System("http://hl7.org/fhir/supplyrequest-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SupplyRequestStatus extends Code {
    /**
     * Draft
     * 
     * <p>The request has been created but is not yet complete or ready for action.
     */
    public static final SupplyRequestStatus DRAFT = SupplyRequestStatus.builder().value(Value.DRAFT).build();

    /**
     * Active
     * 
     * <p>The request is ready to be acted upon.
     */
    public static final SupplyRequestStatus ACTIVE = SupplyRequestStatus.builder().value(Value.ACTIVE).build();

    /**
     * Suspended
     * 
     * <p>The authorization/request to act has been temporarily withdrawn but is expected to resume in the future.
     */
    public static final SupplyRequestStatus SUSPENDED = SupplyRequestStatus.builder().value(Value.SUSPENDED).build();

    /**
     * Cancelled
     * 
     * <p>The authorization/request to act has been terminated prior to the full completion of the intended actions. No 
     * further activity should occur.
     */
    public static final SupplyRequestStatus CANCELLED = SupplyRequestStatus.builder().value(Value.CANCELLED).build();

    /**
     * Completed
     * 
     * <p>Activity against the request has been sufficiently completed to the satisfaction of the requester.
     */
    public static final SupplyRequestStatus COMPLETED = SupplyRequestStatus.builder().value(Value.COMPLETED).build();

    /**
     * Entered in Error
     * 
     * <p>This electronic record should never have existed, though it is possible that real-world decisions were based on it. 
     * (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".).
     */
    public static final SupplyRequestStatus ENTERED_IN_ERROR = SupplyRequestStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The authoring/source system does not know which of the status values currently applies for this observation. Note: 
     * This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
     * system does not know which.
     */
    public static final SupplyRequestStatus UNKNOWN = SupplyRequestStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private SupplyRequestStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SupplyRequestStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SupplyRequestStatus objects from a passed enum value.
     */
    public static SupplyRequestStatus of(Value value) {
        switch (value) {
        case DRAFT:
            return DRAFT;
        case ACTIVE:
            return ACTIVE;
        case SUSPENDED:
            return SUSPENDED;
        case CANCELLED:
            return CANCELLED;
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
     * Factory method for creating SupplyRequestStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SupplyRequestStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SupplyRequestStatus objects from a passed string value.
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
     * Inherited factory method for creating SupplyRequestStatus objects from a passed string value.
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
        SupplyRequestStatus other = (SupplyRequestStatus) obj;
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
         *     An enum constant for SupplyRequestStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SupplyRequestStatus build() {
            SupplyRequestStatus supplyRequestStatus = new SupplyRequestStatus(this);
            if (validating) {
                validate(supplyRequestStatus);
            }
            return supplyRequestStatus;
        }

        protected void validate(SupplyRequestStatus supplyRequestStatus) {
            super.validate(supplyRequestStatus);
        }

        protected Builder from(SupplyRequestStatus supplyRequestStatus) {
            super.from(supplyRequestStatus);
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
         * <p>The request is ready to be acted upon.
         */
        ACTIVE("active"),

        /**
         * Suspended
         * 
         * <p>The authorization/request to act has been temporarily withdrawn but is expected to resume in the future.
         */
        SUSPENDED("suspended"),

        /**
         * Cancelled
         * 
         * <p>The authorization/request to act has been terminated prior to the full completion of the intended actions. No 
         * further activity should occur.
         */
        CANCELLED("cancelled"),

        /**
         * Completed
         * 
         * <p>Activity against the request has been sufficiently completed to the satisfaction of the requester.
         */
        COMPLETED("completed"),

        /**
         * Entered in Error
         * 
         * <p>This electronic record should never have existed, though it is possible that real-world decisions were based on it. 
         * (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".).
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The authoring/source system does not know which of the status values currently applies for this observation. Note: 
         * This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source 
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
         * Factory method for creating SupplyRequestStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SupplyRequestStatus.Value or null if a null value was passed
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
            case "suspended":
                return SUSPENDED;
            case "cancelled":
                return CANCELLED;
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
