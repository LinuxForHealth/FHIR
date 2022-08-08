/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

import java.util.Stack;

/**
 * A binary expression for addition, multiplication, comparison etc
 */
public abstract class BinaryExpNode extends OperatorNode {

    // left <OP> right
    private ExpNode left;
    private ExpNode right;

    /**
     * @return the left
     */
    public ExpNode getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(ExpNode left) {
        this.left = left;
    }

    /**
     * @return the right
     */
    public ExpNode getRight() {
        return right;
    }

    /**
     * @param right the right to set
     */
    public void setRight(ExpNode right) {
        this.right = right;
    }

    @Override
    public void popOperands(Stack<ExpNode> stack) {
        // make sure we take them off in the correct order
        this.right = stack.pop();
        this.left = stack.pop();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.left != null) {
            result.append(left.toString());
        } else {
            result.append("~"); // null value
        }

        result.append(", ");

        if (this.right != null) {
            result.append(right.toString());
        } else {
            result.append("~"); // null value
        }

        return result.toString();
    }
}