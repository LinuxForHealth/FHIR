/*
 * (C) Copyright IBM Corp. 2019
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
import com.ibm.fhir.persistence.jdbc.util.type.DateParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.date.DateTimeHandler;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;

public class DateParmBehaviorUtilTest {
    private static final Logger log = java.util.logging.Logger.getLogger(DateParmBehaviorUtilTest.class.getName());
    private static final Level LOG_LEVEL = Level.INFO;

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

    private ParameterValue generateParameterValue(SearchConstants.Prefix prefix) {
        ParameterValue parameterValue = new ParameterValue();
        parameterValue.setPrefix(prefix);
        return parameterValue;
    }

    private Parameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String... values) throws FHIRSearchException {
        Parameter parameter = new Parameter(SearchConstants.Type.DATE, "test-date", modifier, null);
        for (String value : values) {
            ParameterValue parameterValue = generateParameterValue(prefix);
            DateTimeHandler.parse(prefix, parameterValue, value);
            parameter.getValues().add(parameterValue);
        }
        return parameter;
    }

    private void runTest(Parameter queryParm, List<Timestamp> expectedBindVariables, String expectedSql)
            throws Exception {
        runTest(queryParm, expectedBindVariables, expectedSql, "Date", false);
    }

    private void runTest(Parameter queryParm, List<Timestamp> expectedBindVariables, String expectedSql,
            String tableAlias, boolean approx)
            throws Exception {
        if (log.isLoggable(LOG_LEVEL)) {
            log.info("Expected Bind Variables -> " + expectedBindVariables);
        }
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        List<Timestamp> actualBindVariables = new ArrayList<>();

        DateParmBehaviorUtil behavior = new DateParmBehaviorUtil();
        behavior.executeBehavior(actualWhereClauseSegment, queryParm, actualBindVariables,
                tableAlias);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        if (!approx) {
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

    @SuppressWarnings("unused")
    @Test
    public void testHandleDateRangeComparisonWithExact() throws Exception {
        String vTime = "2019-12-11T00:00:00Z+00:00";

        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp value = Timestamp.from(DateTimeHandler.generateValue(v, vTime));
        Timestamp lower = Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        // gt - Greater Than
        Parameter queryParm = generateParameter(SearchConstants.Prefix.GT, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(upper);
        String expectedSql =
                " AND (((Date.DATE_VALUE > ? OR Date.DATE_START > ?))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // lt - Less Than
        queryParm             = generateParameter(SearchConstants.Prefix.LT, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedSql =
                " AND (((Date.DATE_VALUE < ? OR Date.DATE_END < ?))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // ge - Greater than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.GE, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedSql =
                " AND (((Date.DATE_VALUE >= ? OR Date.DATE_START >= ?))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // le - Less than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.LE, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(upper);
        expectedSql =
                " AND (((Date.DATE_VALUE <= ? OR Date.DATE_END <= ?))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // sa - starts after
        queryParm             = generateParameter(SearchConstants.Prefix.SA, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(upper);
        expectedSql =
                " AND (((Date.DATE_VALUE > ? OR Date.DATE_START > ?))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);

        // eb - Ends before
        queryParm             = generateParameter(SearchConstants.Prefix.EB, null, vTime);
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedSql =
                " AND (((Date.DATE_VALUE < ? OR Date.DATE_END < ?))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00Z+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp value = Timestamp.from(DateTimeHandler.generateValue(v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        Parameter queryParm = generateParameter(SearchConstants.Prefix.EQ, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        String expectedSql =
                " AND ((((Date.DATE_VALUE >= ? AND Date.DATE_VALUE <= ?) OR (Date.DATE_START >= ? AND Date.DATE_END <= ?)))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithMultipleValuesForEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00Z+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp value = Timestamp.from(DateTimeHandler.generateValue(v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        Parameter queryParm = generateParameter(SearchConstants.Prefix.EQ, null, vTime, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedBindVariables.add(upper);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);

        String expectedSql =
                " AND ((((Date.DATE_VALUE >= ? AND Date.DATE_VALUE <= ?) OR (Date.DATE_START >= ? AND Date.DATE_END <= ?))) OR (((Date.DATE_VALUE >= ? AND Date.DATE_VALUE <= ?) OR (Date.DATE_START >= ? AND Date.DATE_END <= ?)))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @SuppressWarnings("unused")
    @Test
    public void testPrecisionWithNotEqual() throws Exception {
        String vTime = "2019-12-11T00:00:00Z+00:00";
        TemporalAccessor v = DateTimeHandler.parse(vTime);
        Timestamp value = Timestamp.from(DateTimeHandler.generateValue(v, vTime));
        Timestamp lower = Timestamp.from(DateTimeHandler.generateLowerBound(Prefix.EQ, v, vTime));
        Timestamp upper = Timestamp.from(DateTimeHandler.generateUpperBound(Prefix.EQ, v, vTime));

        Parameter queryParm = generateParameter(SearchConstants.Prefix.NE, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(upper);
        expectedBindVariables.add(upper);
        expectedBindVariables.add(value);
        expectedBindVariables.add(value);

        String expectedSql =
                " AND ((((Date.DATE_VALUE < ? OR Date.DATE_VALUE > ?) OR (Date.DATE_START < ? OR Date.DATE_END > ?)))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql);
    }

    @Test
    public void testPrecisionWithApprox() throws Exception {
        String vTime = "2019-12-11T00:00:00Z+00:00";

        Parameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, vTime);
        List<Timestamp> expectedBindVariables = new ArrayList<>();

        String expectedSql =
                " AND ((((Date.DATE_VALUE >= ? AND Date.DATE_VALUE <= ?) OR (Date.DATE_START >= ? AND Date.DATE_END <= ?)))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }

    @Test
    public void testPrecisionWithMultipleDifferentApprox() throws Exception {
        String vTime = "2019-12-11T00:00:00Z+00:00";
        String vTime2 = "2019-01-11T00:00:00Z+00:00";

        Parameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, vTime, vTime2);
        List<Timestamp> expectedBindVariables = new ArrayList<>();

        String expectedSql =
                " AND ((((Date.DATE_VALUE >= ? AND Date.DATE_VALUE <= ?) OR (Date.DATE_START >= ? AND Date.DATE_END <= ?))) OR (((Date.DATE_VALUE >= ? AND Date.DATE_VALUE <= ?) OR (Date.DATE_START >= ? AND Date.DATE_END <= ?)))))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, "Date", true);
    }
}