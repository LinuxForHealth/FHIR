/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.CareTeam;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.PractitionerRole;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;

/**
 * Copy over http://hl7.org/fhir/us/carin-bb/2020Feb/Examples.html
 */
public class CarinBlueButtonTest extends ProfilesTestBase {
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

    // Load Organization Resources
    public void loadOrganization() throws Exception {
        String resource = "json/spec/organization-example.json";
        WebTarget target = getWebTarget();
        Organization organization = TestUtil.readExampleResource(resource);
        Entity<Organization> entity = Entity.entity(organization, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Organization").request().post(entity, Response.class);
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
        String out = response.readEntity(String.class);
        System.out.println(out);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        organizationOrg45Id = getLocationLogicalId(response);
        
        response = target.path("Organization/" + organizationOrg45Id).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Coverage Resources
    public void loadCoverage() throws Exception {
        WebTarget target = getWebTarget();
        Coverage coverage = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/Coverage-Coverage1.json");

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
        Response response = target.path("Coverage").request().post(entity, Response.class);
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

        /*
         * "description" : "Who's involved in plan?", "code" : "care-team", "base" : ["CarePlan"], "type" : "reference",
         * "expression" : "CarePlan.careTeam", "xpath" : "f:CarePlan/f:careTeam", "xpathUsage" : "normal", "target" :
         * ["CareTeam"]
         */
    }

    // Load Insurer Resources
    @BeforeClass
    public void loadInsurerClaim() throws Exception {
        /*
         * "code" : "insurer", "base" : ["Claim"], "type" : "reference", "expression" : "Claim.insurer", "xpath" :
         * "f:Claim/f:insurer", "xpathUsage" : "normal", "target" : ["Organization"]
         */
    }

    // Load Location Resources
    public void loadLocation() throws Exception {
        WebTarget target = getWebTarget();

        Location location = TestUtil.readExampleResource("json/spec/location-example.json");
        Entity<Location> entity = Entity.entity(location, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Location").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        locationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Explanation of Benefits Resources
    public void loadExplanationOfBenefits() throws Exception {
        WebTarget target = getWebTarget();

        ExplanationOfBenefit eob = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/");
        Entity<ExplanationOfBenefit> entity = Entity.entity(eob, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("ExplanationOfBenefit").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        explanationOfBenefitId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response = target.path("ExplanationOfBenefit/" + explanationOfBenefitId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load PractitionerRole Resources
    public void loadPractitionerRole() throws Exception {
        Reference location = Reference.builder().reference(com.ibm.fhir.model.type.String.of("Location/" + locationId)).build();
        PractitionerRole practitionerRole = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/PractitionerRole-PractitionerRole1.json");
        practitionerRole = practitionerRole.toBuilder().location(location).build();

        WebTarget target = getWebTarget();
        Entity<PractitionerRole> entityPractitionerRole = Entity.entity(practitionerRole, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("PractitionerRole").request().post(entityPractitionerRole, Response.class);
        String res = response.readEntity(String.class);
        System.out.println(res);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        practitionerRoleId = getLocationLogicalId(response);
        response = target.path("PractitionerRole/" + practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    // Load Resources
    @BeforeClass
    public void loadResources() throws Exception {
        loadLocation(); // Dependent -> loadOrganization, loadPractitionerRole
        loadPractitionerRole(); // Dependent ->
        loadInsurerClaim();
        loadOrganization(); // Dependent -> testCoverage
        loadOrganizationOrg1();
        loadOrganizationOrg45();
        loadCoverage();

        // METHOD_LOAD_COVERAGE, METHOD_LOAD_PATIENT, METHOD_LOAD_PROVIDER, METHOD_LOAD_CARETEAM, METHOD_LOAD_INSURER,
        // METHOD_LOAD_LOCATION
        loadExplanationOfBenefits();
    }

    @AfterClass
    public void deleteResources() {
        WebTarget target = getWebTarget();
        // Response response = target.path("Location/" +
        // locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        // assertResponse(response, Response.Status.OK.getStatusCode());
        // response = target.path("Location/" + locationAbsId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        // assertResponse(response, Response.Status.OK.getStatusCode());

        // Remove Chained Reference
        // response = target.path("PractitionerRole/" +
        // practitionerRoleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
    }

    @Test
    public void testCoverage() throws Exception {
        /*
         * The Coverage resources can represent a Payor with a reference to an Organization resource. The server MAY
         * support the "_include" parameter for search parameters defined on these elements. The client application
         * SHALL support "_include" parameter for search parameters defined on these elements. For example, a server MAY
         * be capable of returning and Organization (payor) for a Coverage using: GET
         * [base]/Coverage?_id=[id]&_include=Coverage:payor
         */

    }

    // ---------------------------------------------------------------------------------------------------------
    // Loads the resources related to CarinBB
    public void loadResourcesDefault() throws Exception {
        WebTarget target = getWebTarget();
        Location location = TestUtil.readExampleResource("json/spec/location-example.json");

        Entity<Location> entity = Entity.entity(location, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Location").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        locationId = getLocationLogicalId(response);

        // Entity<Location> entityAbs = Entity.entity(locationAbs, FHIRMediaType.APPLICATION_FHIR_JSON);
        // Response responseAbs = target.path("Location").request().post(entityAbs, Response.class);
        // assertResponse(responseAbs, Response.Status.CREATED.getStatusCode());

        // locationAbsId = getLocationLogicalId(responseAbs);

        // Next, call the 'read' API to retrieve the new Location and verify it.
        response = target.path("Location/" + locationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testBlueButton() {
        if (!skip) {
            System.out.println("VALID");
        }
    }

    @Test
    public void testCarePlanCareTeamSearch() {
        /*
         * "description" : "Who's involved in plan?", "code" : "care-team", "base" : ["CarePlan"], "type" : "reference",
         * "expression" : "CarePlan.careTeam", "xpath" : "f:CarePlan/f:careTeam", "xpathUsage" : "normal", "target" :
         * ["CareTeam"]
         */

    }

    @Test
    public void testCARINBlueButton_Coverage_Patient_SearchParameter() {
        // Name: CARINBlueButton_Coverage_Patient_SearchParameter
        /*
         * "code": "patient", "base": ["Coverage"], "type": "reference", "expression": "Coverage.beneficiary", "xpath":
         * "f:Coverage/f:beneficiary", CARINBlueButton_Coverage_Patient_SearchParameter
         */
    }

    @Test
    public void testCARINBlueButton_ExplanationOfBenefit_Created_SearchParameter() {
        // Name: CARINBlueButton_ExplanationOfBenefit_Created_SearchParameter

        /*
         * "code": "status", "base": ["ExplanationOfBenefit"], "type": "date", "expression":
         * "explanationofbenefit.created", "xpath": "f:explanationofbenefit/f:created",
         */
    }

    @Test
    public void testCARINBlueButton_ExplanationOfBenefit_Patient_SearchParameter() {
        // Name: CARINBlueButton_ExplanationOfBenefit_Created_SearchParameter

        /*
         * "code": "patient", "base": ["ExplanationOfBenefit"], "type": "reference", "expression":
         * "explanationofbenefit.patient", "xpath": "f:explanationofbenefit/f:patient",
         */
    }
}