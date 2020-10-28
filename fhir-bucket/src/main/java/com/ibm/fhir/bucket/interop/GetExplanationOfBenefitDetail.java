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
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;

/**
 * Use the response bundle from the GetPatientBundle request and use it to retrieve
 * other resources referenced by the ExplanationOfBenefit entries
 */
public class GetExplanationOfBenefitDetail {
    private static final Logger logger = Logger.getLogger(GetPatientBundle.class.getName());
    
    // The logical id of the Patient for which we are retrieving information
    private final Bundle inputBundle;
        
    public GetExplanationOfBenefitDetail(Bundle inputBundle) {
        this.inputBundle = inputBundle;
    }

    /**
     * Get the bundle containing the Patient resource plus a list of ExplanationOfBenefit
     * resources associated with the patient
     * @param client
     * @return
     */
    public Bundle run(FHIRBucketClient client) {
        Bundle.Builder bundleBuilder = Bundle.builder();
        
        // Build a bundle with gets for each ExplanationOfBenefit we can find in the
        // inputBundle
        for (Entry entry: inputBundle.getEntry()) {
            Resource r = entry.getResource();
            if (r.is(Bundle.class)) {
                // this should be the result of the ExplanationOfBenefit search, so iterate
                // over each entry. TODO should visit instead
                Bundle eobs = r.as(Bundle.class);
                for (Entry eobEntry: eobs.getEntry()) {
                    Resource eobr = eobEntry.getResource();
                    if (eobr.is(ExplanationOfBenefit.class)) {
                        ExplanationOfBenefit eob = eobr.as(ExplanationOfBenefit.class);
                        addGetsForEntry(bundleBuilder, eob);
                    }
                }
            }
        }
        
        bundleBuilder.type(BundleType.TRANSACTION);
        
        // TODO refactor this repeated code
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
    
    protected void addGetsForEntry(Bundle.Builder bundleBuilder, ExplanationOfBenefit eob) {

        // Get the Claim for this ExplanationOfBenefit
        if (eob.getClaim() != null && eob.getClaim().getReference() != null) {
            Request.Builder requestBuilder = Request.builder();
            requestBuilder.method(HTTPVerb.GET);
            requestBuilder.url(Url.of(eob.getClaim().getReference().getValue()));
            
            Bundle.Entry.Builder entryBuilder = Bundle.Entry.builder();
            entryBuilder.request(requestBuilder.build());
            bundleBuilder.entry(entryBuilder.build());
        }

        // Get the Provider reference
        if (eob.getProvider() != null && eob.getProvider().getReference() != null) {
            Request.Builder requestBuilder = Request.builder();
            requestBuilder.method(HTTPVerb.GET);
            requestBuilder.url(Url.of(eob.getProvider().getReference().getValue()));
            
            Bundle.Entry.Builder entryBuilder = Bundle.Entry.builder();
            entryBuilder.request(requestBuilder.build());
            bundleBuilder.entry(entryBuilder.build());
        }
    }
}