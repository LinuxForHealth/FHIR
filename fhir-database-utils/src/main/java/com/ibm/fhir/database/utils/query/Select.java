/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.SqlConstants.FROM;
import static com.ibm.fhir.database.utils.query.SqlConstants.SELECT;
import static com.ibm.fhir.database.utils.query.SqlConstants.SPACE;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.query.expression.StatementRenderer;
import com.ibm.fhir.database.utils.query.expression.StringStatementRenderer;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.ExpNodeVisitor;

/**
 * Representation of a select statement built by {@link SelectAdapter#build()}
 */
public class Select {
    // Need a translator to render statement for debugging
    private static final IDatabaseTranslator TRANSLATOR = new DerbyTranslator();

    // The fields, expressions we are selecting
    private final SelectList selectList = new SelectList();

    private boolean distinct;

    // Encapsulated the tables, views, subqueries and joins being selected from
    private final FromClause fromClause = new FromClause();

    // The list of predicates. Optional
    private WhereClause whereClause;

    // The fields/expressions being grouped on. Optional
    private GroupByClause groupByClause;

    // The HAVING predicate. Optional
    private HavingClause havingClause;

    // The fields/expressions determining sort order. Optional
    private OrderByClause orderByClause;

    // offset/limit for pagination
    private PaginationClause paginationClause;

    /**
     * Default constructor. Not a DISTINCT select.
     */
    protected Select() {
    }

    /**
     * Protected constructor
     * @param distinct if true, query will be SELECT DISTINCT ...
     */
    protected Select(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * Factory to create a new instance of the builder needed to create this
     * statement
     *
     * @return
     */
    public static SelectAdapter select(String... columns) {
        return new SelectAdapter(columns);
    }

    public static SelectAdapter select(boolean distinct, String... columns) {
        return new SelectAdapter(distinct, columns);
    }

    /**
     * Add the list of simple columns
     *
     * @param columns
     */
    public void addColumns(String... columns) {
        for (String c : columns) {
            selectList.addColumn(c);
        }
    }

    public void addColumn(String source, String name) {
        selectList.addColumn(source, name);
    }

    public void addColumn(String source, String name, Alias alias) {
        selectList.addColumn(source, name, alias);
    }

    public void addTable(String schemaName, String tableName) {
        fromClause.addTable(schemaName, tableName);
    }

    public void addTable(String tableName, Alias alias) {
        fromClause.addTable(tableName, alias);
    }

    public void addTable(String tableName) {
        fromClause.addTable(tableName);
    }

    public void addTable(String schemaName, String tableName, Alias alias) {
        fromClause.addTable(schemaName, tableName, alias);
    }

    public void addFrom(Select sub, Alias alias) {
        fromClause.addFrom(sub, alias);
    }

    public void setWhereClause(WhereClause wc) {
        this.whereClause = wc;
    }

    public void setGroupByClause(GroupByClause gb) {
        this.groupByClause = gb;
    }

    /**
     * @param predicate
     */
    public void addHavingPredicate(String predicate) {
        if (this.havingClause == null) {
            this.havingClause = new HavingClause();
        }
        this.havingClause.addPredicate(predicate);
    }

    @Override
    public String toString() {
        // Just for information purposes...should use the #render call
        // for database-specific query syntax
        StringBuilder result = new StringBuilder();
        result.append(SELECT);
        if (this.distinct) {
            result.append(" DISTINCT");
        }
        result.append(SPACE).append(this.selectList.toString());
        result.append(SPACE).append(FROM);
        result.append(SPACE).append(this.fromClause.toString());

        if (this.whereClause != null && !this.whereClause.isEmpty()) {
            result.append(SPACE).append(this.whereClause.toString());
        }

        if (this.groupByClause != null) {
            result.append(SPACE).append(this.groupByClause.toString());
        }

        if (this.havingClause != null) {
            result.append(SPACE).append(this.havingClause.toString());
        }

        if (this.orderByClause != null) {
            result.append(SPACE).append(this.orderByClause.toString());
        }

        if (this.paginationClause != null) {
            result.append(SPACE).append(this.paginationClause.toString());
        }

        return result.toString();
    }

    /**
     * A string representation of the query with the bind variables substituted
     * in place which is handy for debugging - but not to be used for actual
     * execution.
     * @return the query string starting XELECT instead of SELECT
     */
    public String toDebugString() {

        try {
            // Generate a pretty-printed string using the renderer so we
            // get an accurate version of the string.
            StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, null, true);
            return this.render(renderer);
        } catch (Exception x) {
            // If we can't render, it's very likely this debug is being used already
            // in a catch clause from an earlier exception. So rather than propagating
            // another exception, we just make note of the error and allow the earlier
            // catch deal with reporting the issue
            return "Failed to render statement: " + x.getMessage();
        }
    }

    /**
     * Render the components of the statement
     * @param <T>
     * @param renderer
     * @return
     */
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.select(distinct, selectList, fromClause, whereClause, groupByClause, havingClause, orderByClause, paginationClause);
    }

    /**
     * Add an inner join to the from clause for this select statement.
     * @param tableName
     * @param alias
     * @param joinOnPredicate
     */
    public void addInnerJoin(String tableName, Alias alias, ExpNode joinOnPredicate) {
        fromClause.addInnerJoin(tableName, alias, joinOnPredicate);
    }

    /**
     * Add a left outer join to the from clause for this select statement.
     * @param tableName
     * @param alias
     * @param joinOnPredicate
     */
    public void addLeftOuterJoin(String tableName, Alias alias, ExpNode joinOnPredicate) {
        fromClause.addLeftOuterJoin(tableName, alias, joinOnPredicate);
    }

    /**
     * @param ob
     */
    public void setOrderByClause(OrderByClause ob) {
        this.orderByClause = ob;
    }

    /**
     * Getter for the whereClause
     * @return the whereClause value, which can be null if not yet set
     */
    public WhereClause getWhereClause() {
        return this.whereClause;
    }

    /**
     * Visit the components of this query
     * @param <T>
     * @param visitor
     * @return
     */
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        T result;
        if (whereClause != null) {
            result = whereClause.visit(visitor);
        } else {
            result = null;
        }
        return result;
    }

    /**
     * Add a pagination clause (offset/limit) to the query.
     * @param offset
     * @param rowsPerPage
     */
    public void addPagination(int offset, int rowsPerPage) {
        this.paginationClause = new PaginationClause(offset, rowsPerPage);
    }

    /**
     * @return the ORDER BY clause object for this select statement. Can be null.
     */
    public OrderByClause getOrderByClause() {
        return this.orderByClause;
    }
}