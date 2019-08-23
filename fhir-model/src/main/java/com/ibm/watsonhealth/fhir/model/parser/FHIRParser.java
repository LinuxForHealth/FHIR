/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.parser;

import java.io.InputStream;
import java.io.Reader;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.parser.exception.FHIRParserException;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

public interface FHIRParser {
    <T extends Resource> T parse(InputStream in) throws FHIRParserException;
    <T extends Resource> T parse(Reader reader) throws FHIRParserException;
    
    void reset();
    
    default <T extends FHIRParser> T as(Class<T> parserClass) {
        return parserClass.cast(this);
    }
    
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
