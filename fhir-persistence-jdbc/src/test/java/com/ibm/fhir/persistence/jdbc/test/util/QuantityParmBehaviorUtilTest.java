/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.fhir.persistence.jdbc.util.type.NumberParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.QuantityParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.parameters.QueryParameter;

public class QuantityParmBehaviorUtilTest {
    private static final Logger log = java.util.logging.Logger.getLogger(QuantityParmBehaviorUtilTest.class.getName());
    private static final Level LOG_LEVEL = Level.FINE;

    //---------------------------------------------------------------------------------------------------------
    // Supporting Methods: 
    @BeforeClass
    public static void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("quantity");
    }

    @AfterClass
    public static void after() throws FHIRException {
        FHIRRequestContext.get().setTenantId("default");
    }

    private QueryParameterValue generateParameterValue(String value, SearchConstants.Prefix prefix) {
        QueryParameterValue parameterValue = new QueryParameterValue();
        parameterValue.setPrefix(prefix);
        parameterValue.setValueNumber(new BigDecimal(value));
        parameterValue.setValueCode("code");
        parameterValue.setValueSystem("system");
        return parameterValue;
    }

    private QueryParameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String code, String... values) {
        QueryParameter parameter = new QueryParameter(SearchConstants.Type.QUANTITY, code, modifier, null);
        for (String value : values) {
            parameter.getValues().add(generateParameterValue(value, prefix));
        }
        return parameter;
    }

    private QueryParameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String value) {
        return generateParameter(prefix, modifier, "Quantity", new String[] { value });
    }

    private QueryParameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String... values) {
        return generateParameter(prefix, modifier, "Quantity", values);
    }

    private void runTest(QueryParameter queryParm, List<Object> expectedBindVariables, String expectedSql, boolean sendNull)
            throws Exception {
        runTest(queryParm, expectedBindVariables, expectedSql, "Basic", sendNull);
    }

    private void runTest(QueryParameter queryParm, List<Object> expectedBindVariables, String expectedSql,
            String tableAlias, boolean sendNull)
            throws Exception {
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        List<Object> actualBindVariables = new ArrayList<>();

        QuantityParmBehaviorUtil behavior = new QuantityParmBehaviorUtil();
        behavior.executeBehavior(actualWhereClauseSegment, queryParm, actualBindVariables,
                tableAlias, generateDao(sendNull));

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        for (Object o : expectedBindVariables) {
            actualBindVariables.remove(o);
        }

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("leftover - bind variables -> " + actualBindVariables);
        }
        assertEquals(actualBindVariables.size(), 0);

    }

    /*
     * checks the generated sql
     */
    public void runSystemTest(boolean sendNull, String system, List<Object> expectedBindVariables, String expectedSql)
            throws FHIRPersistenceException {
        ParameterDAO parameterDao = generateDao(sendNull);
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        String tableAlias = "BASIC";
        List<Object> actualBindVariables = new ArrayList<>();

        QuantityParmBehaviorUtil behavior = new QuantityParmBehaviorUtil();
        behavior.addSystemIfPresent(parameterDao, actualWhereClauseSegment, tableAlias, actualBindVariables, system);

        log.fine("whereClauseSegment -> " + actualWhereClauseSegment.toString());
        log.fine("bind variables -> " + actualBindVariables);

        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        for (Object o : expectedBindVariables) {
            actualBindVariables.remove(o);
        }

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("leftover - bind variables -> " + actualBindVariables);
        }
        assertEquals(actualBindVariables.size(), 0);
    }

    /*
     * checks the generated sql
     */
    public void runCodeTest(String code, List<Object> expectedBindVariables, String expectedSql)
            throws FHIRPersistenceException {
        StringBuilder actualWhereClauseSegment = new StringBuilder();
        String tableAlias = "BASIC";
        List<Object> actualBindVariables = new ArrayList<>();

        QuantityParmBehaviorUtil behavior = new QuantityParmBehaviorUtil();
        behavior.addCodeIfPresent(actualWhereClauseSegment, tableAlias, actualBindVariables, code);

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("whereClauseSegment -> " + actualWhereClauseSegment.toString());
            log.info("bind variables -> " + actualBindVariables);
        }
        assertEquals(actualWhereClauseSegment.toString(), expectedSql);

        for (Object o : expectedBindVariables) {
            actualBindVariables.remove(o);
        }

        if (log.isLoggable(LOG_LEVEL)) {
            log.info("leftover - bind variables -> " + actualBindVariables);
        }
        assertEquals(actualBindVariables.size(), 0);
    }

    //------------------------------------------------------------------
    // To enable mock replacement the method generateDao is here 
    private ParameterDAO generateDao(boolean sendNull) {
        return new ParameterDAO() {

            @Override
            public Connection getConnection() throws FHIRPersistenceDBConnectException {
                return null;
            }

            @Override
            public boolean isDb2Database() {
                return false;
            }

            @Override
            public Map<String, Integer> readAllSearchParameterNames()
                    throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
                return null;
            }

            @Override
            public Map<String, Integer> readAllCodeSystems()
                    throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
                return null;
            }

            @Override
            public int readOrAddParameterNameId(String parameterName)
                    throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
                return 0;
            }

            @Override
            public Integer readParameterNameId(String parameterName)
                    throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
                return null;
            }

            @Override
            public int readOrAddCodeSystemId(String systemName)
                    throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
                return 0;
            }

            @Override
            public Integer readCodeSystemId(String systemName)
                    throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
                if (sendNull) {
                    return null;
                }
                return 1;
            }

            @Override
            public int acquireParameterNameId(String parameterName) throws FHIRPersistenceException {
                return 0;
            }

            @Override
            public int acquireCodeSystemId(String codeSystemName) throws FHIRPersistenceException {
                return 0;
            }

            @Override
            public void addCodeSystemsCacheCandidate(String codeSystemName, Integer codeSystemId)
                    throws FHIRPersistenceException {
                // do nothing
            }

            @Override
            public void addParameterNamesCacheCandidate(String parameterName, Integer parameterId)
                    throws FHIRPersistenceException {
                // do nothing
            }
        };
    }

    //---------------------------------------------------------------------------------------------------------
    @Test
    public void testPrecisionWithExact() throws Exception {

    }

    @Test
    public void testAddSystemIfPresent() throws FHIRPersistenceException {
        String expectedSql = " AND BASIC.CODE_SYSTEM_ID = ?";
        boolean sendNull = false;
        String system = "target";
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(1);
        runSystemTest(sendNull, system, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddSystemIfPresentNotFound() throws FHIRPersistenceException {
        String expectedSql = " AND BASIC.CODE_SYSTEM_ID = ?";
        boolean sendNull = true;
        String system = "target";
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(-1);
        runSystemTest(sendNull, system, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddSystemIfPresentEmpty() throws FHIRPersistenceException {
        String expectedSql = "";
        boolean sendNull = true;
        String system = "";
        List<Object> expectedBindVariables = new ArrayList<>();
        runSystemTest(sendNull, system, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddSystemIfPresentNull() throws FHIRPersistenceException {
        String expectedSql = "";
        boolean sendNull = true;
        String system = null;
        List<Object> expectedBindVariables = new ArrayList<>();
        runSystemTest(sendNull, system, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddSystemIfPresentWithNonNullCache() throws FHIRPersistenceException {
        CodeSystemsCache.putCodeSystemId("quantity~default", "system-example-quantity", 1);
        String expectedSql = " AND BASIC.CODE_SYSTEM_ID = ?";
        boolean sendNull = true;
        String system = "system-example-quantity";
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(1);
        runSystemTest(sendNull, system, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddCodeIfPresent() throws FHIRPersistenceException {
        String expectedSql = " AND BASIC.CODE = ?";
        String code = "target";
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add("target");
        runCodeTest(code, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddCodeIfPresentEmpty() throws FHIRPersistenceException {
        String expectedSql = "";
        String code = "";
        List<Object> expectedBindVariables = new ArrayList<>();
        runCodeTest(code, expectedBindVariables, expectedSql);
    }

    @Test
    public void testAddCodeIfPresentNull() throws FHIRPersistenceException {
        String expectedSql = "";
        String code = null;
        List<Object> expectedBindVariables = new ArrayList<>();
        runCodeTest(code, expectedBindVariables, expectedSql);
    }

    @Test
    public void testHandleQuantityRangeComparisonWithExact() throws Exception {
        // gt - Greater Than
        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.GT, null, "1e3");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql =
                " AND (((Basic.QUANTITY_VALUE > ? OR Basic.QUANTITY_VALUE_HIGH > ?) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // lt - Less Than
        queryParm             = generateParameter(SearchConstants.Prefix.LT, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                " AND (((Basic.QUANTITY_VALUE < ? OR Basic.QUANTITY_VALUE_LOW < ?) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // ge - Greater than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.GE, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                " AND (((Basic.QUANTITY_VALUE >= ? OR Basic.QUANTITY_VALUE_HIGH >= ?) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // le - Less than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.LE, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                " AND (((Basic.QUANTITY_VALUE <= ? OR Basic.QUANTITY_VALUE_LOW <= ?) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // sa - starts after
        queryParm             = generateParameter(SearchConstants.Prefix.SA, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                " AND ((Basic.QUANTITY_VALUE_LOW > ? AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // eb - Ends before
        queryParm             = generateParameter(SearchConstants.Prefix.EB, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                " AND ((Basic.QUANTITY_VALUE_HIGH < ? AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    @Test
    public void testPrecisionWithEqual() throws Exception {
        // in this case, our code if missing Prefix, it injects EQ.
        // therefore this test case tests two conditions 
        // Condition:
        //  [parameter]=100
        //  [parameter]=eq100

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.EQ, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql = " AND ((((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) "
                + "OR (Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    @Test
    public void testPrecisionWithMultipleValuesForEqual() throws Exception {
        // in this case, our code if missing Prefix, it injects EQ.
        // therefore this test case tests two conditions 
        // Condition:
        //  [parameter]=100,1.00
        //  [parameter]=eq100,1.00

        /*
         * We want this SQL generated which appropriately conditions the multiple values
         * together.
         * 
         * <pre>
         * AND (
         * (
         * (
         * (
         * Basic.QUANTITY_VALUE > ?
         * AND Basic.QUANTITY_VALUE <= ?
         * )
         * OR (
         * Basic.QUANTITY_VALUE_LOW < ?
         * AND Basic.QUANTITY_VALUE_HIGH >= ?
         * )
         * )
         * AND Basic.CODE_SYSTEM_ID = ?
         * AND Basic.CODE = ?
         * )
         * OR (
         * (
         * (
         * Basic.QUANTITY_VALUE > ?
         * AND Basic.QUANTITY_VALUE <= ?
         * )
         * OR (
         * Basic.QUANTITY_VALUE_LOW < ?
         * AND Basic.QUANTITY_VALUE_HIGH >= ?
         * )
         * )
         * AND Basic.CODE_SYSTEM_ID = ?
         * AND Basic.CODE = ?
         * )
         * )
         * </pre>
         */

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.EQ, null, new String[] { "100", "1.00" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("99.5"));
        expectedBindVariables.add(new BigDecimal("100.5"));
        expectedBindVariables.add(new BigDecimal("99.5"));
        expectedBindVariables.add(new BigDecimal("100.5"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        expectedBindVariables.add(new BigDecimal("0.995"));
        expectedBindVariables.add(new BigDecimal("1.005"));
        expectedBindVariables.add(new BigDecimal("0.995"));
        expectedBindVariables.add(new BigDecimal("1.005"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = " AND ((((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) OR "
                + "(Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?) "
                + "OR "
                + "(((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) OR "
                + "(Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    /*
     * we want to make sure the equalclause is well balanced
     * <pre>
     * (
     * (
     * Basic.QUANTITY_VALUE > ?
     * AND Basic.QUANTITY_VALUE <= ?
     * )
     * OR (
     * Basic.QUANTITY_VALUE_LOW < ?
     * AND Basic.QUANTITY_VALUE_HIGH >= ?
     * )
     * )
     * </pre>
     */
    @Test
    public void testEqualsClauseBuilder() {
        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();
        String tableAlias = "Basic";
        BigDecimal lowerBound = new BigDecimal("1");
        BigDecimal upperBound = new BigDecimal("2");
        NumberParmBehaviorUtil.buildEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, JDBCConstants.QUANTITY_VALUE,
                lowerBound, upperBound);

        assertEquals(whereClauseSegment.toString(),
                "((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) OR (Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?))");
    }

    @Test
    public void testPrecisionWithNotEqual() throws Exception {
        // Condition:
        //  [parameter]=ne100

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.NE, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = " AND ((((Basic.QUANTITY_VALUE < ? OR Basic.QUANTITY_VALUE >= ?) "
                + "OR (Basic.QUANTITY_VALUE_LOW < ? OR Basic.QUANTITY_VALUE_HIGH > ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    @Test
    public void testPrecisionWithApprox() throws Exception {
        // Condition:
        //  [parameter]=ap100

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = " AND ((((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) "
                + "OR (Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH >= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    @Test
    public void testPrecisionWithApproxNumberOne() throws Exception {
        // Condition:
        //  [parameter]=ap1

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, "1");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("0.4"));
        expectedBindVariables.add(new BigDecimal("1.6"));
        expectedBindVariables.add(new BigDecimal("1.5"));
        expectedBindVariables.add(new BigDecimal("0.5"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = " AND ((((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) "
                + "OR (Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH >= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    @Test
    public void testPrecisionWithMultipleSameApprox() throws Exception {
        // Condition:
        //  [parameter]=ap100,ap100
        // It should de-dupe

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, new String[] { "100", "100" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql = " AND ((((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) "
                + "OR (Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH >= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    @Test
    public void testPrecisionWithMultipleDifferentApprox() throws Exception {
        // Condition:
        //  [parameter]=ap100,ap100.00

        // expectedBindVariables are pivoted on 100
        // It should NOT de-dupe

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, new String[] { "100", "100.00" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(100));
        expectedBindVariables.add(new BigDecimal(100));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("99.5"));
        expectedBindVariables.add(new BigDecimal("100.5"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("100.005"));
        expectedBindVariables.add(new BigDecimal("99.995"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql = " AND ((((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) OR "
                + "(Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH >= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?) "
                + "OR "
                + "(((Basic.QUANTITY_VALUE >= ? AND Basic.QUANTITY_VALUE < ?) OR "
                + "(Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH >= ?)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)))";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }
}