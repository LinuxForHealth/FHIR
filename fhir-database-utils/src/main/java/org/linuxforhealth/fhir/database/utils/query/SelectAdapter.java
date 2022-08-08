/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

/**
 * An abstract representation of a select statement which can be translated
 * into an executable select statement. Keeps track of bind variables (parameter
 * markers) and hopefully is a bit easier to use (and is more reliable) than
 * constructing SQL with StringBuilder.
 * The goal is to support two main use-cases (which drive the API design):
 * <ul>
 * <li>1. Simplify construction of hand-written SQL statements</li>
 * <li>2. Support code-generated SQL where a statement is constructed
 * from another model (e.g. FHIR search queries).</li>
 * </ul>
 * <p/>
 * The goal isn't to enforce building a syntactically perfect SQL statement -
 * checking that is the role of the RDBMS SQL parser. But hopefully this makes
 * things a bit easier, less error-prone and therefore quicker. It also helps to
 * standardize the SQL statement building process across the project.
 */
public class SelectAdapter {

    // the select statement under construction
    private final Select select;

    /**
     * Adapter this select statement
     *
     * @param select
     */
    public SelectAdapter(Select select) {
        this.select = select;
    }

    /**
     * Public constructor taking a collection of string column names
     *
     * @param columns
     */
    public SelectAdapter(String... columns) {
        this.select = new Select();
        this.select.addColumns(columns);
    }

    public SelectAdapter(boolean distinct, String... columns) {
        this.select = new Select(distinct);
        this.select.addColumns(columns);
    }

    public SelectAdapter column(String source, String name) {
        this.select.addColumn(source, name);
        return this;
    }

    public SelectAdapter addColumn(String source, String name, Alias alias) {
        this.select.addColumn(source, name, alias);
        return this;
    }
    
    /**
     * Add a column value with a given alias. Can be used to add literals in
     * the select list
     * @param source
     * @param name
     * @param alias
     * @return
     */
    public SelectAdapter addColumn(String columnValue, Alias alias) {
        this.select.addColumn(columnValue, alias);
        return this;
    }

    /**
     * Add a WITH x AS (SELECT ...) statement to the beginning of the select
     * @param selectClause
     * @param alias
     * @return
     */
    public SelectAdapter with(Select selectClause, Alias alias) {
        this.select.addWithClause(selectClause, alias);
        return this;
    }

    /**
     * Create a from clause for this select statement
     * @return
     */
    public FromAdapter from(String tableName, Alias alias) {
        select.addTable(tableName, alias);
        return new FromAdapter(select);
    }

    /**
     * Add the sub-query select to the FROM clause
     *
     * @param sub the sub-query select statement
     * @param alias
     * @return
     */
    public FromAdapter from(Select sub, Alias alias) {
        select.addFrom(sub, alias);
        return new FromAdapter(select);
    }

    /**
     * Create a from clause for this select statement
     *
     * @return
     */
    public FromAdapter from(String table) {
        FromAdapter result = new FromAdapter(select);
        return result.from(table);
    }

    /**
     * Create a {@link FromAdapter} associated with the current select statement.
     * No row sources are added to the query. The FROM will only be added
     * to the statement if rows sources are subsequently added to the returned
     * {@link FromAdapter}.
     * @return
     */
    public FromAdapter from() {
        return new FromAdapter(select);
    }

    /**
     * Getter for the select statement we are managing
     *
     * @return
     */
    public Select getSelect() {
        return this.select;
    }

    /**
     * Add pagination offset/limit to the query
     * @param translator
     * @param offset
     * @param rowsPerPage
     */
    public void pagination(int offset, int rowsPerPage) {
        select.addPagination(offset, rowsPerPage);
    }

    /**
     * Add a select via UNION
     *
     * @param unionSelect the select to be UNION'd to this select statement
     * @return
     */
    public void union(Select unionSelect) {
        select.setUnion(unionSelect);
    }

    /**
     * Add a select via UNION ALL
     *
     * @param unionAllSelect the select to be UNION ALL'd to this select statement
     * @return
     */
    public void unionAll(Select unionAllSelect) {
        select.setUnionAll(unionAllSelect);
    }

    /**
     * Get the statement we've been constructing
     *
     * @return
     */
    public Select build() {
        return this.select;
    }
}