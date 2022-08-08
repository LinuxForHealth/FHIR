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

@System("http://hl7.org/fhir/address-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class AddressType extends Code {
    /**
     * Postal
     * 
     * <p>Mailing addresses - PO Boxes and care-of addresses.
     */
    public static final AddressType POSTAL = AddressType.builder().value(Value.POSTAL).build();

    /**
     * Physical
     * 
     * <p>A physical address that can be visited.
     */
    public static final AddressType PHYSICAL = AddressType.builder().value(Value.PHYSICAL).build();

    /**
     * Postal &amp; Physical
     * 
     * <p>An address that is both physical and postal.
     */
    public static final AddressType BOTH = AddressType.builder().value(Value.BOTH).build();

    private volatile int hashCode;

    private AddressType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this AddressType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating AddressType objects from a passed enum value.
     */
    public static AddressType of(Value value) {
        switch (value) {
        case POSTAL:
            return POSTAL;
        case PHYSICAL:
            return PHYSICAL;
        case BOTH:
            return BOTH;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AddressType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AddressType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating AddressType objects from a passed string value.
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
     * Inherited factory method for creating AddressType objects from a passed string value.
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
        AddressType other = (AddressType) obj;
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
         *     An enum constant for AddressType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public AddressType build() {
            AddressType addressType = new AddressType(this);
            if (validating) {
                validate(addressType);
            }
            return addressType;
        }

        protected void validate(AddressType addressType) {
            super.validate(addressType);
        }

        protected Builder from(AddressType addressType) {
            super.from(addressType);
            return this;
        }
    }

    public enum Value {
        /**
         * Postal
         * 
         * <p>Mailing addresses - PO Boxes and care-of addresses.
         */
        POSTAL("postal"),

        /**
         * Physical
         * 
         * <p>A physical address that can be visited.
         */
        PHYSICAL("physical"),

        /**
         * Postal &amp; Physical
         * 
         * <p>An address that is both physical and postal.
         */
        BOTH("both");

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
         * Factory method for creating AddressType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding AddressType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "postal":
                return POSTAL;
            case "physical":
                return PHYSICAL;
            case "both":
                return BOTH;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
