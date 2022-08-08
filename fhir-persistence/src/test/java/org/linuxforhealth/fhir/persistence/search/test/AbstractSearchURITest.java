/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.search.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.test.TestUtil;

/**
 * <a href="https://hl7.org/fhir/r4/search.html#uri">FHIR Specification: Search - uri</a>
 *
 */
public abstract class AbstractSearchURITest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicURI.json");
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("uri");
    }

    @Test
    public void testSearchURI_uri() throws Exception {
        assertSearchReturnsSavedResource("uri", "http://hl7.org/fhir/DSTU2");
        assertSearchReturnsSavedResource("uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");

        // Matches are supposed to be precise (e.g. case, accent, and escape sensitive), but aren't
        assertSearchDoesntReturnSavedResource("uri", "http://hl7.org/fhir/DSTU");
        assertSearchDoesntReturnSavedResource("uri", "xttp://hl7.org/fhir/DSTU2");
        assertSearchDoesntReturnSavedResource("uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b");

        // case tests
        assertSearchDoesntReturnSavedResource("uri", "http://HL7.org/FHIR/dstu2");
        assertSearchDoesntReturnSavedResource("uri", "urn:uuid:53FEFA32-1111-2222-3333-55EE120877B7");

        // accent tests
        assertSearchReturnsSavedResource("uri", "http://åé");
        assertSearchDoesntReturnSavedResource("uri", "http://ae");
        assertSearchDoesntReturnSavedResource("uri", "http://HL7.org/FHIR/dstü2");
    }

    @Test
    public void testSearchURI_uri_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.uri", "http://hl7.org/fhir/DSTU2");
        assertSearchReturnsComposition("subject:Basic.uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");

        assertSearchDoesntReturnComposition("subject:Basic.uri", "http://HL7.org/FHIR/dstu2");
        assertSearchDoesntReturnComposition("subject:Basic.uri", "http://hl7.org/fhir/DSTU");
        assertSearchDoesntReturnComposition("subject:Basic.uri", "urn:uuid:53FEFA32-1111-2222-3333-55EE120877B7");

        // TODO add test for diacritics and other unusual characters
    }

    @Test
    public void testSearchURI_uri_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("uri", Collections.singletonList("http://hl7.org/fhir/DSTU2"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }


    @Test
    public void testSearchURI_uri_below() throws Exception {
        assertSearchDoesntReturnSavedResource("uri:below", "http://hl7.org/fhir/");
        assertSearchReturnsSavedResource("uri:below", "http://hl7.org/fhir");
    }

    @Test
    public void testSearchURI_uri_above() throws Exception {
        assertSearchDoesntReturnSavedResource("uri:above", "FHIR/dstu2");
        assertSearchDoesntReturnSavedResource("uri:above", "http://hl7.org/fhir/");
        assertSearchReturnsSavedResource("uri:above", "http://hl7.org/fhir/DSTU2/Fred");
        assertSearchReturnsSavedResource("uri:above", "http://hl7.org/fhir/DSTU2/Fred/Wilma");
    }

    @Test
    public void testSearchURI_uri_missing() throws Exception {
        assertSearchReturnsSavedResource("uri:missing", "false");
        assertSearchDoesntReturnSavedResource("uri:missing", "true");

        assertSearchReturnsSavedResource("missing-uri:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-uri:missing", "false");
    }

    @Test
    public void testSearchURI_uri_missing_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("uri:missing", Collections.singletonList("false"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
        queryParms.clear();
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("uri:missing", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertFalse(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchURI_uri_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.uri:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.uri:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-uri:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-uri:missing", "false");
    }

    @Test
    public void testSearchURI_canonical() throws Exception {
        assertSearchReturnsSavedResource("canonical", "http://hl7.org/fhir/DSTU2");
        assertSearchReturnsSavedResource("canonical", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");

        // Matches are supposed to be precise (e.g. case, accent, and escape sensitive), but aren't
        assertSearchDoesntReturnSavedResource("canonical", "http://hl7.org/fhir/DSTU");
        assertSearchDoesntReturnSavedResource("canonical", "xttp://hl7.org/fhir/DSTU2");
        assertSearchDoesntReturnSavedResource("canonical", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b");

        // case tests
        assertSearchDoesntReturnSavedResource("canonical", "http://HL7.org/FHIR/dstu2");
        assertSearchDoesntReturnSavedResource("canonical", "urn:uuid:53FEFA32-1111-2222-3333-55EE120877B7");

        // accent tests
        assertSearchReturnsSavedResource("canonical", "http://åé");
        assertSearchDoesntReturnSavedResource("canonical", "http://ae");
        assertSearchDoesntReturnSavedResource("canonical", "http://HL7.org/FHIR/dstü2");
    }

    @Test
    public void testSearchURI_canonical_chained() throws Exception {
        assertSearchReturnsComposition("subject:Basic.canonical", "http://hl7.org/fhir/DSTU2");
        assertSearchReturnsComposition("subject:Basic.canonical", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");

        assertSearchDoesntReturnComposition("subject:Basic.canonical", "http://HL7.org/FHIR/dstu2");
        assertSearchDoesntReturnComposition("subject:Basic.canonical", "http://hl7.org/fhir/DSTU");
        assertSearchDoesntReturnComposition("subject:Basic.canonical", "urn:uuid:53FEFA32-1111-2222-3333-55EE120877B7");
    }

    @Test
    public void testSearchURI_canonical_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("canonical", Collections.singletonList("http://hl7.org/fhir/DSTU2"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
    }


    @Test
    public void testSearchURI_canonical_below() throws Exception {
        assertSearchDoesntReturnSavedResource("canonical:below", "http://hl7.org/fhir/");
        assertSearchReturnsSavedResource("canonical:below", "http://hl7.org/fhir");
    }

    @Test
    public void testSearchURI_canonical_above() throws Exception {
        assertSearchDoesntReturnSavedResource("canonical:above", "FHIR/dstu2");
        assertSearchDoesntReturnSavedResource("canonical:above", "http://hl7.org/fhir/");
        assertSearchReturnsSavedResource("canonical:above", "http://hl7.org/fhir/DSTU2/Fred");
        assertSearchReturnsSavedResource("canonical:above", "http://hl7.org/fhir/DSTU2/Fred/Wilma");
    }

    @Test
    public void testSearchURI_canonical_missing() throws Exception {
        assertSearchReturnsSavedResource("canonical:missing", "false");
        assertSearchDoesntReturnSavedResource("canonical:missing", "true");

        assertSearchReturnsSavedResource("missing-canonical:missing", "true");
        assertSearchDoesntReturnSavedResource("missing-canonical:missing", "false");
    }

    @Test
    public void testSearchURI_canonical_missing_revinclude() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("canonical:missing", Collections.singletonList("false"));
        assertTrue(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertTrue(searchReturnsResource(Basic.class, queryParms, composition));
        queryParms.clear();
        queryParms.put("_revinclude", Collections.singletonList("Composition:subject"));
        queryParms.put("canonical:missing", Collections.singletonList("true"));
        assertFalse(searchReturnsResource(Basic.class, queryParms, savedResource));
        assertFalse(searchReturnsResource(Basic.class, queryParms, composition));
    }

    @Test
    public void testSearchURI_canonical_chained_missing() throws Exception {
        assertSearchReturnsComposition("subject:Basic.canonical:missing", "false");
        assertSearchDoesntReturnComposition("subject:Basic.canonical:missing", "true");

        assertSearchReturnsComposition("subject:Basic.missing-canonical:missing", "true");
        assertSearchDoesntReturnComposition("subject:Basic.missing-canonical:missing", "false");
    }
}
