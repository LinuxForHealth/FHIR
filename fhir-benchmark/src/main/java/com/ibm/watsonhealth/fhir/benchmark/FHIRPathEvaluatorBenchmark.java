/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.benchmark;

import static com.ibm.watsonhealth.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;
import static com.ibm.watsonhealth.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXPRESSION;
import static com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil.singleton;

import java.io.StringReader;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.watsonhealth.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.watsonhealth.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathTree;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fluentpath.IFluentPath;

public class FHIRPathEvaluatorBenchmark {
    private static final String EXAMPLE_NAME = "patient-examples-general";
    private static final String EXPRESSION = "Bundle.entry.resource.where(birthDate < @1950-01-01)";

    @State(Scope.Benchmark)
    public static class FHIRPathEvaluatorState {        
        public static final String SPEC_EXAMPLE_NAME = System.getProperty(PROPERTY_EXAMPLE_NAME);
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);
        public static final String EXPRESSION = System.getProperty(PROPERTY_EXPRESSION);
        
        public FhirContext context;
        public IFluentPath fluentPath;
        public FHIRPathEvaluator evaluator;
        public Resource resource;
        public FHIRPathTree tree;
        public IBaseResource baseResource;
                
        @Setup
        public void setUp() throws Exception {
            context = FhirContext.forR4();
            fluentPath = context.newFluentPath();
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            tree = FHIRPathTree.tree(resource);
            evaluator = FHIRPathEvaluator.evaluator(tree);
            baseResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
        }
    }
    
    @Benchmark
    public void benchmarkEvaluator(FHIRPathEvaluatorState state) throws Exception {
        state.evaluator.evaluate(FHIRPathEvaluatorState.EXPRESSION, singleton(state.tree.getRoot()));
    }
    
    @Benchmark
    public void benchmarkHAPIEvaluator(FHIRPathEvaluatorState state) throws Exception {
        state.fluentPath.evaluate(state.baseResource, FHIRPathEvaluatorState.EXPRESSION, IBase.class);
    }
    
    public static void testRun(String exampleName, String expression) throws Exception {
        String specExample = BenchmarkUtil.getSpecExample(Format.JSON, exampleName);
        
        FhirContext context = FhirContext.forR4();
        IBaseResource baseResource = context.newJsonParser().parseResource(new StringReader(specExample));
        IFluentPath fluentPath = context.newFluentPath();
        System.out.println(fluentPath.evaluate(baseResource, expression, IBase.class));
    
        Resource resource = FHIRParser.parser(Format.JSON).parse(new StringReader(specExample));
        FHIRPathTree tree = FHIRPathTree.tree(resource);
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator(tree);        
        System.out.println(evaluator.evaluate(expression, singleton(tree.getRoot())));
    }

    public static void main(String[] args) throws Exception {
        testRun(EXAMPLE_NAME, EXPRESSION);
        new FHIRBenchmarkRunner(FHIRPathEvaluatorBenchmark.class)
                .property(PROPERTY_EXAMPLE_NAME, EXAMPLE_NAME)
                .property(PROPERTY_EXPRESSION, EXPRESSION)
                .run();
    }
}
