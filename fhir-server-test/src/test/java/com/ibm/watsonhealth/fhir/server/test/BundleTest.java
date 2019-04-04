/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleRequest;
import com.ibm.watsonhealth.fhir.model.BundleResponse;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.Extension;
import com.ibm.watsonhealth.fhir.model.HTTPVerbList;
import com.ibm.watsonhealth.fhir.model.HumanName;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Organization;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Practitioner;
import com.ibm.watsonhealth.fhir.model.Reference;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

/**
 * This class tests 'batch' and 'transaction' interactions.
 */
public class BundleTest extends FHIRServerTestBase {
    // Set this to true to have the request and response bundles displayed on the console.
    private boolean debug = false;
    
    // Set this to true to have the request and response bundles pretty printed on the console when debug = true
    private boolean prettyPrint = true;

    // Variables used by the batch tests.
    private Patient patientB1 = null;
    private Patient patientB2 = null;
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
    private static final String HEADER_EXTENSION_URL = "http://www.ibm.com/watsonhealth/fhir/extensions/http-request-header";

    /**
     * Retrieve the server's conformance statement to determine the status of certain runtime options.
     * 
     * @throws Exception
     */
    @BeforeClass
    public void retrieveConfig() throws Exception {
        updateCreateEnabled = isUpdateCreateSupported();
        System.out.println("Update/Create enabled?: " + updateCreateEnabled.toString());

        transactionSupported = isTransactionSupported();
        System.out.println("Transactions supported?: " + transactionSupported.toString());

        compartmentSearchSupported = isComparmentSearchSupported();
        System.out.println("Compartment-based searches supported?: " + compartmentSearchSupported.toString());

        deleteSupported = isDeleteSupported();
        System.out.println("Delete operation supported?: " + deleteSupported.toString());
    }

