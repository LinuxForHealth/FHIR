/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.generator;

import java.io.OutputStream;
import java.io.Writer;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.visitor.Visitable;

public interface FHIRGenerator {
    public static final java.lang.String PROPERTY_INDENT_AMOUNT = "com.ibm.fhir.model.generator.indentAmount";
    
    void generate(Visitable visitable, OutputStream out) throws FHIRGeneratorException;
    void generate(Visitable visitable, Writer writer) throws FHIRGeneratorException;

    boolean isPrettyPrinting();
    void reset();
    
    void setProperty(String name, Object value);
    Object getProperty(String name);
    Object getPropertyOrDefault(String name, Object defaultValue);
    <T> T getProperty(String name, Class<T> type);
    <T> T getPropertyOrDefault(String name, T defaultValue, Class<T> type);
    boolean isPropertySupported(String name);
    
    <T extends FHIRGenerator> T as(Class<T> generatorClass);
    
    static FHIRGenerator generator(Format format, boolean prettyPrinting) {
        switch (format) {
        case JSON:
            return new FHIRJsonGenerator(prettyPrinting);
        case XML:
            return new FHIRXMLGenerator(prettyPrinting);
        case RDF:
        default:
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
    
    static FHIRGenerator generator(Format format) {
        return generator(format, false);
    }
}
