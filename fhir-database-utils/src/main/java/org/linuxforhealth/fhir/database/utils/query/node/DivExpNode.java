/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * Division expression node
 */
public class DivExpNode extends MultiplicativeExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        T leftValue = getLeft().visit(visitor);
        T rightValue = getRight().visit(visitor);
        return visitor.divide(leftValue, rightValue);
    }
}