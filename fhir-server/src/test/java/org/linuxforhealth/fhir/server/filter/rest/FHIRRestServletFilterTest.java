/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.server.filter.rest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.server.exception.FHIRRestServletRequestException;

public class FHIRRestServletFilterTest {

    @Test
    void testCheckFhirVersionParameter_valid() throws Exception {
        Map<String, List<String>> requestHeaders = new HashMap<>();
        requestHeaders.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/fhir+json;fhirVersion=4.0.1"));
        requestHeaders.put(HttpHeaders.ACCEPT, Collections.singletonList("application/fhir+json;fhirVersion=4.0.1"));
        FHIRRequestContext context = new FHIRRequestContext();
        context.setHttpHeaders(requestHeaders);

        FHIRRestServletFilter servletFilter = new FHIRRestServletFilter();
        servletFilter.checkFhirVersionParameter(context);
    }

    @Test
    void testCheckFhirVersionParameter_contentType_invalidFhirVersion() throws Exception {
        Map<String, List<String>> requestHeaders = new HashMap<>();
        requestHeaders.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/fhir+json;fhirVersion=3.0.1"));
        requestHeaders.put(HttpHeaders.ACCEPT, Collections.singletonList("application/fhir+json;fhirVersion=4.0.1"));
        FHIRRequestContext context = new FHIRRequestContext();
        context.setHttpHeaders(requestHeaders);

        FHIRRestServletFilter servletFilter = new FHIRRestServletFilter();
        try {
            servletFilter.checkFhirVersionParameter(context);
            fail();
        } catch (FHIRRestServletRequestException e) {
            assertEquals(e.getHttpStatusCode(), HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @Test
    void testCheckFhirVersionParameter_contentType_multipleFhirVersions() throws Exception {
        Map<String, List<String>> requestHeaders = new HashMap<>();
        requestHeaders.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/fhir+json;fhirVersion=3.0.1"));
        requestHeaders.put(HttpHeaders.ACCEPT, Collections.singletonList("application/fhir+json;fhirVersion=4.0.1"));
        FHIRRequestContext context = new FHIRRequestContext();
        context.setHttpHeaders(requestHeaders);

        FHIRRestServletFilter servletFilter = new FHIRRestServletFilter();
        try {
            servletFilter.checkFhirVersionParameter(context);
            fail();
        } catch (FHIRRestServletRequestException e) {
            assertEquals(e.getHttpStatusCode(), HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @Test
    void testCheckFhirVersionParameter_accept_invalidFhirVersion() throws Exception {
        Map<String, List<String>> requestHeaders = new HashMap<>();
        requestHeaders.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/fhir+json;fhirVersion=4.0.1"));
        requestHeaders.put(HttpHeaders.ACCEPT, Collections.singletonList("application/fhir+json;fhirVersion=3.0.1"));
        FHIRRequestContext context = new FHIRRequestContext();
        context.setHttpHeaders(requestHeaders);

        FHIRRestServletFilter servletFilter = new FHIRRestServletFilter();
        try {
            servletFilter.checkFhirVersionParameter(context);
            fail();
        } catch (FHIRRestServletRequestException e) {
            assertEquals(e.getHttpStatusCode(), HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }
}
