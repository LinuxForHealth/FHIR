/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.query.FromJoin.JoinType;
import com.ibm.fhir.database.utils.query.expression.StatementRenderer;
import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * The FromClause SQL definition
 */
public class FromClause {

    // the list of FROM elements, some of which could be ANSI-style join statements
    private final List<FromItem> items = new ArrayList<>();

    /**
     * Add a table without an alias. Tsk tsk.
     * @param schemaName the schema qualifier for the table
     * @param tableName the table name
     */
    public void addTable(String schemaName, String tableName) {
        TableRowSource fit = new TableRowSource(schemaName, tableName);
        items.add(new FromItem(fit, null));
    }

    /**
     * Add a qualified schema.table with an alias.
     * @param schemaName the schema qualifier for the table
     * @param tableName the table name
     * @param alias the alias to use in select expressions and join predicates
     */
    public void addTable(String schemaName, String tableName, Alias alias) {
        TableRowSource table = new TableRowSource(schemaName, tableName);
        items.add(new FromItem(table, alias));
    }

    /**
     * Add a table to the FROM items list
     * @param tableName
     * @param alias
     */
    public void addTable(String tableName, Alias alias) {
        TableRowSource table = new TableRowSource(tableName);
        items.add(new FromItem(table, alias));
    }

    /**
     * Add a table to the FROM items list
     * @param tableName
     * @param alias
     */
    public void addTable(String tableName) {
        TableRowSource table = new TableRowSource(tableName);
        items.add(new FromItem(table));
    }

    /**
     * Add the sub-query as an item in the from list
     *
     * @param sub
     * @param alias
     */
    public void addFrom(Select sub, Alias alias) {
        SelectRowSource fis = new SelectRowSource(sub);
        items.add(new FromItem(fis, alias));
    }

    @Override
    public String toString() {
        // Building the FROM string is a little more involved because of the
        // potential to mix ANSI-style and traditional joins. The JOIN statements
        // don't require , to separate them. We only need a comma if the next
        // thing we're going to join is ANSI-style.
        if (items.isEmpty()) {
            throw new IllegalStateException("No FROM elements");
        }

        StringBuilder result = new StringBuilder();
        FromItem element = items.get(0);
        result.append(element.toString());
        for (int i=1; i<items.size(); i++) {
            FromItem nextElement = items.get(i);
            if (nextElement.isAnsiJoin()) {
                result.append(" "); // e.g. INNER JOIN ... ON ...
            } else {
                result.append(", ");
            }
            result.append(nextElement.toString());
        }
        return result.toString();
    }

    /**
     * Render this FROM clause using the given renderer
     * @param <T>
     * @param renderer
     * @return
     */
    public <T> T render(StatementRenderer<T> renderer) {
        // Need to pass the items list to the renderer because of the complexity of
        // handling ansi and non-ansi style joins together
        return renderer.from(items);
    }

    /**
     * Add an inner join clause to the FROM items list
     * @param tableName
     * @param alias
     * @param joinOnPredicate
     */
    public void addInnerJoin(String tableName, Alias alias, ExpNode joinOnPredicate) {
        TableRowSource trs = new TableRowSource(tableName);
        items.add(new FromJoin(JoinType.INNER_JOIN, trs, alias, joinOnPredicate));
    }

    /**
     * Add a left outer join clause to the FROM items list
     * @param tableName
     * @param alias
     * @param joinOnPredicate
     */
    public void addLeftOuterJoin(String tableName, Alias alias, ExpNode joinOnPredicate) {
        TableRowSource trs = new TableRowSource(tableName);
        items.add(new FromJoin(JoinType.LEFT_OUTER_JOIN, trs, alias, joinOnPredicate));
    }

}