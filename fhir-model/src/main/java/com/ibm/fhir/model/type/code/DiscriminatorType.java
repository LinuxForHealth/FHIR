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
    public static final DiscriminatorType VALUE = DiscriminatorType.builder().value(ValueSet.VALUE).build();

    /**
     * Exists
     * 
     * <p>The slices are differentiated by the presence or absence of the nominated element.
     */
    public static final DiscriminatorType EXISTS = DiscriminatorType.builder().value(ValueSet.EXISTS).build();

    /**
     * Pattern
     * 
     * <p>The slices have different values in the nominated element, as determined by testing them against the applicable 
     * ElementDefinition.pattern[x].
     */
    public static final DiscriminatorType PATTERN = DiscriminatorType.builder().value(ValueSet.PATTERN).build();

    /**
     * Type
     * 
     * <p>The slices are differentiated by type of the nominated element.
     */
    public static final DiscriminatorType TYPE = DiscriminatorType.builder().value(ValueSet.TYPE).build();

    /**
     * Profile
     * 
     * <p>The slices are differentiated by conformance of the nominated element to a specified profile. Note that if the path 
     * specifies .resolve() then the profile is the target profile on the reference. In this case, validation by the possible 
     * profiles is required to differentiate the slices.
     */
    public static final DiscriminatorType PROFILE = DiscriminatorType.builder().value(ValueSet.PROFILE).build();

    private volatile int hashCode;

    private DiscriminatorType(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating DiscriminatorType objects from a passed enum value.
     */
    public static DiscriminatorType of(ValueSet value) {
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
        return of(ValueSet.from(value));
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
        return of(ValueSet.from(value));
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
        public DiscriminatorType build() {
            return new DiscriminatorType(this);
        }
    }

    public enum ValueSet {
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
         * Factory method for creating DiscriminatorType.ValueSet values from a passed string value.
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
