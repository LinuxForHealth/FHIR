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

@System("http://hl7.org/fhir/chargeitem-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ChargeItemStatus extends Code {
    /**
     * Planned
     * 
     * <p>The charge item has been entered, but the charged service is not yet complete, so it shall not be billed yet but 
     * might be used in the context of pre-authorization.
     */
    public static final ChargeItemStatus PLANNED = ChargeItemStatus.builder().value(Value.PLANNED).build();

    /**
     * Billable
     * 
     * <p>The charge item is ready for billing.
     */
    public static final ChargeItemStatus BILLABLE = ChargeItemStatus.builder().value(Value.BILLABLE).build();

    /**
     * Not billable
     * 
     * <p>The charge item has been determined to be not billable (e.g. due to rules associated with the billing code).
     */
    public static final ChargeItemStatus NOT_BILLABLE = ChargeItemStatus.builder().value(Value.NOT_BILLABLE).build();

    /**
     * Aborted
     * 
     * <p>The processing of the charge was aborted.
     */
    public static final ChargeItemStatus ABORTED = ChargeItemStatus.builder().value(Value.ABORTED).build();

    /**
     * Billed
     * 
     * <p>The charge item has been billed (e.g. a billing engine has generated financial transactions by applying the 
     * associated ruled for the charge item to the context of the Encounter, and placed them into Claims/Invoices.
     */
    public static final ChargeItemStatus BILLED = ChargeItemStatus.builder().value(Value.BILLED).build();

    /**
     * Entered in Error
     * 
     * <p>The charge item has been entered in error and should not be processed for billing.
     */
    public static final ChargeItemStatus ENTERED_IN_ERROR = ChargeItemStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    /**
     * Unknown
     * 
     * <p>The authoring system does not know which of the status values currently applies for this charge item Note: This 
     * concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
     */
    public static final ChargeItemStatus UNKNOWN = ChargeItemStatus.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private ChargeItemStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ChargeItemStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ChargeItemStatus objects from a passed enum value.
     */
    public static ChargeItemStatus of(Value value) {
        switch (value) {
        case PLANNED:
            return PLANNED;
        case BILLABLE:
            return BILLABLE;
        case NOT_BILLABLE:
            return NOT_BILLABLE;
        case ABORTED:
            return ABORTED;
        case BILLED:
            return BILLED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ChargeItemStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ChargeItemStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ChargeItemStatus objects from a passed string value.
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
     * Inherited factory method for creating ChargeItemStatus objects from a passed string value.
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
        ChargeItemStatus other = (ChargeItemStatus) obj;
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
         *     An enum constant for ChargeItemStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ChargeItemStatus build() {
            ChargeItemStatus chargeItemStatus = new ChargeItemStatus(this);
            if (validating) {
                validate(chargeItemStatus);
            }
            return chargeItemStatus;
        }

        protected void validate(ChargeItemStatus chargeItemStatus) {
            super.validate(chargeItemStatus);
        }

        protected Builder from(ChargeItemStatus chargeItemStatus) {
            super.from(chargeItemStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * Planned
         * 
         * <p>The charge item has been entered, but the charged service is not yet complete, so it shall not be billed yet but 
         * might be used in the context of pre-authorization.
         */
        PLANNED("planned"),

        /**
         * Billable
         * 
         * <p>The charge item is ready for billing.
         */
        BILLABLE("billable"),

        /**
         * Not billable
         * 
         * <p>The charge item has been determined to be not billable (e.g. due to rules associated with the billing code).
         */
        NOT_BILLABLE("not-billable"),

        /**
         * Aborted
         * 
         * <p>The processing of the charge was aborted.
         */
        ABORTED("aborted"),

        /**
         * Billed
         * 
         * <p>The charge item has been billed (e.g. a billing engine has generated financial transactions by applying the 
         * associated ruled for the charge item to the context of the Encounter, and placed them into Claims/Invoices.
         */
        BILLED("billed"),

        /**
         * Entered in Error
         * 
         * <p>The charge item has been entered in error and should not be processed for billing.
         */
        ENTERED_IN_ERROR("entered-in-error"),

        /**
         * Unknown
         * 
         * <p>The authoring system does not know which of the status values currently applies for this charge item Note: This 
         * concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
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
         * Factory method for creating ChargeItemStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ChargeItemStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "planned":
                return PLANNED;
            case "billable":
                return BILLABLE;
            case "not-billable":
                return NOT_BILLABLE;
            case "aborted":
                return ABORTED;
            case "billed":
                return BILLED;
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
