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

@System("http://hl7.org/fhir/allergy-intolerance-category")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class AllergyIntoleranceCategory extends Code {
    /**
     * Food
     * 
     * <p>Any substance consumed to provide nutritional support for the body.
     */
    public static final AllergyIntoleranceCategory FOOD = AllergyIntoleranceCategory.builder().value(ValueSet.FOOD).build();

    /**
     * Medication
     * 
     * <p>Substances administered to achieve a physiological effect.
     */
    public static final AllergyIntoleranceCategory MEDICATION = AllergyIntoleranceCategory.builder().value(ValueSet.MEDICATION).build();

    /**
     * Environment
     * 
     * <p>Any substances that are encountered in the environment, including any substance not already classified as food, 
     * medication, or biologic.
     */
    public static final AllergyIntoleranceCategory ENVIRONMENT = AllergyIntoleranceCategory.builder().value(ValueSet.ENVIRONMENT).build();

    /**
     * Biologic
     * 
     * <p>A preparation that is synthesized from living organisms or their products, especially a human or animal protein, 
     * such as a hormone or antitoxin, that is used as a diagnostic, preventive, or therapeutic agent. Examples of biologic 
     * medications include: vaccines; allergenic extracts, which are used for both diagnosis and treatment (for example, 
     * allergy shots); gene therapies; cellular therapies. There are other biologic products, such as tissues, which are not 
     * typically associated with allergies.
     */
    public static final AllergyIntoleranceCategory BIOLOGIC = AllergyIntoleranceCategory.builder().value(ValueSet.BIOLOGIC).build();

    private volatile int hashCode;

    private AllergyIntoleranceCategory(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating AllergyIntoleranceCategory objects from a passed enum value.
     */
    public static AllergyIntoleranceCategory of(ValueSet value) {
        switch (value) {
        case FOOD:
            return FOOD;
        case MEDICATION:
            return MEDICATION;
        case ENVIRONMENT:
            return ENVIRONMENT;
        case BIOLOGIC:
            return BIOLOGIC;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating AllergyIntoleranceCategory objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static AllergyIntoleranceCategory of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating AllergyIntoleranceCategory objects from a passed string value.
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
     * Inherited factory method for creating AllergyIntoleranceCategory objects from a passed string value.
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
        AllergyIntoleranceCategory other = (AllergyIntoleranceCategory) obj;
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
        public AllergyIntoleranceCategory build() {
            return new AllergyIntoleranceCategory(this);
        }
    }

    public enum ValueSet {
        /**
         * Food
         * 
         * <p>Any substance consumed to provide nutritional support for the body.
         */
        FOOD("food"),

        /**
         * Medication
         * 
         * <p>Substances administered to achieve a physiological effect.
         */
        MEDICATION("medication"),

        /**
         * Environment
         * 
         * <p>Any substances that are encountered in the environment, including any substance not already classified as food, 
         * medication, or biologic.
         */
        ENVIRONMENT("environment"),

        /**
         * Biologic
         * 
         * <p>A preparation that is synthesized from living organisms or their products, especially a human or animal protein, 
         * such as a hormone or antitoxin, that is used as a diagnostic, preventive, or therapeutic agent. Examples of biologic 
         * medications include: vaccines; allergenic extracts, which are used for both diagnosis and treatment (for example, 
         * allergy shots); gene therapies; cellular therapies. There are other biologic products, such as tissues, which are not 
         * typically associated with allergies.
         */
        BIOLOGIC("biologic");

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
         * Factory method for creating AllergyIntoleranceCategory.ValueSet values from a passed string value.
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
