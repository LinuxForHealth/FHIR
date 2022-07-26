/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.large;

import static org.testng.AssertJUnit.assertNotNull;

import java.security.SecureRandom;
import java.util.Base64;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * This test checks LARGE resources are stored correctly
 * this test is opaque to any other persistence layer and the history is properly returned.
 */
public class LargeResourceTest extends FHIRServerTestBase {
    private Patient v1Patient;
    private Patient v2Patient;

    /**
     * Creates a small patient.
     */
    @Test(groups = { "large-resource" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);
        addToResourceRegistry("Patient", patientId);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        v1Patient = responsePatient;

        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    /**
     * Updates and ensure the large resource is returned correctly on the same version.
     */
    @Test(groups = { "large-resource" }, dependsOnMethods = { "testCreatePatient" })
    public void testUpdatePatient() throws Exception {
        WebTarget target = getWebTarget();

        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[10000000];
        random.nextBytes(bytes);
        String value = Base64.getEncoder().encodeToString(bytes);

        //@formatter:off
        patient =
                patient.toBuilder().id(v1Patient.getId())
                .extension(Extension.builder()
                    .url("http://ibm.com/fhir/large-large-large/testExtension")
                    .value(Base64Binary.builder().value(value).build()).build())
                .build();
        //@formatter:on

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);

        // Now call the 'update' API.
        String targetPath = "Patient/" + patient.getId();
        Response response = target.path(targetPath).request().put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        addToResourceRegistry("Patient", patient.getId());
        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient.getId()).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        v2Patient = responsePatient;

        TestUtil.assertResourceEquals(patient, v2Patient);
    }

    /**
     * make sure the large resource didn't replace the small resource (v1)
     */
    @Test(groups = { "large-resource" }, dependsOnMethods = { "testUpdatePatient" })
    public void testVreadPatient() {
        WebTarget target = getWebTarget();

        // Call the 'version read' API to retrieve the original created version of the
        // Patient.
        String targetPath = "Patient/" + v1Patient.getId() + "/_history/"
                + v1Patient.getMeta().getVersionId().getValue();
        Response response = target.path(targetPath).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patientToCheck = response.readEntity(Patient.class);
        assertNotNull(patientToCheck);
        TestUtil.assertResourceEquals(patientToCheck, v1Patient);
    }
}