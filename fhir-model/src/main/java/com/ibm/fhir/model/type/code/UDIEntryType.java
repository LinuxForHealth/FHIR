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

@System("http://hl7.org/fhir/udi-entry-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class UDIEntryType extends Code {
    /**
     * Barcode
     * 
     * <p>a barcodescanner captured the data from the device label.
     */
    public static final UDIEntryType BARCODE = UDIEntryType.builder().value(Value.BARCODE).build();

    /**
     * RFID
     * 
     * <p>An RFID chip reader captured the data from the device label.
     */
    public static final UDIEntryType RFID = UDIEntryType.builder().value(Value.RFID).build();

    /**
     * Manual
     * 
     * <p>The data was read from the label by a person and manually entered. (e.g. via a keyboard).
     */
    public static final UDIEntryType MANUAL = UDIEntryType.builder().value(Value.MANUAL).build();

    /**
     * Card
     * 
     * <p>The data originated from a patient's implant card and was read by an operator.
     */
    public static final UDIEntryType CARD = UDIEntryType.builder().value(Value.CARD).build();

    /**
     * Self Reported
     * 
     * <p>The data originated from a patient source and was not directly scanned or read from a label or card.
     */
    public static final UDIEntryType SELF_REPORTED = UDIEntryType.builder().value(Value.SELF_REPORTED).build();

    /**
     * Unknown
     * 
     * <p>The method of data capture has not been determined.
     */
    public static final UDIEntryType UNKNOWN = UDIEntryType.builder().value(Value.UNKNOWN).build();

    private volatile int hashCode;

    private UDIEntryType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this UDIEntryType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating UDIEntryType objects from a passed enum value.
     */
    public static UDIEntryType of(Value value) {
        switch (value) {
        case BARCODE:
            return BARCODE;
        case RFID:
            return RFID;
        case MANUAL:
            return MANUAL;
        case CARD:
            return CARD;
        case SELF_REPORTED:
            return SELF_REPORTED;
        case UNKNOWN:
            return UNKNOWN;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating UDIEntryType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static UDIEntryType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating UDIEntryType objects from a passed string value.
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
     * Inherited factory method for creating UDIEntryType objects from a passed string value.
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
        UDIEntryType other = (UDIEntryType) obj;
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
         *     An enum constant for UDIEntryType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public UDIEntryType build() {
            UDIEntryType uDIEntryType = new UDIEntryType(this);
            if (validating) {
                validate(uDIEntryType);
            }
            return uDIEntryType;
        }

        protected void validate(UDIEntryType uDIEntryType) {
            super.validate(uDIEntryType);
        }

        protected Builder from(UDIEntryType uDIEntryType) {
            super.from(uDIEntryType);
            return this;
        }
    }

    public enum Value {
        /**
         * Barcode
         * 
         * <p>a barcodescanner captured the data from the device label.
         */
        BARCODE("barcode"),

        /**
         * RFID
         * 
         * <p>An RFID chip reader captured the data from the device label.
         */
        RFID("rfid"),

        /**
         * Manual
         * 
         * <p>The data was read from the label by a person and manually entered. (e.g. via a keyboard).
         */
        MANUAL("manual"),

        /**
         * Card
         * 
         * <p>The data originated from a patient's implant card and was read by an operator.
         */
        CARD("card"),

        /**
         * Self Reported
         * 
         * <p>The data originated from a patient source and was not directly scanned or read from a label or card.
         */
        SELF_REPORTED("self-reported"),

        /**
         * Unknown
         * 
         * <p>The method of data capture has not been determined.
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
         * Factory method for creating UDIEntryType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding UDIEntryType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "barcode":
                return BARCODE;
            case "rfid":
                return RFID;
            case "manual":
                return MANUAL;
            case "card":
                return CARD;
            case "self-reported":
                return SELF_REPORTED;
            case "unknown":
                return UNKNOWN;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
