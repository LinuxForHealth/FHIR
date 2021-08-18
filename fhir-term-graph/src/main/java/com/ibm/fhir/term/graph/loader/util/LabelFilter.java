/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.loader.util;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/*
 * Used to filter edge labels during graph creation
 */
public class LabelFilter {
    public static final LabelFilter ACCEPT_ALL = new LabelFilter() {
        @Override
        public boolean accept(String label) {
            return true;
        }
    };

    private final Set<String> labels;

    public LabelFilter(Set<String> labels) {
        this.labels = Objects.requireNonNull(labels, "labels");
    }

    private LabelFilter() {
        this(Collections.emptySet());
    }

    public boolean accept(String label) {
        return labels.contains(label);
    }
}
