package com.ibm.fhir.benchmark;

import static com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner.PROPERTY_EXAMPLE_NAME;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Set;

import org.openjdk.jmh.annotations.Benchmark;

import com.ibm.fhir.benchmark.runner.FHIRBenchmarkRunner;
import com.ibm.fhir.benchmark.util.BenchmarkUtil;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.PublicationStatus;

public class FHIRValueSetBenchmark {
    @Benchmark
    public void readFile() throws Exception {
        final ValueSet.Builder vsBuilder = ValueSet.builder().status(PublicationStatus.DRAFT);
        final ValueSet.Expansion.Builder expansionBuilder = ValueSet.Expansion.builder().timestamp(DateTime.now());
        final ValueSet.Expansion.Contains.Builder template = ValueSet.Expansion.Contains.builder().system(Uri.of("http://snomed.info/sct"));
        
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("procedureReasonCodes.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            reader.lines()
            .forEach(line -> {
                expansionBuilder.contains(template.code(Code.of(line.trim())).build());
            });
        }
        ValueSet valueSet = vsBuilder.expansion(expansionBuilder.build()).build();
    }
    
    @Benchmark
    public void readResource() throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("procedureReasonCodes.json");
        ValueSet valueSet = FHIRParser.parser(Format.JSON).parse(in);
    }
    
    @Benchmark
    public void readSerializedSet() throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("procedureReasonCodes-set.ser");
        try (ObjectInputStream is = new ObjectInputStream(in)) {
            Set set = (Set) is.readObject();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new FHIRBenchmarkRunner(FHIRValueSetBenchmark.class)
                .property(PROPERTY_EXAMPLE_NAME, BenchmarkUtil.getRandomSpecExampleName())
                .run();
    }
}
