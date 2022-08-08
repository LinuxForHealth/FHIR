/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

/**
 * The GROUP BY part of a SELECT statement
 */
public class GroupByAdapter {
    // The select statement we are adapting
    private final Select select;

    // The group by clause for the select we are building
    private final GroupByClause groupByClause;

    /**
     * Protected constructor for modeling the GROUP BY part of a SELECT statement.
     *
     * @param select
     * @param gb
     * @param expressions
     */
    protected GroupByAdapter(Select select, GroupByClause gb, String... expressions) {
        this.select = select;
        this.groupByClause = gb;
        this.groupByClause.add(expressions);
    }

    /**
     * Add the given expression to the group by clause wrapped by this adapter
     * @param expr
     * @return
     */
    public GroupByAdapter expression(String expr) {
        this.groupByClause.add(expr);
        return this;
    }

    /**
     * Attach a HAVING predicate to the statement
     *
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