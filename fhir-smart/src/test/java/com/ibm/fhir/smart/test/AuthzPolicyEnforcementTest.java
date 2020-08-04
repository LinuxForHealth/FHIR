/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.smart.AuthzPolicyEnforcementPersistenceInterceptor;

public class AuthzPolicyEnforcementTest {
    AuthzPolicyEnforcementPersistenceInterceptor interceptor;
    Patient patient;
    Map<String, Object> properties;

    @BeforeClass
    public void setup() throws Exception {
        FHIRRequestContext requestContext = new FHIRRequestContext();
        requestContext.setHttpHeaders(buildRequestHeaders());
        FHIRRequestContext.set(requestContext);

        interceptor = new AuthzPolicyEnforcementPersistenceInterceptor();

        patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        patient = patient.toBuilder()
                .id("11111111-1111-1111-1111-111111111111")
                .build();

        properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
    }

    @Test(expectedExceptions = FHIRPersistenceInterceptorException.class)
    public void testCreate() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        interceptor.beforeCreate(event);
    }

    @Test(expectedExceptions = FHIRPersistenceInterceptorException.class)
    public void testUpdate() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        interceptor.beforeUpdate(event);
    }

    @Test(expectedExceptions = FHIRPersistenceInterceptorException.class)
    public void testDelete() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent();
        event.setPrevFhirResource(patient);
        interceptor.beforeDelete(event);
    }

    @Test
    public void testRead() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        interceptor.afterRead(event);
    }

    @Test
    public void testVRead() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        interceptor.afterVread(event);
    }

    @Test
    public void testHistory() throws Exception {
        Bundle historyBundle = Bundle.builder()
                .type(BundleType.HISTORY)
                .entry(Bundle.Entry.builder().resource(patient).build())
                .build();
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
        interceptor.afterHistory(event);
    }

    @Test
    public void testSearch() throws Exception {
        Bundle searchBundle = Bundle.builder()
                .type(BundleType.SEARCHSET)
                .entry(Bundle.Entry.builder().resource(patient).build())
                .build();
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
        interceptor.afterSearch(event);
    }

    private Map<String, List<String>> buildRequestHeaders() {
        // Uses LinkedHashMap just to preserve the order.
        Map<String, List<String>> requestHeaders = new LinkedHashMap<String, List<String>>();

        List<String> authHeader = Collections.singletonList("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ"
                + "3emdOc0FwWTZGR01KNGJQc0gtZWg5ZTQxNlR4N1lFSXNqU2wyTWI2bURFIn0.eyJleHAiOjE1OTY0OTQ1NDEsImlhdCI6MTU5NjQ"
                + "5NDI0MSwiYXV0aF90aW1lIjoxNTk2NDkzNjMzLCJqdGkiOiI0ZjA0NjVkZC04OGQ2LTRlNjYtYjc4YS1hMjNmZTA0NmE1NjQiLCJ"
                + "pc3MiOiJodHRwczovL2xvY2FsaG9zdDo4NDQzL2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6Imh0dHBzOi8vbG9jYWxob3N0Ojk"
                + "0NDMvZmhpci1zZXJ2ZXIvYXBpL3Y0Iiwic3ViIjoiMzE3Y2Y2M2UtMmEzNi00ZWEzLWEzMjQtOGRkOGY1Mzk4MTQwIiwidHlwIjo"
                + "iQmVhcmVyIiwiYXpwIjoiaW5mZXJubyIsInNlc3Npb25fc3RhdGUiOiJmODNlN2U3Yi1kMmJkLTQ1YTMtYTZjNS0zNzlhOTk2MmQ"
                + "2YmUiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDU2NyJdLCJyZWFsbV9hY2Nlc3MiOns"
                + "icm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImZoaXJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3M"
                + "iOnsiZmhpciI6eyJyb2xlcyI6WyJzZXJ2ZXJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWF"
                + "uYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUgbGF1bmNoL3BhdGllbnQ"
                + "gcGF0aWVudC8qLnJlYWQiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInBhdGllbnRfaWQiOiIxMTExMTExMS0xMTExLTExMTEtMTE"
                + "xMS0xMTExMTExMTExMTEiLCJuYW1lIjoiRkhJUiBVc2VyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiZmhpcnVzZXIiLCJnaXZlbl9"
                + "uYW1lIjoiRkhJUiIsImZhbWlseV9uYW1lIjoiVXNlciIsImVtYWlsIjoidXNlckB0ZXN0LmNvbSJ9.YxXidPtJ8gFLdID6QytAtQ"
                + "FGU6igtGY4CQCra3DJBwlC5eZqvUi7wuWL8gqPyB2poWBXrkVJmyThTg4rxU02M5q0Cjeam2iUO-L7OA1R3LP6F0OrWrg3l6BPHk"
                + "yMW1MPMpA5rXlncwvoLtpW_hTQTEs4yrXg2f-hxNfx_DkpD4nL3hVeSv0erbe699PX7p4DPkir-DAUIXFi5vGmlhDyNAMa--HQhY"
                + "pFNXtc_OBAf7uWfgV9nzgZ7zchncnpmkNscPA1rlpmhTrY1LPFq1FWanEtALClX0T1Oa8hbby7gB7kiKYF1lEKINT12V_quqX-l4"
                + "hOXL8ZYActIM82YxP4bQ");

        requestHeaders.put("Authorization", authHeader);

        return requestHeaders;
    }
}
