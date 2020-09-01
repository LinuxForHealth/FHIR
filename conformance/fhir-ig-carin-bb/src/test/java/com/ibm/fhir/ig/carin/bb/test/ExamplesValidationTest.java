/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import org.testng.annotations.Test;

import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.model.spec.test.SerializationProcessor;
import com.ibm.fhir.validation.test.ValidationProcessor;

public class ExamplesValidationTest {
    @Test
    public void testBBValidationJson() throws Exception {
        R4ExamplesDriver driver = new R4ExamplesDriver();
        SerializationProcessor processor = new SerializationProcessor();
        driver.setProcessor(processor);
        driver.setValidator(new ValidationProcessor());
        driver.processIndex(Index.PROFILES_CARIN_BB_JSON);
    }

    @Test
    public void testBBValidationXML() throws Exception {
        R4ExamplesDriver driver = new R4ExamplesDriver();
        SerializationProcessor processor = new SerializationProcessor();
        driver.setProcessor(processor);
        driver.setValidator(new ValidationProcessor());
        driver.processIndex(Index.PROFILES_CARIN_BB_XML);
    }
}