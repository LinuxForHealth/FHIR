/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.code.AdministrativeGender;
import org.linuxforhealth.fhir.model.util.FHIRUtil;

public class VReadTest extends FHIRServerTestBase {
    private String patientId;
    private String patientVersion;
    private Coding subsettedTag =
            Coding.builder().system(uri("http://terminology.hl7.org/CodeSystem/v3-ObservationValue"))
            .code(Code.of("SUBSETTED"))
            .display(string("subsetted"))
            .build();

    @Test(groups = { "server-vread" })
    public void testCreatePatient() throws Exception {
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

        // Get the patient's logical id and version id.
        patientId = getLocationLogicalId(response);
        patientVersion = getLocationVersionId(response);
        addToResourceRegistry("Patient", patientId);

        // Next, call the 'vread' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId + "/_history/" + patientVersion)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-vread" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_elements() throws Exception {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId + "/_history/" + patientVersion)
                .queryParam("_elements", "active")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        checkSubsetted(patient, Collections.singletonList("getActive"));
    }

    @Test(groups = { "server-vread" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_summary() throws Exception {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId + "/_history/" + patientVersion)
                .queryParam("_summary", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        checkSubsetted(patient, Arrays.asList("getActive", "getBirthDate", "getIdentifier", "getGender",
            "getLink", "getManagingOrganization", "getName", "getTelecom"));
    }

    @Test(groups = { "server-vread" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_elements_summary() throws Exception {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId + "/_history/" + patientVersion)
                .queryParam("_elements", "active")
                .queryParam("_summary", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient patient = response.readEntity(Patient.class);
        assertNotNull(patient);
        checkSubsetted(patient, Collections.singletonList("getActive"));
    }

    @Test(groups = { "server-vread" }, dependsOnMethods = {"testCreatePatient" })
    public void testReadPatient_nonGeneralSearchParameter() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient/" + patientId + "/_history/" + patientVersion)
                .queryParam("_lastUpdated", "ge2020-11-02")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
            "Search parameter '_lastUpdated' is not supported by vread.");
    }

    /**
     * Checks that Patient has the subsetted tag and only the requested elements
     * (and "mandatory elements")  are present in the Patient.
     * @param patient the patient
     * @param methods the methods
     * @throws Exception an exception
     */
    private void checkSubsetted(Patient patient, List<String> methods) throws Exception {
        assertTrue(FHIRUtil.hasTag(patient, subsettedTag));
        // Verify that only the requested elements (and "mandatory elements") are
        // present in the returned Patient.
        Method[] patientMethods = Patient.class.getDeclaredMethods();
        for (int i = 0; i < patientMethods.length; i++) {
            Method patientMethod = patientMethods[i];
            if (patientMethod.getName().startsWith("get")) {
                Object elementValue = patientMethod.invoke(patient);
                if (methods.contains(patientMethod.getName())
                        || patientMethod.getName().equals("getId")
                        || patientMethod.getName().equals("getMeta")) {
                    assertNotNull(elementValue);
                } else {
                    if (elementValue instanceof List) {
                        assertEquals(0, ((List<?>) elementValue).size());
                    } else {
                        assertNull(elementValue);
                    }
                }
            }
        }
    }

}
