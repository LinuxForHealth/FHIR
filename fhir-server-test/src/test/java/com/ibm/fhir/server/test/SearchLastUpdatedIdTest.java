/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.Xhtml.xhtml;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.NarrativeStatus;

/**
 * The tests the behavior of _lastUpdated and _id in combination with other search parameters.
 * These are top level parameters which are used outside of the values tables
 */
public class SearchLastUpdatedIdTest extends FHIRServerTestBase {
    private String patientId;

    protected final String TAG_SYSTEM = "http://ibm.com/fhir/tag";
    protected final String TAG = UUID.randomUUID().toString();

    private final Map<String, String> TEST_CASES = new LinkedHashMap<String, String>() {
        private static final long serialVersionUID = -7809685447831223L;
        {
            // Test ONLY _id
            put("ID_ONLY", "_id=PATIENT_ID");
            put("ID_MULTIPLE_VALUES_ONLY", "_id=PATIENT_ID,PATIENT_ID_2");
            put("ID_MULTIPLES", "_id=PATIENT_ID,PATIENT_ID_2&_id=PATIENT_ID,PATIENT_ID_2");
            // Test ONLY a valid code
            put("NAME_ONLY", "name=PATIENT_NAME");
            put("NAME_MULTIPLE_VALUES_ONLY", "name=PATIENT_NAME,PATIENT_NAME_2");
            // Test ONLY Last Updated
            put("LAST_UPDATED_ONLY", "_lastUpdated=2020");
            put("LAST_UPDATED_MULTIPLE_VALUES_ONLY", "_lastUpdated=2020,2019");
            put("LAST_UPDATED_MULTIPLE_ONLY", "_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010");
            // Test ID with a code `name`
            put("ID_ONLY_WITH_NAME", "_id=PATIENT_ID&name=PATIENT_NAME");
            put("ID_MULTIPLE_VALUES_ONLY_WITH_NAME", "_id=PATIENT_ID,PATIENT_ID_2&name=PATIENT_NAME");
            put("ID_MULTIPLES_WITH_NAME", "_id=PATIENT_ID,PATIENT_ID_2&_id=PATIENT_ID,PATIENT_ID_2&name=PATIENT_NAME");
            // Test ID with multiple code `name`
            put("ID_ONLY_WITH_NAMES", "_id=PATIENT_ID&name=PATIENT_NAME,PATIENT_NAME_2");
            put("ID_MULTIPLE_VALUES_ONLY_WITH_NAMES", "_id=PATIENT_ID,PATIENT_ID_2&name=PATIENT_NAME,PATIENT_NAME_2");
            put("ID_MULTIPLES_WITH_NAMES", "_id=PATIENT_ID,PATIENT_ID_2&_id=PATIENT_ID,PATIENT_ID_2&name=PATIENT_NAME,PATIENT_NAME_2");
            // Test ID with multiple code `name`as the first
            put("ID_ONLY_WITH_NAMES_ORDER", "name=PATIENT_NAME,PATIENT_NAME_2&_id=PATIENT_ID");
            put("ID_MULTIPLE_VALUES_ONLY_WITH_NAMES_ORDER", "name=PATIENT_NAME,PATIENT_NAME_2&_id=PATIENT_ID,PATIENT_ID_2");
            put("ID_MULTIPLES_WITH_NAMES_ORDER", "name=PATIENT_NAME,PATIENT_NAME_2&_id=PATIENT_ID,PATIENT_ID_2&_id=PATIENT_ID,PATIENT_ID_2");
            // Test Last Updated with a code `name`
            put("LAST_UPDATED_WITH_NAME_ORDER", "name=PATIENT_NAME&_lastUpdated=2020");
            put("LAST_UPDATED_WITH_NAME", "_lastUpdated=2020&name=PATIENT_NAME");
            put("LAST_UPDATED_MULTIPLE_VALUES_WITH_NAME", "_lastUpdated=2020,2019&name=PATIENT_NAME");
            put("LAST_UPDATED_MULTIPLE_WITH_NAME", "_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010&name=PATIENT_NAME");
            // Test Last Updated with multiple code `name`
            put("LAST_UPDATED_WITH_NAMES", "_lastUpdated=2020&name=PATIENT_NAME,PATIENT_NAME_2");
            put("LAST_UPDATED_MULTIPLE_VALUES_WITH_NAMES", "_lastUpdated=2020,2019&name=PATIENT_NAME,PATIENT_NAME_2");
            put("LAST_UPDATED_MULTIPLE_WITH_NAMES", "_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010&name=PATIENT_NAME,PATIENT_NAME_2");
            // Test Last Updated with multiple code `name`as the first
            put("LAST_UPDATED_WITH_NAMES_ORDER", "name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=2020");
            put("LAST_UPDATED_MULTIPLE_VALUES_WITH_NAMES_ORDER", "name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=2020,2019");
            put("LAST_UPDATED_MULTIPLE_WITH_NAMES_ORDER", "name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010");
            // Test ID and Last Updated with a code `name`
            put("ID_LAST_UPDATED_WITH_NAME_ORDER", "_id=PATIENT_ID&name=PATIENT_NAME&_lastUpdated=2020");
            put("ID_LAST_UPDATED_WITH_NAME", "_id=PATIENT_ID&_lastUpdated=2020&name=PATIENT_NAME");
            put("ID_LAST_UPDATED_MULTIPLE_VALUES_WITH_NAME", "_id=PATIENT_ID&_lastUpdated=2020,2019&name=PATIENT_NAME");
            put("ID_LAST_UPDATED_MULTIPLE_WITH_NAME", "_id=PATIENT_ID&_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010&name=PATIENT_NAME");
            // Test ID and Last Updated with multiple code `name`
            put("ID_LAST_UPDATED_WITH_NAMES", "_id=PATIENT_ID&_lastUpdated=2020&name=PATIENT_NAME,PATIENT_NAME_2");
            put("ID_LAST_UPDATED_MULTIPLE_VALUES_WITH_NAMES", "_id=PATIENT_ID&_lastUpdated=2020,2019&name=PATIENT_NAME,PATIENT_NAME_2");
            put("ID_LAST_UPDATED_MULTIPLE_WITH_NAMES", "_id=PATIENT_ID&_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010&name=PATIENT_NAME,PATIENT_NAME_2");
            // Test ID and Last Updated with multiple code `name`as the first
            put("ID_LAST_UPDATED_WITH_NAMES_ORDER", "_id=PATIENT_ID&name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=2020");
            put("ID_LAST_UPDATED_MULTIPLE_VALUES_WITH_NAMES_ORDER", "_id=PATIENT_ID&name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=2020,2019");
            put("ID_LAST_UPDATED_MULTIPLE_WITH_NAMES_ORDER", "_id=PATIENT_ID&name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010");
            // Test All with _tag
            put("ID_LAST_UPDATED_MULTIPLE_WITH_NAMES_ORDER", "_tag=tag-to-replace&_id=PATIENT_ID&name=PATIENT_NAME,PATIENT_NAME_2&_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010");
            // Test ID and Last Updated
            put("ID_LAST_UPDATED", "_id=PATIENT_ID&_lastUpdated=2020");
            put("ID_LAST_UPDATED_MULTIPLE_VALUES", "_id=PATIENT_ID&_lastUpdated=2020,2019");
            put("ID_LAST_UPDATED_MULTIPLE", "_id=PATIENT_ID&_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010");
            // Test ID and Last Updated (Alternate Order)
            put("ID_LAST_UPDATED_WITH_ORDER", "_lastUpdated=2020&_id=PATIENT_ID");
            put("ID_LAST_UPDATED_MULTIPLE_VALUES_WITH_ORDER", "_lastUpdated=2020,2019&_id=PATIENT_ID");
            put("ID_LAST_UPDATED_MULTIPLE_WITH_ORDER", "_lastUpdated=le2030,le2019&_lastUpdated=ge2000,ge2010&_id=PATIENT_ID");
        }
    };

