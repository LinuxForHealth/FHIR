/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.Select.alias;
import static com.ibm.fhir.database.utils.query.Select.predicate;
import static com.ibm.fhir.database.utils.query.TestConstants.*;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.expression.Predicate;

/**
 * Unit-test for direct manipulation of the Select class
 * @author rarnold
 */
public class SelectTest {
    @Test
    public void simpleSelect() {
        // Build a Select statement directly, without using the fluent adapter API
        // (as may be done when traversing another AST, for example
        Select select = new Select();
        select.addColumn(FOO_ID, FOO_ID);
        select.addColumn(FOO_NAME, FOO_NAME);
        select.addColumn(FOO_TOWN, FOO_TOWN);
        select.addTable(FOO_TAB, alias(FOO_TAB));
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
        Predicate where = predicate("FOO_TOWN IS NOT NULL")
                .and("FOO_NAME IS NOT NULL")
                .and("length(FOO_TOWN) > 0")
                .or("1 = 0");
    }

}
