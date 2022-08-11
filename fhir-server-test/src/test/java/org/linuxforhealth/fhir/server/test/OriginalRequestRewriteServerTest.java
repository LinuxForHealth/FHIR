/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.Code.code;
import static org.linuxforhealth.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Meta;

/**
 * Basic sniff test of the FHIR Server.
 */
public class OriginalRequestRewriteServerTest extends FHIRServerTestBase {
    private static final String TAG = "OriginalRequestRewriteServerTest";
    private static final String ORIGINAL_URI = "https://frontend-url/base";

    /**
     * Create a Patient and ensure the Location header reflects the OriginalRequestUri
     */
    @Test
    public void testCreatePatient_LocationRewrite() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Meta.Builder meta = patient.getMeta() == null ? Meta.builder() : patient.getMeta().toBuilder();
        meta.tag(Coding.builder().system(uri("http://example.com/fhir/tag")).code(code(TAG)).build());
        patient = patient.toBuilder()
                .meta(meta.build())
                .build();
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request()
                .header("X-FHIR-Forwarded-URL", ORIGINAL_URI + "/Patient")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        assertTrue(response.getLocation().toString().startsWith(ORIGINAL_URI),
                response.getLocation().toString() + " should begin with " + ORIGINAL_URI);
    }

    /**
     * Search for the Patient and ensure that both the search links and the search response bundle entries
     * reflect the OriginalRequestUri
     */
    @Test(dependsOnMethods = "testCreatePatient_LocationRewrite")
    public void testPatientSearch_BaseUrlRewrite() throws Exception {
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").queryParam("_tag", TAG).request()
                .header("X-FHIR-Forwarded-URL", ORIGINAL_URI + "/Patient?_tag=" + TAG)
                .get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = response.readEntity(Bundle.class);
        assertFalse(responseBundle.getLink().isEmpty());
        assertTrue(responseBundle.getLink().stream().allMatch(l -> l.getUrl().getValue().startsWith(ORIGINAL_URI)),
                "All search response bundle links start with the passed in uri");
        assertFalse(responseBundle.getEntry().isEmpty());
        assertTrue(responseBundle.getEntry().stream().allMatch(e -> e.getFullUrl().getValue().startsWith(ORIGINAL_URI)),
                "All search response bundle entry fullUrls start with the passed in uri");
    }

    /**
     * Search for the Patient via a whole-system search and ensure that both the search links
     * and the search response bundle entries reflect the OriginalRequestUri
     */
    @Test(dependsOnMethods = "testCreatePatient_LocationRewrite")
    public void testWholeSystemSearch_BaseUrlRewrite() throws Exception {
        WebTarget target = getWebTarget();

        Response response = target.queryParam("_tag", TAG).request()
                .header("X-FHIR-Forwarded-URL", ORIGINAL_URI + "?_tag=" + TAG)
                .get(Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = response.readEntity(Bundle.class);
        assertFalse(responseBundle.getLink().isEmpty());
        assertTrue(responseBundle.getLink().stream().allMatch(l -> l.getUrl().getValue().startsWith(ORIGINAL_URI)),
                "All search response bundle links start with the passed in uri");
        assertFalse(responseBundle.getEntry().isEmpty());
        assertTrue(responseBundle.getEntry().stream().allMatch(e -> e.getFullUrl().getValue().startsWith(ORIGINAL_URI + "/Patient")),
                "All search response bundle entry fullUrls start with the passed in uri");
    }

    /**
     * Create a Patient and ensure the Location header reflects the OriginalRequestUri
     */
    @Test
    public void testCreatePatientWithCustomBaseUrls() throws Exception {
        WebTarget target = getWebTarget();
        String customBaseUrl;
        Patient patient = TestUtil.getMinimalResource(Patient.class);
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response;

        // Build a new Patient and then call the 'create' API.
        customBaseUrl = "https://example.com";
        response = target.path("Patient").request()
                .header("X-FHIR-Forwarded-URL", customBaseUrl + "/Patient")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        assertTrue(response.getLocation().toString().startsWith(customBaseUrl),
                response.getLocation().toString() + " should begin with " + customBaseUrl);

        customBaseUrl = "https://example.com/my/patient/api";
        response = target.path("Patient").request()
                .header("X-FHIR-Forwarded-URL", customBaseUrl + "/Patient")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        assertTrue(response.getLocation().toString().startsWith(customBaseUrl),
                response.getLocation().toString() + " should begin with " + customBaseUrl);

        customBaseUrl = "https://example.com/my/Patient/api";
        response = target.path("Patient").request()
                .header("X-FHIR-Forwarded-URL", customBaseUrl + "/Patient")
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        assertFalse(response.getLocation().toString().startsWith(customBaseUrl),
                response.getLocation().toString() + " doesn't begin with " + customBaseUrl
                + " because of a documented limitation");
    }
}
