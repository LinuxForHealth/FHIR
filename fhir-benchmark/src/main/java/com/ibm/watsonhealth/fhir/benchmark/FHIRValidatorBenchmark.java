/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.benchmark;

import java.io.StringReader;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.watsonhealth.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.watsonhealth.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.FHIRParser;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;

public class FHIRValidatorBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRValidatorState {
        public static final String SPEC_EXAMPLE_NAME = System.getProperty("com.ibm.watsonhealth.fhir.benchmark.exampleName");
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);
        
        public FhirContext context;
        public FhirValidator validator;
        public Resource resource;
        public IBaseResource hapiResource;
        
        @Setup
        public void setUp() throws Exception {
            context = FhirContext.forR4();
            validator = context.newValidator();
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            hapiResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
        }
    }
    
    @Benchmark
    public void benchmarkValidator(FHIRValidatorState state) throws Exception {
        FHIRValidator.validator(state.resource).validate();
    }
    
    @Benchmark
    public void benchmarkHAPIValidator(FHIRValidatorState state) throws Exception {
        state.validator.validateWithResult(state.hapiResource);
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRValidatorBenchmark.class, BenchmarkUtil.getRandomSpecExampleName()).run();
    }
}
