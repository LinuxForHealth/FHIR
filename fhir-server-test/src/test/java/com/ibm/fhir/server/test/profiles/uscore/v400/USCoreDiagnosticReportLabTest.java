/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles.uscore.v400;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.ig.us.core.tool.USCoreExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.DiagnosticReport;
import com.ibm.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with DiagnosticReport Lab.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-diagnosticreport-lab.html
 */
public class USCoreDiagnosticReportLabTest extends ProfilesTestBaseV2 {

    private String diagnosticReportId1 = null;
    private String diagnosticReportId2 = null;
    private String diagnosticReportId3 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-lab|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        loadDiagnosticReport1();
        loadDiagnosticReport2();
        loadDiagnosticReport3();
    }

    public void loadDiagnosticReport1() throws Exception {
        String resource = "DiagnosticReport-urinalysis.json";
        DiagnosticReport diagnosticReport = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        diagnosticReportId1 = createResourceAndReturnTheLogicalId("DiagnosticReport", diagnosticReport);
    }

    public void loadDiagnosticReport2() throws Exception {
        String resource = "DiagnosticReport-metabolic-panel.json";
        DiagnosticReport diagnosticReport = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        diagnosticReportId2 = createResourceAndReturnTheLogicalId("DiagnosticReport", diagnosticReport);
    }

    public void loadDiagnosticReport3() throws Exception {
        String resource = "DiagnosticReport-cbc.json";
        DiagnosticReport diagnosticReport = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        diagnosticReportId3 = createResourceAndReturnTheLogicalId("DiagnosticReport", diagnosticReport);
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all diagnostic reports for a patient using the patient search parameter:
        // GET [base]/DiagnosticReport?patient=[reference]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);
        assertContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndCategory() throws Exception {
        // SHALL support searching using the combination of the patient and category search parameters:
        // GET [base]/DiagnosticReport?patient=[reference]&type={[system]}|[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/v2-0074|LAB");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);
        assertContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndCode() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/DiagnosticReport?patient=[reference]&code={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("code", "http://loinc.org|24356-8");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
        assertDoesNotContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndCodeWithoutSystem() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/DiagnosticReport?patient=[reference]&code={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("code", "http://loinc.org|24356-8");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
        assertDoesNotContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndMultipleCodes() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/DiagnosticReport?patient=[reference]&code={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("code", "http://loinc.org|24356-8,http://loinc.org|24323-8");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);
        assertDoesNotContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndCategoryAndDate() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/DiagnosticReport?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/v2-0074|LAB&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/v2-0074|LAB");
        parameters.searchParam("date", "2005-07-05");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
        assertContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndCategoryAndDateLeGe() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/DiagnosticReport?patient=[reference]&category=http://terminology.hl7.org/CodeSystem/v2-0074|LAB&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/v2-0074|LAB");
        parameters.searchParam("date", "ge2005-01");
        parameters.searchParam("date", "lt2005-07-05");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertDoesNotContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);
        assertDoesNotContainsIds(bundle, diagnosticReportId3);
    }

    @Test
    public void testSearchForPatientAndCategoryAndDateAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // GET [base]/DiagnosticReport?patient=[reference]&status={system|}[code]{,{system|}[code],...}
        // Note: implied system, and we only pass in the token.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/v2-0074|LAB");
        parameters.searchParam("date", "ge2005-01");
        parameters.searchParam("status", "final");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);
        assertContainsIds(bundle, diagnosticReportId3);
    }
}