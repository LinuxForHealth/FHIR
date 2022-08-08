/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.benchmark.runner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

import org.linuxforhealth.fhir.benchmark.util.BenchmarkUtil;

public class FHIRBenchmarkRunner {
    public static final String PROPERTY_EXAMPLE_NAME = "org.linuxforhealth.fhir.benchmark.exampleName";
    public static final String PROPERTY_EXPRESSION = "org.linuxforhealth.fhir.benchmark.expression";

    private final Class<?> benchmarkClass;
    private final List<String> properties = new ArrayList<>();

    public FHIRBenchmarkRunner(Class<?> benchmarkClass) {
        this.benchmarkClass = benchmarkClass;
    }

    public FHIRBenchmarkRunner property(String key, String value) {
        properties.add(String.format("-D%s=%s", key, value));
        return this;
    }

    /**
     * Run without overriding any parameters
     */
    public Collection<RunResult> run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + benchmarkClass.getSimpleName() + ".*")
                .jvmArgsPrepend("-Xms2g", "-Xmx2g")
                .jvmArgsAppend(properties.toArray(new String[properties.size()]))
                .verbosity(VerboseMode.NORMAL)
                .warmupIterations(1)
                .warmupTime(TimeValue.seconds(10))
                .measurementIterations(2)
                .measurementTime(TimeValue.seconds(10))
                .shouldDoGC(true)
                .forks(2)
                .threads(1)
//              .mode(Mode.AverageTime)
                .addProfiler(StackProfiler.class)
                .build();
        return new Runner(opt).run();
    }

    /**
     * Run and override the 'exampleName' param with the passed fileName
     */
    public Collection<RunResult> run(String fileName) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + benchmarkClass.getSimpleName() + ".*")
                .jvmArgsPrepend("-Xms4g", "-Xmx4g")
                .jvmArgsAppend(properties.toArray(new String[properties.size()]))
                .verbosity(VerboseMode.NORMAL)
                .warmupIterations(1)
                .warmupTime(TimeValue.seconds(10))
                .measurementIterations(2)
                .measurementTime(TimeValue.seconds(10))
                .shouldDoGC(false)
                .forks(1)
//              .mode(Mode.AverageTime)
                .addProfiler(StackProfiler.class)
                .param("exampleName", fileName)
                .build();
        return new Runner(opt).run();
    }

    /**
     * Run the benchmark with all the examples in BenchmarkUtil.SPEC_EXAMPLE_NAMES
     */
    public Collection<RunResult> runAll() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + benchmarkClass.getSimpleName() + ".*")
                .jvmArgsPrepend("-Xms4g", "-Xmx4g")
                .jvmArgsAppend(properties.toArray(new String[properties.size()]))
                .verbosity(VerboseMode.NORMAL)
                .warmupIterations(1)
                .warmupTime(TimeValue.seconds(10))
                .measurementIterations(1)
                .measurementTime(TimeValue.seconds(10))
                .shouldDoGC(true)
                .forks(1)
                .output("results.txt")
                .mode(Mode.SingleShotTime)
                .param("exampleName", BenchmarkUtil.SPEC_EXAMPLE_NAMES.toArray(new String[0])) // https://stackoverflow.com/a/4042464/161022
                .build();
        return new Runner(opt).run();
    }
}
