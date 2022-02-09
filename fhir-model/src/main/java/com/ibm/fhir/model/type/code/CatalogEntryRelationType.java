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

@System("http://hl7.org/fhir/relation-type")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CatalogEntryRelationType extends Code {
    /**
     * Triggers
     * 
     * <p>the related entry represents an activity that may be triggered by the current item.
     */
    public static final CatalogEntryRelationType TRIGGERS = CatalogEntryRelationType.builder().value(Value.TRIGGERS).build();

    /**
     * Replaced By
     * 
     * <p>the related entry represents an item that replaces the current retired item.
     */
    public static final CatalogEntryRelationType IS_REPLACED_BY = CatalogEntryRelationType.builder().value(Value.IS_REPLACED_BY).build();

    private volatile int hashCode;

    private CatalogEntryRelationType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this CatalogEntryRelationType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating CatalogEntryRelationType objects from a passed enum value.
     */
    public static CatalogEntryRelationType of(Value value) {
        switch (value) {
        case TRIGGERS:
            return TRIGGERS;
        case IS_REPLACED_BY:
            return IS_REPLACED_BY;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating CatalogEntryRelationType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static CatalogEntryRelationType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating CatalogEntryRelationType objects from a passed string value.
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
     * Inherited factory method for creating CatalogEntryRelationType objects from a passed string value.
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
        CatalogEntryRelationType other = (CatalogEntryRelationType) obj;
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
         *     An enum constant for CatalogEntryRelationType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public CatalogEntryRelationType build() {
            CatalogEntryRelationType catalogEntryRelationType = new CatalogEntryRelationType(this);
            if (validating) {
                validate(catalogEntryRelationType);
            }
            return catalogEntryRelationType;
        }

        protected void validate(CatalogEntryRelationType catalogEntryRelationType) {
            super.validate(catalogEntryRelationType);
        }

        protected Builder from(CatalogEntryRelationType catalogEntryRelationType) {
            super.from(catalogEntryRelationType);
            return this;
        }
    }

    public enum Value {
        /**
         * Triggers
         * 
         * <p>the related entry represents an activity that may be triggered by the current item.
         */
        TRIGGERS("triggers"),

        /**
         * Replaced By
         * 
         * <p>the related entry represents an item that replaces the current retired item.
         */
        IS_REPLACED_BY("is-replaced-by");

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
         * Factory method for creating CatalogEntryRelationType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding CatalogEntryRelationType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "triggers":
                return TRIGGERS;
            case "is-replaced-by":
                return IS_REPLACED_BY;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
