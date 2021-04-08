/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a function which can take zero or more arguments
 */
public abstract class FunctionExpression implements ExpressionNode {

    /**
     * Get the name of the function
     * @return
     */
    protected abstract String getName();

    /**
     * Get the arguments list
     * @return
     */
    protected abstract List<ExpressionNode> getArgs();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(getName())
            .append("(")
            .append(getArgs().stream().map(p -> p.toString()).collect(Collectors.joining(", ")))
            .append(")");

        return result.toString();
    }
}
