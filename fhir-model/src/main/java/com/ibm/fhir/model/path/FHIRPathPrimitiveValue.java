/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path;

public interface FHIRPathPrimitiveValue extends FHIRPathNode {
    @Override
    default boolean hasValue() {
        return false;
    }
    @Override
    default boolean isPrimitiveValue() {
        return true;
    }
    default boolean isBooleanValue() {
        return false;
    }
    default boolean isDateTimeValue() {
        return false;
    }
    default boolean isStringValue() {
        return false;
    }
    default boolean isTimeValue() {
        return false;
    }
    default boolean isNumberValue() {
        return false;
    }
    default FHIRPathBooleanValue asBooleanValue() {
        return as(FHIRPathBooleanValue.class);
    }
    default FHIRPathDateTimeValue asDateTimeValue() {
        return as(FHIRPathDateTimeValue.class);
    }
    default FHIRPathStringValue asStringValue() {
        return as(FHIRPathStringValue.class);
    }
    default FHIRPathTimeValue asTimeValue() {
        return as(FHIRPathTimeValue.class);
    }
    default FHIRPathNumberValue asNumberValue() {
        return as(FHIRPathNumberValue.class);
    }
}
