/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.Stack;

import org.linuxforhealth.fhir.database.utils.query.Select;

/**
 * Wraps a Select statement in an expression tree
 */
public class SelectExpNode implements ExpNode {
    // The select statement we are wrapping (this is a subquery)
    private final Select select;

    /**
     * Public constructor
     * @param select
     */
    public SelectExpNode(Select select) {
        this.select = select;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.select(select);
    }

    @Override
    public int precedence() {
        return 5;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // NOP
    }

    @Override
    public boolean isOperand() {
        // behave like an operand in the expression tree
        return true;
    }
}