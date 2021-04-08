/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.time.Instant;

import com.ibm.fhir.database.utils.query.Alias;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.DoubleBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.InstantBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.IntegerBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.LongBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.StringBindMarkerNode;

/**
 * Collection of utility functions for building predicate expressions. Consumers
 * should static import the functions they require for cleaner syntax.
 */
public class ExpressionUtils {

    /**
     * Factory function to create a new {@link PredicateAdapter} using the given {@link Predicate}
     * as the current expression.
     * @return
     */
    public static PredicateAdapter predicate() {
        return new PredicateAdapter();
    }

    public static PredicateAdapter on(String col) {
        PredicateAdapter pa = new PredicateAdapter();
        pa.col(col);
        return pa;
    }

    public static PredicateAdapter on(String tableAlias, String col) {
        PredicateAdapter pa = new PredicateAdapter();
        pa.col(tableAlias, col);
        return pa;
    }

    /**
     * Factory function to create a new {@link Exists} predicate expression
     * based on the given {@link Select} statement
     * @param s
     * @return
     */
    public static Exists exists(Select s) {
        return new Exists(s);
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

    public static BindMarkerNode bind(Long value) {
        return new LongBindMarkerNode(value);
    }

    public static BindMarkerNode bind(Integer value) {
        return new IntegerBindMarkerNode(value);
    }

    public static BindMarkerNode bind(Double value) {
        return new DoubleBindMarkerNode(value);
    }

    public static BindMarkerNode bind(Instant value) {
        return new InstantBindMarkerNode(value);
    }
}