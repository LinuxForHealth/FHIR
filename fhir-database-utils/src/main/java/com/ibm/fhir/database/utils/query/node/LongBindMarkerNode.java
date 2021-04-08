/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import com.ibm.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * A bind marker representing a Long value
 */
public class LongBindMarkerNode extends BindMarkerNode {
    // The Long value (can be null)
    private final Long value;

    public LongBindMarkerNode(Long value) {
        this.value = value;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.bindMarker(value);
    }

    @Override
    public void visit(BindMarkerNodeVisitor visitor) {
        visitor.bindLong(value);
    }
}