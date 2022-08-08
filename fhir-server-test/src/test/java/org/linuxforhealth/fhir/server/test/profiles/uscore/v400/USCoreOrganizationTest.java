/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v400;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with Organization.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-organization.html
 */
public class USCoreOrganizationTest extends ProfilesTestBaseV2 {

    public Boolean skip = Boolean.TRUE;

    private String organizationId1 = null;
    private String organizationId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-organization|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        loadOrganization1();
        loadOrganization2();
    }

    public void loadOrganization1() throws Exception {
        String resource = "Organization-acme-lab.json";
        String cls = "Organization";
        organizationId1 = buildAndAssertOnResourceForUsCore(cls, "400", resource);
    }

    public void loadOrganization2() throws Exception {
        String resource = "Organization-saint-luke-w-endpoint.json";
        String cls = "Organization";
        organizationId2 = buildAndAssertOnResourceForUsCore(cls, "400", resource);
    }

    @Test
    public void testSearchByName() throws Exception {
        // SHALL support searching by organization name using the name search parameter:
        // GET [base]/Organization?name=[string]
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

    @Test
    public void testSearchByAddress() throws Exception {
        // SHALL support searching organization based on text address using the address search parameter:
        // GET [base]/Organization?address=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("address:contains", "Washtenaw,Wornall");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Organization.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, organizationId1);
        assertContainsIds(bundle, organizationId2);
    }
}