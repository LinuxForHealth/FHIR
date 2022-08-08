/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.String.of;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.CarePlan;
import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Measure;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.NutritionOrder;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Patient.Link;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.Procedure;
import org.linuxforhealth.fhir.model.resource.Procedure.Performer;
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
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.EncounterStatus;
import org.linuxforhealth.fhir.model.type.code.HTTPVerb;
import org.linuxforhealth.fhir.model.type.code.LinkType;
import org.linuxforhealth.fhir.model.type.code.NutritionOrderIntent;
import org.linuxforhealth.fhir.model.type.code.NutritionOrderStatus;
import org.linuxforhealth.fhir.model.type.code.ProcedureStatus;
import org.linuxforhealth.fhir.model.type.code.SearchEntryMode;

/**
 * These tests execute the _revinclude search behavior.
 */
public class SearchRevIncludeTest extends FHIRServerTestBase {
    private String patient1Id;
    private String patient2Id;
    private String patient3Id;
    private String practitioner1Id;
    private String organization1Id;
    private String procedure1Id;
    private String procedure2Id;
    private String procedure3Id;
    private String procedure4Id;
    private String procedure5Id;
    private String encounter1Id;
    private List<String> nutritionOrderIds = new ArrayList<>();
    private String carePlanId;
    private String measureReportId;
    private String measureId;
    private String libraryId;
    private Instant now = Instant.now();
    private String tag = Long.toString(now.toEpochMilli());

    @Test(groups = { "server-search-revinclude" })
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

    @Test(groups = { "server-search-revinclude" })
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

