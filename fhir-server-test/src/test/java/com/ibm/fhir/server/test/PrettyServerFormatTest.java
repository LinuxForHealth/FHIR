/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Basic Test of the _pretty
 */
public class PrettyServerFormatTest extends FHIRServerTestBase {

    /**
     * Create a minimal Patient, then make sure we can retrieve it with varying
     * format
     */
    @Test(groups = { "server-pretty" })
    public void testPrettyFormatting() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response =
                target.queryParam("_pretty", "true").queryParam("_format", "application/fhir+json").path("Patient/" + patientId)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        String prettyOutput = response.readEntity(String.class);

        response =
                target.queryParam("_pretty", "false").queryParam("_format", "application/fhir+json").path("Patient/" + patientId)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();

        String notPrettyOutput = response.readEntity(String.class);

        assertNotEquals(prettyOutput, notPrettyOutput);
        assertFalse(notPrettyOutput.contains("\n"));
        assertTrue(prettyOutput.contains("\n"));
    }
}