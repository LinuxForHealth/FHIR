/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.Select.alias;

import static com.ibm.fhir.database.utils.query.Select.predicate;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_ID;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_NAME;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_TAB;
import static com.ibm.fhir.database.utils.query.QueryTest.FOO_TOWN;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.query.expression.Predicate;

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

    @Test
    public void whereSelect() {
        // Build a Select statement directly, without using the fluent adapter API
        // (as may be done when traversing another AST, for example
        Select select = new Select();
        select.addColumn(FOO_ID, FOO_ID);
        select.addColumn(FOO_NAME, FOO_NAME);
        select.addColumn(FOO_TOWN, FOO_TOWN);
        select.addTable(FOO_TAB, alias(FOO_TAB));

        // where the fun starts...add a WHERE clause
        Predicate where =
                predicate("FOO_TOWN IS NOT NULL")
                        .and("FOO_NAME IS NOT NULL")
                        .and("length(FOO_TOWN) > 0")
                        .or("1 = 0");

        select.addWhere(where.toString());

        // What our statement should look like
        final String SQL =
                "SELECT FOO_ID.FOO_ID, FOO_NAME.FOO_NAME, FOO_TOWN.FOO_TOWN FROM FOO_TAB AS FOO_TAB" +
                        " WHERE FOO_TOWN IS NOT NULL AND FOO_NAME IS NOT NULL AND length(FOO_TOWN) > 0 AND 1 = 0";
        assertEquals(select.toString(), SQL);
    }
}