    @Test(groups = { "batch" })
    public void testEmptyBundle() throws Exception {
        WebTarget target = getWebTarget();

        // We're specifically using an invalid bundle type because we want to
        // verify that the server reports on the empty bundle condition before checking the bundle type.
        Bundle bundle = buildBundle(BundleTypeList.BATCH_RESPONSE);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), 
            "Bundle parameter is missing or empty");
    }

    @Test(groups = { "batch" })
    public void testMissingBundleType() throws Exception {
        WebTarget target = getWebTarget();

        Bundle bundle = getObjectFactory().createBundle();
        BundleEntry entry = getObjectFactory().createBundleEntry();
        bundle.getEntry().add(entry);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), 
            "Missing required field: 'type'");
    }

    @Test(groups = { "batch" })
    public void testIncorrectBundleType() throws Exception {
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH_RESPONSE);
        BundleEntry entry = getObjectFactory().createBundleEntry();
        bundle.getEntry().add(entry);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), 
            "Bundle.type must be either 'batch' or 'transaction'");
    }

    @Test(groups = { "batch" })
    public void testMissingRequestField() throws Exception {
        String method = "testMissingRequestField";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        bundle.getEntry().add(getObjectFactory().createBundleEntry());

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        printBundle(method, "response", responseBundle);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "BundleEntry is missing the 'request' field");
    }

    @Test(groups = { "batch" })
    public void testMissingMethod() throws Exception {
        String method = "testMissingMethod";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, null, null, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "Missing required field: 'method'");
    }

    @Test(groups = { "batch" })
    public void testIncorrectMethod() throws Exception {
        // If the "delete" operation is supported by the server, then we can't use DELETE 
        // to test out an incorrect method in a bundle request entry :)
        if (deleteSupported) {
            return;
        }
        
        String method = "testIncorrectMethod";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, "placeholder", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        printBundle(method, "response", responseBundle);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "BundleEntry.request contains unsupported HTTP method");
    }

    @Test(groups = { "batch" })
    public void testMissingResource() throws Exception {
        String method = "testMissingResource";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        printBundle(method, "response", responseBundle);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "BundleEntry.resource is required");
    }

    @Test(groups = { "batch" })
    public void testExtraneousResource() throws Exception {
        String method = "testExtraneousResource";
        WebTarget target = getWebTarget();
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/123", null, patient);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        printBundle(method, "response", responseBundle);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "BundleEntry.resource not allowed");
    }

    @Test(groups = { "batch" })
    public void testMissingRequestURL() throws Exception {
        String method = "testMissingRequestURL";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, null, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), "Missing required field: 'url'");
    }

    @Test(groups = { "batch" })
    public void testIncorrectRequestURL() throws Exception {
        String method = "testMissingRequestURL";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/1/blah/2", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "Unrecognized path in request URL");
    }

    @Test(groups = { "batch" })
    public void testInvalidResource() throws Exception {
        String method = "testInvalidResource";
        WebTarget target = getWebTarget();
        Patient patient = readResource(Patient.class, "InvalidPatient.json");

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "cvc-minLength-valid");
    }
    
    @Test(groups = { "batch" })
    public void testInvalidBundle() throws Exception {
        WebTarget target = getWebTarget();
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.readEntity(OperationOutcome.class), 
            "A 'Bundle' resource type is required but a 'Patient' resource type was sent.");
    }

    @Test(groups = { "batch" })
    public void testResourceURLMismatch() throws Exception {
        String method = "testResourceURLMismatch";
        WebTarget target = getWebTarget();
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, patient);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "does not match type specified in request URI");
    }

    @Test(groups = { "batch" })
    public void testInvalidSecondResource() throws Exception {
        String method = "testInvalidSecondResource";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "InvalidPatient.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 3);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(1), Status.BAD_REQUEST.getStatusCode(), "cvc-minLength-valid");
        assertGoodPostPutResponse(responseBundle.getEntry().get(2), Status.CREATED.getStatusCode());
    }

    @Test(groups = { "batch" })
    public void testBatchCreates() throws Exception {
        String method = "testBatchCreates";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientB1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientB2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
    }

    @Test(groups = { "batch" })
    public void testBatchCreatesForVersionAwareUpdates() throws Exception {
        String method = "testBatchCreatesForVersionAwareUpdates";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientBVA1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientBVA2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates" })
    public void testBatchUpdates() throws Exception {
        String method = "testBatchUpdates";
        WebTarget target = getWebTarget();

        // Make a small change to each patient.
        patientB2.setActive(getObjectFactory().createBoolean().withValue(Boolean.TRUE));
        patientB1.setActive(getObjectFactory().createBoolean().withValue(Boolean.FALSE));

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientB2.getId().getValue(), null, patientB2);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientB1.getId().getValue(), null, patientB1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        locationB1 = responseBundle.getEntry().get(1).getResponse().getLocation().getValue();
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreatesForVersionAwareUpdates" })
    public void testBatchUpdatesVersionAware() throws Exception {
        String method = "testBatchUpdatesVersionAware";
        WebTarget target = getWebTarget();

        // Make a small change to each patient.
        patientBVA2.setActive(getObjectFactory().createBoolean().withValue(Boolean.TRUE));
        patientBVA1.setActive(getObjectFactory().createBoolean().withValue(Boolean.FALSE));

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientBVA2.getId().getValue(), "W/\"1\"", patientBVA2);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientBVA1.getId().getValue(), "W/\"1\"", patientBVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdatesVersionAware" })
    public void testBatchUpdatesVersionAwareError1() throws Exception {
        String method = "testBatchUpdatesVersionAwareError1";
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientBVA2);
        Response res1 = target.path("Patient/" + patientBVA2.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientBVA1);
        Response res2 = target.path("Patient/" + patientBVA1.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Make a small change to each patient.
        patientVA2.setActive(getObjectFactory().createBoolean().withValue(Boolean.TRUE));
        patientVA1.setActive(getObjectFactory().createBoolean().withValue(Boolean.FALSE));

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA2.getId().getValue(), "W/\"2\"", patientVA2);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA1.getId().getValue(), "W/\"1\"", patientVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(1), Status.CONFLICT.getStatusCode(), "If-Match version '1' does not match current latest version of resource: 2");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdatesVersionAware", "testBatchUpdatesVersionAwareError1" })
    public void testBatchUpdatesVersionAwareError2() throws Exception {
        String method = "testBatchUpdatesVersionAwareError2";
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientBVA2);
        Response res1 = target.path("Patient/" + patientBVA2.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientBVA1);
        Response res2 = target.path("Patient/" + patientBVA1.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Make a small change to each patient.
        patientVA2.setActive(getObjectFactory().createBoolean().withValue(Boolean.TRUE));
        patientVA1.setActive(getObjectFactory().createBoolean().withValue(Boolean.FALSE));

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA2.getId().getValue(), "W/\"1\"", patientVA2);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA1.getId().getValue(), "W/\"2\"", patientVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertBadResponse(responseBundle.getEntry().get(0), Status.CONFLICT.getStatusCode(), "If-Match version '1' does not match current latest version of resource: 3");
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdatesVersionAware", "testBatchUpdatesVersionAwareError1",
            "testBatchUpdatesVersionAwareError2" })
    public void testBatchUpdatesVersionAwareError3() throws Exception {
        String method = "testBatchUpdatesVersionAwareError3";
        WebTarget target = getWebTarget();

        // First, call the 'read' API to retrieve the previously-created patients.
        assertNotNull(patientBVA2);
        Response res1 = target.path("Patient/" + patientBVA2.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientBVA1);
        Response res2 = target.path("Patient/" + patientBVA1.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Make a small change to each patient.
        patientVA2.setActive(getObjectFactory().createBoolean().withValue(Boolean.TRUE));
        patientVA1.setActive(getObjectFactory().createBoolean().withValue(Boolean.FALSE));

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA2.getId().getValue(), "W/\"2\"", patientVA2);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA1.getId().getValue(), "W/\"2\"", patientVA1);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertBadResponse(responseBundle.getEntry().get(0), Status.CONFLICT.getStatusCode(), "If-Match version '2' does not match current latest version of resource: 3");
        assertBadResponse(responseBundle.getEntry().get(1), Status.CONFLICT.getStatusCode(), "If-Match version '2' does not match current latest version of resource: 3");
    }

    @Test(groups = { "batch" })
    public void testBatchUpdateCreates() throws Exception {
        String method = "testBatchUpdateCreates";
        assertNotNull(updateCreateEnabled);
        if (!updateCreateEnabled.booleanValue()) {
            return;
        }

        Patient p1 = readResource(Patient.class, "Patient_DavidOrtiz.json");
        setNewResourceId(p1);

        Patient p2 = readResource(Patient.class, "Patient_JohnDoe.json");
        setNewResourceId(p2);

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + p1.getId().getValue(), null, p1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + p2.getId().getValue(), null, p2);

        printBundle(method, "request", bundle);

        FHIRResponse response = getFHIRClient().batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);

        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
    }

    /**
     * Generates a new ID for the specified resource and sets it within the resource.
     */
    private void setNewResourceId(Resource resource) {
        resource.withId(getObjectFactory().createId().withValue(UUID.randomUUID().toString()));
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchReads() throws Exception {
        String method = "testBatchReads";
        WebTarget target = getWebTarget();

        assertNotNull(locationB1);

        // Perform a 'read' and a 'vread'.
        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/" + patientB2.getId().getValue(), null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, locationB1, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchHistory() throws Exception {
        String method = "testBatchHistory";
        WebTarget target = getWebTarget();

        // Perform a 'read' and a 'vread'.
        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/" + patientB1.getId().getValue() + "/_history", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() > 1);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchSearch() throws Exception {
        String method = "testBatchSearch";
        WebTarget target = getWebTarget();

        // Perform a 'read' and a 'vread'.
        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() > 1);
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
        // When JPA is configured for persistence, the request completes normally and no results are returned.
        // When Cloudant is configured for persistence, a 400 http status code is returned.
        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/-999/Observation", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());

        // Take a peek at the result bundle.
        Bundle resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() == 0);
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchMixture() throws Exception {
        String method = "testBatchMixture";
        WebTarget target = getWebTarget();

        // Perform a mixture of request types.
        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        // create
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        // update
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientB1.getId().getValue(), null, patientB1);
        // read
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/" + patientB2.getId().getValue(), null, null);
        // vread
        addRequestToBundle(bundle, HTTPVerbList.GET, locationB1, null, null);
        // history
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/" + patientB1.getId().getValue() + "/_history", null, null);
        // search
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 6);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(2), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(3), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(4), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(5), Status.OK.getStatusCode());

        Bundle resultSet;

        // Verify the history results.
        resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(4).getResource());
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() > 2);

        // Verify the search results.
        resultSet = (Bundle) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(5).getResource());
        assertNotNull(resultSet);
        assertTrue(resultSet.getEntry().size() >= 1);
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
        // We'll load in an existing patient mock data file, then just change the family name to be unique.
        Patient patient1 = readResource(Patient.class, "Patient_DavidOrtiz.json");
        String uniqueFamily1 = setUniqueFamilyName(patient1);
        Patient patient2 = readResource(Patient.class, "Patient_JohnDoe.json");
        String uniqueFamily2 = setUniqueFamilyName(patient2);

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient1);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        assertSearchResults(target, uniqueFamily1, 1);
        assertSearchResults(target, uniqueFamily2, 1);

        // Save off the two patients for the update test.
        patientT1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientT2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
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
        // We'll load in an existing patient mock data file, then just change the family name to be unique.
        Patient patient1 = readResource(Patient.class, "Patient_DavidOrtiz.json");
        String uniqueFamily1 = setUniqueFamilyName(patient1);
        Patient patient2 = readResource(Patient.class, "Patient_JohnDoe.json");
        String uniqueFamily2 = setUniqueFamilyName(patient2);

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient1);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        assertSearchResults(target, uniqueFamily1, 1);
        assertSearchResults(target, uniqueFamily2, 1);

        // Save off the two patients for the update test.
        patientTVA1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientTVA2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
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
        String family1 = patientT1.getName().get(0).getFamily().get(0).getValue();
        String family2 = patientT2.getName().get(0).getFamily().get(0).getValue();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientT1.getId().getValue(), null, patientT1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientT2.getId().getValue(), null, patientT2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientT1.getId().getValue() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientT2.getId().getValue() + "/_history", 2);

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
        String family1 = patientTVA1.getName().get(0).getFamily().get(0).getValue();
        String family2 = patientTVA2.getName().get(0).getFamily().get(0).getValue();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientTVA1.getId().getValue(), "W/\"1\"", patientTVA1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientTVA2.getId().getValue(), "W/\"1\"", patientTVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId().getValue() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId().getValue() + "/_history", 2);
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
        Response res1 = target.path("Patient/" + patientTVA2.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res1, Response.Status.OK.getStatusCode());
        Patient patientVA2 = res1.readEntity(Patient.class);
        assertNotNull(patientVA2);

        assertNotNull(patientTVA1);
        Response res2 = target.path("Patient/" + patientTVA1.getId().getValue()).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(res2, Response.Status.OK.getStatusCode());
        Patient patientVA1 = res2.readEntity(Patient.class);
        assertNotNull(patientVA1);

        // Retrieve the family names of the resources to be updated.
        String family1 = patientVA1.getName().get(0).getFamily().get(0).getValue();
        String family2 = patientVA2.getName().get(0).getFamily().get(0).getValue();

        // Make a small change to each patient.
        patientVA2.setActive(getObjectFactory().createBoolean().withValue(Boolean.TRUE));
        patientVA1.setActive(getObjectFactory().createBoolean().withValue(Boolean.FALSE));

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA1.getId().getValue(), "W/\"1\"", patientVA1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientVA2.getId().getValue(), "W/\"2\"", patientVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertBadResponse(responseBundle.getEntry().get(0), Status.CONFLICT.getStatusCode(), "If-Match version '1' does not match current latest version of resource: 2");
        
        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId().getValue() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId().getValue() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdatesVersionAware", "testTransactionUpdatesVersionAwareError1" })
    public void testTransactionUpdatesVersionAwareError2() throws Exception {
        String method = "testTransactionUpdatesVersionAwareError2";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientTVA1.getName().get(0).getFamily().get(0).getValue();
        String family2 = patientTVA2.getName().get(0).getFamily().get(0).getValue();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientTVA1.getId().getValue(), "W/\"2\"", patientTVA1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientTVA2.getId().getValue(), "W/\"1\"", patientTVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertBadResponse(responseBundle.getEntry().get(1), Status.CONFLICT.getStatusCode(), "If-Match version '1' does not match current latest version of resource: 2");
        
        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId().getValue() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId().getValue() + "/_history", 2);
    }

    @Test(groups = { "transaction" }, dependsOnMethods = { "testTransactionUpdatesVersionAware", "testTransactionUpdatesVersionAwareError2" })
    public void testTransactionUpdatesVersionAwareError3() throws Exception {
        String method = "testTransactionUpdatesVersionAwareError3";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        WebTarget target = getWebTarget();

        // Retrieve the family names of the resources to be updated.
        String family1 = patientTVA1.getName().get(0).getFamily().get(0).getValue();
        String family2 = patientTVA2.getName().get(0).getFamily().get(0).getValue();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientTVA1.getId().getValue(), "W/\"1\"", patientTVA1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientTVA2.getId().getValue(), "W/\"1\"", patientTVA2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        boolean foundError = assertOptionalBadResponse(responseBundle.getEntry().get(0), Status.CONFLICT.getStatusCode(), "If-Match version '1' does not match current latest version of resource: 2");
        foundError = foundError || assertOptionalBadResponse(responseBundle.getEntry().get(1), Status.CONFLICT.getStatusCode(), "If-Match version '1' does not match current latest version of resource: 2");
        assertTrue(foundError);
        
        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientTVA1.getId().getValue() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientTVA2.getId().getValue() + "/_history", 2);
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

        Patient p1 = readResource(Patient.class, "Patient_DavidOrtiz.json");
        setNewResourceId(p1);

        Patient p2 = readResource(Patient.class, "Patient_JohnDoe.json");
        setNewResourceId(p2);

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + p1.getId().getValue(), null, p1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + p2.getId().getValue(), null, p2);

        printBundle(method, "request", bundle);

        FHIRResponse response = getFHIRClient().transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);

        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
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
        // We'll load in an existing patient mock data file, then just change the family name to be unique.
        Patient patient1 = readResource(Patient.class, "Patient_DavidOrtiz.json");
        String uniqueFamily1 = setUniqueFamilyName(patient1);
        Patient patient2 = readResource(Patient.class, "Patient_JohnDoe.json");
        String uniqueFamily2 = setUniqueFamilyName(patient2);

        // Perform a bundled request that should fail.
        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient1);
        // mismatch of URL and resource type
        addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, readResource(Patient.class, "Patient_JohnDoe.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 3);

        assertSearchResults(target, uniqueFamily1, 0);
        assertSearchResults(target, uniqueFamily2, 0);
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
        String family1 = patientT1.getName().get(0).getFamily().get(0).getValue();
        String family2 = patientT2.getName().get(0).getFamily().get(0).getValue();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientT1.getId().getValue(), null, patientT1);
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientT2.getId().getValue(), null, patientT2);
        // This will cause a failure - url mismatch with resource type.
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Observation", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 3);
        assertBadResponse(responseBundle.getEntry().get(2), Status.BAD_REQUEST.getStatusCode(), "BundleEntry.resource must contain an id field for a PUT operation");

        assertSearchResults(target, family1, 1);
        assertSearchResults(target, family2, 1);

        assertHistoryResults(target, "Patient/" + patientT1.getId().getValue() + "/_history", 2);
        assertHistoryResults(target, "Patient/" + patientT2.getId().getValue() + "/_history", 2);
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
        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        // create
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        // update
        addRequestToBundle(bundle, HTTPVerbList.PUT, "Patient/" + patientT1.getId().getValue(), null, patientT1);
        // read
        addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/" + patientT2.getId().getValue(), null, null);
        // vread
        addRequestToBundle(bundle, HTTPVerbList.GET, locationT1, null, null);
        // history
