/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.resource.Provenance.Agent;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.NarrativeStatus;

/**
 * Tests the US Core 3.1.1 Profile with AllergyIntolerance and a related Provenance.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-provenance.html
 */
public class USCoreAllergyIntoleranceTest extends ProfilesTestBase {
    private static final String CLASSNAME = USCoreAllergyIntoleranceTest.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;

    private String allergyIntoleranceIdActive = null;
    private String allergyIntoleranceIdInactive = null;
    private String allergyIntoleranceIdResolved = null;
    private String provenanceId = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList(  "http://hl7.org/fhir/us/core/StructureDefinition/us-core-allergyintolerance|3.1.1",
            "http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient|3.1.1",
            "http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            LOG.info("Skipping Tests for 'fhir-ig-us-core - AllergyIntolerance', the profiles don't exist");
        }
    }

    public void loadAllergyIntoleranceActive() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/AllergyIntolerance-example.json";
        WebTarget target = getWebTarget();

        AllergyIntolerance allergyIntolerance = TestUtil.readExampleResource(resource);

        Entity<AllergyIntolerance> entity = Entity.entity(allergyIntolerance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AllergyIntolerance").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        allergyIntoleranceIdActive = getLocationLogicalId(response);
        response = target.path("AllergyIntolerance/" + allergyIntoleranceIdActive).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadAllergyIntoleranceInactive() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/AllergyIntolerance-example.json";
        WebTarget target = getWebTarget();

        AllergyIntolerance allergyIntolerance = TestUtil.readExampleResource(resource);
        // @formatter:off
        CodeableConcept allergyIntoleranceClinicalStatusCodes = CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("inactive"))
                    .display(com.ibm.fhir.model.type.String.of("Inactive"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"))
                    .build())
                .build();
        // @formatter:on
        allergyIntolerance = allergyIntolerance.toBuilder().clinicalStatus(allergyIntoleranceClinicalStatusCodes).build();

        Entity<AllergyIntolerance> entity = Entity.entity(allergyIntolerance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AllergyIntolerance").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        allergyIntoleranceIdInactive = getLocationLogicalId(response);
        response = target.path("AllergyIntolerance/" + allergyIntoleranceIdInactive).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadAllergyIntoleranceResolved() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/AllergyIntolerance-example.json";
        WebTarget target = getWebTarget();

        AllergyIntolerance allergyIntolerance = TestUtil.readExampleResource(resource);
        // @formatter:off
        CodeableConcept allergyIntoleranceClinicalStatusCodes = CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("resolved"))
                    .display(com.ibm.fhir.model.type.String.of("Resolved"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical"))
                    .build())
                .build();
        // @formatter:on
        allergyIntolerance = allergyIntolerance.toBuilder().clinicalStatus(allergyIntoleranceClinicalStatusCodes).build();

        Entity<AllergyIntolerance> entity = Entity.entity(allergyIntolerance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("AllergyIntolerance").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        allergyIntoleranceIdResolved = getLocationLogicalId(response);
        response = target.path("AllergyIntolerance/" + allergyIntoleranceIdResolved).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadProvenanceForAllergyIntoleranceIdActive() throws Exception {
        WebTarget target = getWebTarget();

     // Build the Provenance
        Canonical profile = Canonical.of("http://hl7.org/fhir/us/core/StructureDefinition/us-core-provenance");
        Meta meta = Meta.builder().profile(profile).build();

        // @formatter:off
        CodeableConcept type = CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("author"))
                    .display(com.ibm.fhir.model.type.String.of("Author"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/provenance-participant-type"))
                    .build())
                .build();
        // @formatter:on

        Provenance provenance = Provenance.builder()
            .meta(meta)
            .text(Narrative.builder()
                        .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">loaded from the datastore</div>"))
                        .status(NarrativeStatus.GENERATED).build())
            .target(Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("AllergyIntolerance/" + allergyIntoleranceIdActive))
                .build())
            .recorded(Instant.now())
            .agent(Agent.builder()
                .type(type)
                .who(
                    Reference.builder()
                        .reference(com.ibm.fhir.model.type.String.of("Practitioner/practitioner-1"))
                        .build())
                .onBehalfOf(
                    Reference.builder()
                        .reference(com.ibm.fhir.model.type.String.of("Organization/saint-luke-w-endpoint"))
                        .build())
                .build())
            .build();

        Entity<Provenance> entity = Entity.entity(provenance, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Provenance").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        provenanceId = getLocationLogicalId(response);
        response = target.path("Provenance/" + provenanceId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Resources
    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadAllergyIntoleranceActive();
            addToResourceRegistry("AllergyIntolerance", allergyIntoleranceIdActive);

            loadAllergyIntoleranceInactive();
            addToResourceRegistry("AllergyIntolerance", allergyIntoleranceIdInactive);

            loadAllergyIntoleranceResolved();
            addToResourceRegistry("AllergyIntolerance", allergyIntoleranceIdResolved);

            loadProvenanceForAllergyIntoleranceIdActive();
            addToResourceRegistry("Provenance", provenanceId);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatient() throws Exception {
        // SHALL support searching for all allergies for a patient using the patient search parameter
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#mandatory-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdResolved);
            assertContainsIds(bundle, allergyIntoleranceIdActive);
            assertContainsIds(bundle, allergyIntoleranceIdInactive);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByInactiveStatusWithSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#optional-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical|inactive");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdInactive);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByResolvedStatusWithSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#optional-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical|resolved");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdResolved);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByActiveStatusWithSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#optional-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical|active");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdActive);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByInactiveStatusWithoutSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#optional-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "inactive");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdInactive);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByResolvedStatusWithoutSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#optional-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "resolved");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdResolved);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByActiveStatusWithoutSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and clinical-status search parameters
        // http://build.fhir.org/ig/HL7/US-Core-R4/StructureDefinition-us-core-allergyintolerance.html#optional-search-parameters
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("clinical-status", "active");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdActive);
        }
    }

    @Test
    public void testSearchForAllAllergiesForAPatientByActiveStatusWithoutSystemAndRevinclude() throws Exception {
        // http://example.org/fhir/AllergyIntolerance?_revinclude=Provenance%3Atarget&patient=Examples
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("_revinclude", "Provenance:target");
            FHIRResponse response = client.search(AllergyIntolerance.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, allergyIntoleranceIdActive);
            assertContainsIds(bundle, provenanceId);
        }
    }
}