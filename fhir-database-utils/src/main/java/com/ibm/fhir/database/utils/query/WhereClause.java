/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.query.expression.DebugExpNodeVisitor;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.ExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.PredicateParser;

/**
 * The WhereClause SQL definition
 */
public class WhereClause {
    private final PredicateParser predicateParser;

    public WhereClause() {
        this.predicateParser = new PredicateParser();
    }

    /**
     * Getter for the predicateParser owned by this WhereClause
     * @return
     */
    public PredicateParser getPredicateParser() {
        return this.predicateParser;
    }

    @Override
    public String toString() {
        // Render the predicate expression tree as a string
        ExpNode predicate = predicateParser.parse();
        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        StringBuilder result = new StringBuilder();
        result.append("WHERE ");
        result.append(predicate.visit(visitor));
        return result.toString();
    }

    /**
     * Render a string which can be used for debugging select statement strings
     * @return
     */
    public String toDebugString() {
        // Render the predicate expression tree as a string
        ExpNode predicate = predicateParser.parse();
        DebugExpNodeVisitor visitor = new DebugExpNodeVisitor();
        StringBuilder result = new StringBuilder();
        result.append("WHERE ");
        result.append(predicate.visit(visitor));
        return result.toString();
    }

    /**
     * Visit the nodes of the predicate currently described by the predicateParser
     * @param <T>
     * @param visitor
     * @return
     */
    public <T> T visit(ExpNodeVisitor<T> visitor) {
        ExpNode predicate = predicateParser.parse();
        return predicate.visit(visitor);
    }
}