/*
 * (C) Copyright IBM Corp. 2022
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
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;

/**
 * Call the $bench custom operation
 */
public class CallBench {
    private static final Logger logger = Logger.getLogger(CallBench.class.getName());

    // The value of the threads parameter to the $bench operation
    private final int threads;
    
    // The value of the size parameter to the $bench operation
    private final int size;

    /**
     * Public constructor
     * @param threads
     * @param size
     */
    public CallBench(int threads, int size) {
        this.threads = threads;
        this.size = size;
    }

    /**
     * Call the $bench custom operation with the threads and size
     * as parameters
     * @param client
     * @return
     */
    public void run(FHIRBucketClient client) {
        Parameters.Builder builder = Parameters.builder();
        builder.parameter(Parameter.builder().name("threads").value(this.threads).build());
        builder.parameter(Parameter.builder().name("size").value(this.size).build());
        Parameters request = builder.build();
        String body = FHIRBucketClientUtil.resourceToString(request);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Request [" + client.getBaseUrl() + "] " + body);
        }
        FhirServerResponse response = client.post("$bench", body);
        
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            Resource result = response.getResource();
            if (result != null) {
                // Log out the response if we want to
                if (logger.isLoggable(Level.FINE)) {
                    String resultString = FHIRBucketClientUtil.resourceToString(result);
                    logger.fine("Result resource: " + resultString);
                }
            } else {
                throw new IllegalStateException("No resource returned with 200 OK response");
            }
        } else {
            logger.warning("Request [" + client.getBaseUrl() + "] " + body);
            logger.warning("FHIR server response: " + response.getStatusCode() + " " + response.getStatusMessage() + ": " + response.getOperationalOutcomeText());
            throw new RuntimeException("Request failed: " + response.getStatusCode() + " " + response.getStatusMessage());
        }
    }
}