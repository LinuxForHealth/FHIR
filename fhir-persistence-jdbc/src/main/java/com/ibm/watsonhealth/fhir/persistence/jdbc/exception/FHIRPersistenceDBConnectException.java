/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.exception;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This exception class represents failures encountered while attempting to connect to a JDBC database or datasource.
 * @author markd
 *
 */
public class FHIRPersistenceDBConnectException extends FHIRPersistenceException {

	private static final long serialVersionUID = -8350452448890342596L;
	private static final Status DEFAULT_HTTP_STATUS = Status.INTERNAL_SERVER_ERROR;

	
	public FHIRPersistenceDBConnectException() {
		super();
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

	public FHIRPersistenceDBConnectException(String message) {
		super(message);
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

	public FHIRPersistenceDBConnectException(String message, Throwable cause) {
		super(message, cause);
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

	public FHIRPersistenceDBConnectException(String message, Status httpStatus, Throwable cause) {
		super(message, httpStatus, cause);
	}

	public FHIRPersistenceDBConnectException(String message, Status httpStatus) {
		super(message, httpStatus);
	}

	public FHIRPersistenceDBConnectException(Throwable cause) {
		super(cause);
		this.setHttpStatus(DEFAULT_HTTP_STATUS);
	}

}
