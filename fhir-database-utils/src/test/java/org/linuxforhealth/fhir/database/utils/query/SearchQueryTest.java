/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

import static org.linuxforhealth.fhir.database.utils.query.expression.ExpressionSupport.alias;
import static org.linuxforhealth.fhir.database.utils.query.expression.ExpressionSupport.bind;
import static org.linuxforhealth.fhir.database.utils.query.expression.ExpressionSupport.col;
import static org.linuxforhealth.fhir.database.utils.query.expression.ExpressionSupport.isDeleted;
import static org.linuxforhealth.fhir.database.utils.query.expression.ExpressionSupport.on;
import static org.linuxforhealth.fhir.database.utils.query.expression.ExpressionSupport.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.database.utils.derby.DerbyTranslator;
import org.linuxforhealth.fhir.database.utils.query.expression.BindMarkerNodeVisitor;
import org.linuxforhealth.fhir.database.utils.query.expression.StringExpNodeVisitor;
import org.linuxforhealth.fhir.database.utils.query.expression.StringStatementRenderer;
import org.linuxforhealth.fhir.database.utils.query.node.BindMarkerNode;
import org.linuxforhealth.fhir.database.utils.query.node.ColumnExpNode;
import org.linuxforhealth.fhir.database.utils.query.node.ExpNode;

/**
 * Build some queries which are similar to the FHIR search queries, making sure
 * we have support for the different types we need.
 */
public class SearchQueryTest {
    private static final DerbyTranslator TRANSLATOR = new DerbyTranslator();

