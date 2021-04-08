/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;


/**
 * Currently modeled as a unary predicate rather than a literal until we model
 * equality and inequality operators
 */
public class BindPredicate extends Predicate {

    @Override
    public <T> T render(StatementRenderer<T> renderer) {
        return renderer.bind();
    }
}