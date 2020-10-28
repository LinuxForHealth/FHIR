/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.interop;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.model.resource.Bundle;

/**
 * A workload simulating interop client access. Fetch a Patient and
 * related ExplanationOfBenefit resources, then use these to 
 * drive a second request fetching the Claim and Provider resources
 * referenced by each of the retrieved ExplanationOfBenefit resources.
 */
public class InteropScenario implements IPatientScenario {
    private static final Logger logger = Logger.getLogger(InteropScenario.class.getName());

    // The REST client used to make FHIR server calls. Thread-safe
    private final FHIRBucketClient client;
    
    /**
     * Public constructor
     * @param client
     */
    public InteropScenario(FHIRBucketClient client) {
        this.client = client;
    }

    @Override
    public void process(String patientId, AtomicInteger fhirRequest, AtomicLong fhirRequestTime, AtomicInteger resourceCount) {
        
        long start = System.nanoTime();
        GetPatientBundle r1 = new GetPatientBundle(patientId);
        Bundle r1Bundle = r1.run(client);
        long end1 = System.nanoTime();
        fhirRequestTime.addAndGet(end1 - start);
        fhirRequest.addAndGet(1);
        
        long start2 = end1;
        int detailResourceCount = processResult(r1Bundle);
        long end2 = System.nanoTime();
        fhirRequestTime.addAndGet(end2 - start2);
        fhirRequest.addAndGet(1);
        
        // How many resources did we get in how long?
        int totalResourceCount = r1Bundle.getEntry().size() + detailResourceCount;
        
        if (logger.isLoggable(Level.FINE)) {
            double elapsed = (end2 - start) / 1e9;        
            logger.fine(String.format("Processed patient: resource count=%d [took %5.3f s]", totalResourceCount, elapsed));
        }
        
        resourceCount.addAndGet(totalResourceCount);
    }

    /**
     * Process the result bundle we retrieved. Create a new bundle to fetch the details
     * from each of the bundles.
     * @param bundle
     */
    protected int processResult(Bundle bundle) {
        
        GetExplanationOfBenefitDetail details = new GetExplanationOfBenefitDetail(bundle);
        Bundle detailResponse = details.run(client);
        
        return detailResponse.getEntry().size();
    }
}
