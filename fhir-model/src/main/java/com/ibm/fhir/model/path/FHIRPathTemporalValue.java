/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path;

import java.time.temporal.Temporal;

public interface FHIRPathTemporalValue extends FHIRPathSystemValue {
    @Override
    default boolean isTemporalValue() {
        return true;
    }
    
    default boolean isDateValue() {
        return false;
    }
    
    default boolean isDateTimeValue() {
        return false;
    }
    
    default boolean isTimeValue() {
        return false;
    }
    
    Temporal temporal();
    
    default FHIRPathDateValue asDateValue() {
        return as(FHIRPathDateValue.class);
    }
    
    default FHIRPathDateTimeValue asDateTimeValue() {
        return as(FHIRPathDateTimeValue.class);
    }
    
    default FHIRPathTimeValue asTimeValue() {
        return as(FHIRPathTimeValue.class);
    }
    
    FHIRPathTemporalValue add(FHIRPathQuantityValue quantityValue);
    FHIRPathTemporalValue subtract(FHIRPathQuantityValue quantityValue);
}