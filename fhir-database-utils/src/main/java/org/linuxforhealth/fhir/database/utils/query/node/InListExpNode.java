/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Represents an IN list where list is a list of expression nodes (supports different
 * node types and bind markers).
 */
public class InListExpNode implements ExpNode {
    private ExpNode leftNode;
    private final List<ExpNode> inList;

    /**
     * Public constructor
     * @param inList
     */
    public InListExpNode(Collection<ExpNode> inList) {
        this.inList = new ArrayList<ExpNode>(inList);
    }

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        List<T> args;
        if (inList.size() > 0) {
            args = new ArrayList<>(inList.size());
        } else {
            args = Collections.emptyList();
        }

        for (ExpNode n: inList) {
            args.add(n.visit(visitor));
        }

        T leftValue = this.leftNode.visit(visitor);
        return visitor.in(leftValue, args);
    }

    @Override
    public int precedence() {
        return 5;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // We only pop one value from the stack, because the values list is
        // part of this node, not a separate expression
        this.leftNode = stack.pop();
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append(leftNode.toString());
        result.append(" IN (");
        result.append(this.inList.stream().map(Object::toString).collect(Collectors.joining(",")));
        result.append(")");
        return result.toString();
    }
}