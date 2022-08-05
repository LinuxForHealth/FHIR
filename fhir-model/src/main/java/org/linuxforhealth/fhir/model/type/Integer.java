/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A whole number
 */
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class Integer extends Element {
    protected final java.lang.Integer value;

    protected Integer(Builder builder) {
        super(builder);
        value = builder.value;
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.lang.Integer} that may be null.
     */
    public java.lang.Integer getValue() {
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
     * Factory method for creating Integer objects from a java.lang.Integer
     * 
     * @param value
     *     A java.lang.Integer, not null
     */
    public static Integer of(java.lang.Integer value) {
        Objects.requireNonNull(value, "value");
        return Integer.builder().value(value).build();
    }

    /**
     * Factory method for creating Integer objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed into a java.lang.Integer, not null
     */
    public static Integer of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Integer.builder().value(value).build();
    }

    /**
     * Factory method for creating Integer objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String that can be parsed into a java.lang.Integer, not null
     */
    public static Integer integer(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Integer.builder().value(value).build();
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
        Integer other = (Integer) obj;
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

    public static class Builder extends Element.Builder {
        protected java.lang.Integer value;

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
         * The actual value
         * 
         * @param value
         *     Primitive value for integer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(java.lang.Integer value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = java.lang.Integer.parseInt(value);
            return this;
        }

        /**
         * Build the {@link Integer}
         * 
         * @return
         *     An immutable object of type {@link Integer}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Integer per the base specification
         */
        @Override
        public Integer build() {
            Integer integer = new Integer(this);
            if (validating) {
                validate(integer);
            }
            return integer;
        }

        protected void validate(Integer integer) {
            super.validate(integer);
            ValidationSupport.requireValueOrChildren(integer);
        }

        protected Builder from(Integer integer) {
            super.from(integer);
            value = integer.value;
            return this;
        }
    }
}
