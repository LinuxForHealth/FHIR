/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.query.expression.StatementRenderer;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * Models a JOIN element in the from clause
 */
public class FromJoin extends FromItem {

    private final JoinType joinType;

    private final ExpNode joinOnPredicate;

    // An enumeration of join types
    public enum JoinType {
        INNER_JOIN("INNER JOIN"),
        LEFT_OUTER_JOIN("LEFT OUTER JOIN"),
        FULL_OUTER_JOIN("FULL OUTER JOIN");

        private final String joinText;

        private JoinType(String joinText) {
            this.joinText = joinText;
        }

        @Override
        public String toString() {
            return this.joinText;
        }
    }

    /**
     * Protected constructor
     * @param joinType
     * @param rowSource
     * @param alias
     * @param joinOnPredicate
     */
    protected FromJoin(JoinType joinType, RowSource rowSource, Alias alias, ExpNode joinOnPredicate) {
        super(rowSource, alias);
        this.joinType = joinType;
        this.joinOnPredicate = joinOnPredicate;
    }

    /**
     * Protected constructor for a join which doesn't include an alias
     * @param joinType
     * @param rowSource
     * @param joinOnPredicate
     */
    protected FromJoin(JoinType joinType, RowSource rowSource, ExpNode joinOnPredicate) {
        super(rowSource, null);
        this.joinType = joinType;
        this.joinOnPredicate = joinOnPredicate;
    }

    @Override
    public boolean isAnsiJoin() {
        // We are definitely an ANSI-style join...which is why this exists in the first place
        return true;
    }

    @Override
    public String toString() {
        return toPrettyString(false);
    }

    @Override
    public String toPrettyString(boolean pretty) {
        final StringExpNodeVisitor visitor = new StringExpNodeVisitor(pretty);
        final String joinOnStr = joinOnPredicate.visit(visitor);
        StringBuilder result = new StringBuilder();
        result.append(joinType.toString());
        result.append(" ");
        result.append(super.toPrettyString(pretty));
        if (pretty) {
            result.append(StringExpNodeVisitor.NEWLINE).append("         ");
        }
        result.append(" ON ");
        result.append(joinOnStr);

        return result.toString();
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        T joinFromValue = super.render(renderer);
        T joinOnValue = renderer.render(this.joinOnPredicate);
        final T result;
        switch (joinType) {
        case INNER_JOIN:
            result = renderer.innerJoin(joinFromValue, joinOnValue);
            break;
        case LEFT_OUTER_JOIN:
            result = renderer.leftOuterJoin(joinFromValue, joinOnValue);
            break;
        case FULL_OUTER_JOIN:
            result = renderer.fullOuterJoin(joinFromValue, joinOnValue);
            break;
        default:
            throw new IllegalStateException("Unsupported JOIN type: " + joinType.name());
        }
        return result;
    }
}