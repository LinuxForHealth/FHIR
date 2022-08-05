/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.ig.us.core.tool.USCoreExamplesUtil;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Condition.
 */
public class USCoreConditionTest extends ProfilesTestBaseV2 {
    private String conditionId1 = null;
    private String conditionId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-condition|3.1.1");
    }

    public void loadCondition1() throws Exception {
        String resource = "Condition-example.json";
        Condition condition = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        conditionId1 = createResourceAndReturnTheLogicalId("Condition", condition);
    }

    public void loadCondition2() throws Exception {
        String resource = "Condition-hc1.json";
        Condition condition = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        conditionId2 = createResourceAndReturnTheLogicalId("Condition", condition);
    }

    @Override
    public void loadResources() throws Exception {
        loadCondition1();
        loadCondition2();
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all conditions including problems, health concerns, and encounter diagnosis for a
        // patient using the patient search parameter:
        // GET [base]/Condition?patient=[reference]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("_id", conditionId1 + "," + conditionId2);
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertBaseBundleNotEmpty(bundle);
        assertContainsIds(bundle, conditionId1);
        assertContainsIds(bundle, conditionId2);
    }

    @Test
    public void testSearchForPatientAndClinicalStatus() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters:
        // GET
        // [base]/Condition?patient=[reference]&clinical-status=http://terminology.hl7.org/CodeSystem/condition-clinical|active,http://terminology.hl7.org/CodeSystem/condition-clinical|recurrance,http://terminology.hl7.org/CodeSystem/condition-clinical|remission
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("clinical-status", "http://terminology.hl7.org/CodeSystem/condition-clinical|active,http://terminology.hl7.org/CodeSystem/condition-clinical|recurrance,http://terminology.hl7.org/CodeSystem/condition-clinical|remission");
        parameters.searchParam("_id", conditionId1 + "," + conditionId2); // Protect against bad data
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertBaseBundleNotEmpty(bundle);
        assertContainsIds(bundle, conditionId1);
        assertContainsIds(bundle, conditionId2);
    }

    @Test
    public void testSearchForPatientAndCategoryProblemListItem() throws Exception {
        // SHOULD support searching using the combination of the patient and category search parameters:
        // GET [base]/Condition?patient=[reference]&category={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/condition-category|problem-list-item");
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);

    }

    @Test
    public void testSearchForPatientAndCategoryHealthConcern() throws Exception {
        // SHOULD support searching using the combination of the patient and category search parameters:
        // GET [base]/Condition?patient=[reference]&category={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/condition-category|health-concern");
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertBaseBundleNotEmpty(bundle);
        assertContainsIds(bundle, conditionId2);
    }

    @Test
    public void testSearchForPatientAndCategoryDoesNotExist() throws Exception {
        // SHOULD support searching using the combination of the patient and category search parameters:
        // GET [base]/Condition?patient=[reference]&category={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("category", "http://hl7.org/fhir/us/core/CodeSystem/condition-category|encounter-diagnosis");
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());

    }

    @Test
    public void testSearchForPatientAndCode() throws Exception {
        // SHOULD support searching using the combination of the patient and code search parameters:
        // GET [base]/Condition?patient=[reference]&code={system|}[code]
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
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

    @Test
    public void testSearchForPatientAndOnsetDateGe() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date
        // (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
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

    @Test
    public void testSearchForPatientAndOnsetDateGt() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date
        // (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
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

    @Test
    public void testSearchForPatientAndOnsetDateLt() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date
        // (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("onset-date", "lt2019");
        parameters.searchParam("_id", conditionId1 + "," + conditionId2); // Protect against bad data
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, conditionId1);
    }

    @Test
    public void testSearchForPatientAndOnsetDateLe() throws Exception {
        // SHOULD support searching using the combination of the patient and onset-date search parameters:
        // including support for these onset-date comparators: gt,lt,ge,le
        // including optional support for composite AND search on onset-date
        // (e.g.onset-date=[date]&onset-date=[date]]&...)
        // GET [base]/Condition?patient=[reference]&onset-date={gt|lt|ge|le}[date]{&onset-date={gt|lt|ge|le}[date]&...}
        // "onsetDateTime": "2016-08-10"
        // http://hl7.org/fhir/us/core/StructureDefinition-us-core-condition.html
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("onset-date", "le2016");
        parameters.searchParam("_id", conditionId1 + "," + conditionId2); // Protect against bad data
        FHIRResponse response = client.search(Condition.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, conditionId1);
    }
}