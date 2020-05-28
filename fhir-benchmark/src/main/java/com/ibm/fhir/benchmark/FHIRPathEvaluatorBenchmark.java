/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;
import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXPRESSION;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.io.StringReader;
import java.util.Collection;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

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
        public EvaluationContext evaluationContext;
        public Collection<FHIRPathNode> initialContext;
        public Resource resource;
        public IBaseResource baseResource;

        @Setup
        public void setUp() throws Exception {
            context = FhirContext.forR4();
            fluentPath = context.newFluentPath();
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            evaluator = FHIRPathEvaluator.evaluator();
            evaluationContext = new EvaluationContext(resource);
            initialContext = singleton(evaluationContext.getTree().getRoot());
            baseResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
        }
    }

    @Benchmark
    public void benchmarkEvaluator(FHIRPathEvaluatorState state) throws Exception {
        state.evaluator.evaluate(state.evaluationContext, FHIRPathEvaluatorState.EXPRESSION, state.initialContext);
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
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(resource);
        System.out.println(evaluator.evaluate(evaluationContext, expression, singleton(evaluationContext.getTree().getRoot())));
    }

    public static void main(String[] args) throws Exception {
        testRun(EXAMPLE_NAME, EXPRESSION);
        new FHIRBenchmarkRunner(FHIRPathEvaluatorBenchmark.class)
                .property(PROPERTY_EXAMPLE_NAME, EXAMPLE_NAME)
                .property(PROPERTY_EXPRESSION, EXPRESSION)
                .run();
    }
}
