/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * @author rarnold
 *
 */
public class OrPredicate extends BinaryPredicate {

    /**
     * 
     * @param left
     * @param right
     */
    public OrPredicate(Predicate left, Predicate right) {
        super(left, right);
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getLeft().toString());
        result.append(" OR ");
        result.append(getRight().toString());
        return result.toString();
    }

}
