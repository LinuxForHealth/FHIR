/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.exception.FHIRException;
import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.resource.Bundle;
import com.ibm.watsonhealth.fhir.model.resource.Bundle.Entry;
import com.ibm.watsonhealth.fhir.model.resource.MedicationAdministration;
import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Practitioner;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.BundleType;
import com.ibm.watsonhealth.fhir.model.type.HTTPVerb;
import com.ibm.watsonhealth.fhir.model.type.Uri;

/**
 * This class provides a simple test involving a transaction bundle containing 3
 * POST requests of of different resource types. It is used to perform a system
 * test of the XAResource recovery function. However, this test can also run
 * fine within our server integration test suite.
 */
public class SimpleRouterTest extends FHIRServerTestBase {
    // Set this to true to have the request and response bundles displayed on the
    // console.
    private boolean debug = false;

    private Boolean updateCreateEnabled = null;
    private Boolean transactionSupported = null;
    private Boolean compartmentSearchSupported = null;
    private Boolean deleteSupported = null;

    /**
     * Retrieve the server's conformance statement to determine the status of
     * certain runtime options.
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

    @Test(groups = { "transaction" })
    public void testTransactionCreates() throws Exception {
        String method = "testTransactionCreates";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        MedicationAdministration medAdmin = readResource(MedicationAdministration.class,
                "MedicationAdministration.json");

        Bundle bundle = buildBundle(BundleType.TRANSACTION);
        bundle = addRequestToBundle(bundle, HTTPVerb.POST, "Patient", null, patient);
        bundle = addRequestToBundle(bundle, HTTPVerb.POST, "Practitioner", null, practitioner);
        bundle = addRequestToBundle(bundle, HTTPVerb.POST, "MedicationAdministration", null, medAdmin);

        printBundle(method, "request", bundle);

        FHIRResponse response = getFHIRClient().transaction(bundle);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleType.TRANSACTION_RESPONSE, 3);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(2), Status.CREATED.getStatusCode());
    }

    private void assertGoodGetResponse(Bundle.Entry entry, int expectedStatusCode) throws Exception {
        assertNotNull(entry);
        Entry.Response response = entry.getResponse();
        assertNotNull(response);

        assertNotNull(response.getStatus());
        assertEquals(Integer.toString(expectedStatusCode), response.getStatus().getValue());

        Resource rc = entry.getResource();
        assertNotNull(rc);
    }

    private void assertGoodPostPutResponse(Bundle.Entry entry, int expectedStatusCode) throws Exception {
        assertGoodGetResponse(entry, expectedStatusCode);
        Entry.Response response = entry.getResponse();

        assertNotNull(response.getLocation());
        assertNotNull(response.getLocation().getValue());

        assertNotNull(response.getEtag());
        assertNotNull(response.getEtag().getValue());

        assertNotNull(response.getLastModified());
        assertNotNull(response.getLastModified().getValue());
    }

    private void printBundle(String method, String bundleType, Bundle bundle) throws JAXBException, FHIRException {
        if (debug) {
            System.out.println(method + " " + bundleType + " bundle contents:\n" + writeResource(bundle, Format.JSON));
        }
    }

    private void assertResponseBundle(Bundle bundle, BundleType expectedType, int expectedEntryCount) {
        assertNotNull(bundle);
        assertNotNull(bundle.getType());
        assertNotNull(bundle.getType().getValue());
        assertEquals(expectedType, bundle.getType().getValue());
        if (expectedEntryCount > 0) {
            assertNotNull(bundle.getEntry());
            assertEquals(expectedEntryCount, bundle.getEntry().size());
        }
    }

    private Bundle addRequestToBundle(Bundle bundle, HTTPVerb method, String url, String ifMatch, Resource resource)
            throws Exception {
        Bundle.Entry.Builder entryBuilder = Entry.builder();
        Entry.Request.Builder requestBuilder = Entry.Request.builder(method, Uri.of(url));

        if (ifMatch != null) {
            requestBuilder.ifMatch(string(ifMatch));
        }
        if (resource != null) {
            entryBuilder.resource(resource);
        }

        bundle = bundle.toBuilder().entry(entryBuilder.request(requestBuilder.build()).build()).build();

        return bundle;
    }

    private Bundle buildBundle(BundleType bundleType) {
        Bundle bundle = Bundle.builder(bundleType).build();
        return bundle;
    }

    @SuppressWarnings("unused")
    private void printOOMessage(OperationOutcome oo) {
        System.out.println("Message: " + oo.getIssue().get(0).getDiagnostics().getValue());
    }
}
