/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.SqlConstants.*;

import com.ibm.fhir.database.utils.query.expression.Predicate;
import com.ibm.fhir.database.utils.query.expression.PredicateExpression;

/**
 * Representation of a select statement built by {@link SelectAdapter#build()}
 * TODO currently a work-in-progress, to sketch out ideas and see what
 * works, what's useful etc.
 * @author rarnold
 *
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
    

    /**
     * Factory to create a new instance of the builder needed to create this
     * statement
     * @return
     */
    public static SelectAdapter select(String... columns) {
        return new SelectAdapter(columns);
    }
    
    /**
     * Factory function for creating an {@link Alias} from a string
     * @param aliasStr
     * @return
     */
    public static Alias alias(String aliasStr) {
        return new Alias(aliasStr);
    }
    
    public static Predicate predicate(String boolExpr) {
        return new PredicateExpression(boolExpr);
    }
 
    /**
     * Add the list of simple columns
     * @param columns
     */
    public void addColumns(String... columns) {
        for (String c: columns) {
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
    
    public void addGroupBy(String...expressions) {
        if (groupByClause == null) {
            groupByClause = new GroupByClause();
        }
        groupByClause.add(expressions);
    }

    public void addOrderBy(String...expressions) {
        if (orderByClause == null) {
            orderByClause = new OrderByClause();
        }
        orderByClause.add(expressions);
    }

    @Override
    public String toString() {
        // render the statement as a string. TODO consider using a visitor?
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
        
        return result.toString();
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
}
