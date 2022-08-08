/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.expression;

import java.math.BigDecimal;
import java.time.Instant;

import org.linuxforhealth.fhir.database.utils.query.Alias;
import org.linuxforhealth.fhir.database.utils.query.WhereFragment;
import org.linuxforhealth.fhir.database.utils.query.node.ACosExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.BigDecimalBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.BindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.CosExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.DoubleBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.ExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.InstantBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.IntegerBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.LongBindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.SinExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.StringBindMarkerNode;

/**
 * Collection of utility functions for building predicate expressions. Consumers
 * should static import the functions they require for cleaner syntax.
 */
public class ExpressionSupport {

    /**
     * Private constructor
     */
    private ExpressionSupport() {
        // prevent instantiation
    }

    /**
     * Creates a {@link WhereFragment} starting with a simple column reference
     * for use in a join clause
     * @param col
     * @return
     */
    public static WhereFragment on(String col) {
        WhereFragment where = new WhereFragment();
        where.col(col);
        return where;
    }

    /**
     * Creates a {@link WhereFragment} starting with a qualified column reference
     * for use in a join clause
     * @param tableAlias
     * @param col
     * @return
     */
    public static WhereFragment on(String tableAlias, String col) {
        WhereFragment where = new WhereFragment();
        where.col(tableAlias, col);
        return where;
    }

    /**
     * Factory function for creating an {@link Alias} from a string
     *
     * @param aliasStr
     * @return
     */
    public static Alias alias(String aliasStr) {
        return new Alias(aliasStr);
    }

    /**
     * Factory function for creating a {@link ColumnRef} instance
     * from a string
     * @param ref
     * @return
     */
    public static ColumnRef col(String ref) {
        return new ColumnRef(ref);
    }

    public static ColumnRef col(String alias, String name) {
        return new ColumnRef(alias + "." + name);
    }

    /**
     * Simple container for a string value to make overloading of methods more natural
     * @param value
     * @return
     */
    public static LiteralString string(String value) {
        return new LiteralString(value);
    }

    /**
     * Factory function for creating a BindMarkerNode instance
     * @return
     */
    public static BindMarkerNode bind(String value) {
        return new StringBindMarkerNode(value);
    }

    /**
     * Factory function to create a BindMarkerNode with the
     * given value
     * @param value
     * @return
     */
    public static BindMarkerNode bind(Long value) {
        return new LongBindMarkerNode(value);
    }

    /**
     * Factory function to create a BindMarkerNode with the
     * given value
     * @param value
     * @return
     */
    public static BindMarkerNode bind(Integer value) {
        return new IntegerBindMarkerNode(value);
    }

    /**
     * Factory function to create a BindMarkerNode with the
     * given value
     * @param value
     * @return
     */
    public static BindMarkerNode bind(Double value) {
        return new DoubleBindMarkerNode(value);
    }

    /**
     * Factory function to create a BindMarkerNode with the
     * given value
     * @param value
     * @return
     */
    public static BindMarkerNode bind(Instant value) {
        return new InstantBindMarkerNode(value);
    }

    /**
     * Factory function to create a BindMarkerNode with the
     * given value
     * @param value
     * @return
     */
    public static BindMarkerNode bind(BigDecimal value) {
        return new BigDecimalBindMarkerNode(value);
    }

    /**
     * Factory function to create a SinExpNode with the
     * given value
     * @param value
     * @return
     */
    public static ExpNode sin(ExpNode arg) {
        return new SinExpNode(arg);
    }

    /**
     * Factory function to create a CosExpNode with the
     * given value
     * @param value
     * @return
     */
    public static ExpNode cos(ExpNode arg) {
        return new CosExpNode(arg);
    }

    /**
     * Factory function to create a ACosExpNode with the
     * given value
     * @param value
     * @return
     */
    public static ExpNode acos(ExpNode arg) {
        return new ACosExpNode(arg);
    }

    /**
     * Factory function to create the expression
     *   {alias}.IS_DELETED = 'N'
     * @param value
     * @return
     */
    public static ExpNode isDeleted(String alias) {
        return new WhereFragment().col(alias, "IS_DELETED").eq(string("N")).getExpression();
    }
}