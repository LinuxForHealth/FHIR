/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.expression;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.query.Select;
import org.linuxforhealth.fhir.database.utils.query.node.ExpNodeVisitor;

/**
 * Renders the expression node tree into a string
 */
public class ColumnExpNodeVisitor implements ExpNodeVisitor<Set<String>> {

    @Override
    public Set<String> paren(Set<String> expr) {
        return expr;
    }

    /**
     * Combine left and right sets
     * @param left
     * @param right
     * @return
     */
    private Set<String> merge(Set<String> left, Set<String> right) {
        Set<String> result = new HashSet<>(left);
        result.addAll(right);
        return result;
    }

    @Override
    public Set<String> and(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> or(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> not(Set<String> exp) {
        return exp;
    }

    @Override
    public Set<String> exists(Set<String> statement) {
        return statement;
    }

    @Override
    public Set<String> notExists(Set<String> statement) {
        return statement;
    }

    @Override
    public Set<String> eq(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> neq(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> gt(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> gte(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> lt(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> lte(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> literal(String value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> literal(Long value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> literal(Double value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> column(String tableAlias, String columnName) {
        HashSet<String> result = new HashSet<>();
        result.add(DataDefinitionUtil.getQualifiedName(tableAlias, columnName));
        return result;
    }

    @Override
    public Set<String> add(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> subtract(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> multiply(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> divide(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> isNull(Set<String> expr) {
        return expr;
    }

    @Override
    public Set<String> isNotNull(Set<String> expr) {
        return expr;
    }

    @Override
    public Set<String> between(Set<String> leftValue, Set<String> rightValue) {
        return merge(leftValue, rightValue);
    }

    @Override
    public Set<String> in(Set<String> leftValue, List<Set<String>> args) {
        Set<String> result = new HashSet<>(leftValue);
        for (Set<String> arg: args) {
            result.addAll(arg);
        }
        return result;
    }

    @Override
    public Set<String> like(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> escape(Set<String> left, Set<String> right) {
        return merge(left, right);
    }

    @Override
    public Set<String> bindMarker(Double value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> bindMarker(Long value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> bindMarker(Integer value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> bindMarker(String value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> bindMarker(Instant value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> select(Select select) {
        return select.visit(this);
    }

    @Override
    public Set<String> coalesce(List<ColumnRef> columnRefs) {
        HashSet<String> result = new HashSet<>();
        for (ColumnRef col: columnRefs) {
            result.add(col.getRef());
        }
        return result;
    }

    @Override
    public Set<String> bindMarker(BigDecimal value) {
        return Collections.emptySet();
    }

    @Override
    public Set<String> cos(Set<String> arg) {
        return arg;
    }

    @Override
    public Set<String> acos(Set<String> arg) {
        return arg;
    }

    @Override
    public Set<String> sin(Set<String> arg) {
        return arg;
    }

    @Override
    public Set<String> radians(Set<String> arg) {
        return arg;
    }
}