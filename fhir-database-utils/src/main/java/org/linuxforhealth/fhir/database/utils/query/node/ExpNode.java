/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * A node in the expression tree for building SQL predicate statements
 */
public interface ExpNode {

    /**
     * Generic evaluation visitor
     * @param <T>
     * @param visitor
     * @return
     */
    <T> T visit(ExpNodeVisitor<T> visitor);

    /**
     * Precedence of this expression node. Used to parse the expression into
     * the correct tree. We use 0 to represent the highest precedence
     * @return
     */
    int precedence();

    /**
     * Is this expression node an operator
     * @return
     */
    default boolean isOperator() { return false; }

    /**
     * Is this expression node an operand
     * @return
     */
    default boolean isOperand() { return false; }

    /**
     * Is this expression node a left-paren
     * @return
     */
    default boolean isLeftParen() { return false; }

    /**
     * Is this expression node a right-paren
     * @return
     */
    default boolean isRightParen() { return false; }

    /**
     * Read the operands from the stack
     * @param stack
     */
    void popOperands(Stack<ExpNode> stack);
}
