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

@System("http://hl7.org/fhir/structure-definition-kind")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class StructureDefinitionKind extends Code {
    /**
     * Primitive Data Type
     * 
     * <p>A primitive type that has a value and an extension. These can be used throughout complex datatype, Resource and 
     * extension definitions. Only the base specification can define primitive types.
     */
    public static final StructureDefinitionKind PRIMITIVE_TYPE = StructureDefinitionKind.builder().value(Value.PRIMITIVE_TYPE).build();

    /**
     * Complex Data Type
     * 
     * <p>A complex structure that defines a set of data elements that is suitable for use in 'resources'. The base 
     * specification defines a number of complex types, and other specifications can define additional types. These 
     * structures do not have a maintained identity.
     */
    public static final StructureDefinitionKind COMPLEX_TYPE = StructureDefinitionKind.builder().value(Value.COMPLEX_TYPE).build();

    /**
     * Resource
     * 
     * <p>A 'resource' - a directed acyclic graph of elements that aggregrates other types into an identifiable entity. The 
     * base FHIR resources are defined by the FHIR specification itself but other 'resources' can be defined in additional 
     * specifications (though these will not be recognised as 'resources' by the FHIR specification (i.e. they do not get end-
     * points etc, or act as the targets of references in FHIR defined resources - though other specificatiosn can treat them 
     * this way).
     */
    public static final StructureDefinitionKind RESOURCE = StructureDefinitionKind.builder().value(Value.RESOURCE).build();

    /**
     * Logical
     * 
     * <p>A pattern or a template that is not intended to be a real resource or complex type.
     */
    public static final StructureDefinitionKind LOGICAL = StructureDefinitionKind.builder().value(Value.LOGICAL).build();

    private volatile int hashCode;

    private StructureDefinitionKind(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this StructureDefinitionKind as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating StructureDefinitionKind objects from a passed enum value.
     */
    public static StructureDefinitionKind of(Value value) {
        switch (value) {
        case PRIMITIVE_TYPE:
            return PRIMITIVE_TYPE;
        case COMPLEX_TYPE:
            return COMPLEX_TYPE;
        case RESOURCE:
            return RESOURCE;
        case LOGICAL:
            return LOGICAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating StructureDefinitionKind objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static StructureDefinitionKind of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating StructureDefinitionKind objects from a passed string value.
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
     * Inherited factory method for creating StructureDefinitionKind objects from a passed string value.
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
        StructureDefinitionKind other = (StructureDefinitionKind) obj;
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
         *     An enum constant for StructureDefinitionKind
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public StructureDefinitionKind build() {
            StructureDefinitionKind structureDefinitionKind = new StructureDefinitionKind(this);
            if (validating) {
                validate(structureDefinitionKind);
            }
            return structureDefinitionKind;
        }

        protected void validate(StructureDefinitionKind structureDefinitionKind) {
            super.validate(structureDefinitionKind);
        }

        protected Builder from(StructureDefinitionKind structureDefinitionKind) {
            super.from(structureDefinitionKind);
            return this;
        }
    }

    public enum Value {
        /**
         * Primitive Data Type
         * 
         * <p>A primitive type that has a value and an extension. These can be used throughout complex datatype, Resource and 
         * extension definitions. Only the base specification can define primitive types.
         */
        PRIMITIVE_TYPE("primitive-type"),

        /**
         * Complex Data Type
         * 
         * <p>A complex structure that defines a set of data elements that is suitable for use in 'resources'. The base 
         * specification defines a number of complex types, and other specifications can define additional types. These 
         * structures do not have a maintained identity.
         */
        COMPLEX_TYPE("complex-type"),

        /**
         * Resource
         * 
         * <p>A 'resource' - a directed acyclic graph of elements that aggregrates other types into an identifiable entity. The 
         * base FHIR resources are defined by the FHIR specification itself but other 'resources' can be defined in additional 
         * specifications (though these will not be recognised as 'resources' by the FHIR specification (i.e. they do not get end-
         * points etc, or act as the targets of references in FHIR defined resources - though other specificatiosn can treat them 
         * this way).
         */
        RESOURCE("resource"),

        /**
         * Logical
         * 
         * <p>A pattern or a template that is not intended to be a real resource or complex type.
         */
        LOGICAL("logical");

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
         * Factory method for creating StructureDefinitionKind.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding StructureDefinitionKind.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "primitive-type":
                return PRIMITIVE_TYPE;
            case "complex-type":
                return COMPLEX_TYPE;
            case "resource":
                return RESOURCE;
            case "logical":
                return LOGICAL;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
