/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.examples.ExamplesUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

public class FHIRPathEvaluatorProfileTest {
    private static final int NUM_ITERATIONS = 500000;
    
    public static void main(String[] args) throws Exception {
        String specExample = new BufferedReader(ExamplesUtil.reader("json/spec/patient-examples-general.json"))
                .lines()
                .collect(Collectors.joining(System.lineSeparator()));
        Resource resource = FHIRParser.parser(Format.JSON).parse(new StringReader(specExample));
        FHIRPathTree tree = FHIRPathTree.tree(resource);
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);
        profile(evaluator);
    }

    private static void profile(FHIRPathEvaluator evaluator) throws Exception {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            evaluator.evaluate("Bundle.entry.resource.where(birthDate > @1950)", evaluator.getEvaluationContext().getTree().getRoot());
        }        
    }
}