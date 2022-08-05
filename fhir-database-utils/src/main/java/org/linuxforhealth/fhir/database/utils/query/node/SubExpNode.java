/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

/**
 * Subtraction expression node
 */
public class SubExpNode extends AdditiveExpNode {

    @Override
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        T leftValue = getLeft().visit(visitor);
        T rightValue = getRight().visit(visitor);
        return visitor.subtract(leftValue, rightValue);
    }
}