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

@System("http://hl7.org/fhir/address-use")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AddressUse extends Code {
    /**
     * Home
     * 
     * <p>A communication address at a home.
     */
    public static final AddressUse HOME = AddressUse.builder().value(Value.HOME).build();

    /**
     * Work
     * 
     * <p>An office address. First choice for business related contacts during business hours.
     */
    public static final AddressUse WORK = AddressUse.builder().value(Value.WORK).build();

    /**
     * Temporary
     * 
     * <p>A temporary address. The period can provide more detailed information.
     */
    public static final AddressUse TEMP = AddressUse.builder().value(Value.TEMP).build();

    /**
     * Old / Incorrect
     * 
     * <p>This address is no longer in use (or was never correct but retained for records).
     */
    public static final AddressUse OLD = AddressUse.builder().value(Value.OLD).build();

    /**
     * Billing
     * 
     * <p>An address to be used to send bills, invoices, receipts etc.
     */
    public static final AddressUse BILLING = AddressUse.builder().value(Value.BILLING).build();

    private volatile int hashCode;

    private AddressUse(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AddressUse as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AddressUse objects from a passed enum value.
     */
    public static AddressUse of(Value value) {
        switch (value) {
        case HOME:
            return HOME;
        case WORK:
            return WORK;
        case TEMP:
            return TEMP;
        case OLD:
            return OLD;
        case BILLING:
            return BILLING;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AddressUse objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AddressUse of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AddressUse objects from a passed string value.
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
     * Inherited factory method for creating AddressUse objects from a passed string value.
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
        AddressUse other = (AddressUse) obj;
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
         *     An enum constant for AddressUse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AddressUse build() {
            AddressUse addressUse = new AddressUse(this);
            if (validating) {
                validate(addressUse);
            }
            return addressUse;
        }

        protected void validate(AddressUse addressUse) {
            super.validate(addressUse);
        }

        protected Builder from(AddressUse addressUse) {
            super.from(addressUse);
            return this;
        }
    }

    public enum Value {
        /**
         * Home
         * 
         * <p>A communication address at a home.
         */
        HOME("home"),

        /**
         * Work
         * 
         * <p>An office address. First choice for business related contacts during business hours.
         */
        WORK("work"),

        /**
         * Temporary
         * 
         * <p>A temporary address. The period can provide more detailed information.
         */
        TEMP("temp"),

        /**
         * Old / Incorrect
         * 
         * <p>This address is no longer in use (or was never correct but retained for records).
         */
        OLD("old"),

        /**
         * Billing
         * 
         * <p>An address to be used to send bills, invoices, receipts etc.
         */
        BILLING("billing");

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
         * Factory method for creating AddressUse.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AddressUse.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "home":
                return HOME;
            case "work":
                return WORK;
            case "temp":
                return TEMP;
            case "old":
                return OLD;
            case "billing":
                return BILLING;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
