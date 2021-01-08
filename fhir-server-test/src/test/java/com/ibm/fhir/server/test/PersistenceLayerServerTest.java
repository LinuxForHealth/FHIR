/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Check the outcome of the server makes sense
 * <pre>{
    "resourceType": "OperationOutcome",
    "id": "c0-a8-56-16-d3b6fc1c-33b9-4b71-9296-e19452064973",
    "issue": [
        {
            "severity": "fatal",
            "code": "exception",
            "details": {
                "text": "FHIRPersistenceException: Unexpected exception while creating JDBC persistence layer: 'createCache failed dummy~default'"
            }
        }
    ]
}</pre>
 */
public class PersistenceLayerServerTest extends FHIRServerTestBase {

    @Test(groups = { "server-persistence-layer-tests" })
    public void testPersistenceLayerResponse() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().header("X-FHIR-TENANT-ID", "IAMNOTAREALTENANT").post(entity, Response.class);
        assertResponse(response, Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        OperationOutcome responsePatient = response.readEntity(OperationOutcome.class);
        assertEquals(responsePatient.getIssue().get(0).getDetails().getText().getValue(),
            "FHIRPersistenceException: Unexpected exception while creating JDBC persistence layer: 'createCache failed IAMNOTAREALTENANT~default'");
    }
}
