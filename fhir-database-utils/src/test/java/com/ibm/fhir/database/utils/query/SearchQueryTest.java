/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.alias;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.bind;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.on;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.string;
import static org.testng.Assert.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.ColumnExpNode;
import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * Build some queries which are similar to the FHIR search queries, making sure
 * we have support for the different types we need.
 */
public class SearchQueryTest {

    /**
     * Test modeling of a simple search-like query for a single token parameter
     */
    @Test
    public void searchParameterCount() {

        //      SELECT COUNT(LR.LOGICAL_RESOURCE_ID)
        //        FROM Patient_LOGICAL_RESOURCES LR
        //       WHERE EXISTS (SELECT 1
        //                       FROM Patient_TOKEN_VALUES_V AS param
        //                      WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
        //                        AND param.TOKEN_VALUE = ?
        //                        AND param.PARAMETER_NAME_ID = 1274
        //                        AND param.CODE_SYSTEM_ID = 20016
        //                    )
        //         AND EXISTS (SELECT 1
        //                       FROM Patient_TOKEN_VALUES_V AS param
        //                      WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
        //                        AND param.TOKEN_VALUE = ?
        //                        AND param.PARAMETER_NAME_ID = 1275
        //                        AND param.CODE_SYSTEM_ID = 20016
        //                    )
        //         AND LR.IS_DELETED = 'N'
        //         AND LR.LAST_UPDATED > ?;
        // Generate the parameter sub-selects first for slightly better readability
        Select sub1 = Select.select("1")
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID")
                .and("param", "TOKEN_VALUE").eq(bind("Ford"))
                .and("param", "PARAMETER_NAME_ID").eq(1274)
                .and("param", "CODE_SYSTEM_ID").eq(20016)
                .build();

        Select sub2 = Select.select("1")
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID")
                .and("param", "TOKEN_VALUE").eq(bind("Prefect"))
                .and("param", "PARAMETER_NAME_ID").eq(1275)
                .and("param", "CODE_SYSTEM_ID").eq(20016)
                .build();

        Select query = Select.select("COUNT(LR.LOGICAL_RESOURCE_ID)")
                .from("Patient_LOGICAL_RESOURCES", alias("LR"))
                .where().exists(sub1)
                .and().exists(sub2)
                .and("LR", "IS_DELETED").eq().literal("N")
                .and("LR", "LAST_UPDATED").gte(bind(Instant.now().minusSeconds(3600)))
                .and("LR", "VERSION_ID").isNotNull()
                .build();


        // What our statement should look like. This string must be an exact match
        // with the generated query, so don't mess around with spaces trying to make
        // it look nice!
        final String SQL = "SELECT COUNT(LR.LOGICAL_RESOURCE_ID)"
                + " FROM Patient_LOGICAL_RESOURCES AS LR"
                + " WHERE EXISTS (SELECT 1 FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1274"
                + " AND param.CODE_SYSTEM_ID = 20016)"
                + " AND EXISTS (SELECT 1 FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1275"
                + " AND param.CODE_SYSTEM_ID = 20016)"
                + " AND LR.IS_DELETED = 'N'"
                + " AND LR.LAST_UPDATED >= ?"
                + " AND LR.VERSION_ID IS NOT NULL";
        assertEquals(query.toString(), SQL);
    }

