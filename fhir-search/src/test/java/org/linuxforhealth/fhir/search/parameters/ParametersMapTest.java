/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.search.parameters;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Markdown;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.model.type.code.ResourceTypeCode;
import org.linuxforhealth.fhir.model.type.code.SearchParamType;

/**
 * ParametersMap tests
 */
public class ParametersMapTest {
    SearchParameter sp_a1 = SearchParameter.builder()
            .url(Uri.of("http://example.com/fhir/test/sp_a1"))
            .status(PublicationStatus.ACTIVE)
            .name(string("a"))
            .description(Markdown.of("First param with code 'a'"))
            .base(ResourceTypeCode.RESOURCE)
            .type(SearchParamType.STRING)
            .code(Code.of("a"))
            .expression(string("extension.value as String"))
            .build();
    SearchParameter sp_a2 = SearchParameter.builder()
            .url(Uri.of("http://example.com/fhir/test/sp_a2"))
            .status(PublicationStatus.ACTIVE)
            .name(string("a"))
            .description(Markdown.of("Second param with code 'a'"))
            .base(ResourceTypeCode.RESOURCE)
            .type(SearchParamType.STRING)
            .code(Code.of("a"))
            .expression(string("extension.value as String"))
            .build();
    SearchParameter sp_b = SearchParameter.builder()
            .url(Uri.of("http://example.com/fhir/test/sp_b"))
            .status(PublicationStatus.ACTIVE)
            .name(string("b"))
            .base(ResourceTypeCode.RESOURCE)
            .type(SearchParamType.STRING)
            .description(Markdown.of("Param with code 'b'"))
            .code(Code.of("b"))
            .expression(string("extension.value as String"))
            .build();
    SearchParameter sp_b1 = SearchParameter.builder()
            .url(Uri.of("http://example.com/fhir/test/sp_b"))
            .version("1")
            .status(PublicationStatus.ACTIVE)
            .name(string("b"))
            .base(ResourceTypeCode.RESOURCE)
            .type(SearchParamType.STRING)
            .description(Markdown.of("Param with code 'b'"))
            .code(Code.of("b"))
            .expression(string("extension.value as String"))
            .build();
    SearchParameter sp_b2 = SearchParameter.builder()
            .url(Uri.of("http://example.com/fhir/test/sp_b"))
            .version("2")
            .status(PublicationStatus.ACTIVE)
            .name(string("b"))
            .base(ResourceTypeCode.RESOURCE)
            .type(SearchParamType.STRING)
            .description(Markdown.of("Param with code 'b'"))
            .code(Code.of("b"))
            .expression(string("extension.value as String"))
            .build();

    /**
     * Add various versions of search parameters 'a' and 'b' and check the results
     */
    @Test
    public void testParametersMap() {
        ParametersMap pm = new ParametersMap();
        pm.insert("a", sp_a1);
        pm.insert("a", sp_a2);
        pm.insert("b", sp_b);
        pm.insert("b", sp_b1);
        pm.insert("b", sp_b2);

        assertEquals(pm.lookupByCode("a"), sp_a2);
        assertEquals(pm.lookupByCode("b"), sp_b2);

        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_a1"), sp_a1);
        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_a2"), sp_a2);
        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_b"), sp_b2);
        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_b|1"), sp_b1);
        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_b|2"), sp_b2);

        assertTrue(pm.codeEntries().size() == 2);
        assertTrue(pm.canonicalEntries().size() == 5); // versionless ones for a1, a2, and b2; versioned ones for b1 and b2
    }

    /**
     * Adding the same parameter under two different codes should be supported
     */
    @Test
    public void testParametersMap_duplicateUrl() {
        ParametersMap pm = new ParametersMap();
        pm.insert("b", sp_b);
        pm.insert("b2", sp_b);

        assertEquals(pm.lookupByCode("b"), sp_b);
        assertEquals(pm.lookupByCode("b2"), sp_b);

        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_b"), sp_b);

        assertTrue(pm.codeEntries().size() == 2);
        assertTrue(pm.canonicalEntries().size() == 1);
    }

    /**
     * Adding the same parameter twice should be the same as adding it once
     */
    @Test
    public void testParametersMap_duplicate() {
        ParametersMap pm = new ParametersMap();
        pm.insert("b", sp_b);
        pm.insert("b", sp_b);

        assertEquals(pm.lookupByCode("b"), sp_b);

        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_b"), sp_b);

        assertTrue(pm.codeEntries().size() == 1);
        assertTrue(pm.canonicalEntries().size() == 1);
    }

    /**
     * Add various versions of compartment inclusion parameters 'a' and 'b' and check the results
     */
    @Test
    public void testParametersMap_inclusionParams() {
        ParametersMap pm = new ParametersMap();
        pm.insertInclusionParam("a", sp_a1);
        pm.insertInclusionParam("a", sp_a2);
        pm.insertInclusionParam("b", sp_b);
        pm.insertInclusionParam("b", sp_b1);
        pm.insertInclusionParam("b", sp_b2);

        assertEquals(pm.getInclusionParam("a"), sp_a2);
        assertEquals(pm.getInclusionParam("b"), sp_b2);

        assertTrue(pm.inclusionValues().size() == 2);
    }

    /**
     * Adding the same inclusion parameter twice should be the same as adding it once
     */
    @Test
    public void testParametersMap_inclusionParams_duplicate() {
        ParametersMap pm = new ParametersMap();
        pm.insert("b", sp_b);
        pm.insert("b", sp_b);

        assertEquals(pm.lookupByCode("b"), sp_b);

        assertEquals(pm.lookupByCanonical("http://example.com/fhir/test/sp_b"), sp_b);

        assertTrue(pm.codeEntries().size() == 1);
        assertTrue(pm.canonicalEntries().size() == 1);
    }
}
