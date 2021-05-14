/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Uri;

public class ConditionalReferenceTest extends FHIRServerTestBase {
    @Test
    public void testCreatePatient() {
        Patient patient = buildPatient();

        WebTarget target = getWebTarget();

        Response response = target.path("Patient").path("12345")
                .request()
                .put(Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePatient" })
    public void testBundleTransaction() throws Exception {
        Bundle bundle = TestUtil.readLocalResource("testdata/conditional-reference-bundle.json");

        WebTarget target = getWebTarget();

        Response response = target.request()
                .post(Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON));

        int status = response.getStatus();

        assertTrue(status == Response.Status.OK.getStatusCode());
    }

    private Patient buildPatient() {
        return Patient.builder()
                .id("12345")
                .identifier(Identifier.builder()
                    .system(Uri.of("http://ibm.com/fhir/patient-id"))
                    .value(string("12345"))
                    .build())
                .name(HumanName.builder()
                    .family(string("Doe"))
                    .given(string("John"))
                    .build())
                .build();
    }
}
