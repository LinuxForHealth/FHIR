/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Represents IS NOT NULL in a SQL expression
 */
public class IsNotNullExpNode extends UnaryExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.isNotNull(getExpr().visit(visitor));
    }

    @Override
    public int precedence() {
        return 5;
    }
}