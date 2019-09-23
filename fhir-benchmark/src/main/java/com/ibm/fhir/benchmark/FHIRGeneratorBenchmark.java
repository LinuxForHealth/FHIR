/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.io.StringReader;
import java.io.Writer;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;

import ca.uhn.fhir.context.FhirContext;

public class FHIRGeneratorBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRGeneratorState {
        public static final Writer NOP_WRITER = BenchmarkUtil.createNOPWriter();
        public static final String SPEC_EXAMPLE_NAME = System.getProperty(PROPERTY_EXAMPLE_NAME);
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);
        public static final String XML_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.XML, SPEC_EXAMPLE_NAME);
        
        public FhirContext context;
        public FHIRGenerator jsonGenerator;
        public FHIRGenerator xmlGenerator;
        public Resource resource;
        public IBaseResource baseResource;
        
        @Setup
        public void setUp() throws Exception {            
            context = FhirContext.forR4();
            jsonGenerator = FHIRGenerator.generator(Format.JSON);
            xmlGenerator = FHIRGenerator.generator(Format.XML);
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            baseResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
        }
    }
    
    @Benchmark
    public void benchmarkJsonGenerator(FHIRGeneratorState state) throws Exception {
        state.jsonGenerator.generate(state.resource, FHIRGeneratorState.NOP_WRITER);
    }
    
    @Benchmark
    public void benchmarkXMLGenerator(FHIRGeneratorState state) throws Exception {
        state.xmlGenerator.generate(state.resource, FHIRGeneratorState.NOP_WRITER);
    }
    
    @Benchmark
    public void benchmarkHAPIJsonGenerator(FHIRGeneratorState state) throws Exception {
        state.context.newJsonParser().encodeResourceToWriter(state.baseResource, FHIRGeneratorState.NOP_WRITER);
    }
    
    @Benchmark
    public void benchmarkHAPIXMLGenerator(FHIRGeneratorState state) throws Exception {
        state.context.newXmlParser().encodeResourceToWriter(state.baseResource, FHIRGeneratorState.NOP_WRITER);
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRGeneratorBenchmark.class)
                .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
                .run();
    }
}