    /**
     * Simple select statement
     */
    @Test
    public void simpleWhereTest() {

        // Create a simple select statement
        Select select = Select.select("1")
                .from("Patient_TOKEN_VALUES_V", alias("param"))
                .where("param", "PARAMETER_NAME_ID").eq(1274)
                .build();


        // And make sure it renders to the correct string
        final String SQL = "SELECT 1"
                + " FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.PARAMETER_NAME_ID = 1274";
        assertEquals(select.toString(), SQL);
    }

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
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, bindMarkers, false);
        assertEquals(query.render(renderer), SQL);
        assertEquals(bindMarkers.size(), 3);
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
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, bindMarkers, false);
        assertEquals(query.render(renderer), SQL);
        assertEquals(bindMarkers.size(), 3);
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
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, bindMarkers, false);
        assertEquals(select.render(renderer), SQL);
        assertEquals(bindMarkers.size(), 1);
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
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, bindMarkers, false);
        Select statement = query.build();
        final String rendered = statement.render(renderer);
        final String SQL = "SELECT COUNT(*)"
                + " FROM Patient_LOGICAL_RESOURCES AS LR"
                + " WHERE LR.IS_DELETED = 'N'"
                + " AND EXISTS (SELECT 1 FROM Patient_STR_VALUES AS param"
                + " WHERE param.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"
                + " AND param.PARAMETER_NAME_ID = 1274"
                + " AND (param.STR_VALUE = ?))"
                ;
        assertEquals(rendered, SQL);
        assertEquals(bindMarkers.size(), 1);
    }

    @Test
    public void testChainedCompositeLikeBind() {
        // the statement we're trying to build
        final String likeValue = "FOO%";
        final String SQL = "SELECT COUNT(*)" +
                " FROM Encounter_LOGICAL_RESOURCES AS LR0" +
                " WHERE (LR0.IS_DELETED = 'N')" +
                " AND EXISTS (SELECT 1" +
                " FROM Encounter_TOKEN_VALUES_V AS P1" +
                " INNER JOIN Observation_LOGICAL_RESOURCES AS LR1 ON LR1.LOGICAL_ID = P1.TOKEN_VALUE" +
                " AND LR1.VERSION_ID = COALESCE(P1.REF_VERSION_ID,LR1.VERSION_ID)" +
                " AND P1.PARAMETER_NAME_ID = 1149" +
                " AND P1.CODE_SYSTEM_ID = 20129" +
                " AND (LR1.IS_DELETED = 'N')" +
                " WHERE P1.LOGICAL_RESOURCE_ID = LR0.LOGICAL_RESOURCE_ID" +
                " AND (EXISTS (SELECT 1" +
                " FROM Observation_TOKEN_VALUES_V AS comp1" +
                " INNER JOIN Observation_STR_VALUES AS comp2 ON comp2.LOGICAL_RESOURCE_ID = comp1.LOGICAL_RESOURCE_ID" +
                " AND comp2.PARAMETER_NAME_ID = 21329" +
                " AND comp2.COMPOSITE_ID = comp1.COMPOSITE_ID" +
                " AND (comp2.STR_VALUE LIKE ? ESCAPE '+')" +
                " WHERE comp1.LOGICAL_RESOURCE_ID = LR1.LOGICAL_RESOURCE_ID" +
                " AND comp1.PARAMETER_NAME_ID = 21328" +
                " AND (comp1.COMMON_TOKEN_VALUE_ID = 4464))))";

        // Build the inner-most exists clause first. This represents a composite search parameter join
        Select ex2 = Select.select("1")
            .from("Observation_TOKEN_VALUES_V", alias("comp1"))
            .innerJoin("Observation_STR_VALUES", alias("comp2"),
                on("comp2", "LOGICAL_RESOURCE_ID").eq("comp1", "LOGICAL_RESOURCE_ID")
                .and("comp2", "PARAMETER_NAME_ID").eq(21329)
                .and("comp2", "COMPOSITE_ID").eq("comp1", "COMPOSITE_ID")
                .and().leftParen().col("comp2", "STR_VALUE").like(bind(likeValue)).escape("+").rightParen()
                    )
            .where("comp1", "LOGICAL_RESOURCE_ID").eq("LR1", "LOGICAL_RESOURCE_ID")
            .and("comp1", "PARAMETER_NAME_ID").eq(21328)
            .and().leftParen().col("comp1", "COMMON_TOKEN_VALUE_ID").eq(4464).rightParen()
            .build();

        Select ex1 = Select.select("1")
            .from("Encounter_TOKEN_VALUES_V", alias("P1"))
            .innerJoin("Observation_LOGICAL_RESOURCES", alias("LR1"),
                on("LR1", "LOGICAL_ID").eq("P1", "TOKEN_VALUE")
                .and("LR1", "VERSION_ID").eq().coalesce(col("P1", "REF_VERSION_ID"), col("LR1", "VERSION_ID"))
                .and("P1", "PARAMETER_NAME_ID").eq(1149)
                .and("P1", "CODE_SYSTEM_ID").eq(20129)
                .and(isDeleted("LR1"))
                    )
            .where("P1", "LOGICAL_RESOURCE_ID").eq("LR0", "LOGICAL_RESOURCE_ID")
            .and().leftParen().exists(ex2).rightParen()
            .build();

        Select main = Select.select("COUNT(*)")
            .from("Encounter_LOGICAL_RESOURCES", alias("LR0"))
            .where(isDeleted("LR0"))
            .and().exists(ex1)
            .build();

        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, bindMarkers, false);
        final String rendered = main.render(renderer);
        assertEquals(rendered, SQL);

        // Check we get the full list of bind markers
        assertEquals(bindMarkers.size(), 1);

        BindMarkerNode bindMarker = bindMarkers.get(0);
        BindMarkerNodeVisitor v = new BindMarkerNodeVisitor() {

            @Override
            public void bindString(String value) {
                assertEquals(value, likeValue);
            }

            @Override
            public void bindLong(Long value) {
                // not expected
                assertFalse(true);
            }

            @Override
            public void bindInt(Integer value) {
                // not expected
                assertFalse(true);
            }

            @Override
            public void bindInstant(Instant value) {
                // not expected
                assertFalse(true);
            }

            @Override
            public void bindDouble(Double value) {
                // not expected
                assertFalse(true);
            }

            @Override
            public void bindBigDecimal(BigDecimal value) {
                // not expected
                assertFalse(true);
            }
        };
        bindMarker.visit(v);
    }

    /**
     * Simple union all of select statements
     */
    @Test
    public void unionAllTest() {

        List<String> resourceTypes = Arrays.asList("Patient", "Condition", "Observation");
        Select first = null;
        Select previous = null;
        
        // Create a set of selects combined by UNION ALL
        for (String resourceType : resourceTypes) {
            // Create a simple select statement
            Select select = Select.select("1")
                    .from(resourceType + "_TOKEN_VALUES_V", alias("param"))
                    .where("param", "PARAMETER_NAME_ID").eq(1274)
                    .build();
            
            // Link to previous select via UNION ALL
            if (previous != null) {
                previous.setUnionAll(select);
            } else {
                first = select;
            }
            previous = select;
        }

        // And make sure it renders to the correct string
        final String SQL = "SELECT 1"
                + " FROM Patient_TOKEN_VALUES_V AS param"
                + " WHERE param.PARAMETER_NAME_ID = 1274"
                + " UNION ALL"
                + " SELECT 1"
                + " FROM Condition_TOKEN_VALUES_V AS param"
                + " WHERE param.PARAMETER_NAME_ID = 1274"
                + " UNION ALL"
                + " SELECT 1"
                + " FROM Observation_TOKEN_VALUES_V AS param"
                + " WHERE param.PARAMETER_NAME_ID = 1274";
        final List<BindMarkerNode> bindMarkers = new ArrayList<>();
        StringStatementRenderer renderer = new StringStatementRenderer(TRANSLATOR, bindMarkers, false);
        assertEquals(first.render(renderer), SQL);
        assertEquals(bindMarkers.size(), 0);
    }

}