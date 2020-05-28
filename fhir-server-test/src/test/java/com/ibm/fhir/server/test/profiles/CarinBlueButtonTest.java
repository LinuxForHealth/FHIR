/*
 * (C) Copyright IBM Corp. 2020
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

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CareTeam;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.PractitionerRole;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;

/**
 * Tests using http://hl7.org/fhir/us/carin-bb/2020Feb/Examples.html And the given profile.
 */
public class CarinBlueButtonTest extends ProfilesTestBase {
    private String coverageId = null;
    private String careTeamId = null;
    private String organizationId = null;
    private String organizationOrg1Id = null;
    private String organizationOrg45Id = null;
    private String locationId = null;
    private String patientId = null;
    private String practitionerRoleId = null;
    private String practitionerId = null;
    private String explanationOfBenefitId = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-coverage|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-explanationofbenefit|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-explanationofbenefit-inpatient-facility|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-explanationofbenefit-outpatient-facility|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-explanationofbenefit-pharmacy|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-explanationofbenefit-professional-nonclinician|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-organization|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-patient|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-practitionerrole|0.1.0",
            "http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-relatedperson|0.1.0");
        //@formatter:on
    }

    // Load Organization Resources
    public void loadOrganization() throws Exception {
        String resource = "json/spec/organization-example.json";
        WebTarget target = getWebTarget();
        Organization organization = TestUtil.readExampleResource(resource);

        // Add profile + extra fields required by profile.
        Canonical profile = Canonical.of("http://hl7.org/fhir/us/carin/StructureDefinition/carin-bb-organization");
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

        System.out.println(response.readEntity(String.class));
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        organizationId = getLocationLogicalId(response);
        response = target.path("Organization/" + organizationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadOrganizationOrg1() throws Exception {
        String resource = "json/profiles/fhir-ig-carin-bb/Organization-Org1.json";
        WebTarget target = getWebTarget();
        Organization organization = TestUtil.readExampleResource(resource);
        Entity<Organization> entity = Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        organizationOrg1Id = getLocationLogicalId(response);
        response = target.path("Organization/" + organizationOrg1Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadOrganizationOrg45() throws Exception {
        String resource = "json/profiles/fhir-ig-carin-bb/Organization-Org45.json";
        WebTarget target = getWebTarget();
        Organization organization = TestUtil.readExampleResource(resource);
        Entity<Organization> entity = Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        organizationOrg45Id = getLocationLogicalId(response);

        response = target.path("Organization/" + organizationOrg45Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Coverage Resources
    public void loadCoverage() throws Exception {
        WebTarget target = getWebTarget();
        Coverage coverage = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/Coverage-Coverage1.json");

        Reference org45ref = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Organization/" + organizationOrg45Id)).build();
        coverage = coverage.toBuilder().payor(org45ref).build();

        Entity<Coverage> entity = Entity.entity(coverage, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Coverage").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        coverageId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response = target.path("Coverage/" + coverageId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Patient Resources
    public void loadPatient() throws Exception {
        WebTarget target = getWebTarget();
        Patient patient = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/Patient-Patient1.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
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
    }

    // Load Explanation of Benefits Resources
    public void loadExplanationOfBenefits() throws Exception {
        WebTarget target = getWebTarget();

        ExplanationOfBenefit eob = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/ExplanationOfBenefit-EOB1.json");
        Entity<ExplanationOfBenefit> entity = Entity.entity(eob, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("ExplanationOfBenefit").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        explanationOfBenefitId = getLocationLogicalId(response);

        response = target.path("ExplanationOfBenefit/" + explanationOfBenefitId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load PractitionerRole Resources
    public void loadPractitionerRole() throws Exception {
        Reference location = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Location/" + locationId)).build();

        Reference practitioner = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Practitioner/" + practitionerId)).build();

        Reference organization = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Organization/" + organizationOrg45Id)).build();

        PractitionerRole practitionerRole = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/PractitionerRole-PractitionerRole1.json");
        practitionerRole = practitionerRole.toBuilder().location(location).organization(organization).practitioner(practitioner).build();

        WebTarget target = getWebTarget();
        Entity<PractitionerRole> entityPractitionerRole = Entity.entity(practitionerRole, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("PractitionerRole").request().post(entityPractitionerRole, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        practitionerRoleId = getLocationLogicalId(response);
        response = target.path("PractitionerRole/" + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Resources
    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadLocation();
            loadOrganization();
            loadOrganizationOrg1();
            loadOrganizationOrg45();
            loadProvider();
            loadPractitionerRole();
            loadCoverage();
            loadPatient();
            loadCareteam();
            loadExplanationOfBenefits();
        }
    }

    // Delete Resources
    public void deleteOrganization() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Organization/" + organizationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteOrganizationOrg1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Organization/" + organizationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteOrganizationOrg45() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Organization/" + organizationOrg45Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete Coverage Resources
    public void deleteCoverage() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Coverage/" + coverageId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete Patient Resources
    public void deletePatient() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete Provider Resources
    public void deleteProvider() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Practitioner/" + practitionerId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete CareTeam Resources
    public void deleteCareteam() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("CareTeam/" + careTeamId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete Location Resources
    public void deleteLocation() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete Explanation of Benefits Resources
    public void deleteExplanationOfBenefits() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("ExplanationOfBenefit/" + explanationOfBenefitId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Delete PractitionerRole Resources
    public void deletePractitionerRole() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("PractitionerRole/" + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteLocation();
            deleteOrganization();
            deleteOrganizationOrg1();
            deleteOrganizationOrg45();
            deletePractitionerRole();
            deleteCoverage();
            deletePatient();
            deleteProvider();
            deleteCareteam();
            deleteExplanationOfBenefits();
        }
    }

    @Test
    public void testLocationId() throws Exception {
        if (!skip) {
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
    }

    @Test
    public void testPractitionerId() throws Exception {
        if (!skip) {
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
    }

    @Test
    public void testPractitionerRoleIdIncludeOrg() throws Exception {
        if (!skip) {
            // A common call -> GET [base]/PractitionerRole?_id=[id]
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", practitionerRoleId);
            parameters.searchParam("_include", "PractitionerRole:organization");
            FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, practitionerRoleId, organizationOrg45Id);
        }
    }

    @Test
    public void testPractitionerRoleIdIncludePractitioner() throws Exception {
        if (!skip) {
            // A common call -> GET [base]/PractitionerRole?_id=[id]
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", practitionerRoleId);
            parameters.searchParam("_include", "PractitionerRole:practitioner");
            FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, practitionerRoleId, practitionerId);
        }
    }

    @Test
    public void testPractitionerRoleIdIncludeOrgAndPractitioner() throws Exception {
        if (!skip) {
            // A common call -> GET [base]/PractitionerRole?_id=[id]
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", practitionerRoleId);
            parameters.searchParam("_include", "PractitionerRole:organization");
            // From US Core (2nd indirect profile being tested)
            parameters.searchParam("_include", "PractitionerRole:practitioner");
            FHIRResponse response = client.search(PractitionerRole.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, practitionerRoleId, organizationOrg45Id, practitionerId);
        }
    }

    @Test
    public void testPatientId() throws Exception {
        if (!skip) {
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
    }

    @Test
    public void testOrganizationId() throws Exception {
        if (!skip) {
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
    }

    @Test
    public void testCoverageIdWithInclude() throws Exception {
        if (!skip) {
            // A common call -> GET [base]/Coverage?_id=[id]&_include=Coverage:payor
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", coverageId);
            parameters.searchParam("_include", "Coverage:payor");
            FHIRResponse response = client.search(Coverage.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, coverageId, organizationOrg45Id);
        }
    }

    @Test
    public void testExplanationOfBenefitId() throws Exception {
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_id", explanationOfBenefitId);
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test
    public void testExplanationOfBenefitIdentifierNoSystem() throws Exception {
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("identifier", "|4E93-BE91-EAC1977941A8");
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test
    public void testExplanationOfBenefitIdentifierWithSystem() throws Exception {
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("identifier", "http://www.bcbs.com/fhir/ns/NamingSystem/explanationOfBenefit-identifier|4E93-BE91-EAC1977941A8");
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test
    public void testCARINBlueButton_ExplanationOfBenefit_Patient_SearchParameter() throws Exception {
        if (!skip) {
            // This is now enabled due to HL7 FHIR Jira - https://jira.hl7.org/browse/FHIR-27739
            // https://github.com/IBM/FHIR/issues/1157
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/Patient1");
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test
    public void testExplanationOfBenefitLastUpdated() throws Exception {
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("_lastUpdated", "le1990");
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() == 0);

            parameters = new FHIRParameters();
            parameters.searchParam("_lastUpdated", "ge1990");
            response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test
    public void testCARINBlueButton_Coverage_Patient_SearchParameter() throws Exception {
        // Name: CARINBlueButton_Coverage_Patient_SearchParameter
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("patient", "Patient/Patient1");
            FHIRResponse response = client.search(Coverage.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, coverageId);
        }
    }

    @Test
    public void testCARINBlueButton_ExplanationOfBenefit_Created_SearchParameter() throws Exception {
        // Enabled per https://jira.hl7.org/browse/FHIR-27738 and https://github.com/IBM/FHIR/issues/1156
        // Name: CARINBlueButton_ExplanationOfBenefit_Created_SearchParameter (note it uses the created code in the
        // spec)
        // The Search Parameter embedded in the spec had a bad code, should be 'created'.
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("created", "ge2017-04-15");
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test
    public void testComplicatedInclude() throws Exception {
        if (!skip) {
            /* This example is per:
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
            // https://github.com/IBM/FHIR/issues/1158
            // parameters.searchParam("_include:iterate", "PractitionerRole:practitioner");
            // parameters.searchParam("_include:iterate", "PractitionerRole:organization");
            // parameters.searchParam("_include:iterate", "Coverage:payor");

            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test(enabled = false)
    public void testExplanationOfBenefitServiceDate() throws Exception {
        if (!skip) {
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
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }

    @Test(enabled = false)
    public void testExplanationOfBenefitType() throws Exception {
        if (!skip) {
            /*
             * This should be coming in a new CARIN BB Rev.
             * https://confluence.hl7.org/pages/viewpage.action?pageId=82911348&preview=/82911348/82911352/CARIN%20BB%
             * 20RESTful%20API%20Combined%20-%20FHIR-26702%20-%20FHIR-26693%200513%202020.docx RESTFUL API SHALL type
             * token GET [base]/ExplanationOfBenefit?type=[system]|[code] Shall only be supported in a combination with
             * patient
             */
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("type", "http://hl7.org/fhir/us/carin/CodeSystem/carin-bb-claim-type|inpatient-facility");
            FHIRResponse response = client.search(ExplanationOfBenefit.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, explanationOfBenefitId);
        }
    }
}