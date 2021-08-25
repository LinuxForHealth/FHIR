/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical 
 * substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt 
 * (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are 
 * not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceAmount extends BackboneElement {
    @Summary
    @Choice({ Quantity.class, Range.class, String.class })
    private final Element amount;
    @Summary
    private final CodeableConcept amountType;
    @Summary
    private final String amountText;
    @Summary
    private final ReferenceRange referenceRange;

    private SubstanceAmount(Builder builder) {
        super(builder);
        amount = builder.amount;
        amountType = builder.amountType;
        amountText = builder.amountText;
        referenceRange = builder.referenceRange;
    }

    /**
     * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
     * the average. If only a single definite value for a given element is given, it would be captured in this field.
     * 
     * @return
     *     An immutable object of type {@link Quantity}, {@link Range} or {@link String} that may be null.
     */
    public Element getAmount() {
        return amount;
    }

    /**
     * Most elements that require a quantitative value will also have a field called amount type. Amount type should always 
     * be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual 
     * relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole 
     * ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related 
     * definitional elements.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getAmountType() {
        return amountType;
    }

    /**
     * A textual comment on a numeric value.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getAmountText() {
        return amountText;
    }

    /**
     * Reference range of possible or expected values.
     * 
     * @return
     *     An immutable object of type {@link ReferenceRange} that may be null.
     */
    public ReferenceRange getReferenceRange() {
        return referenceRange;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (amount != null) || 
            (amountType != null) || 
            (amountText != null) || 
            (referenceRange != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(amount, "amount", visitor);
                accept(amountType, "amountType", visitor);
                accept(amountText, "amountText", visitor);
                accept(referenceRange, "referenceRange", visitor);
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
        SubstanceAmount other = (SubstanceAmount) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(amount, other.amount) && 
            Objects.equals(amountType, other.amountType) && 
            Objects.equals(amountText, other.amountText) && 
            Objects.equals(referenceRange, other.referenceRange);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                amount, 
                amountType, 
                amountText, 
                referenceRange);
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

    public static class Builder extends BackboneElement.Builder {
        private Element amount;
        private CodeableConcept amountType;
        private String amountText;
        private ReferenceRange referenceRange;

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
         * May be used to represent additional information that is not part of the basic definition of the element and that 
         * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
         * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
         * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
         * extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element and that 
         * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
         * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
         * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
         * extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored even if unrecognized
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * Convenience method for setting {@code amount} with choice type String.
         * 
         * @param amount
         *     Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
         *     the average. If only a single definite value for a given element is given, it would be captured in this field
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #amount(Element)
         */
        public Builder amount(java.lang.String amount) {
            this.amount = (amount == null) ? null : String.of(amount);
            return this;
        }

        /**
         * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
         * the average. If only a single definite value for a given element is given, it would be captured in this field.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Quantity}</li>
         * <li>{@link Range}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * @param amount
         *     Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
         *     the average. If only a single definite value for a given element is given, it would be captured in this field
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder amount(Element amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Most elements that require a quantitative value will also have a field called amount type. Amount type should always 
         * be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual 
         * relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole 
         * ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related 
         * definitional elements.
         * 
         * @param amountType
         *     Most elements that require a quantitative value will also have a field called amount type. Amount type should always 
         *     be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual 
         *     relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole 
         *     ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related 
         *     definitional elements
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder amountType(CodeableConcept amountType) {
            this.amountType = amountType;
            return this;
        }

        /**
         * Convenience method for setting {@code amountText}.
         * 
         * @param amountText
         *     A textual comment on a numeric value
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #amountText(com.ibm.fhir.model.type.String)
         */
        public Builder amountText(java.lang.String amountText) {
            this.amountText = (amountText == null) ? null : String.of(amountText);
            return this;
        }

        /**
         * A textual comment on a numeric value.
         * 
         * @param amountText
         *     A textual comment on a numeric value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder amountText(String amountText) {
            this.amountText = amountText;
            return this;
        }

        /**
         * Reference range of possible or expected values.
         * 
         * @param referenceRange
         *     Reference range of possible or expected values
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referenceRange(ReferenceRange referenceRange) {
            this.referenceRange = referenceRange;
            return this;
        }

        /**
         * Build the {@link SubstanceAmount}
         * 
         * @return
         *     An immutable object of type {@link SubstanceAmount}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceAmount per the base specification
         */
        @Override
        public SubstanceAmount build() {
            SubstanceAmount substanceAmount = new SubstanceAmount(this);
            if (validating) {
                validate(substanceAmount);
            }
            return substanceAmount;
        }

        protected void validate(SubstanceAmount substanceAmount) {
            super.validate(substanceAmount);
            ValidationSupport.choiceElement(substanceAmount.amount, "amount", Quantity.class, Range.class, String.class);
            ValidationSupport.requireValueOrChildren(substanceAmount);
        }

        protected Builder from(SubstanceAmount substanceAmount) {
            super.from(substanceAmount);
            amount = substanceAmount.amount;
            amountType = substanceAmount.amountType;
            amountText = substanceAmount.amountText;
            referenceRange = substanceAmount.referenceRange;
            return this;
        }
    }

    /**
     * Reference range of possible or expected values.
     */
    public static class ReferenceRange extends BackboneElement {
        @Summary
        private final Quantity lowLimit;
        @Summary
        private final Quantity highLimit;

        private ReferenceRange(Builder builder) {
            super(builder);
            lowLimit = builder.lowLimit;
            highLimit = builder.highLimit;
        }

        /**
         * Lower limit possible or expected.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getLowLimit() {
            return lowLimit;
        }

        /**
         * Upper limit possible or expected.
         * 
         * @return
         *     An immutable object of type {@link Quantity} that may be null.
         */
        public Quantity getHighLimit() {
            return highLimit;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (lowLimit != null) || 
                (highLimit != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(lowLimit, "lowLimit", visitor);
                    accept(highLimit, "highLimit", visitor);
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
            ReferenceRange other = (ReferenceRange) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(lowLimit, other.lowLimit) && 
                Objects.equals(highLimit, other.highLimit);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    lowLimit, 
                    highLimit);
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

        public static class Builder extends BackboneElement.Builder {
            private Quantity lowLimit;
            private Quantity highLimit;

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Lower limit possible or expected.
             * 
             * @param lowLimit
             *     Lower limit possible or expected
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder lowLimit(Quantity lowLimit) {
                this.lowLimit = lowLimit;
                return this;
            }

            /**
             * Upper limit possible or expected.
             * 
             * @param highLimit
             *     Upper limit possible or expected
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder highLimit(Quantity highLimit) {
                this.highLimit = highLimit;
                return this;
            }

            /**
             * Build the {@link ReferenceRange}
             * 
             * @return
             *     An immutable object of type {@link ReferenceRange}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid ReferenceRange per the base specification
             */
            @Override
            public ReferenceRange build() {
                ReferenceRange referenceRange = new ReferenceRange(this);
                if (validating) {
                    validate(referenceRange);
                }
                return referenceRange;
            }

            protected void validate(ReferenceRange referenceRange) {
                super.validate(referenceRange);
                ValidationSupport.requireValueOrChildren(referenceRange);
            }

            protected Builder from(ReferenceRange referenceRange) {
                super.from(referenceRange);
                lowLimit = referenceRange.lowLimit;
                highLimit = referenceRange.highLimit;
                return this;
            }
        }
    }
}
