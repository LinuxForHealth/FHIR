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
 * A set of ordered Quantities defined by a low and high limit.
 * </p>
 */
@Constraint(
    key = "rng-2",
    severity = "error",
    human = "If present, low SHALL have a lower value than high",
    expression = "low.empty() or high.empty() or (low <= high)"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Range extends Element {
    private final Quantity low;
    private final Quantity high;

    private Range(Builder builder) {
        super(builder);
        this.low = builder.low;
        this.high = builder.high;
    }

    /**
     * <p>
     * The low limit. The boundary is inclusive.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getLow() {
        return low;
    }

    /**
     * <p>
     * The high limit. The boundary is inclusive.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Quantity}.
     */
    public Quantity getHigh() {
        return high;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(low, "low", visitor);
                accept(high, "high", visitor);
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
        builder.low = low;
        builder.high = high;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        // optional
        private Quantity low;
        private Quantity high;

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
         * The low limit. The boundary is inclusive.
         * </p>
         * 
         * @param low
         *     Low limit
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder low(Quantity low) {
            this.low = low;
            return this;
        }

        /**
         * <p>
         * The high limit. The boundary is inclusive.
         * </p>
         * 
         * @param high
         *     High limit
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder high(Quantity high) {
            this.high = high;
            return this;
        }

        @Override
        public Range build() {
            return new Range(this);
        }
    }
}
