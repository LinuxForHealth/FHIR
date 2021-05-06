/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * A literal Double
 */
public class DoubleExpNode extends LiteralExpNode {
    private final Double value;

    /**
     * Public constructor
     * @param value the value of the Long. Can be null
     */
    public DoubleExpNode(Double value) {
        this.value = value;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.literal(this.value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "~";
    }
}