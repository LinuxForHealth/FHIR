/*
 * (C) Copyright IBM Corp. 2019, 2021
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

    /**
     * Public constructor
     * @param boolExpr
     */
    public PredicateExpression(String boolExpr) {
        this.expr = boolExpr;
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.expression(expr);
    }
}