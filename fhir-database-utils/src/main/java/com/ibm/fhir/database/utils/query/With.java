/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.query.expression.StatementRenderer;

/**
 * Represents a WITH clause in a select statement
 */
public class With {
    private final Select selectClause;
    private final Alias alias;

    /**
     * Canonical constructor
     * @param selectClause
     * @param alias
     */
    public With(Select selectClause, Alias alias) {
        this.selectClause = selectClause;
        this.alias = alias;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("WITH ")
            .append(alias.toString())
            .append(" AS (")
            .append(selectClause.toString())
            .append(")");
        return result.toString();
    }

    /**
     * @return the selectClause
     */
    public Select getSelectClause() {
        return selectClause;
    }

    /**
     * @return the alias
     */
    public Alias getAlias() {
        return alias;
    }

    /**
     * Render this WITH clause
     * @param <T>
     * @param renderer
     * @return
     */
    public <T> T render(StatementRenderer<T> renderer) {
        T sub = selectClause.render(renderer);
        T aliasValue = alias.render(renderer);
        return renderer.with(sub, aliasValue);
    }
}
