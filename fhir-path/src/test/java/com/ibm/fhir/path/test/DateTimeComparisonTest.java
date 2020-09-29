/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static org.testng.Assert.assertEquals;

import java.util.Collection;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;

public class DateTimeComparisonTest {
    @Test
    public void testDateTimeComparison1() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@1970-01-01 < @2020-01-01");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testDateTimeComparison2() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@1970-01-01 > @2020-01-01");
        assertEquals(result, SINGLETON_FALSE);
    }

    @Test
    public void testDateTimeComparison3() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@1970-01-01 < now()");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testDateTimeComparison4() throws Exception {
        Patient patient = Patient.builder()
                .birthDate(Date.of("1970-01-01"))
                .build();
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate(patient, "Patient.birthDate = @1970-01-01");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testDateTimeComparison5() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@2012-04-15T15:00:00Z = @2012-04-15T10:00:00");
        assertEquals(result, empty());
    }

    @Test
    public void testDateTimeComparison6() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@2012-04-15T15:00:00.123456789 = @2012-04-15T15:00:00.123456");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testDateTimeComparison7() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@2012-04-15T15:00:00.123456789 < @2012-04-15T15:00:00.123457");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testDateTimeComparison8() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@2012-04-15T15:00:00.123457 > @2012-04-15T15:00:00.123456789");
        assertEquals(result, SINGLETON_TRUE);
    }
    
    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison9() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@-1010 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison10() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2010:XX < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison11() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2010-05:XX < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison12() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2010-05-32 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison13() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2010-05:0001:30:30 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison14() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2019-10-11T29 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison15() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2019-10-11T01:78:00 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison16() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2019-10-11T01:30:99 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison17() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2019-10-11T01:30:-1 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison18() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2019-10-11T01:30:20.1234567890 < now()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testDateTimeComparison19() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@2019-10-11T01:30:20.123456789:05-00 < now()");
    }

    @Test
    public void testTimeComparison1() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T12:00:00 < @T12:00:01");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison2() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T12:00:00 > @T11:59:59");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison3() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T12:00:00 = @T12:00:00");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison4() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T00:00:00 < timeOfDay()");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison5() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T15:00:00.123456789 = @T15:00:00.123456");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison6() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T15:00:00.123456789 < @T15:00:00.123457");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison7() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T15:00:00.123457 > @T15:00:00.123456789");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison8() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T15:00:00.1 > @T15:00:00");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison9() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T15:00:00.123 > @T15:00:00.122");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test
    public void testTimeComparison10() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> result = evaluator.evaluate("@T15:00:00.123456 > @T15:00:00.123455");
        assertEquals(result, SINGLETON_TRUE);
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testTimeComparison14() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@T29 < timeOfDay()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testTimeComparison15() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@T01:78:00 < timeOfDay()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testTimeComparison16() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@T01:30:99 < timeOfDay()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testTimeComparison17() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@T01:30:-1 < timeOfDay()");
    }

    @Test(expectedExceptions = FHIRPathException.class)
    public void testTimeComparison18() throws Exception {
        FHIRPathEvaluator.evaluator().evaluate("@T01:30:20.1234567890 < timeOfDay()");
    }

}
