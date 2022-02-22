/*
 * (C) Copyright IBM Corp. 2017, 2022
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
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Observation.Component;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Person;
import com.ibm.fhir.model.resource.Person.Link;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.PractitionerRole;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.util.FHIRUtil;

public class SearchTest extends FHIRServerTestBase {
    private static final boolean DEBUG_SEARCH = false;
    private static final String PREFER_HEADER_RETURN_REPRESENTATION = "return=representation";
    private static final String PREFER_HEADER_NAME = "Prefer";
    private String patientId;
    private String patientIdentifierValue;
    private String observationId;
    private Boolean compartmentSearchSupported = null;
    private String practitionerId;
    private String practitionerId2;
    private String allergyIntoleranceId;
    private String practitionerRoleId;
    private String provenanceId;
    private String organizationId;
    private String carePlanId;
    private String conditionId;
    private Patient patientForDuplicationTest = null;
    // Some of the tests run with tenant1 and datastore study1;
    // The others run with the default tenant and the default datastore.
    private final String tenantName = "tenant1";
    private final String dataStoreId = "study1";

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
    public void testCreateOrganization() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Organization and then call the 'create' API.
        Organization organization = TestUtil.getMinimalResource(Organization.class);

        organization = organization.toBuilder().name(com.ibm.fhir.model.type.String.of("test")).build();
        Entity<Organization> entity =
                Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Organization").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the organization's logical id value.
        organizationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new organization and verify it.
        response = target.path("Organization/"
                + organizationId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Organization responseOrganization = response.readEntity(Organization.class);
        TestUtil.assertResourceEquals(organization, responseOrganization);

        // Call the 'update' API.
        entity = Entity.entity(responseOrganization, FHIRMediaType.APPLICATION_FHIR_JSON);
        response =
                target.path("Organization/" + organizationId).request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .put(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new organization and verify version is 2.
        response = target.path("Organization/"
                + organizationId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        organization = response.readEntity(Organization.class);
        assertEquals("2", organization.getMeta().getVersionId().getValue());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateOrganization" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();
        patientIdentifierValue = UUID.randomUUID().toString();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        patient = patient.toBuilder()
                .gender(AdministrativeGender.MALE)
                .identifier(Identifier.builder()
                    .value(string(patientIdentifierValue))
                    .system(uri("test"))
                    .type(CodeableConcept.builder()
                        .coding(Coding.builder().code(Code.of("typecodea")).system(Uri.of("typesystema")).build())
                        .coding(Coding.builder().code(Code.of("typecodeb")).build())
                        .coding(Coding.builder().code(Code.of("official")).system(Uri.of("http://hl7.org/fhir/identifier-use")).build())
                        .build())
                    .build())
                .managingOrganization(Reference.builder()
                    .reference(com.ibm.fhir.model.type.String.of("Organization/" + organizationId + "/_history/1"))
                    .build())
                .build();
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
                target.path("Patient")
                .queryParam("_id", patientId)
                .queryParam("_count", "1002")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        // Check that count is set to maxPageSize (1001) for the tenant
        String selfLink = getSelfLink(bundle);
        assertTrue(selfLink.contains("_count=1001"));
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
        // Check that count is set to defaultPageSize (11) for the tenant
        String selfLink = getSelfLink(bundle);
        assertTrue(selfLink.contains("_count=11"));
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

        response =
                target.path("Patient").queryParam("gender", "MALE").request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Patient").queryParam("gender", "http://hl7.org/fhir/administrative-gender|MALE").request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
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

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithGenderNot() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("gender:not", "female").request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithIdentifierValueOnly() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier", patientIdentifierValue)
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
    public void testSearchPatientWithIdentifier() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier", "test|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Patient").queryParam("identifier", "test|"+ patientIdentifierValue.toUpperCase())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithIdentifierNotFound() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier", "typesystema|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithIdentifierOfTypeValueOnly() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier:of-type", patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Search parameter 'identifier' with modifier ':of-type' requires at least a code and value");
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithIdentifierOfTypeCodeAndValueOnly() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier:of-type", "typecodeb|"+ patientIdentifierValue)
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
    public void testSearchPatientWithIdentifierOfType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier:of-type", "typesystema|typecodea|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Patient").queryParam("identifier:of-type", "typesystema|TYPECODEA|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Patient").queryParam("identifier:of-type", "http://hl7.org/fhir/identifier-use|official|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Patient").queryParam("identifier:of-type", "http://hl7.org/fhir/identifier-use|OFFICIAL|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithIdentifierOfTypeNotFound() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier:of-type", "test|typecodeb|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
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

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient", "testCreatePractitioner"})
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation =
                TestUtil.buildPatientObservation(patientId, practitionerId, "Observation1.json");
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

        printOutResource(DEBUG_SEARCH, responseObservation);

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
    public void testSearchObservationWithCodeDisplayText() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("_id", observationId)
                    .queryParam("code:text", "blood pressure")
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
    public void testSearchObservationWithCodeText() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("_id", observationId)
                    .queryParam("code:text", "bp")
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
    public void testSearchObservationWithCodeTextNotFound() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation").queryParam("_id", observationId)
                    .queryParam("code:text", "textNotFound")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
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
                .queryParam("_elements", "status,category,subject")
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
            printOutResource(DEBUG_SEARCH, entry.getResource());

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
                target.path("Patient").queryParam("_id", patientId)
                .queryParam("_revinclude", "Observation:patient")
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

    /**
     * 'patient' is a configured Observation search parameter for tenant1,
     * so this tests that compartment info is extracted and searchable
     * when the related parameter is not filtered out
     */
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
        // verify link does not include consecutive '&' characters
        assertTrue(bundle.getLink().size() >= 1);
        assertFalse(bundle.getLink().get(0).getUrl().getValue().contains("&&"));
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation", "retrieveConfig" })
    public void testSearchObservationWithPatientCompartmentViaPost() {
        assertNotNull(compartmentSearchSupported);
        if (!compartmentSearchSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();
        String targetUri = "Patient/" + patientId + "/Observation/_search";
        Response response =
                target.path(targetUri).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(Entity.form(new Form()));
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    /**
     * 'performer' is not a configured Observation search parameter for tenant1,
     * so this tests that compartment info is still extracted and searchable
     * when the related parameter is filtered
     */
    @Test(groups = { "server-search" }, dependsOnMethods = {
            "testCreateObservation", "retrieveConfig" })
    public void testSearchObservationWithPractitionerCompartment() {
        assertNotNull(compartmentSearchSupported);
        if (!compartmentSearchSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();
        String targetUri = "Practitioner/" + practitionerId + "/Observation";
        Response response =
                target.path(targetUri).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        // verify link does not include consecutive '&' characters
        assertTrue(bundle.getLink().size() >= 1);
        assertFalse(bundle.getLink().get(0).getUrl().getValue().contains("&&"));

        // Also verify a negative search
        targetUri = "Practitioner/" + practitionerId2 + "/Observation";
        response =
                target.path(targetUri).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
        // verify link does not include consecutive '&' characters
        assertTrue(bundle.getLink().size() >= 1);
        assertFalse(bundle.getLink().get(0).getUrl().getValue().contains("&&"));
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
    public void testSearchObservationValueOnly() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("component-value-quantity", "125.0");
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
                target.path("Observation")
                        .queryParam("component-value-quantity", "gt123.0||mmHg")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

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

        OperationOutcome oo = response.getResource(OperationOutcome.class);
        printOutResource(DEBUG_SEARCH, oo);
        assertExceptionOperationOutcome(oo, "Search parameter 'category' for resource type 'Observation' was not found.");
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

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_total_none() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_total", "none")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertNull(bundle.getTotal());
        assertTrue(bundle.getEntry().size() == 1);
        assertNotNull(getSelfLink(bundle));
        assertNull(getNextLink(bundle));
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_total_none_exact_count() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_count", "1")
                .queryParam("_total", "none")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertNull(bundle.getTotal());
        assertTrue(bundle.getEntry().size() == 1);
        assertNotNull(getSelfLink(bundle));
        assertNotNull(getNextLink(bundle));
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_total_estimate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_total", "estimate")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getTotal().getValue().equals(1));
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitioner" })
    public void testSearchPractitioner_total_accurate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner").queryParam("_id", practitionerId)
                .queryParam("_total", "accurate")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getTotal().getValue().equals(1));
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateObservation" })
    public void testSearchObservationWithSubjectIncluded_summary_text() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Observation")
                        .queryParam("subject", "Patient/"+ patientId)
                        .queryParam("_include", "Observation:subject")
                        .queryParam("_summary", "text")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "_include and _revinclude are not supported with '_summary=text'");
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
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "_include and _revinclude are not supported with '_summary=text'");
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
        patientForDuplicationTest = response.readEntity(Patient.class);
        assertNotNull(patientForDuplicationTest);

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
            List<Resource> lstRes = new ArrayList<>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> patientSet = new HashSet<>();
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
            List<Resource> lstRes = new ArrayList<>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> patientSet = new HashSet<>();
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
            List<Resource> lstRes = new ArrayList<>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> patientSet = new HashSet<>();
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
            List<Resource> lstRes = new ArrayList<>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> patientSet = new HashSet<>();
            for (Entry entry: bundle.getEntry()) {
                patientSet.add(((Patient) entry.getResource()).getId());
            }
            assertTrue(bundle.getEntry().size() == patientSet.size());
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateOrganization" })
    public void testCreatePractitionerRole() throws Exception {
        WebTarget target = getWebTarget();

        PractitionerRole practitionerRole = TestUtil.readLocalResource("PractitionerRole.json");

        practitionerRole = practitionerRole.toBuilder()
                .organization(Reference.builder()
                    .reference(com.ibm.fhir.model.type.String.of("Organization/" + organizationId + "/_history/2"))
                    .build())
                .build();
        Entity<PractitionerRole> entity =
                Entity.entity(practitionerRole, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("PractitionerRole").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner role's logical id value.
        practitionerRoleId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new practitioner role and verify it.
        response = target.path("PractitionerRole/"
                + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        PractitionerRole responsePractitionerRole =
                response.readEntity(PractitionerRole.class);

        printOutResource(DEBUG_SEARCH, responsePractitionerRole);

        // use it for search
        practitionerRoleId = responsePractitionerRole.getId();
        TestUtil.assertResourceEquals(practitionerRole, responsePractitionerRole);
    }

    @Test(groups = { "server-search" })
    public void testCreatePractitionerForAllergyIntolerance() throws Exception {
        WebTarget target = getWebTarget();

        Practitioner practitioner = TestUtil.readLocalResource("Practitioner.json");

        Entity<Practitioner> entity =
                Entity.entity(practitioner, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Practitioner").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        practitionerId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new practitioner and verify it.
        response = target.path("Practitioner/"
                + practitionerId2).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Practitioner responsePractitioner =
                response.readEntity(Practitioner.class);

        printOutResource(DEBUG_SEARCH, responsePractitioner);

        // use it for search
        practitionerId2 = responsePractitioner.getId();
        TestUtil.assertResourceEquals(practitioner, responsePractitioner);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient", "testCreatePractitionerForAllergyIntolerance", "testCreatePractitionerRole" })
    public void testCreateAllergyIntolerance() throws Exception {
        WebTarget target = getWebTarget();

        AllergyIntolerance allergyIntolerance = TestUtil.readLocalResource("AllergyIntolerance.json");

        allergyIntolerance = allergyIntolerance
            .toBuilder()
            .patient(Reference.builder().reference(string("Patient/" + patientId)).build())
            .recorder(Reference.builder().reference(string("Practitioner/" + practitionerId2)).build())
            .asserter(Reference.builder().reference(string("PractitionerRole/" + practitionerRoleId)).build())
            .build();

        Entity<AllergyIntolerance> entity =
                Entity.entity(allergyIntolerance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("AllergyIntolerance").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the allergy intolerance's logical id value.
        allergyIntoleranceId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new allergy intolerance and verify it.
        response = target.path("AllergyIntolerance/"
                + allergyIntoleranceId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        AllergyIntolerance responseAllergyIntolerance =
                response.readEntity(AllergyIntolerance.class);

        printOutResource(DEBUG_SEARCH, responseAllergyIntolerance);

        // use it for search
        allergyIntoleranceId = responseAllergyIntolerance.getId();
        TestUtil.assertResourceEquals(allergyIntolerance, responseAllergyIntolerance);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateAllergyIntolerance" })
    public void testCreateProvenance() throws Exception {
        WebTarget target = getWebTarget();

        Provenance provenance = TestUtil.readLocalResource("Provenance.json");

        provenance = provenance
            .toBuilder()
            .target(Reference.builder().reference(string("AllergyIntolerance/" + allergyIntoleranceId)).build())
            .build();

        Entity<Provenance> entity =
                Entity.entity(provenance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Provenance").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the allergy intolerance's logical id value.
        provenanceId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new allergy intolerance and verify it.
        response = target.path("Provenance/"
                + provenanceId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Provenance responseProvenance =
                response.readEntity(Provenance.class);

        printOutResource(DEBUG_SEARCH, responseProvenance);

        // use it for search
        provenanceId = responseProvenance.getId();
        TestUtil.assertResourceEquals(provenance, responseProvenance);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateAllergyIntolerance", "testCreateProvenance" })
    public void testSearchAllergyIntoleranceWithWildcardMultipleIncludedAndProvenceRevIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("AllergyIntolerance")
                        .queryParam("patient", "Patient/" + patientId)
                        .queryParam("_include", "AllergyIntolerance:*:Patient", "AllergyIntolerance:*:Practitioner", "AllergyIntolerance:*:PractitionerRole")
                        .queryParam("_revinclude", "Provenance:*")
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 5);
        AllergyIntolerance allergyIntolerance = null;
        Patient patient = null;
        Practitioner practitioner = null;
        PractitionerRole practitionerRole = null;
        Provenance provenance = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof AllergyIntolerance) {
                    allergyIntolerance = (AllergyIntolerance) entry.getResource();
                } else if (entry.getResource() instanceof Patient) {
                    patient = (Patient) entry.getResource();
                } else if (entry.getResource() instanceof Practitioner) {
                    practitioner = (Practitioner) entry.getResource();
                } else if (entry.getResource() instanceof PractitionerRole) {
                    practitionerRole = (PractitionerRole) entry.getResource();
                } else if (entry.getResource() instanceof Provenance) {
                    provenance = (Provenance) entry.getResource();
                }
            }
        }
        assertNotNull(allergyIntolerance);
        assertNotNull(patient);
        assertNotNull(practitioner);
        assertNotNull(practitionerRole);
        assertNotNull(provenance);
        assertEquals(patientId, patient.getId());
        assertEquals(practitionerId2, practitioner.getId());
        assertEquals(practitionerRoleId, practitionerRole.getId());
        assertEquals(provenanceId, provenance.getId());
        assertEquals("Patient/" + patientId, allergyIntolerance.getPatient().getReference().getValue());
        assertEquals("Practitioner/" + practitionerId2, allergyIntolerance.getRecorder().getReference().getValue());
        assertEquals("PractitionerRole/" + practitionerRoleId, allergyIntolerance.getAsserter().getReference().getValue());
        for (Reference reference : provenance.getTarget()) {
            if (reference.getReference() != null) {
                assertEquals("AllergyIntolerance/" + allergyIntoleranceId, reference.getReference().getValue());
            }
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateAllergyIntolerance", "testCreateProvenance" })
    public void testSearchWithRelativePatientId() {
        WebTarget target = getWebTarget();
        Response response = target.path("AllergyIntolerance")
                .queryParam("patient", patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        AllergyIntolerance allergyIntolerance = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof AllergyIntolerance) {
                allergyIntolerance = (AllergyIntolerance) entry.getResource();
            }
        }
        assertNotNull(allergyIntolerance);
        assertEquals(allergyIntoleranceId, allergyIntolerance.getId());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithValidVersionedOrganizationIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("organization", "Organization/" + organizationId)
                                        .queryParam("_include", "Patient:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
        Organization organization = null;
        Patient patient = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof Organization) {
                    organization = (Organization) entry.getResource();
                } else if (entry.getResource() instanceof Patient) {
                    patient = (Patient) entry.getResource();
                }
            }
        }
        assertNotNull(organization);
        assertNotNull(patient);
        assertEquals(organizationId, organization.getId());
        assertEquals("1", organization.getMeta().getVersionId().getValue());
        assertEquals(patientId, patient.getId());
        assertEquals("Organization/"
                + organizationId + "/_history/1", patient.getManagingOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitionerRole" })
    public void testSearchPractitionerRoleWithValidVersionedOrganizationIncluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("PractitionerRole").queryParam("organization", "Organization/" + organizationId)
                                                 .queryParam("_include", "PractitionerRole:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
        Organization organization = null;
        PractitionerRole practitionerRole = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof Organization) {
                    organization = (Organization) entry.getResource();
                } else if (entry.getResource() instanceof PractitionerRole) {
                    practitionerRole = (PractitionerRole) entry.getResource();
                }
            }
        }
        assertNotNull(organization);
        assertNotNull(practitionerRole);
        assertEquals(organizationId, organization.getId());
        assertEquals("2", organization.getMeta().getVersionId().getValue());
        assertEquals(practitionerRoleId, practitionerRole.getId());
        assertEquals("Organization/"
                + organizationId + "/_history/2", practitionerRole.getOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePractitionerRole" })
    public void testSearchOrganizationWithPractitionerRoleValidVersionedReferenceRevincluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Organization").queryParam("_id", organizationId)
                                                 .queryParam("_revinclude", "PractitionerRole:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
        Organization organization = null;
        PractitionerRole practitionerRole = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null) {
                if (entry.getResource() instanceof Organization) {
                    organization = (Organization) entry.getResource();
                } else if (entry.getResource() instanceof PractitionerRole) {
                    practitionerRole = (PractitionerRole) entry.getResource();
                }
            }
        }
        assertNotNull(organization);
        assertNotNull(practitionerRole);
        assertEquals(organizationId, organization.getId());
        assertEquals("2", organization.getMeta().getVersionId().getValue());
        assertEquals(practitionerRoleId, practitionerRole.getId());
        assertEquals("Organization/"
                + organizationId + "/_history/2", practitionerRole.getOrganization().getReference().getValue());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchOrganizationWithPatientInvalidVersionedReferenceRevincluded() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Organization").queryParam("_id", organizationId)
                                                 .queryParam("_revinclude", "Patient:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
        Organization organization = (Organization) bundle.getEntry().get(0).getResource();
        assertNotNull(organization);
        assertEquals(organizationId, organization.getId());
        assertEquals("2", organization.getMeta().getVersionId().getValue());
    }

    @Test(groups = { "server-search" })
    public void test_SearchCarePlan_APDate() throws Exception {
        WebTarget target = getWebTarget();

        // Create the CarePlan that has a start date but no end date
        CarePlan carePlan = TestUtil.readLocalResource("CarePlan.json");
        Entity<CarePlan> entity =
                Entity.entity(carePlan, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("CarePlan").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        carePlanId = getLocationLogicalId(response);

        // Search for the CarePlan with an approximate date search
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", carePlanId);
        parameters.searchParam("date", "ap2021-01-01");
        FHIRResponse fhirResponse = client._search("CarePlan", parameters);
        assertResponse(fhirResponse.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = fhirResponse.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
        CarePlan responseCarePlan = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof CarePlan) {
                responseCarePlan = (CarePlan) entry.getResource();
            }
        }
        assertNotNull(responseCarePlan);
        assertEquals(carePlanId, responseCarePlan.getId());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateAllergyIntolerance" })
    public void testSearchAllergyIntoleranceWithClinicalStatusIn() {
        WebTarget target = getWebTarget();
        Response response = target.path("AllergyIntolerance")
                .queryParam("_id", allergyIntoleranceId)
                .queryParam("clinical-status:in", "http://hl7.org/fhir/ValueSet/allergyintolerance-clinical")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
        assertEquals(allergyIntoleranceId, bundle.getEntry().get(0).getResource().getId());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateAllergyIntolerance" })
    public void testSearchAllergyIntoleranceWithClinicalStatusNotIn() {
        WebTarget target = getWebTarget();
        Response response = target.path("AllergyIntolerance")
                .queryParam("_id", allergyIntoleranceId)
                .queryParam("clinical-status:not-in", "http://hl7.org/fhir/ValueSet/allergyintolerance-verification")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
        assertEquals(allergyIntoleranceId, bundle.getEntry().get(0).getResource().getId());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateAllergyIntolerance" })
    public void testSearchAllergyIntoleranceWithClinicalStatusInNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("AllergyIntolerance")
                .queryParam("_id", allergyIntoleranceId)
                .queryParam("clinical-status:in", "http://hl7.org/fhir/ValueSet/allergyintolerance-verification")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateAllergyIntolerance" })
    public void testSearchAllergyIntoleranceWithClinicalStatusNotInNotFound() {
        WebTarget target = getWebTarget();
        Response response = target.path("AllergyIntolerance")
                .queryParam("_id", allergyIntoleranceId)
                .queryParam("clinical-status:not-in", "http://hl7.org/fhir/ValueSet/allergyintolerance-clinical")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateAllergyIntolerance" })
    public void testSearchAllergyIntoleranceWithImplicitCodeSystemIn() {
        WebTarget target = getWebTarget();
        Response response = target.path("AllergyIntolerance")
                .queryParam("_id", allergyIntoleranceId)
                .queryParam("category:in", "http://hl7.org/fhir/ValueSet/allergy-intolerance-category")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
        assertEquals(allergyIntoleranceId, bundle.getEntry().get(0).getResource().getId());
    }

    @Test(groups = { "server-search" })
    public void testCreateCondition() throws Exception {
        WebTarget target = getWebTarget();

        Condition condition = TestUtil.readLocalResource("Condition.json");

        Entity<Condition> entity =
                Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Condition").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the condition's logical id value.
        conditionId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new condition and verify it.
        response = target.path("Condition/"
                + conditionId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Condition responseCondition =
                response.readEntity(Condition.class);

        printOutResource(DEBUG_SEARCH, responseCondition);

        // use it for search
        conditionId = responseCondition.getId();
        TestUtil.assertResourceEquals(condition, responseCondition);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateCondition"})
    public void testSearchConditionClinicalStatusAbove() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Condition").queryParam("clinical-status:above", "http://terminology.hl7.org/CodeSystem/condition-clinical|relapse")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Condition condition = ((Condition) entry.getResource());
            List<Coding> codings = condition.getClinicalStatus().getCoding();
            assertNotNull(codings);
            boolean passed = false;
            for (Coding coding : codings) {
                if (coding.getSystem().getValue().equals("http://terminology.hl7.org/CodeSystem/condition-clinical") &&
                        (coding.getCode().getValue().equals("active") || coding.getCode().getValue().equals("relapse"))) {
                    passed = true;
                    break;
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateCondition" })
    public void testSearchConditionVerificationStatusBelow() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Condition").queryParam("verification-status:below", "http://terminology.hl7.org/CodeSystem/condition-ver-status|unconfirmed")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Entry entry : bundle.getEntry()) {
            Condition condition = ((Condition) entry.getResource());
            List<Coding> codings = condition.getVerificationStatus().getCoding();
            assertNotNull(codings);
            boolean passed = false;
            for (Coding coding : codings) {
                if (coding.getSystem().getValue().equals("http://terminology.hl7.org/CodeSystem/condition-ver-status") &&
                        (coding.getCode().getValue().equals("unconfirmed") || coding.getCode().getValue().equals("provisional") ||
                                coding.getCode().getValue().equals("differential"))) {
                    passed = true;
                    break;
                }
            }
            assertTrue(passed);
        }
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateCondition" })
    public void testSearchConditionClinicalStatusAboveNotFound() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Condition")
                .queryParam("_id", conditionId)
                .queryParam("clinical-status:above", "http://terminology.hl7.org/CodeSystem/condition-clinical|inactive")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertEquals(bundle.getEntry().size(), 0);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateCondition" })
    public void testSearchConditionVerificationStatusBelowNotFound() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Condition")
                .queryParam("_id", conditionId)
                .queryParam("verification-status:below", "http://terminology.hl7.org/CodeSystem/condition-ver-status|refuted")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertEquals(bundle.getEntry().size(), 0);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateCondition" })
    public void testSearchConditionEvidenceCaseSensitivity() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Condition")
                .queryParam("evidence", "http://terminology.hl7.org/CodeSystem/v3-ObservationValue|A4")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Condition")
                .queryParam("evidence", "http://terminology.hl7.org/CodeSystem/v3-ObservationValue|a4")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().isEmpty());

        response =
                target.path("Condition")
                .queryParam("evidence", "A4")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Condition")
                .queryParam("evidence", "a4")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().isEmpty());

        response =
                target.path("Condition")
                .queryParam("evidence", "http://terminology.hl7.org/CodeSystem/v2-0080|ST")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Condition")
                .queryParam("evidence", "http://terminology.hl7.org/CodeSystem/v2-0080|st")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Condition")
                .queryParam("evidence", "st")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreateCondition" })
    public void testSearchConditionEvidenceCodeAndSystem() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Condition")
                .queryParam("evidence", "http://terminology.hl7.org/CodeSystem/v3-ObservationValue|A4")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Condition")
                .queryParam("evidence", "A4")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);

        response =
                target.path("Condition")
                    .queryParam("evidence", "http://terminology.hl7.org/CodeSystem/v3-ObservationValue|")
                    .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search" })
    public void testSearchWithTenant2_OverrideUri() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        this.printOutResource(true, bundle);
        assertNotNull(bundle);
        String selfLink = getSelfLink(bundle);
        assertEquals(selfLink, "https://chocolate.fudge/Patient?_count=11&_page=1");
    }

    @Test(groups = { "server-search" }, dependsOnMethods = {"testCreatePatient" })
    public void testSearchPatientWithIdentifierNoData() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient").queryParam("identifier", "test|"+ patientIdentifierValue)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .header("Prefer", "return=minimal")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertNull(bundle.getEntry().get(0).getResource());
        assertNotNull(bundle.getEntry().get(0).getResponse());

        response =
                target.path("Patient").queryParam("identifier", "test|"+ patientIdentifierValue.toUpperCase())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .header("Prefer", "return=minimal")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertNull(bundle.getEntry().get(0).getResource());
        assertNotNull(bundle.getEntry().get(0).getResponse());
    }
}