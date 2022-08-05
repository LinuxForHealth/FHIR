/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * Base of literal values in the expression tree
 */
public abstract class LiteralExpNode implements ExpNode {

    @Override
    public int precedence() {
        return 0;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // NOP
    }

    @Override
    public boolean isOperand() {
        return true;
    }
}