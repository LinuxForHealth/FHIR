/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.test;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.contactPoint;
import static com.ibm.watsonhealth.fhir.client.FHIRRequestHeader.header;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Properties;

import javax.json.JsonObject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRClientFactory;
import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRParameters.Modifier;
import com.ibm.watsonhealth.fhir.client.FHIRParameters.ValuePrefix;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.client.test.FHIRClientTestBase;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleRequest;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.Conformance;
import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.HTTPVerbList;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

/**
 * Basic tests related to the FHIR Client API.
 */
public class FHIRClientTest extends FHIRClientTestBase {
    private static final String MIMETYPE_JSON = "application/json+fhir";
    private static final String MIMETYPE_XML = "application/xml+fhir";
    
    private Patient createdPatient = null;
    private Patient updatedPatient = null;
    
    @Test
    public void testFHIRClientCtorProperties1() throws Exception {
        // by default, our testcase creates a FHIRClient instance using test.properties
        // so this test will just verify that the default mimetype is present.
        assertNotNull(client);
        assertEquals(MIMETYPE_JSON, client.getDefaultMimeType());
    }

    @Test
    public void testFHIRClientCtorProperties2() throws Exception {
        // Clone our "testProperties" field.
        Properties props = new Properties();
        props.putAll(testProperties);
        
        // Set the mimetype we want to test with.
        props.setProperty(FHIRClient.PROPNAME_DEFAULT_MIMETYPE, MIMETYPE_XML);
        
        FHIRClient c = FHIRClientFactory.getClient(props);
        assertNotNull(c);
        assertEquals(MIMETYPE_XML, c.getDefaultMimeType());
    }

    @Test
    public void testMetadataWebTarget() throws Exception {

        // Test the 'metadata' API via the low-level WebTarget object.
        String accessToken = client.getOAuth2AccessToken();
        if(accessToken == null) {
            System.out.println("Not using OAuth 2.0 Authorization");
        } else {
            System.out.println("Using OAuth 2.0 Authorization with Bearer token: " + accessToken);
        }
        WebTarget fhirEndpoint = client.getWebTarget();
        assertNotNull(fhirEndpoint);

        Response response = fhirEndpoint.path("metadata").request().get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Conformance conf = response.readEntity(Conformance.class);
        assertNotNull(conf);
        assertNotNull(conf.getFormat());
        assertEquals(4, conf.getFormat().size());
        assertNotNull(conf.getVersion());
        assertNotNull(conf.getName());
    }

    @Test
    public void testMetadata() throws Exception {

        // Test the 'metadata' operation via the high-level 'metadata' method.
        FHIRResponse response = client.metadata();
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        assertEquals(200, response.getStatus());

        Conformance conf = response.getResource(Conformance.class);
        assertNotNull(conf);
        assertNotNull(conf.getFormat());
        assertEquals(4, conf.getFormat().size());
        assertNotNull(conf.getVersion());
        assertNotNull(conf.getName());
    }

    @Test
    public void testCreatePatient() throws Exception {
        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        assertNotNull(patient);

        // Create the patient and then validate the response.
        FHIRResponse response = client.create(patient);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocationURI());
        assertNotNull(response.getETag());
        assertNotNull(response.getLastModified());

