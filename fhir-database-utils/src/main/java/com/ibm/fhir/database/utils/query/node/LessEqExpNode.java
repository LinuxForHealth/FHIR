/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

/**
 * Less than or equal to expression node
 */
public class LessEqExpNode extends ComparativeExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        T leftValue = getLeft().visit(visitor);
        T rightValue = getRight().visit(visitor);
        return visitor.lte(leftValue, rightValue);
    }
}