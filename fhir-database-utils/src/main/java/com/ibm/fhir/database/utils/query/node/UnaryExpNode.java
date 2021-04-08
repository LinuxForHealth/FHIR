/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * A binary expression for addition, multiplication, comparison etc
 */
public abstract class UnaryExpNode extends OperatorNode {

    // A node with a single sub-expression
    private ExpNode expr;

    /**
     * Default public constructor (postponed initialization of the expr value)
     */
    public UnaryExpNode() {
    }

    /**
     * Protected constructor for immediate initialization by a subclass
     * @param expr
     */
    protected UnaryExpNode(ExpNode expr) {
        this.expr = expr;
    }

    /**
     * @return the left
     */
    public ExpNode getExpr() {
        return this.expr;
    }
    /**
     * @param left the left to set
     */
    public void setExpr(ExpNode expr) {
        this.expr = expr;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // just one operand needed
        this.expr = stack.pop();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.expr != null) {
            result.append(expr.toString());
        } else {
            result.append("~");
        }

        return result.toString();
    }
}