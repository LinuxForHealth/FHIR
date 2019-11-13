package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;

public class FHIRValueSetBenchmark3 {
    @State(Scope.Benchmark)
    public static class FHIRValueSetState {
        Set<String> set = new HashSet<>();
        
        @Setup
        public void setUp() throws Exception {
            Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource("procedureReasonCodes.txt").toURI());
            Files.lines(path)
                 .forEach(line -> {
                     set.add("http://snomed.info/sct|" + line.trim());
                 });
        }
    }
    
    @Benchmark
    public void lookup(FHIRValueSetState state) throws Exception {
        state.set.contains("http://snomed.info/sct|718854004");
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRValueSetBenchmark3.class)
                .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
                .run();
    }
}
