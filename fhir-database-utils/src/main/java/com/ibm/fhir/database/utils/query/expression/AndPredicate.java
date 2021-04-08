/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * Implements the SQL "AND" predicate
 */
public class AndPredicate extends BinaryPredicate {

    public AndPredicate(Predicate left, Predicate right) {
        super(left, right);
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        T left = getLeft().render(renderer);
        T right = getRight().render(renderer);
        return renderer.and(left, right);
    }
}
