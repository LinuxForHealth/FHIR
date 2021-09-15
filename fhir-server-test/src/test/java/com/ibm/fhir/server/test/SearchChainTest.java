/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.Code.code;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.Instant;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;

/**
 * The tests execute the chained behavior in order to exercise reference chains.
 */
public class SearchChainTest extends FHIRServerTestBase {
    private String patientId;
    private String procedureId;
    private String encounterId;
    private String carePlanId;
    private String measureReportId;
    private String measureId;
    private String libraryId;
    private String strUniqueTag = UUID.randomUUID().toString();

    @Test(groups = { "server-search-chain" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        Coding security = Coding.builder().system(uri("http://ibm.com/fhir/security/" + strUniqueTag)).code(code(strUniqueTag)).build();
        Coding tag = Coding.builder().system(uri("http://ibm.com/fhir/tag/" + strUniqueTag)).code(code(strUniqueTag)).build();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        patient = patient.toBuilder()
                .meta(Meta.builder()
                    .security(security)
                    .tag(tag)
                    .profile(Canonical.of("http://ibm.com/fhir/profile/Profile/" + strUniqueTag))
                    .build())
                .gender(AdministrativeGender.MALE).build();

        Entity<Patient> entity =
                Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Patient").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Add the patient to the resource registry.
        addToResourceRegistry("Patient", patientId);
        
        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient"})
    public void testCreateProcedure() throws Exception {
        WebTarget target = getWebTarget();

        Reference reference = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Patient/" + patientId)).build();

        // Build a new Procedure and then call the 'create' API.
        Procedure procedure = TestUtil.readExampleResource("json/spec/procedure-example-physical-therapy.json");
        procedure = procedure.toBuilder().subject(reference).build();

        // Add Subject reference to the procedure
        Entity<Procedure> entity =
                Entity.entity(procedure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Procedure").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the procedure's logical id value.
        procedureId = getLocationLogicalId(response);

        // Add the procedure to the resource registry.
        addToResourceRegistry("Procedure", procedureId);
        
        // Next, call the 'read' API to retrieve the new procedure and verify it.
        response = target.path("Procedure/" + procedureId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient"})
    public void testCreateEncounter() throws Exception {
        WebTarget target = getWebTarget();

        Reference reference = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Patient/" + patientId + "/_history/2")).build();

        // Build a new Procedure and then call the 'create' API.
        Encounter encounter = TestUtil.getMinimalResource(Encounter.class);
        encounter = encounter.toBuilder().subject(reference).build();

        // Call the 'create' API.
        Entity<Encounter> entity = Entity.entity(encounter, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Encounter").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the encounter's logical id value.
        encounterId = getLocationLogicalId(response);

        // Add the encounter to the resource registry.
        addToResourceRegistry("Encounter", encounterId);
        
        // Next, call the 'read' API to retrieve the new encounter and verify it.
        response = target.path("Encounter/" + encounterId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { "server-search-chain" })
    public void testCreateLibrary() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Library and then call the 'create' API.
        Library library = TestUtil.getMinimalResource(Library.class);
        library = library.toBuilder()
                .url(Uri.of("http://example.org/fhir/Library/abc"))
                .version(com.ibm.fhir.model.type.String.string("1.0"))
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

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateLibrary"})
    public void testCreateMeasure() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Measure and then call the 'create' API.
        Measure measure = TestUtil.getMinimalResource(Measure.class);
        measure = measure.toBuilder()
                .url(Uri.of("http://example.org/fhir/Measure/abc"))
                .version(com.ibm.fhir.model.type.String.string("1.0"))
                .library(Canonical.of("http://example.org/fhir/Library/abc|1.0"))
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

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateMeasure"})
    public void testCreateCarePlan() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new CarePlan and then call the 'create' API.
        CarePlan carePlan = TestUtil.getMinimalResource(CarePlan.class);
        carePlan = carePlan.toBuilder()
                .instantiatesCanonical(Canonical.of("http://example.org/fhir/Measure/abc"))
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

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateMeasure"})
    public void testCreateMeasureReport() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new MeasureReport and then call the 'create' API.
        MeasureReport measureReport = TestUtil.getMinimalResource(MeasureReport.class);
        measureReport = measureReport.toBuilder()
                .measure(Canonical.of("http://example.org/fhir/Measure/abc|2.0"))
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

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._id", patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithLastUpdated() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._lastUpdated", "gt" + Instant.now().minusSeconds(600).toString())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithIdThatDoesntExist() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._id", patientId + "BAD")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithProfile() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._profile", "http://ibm.com/fhir/profile/Profile/" + strUniqueTag)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithTag() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._tag", "http://ibm.com/fhir/tag/" + strUniqueTag + "|" + strUniqueTag)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithTagSystemOnly() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._tag", "http://ibm.com/fhir/tag/" + strUniqueTag + "|")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithSecurity() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._security", "http://ibm.com/fhir/security/" + strUniqueTag + "|" + strUniqueTag)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchPatientWithSecuritySystemOnly() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure").queryParam("subject:Patient._security", "http://ibm.com/fhir/security/" + strUniqueTag + "|")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateEncounter"})
    public void testVersionedReferenceSearchPatientWithId() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Encounter").queryParam("subject:Patient._id", patientId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 2);
        OperationOutcome oo = null;
        for (Entry bundleEntry : bundle.getEntry()) {
            if (bundleEntry.getResource().is(OperationOutcome.class)) {
                oo = bundleEntry.getResource().as(OperationOutcome.class);
            }
        }
        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getSeverity(), IssueSeverity.WARNING);
        assertEquals(oo.getIssue().get(0).getCode(), IssueType.NOT_SUPPORTED);
        assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(), "Resource with id '" + encounterId +
            "' contains a versioned reference in an element used for chained search, but chained search does not act on versioned references.");
        assertEquals(oo.getIssue().get(0).getExpression().get(0).getValue(), "Encounter.subject");
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithActiveMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.active:missing", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertTrue(bundle.getLink().size() == 1);
        assertTrue(bundle.getLink().get(0).getUrl().getValue().contains("active:missing=true"));
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithActiveNotMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.active:missing", "false")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithGenderMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.gender:missing", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithGenderNotMissing() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.gender:missing", "false")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertTrue(bundle.getLink().size() == 1);
        assertTrue(bundle.getLink().get(0).getUrl().getValue().contains("gender:missing=false"));
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithActiveNot() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.active:not", "true")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithGenderNot_NoResults() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.gender:not", "male")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreatePatient" , "testCreateProcedure"})
    public void testSearchProcedureForPatientWithGenderNot_Results() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Procedure")
                .queryParam("_id", procedureId)
                .queryParam("subject:Patient.gender:not", "female")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateCarePlan"})
    public void testSearchCanonicalMeasure() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("CarePlan").queryParam("instantiates-canonical:Measure._id", measureId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateMeasureReport"})
    public void testSearchCanonicalMeasureNoMatch() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("MeasureReport").queryParam("measure._id", measureId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateMeasure"})
    public void testSearchCanonicalLibrary() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("Measure").queryParam("depends-on:Library._id", libraryId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-chain" }, dependsOnMethods = {"testCreateCarePlan"})
    public void testSearchCanonicalMeasureAndLibrary() {
        WebTarget target = getWebTarget();
        Response response =
                target.path("CarePlan").queryParam("instantiates-canonical:Measure.depends-on:Library._id", libraryId)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);

        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

}