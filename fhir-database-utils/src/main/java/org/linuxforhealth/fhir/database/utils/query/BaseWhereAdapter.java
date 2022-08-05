/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.database.utils.query.expression.ColumnRef;
import org.linuxforhealth.fhir.database.utils.query.expression.LiteralString;
import org.linuxforhealth.fhir.database.utils.query.node.ACosExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.BigDecimalBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.BindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.CoalesceExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.ColumnExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.CosExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.DoubleBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.ExistsExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.ExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.InstantBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.IntegerBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.LongBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.NotExistsExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.PredicateParser;
import org.linuxforhealth.fhir.database.utils.query.node.RadiansExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.SelectExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.SinExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.StringBindMarkerNode;

/**
 * A piece of a where clause which isn't attached to a {@link Select} statement
 * allowing getThis() bit to be reused where the statement/whereClause isn't
 * yet established.
 */
public abstract class BaseWhereAdapter<T> {

    // The parser used to build the where statement
    private final PredicateParser predicateParser;

    /**
     * Returns the sub-class type-specific instance of the class. Used as
     * the return value in fluent methods.
     * @return
     */
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
    /**
     * Add a literal String value to the expression
     * @param value
     * @return
     */
    public T literal(String value) {
        predicateParser.literal(value);
        return getThis();
    }

    /**
     * Add a literal long value to the expression
     * @param value
     * @return
     */
    public T literal(long value) {
        predicateParser.literal(value);
        return getThis();
    }

    /**
     * Add a literal double value to the expression
     * @param value
     * @return
     */
    public T literal(double value) {
        predicateParser.literal(value);
        return getThis();
    }

    // ### EQUAL ###
    /**
     * Add an equals '=' node to the expression
     * @return
     */
    public T eq() {
        predicateParser.eq();
        return getThis();
    }

