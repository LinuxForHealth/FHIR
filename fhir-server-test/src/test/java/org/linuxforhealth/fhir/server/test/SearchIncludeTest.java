/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.String.of;
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

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.CarePlan;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Measure;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Patient.Link;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.Procedure;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Date;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.AdministrativeGender;
import org.linuxforhealth.fhir.model.type.code.LinkType;
import org.linuxforhealth.fhir.model.type.code.ProcedureStatus;
import org.linuxforhealth.fhir.model.type.code.SearchEntryMode;

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
    private String carePlanId;
    private String measureReportId;
    private String measureId;
    private String libraryId;
    private Instant now = Instant.now();
    private String tag = Long.toString(now.toEpochMilli());

    @Test(groups = { "server-search-include" })
    public void testCreateOrganization1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Organization.
        Organization organization = TestUtil.getMinimalResource(Organization.class);
        organization = organization.toBuilder()
                .name(of(tag))
                .build();

        // Call the 'create' API.
        Entity<Organization> entity = Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the organization's logical id value.
        organization1Id = getLocationLogicalId(response);

        // Add the organization to the resource registry.
        addToResourceRegistry("Organization", organization1Id);
        
        // Next, call the 'read' API to retrieve the new organization and verify it.
        response = target.path("Organization/" + organization1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" })
    public void testCreatePractitioner1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Practitioner and then call the 'create' API.
        Practitioner practitioner = TestUtil.getMinimalResource(Practitioner.class);
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

        // Add the practitioner to the resource registry.
        addToResourceRegistry("Practitioner", practitioner1Id);
        
        // Next, call the 'read' API to retrieve the new practitioner and verify it.
        response = target.path("Practitioner/" + practitioner1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" })
    public void testCreatePatient1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(Patient.class);
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

        // Add the patient to the resource registry.
        addToResourceRegistry("Patient", patient1Id);
        
        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePractitioner1", "testCreatePatient1", "testCreateOrganization1"})
    public void testCreatePatient2() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(Patient.class);
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

        // Add the patient to the resource registry.
        addToResourceRegistry("Patient", patient2Id);
        
        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient2Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreatePatient3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(Patient.class);
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

        // Add the patient to the resource registry.
        addToResourceRegistry("Patient", patient3Id);
        
        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patient3Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreateProcedure1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient.
        Procedure procedure = TestUtil.getMinimalResource(Procedure.class);
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

        // Add the procedure to the resource registry.
        addToResourceRegistry("Procedure", procedure1Id);
        
        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreateProcedure2() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient.
        Procedure procedure = TestUtil.getMinimalResource(Procedure.class);
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

        // Add the procedure to the resource registry.
        addToResourceRegistry("Procedure", procedure2Id);
        
        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure2Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2", "testCreateProcedure2"})
    public void testCreateProcedure3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient
        // and partOf and reasonReference references to another procedure.
        Procedure procedure = TestUtil.getMinimalResource(Procedure.class);
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

        // Add the procedure to the resource registry.
        addToResourceRegistry("Procedure", procedure3Id);
        
        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure3Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" })
    public void testCreateLibrary() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Library and then call the 'create' API.
        Library library = TestUtil.getMinimalResource(Library.class);
        library = library.toBuilder()
                .url(Uri.of("http://example.org/fhir/Library/" + tag))
                .version(org.linuxforhealth.fhir.model.type.String.string("1.0"))
                .name(of(tag))
                .build();

        // Call the 'create' API.
        Entity<Library> entity = Entity.entity(library, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Library").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the library's logical id value.
        libraryId = getLocationLogicalId(response);

        // Add the library to the resource registry.
        addToResourceRegistry("Library", libraryId);
        
        // Next, call the 'read' API to retrieve the new library and verify it.
        response = target.path("Library/" + libraryId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateLibrary"})
    public void testCreateMeasure() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Measure and then call the 'create' API.
        Measure measure = TestUtil.getMinimalResource(Measure.class);
        measure = measure.toBuilder()
                .url(Uri.of("http://example.org/fhir/Measure/" + tag))
                .version(org.linuxforhealth.fhir.model.type.String.string("1.0"))
                .library(Canonical.of("http://example.org/fhir/Library/" + tag + "|1.0"))
                .name(of(tag))
                .build();

        // Call the 'create' API.
        Entity<Measure> entity = Entity.entity(measure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Measure").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the measure's logical id value.
        measureId = getLocationLogicalId(response);

        // Add the measure to the resource registry.
        addToResourceRegistry("Measure", measureId);
        
        // Next, call the 'read' API to retrieve the new measure and verify it.
        response = target.path("Measure/" + measureId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateMeasure"})
    public void testCreateCarePlan() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new CarePlan and then call the 'create' API.
        CarePlan carePlan = TestUtil.getMinimalResource(CarePlan.class);
        carePlan = carePlan.toBuilder()
                .instantiatesCanonical(Canonical.of("http://example.org/fhir/Measure/" + tag))
                .instantiatesUri(Uri.of(tag))
                .build();

        // Call the 'create' API.
        Entity<CarePlan> entity = Entity.entity(carePlan, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("CarePlan").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the carePlan's logical id value.
        carePlanId = getLocationLogicalId(response);

        // Add the carePlan to the resource registry.
        addToResourceRegistry("CarePlan", carePlanId);
        
        // Next, call the 'read' API to retrieve the new carePlan and verify it.
        response = target.path("CarePlan/" + carePlanId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateMeasure"})
    public void testCreateMeasureReport() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new MeasureReport and then call the 'create' API.
        MeasureReport measureReport = TestUtil.getMinimalResource(MeasureReport.class);
        measureReport = measureReport.toBuilder()
                .measure(Canonical.of("http://example.org/fhir/Measure/" + tag + "|2.0"))
                .subject(Reference.builder().reference(of("Patient/" + tag)).build())
                .build();

        // Call the 'create' API.
        Entity<MeasureReport> entity = Entity.entity(measureReport, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("MeasureReport").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the measureReport's logical id value.
        measureReportId = getLocationLogicalId(response);

        // Add the measureReport to the resource registry.
        addToResourceRegistry("MeasureReport", measureReportId);
        
        // Next, call the 'read' API to retrieve the new measureReport and verify it.
        response = target.path("MeasureReport/" + measureReportId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
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
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), organization1Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient1Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 2);
        Patient matchPatient = bundle.getEntry().get(0).getResource().as(Patient.class);
        assertEquals(matchPatient.getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        // validate included elements
        assertEquals(matchPatient.getGender(), AdministrativeGender.FEMALE);
        assertEquals(matchPatient.getManagingOrganization().getReference().getValue(), "Organization/" + organization1Id);
        // validate not included elements
        assertEquals(matchPatient.getName().size(), 0);
        assertNull(matchPatient.getBirthDate());
        assertEquals(matchPatient.getGeneralPractitioner().size(), 0);
        assertEquals(matchPatient.getLink().size(), 0);

        Patient includePatient = bundle.getEntry().get(1).getResource().as(Patient.class);
        assertEquals(includePatient.getId(), patient1Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
        // validate included elements
        assertEquals(includePatient.getGender(), AdministrativeGender.MALE);
        assertEquals(includePatient.getBirthDate(), Date.of(now.toString().substring(0,10)));
        assertEquals(includePatient.getName().get(0).getGiven().get(0).getValue(), "1" + tag);
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
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), procedure3Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), procedure2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 3);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 4);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 4);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<expectedMatchCount; ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(procedure1Id));
        assertTrue(matchResourceIds.contains(procedure2Id));
        assertTrue(matchResourceIds.contains(procedure3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=expectedMatchCount; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getTotal().getValue().intValue(), 3);
        assertEquals(bundle.getEntry().size(), 2);
        String matchResourceId = bundle.getEntry().get(0).getResource().getId();
        assertTrue(matchResourceId.equals(procedure1Id) ||
                   matchResourceId.equals(procedure2Id) ||
                   matchResourceId.equals(procedure3Id));
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getTotal().getValue().intValue(), 3);
        assertEquals(bundle.getEntry().size(), 2);
        String matchResourceId = bundle.getEntry().get(0).getResource().getId();
        assertTrue(matchResourceId.equals(procedure1Id) ||
                   matchResourceId.equals(procedure2Id) ||
                   matchResourceId.equals(procedure3Id));
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 5);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertNull(bundle.getTotal());
        assertEquals(bundle.getEntry().size(), 3);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient3Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 5);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(procedure1Id));
        assertTrue(matchResourceIds.contains(procedure2Id));
        assertTrue(matchResourceIds.contains(procedure3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 4);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(procedure3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
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
        assertEquals(bundle.getEntry().size(), 4);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(practitioner1Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(patient3Id));
        assertTrue(includeResourceIds.contains(organization1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2", "testCreatePatient3"})
    public void testSearchIncludeWithSort() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female,unknown")
                .queryParam("_include", "Patient:*")
                .queryParam("_sort", "birthdate")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 5);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient3Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=2; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(resourceIds.contains(patient1Id));
        assertTrue(resourceIds.contains(organization1Id));
        assertTrue(resourceIds.contains(practitioner1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreatePatient2", "testCreatePatient3"})
    public void testSearchIncludeWithSortDesc() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female,unknown")
                .queryParam("_include", "Patient:*")
                .queryParam("_sort", "-birthdate")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 5);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient3Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=2; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(resourceIds.contains(patient1Id));
        assertTrue(resourceIds.contains(organization1Id));
        assertTrue(resourceIds.contains(practitioner1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateMeasure"})
    public void testSearchCanonicalIncludeSingleIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Measure")
                .queryParam("name", tag)
                .queryParam("_include", "Measure:depends-on:Library")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), measureId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), libraryId);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateCarePlan"})
    public void testSearchCanonicalIncludeNoVersionSingleIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("CarePlan")
                .queryParam("instantiates-uri", tag)
                .queryParam("_include", "CarePlan:instantiates-canonical:Measure")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), carePlanId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), measureId);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateMeasureReport"})
    public void testSearchCanonicalIncludeVersionNoInclude() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("MeasureReport")
                .queryParam("patient", "Patient/" + tag)
                .queryParam("_include", "MeasureReport:measure")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 1);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), measureReportId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateCarePlan"})
    public void testSearchCanonicalIncludeIterate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("CarePlan")
                .queryParam("instantiates-uri", tag)
                .queryParam("_include", "CarePlan:instantiates-canonical:Measure")
                .queryParam("_include:iterate", "Measure:depends-on:Library")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 3);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(carePlanId));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(measureId));
        assertTrue(includeResourceIds.contains(libraryId));
    }

}