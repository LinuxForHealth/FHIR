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

@System("http://hl7.org/fhir/extension-context-type")
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class ExtensionContextType extends Code {
    /**
     * FHIRPath
     * 
     * <p>The context is all elements that match the FHIRPath query found in the expression.
     */
    public static final ExtensionContextType FHIRPATH = ExtensionContextType.builder().value(Value.FHIRPATH).build();

    /**
     * Element ID
     * 
     * <p>The context is any element that has an ElementDefinition.id that matches that found in the expression. This 
     * includes ElementDefinition Ids that have slicing identifiers. The full path for the element is [url]#[elementid]. If 
     * there is no #, the Element id is one defined in the base specification.
     */
    public static final ExtensionContextType ELEMENT = ExtensionContextType.builder().value(Value.ELEMENT).build();

    /**
     * Extension URL
     * 
     * <p>The context is a particular extension from a particular StructureDefinition, and the expression is just a uri that 
     * identifies the extension.
     */
    public static final ExtensionContextType EXTENSION = ExtensionContextType.builder().value(Value.EXTENSION).build();

    private volatile int hashCode;

    private ExtensionContextType(Builder builder) {
        super(builder);
    }

    /**
     * Get the value of this ExtensionContextType as an enum constant.
     */
    public Value getValueAsEnum() {
        return (value != null) ? Value.from(value) : null;
    }

    /**
     * Factory method for creating ExtensionContextType objects from a passed enum value.
     */
    public static ExtensionContextType of(Value value) {
        switch (value) {
        case FHIRPATH:
            return FHIRPATH;
        case ELEMENT:
            return ELEMENT;
        case EXTENSION:
            return EXTENSION;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating ExtensionContextType objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static ExtensionContextType of(java.lang.String value) {
        return of(Value.from(value));
    }

    /**
     * Inherited factory method for creating ExtensionContextType objects from a passed string value.
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
     * Inherited factory method for creating ExtensionContextType objects from a passed string value.
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
        ExtensionContextType other = (ExtensionContextType) obj;
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
         *     An enum constant for ExtensionContextType
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Value value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public ExtensionContextType build() {
            ExtensionContextType extensionContextType = new ExtensionContextType(this);
            if (validating) {
                validate(extensionContextType);
            }
            return extensionContextType;
        }

        protected void validate(ExtensionContextType extensionContextType) {
            super.validate(extensionContextType);
        }

        protected Builder from(ExtensionContextType extensionContextType) {
            super.from(extensionContextType);
            return this;
        }
    }

    public enum Value {
        /**
         * FHIRPath
         * 
         * <p>The context is all elements that match the FHIRPath query found in the expression.
         */
        FHIRPATH("fhirpath"),

        /**
         * Element ID
         * 
         * <p>The context is any element that has an ElementDefinition.id that matches that found in the expression. This 
         * includes ElementDefinition Ids that have slicing identifiers. The full path for the element is [url]#[elementid]. If 
         * there is no #, the Element id is one defined in the base specification.
         */
        ELEMENT("element"),

        /**
         * Extension URL
         * 
         * <p>The context is a particular extension from a particular StructureDefinition, and the expression is just a uri that 
         * identifies the extension.
         */
        EXTENSION("extension");

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
         * Factory method for creating ExtensionContextType.Value values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @return
         *     The corresponding ExtensionContextType.Value or null if a null value was passed
         * @throws IllegalArgumentException
         *     If the passed string is not null and cannot be parsed into an allowed code value
         */
        public static Value from(java.lang.String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
            case "fhirpath":
                return FHIRPATH;
            case "element":
                return ELEMENT;
            case "extension":
                return EXTENSION;
            default:
                throw new IllegalArgumentException(value);
            }
        }
    }
}