    private String replaceValues(String input) {
        return input.replaceAll("PATIENT_ID", patientId)
                .replaceAll("PATIENT_NAME", "John")
                .replaceAll("tag-to-replace", TAG);
    }

    @Test(groups = { "server-search-lastupdatedid" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";

        Coding tag =
                Coding.builder()
                        .system(Uri.of(TAG_SYSTEM))
                        .code(Code.of(TAG)).build();

        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .tag(tag)
                .build();

        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();

        patient = patient.toBuilder().meta(meta).text(text).gender(AdministrativeGender.MALE).build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-search-lastupdatedid" }, dependsOnMethods = { "testCreatePatient" })
    public void runTestCasesOnPatient() {
        for (Entry<String, String> testCase : TEST_CASES.entrySet()) {
            WebTarget target = getWebTarget();
            target = target.path("Patient");

            String testCaseName = testCase.getKey();
            String testCaseUrl = testCase.getValue();

            for (String queryParamSegment : testCaseUrl.split("&")) {
                String[] parts = queryParamSegment.split("=");
                target = target.queryParam(parts[0], replaceValues(parts[1]));
            }

            System.out.println(testCaseName);
            Response response = target.request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Bundle bundle = response.readEntity(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
        }
    }

    @Test(groups = { "server-search-lastupdatedid" }, dependsOnMethods = { "testCreatePatient" })
    public void runTestCasesOnSystem() {
        for (Entry<String, String> testCase : TEST_CASES.entrySet()) {
            WebTarget target = getWebTarget();
            target = target.path("/");

            String testCaseName = testCase.getKey();
            String testCaseUrl = testCase.getValue();

            if (testCaseName.contains("NAME")) {
                continue;
            }

            for (String queryParamSegment : testCaseUrl.split("&")) {
                String[] parts = queryParamSegment.split("=");
                target = target.queryParam(parts[0], replaceValues(parts[1]));
            }

            System.out.println(testCaseName);
            Response response = target.request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Bundle bundle = response.readEntity(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
        }
    }

    @Test(groups = { "server-search-lastupdatedid" }, dependsOnMethods = { "testCreatePatient" })
    public void runTestCasesOnPatientSorted() {
        for (Entry<String, String> testCase : TEST_CASES.entrySet()) {
            WebTarget target = getWebTarget();
            target = target.path("Patient");
            target = target.queryParam("_sort","_lastUpdated");
            String testCaseName = testCase.getKey();
            String testCaseUrl = testCase.getValue();

            for (String queryParamSegment : testCaseUrl.split("&")) {
                String[] parts = queryParamSegment.split("=");
                target = target.queryParam(parts[0], replaceValues(parts[1]));
            }

            System.out.println(testCaseName);
            Response response = target.request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Bundle bundle = response.readEntity(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
        }
    }

    @Test(groups = { "server-search-lastupdatedid" }, dependsOnMethods = { "testCreatePatient" })
    public void runTestCasesOnSystemSorted() {
        for (Entry<String, String> testCase : TEST_CASES.entrySet()) {
            WebTarget target = getWebTarget();
            target = target.path("/");

            String testCaseName = testCase.getKey();
            String testCaseUrl = testCase.getValue();
            target = target.queryParam("_sort","_lastUpdated");
            if (testCaseName.contains("NAME")) {
                continue;
            }

            for (String queryParamSegment : testCaseUrl.split("&")) {
                String[] parts = queryParamSegment.split("=");
                target = target.queryParam(parts[0], replaceValues(parts[1]));
            }

            System.out.println(testCaseName);
            Response response = target.request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            assertResponse(response, Response.Status.OK.getStatusCode());

            Bundle bundle = response.readEntity(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
        }
    }
}