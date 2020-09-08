/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.interop;

import java.util.logging.Logger;

import com.ibm.fhir.bucket.client.FhirClient;
import com.ibm.fhir.model.resource.Bundle;

/**
 *
 */
public class CmsPayerScenario implements IPatientScenario {
    private static final Logger logger = Logger.getLogger(CmsPayerScenario.class.getName());

    // The REST client used to make FHIR server calls. Thread-safe
    private final FhirClient client;
    
    /**
     * Public constructor
     * @param client
     */
    public CmsPayerScenario(FhirClient client) {
        this.client = client;
    }

    @Override
    public void process(String patientId) {
        
        GetPatientBundle r1 = new GetPatientBundle(patientId);
        Bundle r1Bundle = r1.run(client);
        
        logger.info("Result count: " + r1Bundle.getEntry().size());
        processResult(r1Bundle);
    }

    /**
     * Process the result bundle we retrieved. Create a new bundle to fetch the details
     * from each of the bundles.
     * @param bundle
     */
    protected void processResult(Bundle bundle) {
        
        GetExplanationOfBenefitDetail details = new GetExplanationOfBenefitDetail(bundle);
        Bundle detailResponse = details.run(client);
        logger.info("Details response count: " + detailResponse.getEntry().size());
    }
}
