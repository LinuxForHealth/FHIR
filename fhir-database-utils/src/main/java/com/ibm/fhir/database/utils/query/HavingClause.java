/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * Manages the Having Clause
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
