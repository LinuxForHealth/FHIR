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

@System("http://hl7.org/fhir/questionnaire-enable-behavior")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EnableWhenBehavior extends Code {
    /**
     * All
     * 
     * <p>Enable the question when all the enableWhen criteria are satisfied.
     */
    public static final EnableWhenBehavior ALL = EnableWhenBehavior.builder().value(Value.ALL).build();

    /**
     * Any
     * 
     * <p>Enable the question when any of the enableWhen criteria are satisfied.
     */
    public static final EnableWhenBehavior ANY = EnableWhenBehavior.builder().value(Value.ANY).build();

    private volatile int hashCode;

    private EnableWhenBehavior(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this EnableWhenBehavior as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating EnableWhenBehavior objects from a passed enum value.
     */
    public static EnableWhenBehavior of(Value value) {
        switch (value) {
        case ALL:
            return ALL;
        case ANY:
            return ANY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating EnableWhenBehavior objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static EnableWhenBehavior of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating EnableWhenBehavior objects from a passed string value.
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
     * Inherited factory method for creating EnableWhenBehavior objects from a passed string value.
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
        EnableWhenBehavior other = (EnableWhenBehavior) obj;
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
         *     An enum constant for EnableWhenBehavior
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public EnableWhenBehavior build() {
            EnableWhenBehavior enableWhenBehavior = new EnableWhenBehavior(this);
            if (validating) {
                validate(enableWhenBehavior);
            }
            return enableWhenBehavior;
        }

        protected void validate(EnableWhenBehavior enableWhenBehavior) {
            super.validate(enableWhenBehavior);
        }

        protected Builder from(EnableWhenBehavior enableWhenBehavior) {
            super.from(enableWhenBehavior);
            return this;
        }
    }

    public enum Value {
        /**
         * All
         * 
         * <p>Enable the question when all the enableWhen criteria are satisfied.
         */
        ALL("all"),

        /**
         * Any
         * 
         * <p>Enable the question when any of the enableWhen criteria are satisfied.
         */
        ANY("any");

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
         * Factory method for creating EnableWhenBehavior.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding EnableWhenBehavior.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "all":
                return ALL;
            case "any":
                return ANY;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
