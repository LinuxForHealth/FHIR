/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import com.ibm.fhir.database.utils.query.Select;

/**
 * An exists clause in a SQL query
 */
public class Exists extends Predicate {

    // The (correlated) select statement typically of the form SELECT 1 FROM ... WHERE ...
    private final Select select;

    public Exists(Select select) {
        this.select = select;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("EXISTS (")
            .append(select.toString())
            .append(")");
        return result.toString();
    }

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.exists(this.select.render(renderer));
    }
}