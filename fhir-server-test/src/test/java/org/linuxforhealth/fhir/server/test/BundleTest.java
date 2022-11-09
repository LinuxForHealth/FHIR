/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.test.TestUtil.isResourceInResponse;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertTrue;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONException;
import org.json.JSONObject;
import org.linuxforhealth.fhir.audit.beans.FHIRContext;
import org.linuxforhealth.fhir.audit.cadf.CadfAttachment;
import org.linuxforhealth.fhir.audit.configuration.ConfigurationTranslator;
import org.linuxforhealth.fhir.audit.mapper.MapperType;
import org.linuxforhealth.fhir.client.FHIRRequestHeader;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.config.ConfigurationService;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.core.HTTPReturnPreference;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Base64Binary;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.HTTPVerb;
import org.linuxforhealth.fhir.model.type.code.IssueType;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * This class tests 'batch' and 'transaction' interactions.
 */
public class BundleTest extends FHIRServerTestBase {
    // Set this to true to have the request and response bundles displayed on the
    // console.
    private boolean DEBUG = false;

    // Set this to true to have the request and response bundles pretty printed on
    // the console when debug = true
    private boolean prettyPrint = true;

    // Variables used by the batch tests.
    private Patient patientB1 = null;
    private Patient patientB2 = null;
    private Patient patientB3 = null;
    private String locationB1 = null;

    // Variables used by the batch tests for version-aware updates.
    private Patient patientBVA1 = null;
    private Patient patientBVA2 = null;

    // Variables used by the batch tests for delete.
    private Patient patientBD1 = null;
    private Patient patientBD2 = null;
    private Patient patientBCD1 = null;

    // Variables used by the transaction tests for delete.
    private Patient patientTD1 = null;
    private Patient patientTD2 = null;
    private Patient patientTCD1 = null;
    private Patient patientTCD2 = null;

    // Variables used by the transaction tests.
    private Patient patientT1 = null;
    private Patient patientT2 = null;
    private String locationT1 = null;

    // Variables used by the transaction tests for version-aware updates.
    private Patient patientTVA1 = null;
    private Patient patientTVA2 = null;

    private Boolean updateCreateEnabled = null;
    private Boolean transactionSupported = null;
    private Boolean compartmentSearchSupported = null;
    private Boolean deleteSupported = null;

    private static final String ORG_EXTENSION_URL = "http://my.url.domain.com/acme-healthcare/lab-collection-org";
    private static final String PATIENT_EXTENSION_URL = "http://my.url.domain.com/acme-healthcare/related-patient";

    private static final String PREFER_HEADER_RETURN_REPRESENTATION = "return=representation";
    private static final String PREFER_HEADER_NAME = "Prefer";
    
    private static boolean kafkaAuditEnabled = false;
    private KafkaConsumer<String, String> consumer = null;
    private Properties connectionProps = null;
    private static final Logger logger = Logger.getLogger(BundleTest.class.getName());

    /**
     * Retrieve the server's conformance statement to determine the status of
     * certain runtime options.
     *
     * @throws Exception
     */
    @BeforeClass
    public void retrieveConfig() throws Exception {
        updateCreateEnabled = isUpdateCreateSupported();
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        System.out.println("Update/Create enabled?: " + updateCreateEnabled.toString());

        transactionSupported = isTransactionSupported();
        System.out.println("Transactions supported?: " + transactionSupported.toString());

        compartmentSearchSupported = isComparmentSearchSupported();
        System.out.println("Compartment-based searches supported?: " + compartmentSearchSupported.toString());

        deleteSupported = isDeleteSupported();
        System.out.println("Delete operation supported?: " + deleteSupported.toString());
        
        kafkaAuditEnabled = Boolean.parseBoolean(testProperties.getProperty("test.audit.kafka.enabled", "false"));
        if (kafkaAuditEnabled) {
            setUpConsumer();
        }
        
        
    }

    /**
     * wraps very common pattern read, print, check the basics into one method.
     * @param response
     * @param method
     * @return
     * @throws Exception
     */
    public Bundle getEntityWithExtraWork(FHIRResponse response, String method) throws Exception {
        Bundle responseBundle = response.getResource(Bundle.class);
        commonWork(responseBundle,method);
        return responseBundle;
    }

    /**
     * wraps very common pattern read, print, check the basics into one method.
     * @param response
     * @param method
     * @return
     * @throws Exception
     */
    public Bundle getEntityWithExtraWork(Response response, String method) throws Exception {
        Bundle responseBundle = response.readEntity(Bundle.class);
        commonWork(responseBundle,method);
        return responseBundle;
    }

    public void commonWork(Bundle responseBundle, String method) throws Exception{
        assertNotNull(responseBundle);
        printBundle(method, "response", responseBundle);
        checkForIssuesWithValidation(responseBundle, true, false, DEBUG);
    }

    @Test(groups = { "batch" })
    public void testEmptyBundle() throws Exception {
        WebTarget target = getWebTarget();

        // We're specifically using an invalid bundle type because we want to
        // verify that the server reports on the empty bundle condition before checking
        // the bundle type.
        Bundle bundle = buildBundle(BundleType.BATCH_RESPONSE);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Bundle.type must be either 'batch' or 'transaction'");
    }

