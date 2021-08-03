/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.performance;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Observation.Component;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.SearchEntryMode;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;

public class SearchPerformanceTest extends FHIRServerTestBase {
    private static final boolean DEBUG_SEARCH = false;
    private String patientId;
    private String observationId;
    private Boolean compartmentSearchSupported = null;
    // Controls which tenant to use for the test.
    private final String tenantName = "default";
    // Controls how many observations and patients to create for the test.
    // Using invocationCount of testng can cause the testng report grows too big, so
    // this config is used to make sure all test users are created in one testng step.
    private int numOfPatientObservationsToCreate = 1000;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        numOfPatientObservationsToCreate = Integer.parseInt(testProperties.getProperty("test.performance.default", "1000"));
    }

    /**
     * Retrieve the server's conformance statement to determine the status of certain runtime options.
     *
     * @throws Exception
     */
    @Test
    public void retrieveConfig() throws Exception {
        compartmentSearchSupported = isComparmentSearchSupported();
    }

    @Test(groups = { "server-search-performance" })
    public void testCreatePatientAndObservation() throws Exception {
        WebTarget target = getWebTarget();
        for (int i = 0; i < numOfPatientObservationsToCreate; i++) {
            // Build a new Patient and then call the 'create' API.
            Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

            patient = patient.toBuilder().gender(AdministrativeGender.MALE).build();
            Entity<Patient> entity =
                    Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response =
                    target.path("Patient")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());

            // Get the patient's logical id value.
            patientId = getLocationLogicalId(response);
            addToResourceRegistry("Patient", patientId);

            // Next, call the 'read' API to retrieve the new patient and verify it.
            response = target.path("Patient/" + patientId)
                                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                                .header("X-FHIR-TENANT-ID", tenantName)
                                .get();
            assertResponse(response, Response.Status.OK.getStatusCode());
            Patient responsePatient = response.readEntity(Patient.class);
            TestUtil.assertResourceEquals(patient, responsePatient);

            // create observation for the patient
            Observation observation =
                    TestUtil.buildPatientObservation(patientId, "Observation1.json");
            observation = observation.toBuilder()
                    .code(CodeableConcept.builder()
                            .coding(Coding.builder()
                                    .system(Uri.of("http://loinc.org"))
                                    .code(Code.of("55284-4"))
                                    .build())
                            .build())
                    .component(Component.builder()
                            .code(CodeableConcept.builder().text(string("component1")).build())
                            .value(Quantity.builder()
                                    .value(Decimal.of(BigDecimal.valueOf(Math.random())))
                                    .unit(string("mmHg"))
                                    .build())
                            .build())
                    .build();
            Entity<Observation> entity2 =
                    Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
            response =
                    target.path("Observation")
                    .request()
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .post(entity2, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());

            // Get the patient's logical id value.
            String observationId = getLocationLogicalId(response);
            addToResourceRegistry("Observation", observationId);

            // Next, call the 'read' API to retrieve the new patient and verify it.
            response = target.path("Observation/"
                    + observationId)
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .get();
            assertResponse(response, Response.Status.OK.getStatusCode());
            Observation responseObservation =
                    response.readEntity(Observation.class);

            printOutResource(DEBUG_SEARCH, responseObservation);

            // use it for search
            observationId = responseObservation.getId();
            TestUtil.assertResourceEquals(observation, responseObservation);
        }
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchPatientWithGivenName() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("given", "John")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = { "testCreatePatientAndObservation" })
    public void testSearchPatientWithID() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("_id", patientId)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = { "testCreatePatientAndObservation" })
    public void testSearchPatientWithBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("birthdate", "1970-01-01")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchPatientWithLTBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("birthdate", "lt1971-01-01")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchPatientWithGTBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("birthdate", "gt1950-08-13")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchPatientWithGender() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("gender", "male")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchObservationWithID() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("_id", observationId)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchObservationWithSubject() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("subject", "Patient/" + patientId)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchObservation() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("_count", "100")
                        .queryParam("_page", "1")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchObservationWithSubjectIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("subject", "Patient/" + patientId)
                        .queryParam("_include", "Observation:subject")
                        .queryParam("_count", "100")
                        .queryParam("_page", "1")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
        Observation observation = null;
        Patient patient = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof Observation) {
                    observation = (Observation) entry.getResource();
                } else if (entry.getResource() instanceof Patient) {
                    patient = (Patient) entry.getResource();
                }
            }
        }
        assertNotNull(observation);
        assertNotNull(patient);
        assertEquals(patientId, patient.getId());
        assertEquals("Patient/" + patientId, observation.getSubject().getReference().getValue());
    }

    @SuppressWarnings("rawtypes")
    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchObservationWithSubjectIncluded_filter_elements()
            throws Exception {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("subject", "Patient/" + patientId)
                        .queryParam("_include", "Observation:subject")
                        .queryParam("_elements", "status,category,subject")
                        .queryParam("_count", "100")
                        .queryParam("_page", "1")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        Coding subsettedTag =
                Coding.builder().system(uri("http://terminology.hl7.org/CodeSystem/v3-ObservationValue")).code(Code.of("SUBSETTED")).display(string("subsetted")).build();
        assertTrue(FHIRUtil.hasTag(bundle, subsettedTag));

        boolean result = false;
        for (Bundle.Entry entry : bundle.getEntry()) {

            if (DEBUG_SEARCH) {
                printOutResource(DEBUG_SEARCH, entry.getResource());
                System.out.println(result + " "
                        + FHIRUtil.hasTag(entry.getResource(), subsettedTag));
            }

            result = result || FHIRUtil.hasTag(entry.getResource(), subsettedTag);
        }
        assertTrue(result);

        assertTrue(bundle.getEntry().size() == 2);

        Observation observation = null;
        Patient patient = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof Observation) {
                    observation = (Observation) entry.getResource();
                } else if (entry.getResource() instanceof Patient) {
                    patient = (Patient) entry.getResource();
                }
            }
        }
        assertNotNull(observation);
        assertTrue(FHIRUtil.hasTag(observation, subsettedTag));
        // Verify that only the requested elements (and "mandatory elements") are
        // present in the returned Observation.
        Method[] observationMethods = Observation.class.getDeclaredMethods();
        for (int i = 0; i < observationMethods.length; i++) {
            Method obsMethod = observationMethods[i];
            if (obsMethod.getName().startsWith("get")) {
                Object elementValue = obsMethod.invoke(observation);
                if (obsMethod.getName().equals("getId")
                        || obsMethod.getName().equals("getMeta")
                        || obsMethod.getName().equals("getStatus")
                        || obsMethod.getName().equals("getSubject")
                        || obsMethod.getName().equals("getCategory")
                        || obsMethod.getName().equals("getCode")) {
                    assertNotNull(elementValue);
                } else {
                    if (elementValue instanceof List) {
                        assertEquals(0, ((List) elementValue).size());
                    } else {
                        assertNull(elementValue);
                    }
                }
            }
        }

        assertNotNull(patient);
        assertFalse(FHIRUtil.hasTag(patient, subsettedTag));
        assertEquals(patientId, patient.getId());
        assertEquals("Patient/"
                + patientId, observation.getSubject().getReference().getValue());
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchPatientWithObservationRevIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("_id", patientId)
                        .queryParam("_revinclude", "Observation:patient")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
        Observation observation = null;
        Patient patient = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof Observation) {
                    observation = (Observation) entry.getResource();
                } else if (entry.getResource() instanceof Patient) {
                    patient = (Patient) entry.getResource();
                }
            }
        }
        assertNotNull(observation);
        assertNotNull(patient);
        assertEquals(patientId, patient.getId());
        assertEquals("Patient/" + patientId, observation.getSubject().getReference().getValue());
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
            "testCreatePatientAndObservation", "retrieveConfig" })
    public void testSearchObservationWithPatientCompartment() {
        assertNotNull(compartmentSearchSupported);
        if (!compartmentSearchSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();
        String targetUri = "Patient/" + patientId + "/Observation";
        Response response =
                target.path(targetUri)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = { "testCreatePatientAndObservation" })
    public void testSearchObservationWithPatient() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("patient", "Patient/" + patientId)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = { "testCreatePatientAndObservation" })
    public void testSearchObservationWithSubjectIncluded_summary_text() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("subject", "Patient/"+ patientId)
                        .queryParam("_include", "Observation:subject")
                        .queryParam("_summary", "text")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("Prefer", "handling=lenient")
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(1, bundle.getTotal().getValue().intValue());
        assertEquals(2, bundle.getEntry().size());
        assertEquals(SearchEntryMode.MATCH, SearchEntryMode.of(bundle.getEntry().get(0).getSearch().getMode().getValue()));
        assertEquals(SearchEntryMode.OUTCOME, SearchEntryMode.of(bundle.getEntry().get(1).getSearch().getMode().getValue()));
    }

    @Test(groups = { "server-search-performance" }, dependsOnMethods = {
    "testCreatePatientAndObservation" })
    public void testSearchPatientWithObservationRevIncluded_summary_text() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                        .queryParam("_id", patientId)
                        .queryParam("_revinclude", "Observation:patient")
                        .queryParam("_summary", "text")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("Prefer", "handling=lenient")
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(1, bundle.getTotal().getValue().intValue());
        assertEquals(2, bundle.getEntry().size());
        assertEquals(SearchEntryMode.MATCH, SearchEntryMode.of(bundle.getEntry().get(0).getSearch().getMode().getValue()));
        assertEquals(SearchEntryMode.OUTCOME, SearchEntryMode.of(bundle.getEntry().get(1).getSearch().getMode().getValue()));
    }

    @Test(groups = { "server-search-performance" })
    public void testSearchObservationWithPatientName() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("subject:Patient.name", "john")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
    }
}