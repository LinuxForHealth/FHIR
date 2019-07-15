/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import org.testng.annotations.Test;

/**
 * Exercise the examples driver, which will process each entry in the test
 * resources directory
 * @author rarnold
 *
 */
public class R4ExamplesTest {

    @Test
	public void perform() throws Exception {
		R4ExamplesDriver driver = new R4ExamplesDriver();	
		driver.setProcessor(new SerializationProcessor());
		driver.processAllExamples();
	}
}
