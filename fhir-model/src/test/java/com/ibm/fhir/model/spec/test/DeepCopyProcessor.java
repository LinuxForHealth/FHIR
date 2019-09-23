/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

import java.io.StringWriter;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.visitor.DeepCopyingVisitor;

/**
 * Tests that DeepCopyingVisitor successfully copies a resource and that the copy is equal to the original
 * @author lmsurpre
 *
 */
public class DeepCopyProcessor implements IExampleProcessor {
    private final DeepCopyingVisitor<? extends Resource> copier;
    
    public DeepCopyProcessor(DeepCopyingVisitor<? extends Resource> copier) {
        this.copier = copier;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.test.spec.IExampleProcessor#process(java.lang.String, com.ibm.fhir.model.resource.Resource)
     */
    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        resource.accept(copier);
        Resource result = copier.getResult();
        
        assertNotSame(result, resource);

        StringWriter writer1 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(resource, writer1);
        StringWriter writer2 = new StringWriter();
        FHIRGenerator.generator(Format.JSON, true).generate(result, writer2);
        assertEquals(writer2.toString(), writer1.toString());
        
        assertEquals(result, resource);
    }
}
