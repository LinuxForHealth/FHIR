/*
 * (C) Copyright IBM Corp. 2019, 2022
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

    @Test
    public void selectSingleWith() {

        Select sub = Select.select()
                .addColumn("param.logical_resource_id", alias("logical_resource_id"))
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "PARAMETER_NAME_ID").eq(1274)
                .build();
        

        // Build a select statement including a WITH clause
        Select select = Select.select()
                .with(sub, alias("w1"))
                .addColumn("1", alias("one"))
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .from("w1")
                .where("param", "PARAMETER_NAME_ID").eq(1274)
                .and("param", "logical_resource_id").eq("w1", "logical_resource_id")
                .build();


        // And make sure it renders to the correct string
        final String EXPECTED = ""
                + "WITH w1 AS (SELECT param.logical_resource_id AS logical_resource_id FROM Patient_TOKEN_VALUES_V AS param WHERE param.PARAMETER_NAME_ID = 1274)"
                + " SELECT 1 AS one "
                + "FROM Patient_TOKEN_VALUES_V AS param, w1 "
                + "WHERE param.PARAMETER_NAME_ID = 1274"
                + " AND param.logical_resource_id = w1.logical_resource_id";
        assertEquals(select.toString(), EXPECTED);
    }
    
}