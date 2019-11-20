/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import java.io.StringReader;
import java.io.Writer;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
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
    static final Writer NOP_WRITER = BenchmarkUtil.createNOPWriter();
    
    @State(Scope.Thread)
    public static class FHIRGenerators {
        FHIRGenerator jsonGenerator = FHIRGenerator.generator(Format.JSON);
        FHIRGenerator xmlGenerator = FHIRGenerator.generator(Format.XML);
    }
    
    @State(Scope.Benchmark)
    public static class FHIRGeneratorState {
        FhirContext context;
        Resource resource;
        IBaseResource baseResource;
        
        // JMH will inject the value into the annotated field before any Setup method is called.
        @Param({"valuesets"})
        public String exampleName;
        
        @Setup
        public void setUp() throws Exception {
            if (exampleName == null) {
                System.err.println("exampleName is null; if you're in Eclipse then make sure annotation processing is on and you've ran 'mvn clean package'.");
                System.exit(1);
            }
            
            // us
            String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, exampleName);
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(JSON_SPEC_EXAMPLE));
            
            // HAPI
            context = FhirContext.forR4();
            baseResource = context.newJsonParser().parseResource(new StringReader(JSON_SPEC_EXAMPLE));
        }
    }
    
    @Benchmark
    public void benchmarkJsonGenerator(FHIRGenerators generators, FHIRGeneratorState state) throws Exception {
        generators.jsonGenerator.generate(state.resource, NOP_WRITER);
    }
    
    @Benchmark
    public void benchmarkXMLGenerator(FHIRGenerators generators, FHIRGeneratorState state) throws Exception {
        generators.xmlGenerator.generate(state.resource, NOP_WRITER);
    }
    
    @Benchmark
    public void benchmarkHAPIJsonGenerator(FHIRGeneratorState state) throws Exception {
        state.context.newJsonParser().encodeResourceToWriter(state.baseResource, NOP_WRITER);
    }
    
    @Benchmark
    public void benchmarkHAPIXMLGenerator(FHIRGeneratorState state) throws Exception {
        state.context.newXmlParser().encodeResourceToWriter(state.baseResource, NOP_WRITER);
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRGeneratorBenchmark.class)
                .run(BenchmarkUtil.getRandomSpecExampleName());
    }
}
