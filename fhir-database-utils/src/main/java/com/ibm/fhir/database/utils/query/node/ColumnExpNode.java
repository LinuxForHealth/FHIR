/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * Represents a reference to a table column or alias in an expression
 */
public class ColumnExpNode implements ExpNode {
    final String tableAlias;
    final String columnName;

    /**
     * Public constructor
     * @param columnName
     */
    public ColumnExpNode(String columnName) {
        this.tableAlias = null;
        this.columnName = columnName;
    }

    /**
     * Public constructor
     * @param tableAlias
     * @param columnName
     */
    public ColumnExpNode(String tableAlias, String columnName) {
        this.tableAlias = tableAlias;
        this.columnName = columnName;
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.column(tableAlias, columnName);
    }

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (tableAlias != null) {
            result.append(tableAlias);
            result.append(".");
        }
        result.append(columnName);

        return result.toString();
    }
}
