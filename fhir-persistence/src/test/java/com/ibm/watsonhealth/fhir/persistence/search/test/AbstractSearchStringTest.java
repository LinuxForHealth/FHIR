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
 * @see https://hl7.org/fhir/dstu2/search.html#string
 */
public abstract class AbstractSearchStringTest extends AbstractPLSearchTest {

    @Test
    public void testCreateBasicResource() throws Exception {
        Basic resource = readResource(Basic.class, "BasicString.json");
        saveBasicResource(resource);
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testCreateChainedBasicResource() throws Exception {
        createCompositionReferencingSavedResource();
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchString_string() throws Exception {
        assertSearchReturnsSavedResource("string:exact", "testString");
        
        assertSearchReturnsSavedResource("string", "testString");
        assertSearchReturnsSavedResource("string", "test");
        
        assertSearchReturnsSavedResource("string:contains", "String");
        assertSearchReturnsSavedResource("string:contains", "string");
        
        assertSearchDoesntReturnSavedResource("string", "String");
        assertSearchDoesntReturnSavedResource("string:exact", "test");
        assertSearchDoesntReturnSavedResource("string:exact", "teststring");
        
        // TODO add test for diacritics and other unusual characters
    }
    
    @Test(dependsOnMethods = { "testCreateChainedBasicResource" })
    public void testSearchString_string_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.string:exact", "testString");
        
        assertSearchReturnsComposition("subject:Basic.string", "testString");
        assertSearchReturnsComposition("subject:Basic.string", "test");
        
CODE_REMOVED
        assertSearchReturnsComposition("subject:Basic.string:contains", "String");
        assertSearchReturnsComposition("subject:Basic.string:contains", "string");
        
        assertSearchDoesntReturnComposition("subject:Basic.string", "String");
CODE_REMOVED
        assertSearchDoesntReturnComposition("subject:Basic.string:exact", "test");
        assertSearchDoesntReturnComposition("subject:Basic.string:exact", "teststring");
        
        // TODO add test for diacritics and other unusual characters
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchString_string_missing() throws Exception {
        assertSearchReturnsSavedResource("string:missing", "false");
        assertSearchDoesntReturnSavedResource("string:missing", "true");
        
        assertSearchReturnsSavedResource("missing-string:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-string:missing", "false");
    }
    
CODE_REMOVED
//    @Test(dependsOnMethods = { "testCreateChainedBasicResource" })
//    public void testSearchString_string_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.string:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.string:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-string:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-string:missing", "false");
//    }
    
    // TODO add tests for Address and HumanName
}
