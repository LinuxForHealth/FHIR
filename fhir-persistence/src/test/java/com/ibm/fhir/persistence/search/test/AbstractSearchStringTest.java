/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.search.exception.FHIRSearchException;

/**
 * @author lmsurpre
 * @see https://hl7.org/fhir/r4/search.html#string
 */
public abstract class AbstractSearchStringTest extends AbstractPLSearchTest {

    protected Basic getBasicResource() throws Exception {
        return readExampleResource("json/ibm/basic/BasicString.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("string");
    }

    @Test
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
    
    @Test
    public void testSearchString_string_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.string:exact", "testString");
        
        assertSearchReturnsComposition("subject:Basic.string", "testString");
        assertSearchReturnsComposition("subject:Basic.string", "test");
        
        assertSearchReturnsComposition("subject:Basic.string:contains", "String");
        assertSearchReturnsComposition("subject:Basic.string:contains", "string");
        
        assertSearchDoesntReturnComposition("subject:Basic.string", "String");

        assertSearchDoesntReturnComposition("subject:Basic.string:exact", "test");
        assertSearchDoesntReturnComposition("subject:Basic.string:exact", "teststring");
        
        // TODO add test for diacritics and other unusual characters
    }
    
    @Test
    public void testSearchString_string_missing() throws Exception {
        assertSearchReturnsSavedResource("string:missing", "false");
        assertSearchDoesntReturnSavedResource("string:missing", "true");
        
        assertSearchReturnsSavedResource("missing-string:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-string:missing", "false");
    }
    
    @Test
    public void testSearchString_string_or() throws Exception {
        assertSearchReturnsSavedResource("string:exact", "foo,testString,bar");
        assertSearchDoesntReturnSavedResource("string:exact", "foo\\,testString,bar");
        assertSearchDoesntReturnSavedResource("string:exact", "foo,testString\\,bar");
    }
    
    @Test
    public void testSearchString_string_escaping() throws Exception {
        assertSearchReturnsSavedResource("string:exact", "special testChars & : ; \\$ \\| \\, \\\\");
        assertSearchReturnsSavedResource("string:contains", "& : ; \\$ \\| \\, \\\\");
    }
    
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchString_string_invalidEscaping() throws Exception {
        runQueryTest(Basic.class, "string", "\\", Integer.MAX_VALUE);
    }
    
//    @Test
//    public void testSearchString_string_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.string:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.string:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-string:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-string:missing", "false");
//    }
    
    // TODO add tests for Address and HumanName
}
