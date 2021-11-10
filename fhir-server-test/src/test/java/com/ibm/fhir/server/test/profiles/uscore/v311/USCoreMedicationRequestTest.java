/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.ig.us.core.tool.USCoreExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with MedicationRequest.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-medicationrequest.html
 */
public class USCoreMedicationRequestTest extends ProfilesTestBaseV2 {

    private String medicationRequestId1 = null;
    private String medicationRequestId2 = null;
    private String medicationRequestId3 = "uscore-mo3";
    private String medicationId3 = "uscore-med2";
    private String medicationRequestId4 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-medicationrequest|3.1.1", "http://hl7.org/fhir/us/core/StructureDefinition/us-core-medication|3.1.1");
    }

    @Override
    public void loadResources() throws Exception {
        loadBundle1();
        loadMedicationRequest1();
        loadMedicationRequest2();
        loadMedicationRequest4();
    }

    public void loadBundle1() throws Exception {
        String resource = "Bundle-uscore-mo3.json";
        WebTarget target = getWebTarget();

        // re-purposing search result bundle - update fields to create valid request bundle
        Bundle bundle = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        List<Bundle.Entry> entries = bundle.getEntry();
        List<Bundle.Entry> output = new ArrayList<>();
        for (Bundle.Entry entry : entries) {
            Request request = Request.builder().method(HTTPVerb.PUT).url(Uri.of(entry.getResource().getClass().getSimpleName() + "/"
                    + entry.getResource().getId())).build();

            Meta meta = Meta.builder().versionId(Id.of("" + System.currentTimeMillis())).build();
            entry.getResource().toBuilder().meta(meta).build();

            Bundle.Entry tmpEntry = entry.toBuilder().request(request).search(null).build();
            output.add(tmpEntry);
        }
        bundle = bundle.toBuilder().type(BundleType.BATCH).entry(output).total(null).build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION).post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("MedicationRequest/" + medicationRequestId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Medication/" + medicationId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadMedicationRequest1() throws Exception {
        String resource = "MedicationRequest-uscore-mo1.json";
        String cls = "MedicationRequest";
        medicationRequestId1 = buildAndAssertOnResourceForUsCore(cls, "311", resource);
    }

    public void loadMedicationRequest2() throws Exception {
        String resource = "MedicationRequest-uscore-mo2.json";
        String cls = "MedicationRequest";
        medicationRequestId2 = buildAndAssertOnResourceForUsCore(cls, "311", resource);
    }

    public void loadMedicationRequest4() throws Exception {
        String resource = "MedicationRequest-self-tylenol.json";
        String cls = "MedicationRequest";
        medicationRequestId4 = buildAndAssertOnResourceForUsCore(cls, "311", resource);
    }

    @Test
    public void testSearchByPatientWithSingleIntent() throws Exception {
        // SHALL support searching using the combination of the patient and intent search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan
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

    @Test
    public void testSearchByPatientWithMultipleIntents() throws Exception {
        // SHALL support searching using the combination of the patient and intent search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan
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

    @Test
    public void testSearchByPatientWithSingleIntentAndStatus() throws Exception {
        // SHALL support searching using the combination of the patient and intent and status search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET
        // [base]/MedicationRequest?patient=[reference]&intent=order,plan&status={system|}[code]{,{system|}[code],...}
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

    @Test
    public void testSearchByPatientWithSingleIntentAndEncounter() throws Exception {
        // SHOULD support searching using the combination of the patient and intent and encounter search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // GET [base]/MedicationRequest?patient=[reference]&intent=order,plan&encounter=[reference]
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

    @Test
    public void testSearchByPatientWithSingleIntentWithInclude() throws Exception {
        // Tests the include
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

    @Test
    public void testSearchByPatientWithSingleIntentWithIncludeAndAuthoredOn() throws Exception {
        // SHOULD support searching using the combination of the patient and intent and authoredon search parameters:
        // including support for composite OR search on intent (e.g.intent={system|}[code],{system|}[code],...)
        // including support for these authoredon comparators: gt,lt,ge,le
        // including optional support for composite AND search on authoredon
        // (e.g.authoredon=[date]&authoredon=[date]]&...)
        // GET
        // [base]/MedicationRequest?patient=[reference]&intent=order,plan&authoredon={gt|lt|ge|le}[date]{&authoredon={gt|lt|ge|le}[date]&...}
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