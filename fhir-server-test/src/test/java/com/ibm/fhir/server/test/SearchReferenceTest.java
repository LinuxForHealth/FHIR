/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;

public class SearchReferenceTest extends FHIRServerTestBase {

    @SuppressWarnings("unused")
    private String patientResourceIdWithReferenceTypeId;
    @SuppressWarnings("unused")
    private String patientResourceIdWithReferenceUrl;
    @SuppressWarnings("unused")
    private String patientWithLiteralReference;
    @SuppressWarnings("unused")
    private String patientResourceIdWithPractitionerReferenceTypeId;
    @SuppressWarnings("unused")
    private String patientResourceIdWithPractitionerRoleReferenceTypeId;
    @SuppressWarnings("unused")
    private String patientWithLogicalReference;
    @SuppressWarnings("unused")
    private String patientWithLiteralAndLogicalReference;
    @SuppressWarnings("unused")
    private String measureWithUrlOnly;
    @SuppressWarnings("unused")
    private String measureWithUrlAndVersion;
    @SuppressWarnings("unused")
    private String measureWithLibraryNoVersion;
    @SuppressWarnings("unused")
    private String measureWithLibraryWithVersion;

    /**
     * creates the various test cases for a patient with a reference
     */
    public String createPatientWithReference(String field, String reference, Identifier logicalReference) throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Reference.Builder referenceBuilder = Reference.builder();
        if (reference != null) {
            referenceBuilder.reference(com.ibm.fhir.model.type.String.of(reference));
        }
        if (logicalReference != null) {
            referenceBuilder.identifier(logicalReference);
        }

        if ("organization".equals(field)) {
            patient = patient.toBuilder()
                    .gender(AdministrativeGender.MALE)
                    .managingOrganization(referenceBuilder.display("Test Organization").build())
                    .build();
        } else if ("general-practitioner".equals(field)) {
            patient = patient.toBuilder()
                    .gender(AdministrativeGender.MALE)
                    .generalPractitioner(referenceBuilder.display("Test Practitioner").build())
                    .build();
        }
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String tmpPatientId = getLocationLogicalId(response);

