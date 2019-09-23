/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter for building the FROM clause of a SELECT statement
 * @author rarnold
 *
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
        this.select.addTable(null, tableName);
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
     * Start building the "WHERE" clause for the statement
     * @param predicate
     * @return
     */
    public WhereAdapter where(String predicate) {
        return new WhereAdapter(this.select, predicate);
    }

    /**
     * Start building a sub-query. This isn't added to the from until {@link FromSubQueryAdapter#subEnd()}
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
        
    public Select build() {
        return select;
    }
    
    public GroupByAdapter groupBy(String... expressions) {
        select.addGroupBy(expressions);
        return new GroupByAdapter(select);
    }
    
    public OrderByAdapter orderBy(String...expressions) {
        select.addOrderBy(expressions);
        return new OrderByAdapter(select);
    }
}
