/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * Implements the SQL "NOT" predicate
 */
public class LikePredicate extends BinaryPredicate {

    public LikePredicate(Predicate left, Predicate right) {
        super(left, right);
    }


    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        T left = getLeft().render(renderer);
        T right = getRight().render(renderer);
        return renderer.like(left, right);
    }
}