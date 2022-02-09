/*
 * (C) Copyright IBM Corp. 2019, 2022
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
 * A range of ratios expressed as a low and high numerator and a denominator.
 */
@Constraint(
    id = "inv-1",
    level = "Rule",
    location = "(base)",
    description = "One of lowNumerator or highNumerator and denominator SHALL be present, or all are absent. If all are absent, there SHALL be some extension present",
    expression = "((lowNumerator.exists() or highNumerator.exists()) and denominator.exists()) or (lowNumerator.empty() and highNumerator.empty() and denominator.empty() and extension.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/RatioRange"
)
@Constraint(
    id = "inv-2",
    level = "Rule",
    location = "(base)",
    description = "If present, lowNumerator SHALL have a lower value than highNumerator",
    expression = "lowNumerator.empty() or highNumerator.empty() or (lowNumerator <= highNumerator)",
    source = "http://hl7.org/fhir/StructureDefinition/RatioRange"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class RatioRange extends DataType {
    @Summary
    private final SimpleQuantity lowNumerator;
    @Summary
    private final SimpleQuantity highNumerator;
    @Summary
    private final SimpleQuantity denominator;

    private RatioRange(Builder builder) {
        super(builder);
        lowNumerator = builder.lowNumerator;
        highNumerator = builder.highNumerator;
        denominator = builder.denominator;
    }

    /**
     * The value of the low limit numerator.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getLowNumerator() {
        return lowNumerator;
    }

    /**
     * The value of the high limit numerator.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getHighNumerator() {
        return highNumerator;
    }

    /**
     * The value of the denominator.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getDenominator() {
        return denominator;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (lowNumerator != null) || 
            (highNumerator != null) || 
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
                accept(lowNumerator, "lowNumerator", visitor);
                accept(highNumerator, "highNumerator", visitor);
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
        RatioRange other = (RatioRange) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(lowNumerator, other.lowNumerator) && 
            Objects.equals(highNumerator, other.highNumerator) && 
            Objects.equals(denominator, other.denominator);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                lowNumerator, 
                highNumerator, 
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

    public static class Builder extends DataType.Builder {
        private SimpleQuantity lowNumerator;
        private SimpleQuantity highNumerator;
        private SimpleQuantity denominator;

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
         * The value of the low limit numerator.
         * 
         * @param lowNumerator
         *     Low Numerator limit
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder lowNumerator(SimpleQuantity lowNumerator) {
            this.lowNumerator = lowNumerator;
            return this;
        }

        /**
         * The value of the high limit numerator.
         * 
         * @param highNumerator
         *     High Numerator limit
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder highNumerator(SimpleQuantity highNumerator) {
            this.highNumerator = highNumerator;
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
        public Builder denominator(SimpleQuantity denominator) {
            this.denominator = denominator;
            return this;
        }

        /**
         * Build the {@link RatioRange}
         * 
         * @return
         *     An immutable object of type {@link RatioRange}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid RatioRange per the base specification
         */
        @Override
        public RatioRange build() {
            RatioRange ratioRange = new RatioRange(this);
            if (validating) {
                validate(ratioRange);
            }
            return ratioRange;
        }

        protected void validate(RatioRange ratioRange) {
            super.validate(ratioRange);
            ValidationSupport.requireValueOrChildren(ratioRange);
        }

        protected Builder from(RatioRange ratioRange) {
            super.from(ratioRange);
            lowNumerator = ratioRange.lowNumerator;
            highNumerator = ratioRange.highNumerator;
            denominator = ratioRange.denominator;
            return this;
        }
    }
}
