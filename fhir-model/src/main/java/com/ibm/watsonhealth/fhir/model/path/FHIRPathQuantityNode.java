/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.math.BigDecimal;
import java.util.Collection;

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
}
