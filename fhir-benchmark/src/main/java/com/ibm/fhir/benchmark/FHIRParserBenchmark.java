/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import java.io.IOException;
import java.io.StringReader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.StrictErrorHandler;


public class FHIRParserBenchmark {
    @State(Scope.Thread)
    public static class FHIRParsers {
        FHIRParser jsonParser = FHIRParser.parser(Format.JSON);
        FHIRParser xmlParser = FHIRParser.parser(Format.XML);
    }
    
    @State(Scope.Benchmark)
    public static class FHIRParserState {
        FhirContext context;
        String JSON_SPEC_EXAMPLE;
        String XML_SPEC_EXAMPLE;
        
        // JMH will inject the value into the annotated field before any Setup method is called.
        @Param({"valuesets"})
        public String exampleName;
        
        @Setup
        public void setUp() throws IOException {
            if (exampleName == null) {
                System.err.println("exampleName is null; if you're in Eclipse then make sure annotation processing is on and you've ran 'mvn clean package'.");
                System.exit(1);
            }
            System.out.println("Setting up for example " + exampleName);
            context = FhirContext.forR4();
            context.setParserErrorHandler(new StrictErrorHandler());
            JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, exampleName);
            XML_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.XML, exampleName);
        }
    }
    
    @Benchmark
    public Resource benchmarkJsonParser(FHIRParsers parsers, FHIRParserState state) throws Exception {
        return parsers.jsonParser.parse(new StringReader(state.JSON_SPEC_EXAMPLE));
    }
    
    @Benchmark
    public Resource benchmarkXMLParser(FHIRParsers parsers, FHIRParserState state) throws Exception {
        return parsers.xmlParser.parse(new StringReader(state.XML_SPEC_EXAMPLE));
    }
    
    @Benchmark
    public void benchmarkHAPIJsonParser(FHIRParserState state) throws Exception {
        state.context.newJsonParser().parseResource(new StringReader(state.JSON_SPEC_EXAMPLE));
    }
    
    @Benchmark
    public void benchmarkHAPIXMLParser(FHIRParserState state) throws Exception {
        state.context.newXmlParser().parseResource(new StringReader(state.XML_SPEC_EXAMPLE));
    }
    
    public static void main(String[] args) throws Exception {
//        new FHIRBenchmarkRunner(FHIRParserBenchmark.class).runAll();
        new FHIRBenchmarkRunner(FHIRParserBenchmark.class).run();
    }
}
