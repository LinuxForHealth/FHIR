/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.query.expression;

/**
 * @author rarnold
 *
 */
public class AndPredicate extends BinaryPredicate {
    
    public AndPredicate(Predicate left, Predicate right) {
        super(left, right);
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getLeft().toString());
        result.append(" AND ");
        result.append(getRight().toString());
        return result.toString();
    }
}
