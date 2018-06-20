/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

public class FHIRPersistenceInterceptorException extends FHIRPersistenceException {
    private static final long serialVersionUID = 1L;

    public FHIRPersistenceInterceptorException(String message) {
        super(message);
    }

    public FHIRPersistenceInterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceInterceptorException withIssue(OperationOutcomeIssue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceInterceptorException withIssue(Collection<OperationOutcomeIssue> issues) {
        super.withIssue(issues);
        return this;
    }
    
}
