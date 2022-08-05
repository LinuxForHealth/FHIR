/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * Represents a NOT <bool-exp> in a SQL predicate expression
 */
public class NotExpNode extends UnaryExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.not(getExpr().visit(visitor));
    }

    @Override
    public int precedence() {
        return 7;
    }
}