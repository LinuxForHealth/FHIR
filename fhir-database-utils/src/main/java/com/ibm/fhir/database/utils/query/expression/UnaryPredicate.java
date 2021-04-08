/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * An unary predicate (e.g. NOT)
 */
public abstract class UnaryPredicate extends Predicate {

    // the predicate wrapped by this unary node
    private final Predicate predicate;

    /**
     * Protected constructor
     * @param predicate
     */
    protected UnaryPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Get the predicate wrapped by this node
     * @return
     */
    protected Predicate getPredicate() {
        return this.predicate;
    }
}