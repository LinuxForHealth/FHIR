/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface FHIRPathNode {
    String name();
    FHIRPathType type();
    boolean hasValue();
    FHIRPathNode getValue();
    Collection<FHIRPathNode> children();
    default Collection<FHIRPathNode> descendants() {
        return stream().skip(1).collect(Collectors.toList());
    }
    default Stream<FHIRPathNode> stream() {
        return Stream.concat(Stream.of(this), children().stream().flatMap(FHIRPathNode::stream));
    }
    default <T extends FHIRPathNode> T as(Class<T> nodeType) {
        return nodeType.cast(this);
    }
}