        // Add the patient to the resource registry.
        addToResourceRegistry("Patient", tmpPatientId);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/"
                + tmpPatientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, responsePatient);
        return tmpPatientId;
    }

    @Test(groups = { "server-search-reference" })
    public void testCreatePatient() throws Exception {
        // references without an explicit type will be interpreted as literals (e.g. externals) not local references
        // we will not infer their type, which means that searches specifying a type will not match them by design
        // Not allowed by default (see: `fhirServer/core/checkReferenceTypes` config parameter)
        // patientResourceIdWithReferenceId = createPatientWithReference("organization", "3001", null);

        // relative reference
        patientResourceIdWithReferenceTypeId = createPatientWithReference("organization", "Organization/3002", null);

        // relative reference because the url aligns with the server base address
        patientResourceIdWithReferenceUrl = createPatientWithReference("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3003", null);

        // patientResourceIdWithReferenceCanonicalUri = createPatientWithReference("https://localhost:9443/fhir-server/api/v4/Organization/3004|1.0.0", null);

        // Literal reference to another server
        patientWithLiteralReference = createPatientWithReference("organization", "https://an.example.com/Organization/3004", null);

        // relative reference to Practitioner for general practitioner
        patientResourceIdWithPractitionerReferenceTypeId = createPatientWithReference("general-practitioner", "Practitioner/3002", null);

        // relative reference to PractitionerRole for general practitioner
        patientResourceIdWithPractitionerRoleReferenceTypeId = createPatientWithReference("general-practitioner", "PractitionerRole/3002", null);

        // logical reference
        patientWithLogicalReference = createPatientWithReference("organization", null, Identifier.builder()
                .system(Uri.of("http://terminology.hl7.org/CodeSystem/v3-IdentifierScope"))
                .value(string("3005a"))
                .build());

        // literal and logical reference
        patientWithLiteralAndLogicalReference = createPatientWithReference("organization", "Organization/3006", Identifier.builder()
                .system(Uri.of("https://an.example.com/OrgId"))
                .value(string("3007a"))
                .build());
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingId() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "3001")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingTypeId() {
        // no match for this search because Organization/3001 because the patient 3001 reference is a literal not relative reference
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "Organization/3001")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingUrl() {
        // no match for this search because Organization/3001 because the patient 3001 reference is a literal not relative reference
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3001")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingId() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "3002")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3002");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingTypeId() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "Organization/3002")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3002");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientTypeIdUsingTypeUrl() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3002")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3002");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingId() {
        // We expect a match here, because 3003 will match Organization/3003 even though
        // no resource type is given.
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "3003")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "https://localhost:9443/fhir-server/api/v4/Organization/3003");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingTypeId() {
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").queryParam("organization", "Organization/3003")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "https://localhost:9443/fhir-server/api/v4/Organization/3003");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientUrlUsingUrl() {
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").queryParam("organization", "https://localhost:9443/fhir-server/api/v4/Organization/3003")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "https://localhost:9443/fhir-server/api/v4/Organization/3003");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralReference() {
        // not expected to find a match because the reference is an external literal
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").queryParam("organization", "3004")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralReferenceWithType() {
        // not expected to find a match because the reference is an external literal
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").queryParam("organization", "Organization/3004")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralReferenceWithUrl() {
        // should find a match here because the search parameter is an exact match of the reference value
        WebTarget target = getWebTarget();

        Response response = target.path("Patient").queryParam("organization", "https://an.example.com/Organization/3004")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "https://an.example.com/Organization/3004");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchWithRelativePatientIdUsingIdMultipleTypes() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("general-practitioner", "3002")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Multiple resource type matches found for logical ID '3002' for search parameter 'general-practitioner'.");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalReferenceUsingId() {
        // not expected to find a match here because the search parameter only searches identifier of reference
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization:identifier", "3001")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalReferenceWithIdentifier() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization:identifier", "http://terminology.hl7.org/CodeSystem/v3-IdentifierScope|3005a")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "http://terminology.hl7.org/CodeSystem/v3-IdentifierScope");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3005a");

        // Code system is case-sensitive - no results expected
        response = target.path("Patient").queryParam("organization:identifier", "http://terminology.hl7.org/CodeSystem/v3-IdentifierScope|3005A")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalReferenceWithIdentifierValue() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization:identifier", "3005a")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "http://terminology.hl7.org/CodeSystem/v3-IdentifierScope");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3005a");

        // No code system specified - code is not case-sensitive - result expected
        response = target.path("Patient").queryParam("organization:identifier", "3005A")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "http://terminology.hl7.org/CodeSystem/v3-IdentifierScope");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3005a");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralRefForPatientWithLiteralAndLogicalRefs() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "Organization/3006")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3006");
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "https://an.example.com/OrgId");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3007a");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLogicalRefForPatientWithLiteralAndLogicalRefs() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization:identifier", "https://an.example.com/OrgId|3007a")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3006");
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "https://an.example.com/OrgId");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3007a");

        // Code system is not case-sensitive - result expected
        response = target.path("Patient").queryParam("organization:identifier", "https://an.example.com/OrgId|3007A")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3006");
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "https://an.example.com/OrgId");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3007a");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchLiteralAndLogicalRefsForPatientWithLiteralAndLogicalRefs() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("organization", "3006")
                .queryParam("organization:identifier", "3007a").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        Patient p = null;
        for (Bundle.Entry entry : bundle.getEntry()) {
            if (entry.getResource() != null && entry.getResource() instanceof Patient) {
                p = (Patient) entry.getResource();
            }
        }
        assertNotNull(p);
        assertEquals(p.getManagingOrganization().getReference().getValue(), "Organization/3006");
        assertEquals(p.getManagingOrganization().getIdentifier().getSystem().getValue(), "https://an.example.com/OrgId");
        assertEquals(p.getManagingOrganization().getIdentifier().getValue().getValue(), "3007a");
    }

    /*
     * creates the various test cases for a measure with a reference
     */
    public String createMeasureWithReference(String url, String version, String library) throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Measure and then call the 'create' API.
        Measure measure = TestUtil.getMinimalResource(Measure.class);

        measure = measure.toBuilder()
                .url(url == null ? null : Uri.of(url))
                .version(version == null ? null : string(version))
                .build();
        if (library != null) {
            measure = measure.toBuilder()
                    .library(Canonical.of(library))
                    .build();
        }
        Entity<Measure> entity = Entity.entity(measure, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Measure").request()
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the measure's logical id value.
        String tmpMeasureId = getLocationLogicalId(response);

        // Add the measure to the resource registry.
        addToResourceRegistry("Measure", tmpMeasureId);

        // Next, call the 'read' API to retrieve the new measure and verify it.
        response = target.path("Measure/"
                + tmpMeasureId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Measure responseMeasure = response.readEntity(Measure.class);
        TestUtil.assertResourceEquals(measure, responseMeasure);
        return tmpMeasureId;
    }

    @Test(groups = { "server-search-reference" })
    public void testCreateMeasure() throws Exception {
        // url and no version
        measureWithUrlOnly = createMeasureWithReference("http://example.org/fhir/Measure/abc", null, null);

        // url and version
        measureWithUrlAndVersion = createMeasureWithReference("http://example.org/fhir/Measure/abc", "1.0", null);

        // url and version and library with no version
        measureWithLibraryNoVersion = createMeasureWithReference("http://example.org/fhir/Measure/def", "1.0", "http://example.org/fhir/Library/abc");

        // url and version and library with version
        measureWithLibraryWithVersion = createMeasureWithReference("http://example.org/fhir/Measure/def", "1.0", "http://example.org/fhir/Library/abc|1.0");
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreateMeasure" })
    public void testSearchCanonicalUrlNoVersion() {
        // expected to find multiple matches
        WebTarget target = getWebTarget();

        Response response = target.path("Measure")
                .queryParam("url", "http://example.org/fhir/Measure/abc")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        for (Bundle.Entry entry : bundle.getEntry()) {
            Resource resource = entry.getResource();
            assertNotNull(resource);
            assertTrue(resource instanceof Measure);
            assertEquals(((Measure) entry.getResource()).getUrl().getValue(), "http://example.org/fhir/Measure/abc");
        }
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreateMeasure" })
    public void testSearchCanonicalUrlWithVersion() {
        // expected to find at least one match
        WebTarget target = getWebTarget();

        Response response = target.path("Measure")
                .queryParam("url", "http://example.org/fhir/Measure/abc|1.0")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Bundle.Entry entry : bundle.getEntry()) {
            Resource resource = entry.getResource();
            assertNotNull(resource);
            assertTrue(resource instanceof Measure);
            assertEquals(((Measure) entry.getResource()).getUrl().getValue(), "http://example.org/fhir/Measure/abc");
            assertEquals(((Measure) entry.getResource()).getVersion().getValue(), "1.0");
        }
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreateMeasure" })
    public void testSearchCanonicalUrlWithBadVersion() {
        // not expected to find any matches
        WebTarget target = getWebTarget();

        Response response = target.path("Measure")
                .queryParam("url", "http://example.org/fhir/Measure/abc|badversion")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreateMeasure" })
    public void testSearchCanonicalLibraryNoVersion() {
        // expected to find multiple matches
        WebTarget target = getWebTarget();

        Response response = target.path("Measure")
                .queryParam("depends-on", "http://example.org/fhir/Library/abc")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        for (Bundle.Entry entry : bundle.getEntry()) {
            Resource resource = entry.getResource();
            assertNotNull(resource);
            assertTrue(resource instanceof Measure);
            assertTrue(((Measure) entry.getResource()).getLibrary().get(0).getValue().startsWith("http://example.org/fhir/Library/abc"));
        }
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreateMeasure" })
    public void testSearchCanonicalLibraryWithVersion() {
        // expected to find at least one match
        WebTarget target = getWebTarget();

        Response response = target.path("Measure")
                .queryParam("depends-on", "http://example.org/fhir/Library/abc|1.0")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        for (Bundle.Entry entry : bundle.getEntry()) {
            Resource resource = entry.getResource();
            assertNotNull(resource);
            assertTrue(resource instanceof Measure);
            assertTrue(((Measure) entry.getResource()).getLibrary().get(0).getValue().startsWith("http://example.org/fhir/Library/abc|1.0"));
        }
    }

    @Test(groups = { "server-search-reference" }, dependsOnMethods = { "testCreateMeasure" })
    public void testSearchCanonicalLibraryWithBadVersion() {
        // not expected to find any matches
        WebTarget target = getWebTarget();

        Response response = target.path("Measure")
                .queryParam("depends-on", "http://example.org/fhir/Library/abc|badversion")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);
    }

}