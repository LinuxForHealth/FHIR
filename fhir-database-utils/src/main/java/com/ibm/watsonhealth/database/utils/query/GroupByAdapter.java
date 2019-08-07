/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.query;

/**
 * The GROUP BY part of a SELECT statement
 * @author rarnold
 *
 */
public class GroupByAdapter {
    // The select statement we are adapting
    private final Select select;

    /**
     * Protected constructor for modeling the GROUP BY part of a SELECT statement.
     * @param select
     */
    protected GroupByAdapter(Select select) {
        this.select = select;
    }
    
    /**
     * Attach a HAVING predicate to the statement
     * @return
     */
    public HavingAdapter having(String predicate) {
        this.select.addHavingPredicate(predicate);
        return new HavingAdapter(select);
    }
    
    public Select build() {
        return this.select;
    }

}
