/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.test;

import com.ibm.watsonhealth.fhir.model.type.ContactPoint;
import static com.ibm.watsonhealth.fhir.client.FHIRRequestHeader.header;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static com.ibm.watsonhealth.fhir.model.type.String.string;

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
import com.ibm.watsonhealth.fhir.client.FHIRRequestHeader;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.client.test.FHIRClientTestBase;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.Bundle.Entry;
import com.ibm.watsonhealth.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.watsonhealth.fhir.model.type.BundleType;
import com.ibm.watsonhealth.fhir.model.resource.CapabilityStatement;
import com.ibm.watsonhealth.fhir.model.type.ContactPointSystem;
import com.ibm.watsonhealth.fhir.model.type.ContactPointUse;
import com.ibm.watsonhealth.fhir.model.type.HTTPVerb;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
//import com.ibm.watsonhealth.fhir.model.resource.Organization.Contact;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Patient.Contact;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.core.MediaType;

/**
 * Basic tests related to the FHIR Client API.
 */
public class FHIRClientTest extends FHIRClientTestBase {
    private static final String MIMETYPE_JSON = MediaType.APPLICATION_JSON_FHIR;
    private static final String MIMETYPE_XML = MediaType.APPLICATION_XML_FHIR;
    
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

        CapabilityStatement conf = response.readEntity(CapabilityStatement.class);
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

        CapabilityStatement conf = response.getResource(CapabilityStatement.class);
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
        patient = patient.toBuilder().contact(Contact.builder()
                .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE)
                        .use(ContactPointUse.MOBILE)
                        .value(string("800-328-7448")).build()).build()).build();      
               
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
        patient = patient.toBuilder().contact(Contact.builder()
                .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE)
                        .use(ContactPointUse.WORK)
                        .value(string("408-400-7448")).build()).build()).build();     
        
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
        String since = updatedPatient.getMeta().getLastUpdated().getValue().toString();
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
        FHIRParameters parameters = new FHIRParameters()
                .searchParam("birthdate", ValuePrefix.LE, "1950-01-01")
                .searchParam("_id", updatedPatient.getId().getValue());
                
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
        FHIRParameters parameters = new FHIRParameters()
                .searchParam("birthdate", ValuePrefix.GE, "1970-01-01")
                .searchParam("_id", updatedPatient.getId().getValue());
                
        FHIRResponse response = client.search("Patient", parameters);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertNotNull(bundle.getEntry());
        assertTrue(bundle.getEntry().size() == 1);
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
    public void testSearchPatientWithParamsBadRequest_preferLenient() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("not-an-attribute", "X");

        FHIRRequestHeader preferHeader = new FHIRRequestHeader("Prefer", "handling=lenient");
        FHIRResponse response = client.search("Patient", parameters, preferHeader);
        assertNotNull(response);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);
        assertTrue(responseBundle.getEntry().size() > 0);
        assertNotNull(responseBundle.getEntry().get(0).getResource());
    }
    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testSearchPatientWithParamsBadRequest_preferStrict() throws Exception {
        FHIRParameters parameters = new FHIRParameters().searchParam("not-an-attribute", "X");

        FHIRRequestHeader preferHeader = new FHIRRequestHeader("Prefer", "handling=strict");
        FHIRResponse response = client.search("Patient", parameters, preferHeader);
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
        Bundle requestBundle = Bundle.builder(BundleType.BATCH).build();
        
        // read
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient/" + createdPatient.getId().getValue(), null);
        // vread
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history/" + updatedPatient.getMeta().getVersionId().getValue(), null);
        // history
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history", null);
        // search
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient?family=Doe&_count=3", null);
        
        FHIRResponse response = client.batch(requestBundle);
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);
        assertNotNull(responseBundle.getEntry());
        assertEquals(4, responseBundle.getEntry().size());
        assertEquals(BundleType.BATCH_RESPONSE.getValue(), responseBundle.getType().getValue());
    }


    @Test(dependsOnMethods = { "testUpdatePatient" })
    public void testTransaction() throws Exception {
        Bundle requestBundle = Bundle.builder(BundleType.BATCH).build();
        // read
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient/" + createdPatient.getId().getValue(), null);
        // vread
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history/" + updatedPatient.getMeta().getVersionId().getValue(), null);
        // history
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient/" + updatedPatient.getId().getValue() + "/_history", null);
        // search
        addRequestToBundle(requestBundle, HTTPVerb.GET, "Patient?family=Doe&_count=3", null);
        
        FHIRResponse response = client.transaction(requestBundle);
        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        
        Bundle responseBundle = response.getResource(Bundle.class);
        assertNotNull(responseBundle);
        assertNotNull(responseBundle.getEntry());
        assertEquals(4, responseBundle.getEntry().size());
        assertEquals(BundleType.TRANSACTION_RESPONSE.getValue(), responseBundle.getType().getValue());
    }

    private Bundle addRequestToBundle(Bundle bundle, HTTPVerb method, String url, Resource resource) throws Exception {
          
        Bundle.Entry.Request request = Bundle.Entry.Request.builder(method, Uri.of(url)).build();   
        Bundle.Entry.Builder entryBuilder = Bundle.Entry.builder().request(request);
             
        if (resource != null) {
            entryBuilder.resource(resource);
        }

        return bundle.toBuilder().entry(entryBuilder.build()).build();
    }

}
