/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.Stack;

import com.ibm.fhir.database.utils.query.expression.BindMarkerNodeVisitor;

/**
 * Represents a bind variable marker (?) in a SQL expression
 */
public abstract class BindMarkerNode implements ExpNode {

    @Override
    public int precedence() {
        return 0; // highest level, like a literal
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // NOP
    }

    @Override
    public boolean isOperand() {
        return true;
    }

    /**
     * Apply this node to the given visitor
     * @param visitor
     */
    public abstract void visit(BindMarkerNodeVisitor visitor);

    @Override
    public String toString() {
        return "?";
    }

    /**
     * Check that the value represented by this bind marker equals
     * the type and value of the given expectedValue.
     * @param expectedValue
     * @return
     */
    public abstract boolean checkTypeAndValue(Object expectedValue);

    /**
     * Return a string of the value being represented by this bind marker,
     * or the defaultValue if the internal value is null.
     * @return
     */
    public abstract String toValueString(String defaultValue);
}