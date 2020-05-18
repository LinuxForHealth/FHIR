/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Expansion.Contains;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;

public class FHIRValueSetBenchmarks {
    @Benchmark
    public ValueSet readJsonResource() throws Exception {
        Reader reader = ExamplesUtil.resourceReader("json/ibm/valueset/ValueSet-large.json");
        return FHIRParser.parser(Format.JSON).parse(reader);
    }

    @Benchmark
    public ValueSet readXmlResource() throws Exception {
        Reader reader = ExamplesUtil.resourceReader("xml/ibm/valueset/ValueSet-large.xml");
        return FHIRParser.parser(Format.XML).parse(reader);
    }

    @Benchmark
    public ValueSet readTxtFile() throws Exception {
        final ValueSet.Builder vsBuilder = ValueSet.builder().status(PublicationStatus.DRAFT);
        final ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now());
        final ValueSet.Expansion.Contains.Builder template = ValueSet.Expansion.Contains.builder();

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ValueSet-large.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            reader.lines()
            .forEach(line -> {
                String[] concept = line.split("|");
                expansionBuilder.contains(template
                        .system(Uri.of(concept[0]))
                        .code(Code.of(concept[1]))
                        .build()
                        );
            });
        }
        return vsBuilder.expansion(expansionBuilder.build()).build();
    }

    @SuppressWarnings("unchecked")
    @Benchmark
    public Set<String> readSerializedSet() throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ValueSet-large-HashSet.ser");
        try (ObjectInputStream is = new ObjectInputStream(in)) {
            return (Set<String>) is.readObject();
        }
    }

    @State(Scope.Benchmark)
    public static class FHIRValueSetState {
        ValueSet valueSet;
        Contains concept;

        @Setup
        public void setUp() throws Exception {
            Reader reader = ExamplesUtil.resourceReader("json/ibm/valueset/ValueSet-large.json");
            valueSet = FHIRParser.parser(Format.JSON).parse(reader);

            List<Contains> concepts = valueSet.getExpansion().getContains();
            // naive attempt to force the worst case
            concept = concepts.get(concepts.size() - 1);
        }
    }

    @Benchmark
    public Set<String> buildSet(FHIRValueSetState state) throws Exception {
        final Set<String> set = new HashSet<>();
        state.valueSet.getExpansion().getContains().stream()
                .forEach(concept -> set.add(concept.getSystem().getValue() + "|" + concept.getCode().getValue()));
        return set;
    }

    @Benchmark
    public boolean lookupInList(FHIRValueSetState state) throws Exception {
        for (ValueSet.Expansion.Contains concept : state.valueSet.getExpansion().getContains()) {
            if (state.concept.getCode().equals(concept.getCode()) && state.concept.getSystem().equals(concept.getSystem())) {
                return true;
            }
        }
        return false;
    }

    @State(Scope.Benchmark)
    public static class FHIRHashSetState {
        Set<String> set;

        @SuppressWarnings("unchecked")
        @Setup
        public void setUp() throws Exception {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ValueSet-large-HashSet.ser");
            try (ObjectInputStream is = new ObjectInputStream(in)) {
                set = (Set<String>) is.readObject();
            }
        }
    }

    @Benchmark
    public boolean lookupInSet(FHIRValueSetState vsState, FHIRHashSetState state) throws Exception {
        return state.set.contains(vsState.concept.getSystem().getValue() + "|" + vsState.concept.getCode().getValue());
    }

    public static void main(String[] args) throws Exception {
//      new FHIRBenchmarkRunner(FHIRValueSetBenchmark.class).run(BenchmarkUtil.getRandomSpecExampleName());
        Options opt = new OptionsBuilder()
                .include(FHIRValueSetBenchmarks.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(2)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
