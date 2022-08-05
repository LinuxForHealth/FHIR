/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.operation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.SerializationUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.CapabilityStatement;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

/**
 * Tests the Everything Operation
 */
public class EverythingOperationTenant1Test extends FHIRServerTestBase {

    private static final String CLASSNAME = EverythingOperationTest.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    public static final String EXPRESSION_OPERATION = "rest.resource.where(type='Patient').operation.name";

    private static Boolean SKIP = null;

    private static final boolean DEBUG = false;

    private Map<String, List<String>> createdResources;
    private String patientId;

    public EverythingOperationTenant1Test() {
        createdResources = new HashMap<>();
    }

    @BeforeClass
    public void checkOperationExistsOnServer() throws Exception {
        if (SKIP == null) {
            CapabilityStatement conf = retrieveConformanceStatement();
            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(conf);
            // All the possible required operations
            Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPRESSION_OPERATION);
            Collection<String> listOfOperations = tmpResults.stream().map(x -> x.getValue().asStringValue().string()).collect(Collectors.toList());

            if (!listOfOperations.contains("everything")) {
                LOG.warning("Operation $everything not found on server, Skipping integration test for $everything");
                SKIP = true;
            } else {
                SKIP = false;
            }
        }
    }

    /**
     * Create a Bundle of 5 resources of various kinds representing a patient's history and save the
     * resource types and IDs of the created resources to eventually ensure that all resources are included
     * in an $everything invocation.
     *
     * @throws Exception
     */
    @Test(groups = { "fhir-operation" })
    public void testCreatePatientWithEverything() throws Exception {
        if (SKIP) {
            return;
        }
        Bundle patientBundle = TestUtil.readLocalResource("everything-operation/Patient999.json");
        Entity<Bundle> entity = Entity.entity(patientBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget().request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .post(entity, Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertFalse(responseBundle.getEntry().isEmpty());
        for (Entry entry : responseBundle.getEntry()) {
            org.linuxforhealth.fhir.model.resource.Bundle.Entry.Response transactionResponse = entry.getResponse();
            assertEquals(transactionResponse.getStatus().getValue(), Integer.toString(Response.Status.CREATED.getStatusCode()));
            String[] locationElements = transactionResponse.getLocation().getValue().split("/");
            assertTrue(locationElements.length > 2, "Incorrect location URI format: " + transactionResponse.getLocation());
            String resourceType = locationElements[0];
            String resourceId = locationElements[1];
            List<String> resources = createdResources.computeIfAbsent(resourceType, k -> new ArrayList<>());
            resources.add(resourceId);
        }
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatientWithEverything" })
    public void testPatientEverything() {
        if (SKIP) {
            return;
        }
        // Get the patient ID and invoke the $everything operation on it
        patientId = createdResources.get("Patient").get(0);
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);

        // Create a deep copy of the created resources so we can modify it
        // but keep the original so we can delete all created resources
        Map<String, List<String>> resourcesMap = SerializationUtils.clone((HashMap<String, List<String>>) createdResources);

        // Ensure that the 5 resources are accounted for in the returning search set bundle
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 5);
        for (Entry entry : everythingBundle.getEntry()) {
            String fullURL = entry.getFullUrl().getValue();
            String[] locationElements = fullURL.replaceAll(getWebTarget().getUri().toString(), "").split("/");
            assertTrue(locationElements.length >= 2, "Incorrect full URL format: " + fullURL);
            String resourceType = locationElements[locationElements.length - 2];
            String resourceId = locationElements[locationElements.length - 1];
            List<String> resources = resourcesMap.get(resourceType);
            if (resources.remove(resourceId)) {
                println("Found expected " + resourceType + ": " + resourceId);
            } else {
                println("Found unknown " + resourceType + ": " + resourceId);
            }
        }

        // List all the resources pending removal for each type
        Set<java.util.Map.Entry<String, List<String>>> entries = resourcesMap.entrySet();
        List<String> keysToRemove = new ArrayList<>();
        for (java.util.Map.Entry<String, List<String>> entry : entries) {
            println(entry.getKey() + ": ");
            if (entry.getValue().isEmpty()) {
                println("- All removed!");
                keysToRemove.add(entry.getKey());
            } else {
                for (String id : entry.getValue()) {
                    println("- " + id);
                }
            }
        }

        // Remove all entries from the map that no longer have resources left to account for
        // we should have accounted for all resources of that type such that the map should be empty
        for (String key : keysToRemove) {
            resourcesMap.remove(key);
        }
        assertTrue(resourcesMap.isEmpty());
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithCount() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_count", 1).request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // Count is ignored
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 5);
    }

    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingAtTypeLevel() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/$everything").queryParam("_count", 1).request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);

        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithTypes() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_type", "Patient,CareTeam,CarePlan").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // We get five because there are 4 Organization resources linked to from the patient (no CareTeam or CarePlans)
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 5);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithBadType() {
        if (SKIP) {
            return;
        }
        Response response =
                getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_type", "CareTeam,CarePlan,UnknownType").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);

        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingForNotExistingPatient() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/some-unknown-id/$everything").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test(groups = { "fhir-operation" })
    public void testCreateAndDeletePatientVerifyDelete() throws Exception {
        if (SKIP) {
            return;
        }
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient ptnt = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity =
                Entity.entity(ptnt, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(ptnt, responsePatient);

        response = getWebTarget().path("Patient/" + id).request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .delete();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = getWebTarget().path("Patient/" + id).request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);
        assertResponse(response, Response.Status.GONE.getStatusCode());

        response = getWebTarget().path("Patient/" + id + "/$everything").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get(Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * Clean up all the resources created in this test.
     */
    @AfterClass
    public void deleteTestResources() {
        Set<java.util.Map.Entry<String, List<String>>> entries = createdResources.entrySet();
        for (java.util.Map.Entry<String, List<String>> entry : entries) {
            String resourceType = entry.getKey();
            List<String> resourceIds = entry.getValue();
            for (String resourceId : resourceIds) {
                Response response = getWebTarget().path(resourceType + "/" + resourceId).request()
                        .header("X-FHIR-TENANT-ID", "tenant1")
                        .header("X-FHIR-DSID", "profile")
                        .delete();
                if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                    println("Could not delete test resource " + resourceType + "/" + resourceId + ": " + response.getStatus());
                }
            }
        }
    }

    private void println(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }
}