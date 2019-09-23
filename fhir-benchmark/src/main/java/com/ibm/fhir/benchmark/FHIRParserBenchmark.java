/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.io.StringReader;
import java.io.Writer;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.StrictErrorHandler;

public class FHIRParserBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRParserState {
        public static final Writer NOP_WRITER = BenchmarkUtil.createNOPWriter();
        public static final String SPEC_EXAMPLE_NAME = System.getProperty(PROPERTY_EXAMPLE_NAME);
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);
        public static final String XML_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.XML, SPEC_EXAMPLE_NAME);
        
        public FhirContext context;
        public FHIRParser jsonParser;
        public FHIRParser xmlParser;
        
        @Setup
        public void setUp() {            
            context = FhirContext.forR4();
            context.setParserErrorHandler(new StrictErrorHandler());
            jsonParser = FHIRParser.parser(Format.JSON);
            xmlParser = FHIRParser.parser(Format.XML);
        }
    }
    
    @Benchmark
    public void benchmarkJsonParser(FHIRParserState state) throws Exception {
        state.jsonParser.parse(new StringReader(FHIRParserState.JSON_SPEC_EXAMPLE));
    }
    
    @Benchmark
    public void benchmarkXMLParser(FHIRParserState state) throws Exception {
        state.xmlParser.parse(new StringReader(FHIRParserState.XML_SPEC_EXAMPLE));
    }
    
    @Benchmark
    public void benchmarkHAPIJsonParser(FHIRParserState state) throws Exception {
        state.context.newJsonParser().parseResource(new StringReader(FHIRParserState.JSON_SPEC_EXAMPLE));
    }
    
    @Benchmark
    public void benchmarkHAPIXMLParser(FHIRParserState state) throws Exception {
        state.context.newXmlParser().parseResource(new StringReader(FHIRParserState.XML_SPEC_EXAMPLE));
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRParserBenchmark.class)
                .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
                .run();
    }
}
