/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.QuantityComparator;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are 
 * not precisely quantified, including amounts involving arbitrary units and floating currencies.
 */
@Constraint(
    id = "qty-3",
    level = "Rule",
    location = "(base)",
    description = "If a code for the unit is present, the system SHALL also be present",
    expression = "code.empty() or system.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Quantity"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Quantity extends Element {
    @Summary
    protected final Decimal value;
    @Summary
    @Binding(
        bindingName = "QuantityComparator",
        strength = BindingStrength.Value.REQUIRED,
        description = "How the Quantity should be understood and represented.",
        valueSet = "http://hl7.org/fhir/ValueSet/quantity-comparator|4.0.1"
    )
    protected final QuantityComparator comparator;
    @Summary
    protected final String unit;
    @Summary
    protected final Uri system;
    @Summary
    protected final Code code;

    protected Quantity(Builder builder) {
        super(builder);
        value = builder.value;
        comparator = builder.comparator;
        unit = builder.unit;
        system = builder.system;
        code = builder.code;
    }

    /**
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value.
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getValue() {
        return value;
    }

    /**
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value 
     * due to measurement issues; e.g. if the comparator is "&lt;" , then the real value is &lt; stated value.
     * 
     * @return
     *     An immutable object of type {@link QuantityComparator} that may be null.
     */
    public QuantityComparator getComparator() {
        return comparator;
    }

    /**
     * A human-readable form of the unit.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * The identification of the system that provides the coded form of the unit.
     * 
     * @return
     *     An immutable object of type {@link Uri} that may be null.
     */
    public Uri getSystem() {
        return system;
    }

    /**
     * A computer processable form of the unit in some unit representation system.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getCode() {
        return code;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (value != null) || 
            (comparator != null) || 
            (unit != null) || 
            (system != null) || 
            (code != null);
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
                accept(comparator, "comparator", visitor);
                accept(unit, "unit", visitor);
                accept(system, "system", visitor);
                accept(code, "code", visitor);
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
        Quantity other = (Quantity) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(value, other.value) && 
            Objects.equals(comparator, other.comparator) && 
            Objects.equals(unit, other.unit) && 
            Objects.equals(system, other.system) && 
            Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                value, 
                comparator, 
                unit, 
                system, 
                code);
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
        protected Decimal value;
        protected QuantityComparator comparator;
        protected String unit;
        protected Uri system;
        protected Code code;

        protected Builder() {
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
         * The value of the measured amount. The value includes an implicit precision in the presentation of the value.
         * 
         * @param value
         *     Numerical value (with implicit precision)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(Decimal value) {
            this.value = value;
            return this;
        }

        /**
         * How the value should be understood and represented - whether the actual value is greater or less than the stated value 
         * due to measurement issues; e.g. if the comparator is "&lt;" , then the real value is &lt; stated value.
         * 
         * @param comparator
         *     &lt; | &lt;= | &gt;= | &gt; - how to understand the value
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comparator(QuantityComparator comparator) {
            this.comparator = comparator;
            return this;
        }

        /**
         * Convenience method for setting {@code unit}.
         * 
         * @param unit
         *     Unit representation
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #unit(com.ibm.fhir.model.type.String)
         */
        public Builder unit(java.lang.String unit) {
            this.unit = (unit == null) ? null : String.of(unit);
            return this;
        }

        /**
         * A human-readable form of the unit.
         * 
         * @param unit
         *     Unit representation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        /**
         * The identification of the system that provides the coded form of the unit.
         * 
         * @param system
         *     System that defines coded unit form
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder system(Uri system) {
            this.system = system;
            return this;
        }

        /**
         * A computer processable form of the unit in some unit representation system.
         * 
         * @param code
         *     Coded form of the unit
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Code code) {
            this.code = code;
            return this;
        }

        /**
         * Build the {@link Quantity}
         * 
         * @return
         *     An immutable object of type {@link Quantity}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Quantity per the base specification
         */
        @Override
        public Quantity build() {
            Quantity quantity = new Quantity(this);
            if (validating) {
                validate(quantity);
            }
            return quantity;
        }

        protected void validate(Quantity quantity) {
            super.validate(quantity);
            ValidationSupport.requireValueOrChildren(quantity);
        }

        protected Builder from(Quantity quantity) {
            super.from(quantity);
            value = quantity.value;
            comparator = quantity.comparator;
            unit = quantity.unit;
            system = quantity.system;
            code = quantity.code;
            return this;
        }
    }
}
