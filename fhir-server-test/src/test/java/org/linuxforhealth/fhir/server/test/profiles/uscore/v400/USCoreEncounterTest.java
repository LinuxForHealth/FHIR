/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v400;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.ig.us.core.tool.USCoreExamplesUtil;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Encounter;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IdentifierUse;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with Encounter.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-encounter.html
 */
public class USCoreEncounterTest extends ProfilesTestBaseV2 {

    private String encounterId1 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-encounter|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        String resource = "Encounter-example-1.json";
        // @formatter:off
        CodeableConcept type = CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("DL"))
                    .display(org.linuxforhealth.fhir.model.type.String.of("Driver's license number"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .build())
                .build();
        // @formatter:on

        Identifier identifier =
                Identifier.builder().system(Uri.of("urn:oid:0.1.2.3.4.5.6.7")).use(IdentifierUse.OFFICIAL).value(org.linuxforhealth.fhir.model.type.String.of("654321")).type(type).build();

        Encounter encounter = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        encounter = encounter.toBuilder().identifier(identifier).build();

        encounterId1 = createResourceAndReturnTheLogicalId("Encounter", encounter);
    }

    @Test
    public void testSearchForId() throws Exception {
        // SHALL support fetching an encounter using the _id search parameter:
        // GET [base]/Encounter?_id=12354
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", encounterId1);
        FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
        assertContainsIds(bundle, encounterId1);
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all Encounters for a patient using the patient search parameter:
        // GET [base]/Encounter?patient=[reference]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Encounter.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, encounterId1);
    }

    @Test
    public void testSearchForPatientAndDate() throws Exception {
        // SHALL support searching using the combination of the date and patient search parameters:
        // including support for these date comparators: gt,lt,ge,le
        // including optional support for composite AND search on date (e.g.date=[date]&date=[date]]&...)
        // GET [base]/Encounter?date={gt|lt|ge|le}[date]{&date={gt|lt|ge|le}[date]&...}&patient=[reference]
        // date is actually a period -> Encounter.period
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

    @Test
    public void testSearchForIdentifierAndDate() throws Exception {
        // SHOULD support searching using the identifier search parameter:
        // GET [base]/Encounter?identifier={system|}[code]
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

    @Test
    public void testSearchForClassAndPatient() throws Exception {
        // SHOULD support searching using the combination of the class and patient search parameters:
        // GET [base]/Encounter?class={system|}[code]&patient=[reference]
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

    @Test
    public void testSearchForClassAndPatientWithSystem() throws Exception {
        // SHOULD support searching using the combination of the class and patient search parameters:
        // GET [base]/Encounter?class={system|}[code]&patient=[reference]
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

    @Test
    public void testSearchForTypeAndPatientWithSystem() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Encounter?patient=[reference]&type={system|}[code]
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

    @Test
    public void testSearchForTypeAndPatient() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Encounter?patient=[reference]&type={system|}[code]
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

    @Test
    public void testSearchForStatusAndPatient() throws Exception {
        // SHOULD support searching using the combination of the patient and status search parameters:
        // GET [base]/Encounter?patient=[reference]&status={system|}[code]

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