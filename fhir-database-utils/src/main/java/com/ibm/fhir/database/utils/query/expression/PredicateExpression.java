/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * Implements the expression predicate
 */
public class PredicateExpression extends Predicate {

    // The string expression we represent
    private String expr;

    public PredicateExpression(String boolExpr) {
        this.expr = boolExpr;
    }

    @Override
    public String toString() {
        return expr;
    }
}