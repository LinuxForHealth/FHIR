/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.query.expression.ColumnRef;
import com.ibm.fhir.database.utils.query.expression.LiteralString;
import com.ibm.fhir.database.utils.query.node.ACosExpNode;
import com.ibm.fhir.database.utils.query.node.BigDecimalBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.CoalesceExpNode;
import com.ibm.fhir.database.utils.query.node.ColumnExpNode;
import com.ibm.fhir.database.utils.query.node.CosExpNode;
import com.ibm.fhir.database.utils.query.node.DoubleBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.ExistsExpNode;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.InstantBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.IntegerBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.LongBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.NotExistsExpNode;
import com.ibm.fhir.database.utils.query.node.PredicateParser;
import com.ibm.fhir.database.utils.query.node.SelectExpNode;
import com.ibm.fhir.database.utils.query.node.SinExpNode;
import com.ibm.fhir.database.utils.query.node.StringBindMarkerNode;

/**
 * A piece of a where clause which isn't attached to a {@link Select} statement
 * allowing getThis() bit to be reused where the statement/whereClause isn't
 * yet established.
 */
public abstract class BaseWhereAdapter<T> {

    // The parser used to build the where statement
    private final PredicateParser predicateParser;

    protected abstract T getThis();

    /**
     * Getter for the predicate parser
     * @return
     */
    protected PredicateParser getPredicateParser() {
        return this.predicateParser;
    }

    /**
     * For building standalone predicates like filters
     */
    protected BaseWhereAdapter() {
        this.predicateParser = new PredicateParser();
    }

    /**
     * Adopts an existing predicate parser
     * @param pp
     */
    protected BaseWhereAdapter(PredicateParser pp) {
        this.predicateParser = pp;
    }

    /**
     * Get the expression that has been built
     * @return
     */
    public ExpNode getExpression() {
        return this.predicateParser.parse();
    }

    // ### LITERALS ###
    public T literal(String value) {
        predicateParser.literal(value);
        return getThis();
    }

    public T literal(long value) {
        predicateParser.literal(value);
        return getThis();
    }

    public T literal(double value) {
        predicateParser.literal(value);
        return getThis();
    }

    // ### EQUAL ###
    public T eq() {
        predicateParser.eq();
        return getThis();
    }

