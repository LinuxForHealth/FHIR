/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * Represents the "HAVING" part of a SELECT statement.
 */
public class HavingAdapter {
    // The immediate select statement we are part of
    private final Select select;

    /**
     * Protected constructor. Should only need to be instantiated
     * as part of a {@link SelectAdapter}.
     *
     * @param select
     */
    protected HavingAdapter(Select select) {
        this.select = select;
    }

    public Select build() {
        return this.select;
    }

    /**
     * Start building the ORDER BY clause
     * @param expressions
     * @return
     */
    public OrderByAdapter orderBy(String... expressions) {
        OrderByClause ob = new OrderByClause();
        ob.add(expressions);
        this.select.setOrderByClause(ob);
        return new OrderByAdapter(select, ob);
    }

}