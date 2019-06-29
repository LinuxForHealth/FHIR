/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;

/**
 * <p>
 * An integer with a value that is positive (e.g. &gt;0)
 * </p>
 */
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class PositiveInt extends Integer {
    private static final int MIN_VALUE = 1;

    private PositiveInt(Builder builder) {
        super(builder);
        ValidationSupport.checkValue(value, MIN_VALUE);
    }

    public static PositiveInt of(java.lang.Integer value) {
        return PositiveInt.builder().value(value).build();
    }

    public static PositiveInt of(java.lang.String value) {
        return PositiveInt.builder().value(value).build();
    }

    public static Integer integer(java.lang.String value) {
        return PositiveInt.builder().value(value).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Integer.Builder {
        private Builder() {
            super();
        }

        /**
         * <p>
         * unique id for the element within a resource (for internal references)
         * </p>
         * 
         * @param id
         *     xml:id (or equivalent in JSON)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * Primitive value for positiveInt
         * </p>
         * 
         * @param value
         *     Primitive value for positiveInt
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder value(java.lang.Integer value) {
            return (Builder) super.value(value);
        }

        public Builder value(java.lang.String value) {
            return (Builder) super.value(value);
        }

        @Override
        public PositiveInt build() {
            return new PositiveInt(this);
        }

        private Builder from(PositiveInt positiveInt) {
            id = positiveInt.id;
            extension.addAll(positiveInt.extension);
            value = positiveInt.value;
            return this;
        }
    }
}
