/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author paulbastide
 *
 */
public abstract class BaseSearchTest {

    // ONLY USED IN TEST 
    // TODO: Review with John
    static {
        System.setProperty("javax.xml.accessExternalSchema","file");
    }
    
    @BeforeMethod
    public void startMethod(Method method) {
        System.out.println("Starting Test -> " + method.getName());
    }
    
    @AfterMethod
    public void afterMethod(Method method) {
        System.out.println("End of Test -> " + method.getName());
    }
    
}
