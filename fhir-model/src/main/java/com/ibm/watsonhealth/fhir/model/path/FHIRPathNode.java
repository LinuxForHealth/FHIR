/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.stream.Stream;

import com.ibm.watsonhealth.fhir.model.visitor.Visitable;

public interface FHIRPathNode extends Comparable<FHIRPathNode> {
    String name();
    FHIRPathType type();
    boolean hasValue();
    FHIRPathPrimitiveValue getValue();
    Collection<FHIRPathNode> children();
    Stream<FHIRPathNode> stream();
    Collection<FHIRPathNode> descendants();
    Visitable visitable();
    default boolean isComparableTo(FHIRPathNode other) {
        return false;
    }
    <T extends FHIRPathNode> boolean is(Class<T> nodeType);
    <T extends FHIRPathNode> T as(Class<T> nodeType);
    default boolean isElementNode() {
        return false;
    }
    default boolean isResourceNode() {
        return false;
    }
    default boolean isPrimitiveValue() {
        return false;
    }
    default FHIRPathElementNode asElementNode() {
        return as(FHIRPathElementNode.class);
    }
    default FHIRPathResourceNode asResourceNode() {
        return as(FHIRPathResourceNode.class);
    }
    default FHIRPathPrimitiveValue asPrimitiveValue() {
        return as(FHIRPathPrimitiveValue.class);
    }
    interface Builder { 
        Builder name(String name);
        Builder value(FHIRPathPrimitiveValue value);
        Builder children(FHIRPathNode... children);
        Builder children(Collection<FHIRPathNode> children);
        FHIRPathNode build();
    }
}
