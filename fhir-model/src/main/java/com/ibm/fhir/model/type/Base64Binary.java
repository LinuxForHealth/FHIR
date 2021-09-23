/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A stream of bytes
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Base64Binary extends Element {
    private final byte[] value;

    private Base64Binary(Builder builder) {
        super(builder);
        value = builder.value;
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.lang.byte[]} that may be null.
     */
    public byte[] getValue() {
        return value;
    }

    @Override
    public boolean hasValue() {
        return (value != null);
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren();
    }

    /**
     * Factory method for creating Base64Binary objects from a byte array; this array should be the actual value.
     * 
     * @param value
     *     The byte array of to-be-encoded content, not null
     */
    public static Base64Binary of(byte[] value) {
        Objects.requireNonNull(value, "value");
        return Base64Binary.builder().value(value).build();
    }

    /**
     * Factory method for creating Base64Binary objects from a Base64 encoded value.
     * 
     * @param value
     *     The Base64 encoded string, not null
     */
    public static Base64Binary of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Base64Binary.builder().value(value).build();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(value, "value", visitor);
            }
            visitor.visitEnd(elementName, elementIndex, this);
            visitor.postVisit(this);
        }
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
        Base64Binary other = (Base64Binary) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Arrays.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                value);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        private byte[] value;

        private Builder() {
            super();
        }

        /**
         * unique id for the element within a resource (for internal references)
         * 
         * @param id
         *     xml:id (or equivalent in JSON)
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * The byte array of the actual value
         * 
         * @param value
         *     The byte array of the actual value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(byte[] value) {
            this.value = value;
            return this;
        }

        /**
         * The base64 encoded value.
         * 
         * @param value
         *     The base64 encoded string
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(java.lang.String value) {
            Objects.requireNonNull(value);
            java.lang.String valueNoWhitespace = value.replaceAll("\\s", "");
            ValidationSupport.validateBase64EncodedString(valueNoWhitespace);
            this.value = Base64.getDecoder().decode(valueNoWhitespace);
            return this;
        }

        /**
         * Build the {@link Base64Binary}
         * 
         * @return
         *     An immutable object of type {@link Base64Binary}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Base64Binary per the base specification
         */
        @Override
        public Base64Binary build() {
            Base64Binary base64Binary = new Base64Binary(this);
            if (validating) {
                validate(base64Binary);
            }
            return base64Binary;
        }

        protected void validate(Base64Binary base64Binary) {
            super.validate(base64Binary);
            ValidationSupport.requireValueOrChildren(base64Binary);
        }

        protected Builder from(Base64Binary base64Binary) {
            super.from(base64Binary);
            value = base64Binary.value;
            return this;
        }
    }
}
