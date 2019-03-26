/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;

/**
 * Thrown when a failure is found processing search parameters.
 * @author markd
 *
 */
public class FHIRPersistenceProcessorException extends FHIRPersistenceException {

    private static final long serialVersionUID = 1L;

    public FHIRPersistenceProcessorException(String message) {
        super(message);
    }

    public FHIRPersistenceProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceException withIssue(OperationOutcomeIssue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceException withIssue(Collection<OperationOutcomeIssue> issues) {
        super.withIssue(issues);
        return this;
    }
}
