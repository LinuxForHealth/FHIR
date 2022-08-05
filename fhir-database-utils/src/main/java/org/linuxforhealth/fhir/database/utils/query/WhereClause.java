/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import org.linuxforhealth.fhir.database.utils.query.expression.DebugExpNodeVisitor;
import org.linuxforhealth.fhir.database.utils.query.expression.StringExpNodeVisitor;
import org.linuxforhealth.fhir.database.utils.query.node.ExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.ExpNodeVisitor;
import org.linuxforhealth.fhir.database.utils.query.node.PredicateParser;

/**
 * The WhereClause SQL definition
 */
public class WhereClause {
    private final PredicateParser predicateParser;

    public WhereClause() {
        this.predicateParser = new PredicateParser();
    }

    /**
     * @return true if this where clause does not contain any predicates
     */
    public boolean isEmpty() {
        return this.predicateParser.isEmpty();
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
        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        return renderPredicate(visitor);
    }

    /**
     * Render a string which can be used for debugging select statement strings
     * @return
     */
    public String toDebugString() {
        DebugExpNodeVisitor visitor = new DebugExpNodeVisitor();
        return renderPredicate(visitor);
    }

    /**
     * Render the member predicate expression as a string using the given visitor
     * @param visitor
     * @return
     */
    private String renderPredicate(StringExpNodeVisitor visitor) {
        StringBuilder result = new StringBuilder();
        if (!isEmpty()) {
            ExpNode predicate = predicateParser.parse();
            result.append("WHERE ");
            result.append(predicate.visit(visitor));
        }
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