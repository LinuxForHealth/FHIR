/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.stream.Collectors;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class FHIRPathEvaluatorProfileTest {
    private static final int NUM_ITERATIONS = 500000;
    
    public static void main(String[] args) throws Exception {
        String specExample = new BufferedReader(ExamplesUtil.resourceReader("json/spec/patient-examples-general.json"))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        Resource resource = FHIRParser.parser(Format.JSON).parse(new StringReader(specExample));
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(resource);
        profile(evaluator, evaluationContext);
    }

    private static void profile(FHIRPathEvaluator evaluator, EvaluationContext evaluationContext) throws Exception {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            evaluator.evaluate(evaluationContext, "Bundle.entry.resource.where(birthDate > @1950)");
        }        
    }
}