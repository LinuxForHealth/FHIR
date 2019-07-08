/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

public interface FHIRPathNumberValue extends FHIRPathPrimitiveValue, Comparable<FHIRPathNumberValue> {
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
    default boolean greaterThan(FHIRPathNumberValue value) {
        return compareTo(value) > 0;
    }
    default boolean greaterThanOrEqual(FHIRPathNumberValue value) {
        return compareTo(value) >= 0;
    }
    default boolean lessThan(FHIRPathNumberValue value) {
        return compareTo(value) < 0;
    }
    default boolean lessThanOrEqual(FHIRPathNumberValue value) {
        return compareTo(value) <= 0;
    }
}
