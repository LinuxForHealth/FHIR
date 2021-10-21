/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.time.Instant;

import com.ibm.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * A bind marker representing a Instant value
 */
public class InstantBindMarkerNode extends BindMarkerNode {
    // The Instant value (can be null)
    private final Instant value;

    public InstantBindMarkerNode(Instant value) {
        this.value = value;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.bindMarker(value);
    }

    @Override
    public void visit(BindMarkerNodeVisitor visitor) {
        visitor.bindInstant(value);
    }

    @Override
    public boolean checkTypeAndValue(Object expectedValue) {
        if (value == null) {
            return expectedValue == null;
        } else if (expectedValue instanceof Instant) {
            return this.value.equals(expectedValue);
        } else {
            return false;
        }
    }

    @Override
    public String toValueString(String defaultValue) {
        return this.value != null ? value.toString() : defaultValue;
    }
}