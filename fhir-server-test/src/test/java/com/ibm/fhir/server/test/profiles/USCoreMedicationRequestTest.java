/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.server.test.SearchAllTest;

/**
 * Tests the US Core 3.1.1 Profile with MedicationRequest.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-medicationrequest.html
 */
public class USCoreMedicationRequestTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String medicationRequestId1 = null;
    private String medicationRequestId2 = null;
    private String medicationRequestId3 = "uscore-mo3";
    private String medicationId3 = "uscore-med2";
    private String medicationRequestId4 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList(
            "http://hl7.org/fhir/us/core/StructureDefinition/us-core-medicationrequest|3.1.1",
            "http://hl7.org/fhir/us/core/StructureDefinition/us-core-medication|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - MedicationRequest', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadBundle1();
            loadMedicationRequest1();
            loadMedicationRequest2();
            loadMedicationRequest4();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteBundleEntry1();
            deleteBundleEntry2();
            deleteMedicationRequest1();
            deleteMedicationRequest2();
            deleteMedicationRequest4();
        }
    }
    
    public void loadBundle1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Bundle-uscore-mo3.json";
        WebTarget target = getWebTarget();

        Bundle bundle = TestUtil.readExampleResource(resource);
        List<Bundle.Entry> entries = bundle.getEntry();
        List<Bundle.Entry> output = new ArrayList<>();
        for (Bundle.Entry entry : entries) {
            Request request = Request.builder()
                    .method(HTTPVerb.PUT)
                    .url(Uri.of(entry.getResource().getClass().getSimpleName() + "/" + entry.getResource().getId()))
                    .build();
            
            Meta meta = Meta.builder()
                    .versionId(Id.of("" + System.currentTimeMillis()))
                    .build();
            entry.getResource().toBuilder().meta(meta).build();
            
            Bundle.Entry tmpEntry = entry.toBuilder().request(request).build();
            output.add(tmpEntry);
        }
        bundle = bundle.toBuilder().type(BundleType.BATCH).entry(output).build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION).post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        String method = "loadBundle1";
        if (DEBUG) {
            Bundle responseBundle = getEntityWithExtraWork(response, method);
            SearchAllTest.generateOutput(responseBundle);
        }

        response = target.path("MedicationRequest/" + medicationRequestId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Medication/" + medicationId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadMedicationRequest1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/MedicationRequest-uscore-mo1.json";
        WebTarget target = getWebTarget();

        MedicationRequest goal = TestUtil.readExampleResource(resource);

        Entity<MedicationRequest> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("MedicationRequest").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/MedicationRequest/12354 (first actual test, but simple)
        medicationRequestId1 = getLocationLogicalId(response);
        response = target.path("MedicationRequest/" + medicationRequestId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteMedicationRequest1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("MedicationRequest/" + medicationRequestId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
    
    public void loadMedicationRequest2() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/MedicationRequest-uscore-mo2.json";
        WebTarget target = getWebTarget();

        MedicationRequest goal = TestUtil.readExampleResource(resource);

        Entity<MedicationRequest> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("MedicationRequest").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/MedicationRequest/12354 (first actual test, but simple)
        medicationRequestId2 = getLocationLogicalId(response);
        response = target.path("MedicationRequest/" + medicationRequestId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteMedicationRequest2() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("MedicationRequest/" + medicationRequestId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
    
    public void loadMedicationRequest4() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/MedicationRequest-self-tylenol.json";
        WebTarget target = getWebTarget();

        MedicationRequest goal = TestUtil.readExampleResource(resource);

        Entity<MedicationRequest> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("MedicationRequest").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/MedicationRequest/12354 (first actual test, but simple)
        medicationRequestId4 = getLocationLogicalId(response);
        response = target.path("MedicationRequest/" + medicationRequestId4).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
    
    public void deleteMedicationRequest4() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("MedicationRequest/" + medicationRequestId4).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }
    
    public void deleteBundleEntry1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("MedicationRequest/" + medicationRequestId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteBundleEntry2() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Medication/" + medicationId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchByPatientWithSingleIntent() throws Exception {
        // SHALL support searching using the combination of the patient and intent search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("intent", "order");
            FHIRResponse response = client.search(MedicationRequest.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, medicationRequestId1);
            assertContainsIds(bundle, medicationRequestId2);
            assertContainsIds(bundle, medicationRequestId3);
            assertDoesNotContainsIds(bundle, medicationRequestId4);
        }
    }
    
    @Test
    public void testSearchByPatientWithMultipleIntents() throws Exception {
        // SHALL support searching using the combination of the patient and intent search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("intent", "order,plan");
            FHIRResponse response = client.search(MedicationRequest.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, medicationRequestId1);
            assertContainsIds(bundle, medicationRequestId2);
            assertContainsIds(bundle, medicationRequestId3);
            assertContainsIds(bundle, medicationRequestId4);
        }
    }
    
    @Test
    public void testSearchByPatientWithSingleIntentAndStatus() throws Exception {
        // SHALL support searching using the combination of the patient and intent and status search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan&status={system|}[code]{,{system|}[code],...}
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("intent", "order");
            parameters.searchParam("status", "active");
            FHIRResponse response = client.search(MedicationRequest.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, medicationRequestId1);
            assertContainsIds(bundle, medicationRequestId2);
            assertContainsIds(bundle, medicationRequestId3);
            assertDoesNotContainsIds(bundle, medicationRequestId4);
        }
    }
    
    @Test
    public void testSearchByPatientWithSingleIntentAndEncounter() throws Exception {
        // SHOULD support searching using the combination of the patient and intent and encounter search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan&encounter=[reference]
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("intent", "plan");
            parameters.searchParam("encounter", "Encounter/example-1");
            FHIRResponse response = client.search(MedicationRequest.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertDoesNotContainsIds(bundle, medicationRequestId1);
            assertDoesNotContainsIds(bundle, medicationRequestId2);
            assertDoesNotContainsIds(bundle, medicationRequestId3);
            assertContainsIds(bundle, medicationRequestId4);
        }
    }
    
    @Test
    public void testSearchByPatientWithSingleIntentWithInclude() throws Exception {
        // Tests the include
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("intent", "order,plan");
            parameters.searchParam("_include", "MedicationRequest:medication");
            FHIRResponse response = client.search(MedicationRequest.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, medicationRequestId1);
            assertContainsIds(bundle, medicationRequestId2);
            assertContainsIds(bundle, medicationRequestId3);
            assertContainsIds(bundle, medicationRequestId4);
            assertContainsIds(bundle, medicationId3);
        }
    }
    
    @Test
    public void testSearchByPatientWithSingleIntentWithIncludeAndAuthoredOn() throws Exception {
        // SHOULD support searching using the combination of the patient and intent and authoredon search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // including support for these authoredon comparators: gt,lt,ge,le
        // including optional support for composite AND search on authoredon (e.g.authoredon=[date]&authoredon=[date]]&...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan&authoredon={gt|lt|ge|le}[date]{&authoredon={gt|lt|ge|le}[date]&...}
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("intent", "order,plan");
            parameters.searchParam("authoredon", "2019-06-24");
            parameters.searchParam("_include", "MedicationRequest:medication");
            FHIRResponse response = client.search(MedicationRequest.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertDoesNotContainsIds(bundle, medicationRequestId1);
            assertDoesNotContainsIds(bundle, medicationRequestId2);
            assertDoesNotContainsIds(bundle, medicationRequestId3);
            assertContainsIds(bundle, medicationRequestId4);
            assertDoesNotContainsIds(bundle, medicationId3);
        }
    }
}