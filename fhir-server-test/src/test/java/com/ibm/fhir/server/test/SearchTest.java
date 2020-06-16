/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.test.TestUtil.isResourceInResponse;
import static com.ibm.fhir.model.type.Code.code;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Observation.Component;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Person;
import com.ibm.fhir.model.resource.Person.Link;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.util.FHIRUtil;

public class SearchTest extends FHIRServerTestBase {

    private static final boolean DEBUG_SEARCH = false;
    private static final String PREFER_HEADER_RETURN_REPRESENTATION = "return=representation";
    private static final String PREFER_HEADER_NAME = "Prefer";
    private String patientId;
    private String observationId;
    private Boolean compartmentSearchSupported = null;
    private String practitionerId;
    private Patient patient4DuplicationTest = null;
    // Some of the tests run with tenant1 and datastore study1;
    // The others run with the default tenant and the default datastore.
    private final String tenantName = "performance10";
    private final String dataStoreId = "default";

    /**
     * Retrieve the server's conformance statement to determine the status of certain runtime options.
     *
     * @throws Exception
     */
    @Test
    public void retrieveConfig() throws Exception {
        compartmentSearchSupported = isComparmentSearchSupported();
        System.out.println("Compartment-based search supported?: "
                + compartmentSearchSupported.toString());
    }

