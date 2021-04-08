/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * The ORDER BY part of a SELECT statement
 */
public class OrderByAdapter {
    // The builder from which we were created
    private final Select select;

    // The order by clause under construction
    private final OrderByClause orderByClause;

    /**
     * Protected constructor for modeling the GROUP BY part of a SELECT statement.
     *
     * @param select
     */
    protected OrderByAdapter(Select select, OrderByClause ob) {
        this.select = select;
        this.orderByClause = ob;
    }

    public OrderByAdapter add(String...strings) {
        this.orderByClause.add(strings);
        return this;
    }

    public Select build() {
        return this.select;
    }
}