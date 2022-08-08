/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A rational number with implicit precision
 */
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class Decimal extends Element {
    private final BigDecimal value;

    private Decimal(Builder builder) {
        super(builder);
        value = builder.value;
    }

    /**
     * The actual value
     * 
     * @return
     *     An immutable object of type {@link java.math.BigDecimal} that may be null.
     */
    public BigDecimal getValue() {
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
     * Factory method for creating Decimal objects from a BigDecimal
     * 
     * @param value
     *     A BigDecimal, not null
     */
    public static Decimal of(BigDecimal value) {
        Objects.requireNonNull(value, "value");
        return Decimal.builder().value(value).build();
    }

    /**
     * Factory method for creating Decimal objects from a Number
     * 
     * @param value
     *     A Number, not null
     */
    public static Decimal of(Number value) {
        Objects.requireNonNull(value, "value");
        return Decimal.builder().value(value.toString()).build();
    }

    /**
     * Factory method for creating Decimal objects from a java.lang.String
     * 
     * @param value
     *     A java.lang.String value that can be parsed into a BigDecimal, not null
     */
    public static Decimal of(java.lang.String value) {
        Objects.requireNonNull(value, "value");
        return Decimal.builder().value(value).build();
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
        Decimal other = (Decimal) obj;
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
        private BigDecimal value;

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
         *     Primitive value for decimal
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(BigDecimal value) {
            this.value = value;
            return this;
        }

        public Builder value(java.lang.String value) {
            this.value = new BigDecimal(value);
            return this;
        }

        /**
         * Build the {@link Decimal}
         * 
         * @return
         *     An immutable object of type {@link Decimal}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Decimal per the base specification
         */
        @Override
        public Decimal build() {
            Decimal decimal = new Decimal(this);
            if (validating) {
                validate(decimal);
            }
            return decimal;
        }

        protected void validate(Decimal decimal) {
            super.validate(decimal);
            ValidationSupport.requireValueOrChildren(decimal);
        }

        protected Builder from(Decimal decimal) {
            super.from(decimal);
            value = decimal.value;
            return this;
        }
    }
}
