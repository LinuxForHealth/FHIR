/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.server.test;

import static com.ibm.watson.health.fhir.model.type.Code.code;
import static com.ibm.watson.health.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watson.health.fhir.client.FHIRParameters;
import com.ibm.watson.health.fhir.client.FHIRResponse;
import com.ibm.watson.health.fhir.core.FHIRMediaType;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.generator.FHIRGenerator;
import com.ibm.watson.health.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.watson.health.fhir.model.resource.Bundle;
import com.ibm.watson.health.fhir.model.resource.Patient;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.Canonical;
import com.ibm.watson.health.fhir.model.type.Coding;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.Meta;

public class SearchAllTest extends FHIRServerTestBase {

    private static final boolean DEBUG_SEARCH = false;

    private String patientId;
    private Instant lastUpdated;

    @Test(groups = { "server-search-all" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        Coding security = Coding.builder().system(uri("http://ibm.com/watsonhealth/fhir/security")).code(code("security")).build();
        Coding tag = Coding.builder().system(uri("http://ibm.com/watsonhealth/fhir/tag")).code(code("tag")).build();

        patient =
                patient.toBuilder().meta(Meta.builder().security(security).tag(tag).profile(Canonical.of("http://ibm.com/watsonhealth/fhir/profile/Profile")).build()).build();

        if (DEBUG_SEARCH) {
            generateOutput(patient);
        }

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);

        lastUpdated = responsePatient.getMeta().getLastUpdated();
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingId() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdated() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_lastUpdated", lastUpdated.getValue().toString());
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingTag() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "tag");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        /*
         * "expression" : "Resource.meta.tag", <br/> "xpath" : "f:Resource/f:meta/f:tag",
         */
        FHIRGenerator.generator(Format.JSON, true).generate(bundle, System.out);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSecurity() throws Exception {
        // <expression value="Resource.meta.security"/>
        // <xpath value="f:Resource/f:meta/f:security"/>

        FHIRParameters parameters = new FHIRParameters();

        // Original - "http://ibm.com/watsonhealth/fhir/security|security"
        parameters.searchParam("_security", "security");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);

        assertNotNull(bundle);
        if(DEBUG_SEARCH) {
            generateOutput(bundle);
        }
        

        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingProfile() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_profile", "http://ibm.com/watsonhealth/fhir/profile/Profile");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    /*
     * generates the output into a resource.
     */
    public static void generateOutput(Resource resource) {

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(resource, System.out);
            System.out.println(writer.toString());
        } catch (FHIRGeneratorException e) {

            e.printStackTrace();
            fail("unable to generate the fhir resource to JSON");

        } catch (IOException e1) {
            e1.printStackTrace();
            fail("unable to generate the fhir resource to JSON (io problem) ");
        }

    }
}
