/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.test.util;

import static org.linuxforhealth.fhir.persistence.jdbc.test.util.ParmBehaviorUtilTestHelper.assertExpectedSQL;
import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.database.utils.query.WhereFragment;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.MolecularSequence;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.util.type.NewNumberParmBehaviorUtil;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;

/**
 * Unit test for the number behavior utility adapted for the new query builder
 */
public class NumberParmBehaviorUtilTest {
    //---------------------------------------------------------------------------------------------------------
    // Supporting Methods:
    @BeforeClass
    public void before() throws FHIRException {
        FHIRRequestContext.get().setTenantId("number");
    }

    @AfterClass
    public void after() throws FHIRException {
        FHIRRequestContext.get().setTenantId("default");
    }

    private QueryParameterValue generateParameterValue(String value, SearchConstants.Prefix prefix) {
        QueryParameterValue parameterValue = new QueryParameterValue();
        parameterValue.setPrefix(prefix);
        parameterValue.setValueNumber(new BigDecimal(value));
        return parameterValue;
    }

    private QueryParameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String code, String... values) {
        QueryParameter parameter = new QueryParameter(SearchConstants.Type.NUMBER, code, modifier, null);
        for (String value : values) {
            parameter.getValues().add(generateParameterValue(value, prefix));
        }
        return parameter;
    }

    private QueryParameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String value) {
        return generateParameter(prefix, modifier, "precision", new String[] { value });
    }

    private QueryParameter generateParameter(SearchConstants.Prefix prefix, SearchConstants.Modifier modifier,
            String... values) {
        return generateParameter(prefix, modifier, "precision", values);
    }

    private void runTest(QueryParameter queryParm, List<Object> expectedBindVariables, String expectedSql)
            throws FHIRPersistenceException {
        runTest(queryParm, expectedBindVariables, expectedSql, "Basic", Basic.class);
    }

    private void runLowerUpperTest(String value, String lower, String upper) {
        BigDecimal decimal = new BigDecimal(value);
        String actualLower = NewNumberParmBehaviorUtil.generateLowerBound(decimal).toPlainString();
        assertEquals(actualLower, lower);

        String actualUpper = NewNumberParmBehaviorUtil.generateUpperBound(decimal).toPlainString();
        assertEquals(actualUpper, upper);
    }

    private void runTest(QueryParameter queryParm, List<Object> expectedBindVariables, String expectedSql,
            String tableAlias, Class<?> resourceType) throws FHIRPersistenceException {
        WhereFragment actualWhereClauseSegment = new WhereFragment();
        NewNumberParmBehaviorUtil newNumberParmBehaviorUtil = new NewNumberParmBehaviorUtil();
        newNumberParmBehaviorUtil.executeBehavior(actualWhereClauseSegment, queryParm, tableAlias);
        assertExpectedSQL(actualWhereClauseSegment, expectedSql, expectedBindVariables);
    }
    //---------------------------------------------------------------------------------------------------------

    @Test
    public void testPrecisionWithExact() throws FHIRPersistenceException {
        // The spec states:
        // When a comparison prefix in the set lgt, lt, ge, le, sa & eb is provided,
        // the implicit precision of the number is ignored, and they are treated as
        // if they have arbitrarily high precision
        // However there is no 'lgt', means gt? (documented in https://jira.hl7.org/browse/FHIR-21216)

        // gt - Greater Than
        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.GT, null, "1e3");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        String expectedSql = "(Basic.NUMBER_VALUE_HIGH > ? OR Basic.NUMBER_VALUE_LOW > ? "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND Basic.NUMBER_VALUE_HIGH IS NULL)";
        runTest(queryParm, expectedBindVariables, expectedSql);

        // lt - Less Than
        queryParm             = generateParameter(SearchConstants.Prefix.LT, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedSql = "(Basic.NUMBER_VALUE_LOW < ? OR Basic.NUMBER_VALUE_HIGH < ? "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND Basic.NUMBER_VALUE_LOW IS NULL)";
        runTest(queryParm, expectedBindVariables, expectedSql);

        // ge - Greater than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.GE, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("5E+2"));
        expectedSql = "(Basic.NUMBER_VALUE_HIGH > ? OR Basic.NUMBER_VALUE_LOW >= ? "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND Basic.NUMBER_VALUE_HIGH IS NULL)";
        runTest(queryParm, expectedBindVariables, expectedSql);

        // le - Less than Equal
        queryParm             = generateParameter(SearchConstants.Prefix.LE, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1E+3"));
        expectedBindVariables.add(new BigDecimal("1.5E+3"));
        expectedSql = "(Basic.NUMBER_VALUE_LOW < ? OR Basic.NUMBER_VALUE_HIGH <= ? "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND Basic.NUMBER_VALUE_LOW IS NULL)";
        runTest(queryParm, expectedBindVariables, expectedSql);

        // sa - starts after
        queryParm             = generateParameter(SearchConstants.Prefix.SA, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1.5E+3"));
        expectedSql = "Basic.NUMBER_VALUE_LOW > ?";
        runTest(queryParm, expectedBindVariables, expectedSql);

        // eb - Ends before
        queryParm             = generateParameter(SearchConstants.Prefix.EB, null, "1e3");
        expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("5E+2"));
        expectedSql = "Basic.NUMBER_VALUE_HIGH < ?";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test()
    public void testPrecisionIntegerWithStartsAfter() throws FHIRPersistenceException {
        // sa - starts after with integer
        QueryParameter queryParm =
                generateParameter(SearchConstants.Prefix.SA, null, "window-end", new String[] { "1" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("1.5"));
        String expectedSql = "MolecularSequence.NUMBER_VALUE_LOW > ?";
        runTest(queryParm, expectedBindVariables, expectedSql, "MolecularSequence", MolecularSequence.class);
    }

    @Test()
    public void testPrecisionIntegerWithEndsBefore() throws FHIRPersistenceException {
        // eb - ends before with integer
        QueryParameter queryParm =
                generateParameter(SearchConstants.Prefix.EB, null, "window-end", new String[] { "1" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("0.5"));
        String expectedSql = "MolecularSequence.NUMBER_VALUE_HIGH < ?";
        runTest(queryParm, expectedBindVariables, expectedSql, "MolecularSequence", MolecularSequence.class);
    }

    @Test
    public void testPrecisionWithEqual() throws FHIRPersistenceException {
        // in this case, our code if missing Prefix, it injects EQ.
        // therefore this test case tests two conditions
        // Condition:
        //  [parameter]=100
        //  [parameter]=eq100

        // expectedBindVariables are pivoted on 100
        // Precision: 3
        // Implied Range: [99.5 ... 100.5)
        // Exclusive and Inclusive

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.EQ, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        String expectedSql = "(Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_HIGH <= ?)";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testPrecisionWithMultipleValuesForEqual() throws FHIRPersistenceException {
        // in this case, our code if missing Prefix, it injects EQ.
        // therefore this test case tests two conditions
        // Condition:
        //  [parameter]=100,1.00
        //  [parameter]=eq100,1.00

        // expectedBindVariables are pivoted on 100 and 1.00
        // Precision: 3 and 5
        // Implied Range: [99.5 ... 100.5) [0.995 ... 1.005)
        // Exclusive and Inclusive

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.EQ, null, new String[] { "100", "1.00" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("99.5"));
        expectedBindVariables.add(new BigDecimal("100.5"));
        expectedBindVariables.add(new BigDecimal("0.995"));
        expectedBindVariables.add(new BigDecimal("1.005"));
        String expectedSql = "(Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_HIGH <= ?) "
                + "OR (Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_HIGH <= ?)";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testPrecisionWithNotEqual() throws FHIRPersistenceException {
        // Condition:
        //  [parameter]=ne100

        // expectedBindVariables are pivoted on 100
        // Precision: 3
        // Implied Range: <= 99.5 OR 100.5 >
        // Exclusive and Inclusive

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.NE, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal(99.5));
        expectedBindVariables.add(new BigDecimal(100.5));
        String expectedSql = "(Basic.NUMBER_VALUE_LOW < ? OR Basic.NUMBER_VALUE_LOW IS NULL "
                + "OR Basic.NUMBER_VALUE_HIGH > ? OR Basic.NUMBER_VALUE_HIGH IS NULL)";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testPrecisionWithApprox() throws FHIRPersistenceException {
        // Condition:
        //  [parameter]=ap100

        // expectedBindVariables are pivoted on 100
        // Precision is implied by the use of ap
        // Implied Range: (89.5 ... 110.5)

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, "100");
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("89.5"));  // V >= lowerBound - factor
        expectedBindVariables.add(new BigDecimal("110.5")); // V <= upperBound + factor
        expectedBindVariables.add(new BigDecimal("89.5"));  // V >= lowerBound - factor
        expectedBindVariables.add(new BigDecimal("110.5")); // V <= upperBound + factor
        expectedBindVariables.add(new BigDecimal("89.5"));  // V >= lowerBound - factor
        expectedBindVariables.add(new BigDecimal("110.5")); // V <= upperBound + factor
        expectedBindVariables.add(new BigDecimal("89.5"));  // V >= lowerBound - factor
        expectedBindVariables.add(new BigDecimal("110.5")); // V <= upperBound + factor
        String expectedSql = "(Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW <= ? "
                + "OR Basic.NUMBER_VALUE IS NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_HIGH <= ? "
                + "OR Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_LOW <= ?) "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW IS NULL "
                + "OR Basic.NUMBER_VALUE_LOW <= ? AND Basic.NUMBER_VALUE_HIGH IS NULL))";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testPrecisionWithApproxNumberOne() throws FHIRPersistenceException {
        // Condition:
        //  [parameter]=ap1

        // expectedBindVariables are pivoted on 100
        // Precision is implied by the use of ap
        // Implied Range: (.4 ... 1.6)

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
        String expectedSql = "(Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW <= ? "
                + "OR Basic.NUMBER_VALUE IS NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_HIGH <= ? "
                + "OR Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_LOW <= ?) "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW IS NULL "
                + "OR Basic.NUMBER_VALUE_LOW <= ? AND Basic.NUMBER_VALUE_HIGH IS NULL))";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testPrecisionWithMultipleSameApprox() throws FHIRPersistenceException {
        // Condition:
        //  [parameter]=ap100,ap100

        // expectedBindVariables are pivoted on 100
        // Precision is implied by the use of ap
        // Implied Range: (89.55 ... 110.55)

        // It should de-dupe

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, new String[] { "100", "100" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        String expectedSql = "(Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW <= ? "
                + "OR Basic.NUMBER_VALUE IS NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_HIGH <= ? "
                + "OR Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_LOW <= ?) "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW IS NULL "
                + "OR Basic.NUMBER_VALUE_LOW <= ? AND Basic.NUMBER_VALUE_HIGH IS NULL))";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testPrecisionWithMultipleDifferentApprox() throws FHIRPersistenceException {
        // Condition:
        //  [parameter]=ap100,ap100.00

        // expectedBindVariables are pivoted on 100
        // It should NOT de-dupe

        QueryParameter queryParm = generateParameter(SearchConstants.Prefix.AP, null, new String[] { "100", "100.00" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.5"));
        expectedBindVariables.add(new BigDecimal("110.5"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        expectedBindVariables.add(new BigDecimal("89.995"));
        expectedBindVariables.add(new BigDecimal("110.005"));
        String expectedSql =
                "(Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW <= ? "
                + "OR Basic.NUMBER_VALUE IS NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_HIGH <= ? "
                + "OR Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_LOW <= ?) "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW IS NULL "
                + "OR Basic.NUMBER_VALUE_LOW <= ? AND Basic.NUMBER_VALUE_HIGH IS NULL)) "
                + "OR (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW <= ? "
                + "OR Basic.NUMBER_VALUE IS NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_HIGH <= ? "
                + "OR Basic.NUMBER_VALUE_LOW >= ? AND Basic.NUMBER_VALUE_LOW <= ?) "
                + "OR Basic.NUMBER_VALUE IS NOT NULL AND (Basic.NUMBER_VALUE_HIGH >= ? AND Basic.NUMBER_VALUE_LOW IS NULL "
                + "OR Basic.NUMBER_VALUE_LOW <= ? AND Basic.NUMBER_VALUE_HIGH IS NULL))";
        runTest(queryParm, expectedBindVariables, expectedSql);
    }

    @Test
    public void testLowerAndUpperBounds() {
        // Value: 1.00
        // range [0.995 ... 1.005)
        runLowerUpperTest("1.00", "0.995", "1.005");

        // Value: 100.00
        // significant figures precision = 5
        // range [99.995 ... 100.005)
        runLowerUpperTest("100.00", "99.995", "100.005");

        // Value: 100
        // significant figures precision = 3
        // range [99.5 ... 100.5)
        runLowerUpperTest("100", "99.5", "100.5");

        // Value: 1e-3
        // significant figures precision = 1
        // Implied Range: [0.0005 ... 0.0015)
        runLowerUpperTest("1e-3", "0.0005", "0.0015");

        // Value: 1e-2
        // significant figures precision = 1
        // Implied Range: [50 ... 150)
        runLowerUpperTest("1e-2", "0.005", "0.015");

        // Value: 1e-1
        // range [5 ... 15)
        runLowerUpperTest("1e-1", "0.05", "0.15");

        // Value: 1e0
        // range [0.5 ... 1.5)
        runLowerUpperTest("1e0", "0.5", "1.5");

        // Value: 1e1
        // range [5 ... 15)
        runLowerUpperTest("1e1", "5", "15");

        // Value: 1e2
        // significant figures precision = 1
        // Implied Range: [50 ... 150)
        runLowerUpperTest("1e2", "50", "150");

        // Value: 1e3
        // significant figures precision = 1
        // Implied Range: [500 ... 1500)
        runLowerUpperTest("1e3", "500", "1500");

        // Value: 1.0e1
        // Implied Range: [9.5 ... 10.5)
        runLowerUpperTest("1.0e1", "9.5", "10.5");

        // Value: 1.0e2
        // Implied Range: [95 ... 105)
        runLowerUpperTest("1.0e2", "95", "105");

        // Value: 1.00e2
        // Implied Range: [99.5 ... 100.5)
        runLowerUpperTest("1.00e2", "99.5", "100.5");

        // Value: 1.1e2
        // Implied Range: [105 ... 115)
        runLowerUpperTest("1.1e2", "105", "115");
    }

    @Test
    public void testLowerAndUpperBoundsScientific() {
        // Value: 100.00e0
        // range [0.995 ... 1.005)
        runLowerUpperTest("100.00e0", "99.995", "100.005");

        // Value: 100.00e-1
        // range [0.995 ... 1.005)
        runLowerUpperTest("100.00e-1", "9.9995", "10.0005");

        // Value: 100.00e1
        // range [0.995 ... 1.005)
        runLowerUpperTest("100.00e1", "999.95", "1000.05");
    }

    @Test(expectedExceptions = {})
    public void testPrecisionIntegerWithEQ() throws FHIRPersistenceException {
        // sa - starts after with integer, and it's not supported
        QueryParameter queryParm =
                generateParameter(SearchConstants.Prefix.EQ, null, "window-end", new String[] { "1" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("0.5"));
        expectedBindVariables.add(new BigDecimal("1.5"));
        String expectedSql = "(MolecularSequence.NUMBER_VALUE_LOW >= ? AND MolecularSequence.NUMBER_VALUE_HIGH <= ?)";
        runTest(queryParm, expectedBindVariables, expectedSql, "MolecularSequence", MolecularSequence.class);
    }

    @Test(expectedExceptions = {})
    public void testPrecisionIntegerWithNE() throws FHIRPersistenceException {
        // sa - starts after with integer, and it's not supported
        QueryParameter queryParm =
                generateParameter(SearchConstants.Prefix.NE, null, "window-end", new String[] { "1" });
        List<Object> expectedBindVariables = new ArrayList<>();
        expectedBindVariables.add(new BigDecimal("0.5"));
        expectedBindVariables.add(new BigDecimal("1.5"));
        String expectedSql = "(MolecularSequence.NUMBER_VALUE_LOW < ? OR MolecularSequence.NUMBER_VALUE_LOW IS NULL "
                + "OR MolecularSequence.NUMBER_VALUE_HIGH > ? OR MolecularSequence.NUMBER_VALUE_HIGH IS NULL)";
        runTest(queryParm, expectedBindVariables, expectedSql, "MolecularSequence", MolecularSequence.class);
    }
}