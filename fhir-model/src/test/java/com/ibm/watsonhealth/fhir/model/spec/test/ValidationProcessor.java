/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.spec.test;

import java.util.List;
import java.util.stream.Collectors;

import com.ibm.watsonhealth.fhir.model.resource.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.validation.FHIRValidator;

/**
 * Strategy to process resources using the {@link FHIRValidator}
 * @author rarnold
 *
 */
public class ValidationProcessor implements IExampleProcessor {

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.model.spec.test.IExampleProcessor#process(java.lang.String, com.ibm.watsonhealth.fhir.model.resource.Resource)
     */
    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        List<OperationOutcome.Issue> issues = FHIRValidator.validator(resource).validate();
        if (!issues.isEmpty()) {
            String info = issues.stream().map(issue -> issue.toString()).collect(Collectors.joining(","));
            
            // Only errors or worse should result in a failure.
            boolean includesFailure = false;
            for (OperationOutcome.Issue issue: issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    includesFailure = true;
                }
            }

            if (includesFailure) {
                // we want the test to fail
                throw new Exception("Input resource failed validation: " + info);
            }
            else {
                System.out.println("Validation issues [INFO]: " + info);
            }
        }
    }

}
