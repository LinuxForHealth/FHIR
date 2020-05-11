/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import java.io.InputStream;
import java.util.Collection;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ResolveFunctionTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ResolveFunctionTest.class.getClassLoader().getResourceAsStream("JSON/observation-example-f001-glucose.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(observation);

            System.out.println("expr: Observation.subject.where(resolve() is Patient)");
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "Observation.subject.where(resolve() is Patient)");
            System.out.println("result: " + result);
            System.out.println("    Observation.subject.reference: " + result.iterator().next().asElementNode().element().as(Reference.class).getReference().getValue());
            System.out.println("");

            System.out.println("expr: Observation.subject.where(resolve() is Device)");
            result = evaluator.evaluate(evaluationContext, "Observation.subject.where(resolve() is Device)");
            System.out.println("result: " + result);
            System.out.println("");
        }
    }
}