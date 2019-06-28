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
    boolean hasValue();
    FHIRPathNode getValue();
    Collection<FHIRPathNode> children();
    default Stream<FHIRPathNode> stream() {
        return Stream.concat(Stream.of(this), children().stream().flatMap(FHIRPathNode::stream));
    }
}
