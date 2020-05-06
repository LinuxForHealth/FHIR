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
}
