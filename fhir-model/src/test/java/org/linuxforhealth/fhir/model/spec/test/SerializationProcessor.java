/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.spec.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.test.ResourceComparatorVisitor;

/**
 * Tests that serialization generates a valid object, which matches the original
 */
public class SerializationProcessor implements IExampleProcessor {

    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            FHIRGenerator.generator( Format.JSON, false).generate(resource, bos);
            
            // Parse the content we just created
            try (InputStreamReader rdr = new InputStreamReader(new ByteArrayInputStream(bos.toByteArray()))) {
                Resource newResource = FHIRParser.parser(Format.JSON).parse(rdr);
                
                if (!newResource.equals(resource)) {
                    
                    // Use the ResourceComparatorVisitor to provide some detail about what's different
                    ResourceComparatorVisitor originals = new ResourceComparatorVisitor();
                    resource.accept(resource.getClass().getSimpleName(), originals);
                    
                    ResourceComparatorVisitor others = new ResourceComparatorVisitor();
                    newResource.accept(newResource.getClass().getSimpleName(), others);
                    
                    // Perform a bi-directional comparison of values in the maps
                    ResourceComparatorVisitor.compare(originals.getValues(), others.getValues());
                    
                    throw new IllegalStateException("Resource mismatch after JSON write/read");
                }
            }
        }
    }
}
