/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path;

import java.math.BigDecimal;

public interface FHIRPathNumberValue extends FHIRPathPrimitiveValue {
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
            return ((FHIRPathQuantityNode) other).isComparableTo(this);
        }
        return other instanceof FHIRPathNumberValue || 
                other.getValue() instanceof FHIRPathNumberValue;
    }
    
    @Override
    default int compareTo(FHIRPathNode other) {
        if (!isComparableTo(other)) {
            throw new IllegalArgumentException();
        }
        if (other instanceof FHIRPathQuantityNode) {
            return decimal().compareTo(((FHIRPathQuantityNode) other).getQuantityValue());
        }
        FHIRPathNumberValue value = (FHIRPathNumberValue) ((other instanceof FHIRPathNumberValue) ? other : other.getValue());
        return decimal().compareTo(value.decimal());
    }
}
