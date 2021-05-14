/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import com.ibm.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * A bind marker representing a Double value
 */
public class DoubleBindMarkerNode extends BindMarkerNode {
    // The Double value (can be null)
    private final Double value;

    public DoubleBindMarkerNode(Double value) {
        this.value = value;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.bindMarker(value);
    }

    @Override
    public void visit(BindMarkerNodeVisitor visitor) {
        visitor.bindDouble(this.value);
    }

    @Override
    public boolean checkTypeAndValue(Object expectedValue) {
        if (value == null) {
            return expectedValue == null;
        } else if (expectedValue instanceof Double) {
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