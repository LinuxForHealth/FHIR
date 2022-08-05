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

@System("http://hl7.org/fhir/filter-operator")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class FilterOperator extends Code {
    /**
     * Equals
     * 
     * <p>The specified property of the code equals the provided value.
     */
    public static final FilterOperator EQUALS = FilterOperator.builder().value(Value.EQUALS).build();

    /**
     * Is A (by subsumption)
     * 
     * <p>Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, 
     * including the provided concept itself (include descendant codes and self).
     */
    public static final FilterOperator IS_A = FilterOperator.builder().value(Value.IS_A).build();

    /**
     * Descendent Of (by subsumption)
     * 
     * <p>Includes all concept ids that have a transitive is-a relationship with the concept Id provided as the value, 
     * excluding the provided concept itself i.e. include descendant codes only).
     */
    public static final FilterOperator DESCENDENT_OF = FilterOperator.builder().value(Value.DESCENDENT_OF).build();

    /**
     * Not (Is A) (by subsumption)
     * 
     * <p>The specified property of the code does not have an is-a relationship with the provided value.
     */
    public static final FilterOperator IS_NOT_A = FilterOperator.builder().value(Value.IS_NOT_A).build();

    /**
     * Regular Expression
     * 
     * <p>The specified property of the code matches the regex specified in the provided value.
     */
    public static final FilterOperator REGEX = FilterOperator.builder().value(Value.REGEX).build();

    /**
     * In Set
     * 
     * <p>The specified property of the code is in the set of codes or concepts specified in the provided value (comma 
     * separated list).
     */
    public static final FilterOperator IN = FilterOperator.builder().value(Value.IN).build();

    /**
     * Not in Set
     * 
     * <p>The specified property of the code is not in the set of codes or concepts specified in the provided value (comma 
     * separated list).
     */
    public static final FilterOperator NOT_IN = FilterOperator.builder().value(Value.NOT_IN).build();

    /**
     * Generalizes (by Subsumption)
     * 
     * <p>Includes all concept ids that have a transitive is-a relationship from the concept Id provided as the value, 
     * including the provided concept itself (i.e. include ancestor codes and self).
     */
    public static final FilterOperator GENERALIZES = FilterOperator.builder().value(Value.GENERALIZES).build();

    /**
     * Exists
     * 
     * <p>The specified property of the code has at least one value (if the specified value is true; if the specified value 
     * is false, then matches when the specified property of the code has no values).
     */
    public static final FilterOperator EXISTS = FilterOperator.builder().value(Value.EXISTS).build();

    private volatile int hashCode;

    private FilterOperator(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this FilterOperator as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating FilterOperator objects from a passed enum value.
     */
    public static FilterOperator of(Value value) {
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
        return of(Value.from(value));
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
        return of(Value.from(value));
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
         *     An enum constant for FilterOperator
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public FilterOperator build() {
            FilterOperator filterOperator = new FilterOperator(this);
            if (validating) {
                validate(filterOperator);
            }
            return filterOperator;
        }

        protected void validate(FilterOperator filterOperator) {
            super.validate(filterOperator);
        }

        protected Builder from(FilterOperator filterOperator) {
            super.from(filterOperator);
            return this;
        }
    }

    public enum Value {
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
         * Factory method for creating FilterOperator.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding FilterOperator.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "=":
                return EQUALS;
            case "is-a":
                return IS_A;
            case "descendent-of":
                return DESCENDENT_OF;
            case "is-not-a":
                return IS_NOT_A;
            case "regex":
                return REGEX;
            case "in":
                return IN;
            case "not-in":
                return NOT_IN;
            case "generalizes":
                return GENERALIZES;
            case "exists":
                return EXISTS;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
