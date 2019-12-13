/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.test.TestUtil;

public class SearchNearTest extends FHIRServerTestBase {
    private String locationId;

    @Test(groups = { "server-search-near" })
    public void testCreateLocation() throws Exception {
        WebTarget target = getWebTarget();

        Location location = TestUtil.readExampleResource("json/spec/location-example.json");

        Entity<Location> entity = Entity.entity(location, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Location").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        locationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response   = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchAllUsingLocationNear() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "42.256500|-83.694810|11.20|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchAllUsingLocationNearExact() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "42.25475478|-83.6945691|0.0|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }
    
    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchAllUsingLocationNearWithinRange() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "40.256500|-80.694810|500.0|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchAllUsingLocationNearReturnNone() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "-83.694810|42.256500|11.20|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 0);
    }
}