/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Patient.Link;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.LinkType;
import com.ibm.fhir.model.type.code.ProcedureStatus;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.SearchEntryMode;

/**
 * These tests execute the _include search behavior.
 */
public class SearchIncludeTest extends FHIRServerTestBase {
    private String patient1Id;
    private String patient2Id;
    private String patient3Id;
    private String practitioner1Id;
    private String organization1Id;
    private String procedure1Id;
    private String procedure2Id;
    private String procedure3Id;
    private Instant now = Instant.now();
    private String tag = Long.toString(now.toEpochMilli());

    @Test(groups = { "server-search-include" })
    public void testCreateOrganization1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Organization.
        Organization organization = TestUtil.getMinimalResource(ResourceType.ORGANIZATION, Format.JSON);
        organization = organization.toBuilder()
                .name(of(tag))
                .build();

        // Call the 'create' API.
        Entity<Organization> entity = Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the organization's logical id value.
        organization1Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new organization and verify it.
        response = target.path("Organization/" + organization1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" })
    public void testCreatePractitioner1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Practitioner and then call the 'create' API.
        Practitioner practitioner = TestUtil.getMinimalResource(ResourceType.PRACTITIONER, Format.JSON);
        practitioner = practitioner.toBuilder()
                .gender(AdministrativeGender.MALE)
                .name(HumanName.builder()
                    .given(of("1" + tag))
                    .build())
                .meta(Meta.builder()
                    .tag(Coding.builder()
                        .code(Code.of(tag))
                        .build())
                    .build())
                .birthDate(Date.of(now.toString().substring(0,10)))
               .build();

        Entity<Practitioner> entity = Entity.entity(practitioner, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Practitioner").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner logical id value.
        practitioner1Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new practitioner and verify it.
        response = target.path("Practitioner/" + practitioner1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" })
    public void testCreatePatient1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(ResourceType.PATIENT, Format.JSON);
        patient = patient.toBuilder()
                .gender(AdministrativeGender.MALE)
                .name(HumanName.builder()
                    .given(of("1" + tag))
                    .build())
                .meta(Meta.builder()
                    .tag(Coding.builder()
                        .code(Code.of(tag))
                        .build())
                    .build())
                .birthDate(Date.of(now.toString().substring(0,10)))
               .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patient1Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePractitioner1", "testCreatePatient1", "testCreateOrganization1"})
    public void testCreatePatient2() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(ResourceType.PATIENT, Format.JSON);
        patient = patient.toBuilder()
                .gender(AdministrativeGender.FEMALE)
                .name(HumanName.builder()
                    .given(of("2" + tag))
                    .build())
                .meta(Meta.builder()
                    .tag(Coding.builder()
                        .code(Code.of(tag))
                        .build())
                    .build())
                .generalPractitioner(Reference.builder().reference(of("Practitioner/" + practitioner1Id)).build())
                .managingOrganization(Reference.builder().reference(of("Organization/" + organization1Id)).build())
                .link(Link.builder().type(LinkType.REFER).other(Reference.builder().reference(of("Patient/" + patient1Id)).build()).build())
                .birthDate(Date.of(now.minus(1, ChronoUnit.DAYS).toString().substring(0,10)))
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patient2Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient2Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreatePatient3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(ResourceType.PATIENT, Format.JSON);
        patient = patient.toBuilder()
                .gender(AdministrativeGender.UNKNOWN)
                .name(HumanName.builder()
                    .given(of("3" + tag))
                    .build())
                .meta(Meta.builder()
                    .tag(Coding.builder()
                        .code(Code.of(tag))
                        .build())
                    .build())
                .generalPractitioner(Reference.builder().reference(of("Practitioner/" + practitioner1Id)).build())
                .managingOrganization(Reference.builder().reference(of("Organization/" + organization1Id)).build())
                .link(Link.builder().type(LinkType.REFER).other(Reference.builder().reference(of("Patient/" + patient2Id)).build()).build())
                .birthDate(Date.of(now.minus(2, ChronoUnit.DAYS).toString().substring(0,10)))
                .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patient3Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient3Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreateProcedure1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient.
        Procedure procedure = TestUtil.getMinimalResource(ResourceType.PROCEDURE, Format.JSON);
        procedure = procedure.toBuilder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder().reference(of("Patient/" + patient2Id)).build())
                .basedOn(Reference.builder().reference(of("CarePlan/" + tag)).build())
                .performed(DateTime.of(now.toString()))
                .instantiatesUri(Uri.of("1" + tag))
                .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("1" + tag)).build()).build())
                .build();

        // Call the 'create' API.
        Entity<Procedure> entity = Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the procedure's logical id value.
        procedure1Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreateProcedure2() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient.
        Procedure procedure = TestUtil.getMinimalResource(ResourceType.PROCEDURE, Format.JSON);
        procedure = procedure.toBuilder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder().reference(of("Patient/" + patient2Id)).build())
                .basedOn(Reference.builder().reference(of("ServiceRequest/" + tag)).build())
                .performed(DateTime.of(now.toString()))
                .instantiatesUri(Uri.of("2" + tag))
                .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("2" + tag)).build()).build())
                .build();

        // Call the 'create' API.
        Entity<Procedure> entity = Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the procedure's logical id value.
        procedure2Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure2Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2", "testCreateProcedure2"})
    public void testCreateProcedure3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient
        // and partOf and reasonReference references to another procedure.
        Procedure procedure = TestUtil.getMinimalResource(ResourceType.PROCEDURE, Format.JSON);
        procedure = procedure.toBuilder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder().reference(of("Patient/" + patient2Id)).build())
                .performed(DateTime.of(now.toString()))
                .instantiatesUri(Uri.of("3" + tag))
                .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("3" + tag)).build()).build())
                .partOf(Reference.builder().reference(of("Procedure/" + procedure2Id)).build())
                .reasonReference(Reference.builder().reference(of("Procedure/" + procedure2Id)).build())
                .build();

        // Call the 'create' API.
        Entity<Procedure> entity = Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the procedure's logical id value.
        procedure3Id = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure3Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @AfterClass
    public void testDeletePatient1() {
        WebTarget target = getWebTarget();
        if (patient1Id != null) {
            Response response   = target.path("Patient/" + patient1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeletePatient2() {
        WebTarget target = getWebTarget();
        if (patient2Id != null) {
            Response response   = target.path("Patient/" + patient2Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeletePatient3() {
        WebTarget target = getWebTarget();
        if (patient3Id != null) {
            Response response   = target.path("Patient/" + patient3Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeleteProcedure1() {
        WebTarget target = getWebTarget();
        if (procedure1Id != null) {
            Response response   = target.path("Procedure/" + procedure1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeleteProcedure2() {
        WebTarget target = getWebTarget();
        if (procedure2Id != null) {
            Response response   = target.path("Procedure/" + procedure2Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeleteProcedure3() {
        WebTarget target = getWebTarget();
        if (procedure3Id != null) {
            Response response   = target.path("Procedure/" + procedure3Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeleteOrganization1() {
        WebTarget target = getWebTarget();
        if (organization1Id != null) {
            Response response   = target.path("Organization/" + organization1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @AfterClass
    public void testDeletePractitioner1() {
        WebTarget target = getWebTarget();
        if (practitioner1Id != null) {
            Response response   = target.path("Practitioner/" + practitioner1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
            assertResponse(response, Response.Status.OK.getStatusCode());
        }
    }

    @Test(groups = { "server-search-include" })
    public void testSearchIncludeWithSortError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_include", "Patient:link")
                .queryParam("_sort", "active")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "_sort search result parameter not supported with _include or _revinclude.");
    }

    @Test(groups = { "server-search-include" })
    public void testSearchIncludeWithSystemSearchError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("")
                .queryParam("_include", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "system search not supported with _include or _revinclude.");
    }

    @Test(groups = { "server-search-include" })
    public void testSearchIncludeWithParseError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_include", "Patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "A value for _include or _revinclude must have at least 2 parts separated by a colon.");
    }

    @Test(groups = { "server-search-include" })
    public void testSearchIncludeWithJoinResourceTypeError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_include", "Procedure:code")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "The join resource type must match the resource type being searched.");
    }

    @Test(groups = { "server-search-include" })
    public void testSearchIncludeUndefinedSearchParameterError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_include", "Patient:badParm")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Undefined Inclusion Parameter: Patient:badParm");
    }

    @Test(groups = { "server-search-include" })
    public void testSearchIncludeWithSearchParameterTypeError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_include", "Patient:address")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Inclusion Parameter must be of type 'reference'. The passed Inclusion Parameter is of type 'string': Patient:address");
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchIncludeSingleIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_include", "Patient:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(2, bundle.getEntry().size());
        assertEquals(patient2Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        assertEquals(organization1Id, bundle.getEntry().get(1).getResource().getId());
        assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(1).getSearch().getMode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchIncludeSingleIncludedResultSameType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_include", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(2, bundle.getEntry().size());
        assertEquals(patient2Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        assertEquals(patient1Id, bundle.getEntry().get(1).getResource().getId());
        assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(1).getSearch().getMode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchIncludeSingleIncludedResultElement() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_elements", "gender,managingOrganization")
                .queryParam("_include", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(2, bundle.getEntry().size());
        Patient matchPatient = bundle.getEntry().get(0).getResource().as(Patient.class);
        assertEquals(patient2Id, matchPatient.getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        // validate included elements
        assertEquals(AdministrativeGender.FEMALE, matchPatient.getGender());
        assertEquals("Organization/" + organization1Id, matchPatient.getManagingOrganization().getReference().getValue());
        // validate not included elements
        assertEquals(0, matchPatient.getName().size());
        assertNull(matchPatient.getBirthDate());
        assertEquals(0, matchPatient.getGeneralPractitioner().size());
        assertEquals(0, matchPatient.getLink().size());

        Patient includePatient = bundle.getEntry().get(1).getResource().as(Patient.class);
        assertEquals(patient1Id, includePatient.getId());
        assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(1).getSearch().getMode());
        // validate included elements
        assertEquals(AdministrativeGender.MALE, includePatient.getGender());
        assertEquals(Date.of(now.toString().substring(0,10)), includePatient.getBirthDate());
        assertEquals("1" + tag, includePatient.getName().get(0).getGiven().get(0).getValue());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure3"})
    public void testSearchIncludeSingleIncludedResultDuplicate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("instantiates-uri", "3" + tag)
                .queryParam("_include", "Procedure:part-of")
                .queryParam("_include", "Procedure:reason-reference")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(2, bundle.getEntry().size());
        assertEquals(procedure3Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        assertEquals(procedure2Id, bundle.getEntry().get(1).getResource().getId());
        assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(1).getSearch().getMode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchIncludeMultipleIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_include", "Patient:organization")
                .queryParam("_include", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(3, bundle.getEntry().size());
        assertEquals(patient2Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(resourceIds.contains(patient1Id));
        assertTrue(resourceIds.contains(organization1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchIncludeWildcardMultipleIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_include", "Patient:*")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(4, bundle.getEntry().size());
        assertEquals(patient2Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(resourceIds.contains(patient1Id));
        assertTrue(resourceIds.contains(organization1Id));
        assertTrue(resourceIds.contains(practitioner1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3" })
    public void testSearchIncludeMultipleMatchAndIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_total", "none")
                .queryParam("date", now.toString())
                .queryParam("_include", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        final int expectedMatchCount = 3;

        assertNotNull(bundle);
        assertNull(bundle.getTotal());
        assertEquals(4, bundle.getEntry().size());
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<expectedMatchCount; ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(matchResourceIds.contains(procedure1Id));
        assertTrue(matchResourceIds.contains(procedure2Id));
        assertTrue(matchResourceIds.contains(procedure3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=expectedMatchCount; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(includeResourceIds.contains(patient2Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3" })
    public void testSearchIncludeMultipleMatchPaged() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("date", now.toString())
                .queryParam("_count", "1")
                .queryParam("_include", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(3, bundle.getTotal().getValue().intValue());
        assertEquals(2, bundle.getEntry().size());
        String matchResourceId = bundle.getEntry().get(0).getResource().getId();
        assertTrue(matchResourceId.equals(procedure1Id) ||
                   matchResourceId.equals(procedure2Id) ||
                   matchResourceId.equals(procedure3Id));
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        assertEquals(patient2Id, bundle.getEntry().get(1).getResource().getId());
        assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(1).getSearch().getMode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3" })
    public void testSearchIncludeMultipleMatchLastPage() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("date", now.toString())
                .queryParam("_count", "2")
                .queryParam("_page", "2")
                .queryParam("_include", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(3, bundle.getTotal().getValue().intValue());
        assertEquals(2, bundle.getEntry().size());
        String matchResourceId = bundle.getEntry().get(0).getResource().getId();
        assertTrue(matchResourceId.equals(procedure1Id) ||
                   matchResourceId.equals(procedure2Id) ||
                   matchResourceId.equals(procedure3Id));
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        assertEquals(patient2Id, bundle.getEntry().get(1).getResource().getId());
        assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(1).getSearch().getMode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3" })
    public void testSearchIncludeAndRevincludeMultipleIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_include", "Patient:organization")
                .queryParam("_revinclude", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(5, bundle.getEntry().size());
        assertEquals(patient2Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(resourceIds.contains(organization1Id));
        assertTrue(resourceIds.contains(procedure1Id));
        assertTrue(resourceIds.contains(procedure2Id));
        assertTrue(resourceIds.contains(procedure3Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient3"})
    public void testSearchIncludeIteratePrimaryType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_total", "none")
                .queryParam("_tag", tag)
                .queryParam("name", "3" + tag)
                .queryParam("_include:iterate", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(3, bundle.getEntry().size());
        assertEquals(patient3Id, bundle.getEntry().get(0).getResource().getId());
        assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(resourceIds.contains(patient2Id));
        assertTrue(resourceIds.contains(patient1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure3"})
    public void testSearchIncludeIterateIncludeType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("date", now.toString())
                .queryParam("_include", "Procedure:patient")
                .queryParam("_include:iterate", "Patient:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(5, bundle.getEntry().size());
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(matchResourceIds.contains(procedure1Id));
        assertTrue(matchResourceIds.contains(procedure2Id));
        assertTrue(matchResourceIds.contains(procedure3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(organization1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure3"})
    public void testSearchIncludeIteratePrimaryAndIncludeType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("instantiates-uri", "3" + tag)
                .queryParam("_include", "Procedure:patient")
                .queryParam("_include:iterate", "Patient:organization", "Procedure:part-of")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(4, bundle.getEntry().size());
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(matchResourceIds.contains(procedure3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(organization1Id));
        assertTrue(includeResourceIds.contains(procedure2Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure3"})
    public void testSearchIncludeIterateRevIncludeType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner")
                .queryParam("name", "1" + tag)
                .queryParam("_revinclude", "Patient:general-practitioner")
                .queryParam("_include:iterate", "Patient:organization")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(4, bundle.getEntry().size());
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.MATCH, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(matchResourceIds.contains(practitioner1Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(SearchEntryMode.INCLUDE, bundle.getEntry().get(i).getSearch().getMode());
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(patient3Id));
        assertTrue(includeResourceIds.contains(organization1Id));
    }

}