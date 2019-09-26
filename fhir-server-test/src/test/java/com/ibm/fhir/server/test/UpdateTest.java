/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.UUID;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Id;

/**
 * Tests the update operation.
 */
public class UpdateTest extends FHIRServerTestBase {
    
    private Boolean updateCreateEnabled = null;
    
    private Patient savedUCPatient = null;
    
    /**
     * Retrieve the server's conformance statement to determine the status
     * of certain runtime options.
     * @throws Exception 
     */
    @Test
    public void retrieveConfig() throws Exception {
        updateCreateEnabled = isUpdateCreateSupported();
        System.out.println("Update/Create enabled?: " + updateCreateEnabled.toString());
    }
    
    /**
     * Test the "update/create" behavior.
     */
    @Test(dependsOnMethods = {"retrieveConfig"})
    public void testUpdateCreate1() throws Exception {
        assertNotNull(updateCreateEnabled);
        
        // If the "Update/Create" feature is enabled, then test it.
        if (updateCreateEnabled.booleanValue()) {
            
            // Generate an ID for the new resource.
            String newId = UUID.randomUUID().toString();
            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            patient = patient.toBuilder().id(Id.of(newId)).build();
            
            // Create the new resource via the update operation.
            FHIRClient client = getFHIRClient();
            FHIRResponse response = client.update(patient);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
            
            // Now read the resource to verify it's there.
            response = client.read("Patient", newId);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            Patient responsePatient = response.getResource(Patient.class);
            assertNotNull(responsePatient);
            savedUCPatient = responsePatient;
        }
    }
    /**
     * Test the normal update behavior.
     */
    @Test(dependsOnMethods = {"testUpdateCreate1"})
    public void testUpdateCreate2() throws Exception {
        assertNotNull(updateCreateEnabled);
        
        // If the "Update/Create" feature is enabled, then test the normal update behavior.
        if (updateCreateEnabled.booleanValue()) {
            
            Patient patient = savedUCPatient;
            patient = patient.toBuilder().birthDate(Date.of("1986-06-20")).build();
            
            // Update the resource that was previously created.
            FHIRClient client = getFHIRClient();
            FHIRResponse response = client.update(patient);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            String locationURI = response.getLocation();
            String[] locationTokens = getLocationURITokens(locationURI);
            assertEquals(4, locationTokens.length);
            assertEquals("2", locationTokens[3]);
            
            // Now read the resource to verify it's there.
            response = client.vread(locationTokens[0], locationTokens[1], locationTokens[3]);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            Patient responsePatient = response.getResource(Patient.class);
            assertNotNull(responsePatient);
        }
    }

    /**
     * Test the base-level "update" behavior (negative test).
     */
    @Test(dependsOnMethods = {"retrieveConfig"})
    public void testUpdateOnly1() throws Exception {
        assertNotNull(updateCreateEnabled);
        
        // If the "Update/Create" feature is disabled, then make sure
        // we get an error when trying to do an update on a non-existent resource.
        if (!updateCreateEnabled.booleanValue()) {
            
            // Generate an ID for the new resource.
            String newId = UUID.randomUUID().toString();
            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            patient = patient.toBuilder().id(Id.of(newId)).build();
            
            // Call update for this new resource and make sure we get back an error.
            FHIRClient client = getFHIRClient();
            FHIRResponse response = client.update(patient);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.METHOD_NOT_ALLOWED.getStatusCode());
        }
    }
    
    /**
     * Test the base-level "update" behavior.
     */
    @Test(dependsOnMethods = {"retrieveConfig"})
    public void testUpdateOnly2() throws Exception {
        assertNotNull(updateCreateEnabled);
        
        // If the "Update/Create" feature is disabled, then test the base behavior
        // to make sure we can create and then update a resource.
        if (!updateCreateEnabled.booleanValue()) {
            
            // Generate an ID for the new resource.
            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            
            // Call update for this new resource and make sure we get back an error.
            FHIRClient client = getFHIRClient();
            FHIRResponse response = client.create(patient);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
            String locationURI = response.getLocation();
            String[] locationTokens = getLocationURITokens(locationURI);
            assertEquals(4, locationTokens.length);
            assertEquals("1", locationTokens[3]);
            
            // Read the new patient.
            response = client.read(locationTokens[0], locationTokens[1]);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            Patient createdPatient = response.getResource(Patient.class);
            assertNotNull(createdPatient);
            
            // Update the patient.
            createdPatient = createdPatient.toBuilder().birthDate(Date.of("1987-10-09")).build();
            
            response = client.update(createdPatient);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            locationURI = response.getLocation();
            locationTokens = getLocationURITokens(locationURI);
            assertEquals(4, locationTokens.length);
            assertEquals("2", locationTokens[3]);            
        }
    }


    /**
     * Test the base-level "update" behavior.
     */
    @Test(dependsOnMethods = {"retrieveConfig"})
    public void testUpdateCreate3() throws Exception {
        assertNotNull(updateCreateEnabled);
        
        if (updateCreateEnabled.booleanValue()) {
            
            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            
            FHIRClient client = getFHIRClient();
            FHIRResponse response = client.create(patient);
            assertNotNull(response);
            assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
            String locationURI = response.getLocation();
            
            String[] locationTokens = response.parseLocation(response.getLocation());
            String deletedId = locationTokens[1];
                       
            // Read the new patient.
            response = client.read(locationTokens[0], locationTokens[1]);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            Patient createdPatient = response.getResource(Patient.class);
            assertNotNull(createdPatient);
            
            
            response = client.delete("Patient", deletedId);
            assertNotNull(response);
            if (isDeleteSupported()) {
                assertResponse(response.getResponse(), Response.Status.NO_CONTENT.getStatusCode());
                assertNotNull(response.getETag());
                assertEquals("W/\"2\"", response.getETag());
            } else {
                assertResponse(response.getResponse(), Response.Status.METHOD_NOT_ALLOWED.getStatusCode());
            }
            
            
            // Read the new patient.
            response = client.read("Patient", deletedId);
            assertResponse(response.getResponse(), Response.Status.GONE.getStatusCode());
            
            
            // Update the patient.
            createdPatient = createdPatient.toBuilder().birthDate(Date.of("1987-10-09")).build();
            
            response = client.update(createdPatient);
            assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
            locationURI = response.getLocation();
            locationTokens = getLocationURITokens(locationURI);
            assertEquals(4, locationTokens.length);
            assertEquals("3", locationTokens[3]);            
        }
    }
}
