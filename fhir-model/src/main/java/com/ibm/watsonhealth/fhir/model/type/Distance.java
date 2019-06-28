/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.Collection;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.QuantityComparator;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A length - a value with a unit that is a physical distance.
 * </p>
 */
@Constraint(
    id = "dis-1",
    level = "Rule",
    location = "(base)",
    description = "There SHALL be a code if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.",
    expression = "(code.exists() or value.empty()) and (system.empty() or system = %ucum)"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Distance extends Quantity {
    private Distance(Builder builder) {
        super(builder);
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(value, "value", visitor);
                accept(comparator, "comparator", visitor);
                accept(unit, "unit", visitor);
                accept(system, "system", visitor);
                accept(code, "code", visitor);
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
        builder.value = value;
        builder.comparator = comparator;
        builder.unit = unit;
        builder.system = system;
        builder.code = code;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Quantity.Builder {
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
         * The value of the measured amount. The value includes an implicit precision in the presentation of the value.
         * </p>
         * 
         * @param value
         *     Numerical value (with implicit precision)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder value(Decimal value) {
            return (Builder) super.value(value);
        }

        /**
         * <p>
         * How the value should be understood and represented - whether the actual value is greater or less than the stated value 
         * due to measurement issues; e.g. if the comparator is "&lt;" , then the real value is &lt; stated value.
         * </p>
         * 
         * @param comparator
         *     &lt; | &lt;= | &gt;= | &gt; - how to understand the value
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder comparator(QuantityComparator comparator) {
            return (Builder) super.comparator(comparator);
        }

        /**
         * <p>
         * A human-readable form of the unit.
         * </p>
         * 
         * @param unit
         *     Unit representation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder unit(String unit) {
            return (Builder) super.unit(unit);
        }

        /**
         * <p>
         * The identification of the system that provides the coded form of the unit.
         * </p>
         * 
         * @param system
         *     System that defines coded unit form
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder system(Uri system) {
            return (Builder) super.system(system);
        }

        /**
         * <p>
         * A computer processable form of the unit in some unit representation system.
         * </p>
         * 
         * @param code
         *     Coded form of the unit
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder code(Code code) {
            return (Builder) super.code(code);
        }

        @Override
        public Distance build() {
            return new Distance(this);
        }
    }
}
