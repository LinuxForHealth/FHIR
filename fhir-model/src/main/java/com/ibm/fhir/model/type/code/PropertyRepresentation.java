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

@System("http://hl7.org/fhir/property-representation")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class PropertyRepresentation extends Code {
    /**
     * XML Attribute
     */
    public static final PropertyRepresentation XML_ATTR = PropertyRepresentation.builder().value(ValueSet.XML_ATTR).build();

    /**
     * XML Text
     */
    public static final PropertyRepresentation XML_TEXT = PropertyRepresentation.builder().value(ValueSet.XML_TEXT).build();

    /**
     * Type Attribute
     */
    public static final PropertyRepresentation TYPE_ATTR = PropertyRepresentation.builder().value(ValueSet.TYPE_ATTR).build();

    /**
     * CDA Text Format
     */
    public static final PropertyRepresentation CDA_TEXT = PropertyRepresentation.builder().value(ValueSet.CDA_TEXT).build();

    /**
     * XHTML
     */
    public static final PropertyRepresentation XHTML = PropertyRepresentation.builder().value(ValueSet.XHTML).build();

    private volatile int hashCode;

    private PropertyRepresentation(Builder builder) {
        super(builder);
    }

    public static PropertyRepresentation of(ValueSet value) {
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

    public static PropertyRepresentation of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

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
        public PropertyRepresentation build() {
            return new PropertyRepresentation(this);
        }
    }

    public enum ValueSet {
        /**
         * XML Attribute
         */
        XML_ATTR("xmlAttr"),

        /**
         * XML Text
         */
        XML_TEXT("xmlText"),

        /**
         * Type Attribute
         */
        TYPE_ATTR("typeAttr"),

        /**
         * CDA Text Format
         */
        CDA_TEXT("cdaText"),

        /**
         * XHTML
         */
        XHTML("xhtml");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

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
