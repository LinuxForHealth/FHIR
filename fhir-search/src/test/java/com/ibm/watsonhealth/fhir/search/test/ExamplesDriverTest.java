/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.watsonhealth.fhir.model.spec.test.R4ExamplesDriver.TestType;
import com.ibm.watsonhealth.fhir.model.spec.test.ValidationProcessor;

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
        
        // TODO switch to ALL one the generated examples can be validated cleanly
        driver.processExamples(TestType.ALL);
     }
    
}
