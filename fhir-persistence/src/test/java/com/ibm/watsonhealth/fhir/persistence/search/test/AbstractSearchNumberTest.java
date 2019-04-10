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
    public void testCreateChainedBasicResource() throws Exception {
        createCompositionReferencingSavedResource();
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
    
    @Test(dependsOnMethods = { "testCreateChainedBasicResource" })
    public void testSearchNumber_integer_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.integer", "12");
        assertSearchReturnsComposition("subject:Basic.integer", "1.2e1");
        
        assertSearchReturnsComposition("subject:Basic.integer", "lt13");
        assertSearchReturnsComposition("subject:Basic.integer", "gt11");
        assertSearchReturnsComposition("subject:Basic.integer", "le12");
        assertSearchReturnsComposition("subject:Basic.integer", "le13");
        assertSearchReturnsComposition("subject:Basic.integer", "ge12");
        assertSearchReturnsComposition("subject:Basic.integer", "ge11");
        
        assertSearchReturnsComposition("subject:Basic.integer", "ne13");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_integer_missing() throws Exception {
        assertSearchReturnsSavedResource("integer:missing", "false");
        assertSearchDoesntReturnSavedResource("integer:missing", "true");
        
        assertSearchReturnsSavedResource("missing-integer:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-integer:missing", "false");
    }
    
CODE_REMOVED
//    @Test(dependsOnMethods = { "testCreateChainedBasicResource" })
//    public void testSearchNumber_integer_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.integer:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.integer:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-integer:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-integer:missing", "false");
//    }
    
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
    
    @Test(dependsOnMethods = { "testCreateChainedBasicResource" })
    public void testSearchNumber_decimal_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.decimal", "99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "9999e-2");
        
        assertSearchReturnsComposition("subject:Basic.decimal", "lt100");
        assertSearchReturnsComposition("subject:Basic.decimal", "gt99");
        assertSearchReturnsComposition("subject:Basic.decimal", "le99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "le100");
        assertSearchReturnsComposition("subject:Basic.decimal", "ge99.99");
        assertSearchReturnsComposition("subject:Basic.decimal", "ge99");
        
        assertSearchReturnsComposition("subject:Basic.decimal", "ne13");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchNumber_decimal_missing() throws Exception {
        assertSearchReturnsSavedResource("decimal:missing", "false");
        assertSearchDoesntReturnSavedResource("decimal:missing", "true");
        
        assertSearchReturnsSavedResource("missing-decimal:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-decimal:missing", "false");
    }
    
CODE_REMOVED
//    @Test(dependsOnMethods = { "testCreateChainedBasicResource" })
//    public void testSearchNumber_decimal_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.decimal:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.decimal:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-decimal:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-decimal:missing", "false");
//    }
}
