/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Uri;

public class SearchExtensionsTest extends FHIRServerTestBase {
    private static final String EXTENSION_BASE_URL = FHIRConstants.EXT_BASE + "Patient/";

    private static final boolean DEBUG_SEARCH = false;

    private Patient savedCreatedPatientWithExtensions;

    @Test(groups = { "server-search" })
    public void testCreatePatientWithExtensions() throws Exception {
        WebTarget target = getWebTarget();

        Patient patient = TestUtil.readLocalResource("Patient_SearchExtensions.json");
        patient = patient.toBuilder()
                .extension(Extension.builder()
                        .url(EXTENSION_BASE_URL + "favorite-color")
                        .value(string("blue"))
                        .build())
                .extension(Extension.builder()
                        .url(EXTENSION_BASE_URL + "favorite-number")
                        .value(Integer.of(5))
                        .build())
                .extension(Extension.builder()
                        .url(EXTENSION_BASE_URL + "favorite-code")
                        .value(CodeableConcept.builder()
                            .coding(Coding.builder().code(Code.of("someCode-1234")).system(uri("http://ibm.com/fhir/system")).build())
                            .build())
                        .build())
                .extension(Extension.builder()
                        .url(EXTENSION_BASE_URL + "favorite-uri")
                        .value(uri("http://www.ibm.com"))
                        .build())
                .extension(Extension.builder()
                        .url(EXTENSION_BASE_URL + "favorite-quantity")
                        .value(Quantity.builder().unit(string("lbs")).value(Decimal.of(180)).build())
                        .build())
                .extension(Extension.builder()
                        .url(EXTENSION_BASE_URL + "favorite-date")
                        .value(Date.of("2018-10-25"))
                        .build())
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        savedCreatedPatientWithExtensions = responsePatient;
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatientWithExtensions" })
    public void testSearchPatientWithExtensions() {
        WebTarget target = getWebTarget();

        String favoriteColor = savedCreatedPatientWithExtensions.getExtension().get(0).getValue()
                .as(com.ibm.fhir.model.type.String.class).getValue();
        Response response = target.path("Patient")
                .queryParam("favorite-color", favoriteColor)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        printOutResource(DEBUG_SEARCH, bundle);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatientWithExtensions" })
    public void testSearchPatientWithExtensionsNotFound() throws FHIRGeneratorException {
        WebTarget target = getWebTarget();

        // "red"
        Response response = target.path("Patient")
                .queryParam("favorite-color", "red")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        printOutResource(DEBUG_SEARCH, bundle);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatientWithExtensions" })
    public void testSearchPatientWithUnknownExtension() {
        WebTarget target = getWebTarget();

        // in lenient mode, an unknown parameter should be ignored
        Response response = target.path("Patient")
                .queryParam("fake-parameter", "fakeValue")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("Prefer", "handling=lenient")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);

        // in strict mode, an unknown parameter should result in a 400 Bad Request
        response = target.path("Patient")
                .queryParam("fake-parameter", "fakeValue")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("Prefer", "handling=strict")
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);

        // multiple headers should be handled appropriately
        response = target.path("Patient")
                .queryParam("fake-parameter", "fakeValue")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("Prefer", "fakeName=fakeValue")
                .header("Prefer", "handling=strict")
                .header("Prefer", "fakeName2=fakeValue2")
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);

        // multiple parts to the Prefer header should be handled
        response = target.path("Patient")
                .queryParam("fake-parameter", "fakeValue")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("Prefer", "fakeName=fakeValue;handling=strict;fakeName2=fakeValue2")
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        oo = response.readEntity(OperationOutcome.class);
        assertNotNull(oo);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatientWithExtensions" })
    public void testSearchPatientWithBaseParametersAndExtensions() {
        WebTarget target = getWebTarget();
        String family = savedCreatedPatientWithExtensions.getName().get(0).getFamily().getValue();
        String favoriteColor =
                ((com.ibm.fhir.model.type.String) savedCreatedPatientWithExtensions.getExtension().get(0).getValue()).getValue();
        Integer favoriteNumber =
                (Integer) savedCreatedPatientWithExtensions.getExtension().get(1).getValue();
        Coding favoriteCode =
                ((CodeableConcept) savedCreatedPatientWithExtensions.getExtension().get(2).getValue()).getCoding().get(0);
        Uri favoriteUri = (Uri) savedCreatedPatientWithExtensions.getExtension().get(3).getValue();
        Quantity favoriteQuantity =
                (Quantity) savedCreatedPatientWithExtensions.getExtension().get(4).getValue();
        Date favoriteDate =
                (Date) savedCreatedPatientWithExtensions.getExtension().get(5).getValue();

        /*
         * Previously the favorite-code favorite-code=http://ibm.com/fhir/system%7CsomeCode-1234 using <code>
         * favoriteCode.getSystem().getValue()+ "|"</code>
         */

        Response response = target.path("Patient")
                .queryParam("family", family)
                .queryParam("favorite-color", favoriteColor)
                .queryParam("favorite-number", favoriteNumber.getValue())
                .queryParam("favorite-code", favoriteCode.getCode().getValue())
                .queryParam("favorite-uri", favoriteUri.getValue())
                .queryParam("favorite-quantity", favoriteQuantity.getValue().getValue().toString()
                        + "||" + favoriteQuantity.getUnit().getValue())
                .queryParam("favorite-date", favoriteDate.getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);

        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        // Testing the behavior specific to the URI patterns
        // Response should be empty as the URI value is not an exact match
        response = target.path("Patient")
                .queryParam("favorite-uri", favoriteUri.getValue().substring(0,10))
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);

        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() == 0);

        // Response should be empty as the URI value is not an exact match
        response = target.path("Patient")
                .queryParam("favorite-uri", favoriteUri.getValue())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);

        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() > 0);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatientWithExtensions" })
    public void testSearchPatientWithBaseParametersAndExtensionsWithAbove() {
        WebTarget target = getWebTarget();
        Uri favoriteUri = (Uri) savedCreatedPatientWithExtensions.getExtension().get(3).getValue();

        Response response = target.path("Patient")
                .queryParam("favorite-uri:above", favoriteUri.getValue() + "/12345/12345")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() > 0);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatientWithExtensions" })
    public void testSearchPatientWithBaseParametersAndExtensionsWithBelow() {
        WebTarget target = getWebTarget();

        Response response = target.path("Patient")
                .queryParam("favorite-uri:below", "http://www.ibm.co")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() == 0);
    }
}