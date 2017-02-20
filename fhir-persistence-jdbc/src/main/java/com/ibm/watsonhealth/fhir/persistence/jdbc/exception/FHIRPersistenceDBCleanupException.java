/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.exception;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This exception class represents failures encountered while attempting to close/cleanup JDBC resources.
 * @author markd
 *
 */
public class FHIRPersistenceDBCleanupException extends FHIRPersistenceException {

	private static final long serialVersionUID = -8350452448890342596L;

	
	public FHIRPersistenceDBCleanupException() {
		super();
	}

	public FHIRPersistenceDBCleanupException(String message) {
		super(message);
		
	}

	public FHIRPersistenceDBCleanupException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public FHIRPersistenceDBCleanupException(String message, Status httpStatus, Throwable cause) {
		super(message, httpStatus, cause);
	}

	public FHIRPersistenceDBCleanupException(String message, Status httpStatus) {
		super(message, httpStatus);
		
	}

	public FHIRPersistenceDBCleanupException(Throwable cause) {
		super(cause);
		
	}

}
