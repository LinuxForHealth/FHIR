/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.query.expression;

/**
 * A binary predicate node (like AND, OR etc)
 * @author rarnold
 *
 */
public abstract class BinaryPredicate extends Predicate {
    private Predicate left;
    private Predicate right;
    
    protected BinaryPredicate(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    public Predicate getLeft() {
        return this.left;
    }
    
    public Predicate getRight() {
        return this.right;
    }
}
