/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * The ACOS SQL function
 */
public class ACosExpNode implements ExpNode {
    private final ExpNode arg;

    /**
     * Public constructor
     * @param refs
     */
    public ACosExpNode(ExpNode arg) {
        this.arg = arg;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.acos(arg.visit(visitor));
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
