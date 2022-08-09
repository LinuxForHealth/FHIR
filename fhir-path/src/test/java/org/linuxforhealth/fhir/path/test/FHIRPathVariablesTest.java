/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import static org.testng.Assert.assertEquals;

import java.util.Collection;

import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathStringValue;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.path.util.FHIRPathUtil;
import org.testng.annotations.Test;

/**
 * Test FHIRPath expressions that use variables / externalConstants like %resource
 */
public class FHIRPathVariablesTest {
    Patient patient = Patient.builder()
            .id("test")
            .name(HumanName.builder()
                    .given("Lee")
                    .build())
            .build();

    @Test
    public void testRepeatFunction_CodeSystem() throws Exception {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

        Collection<FHIRPathNode> initialContext = evaluator.evaluate(patient, "Patient.name");

        EvaluationContext evaluationContext = new EvaluationContext(patient);
        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "%resource.id", initialContext);

        assertEquals("test", FHIRPathUtil.getSingleton(result, FHIRPathStringValue.class).string());
    }
}
