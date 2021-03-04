/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.code.AdministrativeGender;

/**
 * This class tests the system history interactions
 * [base]/_history
 * [base]/_history?_count=10
 * [base]/_history?_count=10&_since=2021-02-21T03:00:53.878052Z"
 * [base]/_history?_count=10&_since=2021-02-21T03:00:53.878052Z&_afterHistoryId=4"
 */
public class SystemHistoryTest extends FHIRServerTestBase {
    private static final Logger logger = Logger.getLogger(SystemHistoryTest.class.getName());

    // Create some resources, update, delete and undelete
    private boolean deleteSupported = false;

    /**
     * Retrieve the server's conformance statement to determine the status of certain runtime options.
     *
     * @throws Exception
     */
    @BeforeClass
    public void retrieveConfig() throws Exception {
        deleteSupported = isDeleteSupported();
        System.out.println("Delete operation supported?: " + Boolean.valueOf(deleteSupported).toString());
    }

    /**
     * Generate some change log activity by creating, updating and deleting
     * some resources.
     * @throws Exception
     */
    @Test
    public void populateResourcesForHistory() throws Exception {
        if (!deleteSupported) {
            logger.info("Delete not supported. Skipping system history test");
            return; // can't do anything useful with this test
        }

        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("patient-example-a.json");

        patient = patient.toBuilder().gender(AdministrativeGender.MALE).build();
        Entity<Patient> entity =
                Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);

        updateResource(responsePatient);                    // 1
        updateResource(responsePatient);                    // 2
        deleteResource("Patient", responsePatient.getId()); // 3
        undeleteResource(responsePatient);                  // 4
        updateResource(responsePatient);                    // 5
    }


    @Test(dependsOnMethods = {"populateResourcesForHistory"})
    public void testSystemHistory1() throws Exception {
        if (!deleteSupported) {
            return;
        }

        // Get the system history...which will be a bundle of changes. Without any
        // _since filter, we'll probably get back data which has been created by
        // other tests, so the only assertion we can make is we get back at least
        // the number of change records we expect.
        FHIRResponse response = client.history(null);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() >= 5);
    }

    /**
     * Update the resource
     * @param resourceToUpdate
     * @throws Exception
     */
    public void updateResource(Resource resourceToUpdate) throws Exception {
        FHIRResponse response = client.update(resourceToUpdate);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    /**
     * Undelete the resource - which means just do an update. Checks the response.
     * @param resourceToUndelete
     * @throws Exception
     */
    public void undeleteResource(Resource resourceToUndelete) throws Exception {
        if (!deleteSupported) {
            return;
        }

        // FHIR server responds with 201 CREATED
        FHIRResponse response = client.update(resourceToUndelete);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());
    }

    /**
     * Delete the resource identified by resourceTypeName/logicalId and check the response
     * @param resourceTypeName
     * @param logicalId
     * @throws Exception
     */
    public void deleteResource(String resourceTypeName, String logicalId) throws Exception {
        if (!deleteSupported) {
            return;
        }

        FHIRResponse response = client.delete(resourceTypeName, logicalId);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        assertNotNull(response.getETag());
    }
}