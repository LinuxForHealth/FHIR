/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import static org.testng.Assert.assertEquals;

import java.io.StringWriter;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.visitor.CopyingVisitor;

/**
 * Tests that a CopyingVisitor successfully copies a resource and that the copy is equal to the original
 * @author lmsurpre
 */
public class CopyProcessor implements IExampleProcessor {
    private final CopyingVisitor<? extends Resource> copier;
    
    public CopyProcessor(CopyingVisitor<? extends Resource> copier) {
        this.copier = copier;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.test.spec.IExampleProcessor#process(java.lang.String, com.ibm.watsonhealth.fhir.model.resource.Resource)
     */
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
