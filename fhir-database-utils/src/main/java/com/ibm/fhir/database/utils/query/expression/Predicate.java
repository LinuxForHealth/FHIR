/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * represents a WHERE expression predicate. Note that we're trying to support the
 * textual representation of the WHERE clause predicate (to support programmatic
 * construction) - this is not intended to be an evaluation engine.
 * @author rarnold
 *
 */
public class Predicate {

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
    
    public static Predicate not(Predicate p) {
        return new NotPredicate(p);
    }

    public static Predicate not(String boolExpr) {
        return not(new PredicateExpression(boolExpr));
    }
}
