package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;

public class FHIRValueSetBenchmark2 {
    @State(Scope.Benchmark)
    public static class FHIRValueSetState {
        public ValueSet valueSet;
        public int size;
        
        @Setup
        public void setUp() throws Exception {
            ValueSet.Builder vsBuilder = ValueSet.builder().status(PublicationStatus.DRAFT);
            ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now());
            ValueSet.Expansion.Contains.Builder template = ValueSet.Expansion.Contains.builder().system(Uri.of("http://snomed.info/sct"));
            
            Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource("procedureReasonCodes.txt").toURI());

            AtomicInteger counter = new AtomicInteger();
            
            Files.lines(path)
                 .forEach(line -> {
                     counter.incrementAndGet();
                     expansionBuilder.contains(template.code(Code.of(line.trim())).build());
                 });
            
            valueSet = vsBuilder.expansion(expansionBuilder.build()).build();
            size = counter.get();
        }
    }
    
    @Benchmark
    public void buildSet(FHIRValueSetState state) throws Exception {
        final Set<String> set = new HashSet<>(state.size);
        state.valueSet.getExpansion().getContains().stream()
                .forEach(concept -> set.add(concept.getCode().getValue() + "|" + concept.getCode().getValue()));
    }
    
    @Benchmark
    public void lookup(FHIRValueSetState state) throws Exception {
        for (ValueSet.Expansion.Contains concept : state.valueSet.getExpansion().getContains()) {
            if ("718854004".equals(concept.getCode().getValue()) && "http://snomed.info/sct".equals(concept.getSystem().getValue())) {
                return;
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRValueSetBenchmark2.class)
                .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
                .run();
    }
}
