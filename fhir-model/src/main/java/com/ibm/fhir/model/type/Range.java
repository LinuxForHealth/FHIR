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
 * A set of ordered Quantities defined by a low and high limit.
 */
@Constraint(
    id = "rng-2",
    level = "Rule",
    location = "(base)",
    description = "If present, low SHALL have a lower value than high",
    expression = "low.empty() or high.empty() or (low <= high)",
    source = "http://hl7.org/fhir/StructureDefinition/Range"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Range extends Element {
    @Summary
    private final SimpleQuantity low;
    @Summary
    private final SimpleQuantity high;

    private Range(Builder builder) {
        super(builder);
        low = builder.low;
        high = builder.high;
    }

    /**
     * The low limit. The boundary is inclusive.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getLow() {
        return low;
    }

    /**
     * The high limit. The boundary is inclusive.
     * 
     * @return
     *     An immutable object of type {@link SimpleQuantity} that may be null.
     */
    public SimpleQuantity getHigh() {
        return high;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (low != null) || 
            (high != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(low, "low", visitor);
                accept(high, "high", visitor);
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
        Range other = (Range) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(low, other.low) && 
            Objects.equals(high, other.high);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                low, 
                high);
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
        private SimpleQuantity low;
        private SimpleQuantity high;

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
         * The low limit. The boundary is inclusive.
         * 
         * @param low
         *     Low limit
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder low(SimpleQuantity low) {
            this.low = low;
            return this;
        }

        /**
         * The high limit. The boundary is inclusive.
         * 
         * @param high
         *     High limit
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder high(SimpleQuantity high) {
            this.high = high;
            return this;
        }

        /**
         * Build the {@link Range}
         * 
         * @return
         *     An immutable object of type {@link Range}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Range per the base specification
         */
        @Override
        public Range build() {
            Range range = new Range(this);
            if (validating) {
                validate(range);
            }
            return range;
        }

        protected void validate(Range range) {
            super.validate(range);
            ValidationSupport.requireValueOrChildren(range);
        }

        protected Builder from(Range range) {
            super.from(range);
            low = range.low;
            high = range.high;
            return this;
        }
    }
}
