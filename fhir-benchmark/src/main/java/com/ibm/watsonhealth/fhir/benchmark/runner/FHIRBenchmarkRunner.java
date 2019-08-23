/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.benchmark.runner;

import java.util.Collection;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

import com.ibm.watsonhealth.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;

public class FHIRBenchmarkRunner {
    private final Class<?> benchmarkClass;
    private final String exampleName;
    
    public FHIRBenchmarkRunner(Class<?> benchmarkClass, String exampleName) {
        this.benchmarkClass = benchmarkClass;
        this.exampleName = exampleName;
    }
    
    public Collection<RunResult> run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + benchmarkClass.getSimpleName() + ".*")
                .jvmArgsAppend("-Xms2g", "-Xmx2g", "-Dcom.ibm.watsonhealth.fhir.benchmark.exampleName=" + exampleName)
                .verbosity(VerboseMode.NORMAL)
                .warmupIterations(5)
                .warmupTime(TimeValue.seconds(10))
                .measurementTime(TimeValue.seconds(10))
                .measurementIterations(5)
                .shouldDoGC(true)
                .forks(1)
                .build();
        Collection<RunResult> result = new Runner(opt).run();
        System.out.println("");
        System.out.println("# Spec example used: " + exampleName + ", JSON file size (bytes): " + BenchmarkUtil.getSpecExampleSize(Format.JSON, exampleName) + ", XML file size (bytes): " + BenchmarkUtil.getSpecExampleSize(Format.XML, exampleName));
        return result;
    }
}
