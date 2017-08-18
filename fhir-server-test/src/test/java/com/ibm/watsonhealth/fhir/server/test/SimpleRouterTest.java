/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleRequest;
import com.ibm.watsonhealth.fhir.model.BundleResponse;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.HTTPVerbList;
import com.ibm.watsonhealth.fhir.model.MedicationAdministration;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Practitioner;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

/**
 * This class provides a simple test involving a transaction bundle containing 3 POST requests of
 * of different resource types.   It is used to perform a system test of the XAResource recovery function.
 * However, this test can also run fine within our server integration test suite.
 */
public class SimpleRouterTest extends FHIRServerTestBase {
    // Set this to true to have the request and response bundles displayed on the console.
    private boolean debug = false;

    private Boolean updateCreateEnabled = null;
    private Boolean transactionSupported = null;
    private Boolean compartmentSearchSupported = null;
    private Boolean deleteSupported = null;

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

    @Test(groups = { "transaction" })
    public void testTransactionCreates() throws Exception {
        String method = "testTransactionCreates";
        assertNotNull(transactionSupported);
        if (!transactionSupported.booleanValue()) {
            return;
        }

        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        MedicationAdministration medAdmin = readResource(MedicationAdministration.class, "MedicationAdministration.json");

        Bundle bundle = buildBundle(BundleTypeList.TRANSACTION);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Patient", null, patient);
        addRequestToBundle(bundle, HTTPVerbList.POST, "Practitioner", null, practitioner);
        addRequestToBundle(bundle, HTTPVerbList.POST, "MedicationAdministration", null, medAdmin);

        printBundle(method, "request", bundle);
        
        FHIRResponse response = getFHIRClient().transaction(bundle);

        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.getResource(Bundle.class);
        printBundle(method, "response", responseBundle);
        assertResponseBundle(responseBundle, BundleTypeList.TRANSACTION_RESPONSE, 3);
        assertGoodPostPutResponse(responseBundle.getEntry().get(0), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(1), Status.CREATED.getStatusCode());
        assertGoodPostPutResponse(responseBundle.getEntry().get(2), Status.CREATED.getStatusCode());
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

    private void printBundle(String method, String bundleType, Bundle bundle) throws JAXBException {
        if (debug) {
            System.out.println(method + " " + bundleType + " bundle contents:\n" + writeResource(bundle, Format.JSON));
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
