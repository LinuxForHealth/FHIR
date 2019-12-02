/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path;

import java.math.BigDecimal;

public interface FHIRPathNumberValue extends FHIRPathSystemValue {
    @Override
    default boolean isNumberValue() {
        return true;
    }
    
    default boolean isDecimalValue() {
        return false;
    }
    
    default boolean isIntegerValue() {
        return false;
    }
    
    BigDecimal decimal();
    Integer integer();
    
    default FHIRPathDecimalValue asDecimalValue() {
        return as(FHIRPathDecimalValue.class);
    }
    
    default FHIRPathIntegerValue asIntegerValue() {
        return as(FHIRPathIntegerValue.class);
    }
    
    default Number number() {
        if (isDecimalValue()) {
            return asDecimalValue().decimal();
        }
        return asIntegerValue().integer();
    }
    
    // operations
    FHIRPathNumberValue add(FHIRPathNumberValue value);
    FHIRPathNumberValue subtract(FHIRPathNumberValue value);
    FHIRPathNumberValue multiply(FHIRPathNumberValue value);
    FHIRPathNumberValue divide(FHIRPathNumberValue value);
    FHIRPathNumberValue div(FHIRPathNumberValue value);
    FHIRPathNumberValue mod(FHIRPathNumberValue value);
    FHIRPathNumberValue negate();
    FHIRPathNumberValue plus();

    @Override
    default boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathQuantityNode) {
            FHIRPathQuantityNode quantityNode = (FHIRPathQuantityNode) other;
            return quantityNode.hasValue() || quantityNode.getQuantityValue() != null;
        }
        return other instanceof FHIRPathQuantityValue ||  
                other instanceof FHIRPathNumberValue || 
                other.getValue() instanceof FHIRPathNumberValue;
    }
    
    @Override
    default int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathQuantityNode) {
            FHIRPathQuantityNode quantityNode = (FHIRPathQuantityNode) other;
            if (quantityNode.hasValue()) {
                FHIRPathQuantityValue quantityValue = (FHIRPathQuantityValue) quantityNode.getValue();
                return decimal().compareTo(quantityValue.value());
            }
            return decimal().compareTo(quantityNode.getQuantityValue());
        }
        if (other instanceof FHIRPathQuantityValue) {
            return decimal().compareTo(((FHIRPathQuantityValue) other).value());
        }
        FHIRPathNumberValue value = (other instanceof FHIRPathNumberValue) ? (FHIRPathNumberValue) other : (FHIRPathNumberValue) other.getValue();
        return decimal().compareTo(value.decimal());
    }
}
