/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.validation.FHIRValidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.validation.FhirValidator;

public class FHIRParserValidatorGeneratorBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRParserValidatorGeneratorBenchmarkState {
        public static final Writer NOP_WRITER = BenchmarkUtil.createNOPWriter();
        public static final String SPEC_EXAMPLE_NAME = System.getProperty(PROPERTY_EXAMPLE_NAME);
        public static final String JSON_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.JSON, SPEC_EXAMPLE_NAME);
        public static final String XML_SPEC_EXAMPLE = BenchmarkUtil.getSpecExample(Format.XML, SPEC_EXAMPLE_NAME);

        public FhirContext context;
        public FhirValidator fhirValidator;
        public FHIRValidator validator;
        public FHIRParser jsonParser;
        public FHIRGenerator jsonGenerator;
        public FHIRParser xmlParser;
        public FHIRGenerator xmlGenerator;

        @Setup
        public void setUp() {
            context = FhirContext.forR4();
            context.setParserErrorHandler(new StrictErrorHandler());
            fhirValidator = context.newValidator();
            validator = FHIRValidator.validator();
            jsonParser = FHIRParser.parser(Format.JSON);
            jsonGenerator = FHIRGenerator.generator(Format.JSON);
            xmlParser = FHIRParser.parser(Format.XML);
            xmlGenerator = FHIRGenerator.generator(Format.XML);
        }
    }

    @Benchmark
    public void benchmarkJsonParserValidatorGenerator(FHIRParserValidatorGeneratorBenchmarkState state) throws Exception {
        Resource resource = state.jsonParser.parse(new StringReader(FHIRParserValidatorGeneratorBenchmarkState.JSON_SPEC_EXAMPLE));
        state.validator.validate(resource);
        state.jsonGenerator.generate(resource, FHIRParserValidatorGeneratorBenchmarkState.NOP_WRITER);
    }

    @Benchmark
    public void benchmarkXMLParserValidatorGenerator(FHIRParserValidatorGeneratorBenchmarkState state) throws Exception {
        Resource resource = state.xmlParser.parse(new StringReader(FHIRParserValidatorGeneratorBenchmarkState.XML_SPEC_EXAMPLE));
        state.validator.validate(resource);
        state.xmlGenerator.generate(resource, FHIRParserValidatorGeneratorBenchmarkState.NOP_WRITER);
    }

    @Benchmark
    public void benchmarkHAPIJsonParserValidatorGenerator(FHIRParserValidatorGeneratorBenchmarkState state) throws Exception {
        IParser parser = state.context.newJsonParser();
        IBaseResource resource = parser.parseResource(new StringReader(FHIRParserValidatorGeneratorBenchmarkState.JSON_SPEC_EXAMPLE));
        state.fhirValidator.validateWithResult(resource);
        parser.encodeResourceToWriter(resource, FHIRParserValidatorGeneratorBenchmarkState.NOP_WRITER);
    }

    @Benchmark
    public void benchmarkHAPIXMLParserValidatorGenerator(FHIRParserValidatorGeneratorBenchmarkState state) throws Exception {
        IParser parser = state.context.newXmlParser();
        IBaseResource baseResource = parser.parseResource(new StringReader(FHIRParserValidatorGeneratorBenchmarkState.XML_SPEC_EXAMPLE));
        state.fhirValidator.validateWithResult(baseResource);
        parser.encodeResourceToWriter(baseResource, FHIRParserValidatorGeneratorBenchmarkState.NOP_WRITER);
    }

    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRParserValidatorGeneratorBenchmark.class)
            .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
            .run();
    }
}
