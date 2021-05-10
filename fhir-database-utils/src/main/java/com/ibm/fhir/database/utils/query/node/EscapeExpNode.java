/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Represents an ESCAPE '+' expression in a SQL predicate expression
 * for use with LIKE
 */
public class EscapeExpNode extends BinaryExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        T leftValue = getLeft().visit(visitor);
        T rightValue = getRight().visit(visitor);
        return visitor.escape(leftValue, rightValue);
    }

    @Override
    public int precedence() {
        return 7;
    }
}