/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.model.path.test;

import java.io.InputStream;
import java.util.Collection;

import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.path.FHIRPathNode;
import com.ibm.watson.health.fhir.model.path.FHIRPathTree;
import com.ibm.watson.health.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watson.health.fhir.model.resource.Observation;
import com.ibm.watson.health.fhir.model.type.Reference;

public class ResolveFunctionTest {    
    public static void main(String[] args) throws Exception {
        try (InputStream in = ResolveFunctionTest.class.getClassLoader().getResourceAsStream("JSON/observation-example-f001-glucose.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);
            
            FHIRPathEvaluator.DEBUG = true;
            
            FHIRPathTree tree = FHIRPathTree.tree(observation);
            
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);
            
            System.out.println("expr: Observation.subject.where(resolve() is Patient)");
            Collection<FHIRPathNode> result = evaluator.evaluate("Observation.subject.where(resolve() is Patient)", tree.getRoot());
            System.out.println("result: " + result);
            System.out.println("    Observation.subject.reference: " + result.iterator().next().asElementNode().element().as(Reference.class).getReference().getValue());
            System.out.println("");
            
            System.out.println("expr: Observation.subject.where(resolve() is Device)");
            result = evaluator.evaluate("Observation.subject.where(resolve() is Device)", tree.getRoot());
            System.out.println("result: " + result);
            System.out.println("");
        }
    }
}
