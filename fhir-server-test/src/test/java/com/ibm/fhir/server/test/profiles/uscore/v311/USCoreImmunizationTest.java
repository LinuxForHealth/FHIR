/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.ig.us.core.tool.USCoreExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Immunization;
import com.ibm.fhir.server.test.profiles.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Immunization.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-Immunization.html
 */
public class USCoreImmunizationTest extends ProfilesTestBaseV2 {
    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String immunizationId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-immunization|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Immunization', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadImmunization1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteImmunization1();
        }
    }

    public void loadImmunization1() throws Exception {
        String resource = "Immunization-imm-1.json";
        WebTarget target = getWebTarget();

        Immunization immunization = USCoreExamplesUtil.readLocalJSONResource("311", resource);

        Entity<Immunization> entity = Entity.entity(immunization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Immunization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Immunization/12354 (first actual test, but simple)
        immunizationId1 = getLocationLogicalId(response);
        response = target.path("Immunization/" + immunizationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteImmunization1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Immunization/" + immunizationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all immunizations for a patient using the patient search parameter:
        // GET [base]/Immunization?patient=[reference]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(Immunization.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, immunizationId1);
        }
    }

    @Test
    public void testSearchForPatientAndDate() throws Exception {
        // SHOULD support searching using the combination of the patient and lifecycle-status search parameters:
        // GET [base]/Immunization?patient=[reference]&lifecycle-status={system|}[code]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("date", "ge2016");
            parameters.searchParam("date", "lt2017");
            FHIRResponse response = client.search(Immunization.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, immunizationId1);
        }
    }

    @Test
    public void testSearchForPatientAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // GET [base]/Immunization?patient=[reference]&status={system|}[code]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("status", "completed");
            FHIRResponse response = client.search(Immunization.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, immunizationId1);
        }
    }
}