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
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Procedure;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Procedure.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-procedure.html
 */
public class USCoreProcedureTest extends ProfilesTestBaseV2 {

    private String procedureId1 = null;
    private String procedureId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-procedure|3.1.1");
    }

    @Override
    public void loadResources() throws Exception {
        loadProcedure1();
        loadProcedure2();
    }

    public void loadProcedure1() throws Exception {
        String resource = "Procedure-rehab.json";
        String cls = "Procedure";
        procedureId1 = buildAndAssertOnResourceForUsCore(cls, "311", resource);
    }

    public void loadProcedure2() throws Exception {
        String resource = "Procedure-defib-implant.json";
        String cls = "Procedure";
        procedureId2 = buildAndAssertOnResourceForUsCore(cls, "311", resource);
    }

    @Test
    public void testSearchByPatient() throws Exception {
        // SHALL support searching for all procedures for a patient using the patient search parameter:
        // GET [base]/Procedure?patient=[reference]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
        assertContainsIds(bundle, procedureId2);
    }

    @Test
    public void testSearchByPatientAndDate() throws Exception {
        // SHALL support searching using the combination of the patient and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET [base]/Procedure?patient=[reference]&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("date", "2002,2019");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
        assertContainsIds(bundle, procedureId2);
    }

    @Test
    public void testSearchByPatientAndDateAndCodeSct() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/Procedure?patient=[reference]&code={system|}[code]{,{system|}[code],...}&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("date", "2002,2019");
        parameters.searchParam("code", "http://snomed.info/sct|35637008");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
    }

    @Test
    public void testSearchByPatientAndDateAndCodeIcd10() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/Procedure?patient=[reference]&code={system|}[code]{,{system|}[code],...}&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("date", "2002,2019");
        parameters.searchParam("code", "http://www.cms.gov/Medicare/Coding/ICD10|HZ30ZZZ");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
    }

    @Test
    public void testSearchByPatientAndDateAndCodeWithoutSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/Procedure?patient=[reference]&code={system|}[code]{,{system|}[code],...}&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("date", "2002,2019");
        parameters.searchParam("code", "HZ30ZZZ");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
    }

    @Test
    public void testSearchByPatientAndDateAndCodeWithoutSystemAndWithSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and code and date search parameters:
        // including optional support for composite OR search on code (e.g.code={system|}[code],{system|}[code],...)
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET
        // [base]/Procedure?patient=[reference]&code={system|}[code]{,{system|}[code],...}&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("date", "2002,2019");
        parameters.searchParam("code", "HZ30ZZZ,http://www.ama-assn.org/go/cpt|33249");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
        assertContainsIds(bundle, procedureId2);
    }

    @Test
    public void testSearchByPatientAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET [base]/Procedure?patient=[reference]&status={system|}[code]{,{system|}[code],...}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("status", "completed");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Procedure.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, procedureId1);
        assertContainsIds(bundle, procedureId2);
    }
}