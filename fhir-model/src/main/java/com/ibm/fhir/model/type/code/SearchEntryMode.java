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

@System("http://hl7.org/fhir/search-entry-mode")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SearchEntryMode extends Code {
    /**
     * Match
     * 
     * <p>This resource matched the search specification.
     */
    public static final SearchEntryMode MATCH = SearchEntryMode.builder().value(Value.MATCH).build();

    /**
     * Include
     * 
     * <p>This resource is returned because it is referred to from another resource in the search set.
     */
    public static final SearchEntryMode INCLUDE = SearchEntryMode.builder().value(Value.INCLUDE).build();

    /**
     * Outcome
     * 
     * <p>An OperationOutcome that provides additional information about the processing of a search.
     */
    public static final SearchEntryMode OUTCOME = SearchEntryMode.builder().value(Value.OUTCOME).build();

    private volatile int hashCode;

    private SearchEntryMode(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SearchEntryMode as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SearchEntryMode objects from a passed enum value.
     */
    public static SearchEntryMode of(Value value) {
        switch (value) {
        case MATCH:
            return MATCH;
        case INCLUDE:
            return INCLUDE;
        case OUTCOME:
            return OUTCOME;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SearchEntryMode objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SearchEntryMode of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SearchEntryMode objects from a passed string value.
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
     * Inherited factory method for creating SearchEntryMode objects from a passed string value.
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
        SearchEntryMode other = (SearchEntryMode) obj;
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
         *     An enum constant for SearchEntryMode
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SearchEntryMode build() {
            SearchEntryMode searchEntryMode = new SearchEntryMode(this);
            if (validating) {
                validate(searchEntryMode);
            }
            return searchEntryMode;
        }

        protected void validate(SearchEntryMode searchEntryMode) {
            super.validate(searchEntryMode);
        }

        protected Builder from(SearchEntryMode searchEntryMode) {
            super.from(searchEntryMode);
            return this;
        }
    }

    public enum Value {
        /**
         * Match
         * 
         * <p>This resource matched the search specification.
         */
        MATCH("match"),

        /**
         * Include
         * 
         * <p>This resource is returned because it is referred to from another resource in the search set.
         */
        INCLUDE("include"),

        /**
         * Outcome
         * 
         * <p>An OperationOutcome that provides additional information about the processing of a search.
         */
        OUTCOME("outcome");

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
         * Factory method for creating SearchEntryMode.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SearchEntryMode.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "match":
                return MATCH;
            case "include":
                return INCLUDE;
            case "outcome":
                return OUTCOME;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
