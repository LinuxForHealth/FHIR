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

@System("http://hl7.org/fhir/search-modifier-code")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SearchModifierCode extends Code {
    /**
     * Missing
     * 
     * <p>The search parameter returns resources that have a value or not.
     */
    public static final SearchModifierCode MISSING = SearchModifierCode.builder().value(Value.MISSING).build();

    /**
     * Exact
     * 
     * <p>The search parameter returns resources that have a value that exactly matches the supplied parameter (the whole 
     * string, including casing and accents).
     */
    public static final SearchModifierCode EXACT = SearchModifierCode.builder().value(Value.EXACT).build();

    /**
     * Contains
     * 
     * <p>The search parameter returns resources that include the supplied parameter value anywhere within the field being 
     * searched.
     */
    public static final SearchModifierCode CONTAINS = SearchModifierCode.builder().value(Value.CONTAINS).build();

    /**
     * Not
     * 
     * <p>The search parameter returns resources that do not contain a match.
     */
    public static final SearchModifierCode NOT = SearchModifierCode.builder().value(Value.NOT).build();

    /**
     * Text
     * 
     * <p>The search parameter is processed as a string that searches text associated with the code/value - either 
     * CodeableConcept.text, Coding.display, or Identifier.type.text.
     */
    public static final SearchModifierCode TEXT = SearchModifierCode.builder().value(Value.TEXT).build();

    /**
     * In
     * 
     * <p>The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests 
     * whether the coding is in the specified value set.
     */
    public static final SearchModifierCode IN = SearchModifierCode.builder().value(Value.IN).build();

    /**
     * Not In
     * 
     * <p>The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests 
     * whether the coding is not in the specified value set.
     */
    public static final SearchModifierCode NOT_IN = SearchModifierCode.builder().value(Value.NOT_IN).build();

    /**
     * Below
     * 
     * <p>The search parameter tests whether the value in a resource is subsumed by the specified value (is-a, or 
     * hierarchical relationships).
     */
    public static final SearchModifierCode BELOW = SearchModifierCode.builder().value(Value.BELOW).build();

    /**
     * Above
     * 
     * <p>The search parameter tests whether the value in a resource subsumes the specified value (is-a, or hierarchical 
     * relationships).
     */
    public static final SearchModifierCode ABOVE = SearchModifierCode.builder().value(Value.ABOVE).build();

    /**
     * Type
     * 
     * <p>The search parameter only applies to the Resource Type specified as a modifier (e.g. the modifier is not actually :
     * type, but :Patient etc.).
     */
    public static final SearchModifierCode TYPE = SearchModifierCode.builder().value(Value.TYPE).build();

    /**
     * Identifier
     * 
     * <p>The search parameter applies to the identifier on the resource, not the reference.
     */
    public static final SearchModifierCode IDENTIFIER = SearchModifierCode.builder().value(Value.IDENTIFIER).build();

    /**
     * Of Type
     * 
     * <p>The search parameter has the format system|code|value, where the system and code refer to an Identifier.type.coding.
     * system and .code, and match if any of the type codes match. All 3 parts must be present.
     */
    public static final SearchModifierCode OF_TYPE = SearchModifierCode.builder().value(Value.OF_TYPE).build();

    private volatile int hashCode;

    private SearchModifierCode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SearchModifierCode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SearchModifierCode objects from a passed enum value.
     */
    public static SearchModifierCode of(Value value) {
        switch (value) {
        case MISSING:
            return MISSING;
        case EXACT:
            return EXACT;
        case CONTAINS:
            return CONTAINS;
        case NOT:
            return NOT;
        case TEXT:
            return TEXT;
        case IN:
            return IN;
        case NOT_IN:
            return NOT_IN;
        case BELOW:
            return BELOW;
        case ABOVE:
            return ABOVE;
        case TYPE:
            return TYPE;
        case IDENTIFIER:
            return IDENTIFIER;
        case OF_TYPE:
            return OF_TYPE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SearchModifierCode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SearchModifierCode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SearchModifierCode objects from a passed string value.
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
     * Inherited factory method for creating SearchModifierCode objects from a passed string value.
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
        SearchModifierCode other = (SearchModifierCode) obj;
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
         *     An enum constant for SearchModifierCode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SearchModifierCode build() {
            SearchModifierCode searchModifierCode = new SearchModifierCode(this);
            if (validating) {
                validate(searchModifierCode);
            }
            return searchModifierCode;
        }

        protected void validate(SearchModifierCode searchModifierCode) {
            super.validate(searchModifierCode);
        }

        protected Builder from(SearchModifierCode searchModifierCode) {
            super.from(searchModifierCode);
            return this;
        }
    }

    public enum Value {
        /**
         * Missing
         * 
         * <p>The search parameter returns resources that have a value or not.
         */
        MISSING("missing"),

        /**
         * Exact
         * 
         * <p>The search parameter returns resources that have a value that exactly matches the supplied parameter (the whole 
         * string, including casing and accents).
         */
        EXACT("exact"),

        /**
         * Contains
         * 
         * <p>The search parameter returns resources that include the supplied parameter value anywhere within the field being 
         * searched.
         */
        CONTAINS("contains"),

        /**
         * Not
         * 
         * <p>The search parameter returns resources that do not contain a match.
         */
        NOT("not"),

        /**
         * Text
         * 
         * <p>The search parameter is processed as a string that searches text associated with the code/value - either 
         * CodeableConcept.text, Coding.display, or Identifier.type.text.
         */
        TEXT("text"),

        /**
         * In
         * 
         * <p>The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests 
         * whether the coding is in the specified value set.
         */
        IN("in"),

        /**
         * Not In
         * 
         * <p>The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests 
         * whether the coding is not in the specified value set.
         */
        NOT_IN("not-in"),

        /**
         * Below
         * 
         * <p>The search parameter tests whether the value in a resource is subsumed by the specified value (is-a, or 
         * hierarchical relationships).
         */
        BELOW("below"),

        /**
         * Above
         * 
         * <p>The search parameter tests whether the value in a resource subsumes the specified value (is-a, or hierarchical 
         * relationships).
         */
        ABOVE("above"),

        /**
         * Type
         * 
         * <p>The search parameter only applies to the Resource Type specified as a modifier (e.g. the modifier is not actually :
         * type, but :Patient etc.).
         */
        TYPE("type"),

        /**
         * Identifier
         * 
         * <p>The search parameter applies to the identifier on the resource, not the reference.
         */
        IDENTIFIER("identifier"),

        /**
         * Of Type
         * 
         * <p>The search parameter has the format system|code|value, where the system and code refer to an Identifier.type.coding.
         * system and .code, and match if any of the type codes match. All 3 parts must be present.
         */
        OF_TYPE("ofType");

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
         * Factory method for creating SearchModifierCode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SearchModifierCode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "missing":
                return MISSING;
            case "exact":
                return EXACT;
            case "contains":
                return CONTAINS;
            case "not":
                return NOT;
            case "text":
                return TEXT;
            case "in":
                return IN;
            case "not-in":
                return NOT_IN;
            case "below":
                return BELOW;
            case "above":
                return ABOVE;
            case "type":
                return TYPE;
            case "identifier":
                return IDENTIFIER;
            case "ofType":
                return OF_TYPE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
