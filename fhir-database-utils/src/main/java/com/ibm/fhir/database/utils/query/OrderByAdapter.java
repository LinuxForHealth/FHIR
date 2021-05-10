/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * The ORDER BY part of a SELECT statement
 */
public class OrderByAdapter {
    // The  select statement under construction
    private final Select select;

    // The order by clause under construction
    private final OrderByClause orderByClause;

    /**
     * Protected constructor for modeling the GROUP BY part of a SELECT statement.
     * @param select
     * @param ob
     */
    protected OrderByAdapter(Select select, OrderByClause ob) {
        this.select = select;
        this.orderByClause = ob;
    }

    /**
     * Add the given string expressions to the order by clause
     * @param strings
     * @return
     */
    public OrderByAdapter add(String...strings) {
        this.orderByClause.add(strings);
        return this;
    }

    /**
     * Get the select statement we've been building
     * @return
     */
    public Select build() {
        return this.select;
    }
}