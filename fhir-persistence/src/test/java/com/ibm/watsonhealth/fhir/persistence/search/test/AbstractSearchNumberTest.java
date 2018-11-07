/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.search.test;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Basic;

/**
 * @author lmsurpre
 * @see https://hl7.org/fhir/dstu2/search.html#number
 */
public abstract class AbstractSearchNumberTest extends AbstractPLSearchTest {

    @Test
    public void testCreateBasicResource() throws Exception {
        Basic resource = readResource(Basic.class, "BasicNumber.json");
        saveBasicResource(resource);
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_integer() throws Exception {
        assertSearchReturnsSavedResource("integer", "12");
        assertSearchReturnsSavedResource("integer", "1.2e1");
        
        assertSearchReturnsSavedResource("integer", "lt13");
        assertSearchReturnsSavedResource("integer", "gt11");
        assertSearchReturnsSavedResource("integer", "le12");
        assertSearchReturnsSavedResource("integer", "le13");
        assertSearchReturnsSavedResource("integer", "ge12");
        assertSearchReturnsSavedResource("integer", "ge11");
        
        assertSearchReturnsSavedResource("integer", "ne13");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_decimal() throws Exception {
        assertSearchReturnsSavedResource("decimal", "99.99");
        assertSearchReturnsSavedResource("decimal", "9999e-2");
        
        assertSearchReturnsSavedResource("decimal", "lt100");
        assertSearchReturnsSavedResource("decimal", "gt99");
        assertSearchReturnsSavedResource("decimal", "le99.99");
        assertSearchReturnsSavedResource("decimal", "le100");
        assertSearchReturnsSavedResource("decimal", "ge99.99");
        assertSearchReturnsSavedResource("decimal", "ge99");
        
        assertSearchReturnsSavedResource("decimal", "ne13");
    }
}
