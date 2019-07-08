/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

public class FHIRPathIntegerValue extends FHIRPathAbstractNode implements FHIRPathNumberValue {
    private final Integer integer;
    
    protected FHIRPathIntegerValue(Builder builder) {
        super(builder);
        integer = builder.integer;
    }
    
    @Override
    public boolean isIntegerValue() {
        return true;
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
        public Builder value(FHIRPathPrimitiveValue value) {
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
    public FHIRPathNumberValue add(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return FHIRPathDecimalValue.decimalValue(new BigDecimal(integer.toString()).add(node.asDecimalValue().decimal()));
        }
        return integerValue(integer + node.asIntegerValue().integer());
    }

    @Override
    public FHIRPathNumberValue subtract(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return FHIRPathDecimalValue.decimalValue(new BigDecimal(integer.toString()).subtract(node.asDecimalValue().decimal()));
        }
        return integerValue(integer - node.asIntegerValue().integer());
    }

    @Override
    public FHIRPathNumberValue multiply(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return FHIRPathDecimalValue.decimalValue(new BigDecimal(integer.toString()).multiply(node.asDecimalValue().decimal()));
        }
        return integerValue(integer * node.asIntegerValue().integer());
    }

    @Override
    public FHIRPathNumberValue divide(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return FHIRPathDecimalValue.decimalValue(new BigDecimal(integer.toString()).divide(node.asDecimalValue().decimal()));
        }
        return integerValue(integer / node.asIntegerValue().integer());
    }

    @Override
    public FHIRPathNumberValue div(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return FHIRPathDecimalValue.decimalValue(new BigDecimal(integer.toString()).divideToIntegralValue(node.asDecimalValue().decimal()));
        }
        return integerValue(integer / node.asIntegerValue().integer());
    }

    @Override
    public FHIRPathNumberValue mod(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return FHIRPathDecimalValue.decimalValue(new BigDecimal(integer.toString()).remainder(node.asDecimalValue().decimal()));
        }
        return integerValue(integer % node.asIntegerValue().integer());
    }

    @Override
    public int compareTo(FHIRPathNumberValue node) {
        if (node.isDecimalValue()) {
            return new BigDecimal(integer.toString()).compareTo(node.asDecimalValue().decimal());
        }
        return integer - node.asIntegerValue().integer();
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
        FHIRPathIntegerValue other = (FHIRPathIntegerValue) obj;
        return Objects.equals(integer, other.integer());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(integer);
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
    public String toString() {
        return integer.toString();
    }
}
