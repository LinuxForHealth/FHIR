/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.exception;

import java.util.Collection;

import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This exception class represents failures encountered while attempting to access (read, write) data in the FHIR DB. 
 * @author markd
 *
 */
public class FHIRPersistenceDataAccessException extends FHIRPersistenceException {

	private static final long serialVersionUID = -8350452448890342596L;

	public FHIRPersistenceDataAccessException(String message) {
		super(message);
	}

	public FHIRPersistenceDataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
    public FHIRPersistenceDataAccessException withIssue(OperationOutcomeIssue... issues) {
        super.withIssue(issues);
        return this;
    }
	
	@Override
	public FHIRPersistenceDataAccessException withIssue(Collection<OperationOutcomeIssue> issues) {
	    super.withIssue(issues);
	    return this;
	}
	
}
