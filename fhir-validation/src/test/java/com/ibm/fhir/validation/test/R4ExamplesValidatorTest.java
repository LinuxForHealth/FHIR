/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.spec.test.Expectation;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.model.spec.test.SerializationProcessor;

/**
 * Exercise the examples driver, which will process each entry in the test
 * resources directory
 */
public class R4ExamplesValidatorTest {
    private R4ExamplesDriver driver;

    @BeforeClass
    public void setup() {
        driver = new R4ExamplesDriver();
    }

    @Test
    public void validationTest() throws Exception {
        driver.setProcessor(new SerializationProcessor());
        driver.setValidator(new ValidationProcessor());
        String index = System.getProperty(this.getClass().getName()
                + ".index", Index.MINIMAL_JSON.name());
        driver.processIndex(Index.valueOf(index));
    }

    /**
     * Main method only used for driving ad-hoc testing
     */
    public static void main(String[] args) throws Exception {
        R4ExamplesValidatorTest self = new R4ExamplesValidatorTest();
        self.setup();
        self.driver.setProcessor(new SerializationProcessor());
        self.driver.setValidator(new ValidationProcessor());
        self.driver.processExample("json/spec/dataelements.json", Format.JSON, Expectation.OK);
    }
}
