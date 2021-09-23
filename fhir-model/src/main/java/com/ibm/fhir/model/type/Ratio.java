/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A relationship of two Quantity values - expressed as a numerator and a denominator.
 */
@Constraint(
    id = "rat-1",
    level = "Rule",
    location = "(base)",
    description = "Numerator and denominator SHALL both be present, or both are absent. If both are absent, there SHALL be some extension present",
    expression = "(numerator.empty() xor denominator.exists()) and (numerator.exists() or extension.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/Ratio"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Ratio extends Element {
    @Summary
    private final Quantity numerator;
    @Summary
    private final Quantity denominator;

    private Ratio(Builder builder) {
        super(builder);
        numerator = builder.numerator;
        denominator = builder.denominator;
    }

    /**
     * The value of the numerator.
     * 
     * @return
     *     An immutable object of type {@link Quantity} that may be null.
     */
    public Quantity getNumerator() {
        return numerator;
    }

    /**
     * The value of the denominator.
     * 
     * @return
     *     An immutable object of type {@link Quantity} that may be null.
     */
    public Quantity getDenominator() {
        return denominator;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (numerator != null) || 
            (denominator != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(numerator, "numerator", visitor);
                accept(denominator, "denominator", visitor);
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
        Ratio other = (Ratio) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(numerator, other.numerator) && 
            Objects.equals(denominator, other.denominator);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                numerator, 
                denominator);
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
        private Quantity numerator;
        private Quantity denominator;

        private Builder() {
            super();
        }

        /**
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * 
         * @param id
         *     Unique id for inter-element referencing
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
         * The value of the numerator.
         * 
         * @param numerator
         *     Numerator value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder numerator(Quantity numerator) {
            this.numerator = numerator;
            return this;
        }

        /**
         * The value of the denominator.
         * 
         * @param denominator
         *     Denominator value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder denominator(Quantity denominator) {
            this.denominator = denominator;
            return this;
        }

        /**
         * Build the {@link Ratio}
         * 
         * @return
         *     An immutable object of type {@link Ratio}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Ratio per the base specification
         */
        @Override
        public Ratio build() {
            Ratio ratio = new Ratio(this);
            if (validating) {
                validate(ratio);
            }
            return ratio;
        }

        protected void validate(Ratio ratio) {
            super.validate(ratio);
            ValidationSupport.requireValueOrChildren(ratio);
        }

        protected Builder from(Ratio ratio) {
            super.from(ratio);
            numerator = ratio.numerator;
            denominator = ratio.denominator;
            return this;
        }
    }
}
