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

@System("http://hl7.org/fhir/concept-property-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class PropertyType extends Code {
    /**
     * code (internal reference)
     * 
     * <p>The property value is a code that identifies a concept defined in the code system.
     */
    public static final PropertyType CODE = PropertyType.builder().value(ValueSet.CODE).build();

    /**
     * Coding (external reference)
     * 
     * <p>The property value is a code defined in an external code system. This may be used for translations, but is not the 
     * intent.
     */
    public static final PropertyType CODING = PropertyType.builder().value(ValueSet.CODING).build();

    /**
     * string
     * 
     * <p>The property value is a string.
     */
    public static final PropertyType STRING = PropertyType.builder().value(ValueSet.STRING).build();

    /**
     * integer
     * 
     * <p>The property value is a string (often used to assign ranking values to concepts for supporting score assessments).
     */
    public static final PropertyType INTEGER = PropertyType.builder().value(ValueSet.INTEGER).build();

    /**
     * boolean
     * 
     * <p>The property value is a boolean true | false.
     */
    public static final PropertyType BOOLEAN = PropertyType.builder().value(ValueSet.BOOLEAN).build();

    /**
     * dateTime
     * 
     * <p>The property is a date or a date + time.
     */
    public static final PropertyType DATE_TIME = PropertyType.builder().value(ValueSet.DATE_TIME).build();

    /**
     * decimal
     * 
     * <p>The property value is a decimal number.
     */
    public static final PropertyType DECIMAL = PropertyType.builder().value(ValueSet.DECIMAL).build();

    private volatile int hashCode;

    private PropertyType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating PropertyType objects from a passed enum value.
     */
    public static PropertyType of(ValueSet value) {
        switch (value) {
        case CODE:
            return CODE;
        case CODING:
            return CODING;
        case STRING:
            return STRING;
        case INTEGER:
            return INTEGER;
        case BOOLEAN:
            return BOOLEAN;
        case DATE_TIME:
            return DATE_TIME;
        case DECIMAL:
            return DECIMAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating PropertyType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static PropertyType of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating PropertyType objects from a passed string value.
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
     * Inherited factory method for creating PropertyType objects from a passed string value.
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
        PropertyType other = (PropertyType) obj;
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
        public PropertyType build() {
            return new PropertyType(this);
        }
    }

    public enum ValueSet {
        /**
         * code (internal reference)
         * 
         * <p>The property value is a code that identifies a concept defined in the code system.
         */
        CODE("code"),

        /**
         * Coding (external reference)
         * 
         * <p>The property value is a code defined in an external code system. This may be used for translations, but is not the 
         * intent.
         */
        CODING("Coding"),

        /**
         * string
         * 
         * <p>The property value is a string.
         */
        STRING("string"),

        /**
         * integer
         * 
         * <p>The property value is a string (often used to assign ranking values to concepts for supporting score assessments).
         */
        INTEGER("integer"),

        /**
         * boolean
         * 
         * <p>The property value is a boolean true | false.
         */
        BOOLEAN("boolean"),

        /**
         * dateTime
         * 
         * <p>The property is a date or a date + time.
         */
        DATE_TIME("dateTime"),

        /**
         * decimal
         * 
         * <p>The property value is a decimal number.
         */
        DECIMAL("decimal");

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
         * Factory method for creating PropertyType.ValueSet values from a passed string value.
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
