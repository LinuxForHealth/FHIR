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

public class FHIRPathIntegerValue extends FHIRPathAbstractNode implements FHIRPathNumberValue {
    private final Integer integer;
    private final BigDecimal decimal;
    
    protected FHIRPathIntegerValue(Builder builder) {
        super(builder);
        integer = builder.integer;
        // promote this integer to BigDecimal
        decimal = new BigDecimal(integer.toString());
    }
    
    @Override
    public boolean isIntegerValue() {
        return true;
    }
    
    public BigDecimal decimal() {
        return decimal;
    }
    
    public Integer integer() {
        return integer;
    }
    
    public static FHIRPathIntegerValue integerValue(Integer integer) {
        return FHIRPathIntegerValue.builder(integer).build();
    }
    
    public static FHIRPathIntegerValue integerValue(String name, Integer integer) {
        return FHIRPathIntegerValue.builder(integer).name(name).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, integer);
    }
    
    public static Builder builder(Integer integer) {
        return new Builder(FHIRPathType.SYSTEM_INTEGER, integer);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final Integer integer;
        
        private Builder(FHIRPathType type, Integer integer) {
            super(type);
            this.integer = integer;
        }
        
        @Override
        public Builder name(String name) {
            return (Builder) super.name(name);
        }
        
        @Override
        public Builder path(String path) {
            return (Builder) super.path(path);
        }
        
        @Override
        public Builder value(FHIRPathSystemValue value) {
            return this;
        }
        
        @Override
        public Builder children(FHIRPathNode... children) {
            return this;
        }
        
        @Override
        public Builder children(Collection<FHIRPathNode> children) {
            return this;
        }

        @Override
        public FHIRPathIntegerValue build() {
            return new FHIRPathIntegerValue(this);
        }
    }

    @Override
    public FHIRPathNumberValue add(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.add(value.decimal()));
        }
        return integerValue(integer + value.integer());
    }

    @Override
    public FHIRPathNumberValue subtract(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.subtract(value.decimal()));
        }
        return integerValue(integer - value.integer());
    }

    @Override
    public FHIRPathNumberValue multiply(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.multiply(value.decimal()));
        }
        return integerValue(integer * value.integer());
    }

    @Override
    public FHIRPathNumberValue divide(FHIRPathNumberValue value) {
        return decimalValue(decimal.divide(value.decimal(), MathContext.DECIMAL64));
    }

    @Override
    public FHIRPathNumberValue div(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.divideToIntegralValue(value.decimal()));
        }
        return integerValue(integer / value.integer());
    }

    @Override
    public FHIRPathNumberValue mod(FHIRPathNumberValue value) {
        if (value.isDecimalValue()) {
            return decimalValue(decimal.remainder(value.decimal()));
        }
        return integerValue(integer % value.integer());
    }
    
    @Override
    public FHIRPathNumberValue negate() {
        return integerValue(-integer);
    }

    @Override
    public FHIRPathNumberValue plus() {
        return this;
    }

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
    
    @Override
    public int hashCode() {
        return Objects.hashCode(integer);
    }

    @Override
    public String toString() {
        return integer.toString();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}
