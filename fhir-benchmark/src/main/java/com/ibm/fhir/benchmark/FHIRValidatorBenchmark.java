/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.io.StringReader;

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
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.validation.FHIRValidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class FHIRValidatorBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRValidatorState {
        public static final String SPEC_EXAMPLE_NAME = System.getProperty(PROPERTY_EXAMPLE_NAME);
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);

        public FhirContext context;
        public FhirValidator fhirValidator;
        public IBaseResource baseResource;
        public FHIRValidator validator;
        public Resource resource;
        public EvaluationContext evaluationContext;

        @Setup
        public void setUp() throws Exception {
            context = FhirContext.forR4();
            fhirValidator = context.newValidator();
            baseResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
            validator = FHIRValidator.validator();
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            evaluationContext = new EvaluationContext(resource);
        }
    }

    @Benchmark
    public void benchmarkValidator(FHIRValidatorState state) throws Exception {
        state.validator.validate(state.evaluationContext);
    }

    @Benchmark
    public void benchmarkHAPIValidator(FHIRValidatorState state) throws Exception {
        state.fhirValidator.validateWithResult(state.baseResource);
    }

    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRValidatorBenchmark.class)
            .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
            .run();
    }
}
