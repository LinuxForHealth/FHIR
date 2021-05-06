/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * A binary predicate node (like AND, OR etc)
 */
public abstract class BinaryPredicate extends Predicate {
    private final Predicate left;
    private final Predicate right;

    /**
     * Protected constructor
     * @param left
     * @param right
     */
    protected BinaryPredicate(Predicate left, Predicate right) {
        this.left = left;
        this.right = right;
    }

    /**
     * The left predicate of the binary expression
     * @return
     */
    public Predicate getLeft() {
        return this.left;
    }

    /**
     * The right predicate of the binary expression
     * @return
     */
    public Predicate getRight() {
        return this.right;
    }
}