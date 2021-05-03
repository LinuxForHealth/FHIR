/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.util.List;

import com.ibm.fhir.database.utils.query.FromClause;
import com.ibm.fhir.database.utils.query.FromItem;
import com.ibm.fhir.database.utils.query.GroupByClause;
import com.ibm.fhir.database.utils.query.HavingClause;
import com.ibm.fhir.database.utils.query.OrderByClause;
import com.ibm.fhir.database.utils.query.PaginationClause;
import com.ibm.fhir.database.utils.query.SelectList;
import com.ibm.fhir.database.utils.query.WhereClause;
import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * Defines the contract for rendering statements. Can be used to address
 * small differences in database syntax and some simple optimizations/query
 * rewrites if necessary
 */
public interface StatementRenderer<T> {

    T and(T left, T right);
    T or(T left, T right);
    T equals(T left, T right);
    T notEquals(T left, T right);
    T not(T left);
    T exists(T statement);

    /**
     * Render the select statement using each of the components, some of which
     * may be optional (null)
     * @param selectList
     * @param fromClause
     * @param whereClause
     * @param groupByClause
     * @param havingClause
     * @param orderByClause
     * @return
     */
    T select(boolean distinct, SelectList selectList, FromClause fromClause, WhereClause whereClause, GroupByClause groupByClause, HavingClause havingClause,
        OrderByClause orderByClause, PaginationClause paginationClause);
    /**
     * Wrap the given expression with parens to control precedence
     * @param render
     * @return
     */
    T paren(T expression);

    /**
     * Render the string expression
     * @param expr
     * @return
     */
    T expression(String expr);

    /**
     * Render a bind parameter marker
     * @return
     */
    T bind();

    /**
     * @param left
     * @param right
     * @return
     */
    T like(T left, T right);
    /**
     * @param items
     * @return
     */
    T from(List<FromItem> items);

    /**
     * Render the given item
     * @param item
     * @return
     */
    T fromItem(FromItem item);
    /**
     * @param sub
     * @return
     */
    T rowSource(T sub);
    /**
     * @param subValue
     * @param aliasValue
     * @return
     */
    T fromItem(T subValue, T aliasValue);
    /**
     * @param alias
     * @return
     */
    T alias(String alias);
    /**
     * @param schemaName
     * @param tableName
     * @return
     */
    T rowSource(String schemaName, String tableName);
    /**
     * @param joinOnPredicate
     * @return
     */
    T render(ExpNode joinOnPredicate);
    /**
     * @param joinFromValue
     * @param joinOnValue
     * @return
     */
    T innerJoin(T joinFromValue, T joinOnValue);
    /**
     * @param joinFromValue
     * @param joinOnValue
     * @return
     */
    T leftOuterJoin(T joinFromValue, T joinOnValue);

    /**
     * @param joinFromValue
     * @param joinOnValue
     * @return
     */
    T fullOuterJoin(T joinFromValue, T joinOnValue);
}
