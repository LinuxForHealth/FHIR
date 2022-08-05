/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.math.BigDecimal;

import org.linuxforhealth.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * A bind marker representing a BigDecimal value
 */
public class BigDecimalBindMarkerNode extends BindMarkerNode {
    // The BigDecimal value (can be null)
    private final BigDecimal value;

    public BigDecimalBindMarkerNode(BigDecimal value) {
        this.value = value;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.bindMarker(value);
    }

    @Override
    public void visit(BindMarkerNodeVisitor visitor) {
        visitor.bindBigDecimal(value);
    }

    @Override
    public boolean checkTypeAndValue(Object expectedValue) {
        if (value == null) {
            return expectedValue == null;
        } else if (expectedValue instanceof BigDecimal) {
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