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
import org.linuxforhealth.fhir.model.resource.Location;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Location.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-location.html
 */
public class USCoreLocationTest extends ProfilesTestBaseV2 {

    private String locationId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-location|3.1.1");
    }

    @Override
    public void loadResources() throws Exception {
        String resource = "Location-hl7east.json";
        Location location = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        locationId1 = createResourceAndReturnTheLogicalId("Location", location);
    }

    @Test
    public void testSearchForName() throws Exception {
        // SHALL support searching by location name using the name search parameter:
        // GET [base]/Location?name=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("name", "Health Level Seven International - Amherst");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, locationId1);
    }

    @Test
    public void testSearchForAddress() throws Exception {
        // SHALL support searching location based on text address using the address search parameter:
        // GET [base]/Location?address=[string]
        // "3300 Washtenaw Avenue, Suite 227"
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("address:contains", "Washtenaw");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, locationId1);
    }

    @Test
    public void testSearchForAddressCity() throws Exception {
        // SHOULD support searching using the address-city search parameter:
        // GET [base]/Location?address-city=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("address-city", "Amherst");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, locationId1);
    }

    @Test
    public void testSearchForAddressState() throws Exception {
        // SHOULD support searching using the address-state search parameter:
        // GET [base]/Location?address-state=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("address-state", "MA");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, locationId1);
    }

    @Test
    public void testSearchForAddressPostalCode() throws Exception {
        // SHOULD support searching using the address-postalcode search parameter:
        // GET [base]/Location?address-postalcode=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("address-postalcode", "01002");
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, locationId1);
    }
}