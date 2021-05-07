/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.QueryTest.FOO_ID;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_NAME;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_TAB;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_TOWN;
import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.alias;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit-test for direct manipulation of the Select class
 */
public class SelectTest {
    @Test
    public void simpleSelect() {
        // Build a Select statement directly, without using the fluent adapter API
        // (as may be done when traversing another AST, for example
        Select select = new Select();
        select.addColumn(FOO_TAB, FOO_ID);
        select.addColumn(FOO_TAB, FOO_NAME);
        select.addColumn(FOO_TAB, FOO_TOWN);
        select.addTable(FOO_TAB, alias(FOO_TAB));

        final String SQL =
                "SELECT FOO_TAB.FOO_ID, FOO_TAB.FOO_NAME, FOO_TAB.FOO_TOWN FROM FOO_TAB AS FOO_TAB";
        assertEquals(select.toString(), SQL);
    }
}