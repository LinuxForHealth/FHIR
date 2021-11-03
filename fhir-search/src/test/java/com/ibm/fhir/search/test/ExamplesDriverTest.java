/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.validation.test.ValidationProcessor;

public class ExamplesDriverTest {

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    /**
     * Process all the examples in the fhir-r4-spec example library
     */
    @Test(groups = { "server-examples" })
    public void processExamples() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        // Process each of the examples using the provided ExampleRequestProcessor. We want to
        // validate first before we try and send to FHIR
        final R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setValidator(new ValidationProcessor());
        driver.setProcessor(new ExtractorRequestProcessor());
        String index = System.getProperty(this.getClass().getName()
            + ".index", Index.MINIMAL_JSON.name());
        driver.processIndex(Index.valueOf(index));
        FHIRRequestContext.remove();
     }
    
}
