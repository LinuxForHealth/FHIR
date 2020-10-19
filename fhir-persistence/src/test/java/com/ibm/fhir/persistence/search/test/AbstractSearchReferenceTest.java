/*
 * (C) Copyright IBM Corp. 2018, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * @see https://hl7.org/fhir/r4/search.html#reference
 */
public abstract class AbstractSearchReferenceTest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicReference.json");
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("reference");
        
        // Need to set reference before storing the resource. The server-url
        // is now used to determine if an absolute reference is local (can be served
        // from this FHIR server).
        createReference();
    }

    @BeforeClass
    public void createReference() throws FHIRException {
        String originalRequestUri = "https://example.com/Patient/123";
        FHIRRequestContext context = FHIRRequestContext.get();
        context.setOriginalRequestUri(originalRequestUri);
    }

    @Test
    public void testSearchReference_Reference_relative() throws Exception {
        // Reference by id really only works when the system knows which resource type(s)
        // can be referenced from a given element.
        assertSearchReturnsSavedResource("Reference-relative", "Patient/123");
        assertSearchReturnsSavedResource("Reference-relative", "123");

        assertSearchReturnsSavedResource("Reference-relative", "https://example.com/Patient/123");

        assertSearchReturnsSavedResource("Reference-relative:Patient", "123");
        assertSearchDoesntReturnSavedResource("Reference-relative:Basic", "123");
    }

    @Test
    public void testSearchReference_Reference_relative_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Reference-relative", "Patient/123");
        /*
         * Currently, as documented in our conformance doc, we do not support modifiers on chained parameters.
         * See https://ibm.github.io/FHIR/Conformance#search-modifiers and
         * refer to https://github.com/IBM/FHIR/issues/473 to track the issue.
         */
        // assertSearchReturnsComposition("subject:Basic.Reference-relative:Patient", "123");
        // assertSearchReturnsComposition("subject:Basic.Reference-relative:Basic", "123");
    }

    @Test
    public void testSearchReference_Reference_relative_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("Reference-relative", Collections.singletonList("Patient/123"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchReference_Reference_absolute() throws Exception {
        assertSearchReturnsSavedResource("Reference-absolute", "https://example.com/Patient/123");
        assertSearchReturnsSavedResource("Reference-absolute", "Patient/123");
        assertSearchReturnsSavedResource("Reference-absolute", "123");

        assertSearchReturnsSavedResource("Reference-absolute:Patient", "123");
    }

    @Test
    public void testSearchReference_Reference_missing() throws Exception {
        assertSearchReturnsSavedResource("Reference:missing", "false");
        assertSearchDoesntReturnSavedResource("Reference:missing", "true");

        assertSearchReturnsSavedResource("missing-Reference:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-Reference:missing", "false");
    }

    @Test
    public void testSearchReference_uri() throws Exception {
        assertSearchReturnsSavedResource("uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
    }

    @Test
    public void testSearchReference_uri_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
    }

    @Test
    public void testSearchReference_uri_missing() throws Exception {
        assertSearchReturnsSavedResource("uri:missing", "false");
        assertSearchDoesntReturnSavedResource("uri:missing", "true");

        assertSearchReturnsSavedResource("missing-uri:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-uri:missing", "false");
    }

    @Test
    public void testSearchReference_Reference_identifier() throws Exception {
        // should the server index the Reference.identifier field if there is no valued reference?
    }

    @Test
    public void testSearchReference_Reference_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.Reference:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.Reference:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-Reference:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-Reference:missing", "false");
    }

    @Test
    public void testSearchReference_uri_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.uri:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.uri:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-uri:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-uri:missing", "false");
    }
}
