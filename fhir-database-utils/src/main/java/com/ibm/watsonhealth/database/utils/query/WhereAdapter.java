/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.query;

/**
 * Adapter to help build the WHERE clause expression. To allow a fluent
 * style, we construct an expression tree
 * @author rarnold
 *
 */
public class WhereAdapter {
    // The select statement for this where clause
    private final Select select;

    /**
     * Public constructor
     * @param from
     */
    public WhereAdapter(Select select, String predicate) {
        this.select = select;
    }
    
    public WhereAdapter where(String predicate) {
        // Add this predicate to the select statement
        return this;
    }

    /**
     * Start building the GROUP BY clause
     * @param expressions
     * @return
     */
    public GroupByAdapter groupBy(String... expressions) {
        return new GroupByAdapter(select);
    }
    
    /**
     * Start building the ORDER BY clause
     * @param expressions
     * @return
     */
    public OrderByAdapter orderBy(String... expressions) {
        return new OrderByAdapter(select);
    }

    
    public Select build() {
        return select;
    }
}
