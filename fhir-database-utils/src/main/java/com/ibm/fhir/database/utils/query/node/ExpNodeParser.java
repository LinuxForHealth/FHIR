/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * Stack based parser for processing expression nodes
 */
public class ExpNodeParser {

    private final Stack<ExpNode> stack = new Stack<>();

    /**
     * Push a node representing the column reference onto the stack
     * @param columnName
     */
    public void column(String columnName) {
        push(new ColumnExpNode(columnName));
    }

    public void literal(String str) {

    }

    public void literal(Double dbl) {

    }

    public void literal(Long lng) {

    }

    /**
     * Push an and node to the stack, taking into account precedence
     */
    public void and() {

    }

    /**
     * Push an or node to the stack, taking into account precedence
     */
    public void or() {

    }

    public void eq() {

    }

    public void neq() {

    }

    public void gt() {

    }

    public void gte() {

    }

    public void lt() {

    }

    public void lte() {

    }

    private void push(ExpNode n) {
        stack.push(n);
    }
}
