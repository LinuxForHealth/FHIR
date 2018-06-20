/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.exception;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;

public class FHIROperationNotFoundException extends FHIROperationException {
    private static final long serialVersionUID = 1L;

    public FHIROperationNotFoundException(String message) {
        super(message);
    }

    public FHIROperationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    @Override
    public FHIROperationNotFoundException withIssue(OperationOutcomeIssue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIROperationNotFoundException withIssue(Collection<OperationOutcomeIssue> issues) {
        super.withIssue(issues);
        return this;
    }
}
