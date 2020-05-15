/*
 * (C) Copyright IBM Corp. 2019, 2020
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

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.PractitionerRole;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;

public class SearchNearTest extends FHIRServerTestBase {
    private String locationId;
    private String locationAbsId;
    private String practitionerRoleId;

    @Test(groups = { "server-search-near" })
    public void testCreateLocation() throws Exception {
        WebTarget target = getWebTarget();

        Location location = TestUtil.readExampleResource("json/spec/location-example.json");
        Location locationAbs = TestUtil.readExampleResource("json/ibm/complete-absent/Location-1.json");
        Entity<Location> entity = Entity.entity(location, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Location").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        locationId = getLocationLogicalId(response);

        Entity<Location> entityAbs = Entity.entity(locationAbs, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response responseAbs = target.path("Location").request().post(entityAbs, Response.class);
        assertResponse(responseAbs, Response.Status.CREATED.getStatusCode());

        locationAbsId = getLocationLogicalId(responseAbs);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response   = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response   = target.path("Location/" + locationAbsId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Chained Reference
        Entity<PractitionerRole> entityPractitionerRole = Entity.entity(buildChainedResources(), FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("PractitionerRole").request().post(entityPractitionerRole, Response.class);
        String res = response.readEntity(String.class);
                System.out.println(res);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        practitionerRoleId = getLocationLogicalId(response);
        response   = target.path("PractitionerRole/" + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public PractitionerRole buildChainedResources() throws Exception {
        Reference location = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Location/" + locationId)).build();
        PractitionerRole practitionerRole =TestUtil.readExampleResource("/json/spec/practitionerrole-example.json");
        return practitionerRole.toBuilder().location(location).build();
    }

    @AfterClass
    public void testDeleteLocations() {
        WebTarget target = getWebTarget();
        Response response   = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
        response   = target.path("Location/" + locationAbsId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Remove Chained Reference
        response   = target.path("PractitionerRole/" + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationNear() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "42.256500|-83.694810|11.20|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationNearExact() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "42.25475478|-83.6945691|0.0|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationNearWithinRange() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "40.256500|-80.694810|500.0|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationNearWithinChainedRange() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("location.near", "40.256500|-80.694810|500.0|km");
        FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationNearReturnNone() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "-83.694810|42.256500|11.20|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 0);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationNearWithinRangeMultiple() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near", "40.256500|-80.694810|500.0|km,40.256500|-80.694810|500.0|km");
        parameters.searchParam("near", "42.256500|-83.694810|11.20|km");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);

        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationWithMissing() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-near" }, dependsOnMethods = { "testCreateLocation" })
    public void testSearchUsingLocationWithMissingFalse() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("near:missing", "false");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }
    
}