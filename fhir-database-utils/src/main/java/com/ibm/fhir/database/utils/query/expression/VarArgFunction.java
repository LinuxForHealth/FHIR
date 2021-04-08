/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A function with a variable number of arguments
 */
public abstract class VarArgFunction extends FunctionExpression {

    // The variable list of arguments for this function
    private final List<ExpressionNode> args;

    /**
     * Protected constructor
     * @param args
     */
    protected VarArgFunction(List<ExpressionNode> args) {
        this.args = Collections.unmodifiableList(new ArrayList<>(args));
    }

    @Override
    protected List<ExpressionNode> getArgs() {
        return this.args;
    }
}