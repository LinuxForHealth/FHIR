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
import com.ibm.fhir.model.resource.Goal;
import com.ibm.fhir.server.test.profiles.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Goal.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-goal.html
 */
public class USCoreGoalTest extends ProfilesTestBaseV2 {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String goalId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-goal|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Goal', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadGoal1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteGoal1();
        }
    }

    public void loadGoal1() throws Exception {
        String resource = "Goal-goal-1.json";
        WebTarget target = getWebTarget();
        Goal goal = USCoreExamplesUtil.readLocalJSONResource("311", resource);

        Entity<Goal> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Goal").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Goal/12354 (first actual test, but simple)
        goalId1 = getLocationLogicalId(response);
        response = target.path("Goal/" + goalId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteGoal1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Goal/" + goalId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all goals for a patient using the patient search parameter:
        // GET [base]/Goal?patient=[reference]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(Goal.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, goalId1);
        }
    }

    @Test
    public void testSearchForPatientLifecycleStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and lifecycle-status search parameters:
        // GET [base]/Goal?patient=[reference]&lifecycle-status={system|}[code]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("lifecycle-status", "active");
            FHIRResponse response = client.search(Goal.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, goalId1);
        }
    }

    @Test
    public void testSearchForPatientTargetDate() throws Exception {
        // SHOULD support searching using the combination of the patient and target-date search parameters:
        // including support for these target-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on target-date
        // (e.g.target-date=[date]&target-date=[date]]&...)
        // GET [base]/Goal?patient=[reference]&target-date={gt|lt|ge|le}[date]{&target-date={gt|lt|ge|le}[date]&...}

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("target-date", "ge2016");
            FHIRResponse response = client.search(Goal.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, goalId1);
        }
    }
}