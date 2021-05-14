/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collections;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueType;

public class ConditionalReferenceTest extends FHIRServerTestBase {
    @Test
    public void testCreatePatients() {
        Patient patient = buildPatient();

        WebTarget target = getWebTarget();

        Response response = target.path("Patient").path("12345")
                .request()
                .put(Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode());

        patient = patient.toBuilder()
                .id("54321")
                .identifier(Collections.singletonList(Identifier.builder()
                    .system(Uri.of("http://ibm.com/fhir/patient-id"))
                    .value(string("54321"))
                    .build()))
                .build();

        response = target.path("Patient").path("54321")
                .request()
                .put(Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON));
        status = response.getStatus();
        assertTrue(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode());
    }

    @Test(dependsOnMethods = { "testCreatePatients" })
    public void testBundleTransactionConditionalReference() throws Exception {
        Bundle bundle = TestUtil.readLocalResource("testdata/conditional-reference-bundle.json");

        WebTarget target = getWebTarget();

        Response response = target.request()
                .post(Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.OK.getStatusCode());

        response = target.path("Observation/67890").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        status = response.getStatus();
        assertTrue(status == Response.Status.OK.getStatusCode());

        Observation observation = response.readEntity(Observation.class);
        assertEquals(observation.getSubject().getReference().getValue(), "Patient/12345");
    }

    @Test(dependsOnMethods = { "testCreatePatients" })
    public void testBundleTransactionInvalidConditionalReferenceNoQueryParameters() throws Exception {
        Bundle bundle = TestUtil.readLocalResource("testdata/conditional-reference-bundle.json");

        Entry entry = bundle.getEntry().get(0);
        entry = entry.toBuilder()
                .resource(entry.getResource().as(Observation.class).toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Patient?"))
                        .build())
                    .build())
                .build();

        bundle = bundle.toBuilder()
                .entry(Collections.singletonList(entry))
                .build();

        WebTarget target = getWebTarget();

        Response response = target.request()
                .post(Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertEquals(outcome.getIssue().get(0).getDetails().getText().getValue(), "Invalid conditional reference: no query parameters found");
    }

    @Test(dependsOnMethods = { "testCreatePatients" })
    public void testBundleTransactionInvalidConditionalReferenceResultParameter() throws Exception {
        Bundle bundle = TestUtil.readLocalResource("testdata/conditional-reference-bundle.json");

        Entry entry = bundle.getEntry().get(0);
        entry = entry.toBuilder()
                .resource(entry.getResource().as(Observation.class).toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Patient?_count=1"))
                        .build())
                    .build())
                .build();

        bundle = bundle.toBuilder()
                .entry(Collections.singletonList(entry))
                .build();

        WebTarget target = getWebTarget();

        Response response = target.request()
                .post(Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertEquals(outcome.getIssue().get(0).getDetails().getText().getValue(), "Invalid conditional reference: only filtering parameters are allowed");
    }

    @Test(dependsOnMethods = { "testCreatePatients" })
    public void testBundleTransactionConditionalReferenceNoResult() throws Exception {
        Bundle bundle = TestUtil.readLocalResource("testdata/conditional-reference-bundle.json");

        Entry entry = bundle.getEntry().get(0);
        entry = entry.toBuilder()
                .resource(entry.getResource().as(Observation.class).toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Patient?identifier=___invalid___"))
                        .build())
                    .build())
                .build();

        bundle = bundle.toBuilder()
                .entry(Collections.singletonList(entry))
                .build();

        WebTarget target = getWebTarget();

        Response response = target.request()
                .post(Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.NOT_FOUND);
        assertEquals(outcome.getIssue().get(0).getDetails().getText().getValue(), "Error resolving conditional reference: search must return exactly one result");
    }

    @Test(dependsOnMethods = { "testCreatePatients" })
    public void testBundleTransactionConditionalReferenceMultipleMatches() throws Exception {
        Bundle bundle = TestUtil.readLocalResource("testdata/conditional-reference-bundle.json");

        Entry entry = bundle.getEntry().get(0);
        entry = entry.toBuilder()
                .resource(entry.getResource().as(Observation.class).toBuilder()
                    .subject(Reference.builder()
                        .reference(string("Patient?family=Doe&given=John"))
                        .build())
                    .build())
                .build();

        bundle = bundle.toBuilder()
                .entry(Collections.singletonList(entry))
                .build();

        WebTarget target = getWebTarget();

        Response response = target.request()
                .post(Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON));
        int status = response.getStatus();
        assertTrue(status == Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertEquals(outcome.getIssue().get(0).getCode(), IssueType.MULTIPLE_MATCHES);
        assertEquals(outcome.getIssue().get(0).getDetails().getText().getValue(), "Error resolving conditional reference: search must return exactly one result");
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
