/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.SqlConstants.FROM;
import static com.ibm.fhir.database.utils.query.SqlConstants.SELECT;
import static com.ibm.fhir.database.utils.query.SqlConstants.SPACE;

import java.util.List;

import com.ibm.fhir.database.utils.query.expression.StatementRenderer;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.ExpNodeVisitor;

/**
 * Representation of a select statement built by {@link SelectAdapter#build()}
 */
public class Select {
    // The fields, expressions we are selecting
    private final SelectList selectList = new SelectList();

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
     * Factory to create a new instance of the builder needed to create this
     * statement
     *
     * @return
     */
    public static SelectAdapter select(String... columns) {
        return new SelectAdapter(columns);
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

    public void addTable(String schemaName, String tableName) {
        fromClause.addTable(schemaName, tableName);
    }

    public void addTable(String tableName, Alias alias) {
        fromClause.addTable(tableName, alias);
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
        result.append(SPACE).append(this.selectList.toString());
        result.append(SPACE).append(FROM);
        result.append(SPACE).append(this.fromClause.toString());

        if (this.whereClause != null) {
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
        StringBuilder result = new StringBuilder();

        // on purpose...so you can't accidentally use this query string
        result.append("SELECT");
        result.append(SPACE).append(this.selectList.toString());
        result.append(SPACE).append(FROM);
        result.append(SPACE).append(this.fromClause.toString());

        if (this.whereClause != null) {
            result.append(SPACE).append(this.whereClause.toDebugString());
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

        return result.toString();
    }

    /**
     * Render the components of the statement
     * @param <T>
     * @param renderer
     * @return
     */
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.select(selectList, fromClause, whereClause, groupByClause, havingClause, orderByClause, paginationClause);
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
     * @param ob
     */
    public void setOrderByClause(OrderByClause ob) {
        this.orderByClause = ob;
    }

    /**
     * Collect all the bind marker nodes associated with this statement
     * @param bindMarkers
     */
    public void gatherBindMarkers(List<BindMarkerNode> bindMarkers) {
        // at present, only bind markers in the where clause are supported
        if (whereClause != null) {
            whereClause.gatherBindMarkers(bindMarkers);
        }
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
}