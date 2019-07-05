/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.stream.Stream;

public interface FHIRPathNode {
    String name();
    FHIRPathType type();
    boolean hasValue();
    FHIRPathPrimitiveTypeNode getValue();
    Collection<FHIRPathNode> children();
    Stream<FHIRPathNode> stream();
    Collection<FHIRPathNode> descendants();
    <T extends FHIRPathNode> boolean is(Class<T> nodeType);
    <T extends FHIRPathNode> T as(Class<T> nodeType);
    default boolean isElementNode() {
        return false;
    }
    default boolean isResourceNode() {
        return false;
    }
    default boolean isPrimitiveTypeNode() {
        return false;
    }
    default FHIRPathElementNode asElementNode() {
        return as(FHIRPathElementNode.class);
    }
    default FHIRPathResourceNode asResourceNode() {
        return as(FHIRPathResourceNode.class);
    }
    default FHIRPathPrimitiveTypeNode asPrimitiveTypeNode() {
        return as(FHIRPathPrimitiveTypeNode.class);
    }
    interface Builder { 
        Builder name(String name);
        Builder value(FHIRPathPrimitiveTypeNode value);
        Builder children(FHIRPathNode... children);
        Builder children(Collection<FHIRPathNode> children);
        Builder children(Builder builder);
        FHIRPathNode build();
    }
}