    @Test(groups = { "batch" })
    public void testMissingBundleType() throws Exception {
        WebTarget target = getWebTarget();

        JsonObjectBuilder bundleObject = Json.createBuilderFactory(null).createObjectBuilder();
        bundleObject.add("resourceType", "Bundle");

        Entity<JsonObject> entity = Entity.entity(bundleObject.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertTrue(response.getStatus() >= 400);
    }

    @Test(groups = { "batch" })
    public void testIncorrectBundleType() throws Exception {
        WebTarget target = getWebTarget();

        Bundle bundle = Bundle.builder().type(BundleType.BATCH_RESPONSE).build();

        // Add one non-empty entry to allow the entry pass the build validation.
        String patientLocalRef = "urn:Patient_testIncorrectBundleType";

        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        HumanName newName = HumanName.builder().family(string("Doe_testIncorrectBundleType")).given(string("John"))
                .build();
        List<HumanName> emptyList = new ArrayList<HumanName>();
        patient = patient.toBuilder().name(emptyList).name(newName).build();

        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);


        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "Bundle.type must be either 'batch' or 'transaction'");
    }

    @Test(groups = { "batch" })
    public void testMissingRequestField() throws Exception {
        WebTarget target = getWebTarget();

        JsonObject incompleteEntry = Json.createObjectBuilder().add("fullUrl", "http://example.com").build();
        JsonArray entryArray = Json.createArrayBuilder().add(incompleteEntry).build();
        JsonObject bundleWithEntry = TestUtil.getEmptyBundleJsonObjectBuilder()
                .add("entry", entryArray).build();
        Entity<JsonObject> entity = Entity.entity(bundleWithEntry, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertEquals(response.getStatus(), 200);

        Bundle responseBundle = response.readEntity(Bundle.class);
        assertEquals(responseBundle.getEntry().get(0).getResponse().getStatus().getValue(), "400");
    }

    @Test(groups = { "batch" })
    public void testMissingMethod() throws Exception {
        WebTarget target = getWebTarget();

        JsonObjectBuilder bundleObject = TestUtil.getEmptyBundleJsonObjectBuilder();
        JsonObject PatientJsonObject = TestUtil.readJsonObject("Patient_JohnDoe.json");
        JsonObject requestJsonObject = TestUtil.getRequestJsonObject("", "Patient");
        JsonObjectBuilder resourceObject = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject.add( "resource", PatientJsonObject).add("request", requestJsonObject);

        bundleObject.add("Entry", Json.createBuilderFactory(null).createArrayBuilder().add(resourceObject));

        Entity<JsonObject> entity = Entity.entity(bundleObject.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertTrue(response.getStatus() >= 400);
    }

    @Test(groups = { "batch" })
    public void testIncorrectMethod() throws Exception {
        // If the "delete" operation is supported by the server, then we can't use
        // DELETE
        // to test out an incorrect method in a bundle request entry :)
        if (deleteSupported) {
            return;
        }

        String method = "testIncorrectMethod";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, "placeholder", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        printBundle(method, "response", responseBundle);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(),
                "Bundle.Entry.request contains unsupported HTTP method");
    }

    @Test(groups = { "batch" })
    public void testMissingResource() throws Exception {
        WebTarget target = getWebTarget();

        JsonObjectBuilder bundleObject = TestUtil.getEmptyBundleJsonObjectBuilder();
        JsonObject requestJsonObject = TestUtil.getRequestJsonObject("POST", "Patient");
        JsonObjectBuilder resourceObject = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject.add("request", requestJsonObject);

        bundleObject.add("Entry", Json.createBuilderFactory(null).createArrayBuilder().add(resourceObject));

        Entity<JsonObject> entity = Entity.entity(bundleObject.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertTrue(response.getStatus() >= 400);
    }

    @Test(groups = { "batch" })
    public void testExtraneousResource() throws Exception {
        String method = "testExtraneousResource";
        WebTarget target = getWebTarget();
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/123", null, patient);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        printBundle(method, "response", responseBundle);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(),
                "Bundle.Entry.resource not allowed");
    }

    @Test(groups = { "batch" })
    public void testMissingRequestURL() throws Exception {
        WebTarget target = getWebTarget();

        JsonObjectBuilder bundleObject = TestUtil.getEmptyBundleJsonObjectBuilder();
        JsonObject PatientJsonObject = TestUtil.readJsonObject("Patient_JohnDoe.json");
        JsonObject requestJsonObject = TestUtil.getRequestJsonObject("POST", "Patient");
        JsonObjectBuilder resourceObject = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject.add( "resource", PatientJsonObject).add("request", requestJsonObject);

        bundleObject.add("Entry", Json.createBuilderFactory(null).createArrayBuilder().add(resourceObject));

        Entity<JsonObject> entity = Entity.entity(bundleObject.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertTrue(response.getStatus() >= 400);
    }

    @Test(groups = { "batch" })
    public void testIncorrectRequestURL() throws Exception {
        String method = "testMissingRequestURL";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/1/blah/2", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.NOT_FOUND.getStatusCode(),
                "Unrecognized path in request URL");
    }



    @Test(groups = { "batch" })
    public void testInvalidResource() throws Exception {
        WebTarget target = getWebTarget();

        JsonObjectBuilder bundleObject = TestUtil.getEmptyBundleJsonObjectBuilder();
        JsonObject PatientJsonObject = TestUtil.readJsonObject("InvalidPatient.json");
        JsonObject requestJsonObject = TestUtil.getRequestJsonObject("POST", "Patient");
        JsonObjectBuilder resourceObject = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject.add( "resource", PatientJsonObject).add("request", requestJsonObject);

        bundleObject.add("Entry", Json.createBuilderFactory(null).createArrayBuilder().add(resourceObject));

        Entity<JsonObject> entity = Entity.entity(bundleObject.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertTrue(response.getStatus() >= 400);
    }

    @Test(groups = { "batch" })
    public void testInvalidBundle() throws Exception {
        WebTarget target = getWebTarget();
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class),
                "A 'Bundle' resource type is required but a 'Patient' resource type was sent.");
    }

    @Test(groups = { "batch" })
    public void testResourceURLMismatch() throws Exception {
        String method = "testResourceURLMismatch";
        WebTarget target = getWebTarget();
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, patient);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(),
                "does not match type specified in request URI");
    }

    @Test(groups = { "batch" })
    public void testInvalidSecondResource() throws Exception {
        WebTarget target = getWebTarget();


        JsonObjectBuilder bundleObject = TestUtil.getEmptyBundleJsonObjectBuilder();

        JsonObject PatientJsonObject1 = TestUtil.readJsonObject("Patient_DavidOrtiz.json");
        JsonObject PatientJsonObject2 = TestUtil.readJsonObject("InvalidPatient.json");
        JsonObject PatientJsonObject3 = TestUtil.readJsonObject("Patient_JohnDoe.json");
        JsonObject requestJsonObject = TestUtil.getRequestJsonObject("POST", "Patient");

        JsonObjectBuilder resourceObject1 = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject1.add( "resource", PatientJsonObject1).add("request", requestJsonObject);

        JsonObjectBuilder resourceObject2 = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject2.add( "resource", PatientJsonObject2).add("request", requestJsonObject);

        JsonObjectBuilder resourceObject3 = Json.createBuilderFactory(null).createObjectBuilder();
        resourceObject3.add( "resource", PatientJsonObject3).add("request", requestJsonObject);

        bundleObject.add("Entry", Json.createBuilderFactory(null).createArrayBuilder()
                .add(resourceObject1).add(resourceObject2).add(resourceObject3));

        Entity<JsonObject> entity = Entity.entity(bundleObject.build(), FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertTrue(response.getStatus() >= 400);
    }

    @Test(groups = { "batch" })
    public void testBatchCreates() throws Exception {
        String method = "testBatchCreates";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtizEncoding.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 3);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(2), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientB1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientB2 = (Patient) responseBundle.getEntry().get(1).getResource();
        patientB3 = (Patient) responseBundle.getEntry().get(2).getResource();
    }

    @Test(groups = { "batch" })
    public void testBatchCreatesForVersionAwareUpdates() throws Exception {
        String method = "testBatchCreatesForVersionAwareUpdates";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientBVA1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientBVA2 = (Patient) responseBundle.getEntry().get(1).getResource();
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates" })
    public void testBatchUpdates() throws Exception {
        String method = "testBatchUpdates";
        WebTarget target = getWebTarget();

        // Make a small change to each patient.
        patientB2 = patientB2.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.TRUE).build();
        patientB1 = patientB1.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.FALSE).build();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientB2.getId(), null,
                patientB2);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientB1.getId(), null,
                patientB1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        locationB1 = responseBundle.getEntry().get(1).getResponse().getLocation().getValue();

        patientB1 = (Patient) responseBundle.getEntry().get(1).getResource();
        patientB2 = (Patient) responseBundle.getEntry().get(0).getResource();

    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreatesForVersionAwareUpdates" })
    public void testBatchUpdatesVersionAware() throws Exception {
        String method = "testBatchUpdatesVersionAware";
        WebTarget target = getWebTarget();

        // Make a small change to each patient.
        patientBVA2 = patientBVA2.toBuilder().deceased(true).build();
        patientBVA1 = patientBVA1.toBuilder().deceased(false).build();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientBVA2.getId(), "W/\"1\"",
                patientBVA2);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientBVA1.getId(), "W/\"1\"",
                patientBVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdatesVersionAware" })
    public void testBatchUpdatesVersionAwareError1() throws Exception {
        String method = "testBatchUpdatesVersionAwareError1";
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientBVA2);
        Response res1 = target.path("Patient/" + patientBVA2.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientBVA1);
        Response res2 = target.path("Patient/" + patientBVA1.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Make a small change to each patient.
        patientVA2 = patientVA2.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.TRUE).build();
        patientVA1 = patientVA1.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.FALSE).build();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA2.getId(), "W/\"2\"",
                patientVA2);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA1.getId(), "W/\"1\"",
                patientVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(1), Status.PRECONDITION_FAILED.getStatusCode(),
                "If-Match version '1' does not match current latest version of resource: 2");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdatesVersionAware",
            "testBatchUpdatesVersionAwareError1" })
    public void testBatchUpdatesVersionAwareError2() throws Exception {
        String method = "testBatchUpdatesVersionAwareError2";
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientBVA2);
        Response res1 = target.path("Patient/" + patientBVA2.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientBVA1);
        Response res2 = target.path("Patient/" + patientBVA1.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Make a small change to each patient.
        patientVA2 = patientVA2.toBuilder().language(Code.of("en")).build();
        patientVA1 = patientVA1.toBuilder().language(Code.of("en")).build();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA2.getId(), "W/\"1\"",
                patientVA2);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA1.getId(), "W/\"2\"",
                patientVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertBadResponse(responseBundle.getEntry().get(0), Status.PRECONDITION_FAILED.getStatusCode(),
                "If-Match version '1' does not match current latest version of resource: 3");
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdatesVersionAware",
            "testBatchUpdatesVersionAwareError1", "testBatchUpdatesVersionAwareError2" })
    public void testBatchUpdatesVersionAwareError3() throws Exception {
        String method = "testBatchUpdatesVersionAwareError3";
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientBVA2);
        Response res1 = target.path("Patient/" + patientBVA2.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientBVA1);
        Response res2 = target.path("Patient/" + patientBVA1.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Make a small change to each patient.
        patientVA2 = patientVA2.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.TRUE).build();
        patientVA1 = patientVA1.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.FALSE).build();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA2.getId(), "W/\"2\"",
                patientVA2);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA1.getId(), "W/\"2\"",
                patientVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertBadResponse(responseBundle.getEntry().get(0), Status.PRECONDITION_FAILED.getStatusCode(),
                "If-Match version '2' does not match current latest version of resource: 3");
        assertBadResponse(responseBundle.getEntry().get(1), Status.PRECONDITION_FAILED.getStatusCode(),
                "If-Match version '2' does not match current latest version of resource: 3");
    }

    @Test(groups = { "batch" })
    public void testBatchUpdateCreates() throws Exception {
        String method = "testBatchUpdateCreates";
        assertNotNull(updateCreateEnabled);
        if (!updateCreateEnabled.booleanValue()) {
            return;
        }

        Patient p1 = TestUtil.readLocalResource("Patient_DavidOrtiz.json");
        p1 = (Patient)setNewResourceId(p1);

        Patient p2 = TestUtil.readLocalResource("Patient_JohnDoe.json");
        p2 = (Patient)setNewResourceId(p2);

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + p1.getId(), null, p1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + p2.getId(), null, p2);

        printBundle(method, "request", bundle);

        FHIRResponse response = getFHIRClient().batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
    }

    /**
     * Generates a new ID for the specified resource and sets it within the
     * resource.
     */
    private Resource setNewResourceId(Resource resource) {
        return resource.toBuilder().id(UUID.randomUUID().toString()).build();
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchReads() throws Exception {
        String method = "testBatchReads";
        WebTarget target = getWebTarget();

        assertNotNull(locationB1);

        // Perform a 'read' and a 'vread'.
        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/" + patientB2.getId(), null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, locationB1, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchHistory() throws Exception {
        String method = "testBatchHistory";
        WebTarget target = getWebTarget();

        // Get the history for patientB1
        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/" + patientB1.getId() + "/_history",
                null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle historyBundle = (Bundle) responseBundle.getEntry().get(0).getResource();
        assertNotNull(historyBundle);

        List<Bundle.Entry> resultSet = historyBundle.getEntry();
        assertTrue(resultSet.size() > 1);

        boolean result = false;
        for(Bundle.Entry entry : resultSet) {
            if(entry.getResponse() != null){
                String returnedStatus = entry.getResponse().getStatus().getValue();
                assertNotNull(returnedStatus);
                assertTrue(returnedStatus.startsWith("200") || returnedStatus.startsWith("201"));
                result = true;
            }
        }
        assertTrue("Test the entries are processed", result);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchSearch() throws Exception {
        String method = "testBatchSearch";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) responseBundle.getEntry().get(0).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() > 1);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchSearchWithEncoding() throws Exception {
        String method = "testBatchSearch";
        WebTarget target = getWebTarget();

        // input should encode actual values, not encode the separators
        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient?family:exact=Ortiz%26Jeter&_count=1000", null,
                null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);

        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) responseBundle.getEntry().get(0).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() >= 1);

        List<Resource> lstRes = new ArrayList<Resource>();
        for (Bundle.Entry entry : resultSet.getEntry()) {
            lstRes.add(entry.getResource());
        }
        // B1 is Ortiz, B3 is Ortiz&Jeter
        assertTrue(isResourceInResponse(patientB3, lstRes));
        assertTrue(!isResourceInResponse(patientB1, lstRes));
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchPostSearch() throws Exception {
        String method = "testBatchPostSearch";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient/_search?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) responseBundle.getEntry().get(0).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() > 1);
        assertNotNull(resultSet.getEntry().get(0).getResource());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchSearchAll() throws Exception {
        String method = "testBatchSearchAll";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "_search?_id=" + patientB1.getId(), null,
                null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) responseBundle.getEntry().get(0).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() == 1);
        assertNotNull(resultSet.getEntry().get(0).getResource());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchCompartmentSearch() throws Exception {
        String method = "testBatchCompartmentSearch";
        assertNotNull(compartmentSearchSupported);
        if (!compartmentSearchSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Perform a compartment type search.
        // When JPA is configured for persistence, the request completes normally and no
        // results are returned.
        // When Cloudant is configured for persistence, a 400 http status code is
        // returned.
        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/-999/Observation", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) responseBundle.getEntry().get(0).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() == 0);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchMixture() throws Exception {
        String method = "testBatchMixture";
        WebTarget target = getWebTarget();

        // change at least one field so that the update below isn't skipped
        patientB1 = patientB1.toBuilder()
                .deceased(true)
                .build();

        // Perform a mixture of request types.
        Bundle bundle = buildBundle(BundleType.BATCH);
        // create
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        // update
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientB1.getId(), null,
                patientB1);
        // read
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/" + patientB2.getId(), null, null);
        // vread
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, locationB1, null, null);
        // history
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/" + patientB1.getId() + "/_history",
                null, null);
        // search
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 6);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(2), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(3), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(4), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(5), Status.OK.getStatusCode());

        Bundle resultSet;

        // Verify the history results.
        resultSet = (Bundle) responseBundle.getEntry().get(4).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() > 2);

        // Verify the search results.
        resultSet = (Bundle) responseBundle.getEntry().get(5).getResource();
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() >= 1);

        patientB1 = (Patient) responseBundle.getEntry().get(1).getResource();

    }

    @Test(groups = { "transaction" })
    public void testTransactionCreates() throws Exception {
        String method = "testTransactionCreates";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Create a patient with a UUID for the family name.
        // We do this so that we can verify whether or not the patient exists
        // in the datastore without worrying about overlaps with other patient names.
        // We'll load in an existing patient mock data file, then just change the family
        // name to be unique.
        Patient patient1 = TestUtil.readLocalResource("Patient_DavidOrtiz.json");
        String uniqueFamily1 = UUID.randomUUID().toString();
        patient1 = setUniqueFamilyName(patient1, uniqueFamily1);
        Patient patient2 = TestUtil.readLocalResource("Patient_JohnDoe.json");
        String uniqueFamily2 = UUID.randomUUID().toString();
        patient2 = setUniqueFamilyName(patient2, uniqueFamily2);

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        assertSearchResults(target, uniqueFamily1, 1);
        assertSearchResults(target, uniqueFamily2, 1);

        // Save off the two patients for the update test.
        patientT1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientT2 = (Patient) responseBundle.getEntry().get(1).getResource();
    }

    @Test(groups = { "transaction" })
    public void testTransactionCreatesForVersionAwareUpdates() throws Exception {
        String method = "testTransactionCreatesForVersionAwareUpdates";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Create a patient with a UUID for the family name.
        // We do this so that we can verify whether or not the patient exists
        // in the datastore without worrying about overlaps with other patient names.
        // We'll load in an existing patient mock data file, then just change the family
        // name to be unique.
        Patient patient1 = TestUtil.readLocalResource("Patient_DavidOrtiz.json");
        String uniqueFamily1 = UUID.randomUUID().toString();
        patient1 = setUniqueFamilyName(patient1, uniqueFamily1);
        Patient patient2 = TestUtil.readLocalResource("Patient_JohnDoe.json");
        String uniqueFamily2 = UUID.randomUUID().toString();
        patient2 = setUniqueFamilyName(patient2, uniqueFamily2);

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        assertSearchResults(target, uniqueFamily1, 1);
        assertSearchResults(target, uniqueFamily2, 1);

        // Save off the two patients for the update test.
        patientTVA1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientTVA2 = (Patient) responseBundle.getEntry().get(1).getResource();
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionCreates" })
    public void testTransactionUpdates() throws Exception {
        String method = "testTransactionUpdates";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientT1.getName().get(0).getFamily().getValue();
        String family2 = patientT2.getName().get(0).getFamily().getValue();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT1.getId(), null,
                patientT1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT2.getId(), null,
                patientT2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientT1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientT2.getId() + "/_history", 2);

        locationT1 = responseBundle.getEntry().get(0).getResponse().getLocation().getValue();
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionCreatesForVersionAwareUpdates" })
    public void testTransactionUpdatesVersionAware() throws Exception {
        String method = "testTransactionUpdatesVersionAware";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientTVA1.getName().get(0).getFamily().getValue();
        String family2 = patientTVA2.getName().get(0).getFamily().getValue();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientTVA1.getId(), "W/\"1\"",
                patientTVA1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientTVA2.getId(), "W/\"1\"",
                patientTVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdatesVersionAware" })
    public void testTransactionUpdatesVersionAwareError1() throws Exception {
        String method = "testTransactionUpdatesVersionAwareError1";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientTVA2);
        Response res1 = target.path("Patient/" + patientTVA2.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);
        checkForIssuesWithValidation(patientVA2, true, false, DEBUG);

        assertNotNull(patientTVA1);
        Response res2 = target.path("Patient/" + patientTVA1.getId())
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);
        checkForIssuesWithValidation(patientVA1, true, false, DEBUG);

        // Retrieve the family names of the resources to be updated.
        String family1 = patientVA1.getName().get(0).getFamily().getValue();
        String family2 = patientVA2.getName().get(0).getFamily().getValue();

        // Make a small change to each patient.
        patientVA2 = patientVA2.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.TRUE).build();
        patientVA1 = patientVA1.toBuilder().active(org.linuxforhealth.fhir.model.type.Boolean.FALSE).build();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA1.getId(), "W/\"1\"",
                patientVA1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientVA2.getId(), "W/\"2\"",
                patientVA2);
        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.CONFLICT);

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdatesVersionAware",
            "testTransactionUpdatesVersionAwareError1" })
    public void testTransactionUpdatesVersionAwareError2() throws Exception {
        String method = "testTransactionUpdatesVersionAwareError2";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientTVA1.getName().get(0).getFamily().getValue();
        String family2 = patientTVA2.getName().get(0).getFamily().getValue();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientTVA1.getId(), "W/\"2\"",
                patientTVA1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientTVA2.getId(), "W/\"1\"",
                patientTVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.CONFLICT);

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdatesVersionAware",
            "testTransactionUpdatesVersionAwareError2" })
    public void testTransactionUpdatesVersionAwareError3() throws Exception {
        String method = "testTransactionUpdatesVersionAwareError3";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientTVA1.getName().get(0).getFamily().getValue();
        String family2 = patientTVA2.getName().get(0).getFamily().getValue();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientTVA1.getId(), "W/\"1\"",
                patientTVA1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientTVA2.getId(), "W/\"1\"",
                patientTVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.CONFLICT);

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId() + "/_history", 2);
    }

    @Test(groups = { "transaction" })
    public void testTransactionUpdateCreates() throws Exception {
        String method = "testTransactionUpdateCreates";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        assertNotNull(updateCreateEnabled);
        if (!updateCreateEnabled.booleanValue()) {
            return;
        }

        Patient p1 = TestUtil.readLocalResource("Patient_DavidOrtiz.json");
        p1 = (Patient)setNewResourceId(p1);

        Patient p2 = TestUtil.readLocalResource("Patient_JohnDoe.json");
        p2 = (Patient)setNewResourceId(p2);

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + p1.getId(), null, p1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + p2.getId(), null, p2);

        printBundle(method, "request", bundle);

        FHIRResponse response = getFHIRClient().transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);
        checkForIssuesWithValidation(responseBundle, true, false, DEBUG);

        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
    }

    @Test(groups = { "transaction" })
    public void testTransactionCreatesError() throws Exception {
        String method = "testTransactionCreatesError";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Create a patient with a UUID for the family name.
        // We do this so that we can verify whether or not the patient exists
        // in the datastore without worrying about overlaps with other patient names.
        // We'll load in an existing patient mock data file, then just change the family
        // name to be unique.
        Patient patient1 = TestUtil.readLocalResource("Patient_DavidOrtiz.json");
        String uniqueFamily1 = UUID.randomUUID().toString();
        patient1 = setUniqueFamilyName(patient1, uniqueFamily1);
        Patient patient2 = TestUtil.readLocalResource("Patient_JohnDoe.json");
        String uniqueFamily2 = UUID.randomUUID().toString();
        patient2 = setUniqueFamilyName(patient2, uniqueFamily2);

        // Perform a bundled request that should fail.
        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient1);
        // mismatch of URL and resource type
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.INVALID);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdates" })
    public void testTransactionUpdatesError() throws Exception {
        String method = "testTransactionUpdatesError";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientT1.getName().get(0).getFamily().getValue();
        String family2 = patientT2.getName().get(0).getFamily().getValue();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT1.getId(), null,
                patientT1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT2.getId(), null,
                patientT2);
        // This will cause a failure - url mismatch with resource type.
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Observation/1", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.INVALID);

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientT1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientT2.getId() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdates" })
    public void testTransactionInvalidRequestError() throws Exception {
        String method = "testTransactionInvalidRequestError";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientT1.getName().get(0).getFamily().getValue();
        String family2 = patientT2.getName().get(0).getFamily().getValue();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT1.getId(), null,
                patientT1);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT2.getId(), null,
                patientT2);


        // the URL does not have an id and nor is it a conditional update, so this is an error
        // which should be picked up when translating the bundle entry into an interaction
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Observation", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.INVALID);

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientT1.getId() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientT2.getId() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdatesError" })
    public void testTransactionMixture() throws Exception {
        String method = "testTransactionMixture";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Perform a mixture of request types.
        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        // create
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        // update
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Patient/" + patientT1.getId(), null,
                patientT1);
        // read
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/" + patientT2.getId(), null, null);
        // vread
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, locationT1, null, null);
        // history
