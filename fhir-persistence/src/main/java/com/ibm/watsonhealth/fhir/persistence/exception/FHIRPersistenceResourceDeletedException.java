/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.exception;

public class FHIRPersistenceResourceDeletedException extends FHIRPersistenceException {
	private static final long serialVersionUID = 1L;
	
	public FHIRPersistenceResourceDeletedException() {
	    super();
	}
	public FHIRPersistenceResourceDeletedException(String message) {
		super(message);
	}
	
	public FHIRPersistenceResourceDeletedException(String message, Throwable cause) {
	    super(message, cause);
	}
    
    public FHIRPersistenceResourceDeletedException(Throwable cause) {
        super(cause);
    }
}
