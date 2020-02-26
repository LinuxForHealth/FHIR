/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.benchmark.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.format.Format;

public final class BenchmarkUtil {
    private static final Random RANDOM = new Random();
    public static final List<String> SPEC_EXAMPLE_NAMES = buildSpecExampleNames();
    
    private BenchmarkUtil() { }
    
    /**
     * @return the list of spec examples for which we have both an XML and JSON variant 
     */
    private static List<String> buildSpecExampleNames() {
        try (Reader jsonReader = ExamplesUtil.resourceReader("spec-json.txt"); Reader xmlReader = ExamplesUtil.resourceReader("spec-xml.txt")) {
            Set<String> jsonExampleNames = new BufferedReader(jsonReader)
                    .lines()
                    .filter(line -> line.startsWith("OK"))
                    .map(line -> line.substring(line.lastIndexOf("/") + 1, line.lastIndexOf(".")))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            Set<String> xmlExampleNames = new BufferedReader(xmlReader)
                    .lines()
                    .filter(line -> line.startsWith("OK"))
                    .map(line -> line.substring(line.lastIndexOf("/") + 1, line.lastIndexOf(".")))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            Set<String> exampleNames = new LinkedHashSet<>(xmlExampleNames);
            exampleNames.retainAll(jsonExampleNames);
            return Collections.unmodifiableList(new ArrayList<>(exampleNames));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    
    public static List<String> getSpecExampleNames() {
        return SPEC_EXAMPLE_NAMES;
    }
    
    public static String getSpecExample(Format format, String exampleName) {
        String resource;
        switch (format) {
        case JSON:
            resource = "json/spec/" + exampleName + ".json";
            break;
        case XML:
            resource = "xml/spec/" + exampleName + ".xml";
            break;
        case RDF:
        default:
            throw new IllegalArgumentException("No examples available for format: " + format);
        }
        try (Reader reader = ExamplesUtil.resourceReader(resource)) {
            return new BufferedReader(reader).lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    
    public static int getSpecExampleSize(Format format, String exampleName) {
        return getSpecExample(format, exampleName).length();
    }
    
    public static String getRandomSpecExample(Format format) {
        return getSpecExample(format, getRandomSpecExampleName());
    }
    
    public static String getRandomSpecExampleName() {
        return SPEC_EXAMPLE_NAMES.get(RANDOM.nextInt(SPEC_EXAMPLE_NAMES.size()));
    }
    
    public static Writer createNOPWriter() {
        return new Writer() {
            @Override
            public Writer append(char c) {
                // do nothing
                return this;
            }
            
            @Override
            public Writer append(CharSequence csq) {
                // do nothing
                return this;
            }
            
            @Override
            public Writer append(CharSequence csq, int start, int end) {
                // do nothing
                return this;
            }
            
            @Override
            public void close() throws IOException {
                // do nothing
            }

            @Override
            public void flush() throws IOException {
                // do nothing
            }
            
            @Override
            public void write(char[] cbuf) throws IOException {
                // do nothing
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                // do nothing
            }
            
            @Override
            public void write(int c) throws IOException {
                // do nothing
            }
            
            @Override
            public void write(String str) throws IOException {
                // do nothing
            }
            
            @Override
            public void write(String str, int off, int len) throws IOException {
                // do nothing
            }
        };
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < SPEC_EXAMPLE_NAMES.size(); i++) {
            System.out.println(getRandomSpecExample(Format.JSON));
        }
    }
}
