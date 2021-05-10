/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Represents a NOT EXISTS (<select>) clause in a SQL predicate expression
 */
public class NotExistsExpNode extends UnaryExpNode {

    /**
     * Public constructor
     * @param sub
     */
    public NotExistsExpNode(ExpNode sub) {
        super(sub);
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.notExists(getExpr().visit(visitor));
    }

    @Override
    public int precedence() {
        return 1;
    }

    @Override
    public boolean isOperand() {
        return true;
    }
}