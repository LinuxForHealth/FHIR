/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

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
import com.ibm.fhir.model.resource.DocumentReference;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the US Core 3.1.1 Profile with DocumentReference.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-documentreference.html
 * https://www.hl7.org/fhir/us/core/clinical-notes-guidance.html
 */
public class USCoreDocumentReferenceTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreImmunizationTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String documentReferenceId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-documentreference|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - DocumentReference', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadDocumentReference1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteDocumentReference1();
        }
    }

    public void loadDocumentReference1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core//DocumentReference-episode-summary.json";
        WebTarget target = getWebTarget();

        DocumentReference goal = TestUtil.readExampleResource(resource);

        Entity<DocumentReference> entity = Entity.entity(goal, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("DocumentReference").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/DocumentReference/12354 (first actual test, but simple)
        documentReferenceId1 = getLocationLogicalId(response);
        response = target.path("DocumentReference/" + documentReferenceId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteDocumentReference1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("DocumentReference/" + documentReferenceId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForId() throws Exception {
        // SHALL support fetching a DocumentReference using the _id search parameter:
        // GET [base]/DocumentReference[id]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", documentReferenceId1);
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
    
    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all documentreferences for a patient using the patient search parameter:
        // GET [base]/DocumentReference?patient=[reference]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
    
    @Test
    public void testSearchForPatientAndCategory() throws Exception {
        // SHALL support searching using the combination of the patient and category search parameters:
        // GET [base]/DocumentReference?patient=[reference]&category=http://hl7.org/fhir/us/core/CodeSystem/us-core-documentreference-category|clinical-note
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/us-core-documentreference-category|clinical-note");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
    
    @Test
    public void testSearchForPatientAndCategoryAndDate() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET [base]/DocumentReference?patient=[reference]&category=http://hl7.org/fhir/us/core/CodeSystem/us-core-documentreference-category|clinical-note&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/us-core-documentreference-category|clinical-note");
            parameters.searchParam("date", "ge2016");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
    
    @Test
    public void testSearchForPatientAndCategoryAndBadDate() throws Exception {
        // SHALL support searching using the combination of the patient and category and date search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET [base]/DocumentReference?patient=[reference]&category=http://hl7.org/fhir/us/core/CodeSystem/us-core-documentreference-category|clinical-note&date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/us-core-documentreference-category|clinical-note");
            parameters.searchParam("date", "ge2017");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);
        }
    }
    
    @Test
    public void testSearchForPatientAndType() throws Exception {
        // SHALL support searching using the combination of the patient and type search parameters:
        // GET [base]/DocumentReference?patient=[reference]&type={system|}[code]
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("type", "http://loinc.org|34133-9");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
    
    @Test
    public void testSearchForPatientAndStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // including support for composite OR search on status (e.g.status={system|}[code],{system|}[code],...)
        // GET [base]/DocumentReference?patient=[reference]&status={system|}[code]{,{system|}[code],...}
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("type", "http://loinc.org|34133-9");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
    
    @Test
    public void testSearchForPatientAndTypeAndPeriod() throws Exception {
        // SHOULD support searching using the combination of the patient and type and period search parameters:
        // including support for these period comparators: gt,lt,ge,le
        // including optional support for composite AND search on period (e.g.period=[date]&period=[date]]&...)
        // GET [base]/DocumentReference?patient=[reference]&type={system|}[code]&period={gt|lt|ge|le}[date]{&period={gt|lt|ge|le}[date]&...}
        
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("type", "http://loinc.org|34133-9");
            parameters.searchParam("period", "2004");
            FHIRResponse response = client.search(DocumentReference.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, documentReferenceId1);
        }
    }
}