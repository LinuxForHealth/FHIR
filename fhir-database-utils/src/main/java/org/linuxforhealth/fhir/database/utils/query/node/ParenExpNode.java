/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * An expression contained within parens
 */
public class ParenExpNode extends UnaryExpNode {

    public ParenExpNode(ExpNode expr) {
        setExpr(expr);
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.paren(getExpr().visit(visitor));
    }

    @Override
    public int precedence() {
        return 0;
    }

    @Override
    public boolean isOperand() {
        // If this node is found in the source expression, we treat as
        // an operand which means it looks like a single entity.
        return true;
    }
}