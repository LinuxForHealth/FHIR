/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import org.linuxforhealth.fhir.benchmark.runner.FHIRBenchmarkRunner;
import org.linuxforhealth.fhir.examples.ExamplesUtil;
import org.linuxforhealth.fhir.examples.Index;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.parser.exception.FHIRParserException;


public class FHIRIndexParserBenchmark {
    @State(Scope.Benchmark)
    public static class FHIRParserState {
        public static final Index INDEX = Index.PROFILES_PDEX_PLAN_NET_JSON;

        Set<String> JSON_SPEC_EXAMPLES = new HashSet<>();

        @Setup
        public void setUp() throws IOException {
            System.out.println("Setting up for index " + INDEX);

            // Each line of the index file should be a path to an example resource and an expected outcome
            try (BufferedReader br = new BufferedReader(ExamplesUtil.indexReader(INDEX))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    if (tokens.length == 2) {
                        String expectation = tokens[0];
                        String example = tokens[1];
                        if ("OK".equals(expectation)) {
                            Reader resourceReader = ExamplesUtil.resourceReader(example);
                            JSON_SPEC_EXAMPLES.add(IOUtils.toString(resourceReader));
                        }
                    }
                }
            }
        }
    }

    @Benchmark
    public Set<?> benchmarkJsonParserStream(FHIRParserState state) throws Exception {
        return state.JSON_SPEC_EXAMPLES.stream()
            .map(s -> new StringReader(s))
            .map(r -> {
                try {
                    FHIRParser parser = FHIRParser.parser(Format.JSON);
                    return parser.parse(r);
                } catch (FHIRParserException e) {
                    throw new RuntimeException("exception during parse", e);
                }
            })
            .collect(Collectors.toSet());
    }

    @Benchmark
    public Set<?> benchmarkJsonParserStreamParallel(FHIRParserState state) throws Exception {
        return state.JSON_SPEC_EXAMPLES.parallelStream()
            .map(s -> new StringReader(s))
            .map(r -> {
                try {
                    FHIRParser parser = FHIRParser.parser(Format.JSON);
                    return parser.parse(r);
                } catch (FHIRParserException e) {
                    throw new RuntimeException("exception during parse", e);
                }
            })
            .collect(Collectors.toSet());
    }

    @Benchmark
    public Set<?> benchmarkJsonParserNonValidatingStream(FHIRParserState state) throws Exception {
        return state.JSON_SPEC_EXAMPLES.stream()
            .map(s -> new StringReader(s))
            .map(r -> {
                try {
                    FHIRParser parser = FHIRParser.parser(Format.JSON);
                    parser.setValidating(false);
                    return parser.parse(r);
                } catch (FHIRParserException e) {
                    throw new RuntimeException("exception during parse", e);
                }
            })
            .collect(Collectors.toSet());
    }

    @Benchmark
    public Set<?> benchmarkJsonParserNonValidatingStreamParallel(FHIRParserState state) throws Exception {
        return state.JSON_SPEC_EXAMPLES.parallelStream()
            .map(s -> new StringReader(s))
            .map(r -> {
                try {
                    FHIRParser parser = FHIRParser.parser(Format.JSON);
                    parser.setValidating(false);
                    return parser.parse(r);
                } catch (FHIRParserException e) {
                    throw new RuntimeException("exception during parse", e);
                }
            })
            .collect(Collectors.toSet());
    }


    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRIndexParserBenchmark.class).run();
    }
}
