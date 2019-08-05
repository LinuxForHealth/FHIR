/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.generator;

import java.io.OutputStream;
import java.io.Writer;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watsonhealth.fhir.model.resource.Resource;

public interface FHIRGenerator {
    void generate(Resource resource, OutputStream out) throws FHIRGeneratorException;
    void generate(Resource resource, Writer writer) throws FHIRGeneratorException;
    boolean isPrettyPrinting();
    void reset();
    default <T extends FHIRGenerator> T as(Class<T> generatorClass) {
        return generatorClass.cast(this);
    }
    static FHIRGenerator generator(Format format, boolean prettyPrinting) {
        switch (format) {
        case JSON:
            return new FHIRJsonGenerator(prettyPrinting);
        case XML:
            return new FHIRXMLGenerator(prettyPrinting);
        case RDF:
        default:
            throw new UnsupportedOperationException(String.format("Unsupported format: %s", format));
        }
    }
    static FHIRGenerator generator(Format format) {
        return generator(format, false);
    }
}
