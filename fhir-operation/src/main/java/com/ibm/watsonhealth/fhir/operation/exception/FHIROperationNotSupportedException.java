/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.exception;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;

public class FHIROperationNotSupportedException extends FHIROperationException {
    private static final long serialVersionUID = 1L;

    public FHIROperationNotSupportedException(String message) {
        super(message);
    }

    public FHIROperationNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
    
    @Override
    public FHIROperationNotSupportedException withIssue(OperationOutcomeIssue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIROperationNotSupportedException withIssue(Collection<OperationOutcomeIssue> issues) {
        super.withIssue(issues);
        return this;
    }
}
