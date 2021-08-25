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
 * Value of "true" or "false"
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Boolean extends Element {
    public static final Boolean TRUE = Boolean.of(true);
    public static final Boolean FALSE = Boolean.of(false);

    private final java.lang.Boolean value;

    private Boolean(Builder builder) {
        super(builder);
        value = builder.value;
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.lang.Boolean} that may be null.
     */
    public java.lang.Boolean getValue() {
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
     * Factory method for creating Boolean objects from a java.lang.Boolean
     * 
     * @param value
     *     A java.lang.Boolean, not null
     */
    public static Boolean of(java.lang.Boolean value) {
        Objects.requireNonNull(value, "value");
        return Boolean.builder().value(value).build();
    }

    /**
     * Factory method for creating Boolean objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed into a java.lang.Boolean, not null
     */
    public static Boolean of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Boolean.builder().value(value).build();
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
        Boolean other = (Boolean) obj;
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
        private java.lang.Boolean value;

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
         * The actual value
         * 
         * @param value
         *     Primitive value for boolean
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(java.lang.Boolean value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = java.lang.Boolean.parseBoolean(value);
            return this;
        }

        /**
         * Build the {@link Boolean}
         * 
         * @return
         *     An immutable object of type {@link Boolean}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Boolean per the base specification
         */
        @Override
        public Boolean build() {
            Boolean _boolean = new Boolean(this);
            if (validating) {
                validate(_boolean);
            }
            return _boolean;
        }

        protected void validate(Boolean _boolean) {
            super.validate(_boolean);
            ValidationSupport.requireValueOrChildren(_boolean);
        }

        protected Builder from(Boolean _boolean) {
            super.from(_boolean);
            value = _boolean.value;
            return this;
        }
    }
}
