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

@System("http://hl7.org/fhir/search-comparator")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SearchComparator extends Code {
    /**
     * Equals
     * 
     * <p>the value for the parameter in the resource is equal to the provided value.
     */
    public static final SearchComparator EQ = SearchComparator.builder().value(Value.EQ).build();

    /**
     * Not Equals
     * 
     * <p>the value for the parameter in the resource is not equal to the provided value.
     */
    public static final SearchComparator NE = SearchComparator.builder().value(Value.NE).build();

    /**
     * Greater Than
     * 
     * <p>the value for the parameter in the resource is greater than the provided value.
     */
    public static final SearchComparator GT = SearchComparator.builder().value(Value.GT).build();

    /**
     * Less Than
     * 
     * <p>the value for the parameter in the resource is less than the provided value.
     */
    public static final SearchComparator LT = SearchComparator.builder().value(Value.LT).build();

    /**
     * Greater or Equals
     * 
     * <p>the value for the parameter in the resource is greater or equal to the provided value.
     */
    public static final SearchComparator GE = SearchComparator.builder().value(Value.GE).build();

    /**
     * Less of Equal
     * 
     * <p>the value for the parameter in the resource is less or equal to the provided value.
     */
    public static final SearchComparator LE = SearchComparator.builder().value(Value.LE).build();

    /**
     * Starts After
     * 
     * <p>the value for the parameter in the resource starts after the provided value.
     */
    public static final SearchComparator SA = SearchComparator.builder().value(Value.SA).build();

    /**
     * Ends Before
     * 
     * <p>the value for the parameter in the resource ends before the provided value.
     */
    public static final SearchComparator EB = SearchComparator.builder().value(Value.EB).build();

    /**
     * Approximately
     * 
     * <p>the value for the parameter in the resource is approximately the same to the provided value.
     */
    public static final SearchComparator AP = SearchComparator.builder().value(Value.AP).build();

    private volatile int hashCode;

    private SearchComparator(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this SearchComparator as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating SearchComparator objects from a passed enum value.
     */
    public static SearchComparator of(Value value) {
        switch (value) {
        case EQ:
            return EQ;
        case NE:
            return NE;
        case GT:
            return GT;
        case LT:
            return LT;
        case GE:
            return GE;
        case LE:
            return LE;
        case SA:
            return SA;
        case EB:
            return EB;
        case AP:
            return AP;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating SearchComparator objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static SearchComparator of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating SearchComparator objects from a passed string value.
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
     * Inherited factory method for creating SearchComparator objects from a passed string value.
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
        SearchComparator other = (SearchComparator) obj;
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
         *     An enum constant for SearchComparator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public SearchComparator build() {
            SearchComparator searchComparator = new SearchComparator(this);
            if (validating) {
                validate(searchComparator);
            }
            return searchComparator;
        }

        protected void validate(SearchComparator searchComparator) {
            super.validate(searchComparator);
        }

        protected Builder from(SearchComparator searchComparator) {
            super.from(searchComparator);
            return this;
        }
    }

    public enum Value {
        /**
         * Equals
         * 
         * <p>the value for the parameter in the resource is equal to the provided value.
         */
        EQ("eq"),

        /**
         * Not Equals
         * 
         * <p>the value for the parameter in the resource is not equal to the provided value.
         */
        NE("ne"),

        /**
         * Greater Than
         * 
         * <p>the value for the parameter in the resource is greater than the provided value.
         */
        GT("gt"),

        /**
         * Less Than
         * 
         * <p>the value for the parameter in the resource is less than the provided value.
         */
        LT("lt"),

        /**
         * Greater or Equals
         * 
         * <p>the value for the parameter in the resource is greater or equal to the provided value.
         */
        GE("ge"),

        /**
         * Less of Equal
         * 
         * <p>the value for the parameter in the resource is less or equal to the provided value.
         */
        LE("le"),

        /**
         * Starts After
         * 
         * <p>the value for the parameter in the resource starts after the provided value.
         */
        SA("sa"),

        /**
         * Ends Before
         * 
         * <p>the value for the parameter in the resource ends before the provided value.
         */
        EB("eb"),

        /**
         * Approximately
         * 
         * <p>the value for the parameter in the resource is approximately the same to the provided value.
         */
        AP("ap");

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
         * Factory method for creating SearchComparator.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding SearchComparator.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "eq":
                return EQ;
            case "ne":
                return NE;
            case "gt":
                return GT;
            case "lt":
                return LT;
            case "ge":
                return GE;
            case "le":
                return LE;
            case "sa":
                return SA;
            case "eb":
                return EB;
            case "ap":
                return AP;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
