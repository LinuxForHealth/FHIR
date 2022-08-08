/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v400;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.ig.us.core.tool.USCoreExamplesUtil;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry.Request;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.PractitionerRole;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.HTTPVerb;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with Practitioner and PractitionerRole
 *
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitionerrole.html
 */
public class USCorePractitionerAndPractitionerRoleTest extends ProfilesTestBaseV2 {
    private String practitionerId = "Practitioner-1011";
    private String practitionerRoleId = "PractitionerRole-1";
    private String endpointId = "71";

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitionerrole|4.0.0", "http://hl7.org/fhir/us/core/StructureDefinition/us-core-practitioner|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        String resource = "Bundle-66c8856b-ba11-4876-8aa8-467aad8c11a2.json";
        WebTarget target = getWebTarget();

        // re-purposing search result bundle - update fields to create valid request bundle
        Bundle bundle = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        List<Bundle.Entry> entries = bundle.getEntry();
        List<Bundle.Entry> output = new ArrayList<>();
        for (Bundle.Entry entry : entries) {
            Request request = Request.builder().method(HTTPVerb.PUT).url(Uri.of(entry.getResource().getClass().getSimpleName() + "/"
                    + entry.getResource().getId())).build();

            Meta meta = Meta.builder().versionId(Id.of("" + System.currentTimeMillis())).build();
            entry.getResource().toBuilder().meta(meta).build();

            Bundle.Entry tmpEntry = entry.toBuilder().request(request).search(null).build();
            output.add(tmpEntry);
        }
        bundle = bundle.toBuilder().type(BundleType.BATCH).entry(output).total(null).build();

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION).post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        response = target.path("Practitioner/" + practitionerId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        this.addToResourceRegistry("Practitioner", practitionerId);

        response = target.path("PractitionerRole/" + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        this.addToResourceRegistry("PractitionerRole", practitionerRoleId);

        response = target.path("Endpoint/" + endpointId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        this.addToResourceRegistry("Endpoint", endpointId);
    }

    @Test
    public void testSearchForPractitionerByName() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitioner.html
        // SHALL support searching for a practitioner by a string match of any part of name using the name search
        // parameter:
        // GET [base]/Practitioner?name=[string]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("name:contains", "Richard");
        FHIRResponse response = client.search(Practitioner.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
    }

    @Test
    public void testSearchForPractitionerByIdentifierWithSystem() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitioner.html
        // SHALL support searching a practitioner by an identifier such as an NPI using the identifier search parameter:
        // GET [base]/Practitioner?identifier={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "http://hl7.org/fhir/sid/us-ssn|000001011");
        FHIRResponse response = client.search(Practitioner.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
    }

    @Test
    public void testSearchForPractitionerByIdentifierWithoutSystem() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitioner.html
        // SHALL support searching a practitioner by an identifier such as an NPI using the identifier search parameter:
        // GET [base]/Practitioner?identifier={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "000001011");
        FHIRResponse response = client.search(Practitioner.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
    }

    @Test
    public void testSearchForPractitionerByIdentifierWithSecondOfficial() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitioner.html
        // SHALL support searching a practitioner by an identifier such as an NPI using the identifier search parameter:
        // GET [base]/Practitioner?identifier={system|}[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "http://hl7.org/fhir/sid/us-npi|9999991011");
        FHIRResponse response = client.search(Practitioner.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
    }

    @Test
    public void testSearchForPractitionerRoleBySpeciality() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitionerrole.html
        // SHALL support searching practitioner role by specialty using the specialty search parameter:
        // including optional support for these _include parameters:
        // PractitionerRole:endpoint,PractitionerRole:practitioner
        // GET
        // [base]/PractitionerRole?specialty={[system]}|[code]{&_include=PractitionerRole:practitioner}{&_include=PractitionerRole?endpoint}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("specialty", "http://nucc.org/provider-taxonomy|208D00000X");
        FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerRoleId);
    }

    @Test
    public void testSearchForPractitionerRoleBySpecialityWithInclude() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitionerrole.html
        // SHALL support searching practitioner role by specialty using the specialty search parameter:
        // including optional support for these _include parameters:
        // PractitionerRole:endpoint,PractitionerRole:practitioner
        // GET
        // [base]/PractitionerRole?specialty={[system]}|[code]{&_include=PractitionerRole:practitioner}{&_include=PractitionerRole?endpoint}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("specialty", "http://nucc.org/provider-taxonomy|208D00000X");
        parameters.searchParam("_include", "PractitionerRole:practitioner");
        FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
        assertContainsIds(bundle, practitionerRoleId);
    }

    @Test
    public void testSearchForPractitionerRoleBySpecialityWithMultipleIncludes() throws Exception {
        // https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-practitionerrole.html
        // SHALL support searching practitioner role by specialty using the specialty search parameter:
        // including optional support for these _include parameters:
        // PractitionerRole:endpoint,PractitionerRole:practitioner
        // GET
        // [base]/PractitionerRole?specialty={[system]}|[code]{&_include=PractitionerRole:practitioner}{&_include=PractitionerRole?endpoint}
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("specialty", "http://nucc.org/provider-taxonomy|208D00000X");
        parameters.searchParam("_include", "PractitionerRole:practitioner");
        parameters.searchParam("_include", "PractitionerRole:endpoint");
        FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
        assertContainsIds(bundle, practitionerRoleId);
        assertContainsIds(bundle, endpointId);
    }
}