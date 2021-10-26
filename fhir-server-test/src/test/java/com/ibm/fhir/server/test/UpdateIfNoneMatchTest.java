/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.UUID;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Test conditional create-on-update using If-None-Match
 */
public class UpdateIfNoneMatchTest extends FHIRServerTestBase {
    private static final String HEADERNAME_IF_NONE_MATCH = "If-None-Match";
    private static final String HEADERNAME_PREFER = "Prefer";

    @Test
    public void testCreateOnUpdate() throws Exception {

        if (this.isUpdateCreateSupported()) {

            // Read a JSON Patient and set the id.
            String patientLogicalId = UUID.randomUUID().toString();
            Patient patient = TestUtil.readLocalResource("Patient_SalMonella.json");
            patient = patient.toBuilder().id(patientLogicalId).build();
            
            FHIRResponse response = client.update(patient);
            int status = response.getStatus();
            assertEquals(status, 201);
            assertEquals(response.getETag(), "W/\"1\"");

            // Now try an update with If-None-Match defined
            final FHIRRequestHeader ifNoneMatch = new FHIRRequestHeader(HEADERNAME_IF_NONE_MATCH, "*");
            response = client.update(patient, ifNoneMatch);
            status = response.getStatus();
            assertEquals(status, 304);
            assertEquals(response.getETag(), "W/\"1\""); // not updated

            // Read back the patient and make sure it is still at version 1
            response = client.read(Patient.class.getSimpleName(), patientLogicalId);
            // Search on the patient id, and see how many results we get. There should only
            // be one.
            response = client.read("Patient", patientLogicalId);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

            patient =  response.getResource(Patient.class);
            assertEquals(patient.getId(), patientLogicalId);
            assertEquals(patient.getMeta().getVersionId().getValue(), "1");
        }
    }



    /**
     * Perform a Patient create-on-update with If-None-Match and check
     * the OperationOutcome matches the request.
     * @throws Exception
     */
    @Test
    public void testCreateOnUpdateAfterDelete() throws Exception {

        if (this.isUpdateCreateSupported()) {

            // Read a JSON Patient and set the id.
            String patientLogicalId = UUID.randomUUID().toString();
            Patient patient = TestUtil.readLocalResource("Patient_SalMonella.json");
            patient = patient.toBuilder().id(patientLogicalId).build();
            
            FHIRResponse response = client.update(patient);
            int status = response.getStatus();
            assertEquals(status, 201);
            assertEquals(response.getETag(), "W/\"1\"");
            
            // Delete the resource. The deletion marker will have version 2
            response = client.delete(Patient.class.getSimpleName(), patientLogicalId);
            assertEquals(response.getStatus(), 200);
            
            // Now try an update with If-None-Match defined resulting in version 3
            final FHIRRequestHeader ifNoneMatch = new FHIRRequestHeader(HEADERNAME_IF_NONE_MATCH, "*");
            response = client.update(patient, ifNoneMatch);
            status = response.getStatus();
            assertEquals(status, 201); // Undeleted the resource, treated as Created
            assertEquals(response.getETag(), "W/\"3\"");
        }
    }
}