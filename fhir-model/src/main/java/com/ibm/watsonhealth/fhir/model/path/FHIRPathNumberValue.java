/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

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
    
    @Override
    default boolean isComparableTo(FHIRPathNode other) {
        if (other instanceof FHIRPathQuantityNode) {
            FHIRPathQuantityNode quantityNode = (FHIRPathQuantityNode) other;
            return quantityNode.isComparableTo(this);
        }
        return other instanceof FHIRPathNumberValue;
    }
    
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
}