    @Test(groups = { "server-search-revinclude" })
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePractitioner1", "testCreatePatient1", "testCreateOrganization1"})
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreatePatient3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.getMinimalResource(Patient.class);
        patient = patient.toBuilder()
                .gender(AdministrativeGender.MALE)
                .name(HumanName.builder()
                    .given(of("3" + tag))
                    .build())
                .meta(Meta.builder()
                    .tag(Coding.builder()
                        .code(Code.of(tag))
                        .build())
                    .build())
                .birthDate(Date.of(now.minus(2, ChronoUnit.DAYS).toString().substring(0,10)))
                .link(Link.builder().type(LinkType.REFER).other(Reference.builder().reference(of("Patient/" + patient2Id)).build()).build())
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreateProcedure3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add performer reference to patient.
        Procedure procedure = TestUtil.getMinimalResource(Procedure.class);
        procedure = procedure.toBuilder()
                .status(ProcedureStatus.COMPLETED)
                .performer(Performer.builder().actor(Reference.builder().reference(of("Patient/" + patient2Id)).build()).build())
                .performed(DateTime.of(now.toString()))
                .instantiatesUri(Uri.of("3" + tag))
                .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("3" + tag)).build()).build())
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient1"})
    public void testCreateProcedure4() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient.
        Procedure procedure = TestUtil.getMinimalResource(Procedure.class);
        procedure = procedure.toBuilder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder().reference(of("Patient/" + patient1Id)).build())
                .performed(DateTime.of(now.toString()))
                .instantiatesUri(Uri.of("4" + tag))
                .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("4" + tag)).build()).build())
                .build();

        // Call the 'create' API.
        Entity<Procedure> entity = Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the procedure's logical id value.
        procedure4Id = getLocationLogicalId(response);

        // Add the procedure to the resource registry.
        addToResourceRegistry("Procedure", procedure4Id);
        
        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure4Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient3", "testCreateProcedure2"})
    public void testCreateProcedure5() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Procedure and add subject reference to patient
        // and partOf and reasonReference references to another procedure.
        Procedure procedure = TestUtil.getMinimalResource(Procedure.class);
        procedure = procedure.toBuilder()
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder().reference(of("Patient/" + patient3Id)).build())
                .performed(DateTime.of(now.toString()))
                .instantiatesUri(Uri.of("5" + tag))
                .code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("5" + tag)).build()).build())
                .partOf(Reference.builder().reference(of("Procedure/" + procedure2Id)).build())
                .reasonReference(Reference.builder().reference(of("Procedure/" + procedure2Id)).build())
                .build();

        // Call the 'create' API.
        Entity<Procedure> entity = Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the procedure's logical id value.
        procedure5Id = getLocationLogicalId(response);

        // Add the procedure to the resource registry.
        addToResourceRegistry("Procedure", procedure5Id);
        
        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedure5Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
    public void testCreateEncounter1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Encounter and add subject reference to patient.
        Encounter encounter = TestUtil.getMinimalResource(Encounter.class);
        encounter = encounter.toBuilder()
                .status(EncounterStatus.FINISHED)
                .subject(Reference.builder().reference(of("Patient/" + patient2Id)).build())
                .build();

        // Call the 'create' API.
        Entity<Encounter> entity = Entity.entity(encounter, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Encounter").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the encounter's logical id value.
        encounter1Id = getLocationLogicalId(response);

        // Add the encounter to the resource registry.
        addToResourceRegistry("Encounter", encounter1Id);
        
        // Next, call the 'read' API to retrieve the new encounter and verify it.
        response = target.path("Encounter/" + encounter1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient3"})
    public void testCreateNutritionOrders() throws Exception {
        // Build a new NutritionOrder and add reference to patient.
        NutritionOrder nutritionOrder = TestUtil.getMinimalResource(NutritionOrder.class);
        nutritionOrder = nutritionOrder.toBuilder()
                .status(NutritionOrderStatus.ACTIVE)
                .intent(NutritionOrderIntent.DIRECTIVE)
                .dateTime(DateTime.of(now.toString()))
                .patient(Reference.builder().reference(of("Patient/" + patient3Id)).build())
                .build();

        String uuid = UUID.randomUUID().toString();

        // Generate a Batch
        ExecutorService svc = Executors.newFixedThreadPool(3);
        List<Future<NutritionOrderCallableResult>> futures = new ArrayList<>();
        int count = 0;
        for (int j = 0; j <10; j++) {
            Bundle.Builder bundleBuilder = Bundle.builder();
            List<Bundle.Entry> entries = new ArrayList<>();
            for (int i=0; i<101; ++i) {
                Bundle.Entry.Request request = Bundle.Entry.Request.builder()
                        .url(Uri.uri("NutritionOrder/" + uuid + "-" + count))
                        .method(HTTPVerb.PUT)
                        .build();
                Bundle.Entry entry = Bundle.Entry.builder()
                        .request(request)
                        .resource(nutritionOrder.toBuilder().id(uuid + "-" + count).build())
                        .build();
                if (count <= 1001) {
                    entries.add(entry);
                    count++;
                }
            }
            Bundle bundle = bundleBuilder
                    .type(BundleType.TRANSACTION)
                    .entry(entries)
                    .build();

            // Call the 'batch' API.
            Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
            NutritionOrderCallable callable = new NutritionOrderCallable(getWebTarget(), entity);
            Future<NutritionOrderCallableResult> future = svc.submit(callable);
            Thread.sleep(1000);
            futures.add(future);
        }

        boolean finished = false;
        int completed = 0;
        int terminal = 0;
        while (!finished && terminal++ != 300) {
            for (Future<NutritionOrderCallableResult> future : futures) {
                if (future.isDone()) {
                    if (!future.get().complete) {
                        nutritionOrderIds.addAll(future.get().results);
                        future.get().complete = Boolean.TRUE;
                        completed++;
                    }
                } else if (future.isCancelled()) {
                    svc.shutdown();
                    throw new Exception("Failed");
                }
            }
            finished = futures.size() == completed;
            Thread.sleep(1000);
        }
        System.out.println("Nutrition Order Size: " + nutritionOrderIds.size());

        assertTrue(nutritionOrderIds.size() > 1000);
    }

    public static class NutritionOrderCallableResult {
        List<String> results = new ArrayList<>();
        Boolean complete = Boolean.FALSE;
        Boolean deleted = Boolean.FALSE;
    }

    public static class NutritionOrderCallable implements Callable<NutritionOrderCallableResult> {
        private Boolean DEBUG = Boolean.FALSE;
        private WebTarget target = null;
        private Entity<Bundle> entity = null;

        public NutritionOrderCallable(WebTarget target, Entity<Bundle> entity) {
            this.target = target;
            this.entity = entity;
        }

        @Override
        public NutritionOrderCallableResult call() throws Exception {
            NutritionOrderCallableResult result = new NutritionOrderCallableResult();
            Response response = target.path("/").request().post(entity, Response.class);
            assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL);
            Bundle bundleResponse = response.readEntity(Bundle.class);
            assertFalse(bundleResponse.getEntry().isEmpty());
            for (Bundle.Entry entry : bundleResponse.getEntry()) {
                assertEquals(entry.getResponse().getStatus().getValue(), "201");
                result.results.add(entry.getResponse().getId());
            }
            if (DEBUG) {
                System.out.println(this.hashCode() + " " + result.results.size());
            }
            return result;
        }
    }

    @Test(groups = { "server-search-revinclude" })
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateLibrary"})
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateMeasure"})
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

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateMeasure"})
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

    @AfterClass
    public void testDeleteNutritionOrders() throws Exception {
        Iterator<String> iter = nutritionOrderIds.iterator();
        // Generate a Batch
        ExecutorService svc = Executors.newFixedThreadPool(5);
        List<Future<NutritionOrderCallableResult>> futures = new ArrayList<>();
        int count = 0;
        for (int j = 0; j <10; j++) {
            Bundle.Builder bundleBuilder = Bundle.builder();
            List<Bundle.Entry> entries = new ArrayList<>();
            for (int i=0; i<101; ++i) {
                if (iter.hasNext()) {
                    Bundle.Entry.Request request = Bundle.Entry.Request.builder()
                            .url(Uri.uri("NutritionOrder/" + iter.next()))
                            .method(HTTPVerb.DELETE)
                            .build();
                    Bundle.Entry entry = Bundle.Entry.builder()
                            .request(request)
                            .build();
                    if (count <= 1000) {
                        entries.add(entry);
                        count++;
                    }
                }
            }
            Bundle bundle = bundleBuilder
                    .type(BundleType.BATCH)
                    .entry(entries)
                    .build();

            // Call the 'batch' API.
            Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
            NutritionOrderCallableDelete callable = new NutritionOrderCallableDelete(getWebTarget(), entity, this);
            Future<NutritionOrderCallableResult> future = svc.submit(callable);
            futures.add(future);
        }

        boolean finished = false;
        int terminal = 0;
        int completed = 0;
        while (!finished && terminal++ != 300) {
            for (Future<NutritionOrderCallableResult> future : futures) {
                if (future.isDone()) {
                    if (!future.get().complete) {
                        if (!future.get().deleted) {
                            throw new Exception("Failed to complete the delete operation");
                        }
                        future.get().complete = true;
                        completed++;
                    }
                } else if (future.isCancelled()) {
                    svc.shutdown();
                    throw new Exception("Failed");
                }
            }
            finished = futures.size() == completed;
            Thread.sleep(1000);
        }
    }

    public static class NutritionOrderCallableDelete implements Callable<NutritionOrderCallableResult> {
        private WebTarget target = null;
        private Entity<Bundle> entity = null;
        private SearchRevIncludeTest test = null;

        public NutritionOrderCallableDelete(WebTarget target, Entity<Bundle> entity, SearchRevIncludeTest test) {
            this.target = target;
            this.entity = entity;
            this.test = test;
        }

        @Override
        public NutritionOrderCallableResult call() throws Exception {
            NutritionOrderCallableResult result = new NutritionOrderCallableResult();
            Response response = target.path("/").request().post(entity, Response.class);
            test.assertResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundleResponse = response.readEntity(Bundle.class);
            assertFalse(bundleResponse.getEntry().isEmpty());
            result.deleted = Boolean.TRUE;
            for (Bundle.Entry entry : bundleResponse.getEntry()) {
                result.deleted = result.deleted && entry.getResponse().getStatus() != null;
            }
            return result;
        }
    }

    @Test(groups = { "server-search-revinclude" })
    public void testSearchRevIncludeWithSystemSearchError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("")
                .queryParam("_revinclude", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "system search not supported with _include or _revinclude.");
    }

    @Test(groups = { "server-search-revinclude" })
    public void testSearchRevIncludeWithParseError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_revinclude", "Patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "A value for _include or _revinclude must have at least 2 parts separated by a colon.");
    }

    @Test(groups = { "server-search-revinclude" })
    public void testSearchRevIncludeWithJoinResourceTypeError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_revinclude", "BadType:code")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "'BadType' is not a valid resource type.");
    }

    @Test(groups = { "server-search-revinclude" })
    public void testSearchRevIncludeWithTargetResourceTypeError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_revinclude", "Procedure:subject:Group")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "The search parameter target type must match the resource type being searched.");
    }

    @Test(groups = { "server-search-revinclude" })
    public void testSearchRevIncludeUndefinedSearchParameterError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_revinclude", "Procedure:badParm")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Undefined Inclusion Parameter: Procedure:badParm");
    }

    @Test(groups = { "server-search-revinclude" })
    public void testSearchRevIncludeWithSearchParameterTypeError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_revinclude", "Procedure:code")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Inclusion Parameter must be of type 'reference'. The passed Inclusion Parameter is of type 'token': Procedure:code");
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateNutritionOrders"})
    public void testSearchRevIncludeWithTooManyIncludesError() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_revinclude", "NutritionOrder:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();

        if (response.getStatus() == 200) {
            Bundle bundle = response.readEntity(Bundle.class);
            System.out.println(bundle.getEntry().size());
            printOutResource(true, bundle);
        }
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Number of returned 'include' resources exceeds allowable limit of 1000");
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchRevIncludeSingleIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Practitioner")
                .queryParam("_tag", tag)
                .queryParam("_revinclude", "Patient:general-practitioner")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), practitioner1Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchRevIncludeSingleIncludedResultSameType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_id", patient1Id)
                .queryParam("_revinclude", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient1Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient2"})
    public void testSearchRevIncludeSingleIncludedResultElement() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_id", patient1Id)
                .queryParam("_elements", "name")
                .queryParam("_revinclude", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        Patient matchPatient = bundle.getEntry().get(0).getResource().as(Patient.class);
        assertEquals(matchPatient.getId(), patient1Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        // validate included elements
        assertEquals(matchPatient.getName().get(0).getGiven().get(0).getValue(), "1" + tag);
        // validate not included elements
        assertNull(matchPatient.getGender());
        assertNull(matchPatient.getBirthDate());

        Patient includePatient = bundle.getEntry().get(1).getResource().as(Patient.class);
        assertEquals(includePatient.getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
        // validate included elements
        assertEquals(includePatient.getGender(), AdministrativeGender.FEMALE);
        assertEquals(includePatient.getBirthDate(), Date.of(now.minus(1, ChronoUnit.DAYS).toString().substring(0,10)));
        assertEquals(includePatient.getName().get(0).getGiven().get(0).getValue(), "2" + tag);
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure5"})
    public void testSearchRevIncludeSingleIncludedResultDuplicate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("instantiates-uri", "2" + tag)
                .queryParam("_revinclude", "Procedure:part-of")
                .queryParam("_revinclude", "Procedure:reason-reference")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), procedure2Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), procedure5Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateEncounter1"})
    public void testSearchRevIncludeMultipleIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_revinclude", "Procedure:patient")
                .queryParam("_revinclude", "Encounter:patient")
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
        assertTrue(resourceIds.contains(procedure1Id));
        assertTrue(resourceIds.contains(procedure2Id));
        assertTrue(resourceIds.contains(encounter1Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3"})
    public void testSearchRevIncludeWildcardMultipleIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("gender", "female")
                .queryParam("_revinclude", "Procedure:*")
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
        assertTrue(resourceIds.contains(procedure1Id));
        assertTrue(resourceIds.contains(procedure2Id));
        assertTrue(resourceIds.contains(procedure3Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3", "testCreateProcedure4" })
    public void testSearchRevIncludeMultipleMatchAndIncludedResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_total", "none")
                .queryParam("_tag", tag)
                .queryParam("_revinclude", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        final int expectedMatchCount = 3;

        assertNotNull(bundle);
        assertNull(bundle.getTotal());
        assertEquals(bundle.getEntry().size(), 7);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<expectedMatchCount; ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(patient1Id));
        assertTrue(matchResourceIds.contains(patient2Id));
        assertTrue(matchResourceIds.contains(patient3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=expectedMatchCount; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(procedure1Id));
        assertTrue(includeResourceIds.contains(procedure2Id));
        assertTrue(includeResourceIds.contains(procedure4Id));
        assertTrue(includeResourceIds.contains(procedure5Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3" })
    public void testSearchRevIncludeMultipleMatchPaged() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_count", "1")
                .queryParam("_revinclude", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getTotal().getValue().intValue(), 3);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        if (patient1Id.equals(bundle.getEntry().get(0).getResource().getId())) {
            assertEquals(bundle.getEntry().size(), 2);
            assertTrue(includeResourceIds.contains(procedure4Id));
        } else if (patient2Id.equals(bundle.getEntry().get(0).getResource().getId())) {
            assertEquals(bundle.getEntry().size(), 3);
            assertTrue(includeResourceIds.contains(procedure1Id));
            assertTrue(includeResourceIds.contains(procedure2Id));
        } else if (patient3Id.equals(bundle.getEntry().get(0).getResource().getId())) {
            assertEquals(bundle.getEntry().size(), 2);
            assertTrue(includeResourceIds.contains(procedure5Id));
        } else {
            fail();
        }
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2", "testCreateProcedure3" })
    public void testSearchRevIncludeMultipleMatchLastPage() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_count", "2")
                .queryParam("_page", "2")
                .queryParam("_revinclude", "Procedure:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getTotal().getValue().intValue(), 3);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        if (patient1Id.equals(bundle.getEntry().get(0).getResource().getId())) {
            assertEquals(bundle.getEntry().size(), 2);
            assertTrue(includeResourceIds.contains(procedure4Id));
        } else if (patient2Id.equals(bundle.getEntry().get(0).getResource().getId())) {
            assertEquals(bundle.getEntry().size(), 3);
            assertTrue(includeResourceIds.contains(procedure1Id));
            assertTrue(includeResourceIds.contains(procedure2Id));
        } else if (patient3Id.equals(bundle.getEntry().get(0).getResource().getId())) {
            assertEquals(bundle.getEntry().size(), 2);
            assertTrue(includeResourceIds.contains(procedure5Id));
        } else {
            fail();
        }
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreatePatient3"})
    public void testSearchRevIncludeIteratePrimaryType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_total", "none")
                .queryParam("_tag", tag)
                .queryParam("_id", patient1Id)
                .queryParam("_revinclude:iterate", "Patient:link")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertNull(bundle.getTotal());
        assertEquals(bundle.getEntry().size(), 3);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient1Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(patient3Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure2"})
    public void testSearchRevIncludeIterateRevIncludeType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Organization")
                .queryParam("name", tag)
                .queryParam("_revinclude", "Patient:organization")
                .queryParam("_revinclude:iterate", "Procedure:subject")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 4);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), organization1Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(procedure1Id));
        assertTrue(includeResourceIds.contains(procedure2Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateEncounter1", "testCreateProcedure1", "testCreateProcedure4", "testCreateProcedure5"})
    public void testSearchRevIncludeIteratePrimaryAndRevIncludeType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_revinclude", "Procedure:subject")
                .queryParam("_revinclude:iterate", "Procedure:part-of", "Encounter:patient")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 8);
        List<String> matchResourceIds = new ArrayList<>();
        for (int i=0; i<bundle.getTotal().getValue(); ++i) {
            matchResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.MATCH);
        }
        assertTrue(matchResourceIds.contains(patient1Id));
        assertTrue(matchResourceIds.contains(patient2Id));
        assertTrue(matchResourceIds.contains(patient3Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(procedure1Id));
        assertTrue(includeResourceIds.contains(procedure2Id));
        assertTrue(includeResourceIds.contains(procedure4Id));
        assertTrue(includeResourceIds.contains(procedure5Id));
        assertTrue(includeResourceIds.contains(encounter1Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateEncounter1", "testCreateProcedure1"})
    public void testSearchRevIncludeIterateIncludeType() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("instantiates-uri", "1" + tag)
                .queryParam("_include", "Procedure:subject")
                .queryParam("_revinclude:iterate", "Encounter:patient")
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
        assertTrue(matchResourceIds.contains(procedure1Id));
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=bundle.getTotal().getValue(); i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(patient2Id));
        assertTrue(includeResourceIds.contains(encounter1Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure4", "testCreateProcedure5", "testCreatePatient3"})
    public void testSearchRevIncludeWithSort() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_revinclude", "Procedure:patient")
                .queryParam("_sort", "birthdate")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 7);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient3Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(2).getResource().getId(), patient1Id);
        assertEquals(bundle.getEntry().get(2).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> resourceIds = new ArrayList<>();
        for (int i=3; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(resourceIds.contains(procedure1Id));
        assertTrue(resourceIds.contains(procedure2Id));
        assertTrue(resourceIds.contains(procedure4Id));
        assertTrue(resourceIds.contains(procedure5Id));
    }

    @Test(groups = { "server-search-include" }, dependsOnMethods = {"testCreateProcedure1", "testCreateProcedure4", "testCreateProcedure5", "testCreatePatient3"})
    public void testSearchRevIncludeWithSortDesc() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Patient")
                .queryParam("_tag", tag)
                .queryParam("_revinclude", "Procedure:patient")
                .queryParam("_sort", "-birthdate")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 7);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), patient1Id);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), patient2Id);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(2).getResource().getId(), patient3Id);
        assertEquals(bundle.getEntry().get(2).getSearch().getMode(), SearchEntryMode.MATCH);
         List<String> resourceIds = new ArrayList<>();
        for (int i=3; i<bundle.getEntry().size(); ++i) {
            resourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(resourceIds.contains(procedure1Id));
        assertTrue(resourceIds.contains(procedure2Id));
        assertTrue(resourceIds.contains(procedure4Id));
        assertTrue(resourceIds.contains(procedure5Id));
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateMeasure"})
    public void testSearchCanonicalRevIncludeSingleIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Library")
                .queryParam("name", tag)
                .queryParam("_revinclude", "Measure:depends-on")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), libraryId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), measureId);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateCarePlan"})
    public void testSearchCanonicalRevIncludeNoVersionSingleIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Measure")
                .queryParam("name", tag)
                .queryParam("_revinclude", "CarePlan:instantiates-canonical")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 2);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), measureId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        assertEquals(bundle.getEntry().get(1).getResource().getId(), carePlanId);
        assertEquals(bundle.getEntry().get(1).getSearch().getMode(), SearchEntryMode.INCLUDE);
    }


    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateMeasureReport"})
    public void testSearchCanonicalRevIncludeVersionNoIncludedResult() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Measure")
                .queryParam("name", tag)
                .queryParam("_revinclude", "MeasureReport:measure")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 1);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), measureId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
    }

    @Test(groups = { "server-search-revinclude" }, dependsOnMethods = {"testCreateCarePlan"})
    public void testSearchCanonicalRevIncludeIterate() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Library")
                .queryParam("name", tag)
                .queryParam("_revinclude", "Measure:depends-on")
                .queryParam("_revinclude:iterate", "CarePlan:instantiates-canonical")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getTotal().getValue().intValue(), 1);
        assertEquals(bundle.getEntry().size(), 3);
        assertEquals(bundle.getEntry().get(0).getResource().getId(), libraryId);
        assertEquals(bundle.getEntry().get(0).getSearch().getMode(), SearchEntryMode.MATCH);
        List<String> includeResourceIds = new ArrayList<>();
        for (int i=1; i<bundle.getEntry().size(); ++i) {
            includeResourceIds.add(bundle.getEntry().get(i).getResource().getId());
            assertEquals(bundle.getEntry().get(i).getSearch().getMode(), SearchEntryMode.INCLUDE);
        }
        assertTrue(includeResourceIds.contains(measureId));
        assertTrue(includeResourceIds.contains(carePlanId));
    }

}