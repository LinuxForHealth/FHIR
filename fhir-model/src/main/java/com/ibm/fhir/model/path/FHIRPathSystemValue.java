/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.path;

public interface FHIRPathSystemValue extends FHIRPathNode {
    @Override
    default boolean hasValue() {
        return false;
    }
    @Override
    default boolean isSystemValue() {
        return true;
    }
    default boolean isBooleanValue() {
        return false;
    }
    default boolean isStringValue() {
        return false;
    }
    default boolean isQuantityValue() {
        return false;
    }
    default boolean isNumberValue() {
        return false;
    }
    default boolean isTemporalValue() {
        return false;
    }
    default FHIRPathBooleanValue asBooleanValue() {
        return as(FHIRPathBooleanValue.class);
    }
    default FHIRPathStringValue asStringValue() {
        return as(FHIRPathStringValue.class);
    }
    default FHIRPathQuantityValue asQuantityValue() {
        return as(FHIRPathQuantityValue.class);
    }
    default FHIRPathNumberValue asNumberValue() {
        return as(FHIRPathNumberValue.class);
    }
    default FHIRPathTemporalValue asTemporalValue() {
        return as(FHIRPathTemporalValue.class);
    }
}
