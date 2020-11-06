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

@System("http://hl7.org/fhir/filter-operator")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class FilterOperator extends Code {
    /**
     * Equals
     * 
     * <p>The specified property of the code equals the provided value.
     */
    public static final FilterOperator EQUALS = FilterOperator.builder().value(ValueSet.EQUALS).build();

    /**
     * Is A (by subsumption)
     * 
     * <p>Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, 
     * including the provided concept itself (include descendant codes and self).
     */
    public static final FilterOperator IS_A = FilterOperator.builder().value(ValueSet.IS_A).build();

    /**
     * Descendent Of (by subsumption)
     * 
     * <p>Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, 
     * excluding the provided concept itself i.e. include descendant codes only).
     */
    public static final FilterOperator DESCENDENT_OF = FilterOperator.builder().value(ValueSet.DESCENDENT_OF).build();

    /**
     * Not (Is A) (by subsumption)
     * 
     * <p>The specified property of the code does not have an is-a relationship with the provided value.
     */
    public static final FilterOperator IS_NOT_A = FilterOperator.builder().value(ValueSet.IS_NOT_A).build();

    /**
     * Regular Expression
     * 
     * <p>The specified property of the code matches the regex specified in the provided value.
     */
    public static final FilterOperator REGEX = FilterOperator.builder().value(ValueSet.REGEX).build();

    /**
     * In Set
     * 
     * <p>The specified property of the code is in the set of codes or concepts specified in the provided value (comma 
     * separated list).
     */
    public static final FilterOperator IN = FilterOperator.builder().value(ValueSet.IN).build();

    /**
     * Not in Set
     * 
     * <p>The specified property of the code is not in the set of codes or concepts specified in the provided value (comma 
     * separated list).
     */
    public static final FilterOperator NOT_IN = FilterOperator.builder().value(ValueSet.NOT_IN).build();

    /**
     * Generalizes (by Subsumption)
     * 
     * <p>Includes all concept ids that have a transitive is-a relationship from the concept Id provided as the value, 
     * including the provided concept itself (i.e. include ancestor codes and self).
     */
    public static final FilterOperator GENERALIZES = FilterOperator.builder().value(ValueSet.GENERALIZES).build();

    /**
     * Exists
     * 
     * <p>The specified property of the code has at least one value (if the specified value is true; if the specified value 
     * is false, then matches when the specified property of the code has no values).
     */
    public static final FilterOperator EXISTS = FilterOperator.builder().value(ValueSet.EXISTS).build();

    private volatile int hashCode;

    private FilterOperator(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating FilterOperator objects from a passed enum value.
     */
    public static FilterOperator of(ValueSet value) {
        switch (value) {
        case EQUALS:
            return EQUALS;
        case IS_A:
            return IS_A;
        case DESCENDENT_OF:
            return DESCENDENT_OF;
        case IS_NOT_A:
            return IS_NOT_A;
        case REGEX:
            return REGEX;
        case IN:
            return IN;
        case NOT_IN:
            return NOT_IN;
        case GENERALIZES:
            return GENERALIZES;
        case EXISTS:
            return EXISTS;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating FilterOperator objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static FilterOperator of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating FilterOperator objects from a passed string value.
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
     * Inherited factory method for creating FilterOperator objects from a passed string value.
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
        FilterOperator other = (FilterOperator) obj;
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
        public FilterOperator build() {
            return new FilterOperator(this);
        }
    }

    public enum ValueSet {
        /**
         * Equals
         * 
         * <p>The specified property of the code equals the provided value.
         */
        EQUALS("="),

        /**
         * Is A (by subsumption)
         * 
         * <p>Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, 
         * including the provided concept itself (include descendant codes and self).
         */
        IS_A("is-a"),

        /**
         * Descendent Of (by subsumption)
         * 
         * <p>Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, 
         * excluding the provided concept itself i.e. include descendant codes only).
         */
        DESCENDENT_OF("descendent-of"),

        /**
         * Not (Is A) (by subsumption)
         * 
         * <p>The specified property of the code does not have an is-a relationship with the provided value.
         */
        IS_NOT_A("is-not-a"),

        /**
         * Regular Expression
         * 
         * <p>The specified property of the code matches the regex specified in the provided value.
         */
        REGEX("regex"),

        /**
         * In Set
         * 
         * <p>The specified property of the code is in the set of codes or concepts specified in the provided value (comma 
         * separated list).
         */
        IN("in"),

        /**
         * Not in Set
         * 
         * <p>The specified property of the code is not in the set of codes or concepts specified in the provided value (comma 
         * separated list).
         */
        NOT_IN("not-in"),

        /**
         * Generalizes (by Subsumption)
         * 
         * <p>Includes all concept ids that have a transitive is-a relationship from the concept Id provided as the value, 
         * including the provided concept itself (i.e. include ancestor codes and self).
         */
        GENERALIZES("generalizes"),

        /**
         * Exists
         * 
         * <p>The specified property of the code has at least one value (if the specified value is true; if the specified value 
         * is false, then matches when the specified property of the code has no values).
         */
        EXISTS("exists");

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
         * Factory method for creating FilterOperator.ValueSet values from a passed string value.
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
