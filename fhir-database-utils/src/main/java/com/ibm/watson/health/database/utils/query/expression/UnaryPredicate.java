/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.query.expression;

/**
 * An unary predicate (e.g. NOT)
 * @author rarnold
 *
 */
public abstract class UnaryPredicate extends Predicate {

    private Predicate predicate;
    
    public UnaryPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return this.predicate;
    }
}
