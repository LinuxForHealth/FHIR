/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * Represents a {col} LIKE {string-exp} | {bind-var} in a SQL predicate expression
 */
public class LikeExpNode extends BinaryExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        T leftValue = getLeft().visit(visitor);
        T rightValue = getRight().visit(visitor);
        return visitor.like(leftValue, rightValue);
    }

    @Override
    public int precedence() {
        return 7;
    }
}