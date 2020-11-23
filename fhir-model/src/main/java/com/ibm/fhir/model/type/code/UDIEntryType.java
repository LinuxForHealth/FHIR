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

@System("http://hl7.org/fhir/udi-entry-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class UDIEntryType extends Code {
    /**
     * Barcode
     * 
     * <p>a barcodescanner captured the data from the device label.
     */
    public static final UDIEntryType BARCODE = UDIEntryType.builder().value(ValueSet.BARCODE).build();

    /**
     * RFID
     * 
     * <p>An RFID chip reader captured the data from the device label.
     */
    public static final UDIEntryType RFID = UDIEntryType.builder().value(ValueSet.RFID).build();

    /**
     * Manual
     * 
     * <p>The data was read from the label by a person and manually entered. (e.g. via a keyboard).
     */
    public static final UDIEntryType MANUAL = UDIEntryType.builder().value(ValueSet.MANUAL).build();

    /**
     * Card
     * 
     * <p>The data originated from a patient's implant card and was read by an operator.
     */
    public static final UDIEntryType CARD = UDIEntryType.builder().value(ValueSet.CARD).build();

    /**
     * Self Reported
     * 
     * <p>The data originated from a patient source and was not directly scanned or read from a label or card.
     */
    public static final UDIEntryType SELF_REPORTED = UDIEntryType.builder().value(ValueSet.SELF_REPORTED).build();

    /**
     * Unknown
     * 
     * <p>The method of data capture has not been determined.
     */
    public static final UDIEntryType UNKNOWN = UDIEntryType.builder().value(ValueSet.UNKNOWN).build();

    private volatile int hashCode;

    private UDIEntryType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating UDIEntryType objects from a passed enum value.
     */
    public static UDIEntryType of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public UDIEntryType build() {
            return new UDIEntryType(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating UDIEntryType.ValueSet values from a passed string value.
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
