/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A relationship of two Quantity values - expressed as a numerator and a denominator.
 * </p>
 */
@Constraint(
    id = "rat-1",
    level = "Rule",
    location = "(base)",
    description = "Numerator and denominator SHALL both be present, or both are absent. If both are absent, there SHALL be some extension present",
    expression = "(numerator.empty() xor denominator.exists()) and (numerator.exists() or extension.exists())"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Ratio extends Element {
    private final Quantity numerator;
    private final Quantity denominator;

    private Ratio(Builder builder) {
        super(builder);
        this.numerator = builder.numerator;
        this.denominator = builder.denominator;
    }

    /**
     * <p>
     * The value of the numerator.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getNumerator() {
        return numerator;
    }

    /**
     * <p>
     * The value of the denominator.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getDenominator() {
        return denominator;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(numerator, "numerator", visitor);
                accept(denominator, "denominator", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.numerator = numerator;
        builder.denominator = denominator;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        // optional
        private Quantity numerator;
        private Quantity denominator;

        private Builder() {
            super();
        }

        /**
         * <p>
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * </p>
         * 
         * @param id
         *     Unique id for inter-element referencing
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
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
         * The value of the numerator.
         * </p>
         * 
         * @param numerator
         *     Numerator value
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder numerator(Quantity numerator) {
            this.numerator = numerator;
            return this;
        }

        /**
         * <p>
         * The value of the denominator.
         * </p>
         * 
         * @param denominator
         *     Denominator value
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder denominator(Quantity denominator) {
            this.denominator = denominator;
            return this;
        }

        @Override
        public Ratio build() {
            return new Ratio(this);
        }
    }
}
