/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.math.BigDecimal;
import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Quantity;

public class FHIRPathQuantityNode extends FHIRPathElementNode {    
    private final Quantity quantity;
    
    protected FHIRPathQuantityNode(Builder builder) {
        super(builder);
        quantity = builder.quantity;
    }
    
    public Quantity quantity() {
        return quantity;
    }
    
    public BigDecimal getQuantityValue() {
        if (quantity.getValue() != null && quantity.getValue().getValue() != null) {
            return quantity.getValue().getValue();
        }
        return null;
    }
    
    public String getQuantityUnit() {
        // prefer code over unit
        if (quantity.getCode() != null && quantity.getCode().getValue() != null) {
            return quantity.getCode().getValue();
        }
        if (quantity.getUnit() != null && quantity.getUnit().getValue() != null) {
            return quantity.getUnit().getValue();
        }
        return null;
    }
    
    @Override
    public boolean isQuantityNode() {
        return true;
    }
    
    public static Builder builder(Quantity quantity) {
        return new Builder(FHIRPathType.FHIR_QUANTITY, quantity);
    }

    public static class Builder extends FHIRPathElementNode.Builder {
        private final Quantity quantity;
        
        protected Builder(FHIRPathType type, Quantity quantity) {
            super(type, quantity);
            this.quantity = quantity;
        }
        
        public Builder name(java.lang.String name) {
            return (Builder) super.name(name);
        }
        
        public Builder value(FHIRPathPrimitiveValue value) {
            return (Builder) super.value(value);
        }
        
        public Builder children(FHIRPathNode... children) {
            return (Builder) super.children(children);
        }
        
        public Builder children(Collection<FHIRPathNode> children) {
            return (Builder) super.children(children);
        }

        @Override
        public FHIRPathQuantityNode build() {
            return new FHIRPathQuantityNode(this);
        }
    }
    
    @Override
    public boolean isComparableTo(FHIRPathNode other) {
        BigDecimal value = getQuantityValue();
        if (other instanceof FHIRPathNumberValue) {
            return value != null;
        }
        if (other instanceof FHIRPathQuantityNode) {
            BigDecimal otherValue = ((FHIRPathQuantityNode) other).getQuantityValue();
            
            String unit = getQuantityUnit();
            String otherUnit = ((FHIRPathQuantityNode) other).getQuantityUnit();
            
            return value != null && otherValue != null && 
                    unit != null && otherUnit != null && 
                    // units must be equal
                    unit.equals(otherUnit);
        }
        return false;
    }

    @Override
    public int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        BigDecimal value = getQuantityValue();
        if (other instanceof FHIRPathNumberValue) {
            return value.compareTo(((FHIRPathNumberValue) other).decimal());
        }
        return value.compareTo(((FHIRPathQuantityNode) other).getQuantityValue());
    }
    
    public FHIRPathQuantityNode add(FHIRPathQuantityNode node) {
        Quantity quantity = Quantity.builder()
                .value(Decimal.of(getQuantityValue().add(node.getQuantityValue())))
                .unit(string(getQuantityUnit()))
                .build();
        return FHIRPathQuantityNode.builder(quantity).build();
    }
    
    public FHIRPathQuantityNode subtract(FHIRPathQuantityNode node) {
        Quantity quantity = Quantity.builder()
                .value(Decimal.of(getQuantityValue().subtract(node.getQuantityValue())))
                .unit(string(getQuantityUnit()))
                .build();
        return FHIRPathQuantityNode.builder(quantity).build();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        BigDecimal quantityValue = getQuantityValue();
        String quantityUnit = getQuantityUnit();
        sb.append(quantityValue != null ? quantityValue.toPlainString() : "<no value>");
        sb.append(" ");
        sb.append("'");
        sb.append(quantityUnit != null ? quantityUnit : "<no unit>");
        sb.append("'");
        return sb.toString();
    }
}