        // Next, cache the newly-created patient.
        String[] locationTokens = response.parseLocation(response.getLocation());
        response = client.read(locationTokens[0], locationTokens[1]);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        createdPatient = response.getResource(Patient.class);
        assertNotNull(createdPatient);
    }

    @Test
    public void testCreatePatientJsonObject() throws Exception {
        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        assertNotNull(patient);
        JsonObject jsonObj = FHIRUtil.toJsonObject(patient);

        FHIRResponse response = client.create(jsonObj);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.CREATED.getStatusCode());

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocationURI());
        assertNotNull(response.getETag());
        assertNotNull(response.getLastModified());
    }

    @Test(dependsOnMethods = {"testCreatePatient"})
    public void testUpdatePatient() throws Exception {
        assertNotNull(createdPatient);
        
        // Read the patient, then modify it.
        FHIRResponse response = client.read("Patient", createdPatient.getId().getValue());
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Patient patient = response.getResource(Patient.class);
        assertNotNull(patient);

        // Next, add an additional contact phone number
        patient = patient.withTelecom(
            contactPoint(ContactPointSystemList.PHONE, "800-328-7448", ContactPointUseList.MOBILE));
        
        String ifMatchValue = "W/\"" + patient.getMeta().getVersionId().getValue() + "\"";

        // Update the patient and then validate the response.
        response = client.update(patient, header("If-Match", ifMatchValue));
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocationURI());
        assertNotNull(response.getETag());
        assertNotNull(response.getLastModified());

        // Next, cache the newly-updated patient.
        String[] locationTokens = response.parseLocation(response.getLocation());
        response = client.read(locationTokens[0], locationTokens[1]);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        updatedPatient = response.getResource(Patient.class);
        assertNotNull(updatedPatient);
    }
    
    @Test(dependsOnMethods = {"testCreatePatient"})
    public void testUpdatePatientJsonObject() throws Exception {
        assertNotNull(createdPatient);
        
        // Read the patient, then modify it.
        FHIRResponse response = client.read("Patient", createdPatient.getId().getValue());
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Patient patient = response.getResource(Patient.class);
        assertNotNull(patient);

        // Next, add an additional contact phone number
        patient = patient.withTelecom(
            contactPoint(ContactPointSystemList.PHONE, "408-400-7448", ContactPointUseList.WORK));
        
        JsonObject jsonObj = FHIRUtil.toJsonObject(patient);

        // Update the patient and then validate the response.
        response = client.update(jsonObj);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocationURI());
        assertNotNull(response.getETag());
        assertNotNull(response.getLastModified());

        // Next, cache the newly-updated patient.
        String[] locationTokens = response.parseLocation(response.getLocation());
        response = client.read(locationTokens[0], locationTokens[1]);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        updatedPatient = response.getResource(Patient.class);
        assertNotNull(updatedPatient);
    }

    @Test(dependsOnMethods = { "testCreatePatient" })
    public void testReadPatient() throws Exception {
        assertNotNull(createdPatient);
        FHIRResponse response = client.read("Patient", createdPatient.getId().getValue());
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        assertNotNull(response.getETag());
        assertNotNull(response.getLastModified());
    }

    @Test(dependsOnMethods = { "testCreatePatient" })
    public void testReadPatientNotFound() throws Exception {
        assertNotNull(createdPatient);
        FHIRResponse response = client.read("Patient", "INVALID_ID");
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.NOT_FOUND.getStatusCode());
        
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertNotNull(oo);
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testVReadPatient() throws Exception {
        assertNotNull(updatedPatient);
        FHIRResponse response = client.vread("Patient", updatedPatient.getId().getValue(), updatedPatient.getMeta().getVersionId().getValue());
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        assertNotNull(response.getETag());
        assertNotNull(response.getLastModified());
    }

    @Test(dependsOnMethods = { "testUpdatePatient", "testUpdatePatientJsonObject" })
    public void testHistoryPatientNoParams() throws Exception {
        FHIRResponse response = client.history("Patient", updatedPatient.getId().getValue(), null);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertEquals(3, bundle.getEntry().size());
    }

    @Test(dependsOnMethods = { "testHistoryPatientNoParams" })
    public void testHistoryPatientWithCountPage() throws Exception {
        FHIRParameters parameters = new FHIRParameters().count(1).page(2);
        FHIRResponse response = client.history("Patient", updatedPatient.getId().getValue(), parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertEquals(1, bundle.getEntry().size());
    }

    @Test(dependsOnMethods = { "testHistoryPatientNoParams" })
    public void testHistoryPatientWithSince() throws Exception {
        String since = updatedPatient.getMeta().getLastUpdated().getValue().toXMLFormat();
        FHIRParameters parameters = new FHIRParameters().since(since);
        FHIRResponse response = client.history("Patient", updatedPatient.getId().getValue(), parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertEquals(1, bundle.getEntry().size());
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientNoParams() throws Exception {
        FHIRResponse response = client.search("Patient", null);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientNoResults() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("name", "NOT_A_PATIENT");
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertEquals(0, bundle.getEntry().size());
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientWithParams1() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("name", "Doe");
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientWithParams3() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("birthdate", ValuePrefix.LE, "1950-01-01");
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertEquals(0, bundle.getEntry().size());
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientWithParams4() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("birthdate", ValuePrefix.GE, "1970-01-01");
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientWithParams2() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("name", Modifier.CONTAINS, "Doe");
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientWithParamsBadRequest() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("not-an-attribute", "X");
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
        
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertNotNull(oo);
    }

    @Test
    public void testValidatePatient() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        assertNotNull(patient);

        // Create the patient and then validate the response.
        FHIRResponse response = client.validate(patient);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertNotNull(oo);
    }

    @Test
    public void testValidatePatientJsonObject() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        assertNotNull(patient);
        JsonObject jsonObj = FHIRUtil.toJsonObject(patient);

        FHIRResponse response = client.validate(jsonObj);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertNotNull(oo);
    }

    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testBatch() throws Exception {
        Bundle requestBundle = buildBundle(BundleTypeList.BATCH);
        // read
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient/" + createdPatient.getId().getValue(), null);
        // vread
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history/" + updatedPatient.getMeta().getVersionId().getValue(), null);
        // history
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history", null);
        // search
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient?family=Doe&_count=3", null);
        
        FHIRResponse response = client.batch(requestBundle);
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);
        assertNotNull(responseBundle.getEntry());
        assertEquals(4, responseBundle.getEntry().size());
        assertEquals(BundleTypeList.BATCH_RESPONSE, responseBundle.getType().getValue());
    }


    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testTransaction() throws Exception {
        Bundle requestBundle = buildBundle(BundleTypeList.BATCH);
        // read
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient/" + createdPatient.getId().getValue(), null);
        // vread
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history/" + updatedPatient.getMeta().getVersionId().getValue(), null);
        // history
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history", null);
        // search
        addRequestToBundle(requestBundle, HTTPVerbList.GET, "Patient?family=Doe&_count=3", null);
        
        FHIRResponse response = client.transaction(requestBundle);
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);
        assertNotNull(responseBundle.getEntry());
        assertEquals(4, responseBundle.getEntry().size());
        assertEquals(BundleTypeList.TRANSACTION_RESPONSE, responseBundle.getType().getValue());
    }
    private Bundle buildBundle(BundleTypeList bundleType) {
        Bundle bundle = getObjectFactory().createBundle().withType(getObjectFactory().createBundleType().withValue(bundleType));
        return bundle;
    }

    private BundleEntry addRequestToBundle(Bundle bundle, HTTPVerbList method, String url, Resource resource) throws Exception {
        BundleEntry entry = addRequestEntryToBundle(bundle);
        BundleRequest request = entry.getRequest();
        if (method != null) {
            request.setMethod(getObjectFactory().createHTTPVerb().withValue(method));
        }
        if (url != null) {
            request.setUrl(getObjectFactory().createUri().withValue(url));
        }
        if (resource != null) {
            ResourceContainer container = getObjectFactory().createResourceContainer();
            FHIRUtil.setResourceContainerResource(container, resource);
            entry.setResource(container);
        }

        return entry;
    }

    private BundleEntry addRequestEntryToBundle(Bundle bundle) {
        BundleRequest request = getObjectFactory().createBundleRequest();
        BundleEntry requestEntry = getObjectFactory().createBundleEntry().withRequest(request);
        bundle.getEntry().add(requestEntry);
        return requestEntry;
    }
}
