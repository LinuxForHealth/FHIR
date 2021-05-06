/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * A literal string
 */
public class StringExpNode extends LiteralExpNode {
    private final String value;

    /**
     * Public constructor
     * @param value the value of the string. Can be null
     */
    public StringExpNode(String value) {
        this.value = value;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.literal(this.value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.value != null) {
            result.append("'");
            result.append(value);
            result.append("'");
        } else {
            result.append("~");
        }

        return result.toString();
    }
}