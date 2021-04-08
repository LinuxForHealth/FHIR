/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;


/**
 * Represents a LIKE <string-exp> | <bind-var> in a SQL predicate expression
 */
public class LikeExpNode extends UnaryExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.like(getExpr().visit(visitor));
    }

    @Override
    public int precedence() {
        return 7;
    }
}