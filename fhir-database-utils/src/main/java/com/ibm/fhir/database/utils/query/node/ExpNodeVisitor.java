/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.expression.ColumnRef;

/**
 * A visitor for processing expression trees (SQL predicate statements). This
 * is used mainly to write a valid SQL statement which is handed to a database
 * to process...we're not in the business of trying to evaluate the actual
 * expressions here.
 */
public interface ExpNodeVisitor<T> {

    T paren(T expr);
    T and(T left, T right);
    T or(T left, T right);
    T not(T exp);
    T exists(T statement);
    T notExists(T statement);
    T eq(T left, T right);
    T neq(T left, T right);
    T gt(T left, T right);
    T gte(T left, T right);
    T lt(T left, T right);
    T lte(T left, T right);
    T literal(String value);
    T literal(Long value);
    T literal(Double value);
    T column(String tableAlias, String columnName);
    T add(T left, T right);
    T sub(T left, T right);
    T mult(T left, T right);
    T div(T left, T right);
    T isNull(T expr);
    T isNotNull(T expr);
    T between(T leftValue, T rightValue);
    T in(List<T> args);
    T like(T expr);
    T escape(T expr);

    // Support for different bindMarker types
    T bindMarker(Double value);
    T bindMarker(Long value);
    T bindMarker(Integer value);
    T bindMarker(String value);
    T bindMarker(Instant value);
    /**
     * Process a sub-select statement
     * @param select
     * @return
     */
    T select(Select select);

    /**
     * Render a COALESCE(...) function
     * @param columnRefs
     * @return
     */
    T coalesce(List<ColumnRef> columnRefs);
    /**
     * @param value
     * @return
     */
    T bindMarker(BigDecimal value);
}