/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.CopyProcessor;
import com.ibm.fhir.model.spec.test.Expectation;
import com.ibm.fhir.model.spec.test.R4ExamplesDriver;
import com.ibm.fhir.model.spec.test.SerializationProcessor;
import com.ibm.fhir.model.visitor.CopyingVisitor;

/**
 * Exercise the examples driver, which will process each entry in the test
 * resources directory
 * @author rarnold
 *
 */
public class R4ExamplesValidatorTest {
    private R4ExamplesDriver driver;
    
    @BeforeClass
    public void setup() {
        driver = new R4ExamplesDriver();
    }

    @Test
    public void serializationTest() throws Exception {
        driver.setProcessor(new SerializationProcessor());
        driver.setValidator(new ValidationProcessor());
        driver.processAllExamples();
    }
    
    @Test
    public void copyTest() throws Exception {
        driver.setProcessor(new CopyProcessor(new CopyingVisitor<Resource>()));
        driver.processAllExamples();
    }
    
    /**
     * Main method only used for driving ad-hoc testing
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        R4ExamplesValidatorTest self = new R4ExamplesValidatorTest();
        self.setup();
        self.driver.setProcessor(new SerializationProcessor());
        self.driver.setValidator(new ValidationProcessor());
        self.driver.processExample("xml/ibm/complete-mock/RiskAssessment-1.xml", Format.XML, Expectation.OK);
    }
}
