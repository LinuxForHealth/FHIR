/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.examples.Index;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.spec.test.Expectation;
import org.linuxforhealth.fhir.model.spec.test.R4ExamplesDriver;
import org.linuxforhealth.fhir.model.spec.test.SerializationProcessor;

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
                + ".index", Index.SPEC_JSON.name());
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
        self.driver.processExample("json/spec/extension-event-statusreason.json", Format.JSON, Expectation.OK);
    }
}
