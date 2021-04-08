/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import com.ibm.fhir.database.utils.query.Select;

/**
 * represents a WHERE expression predicate. Note that we're trying to support the
 * textual representation of the WHERE clause predicate (to support programmatic
 * construction) - this is not intended to be an evaluation engine.
 */
public abstract class Predicate {

    public abstract <T> T render(StatementRenderer<T> renderer);

    public Predicate and(String boolExpr) {
        return and(new PredicateExpression(boolExpr));
    }

    public Predicate and(Predicate right) {
        return new AndPredicate(this, right);
    }

    public Predicate or(String boolExpr) {
        return or(new PredicateExpression(boolExpr));
    }

    public Predicate or(Predicate right) {
        return new AndPredicate(this, right);
    }

    public Predicate not() {
        return not(this);
    }

    /**
     * Factory method to create an EXISTS predicate expression node
     * @param correlatedSubSelect
     * @return
     */
    public static Predicate exists(Select correlatedSubSelect) {
        return new Exists(correlatedSubSelect);
    }

    /**
     * Factory method to create a NOT <expr> predicate expression node
     * @param p
     * @return
     */
    public static Predicate not(Predicate p) {
        return new NotPredicate(p);
    }

    /**
     * Factory helper method to model not the given boolean expression
     * @param boolExpr
     * @return
     */
    public static Predicate not(String boolExpr) {
        return not(new PredicateExpression(boolExpr));
    }
}
