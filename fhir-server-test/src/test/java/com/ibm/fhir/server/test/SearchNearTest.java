/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.PractitionerRole;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;

public class SearchNearTest extends FHIRServerTestBase {
    private String locationId;

    @Test(groups = { "server-search-near" })
    public void testCreateLocation() throws Exception {
        Location location = TestUtil.readExampleResource("json/spec/location-example.json");
        locationId = createResourceAndReturnTheLogicalId("Location", location);

        Location locationAbs = TestUtil.readExampleResource("json/ibm/complete-absent/Location-1.json");
        createResourceAndReturnTheLogicalId("Location", locationAbs);

        createResourceAndReturnTheLogicalId("PractitionerRole", buildChainedResources());
    }

    public PractitionerRole buildChainedResources() throws Exception {
        Reference location = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Location/" + locationId)).build();
        PractitionerRole practitionerRole =TestUtil.readExampleResource("/json/spec/practitionerrole-example.json");
        return practitionerRole.toBuilder().location(location).build();
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