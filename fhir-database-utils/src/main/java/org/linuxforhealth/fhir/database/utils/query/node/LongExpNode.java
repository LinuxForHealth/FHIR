/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * A literal Long. Nullable
 */
public class LongExpNode extends LiteralExpNode {
    private final Long value;

    /**
     * Public constructor
     * @param value the value of the Long. Can be null
     */
    public LongExpNode(Long value) {
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