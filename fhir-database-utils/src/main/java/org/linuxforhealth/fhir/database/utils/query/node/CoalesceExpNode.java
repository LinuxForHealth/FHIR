/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.linuxforhealth.fhir.database.utils.query.expression.ColumnRef;

/**
 * The COALESCE SQL function
 */
public class CoalesceExpNode implements ExpNode {
    private final List<ColumnRef> columnRefs;

    /**
     * Public constructor
     * @param refs
     */
    public CoalesceExpNode(ColumnRef[] refs) {
        this.columnRefs = Arrays.asList(refs);
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.coalesce(this.columnRefs);
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
