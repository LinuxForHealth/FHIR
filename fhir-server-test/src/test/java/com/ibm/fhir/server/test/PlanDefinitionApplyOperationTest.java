/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.ActivityDefinition;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;

/**
 * Test cases for the <b>$apply</b> operation
 *
 * @link http://hl7.org/fhir/plandefinition-operation-apply.html
 *
 * The scenarios below model the FHIR Connectathon track:
 * @link https://confluence.hl7.org/display/FHIR/2019-09+Care+Planning+and+Management+Track
 * @link http://hl7.org/fhir/plandefinition-examples.html
 * @link http://hl7.org/fhir/activitydefinition-order-serum-zika-dengue-virus-igm.json
 */
public class PlanDefinitionApplyOperationTest extends FHIRServerTestBase {

    public static Boolean DEBUG_APPLY = Boolean.FALSE;
    public static final String TEST_GROUP_NAME = "plan-defintion-apply-operation";

    // URL Pattern:
    public static final String BASE_VALID_URL = "PlanDefinition/$apply";
    public static final String BASE_INVALID_URL = "%s/$apply";
    public static final String RESOURCE_VALID_URL = "PlanDefinition/%s/$apply";
    public static final String RESOURCE_INVALID_URL = "%s/%s/$apply";

    private String patientId = null;
    private String practitionerId = null;
    private String planDefinitionId = null;
    private String adId = null;
    private String adId2 = null;

    private PlanDefinition planDefinitionResource = null;

    @Test(groups = { TEST_GROUP_NAME })
    public void loadTestData() throws Exception {
        // Subject - Patient
        final String PATIENT_JSON = "Patient_DavidOrtiz.json";
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource(PATIENT_JSON);
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);
        if (DEBUG_APPLY) {
            System.out.println("Patient ID => [" + patientId + "]");
        }
        addToResourceRegistry("Patient", patientId);