    /**
     * Add '= {ref}' to the expression
     * @param ref
     * @return
     */
    public T eq(ColumnRef ref) {
        predicateParser.eq();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add '= {literalValue}' to the expression
     * @param literalValue
     * @return
     */
    public T eq(long literalValue) {
        predicateParser.eq();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add '= {literalValue}' to the expression
     * @param literalValue
     * @return
     */
    public T eq(int literalValue) {
        predicateParser.eq();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    /**
     * Add '= {columnExpression}' to the expression
     * @param literalValue
     * @return
     */
    public T eq(String columnExpression) {
        predicateParser.eq();
        predicateParser.column(columnExpression);
        return getThis();
    }

    /**
     * Add '= {str}' to the expression
     * @param str
     * @return
     */
    public T eq(LiteralString str) {
        predicateParser.eq();
        predicateParser.literal(str.getValue());
        return getThis();
    }

    /**
     * Add '= {tableAlias}.{columnExpression}' to the expression
     * @param literalValue
     * @return
     */
    public T eq(String tableAlias, String columnExpression) {
        predicateParser.eq();
        predicateParser.column(tableAlias, columnExpression);
        return getThis();
    }

    /**
     * Add a bind marker and value to the expression
     * @param bindMarker
     * @return
     */
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

    /**
     * Add AND {predicate} to the expression
     * @param predicate
     * @return
     */
    public T and(ExpNode predicate) {
        predicateParser.and();
        predicateParser.addToken(predicate);
        return getThis();
    }

    /**
     * Add AND {element} to the expression where {element} is typically a simple column name
     */
    public T and(String element) {
        predicateParser.and();
        predicateParser.column(element);
        return getThis();
    }

    /**
     * Add AND {tableAlias}.{columnName} to the expression
     * @param tableAlias
     * @param columnName
     * @return
     */
    public T and(String tableAlias, String columnName) {
        predicateParser.and();
        predicateParser.column(tableAlias, columnName);
        return getThis();
    }

    /**
     * Add AND {ref} to the expression
     * @param ref the column reference
     * @return
     */
    public T and(ColumnRef ref) {
        predicateParser.and();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add OR to the expression
     * @return
     */
    public T or() {
        predicateParser.or();
        return getThis();
    }

    /**
     * Add OR {predicate} to the expression
     * @param predicate
     * @return
     */
    public T or(ExpNode predicate) {
        predicateParser.or();
        predicateParser.addToken(predicate);
        return getThis();
    }

    /**
     * Add OR {element} to the expression where element is typically a column name
     * @param element
     * @return
     */
    public T or(String element) {
        predicateParser.or();
        predicateParser.column(element);
        return getThis();
    }

    /**
     * Add OR {tableAlias}.{columnName} to the expression
     * @param tableAlias
     * @param columnName
     * @return
     */
    public T or(String tableAlias, String columnName) {
        predicateParser.or();
        predicateParser.column(tableAlias, columnName);
        return getThis();
    }

    /**
     * Add OR {ref} to the expression
     * @param ref the column reference
     * @return
     */
    public T or(ColumnRef ref) {
        predicateParser.or();
        predicateParser.column(ref.toString());
        return getThis();
    }

    // ### NOT EQUAL ###
    /**
     * Add != to the expression
     * @return
     */
    public T neq() {
        predicateParser.neq();
        return getThis();
    }

    /**
     * Add != {ref} to the expression
     * @param ref the column reference
     * @return
     */
    public T neq(ColumnRef ref) {
        predicateParser.neq();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add != {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T neq(long literalValue) {
        predicateParser.neq();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add != {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T neq(int literalValue) {
        predicateParser.neq();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    /**
     * Add != {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T neq(String literalValue) {
        predicateParser.neq();
        predicateParser.column(literalValue);
        return getThis();
    }

    /**
     * Add != {str} to the expression
     * @param str a literal string value
     * @return
     */
    public T neq(LiteralString str) {
        predicateParser.neq();
        predicateParser.literal(str.getValue());
        return getThis();
    }

    /**
     * Add a bind marker and its value to the expression
     * @param bindMarker
     * @return
     */
    public T neq(BindMarkerNode bindMarker) {
        predicateParser.neq();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### GREATER THAN ###
    /**
     * Add > to the expression
     * @return
     */
    public T gt() {
        predicateParser.gt();
        return getThis();
    }

    /**
     * Add > {ref} to the expression
     * @param ref the column reference
     * @return
     */
    public T gt(ColumnRef ref) {
        predicateParser.gt();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add > {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T gt(long literalValue) {
        predicateParser.gt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add > {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T gt(int literalValue) {
        predicateParser.gt();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    /**
     * Add > {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T gt(String literalValue) {
        predicateParser.gt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add a bind marker and its value to the expression
     * @param bindMarker
     * @return
     */
    public T gt(BindMarkerNode bindMarker) {
        predicateParser.gt();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### GREATER THAN EQUAL ###
    /**
     * Add >= to the expression
     * @return
     */
    public T gte() {
        predicateParser.gte();
        return getThis();
    }

    /**
     * Add >= {ref} to the expression
     * @param ref the column reference
     * @return
     */
    public T gte(ColumnRef ref) {
        predicateParser.gte();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add >= {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T gte(long literalValue) {
        predicateParser.gte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add >= {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T gte(int literalValue) {
        predicateParser.gte();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    /**
     * Add >= {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T gte(String literalValue) {
        predicateParser.gte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add >= {bindMarker} to the expression
     * @param bindMarker
     * @return
     */
    public T gte(BindMarkerNode bindMarker) {
        predicateParser.gte();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### LESS THAN ###
    /**
     * Add < to the expression
     * @return
     */
    public T lt() {
        predicateParser.lt();
        return getThis();
    }

    /**
     * Add < {ref} to the expression
     * @param ref the column reference
     * @return
     */
    public T lt(ColumnRef ref) {
        predicateParser.lt();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add < {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T lt(long literalValue) {
        predicateParser.lt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add < {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T lt(int literalValue) {
        predicateParser.lt();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    /**
     * Add < {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T lt(String literalValue) {
        predicateParser.lt();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add < {bindMarker} to the expression
     * @param bindMarker
     * @return
     */
    public T lt(BindMarkerNode bindMarker) {
        predicateParser.lt();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### LESS THAN EQUAL ###
    /**
     * Add <= to the expression
     * @return
     */
    public T lte() {
        predicateParser.lte();
        return getThis();
    }

    /**
     * Add <= ref to the expression
     * @param ref
     * @return
     */
    public T lte(ColumnRef ref) {
        predicateParser.lte();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add <= {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T lte(long literalValue) {
        predicateParser.lte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add <= {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T lte(int literalValue) {
        predicateParser.lte();
        predicateParser.literal((long)literalValue);
        return getThis();
    }

    /**
     * Add <= {literalValue} to the expression
     * @param literalValue
     * @return
     */
    public T lte(String literalValue) {
        predicateParser.lte();
        predicateParser.literal(literalValue);
        return getThis();
    }

    /**
     * Add <= {bindMarker} to the expression
     * @param bindMarker
     * @return
     */
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

    /**
     * Add the qualified name {tableAlias}.{expression} to the expression
     * @param tableAlias
     * @param expression
     * @return
     */
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

    /**
     * Add NOT to the expression
     * @param expr
     * @return
     */
    public T not(ExpNode expr) {
        predicateParser.not();
        predicateParser.addToken(expr);
        return getThis();
    }

    /**
     * Add * to the expression
     * @return
     */
    public T mult() {
        predicateParser.mult();
        return getThis();
    }

    /**
     * Add + to the expression
     * @return
     */
    public T add() {
        predicateParser.add();
        return getThis();
    }

    /**
     * Add - to the expression
     * @return
     */
    public T sub() {
        predicateParser.sub();
        return getThis();
    }

    /**
     * Add / to the expression
     * @return
     */
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

    /**
     * Add SIN(arg) to the expression
     * @param arg
     * @return
     */
    public T sin(ExpNode arg) {
        predicateParser.addToken(new SinExpNode(arg));
        return getThis();
    }

    /**
     * Add SIN(arg) to the expression
     * @param arg
     * @return
     */
    public T sin(ColumnRef arg) {
        predicateParser.addToken(new SinExpNode(new ColumnExpNode(arg.getRef())));
        return getThis();
    }

    /**
     * Add COS(arg) to the expression
     * @param arg
     * @return
     */
    public T cos(ExpNode arg) {
        predicateParser.addToken(new CosExpNode(arg));
        return getThis();
    }

    /**
     * Add COS(arg) to the expression
     * @param arg
     * @return
     */
    public T cos(ColumnRef arg) {
        predicateParser.addToken(new CosExpNode(new ColumnExpNode(arg.getRef())));
        return getThis();
    }

    /**
     * Add ACOS(arg) to the expression
     * @param arg
     * @return
     */
    public T acos(ExpNode arg) {
        predicateParser.addToken(new ACosExpNode(arg));
        return getThis();
    }

    /**
     * Add ACOS(arg) to the expression
     * @param arg
     * @return
     */
    public T acos(ColumnRef arg) {
        predicateParser.addToken(new ACosExpNode(new ColumnExpNode(arg.getRef())));
        return getThis();
    }

    /**
     * Add RADIANS(arg) to the expression
     * @param arg
     * @return
     */
    public T radians(ExpNode arg) {
        predicateParser.addToken(new RadiansExpNode(arg));
        return getThis();
    }

    /**
     * Add RADIANS(arg) to the expression
     * @param arg
     * @return
     */
    public T radians(ColumnRef arg) {
        predicateParser.addToken(new RadiansExpNode(new ColumnExpNode(arg.getRef())));
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
     * Add a left paren <code>(</code> to the expression
     */
    public T leftParen() {
        predicateParser.leftParen();
        return getThis();
    }

    /**
     * Add a right paren <code>)</code> to the expression
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

    /**
     * Add IN (123, 124, ...) for a literal list of Long values
     * @param inList
     * @return
     */
    public T inLiteralLong(List<Long> inList) {
        final Long[] literals = inList.stream().toArray(Long[]::new);
        predicateParser.inLiteral(literals);
        return getThis();
    }

    // ### LIKE ###
    /**
     * Add LIKE to the expression
     * @return
     */
    public T like() {
        predicateParser.like();
        return getThis();
    }

    /**
     * Add LIKE {ref} to the expression
     * @param ref
     * @return
     */
    public T like(ColumnRef ref) {
        predicateParser.like();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add LIKE {str} to the expression
     * @param str
     * @return
     */
    public T like(String str) {
        predicateParser.like();
        predicateParser.literal(str);
        return getThis();
    }

    /**
     * Add LIKE {tableAlias}.{columnExpression} to the expression
     * @param tableAlias
     * @param columnExpression
     * @return
     */
    public T like(String tableAlias, String columnExpression) {
        predicateParser.like();
        predicateParser.column(tableAlias, columnExpression);
        return getThis();
    }

    /**
     * Add LIKE ? to the expression
     * @param bindMarker
     * @return
     */
    public T like(BindMarkerNode bindMarker) {
        predicateParser.like();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    // ### ESCAPE ###
    /**
     * Add ESCAPE to the expression
     * @return
     */
    public T escape() {
        predicateParser.escape();
        return getThis();
    }

    /**
     * Add ESCAPE {ref} to the expression
     * @param ref
     * @return
     */
    public T escape(ColumnRef ref) {
        predicateParser.escape();
        predicateParser.column(ref.toString());
        return getThis();
    }

    /**
     * Add ESCAPE {str} to the expression
     * @param str
     * @return
     */
    public T escape(String str) {
        predicateParser.escape();
        predicateParser.literal(str);
        return getThis();
    }

    /**
     * Add ESCAPE {tableAlias}.{columnExpression} to the expression
     * @param tableAlias
     * @param columnExpression
     * @return
     */
    public T escape(String tableAlias, String columnExpression) {
        predicateParser.escape();
        predicateParser.column(tableAlias, columnExpression);
        return getThis();
    }

    /**
     * Add ESCAPE ? to the expression
     * @param bindMarker
     * @return
     */
    public T escape(BindMarkerNode bindMarker) {
        predicateParser.escape();
        predicateParser.bindMarker(bindMarker);
        return getThis();
    }

    /**
     * Add the expression directly without any other operator
     * @param filter
     * @return
     */
    public T filter(ExpNode filter) {
        predicateParser.addToken(filter);
        return getThis();
    }

    /**
     * Add a bind marker and its string value to the expression. Handles null.
     * @param param
     * @return
     */
    public T bind(String param) {
        predicateParser.bindMarker(new StringBindMarkerNode(param));
        return getThis();
    }

    /**
     * Add a bind marker and Long value to the expression. Handles null.
     * @param param
     * @return
     */
    public T bind(Long param) {
        predicateParser.bindMarker(new LongBindMarkerNode(param));
        return getThis();
    }

    /**
     * Add a bind marker and Integer value to the expression. Handles null.
     * @param param
     * @return
     */
    public T bind(Integer param) {
        predicateParser.bindMarker(new IntegerBindMarkerNode(param));
        return getThis();
    }

    /**
     * Add a bind marker and Double value to the expression. Handles null.
     * @param param
     * @return
     */
    public T bind(Double param) {
        predicateParser.bindMarker(new DoubleBindMarkerNode(param));
        return getThis();
    }

    /**
     * Add a bind marker and Instant value to the expression. Handles null.
     * @param param
     * @return
     */
    public T bind(Instant param) {
        predicateParser.bindMarker(new InstantBindMarkerNode(param));
        return getThis();
    }

    /**
     * Add a bind marker and BigDecimal value to the expression. Handles null.
     * @param param
     * @return
     */
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