/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * An amount of economic utility in some recognized currency.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Money extends Element {
    @Summary
    private final Decimal value;
    @Summary
    @Binding(
        bindingName = "CurrencyCode",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/currencies|4.3.0-cibuild"
    )
    private final Code currency;

    private Money(Builder builder) {
        super(builder);
        value = builder.value;
        currency = builder.currency;
    }

    /**
     * Numerical value (with implicit precision).
     * 
     * @return
     *     An immutable object of type {@link Decimal} that may be null.
     */
    public Decimal getValue() {
        return value;
    }

    /**
     * ISO 4217 Currency Code.
     * 
     * @return
     *     An immutable object of type {@link Code} that may be null.
     */
    public Code getCurrency() {
        return currency;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (value != null) || 
            (currency != null);
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
                accept(currency, "currency", visitor);
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
        Money other = (Money) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(value, other.value) && 
            Objects.equals(currency, other.currency);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                value, 
                currency);
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
        private Decimal value;
        private Code currency;

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
         * Numerical value (with implicit precision).
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
         * ISO 4217 Currency Code.
         * 
         * @param currency
         *     ISO 4217 Currency Code
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder currency(Code currency) {
            this.currency = currency;
            return this;
        }

        /**
         * Build the {@link Money}
         * 
         * @return
         *     An immutable object of type {@link Money}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Money per the base specification
         */
        @Override
        public Money build() {
            Money money = new Money(this);
            if (validating) {
                validate(money);
            }
            return money;
        }

        protected void validate(Money money) {
            super.validate(money);
            ValidationSupport.requireValueOrChildren(money);
        }

        protected Builder from(Money money) {
            super.from(money);
            value = money.value;
            currency = money.currency;
            return this;
        }
    }
}
