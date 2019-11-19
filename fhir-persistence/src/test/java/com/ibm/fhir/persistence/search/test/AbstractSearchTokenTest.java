/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * @author lmsurpre
 * @see https://hl7.org/fhir/r4/search.html#token
 */
public abstract class AbstractSearchTokenTest extends AbstractPLSearchTest {

    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicToken.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("token");
    }

    // Searching strings as tokens is not currently supported
//    @Test
//    public void testSearchToken_string() throws Exception {
//        assertSearchReturnsSavedResource("string", "testString");
//        assertSearchReturnsSavedResource("string", "|testString");
//    }
    
    @Test
    public void testSearchToken_boolean() throws Exception {
        assertSearchReturnsSavedResource("boolean", "true");
        assertSearchDoesntReturnSavedResource("boolean", "false");
        
        // FHIR boolean values have an implicit code of http://hl7.org/fhir/special-values
        // so I think the "|true" variant should return empty 
        // and the "http://hl7.org/fhir/special-values|true" variant should return the resource.
//        assertSearchDoesntReturnSavedResource("boolean", "|true");
        assertSearchDoesntReturnSavedResource("boolean", "|false");
        
//        assertSearchReturnsSavedResource("boolean", "http://hl7.org/fhir/special-values|true");
        assertSearchDoesntReturnSavedResource("boolean", "http://hl7.org/fhir/special-values|false");
    }
    
    @Test
    public void testSearchToken_boolean_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.boolean", "true");
        assertSearchDoesntReturnComposition("subject:Basic.boolean", "false");
        
        // FHIR boolean values have an implicit code of http://hl7.org/fhir/special-values
        // so I think the "|true" variant should return empty 
        // and the "http://hl7.org/fhir/special-values|true" variant should return the resource.
//        assertSearchDoesntReturnComposition("subject:Basic.boolean", "|true");
        assertSearchDoesntReturnComposition("subject:Basic.boolean", "|false");
        
//        assertSearchReturnsComposition("subject:Basic.boolean", "http://hl7.org/fhir/special-values|true");
        assertSearchDoesntReturnComposition("subject:Basic.boolean", "http://hl7.org/fhir/special-values|false");
    }
    
    @Test
    public void testSearchToken_boolean_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("boolean", Collections.singletonList("true"));
        System.out.println("Basic --> "+savedResource);
        System.out.println("Composition --> " + composition);
        
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }
    
    
    @Test
    public void testSearchToken_boolean_missing() throws Exception {
        assertSearchReturnsSavedResource("boolean:missing", "false");
        assertSearchDoesntReturnSavedResource("boolean:missing", "true");
        
        assertSearchReturnsSavedResource("missing-boolean:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-boolean:missing", "false");
    }

//    @Test
//    public void testSearchToken_boolean_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.boolean:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.boolean:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-boolean:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-boolean:missing", "false");
//    }
    
    @Test
    public void testSearchToken_code() throws Exception {
        assertSearchReturnsSavedResource("code", "code");
        assertSearchReturnsSavedResource("code", "|code");
    }
    
    @Test
    public void testSearchToken_code_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.code", "code");
        assertSearchReturnsComposition("subject:Basic.code", "|code");
    }
    
    @Test
    public void testSearchToken_code_missing() throws Exception {
        assertSearchReturnsSavedResource("code:missing", "false");
        assertSearchDoesntReturnSavedResource("code:missing", "true");
        
        assertSearchReturnsSavedResource("missing-code:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-code:missing", "false");
    }

//    @Test
//    public void testSearchToken_code_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.code:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.code:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-code:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-code:missing", "false");
//    }
    
    @Test
    public void testSearchToken_Coding() throws Exception {
        assertSearchReturnsSavedResource("Coding", "code");
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
//        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|");
        
        // This shouldn't return any results because the Coding has a system
//        assertSearchDoesntReturnSavedResource("Coding", "|code");
    }
    
    @Test
    public void testSearchToken_Coding_or() throws Exception {
        assertSearchReturnsSavedResource("Coding", "foo,code,bar");
        assertSearchDoesntReturnSavedResource("Coding", "foo\\,code,bar");
        assertSearchDoesntReturnSavedResource("Coding", "foo,code\\,bar");
    }
    
    @Test
    public void testSearchToken_Coding_escaped() throws Exception {
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
        assertSearchDoesntReturnSavedResource("Coding", "http://example.org/codesystem\\|code");
    }
    
    @Test
    public void testSearchToken_Coding_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Coding", "code");
        assertSearchReturnsComposition("subject:Basic.Coding", "http://example.org/codesystem|code");
