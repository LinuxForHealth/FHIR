/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.PredicateParser;

/**
 * An adapter used to build (simple) query predicates
 */
public class PredicateAdapter {

    private final PredicateParser predicateParser = new PredicateParser();

    /**
     * Default constructor
     */
    public PredicateAdapter() {
    }

    /**
     * Public constructor
     * @param current
     */
    public PredicateAdapter(String expr) {
        predicateParser.column(expr);
    }

    // ### LITERALS ###
    public PredicateAdapter literal(String value) {
        predicateParser.literal(value);
        return this;
    }

    public PredicateAdapter literal(long value) {
        predicateParser.literal(value);
        return this;
    }

    public PredicateAdapter literal(double value) {
        predicateParser.literal(value);
        return this;
    }

    // ### EQUAL ###
    public PredicateAdapter eq() {
        predicateParser.eq();
        return this;
    }

    public PredicateAdapter eq(ColumnRef ref) {
        predicateParser.eq();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter eq(long literalValue) {
        predicateParser.eq();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter eq(int literalValue) {
        predicateParser.eq();
        predicateParser.literal((long)literalValue);
        return this;
    }

    public PredicateAdapter eq(String columnExpression) {
        predicateParser.eq();
        predicateParser.column(columnExpression);
        return this;
    }

    public PredicateAdapter eq(String tableAlias, String columnExpression) {
        predicateParser.eq();
        predicateParser.column(tableAlias, columnExpression);
        return this;
    }

    public PredicateAdapter eq(BindMarkerNode bindMarker) {
        predicateParser.eq();
        predicateParser.bindMarker(bindMarker);
        return this;
    }


    /**
     * Add an AND to the list of expression tokens
     * @return
     */
    public PredicateAdapter and() {
        predicateParser.and();
        return this;
    }

    public PredicateAdapter and(ExpNode predicate) {
        predicateParser.and();
        predicateParser.addToken(predicate);
        return this;
    }

    public PredicateAdapter and(String predicate) {
        predicateParser.and();
        predicateParser.column(predicate);
        return this;
    }

    public PredicateAdapter and(String tableAlias, String predicate) {
        predicateParser.and();
        predicateParser.column(tableAlias, predicate);
        return this;
    }

    public PredicateAdapter and(ColumnRef ref) {
        predicateParser.and();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter or() {
        predicateParser.or();
        return this;
    }

    public PredicateAdapter or(ExpNode predicate) {
        predicateParser.or();
        predicateParser.addToken(predicate);
        return this;
    }

    public PredicateAdapter or(String predicate) {
        predicateParser.or();
        predicateParser.column(predicate);
        return this;
    }

    public PredicateAdapter or(String tableAlias, String predicate) {
        predicateParser.or();
        predicateParser.column(tableAlias, predicate);
        return this;
    }

    public PredicateAdapter or(ColumnRef ref) {
        predicateParser.or();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter expr(String expression) {
        predicateParser.column(expression);
        return this;
    }


    // ### NOT EQUAL ###
    public PredicateAdapter neq() {
        predicateParser.neq();
        return this;
    }

    public PredicateAdapter neq(ColumnRef ref) {
        predicateParser.neq();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter neq(long literalValue) {
        predicateParser.neq();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter neq(int literalValue) {
        predicateParser.neq();
        predicateParser.literal((long)literalValue);
        return this;
    }

    public PredicateAdapter neq(String literalValue) {
        predicateParser.neq();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter neq(BindMarkerNode bindMarker) {
        predicateParser.neq();
        predicateParser.bindMarker(bindMarker);
        return this;
    }

    // ### GREATER THAN ###
    public PredicateAdapter gt(ColumnRef ref) {
        predicateParser.gt();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter gt(long literalValue) {
        predicateParser.gt();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter gt(int literalValue) {
        predicateParser.gt();
        predicateParser.literal((long)literalValue);
        return this;
    }

    public PredicateAdapter gt(String literalValue) {
        predicateParser.gt();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter gt(BindMarkerNode bindMarker) {
        predicateParser.gt();
        predicateParser.bindMarker(bindMarker);
        return this;
    }

    // ### GREATER THAN EQUAL ###
    public PredicateAdapter gte(ColumnRef ref) {
        predicateParser.gte();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter gte(long literalValue) {
        predicateParser.gte();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter gte(int literalValue) {
        predicateParser.gte();
        predicateParser.literal((long)literalValue);
        return this;
    }

    public PredicateAdapter gte(String literalValue) {
        predicateParser.gte();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter gte(BindMarkerNode bindMarker) {
        predicateParser.gte();
        predicateParser.bindMarker(bindMarker);
        return this;
    }

    // ### LESS THAN ###
    public PredicateAdapter lt(ColumnRef ref) {
        predicateParser.lt();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter lt(long literalValue) {
        predicateParser.lt();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter lt(int literalValue) {
        predicateParser.lt();
        predicateParser.literal((long)literalValue);
        return this;
    }

    public PredicateAdapter lt(String literalValue) {
        predicateParser.lt();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter lt(BindMarkerNode bindMarker) {
        predicateParser.lt();
        predicateParser.bindMarker(bindMarker);
        return this;
    }

    // ### LESS THAN EQUAL ###
    public PredicateAdapter lte(ColumnRef ref) {
        predicateParser.lte();
        predicateParser.column(ref.toString());
        return this;
    }

    public PredicateAdapter lte(long literalValue) {
        predicateParser.lte();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter lte(int literalValue) {
        predicateParser.lte();
        predicateParser.literal((long)literalValue);
        return this;
    }

    public PredicateAdapter lte(String literalValue) {
        predicateParser.lte();
        predicateParser.literal(literalValue);
        return this;
    }

    public PredicateAdapter lte(BindMarkerNode bindMarker) {
        predicateParser.lte();
        predicateParser.bindMarker(bindMarker);
        return this;
    }


    /**
     * Add a column expression or reference
     * @param expression
     * @return
     */
    public PredicateAdapter col(String expression) {
        predicateParser.column(expression);
        return this;
    }

    public PredicateAdapter col(String tableAlias, String expression) {
        predicateParser.column(tableAlias, expression);
        return this;
    }

    /**
     * Familiar syntax for fluent/builder pattern
     * @return
     */
    public ExpNode build() {
        return predicateParser.parse();
    }
}