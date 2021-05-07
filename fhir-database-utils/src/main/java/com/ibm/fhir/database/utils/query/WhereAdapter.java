/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * Adapter to help build the WHERE clause expression. To allow a fluent
 * style, we construct an expression tree
 *
 */
public class WhereAdapter extends BaseWhereAdapter<WhereAdapter> {
    // The select statement under construction
    private final Select select;

    /**
     * Public constructor to start the WHERE clause in a select statement
     * @param select
     * @param whereClause
     */
    public WhereAdapter(Select select, WhereClause whereClause) {
        super(whereClause.getPredicateParser());
        this.select = select;
    }

    /**
     * Public constructor
     * @param select
     * @param whereClause
     * @param predicate
     */
    public WhereAdapter(Select select, WhereClause whereClause, String predicate) {
        super(whereClause.getPredicateParser());
        this.select = select;
        getPredicateParser().column(predicate);
    }

    /**
     * Convenience for when the statement is written like
     *   .where("lr", "current_resource_id").eq("r", "resource_id")
     * @param select
     * @param whereClause
     * @param tableAlias
     * @param columnName
     */
    public WhereAdapter(Select select, WhereClause whereClause, String tableAlias, String columnName) {
        super(whereClause.getPredicateParser());
        this.select = select;
        getPredicateParser().column(tableAlias, columnName);
    }

    public WhereAdapter(Select select, WhereClause whereClause, ExpNode predicate) {
        super(whereClause.getPredicateParser());
        this.select = select;
        getPredicateParser().addToken(predicate);
    }

    /**
     * Start building the GROUP BY clause
     * @param expressions
     * @return
     */
    public GroupByAdapter groupBy(String... expressions) {
        GroupByClause gb = new GroupByClause();
        select.setGroupByClause(gb);
        return new GroupByAdapter(select, gb, expressions);
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

    /**
     * Get the select statement wrapped by this adapter
     * @return
     */
    public Select build() {
        return select;
    }

    @Override
    protected WhereAdapter getThis() {
        return this;
    }
}