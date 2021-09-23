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

@System("http://hl7.org/fhir/namingsystem-identifier-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class NamingSystemIdentifierType extends Code {
    /**
     * OID
     * 
     * <p>An ISO object identifier; e.g. 1.2.3.4.5.
     */
    public static final NamingSystemIdentifierType OID = NamingSystemIdentifierType.builder().value(Value.OID).build();

    /**
     * UUID
     * 
     * <p>A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
     */
    public static final NamingSystemIdentifierType UUID = NamingSystemIdentifierType.builder().value(Value.UUID).build();

    /**
     * URI
     * 
     * <p>A uniform resource identifier (ideally a URL - uniform resource locator); e.g. http://unitsofmeasure.org.
     */
    public static final NamingSystemIdentifierType URI = NamingSystemIdentifierType.builder().value(Value.URI).build();

    /**
     * Other
     * 
     * <p>Some other type of unique identifier; e.g. HL7-assigned reserved string such as LN for LOINC.
     */
    public static final NamingSystemIdentifierType OTHER = NamingSystemIdentifierType.builder().value(Value.OTHER).build();

    private volatile int hashCode;

    private NamingSystemIdentifierType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this NamingSystemIdentifierType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating NamingSystemIdentifierType objects from a passed enum value.
     */
    public static NamingSystemIdentifierType of(Value value) {
        switch (value) {
        case OID:
            return OID;
        case UUID:
            return UUID;
        case URI:
            return URI;
        case OTHER:
            return OTHER;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating NamingSystemIdentifierType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static NamingSystemIdentifierType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating NamingSystemIdentifierType objects from a passed string value.
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
     * Inherited factory method for creating NamingSystemIdentifierType objects from a passed string value.
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
        NamingSystemIdentifierType other = (NamingSystemIdentifierType) obj;
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
         *     An enum constant for NamingSystemIdentifierType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public NamingSystemIdentifierType build() {
            NamingSystemIdentifierType namingSystemIdentifierType = new NamingSystemIdentifierType(this);
            if (validating) {
                validate(namingSystemIdentifierType);
            }
            return namingSystemIdentifierType;
        }

        protected void validate(NamingSystemIdentifierType namingSystemIdentifierType) {
            super.validate(namingSystemIdentifierType);
        }

        protected Builder from(NamingSystemIdentifierType namingSystemIdentifierType) {
            super.from(namingSystemIdentifierType);
            return this;
        }
    }

    public enum Value {
        /**
         * OID
         * 
         * <p>An ISO object identifier; e.g. 1.2.3.4.5.
         */
        OID("oid"),

        /**
         * UUID
         * 
         * <p>A universally unique identifier of the form a5afddf4-e880-459b-876e-e4591b0acc11.
         */
        UUID("uuid"),

        /**
         * URI
         * 
         * <p>A uniform resource identifier (ideally a URL - uniform resource locator); e.g. http://unitsofmeasure.org.
         */
        URI("uri"),

        /**
         * Other
         * 
         * <p>Some other type of unique identifier; e.g. HL7-assigned reserved string such as LN for LOINC.
         */
        OTHER("other");

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
         * Factory method for creating NamingSystemIdentifierType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding NamingSystemIdentifierType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "oid":
                return OID;
            case "uuid":
                return UUID;
            case "uri":
                return URI;
            case "other":
                return OTHER;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