    /**
     * Test modeling of a simple search-like query for a single token parameter
     */
    @Test
    public void searchParameter() {

        //      SELECT LR.LOGICAL_RESOURCE_ID, R.DATA, R.VERSION_ID
        //        FROM Patient_LOGICAL_RESOURCES LR
        //        JOIN Patient_RESOURCES R
        //          ON R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID
        //       WHERE EXISTS (SELECT 1
        //                       FROM Patient_TOKEN_VALUES_V AS param
        //                      WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
        //                        AND param.TOKEN_VALUE = ?
        //                        AND param.PARAMETER_NAME_ID = 1274
        //                        AND param.CODE_SYSTEM_ID = 20016
        //                    )
        //         AND EXISTS (SELECT 1
        //                       FROM Patient_TOKEN_VALUES_V AS param
        //                      WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID
        //                        AND param.TOKEN_VALUE = ?
        //                        AND param.PARAMETER_NAME_ID = 1275
        //                        AND param.CODE_SYSTEM_ID = 20016
        //                    )
        //         AND LR.IS_DELETED = 'N'
        //         AND LR.LAST_UPDATED > ?

        // Generate the parameter sub-selects first for slightly better readability
        Select sub1 = Select.select("1")
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID")
                .and("param", "TOKEN_VALUE").eq(bind("Ford"))
                .and("param", "PARAMETER_NAME_ID").eq(1274)
                .and("param", "CODE_SYSTEM_ID").eq(20016)
                .build();

        final String sub1Expected = "SELECT 1 FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1274"
                + " AND param.CODE_SYSTEM_ID = 20016";
        assertEquals(sub1.toString(), sub1Expected);


        Select sub2 = Select.select("1")
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID")
                .and("param", "TOKEN_VALUE").eq(bind("Prefect"))
                .and("param", "PARAMETER_NAME_ID").eq(1275)
                .and("param", "CODE_SYSTEM_ID").eq(20016)
                .build();

        final String sub2Expected = "SELECT 1 FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1275"
                + " AND param.CODE_SYSTEM_ID = 20016";
        assertEquals(sub2.toString(), sub2Expected);

        Select query = Select.select("LR.LOGICAL_RESOURCE_ID", "R.DATA", "R.VERSION_ID")
                .from("Patient_LOGICAL_RESOURCES", alias("LR"))
                .innerJoin("Patient_RESOURCES", alias("R"), on("R", "RESOURCE_ID").eq("LR", "CURRENT_RESOURCE_ID"))
                .where().exists(sub1)
                .and().exists(sub2)
                .and("LR", "IS_DELETED").eq().literal("N")
                .and("LR", "LAST_UPDATED").gt(bind(Instant.now().minusSeconds(3600)))
                .build();

        // What our statement should look like. This string must be an exact match
        // with the generated query, so don't mess around with spaces trying to make
        // it look nice!
        final String SQL = "SELECT LR.LOGICAL_RESOURCE_ID, R.DATA, R.VERSION_ID"
                + " FROM Patient_LOGICAL_RESOURCES AS LR"
                + " INNER JOIN Patient_RESOURCES AS R"
                + " ON R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID"
                + " WHERE EXISTS (SELECT 1 FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1274"
                + " AND param.CODE_SYSTEM_ID = 20016)"
                + " AND EXISTS (SELECT 1 FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1275"
                + " AND param.CODE_SYSTEM_ID = 20016)"
                + " AND LR.IS_DELETED = 'N'"
                + " AND LR.LAST_UPDATED > ?";
        assertEquals(query.toString(), SQL);
    }

    /**
     * Test modeling of a simple search-like query for a single token parameter
     */
    @Test
    public void multipleWhereTest() {

        // Generate the parameter sub-selects first for slightly better readability
        Select select = Select.select("1")
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID")
                .where("param", "TOKEN_VALUE").eq(bind("Ford"))
                .where("param", "PARAMETER_NAME_ID").eq(1274)
                .where("param", "CODE_SYSTEM_ID").eq(20016)
                .build();


        // What our statement should look like. This string must be an exact match
        // with the generated query, so don't mess around with spaces trying to make
        // it look nice!
        final String SQL = "SELECT 1"
                + " FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.TOKEN_VALUE = ?"
                + " AND param.PARAMETER_NAME_ID = 1274"
                + " AND param.CODE_SYSTEM_ID = 20016";
        assertEquals(select.toString(), SQL);
    }

    @Test
    public void testColumnExp() {
        StringExpNodeVisitor visitor = new StringExpNodeVisitor();
        ColumnExpNode c = new ColumnExpNode("LR", "LOGICAL_RESOURCE_ID");
        assertEquals(c.visit(visitor), "LR.LOGICAL_RESOURCE_ID");
    }

    /**
     * Replicate the steps used by the query builder
     */
    @Test
    public void queryBuilderTest() {
        final String resourceType = "Patient";

        final String lrAliasName = "LR";

        final String xxLogicalResources = resourceType + "_LOGICAL_RESOURCES";
        SelectAdapter query = Select.select("COUNT(*)");
        query.from(xxLogicalResources, alias(lrAliasName))
            .where(lrAliasName, "IS_DELETED").eq(string("N"));

        // create the filter statement for a simple string parameter
        final String xxStrValues = resourceType + "_STR_VALUES";
        final String paramAlias = "param";
        WhereFragment whereFragment = new WhereFragment();
        whereFragment.col(paramAlias, "STR_VALUE").eq(bind("MyName"));
        ExpNode filter = whereFragment.getExpression();

        // Attach an exists clause to the query
        Select exists = Select.select("1")
                .from(xxStrValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(1274L)
                .and(filter)
                .build();
                ;

        // Add the exists to the where clause of the main query which already has a predicate
        // so we need to AND the exists
        query.from().where().and().exists(exists);

        // The count query with one string parameter
        Select statement = query.build();
        final String SQL = "SELECT COUNT(*)"
                + " FROM Patient_LOGICAL_RESOURCES AS LR"
                + " WHERE LR.IS_DELETED = 'N'"
                + " AND EXISTS (SELECT 1 FROM Patient_STR_VALUES AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.PARAMETER_NAME_ID = 1274"
                + " AND (param.STR_VALUE = ?))"
                ;
        assertEquals(statement.toString(), SQL);

        // Check the bind markers
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        statement.gatherBindMarkers(bindMarkers);
        assertEquals(bindMarkers.size(), 1);
    }
}