/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * Represents IS NULL in a SQL expression
 */
public class IsNullExpNode extends UnaryExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        return visitor.isNull(getExpr().visit(visitor));
    }

    @Override
    public int precedence() {
        return 5;
    }
}