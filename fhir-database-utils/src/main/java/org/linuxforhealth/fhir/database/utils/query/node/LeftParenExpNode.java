/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * Represents a left paren when parsing an expression
 */
public class LeftParenExpNode implements ExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        // NOP. Only used during parse.
        return null;
    }

    @Override
    public int precedence() {
        return 1;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // NOP
    }

    @Override
    public boolean isLeftParen() {
        return true;
    }
}