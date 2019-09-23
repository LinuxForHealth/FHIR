/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * Represents the "HAVING" part of a SELECT statement.
 * @author rarnold
 */
public class HavingAdapter {

    // The immediate select statement we are part of
    private final Select select;
    
    /**
     * Protected constructor. Should only need to be instantiated
     * as part of a {@link SelectAdapter}.
     * @param select
     */
    protected HavingAdapter(Select select) {
        this.select = select;
    }
    
    public Select build() {
        return this.select;
    }

    /**
     * Order by is the very last part of a statement
     * @return
     */
    public OrderByAdapter orderBy() {
        return new OrderByAdapter(select);
    }

}
