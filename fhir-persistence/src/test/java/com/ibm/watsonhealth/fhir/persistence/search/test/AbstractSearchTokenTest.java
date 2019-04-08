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
 * @see https://hl7.org/fhir/dstu2/search.html#token
 */
public abstract class AbstractSearchTokenTest extends AbstractPLSearchTest {

    @Test
    public void testCreateBasicResource() throws Exception {
        Basic resource = readResource(Basic.class, "BasicToken.json");
        saveBasicResource(resource);
    }

    // Searching strings as tokens is not currently supported
//    @Test(dependsOnMethods = { "testCreateBasicResource" })
//    public void testSearchToken_string() throws Exception {
//        assertSearchReturnsSavedResource("string", "testString");
//        assertSearchReturnsSavedResource("string", "|testString");
//    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
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
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_boolean_missing() throws Exception {
        assertSearchReturnsSavedResource("boolean:missing", "false");
        assertSearchDoesntReturnSavedResource("boolean:missing", "true");
        
        assertSearchReturnsSavedResource("missing-boolean:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-boolean:missing", "false");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_code() throws Exception {
        assertSearchReturnsSavedResource("extension-code", "code");
        assertSearchReturnsSavedResource("extension-code", "|code");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_code_missing() throws Exception {
        assertSearchReturnsSavedResource("extension-code:missing", "false");
        assertSearchDoesntReturnSavedResource("extension-code:missing", "true");
        
        assertSearchReturnsSavedResource("extension-missing-code:missing", "true");
        assertSearchDoesntReturnSavedResource("extension-missing-code:missing", "false");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_Coding() throws Exception {
        assertSearchReturnsSavedResource("Coding", "code");
        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|code");
//        assertSearchReturnsSavedResource("Coding", "http://example.org/codesystem|");
        
        // This shouldn't return any results because the Coding has a system
//        assertSearchDoesntReturnSavedResource("Coding", "|code");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_Coding_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Coding-noSystem", "code");
        assertSearchReturnsSavedResource("Coding-noSystem", "|code");
    }
    
    // Currently codings with no code are skipped
//    @Test(dependsOnMethods = { "testCreateBasicResource" })
//    public void testSearchDate_Coding_NoCode() throws Exception {
//        assertSearchReturnsSavedResource("Coding-noCode", "http://example.org/codesystem|");
//    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_Coding_missing() throws Exception {
        assertSearchReturnsSavedResource("Coding:missing", "false");
        assertSearchDoesntReturnSavedResource("Coding:missing", "true");
        
        assertSearchReturnsSavedResource("missing-Coding:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Coding:missing", "false");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_Identifier() throws Exception {
        assertSearchReturnsSavedResource("Identifier", "code");
        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|code");
//        assertSearchReturnsSavedResource("Identifier", "http://example.org/identifiersystem|");
        
        // This shouldn't return any results because the Identifier has a system
//        assertSearchDoesntReturnSavedResource("Identifier", "|code");
    }
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_Identifier_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("Identifier-noSystem", "code");
        assertSearchReturnsSavedResource("Identifier-noSystem", "|code");
    }
    
    // Currently identifiers with no value are skipped
//    @Test(dependsOnMethods = { "testCreateBasicResource" })
//    public void testSearchDate_Identifier_NoValue() throws Exception {
//        assertSearchReturnsSavedResource("Identifier-noValue", "http://example.org/identifiersystem|");
//    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_Identifier_missing() throws Exception {
        assertSearchReturnsSavedResource("Identifier:missing", "false");
        assertSearchDoesntReturnSavedResource("Identifier:missing", "true");
        
        assertSearchReturnsSavedResource("missing-Identifier:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Identifier:missing", "false");
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_ContactPoint() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint", "(555) 675 5745");
        assertSearchReturnsSavedResource("ContactPoint", "phone|(555) 675 5745");
//        assertSearchReturnsSavedResource("ContactPoint", "phone|");
        
        // ContactPoint should search on use instead of system
CODE_REMOVED
//        assertSearchReturnsSavedResource("ContactPoint", "home|(555) 675 5745");
//        assertSearchReturnsSavedResource("ContactPoint", "home|");
        
        // This shouldn't return any results because the ContactPoint has a system
//        assertSearchDoesntReturnSavedResource("ContactPoint", "|(555) 675 5745");
    }
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchToken_ContactPoint_URI() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-uri", "tel:+15556755745");
        assertSearchReturnsSavedResource("ContactPoint-uri", "phone|tel:+15556755745");
//        assertSearchReturnsSavedResource("ContactPoint-uri", "phone|");
        
        // This shouldn't return any results because the ContactPoint has a system
//        assertSearchDoesntReturnSavedResource("ContactPoint-uri", "|tel:+15556755745");
    }
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_ContactPoint_HomeFax() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-homeFax", "(555) 675 5745");
        assertSearchReturnsSavedResource("ContactPoint-homeFax", "fax|(555) 675 5745");
//        assertSearchReturnsSavedResource("ContactPoint-homeFax", "fax|");
        
        // ContactPoint should search on use instead of system
CODE_REMOVED
//        assertSearchReturnsSavedResource("ContactPoint-homeFax", "home|(555) 675 5745");
//        assertSearchReturnsSavedResource("ContactPoint-homeFax", "home|");
        
        // This shouldn't return any results because the ContactPoint has a system
//        assertSearchDoesntReturnSavedResource("ContactPoint-homeFax", "|(555) 675 5745");
    }
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_ContactPoint_NoUse() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noUse", "test@example.com");
        // ContactPoint should search on use instead of system
CODE_REMOVED
//        assertSearchReturnsSavedResource("ContactPoint-noUse", "|test@example.com");
    }
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_ContactPoint_NoSystem() throws Exception {
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "test@example.com");
        assertSearchReturnsSavedResource("ContactPoint-noSystem", "|test@example.com");
    }
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchDate_ContactPoint_NoValue() throws Exception {
//        assertSearchReturnsSavedResource("ContactPoint-noValue", "phone|");
        
        // ContactPoint should search on use instead of system
CODE_REMOVED
//        assertSearchReturnsSavedResource("ContactPoint-noValue", "home|");
    }
}
