/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * @author lmsurpre
 * @see https://hl7.org/fhir/r4/search.html#token
 */
public abstract class AbstractSearchCompositeTest extends AbstractPLSearchTest {

    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicComposite.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("composite");
    }

    @Test
    public void testSearchToken_boolean() throws Exception {
        assertSearchReturnsSavedResource("composite-boolean", "true$true");
        assertSearchDoesntReturnSavedResource("composite-boolean", "false$true");
        assertSearchDoesntReturnSavedResource("composite-boolean", "true$false");
        assertSearchDoesntReturnSavedResource("composite-boolean", "false$false");
    }
    
    @Test
    public void testSearchToken_boolean_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.composite-boolean", "true$true");
        assertSearchDoesntReturnComposition("subject:Basic.composite-boolean", "true$false");
        assertSearchDoesntReturnComposition("subject:Basic.composite-boolean", "false$true");
        assertSearchDoesntReturnComposition("subject:Basic.composite-boolean", "false$false");
    }

//    @Test
//    public void testSearchToken_boolean_revinclude() throws Exception {
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
//        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
//        queryParms.put("boolean", Collections.singletonList("true"));
//        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
//        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
//    }
//    
//    
//    @Test
//    public void testSearchToken_boolean_missing() throws Exception {
//        assertSearchReturnsSavedResource("boolean:missing", "false");
//        assertSearchDoesntReturnSavedResource("boolean:missing", "true");
//        
//        assertSearchReturnsSavedResource("missing-boolean:missing", "true");
//        assertSearchDoesntReturnSavedResource("missing-boolean:missing", "false");
//    }
//    
//    @Test
//    public void testSearchToken_code() throws Exception {
//        assertSearchReturnsSavedResource("code", "code");
//        assertSearchReturnsSavedResource("code", "|code");
//    }
//    
//    @Test
//    public void testSearchToken_code_chained() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.code", "code");
//        assertSearchReturnsComposition("subject:Basic.code", "|code");
//    }
//
//    @Test
//    public void testSearchToken_CodeableConcept() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept", "code");
//        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|code");
//    }
//    
//    @Test
//    public void testSearchToken_CodeableConcept_or() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept", "foo,code,bar");
//        assertSearchDoesntReturnSavedResource("CodeableConcept", "foo\\,code,bar");
//        assertSearchDoesntReturnSavedResource("CodeableConcept", "foo,code\\,bar");
//    }
//    
//    @Test
//    public void testSearchToken_CodeableConcept_escaped() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept", "http://example.org/codesystem|code");
//        assertSearchDoesntReturnSavedResource("CodeableConcept", "http://example.org/codesystem\\|code");
//    }
//    
//    @Test
//    public void testSearchToken_CodeableConcept_chained() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.CodeableConcept", "code");
//        assertSearchReturnsComposition("subject:Basic.CodeableConcept", "http://example.org/codesystem|code");
//    }
//    
//    @Test
//    public void testSearchDate_CodeableConcept_multiCoded() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept-multiCoded", "http://example.org/codesystem|code");
//        assertSearchReturnsSavedResource("CodeableConcept-multiCoded", "http://example.org/othersystem|code");
//    }
//    
//    @Test
//    public void testSearchToken_CodeableConcept_missing() throws Exception {
//        assertSearchReturnsSavedResource("CodeableConcept:missing", "false");
//        assertSearchDoesntReturnSavedResource("CodeableConcept:missing", "true");
//        
//        assertSearchReturnsSavedResource("missing-CodeableConcept:missing", "true");
//        assertSearchDoesntReturnSavedResource("missing-CodeableConcept:missing", "false");
//    }
//    
//    @Test
//    public void testSearchToken_Coding() throws Exception {
//        assertSearchReturnsSavedResource("Coding", "code");
//        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
//    }
//    
//    @Test
//    public void testSearchToken_Coding_or() throws Exception {
//        assertSearchReturnsSavedResource("Coding", "foo,code,bar");
//        assertSearchDoesntReturnSavedResource("Coding", "foo\\,code,bar");
//        assertSearchDoesntReturnSavedResource("Coding", "foo,code\\,bar");
//    }
//    
//    @Test
//    public void testSearchToken_Coding_escaped() throws Exception {
//        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
//        assertSearchDoesntReturnSavedResource("Coding", "http://example.org/codesystem\\|code");
//    }
//    
//    @Test
//    public void testSearchToken_Coding_chained() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Coding", "code");
//        assertSearchReturnsComposition("subject:Basic.Coding", "http://example.org/codesystem|code");
//    }
//    
//    @Test
//    public void testSearchDate_Coding_NoSystem() throws Exception {
//        assertSearchReturnsSavedResource("Coding-noSystem", "code");
//        assertSearchReturnsSavedResource("Coding-noSystem", "|code");
//    }
//    
//    @Test
//    public void testSearchToken_Identifier() throws Exception {
//        assertSearchReturnsSavedResource("Identifier", "code");
//        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|code");
//    }
//    
//    @Test
//    public void testSearchToken_Identifier_chained() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.Identifier", "code");
//        assertSearchReturnsComposition("subject:Basic.Identifier", "http://example.org/identifiersystem|code");
//    }
//    
//    @Test
//    public void testSearchDate_Identifier_NoSystem() throws Exception {
//        assertSearchReturnsSavedResource("Identifier-noSystem", "code");
//        assertSearchReturnsSavedResource("Identifier-noSystem", "|code");
//    }
//    
//    @Test
//    public void testSearchToken_ContactPoint() throws Exception {
//        assertSearchReturnsSavedResource("ContactPoint", "(555) 675 5745");
//    }
//    @Test
//    public void testSearchToken_ContactPoint_chained() throws Exception {
//        assertSearchReturnsComposition("subject:Basic.ContactPoint", "(555) 675 5745");
//    }
//    @Test
//    public void testSearchToken_ContactPoint_URI() throws Exception {
//        assertSearchReturnsSavedResource("ContactPoint-uri", "tel:+15556755745");
//    }
//    @Test
//    public void testSearchDate_ContactPoint_HomeFax() throws Exception {
//        assertSearchReturnsSavedResource("ContactPoint-homeFax", "(555) 675 5745");
//    }
//    @Test
//    public void testSearchDate_ContactPoint_NoUse() throws Exception {
//        assertSearchReturnsSavedResource("ContactPoint-noUse", "test@example.com");
//    }
//    @Test
//    public void testSearchDate_ContactPoint_NoSystem() throws Exception {
//        assertSearchReturnsSavedResource("ContactPoint-noSystem", "test@example.com");
//        assertSearchReturnsSavedResource("ContactPoint-noSystem", "|test@example.com");
//    }
}
