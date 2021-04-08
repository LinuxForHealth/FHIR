/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * Implements the SQL "AND" predicate
 */
public class EqualsPredicate extends BinaryPredicate {

    public EqualsPredicate(Predicate left, Predicate right) {
        super(left, right);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getLeft().toString());
        result.append(" = ");
        result.append(getRight().toString());
        return result.toString();
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        T left = getLeft().render(renderer);
        T right = getRight().render(renderer);
        return renderer.equals(left, right);
    }
}
