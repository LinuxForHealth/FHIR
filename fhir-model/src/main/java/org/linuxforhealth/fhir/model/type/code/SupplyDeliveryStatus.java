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

@System("http://hl7.org/fhir/supplydelivery-status")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SupplyDeliveryStatus extends Code {
    /**
     * In Progress
     * 
     * <p>Supply has been requested, but not delivered.
     */
    public static final SupplyDeliveryStatus IN_PROGRESS = SupplyDeliveryStatus.builder().value(Value.IN_PROGRESS).build();

    /**
     * Delivered
     * 
     * <p>Supply has been delivered ("completed").
     */
    public static final SupplyDeliveryStatus COMPLETED = SupplyDeliveryStatus.builder().value(Value.COMPLETED).build();

    /**
     * Abandoned
     * 
     * <p>Delivery was not completed.
     */
    public static final SupplyDeliveryStatus ABANDONED = SupplyDeliveryStatus.builder().value(Value.ABANDONED).build();

    /**
     * Entered In Error
     * 
     * <p>This electronic record should never have existed, though it is possible that real-world decisions were based on it. 
     * (If real-world activity has occurred, the status should be "abandoned" rather than "entered-in-error".).
     */
    public static final SupplyDeliveryStatus ENTERED_IN_ERROR = SupplyDeliveryStatus.builder().value(Value.ENTERED_IN_ERROR).build();

    private volatile int hashCode;

    private SupplyDeliveryStatus(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SupplyDeliveryStatus as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SupplyDeliveryStatus objects from a passed enum value.
     */
    public static SupplyDeliveryStatus of(Value value) {
        switch (value) {
        case IN_PROGRESS:
            return IN_PROGRESS;
        case COMPLETED:
            return COMPLETED;
        case ABANDONED:
            return ABANDONED;
        case ENTERED_IN_ERROR:
            return ENTERED_IN_ERROR;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SupplyDeliveryStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SupplyDeliveryStatus of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SupplyDeliveryStatus objects from a passed string value.
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
     * Inherited factory method for creating SupplyDeliveryStatus objects from a passed string value.
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
        SupplyDeliveryStatus other = (SupplyDeliveryStatus) obj;
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
         *     An enum constant for SupplyDeliveryStatus
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SupplyDeliveryStatus build() {
            SupplyDeliveryStatus supplyDeliveryStatus = new SupplyDeliveryStatus(this);
            if (validating) {
                validate(supplyDeliveryStatus);
            }
            return supplyDeliveryStatus;
        }

        protected void validate(SupplyDeliveryStatus supplyDeliveryStatus) {
            super.validate(supplyDeliveryStatus);
        }

        protected Builder from(SupplyDeliveryStatus supplyDeliveryStatus) {
            super.from(supplyDeliveryStatus);
            return this;
        }
    }

    public enum Value {
        /**
         * In Progress
         * 
         * <p>Supply has been requested, but not delivered.
         */
        IN_PROGRESS("in-progress"),

        /**
         * Delivered
         * 
         * <p>Supply has been delivered ("completed").
         */
        COMPLETED("completed"),

        /**
         * Abandoned
         * 
         * <p>Delivery was not completed.
         */
        ABANDONED("abandoned"),

        /**
         * Entered In Error
         * 
         * <p>This electronic record should never have existed, though it is possible that real-world decisions were based on it. 
         * (If real-world activity has occurred, the status should be "abandoned" rather than "entered-in-error".).
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
         * Factory method for creating SupplyDeliveryStatus.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SupplyDeliveryStatus.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "in-progress":
                return IN_PROGRESS;
            case "completed":
                return COMPLETED;
            case "abandoned":
                return ABANDONED;
            case "entered-in-error":
                return ENTERED_IN_ERROR;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
