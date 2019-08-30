/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.search.test;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.watson.health.fhir.model.spec.test.ValidationProcessor;

public class ExamplesDriverTest {

    /**
     * Process all the examples in the fhir-r4-spec example library
     */
    @Test(groups = { "server-examples" })
    public void processExamples() throws Exception {
        
        // Process each of the examples using the provided ExampleRequestProcessor. We want to
        // validate first before we try and send to FHIR
        final R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setValidator(new ValidationProcessor());
        driver.setProcessor(new ExtractorRequestProcessor());
        
        driver.processAllExamples();
     }
    
}
