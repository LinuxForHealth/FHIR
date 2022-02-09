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

@System("http://hl7.org/fhir/property-representation")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class PropertyRepresentation extends Code {
    /**
     * XML Attribute
     * 
     * <p>In XML, this property is represented as an attribute not an element.
     */
    public static final PropertyRepresentation XML_ATTR = PropertyRepresentation.builder().value(Value.XML_ATTR).build();

    /**
     * XML Text
     * 
     * <p>This element is represented using the XML text attribute (primitives only).
     */
    public static final PropertyRepresentation XML_TEXT = PropertyRepresentation.builder().value(Value.XML_TEXT).build();

    /**
     * Type Attribute
     * 
     * <p>The type of this element is indicated using xsi:type.
     */
    public static final PropertyRepresentation TYPE_ATTR = PropertyRepresentation.builder().value(Value.TYPE_ATTR).build();

    /**
     * CDA Text Format
     * 
     * <p>Use CDA narrative instead of XHTML.
     */
    public static final PropertyRepresentation CDA_TEXT = PropertyRepresentation.builder().value(Value.CDA_TEXT).build();

    /**
     * XHTML
     * 
     * <p>The property is represented using XHTML.
     */
    public static final PropertyRepresentation XHTML = PropertyRepresentation.builder().value(Value.XHTML).build();

    private volatile int hashCode;

    private PropertyRepresentation(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this PropertyRepresentation as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating PropertyRepresentation objects from a passed enum value.
     */
    public static PropertyRepresentation of(Value value) {
        switch (value) {
        case XML_ATTR:
            return XML_ATTR;
        case XML_TEXT:
            return XML_TEXT;
        case TYPE_ATTR:
            return TYPE_ATTR;
        case CDA_TEXT:
            return CDA_TEXT;
        case XHTML:
            return XHTML;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating PropertyRepresentation objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static PropertyRepresentation of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating PropertyRepresentation objects from a passed string value.
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
     * Inherited factory method for creating PropertyRepresentation objects from a passed string value.
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
        PropertyRepresentation other = (PropertyRepresentation) obj;
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
         *     An enum constant for PropertyRepresentation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public PropertyRepresentation build() {
            PropertyRepresentation propertyRepresentation = new PropertyRepresentation(this);
            if (validating) {
                validate(propertyRepresentation);
            }
            return propertyRepresentation;
        }

        protected void validate(PropertyRepresentation propertyRepresentation) {
            super.validate(propertyRepresentation);
        }

        protected Builder from(PropertyRepresentation propertyRepresentation) {
            super.from(propertyRepresentation);
            return this;
        }
    }

    public enum Value {
        /**
         * XML Attribute
         * 
         * <p>In XML, this property is represented as an attribute not an element.
         */
        XML_ATTR("xmlAttr"),

        /**
         * XML Text
         * 
         * <p>This element is represented using the XML text attribute (primitives only).
         */
        XML_TEXT("xmlText"),

        /**
         * Type Attribute
         * 
         * <p>The type of this element is indicated using xsi:type.
         */
        TYPE_ATTR("typeAttr"),

        /**
         * CDA Text Format
         * 
         * <p>Use CDA narrative instead of XHTML.
         */
        CDA_TEXT("cdaText"),

        /**
         * XHTML
         * 
         * <p>The property is represented using XHTML.
         */
        XHTML("xhtml");

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
         * Factory method for creating PropertyRepresentation.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding PropertyRepresentation.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "xmlAttr":
                return XML_ATTR;
            case "xmlText":
                return XML_TEXT;
            case "typeAttr":
                return TYPE_ATTR;
            case "cdaText":
                return CDA_TEXT;
            case "xhtml":
                return XHTML;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
