/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.ig.carin.bb.test.tool.C4BBExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CareTeam;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Carin for BlueButton (C4BB) Profile Integration Tests v1.0.0
 */
public class CarinBlueButtonV100Test extends ProfilesTestBaseV2 {

    private String coverageId = null;
    private String careTeamId = null;
    private String organizationId = null;
    private String organizationOrg1Id = null;
    private String organizationOrg45Id = null;
    private String locationId = null;
    private String patientId = null;
    private String practitionerId = null;

    private String explanationOfBenefitInPatientId = null;
    private String explanationOfBenefitsOutPatient = null;
    private String explanationOfBenefitsPharmacyId = null;
    private String explanationOfBenefitsProfessionalId = null;
    private String eobInpatientInstitutionalEx1id = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList(
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Coverage|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-ExplanationOfBenefit|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-ExplanationOfBenefit-Inpatient-Institutional|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-ExplanationOfBenefit-Outpatient-Institutional|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-ExplanationOfBenefit-Pharmacy|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-ExplanationOfBenefit-Professional-NonClinician|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Organization|1.0.0",
            "http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Patient|1.0.0");
        //@formatter:on
    }

    @Override
    public void loadResources() throws Exception {
        loadLocation();
        loadOrganization();
        loadOrganizationOrg1();
        loadOrganizationOrg45();
        loadProvider();
        loadCoverage();
        loadPatient();
        loadCareteam();
        loadExplanationOfBenefitsInPatient();
        loadExplanationOfBenefitsOutPatient();
        loadExplanationOfBenefitsPharmacy();
        loadExplanationOfBenefitsProfessional();
        loadExplanationOfBenefitsInpatientInstitutionalEx1();
    }

    // Load Organization Resources
    public void loadOrganization() throws Exception {
        String resource = "json/spec/organization-example.json";
        WebTarget target = getWebTarget();
        Organization organization = TestUtil.readExampleResource(resource);

        // Add profile + extra fields required by profile.
        Canonical profile = Canonical.of("http://hl7.org/fhir/us/C4BB/StructureDefinition/C4BB-Organization");
        Meta meta = organization.getMeta().toBuilder().profile(profile).build();

        // @formatter:off
        CodeableConcept type =
                CodeableConcept.builder()
                    .coding(Coding.builder()
                        .code(Code.of("NPI"))
                        .display(string("National provider identifier"))
                        .system(uri("http://terminology.hl7.org/CodeSystem/v2-0203"))
                        .build())
                    .build();

        Identifier identifierNPI =
                Identifier.builder()
                    .system(uri("http://hl7.org/fhir/sid/us-npi"))
                    .value(string("1234556"))
                    .type(type)
                    .build();

        type = CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("TAX"))
                    .display(string("Tax ID number"))
                    .system(uri("http://terminology.hl7.org/CodeSystem/v2-0203"))
                    .build())
                .build();

        Identifier identifierTaxId =
                Identifier.builder()
                    .system(uri("urn:oid:2.16.840.1.113883.4.4"))
                    .value(string("1234567"))
                    .type(type)
                    .build();

        organization = organization.toBuilder()
                            .meta(meta)
                            .identifier(identifierNPI, identifierTaxId)
                            .active(com.ibm.fhir.model.type.Boolean.TRUE)
                            .build();
        // @formatter:on

        Entity<Organization> entity = Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);

        assertResponse(response, Response.Status.CREATED.getStatusCode());
        organizationId = getLocationLogicalId(response);

        response = target.path("Organization/" + organizationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        addToResourceRegistry("Organization", organizationId);
    }

    public void loadOrganizationOrg1() throws Exception {
        String resource = "Organization-OrganizationProvider1.json";
        organizationOrg1Id = buildAndAssertOnResourceForC4BB("Organization", "100", resource);
    }

    public void loadOrganizationOrg45() throws Exception {
        String resource = "Organization-Payer1.json";
        organizationOrg45Id = buildAndAssertOnResourceForC4BB("Organization", "100", resource);
    }

    // Load Coverage Resources
    public void loadCoverage() throws Exception {
        String resource = "Coverage-Coverage1.json";
        Coverage coverage = C4BBExamplesUtil.readLocalJSONResource("100", resource);

        Reference org45ref = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Organization/" + organizationOrg45Id))
                .build();
        coverage = coverage.toBuilder().payor(Arrays.asList(org45ref)).build();

        coverageId = buildAndAssertOnResource("Coverage", coverage);
    }

    // Load Patient Resources
    public void loadPatient() throws Exception {
        String resource = "Patient-Patient1.json";
        patientId = buildAndAssertOnResourceForC4BB("Patient", "100", resource);
    }

    // Load Provider Resources
    public void loadProvider() throws Exception {
        WebTarget target = getWebTarget();
        Practitioner practitioner = TestUtil.readExampleResource("json/spec/practitioner-example.json");

        Entity<Practitioner> entity = Entity.entity(practitioner, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Practitioner").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        practitionerId = getLocationLogicalId(response);

        response = target.path("Practitioner/" + practitionerId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        addToResourceRegistry("Practitioner", practitionerId);
    }

    // Load CareTeam Resources
    public void loadCareteam() throws Exception {
        WebTarget target = getWebTarget();
        CareTeam careTeam = TestUtil.readExampleResource("json/spec/careteam-example.json");

        Entity<CareTeam> entity = Entity.entity(careTeam, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("CareTeam").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        careTeamId = getLocationLogicalId(response);

        response = target.path("CareTeam/" + careTeamId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        addToResourceRegistry("CareTeam", careTeamId);
    }

    // Load Location Resources
    public void loadLocation() throws Exception {
        WebTarget target = getWebTarget();

        Location location = TestUtil.readExampleResource("json/spec/location-example.json");
        Entity<Location> entity = Entity.entity(location, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Location").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        locationId = getLocationLogicalId(response);

        response = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        addToResourceRegistry("Location", locationId);
    }

    // Load Explanation of Benefits Resources
    public void loadExplanationOfBenefitsInPatient() throws Exception {
        String resource = "ExplanationOfBenefit-EOBInpatient1.json";
        explanationOfBenefitInPatientId = buildAndAssertOnResourceForC4BB("ExplanationOfBenefit", "100", resource);
    }

    public void loadExplanationOfBenefitsOutPatient() throws Exception {
        String resource = "ExplanationOfBenefit-EOBOutpatientInstitutional1.json";
        explanationOfBenefitsOutPatient = buildAndAssertOnResourceForC4BB("ExplanationOfBenefit", "100", resource);
    }

    public void loadExplanationOfBenefitsPharmacy() throws Exception {
        String resource = "ExplanationOfBenefit-EOBPharmacy1.json";
        explanationOfBenefitsPharmacyId = buildAndAssertOnResourceForC4BB("ExplanationOfBenefit", "100", resource);
    }

    public void loadExplanationOfBenefitsProfessional() throws Exception {
        String resource = "ExplanationOfBenefit-EOBProfessional1a.json";
        explanationOfBenefitsProfessionalId = buildAndAssertOnResourceForC4BB("ExplanationOfBenefit", "100", resource);
    }

    // ExplanationOfBenefit-EOBInpatient1.json
    public void loadExplanationOfBenefitsInpatientInstitutionalEx1() throws Exception {
        String resource = "ExplanationOfBenefit-EOBInpatient1.json";
        eobInpatientInstitutionalEx1id = buildAndAssertOnResourceForC4BB("ExplanationOfBenefit", "100", resource);
    }

    @Test
    public void testLocationId() throws Exception {
        // A common call -> GET [base]/Location?_id=[id]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", locationId);
        FHIRResponse response = client.search(Location.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, locationId);
    }

    @Test
    public void testPractitionerId() throws Exception {
        // A common call -> GET [base]/Practitioner?_id=[id]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", practitionerId);
        FHIRResponse response = client.search(Practitioner.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, practitionerId);
    }

    @Test
    public void testPatientId() throws Exception {
        // A common call -> GET [base]/Patient?_id=[id]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRResponse response = client.search(Patient.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, patientId);
    }

    @Test
    public void testOrganizationId() throws Exception {
        // A common call -> GET [base]/Organization?_id=[id],[id],[id]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", String.join(",", organizationId, organizationOrg1Id, organizationOrg45Id));
        FHIRResponse response = client.search(Organization.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, organizationId);
    }

    @Test
    public void testCoverageIdWithInclude() throws Exception {
        // A common call -> GET [base]/Coverage?_id=[id]&_include=Coverage:payor
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", coverageId);
        parameters.searchParam("_include", "Coverage:payor");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Coverage.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, coverageId, organizationOrg45Id);
    }

    @Test
    public void testExplanationOfBenefitId() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", this.explanationOfBenefitInPatientId + "," + this.explanationOfBenefitsOutPatient + ","
                + this.explanationOfBenefitsPharmacyId + "," + this.explanationOfBenefitsProfessionalId + "," + this.eobInpatientInstitutionalEx1id);
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 4);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
        assertContainsIds(bundle, explanationOfBenefitsOutPatient);
        assertContainsIds(bundle, explanationOfBenefitsPharmacyId);
        assertContainsIds(bundle, explanationOfBenefitsProfessionalId);
        assertContainsIds(bundle, eobInpatientInstitutionalEx1id);
    }

    @Test
    public void testExplanationOfBenefitIdentifierNoSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "AW123412341234123412341234123412");
        parameters.searchParam("_id", explanationOfBenefitInPatientId);
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test
    public void testExplanationOfBenefitIdentifierWithSystem() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("identifier", "https://www.xxxplan.com/fhir/EOBIdentifier|AW123412341234123412341234123412");
        parameters.searchParam("_id", explanationOfBenefitInPatientId);
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test
    public void testExplanationOfBenefitServiceDate_BillablePeriod() throws Exception {
        // Service Date using ExplanationOfBenefit.billablePeriod
        // EoB - Pharmacy Benefits
        // "start": "2019-10-30",
        // "end": "2019-10-31"
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("service-date", "ge2019-10-01");
        parameters.searchParam("service-date", "le2019-11-01");
        parameters.searchParam("_id", explanationOfBenefitsPharmacyId);
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, this.explanationOfBenefitsPharmacyId);
    }

    @Test
    public void testCARINBlueButton_ExplanationOfBenefit_Patient_SearchParameter() throws Exception {
        // This is now enabled due to HL7 FHIR Jira - https://jira.hl7.org/browse/FHIR-27739
        // https://github.com/IBM/FHIR/issues/1157
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/Patient1");
        parameters.searchParam("_id", explanationOfBenefitInPatientId);
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test
    public void testExplanationOfBenefitLastUpdated() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_lastUpdated", "le1990");
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 0);

        // Sometimes we have too many resources accumulated over time, and then we need to
        // do some sorting to ensure a deterministic outcome.
        parameters = new FHIRParameters();
        parameters.searchParam("_lastUpdated", "gt1990");
        parameters.searchParam("_count", "10");
        parameters.searchParam("_sort", "-_lastUpdated");
        response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test
    public void testCARINBlueButton_Coverage_Patient_SearchParameter() throws Exception {
        // Name: CARINBlueButton_Coverage_Patient_SearchParameter
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/Patient1");
        FHIRResponse response = client.search(Coverage.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, coverageId);
    }

    @Test
    public void testCARINBlueButton_ExplanationOfBenefit_Created_SearchParameter() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("created", "2019-11-02T00:00:00+00:00");
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test
    public void testComplicatedInclude() throws Exception {
        /*
         * This example is per:
         * https://confluence.hl7.org/pages/viewpage.action?pageId=82911348&preview=/82911348/82911352/CARIN%20BB%
         * 20RESTful%20API%20Combined%20-%20FHIR-26702%20-%20FHIR-26693%200513%202020.docx RESTFUL API SHALL
         */
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/Patient1");
        parameters.searchParam("_lastUpdated", "ge2020");
        parameters.searchParam("_include", "ExplanationOfBenefit:patient");
        parameters.searchParam("_include", "ExplanationOfBenefit:provider");
        parameters.searchParam("_include", "ExplanationOfBenefit:care-team");
        parameters.searchParam("_include", "ExplanationOfBenefit:coverage");
        // parameters.searchParam("_include:iterate", "Coverage:payor");

        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test(enabled = false)
    public void testExplanationOfBenefitServiceDate() throws Exception {
        /*
         * This should be coming in a new CARIN BB Rev.
         * https://confluence.hl7.org/pages/viewpage.action?pageId=82911348&preview=/82911348/82911352/CARIN%20BB%
         * 20RESTful%20API%20Combined%20-%20FHIR-26702%20-%20FHIR-26693%200513%202020.docx RESTFUL API SHALL
         * service-date date GET [base]/ExplanationOfBenefit?service-date=[prefix][date] Shall only be supported in
         * a combination with patient
         */
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("service-date", "ge2014");
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }

    @Test(enabled = false)
    public void testExplanationOfBenefitType() throws Exception {
        /*
         * This should be coming in a new CARIN BB Rev.
         * https://confluence.hl7.org/pages/viewpage.action?pageId=82911348&preview=/82911348/82911352/CARIN%20BB%
         * 20RESTful%20API%20Combined%20-%20FHIR-26702%20-%20FHIR-26693%200513%202020.docx RESTFUL API SHALL type
         * token GET [base]/ExplanationOfBenefit?type=[system]|[code] Shall only be supported in a combination with
         * patient
         */
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("type", "http://terminology.hl7.org/CodeSystem/claim-type|institutional");
        FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, explanationOfBenefitInPatientId);
    }
}