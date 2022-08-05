/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import static org.linuxforhealth.fhir.path.FHIRPathQuantityValue.quantityValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getQuantityValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Year;
import java.time.temporal.ChronoField;
import java.util.Collection;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;

public class BetweenFunctionTest {
    @Test
    public void testBetweenFunction1() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@1970-01-01, @2020-01-01, 'years')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(50), "years"));
    }

    @Test
    public void testBetweenFunction2() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@1970-01-01, @2020-01-01, 'months')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(600), "months"));
    }

    @Test
    public void testBetweenFunction3() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@1970-01-01, @2020-01-01, 'weeks')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(2608), "weeks"));
    }

    @Test
    public void testBetweenFunction4() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@1970-01-01, @2020-01-01, 'days')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(18262), "days"));
    }

    @Test
    public void testBetweenFunction5() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@T00:00:00.000, @T12:34:56.789, 'hours')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(12), "hours"));
    }

    @Test
    public void testBetweenFunction6() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@T00:00:00.000, @T12:34:56.789, 'minutes')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(754), "minutes"));
    }

    @Test
    public void testBetweenFunction7() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@T00:00:00.000, @T12:34:56.789, 'seconds')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(45296), "seconds"));
    }

    @Test
    public void testBetweenFunction8() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@T00:00:00.000, @T12:34:56.789, 'milliseconds')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(45296789), "milliseconds"));
    }

    @Test
    public void testBetweenFunction9() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@2020-01-01, @1970-01-01, 'years')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(-50), "years"));
    }

    @Test
    public void testBetweenFunction10() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@1970-01-01, @2020-01-01T00:00:00Z, 'years')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(50), "years"));
    }

    @Test
    public void testBetweenFunction11() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        try {
            evaluator.evaluate("between(@1970-01-01T00:00:00Z, @2020-01-01, 'years')");
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof FHIRPathException);
            assertTrue(e.getCause() instanceof DateTimeException);
        }
    }

    @Test
    public void testBetweenFunction12() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("between(@1970-01-01T00:00:00Z.toDate(), @2020-01-01, 'years')");
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(50), "years"));
    }

    @Test
    public void testBetweenFunction13() throws Exception {
        Patient patient = Patient.builder()
            .birthDate(Date.of("1970-01-01"))
            .build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(patient, "between(Patient.birthDate, today(), 'years')");
        int diff = Year.now().get(ChronoField.YEAR) - 2020;
        assertEquals(getQuantityValue(result), quantityValue(new BigDecimal(50 + diff), "years"));
    }
}
