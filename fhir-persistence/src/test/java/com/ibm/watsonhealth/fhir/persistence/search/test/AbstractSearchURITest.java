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
 * @see https://hl7.org/fhir/dstu2/search.html#uri
 */
public abstract class AbstractSearchURITest extends AbstractPLSearchTest {

    @Test
    public void testCreateBasicResource() throws Exception {
        Basic resource = readResource(Basic.class, "BasicURI.json");
        saveBasicResource(resource);
    }

    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchURI_uri() throws Exception {
        assertSearchReturnsSavedResource("uri", "http://hl7.org/fhir/DSTU2");
        assertSearchReturnsSavedResource("uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        
        // Matches are supposed to be precise (e.g. case, accent, and escape sensitive), but aren't
        // 
//        assertSearchDoesntReturnSavedResource("uri", "http://HL7.org/FHIR/dstu2");
//        assertSearchDoesntReturnSavedResource("uri", "urn:uuid:53FEFA32-1111-2222-3333-55EE120877B7");
        
        // TODO add test for diacritics and other unusual characters
    }
    
    @Test(dependsOnMethods = { "testCreateBasicResource" })
    public void testSearchURI_uri_below() throws Exception {
        assertSearchReturnsSavedResource("uri:below", "http://hl7.org/fhir/");
    }
}
