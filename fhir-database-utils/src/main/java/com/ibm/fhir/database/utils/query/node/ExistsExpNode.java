/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Represents an EXISTS (<select>) clause in a SQL predicate expression
 */
public class ExistsExpNode extends UnaryExpNode {

    /**
     * Public constructor
     * @param sub
     */
    public ExistsExpNode(ExpNode sub) {
        super(sub);
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.exists(getExpr().visit(visitor));
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