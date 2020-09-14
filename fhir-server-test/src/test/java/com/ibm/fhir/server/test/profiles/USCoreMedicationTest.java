/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

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
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the US Core 3.1.1 Profile with Medication.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-medication.html
 */
public class USCoreMedicationTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String medicationId1 = null;
    private String medicationId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-medication|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Medication', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadMedication1();
            loadMedication2();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteMedication1();
            deleteMedication2();
        }
    }

    public void loadMedication1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Medication-uscore-med1.json";
        WebTarget target = getWebTarget();

        Medication goal = TestUtil.readExampleResource(resource);

        Entity<Medication> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Medication").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Medication/12354 (first actual test, but simple)
        medicationId1 = getLocationLogicalId(response);
        response = target.path("Medication/" + medicationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteMedication1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Medication/" + medicationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
    
    public void loadMedication2() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Medication-uscore-med2.json";
        WebTarget target = getWebTarget();

        Medication goal = TestUtil.readExampleResource(resource);

        Entity<Medication> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Medication").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Medication/12354 (first actual test, but simple)
        medicationId2 = getLocationLogicalId(response);
        response = target.path("Medication/" + medicationId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteMedication2() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Medication/" + medicationId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForCode() throws Exception {
        // There are no specific search terms for Medication, using the base spec.
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("code", "http://www.nlm.nih.gov/research/umls/rxnorm|206765,http://www.nlm.nih.gov/research/umls/rxnorm|582620");
            FHIRResponse response = client.search(Medication.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, medicationId1);
            assertContainsIds(bundle, medicationId2);
        }
    }
}