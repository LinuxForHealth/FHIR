/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

public class PropertyRepresentation extends Code {
    /**
     * XML Attribute
     */
    public static final PropertyRepresentation XML_ATTR = PropertyRepresentation.of(ValueSet.XML_ATTR);

    /**
     * XML Text
     */
    public static final PropertyRepresentation XML_TEXT = PropertyRepresentation.of(ValueSet.XML_TEXT);

    /**
     * Type Attribute
     */
    public static final PropertyRepresentation TYPE_ATTR = PropertyRepresentation.of(ValueSet.TYPE_ATTR);

    /**
     * CDA Text Format
     */
    public static final PropertyRepresentation CDA_TEXT = PropertyRepresentation.of(ValueSet.CDA_TEXT);

    /**
     * XHTML
     */
    public static final PropertyRepresentation XHTML = PropertyRepresentation.of(ValueSet.XHTML);

    private PropertyRepresentation(Builder builder) {
        super(builder);
    }

    public static PropertyRepresentation of(java.lang.String value) {
        return PropertyRepresentation.builder().value(value).build();
    }

    public static PropertyRepresentation of(ValueSet value) {
        return PropertyRepresentation.builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
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
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
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
