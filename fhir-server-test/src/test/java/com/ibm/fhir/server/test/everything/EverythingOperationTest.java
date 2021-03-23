/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.everything;

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
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.SerializationUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.server.test.FHIRServerTestBase;
import com.ibm.fhir.server.test.profiles.ProfilesTestBase;

/**
 * Tests the Everything Operation
 */
public class EverythingOperationTest extends FHIRServerTestBase {

    private static final String CLASSNAME = ProfilesTestBase.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

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
                logger.warning("Operation $everything not found on server");
                SKIP = true;
            } else {
                SKIP = false;
            }
        }
    }

    /**
     * Create a Bundle of 895 resources of various kinds representing a patient's history and save the
     * resource types and IDs of the created resources to eventually ensure that all resources are included
     * in an $everything invocation.
     *
     * @throws Exception
     */
    @Test(groups = { "fhir-operation" })
    public void testCreatePatientWithEverything() throws Exception {
        if (SKIP) {
            logger.warning("Skipping integration test for $everything");
            return;
        }
        Bundle patientBundle = TestUtil.readLocalResource("everything-operation/Antonia30_Acosta403.json");
        Entity<Bundle> entity = Entity.entity(patientBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget().request().post(entity, Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertFalse(responseBundle.getEntry().isEmpty());
        for (Entry entry : responseBundle.getEntry()) {
            com.ibm.fhir.model.resource.Bundle.Entry.Response transactionResponse = entry.getResponse();
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
            logger.warning("Skipping integration test for $everything");
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
        // TODO: Add support for retrieving these two that aren't currently part of the compartment resources
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
            logger.warning("Skipping integration test for $everything");
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_count", 1).request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // Count is ignored
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 895);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithStartAndStop() {
        if (SKIP) {
            logger.warning("Skipping integration test for $everything");
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
            logger.warning("Skipping integration test for $everything");
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_type", "CareTeam,CarePlan").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // 5 CareTeams + 5 CarePlans + 1 Patient
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 11);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithBadType() {
        if (SKIP) {
            logger.warning("Skipping integration test for $everything");
            return;
        }
        Response response =
                getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_type", "CareTeam,CarePlan,UnknownType").request().get(Response.class);

        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithSince() {
        if (SKIP) {
            logger.warning("Skipping integration test for $everything");
            return;
        }
        Response response = getWebTarget().path("Patient/" + patientId
                + "/$everything").queryParam("_type", "CareTeam,CarePlan").queryParam("_since", "2021-01-01T00:00:00Z").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // 5 CareTeams + 5 CarePlans + 1 Patient
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 11);
    }

    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithFutureSince() {
        if (SKIP) {
            logger.warning("Skipping integration test for $everything");
            return;
        }
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime tomorrow = today.plusDays(1);

        Response response = getWebTarget().path("Patient/" + patientId + "/$everything").queryParam("_since", tomorrow + "Z").request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);

        // Only the patient and 0 resources
        assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 1);
    }

    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingForNotExistingPatient() {
        if (SKIP) {
            logger.warning("Skipping integration test for $everything");
            return;
        }
        Response response = getWebTarget().path("Patient/some-unknown-id/$everything").request().get(Response.class);
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
