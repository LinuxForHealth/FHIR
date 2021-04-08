/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.util.List;

/**
 * Represents the SQL COALESCE function.
 */
public class Coalesce extends VarArgFunction {
    private static final String FUNCTION_NAME = "COALESCE";

    /**
     * Public constructor
     * @param args
     */
    public Coalesce(List<ExpressionNode> args) {
        super(args);
    }

    @Override
    protected String getName() {
        return FUNCTION_NAME;
    }
}