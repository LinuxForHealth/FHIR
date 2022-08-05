/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.spec.test;

import static org.testng.Assert.assertEquals;

import java.io.StringWriter;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.visitor.CopyingVisitor;

/**
 * Tests that a CopyingVisitor successfully copies a resource and that the copy is equal to the original
 */
public class CopyProcessor implements IExampleProcessor {
    private final CopyingVisitor<? extends Resource> copier;
    
    public CopyProcessor(CopyingVisitor<? extends Resource> copier) {
        this.copier = copier;
    }
    
    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        resource.accept(copier);
        Resource result = copier.getResult();
        
        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(resource, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);
        assertEquals(writer2.toString(), writer1.toString());
        
        assertEquals(result, resource);
    }
}
