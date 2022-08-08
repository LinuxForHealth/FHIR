/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.bulkdata;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Group;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.search.group.GroupSearchCompiler;
import org.linuxforhealth.fhir.search.group.GroupSearchCompilerFactory;
import org.linuxforhealth.fhir.server.test.BundleTest;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

/**
 * Tests the Dynamic Group to Search Query as an Integration Test
 */
public class DynamicGroupToSearchQueryTest extends FHIRServerTestBase {
    private Group group = null;
    private String savedGroupId = null;
    private Bundle sampleBundle = null;

    @BeforeClass
    public void startup() throws Exception {
        WebTarget target = getWebTarget();

        group = TestUtil.readExampleResource("json/ibm/bulk-data/group/age-range-with-gender-and-exclude-group.json");
        Entity<Group> entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Group").request().header("Prefer", "return=representation").post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        savedGroupId = getLocationLogicalId(response);

        response = target.path("Group/" + savedGroupId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle bundle = (Bundle) TestUtil.readExampleResource("json/ibm/bulk-data/group/age-range-with-gender-and-exclude-example.json");
        Entity<Bundle> bundleEntity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response bundleResponse = target.path("/").request().header("Prefer", "return=representation").post(bundleEntity, Response.class);
        assertResponse(bundleResponse, Response.Status.OK.getStatusCode());

        BundleTest bt = new BundleTest();
        sampleBundle = bt.getEntityWithExtraWork(bundleResponse, "startup");
    }

    @AfterClass
    public void shutdown() {
        for (org.linuxforhealth.fhir.model.resource.Bundle.Entry entry : sampleBundle.getEntry()) {
            String id = entry.getResource().getId();
            String resourceType = entry.getResource().getClass().getSimpleName();
            WebTarget target = getWebTarget();
            Response response = target.path(resourceType + "/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @Test
    public void testDynamicGroupToSearchQueryTest() throws Exception {
        GroupSearchCompiler compiler = GroupSearchCompilerFactory.getInstance();
        MultivaluedMap<String, String> queryParams = compiler.groupToSearch(group, "Patient");

        FHIRParameters parameters = new FHIRParameters();
        for (Entry<String, List<String>> entry : queryParams.entrySet()) {
            if (entry.getValue() != null) {
                for (String val : entry.getValue()) {
                    parameters.searchParam(entry.getKey(), val);
                }
            }
        }

        // sort newest to oldest
        parameters.searchParam("_sort", "-_lastUpdated");
        parameters.searchParam("_count", "10000");
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        List<String> resources = new ArrayList<>();
        for (org.linuxforhealth.fhir.model.resource.Bundle.Entry entry : sampleBundle.getEntry()) {
            String id = entry.getResource().getId();
            String resourceType = entry.getResource().getClass().getSimpleName();

            // Intentionally doing this as a loop, in the future we'll have _include and _has
            if ("Patient".equals(resourceType)) {
                resources.add(resourceType + "/" + id);
            }
        }

        for (org.linuxforhealth.fhir.model.resource.Bundle.Entry entry : bundle.getEntry()) {
            String id = entry.getResource().getId();
            String resourceType = entry.getResource().getClass().getSimpleName();
            if (resources.contains(resourceType + "/" + id)) {
                resources.remove(resourceType + "/" + id);
            }
        }
        assertTrue(resources.isEmpty());
    }
}