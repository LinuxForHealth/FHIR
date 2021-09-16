/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.jdbc.util.type.LastUpdatedParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.date.DateTimeHandler;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

public class LastUpdatedParmBehaviorUtilTest {
    private static final Logger log =
            java.util.logging.Logger.getLogger(LastUpdatedParmBehaviorUtilTest.class.getName());
    private static final Level LOG_LEVEL = Level.FINE;

    //---------------------------------------------------------------------------------------------------------
    // Supporting Methods:
    @BeforeClass
    public static void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("date");
    }

    @AfterClass
    public static void after() throws FHIRException {
        FHIRRequestContext.get().setTenantId("default");
    }

    private QueryParameterValue generateQueryParameterValue(SearchConstants.Prefix prefix) {
        QueryParameterValue parameterValue = new QueryParameterValue();
        parameterValue.setPrefix(prefix);
        return parameterValue;
    }

    private QueryParameter generateQueryParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String... values) throws FHIRSearchException {
        QueryParameter parameter = new QueryParameter(SearchConstants.Type.DATE, "test-date", modifier, null);
        for (String value : values) {
            QueryParameterValue parameterValue = generateQueryParameterValue(prefix);
            DateTimeHandler.parse(prefix, parameterValue, value);
            parameter.getValues().add(parameterValue);
        }
        return parameter;
    }

    private void runTest(QueryParameter queryParm, List<Timestamp> expectedBindVariables, String expectedSql)
            throws Exception {
        runTest(queryParm, expectedBindVariables, expectedSql, "Date", false);
    }

    private void runTest(QueryParameter queryParm, List<Timestamp> expectedBindVariables, String expectedSql,
            String tableAlias, boolean approx)
            throws Exception {
        if (log.isLoggable(LOG_LEVEL)) {
            log.info("Expected Bind Variables -> " + expectedBindVariables);
        }
        StringBuilder actualWhereClauseSegment = new StringBuilder();

        LastUpdatedParmBehaviorUtil behavior = new LastUpdatedParmBehaviorUtil();
        behavior.executeBehavior(actualWhereClauseSegment, queryParm);
        List<Timestamp> actualBindVariables = behavior.getBindVariables();

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        if (!approx) {
            // TODO ensure they apear in the right order?
            for (Object o : expectedBindVariables) {
                Timestamp t1 = (Timestamp) o;
                String i1 = "" + t1;
                Iterator<Timestamp> ttt = actualBindVariables.iterator();
                while (ttt.hasNext()) {
                    Timestamp t2 = ttt.next();
                    String i2 = "" + t2;
                    if (i1.compareTo(i2) == 0) {
                        ttt.remove();
                    }
                }
            }

            if (log.isLoggable(LOG_LEVEL)) {
                log.info("leftover - bind variables -> " + actualBindVariables);
            }
            assertEquals(actualBindVariables.size(), 0);
        }

    }

    @Test
    public void testHandleDateRangeComparisonWithExact() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);

        // gt - Greater Than
        QueryParameter queryParm = generateQueryParameter(Prefix.GT, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.GT, v, vTime)));
        String expectedSql =
                "(LAST_UPDATED > ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // lt - Less Than
        queryParm             = generateQueryParameter(Prefix.LT, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.LT, v, vTime)));
        expectedSql =
                "(LAST_UPDATED < ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // ge - Greater than Equal
        queryParm             = generateQueryParameter(Prefix.GE, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.GE, v, vTime)));
        expectedSql = "(LAST_UPDATED >= ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // le - Less than Equal
        queryParm             = generateQueryParameter(Prefix.LE, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.LE, v, vTime)));
        expectedSql = "(LAST_UPDATED <= ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // sa - starts after
        queryParm             = generateQueryParameter(SearchConstants.Prefix.SA, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.SA, v, vTime)));
        expectedSql = "(LAST_UPDATED > ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // eb - Ends before
        queryParm             = generateQueryParameter(SearchConstants.Prefix.EB, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.EB, v, vTime)));
        expectedSql = "(LAST_UPDATED < ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp lower = Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.EQ, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(lower);
        String expectedSql = "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithMultipleValuesForEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp lower = Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.EQ, null, vTime, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(lower);
        expectedBindVariables.add(lower);
        expectedBindVariables.add(lower);

        String expectedSql =
                "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?)) OR ((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithNotEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp lower = Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.NE, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(lower);
        expectedBindVariables.add(upper);

        String expectedSql =
                "((LAST_UPDATED < ? AND LAST_UPDATED > ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithApprox() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.AP, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();

        String expectedSql = "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }

    @Test
    public void testPrecisionWithMultipleDifferentApprox() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        String vTime2 = "2019-01-11T00:00:00+00:00";

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.AP, null, vTime, vTime2);
        List<Timestamp> expectedBindVariables = new ArrayList<>();

        String expectedSql =
                "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?)) OR ((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }

    @Test
    public void testPrecisionWithMultipleDifferentApproxUTC() throws Exception {
        String vTime = "2019-12-11T00:00:00Z";
        String vTime2 = "2019-01-11T00:00:00Z";

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.AP, null, vTime, vTime2);
        List<Timestamp> expectedBindVariables = new ArrayList<>();

        String expectedSql =
                "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?)) OR ((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }
}