    @Test(groups = { "server-search" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        patient = patient.toBuilder().gender(AdministrativeGender.MALE).build();
        Entity<Patient> entity =
                Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-search" })
    public void testCreatePersonWithManyLinks() throws Exception {
        // Build a new Person with a single links and then call 'create-on-update'.
        // This will ensure that the search parameters are added to the cache before we
        // insert one with a thousand of them.
        String identifier = UUID.randomUUID().toString();
        String patientRef = "Patient/" + identifier;
        Person person =
                createPersonWithIdentifierAndLink(identifier, patientRef);

        // Update the person with a bunch more links
        person = addLinks(person, 256 / 4);
        FHIRResponse updateResponse = client.update(person);
        assertResponse(updateResponse, Response.Status.OK.getStatusCode());

        // Finally, call the 'search' API to retrieve the new person via its first link
        // and verify it.
        FHIRResponse searchResponse =
                client.search("Person", new FHIRParameters().searchParam("patient", patientRef));
        assertResponse(searchResponse, Response.Status.OK.getStatusCode());
        Bundle responseBundle = searchResponse.getResource(Bundle.class);
        assertEquals(responseBundle.getEntry().size(), 1);
        Person responsePerson =
                (Person) responseBundle.getEntry().get(0).getResource();
        TestUtil.assertResourceEquals(person, responsePerson);
    }

    @Test(groups = { "server-search" })
    public void testCreatePersonWithManyIdentifiers() throws Exception {
        // Build a new Person with a single links and then call 'create-on-update'.
        // This will ensure that the search parameters are added to the cache before we
        // insert one with a thousand of them.
        String identifier = UUID.randomUUID().toString();
        String patientRef = "Patient/" + identifier;
        Person person =
                createPersonWithIdentifierAndLink(identifier, patientRef);

        // Update the person with a bunch more identifiers
        person = addIdentifiers(person, 256);
        FHIRResponse updateResponse = client.update(person);
        assertResponse(updateResponse, Response.Status.OK.getStatusCode());

        // Finally, call the 'search' API to retrieve the new person via its first link
        // and verify it.
        FHIRResponse searchResponse =
                client.search("Person", new FHIRParameters().searchParam("identifier", identifier));
        assertResponse(searchResponse, Response.Status.OK.getStatusCode());
        Bundle responseBundle = searchResponse.getResource(Bundle.class);
        assertEquals(responseBundle.getEntry().size(), 1);
        Person responsePerson =
                (Person) responseBundle.getEntry().get(0).getResource();
        TestUtil.assertResourceEquals(person, responsePerson);
    }

    private Person createPersonWithIdentifierAndLink(String identifier, String patientRef) throws Exception {
        String id = UUID.randomUUID().toString();

        Person person =
                Person.builder().id(id)
                      .identifier(Identifier.builder().value(string(identifier)).system(uri("test")).build())
                      .link(Link.builder().target(Reference.builder().reference(string(patientRef)).build()).build())
                      .build();

        FHIRResponse createResponse = client.update(person);
        assertResponse(createResponse, Response.Status.CREATED.getStatusCode());
        return person;
    }

    private Person addLinks(Person person, int numberOfLinks) {
        Person.Builder personBuilder = person.toBuilder();
        for (int i = 0; i < numberOfLinks; i++) {
            Person.Link link =
                    Link.builder().target(Reference.builder().reference(string("Patient/"
                            + UUID.randomUUID())).build()).build();
            personBuilder.link(link);
        }
        return personBuilder.build();
    }

    private Person addIdentifiers(Person person, int numberOfIdentifiers) {
        Person.Builder personBuilder = person.toBuilder();
        for (int i = 0; i < numberOfIdentifiers; i++) {
            Identifier identifier =
                    Identifier.builder().system(uri("test")).value(string(UUID.randomUUID().toString())).build();

            personBuilder.identifier(identifier);
        }
        return personBuilder.build();
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithGivenName() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("given", "John").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void test_SearchPatientWithGivenName() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("given", "John");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithID() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("_id", patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void test_SearchPatientWithID() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response = client._search("Patient", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("birthdate", "1970-01-01").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void test_SearchPatientWithBirthDate() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("birthdate", "1970-01-01");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithLTBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("birthdate", "lt1971-01-01").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void test_SearchPatientWithLTBirthDate() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("birthdate", "lt1971-01-01");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithGTBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("birthdate", "gt1950-08-13").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void test_SearchPatientWithGTBirthDate() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("birthdate", "gt1950-08-13");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithGender() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("gender", "male").request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void test_SearchPatientWithGender() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("gender", "male");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response = client._search("Patient", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" })
    public void testCreateObservationWithRange() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation =
                TestUtil.buildPatientObservation(patientId, "Observation5.json");
        Entity<Observation> entity =
                Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Observation").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the observation's logical id value.
        observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new observation and verify it.
        response = target.path("Observation/"
                + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation =
                response.readEntity(Observation.class);

        // use it for search
        observationId = responseObservation.getId();
        TestUtil.assertResourceEquals(observation, responseObservation);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation =
                TestUtil.buildPatientObservation(patientId, "Observation1.json");
        Entity<Observation> entity =
                Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Observation").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the observation's logical id value.
        observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new observation's and verify it.
        response = target.path("Observation/"
                + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation =
                response.readEntity(Observation.class);

        if (DEBUG_SEARCH) {
            SearchAllTest.generateOutput(responseObservation);
        }

        // use it for search
        observationId = responseObservation.getId();
        TestUtil.assertResourceEquals(observation, responseObservation);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithID() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("_id", observationId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationWithID() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", observationId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response =
                client._search("Observation", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithSubject() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"
                        + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithSubjectIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"
                        + patientId).queryParam("_include", "Observation:subject")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
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
        assertEquals("Patient/"
                + patientId, observation.getSubject().getReference().getValue());
    }

    @SuppressWarnings("rawtypes")
    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithSubjectIncluded_filter_elements()
            throws Exception {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"
                        + patientId).queryParam("_include", "Observation:subject")
                .queryParam("_elements", "status", "category", "subject")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
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

                SearchAllTest.generateOutput(entry.getResource());
                System.out.println(result + " "
                        + FHIRUtil.hasTag(entry.getResource(), subsettedTag));
            }

            result = result
                    || FHIRUtil.hasTag(entry.getResource(), subsettedTag);
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

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchPatientWithObservationRevIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")//.queryParam("_id", patientId)
                .queryParam("_revinclude", "Condition:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        //assertTrue(bundle.getEntry().size() == 2);
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
        assertEquals("Patient/"
                + patientId, observation.getSubject().getReference().getValue());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation", "retrieveConfig" })
    public void testSearchObservationWithPatientCompartment() {
        assertNotNull(compartmentSearchSupported);
        if (!compartmentSearchSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();
        String targetUri = "Patient/" + patientId + "/Observation";
        Response response =
                target.path(targetUri).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationWithSubject() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subject", "Patient/" + patientId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response =
                client._search("Observation", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithPatient() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("patient", "Patient/"
                        + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationWithPatient() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/" + patientId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response =
                client._search("Observation", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationCodeSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("component-value-quantity", "125.0||mmHg")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Observation observation = ((Observation) entry.getResource());
            assertNotNull(observation.getComponent());
            boolean passed = false;
            for (Component component: observation.getComponent()) {
                if (component.getValue().is(Quantity.class)) {
                    Decimal value = ((Quantity)component.getValue()).getValue();
                    // According to fhir spec, the value should be within +/-0.05.
                    if (value.getValue().doubleValue() <= 125.05
                            &&  value.getValue().doubleValue() >= 124.95) {
                        passed = true;
                        break;
                    }
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationCodeSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "125.0||mmHg");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response =
                client._search("Observation", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Observation observation = ((Observation) entry.getResource());
            assertNotNull(observation.getComponent());
            boolean passed = false;
            for (Component component: observation.getComponent()) {
                if (component.getValue().is(Quantity.class)) {
                    Decimal value = ((Quantity)component.getValue()).getValue();
                    // According to fhir spec, the value should be within +/-0.05.
                    if (value.getValue().doubleValue() <= 125.05
                            &&  value.getValue().doubleValue() >= 124.95) {
                        passed = true;
                        break;
                    }
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationCodeLTSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("component-value-quantity", "le126.0||mmHg")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Observation observation = ((Observation) entry.getResource());
            assertNotNull(observation.getComponent());
            boolean passed = false;
            for (Component component: observation.getComponent()) {
                if (component.getValue().is(Quantity.class)) {
                    Decimal value = ((Quantity)component.getValue()).getValue();
                    if (value.getValue().doubleValue() <= 126.0) {
                        passed = true;
                        break;
                    }
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationCodeLTSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "le126.0||mmHg");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response =
                client._search("Observation", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Observation observation = ((Observation) entry.getResource());
            assertNotNull(observation.getComponent());
            boolean passed = false;
            for (Component component: observation.getComponent()) {
                if (component.getValue().is(Quantity.class)) {
                    Decimal value = ((Quantity)component.getValue()).getValue();
                    if (value.getValue().doubleValue() <= 126.0) {
                        passed = true;
                        break;
                    }
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationCodeGTSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("component-value-quantity", "gt123.0||mmHg")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        if (DEBUG_SEARCH) {
            SearchAllTest.generateOutput(bundle);
        }
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Observation observation = ((Observation) entry.getResource());
            assertNotNull(observation.getComponent());
            boolean passed = false;
            for (Component component: observation.getComponent()) {
                if (component.getValue().is(Quantity.class)) {
                    Decimal value = ((Quantity)component.getValue()).getValue();
                    if (value.getValue().doubleValue() > 123.0) {
                        passed = true;
                        break;
                    }
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationCodeGTSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "gt123.0||mmHg");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader header2 =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRResponse response =
                client._search("Observation", parameters, header, header2);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Observation observation = ((Observation) entry.getResource());
            assertNotNull(observation.getComponent());
            boolean passed = false;
            for (Component component: observation.getComponent()) {
                if (component.getValue().is(Quantity.class)) {
                    Decimal value = ((Quantity)component.getValue()).getValue();
                    if (value.getValue().doubleValue() > 123.0) {
                        passed = true;
                        break;
                    }
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationFilteredSearchParameter1_preferStrict()
            throws Exception {
        // 'category' search parameter is filtered out for tenant1.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("category", "foo");
        FHIRRequestHeader tenantHeader =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader dsHeader =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRRequestHeader preferStrictHeader =
                new FHIRRequestHeader("Prefer", "handling=strict");
        FHIRResponse response =
                client._search("Observation", parameters, tenantHeader, dsHeader, preferStrictHeader);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());

        if (DEBUG_SEARCH) {
            SearchAllTest.generateOutput(response.getResource(OperationOutcome.class));
        }

        assertExceptionOperationOutcome(response.getResource(OperationOutcome.class),
                "Search parameter 'category' for resource type 'Observation' was not found.");
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationFilteredSearchParameter1_preferLenient()
            throws Exception {
        // 'category' search parameter is filtered out for tenant1.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("category", "foo");
        FHIRRequestHeader tenantHeader =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader dsHeader =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRRequestHeader preferStrictHeader =
                new FHIRRequestHeader("Prefer", "handling=lenient");
        FHIRResponse response =
                client._search("Observation", parameters, tenantHeader, dsHeader, preferStrictHeader);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void test_SearchObservationFilteredSearchParameter1_preferBogus()
            throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subject", "foo");
        FHIRRequestHeader tenantHeader =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
        FHIRRequestHeader dsHeader =
                new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);
        FHIRRequestHeader preferStrictHeader =
                new FHIRRequestHeader("Prefer", "handling=bogus");
        FHIRResponse response =
                client._search("Observation", parameters, tenantHeader, dsHeader, preferStrictHeader);
        // Assumes the server is in strict mode
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test(groups = { "server-search" })
    public void testCreatePractitioner() throws Exception {
        // Build a new Practitioner and then call the 'create' API.
        Practitioner practitioner = TestUtil.readLocalResource("Practitioner.json");
        assertNotNull(practitioner);
        WebTarget target = getWebTarget();

        Entity<Practitioner> entity =
                Entity.entity(practitioner, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Practitioner").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        practitionerId = getLocationLogicalId(response);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_Summary_Text() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_summary", "text")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);

        Practitioner practitioner = (Practitioner) bundle.getEntry().get(0).getResource();
        assertNotNull(practitioner);
        assertNotNull(practitioner.getText());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_Summary_Data() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_summary", "data")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);

        Practitioner practitioner = (Practitioner) bundle.getEntry().get(0).getResource();
        assertNotNull(practitioner);
        assertNull(practitioner.getText());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_Summary_Count() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_summary", "count")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getTotal().getValue().equals(1));
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_Count0() {
        // Should behave just like summary=count
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_count", "0")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getTotal().getValue().equals(1));
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithSubjectIncluded_summary_text() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"+ patientId)
                .queryParam("_include", "Observation:subject")
                .queryParam("_summary", "text")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }


    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchPatientWithObservationRevIncluded_summary_text() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("_id", patientId)
                .queryParam("_revinclude", "Observation:patient")
                .queryParam("_summary", "text")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }


    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchPatientWithObservationRevIncluded_summary_invalid_strict() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("_id", patientId)
                .queryParam("_revinclude", "Observation:patient")
                .queryParam("_summary", "invalid")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .header("Prefer", "handling=strict")
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "An error occurred while parsing parameter '_summary'");
    }

    @Test(groups = { "server-search" })
    public void testCreatePatientWith2Tags() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient with 2 tags and one duplicated tag and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Coding security = Coding.builder().system(uri("http://ibm.com/fhir/security")).code(code("security")).build();
        Coding tag = Coding.builder().system(uri("system")).code(code("tag")).build();
        Coding tag2 = Coding.builder().system(uri("system")).code(code("tag2")).build();

        patient = patient.toBuilder()
                .meta(Meta.builder()
                        .security(security)
                        .tag(tag)
                        .tag(tag)
                        .tag(tag2)
                        .profile(Canonical.of("http://ibm.com/fhir/profile/Profile"))
                        .build())
                .build();


        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request()
                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        patient4DuplicationTest = response.readEntity(Patient.class);
        assertNotNull(patient4DuplicationTest);

    }


    @Test(groups = { "server-search"}, dependsOnMethods = {"testCreatePatientWith2Tags" })
    public void testSearchAllUsing2TagsAndNoExistingTag() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        // tag88 doesn't exist, this case is created according to a reported test failure.
        parameters.searchParam("_tag", "system|tag88,tag2,tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatientWith2Tags();
        response = client.search("Patient", parameters);
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
            HashSet<String> patientSet = new HashSet<String>();
            for (Entry entry: bundle.getEntry()) {
                patientSet.add(((Patient) entry.getResource()).getId());
            }
            assertTrue(bundle.getEntry().size() == patientSet.size());
        }
    }


    @Test(groups = { "server-search"}, dependsOnMethods = {"testCreatePatientWith2Tags" })
    public void testSearchAllUsing2Tags() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "system|tag2,tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatientWith2Tags();
        response = client.search("Patient", parameters);
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
            HashSet<String> patientSet = new HashSet<String>();
            for (Entry entry: bundle.getEntry()) {
                patientSet.add(((Patient) entry.getResource()).getId());
            }
            assertTrue(bundle.getEntry().size() == patientSet.size());
        }
    }

    @Test(groups = { "server-search"}, dependsOnMethods = {"testCreatePatientWith2Tags" })
    public void testSearchAllUsing2FullTags() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "system|tag2,system|tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatientWith2Tags();
        response = client.search("Patient", parameters);
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
            HashSet<String> patientSet = new HashSet<String>();
            for (Entry entry: bundle.getEntry()) {
                patientSet.add(((Patient) entry.getResource()).getId());
            }
            assertTrue(bundle.getEntry().size() == patientSet.size());
        }
    }

    @Test(groups = { "server-search"}, dependsOnMethods = {"testCreatePatientWith2Tags" })
    public void testSearchAllUsingOneTag() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatientWith2Tags();
        response = client.search("Patient", parameters);
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
            HashSet<String> patientSet = new HashSet<String>();
            for (Entry entry: bundle.getEntry()) {
                patientSet.add(((Patient) entry.getResource()).getId());
            }
            assertTrue(bundle.getEntry().size() == patientSet.size());
        }
    }
}
