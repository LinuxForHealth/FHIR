/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

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
        return visitor.in(args);
    }

    @Override
    public int precedence() {
        return 5;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
    }

    @Override
    public boolean isOperand() {
        // for the purposes of parsing, we treat this entire node as a single operand
        // because currently the list of args needs to be built separately, not by the
        // parser.
        return true;
    }

    @Override
    public String toString() {
        return this.inList.stream().map(Object::toString).collect(Collectors.joining(","));
    }
}