/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.visitor.CopyingVisitor;

/**
 * Exercise the examples driver, which will process each entry in the test
 * resources directory
 * @author rarnold
 *
 */
public class R4ExamplesTest {
    private R4ExamplesDriver driver;
    
    @BeforeClass
    public void setup() {
        driver = new R4ExamplesDriver();
    }

    @Test
	public void serializationTest() throws Exception {
		driver.setProcessor(new SerializationProcessor());
		
		if ("true".equalsIgnoreCase(System.getProperty(this.getClass().getName() + ".withValidation"))) {
		    driver.setValidator(new ValidationProcessor());
		}
		
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
        R4ExamplesTest self = new R4ExamplesTest();
        self.setup();
        self.driver.setProcessor(new SerializationProcessor());
        self.driver.setValidator(new ValidationProcessor());
        
        self.driver.processExample("json/ibm/complete-absent/Parameters-28.json", Expectation.OK);
    }
}
