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
import com.ibm.fhir.model.resource.CareTeam;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.CareTeamStatus;

/**
 * Tests the US Core 3.1.1 Profile with CareTeam.
 *
 *The specification says the following parameters should work:
 * <code>status=http://hl7.org/fhir/care-team-status|active</code>
 * It's a default binding and should work without a bound system. We only extract active, and not the default system.
 */
public class USCoreCareTeamTest extends ProfilesTestBase {
    private static final String CLASSNAME = USCoreCareTeamTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;

    private String careTeamId = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-careteam|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - CareTeam', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadCareTeam();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteCareTeam();
        }
    }

    public void loadCareTeam() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/CareTeam-example.json";
        WebTarget target = getWebTarget();

        CareTeam CareTeam = TestUtil.readExampleResource(resource);

        // Note: The test uses ACTIVE as a CodeableConcept rather than a plain string.
        CareTeam = CareTeam.toBuilder().status(CareTeamStatus.ACTIVE).build();

        Entity<CareTeam> entity = Entity.entity(CareTeam, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("CareTeam").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        careTeamId = getLocationLogicalId(response);
        response = target.path("CareTeam/" + careTeamId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteCareTeam() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("CareTeam/" + careTeamId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForPatientAndCategory() throws Exception {
        // SHALL support searching using the combination of the patient and status search parameters:
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET [base]/CareTeam?patient=[reference]&status=active
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-careteam.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("status", "http://hl7.org/fhir/care-team-status|active");
            FHIRResponse response = client.search(CareTeam.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, careTeamId);
        }
    }
}