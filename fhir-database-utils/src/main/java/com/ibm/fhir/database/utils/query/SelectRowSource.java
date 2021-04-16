/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import com.ibm.fhir.database.utils.query.expression.StringStatementRenderer;

/**
 * Represents a table referenced in the from list
 */
public class SelectRowSource implements RowSource {

    // The sub-query
    private final Select select;

    /**
     * Protected constructor
     *
     * @param subQuery
     * @param alias
     */
    protected SelectRowSource(Select subQuery) {
        this.select = subQuery;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(select.toString());
        result.append(")");

        return result.toString();
    }

    @Override
    public String toPrettyString(boolean pretty) {
        StringStatementRenderer renderer = new StringStatementRenderer(null, pretty);
        return select.render(renderer);
    }
}