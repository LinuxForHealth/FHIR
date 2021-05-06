/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * Implements the SQL "NOT" predicate
 */
public class NotPredicate extends UnaryPredicate {

    public NotPredicate(Predicate p) {
        super(p);
    }


    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.not(getPredicate().render(renderer));
    }
}
