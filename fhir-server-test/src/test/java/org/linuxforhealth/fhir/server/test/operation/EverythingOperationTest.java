/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.operation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

/**
 * Tests the Everything Operation
 */
public class EverythingOperationTest extends FHIRServerTestBase {

    private static final String CLASSNAME = EverythingOperationTest.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    public static final String EXPRESSION_OPERATION = "rest.resource.operation.name";

    private static Boolean SKIP = null;

    private static final boolean DEBUG = false;

    private Map<String, List<String>> createdResources;
    private String patientId;

    public EverythingOperationTest() {
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
     * Create a Bundle of 901 resources of various kinds (895 associated with the Patient) representing a
     * patient's history and save the resource types and IDs of the created resources to eventually ensure
     * that all resources are included in an $everything invocation.
     */
    @Test(groups = { "fhir-operation" })
    public void testCreatePatientWithEverything() throws Exception {
        if (SKIP) {
            return;
        }
        Bundle patientBundle = TestUtil.readLocalResource("everything-operation/Antonia30_Acosta403.json");
        Entity<Bundle> entity = Entity.entity(patientBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget().request().post(entity, Response.class);

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
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").request().get(Response.class);

        // Create a deep copy of the created resources so we can modify it
        // but keep the original so we can delete all created resources
        Map<String, List<String>> resourcesMap = SerializationUtils.clone((HashMap<String, List<String>>) createdResources);

        // Ensure that the 895 resources are accounted for in the returning search set bundle
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 895);
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
        // Remove these two as these two resources are not part of the default server config
        keysToRemove.add(Practitioner.class.getSimpleName());
        keysToRemove.add(Organization.class.getSimpleName());

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
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_count", 1).request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // Count is ignored
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 895);
    }

    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingAtTypeLevel() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/$everything").queryParam("_count", 1).request().get(Response.class);

        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithStartAndStop() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId
                + "/$everything").queryParam("start", "1990-01-01").queryParam("end", "2010-01-01").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // The number of companies was reduced as the scope was narrowed down to a decade
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 371);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithTypes() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_type", "CareTeam,CarePlan").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // 5 CareTeams + 5 CarePlans
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 10);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithBadType() {
        if (SKIP) {
            return;
        }
        Response response =
                getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_type", "CareTeam,CarePlan,UnknownType").request().get(Response.class);

        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithSince() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId
                + "/$everything").queryParam("_type", "CareTeam,CarePlan").queryParam("_since", "2021-01-01T00:00:00Z").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // 5 CareTeams + 5 CarePlans
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 10);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithFutureSince() {
        if (SKIP) {
            return;
        }
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime tomorrow = today.plusDays(1);

        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_since", tomorrow + "Z").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // None of the resources are "since" tomorrow
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 0);
    }

    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingForNotExistingPatient() {
        if (SKIP) {
            return;
        }
        Response response = getWebTarget().path("Patient/some-unknown-id/$everything").request().get(Response.class);
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
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + id).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "default")
                .header("X-FHIR-DSID", "default")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(ptnt, responsePatient);

        response = getWebTarget().path("Patient/" + id).request().delete();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = getWebTarget().path("Patient/" + id).request().get(Response.class);
        assertResponse(response, Response.Status.GONE.getStatusCode());

        response = getWebTarget().path("Patient/" + id + "/$everything").request().get(Response.class);
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
                Response response = getWebTarget().path(resourceType + "/" + resourceId).request().delete();
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