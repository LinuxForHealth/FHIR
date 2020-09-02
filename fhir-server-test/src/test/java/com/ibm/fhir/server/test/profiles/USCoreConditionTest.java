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
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests the US Core 3.1.1 Profile with Condition.
 */
public class USCoreConditionTest extends ProfilesTestBase {
    private static final String CLASSNAME = USCoreConditionTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;

    private String conditionId1 = null;
    private String conditionId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-condition|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Condition', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadCondition1();
            loadCondition2();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteCondition1();
            deleteCondition2();
        }
    }

    public void loadCondition1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Condition-example.json";
        WebTarget target = getWebTarget();

        Condition condition = TestUtil.readExampleResource(resource);

        Entity<Condition> entity = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Condition").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        conditionId1 = getLocationLogicalId(response);
        response = target.path("Condition/" + conditionId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadCondition2() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Condition-hc1.json";
        WebTarget target = getWebTarget();

        Condition condition = TestUtil.readExampleResource(resource);

        Entity<Condition> entity = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Condition").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        conditionId2 = getLocationLogicalId(response);
        response = target.path("Condition/" + conditionId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteCondition1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Condition/" + conditionId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteCondition2() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Condition/" + conditionId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all conditions including problems, health concerns, and encounter diagnosis for a
        // patient using the patient search parameter:
        // GET [base]/Condition?patient=[reference]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
            assertContainsIds(bundle, conditionId2);
        }
    }

    @Test
    public void testSearchForPatientAndClinicalStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters:
        // GET
        // [base]/Condition?patient=[reference]&clinical-status=http://terminology.hl7.org/CodeSystem/condition-clinical|active,http://terminology.hl7.org/CodeSystem/condition-clinical|recurrance,http://terminology.hl7.org/CodeSystem/condition-clinical|remission
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "http://terminology.hl7.org/CodeSystem/condition-clinical|active,http://terminology.hl7.org/CodeSystem/condition-clinical|recurrance,http://terminology.hl7.org/CodeSystem/condition-clinical|remission");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
            assertContainsIds(bundle, conditionId2);
        }
    }

    @Test
    public void testSearchForPatientAndCategoryProblemListItem() throws Exception {
        // SHOULD support searching using the combination of the patient and category search parameters:
        // GET [base]/Condition?patient=[reference]&category={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/condition-category|problem-list-item");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);
        }
    }

    @Test
    public void testSearchForPatientAndCategoryHealthConcern() throws Exception {
        // SHOULD support searching using the combination of the patient and category search parameters:
        // GET [base]/Condition?patient=[reference]&category={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/condition-category|health-concern");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId2);
        }
    }

    @Test
    public void testSearchForPatientAndCategoryDoesNotExist() throws Exception {
        // SHOULD support searching using the combination of the patient and category search parameters:
        // GET [base]/Condition?patient=[reference]&category={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("category", "http://terminology.hl7.org/CodeSystem/condition-category|encounter-diagnosis");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);
        }
    }

    @Test
    public void testSearchForPatientAndCode() throws Exception {
        // SHOULD support searching using the combination of the patient and code search parameters:
        // GET [base]/Condition?patient=[reference]&code={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("code", "http://snomed.info/sct|442311008");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
        }
    }

    @Test
    public void testSearchForPatientAndOnsetDateGe() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("onset-date", "ge2016");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
        }
    }

    @Test
    public void testSearchForPatientAndOnsetDateGt() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("onset-date", "gt2015");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
        }
    }

    @Test
    public void testSearchForPatientAndOnsetDateLt() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("onset-date", "lt2019");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
        }
    }

    @Test
    public void testSearchForPatientAndOnsetDateLe() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("onset-date", "le2016");
            FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, conditionId1);
        }
    }
}