//      bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient/" + patientT1.getId() + "/_history", null, null);
        // search
//      bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "Patient?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 4);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(2), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(3), Status.OK.getStatusCode());
//      assertGoodGetResponse(responseBundle.getEntry().get(4), Status.OK.getStatusCode());
//      assertGoodGetResponse(responseBundle.getEntry().get(5), Status.OK.getStatusCode());

//      Bundle resultSet;

        // Verify the history results.
//      resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(4).getResource());
//      assertNotNull(resultSet);
//      assertTrue(resultSet.getEntry().size() > 2);

        // Verify the search results.
//      resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(5).getResource());
//      assertNotNull(resultSet);
//      assertTrue(resultSet.getEntry().size() > 3);
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefs1() throws Exception {
        String method = "testBatchLocalRefs1";
        // 3 Observations each referencing its own Patient
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);

        // Add 3 POST entries to create the Patient resources.
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);

            Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

            HumanName newName = HumanName.builder().family(string("Doe_" + Integer.toString(i))).given(string("John"))
                    .build();
            List<HumanName> emptyList = new ArrayList<HumanName>();
            patient = patient.toBuilder().name(emptyList).name(newName).build();

            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);
        }

        // Next, add 3 POST entries to create the Observation resources
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);

            // Create an Observation that will reference the Patient via a local reference.
            Observation obs = TestUtil.readLocalResource("Observation1.json");
            obs = obs.toBuilder().subject(Reference.builder().reference(string(patientLocalRef)).build()).build();

            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, obs);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION).post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);

        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 6);
        for (int i = 0; i < 6; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }

        // Verify that each Observation correctly references its corresponding Patient.
        for (int i = 0; i < 3; i++) {
            Bundle.Entry patientEntry = responseBundle.getEntry().get(i);
            Bundle.Entry obsEntry = responseBundle.getEntry().get(i + 3);

            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = obsEntry.getResource();
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(actualReference, expectedReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefs2() throws Exception {
        String method = "testBatchLocalRefs2";
        // 3 Observations referencing the same patient
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);

        String patientLocalRef = "urn:Patient_1";

        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);

        // Next, add 3 POST entries to create the Observation resources
        for (int i = 1; i <= 3; i++) {
            // Create an Observation that will reference the Patient via a local reference.
            Observation obs = TestUtil.readLocalResource("Observation1.json");

            obs = obs.toBuilder().subject(Reference.builder().reference(string(patientLocalRef)).build()).build();
            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, obs);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 4);
        for (int i = 0; i < 4; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }

        // Verify that each Observation correctly references the Patient.
        for (int i = 1; i < 4; i++) {
            Bundle.Entry patientEntry = responseBundle.getEntry().get(0);
            Bundle.Entry obsEntry = responseBundle.getEntry().get(i);

            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = obsEntry.getResource();
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(actualReference, expectedReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefs3() throws Exception {
        // 3 Observations each referencing its own Patient
        // 3 PUTs (Observations) before 3 POSTs (Patients)
        String method = "testBatchLocalRefs3";
        WebTarget target = getWebTarget();

        // Create 3 Observations via a bundled request.
        Observation[] newObservations = createObservations("Observation1.json", 3);

        // Create the request bundle.
        Bundle bundle = buildBundle(BundleType.BATCH);

        // Next, add 3 PUT entries to update the Observation resources.
        for (int i = 1; i <= 3; i++) {
            Observation obs = newObservations[i - 1];

            // Update the previously-created Observation to add a subject and extension.
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);
            obs = obs.toBuilder().subject(Reference.builder().reference(string(patientLocalRef)).build())
                    .extension(Extension.builder().url(PATIENT_EXTENSION_URL)
                            .value(Reference.builder().reference(string(patientLocalRef)).build()).build())
                    .build();

            bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, "Observation/" + obs.getId(), null, obs);
        }

        // Add 3 POST entries to create the Patient resources.
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);

            Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

            HumanName newName = HumanName.builder().family(string("Doe_" + Integer.toString(i))).given(string("John"))
                    .build();
            List<HumanName> emptyList = new ArrayList<HumanName>();
            patient = patient.toBuilder().name(emptyList).name(newName).build();

            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        // Verify that each of the Observations was updated,
        // and that each of the Patients was created.
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 6);
        for (int i = 0; i < 3; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.OK.getStatusCode());
        }
        for (int i = 3; i < 6; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }

        // Verify that each Observation correctly references its corresponding Patient.
        for (int i = 0; i < 3; i++) {
            Bundle.Entry obsEntry = responseBundle.getEntry().get(i);
            Bundle.Entry patientEntry = responseBundle.getEntry().get(i + 3);

            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the response entry.
            Resource resource = obsEntry.getResource();
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;

            // Verify the subject field.
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(actualReference, expectedReference);

            // Verify the "related-patient" reference in the extension attribute.
            assertNotNull(obs.getExtension().get(0));
            assertNotNull(obs.getExtension().get(0).getValue());
            Reference extRef = (Reference) obs.getExtension().get(0).getValue();
            String actualExtReference = extRef.getReference().getValue();
            assertEquals(actualExtReference, expectedReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefs4() throws Exception {
        String method = "testBatchLocalRefs4";
        // 3 Observations each referencing its own Patient
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);

        // Add 3 POST entries to create the Patient resources.
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = (i < 3) ? "https://test.com/fhir-server/api/v4/Patient/" + Integer.toString(i) : "Patient/3";

            Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

            HumanName newName = HumanName.builder().family(string("Doe_" + Integer.toString(i))).given(string("John"))
                    .build();
            List<HumanName> emptyList = new ArrayList<HumanName>();
            patient = patient.toBuilder().name(emptyList).name(newName).build();

            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);
        }

        // Next, add 3 POST entries to create the Observation resources
        for (int i = 1; i <= 3; i++) {
            // Create an Observation that will reference the Patient via a local reference.
            String observationLocalRef;
            Observation obs = TestUtil.readLocalResource("Observation1.json");
            if (i == 1) {
                obs = obs.toBuilder().subject(Reference.builder().reference(string("Patient/1")).build()).build();
                observationLocalRef = "https://test.com/fhir-server/api/v4/Observation/1";
            } else if (i == 2) {
                obs = obs.toBuilder().subject(Reference.builder().reference(string("https://test.com/fhir-server/api/v4/Patient/2")).build()).build();
                observationLocalRef = "https://test2.com/fhir-server/api/v4/Observation/2";
            } else {
                obs = obs.toBuilder().subject(Reference.builder().reference(string("Patient/3")).build()).build();
                observationLocalRef = "urn:3";
            }
            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, obs, observationLocalRef);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION).post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);

        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 6);
        for (int i = 0; i < 6; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }

        // Verify that each Observation correctly references its corresponding Patient.
        for (int i = 0; i < 3; i++) {
            Bundle.Entry patientEntry = responseBundle.getEntry().get(i);
            Bundle.Entry obsEntry = responseBundle.getEntry().get(i + 3);

            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = obsEntry.getResource();
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(actualReference, expectedReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchErrorDuplicateUrl() throws Exception {
        // duplicate fullUrl values
        String method = "testBatchErrorDuplicateUrl";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);

        // Add 2 POST entries to create the Patient resources but use duplicate fullUrl
        // values.
        String patientLocalRef = "urn:Patient_X";
        Patient patient = TestUtil.getMinimalResource(Patient.class);
        for (int i = 1; i <= 2; i++) {
            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        checkForIssuesWithValidation(responseBundle, true, false, DEBUG);

        // Verify that the first patient was created, and the second was not.
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(1), Status.BAD_REQUEST.getStatusCode(),
                "Duplicate local identifier encountered in bundled request entry:");
    }

    @Test(groups = { "transaction" })
    public void testTransactionLocalRefs1() throws Exception {
        // 1 Patient referencing an Organization and a Practitioner
        // and 2 Observations that reference the Patient
        String method = "testTransactionLocalRefs1";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);

        // Add a POST entry to create the Organization.
        String orgLocalRef = "urn:Org_1";
        Organization org = TestUtil.readLocalResource("AcmeOrg.json");
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Organization", null, org, orgLocalRef);

        // Add a POST entry to create the Practitioner.
        String practitionerLocalRef = "urn:Doctor_1";
        Practitioner doctor = TestUtil.readLocalResource("DrStrangelove.json");

        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Practitioner", null, doctor, practitionerLocalRef);

        // Next, add a POST entry to create the Patient.
        String patientLocalRef = "urn:uuid:" + UUID.randomUUID().toString();
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        patient = patient.toBuilder().managingOrganization(Reference.builder().reference(string(orgLocalRef)).build())
                .generalPractitioner(Reference.builder().reference(string(practitionerLocalRef)).build()).build();

        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);

        // Next, add 2 POST entries to create the Observation resources
        for (int i = 1; i <= 2; i++) {
            // Load the Observation resource.
            Observation obs = TestUtil.readLocalResource("Observation1.json");

            // Set its "subject" field to reference the Patient.
            // Add an extension element that references the Organization
            obs = obs.toBuilder().subject(Reference.builder().reference(string(patientLocalRef)).build())
                    .extension(Extension.builder().url(ORG_EXTENSION_URL)
                            .value(Reference.builder().reference(string(orgLocalRef)).build()).build())
                    .build();

            bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, obs);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);

        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 5);
        for (int i = 0; i < 5; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }

        // Next, check each observation to make sure their local references were
        // processed correctly.
        for (int i = 3; i < 5; i++) {
            Bundle.Entry orgEntry = responseBundle.getEntry().get(0);
            Bundle.Entry patientEntry = responseBundle.getEntry().get(2);
            Bundle.Entry obsEntry = responseBundle.getEntry().get(i);

            // Retrieve the Organization's id from its response entry.
            String orgId = orgEntry.getResponse().getId();
            String expectedOrgReference = "Organization/" + orgId;

            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedPatientReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = obsEntry.getResource();
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;

            // Verify the Organization.subject field.
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(actualReference, expectedPatientReference);

            // Verify the "org" reference in the extension attribute.
            assertNotNull(obs.getExtension().get(0));
            assertNotNull(obs.getExtension().get(0).getValue());
            Reference orgRef = (Reference) obs.getExtension().get(0).getValue();
            String actualOrgReference = orgRef.getReference().getValue();
            assertEquals(actualOrgReference, expectedOrgReference);
        }
    }

    @Test(groups = { "transaction" })
    public void testTransactionLocalRefs2() throws Exception {
        // 1 Patient referencing an Organization and a Practitioner
        // and 2 Observations that reference the Patient
        String method = "testTransactionLocalRefs2";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);

        // Add a POST entry to create the Organization.
        String urlBase = "http://test.org/fhir-server/api/v4/";
        String orgRelativeRef = "Organization/Org1";
        String orgLocalRef = urlBase + orgRelativeRef;
        Organization org = TestUtil.readLocalResource("AcmeOrg.json");
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Organization", null, org, orgLocalRef);

        // Add a POST entry to create the Practitioner.
        String practitionerRelativeRef = "Practitioner/Doctor1";
        String practitionerLocalRef = urlBase + practitionerRelativeRef;
        Practitioner doctor = TestUtil.readLocalResource("DrStrangelove.json");

        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Practitioner", null, doctor, practitionerLocalRef);

        // Next, add a POST entry to create the Patient.
        String patientRelativeRef = "Patient/" + UUID.randomUUID().toString();
        String patientLocalRef = urlBase + patientRelativeRef;
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        patient = patient.toBuilder().managingOrganization(Reference.builder().reference(string(orgRelativeRef)).build())
                .generalPractitioner(Reference.builder().reference(string(practitionerRelativeRef)).build()).build();

        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null, patient, patientLocalRef);

        // Next, add 2 POST entries to create the Observation resources
        for (int i = 1; i <= 2; i++) {
            // Load the Observation resource.
            Observation obs = TestUtil.readLocalResource("Observation1.json");

            // Set its "subject" field to reference the Patient.
            // Add an extension element that references the Organization
            String obsLocalRef = urlBase + "Observation/" + Integer.toString(i);
            if (i == 1) {
                obs = obs.toBuilder().subject(Reference.builder().reference(string(patientLocalRef)).build())
                        .extension(Extension.builder().url(ORG_EXTENSION_URL)
                            .value(Reference.builder().reference(string(orgRelativeRef)).build()).build())
                        .build();

                bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, obs, obsLocalRef);
            } else {
                obs = obs.toBuilder().subject(Reference.builder().reference(string(patientRelativeRef)).build())
                        .extension(Extension.builder().url(ORG_EXTENSION_URL)
                            .value(Reference.builder().reference(string(orgLocalRef)).build()).build())
                        .build();

                bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Observation", null, obs, obsLocalRef);
            }
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);

        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 5);
        for (int i = 0; i < 5; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }

        Bundle.Entry orgEntry = responseBundle.getEntry().get(0);
        Bundle.Entry practitionerEntry = responseBundle.getEntry().get(1);
        Bundle.Entry patientEntry = responseBundle.getEntry().get(2);

        // Retrieve the Organization's id from its response entry.
        String orgId = orgEntry.getResponse().getId();
        String expectedOrgReference = "Organization/" + orgId;

        // Retrieve the Practitioner's id from its response entry.
        String practitionerId = practitionerEntry.getResponse().getId();
        String expectedPractitionerReference = "Practitioner/" + practitionerId;

        // Retrieve the Patient's id from its response entry.
        String patientId = patientEntry.getResponse().getId();
        String expectedPatientReference = "Patient/" + patientId;

        // Check Patient to make sure its local references were processed correctly.
        Resource patientResource = patientEntry.getResource();
        assertTrue(patientResource instanceof Patient);
        patient = (Patient) patientResource;

        // Verify the Patient.managingOrganization field.
        assertNotNull(patient.getManagingOrganization());
        assertNotNull(patient.getManagingOrganization().getReference());
        String actualReference = patient.getManagingOrganization().getReference().getValue();
        assertNotNull(actualReference);
        assertEquals(actualReference, expectedOrgReference);

        // Verify the Patient.generalPractitioner field.
        assertNotNull(patient.getGeneralPractitioner());
        assertNotNull(patient.getGeneralPractitioner().get(0).getReference());
        actualReference = patient.getGeneralPractitioner().get(0).getReference().getValue();
        assertNotNull(actualReference);
        assertEquals(actualReference, expectedPractitionerReference);

        // Next, check each observation to make sure their local references were
        // processed correctly.
        for (int i = 3; i < 5; i++) {
            // Retrieve the resource from the request entry.
            Bundle.Entry obsEntry = responseBundle.getEntry().get(i);
            Resource resource = obsEntry.getResource();
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;

            // Verify the Observation.subject field.
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(actualReference, expectedPatientReference);

            // Verify the "org" reference in the extension attribute.
            assertNotNull(obs.getExtension().get(0));
            assertNotNull(obs.getExtension().get(0).getValue());
            Reference orgRef = (Reference) obs.getExtension().get(0).getValue();
            String actualOrgReference = orgRef.getReference().getValue();
            assertEquals(actualOrgReference, expectedOrgReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchCreatesForDelete() throws Exception {
        String method = "testBatchCreatesForDelete";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientBD1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientBD2 = (Patient) responseBundle.getEntry().get(1).getResource();
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreatesForDelete" })
    public void testBatchDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testBatchDeletes";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        String url1 = "Patient/" + patientBD1.getId();
        String url2 = "Patient/" + patientBD2.getId();
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url1, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url2, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url2, null, patientBD2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 3);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
        assertBadResponse(responseBundle.getEntry().get(2), Status.BAD_REQUEST.getStatusCode(),
                "Bundle.Entry.resource not allowed for BundleEntry with DELETE method.");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchDeletes" })
    public void testBatchReadDeletedResources() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testBatchReadDeletedResources";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);
        String url1 = "Patient/" + patientBD1.getId();
        String url2 = "Patient/" + patientBD1.getId() + "/_history/1";
        String url3 = "Patient/" + patientBD1.getId() + "/_history/2";
        String url4 = "Patient/" + patientBD1.getId() + "/_history";
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url1, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url2, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url3, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url4, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 4);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.GONE.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(2), Status.GONE.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(3), Status.OK.getStatusCode());
    }

    @Test(groups = { "transaction" })
    public void testTransactionCreatesForDelete() throws Exception {
        String method = "testTransactionCreatesForDelete";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request()
                                .header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION)
                                .post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientTD1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientTD2 = (Patient) responseBundle.getEntry().get(1).getResource();
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionCreatesForDelete" })
    public void testTransactionDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testTransactionDeletes";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        String url1 = "Patient/" + patientTD1.getId();
        String url2 = "Patient/" + patientTD2.getId();
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url1, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url2, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionDeletes" })
    public void testTransactionReadDeletedResources() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testTransactionReadDeletedResources";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        String url1 = "Patient/" + patientTD1.getId();
        String url2 = "Patient/" + patientTD1.getId() + "/_history/1";
        String url3 = "Patient/" + patientTD1.getId() + "/_history/2";
        String url4 = "Patient/" + patientTD1.getId() + "/_history";
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url1, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url2, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url3, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, url4, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.readEntity(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.DELETED);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates", "testTransactionCreates" })
    public void testBatchConditionalCreates() throws Exception {
        String method = "testBatchConditionalCreates";

        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleType.BATCH);
        // Set first request to yield no matches.
        bundle = addRequestToBundle("_id=NOMATCHES", bundle, HTTPVerb.POST, "Patient", null, patient);
        // Set second request to yield 1 match.
        bundle = addRequestToBundle("_id=" + patientB1.getId(), bundle, HTTPVerb.POST, "Patient", null,
                patient);
        // Set third request to yield multiple matches.
        bundle = addRequestToBundle("name=Doe", bundle, HTTPVerb.POST, "Patient", null, patient);
        // Set fourth request to yield invalid search.
        bundle = addRequestToBundle("BAD:PARAM=1", bundle, HTTPVerb.POST, "Patient", null, patient);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        checkForIssuesWithValidation(responseBundle, true, false, DEBUG);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 4);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(2), Status.PRECONDITION_FAILED.getStatusCode(),
                "returned multiple matches");
        assertBadResponse(responseBundle.getEntry().get(3), Status.BAD_REQUEST.getStatusCode(),
                "Search parameter 'BAD' for resource type 'Patient' was not found.");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates", "testTransactionCreates" })
    public void testTransactionConditionalCreates() throws Exception {
        String method = "testTransactionConditionalCreates";

        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        // Set first request to yield no matches.
        bundle = addRequestToBundle("_id=NOMATCHES", bundle, HTTPVerb.POST, "Patient", null, patient);
        // Set second request to yield 1 match.
        bundle = addRequestToBundle("_id=" + patientB1.getId(), bundle, HTTPVerb.POST, "Patient", null, patient);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        checkForIssuesWithValidation(responseBundle, true, false, DEBUG);

        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testTransactionConditionalCreates" })
    public void testTransactionConditionalCreatesError1() throws Exception {
        String method = "testTransactionConditionalCreatesError1";

        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        // Set request to yield multiple matches.
        bundle = addRequestToBundle("name=Doe", bundle, HTTPVerb.POST, "Patient", null, patient);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.getResource(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.MULTIPLE_MATCHES);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testTransactionConditionalCreates" })
    public void testTransactionConditionalCreatesError2() throws Exception {
        String method = "testTransactionConditionalCreatesError2";

        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        // Set request to yield invalid search.
        bundle = addRequestToBundle("BAD:PARAM=1", bundle, HTTPVerb.POST, "Patient", null, patient);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.getResource(OperationOutcome.class);

        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getCode().getValueAsEnum(), IssueType.Value.INVALID);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchConditionalUpdates() throws Exception {
        String method = "testBatchConditionalUpdates";

        String patientId = UUID.randomUUID().toString();
        Patient patient = TestUtil.readLocalResource("Patient_DavidOrtiz.json");
        patient = patient.toBuilder().id(patientId).build();

        String urlString = "Patient?_id=" + patientId;
        String multipleMatches = "Patient?name=Doe";
        String badSearch = "Patient?NOTASEARCH:PARAM=foo";

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, urlString, null, patient);

        // Removed for 1869. We no longer support bundles with multiple updates for the same resource.
        // bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, urlString, null, patient.toBuilder().id(null).build());

        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, multipleMatches, null, patient);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.PUT, badSearch, null, patient);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 3);
        int item = 0;
        assertGoodPostPutResponse(responseBundle.getEntry().get(item++), Status.CREATED.getStatusCode());
        // assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode()); REMOVED in 1869
        assertBadResponse(responseBundle.getEntry().get(item++), Status.PRECONDITION_FAILED.getStatusCode(),
                "returned multiple matches");
        assertBadResponse(responseBundle.getEntry().get(item++), Status.BAD_REQUEST.getStatusCode(),
                "Search parameter 'NOTASEARCH' for resource type 'Patient' was not found.");

        // Next, verify that we have one version of the Patient resource.
        response = client.history("Patient", patientId, null);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle historyBundle = response.getResource(Bundle.class);
        assertNotNull(historyBundle);
        assertEquals(historyBundle.getTotal().getValue().intValue(), 1);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates" })
    public void testBatchCreatesForConditionalDelete() throws Exception {
        String method = "testBatchCreatesForConditionalDelete";

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        FHIRRequestHeader preferHeader = new FHIRRequestHeader(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION);
        FHIRResponse response = client.batch(bundle, preferHeader);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 1);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());

        // Save off the patient for the delete test.
        patientBCD1 = (Patient) responseBundle.getEntry().get(0).getResource();
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreatesForConditionalDelete" })
    public void testBatchConditionalDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }

        String method = "testBatchConditionalDeletes";

        String patientId = patientBCD1.getId();
        String noMatches = "Patient?_id=NOMATCHES";
        String oneMatch = "Patient?_id=" + patientId;
        String multipleMatches = "Patient?name=Doe";
        String badSearch = "Patient?NOTASEARCH:PARAM=foo";

        Bundle bundle = buildBundle(BundleType.BATCH);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, noMatches, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, oneMatch, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, multipleMatches, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, badSearch, null, null);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 4);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);

        // A search that results in multiple matches:
        // (1) if matches > FHIRConstants.FHIR_CONDITIONAL_DELETE_MAX_NUMBER_DEFAULT, then result in a 412 status code.
        // (2) if matches <= FHIRConstants.FHIR_CONDITIONAL_DELETE_MAX_NUMBER_DEFAULT, then result in a 204 status code.
        WebTarget target = getWebTarget();
        Response response2 =
                target.path("Patient").queryParam("name", "Doe").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response2, Response.Status.OK.getStatusCode());
        Bundle searchResultBundle = response2.readEntity(Bundle.class);
        if (searchResultBundle.getTotal().getValue() <= 10 ) {
            assertGoodGetResponse(responseBundle.getEntry().get(2), Status.NO_CONTENT.getStatusCode(), HTTPReturnPreference.MINIMAL);
        } else {
            assertBadResponse(responseBundle.getEntry().get(2), Status.PRECONDITION_FAILED.getStatusCode(),
                    "The search criteria specified for a conditional delete operation returned too many matches");
        }

        assertBadResponse(responseBundle.getEntry().get(3), Status.BAD_REQUEST.getStatusCode(),
                "Search parameter 'NOTASEARCH' for resource type 'Patient' was not found.");

        // Next, verify that we can't read the Patient resource.
        response = client.read("Patient", patientId);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.GONE.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchCustomOperations() throws Exception {
        String method = "testBatchSearchAll";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleType.BATCH);

        // 1. POST request with resource at resource level
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Parameters validateOperationParameters = Parameters.builder()
                .parameter(Parameter.builder().name(string("resource")).resource(patient).build()).build();

        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient/$validate", null,
                validateOperationParameters);

        // 2. POST request with resource at resource instance level
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST,
                "Patient/" + patientB1.getId() + "/$validate", null, validateOperationParameters);

        //////
        // Commented out because $hello operation isn't installed by default
        //////
        // 3. GET request at global level
        //bundle = addRequestToBundle(null, bundle, HTTPVerb.GET, "$hello?input=" + message, null, null);

        // 4. POST request at global level
        //Parameters hellowWorldParameters = Parameters.builder()
        //        .parameter(Parameter.builder().name(string("input")).value(string(message)).build()).build();
        //bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "$hello", null, hellowWorldParameters);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);

        assertResponseBundle(responseBundle, BundleType.BATCH_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        assertNotNull(responseBundle.getEntry().get(0).getResource());
        assertNotNull(responseBundle.getEntry().get(1).getResource());
    }

    @Test(groups = { "transaction" })
    public void testTransactionCreatesForConditionalDelete() throws Exception {
        String method = "testTransactionCreatesForConditionalDelete";

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_DavidOrtiz.json"));
        bundle = addRequestToBundle(null, bundle, HTTPVerb.POST, "Patient", null,
                TestUtil.readLocalResource("Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        FHIRRequestHeader preferHeader = new FHIRRequestHeader(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION);
        FHIRResponse response = client.transaction(bundle, preferHeader);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = getEntityWithExtraWork(response,method);
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientTCD1 = (Patient) responseBundle.getEntry().get(0).getResource();
        patientTCD2 = (Patient) responseBundle.getEntry().get(1).getResource();
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionCreatesForConditionalDelete" })
    public void testTransactionConditionalDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testTransactionDeletes";

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        String url1 = "Patient?_id=" + patientTCD1.getId();
        String url2 = "Patient?_id=" + patientTCD2.getId();
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url1, null, null);
        bundle = addRequestToBundle(null, bundle, HTTPVerb.DELETE, url2, null, null);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode(), HTTPReturnPreference.MINIMAL);
    }

    /**
     * To test UPDATE_IF_MODIFIED_HEADER_NAME we must use multiple requests because:
     *   "A resource can only appear in a transaction once (by identity)."
     * Transaction requests:
     *   1. Bundle with PUT Patient/randomId (update-as-create)
     *   2. Bundle with PUT Patient/randomId (skip update)
     *   3. Bundle with PATCH Patient/randomId (skip update)
     * and posts a transaction bundle with an update and a patch; both should be skipped on the server
     * Procedure has local reference to Patient.
     */
    @Test
    public void testTransactionBundleWithSkippableUpdates() throws Exception {
        String randomId = UUID.randomUUID().toString();
        Patient patient = Patient.builder()
                .id(randomId)
                .active(org.linuxforhealth.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .url(Uri.of("Patient/"+randomId))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process bundle
        FHIRRequestHeader returnPref = FHIRRequestHeader.header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION);
        FHIRResponse response = client.transaction(requestBundle, returnPref);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(responseBundle.getEntry().size(), 1);

        Bundle.Entry entry1 = responseBundle.getEntry().get(0);
        assertNotNull(entry1.getResource());
        assertEquals(entry1.getResponse().getStatus().getValue(), "201");
        assertEquals(entry1.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/1");
        Patient responsePatient = entry1.getResource().as(Patient.class);


        // Transaction 2. PUT the same patient again. Should be skipped because the resource matches
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        requestBundle = Bundle.builder()
                .id("bundle2")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry2)
                .build();

        // Process bundle
        FHIRResponse response2 = client.transaction(requestBundle, returnPref);
        assertNotNull(response2);
        assertResponse(response2.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle2 = response2.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle2);

        // Validate results
        assertNotNull(responseBundle2);
        assertEquals(responseBundle2.getEntry().size(), 1);
        Bundle.Entry entry2 = responseBundle2.getEntry().get(0);
        assertEquals(entry2.getResponse().getStatus().getValue(), "200");
        assertEquals(entry2.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/1");
        assertEquals(entry2.getResource(), responsePatient);

        // Transaction 3. PATCH the same patient with a NOP patch - should be skipped
        Bundle.Entry.Request patchRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PATCH)
                .url(Uri.of("Patient/"+randomId))
                .build();
        Parameters nopPatch = Parameters.builder()
                .parameter(Parameter.builder()
                    .name(string("operation"))
                    .part(Parameter.builder()
                        .name(string("type"))
                        .value(Code.of("replace"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("path"))
                        .value(string("Patient.active"))
                        .build())
                    .part(Parameter.builder()
                        .name(string("value"))
                        .value(org.linuxforhealth.fhir.model.type.Boolean.TRUE)
                        .build())
                    .build())
                .build();
        Bundle.Entry bundleEntry3 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:3"))
                .resource(nopPatch)
                .request(patchRequest)
                .build();
        requestBundle = Bundle.builder()
                .id("bundle3")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry3)
                .build();

        // Process the bundle request and check that the patch was skipped
        FHIRResponse response3 = client.transaction(requestBundle, returnPref);
        assertNotNull(response3);
        assertResponse(response3.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle3 = response3.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle3);
        Bundle.Entry entry3 = responseBundle3.getEntry().get(0);
        assertEquals(entry3.getResponse().getStatus().getValue(), "200");
        assertEquals(entry3.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/1");
        assertEquals(entry3.getResource(), responsePatient);
    }

    /**
     * To test If-None-Match (conditional create-on-update) we must use
     * multiple requests because:
     *   "A resource can only appear in a transaction once (by identity)."
     * Requests:
     * <pre>
     *   1. Transaction Bundle with PUT Patient/randomId (create-on-update - 201 Created)
     *   2. Transaction Bundle with PUT Patient/randomId (skip update - 400 error)
     *   3. DELETE Patient/randomId (deleted - 200 OK)
     *   4. Transaction Bundle with PUT Patient/randomId (create-on-update - 201 Created)
     * </pre>
     */
    @Test
    public void testTransactionBundleWithIfNoneMatch() throws Exception {
        String randomId = UUID.randomUUID().toString();
        Patient patient = Patient.builder()
                .id(randomId)
                .active(org.linuxforhealth.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .ifNoneMatch("*")
                .url(Uri.of("Patient/"+randomId))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry)
                .build();

        // Process bundle
        FHIRRequestHeader returnPref = FHIRRequestHeader.header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION);
        FHIRResponse response = client.transaction(requestBundle, returnPref);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(responseBundle.getEntry().size(), 1);

        Bundle.Entry entry1 = responseBundle.getEntry().get(0);
        assertNotNull(entry1.getResource());
        assertEquals(entry1.getResponse().getStatus().getValue(), "201");
        assertEquals(entry1.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/1");


        // Interaction 2. PUT the same patient again. Should be skipped because IfNoneMatch - because the
        // processing error for the entry is 412, this should fail the bundle with a 400
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        requestBundle = Bundle.builder()
                .id("bundle2")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry2)
                .build();

        // Process bundle
        FHIRResponse response2 = client.transaction(requestBundle, returnPref);
        assertNotNull(response2);
        assertResponse(response2.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
        OperationOutcome operationOutcome2 = response2.getResource(OperationOutcome.class);

        // Validate results
        assertNotNull(operationOutcome2);

        // Interaction 3. DELETE the patient
        FHIRResponse response3 = client.delete(Patient.class.getSimpleName(), randomId);
        assertNotNull(response3);
        assertResponse(response3.getResponse(), Response.Status.OK.getStatusCode());

        // Interaction 4. Undelete
        Bundle.Entry bundleEntry4 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        requestBundle = Bundle.builder()
                .id("bundle4")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry4)
                .build();

        // Process bundle
        FHIRResponse response4 = client.transaction(requestBundle, returnPref);
        assertNotNull(response4);
        assertResponse(response4.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle4 = response4.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle4);

        // Validate results
        assertNotNull(responseBundle4);
        assertEquals(responseBundle4.getEntry().size(), 1);
        Bundle.Entry entry4 = responseBundle4.getEntry().get(0);

        // Undelete uses 201 Created to pass Touchstone
        assertEquals(entry4.getResponse().getStatus().getValue(), "201");

        // Version 2 is the delete marker, so should be version 3 after undelete
        assertEquals(entry4.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/3");
    }

    /**
     * To test If-None-Match (conditional create-on-update) we must use
     * multiple requests because:
     *   "A resource can only appear in a transaction once (by identity)."
     * Requests:
     *   1. Batch Bundle with PUT Patient/randomId (create-on-update - 201 Created)
     *   2. Batch Bundle with PUT Patient/randomId (skip update - 412 Precondition Failed)
     *   3. DELETE Patient/randomId (deleted - 200 OK)
     *   4. Batch Bundle with PUT Patient/randomId (create-on-update - 201 Created)
     */
    @Test
    public void testBatchBundleWithIfNoneMatch() throws Exception {
        String randomId = UUID.randomUUID().toString();
        Patient patient = Patient.builder()
                .id(randomId)
                .active(org.linuxforhealth.fhir.model.type.Boolean.TRUE)
                .build();
        Bundle.Entry.Request bundleEntryRequest = Bundle.Entry.Request.builder()
                .method(HTTPVerb.PUT)
                .ifNoneMatch("*")
                .url(Uri.of("Patient/"+randomId))
                .build();
        Bundle.Entry bundleEntry = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:1"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.BATCH)
                .entry(bundleEntry)
                .build();

        // Process bundle
        FHIRRequestHeader returnPref = FHIRRequestHeader.header(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION);
        FHIRResponse response = client.batch(requestBundle, returnPref);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle);

        // Validate results
        assertNotNull(responseBundle);
        assertEquals(responseBundle.getEntry().size(), 1);

        Bundle.Entry entry1 = responseBundle.getEntry().get(0);
        assertNotNull(entry1.getResource());
        assertEquals(entry1.getResponse().getStatus().getValue(), "201");
        assertEquals(entry1.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/1");


        // Interaction 2. PUT the same patient again. Should be skipped because IfNoneMatch
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        requestBundle = Bundle.builder()
                .id("bundle2")
                .type(BundleType.BATCH)
                .entry(bundleEntry2)
                .build();

        // Process bundle
        FHIRResponse response2 = client.batch(requestBundle, returnPref);
        assertNotNull(response2);
        assertResponse(response2.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle2 = response2.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle2);

        // Validate results
        assertNotNull(responseBundle2);
        assertEquals(responseBundle2.getEntry().size(), 1);
        Bundle.Entry entry2 = responseBundle2.getEntry().get(0);
        assertEquals(entry2.getResponse().getStatus().getValue(), "412");

        // Interaction 3. DELETE the patient
        FHIRResponse response3 = client.delete(Patient.class.getSimpleName(), randomId);
        assertNotNull(response3);
        assertResponse(response3.getResponse(), Response.Status.OK.getStatusCode());

        // Interaction 4. Undelete
        Bundle.Entry bundleEntry4 = Bundle.Entry.builder()
                .fullUrl(Uri.of("urn:2"))
                .resource(patient)
                .request(bundleEntryRequest)
                .build();
        requestBundle = Bundle.builder()
                .id("bundle4")
                .type(BundleType.BATCH)
                .entry(bundleEntry4)
                .build();

        // Process bundle
        FHIRResponse response4 = client.batch(requestBundle, returnPref);
        assertNotNull(response4);
        assertResponse(response4.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle4 = response4.getResource(Bundle.class);
        printBundle("PUT", "response", responseBundle4);

        // Validate results
        assertNotNull(responseBundle4);
        assertEquals(responseBundle4.getEntry().size(), 1);
        Bundle.Entry entry4 = responseBundle4.getEntry().get(0);

        // Undelete uses 201 Created to pass Touchstone
        assertEquals(entry4.getResponse().getStatus().getValue(), "201");

        // Version 2 is the delete marker, so should be version 3 after undelete
        assertEquals(entry4.getResponse().getLocation().getValue(), "Patient/"+randomId+"/_history/3");
    }

    /**
     * Helper function to create a set of Observations, and return them in a
     * response bundle.
     */
    private Observation[] createObservations(String filename, int numObservations) throws Exception {
        String method = "createObservations";
        Observation[] result = new Observation[numObservations];

        // Build the request bundle.
        Bundle requestBundle = buildBundle(BundleType.TRANSACTION);

        // Add the POST entries to create the Observation resources.
        for (int i = 0; i < numObservations; i++) {
            Observation obs = TestUtil.readLocalResource(filename);
            requestBundle = addRequestToBundle(null, requestBundle, HTTPVerb.POST, "Observation", null, obs);
        }

        printBundle(method, "request", requestBundle);

        // Invoke the REST API and then return the response bundle.
        FHIRRequestHeader httpReturnPrefHeader = new FHIRRequestHeader(PREFER_HEADER_NAME, PREFER_HEADER_RETURN_REPRESENTATION);
        FHIRResponse response = client.transaction(requestBundle, httpReturnPrefHeader);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);

        printBundle(method, "response", responseBundle);

        // Extract each of the Observations from the response bundle and return via the
        // array.
        for (int i = 0; i < numObservations; i++) {
            Bundle.Entry entry = responseBundle.getEntry().get(i);
            Resource resource = entry.getResource();
            assertTrue(resource instanceof Observation);
            result[i] = (Observation) resource;
        }

        return result;
    }

    private void assertSearchResults(WebTarget target, String uniqueFamily, int expectedResults) {
        Response response = target.path("Patient").queryParam("family", uniqueFamily)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), expectedResults);
    }

    private void assertHistoryResults(WebTarget target, String url, int expectedResults) {
        Response response = target.path(url).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), expectedResults);
    }

    private void assertGoodGetResponse(Bundle.Entry entry, int expectedStatusCode) throws Exception {
        assertGoodGetResponse(entry, expectedStatusCode, HTTPReturnPreference.REPRESENTATION);
    }

    private void assertGoodGetResponse(Bundle.Entry entry, int expectedStatusCode, HTTPReturnPreference returnPref) throws Exception {
        assertNotNull(entry);
        Bundle.Entry.Response response = entry.getResponse();
        assertNotNull(response);

        assertNotNull(response.getStatus());

        // TestNG: ACTUAL, EXPECTED
        assertEquals(response.getStatus().getValue(), Integer.toString(expectedStatusCode));

        if (returnPref != null && !returnPref.equals(HTTPReturnPreference.MINIMAL)) {
            Resource rc = entry.getResource();
            assertNotNull(rc);
        }
    }

    private void assertGoodPostPutResponse(Bundle.Entry entry, int expectedStatusCode) throws Exception {
        assertGoodPostPutResponse(entry, expectedStatusCode, HTTPReturnPreference.MINIMAL);
    }

    private void assertGoodPostPutResponse(Bundle.Entry entry, int expectedStatusCode, HTTPReturnPreference returnPref) throws Exception {
        assertGoodGetResponse(entry, expectedStatusCode, returnPref);
        Bundle.Entry.Response response = entry.getResponse();

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocation().getValue());

        assertNotNull(response.getEtag());
        assertNotNull(response.getEtag().getValue());

        assertNotNull(response.getLastModified());
        assertNotNull(response.getLastModified().getValue());
    }

    private void assertBadResponse(Bundle.Entry entry, int expectedStatusCode, String expectedMsg) {
        assertNotNull(entry);
        Bundle.Entry.Response response = entry.getResponse();
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(response.getStatus().getValue(), Integer.toString(expectedStatusCode));

        OperationOutcome oo = (OperationOutcome) entry.getResource();
        assertNotNull(oo);
        assertNotNull(oo.getIssue());
        assertTrue(oo.getIssue().size() > 0);
        if (expectedMsg != null) {
            String msg = oo.getIssue().get(0).getDetails().getText().getValue() ;
            assertNotNull(msg);
            assertTrue("'" + msg + "' doesn't contain '" + expectedMsg + "'", msg.contains(expectedMsg));
        }
    }

    private void printBundle(String method, String bundleType, Bundle bundle) throws FHIRException {
        if (DEBUG) {
            System.out.println(method + " " + bundleType + " bundle contents:\n"
                    + TestUtil.writeResource(bundle, Format.JSON, prettyPrint));
        }
    }

    private Bundle addRequestToBundle(String ifNoexisting, Bundle bundle, HTTPVerb method, String url, String ifMatch,
            Resource resource) throws Exception {
        return addRequestToBundle2(ifNoexisting, bundle, method, url, ifMatch, resource, null, null);
    }

    private Bundle addRequestToBundle(String ifNoexisting, String fullUrl, Bundle bundle, HTTPVerb method, String url,
            String ifMatch, Resource resource, List<Extension> requestExtensions) throws Exception {
        Bundle.Entry.Builder entryBuilder = Entry.builder();
        Bundle.Entry.Request.Builder requestBuilder = Bundle.Entry.Request.builder().method(method).url(Uri.of(url));

        if (ifMatch != null) {
            requestBuilder.ifMatch(string(ifMatch));
        }
        if (ifNoexisting != null) {
            requestBuilder.ifNoneExist(string(ifNoexisting));
        }
        if (resource != null) {
            entryBuilder.resource(resource);
        }
        if (requestExtensions != null) {
            requestBuilder.extension(requestExtensions);
        }

        if (fullUrl != null) {
            entryBuilder.fullUrl(Uri.of(fullUrl));
        }

        bundle = bundle.toBuilder().entry(entryBuilder.request(requestBuilder.build()).build()).build();

        return bundle;
    }

    private Bundle addRequestToBundle(String ifNoexisting, Bundle bundle, HTTPVerb method, String url, String ifMatch,
            Resource resource, String fullUrl) throws Exception {
        return addRequestToBundle2(ifNoexisting, bundle, method, url, ifMatch, resource, fullUrl, null);
    }

    private Bundle addRequestToBundle2(String ifNoexisting, Bundle bundle, HTTPVerb method, String url, String ifMatch,
            Resource resource, String fullUrl, List<Extension> requestExtensions) throws Exception {
        bundle = addRequestToBundle(ifNoexisting, fullUrl, bundle, method, url, ifMatch, resource, requestExtensions);

        return bundle;
    }

    private Bundle buildBundle(BundleType bundleType) {
        Bundle bundle = Bundle.builder().type(bundleType).build();
        return bundle;
    }

    @SuppressWarnings("unused")
    private void printOOMessage(OperationOutcome oo) {
        System.out.println("Message: " + oo.getIssue().get(0).getDiagnostics().getValue());
    }
    
    /**
     * method for setting up the Kafka-consumer properties.
     */
    public void setUpConsumer() {
        try {
            Properties kafkaAuditSSLProperties = TestUtil.readTestProperties("kafka/client-ssl.properties");
            // Set up our properties for connecting to the kafka server.
            connectionProps = new Properties();
            connectionProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getAuditKafkaConnectionInfo());
            connectionProps.put("group.id", "test-audit-group");
            connectionProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            connectionProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            connectionProps.put("max.poll.records", "100");
            connectionProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
            connectionProps.putAll(kafkaAuditSSLProperties);
            consumer = new KafkaConsumer<String, String>(connectionProps);
            List<TopicPartition> partitionList = getPartitionInfo(consumer, getAuditKafkaTopicName());
            consumer.assign(partitionList);
            consumer.seekToEnd(partitionList);
            consumer.poll(Duration.ofSeconds(1));
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "failed to set up consumer: ", t);
        } 
        
    }
    
    /**
     * Method to fetch the Kafka partition information for a Kafka topic.
     * @param consumer The Kafka consumer. 
     * @param topicName the topic name.
     * @return
     */
    private List<TopicPartition> getPartitionInfo(KafkaConsumer<String,String> consumer, String topicName) {
        // Checking for topic existence before subscribing
        List<TopicPartition> partitionList = new ArrayList<>();
        List<PartitionInfo> partitions = consumer.partitionsFor(topicName);
        if (partitions == null || partitions.isEmpty()) {
            throw new IllegalStateException("topic not found");
        } else {
            // dump the list of partitions configured for this topic
            for (PartitionInfo partition: partitions) {
                partitionList.add(new TopicPartition(topicName, partition.partition()));
            }
        }
        return partitionList;
    }
    
    /**
     * Test if a transaction bundle audit log message contains the resource type, id and version.
     * @throws Exception
     */
    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionCreates", "testTransactionCreatesError" })
    public void testTransactionAuditLog() throws Exception {
        logger.info("validating transaction bundle audit log");
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }
        validateAuditLogMessages();
    }
    
    /**
     * Test if a batch bundle audit log message contains the resource type, id and version.
     * @throws Exception
     */
    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionCreates", "testTransactionCreatesError" })
    public void testBatchAuditLog() throws Exception {
        logger.info("validating batch bundle audit log");
        validateAuditLogMessages();
    }

    /**
     * Method to validate the 'bundle' audit log messages.
     * @throws Exception
     */
    private void validateAuditLogMessages() throws Exception {
        logger.info("validating audit log messages");
        MapperType mapperType = null;
        ConfigurationTranslator translator = new ConfigurationTranslator();
        PropertyGroup pg = ConfigurationService.loadConfiguration("src/test/resources/fhir-server-config.json");
        PropertyGroup auditProps = pg.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
        mapperType = translator.getMapperType(auditProps);
        FHIRContext fhirCtx = null;
        ConsumerRecords<String, String> records = fetchAuditLogs(Duration.ofSeconds(1));
        for (ConsumerRecord<String, String> record : records) {
            JSONObject auditRecord = new JSONObject(record.value());
            if ("CADF".equals(mapperType.name())) {
                org.json.JSONArray attachements = auditRecord.getJSONArray("attachments");
                JSONObject attachment = (JSONObject) attachements.get(0);
                ByteArrayInputStream attachmentStream = new ByteArrayInputStream(attachment.toString().getBytes());
                CadfAttachment item = CadfAttachment.Parser.parse(attachmentStream);
                ByteArrayInputStream fhirCtxStream = new ByteArrayInputStream(item.getContent().toString().getBytes());
                fhirCtx = FHIRContext.FHIRParser.parse(fhirCtxStream);
                
            } else {
                org.json.JSONArray entityArray = auditRecord.getJSONArray("entity");
                org.json.JSONArray detailsArray = ((JSONObject)entityArray.get(0)).getJSONArray("detail");
                String jsonValue = ((JSONObject)detailsArray.get(0)).getString("valueBase64Binary");
                Base64Binary base64Binary = Base64Binary.builder().value(jsonValue).build();
                Assert.assertNotNull(base64Binary);
                byte[] bytes = base64Binary.getValue();
                ByteArrayInputStream fhirCtxStream = new ByteArrayInputStream(bytes);
                fhirCtx = FHIRContext.FHIRParser.parse(fhirCtxStream);
                
            }
            assertNotNull(fhirCtx);
            if (fhirCtx.getEventType().equals("fhir-bundle") && fhirCtx.getData() != null) {
                logger.info("validating audit log messages for resource " + fhirCtx.getData().getResourceType() + " with id "+ fhirCtx.getData().getId());
                assertNotNull(fhirCtx.getData().getId());
                assertNotNull(fhirCtx.getData().getResourceType());
                // skip version Id validation for history and search requests
                if (!"Bundle".equals(fhirCtx.getData().getResourceType())) {
                    assertNotNull(fhirCtx.getData().getVersionId());
                }
                if (fhirCtx.getApiParameters() != null && fhirCtx.getApiParameters().getStatus() != null && fhirCtx.getApiParameters().getStatus().intValue() == Status.OK.getStatusCode()) {
                    assertNull(fhirCtx.getBatch());
                }
            }
        }
    }
    
    /**
     * Method to fetch the audit logs from kafka broker.
     * @param timeout the poll timeout duration.
     * @return ConsumerRecords - the kafka consumer records.
     */
    private ConsumerRecords<String, String> fetchAuditLogs(Duration timeout) {
        boolean continuePoll = true;
        int count = 0; 
        ConsumerRecords<String, String> records = null;
        while (continuePoll) { 
            records = consumer.poll(timeout);
            logger.info("audit logs records size "+records.count());
            
            if (records.count() > 0 || count > 2) {
                continuePoll = false;
            }
            count++;
        }
        
        return records;
    }
    
    
}
