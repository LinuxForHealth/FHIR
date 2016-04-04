/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

public class FHIRPersistenceResourceNotFoundException extends FHIRPersistenceException {
	private static final long serialVersionUID = 1L;
	
	public FHIRPersistenceResourceNotFoundException() {
	    super();
	}
	public FHIRPersistenceResourceNotFoundException(String message) {
		super(message);
	}
	
	public FHIRPersistenceResourceNotFoundException(String message, Throwable cause) {
	    super(message, cause);
	}
    
    public FHIRPersistenceResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
