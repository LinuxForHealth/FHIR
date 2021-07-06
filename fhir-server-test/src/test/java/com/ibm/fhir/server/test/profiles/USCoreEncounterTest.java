/*
 * (C) Copyright IBM Corp. 2020
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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IdentifierUse;

/**
 * Tests the US Core 3.1.1 Profile with Encounter.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-encounter.html
 */
public class USCoreEncounterTest extends ProfilesTestBase {

    private static final String CLASSNAME = USCoreEncounterTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String encounterId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-encounter|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Encounter', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadEncounter1();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteEncounter1();
        }
    }

    public void loadEncounter1() throws Exception {
        String resource = "json/profiles/fhir-ig-us-core/Encounter-example-1.json";
        WebTarget target = getWebTarget();

        // @formatter:off
        CodeableConcept type = CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("DL"))
                    .display(com.ibm.fhir.model.type.String.of("Driver's license number"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .build())
                .build();
        // @formatter:on

        Identifier identifier =
                Identifier.builder()
                    .system(Uri.of("urn:oid:0.1.2.3.4.5.6.7"))
                    .use(IdentifierUse.OFFICIAL)
                    .value(com.ibm.fhir.model.type.String.of("654321"))
                    .type(type)
                    .build();

        Encounter encounter = TestUtil.readExampleResource(resource);
        encounter = encounter.toBuilder().identifier(identifier).build();

        Entity<Encounter> entity = Entity.entity(encounter, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Encounter").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // GET [base]/Encounter/12354 (first actual test, but simple)
        encounterId1 = getLocationLogicalId(response);
        response = target.path("Encounter/" + encounterId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteEncounter1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Encounter/" + encounterId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForId() throws Exception {
        // SHALL support fetching an encounter using the _id search parameter:
        // GET [base]/Encounter?_id=12354
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", encounterId1);
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all Encounters for a patient using the patient search parameter:
        // GET [base]/Encounter?patient=[reference]
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForPatientAndDate() throws Exception {
        // SHALL support searching using the combination of the date and patient search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET [base]/Encounter?date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}&patient=[reference]
        // date is actually a period -> Encounter.period

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("date", "ge2015-11-01");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForIdentifierAndDate() throws Exception {
        // SHOULD support searching using the identifier search parameter:
        // GET [base]/Encounter?identifier={system|}[code]

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("identifier", "654321");
            parameters.searchParam("date", "ge2015-11-01");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForClassAndPatient() throws Exception {
        // SHOULD support searching using the combination of the class and patient search parameters:
        // GET [base]/Encounter?class={system|}[code]&patient=[reference]

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("class", "AMB");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForClassAndPatientWithSystem() throws Exception {
        // SHOULD support searching using the combination of the class and patient search parameters:
        // GET [base]/Encounter?class={system|}[code]&patient=[reference]

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("class", "http://terminology.hl7.org/CodeSystem/v3-ActCode|AMB");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForTypeAndPatientWithSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Encounter?patient=[reference]&type={system|}[code]

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("type", "http://www.ama-assn.org/go/cpt|99201");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForTypeAndPatient() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Encounter?patient=[reference]&type={system|}[code]

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("type", "99201");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }

    @Test
    public void testSearchForStatusAndPatient() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // GET [base]/Encounter?patient=[reference]&status={system|}[code]

        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/example");
            parameters.searchParam("status", "finished");
            FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, encounterId1);
        }
    }
}