/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * The ORDER BY part of a SELECT statement
 * @author rarnold
 *
 */
public class OrderByAdapter {
    // The builder from which we were created
    private final Select select;

    /**
     * Protected constructor for modeling the GROUP BY part of a SELECT statement.
     * @param select
     */
    protected OrderByAdapter(Select select) {
        this.select = select;
    }
        
    public Select build() {
        return this.select;
    }

}