    public T eq(ColumnRef ref) {
        predicateParser.eq();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T eq(long literalValue) {
        predicateParser.eq();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T eq(int literalValue) {
        predicateParser.eq();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    public T eq(String columnExpression) {
        predicateParser.eq();
        predicateParser.column(columnExpression);
        return getThis();
    }

    public T eq(LiteralString str) {
        predicateParser.eq();
        predicateParser.literal(str.getValue());
        return getThis();
    }

    public T eq(String tableAlias, String columnExpression) {
        predicateParser.eq();
        predicateParser.column(tableAlias, columnExpression);
        return getThis();
    }

    public T eq(BindMarkerNode bindMarker) {
        predicateParser.eq();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    /**
     * To simplify code, support multiple where clauses, treating each as an AND
     * @param columnName
     * @return
     */
    public T where(String columnName) {
        predicateParser.and();
        predicateParser.column(columnName);
        return getThis();
    }

    /**
     * To simplify code, support multiple where clauses, treating each as an AND
     * @param tableAlias
     * @param columnName
     * @return
     */
    public T where(String tableAlias, String columnName) {
        predicateParser.and();
        predicateParser.column(tableAlias, columnName);
        return getThis();
    }

    /**
     * Add an AND to the list of expression tokens
     * @return
     */
    public T and() {
        predicateParser.and();
        return getThis();
    }

    public T and(ExpNode predicate) {
        predicateParser.and();
        predicateParser.addToken(predicate);
        return getThis();
    }

    public T and(String element) {
        predicateParser.and();
        predicateParser.column(element);
        return getThis();
    }

    public T and(String tableAlias, String columnName) {
        predicateParser.and();
        predicateParser.column(tableAlias, columnName);
        return getThis();
    }

    public T and(ColumnRef ref) {
        predicateParser.and();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T or() {
        predicateParser.or();
        return getThis();
    }

    public T or(ExpNode predicate) {
        predicateParser.or();
        predicateParser.addToken(predicate);
        return getThis();
    }

    public T or(String element) {
        predicateParser.or();
        predicateParser.column(element);
        return getThis();
    }

    public T or(String tableAlias, String columnName) {
        predicateParser.or();
        predicateParser.column(tableAlias, columnName);
        return getThis();
    }

    public T or(ColumnRef ref) {
        predicateParser.or();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T expr(String expression) {
        predicateParser.column(expression);
        return getThis();
    }


    // ### NOT EQUAL ###
    public T neq() {
        predicateParser.neq();
        return getThis();
    }

    public T neq(ColumnRef ref) {
        predicateParser.neq();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T neq(long literalValue) {
        predicateParser.neq();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T neq(int literalValue) {
        predicateParser.neq();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    public T neq(String literalValue) {
        predicateParser.neq();
        predicateParser.column(literalValue);
        return getThis();
    }

    public T neq(LiteralString str) {
        predicateParser.neq();
        predicateParser.literal(str.getValue());
        return getThis();
    }

    public T neq(BindMarkerNode bindMarker) {
        predicateParser.neq();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### GREATER THAN ###
    public T gt() {
        predicateParser.gt();
        return getThis();
    }

    public T gt(ColumnRef ref) {
        predicateParser.gt();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T gt(long literalValue) {
        predicateParser.gt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T gt(int literalValue) {
        predicateParser.gt();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    public T gt(String literalValue) {
        predicateParser.gt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T gt(BindMarkerNode bindMarker) {
        predicateParser.gt();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### GREATER THAN EQUAL ###
    public T gte() {
        predicateParser.gte();
        return getThis();
    }

    public T gte(ColumnRef ref) {
        predicateParser.gte();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T gte(long literalValue) {
        predicateParser.gte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T gte(int literalValue) {
        predicateParser.gte();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    public T gte(String literalValue) {
        predicateParser.gte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T gte(BindMarkerNode bindMarker) {
        predicateParser.gte();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### LESS THAN ###
    public T lt() {
        predicateParser.lt();
        return getThis();
    }

    public T lt(ColumnRef ref) {
        predicateParser.lt();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T lt(long literalValue) {
        predicateParser.lt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T lt(int literalValue) {
        predicateParser.lt();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    public T lt(String literalValue) {
        predicateParser.lt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T lt(BindMarkerNode bindMarker) {
        predicateParser.lt();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### LESS THAN EQUAL ###
    public T lte() {
        predicateParser.lte();
        return getThis();
    }

    public T lte(ColumnRef ref) {
        predicateParser.lte();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T lte(long literalValue) {
        predicateParser.lte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T lte(int literalValue) {
        predicateParser.lte();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    public T lte(String literalValue) {
        predicateParser.lte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    public T lte(BindMarkerNode bindMarker) {
        predicateParser.lte();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }


    /**
     * Add a column expression or reference
     * @param expression
     * @return
     */
    public T col(String expression) {
        predicateParser.column(expression);
        return getThis();
    }

    public T col(String tableAlias, String expression) {
        predicateParser.column(tableAlias, expression);
        return getThis();
    }

    /**
     * Add an exists clause using the given correlated sub-select statement
     * @param correlatedSubSelect
     * @return
     */
    public T exists(Select correlatedSubSelect) {
        SelectExpNode sub = new SelectExpNode(correlatedSubSelect);
        predicateParser.addToken(new ExistsExpNode(sub));
        return getThis();
    }

    /**
     * Add a NOT EXISTS clause using the given correlated sub-select statement
     * @param correlatedSubSelect
     * @return
     */
    public T notExists(Select correlatedSubSelect) {
        SelectExpNode sub = new SelectExpNode(correlatedSubSelect);
        predicateParser.addToken(new NotExistsExpNode(sub));
        return getThis();
    }

    public T mult() {
        predicateParser.mult();
        return getThis();
    }

    public T add() {
        predicateParser.add();
        return getThis();
    }

    public T sub() {
        predicateParser.sub();
        return getThis();
    }

    public T div() {
        predicateParser.div();
        return getThis();
    }


    /**
     * Add a COALESCE(c1, c2, ...) function. These should actually be column
     * expressions, but for now we only need to model them as table.col values
     * @param columnRefs
     * @return
     */
    public T coalesce(ColumnRef... columnRefs) {
        predicateParser.addToken(new CoalesceExpNode(columnRefs));
        return getThis();
    }

    public T sin(ExpNode arg) {
        predicateParser.addToken(new SinExpNode(arg));
        return getThis();
    }

    public T sin(ColumnRef arg) {
        predicateParser.addToken(new SinExpNode(new ColumnExpNode(arg.getRef())));
        return getThis();
    }


    public T cos(ExpNode arg) {
        predicateParser.addToken(new CosExpNode(arg));
        return getThis();
    }

    public T cos(ColumnRef arg) {
        predicateParser.addToken(new CosExpNode(new ColumnExpNode(arg.getRef())));
        return getThis();
    }

    public T acos(ExpNode arg) {
        predicateParser.addToken(new ACosExpNode(arg));
        return getThis();
    }

    public T acos(ColumnRef arg) {
        predicateParser.addToken(new ACosExpNode(new ColumnExpNode(arg.getRef())));
        return getThis();
    }

    /**
     * Add an IS NOT NULL operator to the expression
     * @return
     */
    public T isNotNull() {
        predicateParser.isNotNull();
        return getThis();
    }


    /**
     * Add an IS NULL operator to the expression
     * @return
     */
    public T isNull() {
        predicateParser.isNull();
        return getThis();
    }

    /**
     * Add a left paren `(` to the expression
     */
    public T leftParen() {
        predicateParser.leftParen();
        return getThis();
    }

    /**
     * Add a right paren `)` to the expression
     */
    public T rightParen() {
        predicateParser.rightParen();
        return getThis();
    }

    /**
     * Add IN (?, ?, ...) for a list of String values
     * @param inList
     * @return
     */
    public T in(String[] inList) {
        predicateParser.inString(Arrays.asList(inList));
        return getThis();
    }

    /**
     * Thanks to type erasure we can only have one type of list here. No overloading.
     * String is the most likely type of field, so we go with that. Uses bind-markers,
     * not literals.
     * @param inList
     * @return
     */
    public T in(List<String> inList) {
        predicateParser.inString(inList);
        return getThis();
    }

    /**
     * Add IN (?, ?, ...) for a list of Long values
     * @param inList
     * @return
     */
    public T in(Long[] inList) {
        predicateParser.inLong(Arrays.asList(inList));
        return getThis();
    }

    /**
     * Add IN (123, 124, ...) for a literal list of Long values
     * @param inList
     * @return
     */
    public T inLiteral(Long[] inList) {
        predicateParser.inLiteral(inList);
        return getThis();
    }

    // ### LIKE ###
    public T like() {
        predicateParser.like();
        return getThis();
    }

    public T like(ColumnRef ref) {
        predicateParser.like();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T like(String str) {
        predicateParser.like();
        predicateParser.literal(str);
        return getThis();
    }

    public T like(String tableAlias, String columnExpression) {
        predicateParser.like();
        predicateParser.column(tableAlias, columnExpression);
        return getThis();
    }

    public T like(BindMarkerNode bindMarker) {
        predicateParser.like();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### ESCAPE ###
    public T escape() {
        predicateParser.escape();
        return getThis();
    }

    public T escape(ColumnRef ref) {
        predicateParser.escape();
        predicateParser.column(ref.toString());
        return getThis();
    }

    public T escape(String str) {
        predicateParser.escape();
        predicateParser.literal(str);
        return getThis();
    }

    public T escape(String tableAlias, String columnExpression) {
        predicateParser.escape();
        predicateParser.column(tableAlias, columnExpression);
        return getThis();
    }

    public T escape(BindMarkerNode bindMarker) {
        predicateParser.escape();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    public T bind(String param) {
        predicateParser.bindMarker(new StringBindMarkerNode(param));
        return getThis();
    }

    public T bind(Long param) {
        predicateParser.bindMarker(new LongBindMarkerNode(param));
        return getThis();
    }

    public T bind(Integer param) {
        predicateParser.bindMarker(new IntegerBindMarkerNode(param));
        return getThis();
    }

    public T bind(Double param) {
        predicateParser.bindMarker(new DoubleBindMarkerNode(param));
        return getThis();
    }

    public T bind(Instant param) {
        predicateParser.bindMarker(new InstantBindMarkerNode(param));
        return getThis();
    }

    public T bind(BigDecimal value) {
        predicateParser.bindMarker(new BigDecimalBindMarkerNode(value));
        return getThis();
    }

    /**
     * Add the operator op to the expression
     * @param op the enum of the operator to add
     * @return
     */
    public T operator(Operator op) {
        switch (op) {
        case EQ:
            predicateParser.eq();
            break;
        case LIKE:
            predicateParser.like();
            break;
        case LT:
            predicateParser.lt();
            break;
        case LTE:
            predicateParser.lte();
            break;
        case GT:
            predicateParser.gt();
            break;
        case GTE:
            predicateParser.gte();
            break;
        case NE:
            predicateParser.neq();
            break;
        }

        return getThis();
    }
}