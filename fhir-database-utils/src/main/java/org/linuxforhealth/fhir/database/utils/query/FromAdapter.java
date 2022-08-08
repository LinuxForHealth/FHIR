/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import org.linuxforhealth.fhir.database.utils.query.node.ExpNode;

/**
 * Adapter for building the FROM clause of a SELECT statement
 */
public class FromAdapter {

    // the select statement being built
    private final Select select;

    /**
     * Model the "from" part of the select statement
     * @param select
     */
    public FromAdapter(Select select) {
        this.select = select;
    }

    /**
     * Add a table to the from clause
     * returning this {@link FromAdapter} ready for the next item
     * @param tableName
     * @return
     */
    public FromAdapter from(String tableName) {
        this.select.addTable(tableName);
        return this;
    }

    /**
     * Add a table with an alias (tab AS foo) to the from clause
     * returning this {@link FromAdapter} ready for the next item
     * @param tableName
     * @param alias
     * @return
     */
    public FromAdapter from(String tableName, Alias alias) {
        this.select.addTable(tableName, alias);
        return this;
    }

    /**
     * Add an INNER JOIN for the given table
     * @param tableName
     * @param alias
     * @param joinOnPredicate
     * @return
     */
    public FromAdapter innerJoin(String tableName, Alias alias, WhereFragment joinOnPredicate) {
        this.select.addInnerJoin(tableName, alias, joinOnPredicate.getExpression());
        return this;
    }

    /**
     * Add an INNER JOIN for the given sub select
     * @param sub
     * @param alias
     * @param joinOnPredicate
     * @return
     */
    public FromAdapter innerJoin(Select sub, Alias alias, WhereFragment joinOnPredicate) {
        this.select.addInnerJoin(sub, alias, joinOnPredicate.getExpression());
        return this;
    }

    /**
     * Add a LEFT OUTER JOIN for the given table
     * @param tableName
     * @param alias
     * @param joinOnPredicate
     * @return
     */
    public FromAdapter leftOuterJoin(String tableName, Alias alias, WhereFragment joinOnPredicate) {
        this.select.addLeftOuterJoin(tableName, alias, joinOnPredicate.getExpression());
        return this;
    }

    /**
     * Start building the "WHERE" clause for the statement
     * @param predicate
     * @return
     */
    public WhereAdapter where(String predicate) {
        WhereClause wc = establishWhereClause();
        return new WhereAdapter(this.select, wc, predicate);
    }

    /**
     * Starts building a WHERE clause with the {tableAlias}.{columnName}
     * @param tableAlias
     * @param columnName
     * @return
     */
    public WhereAdapter where(String tableAlias, String columnName) {
        WhereClause wc = establishWhereClause();
        return new WhereAdapter(this.select, wc, tableAlias, columnName);
    }

    /**
     * Starts building a WHERE clause without any predicate nodes
     * @return
     */
    public WhereAdapter where() {
        WhereClause wc = establishWhereClause();
        return new WhereAdapter(this.select, wc);
    }

    /**
     * Builds a WHERE clause starting with the given predicate node
     * @param predicate
     * @return
     */
    public WhereAdapter where(ExpNode predicate) {
        WhereClause wc = establishWhereClause();
        return new WhereAdapter(this.select, wc, predicate);
    }

    /**
     * Adds a where clause if one does not yet exist
     * @return
     */
    public WhereClause establishWhereClause() {
        WhereClause wc = this.select.getWhereClause();
        if (wc == null) {
            wc = new WhereClause();
            this.select.setWhereClause(wc);
        }
        return wc;
    }

    /**
     * Start building a sub-query. This isn't added to the from until {@link FromSubQueryAdapter#subEnd(String)}
     * is called
     * @return
     */
    public FromSubQueryAdapter subStart() {
        // We pass ourselves in, so that we're the state things return
        // to when subEnd is called. Also, the current select statement
        // is provided so that the sub-select can be added to it when
        // complete
        return new FromSubQueryAdapter(this.select, this);
    }

    /**
     * Provide the select statement we've been building
     * @return
     */
    public Select build() {
        return select;
    }

    /**
     * Start a group by expression
     * @param expressions
     * @return
     */
    public GroupByAdapter groupBy(String... expressions) {
        GroupByClause gb = new GroupByClause();
        this.select.setGroupByClause(gb);
        return new GroupByAdapter(select, gb, expressions);
    }

    /**
     * Start an ORDER BY expression
     * @param expressions
     * @return
     */
    public OrderByAdapter orderBy(String...expressions) {
        OrderByClause orderBy = select.getOrderByClause();
        if (orderBy == null) {
            orderBy = new OrderByClause();
            select.setOrderByClause(orderBy);
        }
        orderBy.add(expressions);
        return new OrderByAdapter(select, orderBy);
    }
}