/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

/**
 * Thrown when a failure is found processing search parameters.
 * @author markd
 *
 */
public class FHIRPersistenceProcessorException extends FHIRPersistenceException {

	private static final long serialVersionUID = 1L;

	public FHIRPersistenceProcessorException() {
		super();
	}

	public FHIRPersistenceProcessorException(String message) {
		super(message);
	}

	public FHIRPersistenceProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public FHIRPersistenceProcessorException(Throwable cause) {
		super(cause);
	}

}
