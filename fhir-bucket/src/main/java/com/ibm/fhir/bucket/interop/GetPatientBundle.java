/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.interop;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.bucket.client.FHIRBucketClientUtil;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;

/**
 * Retrieve a bundle containing the Patient and a number of ExplanationOfBenefits
 */
public class GetPatientBundle {
    private static final Logger logger = Logger.getLogger(GetPatientBundle.class.getName());
    
    // The logical id of the Patient for which we are retrieving information
    private final String patientLogicalId;
        
    public GetPatientBundle(String patientLogicalId) {
        this.patientLogicalId = patientLogicalId;
    }

    /**
     * Get the bundle containing the Patient resource plus a list of ExplanationOfBenefit
     * resources associated with the patient
     * @param client
     * @return
     */
    public Bundle run(FHIRBucketClient client) {
        Bundle.Builder bundleBuilder = Bundle.builder();
        
        // Build a bundle with two requests
        // 1. Patient read
        // 2. ExplanationOfBenefit search
        Request.Builder requestBuilder = Request.builder();
        requestBuilder.method(HTTPVerb.GET);
        requestBuilder.url(Url.of("Patient/" + patientLogicalId));
        
        Bundle.Entry.Builder entryBuilder = Bundle.Entry.builder();
        entryBuilder.request(requestBuilder.build());
        bundleBuilder.entry(entryBuilder.build());

        // EOB
        requestBuilder = Request.builder();
        requestBuilder.method(HTTPVerb.GET);
        requestBuilder.url(Url.of("ExplanationOfBenefit?patient=" + patientLogicalId));
        
        entryBuilder = Bundle.Entry.builder();
        entryBuilder.request(requestBuilder.build());
        bundleBuilder.entry(entryBuilder.build());
        
        bundleBuilder.type(BundleType.TRANSACTION);
        
        Bundle request = bundleBuilder.build();
        String body = FHIRBucketClientUtil.resourceToString(request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Request [" + client.getBaseUrl() + "] " + body);
        }
        FhirServerResponse response = client.post("", body);
        
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            Resource result = response.getResource();
            if (result != null) {

                // Log out the response if we want to
                if (logger.isLoggable(Level.FINE)) {
                    String resultString = FHIRBucketClientUtil.resourceToString(result);
                    logger.fine("Result resource: " + resultString);
                }
                
                if (result.is(Bundle.class)) {
                    return result.as(Bundle.class);
                } else {
                    throw new IllegalStateException("FHIR server response was 200 OK but returned " + result.getClass().getSimpleName());
                }
            } else {
                throw new IllegalStateException("No bundle returned with 200 OK response");
            }
        } else {
            logger.warning("Request [" + client.getBaseUrl() + "] " + body);
            logger.warning("FHIR server response: " + response.getStatusCode() + " " + response.getStatusMessage() + ": " + response.getOperationalOutcomeText());
            throw new RuntimeException("Request failed: " + response.getStatusCode() + " " + response.getStatusMessage());
        }
    }
}