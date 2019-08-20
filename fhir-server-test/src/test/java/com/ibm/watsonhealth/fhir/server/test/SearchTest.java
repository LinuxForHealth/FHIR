/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static com.ibm.watsonhealth.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRRequestHeader;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Person;
import com.ibm.watsonhealth.fhir.model.resource.Person.Link;
import com.ibm.watsonhealth.fhir.model.type.AdministrativeGender;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

public class SearchTest extends FHIRServerTestBase {
    
    private static final boolean DEBUG = false;
    
    private String patientId;
    private String observationId;
    private Boolean compartmentSearchSupported = null;

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
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        patient = patient.toBuilder().gender(AdministrativeGender.MALE).build();
        Entity<Patient> entity =
                Entity.entity(patient, MediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + patientId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);
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
        assertResourceEquals(person, responsePerson);
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
        assertResourceEquals(person, responsePerson);
    }

    // Uncomment in order to test the limits when using the 'normalized' model with
    // Db2
    // @Test(groups = { "server-search" })
    // public void testCreatePersonWithTooManyLinks() throws Exception {
    // // Build a new Person with a single identifier and then call call 'create-on-update'.
    // // This will ensure that the search parameters are added to the cache before we insert one with a thousand of
    // them.
    // String identifier = UUID.randomUUID().toString();
    // String patientRef = "Patient/" + identifier;
    // Person person = createPersonWithIdentifierAndLink(identifier, patientRef);
    //
    // // Update the person with a bunch more links to exceed the limit
    // addLinks(person, 1024 / 4);
    // FHIRResponse response = client.update(person);
    // assertResponse(response, Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode());
    // }
    //
    // @Test(groups = { "server-search" })
    // public void testCreatePersonWithTooManyIdentifiers() throws Exception {
    // // Build a new Person with a single identifier and then call call 'create-on-update'.
    // // This will ensure that the search parameters are added to the cache before we insert one with a thousand of
    // them.
    // String identifier = UUID.randomUUID().toString();
    // String patientRef = "Patient/" + identifier;
    // Person person = createPersonWithIdentifierAndLink(identifier, patientRef);
    //
    // // Update the person with a bunch more links to exceed the limit
    // addIdentifiers(person, 1024);
    // FHIRResponse response = client.update(person);
    // assertResponse(response, Response.Status.REQUEST_ENTITY_TOO_LARGE.getStatusCode());
    // }

    private Person createPersonWithIdentifierAndLink(String identifier,
        String patientRef) throws Exception {
        String id = UUID.randomUUID().toString();

        Person person =
                Person.builder().id(Id.of(id)).identifier(Identifier.builder().value(string(identifier)).system(uri("test")).build()).link(Link.builder().target(Reference.builder().reference(string(patientRef)).build()).build()).build();

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

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testSearchPatientWithGivenName() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("given", "John").request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void test_SearchPatientWithGivenName() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("given", "John");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testSearchPatientWithID() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("_id", patientId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void test_SearchPatientWithID() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testSearchPatientWithBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("birthdate", "1970-01-01").request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void test_SearchPatientWithBirthDate() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("birthdate", "1970-01-01");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testSearchPatientWithLTBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("birthdate", "lt1971-01-01").request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void test_SearchPatientWithLTBirthDate() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("birthdate", "lt1971-01-01");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testSearchPatientWithGTBirthDate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("birthdate", "gt1950-08-13").request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void test_SearchPatientWithGTBirthDate() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("birthdate", "gt1950-08-13");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testSearchPatientWithGender() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("gender", "male").request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void test_SearchPatientWithGender() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("gender", "male");
        FHIRResponse response = client._search("Patient", parameters);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" })
    public void testCreateObservationWithRange() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation =
                buildObservation(patientId, "Observation5.json");
        Entity<Observation> entity =
                Entity.entity(observation, MediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Observation").request().header("X-FHIR-TENANT-ID", "tenant1").post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Observation/"
                + observationId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation =
                response.readEntity(Observation.class);

        // use it for serach
        observationId = responseObservation.getId().getValue();
        assertResourceEquals(observation, responseObservation);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservationWithRange" })
    public void testSearchObservationObservationWithRange() {
        // Range is 3.0 - 7.0 mg

        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("value-range", "ap5.0|http://loinc.org|v15074-8").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservationWithRange" })
    public void test_SearchObservationObservationWithRange() throws Exception {
        // Range is 3.0 - 7.0 mg

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("value-range", "ap5.0|http://loinc.org|v15074-8");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @SuppressWarnings("rawtypes")
    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservationWithRange" })
    public void test_SearchObservationObservationWithRange_filter_elements()
        throws Exception {
        // Range is 3.0 - 7.0 mg

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("value-range", "ap5.0|http://loinc.org|v15074-8");
        parameters.searchParam("_elements", "status,category");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        Coding subsettedTag =
                Coding.builder().system(uri("http://terminology.hl7.org/CodeSystem/v3-ObservationValue")).code(Code.of("SUBSETTED")).display(string("subsetted")).build();

        boolean result = false;
        for (Bundle.Entry entry : bundle.getEntry()) {

            if (DEBUG) {

                SearchAllTest.generateOutput(entry.getResource());
                System.out.println(result + " "
                        + FHIRUtil.hasTag(entry.getResource(), subsettedTag));
            }

            result = result
                    || FHIRUtil.hasTag(entry.getResource(), subsettedTag);
        }
        assertTrue(result);

        assertTrue(bundle.getEntry().size() >= 1);
        Observation retrievedObservation =
                (Observation) bundle.getEntry().get(0).getResource();
        assertNotNull(retrievedObservation);
        assertTrue(FHIRUtil.hasTag(retrievedObservation, subsettedTag));

        // Verify that only the requested elements were returned in the Observation.
        Method[] observationMethods = Observation.class.getMethods();
        for (int i = 0; i < observationMethods.length; i++) {
            Method obsMethod = observationMethods[i];
            if (obsMethod.getName().startsWith("get")) {
                Object elementValue = obsMethod.invoke(retrievedObservation);
                if (obsMethod.getName().equals("getId") || // required for all returned resources
                        obsMethod.getName().equals("getMeta") || // required for all returned resources
                        obsMethod.getName().equals("getStatus") || // required for Observations AND included in elements
                                                                   // filter
                        obsMethod.getName().equals("getCategory") || // included in elements filter
                        obsMethod.getName().equals("getCode") // required for Observations
                ) {
                    assertNotNull(elementValue);
                } else if (!obsMethod.getName().equals("getClass")) {
                    if (elementValue instanceof List) {
                        assertEquals(0, ((List) elementValue).size());
                    } else {
                        assertNull(elementValue);
                    }
                }
            }
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreatePatient" })
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation =
                buildObservation(patientId, "Observation1.json");
        Entity<Observation> entity =
                Entity.entity(observation, MediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Observation").request().header("X-FHIR-TENANT-ID", "tenant1").post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Observation/"
                + observationId).request(MediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation =
                response.readEntity(Observation.class);

        // use it for serach
        observationId = responseObservation.getId().getValue();
        assertResourceEquals(observation, responseObservation);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationWithID() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("_id", observationId).request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationWithID() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", observationId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationWithSubject() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"
                        + patientId).request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationWithSubjectIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"
                        + patientId).queryParam("_include", "Observation:subject").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
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
        assertEquals(patientId, patient.getId().getValue());
        assertEquals("Patient/"
                + patientId, observation.getSubject().getReference().getValue());
    }

    @SuppressWarnings("rawtypes")
    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationWithSubjectIncluded_filter_elements()
        throws Exception {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("subject", "Patient/"
                        + patientId).queryParam("_include", "Observation:subject").queryParam("_elements", "status", "category", "subject").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        Coding subsettedTag =
                Coding.builder().system(uri("http://terminology.hl7.org/CodeSystem/v3-ObservationValue")).code(Code.of("SUBSETTED")).display(string("subsetted")).build();
        assertTrue(FHIRUtil.hasTag(bundle, subsettedTag));

        boolean result = false;
        for (Bundle.Entry entry : bundle.getEntry()) {

            if (DEBUG) {

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
        assertEquals(patientId, patient.getId().getValue());
        assertEquals("Patient/"
                + patientId, observation.getSubject().getReference().getValue());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchPatientWithObservationRevIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("_id", patientId).queryParam("_revinclude", "Observation:patient").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
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
        assertEquals(patientId, patient.getId().getValue());
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
                target.path(targetUri).request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationWithSubject() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subject", "Patient/" + patientId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationWithPatient() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("patient", "Patient/"
                        + patientId).request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationWithPatient() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/" + patientId);
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationCodeSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("component-value-quantity", "125.0||mmHg").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationCodeSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "125.0||mmHg");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationCodeLTSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("component-value-quantity", "le126.0||mmHg").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationCodeLTSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "le126.0||mmHg");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void testSearchObservationCodeGTSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("component-value-quantity", "gt123.0||mmHg").request(MediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", "tenant1").get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        SearchAllTest.generateOutput(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationCodeGTSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "gt123.0||mmHg");
        FHIRRequestHeader header =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRResponse response =
                client._search("Observation", parameters, header);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationFilteredSearchParameter1_preferStrict()
        throws Exception {
        // 'category' search parameter is filtered out for tenant1.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("category", "foo");
        FHIRRequestHeader tenantHeader =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRRequestHeader preferStrictHeader =
                new FHIRRequestHeader("Prefer", "handling=strict");
        FHIRResponse response =
                client._search("Observation", parameters, tenantHeader, preferStrictHeader);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());

        if (DEBUG) {
            SearchAllTest.generateOutput(response.getResource(OperationOutcome.class));
        }

        assertExceptionOperationOutcome(response.getResource(OperationOutcome.class), "An error occurred while parsing search parameter 'category'");
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationFilteredSearchParameter1_preferLenient()
        throws Exception {
        // 'category' search parameter is filtered out for tenant1.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("category", "foo");
        FHIRRequestHeader tenantHeader =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRRequestHeader preferStrictHeader =
                new FHIRRequestHeader("Prefer", "handling=lenient");
        FHIRResponse response =
                client._search("Observation", parameters, tenantHeader, preferStrictHeader);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation" })
    public void test_SearchObservationFilteredSearchParameter1_preferBogus()
        throws Exception {
        // 'category' search parameter is filtered out for tenant1.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("category", "foo");
        FHIRRequestHeader tenantHeader =
                new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRRequestHeader preferStrictHeader =
                new FHIRRequestHeader("Prefer", "handling=bogus");
        FHIRResponse response =
                client._search("Observation", parameters, tenantHeader, preferStrictHeader);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
    }
}
