/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark.runner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

public class FHIRBenchmarkRunner {
    public static final String PROPERTY_EXAMPLE_NAME = "com.ibm.fhir.benchmark.exampleName";
    public static final String PROPERTY_EXPRESSION = "com.ibm.fhir.benchmark.expression";

    private final Class<?> benchmarkClass;
    private final List<String> properties = new ArrayList<>();
    
    public FHIRBenchmarkRunner(Class<?> benchmarkClass) {
        this.benchmarkClass = benchmarkClass;
    }
    
    public FHIRBenchmarkRunner property(String key, String value) {
        properties.add(String.format("-D%s=%s", key, value));
        return this;
    }
    
    public Collection<RunResult> run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + benchmarkClass.getSimpleName() + ".*")
                .jvmArgsPrepend("-Xms2g", "-Xmx2g")
                .jvmArgsAppend(properties.toArray(new String[properties.size()]))
                .verbosity(VerboseMode.NORMAL)
                .warmupIterations(2)
                .warmupTime(TimeValue.seconds(10))
                .measurementIterations(2)
                .measurementTime(TimeValue.seconds(10))
                .shouldDoGC(true)
                .forks(1)
                .mode(Mode.AverageTime)
                .build();
        return new Runner(opt).run();
    }
}
