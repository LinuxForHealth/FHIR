/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

/**
 * Thrown for methods or features not yet fully implemented.
 * @author markd
 *
 */
public class FHIRPersistenceNotSupportedException extends FHIRPersistenceException {

	private static final long serialVersionUID = 1L;

	public FHIRPersistenceNotSupportedException() {
		super();
	}

	public FHIRPersistenceNotSupportedException(String message) {
		super(message);
	}

	public FHIRPersistenceNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

	public FHIRPersistenceNotSupportedException(Throwable cause) {
		super(cause);
	}

}
