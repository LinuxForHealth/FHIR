/*
 * (C) Copyright IBM Corp. 2021
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.BundleType;

/**
 * 
 * @author Luis A. García
 */
public class EverythingOperationTest extends FHIRServerTestBase {
    
    private static final boolean DEBUG = false;

    private Map<String, List<String>> createdResources;
    private String patientId;
    
    /**
     * 
     */
    public EverythingOperationTest() {
        createdResources = new HashMap<>();
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
        Bundle patientBundle = TestUtil.readLocalResource("everything-operation/Antonia30_Acosta403.json");
        Entity<Bundle> entity = Entity.entity(patientBundle, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = getWebTarget()
                .request()
                .post(entity, Response.class);
        
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

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testCreatePatientWithEverything" })
    public void testPatientEverything() {
        // Get the patient ID and invoke the $everything operation on it
        patientId = createdResources.get("Patient").get(0);
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .request()
                .get(Response.class);

        // Ensure that the 895 resources are accounted for in the returning search set bundle 
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 895);
        for (Entry entry : everythingBundle.getEntry()) {
            String fullURL = entry.getFullUrl().getValue();
            String[] locationElements = fullURL.replaceAll(getWebTarget().getUri().toString(), "").split("/");
            assertTrue(locationElements.length >= 2, "Incorrect full URL format: " + fullURL);
            String resourceType = locationElements[locationElements.length - 2];
            String resourceId = locationElements[locationElements.length - 1];
            List<String> resources = createdResources.get(resourceType);
            if (resources.remove(resourceId)) {
                println("Found expected " + resourceType + ": " + resourceId);
            } else {
                println("Found unkown " + resourceType + ": " + resourceId);
            }
        }
        
        // List all the resources pending removal for each type
        Set<java.util.Map.Entry<String, List<String>>> entries = createdResources.entrySet();
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
            createdResources.remove(key);
        }
        assertTrue(createdResources.isEmpty());
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithCount() {
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .queryParam("_count", 1)
                .request()
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        
        // The initial bundle includes resources from 11 resource types (not counting practitioners and organizations), 
        // here we test that when count = 1 we only get 12 resources, 1 from each resource type + 1 for the Patient
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 12);
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithStartAndStop() {
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .queryParam("start", "1990-01-01")
                .queryParam("end", "2010-01-01")
                .request()
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        
        // The number of companies was reduced as the scope was narrowed down to a decade
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 371);
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithTypes() {
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .queryParam("_type", "CareTeam,CarePlan")
                .request()
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        
        // 5 CareTeams + 5 CarePlans + 1 Patient
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 11);
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithBadType() {
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .queryParam("_type", "CareTeam,CarePlan,UnknownType")
                .request()
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        
        // When wrong types are included those types are dropped
        // 5 CareTeams + 5 CarePlans + 1 Patient + 0 UnknownType
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 11);
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithSince() {
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .queryParam("_type", "CareTeam,CarePlan")
                .queryParam("_since", "2021-01-01T00:00:00Z")
                .request()
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        
        // 5 CareTeams + 5 CarePlans + 1 Patient
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 11);
    }

    /**
     * 
     */
    @Test(groups = { "fhir-operation" }, dependsOnMethods = { "testPatientEverything" })
    public void testPatientEverythingWithFutureSince() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        LocalDateTime tomorrow = today.plusDays(1);
        
        Response response = getWebTarget()
                .path("Patient/" + patientId +"/$everything")
                .queryParam("_since", tomorrow + "Z")
                .request()
                .get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle everythingBundle = response.readEntity(Bundle.class);
        
        // Only the patient and 0 resources
        BundleTest.assertResponseBundle(everythingBundle, BundleType.SEARCHSET, 1);
    }
    
    /**
     * 
     */
    @Test(groups = { "fhir-operation" })
    public void testPatientEverythingForNotExistingPatient() {
        Response response = getWebTarget()
                .path("Patient/some-unknown-id/$everything")
                .request()
                .get(Response.class);
        assertResponse(response, Response.Status.NOT_FOUND.getStatusCode());        
    }
 
    private void println(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }
}
