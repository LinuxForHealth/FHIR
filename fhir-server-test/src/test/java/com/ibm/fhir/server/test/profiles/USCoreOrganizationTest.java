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
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the US Core 3.1.1 Profile with Organization.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-organization.html
 */
public class USCoreOrganizationTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String organizationId1 = null;
    private String organizationId2  = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-organization|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Organization', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadOrganization1();
            loadOrganization2();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteOrganization1();
            deleteOrganization2();
        }
    }

    public void loadOrganization1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Organization-acme-lab.json";
        WebTarget target = getWebTarget();

        Organization goal = TestUtil.readExampleResource(resource);

        Entity<Organization> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Organization/12354 (first actual test, but simple)
        organizationId1 = getLocationLogicalId(response);
        response = target.path("Organization/" + organizationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteOrganization1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Organization/" + organizationId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
    
    public void loadOrganization2() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Organization-saint-luke-w-endpoint.json";
        WebTarget target = getWebTarget();

        Organization goal = TestUtil.readExampleResource(resource);

        Entity<Organization> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Organization/12354 (first actual test, but simple)
        organizationId2 = getLocationLogicalId(response);
        response = target.path("Organization/" + organizationId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteOrganization2() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Organization/" + organizationId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchByName() throws Exception {
        // SHALL support searching by organization name using the name search parameter:
        // GET [base]/Organization?name=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("name:contains", "Labs,Hospital");
            FHIRResponse response = client.search(Organization.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, organizationId1);
            assertContainsIds(bundle, organizationId2);
        }
    }

    @Test
    public void testSearchByAddress() throws Exception {
        // SHALL support searching organization based on text address using the address search parameter:
        // GET [base]/Organization?address=[string]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("address:contains", "Washtenaw,Wornall");
            FHIRResponse response = client.search(Organization.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, organizationId1);
            assertContainsIds(bundle, organizationId2);
        }
    }
}