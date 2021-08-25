/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other 
 * than single spaces in the contents
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Code extends String {
    protected Code(Builder builder) {
        super(builder);
    }

    @Override
    public boolean hasValue() {
        return (value != null);
    }

    /**
     * Factory method for creating Code objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String, not null
     */
    public static Code of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Code.builder().value(value).build();
    }

    /**
     * Factory method for creating Code objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String, not null
     */
    public static String string(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Code.builder().value(value).build();
    }

    /**
     * Factory method for creating Code objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String that can be parsed into a valid FHIR code value, not null
     */
    public static Code code(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Code.builder().value(value).build();
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
        Code other = (Code) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(value, other.value);
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

    public static class Builder extends String.Builder {
        protected Builder() {
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
         * Primitive value for code
         * 
         * @param value
         *     Primitive value for code
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder value(java.lang.String value) {
            return (Builder) super.value(value);
        }

        /**
         * Build the {@link Code}
         * 
         * @return
         *     An immutable object of type {@link Code}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Code per the base specification
         */
        @Override
        public Code build() {
            Code code = new Code(this);
            if (validating) {
                validate(code);
            }
            return code;
        }

        protected void validate(Code code) {
            super.validate(code);
            ValidationSupport.checkCode(code.value);
        }

        protected Builder from(Code code) {
            super.from(code);
            return this;
        }
    }
}
