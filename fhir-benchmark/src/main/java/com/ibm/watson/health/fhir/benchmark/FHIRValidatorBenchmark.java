/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.benchmark;

import static com.ibm.watson.health.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.io.StringReader;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.watson.health.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.watson.health.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.path.FHIRPathTree;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.validation.FHIRValidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class FHIRValidatorBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRValidatorState {
        public static final String SPEC_EXAMPLE_NAME = System.getProperty(PROPERTY_EXAMPLE_NAME);
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);
        
        public FhirContext context;
        public FhirValidator validator;
        public Resource resource;
        public FHIRPathTree tree;
        public IBaseResource baseResource;
        
        @Setup
        public void setUp() throws Exception {
            context = FhirContext.forR4();
            validator = context.newValidator();
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            tree = FHIRPathTree.tree(resource);
            baseResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
        }
    }
    
    @Benchmark
    public void benchmarkValidator(FHIRValidatorState state) throws Exception {
        FHIRValidator.validator(state.tree).validate();
    }
    
    @Benchmark
    public void benchmarkHAPIValidator(FHIRValidatorState state) throws Exception {
        state.validator.validateWithResult(state.baseResource);
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRValidatorBenchmark.class)
                .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
                .run();
    }
}
