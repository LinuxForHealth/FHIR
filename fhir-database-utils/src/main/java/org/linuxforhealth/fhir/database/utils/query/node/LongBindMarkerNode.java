/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import org.linuxforhealth.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * A bind marker representing a nullable Long value
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

    @Override
    public boolean checkTypeAndValue(Object expectedValue) {
        if (value == null) {
            return expectedValue == null;
        } else if (expectedValue instanceof Long) {
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