/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

/**
 * @author rarnold
 *
 */
public class NotPredicate extends UnaryPredicate {

    public NotPredicate(Predicate p) {
        super(p);
    }
    

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("NOT ");
        result.append(getPredicate().toString());
        return result.toString();
    }
}
