/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.generator;

import java.io.OutputStream;
import java.io.Writer;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.visitor.Visitable;

/**
 * Generate FHIR resource representations from fhir-model objects
 */
public interface FHIRGenerator {
    /**
     * Property name for a property that controls the amount of indentation to use for each indentation "level"
     */
    public static final java.lang.String PROPERTY_INDENT_AMOUNT = "org.linuxforhealth.fhir.model.generator.indentAmount";
    
    /**
     * Write {@code visitable} to the passed OutputStream. This method does not close the passed OutputStream.
     * 
     * For {@code visitable} of type Resource, this serializes the resource in accordance with the specification.
     * For {@code visitable} of type Element, this serializes the element content using a suitable wrapper for the configured format.
     * 
     * @param visitable The visitable Resource or Element to serialize
     * @param out
     * @throws FHIRGeneratorException
     */
    void generate(Visitable visitable, OutputStream out) throws FHIRGeneratorException;
    
    /**
     * Write {@code visitable} using the passed Writer. This method does not close the passed Writer.
     * 
     * For {@code visitable} of type Resource, this serializes the resource in accordance with the specification.
     * For {@code visitable} of type Element, this serializes the element content using a suitable wrapper for the configured format.
     * 
     * @param visitable The visitable Resource or Element to serialize
     * @param writer
     * @throws FHIRGeneratorException
     */
    void generate(Visitable visitable, Writer writer) throws FHIRGeneratorException;

    /**
     * @return whether this FHIRGenerator is configured to pretty-print its output
     */
    boolean isPrettyPrinting();
    
    /**
     * Set the property with the given name to the passed value
     * 
     * @throws IllegalArgumentException if the property {@code name} is unknown or unsupported
     * @see {@link #isPropertySupported(String)}
     */
    void setProperty(String name, Object value);
    
    /**
     * @return the property value or {@code null} if the property has no value
     */
    Object getProperty(String name);
    
    /**
     * @return the property value or {@code defaultValue} if the property has no value
     */
    Object getPropertyOrDefault(String name, Object defaultValue);
    
    /**
     * @return the property value or {@code null} if the property has no value
     */
    <T> T getProperty(String name, Class<T> type);
    
    /**
     * @return the property value or {@code defaultValue} if the property has no value
     */
    <T> T getPropertyOrDefault(String name, T defaultValue, Class<T> type);
    
    /**
     * Whether the generator supports the property with the passed name
     */
    boolean isPropertySupported(String name);
    
    <T extends FHIRGenerator> T as(Class<T> generatorClass);
    
    /**
     * Create a FHIRGenerator (without pretty-printing) for the given format.
     * 
     * @param format
     * @return
     * @throws IllegalArgumentException if {@code format} is not supported
     */
    static FHIRGenerator generator(Format format) {
        return generator(format, false);
    }
    
    /**
     * Create a FHIRGenerator for the given format.
     * 
     * @param format
     * @param prettyPrinting whether the returned FHIRGenerator should pretty-print its output
     * @return
     * @throws IllegalArgumentException if {@code format} is not supported
     */
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
}
