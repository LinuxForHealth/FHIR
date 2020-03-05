/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import static com.ibm.fhir.path.FHIRPathDecimalValue.decimalValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Objects;

import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

/**
 * A {@link FHIRPathNumberValue} node that wraps an {@link Integer} value
 */
public class FHIRPathIntegerValue extends FHIRPathAbstractNode implements FHIRPathNumberValue {
    private final Integer integer;
    private final BigDecimal decimal;
    
    protected FHIRPathIntegerValue(Builder builder) {
        super(builder);
        integer = builder.integer;
        // promote this integer to BigDecimal
        decimal = new BigDecimal(integer.toString());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIntegerValue() {
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
    public Integer integer() {
        return integer;
    }
    
    /**
     * Static factory method for creating FHIRPathIntegerValue instances from an {@link Integer} value
     * 
     * @param integer
     *     the {@link Integer} value
     * @return
     *     a new FHIRPathIntegerValue instance
     */
    public static FHIRPathIntegerValue integerValue(Integer integer) {
        return FHIRPathIntegerValue.builder(integer).build();
    }
    
    /**
     * Static factory method for creating named FHIRPathIntegerValue instances from an {@link Integer} value
     * 
     * @param name
     *     the name
     * @param integer
     *     the {@link Integer} value
     * @return
     *     a new named FHIRPathIntegerValue instance
     */
    public static FHIRPathIntegerValue integerValue(String name, Integer integer) {
        return FHIRPathIntegerValue.builder(integer).name(name).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Builder toBuilder() {
        return new Builder(type, integer);
    }
    
    /**
     * Static factory method for creating builder instances from an {@link Integer} value
     * 
     * @param decimal
     *     the {@link Integer} value
     * @return
     *     a new builder for building FHIRPathIntegerValue instances
     */
    public static Builder builder(Integer integer) {
        return new Builder(FHIRPathType.SYSTEM_INTEGER, integer);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final Integer integer;
        
        private Builder(FHIRPathType type, Integer integer) {
            super(type);
            this.integer = integer;
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
         * Build a FHIRPathIntegerValue instance using this builder
         * 
         * @return
         *     a new FHIRPathIntegerValue instance
         */
        @Override
        public FHIRPathIntegerValue build() {
            return new FHIRPathIntegerValue(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue add(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.add(value.decimal()));
        }
        return integerValue(integer + value.integer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue subtract(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.subtract(value.decimal()));
        }
        return integerValue(integer - value.integer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue multiply(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.multiply(value.decimal()));
        }
        return integerValue(integer * value.integer());
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
        if (value.isDecimalValue()) {
            return decimalValue(decimal.divideToIntegralValue(value.decimal()));
        }
        return integerValue(integer / value.integer());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue mod(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.remainder(value.decimal()));
        }
        return integerValue(integer % value.integer());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue negate() {
        return integerValue(-integer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FHIRPathNumberValue plus() {
        return this;
    }
    
    /**
     * Indicates whether the integer value wrapped by this FHIRPathIntegerValue node is equal the parameter (or its primitive value)
     * 
     * @param obj
     *     the other {@link Object}
     * @return
     *     true if the integer value wrapped by this FHIRPathIntegerValue node is equal the parameter (or its primitive value), otherwise false
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
        return Objects.hashCode(integer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return integer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
