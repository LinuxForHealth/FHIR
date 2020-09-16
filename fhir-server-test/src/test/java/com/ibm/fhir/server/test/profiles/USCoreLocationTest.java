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
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the US Core 3.1.1 Profile with Location.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-location.html
 */
public class USCoreLocationTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String locationId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-location|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Location', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadLocation1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteLocation1();
        }
    }

    public void loadLocation1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Location-hl7east.json";
        WebTarget target = getWebTarget();

        Location goal = TestUtil.readExampleResource(resource);

        Entity<Location> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Location").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Location/12354 (first actual test, but simple)
        locationId1 = getLocationLogicalId(response);
        response = target.path("Location/" + locationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteLocation1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Location/" + locationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForName() throws Exception {
        // SHALL support searching by location name using the name search parameter:
        // GET [base]/Location?name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name", "Health Level Seven International - Amherst");
            FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, locationId1);
        }
    }

    @Test
    public void testSearchForAddress() throws Exception {
        // SHALL support searching location based on text address using the address search parameter:
        // GET [base]/Location?address=[string]
        // "3300 Washtenaw Avenue, Suite 227"
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("address:contains", "Washtenaw");
            FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, locationId1);
        }
    }

    @Test
    public void testSearchForAddressCity() throws Exception {
        // SHOULD support searching using the address-city search parameter:
        // GET [base]/Location?address-city=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("address-city", "Amherst");
            FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, locationId1);
        }
    }

    @Test
    public void testSearchForAddressState() throws Exception {
        // SHOULD support searching using the address-state search parameter:
        // GET [base]/Location?address-state=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("address-state", "MA");
            FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, locationId1);
        }
    }

    @Test
    public void testSearchForAddressPostalCode() throws Exception {
        // SHOULD support searching using the address-postalcode search parameter:
        // GET [base]/Location?address-postalcode=[string]
        if (!skip) {
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
}