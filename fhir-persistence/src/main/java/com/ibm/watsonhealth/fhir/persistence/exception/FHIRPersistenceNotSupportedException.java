/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;

/**
 * Thrown for methods or features not yet fully implemented.
 * @author markd
 *
 */
public class FHIRPersistenceNotSupportedException extends FHIRPersistenceException {

	private static final long serialVersionUID = 1L;

	public FHIRPersistenceNotSupportedException(String message) {
		super(message);
	}

	public FHIRPersistenceNotSupportedException(String message, Throwable cause) {
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
