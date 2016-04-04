/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

public class FHIRPersistenceException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public FHIRPersistenceException() {
	    super();
	}
	public FHIRPersistenceException(String message) {
		super(message);
	}
	
	public FHIRPersistenceException(String message, Throwable cause) {
	    super(message, cause);
	}
    
    public FHIRPersistenceException(Throwable cause) {
        super(cause);
    }
}