        // Subject - Practitioner
        Practitioner doctor = TestUtil.readLocalResource("DrStrangelove.json");
        Entity<Practitioner> entity1 = Entity.entity(doctor, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Practitioner").request().post(entity1, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        practitionerId = getLocationLogicalId(response);
        if (DEBUG_APPLY) {
            System.out.println("Practitioner ID => [" + practitionerId + "]");
        }
        addToResourceRegistry("Practitioner", practitionerId);

        // ActivityDefinition 1
        ActivityDefinition ad = TestUtil.readLocalResource("ActivityDefinition-1.json");
        Entity<ActivityDefinition> entity2 = Entity.entity(ad, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("ActivityDefinition").request().post(entity2, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        adId = getLocationLogicalId(response);
        if (DEBUG_APPLY) {
            System.out.println("ActivityDefinition ID => [" + adId + "]");
        }
        addToResourceRegistry("ActivityDefinition", adId);

        // ActivityDefinition 2
        ActivityDefinition ad2 = TestUtil.readLocalResource("ActivityDefinition-2.json");
        Entity<ActivityDefinition> entity3 = Entity.entity(ad2, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("ActivityDefinition").request().post(entity3, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        adId2 = getLocationLogicalId(response);
        if (DEBUG_APPLY) {
            System.out.println("ActivityDefinition ID => [" + adId2 + "]");
        }
        addToResourceRegistry("ActivityDefinition", adId2);

        // Many need to create
        // Subject - Organization
        // Subject - Location
        // Subject - Device
        // Subject - Group
        // Subject - Invalid Resource Type (Negative Test)

        /*
         * Create PlanDefinitions
         */
        PlanDefinition planDefinition = TestUtil.readLocalResource("PlanDefinition-1.json");
        Entity<PlanDefinition> entityPd = Entity.entity(planDefinition, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("PlanDefinition").request().post(entityPd, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the practitioner's logical id value.
        planDefinitionId = getLocationLogicalId(response);
        if (DEBUG_APPLY) {
            System.out.println("PlanDefinition ID => [" + planDefinitionId + "]");
        }
        addToResourceRegistry("PlanDefinition", planDefinitionId);

        // Store the Plan Definition
        response = target.path("PlanDefinition/" + planDefinitionId).request().get(Response.class);

        assertResponse(response, Response.Status.OK.getStatusCode());
        planDefinitionResource = response.readEntity(PlanDefinition.class);

    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = "loadTestData")
    public void testSubjectPatient() {
        List<String> subjects = Arrays.asList("Patient/" + patientId);

        // Valid - Instance Level.
        // ApplyOperationResult result =
        // runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects,
        // null, practitionerId, null, null, null, null, null, null);

        Response response =
                doPost(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects, null, "Practitioner/" + practitionerId, "Organization/my-org", "user-type", "user-language", "user-task-context", "my-setting", "my-setting-context");
        assertEquals(response.getStatus(), 200);

        CarePlan carePlan = response.readEntity(CarePlan.class);
        if (DEBUG_APPLY) {
            System.out.println(carePlan);
        }
        assertNotNull(carePlan);
    }


    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = "loadTestData")
    public void testSubjectPatient_GET() {
        List<String> subjects = Arrays.asList("Patient/" + patientId);

        // Valid - Instance Level.
        // ApplyOperationResult result =
        // runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects,
        // null, practitionerId, null, null, null, null, null, null);

        Response response =
                doGet(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects, null, "Practitioner/" + practitionerId, "Organization/my-org", null, null, null, null, null);
        assertEquals(response.getStatus(), 200);

        CarePlan carePlan = response.readEntity(CarePlan.class);
        if (DEBUG_APPLY) {
            System.out.println(carePlan);
        }
        assertNotNull(carePlan);
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = "loadTestData")
    public void testSubjectPatient_GET_nonPrimitiveParameters() {
        List<String> subjects = Arrays.asList("Patient/" + patientId);

        // Valid - Instance Level.
        // ApplyOperationResult result =
        // runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects,
        // null, practitionerId, null, null, null, null, null, null);

        Response response =
                doGet(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects, null, "Practitioner/" + practitionerId, "Organization/my-org", "user-type", "user-language", "user-task-context", "my-setting", "my-setting-context");
        assertEquals(response.getStatus(), 400);
    }

    public Response doPost(String mimeType, boolean root, boolean invalid, String planDefinition, List<String> subject, String encounter, String practitioner,
        String organization, String userType, String userLanguage, String userTaskContext, String setting, String settingContext) {

        Parameters parameters = generateParameters(planDefinitionId, subject, null, practitioner, null, null, null, null, null, null);
        WebTarget target = getWebTarget();

        // valid && root by default
        // URL: [base]/PlanDefinition/$apply
        String path = BASE_VALID_URL;
        if (invalid && root) {
            // URL: [base]/Patient/$apply
            // Result in 400
            path = String.format(BASE_INVALID_URL, "Patient");
        } else if (invalid && !root) {
            // URL: [base]/Patient/[ID]/$apply
            // Result in 400
            path = String.format(RESOURCE_INVALID_URL, "Patient", planDefinition);
        } else if (!invalid && !root) {
            // URL: [base]/PlanDefinition/[ID]/$apply
            path = String.format(RESOURCE_VALID_URL, planDefinition);
        }

        target = target.path(path);

        // Only if at the root.
        if (root) {
            target = addQueryParameter(target, "planDefinition", planDefinition);
        }
        target = addQueryParameterList(target, "subject", subject);
        target = addQueryParameter(target, "encounter", encounter);
        target = addQueryParameter(target, "practitioner", practitioner);
        target = addQueryParameter(target, "organization", organization);
        target = addQueryParameter(target, "userType", userType);
        target = addQueryParameter(target, "userLanguage", userLanguage);
        target = addQueryParameter(target, "userTaskContext", userTaskContext);
        target = addQueryParameter(target, "setting", setting);
        target = addQueryParameter(target, "settingContext", settingContext);

        if (DEBUG_APPLY) {
            System.out.println("URL -> " + target.getUri());
        }

        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        return target.request(mimeType).post(entity, Response.class);
    }

    public Response doGet(String mimeType, boolean root, boolean invalid, String planDefinition, List<String> subject, String encounter, String practitioner,
        String organization, String userType, String userLanguage, String userTaskContext, String setting, String settingContext) {

        WebTarget target = getWebTarget();

        // valid && root by default
        // URL: [base]/PlanDefinition/$apply
        String path = BASE_VALID_URL;
        if (invalid && root) {
            // URL: [base]/Patient/$apply
            // Result in 400
            path = String.format(BASE_INVALID_URL, "Patient");
        } else if (invalid && !root) {
            // URL: [base]/Patient/[ID]/$apply
            // Result in 400
            path = String.format(RESOURCE_INVALID_URL, "Patient", planDefinition);
        } else if (!invalid && !root) {
            // URL: [base]/PlanDefinition/[ID]/$apply
            path = String.format(RESOURCE_VALID_URL, planDefinition);
        }

        target = target.path(path);

        // Only if at the root.
        if (root) {
            target = addQueryParameter(target, "planDefinition", planDefinition);
        }
        target = addQueryParameterList(target, "subject", subject);
        target = addQueryParameter(target, "encounter", encounter);
        target = addQueryParameter(target, "practitioner", practitioner);
        target = addQueryParameter(target, "organization", organization);
        target = addQueryParameter(target, "userType", userType);
        target = addQueryParameter(target, "userLanguage", userLanguage);
        target = addQueryParameter(target, "userTaskContext", userTaskContext);
        target = addQueryParameter(target, "setting", setting);
        target = addQueryParameter(target, "settingContext", settingContext);

        if (DEBUG_APPLY) {
            System.out.println("URL -> " + target.getUri());
        }

        return target.request(mimeType).accept(FHIRMediaType.APPLICATION_FHIR_JSON).get();
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = "loadTestData")
    public void testEmptySubjectPatient() {
        List<String> subjects = Arrays.asList("");

        // Valid - Instance Level.
        ApplyOperationResult result =
                runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects, null, practitionerId, null, null, null, null, null, null);

        assertEquals(result.getResponse().getStatus(), 400);

    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = "loadTestData")
    public void testEmptyPlanDefinitionPatient() {
        List<String> subjects = Arrays.asList(patientId);

        // Valid - Instance Level.
        ApplyOperationResult result =
                runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, "", subjects, null, practitionerId, null, null, null, null, null, null);

        assertEquals(result.getResponse().getStatus(), 400);

    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = "loadTestData")
    public void testWrongPlanDefinitionPatient() {
        List<String> subjects = Arrays.asList(patientId);

        String badPlanDefinitionId = "BAD-BAD-BAD-BAD";

        // Valid - Instance Level.
        ApplyOperationResult result =
                runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, badPlanDefinitionId, subjects, null, practitionerId, null, null, null, null, null, null);

        assertEquals(result.getResponse().getStatus(), 404);

    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testEmptySubjectListPatient() {
        List<String> subjects = Collections.emptyList();

        // Valid - Instance Level.
        ApplyOperationResult result =
                runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinitionId, subjects, null, practitionerId, null, null, null, null, null, null);

        assertEquals(result.getResponse().getStatus(), 400);
    }

    public Parameters generateParameters(String planDefinition, List<String> subject, String encounter, String practitioner, String organization,
        String userType, String userLanguage, String userTaskContext, String setting, String settingContext) {
        List<Parameter> parameters = new ArrayList<>();

        if (planDefinition != null) {
            parameters.add(Parameter.builder().name(string("planDefinition")).resource(planDefinitionResource).build());
        }

        if (subject != null) {
            for (String s : subject) {
                parameters.add(Parameter.builder().name(string("subject")).value(string(s)).build());
            }
        }

        if (encounter != null) {
            parameters.add(Parameter.builder().name(string("encounter")).value(string(encounter)).build());
        }

        if (practitioner != null) {
            parameters.add(Parameter.builder().name(string("practitioner")).value(string(practitioner)).build());
        }

        if (organization != null) {
            parameters.add(Parameter.builder().name(string("organization")).value(string(organization)).build());
        }

        if (userType != null) {
            CodeableConcept cc = CodeableConcept.builder().text(string("userType")).coding(Coding.builder().code(Code.of(userType)).build()).build();
            parameters.add(Parameter.builder().name(string("userType")).value(cc).build());
        }

        if (userLanguage != null) {
            CodeableConcept cc = CodeableConcept.builder().text(string("userLanguage")).coding(Coding.builder().code(Code.of(userLanguage)).build()).build();
            parameters.add(Parameter.builder().name(string("userLanguage")).value(cc).build());
        }
        if (userTaskContext != null) {
            CodeableConcept cc = CodeableConcept.builder()
                    .text(string("userTaskContext"))
                    .coding(Coding.builder().code(Code.of(userTaskContext)).build())
                    .build();
            parameters.add(Parameter.builder().name(string("userTaskContext")).value(cc).build());
        }

        if (setting != null) {
            CodeableConcept cc = CodeableConcept.builder().text(string("setting")).coding(Coding.builder().code(Code.of(setting)).build()).build();
            parameters.add(Parameter.builder().name(string("setting")).value(cc).build());
        }

        if (settingContext != null) {
            CodeableConcept cc = CodeableConcept.builder()
                    .text(string("settingContext"))
                    .coding(Coding.builder().code(Code.of(settingContext)).build())
                    .build();
            parameters.add(Parameter.builder().name(string("settingContext")).value(cc).build());
        }

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        return builder.build();
    }

    /**
     * Runs the valid tests
     *
     * @param planDefinition
     * @param subject
     * @param encounter
     * @param practitioner
     * @param organization
     * @param userType
     * @param userLanguage
     * @param userTaskContext
     * @param setting
     * @param settingContext
     * @return
     */
    public List<ApplyOperationResult> runValidTest(String planDefinition, List<String> subject, String encounter, String practitioner, String organization,
            String userType, String userLanguage, String userTaskContext, String setting, String settingContext) {
        List<ApplyOperationResult> results = new ArrayList<>();

        // Instance Level
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, false, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_XML, false, false, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));

        // Resource Level
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, true, false, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_XML, true, false, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));

        assertNotNull(results);
        assertFalse(results.isEmpty());
        for (ApplyOperationResult result : results) {
            assertEquals(result.getResponseCode(), 200);
        }

        return results;
    }

    /**
     * Runs the invalid tests
     *
     * @param planDefinition
     * @param subject
     * @param encounter
     * @param practitioner
     * @param organization
     * @param userType
     * @param userLanguage
     * @param userTaskContext
     * @param setting
     * @param settingContext
     * @return
     */
    public List<ApplyOperationResult> runInvalidTest(String planDefinition, List<String> subject, String encounter, String practitioner, String organization,
            String userType, String userLanguage, String userTaskContext, String setting, String settingContext) {
        List<ApplyOperationResult> results = new ArrayList<>();
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, false, true, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_XML, false, true, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_JSON, true, true, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));
        results.add(runIndividualPlanDefinition(FHIRMediaType.APPLICATION_FHIR_XML, true, true, planDefinition, subject, encounter, practitioner, organization, userType, userLanguage, userTaskContext, setting, settingContext));

        assertNotNull(results);
        assertFalse(results.isEmpty());
        for (ApplyOperationResult result : results) {
            assertEquals(result.getResponseCode(), 400);
        }

        return results;
    }

    /**
     * runs the individual plan definition call
     *
     * @param mimeType
     * @param root
     * @param invalid
     * @param planDefinition
     * @param subject
     * @param encounter
     * @param practitioner
     * @param organization
     * @param userType
     * @param userLanguage
     * @param userTaskContext
     * @param setting
     * @param settingContext
     * @return
     */
    public ApplyOperationResult runIndividualPlanDefinition(String mimeType, boolean root, boolean invalid, String planDefinition, List<String> subject,
            String encounter, String practitioner, String organization, String userType, String userLanguage, String userTaskContext, String setting,
            String settingContext) {

        ApplyOperationResult result = new ApplyOperationResult();

        // valid && root by default
        // URL: [base]/PlanDefinition/$apply
        String path = BASE_VALID_URL;
        if (invalid && root) {
            // URL: [base]/Patient/$apply
            // Result in 400
            path = String.format(BASE_INVALID_URL, "Patient");
        } else if (invalid && !root) {
            // URL: [base]/Patient/[ID]/$apply
            // Result in 400
            path = String.format(RESOURCE_INVALID_URL, "Patient", planDefinition);
        } else if (!invalid && !root) {
            // URL: [base]/PlanDefinition/[ID]/$apply
            path = String.format(RESOURCE_VALID_URL, planDefinition);
        }

        WebTarget target = getWebTarget();
        target = target.path(path);

        // Only if at the root.
        if (root) {
            target = addQueryParameter(target, "planDefinition", planDefinition);
        }
        target = addQueryParameterList(target, "subject", subject);
        target = addQueryParameter(target, "encounter", encounter);
        target = addQueryParameter(target, "practitioner", practitioner);
        target = addQueryParameter(target, "organization", organization);
        target = addQueryParameter(target, "userType", userType);
        target = addQueryParameter(target, "userLanguage", userLanguage);
        target = addQueryParameter(target, "userTaskContext", userTaskContext);
        target = addQueryParameter(target, "setting", setting);
        target = addQueryParameter(target, "settingContext", settingContext);

        Response r = target.request(mimeType).get();
        result.setResponse(r);
        return result;
    }

    /**
     * add query parameter list
     *
     * @param target
     * @param header
     * @param vals
     * @return
     */
    public WebTarget addQueryParameterList(WebTarget target, String header, List<String> vals) {
        if (header != null) {
            if (vals != null) {
                for (String val : vals) {
                    target = target.queryParam(header, val);
                }
            }

        }
        return target;
    }

    /**
     * adds the query parameter
     *
     * @param target
     * @param header
     * @param val
     * @return
     */
    public WebTarget addQueryParameter(WebTarget target, String header, String val) {
        if (header != null) {
            if (val != null) {
                target = target.queryParam(header, val);
            }

        }
        return target;
    }

    /**
     * Result from the Query
     */
    public static class ApplyOperationResult {
        private int responseCode = 0;

        private Response r;

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public Response getResponse() {
            return r;
        }

        public void setResponse(Response r) {
            this.r = r;
        }
    }
}
