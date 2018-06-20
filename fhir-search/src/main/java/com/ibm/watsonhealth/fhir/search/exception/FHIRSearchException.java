/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.exception;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;

public class FHIRSearchException extends FHIROperationException {
    private static final long serialVersionUID = 1L;
    
    public FHIRSearchException(String message) {
        super(message);
    }
    
    public FHIRSearchException(String message, Throwable cause) {
        super(message, cause);
    }
    
    @Override
    public FHIRSearchException withIssue(OperationOutcomeIssue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRSearchException withIssue(Collection<OperationOutcomeIssue> issues) {
        super.withIssue(issues);
        return this;
    }
}
