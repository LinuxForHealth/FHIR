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

@System("http://hl7.org/fhir/identifier-use")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class IdentifierUse extends Code {
    /**
     * Usual
     * 
     * <p>The identifier recommended for display and use in real-world interactions.
     */
    public static final IdentifierUse USUAL = IdentifierUse.builder().value(Value.USUAL).build();

    /**
     * Official
     * 
     * <p>The identifier considered to be most trusted for the identification of this item. Sometimes also known as "primary" 
     * and "main". The determination of "official" is subjective and implementation guides often provide additional 
     * guidelines for use.
     */
    public static final IdentifierUse OFFICIAL = IdentifierUse.builder().value(Value.OFFICIAL).build();

    /**
     * Temp
     * 
     * <p>A temporary identifier.
     */
    public static final IdentifierUse TEMP = IdentifierUse.builder().value(Value.TEMP).build();

    /**
     * Secondary
     * 
     * <p>An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but 
     * cannot be consistently assigned to the same object again in a different context.
     */
    public static final IdentifierUse SECONDARY = IdentifierUse.builder().value(Value.SECONDARY).build();

    /**
     * Old
     * 
     * <p>The identifier id no longer considered valid, but may be relevant for search purposes. E.g. Changes to identifier 
     * schemes, account merges, etc.
     */
    public static final IdentifierUse OLD = IdentifierUse.builder().value(Value.OLD).build();

    private volatile int hashCode;

    private IdentifierUse(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this IdentifierUse as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating IdentifierUse objects from a passed enum value.
     */
    public static IdentifierUse of(Value value) {
        switch (value) {
        case USUAL:
            return USUAL;
        case OFFICIAL:
            return OFFICIAL;
        case TEMP:
            return TEMP;
        case SECONDARY:
            return SECONDARY;
        case OLD:
            return OLD;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating IdentifierUse objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static IdentifierUse of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating IdentifierUse objects from a passed string value.
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
     * Inherited factory method for creating IdentifierUse objects from a passed string value.
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
        IdentifierUse other = (IdentifierUse) obj;
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
         *     An enum constant for IdentifierUse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public IdentifierUse build() {
            IdentifierUse identifierUse = new IdentifierUse(this);
            if (validating) {
                validate(identifierUse);
            }
            return identifierUse;
        }

        protected void validate(IdentifierUse identifierUse) {
            super.validate(identifierUse);
        }

        protected Builder from(IdentifierUse identifierUse) {
            super.from(identifierUse);
            return this;
        }
    }

    public enum Value {
        /**
         * Usual
         * 
         * <p>The identifier recommended for display and use in real-world interactions.
         */
        USUAL("usual"),

        /**
         * Official
         * 
         * <p>The identifier considered to be most trusted for the identification of this item. Sometimes also known as "primary" 
         * and "main". The determination of "official" is subjective and implementation guides often provide additional 
         * guidelines for use.
         */
        OFFICIAL("official"),

        /**
         * Temp
         * 
         * <p>A temporary identifier.
         */
        TEMP("temp"),

        /**
         * Secondary
         * 
         * <p>An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but 
         * cannot be consistently assigned to the same object again in a different context.
         */
        SECONDARY("secondary"),

        /**
         * Old
         * 
         * <p>The identifier id no longer considered valid, but may be relevant for search purposes. E.g. Changes to identifier 
         * schemes, account merges, etc.
         */
        OLD("old");

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
         * Factory method for creating IdentifierUse.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding IdentifierUse.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "usual":
                return USUAL;
            case "official":
                return OFFICIAL;
            case "temp":
                return TEMP;
            case "secondary":
                return SECONDARY;
            case "old":
                return OLD;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
