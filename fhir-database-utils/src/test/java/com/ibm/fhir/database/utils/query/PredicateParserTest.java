/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.PredicateParser;

/**
 * Test the {@link PredicateParser}
 */
public class PredicateParserTest {

    @Test
    public void expression1() {
        PredicateParser pp = new PredicateParser();
        pp.column("foo_id");
        pp.eq();
        pp.literal(1L);
        pp.add();
        pp.column("x");

        ExpNode expression = pp.parse();

        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        String exprString = expression.visit(visitor);
        assertEquals(exprString, "foo_id = 1 + x");
    }

    @Test
    public void expressionParens() {
        PredicateParser pp = new PredicateParser();
        pp.column("foo_id");
        pp.eq();
        pp.leftParen();
        pp.literal(1L);
        pp.add();
        pp.column("x");
        pp.rightParen();

        ExpNode expression = pp.parse();

        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        String exprString = expression.visit(visitor);
        assertEquals(exprString, "foo_id = (1 + x)");
    }

    @Test
    public void expressionLogic() {
        PredicateParser pp = new PredicateParser();
        pp.column("foo_id");
        pp.eq();
        pp.literal(1234L);
        pp.and();
        pp.column("foo_name");
        pp.eq();
        pp.column("other_name");
        pp.or();
        pp.column("something");
        pp.eq();
        pp.literal("blank");

        ExpNode expression = pp.parse();

        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        String exprString = expression.visit(visitor);
        assertEquals(exprString, "foo_id = 1234 AND foo_name = other_name OR something = 'blank'");
    }

    @Test
    public void expressionLogicWithParen() {
        PredicateParser pp = new PredicateParser();
        pp.column("foo_id");
        pp.eq();
        pp.literal(1234L);
        pp.and();
        pp.leftParen();
        pp.column("foo_name");
        pp.eq();
        pp.column("other_name");
        pp.or();
        pp.column("something");
        pp.eq();
        pp.literal("blank");
        pp.rightParen();

        ExpNode expression = pp.parse();

        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        String exprString = expression.visit(visitor);
        assertEquals(exprString, "foo_id = 1234 AND (foo_name = other_name OR something = 'blank')");
    }
}