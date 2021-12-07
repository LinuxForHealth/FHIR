/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import com.ibm.fhir.examples.Index;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.visitor.CopyingVisitor;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Exercise the examples driver, which will process each entry in the test
 * resources directory
 */
public class R4ExamplesTest {
    private R4ExamplesDriver driver;
    
    @BeforeClass
    public void setup() {
//        FHIRModelConfig.setCheckReferenceTypes(false);
        driver = new R4ExamplesDriver();
    }

    @Test
    public void serializationTest() throws Exception {
        driver.setProcessor(new SerializationProcessor());
        String index = System.getProperty(this.getClass().getName()
            + ".index", Index.ALL_JSON.name());
        driver.processIndex(Index.valueOf(index));
    }
    
    @Test
    public void copyTest() throws Exception {
        driver.setProcessor(new CopyProcessor(new CopyingVisitor<Resource>()));
        String index = System.getProperty(this.getClass().getName()
            + ".index", Index.ALL_JSON.name());
        driver.processIndex(Index.valueOf(index));
    }
    
    /**
     * Main method only used for driving ad-hoc testing
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        R4ExamplesTest self = new R4ExamplesTest();
        self.setup();
        // self.driver.setProcessor(new SerializationProcessor());
        self.driver.setProcessor(new CopyProcessor(new CopyingVisitor<Resource>()));
        self.driver.processExample("json/spec/ingredient-example.json", Format.JSON, Expectation.OK);
    }
}
