/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.bind;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * Unit tests for the {@link WhereFragment} class.
 */
public class WhereFragmentTest {

    @Test
    public void likeTest() {
        WhereFragment where = new WhereFragment();
        where.col("tab", "foo").like(bind("hello%")).escape("+");

        ExpNode filter = where.getExpression();
        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        String expression = filter.visit(visitor);
        assertEquals(expression, "tab.foo LIKE ? ESCAPE '+'");
    }
}