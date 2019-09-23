/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.query.Select;

import static com.ibm.fhir.database.utils.query.Select.alias;
import static com.ibm.fhir.database.utils.query.Select.select;
import static com.ibm.fhir.database.utils.query.SqlConstants.*;
import static com.ibm.fhir.database.utils.query.TestConstants.*;

/**
 * @author rarnold
 *
 */
public class QueryTest {

    @Test
    public void plainQuery() {
        Select query = Select.select(FOO_ID, FOO_NAME, FOO_AGE)
                .from(FOO_TAB)
                .where(FOO_AGE + " " + IS_NOT_NULL)
                .build();

        // What our statement should look like
        final String SQL = "SELECT FOO_ID, FOO_NAME, FOO_AGE FROM FOO_TAB WHERE FOO_AGE IS NOT NULL";
        // assertEquals(query.toString(), SQL);
    }

    @Test
    public void groupByQuery() {
        // Find the age of the oldest person by towns with a population of 10 or more
        Select query = Select.select(FOO_TOWN, "MAX(" + FOO_AGE + ")")
            .from(FOO_TAB)
            .where(FOO_AGE + " " + IS_NOT_NULL)
            .groupBy(FOO_TOWN)
            .having("count(*) > 10")
            .build();
        
        // Here's what our SQL should be
        final String SQL = "SELECT FOO_TOWN, MAX(FOO_AGE) FROM FOO_TAB WHERE FOO_AGE IS NOT NULL GROUP BY FOO_TOWN HAVING COUNT(*) > 10";

        // TODO
        // assertEquals(query.toString(), SQL);
    }

    @Test
    public void orderByQuery() {
        Select query = Select.select(FOO_ID, FOO_NAME, FOO_AGE)
                .from(FOO_TAB)
                .orderBy(FOO_ID, FOO_NAME, FOO_AGE)
                .build();

        // What our statement should look like
        final String SQL = "SELECT FOO_ID, FOO_NAME, FOO_AGE FROM FOO_TAB ORDER BY FOO_ID, FOO_NAME, FOO_AGE";
        
        // TODO
        // assertEquals(query.toString(), SQL);
    }

    @Test
    public void fromSubQuery() {
        Select query = select("*")
                .from(select(FOO_ID).from(FOO_TAB).build(), alias("sub"))
                .build();

        // What our statement should look like
        final String SQL = "SELECT FOO_ID, FOO_NAME, FOO_AGE FROM FOO_TAB ORDER BY FOO_ID, FOO_NAME, FOO_AGE";
        
        // TODO
        // assertEquals(query.toString(), SQL);
    }

}
