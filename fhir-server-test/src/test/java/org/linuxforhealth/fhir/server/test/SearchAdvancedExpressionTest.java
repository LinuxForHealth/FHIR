/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.test.TestUtil;

/**
 * This Tests advanced expressions with FHIR Path
 */
public class SearchAdvancedExpressionTest extends FHIRServerTestBase {
    private String encounterId;

    @BeforeClass(groups = { "server-search-advanced" })
    public void testCreateEncounter() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Encounter and then call the 'create' API.
        Encounter encounter = TestUtil.readExampleResource("json/spec/encounter-example-f203-20130311.json");

        Entity<Encounter> entity = Entity.entity(encounter, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Encounter")
                .request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        encounterId = getLocationLogicalId(response);

        response = target.path("Encounter/" + encounterId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-advanced" }, dependsOnMethods = { })
    public void testSearchWithSearchParameter() {
        WebTarget target = getWebTarget();

        Response response = target.path("Encounter")
                .queryParam("diff-start-end-time", "lt900")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    /*
     * Cleanup the existing resources
     */
    @AfterClass(groups = { "server-search-advanced" })
    public void deleteEncounter() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Encounter/" + encounterId)
                .request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "study1")
                .delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
}