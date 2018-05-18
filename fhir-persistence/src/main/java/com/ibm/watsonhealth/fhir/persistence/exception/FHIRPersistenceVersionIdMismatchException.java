/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This exception class represents an occurrence of a mismatch between the version id in the resource json vs.
 * the next expected version. This can happen due to a race condition when a particular resource is being updated concurrently
 * on different threads.
 * @author markd
 *
 */
public class FHIRPersistenceVersionIdMismatchException extends FHIRPersistenceException {

	private static final long serialVersionUID = -8350452448890342596L;
	private static final Status DEFAULT_HTTP_STATUS = Status.CONFLICT;

	
	public FHIRPersistenceVersionIdMismatchException() {
		super();
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

	public FHIRPersistenceVersionIdMismatchException(String message) {
		super(message);
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

	public FHIRPersistenceVersionIdMismatchException(String message, Throwable cause) {
		super(message, cause);
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

	public FHIRPersistenceVersionIdMismatchException(String message, Status httpStatus) {
		super(message, httpStatus);
		
	}

	public FHIRPersistenceVersionIdMismatchException(Throwable cause) {
		super(cause);
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

}
