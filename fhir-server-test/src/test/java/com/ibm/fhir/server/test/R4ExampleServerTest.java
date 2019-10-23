/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import org.testng.annotations.Test;

import com.ibm.fhir.model.spec.test.DriverMetrics;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.validation.test.ValidationProcessor;

/**
 * Basic sniff test of the FHIR Server.
 */
public class R4ExampleServerTest extends FHIRServerTestBase {
    
    /**
     * Process all the examples in the fhir-r4-spec example library
     */
    @Test(groups = { "server-examples" })
    public void processExamples() throws Exception {
        DriverMetrics dm = new DriverMetrics();
        
        // Process each of the examples using the provided ExampleRequestProcessor. We want to
        // validate first before we try and send to FHIR
        final R4ExamplesDriver driver = new R4ExamplesDriver();
        driver.setMetrics(dm);
        driver.setValidator(new ValidationProcessor());
        driver.setProcessor(new ExampleRequestProcessor(this, null, dm, 1));
        
        driver.processAllExamples();
     }
}
