/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.ig.us.core.tool.USCoreExamplesUtil;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.DiagnosticReport;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with DiagnosticReport Lab.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-diagnosticreport-note.html
 */
public class USCoreDiagnosticReportNoteTest extends ProfilesTestBaseV2 {

    private String diagnosticReportId1 = null;
    private String diagnosticReportId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-note|3.1.1");
    }

    @Override
    public void loadResources() throws Exception {
        loadDiagnosticReport1();
        loadDiagnosticReport2();
    }

    public void loadDiagnosticReport1() throws Exception {
        String resource = "DiagnosticReport-cardiology-report.json";
        DiagnosticReport diagnosticReport = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        diagnosticReportId1 = createResourceAndReturnTheLogicalId("DiagnosticReport", diagnosticReport);
    }

    public void loadDiagnosticReport2() throws Exception {
        String resource = "DiagnosticReport-chest-xray-report.json";
        DiagnosticReport diagnosticReport = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        diagnosticReportId2 = createResourceAndReturnTheLogicalId("DiagnosticReport", diagnosticReport);
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all diagnosticreports for a patient using the patient search parameter:
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
    }

    @Test
    public void testSearchForPatientAndCategory() throws Exception {
        // SHALL support searching using the combination of the patient and category search parameters:
        // GET [base]/DiagnosticReport?patient=[reference]&category={system|}[code]

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "LP29708-2");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
    }

    @Test
    public void testSearchForPatientAndCategoryAndSystem() throws Exception {
        // SHALL support searching using the combination of the patient and category search parameters:
        // GET [base]/DiagnosticReport?patient=[reference]&category={system|}[code]

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://loinc.org|LP29708-2");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
    }

    @Test
    public void testSearchForPatientAndCode() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/DiagnosticReport?patient=[reference]&code={system|}[code]{,{system|}[code],...}

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("code", "http://loinc.org|45033-8");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
    }

    @Test
    public void testSearchForPatientAndCodes() throws Exception {
        // SHALL support searching using the combination of the patient and code search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // GET [base]/DiagnosticReport?patient=[reference]&code={system|}[code]{,{system|}[code],...}

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("code", "http://loinc.org|45033-8,http://loinc.org|30746-2");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);
    }

    @Test
    public void testSearchForPatientAndCategoryAndDate() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/DiagnosticReport?patient=[reference]&category={system|}[code]&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "LP29708-2");
        parameters.searchParam("date", "2011-01-01T21Z");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);
    }

    @Test
    public void testSearchForPatientAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET [base]/DiagnosticReport?patient=[reference]&status={system|}[code]{,{system|}[code],...}

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("status", "final");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertContainsIds(bundle, diagnosticReportId2);

    }

    @Test
    public void testSearchForPatientAndCodeAndDate() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/DiagnosticReport?patient=[reference]&code={system|}[code]{,{system|}[code],...}&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("code", "http://loinc.org|45033-8,http://loinc.org|30746-2");
        parameters.searchParam("date", "2011-01-01T21Z");
        FHIRResponse response = client.search(DiagnosticReport.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, diagnosticReportId1);
        assertDoesNotContainsIds(bundle, diagnosticReportId2);

    }
}