/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.test.TestUtil.isResourceInResponse;
import static com.ibm.fhir.model.type.Code.code;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;

public class SearchAllTest extends FHIRServerTestBase {

    private static final boolean DEBUG_SEARCH = false;

    private String patientId;
    private Instant lastUpdated;
    private Patient patient4DuplicationTest = null;

    @Test(groups = { "server-search-all" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient with 2 tags and one duplicated tag and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Coding security = Coding.builder().system(uri("http://ibm.com/fhir/security")).code(code("security")).build();
        Coding tag = Coding.builder().system(uri("http://ibm.com/fhir/tag")).code(code("tag")).build();
        Coding tag2 = Coding.builder().system(uri("http://ibm.com/fhir/tag")).code(code("tag2")).build();

        patient = patient.toBuilder()
                .meta(Meta.builder()
                        .security(security)
                        .tag(tag)
                        .tag(tag)
                        .tag(tag2)
                        .profile(Canonical.of("http://ibm.com/fhir/profile/Profile"))
                        .build())
                .build();

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
        patient4DuplicationTest = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, patient4DuplicationTest);

        lastUpdated = patient4DuplicationTest.getMeta().getLastUpdated();
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

        // Original - "http://ibm.com/fhir/security|security"
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
        parameters.searchParam("_profile", "http://ibm.com/fhir/profile/Profile");
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
    
    @Test(groups = { "server-search-all"}, dependsOnMethods = {
    "testCreatePatient" })
    public void testSearchAllUsing2TagsAndNoExistingTag() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        // tag88 doesn't exist, this case is created according to a reported test failure.
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|tag88,tag2,tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patient4DuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is 
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();  
            for (Entry entry: bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId().getValue());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }
    
    
    @Test(groups = { "server-search-all"}, dependsOnMethods = {
    "testCreatePatient" })
    public void testSearchAllUsing2Tags() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|tag2,tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patient4DuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is 
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();  
            for (Entry entry: bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId().getValue());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }
    
    @Test(groups = { "server-search-all"}, dependsOnMethods = {
    "testCreatePatient" })
    public void testSearchAllUsing2FullTags() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|tag2,http://ibm.com/fhir/tag|tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patient4DuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is 
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();  
            for (Entry entry: bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId().getValue());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }

    @Test(groups = { "server-search-all"}, dependsOnMethods = {
    "testCreatePatient" })
    public void testSearchAllUsingOneTag() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patient4DuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is 
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();  
            for (Entry entry: bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId().getValue());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }
}