//        assertSearchReturnsComposition("subject:Basic.Coding", "http://example.org/codesystem|");
        
        // This shouldn't return any results because the Coding has a system
//        assertSearchDoesntReturnComposition("subject:Basic.Coding", "|code");
    }
    
    @Test
    public void testSearchDate_Coding_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Coding-noSystem", "code");
        assertSearchReturnsSavedResource("Coding-noSystem", "|code");
    }
    
    // Currently codings with no code are skipped
//    @Test
//    public void testSearchDate_Coding_NoCode() throws Exception {
//        assertSearchReturnsSavedResource("Coding-noCode", "http://example.org/codesystem|");
//    }
    
    @Test
    public void testSearchToken_Coding_missing() throws Exception {
        assertSearchReturnsSavedResource("Coding:missing", "false");
        assertSearchDoesntReturnSavedResource("Coding:missing", "true");
        
        assertSearchReturnsSavedResource("missing-Coding:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Coding:missing", "false");
    }

//    @Test
//    public void testSearchToken_Coding_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Coding:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.Coding:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-Coding:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-Coding:missing", "false");
//    }
    
    @Test
    public void testSearchToken_Identifier() throws Exception {
        assertSearchReturnsSavedResource("Identifier", "code");
        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|code");
//        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|");
        
        // This shouldn't return any results because the Identifier has a system
//        assertSearchDoesntReturnSavedResource("Identifier", "|code");
    }
    
    @Test
    public void testSearchToken_Identifier_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Identifier", "code");
        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|code");
//        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|");
        
        // This shouldn't return any results because the Identifier has a system
//        assertSearchDoesntReturnComposition("subject:Basic.Identifier", "|code");
    }
    
    @Test
    public void testSearchDate_Identifier_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Identifier-noSystem", "code");
        assertSearchReturnsSavedResource("Identifier-noSystem", "|code");
    }
    
    // Currently identifiers with no value are skipped
//    @Test
//    public void testSearchDate_Identifier_NoValue() throws Exception {
//        assertSearchReturnsSavedResource("Identifier-noValue", "http://example.org/identifiersystem|");
//    }
    
    @Test
    public void testSearchToken_Identifier_missing() throws Exception {
        assertSearchReturnsSavedResource("Identifier:missing", "false");
        assertSearchDoesntReturnSavedResource("Identifier:missing", "true");
        
        assertSearchReturnsSavedResource("missing-Identifier:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Identifier:missing", "false");
    }

//    @Test
//    public void testSearchToken_Identifier_chained_missing() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Identifier:missing", "false");
//        assertSearchDoesntReturnComposition("subject:Basic.Identifier:missing", "true");
//        
//        assertSearchReturnsComposition("subject:Basic.missing-Identifier:missing", "true");
//        assertSearchDoesntReturnComposition("subject:Basic.missing-Identifier:missing", "false");
//    }
    
    @Test
    public void testSearchToken_ContactPoint() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint", "(555) 675 5745");
    }
    @Test
    public void testSearchToken_ContactPoint_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.ContactPoint", "(555) 675 5745");
    }
    @Test
    public void testSearchToken_ContactPoint_URI() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-uri", "tel:+15556755745");
    }
    @Test
    public void testSearchDate_ContactPoint_HomeFax() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-homeFax", "(555) 675 5745");
    }
    @Test
    public void testSearchDate_ContactPoint_NoUse() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noUse", "test@example.com");
    }
    @Test
    public void testSearchDate_ContactPoint_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "test@example.com");
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "|test@example.com");
    }
}
