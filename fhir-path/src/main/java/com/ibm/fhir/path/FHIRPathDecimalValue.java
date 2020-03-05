/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathNumberValue} node that wraps a {@link BigDecimal} value
 */
public class FHIRPathDecimalValue extends FHIRPathAbstractNode implements FHIRPathNumberValue {
    private final BigDecimal decimal;
    
    protected FHIRPathDecimalValue(Builder builder) {
        super(builder);
        decimal = builder.decimal;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDecimalValue() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal decimal() {
        return decimal;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer integer() {
        return decimal.intValue();
    }
    
    /**
     * Static factory method for creating FHIRPathDecimalValue instances from a {@link BigDecimal} value
     * 
     * @param decimal
     *     the {@link BigDecimal} value
     * @return
     *     a new FHIRPathDecimalValue instance
     */
    public static FHIRPathDecimalValue decimalValue(BigDecimal decimal) {
        return FHIRPathDecimalValue.builder(decimal).build();
    }
    
    /**
     * Static factory method for creating named FHIRPathDecimalValue instances from a {@link BigDecimal} value
     * 
     * @param name
     *     the name
     * @param decimal
     *     the {@link BigDecimal} value
     * @return
     *     a new named FHIRPathDecimalValue instance
     */
    public static FHIRPathDecimalValue decimalValue(String name, BigDecimal decimal) {
        return FHIRPathDecimalValue.builder(decimal).name(name).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder toBuilder() {
        return new Builder(type, decimal);
    }
    
    /**
     * Static factory method for creating builder instances from a {@link BigDecimal} value
     * 
     * @param decimal
     *     the {@link BigDecimal} value
     * @return
     *     a new builder for building FHIRPathDecimalValue instances
     */
    public static Builder builder(BigDecimal decimal) {
        return new Builder(FHIRPathType.SYSTEM_DECIMAL, decimal);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final BigDecimal decimal;
        
        private Builder(FHIRPathType type, BigDecimal decimal) {
            super(type);
            this.decimal = decimal;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder value(FHIRPathSystemValue value) {
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder children(FHIRPathNode... children) {
            return this;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            return this;
        }

        /**
         * Build a FHIRPathDateValue instance using this builder
         * 
         * @return
         *     a new FHIRPathDateValue instance
         */
        @Override
        public FHIRPathDecimalValue build() {
            return new FHIRPathDecimalValue(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue add(FHIRPathNumberValue value) {
        return decimalValue(decimal.add(value.decimal()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue subtract(FHIRPathNumberValue value) {
        return decimalValue(decimal.subtract(value.decimal()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue multiply(FHIRPathNumberValue value) {
        return decimalValue(decimal.multiply(value.decimal()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue divide(FHIRPathNumberValue value) {
        return decimalValue(decimal.divide(value.decimal(), MathContext.DECIMAL64));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue div(FHIRPathNumberValue value) {
        return decimalValue(decimal.divideToIntegralValue(value.decimal()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue mod(FHIRPathNumberValue value) {
        return decimalValue(decimal.remainder(value.decimal()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue negate() {
        return decimalValue(decimal.negate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue plus() {
        return this;
    }

    /**
     * Indicates whether the decimal value wrapped by this FHIRPathDecimalValue node is equal the parameter (or its primitive value)
     * 
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the decimal value wrapped by this FHIRPathDecimalValue node is equal the parameter (or its primitive value), otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FHIRPathNode)) {
            return false;
        }
        FHIRPathNode other = (FHIRPathNode) obj;
        if (!isComparableTo(other)) {
            return false;
        }
        return compareTo(other) == 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(decimal);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return decimal.toPlainString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
