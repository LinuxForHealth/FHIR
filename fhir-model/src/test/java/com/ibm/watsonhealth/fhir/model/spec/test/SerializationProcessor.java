/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

/**
 * Tests that serialization generates a valid object, which matches the original
 * @author rarnold
 *
 */
public class SerializationProcessor implements IExampleProcessor {

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.test.spec.IExampleProcessor#process(java.lang.String, com.ibm.watsonhealth.fhir.model.resource.Resource)
     */
    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            FHIRUtil.write(resource, Format.JSON, bos, false);
            
            // Parse the content we just created
            try (InputStreamReader rdr = new InputStreamReader(new ByteArrayInputStream(bos.toByteArray()))) {
                Resource newResource = FHIRUtil.read(resource.getClass(), Format.JSON, rdr);
                
                if (!newResource.equals(resource)) {
                    throw new IllegalStateException("Resource mismatch after JSON write/read");
                }
            }
        }
    }
}
