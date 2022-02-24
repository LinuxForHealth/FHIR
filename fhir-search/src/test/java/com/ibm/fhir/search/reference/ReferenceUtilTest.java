/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.reference;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

/**
 * Unit tests for {@link ReferenceUtil}
 */
public class ReferenceUtilTest {

    @Test
    public void testLiteralRelativeShort() {
        final String baseUrl = "http://example.com/";
        final String refString = "Patient/abc1234";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_RELATIVE);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), "abc1234");
    }

    @Test
    public void testLiteralRelativeLong() {
        final String baseUrl = "http://example.com/";
        final String refString = baseUrl + "Patient/abc1234";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_RELATIVE);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), "abc1234");
    }

    @Test
    public void testLiteralRelativeShortVersion() {
        final String baseUrl = "http://example.com/";
        final String refString = "Patient/abc1234/_history/1";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_RELATIVE);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), "abc1234");
        assertNotNull(rv.getVersion());
        assertEquals(rv.getVersion().intValue(), 1);
    }

    @Test
    public void testLiteralRelativeLongVersion() {
        final String baseUrl = "http://example.com/";
        final String refString = baseUrl + "Patient/abc1234/_history/1";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_RELATIVE);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), "abc1234");
        assertNotNull(rv.getVersion());
        assertEquals(rv.getVersion().intValue(), 1);
    }

    @Test
    public void testLiteralAbsolute() {
        final String baseUrl = "http://example.com/";
        final String refString = "http://an.other.system/Patient/abc1234";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .type(Uri.of("Patient"))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_ABSOLUTE);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), refString);
    }

    @Test
    public void testLiteralAbsoluteVersion() {
        final String baseUrl = "http://example.com/";
        final String refString = "http://an.other.system/Patient/abc1234/_history/1";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .type(Uri.of("Patient"))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_ABSOLUTE);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), refString);
    }

    @Test
    public void testLiteralAbsoluteNoType() {
        final String baseUrl = "http://example.com/";
        final String refString = "http://an.other.system/Patient/abc1234";
        Reference ref = Reference.builder()
            .reference(string(refString))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LITERAL_ABSOLUTE);
        assertNull(rv.getTargetResourceType());
        assertEquals(rv.getValue(), refString);
    }

    @Test
    public void testLogical() {
        final String baseUrl = "http://example.com/";
        final String idString = "000-12-3456";
        Reference ref = Reference.builder()
            .identifier(Identifier.builder().value(string(idString)).build())
            .type(Uri.of("Patient"))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.LOGICAL);
        assertEquals(rv.getTargetResourceType(), "Patient");
        assertEquals(rv.getValue(), idString);
    }

    @Test
    public void testDisplayOnly() {
        final String baseUrl = "http://example.com/";
        Reference ref = Reference.builder()
            .display(string("display value"))
            .build();

        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
        assertEquals(rv.getType(), ReferenceType.DISPLAY_ONLY);
        assertNull(rv.getValue());
        assertNull(rv.getTargetResourceType());
        assertNull(rv.getVersion());
    }

    @Test
    public void testGetBaseUrlFromBundle() throws FHIRSearchException {
        final String fullUrl = "https://localhost:9443/fhir-server/api/v4/Observation/17546b5a5a9-872ecfe4-cb5e-4f8c-a381-5b13df536f87";
        Bundle.Entry entry = Bundle.Entry.builder()
            .fullUrl(Uri.of(fullUrl))
            .build();

        final String baseUrl = ReferenceUtil.getBaseUrlFromBundle(entry);
        assertNotNull(baseUrl);
        assertEquals(baseUrl, "https://localhost:9443/fhir-server/api/v4/");
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testGetBaseUrlFromBundleInvalid() throws FHIRSearchException {
        final String fullUrl = "Observation/17546b5a5a9-872ecfe4-cb5e-4f8c-a381-5b13df536";
        Bundle.Entry entry = Bundle.Entry.builder()
            .fullUrl(Uri.of(fullUrl))
            .build();

        // should throw FHIRSearchException
        ReferenceUtil.getBaseUrlFromBundle(entry);
    }

    @Test(expectedExceptions = FHIRSearchException.class)
    public void testGetBaseUrlFromBundleInvalid2() throws FHIRSearchException {
        final String fullUrl = "17546b5a5a9-872ecfe4-cb5e-4f8c-a381-5b13df536";
        Bundle.Entry entry = Bundle.Entry.builder()
            .fullUrl(Uri.of(fullUrl))
            .build();

        // should throw FHIRSearchException
        ReferenceUtil.getBaseUrlFromBundle(entry);
    }

    @Test
    public void testGetServiceBaseUrl() throws FHIRException {
        final String fullUrl = "https://localhost:9443/fhir-server/api/v4/Observation/17546b5a5a9-872ecfe4-cb5e-4f8c-a381-5b13df536f87";
        FHIRRequestContext context = new FHIRRequestContext("default");
        context.setOriginalRequestUri(fullUrl);
        FHIRRequestContext.set(context);
        final String serviceBaseUrl = ReferenceUtil.getServiceBaseUrl();
        assertEquals(serviceBaseUrl, "https://localhost:9443/fhir-server/api/v4/");
    }

    @Test
    public void testGetServiceBaseUrl2() throws FHIRException {
        final String fullUrl = "https://localhost:9443/fhir-server/api/v4/Observation/";
        FHIRRequestContext context = new FHIRRequestContext("default");
        context.setOriginalRequestUri(fullUrl);
        FHIRRequestContext.set(context);
        final String serviceBaseUrl = ReferenceUtil.getServiceBaseUrl();
        assertEquals(serviceBaseUrl, "https://localhost:9443/fhir-server/api/v4/");
    }

    @Test
    public void testGetServiceBaseUrl3() throws FHIRException {
        final String fullUrl = "https://localhost:9443/fhir-server/api/v4/Observation";
        FHIRRequestContext context = new FHIRRequestContext("default");
        context.setOriginalRequestUri(fullUrl);
        FHIRRequestContext.set(context);
        final String serviceBaseUrl = ReferenceUtil.getServiceBaseUrl();
        assertEquals(serviceBaseUrl, "https://localhost:9443/fhir-server/api/v4/");
    }

    @Test
    public void testGetServiceBaseUrl4() throws FHIRException {
        final String fullUrl = "https://localhost:9443/fhir-server/api/v4/";
        FHIRRequestContext context = new FHIRRequestContext("default");
        context.setOriginalRequestUri(fullUrl);
        FHIRRequestContext.set(context);
        final String serviceBaseUrl = ReferenceUtil.getServiceBaseUrl();
        assertEquals(serviceBaseUrl, "https://localhost:9443/fhir-server/api/v4/");
    }

    @Test
    public void testGetServiceBaseUrl5() throws FHIRException {
        final String fullUrl = "https://localhost:9443/fhir-server/api/v4";
        FHIRRequestContext context = new FHIRRequestContext("default");
        context.setOriginalRequestUri(fullUrl);
        FHIRRequestContext.set(context);
        final String serviceBaseUrl = ReferenceUtil.getServiceBaseUrl();

        // Note the special case here...we tack on the final "/" for consistency
        assertEquals(serviceBaseUrl, "https://localhost:9443/fhir-server/api/v4/");
    }

    @Test
    public void testGetServiceBaseUrlNoOriginal() throws FHIRException {
        FHIRRequestContext context = new FHIRRequestContext("default");
        FHIRRequestContext.set(context);
        final String serviceBaseUrl = ReferenceUtil.getServiceBaseUrl();
        assertNull(serviceBaseUrl);
    }
}