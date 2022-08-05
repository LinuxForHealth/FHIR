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

@System("http://hl7.org/fhir/search-param-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class SearchParamType extends Code {
    /**
     * Number
     * 
     * <p>Search parameter SHALL be a number (a whole number, or a decimal).
     */
    public static final SearchParamType NUMBER = SearchParamType.builder().value(Value.NUMBER).build();

    /**
     * Date/DateTime
     * 
     * <p>Search parameter is on a date/time. The date format is the standard XML format, though other formats may be 
     * supported.
     */
    public static final SearchParamType DATE = SearchParamType.builder().value(Value.DATE).build();

    /**
     * String
     * 
     * <p>Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match 
     * just the start of a string. String parameters may contain spaces.
     */
    public static final SearchParamType STRING = SearchParamType.builder().value(Value.STRING).build();

    /**
     * Token
     * 
     * <p>Search parameter on a coded element or identifier. May be used to search through the text, display, code and 
     * code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of 
     * namespace and value, separated by a "|", depending on the modifier used.
     */
    public static final SearchParamType TOKEN = SearchParamType.builder().value(Value.TOKEN).build();

    /**
     * Reference
     * 
     * <p>A reference to another resource (Reference or canonical).
     */
    public static final SearchParamType REFERENCE = SearchParamType.builder().value(Value.REFERENCE).build();

    /**
     * Composite
     * 
     * <p>A composite search parameter that combines a search on two values together.
     */
    public static final SearchParamType COMPOSITE = SearchParamType.builder().value(Value.COMPOSITE).build();

    /**
     * Quantity
     * 
     * <p>A search parameter that searches on a quantity.
     */
    public static final SearchParamType QUANTITY = SearchParamType.builder().value(Value.QUANTITY).build();

    /**
     * URI
     * 
     * <p>A search parameter that searches on a URI (RFC 3986).
     */
    public static final SearchParamType URI = SearchParamType.builder().value(Value.URI).build();

    /**
     * Special
     * 
     * <p>Special logic applies to this parameter per the description of the search parameter.
     */
    public static final SearchParamType SPECIAL = SearchParamType.builder().value(Value.SPECIAL).build();

    private volatile int hashCode;

    private SearchParamType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SearchParamType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SearchParamType objects from a passed enum value.
     */
    public static SearchParamType of(Value value) {
        switch (value) {
        case NUMBER:
            return NUMBER;
        case DATE:
            return DATE;
        case STRING:
            return STRING;
        case TOKEN:
            return TOKEN;
        case REFERENCE:
            return REFERENCE;
        case COMPOSITE:
            return COMPOSITE;
        case QUANTITY:
            return QUANTITY;
        case URI:
            return URI;
        case SPECIAL:
            return SPECIAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SearchParamType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SearchParamType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SearchParamType objects from a passed string value.
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
     * Inherited factory method for creating SearchParamType objects from a passed string value.
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
        SearchParamType other = (SearchParamType) obj;
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
         *     An enum constant for SearchParamType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SearchParamType build() {
            SearchParamType searchParamType = new SearchParamType(this);
            if (validating) {
                validate(searchParamType);
            }
            return searchParamType;
        }

        protected void validate(SearchParamType searchParamType) {
            super.validate(searchParamType);
        }

        protected Builder from(SearchParamType searchParamType) {
            super.from(searchParamType);
            return this;
        }
    }

    public enum Value {
        /**
         * Number
         * 
         * <p>Search parameter SHALL be a number (a whole number, or a decimal).
         */
        NUMBER("number"),

        /**
         * Date/DateTime
         * 
         * <p>Search parameter is on a date/time. The date format is the standard XML format, though other formats may be 
         * supported.
         */
        DATE("date"),

        /**
         * String
         * 
         * <p>Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match 
         * just the start of a string. String parameters may contain spaces.
         */
        STRING("string"),

        /**
         * Token
         * 
         * <p>Search parameter on a coded element or identifier. May be used to search through the text, display, code and 
         * code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of 
         * namespace and value, separated by a "|", depending on the modifier used.
         */
        TOKEN("token"),

        /**
         * Reference
         * 
         * <p>A reference to another resource (Reference or canonical).
         */
        REFERENCE("reference"),

        /**
         * Composite
         * 
         * <p>A composite search parameter that combines a search on two values together.
         */
        COMPOSITE("composite"),

        /**
         * Quantity
         * 
         * <p>A search parameter that searches on a quantity.
         */
        QUANTITY("quantity"),

        /**
         * URI
         * 
         * <p>A search parameter that searches on a URI (RFC 3986).
         */
        URI("uri"),

        /**
         * Special
         * 
         * <p>Special logic applies to this parameter per the description of the search parameter.
         */
        SPECIAL("special");

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
         * Factory method for creating SearchParamType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SearchParamType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "number":
                return NUMBER;
            case "date":
                return DATE;
            case "string":
                return STRING;
            case "token":
                return TOKEN;
            case "reference":
                return REFERENCE;
            case "composite":
                return COMPOSITE;
            case "quantity":
                return QUANTITY;
            case "uri":
                return URI;
            case "special":
                return SPECIAL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
