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

@System("http://hl7.org/fhir/discriminator-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DiscriminatorType extends Code {
    /**
     * Value
     * 
     * <p>The slices have different values in the nominated element.
     */
    public static final DiscriminatorType VALUE = DiscriminatorType.builder().value(Value.VALUE).build();

    /**
     * Exists
     * 
     * <p>The slices are differentiated by the presence or absence of the nominated element.
     */
    public static final DiscriminatorType EXISTS = DiscriminatorType.builder().value(Value.EXISTS).build();

    /**
     * Pattern
     * 
     * <p>The slices have different values in the nominated element, as determined by testing them against the applicable 
     * ElementDefinition.pattern[x].
     */
    public static final DiscriminatorType PATTERN = DiscriminatorType.builder().value(Value.PATTERN).build();

    /**
     * Type
     * 
     * <p>The slices are differentiated by type of the nominated element.
     */
    public static final DiscriminatorType TYPE = DiscriminatorType.builder().value(Value.TYPE).build();

    /**
     * Profile
     * 
     * <p>The slices are differentiated by conformance of the nominated element to a specified profile. Note that if the path 
     * specifies .resolve() then the profile is the target profile on the reference. In this case, validation by the possible 
     * profiles is required to differentiate the slices.
     */
    public static final DiscriminatorType PROFILE = DiscriminatorType.builder().value(Value.PROFILE).build();

    private volatile int hashCode;

    private DiscriminatorType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this DiscriminatorType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating DiscriminatorType objects from a passed enum value.
     */
    public static DiscriminatorType of(Value value) {
        switch (value) {
        case VALUE:
            return VALUE;
        case EXISTS:
            return EXISTS;
        case PATTERN:
            return PATTERN;
        case TYPE:
            return TYPE;
        case PROFILE:
            return PROFILE;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating DiscriminatorType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static DiscriminatorType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating DiscriminatorType objects from a passed string value.
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
     * Inherited factory method for creating DiscriminatorType objects from a passed string value.
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
        DiscriminatorType other = (DiscriminatorType) obj;
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
         *     An enum constant for DiscriminatorType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public DiscriminatorType build() {
            DiscriminatorType discriminatorType = new DiscriminatorType(this);
            if (validating) {
                validate(discriminatorType);
            }
            return discriminatorType;
        }

        protected void validate(DiscriminatorType discriminatorType) {
            super.validate(discriminatorType);
        }

        protected Builder from(DiscriminatorType discriminatorType) {
            super.from(discriminatorType);
            return this;
        }
    }

    public enum Value {
        /**
         * Value
         * 
         * <p>The slices have different values in the nominated element.
         */
        VALUE("value"),

        /**
         * Exists
         * 
         * <p>The slices are differentiated by the presence or absence of the nominated element.
         */
        EXISTS("exists"),

        /**
         * Pattern
         * 
         * <p>The slices have different values in the nominated element, as determined by testing them against the applicable 
         * ElementDefinition.pattern[x].
         */
        PATTERN("pattern"),

        /**
         * Type
         * 
         * <p>The slices are differentiated by type of the nominated element.
         */
        TYPE("type"),

        /**
         * Profile
         * 
         * <p>The slices are differentiated by conformance of the nominated element to a specified profile. Note that if the path 
         * specifies .resolve() then the profile is the target profile on the reference. In this case, validation by the possible 
         * profiles is required to differentiate the slices.
         */
        PROFILE("profile");

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
         * Factory method for creating DiscriminatorType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding DiscriminatorType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "value":
                return VALUE;
            case "exists":
                return EXISTS;
            case "pattern":
                return PATTERN;
            case "type":
                return TYPE;
            case "profile":
                return PROFILE;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
