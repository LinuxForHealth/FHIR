/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.path.visitor.FHIRPathNodeVisitor;

public class FHIRPathQuantityValue extends FHIRPathAbstractNode implements FHIRPathSystemValue {
    private final BigDecimal value;
    private final String unit;

    protected FHIRPathQuantityValue(Builder builder) {
        super(builder);
        this.value = builder.value;
        this.unit = builder.unit;
    }
    
    @Override
    public boolean isQuantityValue() {
        return true;
    }
    
    public BigDecimal value() {
        return value;
    }
    
    public String unit() {
        return unit;
    }
    
    public static FHIRPathQuantityValue quantityValue(Quantity quantity) {
        if (quantity.getValue() != null && 
            quantity.getValue().getValue() != null && 
            quantity.getSystem() != null && 
            "http://unitsofmeasure.org".equals(quantity.getSystem().getValue()) && 
            quantity.getCode() != null && 
            quantity.getCode().getValue() != null) {
            BigDecimal value = quantity.getValue().getValue();
            String unit = getUnit(quantity.getCode().getValue());
            return quantityValue(value, unit);
        }
        return null;
    }

    private static String getUnit(String code) {
        switch (code) {
        case "a":
            return "year";
        case "mo":
            return "month";
        case "d":
            return "day";
        case "h":
            return "hour";
        case "min":
            return "minute";
        case "s":
            return "second";
        }
        return code;
    }

    public static FHIRPathQuantityValue quantityValue(BigDecimal value, String unit) {
        return FHIRPathQuantityValue.builder(value, unit).build();
    }

    @Override
    public Builder toBuilder() {
        return new Builder(type, value, unit);
    }
    
    public static Builder builder(BigDecimal value, String unit) {
        return new Builder(FHIRPathType.SYSTEM_QUANTITY, value, unit);
    }
    
    public static class Builder extends FHIRPathAbstractNode.Builder {
        private final BigDecimal value;
        private final String unit;
        
        private Builder(FHIRPathType type, BigDecimal value, String unit) {
            super(type);
            this.value = value;
            this.unit = unit;
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
        public FHIRPathQuantityValue build() {
            return new FHIRPathQuantityValue(this);
        }
    }
    
    public FHIRPathQuantityValue add(FHIRPathQuantityValue quantityValue) {
        return FHIRPathQuantityValue.quantityValue(value.add(quantityValue.value()), unit);
    }
    
    public FHIRPathQuantityValue subtract(FHIRPathQuantityValue quantityValue) {
        return FHIRPathQuantityValue.quantityValue(value.subtract(quantityValue.value()), unit);
    }

    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
            FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
            return unit.equals(quantityValue.unit());
        }
        return (other instanceof FHIRPathNumberValue) || 
                (other.getValue() instanceof FHIRPathNumberValue);
    }

    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathQuantityValue || other.getValue() instanceof FHIRPathQuantityValue) {
            FHIRPathQuantityValue quantityValue = (other instanceof FHIRPathQuantityValue) ? (FHIRPathQuantityValue) other : (FHIRPathQuantityValue) other.getValue();
            return value.compareTo(quantityValue.value());
        }
        FHIRPathNumberValue numberValue = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
        return value.compareTo(numberValue.decimal());
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
        if (other instanceof FHIRPathQuantityValue || other instanceof FHIRPathNumberValue) {
            return compareTo(other) == 0;
        }
        return compareTo(other.getValue()) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value, unit);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value.toPlainString()).append(" '").append(unit).append("'");
        return sb.toString();
    }

    @Override
    public void accept(FHIRPathNodeVisitor visitor) {
        visitor.visit(this);
    }
}