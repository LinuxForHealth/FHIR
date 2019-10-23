/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import static org.testng.Assert.assertEquals;

import java.io.StringWriter;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.visitor.CopyingVisitor;

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