//      addRequestToBundle(bundle, HTTPVerbList.GET, "Patient/" + patientT1.getId().getValue() + "/_history", null, null);
        // search
//      addRequestToBundle(bundle, HTTPVerbList.GET, "Patient?family=Ortiz&_count=100", null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 4);
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

        Bundle bundle = buildBundle(BundleTypeList.BATCH);

        // Add 3 POST entries to create the Patient resources.
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);

            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            HumanName newName = getObjectFactory().createHumanName().withFamily(getObjectFactory().createString().withValue("Doe_"
                    + Integer.toString(i))).withGiven(getObjectFactory().createString().withValue("John"));
            patient.getName().clear();
            patient.withName(newName);

            addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);
        }

        // Next, add 3 POST entries to create the Observation resources
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);

            // Create an Observation that will reference the Patient via a local reference.
            Observation obs = readResource(Observation.class, "Observation1.json");
            obs.setSubject(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(patientLocalRef)));
            addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, obs);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);

        printBundle(method, "response", responseBundle);

        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 6);
        for (int i = 0; i < 6; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }
        
        // Verify that each Observation correctly references its corresponding Patient.
        for (int i = 0; i < 3; i++) {
            BundleEntry patientEntry = responseBundle.getEntry().get(i);
            BundleEntry obsEntry = responseBundle.getEntry().get(i+3);
            
            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = FHIRUtil.getResourceContainerResource(obsEntry.getResource());
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(expectedReference, actualReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefs2() throws Exception {
        String method = "testBatchLocalRefs2";
        // 3 Observations referencing the same patient
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);

        String patientLocalRef = "urn:Patient_1";

        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);

        // Next, add 3 POST entries to create the Observation resources
        for (int i = 1; i <= 3; i++) {
            // Create an Observation that will reference the Patient via a local reference.
            Observation obs = readResource(Observation.class, "Observation1.json");
            obs.setSubject(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(patientLocalRef)));
            addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, obs);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        
        printBundle(method, "response", responseBundle);
        
        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 4);
        for (int i = 0; i < 4; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }
        
        // Verify that each Observation correctly references the Patient.
        for (int i = 1; i < 4; i++) {
            BundleEntry patientEntry = responseBundle.getEntry().get(0);
            BundleEntry obsEntry = responseBundle.getEntry().get(i);
            
            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = FHIRUtil.getResourceContainerResource(obsEntry.getResource());
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(expectedReference, actualReference);
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
        Bundle bundle = buildBundle(BundleTypeList.BATCH);

        // Next, add 3 PUT entries to update the Observation resources.
        for (int i = 1; i <= 3; i++) {
            Observation obs = newObservations[i-1];
            
            // Update the previously-created Observation to add a subject and extension.
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);
            obs.setSubject(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(patientLocalRef)));
            Extension ext = getObjectFactory().createExtension();
            ext.setUrl(PATIENT_EXTENSION_URL);
            ext.setValueReference(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(patientLocalRef)));
            obs.getExtension().add(ext);

            addRequestToBundle(bundle, HTTPVerbList.PUT, "Observation/" + obs.getId().getValue(), null, obs);
        }

        // Add 3 POST entries to create the Patient resources.
        for (int i = 1; i <= 3; i++) {
            String patientLocalRef = "urn:Patient_" + Integer.toString(i);

            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            HumanName newName = getObjectFactory().createHumanName().withFamily(getObjectFactory().createString().withValue("Doe_"
                    + Integer.toString(i))).withGiven(getObjectFactory().createString().withValue("John"));
            patient.getName().clear();
            patient.withName(newName);

            addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);

        printBundle(method, "response", responseBundle);

        // Verify that each of the Observations was updated,
        // and that each of the Patients was created.
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 6);
        for (int i = 0; i < 3; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.OK.getStatusCode());
        }
        for (int i = 3; i < 6; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }
        
        // Verify that each Observation correctly references its corresponding Patient.
        for (int i = 0; i < 3; i++) {
            BundleEntry obsEntry = responseBundle.getEntry().get(i);
            BundleEntry patientEntry = responseBundle.getEntry().get(i+3);
            
            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedReference = "Patient/" + patientId;

            // Retrieve the resource from the response entry.
            Resource resource = FHIRUtil.getResourceContainerResource(obsEntry.getResource());
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            
            // Verify the subject field.
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(expectedReference, actualReference);
            
            // Verify the "related-patient" reference in the extension attribute.
            assertNotNull(obs.getExtension().get(0));
            assertNotNull(obs.getExtension().get(0).getValueReference());
            Reference extRef = obs.getExtension().get(0).getValueReference();
            String actualExtReference = extRef.getReference().getValue();
            assertEquals(expectedReference, actualExtReference);
        }
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefsError1() throws Exception {
        // duplicate fullUrl values
        String method = "testBatchLocalRefsError1";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);

        // Add 2 POST entries to create the Patient resources but use duplicate fullUrl values.
        String patientLocalRef = "urn:Patient_X";
        for (int i = 1; i <= 2; i++) {
            Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
            HumanName newName = getObjectFactory().createHumanName().withFamily(getObjectFactory().createString().withValue("Doe_"
                    + Integer.toString(i))).withGiven(getObjectFactory().createString().withValue("John"));
            patient.getName().clear();
            patient.withName(newName);

            addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        
        printBundle(method, "response", responseBundle);
        
        // Verify that the first patient was created, and the second was not.
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(1), Status.BAD_REQUEST.getStatusCode(), "Duplicate local identifier encountered in bundled request entry:");
    }

    @Test(groups = { "batch" })
    public void testBatchLocalRefsError2() throws Exception {
        // use reference before it's defined
        String method = "testBatchLocalRefsError2";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);

        String patientLocalRef = "urn:Patient_1";

        // Add a POST entry to create an Observation that will reference the Patient via a local reference.
        Observation obs = readResource(Observation.class, "Observation1.json");
        obs.setSubject(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(patientLocalRef)));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, obs);

        // Add a POST entry to create the Patient.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);

        printBundle(method, "response", responseBundle);
        
        // Verify that the observation request failed, and the patient request succeeded
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), "is undefined in the request bundle");
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
    }

    @Test(groups = { "transaction" })
    public void testTransactionLocalRefs1() throws Exception {
        // 1 Patient referencing an Organization and a Practitioner
        // and 2 Observations that reference the Patient
        String method = "testTransactionLocalRefs1";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);

        // Add a POST entry to create the Organization.
        String orgLocalRef = "urn:Org_1";
        Organization org = readResource(Organization.class, "AcmeOrg.json");
        addRequestToBundle(bundle, HTTPVerbList.POST, "Organization", null, org, orgLocalRef);

        // Add a POST entry to create the Practitioner and make it reference the Organization.
        String practitionerLocalRef = "urn:Doctor_1";
        Practitioner doctor = readResource(Practitioner.class, "DrStrangelove.json");
        doctor.getPractitionerRole().get(0).getManagingOrganization().setReference(getObjectFactory().createString().withValue(orgLocalRef));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Practitioner", null, doctor, practitionerLocalRef);

        // Next, add a POST entry to create the Patient.
        String patientLocalRef = "urn:uuid:" + UUID.randomUUID().toString();
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        patient.withCareProvider(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(practitionerLocalRef)));
        patient.withManagingOrganization(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(orgLocalRef)));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);

        // Next, add 2 POST entries to create the Observation resources
        for (int i = 1; i <= 2; i++) {
            // Load the Observation resource.
            Observation obs = readResource(Observation.class, "Observation1.json");

            // Set its "subject" field to reference the Patient.
            obs.setSubject(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(patientLocalRef)));

            // Add an extension element that references the Organization
            Extension ext = getObjectFactory().createExtension();
            ext.setUrl(ORG_EXTENSION_URL);
            ext.setValueReference(getObjectFactory().createReference().withReference(getObjectFactory().createString().withValue(orgLocalRef)));
            obs.getExtension().add(ext);

            addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, obs);
        }

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        
        printBundle(method, "response", responseBundle);
        
        // Verify that each resource was created as expected.
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 5);
        for (int i = 0; i < 5; i++) {
            assertGoodPostPutResponse(responseBundle.getEntry().get(i), Status.CREATED.getStatusCode());
        }
        
        // Next, check each observation to make sure their local references were processed correctly.
        for (int i = 3; i < 5; i++) {
            BundleEntry orgEntry = responseBundle.getEntry().get(0);
            BundleEntry patientEntry = responseBundle.getEntry().get(2);
            BundleEntry obsEntry = responseBundle.getEntry().get(i);
          
            // Retrieve the Organization's id from its response entry.
            String orgId = orgEntry.getResponse().getId();
            String expectedOrgReference = "Organization/" + orgId;
            
            // Retrieve the Patient's id from its response entry.
            String patientId = patientEntry.getResponse().getId();
            String expectedPatientReference = "Patient/" + patientId;

            // Retrieve the resource from the request entry.
            Resource resource = FHIRUtil.getResourceContainerResource(obsEntry.getResource());
            assertTrue(resource instanceof Observation);
            Observation obs = (Observation) resource;
            
            // Verify the Organization.subject field.
            assertNotNull(obs.getSubject());
            assertNotNull(obs.getSubject().getReference());
            String actualReference = obs.getSubject().getReference().getValue();
            assertNotNull(actualReference);
            assertEquals(expectedPatientReference, actualReference);
            
            // Verify the "org" reference in the extension attribute.
            assertNotNull(obs.getExtension().get(0));
            assertNotNull(obs.getExtension().get(0).getValueReference());
            Reference orgRef = obs.getExtension().get(0).getValueReference();
            String actualOrgReference = orgRef.getReference().getValue();
            assertEquals(expectedOrgReference, actualOrgReference);
        }
    }

    @Test(groups = { "transaction" })
    public void testTransactionLocalRefsError1() throws Exception {
        // reference undefined resource
        String method = "testTransactionLocalRefsError1";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);

        // Add a POST entry to create a Patient
        String patientLocalRef = "urn:uuid:" + UUID.randomUUID().toString();
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient, patientLocalRef);

        // Add a POST entry to create an Observation resource
        // Create an Observation that will reference the Patient via a local reference.
        Observation obs = readResource(Observation.class, "Observation1.json");
        obs.setSubject(
            getObjectFactory().createReference()
                .withReference(getObjectFactory().createString().withValue("urn:BAD_REFERENCE")));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Observation", null, obs);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        
        printBundle(method, "response", responseBundle);
        
        // Verify that response bundle.
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);

        // Verify that the observation request failed.
        assertBadResponse(responseBundle.getEntry().get(1), Status.BAD_REQUEST.getStatusCode(), "is undefined in the request bundle");

        // Verify that we cannot do a 'read' on the Patient.
        BundleEntry patientEntry = responseBundle.getEntry().get(0);
        assertNotNull(patientEntry);
        String patientId = patientEntry.getResponse().getId();
        FHIRResponse apiResponse = client.read("Patient", patientId);
        assertNotNull(apiResponse);
        assertEquals(Status.NOT_FOUND.getStatusCode(), apiResponse.getStatus());
    }

    @Test(groups = { "batch" })
    public void testBatchCreatesForDelete() throws Exception {
        String method = "testBatchCreatesForDelete";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientBD1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientBD2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
    }

    @Test(groups = { "batch" }, dependsOnMethods = {"testBatchCreatesForDelete"})
    public void testBatchDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testBatchDeletes";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        String url1 = "Patient/" + patientBD1.getId().getValue();
        String url2 = "Patient/" + patientBD2.getId().getValue();
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url1, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url2, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url2, null, patientBD2);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 3);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.NO_CONTENT.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.NO_CONTENT.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(2), Status.BAD_REQUEST.getStatusCode(), "BundleEntry.resource not allowed for BundleEntry with DELETE method.");
    }

    @Test(groups = { "batch" }, dependsOnMethods = {"testBatchDeletes"})
    public void testBatchReadDeletedResources() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testBatchReadDeletedResources";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        String url1 = "Patient/" + patientBD1.getId().getValue();
        String url2 = "Patient/" + patientBD1.getId().getValue() + "/_history/1";
        String url3 = "Patient/" + patientBD1.getId().getValue() + "/_history/2";
        String url4 = "Patient/" + patientBD1.getId().getValue() + "/_history";
        addRequestToBundle(bundle, HTTPVerbList.GET, url1, null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, url2, null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, url3, null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, url4, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 4);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.GONE.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(2), Status.GONE.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(3), Status.OK.getStatusCode());
    }

    @Test(groups = { "transaction" })
    public void testTransactionCreatesForDelete() throws Exception {
        String method = "testTransactionCreatesForDelete";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientTD1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientTD2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
    }

    @Test(groups = { "transaction" }, dependsOnMethods = {"testTransactionCreatesForDelete"})
    public void testTransactionDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testTransactionDeletes";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        String url1 = "Patient/" + patientTD1.getId().getValue();
        String url2 = "Patient/" + patientTD2.getId().getValue();
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url1, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url2, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.NO_CONTENT.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.NO_CONTENT.getStatusCode());
    }

    @Test(groups = { "transaction" }, dependsOnMethods = {"testTransactionDeletes"})
    public void testTransactionReadDeletedResources() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testTransactionReadDeletedResources";
        WebTarget target = getWebTarget();

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        String url1 = "Patient/" + patientTD1.getId().getValue();
        String url2 = "Patient/" + patientTD1.getId().getValue() + "/_history/1";
        String url3 = "Patient/" + patientTD1.getId().getValue() + "/_history/2";
        String url4 = "Patient/" + patientTD1.getId().getValue() + "/_history";
        addRequestToBundle(bundle, HTTPVerbList.GET, url1, null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, url2, null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, url3, null, null);
        addRequestToBundle(bundle, HTTPVerbList.GET, url4, null, null);

        printBundle(method, "request", bundle);

        Entity<Bundle> entity = Entity.entity(bundle, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.request().post(entity, Response.class);
        assertResponse(response, Response.Status.GONE.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 4);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.GONE.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates", "testTransactionCreates" })
    public void testBatchConditionalCreates() throws Exception {
        String method = "testBatchConditionalCreates";
        
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        
        // Set first request to yield no matches.
        bundle.getEntry().get(0).getRequest().setIfNoneExist(objFactory.createString().withValue("_id=NOMATCHES"));
        
        // Set second request to yield 1 match.
        bundle.getEntry().get(1).getRequest().setIfNoneExist(objFactory.createString().withValue("_id=" + patientB1.getId().getValue()));
        
        // Set third request to yield multiple matches.
        bundle.getEntry().get(2).getRequest().setIfNoneExist(objFactory.createString().withValue("name=Doe"));
        
        // Set fourth request to yield invalid search.
        bundle.getEntry().get(3).getRequest().setIfNoneExist(objFactory.createString().withValue("BADPARAM=1"));
        
        printBundle(method, "request", bundle);
        
        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 4);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(2), Status.PRECONDITION_FAILED.getStatusCode(), "returned multiple matches");
        assertBadResponse(responseBundle.getEntry().get(3), Status.BAD_REQUEST.getStatusCode(), 
            "Search parameter 'BADPARAM' for resource type 'Patient' was not found.");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates", "testTransactionCreates" })
    public void testTransactionConditionalCreates() throws Exception {
        String method = "testTransactionConditionalCreates";
        
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        
        // Set first request to yield no matches.
        bundle.getEntry().get(0).getRequest().setIfNoneExist(objFactory.createString().withValue("_id=NOMATCHES"));
        
        // Set second request to yield 1 match.
        bundle.getEntry().get(1).getRequest().setIfNoneExist(objFactory.createString().withValue("_id=" + patientB1.getId().getValue()));
        
        printBundle(method, "request", bundle);
        
        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testTransactionConditionalCreates" })
    public void testTransactionConditionalCreatesError1() throws Exception {
        String method = "testTransactionConditionalCreatesError1";
        
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        
        // Set request to yield multiple matches.
        bundle.getEntry().get(0).getRequest().setIfNoneExist(objFactory.createString().withValue("name=Doe"));
        
        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.PRECONDITION_FAILED.getStatusCode(), "returned multiple matches");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testTransactionConditionalCreates" })
    public void testTransactionConditionalCreatesError2() throws Exception {
        String method = "testTransactionConditionalCreatesError2";
        
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        
        // Set request to yield invalid search.
        bundle.getEntry().get(0).getRequest().setIfNoneExist(objFactory.createString().withValue("BADPARAM=1"));
        
        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 1);
        assertBadResponse(responseBundle.getEntry().get(0), Status.BAD_REQUEST.getStatusCode(), 
                "Search parameter 'BADPARAM' for resource type 'Patient' was not found.");
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchUpdates" })
    public void testBatchConditionalUpdates() throws Exception {
        String method = "testBatchConditionalUpdates";
        
        String patientId = UUID.randomUUID().toString();
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");
        patient.withId(objFactory.createId().withValue(patientId));
        
        String urlString = "Patient?_id=" + patientId;
        String multipleMatches = "Patient?name=Doe";
        String badSearch = "Patient?NOTASEARCHPARAM=foo";

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.PUT, urlString, null, patient);
        addRequestToBundle(bundle, HTTPVerbList.PUT, urlString, null, patient);
        addRequestToBundle(bundle, HTTPVerbList.PUT, multipleMatches, null, patient);
        addRequestToBundle(bundle, HTTPVerbList.PUT, badSearch, null, patient);
        
        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 4);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.OK.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(2), Status.PRECONDITION_FAILED.getStatusCode(), "returned multiple matches");
        assertBadResponse(responseBundle.getEntry().get(3), Status.BAD_REQUEST.getStatusCode(), 
            "Search parameter 'NOTASEARCHPARAM' for resource type 'Patient' was not found.");

        // Next, verify that we have two versions of the Patient resource.
        response = client.history("Patient", patientId, null);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle historyBundle = response.getResource(Bundle.class);
        assertNotNull(historyBundle);
        assertEquals(2, historyBundle.getTotal().getValue().intValue());
    }

    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreates" })
    public void testBatchCreatesForConditionalDelete() throws Exception {
        String method = "testBatchCreatesForConditionalDelete";

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 1);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());

        // Save off the patient for the delete test.
        patientBCD1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
    }
    @Test(groups = { "batch" }, dependsOnMethods = { "testBatchCreatesForConditionalDelete" })
    public void testBatchConditionalDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        
        String method = "testBatchConditionalDeletes";
        
        String patientId = patientBCD1.getId().getValue();
        String noMatches = "Patient?_id=NOMATCHES";
        String oneMatch = "Patient?_id=" + patientId;
        String multipleMatches = "Patient?name=Doe";
        String badSearch = "Patient?NOTASEARCHPARAM=foo";

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, noMatches, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, oneMatch, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, multipleMatches, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, badSearch, null, null);
        
        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 4);
        assertBadResponse(responseBundle.getEntry().get(0), Status.NOT_FOUND.getStatusCode(), "Search criteria for a conditional delete operation yielded no matches");
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.NO_CONTENT.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(2), Status.PRECONDITION_FAILED.getStatusCode(), "returned multiple matches");
        assertBadResponse(responseBundle.getEntry().get(3), Status.BAD_REQUEST.getStatusCode(), 
            "Search parameter 'NOTASEARCHPARAM' for resource type 'Patient' was not found");

        // Next, verify that we can't read the Patient resource.
        response = client.read("Patient", patientId);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.GONE.getStatusCode());
    }

    @Test(groups = { "transaction" })
    public void testTransactionCreatesForConditionalDelete() throws Exception {
        String method = "testTransactionCreatesForConditionalDelete";

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_DavidOrtiz.json"));
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, readResource(Patient.class, "Patient_JohnDoe.json"));

        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());

        // Save off the two patients for the update test.
        patientTCD1 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(0).getResource());
        patientTCD2 = (Patient) FHIRUtil.getResourceContainerResource(responseBundle.getEntry().get(1).getResource());
    }

    @Test(groups = { "transaction" }, dependsOnMethods = {"testTransactionCreatesForConditionalDelete"})
    public void testTransactionConditionalDeletes() throws Exception {
        if (!deleteSupported) {
            return;
        }
        String method = "testTransactionDeletes";

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        String url1 = "Patient?_id=" + patientTCD1.getId().getValue();
        String url2 = "Patient?_id=" + patientTCD2.getId().getValue();
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url1, null, null);
        addRequestToBundle(bundle, HTTPVerbList.DELETE, url2, null, null);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.transaction(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 2);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.NO_CONTENT.getStatusCode());
        assertGoodGetResponse(responseBundle.getEntry().get(1), Status.NO_CONTENT.getStatusCode());
    }
    
    @Test(groups = { "batch" })
    public void testRequestProperties() throws Exception {
        String method = "testRequestProperties1";
        
        String patientId = UUID.randomUUID().toString();
        String urlString = "Patient?_id=" + patientId;

        Bundle bundle = buildBundle(BundleTypeList.BATCH);
        
        ObjectFactory of = getObjectFactory();
        List<Extension> extensions;
        Extension extension;
        
        // 1) Add a request with some valid request header extensions.
        extensions = new ArrayList<>();
        extension = of.createExtension().withUrl(HEADER_EXTENSION_URL)
                .withValueString(of.createString().withValue("X-WHC-LSF-resourcename: Participant"));
        extensions.add(extension);
        extension = of.createExtension().withUrl("urn:some-url")
                .withValueString(of.createString().withValue("Some string value..."));
        extensions.add(extension);
        extension = of.createExtension().withUrl(HEADER_EXTENSION_URL)
                .withValueString(of.createString().withValue("X-WHC-LSF-studyid: study001"));
        extensions.add(extension);

        addRequestToBundle(bundle, HTTPVerbList.GET, urlString, null, null, extensions);
        
        // 2) Add a request with an invalid request header extension.
        extensions = new ArrayList<>();
        extension = of.createExtension().withUrl(HEADER_EXTENSION_URL)
                .withValueString(of.createString().withValue("FOO"));
        extensions.add(extension);

        addRequestToBundle(bundle, HTTPVerbList.GET, urlString, null, null, extensions);

        // 3) Add a request with an invalid request header extension.
        extensions = new ArrayList<>();
        extension = of.createExtension().withUrl(HEADER_EXTENSION_URL)
                .withValueString(of.createString().withValue("Header1:"));
        extensions.add(extension);

        addRequestToBundle(bundle, HTTPVerbList.GET, urlString, null, null, extensions);
       
        // 4) Add a request with an invalid request header extension.
        extensions = new ArrayList<>();
        extension = of.createExtension().withUrl(HEADER_EXTENSION_URL)
                .withValueBoolean(of.createBoolean().withValue(Boolean.TRUE));
        extensions.add(extension);

        addRequestToBundle(bundle, HTTPVerbList.GET, urlString, null, null, extensions);

        printBundle(method, "request", bundle);

        FHIRResponse response = client.batch(bundle);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.BATCH_RESPONSE, 4);
        assertGoodGetResponse(responseBundle.getEntry().get(0), Status.OK.getStatusCode());
        assertBadResponse(responseBundle.getEntry().get(1), Status.BAD_REQUEST.getStatusCode(), "The proper syntax for");
        assertBadResponse(responseBundle.getEntry().get(2), Status.BAD_REQUEST.getStatusCode(), "The proper syntax for");
        assertBadResponse(responseBundle.getEntry().get(3), Status.BAD_REQUEST.getStatusCode(), "The valueString field is required");
    }
    
    /**
     * Helper function to create a set of Observations, and return them in a response bundle.
     */
    private Observation[] createObservations(String filename, int numObservations) throws Exception {
        String method = "createObservations";
        Observation[] result = new Observation[numObservations];
        
        // Build the request bundle.
        Bundle requestBundle = buildBundle(BundleTypeList.TRANSACTION);

        // Add the POST entries to create the Observation resources.
        for (int i = 0; i < numObservations; i++) {
            Observation obs = readResource(Observation.class, filename);
            addRequestToBundle(requestBundle, HTTPVerbList.POST, "Observation", null, obs);
        }

        printBundle(method, "request",requestBundle);

        // Invoke the REST API and then return the response bundle.
        FHIRResponse response = client.transaction(requestBundle);
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);

        printBundle(method, "response",responseBundle);
        
        // Extract each of the Observations from the response bundle and return via the array.
        for (int i = 0; i < numObservations; i++) {
            BundleEntry entry = responseBundle.getEntry().get(i);
            Resource resource = FHIRUtil.getResourceContainerResource(entry.getResource());
            assertTrue(resource instanceof Observation);
            result[i] = (Observation) resource;
        }
        
        return result;
    }

    private void assertSearchResults(WebTarget target, String uniqueFamily, int expectedResults) {
        Response response = target.path("Patient").queryParam("family", uniqueFamily).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(expectedResults, bundle.getEntry().size());
    }

    private void assertHistoryResults(WebTarget target, String url, int expectedResults) {
        Response response = target.path(url).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertEquals(expectedResults, bundle.getEntry().size());
    }

    private String setUniqueFamilyName(Patient patient) {
        String uniqueName = UUID.randomUUID().toString();
        List<com.ibm.watsonhealth.fhir.model.String> familyList = patient.getName().get(0).getFamily();
        familyList.clear();
        familyList.add(getObjectFactory().createString().withValue(uniqueName));
        return uniqueName;
    }

    private void assertGoodGetResponse(BundleEntry entry, int expectedStatusCode) throws Exception {
        assertNotNull(entry);
        BundleResponse response = entry.getResponse();
        assertNotNull(response);

        assertNotNull(response.getStatus());
        assertEquals(Integer.toString(expectedStatusCode), response.getStatus().getValue());

        ResourceContainer rc = entry.getResource();
        assertNotNull(rc);
        Resource resource = FHIRUtil.getResourceContainerResource(rc);
        assertNotNull(resource);
    }

    private void assertGoodPostPutResponse(BundleEntry entry, int expectedStatusCode) throws Exception {
        assertGoodGetResponse(entry, expectedStatusCode);
        BundleResponse response = entry.getResponse();

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocation().getValue());

        assertNotNull(response.getEtag());
        assertNotNull(response.getEtag().getValue());

        assertNotNull(response.getLastModified());
        assertNotNull(response.getLastModified().getValue());
    }

    private void assertBadResponse(BundleEntry entry, int expectedStatusCode, String expectedMsg) {
        assertNotNull(entry);
        BundleResponse response = entry.getResponse();
        assertNotNull(response);
        assertNotNull(response.getStatus());
        assertEquals(Integer.toString(expectedStatusCode), response.getStatus().getValue());
        ResourceContainer rc = entry.getResource();
        assertNotNull(rc);
        OperationOutcome oo = rc.getOperationOutcome();
        assertNotNull(oo);
        assertNotNull(oo.getIssue());
        assertTrue(oo.getIssue().size() > 0);
        if (expectedMsg != null) {
            String msg = oo.getIssue().get(0).getDiagnostics().getValue();
            assertNotNull(msg);
            assertTrue("'" + msg + "' doesn't contain '" + expectedMsg + "'", msg.contains(expectedMsg));
        }
    }

    private boolean assertOptionalBadResponse(BundleEntry entry, int expectedStatusCode, String expectedMsg) {
        assertNotNull(entry);
        BundleResponse response = entry.getResponse();
        assertNotNull(response);

        if (response.getStatus() != null && response.getStatus().getExtension().isEmpty()) {
            assertEquals(Integer.toString(expectedStatusCode), response.getStatus().getValue());
            ResourceContainer rc = entry.getResource();
            assertNotNull(rc);
            OperationOutcome oo = rc.getOperationOutcome();
            assertNotNull(oo);
            assertNotNull(oo.getIssue());
            assertTrue(oo.getIssue().size() > 0);
            if (expectedMsg != null) {
                String msg = oo.getIssue().get(0).getDiagnostics().getValue();
                assertNotNull(msg);
                assertTrue("'" + msg + "' doesn't contain '" + expectedMsg + "'", msg.contains(expectedMsg));
            }
        }
        
        return response.getStatus() != null;
    }

    private void printBundle(String method, String bundleType, Bundle bundle) throws JAXBException {
        if (debug) {
            System.out.println(method + " " + bundleType + " bundle contents:\n" + writeResource(bundle, Format.JSON, prettyPrint));
        }
    }

    private void assertResponseBundle(Bundle bundle, BundleTypeList expectedType, int expectedEntryCount) {
        assertNotNull(bundle);
        assertNotNull(bundle.getType());
        assertNotNull(bundle.getType().getValue());
        assertEquals(expectedType, bundle.getType().getValue());
        if (expectedEntryCount > 0) {
            assertNotNull(bundle.getEntry());
            assertEquals(expectedEntryCount, bundle.getEntry().size());
        }
    }

    private BundleEntry addRequestEntryToBundle(Bundle bundle) {
        BundleRequest request = getObjectFactory().createBundleRequest();
        BundleEntry requestEntry = getObjectFactory().createBundleEntry().withRequest(request);
        bundle.getEntry().add(requestEntry);
        return requestEntry;
    }
    private BundleEntry addRequestToBundle(Bundle bundle, HTTPVerbList method, String url, String ifMatch, Resource resource) throws Exception {
        return addRequestToBundle(bundle, method, url, ifMatch, resource, null, null);
    }

    private BundleEntry addRequestToBundle(Bundle bundle, HTTPVerbList method, String url, String ifMatch, Resource resource, List<Extension> requestExtensions) throws Exception {
        BundleEntry entry = addRequestEntryToBundle(bundle);
        BundleRequest request = entry.getRequest();
        if (method != null) {
            request.setMethod(getObjectFactory().createHTTPVerb().withValue(method));
        }
        if (url != null) {
            request.setUrl(getObjectFactory().createUri().withValue(url));
        }
        if (ifMatch != null) {
            request.setIfMatch(getObjectFactory().createString().withValue(ifMatch));
        }
        if (resource != null) {
            ResourceContainer container = getObjectFactory().createResourceContainer();
            FHIRUtil.setResourceContainerResource(container, resource);
            entry.setResource(container);
        }
        if (requestExtensions != null) {
            request.withExtension(requestExtensions);
        }

        return entry;
    }

    private BundleEntry addRequestToBundle(Bundle bundle, HTTPVerbList method, String url, String ifMatch, Resource resource, String fullUrl) throws Exception {
        return addRequestToBundle(bundle, method, url, ifMatch, resource, fullUrl, null);
    }

    private BundleEntry addRequestToBundle(Bundle bundle, HTTPVerbList method, String url, String ifMatch, Resource resource, String fullUrl,
        List<Extension> requestExtensions) throws Exception {
        BundleEntry entry = addRequestToBundle(bundle, method, url, ifMatch, resource, requestExtensions);
        if (entry != null && fullUrl != null) {
            entry.setFullUrl(getObjectFactory().createUri().withValue(fullUrl));
        }

        return entry;
    }

    private Bundle buildBundle(BundleTypeList bundleType) {
        Bundle bundle = getObjectFactory().createBundle().withType(getObjectFactory().createBundleType().withValue(bundleType));
        return bundle;
    }

    @SuppressWarnings("unused")
    private void printOOMessage(OperationOutcome oo) {
        System.out.println("Message: " + oo.getIssue().get(0).getDiagnostics().getValue());
    }
}
