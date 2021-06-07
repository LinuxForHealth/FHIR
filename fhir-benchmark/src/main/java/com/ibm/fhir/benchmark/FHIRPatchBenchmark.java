/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import java.io.StringReader;
import java.time.Instant;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.spi.JsonProvider;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.patch.FHIRPathPatch;

public class FHIRPatchBenchmark {
    private static final Extension FHIR_EXTENSION = Extension.builder()
            .url("myTime")
            .value(DateTime.now())
            .build();
    private static final JsonObject JSON_EXTENSION = Json.createObjectBuilder()
            .add("url", "myTime")
            .add("valueDateTime", Instant.now().toString())
            .build();
    private static final JsonProvider JSON_PROVIDER = JsonProvider.provider();
    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = JSON_PROVIDER.createBuilderFactory(null);

    @State(Scope.Benchmark)
    public static class FHIRPathEvaluatorState {
        Resource resource;
        String fhirPath;

        // JMH will inject the value into the annotated field before any Setup method is called.
        @Param({"valuesets"})
        public String exampleName;

        @Setup
        public void setUp() throws Exception {
            if (exampleName == null) {
                System.err.println("exampleName is null; if you're in Eclipse then make sure annotation processing is on and you've ran 'mvn clean package'.");
                System.exit(1);
            }
            System.out.println("Setting up for example " + exampleName);
            String resourceText = BenchmarkUtil.getSpecExample(Format.JSON, exampleName);
            resource = FHIRParser.parser(Format.JSON).parse(new StringReader(resourceText));
            fhirPath = ModelSupport.getTypeName(resource.getClass());
        }
    }

    @Benchmark
    public Resource benchmarkFHIRPathPatch(FHIRPathEvaluatorState state) throws Exception {
        FHIRPatch patch = FHIRPathPatch.builder()
                .add(state.fhirPath, "extension", FHIR_EXTENSION)
                .build();
        return patch.apply(state.resource);
    }

    @Benchmark
    public Resource benchmarkJSONPatch(FHIRPathEvaluatorState state) throws Exception {
        FHIRPatch patch = FHIRPatch.patch(JSON_PROVIDER.createPatchBuilder()
            .add("/extension", JSON_BUILDER_FACTORY.createArrayBuilder().build())
            .add("/extension/-", JSON_EXTENSION)
            .build());
        return patch.apply(state.resource);
    }

    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRPatchBenchmark.class)
                .run(BenchmarkUtil.getRandomSpecExampleName());
    }
}
