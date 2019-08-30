/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.query;

/**
 * @author rarnold
 *
 */
public class HavingClause {

    private String predicate;
    /**
     * @param predicate
     */
    public void addPredicate(String predicate) {
        this.predicate = predicate;
    }

}
