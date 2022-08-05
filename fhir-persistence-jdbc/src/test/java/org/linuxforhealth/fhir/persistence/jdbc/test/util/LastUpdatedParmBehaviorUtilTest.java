/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.linuxforhealth.fhir.persistence.jdbc.test.util.ParmBehaviorUtilTestHelper.assertExpectedSQL;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.database.utils.query.WhereFragment;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.persistence.jdbc.util.type.NewLastUpdatedParmBehaviorUtil;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SearchConstants.Prefix;
import org.linuxforhealth.fhir.search.date.DateTimeHandler;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;

public class LastUpdatedParmBehaviorUtilTest {
    //---------------------------------------------------------------------------------------------------------
    // Supporting Methods:
    @BeforeClass
    public void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("date");
    }

    @AfterClass
    public void after() throws FHIRException {
        FHIRRequestContext.get().setTenantId("default");
    }

    private QueryParameterValue generateQueryParameterValue(SearchConstants.Prefix prefix) {
        QueryParameterValue parameterValue = new QueryParameterValue();
        parameterValue.setPrefix(prefix);
        return parameterValue;
    }

    private QueryParameter generateQueryParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String... values) throws FHIRSearchException {
        QueryParameter parameter = new QueryParameter(SearchConstants.Type.DATE, "_lastUpdated", modifier, null);
        for (String value : values) {
            QueryParameterValue parameterValue = generateQueryParameterValue(prefix);
            DateTimeHandler.parse(prefix, parameterValue, value);
            parameter.getValues().add(parameterValue);
        }
        return parameter;
    }

    private void runTest(QueryParameter queryParm, List<Object> expectedBindVariables, String expectedSql)
            throws Exception {
        runTest(queryParm, expectedBindVariables, expectedSql, "Date", false);
    }

    private void runTest(QueryParameter queryParm, List<Object> expectedBindVariables, String expectedSql,
            String tableAlias, boolean approx) throws Exception {
        WhereFragment actualWhereClauseSegment = new WhereFragment();
        NewLastUpdatedParmBehaviorUtil behavior = new NewLastUpdatedParmBehaviorUtil(null);
        behavior.executeBehavior(actualWhereClauseSegment, queryParm);
        assertExpectedSQL(actualWhereClauseSegment, expectedSql, expectedBindVariables, approx);
    }
    //---------------------------------------------------------------------------------------------------------

    @Test
    public void testHandleDateRangeComparisonWithExact() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);

        // gt - Greater Than
        QueryParameter queryParm = generateQueryParameter(Prefix.GT, null, vTime);
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(DateTimeHandler.generateUpperBound(Prefix.GT, v, vTime));
        String expectedSql =
                "(LAST_UPDATED > ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // lt - Less Than
        queryParm             = generateQueryParameter(Prefix.LT, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(DateTimeHandler.generateLowerBound(Prefix.LT, v, vTime));
        expectedSql =
                "(LAST_UPDATED < ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // ge - Greater than Equal
        queryParm             = generateQueryParameter(Prefix.GE, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(DateTimeHandler.generateLowerBound(Prefix.GE, v, vTime));
        expectedSql = "(LAST_UPDATED >= ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // le - Less than Equal
        queryParm             = generateQueryParameter(Prefix.LE, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(DateTimeHandler.generateUpperBound(Prefix.LE, v, vTime));
        expectedSql = "(LAST_UPDATED <= ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // sa - starts after
        queryParm             = generateQueryParameter(SearchConstants.Prefix.SA, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(DateTimeHandler.generateUpperBound(Prefix.SA, v, vTime));
        expectedSql = "(LAST_UPDATED > ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // eb - Ends before
        queryParm             = generateQueryParameter(SearchConstants.Prefix.EB, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(DateTimeHandler.generateLowerBound(Prefix.EB, v, vTime));
        expectedSql = "(LAST_UPDATED < ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Instant lower = DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime);
        Instant upper = DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime);

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.EQ, null, vTime);
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(lower);
        expectedBindVariables.add(upper);

        String expectedSql = "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithMultipleValuesForEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Instant lower = DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime);
        Instant upper = DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime);

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.EQ, null, vTime, vTime);
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(lower);
        expectedBindVariables.add(upper);
        expectedBindVariables.add(lower);
        expectedBindVariables.add(upper);

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
        Instant lower = DateTimeHandler.generateLowerBound(Prefix.NE, v, vTime);
        Instant upper = DateTimeHandler.generateUpperBound(Prefix.NE, v, vTime);

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.NE, null, vTime);
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(lower);
        expectedBindVariables.add(upper);

        String expectedSql =
                "((LAST_UPDATED < ? OR LAST_UPDATED > ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithApprox() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.AP, null, vTime);
        List<Object> expectedBindVariables = new ArrayList<>();

        // Because approximate values are relative to "now", this is hard to test.
        // Add some placeholder variables so we can at least assert that the proper number of bind vars are present.
        Instant lowerBound = DateTimeHandler.generateLowerBound(Prefix.AP, v, vTime);
        Instant upperBound = DateTimeHandler.generateUpperBound(Prefix.AP, v, vTime);
        expectedBindVariables.add(lowerBound);
        expectedBindVariables.add(upperBound);

        String expectedSql = "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }

    @Test
    public void testPrecisionWithMultipleDifferentApprox() throws Exception {
        String vTime = "2019-12-11T00:00:00+00:00";
        String vTime2 = "2019-01-11T00:00:00+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        TemporalAccessor v2 = DateTimeHandler.parse(vTime);

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.AP, null, vTime, vTime2);
        List<Object> expectedBindVariables = new ArrayList<>();

        // Because approximate values are relative to "now", this is hard to test.
        // Add some placeholder variables so we can at least assert that the proper number of bind vars are present.
        Instant lowerBound1 = DateTimeHandler.generateLowerBound(Prefix.AP, v, vTime);
        Instant upperBound1 = DateTimeHandler.generateUpperBound(Prefix.AP, v, vTime);
        Instant lowerBound2 = DateTimeHandler.generateLowerBound(Prefix.AP, v2, vTime2);
        Instant upperBound2 = DateTimeHandler.generateUpperBound(Prefix.AP, v2, vTime2);
        expectedBindVariables.add(lowerBound1);
        expectedBindVariables.add(upperBound1);
        expectedBindVariables.add(lowerBound2);
        expectedBindVariables.add(upperBound2);

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
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        TemporalAccessor v2 = DateTimeHandler.parse(vTime);

        QueryParameter queryParm = generateQueryParameter(SearchConstants.Prefix.AP, null, vTime, vTime2);
        List<Object> expectedBindVariables = new ArrayList<>();

        // Because approximate values are relative to "now", this is hard to test.
        // Add some placeholder variables so we can at least assert that the proper number of bind vars are present.
        Instant lowerBound1 = DateTimeHandler.generateLowerBound(Prefix.AP, v, vTime);
        Instant upperBound1 = DateTimeHandler.generateUpperBound(Prefix.AP, v, vTime);
        Instant lowerBound2 = DateTimeHandler.generateLowerBound(Prefix.AP, v2, vTime2);
        Instant upperBound2 = DateTimeHandler.generateUpperBound(Prefix.AP, v2, vTime2);
        expectedBindVariables.add(lowerBound1);
        expectedBindVariables.add(upperBound1);
        expectedBindVariables.add(lowerBound2);
        expectedBindVariables.add(upperBound2);

        String expectedSql =
                "((LAST_UPDATED >= ? AND LAST_UPDATED <= ?)) OR ((LAST_UPDATED >= ? AND LAST_UPDATED <= ?))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }
}