/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import static com.ibm.fhir.persistence.jdbc.test.util.ParmBehaviorUtilTestHelper.assertExpectedSQL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dto.CommonTokenValue;
import com.ibm.fhir.persistence.jdbc.dto.ResourceReferenceValue;
import com.ibm.fhir.persistence.jdbc.util.type.NewNumberParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.NewQuantityParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

public class QuantityParmBehaviorUtilTest {
    //---------------------------------------------------------------------------------------------------------
    // Supporting Methods:
    @BeforeClass
    public void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("quantity");
    }

    @AfterClass
    public void after() throws FHIRException {
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
            String tableAlias, boolean sendNull) throws Exception {
        WhereFragment actualWhereClauseSegment = new WhereFragment();
        NewQuantityParmBehaviorUtil behavior = new NewQuantityParmBehaviorUtil(mockIdCache(sendNull));
        behavior.executeBehavior(actualWhereClauseSegment, queryParm, tableAlias);
        assertExpectedSQL(actualWhereClauseSegment, expectedSql, expectedBindVariables);
    }

    // the new query builder cannot generate a where fragment that starts with AND
    // and so this adds a dummy "1 = 1" to the lhs of the expression to make it valid
    public void runSystemTest(boolean sendNull, String system, List<Object> expectedBindVariables, String expectedSql)
            throws FHIRPersistenceException {
        JDBCIdentityCache idCache = mockIdCache(sendNull);
        WhereFragment actualWhereClauseSegment = new WhereFragment().literal(1).eq().literal(1);
        String tableAlias = "BASIC";

        NewQuantityParmBehaviorUtil behavior = new NewQuantityParmBehaviorUtil(idCache);
        behavior.addSystemIfPresent(actualWhereClauseSegment, tableAlias, system);
        assertExpectedSQL(actualWhereClauseSegment, "1 = 1" + expectedSql, expectedBindVariables);
    }

    // the new query builder cannot generate a where fragment that starts with AND
    // and so this adds a dummy "1 = 1" to the lhs of the expression to make it valid
    public void runCodeTest(String code, List<Object> expectedBindVariables, String expectedSql)
            throws FHIRPersistenceException {
        WhereFragment actualWhereClauseSegment = new WhereFragment().literal(1).eq().literal(1);
        String tableAlias = "BASIC";

        NewQuantityParmBehaviorUtil behavior = new NewQuantityParmBehaviorUtil(mockIdCache(true));
        behavior.addCodeIfPresent(actualWhereClauseSegment, tableAlias, code);
        assertExpectedSQL(actualWhereClauseSegment, "1 = 1" + expectedSql, expectedBindVariables);
    }

    /**
     * Create and return a mock {@link JDBCIdentityCache}
     * @param sendNull
     * @return
     */
    private JDBCIdentityCache mockIdCache(boolean sendNull) {
        return new JDBCIdentityCache() {
            @Override
            public Integer getResourceTypeId(String resourceType) throws FHIRPersistenceException {
                return null;
            }

            @Override
            public String getResourceTypeName(Integer resourceTypeId) throws FHIRPersistenceException {
                return null;
            }

            @Override
            public Integer getCodeSystemId(String codeSystem) throws FHIRPersistenceException {
                if (sendNull) {
                    return null;
                }
                return 1;
            }

            @Override
            public Integer getCanonicalId(String canonicalValue) throws FHIRPersistenceException {
                return null;
            }

            @Override
            public Integer getParameterNameId(String parameterName) throws FHIRPersistenceException {
                return 0;
            }

            @Override
            public Long getCommonTokenValueId(String codeSystem, String tokenValue) {
                return 0l;
            }

            @Override
            public Set<Long> getCommonTokenValueIds(Collection<CommonTokenValue> tokenValues) {
                return null;
            }

            @Override
            public List<Long> getCommonTokenValueIdList(String tokenValue) {
                return null;
            }

            @Override
            public List<String> getResourceTypeNames() throws FHIRPersistenceException {
                return null;
            }

            @Override
            public List<Integer> getResourceTypeIds() throws FHIRPersistenceException {
                return null;
            }

            @Override
            public Long getLogicalResourceId(String resourceType, String logicalId) throws FHIRPersistenceException {
                return null;
            }

            @Override
            public Set<Long> getLogicalResourceIds(Collection<ResourceReferenceValue> referenceValues) throws FHIRPersistenceException {
                return null;
            }

            @Override
            public List<Long> getLogicalResourceIdList(String logicalId) throws FHIRPersistenceException {
                return null;
            }
        };
    }
    //---------------------------------------------------------------------------------------------------------

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
        String expectedSql = " AND BASIC.CODE_SYSTEM_ID = ?";
        boolean sendNull = false;
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
                "((Basic.QUANTITY_VALUE_HIGH > ? OR Basic.QUANTITY_VALUE_LOW > ? "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND Basic.QUANTITY_VALUE_HIGH IS NULL) "
                + "AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
                "((Basic.QUANTITY_VALUE_LOW < ? OR Basic.QUANTITY_VALUE_HIGH < ? "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND Basic.QUANTITY_VALUE_LOW IS NULL) "
                + "AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // ge - Greater than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.GE, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("5E+2"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                "((Basic.QUANTITY_VALUE_HIGH > ? OR Basic.QUANTITY_VALUE_LOW >= ? "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND Basic.QUANTITY_VALUE_HIGH IS NULL) "
                + "AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // le - Less than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.LE, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1.5E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                "((Basic.QUANTITY_VALUE_LOW < ? OR Basic.QUANTITY_VALUE_HIGH <= ? "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND Basic.QUANTITY_VALUE_LOW IS NULL) "
                + "AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // sa - starts after
        queryParm             = generateParameter(SearchConstants.Prefix.SA, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1.5E+3"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                "(Basic.QUANTITY_VALUE_LOW > ? AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);

        // eb - Ends before
        queryParm             = generateParameter(SearchConstants.Prefix.EB, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("5E+2"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        expectedSql =
                "(Basic.QUANTITY_VALUE_HIGH < ? AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql = "((Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?) "
                + "AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        expectedBindVariables.add(new BigDecimal("0.995"));
        expectedBindVariables.add(new BigDecimal("1.005"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = "((Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?) "
                + "OR ((Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }

    /*
     * we want to make sure the equals clause is well balanced
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
        WhereFragment whereClauseSegment = new WhereFragment();
        String tableAlias = "Basic";
        BigDecimal lowerBound = new BigDecimal("1");
        BigDecimal upperBound = new BigDecimal("2");

        String expectedSql = "(Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_HIGH <= ?)";
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(lowerBound);
        expectedBindVariables.add(upperBound);

        NewNumberParmBehaviorUtil.buildEqualsRangeClause(whereClauseSegment, tableAlias, JDBCConstants.QUANTITY_VALUE,
                lowerBound, upperBound);
        assertExpectedSQL(whereClauseSegment, expectedSql, expectedBindVariables);
    }

    @Test
    public void testPrecisionWithNotEqual() throws Exception {
        // Condition:
        //  [parameter]=ne100

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.NE, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = "((Basic.QUANTITY_VALUE_LOW < ? OR Basic.QUANTITY_VALUE_LOW IS NULL "
                + "OR Basic.QUANTITY_VALUE_HIGH > ? OR Basic.QUANTITY_VALUE_HIGH IS NULL) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = "((Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW <= ? "
                + "OR Basic.QUANTITY_VALUE IS NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_HIGH <= ? "
                + "OR Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_LOW <= ?) "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW IS NULL "
                + "OR Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH IS NULL)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
        expectedBindVariables.add(new BigDecimal("0.4"));
        expectedBindVariables.add(new BigDecimal("1.6"));
        expectedBindVariables.add(new BigDecimal("0.4"));
        expectedBindVariables.add(new BigDecimal("1.6"));
        expectedBindVariables.add(new BigDecimal("0.4"));
        expectedBindVariables.add(new BigDecimal("1.6"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        String expectedSql = "((Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW <= ? "
                + "OR Basic.QUANTITY_VALUE IS NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_HIGH <= ? "
                + "OR Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_LOW <= ?) "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW IS NULL "
                + "OR Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH IS NULL)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql = "((Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW <= ? "
                + "OR Basic.QUANTITY_VALUE IS NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_HIGH <= ? "
                + "OR Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_LOW <= ?) "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW IS NULL "
                + "OR Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH IS NULL)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
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
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(new BigDecimal(89.5));
        expectedBindVariables.add(new BigDecimal(110.5));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");

        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(1);
        expectedBindVariables.add("code");
        String expectedSql = "((Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW <= ? "
                + "OR Basic.QUANTITY_VALUE IS NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_HIGH <= ? "
                + "OR Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_LOW <= ?) "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW IS NULL "
                + "OR Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH IS NULL)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?) "
                + "OR ((Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW <= ? "
                + "OR Basic.QUANTITY_VALUE IS NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_HIGH <= ? "
                + "OR Basic.QUANTITY_VALUE_LOW >= ? AND Basic.QUANTITY_VALUE_LOW <= ?) "
                + "OR Basic.QUANTITY_VALUE IS NOT NULL AND (Basic.QUANTITY_VALUE_HIGH >= ? AND Basic.QUANTITY_VALUE_LOW IS NULL "
                + "OR Basic.QUANTITY_VALUE_LOW <= ? AND Basic.QUANTITY_VALUE_HIGH IS NULL)) AND Basic.CODE_SYSTEM_ID = ? AND Basic.CODE = ?)";
        runTest(queryParm,
                expectedBindVariables,
                expectedSql, false);
    }
}