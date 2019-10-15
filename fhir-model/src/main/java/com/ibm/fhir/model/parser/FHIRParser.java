/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.parser;

import java.io.InputStream;
import java.io.Reader;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Parse FHIR resource representations into fhir-model objects
 */
public interface FHIRParser {
    /**
     * Read a resource from the passed InputStream. This method does not close the passed InputStream.
     * 
     * @param <T> The resource type to read
     * @param in
     * @return
     * @throws FHIRParserException
     * @throws ClassCastException If the InputStream contains a FHIR resource type that cannot be cast to the requested type 
     */
    <T extends Resource> T parse(InputStream in) throws FHIRParserException;
    
    /**
     * Read a resource using the passed Reader. This method does not close the passed Reader.
     * 
     * @param <T> The resource type to read
     * @param reader
     * @return
     * @throws FHIRParserException
     * @throws ClassCastException If the InputStream contains a FHIR resource type that cannot be cast to the requested type 
     */
    <T extends Resource> T parse(Reader reader) throws FHIRParserException;
    
    /**
     * Attempt to cast the FHIRParser to a specific subclass
     * 
     * @param <T> The FHIRParser subclass to cast to
     * @return
     * @throws ClassCastException If the InputStream contains a FHIR resource type that cannot be cast to the requested type 
     */
    default <T extends FHIRParser> T as(Class<T> parserClass) {
        return parserClass.cast(this);
    }
    
    /**
     * Create a FHIRParser for the given format.
     * 
     * @param format
     * @return
     * @throws IllegalArgumentException if {@code format} is not supported
     */
    static FHIRParser parser(Format format) {
        switch (format) {
        case JSON:
            return new FHIRJsonParser();
        case XML:
            return new FHIRXMLParser();
        case RDF:
        default:
            throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}
