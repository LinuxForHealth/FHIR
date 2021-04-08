/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * A predicate contained within parens (to control precedence)
 */
public class ParenPredicate extends UnaryPredicate {

    /**
     * Public constructor
     * @param p
     */
    public ParenPredicate(Predicate p) {
        super(p);
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.paren(getPredicate().render(renderer));
    }
}